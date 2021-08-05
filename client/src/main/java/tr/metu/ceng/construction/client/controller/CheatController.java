package tr.metu.ceng.construction.client.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tr.metu.ceng.construction.client.DTO.TableStateDTO;
import tr.metu.ceng.construction.client.constant.GameConstants;

import static tr.metu.ceng.construction.client.constant.MessageConstants.CHEATING_NOT_ALLOWED;
import static tr.metu.ceng.construction.client.constant.RequestConstants.baseURL;

/**
 * Responsible for communication with backend in cheating feature
 */
public class CheatController {

    /**
     * Makes an API request to backend 'cheat' endpoint without blocking the application.
     * @param tableState this is the status of the table when the request sent
     * @return status of the table after cheating.
     */
    public TableStateDTO cheat(TableStateDTO tableState) {
        TableStateDTO returnedTableStateDTO = WebClient.create(baseURL + "game/cheat/")
                .post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(tableState), TableStateDTO.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new Exception(CHEATING_NOT_ALLOWED)))
                .bodyToMono(TableStateDTO.class)
                .block();

        if (null != returnedTableStateDTO) {
            //System.out.println("leveled up:" + returnedTableStateDTO.getLevel());
            GameConstants.TABLE_STATE = returnedTableStateDTO;
        }

        return returnedTableStateDTO;
    }
}
