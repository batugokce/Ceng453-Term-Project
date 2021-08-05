package tr.metu.ceng.construction.server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.metu.ceng.construction.server.exception.GameNotFoundException;
import tr.metu.ceng.construction.server.exception.GameNotMultiPlayerException;
import tr.metu.ceng.construction.server.exception.PlayerNotFoundException;
import tr.metu.ceng.construction.server.service.GameService;
import tr.metu.ceng.construction.server.DTO.TableStateDTO;

/**
 * Responsible for handling requests that has been made for a new game start.
 *
 */
@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    /**
     * This method starts a game by creating a new game,
     * dealing cards to the player and the computer and setting the table.
     *
     * @param username this is the username of the player starting game.
     * @return status of the table after creating the game.
     */
    @GetMapping("/start/{username}")
    public ResponseEntity<TableStateDTO> startGame(@PathVariable String username){
        try {
            TableStateDTO tableStateDTO = gameService.startGame(username);
            return ResponseEntity.ok(tableStateDTO);
        } catch (PlayerNotFoundException | GameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * This method provides a cheating.
     * The player passes the level and gets a score of 151 from the current level.
     * This is valid only for single-player game levels.
     *
     * @param tableState this is the status of the table when the request sent
     * @return status of the table after cheating.
     */
    @PostMapping("/cheat")
    public ResponseEntity<TableStateDTO> cheat(@RequestBody TableStateDTO tableState){
        try {
            TableStateDTO newTableStateDTO = gameService.cheat(tableState);
            return ResponseEntity.ok(newTableStateDTO);
        } catch (PlayerNotFoundException | GameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tableState);
        } catch (GameNotMultiPlayerException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tableState);
        }
    }

}
