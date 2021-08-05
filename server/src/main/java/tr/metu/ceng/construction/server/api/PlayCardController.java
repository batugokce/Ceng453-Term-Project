package tr.metu.ceng.construction.server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.metu.ceng.construction.server.DTO.PlayerActionDTO;
import tr.metu.ceng.construction.server.DTO.TableStateDTO;
import tr.metu.ceng.construction.server.DTO.TableStateStagesDTO;
import tr.metu.ceng.construction.server.exception.CardNotBelongToPlayerException;
import tr.metu.ceng.construction.server.exception.GameNotFoundException;
import tr.metu.ceng.construction.server.exception.PlayerNotAuthorizedException;
import tr.metu.ceng.construction.server.exception.PlayerNotFoundException;
import tr.metu.ceng.construction.server.service.PlayCardService;


/**
 * Responsible for handling requests that players made in order to play card.
 *
 */
@RestController
@RequestMapping("/api/v1/playCard")
@RequiredArgsConstructor
public class PlayCardController {

    private final PlayCardService service;

    /**
     * Handles player's move requests, delegates the job to service layer methods.
     *
     * @param action the entity that includes card played, game id, token of the player.
     * @return status of the table after player's move.
     */
    @PostMapping
    public ResponseEntity<TableStateStagesDTO> playCard(@RequestBody PlayerActionDTO action) {
        try {
            TableStateStagesDTO tableStateDTO = service.playCard(action);
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
