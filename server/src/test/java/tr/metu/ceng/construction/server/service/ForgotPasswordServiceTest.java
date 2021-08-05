package tr.metu.ceng.construction.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import tr.metu.ceng.construction.server.config.Encryption;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.repository.PlayerRepository;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForgotPasswordServiceTest {


    @Mock
    private JavaMailSender mailSender;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private Encryption encryption;
    @InjectMocks
    private ForgotPasswordService forgotPasswordService;

    private Player player;
    private String randomPassword = "thisIsRandomPassword";
    private final String emailAddress = "test@test.com";
    private MimeMessage mimeMessage;
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        player = new Player();
        player.setToken("token1");
        player.setUsername("username");
        mimeMessage = Mockito.mock(MimeMessage.class);
        javaMailSender = Mockito.mock(JavaMailSender.class);
    }

    @Test
    void forgotPasswordTestNullPlayer() {
        when(playerRepository.findByEmailAddress(anyString())).thenReturn(null);

        boolean response = forgotPasswordService.forgotPassword(emailAddress);

        assertFalse(response);
    }

    @Test
    void forgotPasswordTestSuccess() {
        when(playerRepository.findByEmailAddress(anyString())).thenReturn(player);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        boolean response = forgotPasswordService.forgotPassword(emailAddress);

        assertTrue(response);
    }

    @Test
    void generatePasswordTestNullPlayer() {
        when(playerRepository.findByPasswordResetToken(anyString())).thenReturn(null);

        String response = forgotPasswordService.generatePassword("token");

        assertNull(response);
    }

    @Test
    void generatePasswordTestSuccess() {
        player.setPasswordResetToken("passwordResetToken");
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        when(encryption.encoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(playerRepository.findByPasswordResetToken(anyString())).thenReturn(player);

        String response = forgotPasswordService.generatePassword("token");

        assertNotNull(response);
        assertNull(player.getPasswordResetToken());
        verify(playerRepository).save(any());
    }

    @Test
    void cancelResetTokenTestSuccess() {
        when(playerRepository.findByPasswordResetToken(anyString())).thenReturn(player);

        forgotPasswordService.cancelResetToken("token");

        verify(playerRepository).save(any());
    }
}