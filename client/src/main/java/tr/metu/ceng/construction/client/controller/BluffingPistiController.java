package tr.metu.ceng.construction.client.controller;

import org.springframework.web.reactive.function.client.WebClient;
import tr.metu.ceng.construction.client.DTO.PlayerActionDTO;
import tr.metu.ceng.construction.client.DTO.TableStateStagesDTO;

import static tr.metu.ceng.construction.client.constant.RequestConstants.baseURL;
import static tr.metu.ceng.construction.client.scene.utils.CommonItems.getTableStateStagesDTO;

/**
 * Responsible for communication with backend in bluffing feature.
 */
public class BluffingPistiController {

    private final WebClient webClient = WebClient.create(baseURL + "bluffing");

    /**
     * Makes a 'bluffing' request to backend.
     * @param action action of the player
     * @return stage dto that includes state of the table
     */
    public TableStateStagesDTO bluffCard(PlayerActionDTO action) {
        return getTableStateStagesDTO(action, webClient);
    }
}
