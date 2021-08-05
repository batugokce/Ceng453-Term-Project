package tr.metu.ceng.construction.server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.metu.ceng.construction.server.DTO.ScoreDTO;
import tr.metu.ceng.construction.server.service.ScoreService;

import java.util.List;

/**
 * Responsible for handling requests related with leaderboard.
 *
 */
@RequestMapping("/api/v1/score")
@RequiredArgsConstructor
@RestController
public class ScoreController {

    private final ScoreService scoreService;

    /**
     * This method is responsible for handling request from the client after multiplayer game finishes
     *
     * @param id player ID
     * @param score overall score of the player
     */
    @GetMapping("/save/{id}/{score}")
    public boolean saveScore(@PathVariable Long id, @PathVariable int score) {
        scoreService.saveScore(score, id);
        return true;
    }

    /**
     * This method gets all times scores of player sorted by descending score
     * and descending date.
     *
     * @return sorted score list
     */
    @GetMapping("/allTimes")
    public ResponseEntity<List<ScoreDTO>> getAllTimesScores() {
        List<ScoreDTO> scoreList = scoreService.findAllTimesScores();
        return  ResponseEntity.ok(scoreList);
    }

    /**
     * This method gets weekly (last recent 7 days) scores of users sorted by descending score
     * and descending date.
     *
     * @return sorted score list
     */
    @GetMapping("/weekly")
    public ResponseEntity<List<ScoreDTO>> getWeeklyScores() {
        List<ScoreDTO> scoreList = scoreService.findWeeklyScores();
        return  ResponseEntity.ok(scoreList);
    }

    /**
     * This method gets monthly (last recent 30 days) scores of users sorted by descending score
     * and descending date.
     *
     * @return sorted score list
     */
    @GetMapping("/monthly")
    public ResponseEntity<List<ScoreDTO>> getMonthlyScores() {
        List<ScoreDTO> scoreList = scoreService.findMonthlyScores();
        return  ResponseEntity.ok(scoreList);
    }
}
