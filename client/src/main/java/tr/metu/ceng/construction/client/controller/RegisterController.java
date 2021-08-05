package tr.metu.ceng.construction.client.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tr.metu.ceng.construction.client.DTO.PlayerDTO;
import tr.metu.ceng.construction.client.constant.RequestConstants;

import static tr.metu.ceng.construction.client.constant.MessageConstants.*;

/**
 * Responsible for communication with backend in register feature
 */
public class RegisterController {
    /**
     * This method provides registration to the application.
     *
     * @param username username of the player
     * @param password password of the player to be created
     * @param emailAddress email address of the player to be created
     * @return a success message if registration operation successful.
     * if not return an error message.
     */
    public String register(String username, String password, String emailAddress) {

        WebClient webClient = WebClient.create(RequestConstants.baseURL);

        PlayerDTO player = new PlayerDTO();
        player.setUsername(username);
        player.setPassword(password);
        player.setEmailAddress(emailAddress);

        PlayerDTO returnedPlayer =webClient.post()
                .uri("player/create")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(player), PlayerDTO.class)
                .retrieve()
                .bodyToMono(PlayerDTO.class)
                .block();

        if (returnedPlayer == null) {
            return USERNAME_OR_EMAIL_NOT_UNIQUE;
        } else {
            return SUCCESSFUL_REGISTRATION;
        }
    }
}
