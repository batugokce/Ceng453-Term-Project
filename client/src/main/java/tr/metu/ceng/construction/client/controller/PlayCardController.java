package tr.metu.ceng.construction.client.controller;

import org.springframework.web.reactive.function.client.WebClient;
import tr.metu.ceng.construction.client.DTO.PlayerActionDTO;
import tr.metu.ceng.construction.client.DTO.TableStateStagesDTO;

import static tr.metu.ceng.construction.client.constant.RequestConstants.baseURL;
import static tr.metu.ceng.construction.client.scene.utils.CommonItems.getTableStateStagesDTO;

/**
 * Responsible for communication with backend in playing card feature
 */
public class PlayCardController {

    private final WebClient webClient = WebClient.create(baseURL + "playCard/");

    /**
     * Makes an API request to backend 'playCard' endpoint.
     * @param action player's action
     * @return a stage DTO that includes general state of the table
     */
    public TableStateStagesDTO playCard(PlayerActionDTO action) {
        return getTableStateStagesDTO(action, webClient);
    }
}
