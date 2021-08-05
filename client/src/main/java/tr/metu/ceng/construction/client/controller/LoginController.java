package tr.metu.ceng.construction.client.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tr.metu.ceng.construction.client.DTO.LoginCredentialsDTO;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.client.constant.RequestConstants;
import tr.metu.ceng.construction.client.constant.StageConstants;
import tr.metu.ceng.construction.client.scene.MainMenuScene;

import static tr.metu.ceng.construction.client.constant.MessageConstants.INVALID_LOGIN;
import static tr.metu.ceng.construction.client.constant.MessageConstants.SUCCESSFUL_LOGIN;

/**
 * Responsible for communication with backend in login feature
 */
public class LoginController {

    /**
     * This method provides login to the application.
     *
     * @param username username of the current player
     * @param password password of the current player
     * @return success message if login operation successful.
     * if not return an error message.
     */
    public String login(String username, String password) {

        WebClient webClient = WebClient.create(RequestConstants.baseURL);

        LoginCredentialsDTO loginCredentialsRequest = new LoginCredentialsDTO();
        loginCredentialsRequest.setUsername(username);
        loginCredentialsRequest.setPassword(password);

        try {
            LoginCredentialsDTO loginCredentialsResponse = webClient.post()
                    .uri("player/login")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(loginCredentialsRequest), LoginCredentialsDTO.class)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new Exception(INVALID_LOGIN)))
                    .bodyToMono(LoginCredentialsDTO.class)
                    .block();

            GameConstants.USERNAME = username;
            GameConstants.TOKEN = loginCredentialsResponse.getToken();
            GameConstants.PLAYER_ID = loginCredentialsResponse.getPlayerId();
            return SUCCESSFUL_LOGIN;
        } catch (Exception e) {
            // username or password is invalid
            return INVALID_LOGIN;
        }
    }
}