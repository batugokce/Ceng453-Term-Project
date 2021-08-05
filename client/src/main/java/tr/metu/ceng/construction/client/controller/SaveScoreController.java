package tr.metu.ceng.construction.client.controller;

import org.springframework.web.reactive.function.client.WebClient;
import tr.metu.ceng.construction.client.constant.GameConstants;

import static tr.metu.ceng.construction.client.constant.RequestConstants.baseURL;

/**
 * Responsible for communication with backend in saving score after multiplayer game finishes
 */
public class SaveScoreController {

    private final WebClient webClient = WebClient.create(baseURL + "score/save/");

    /**
     * Make a request to backend for saving overall score
     * @param score overall score of the player
     * @return a mono wrapper with the response from the backend
     */
    public Boolean saveScore(Integer score) {
        return webClient.get()
                .uri(GameConstants.PLAYER_ID.toString() + '/' + score.toString())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

    }

}
