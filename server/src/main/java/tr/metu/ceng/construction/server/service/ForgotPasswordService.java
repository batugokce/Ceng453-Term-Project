package tr.metu.ceng.construction.server.service;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tr.metu.ceng.construction.server.config.Encryption;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.repository.PlayerRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final JavaMailSender mailSender;
    private final PlayerRepository playerRepository;
    private final Encryption encryption;
    private final int CREDENTIAL_LENGTH = 16;

    public boolean forgotPassword(String emailAddress) {
        Player player = playerRepository.findByEmailAddress(emailAddress);

        if (null == player) {
            return false;
        }

        String resetToken = RandomString.make(CREDENTIAL_LENGTH);
        sendEmail(emailAddress, resetToken, player.getUsername());

        player.setPasswordResetToken(resetToken);
        playerRepository.save(player);
        return true;
    }

    private void sendEmail(String emailAddress, String token, String username) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setText(prepareEmailText(token, username), true);
            mimeMessageHelper.setTo(emailAddress);
            mimeMessageHelper.setSubject("Password Reset Link");

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String prepareEmailText(String token, String username) {
        String baseURL = "localhost:8080/api/v1/resetPassword/generatePassword?token=%s&cancel=%s";
        String resetLink = String.format(baseURL, token, false);
        String cancelLink = String.format(baseURL, token, true);

        return  " <p>" +
                "    <h1>Hello " + username + " !</h1>" +
                " <h3>We have received a password reset request for your account.</h3>" +
                " If you have made this action, you can get your new password using <a href=\"http://" + resetLink + "\">this link</a>. " +
                " Otherwise, please cancel this operation from <a href=\"http://" + cancelLink + "\"> here</a>." +
                " </p>";
    }

    public String generatePassword(String token) {
        Player player = playerRepository.findByPasswordResetToken(token);

        if (null == player) {
            return null;
        }

        String newPassword = RandomString.make(CREDENTIAL_LENGTH);
        player.setPassword(encryption.encoder().encode(newPassword));
        player.setPasswordResetToken(null);
        playerRepository.save(player);

        return newPassword;
    }

    public void cancelResetToken(String token) {
        Player player = playerRepository.findByPasswordResetToken(token);

        if (null != player) {
            player.setPasswordResetToken(null);
            playerRepository.save(player);
        }
    }
}
