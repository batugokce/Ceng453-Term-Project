package tr.metu.ceng.construction.server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tr.metu.ceng.construction.server.service.ForgotPasswordService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/resetPassword")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @GetMapping("/sendEmail/{emailAddress}")
    public boolean forgotPassword(@PathVariable String emailAddress) {
        return forgotPasswordService.forgotPassword(emailAddress);
    }

    @GetMapping("/generatePassword")
    public String generatePassword(@RequestParam String token, @RequestParam(defaultValue = "false") boolean cancel) {
        if (cancel) {
            forgotPasswordService.cancelResetToken(token);
            return "Password reset operation has been cancelled successfully.";
        }
        String newPassword = forgotPasswordService.generatePassword(token);

        return Objects.requireNonNullElse(newPassword, "Token is incorrect");
    }
}
