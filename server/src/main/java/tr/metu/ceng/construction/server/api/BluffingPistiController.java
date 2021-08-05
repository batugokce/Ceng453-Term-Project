package tr.metu.ceng.construction.server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.metu.ceng.construction.server.DTO.PlayerActionDTO;
import tr.metu.ceng.construction.server.DTO.TableStateStagesDTO;
import tr.metu.ceng.construction.server.exception.CardNotBelongToPlayerException;
import tr.metu.ceng.construction.server.exception.GameNotFoundException;
import tr.metu.ceng.construction.server.exception.PlayerNotAuthorizedException;
import tr.metu.ceng.construction.server.exception.PlayerNotFoundException;
import tr.metu.ceng.construction.server.service.BluffingPistiService;

/**
 * Responsible for handling requests that has been made for the bluffing pisti claims.
 *
 */
@RestController
@RequestMapping("/api/v1/bluffing")
@RequiredArgsConstructor
public class BluffingPistiController {

    private final BluffingPistiService bluffingPistiService;

    /**
     * Handles player's bluffing claims, delegates the job to service layer methods.
     *
     * @param action the entity that includes card played, game id, token of the player.
     * @return status of the table after player's move.
     */
    @PostMapping
    public ResponseEntity<TableStateStagesDTO> bluffCard(@RequestBody PlayerActionDTO action) {
        try {
            TableStateStagesDTO tableStateDTO = bluffingPistiService.bluffCard(action);
            return ResponseEntity.ok(tableStateDTO);
        } catch (GameNotFoundException | PlayerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TableStateStagesDTO(action.getTableState(), action.getTableState()));
        } catch (PlayerNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new TableStateStagesDTO(action.getTableState(), action.getTableState()));
        } catch (CardNotBelongToPlayerException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new TableStateStagesDTO(action.getTableState(), action.getTableState()));
        }
    }
}
