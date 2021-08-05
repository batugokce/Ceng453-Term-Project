package tr.metu.ceng.construction.client.controller;

import org.springframework.web.reactive.function.client.WebClient;
import tr.metu.ceng.construction.client.DTO.TableStateDTO;
import tr.metu.ceng.construction.client.constant.GameConstants;

import static tr.metu.ceng.construction.client.constant.RequestConstants.baseURL;

/**
 * Responsible for communication with backend in starting gaming feature once per game
 */
public class StartGameController {

    private final WebClient webClient = WebClient.create(baseURL + "game/start/");

    /**
     * Makes an HTTP request to backend '/game/start' endpoint.
     * @return initial state of the table
     */
    public TableStateDTO startGame() {
        TableStateDTO tableStateDTO =  webClient.get()
                .uri(GameConstants.USERNAME)
                .retrieve()
                .bodyToMono(TableStateDTO.class)
                .block();


        System.out.println(tableStateDTO);
        GameConstants.TABLE_STATE = tableStateDTO;
        GameConstants.GAME_ID = tableStateDTO.getGameId();

        return tableStateDTO;
    }
}
