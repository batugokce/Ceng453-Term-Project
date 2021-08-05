package tr.metu.ceng.construction.client.controller;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static tr.metu.ceng.construction.client.constant.RequestConstants.baseURL;

/**
 * Responsible for communication with backend in forgot password feature
 */
public class ForgotPasswordController {

    private final WebClient webClient = WebClient.create(baseURL + "resetPassword/sendEmail/");

    /**
     * Makes an API request to backend 'forget password' endpoint without blocking the application
     * @param emailAddress e-mail address of the user
     * @return a mono wrapper with the response from the backend
     */
    public Mono<Boolean> forgotPassword(String emailAddress) {
        return webClient.get()
                .uri(emailAddress)
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
