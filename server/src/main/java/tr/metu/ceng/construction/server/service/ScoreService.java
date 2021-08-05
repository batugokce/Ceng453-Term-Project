package tr.metu.ceng.construction.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.metu.ceng.construction.server.DTO.ScoreDTO;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.model.Score;
import tr.metu.ceng.construction.server.repository.PlayerRepository;
import tr.metu.ceng.construction.server.repository.ScoreRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for managing score entities.
 */
@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final PlayerRepository playerRepository;

    /**
     * Responsible for creating new score entity, and saving it in the database.
     * When a player completes the game, this method is called by specifying score, and owner player.
     *
     * @param score the score that player collected
     * @param player owner player of the score entity
     */
    public Score saveScore(int score, Player player) {
        Score scoreEntity = new Score();
        scoreEntity.setScore(score);
        scoreEntity.setOwnerPlayer(player);
        scoreEntity.setCreatedDate(LocalDate.now());
        return scoreRepository.save(scoreEntity);
    }

    /**
     * Responsible for saving score given a score and player id
     * @param score overall score of the player
     * @param playerId ID of the player
     */
    public void saveScore(int score, Long playerId) {
        playerRepository.findById(playerId).ifPresent(player -> saveScore(score, player));
    }

    /**
     * Responsible for getting all times scores from the database.
     *
     * @return sorted all-times Score list
     */
    public List<ScoreDTO> findAllTimesScores() {
        return convertScoreListFromObjectArrayList(scoreRepository.findAllTimesScores());
    }

    /**
     * Responsible for getting scores recorded in the last recent week (last 7 days) from the database.
     *
     * @return sorted weekly Score list
     */
    public List<ScoreDTO> findWeeklyScores() {
        LocalDate lastWeek = getPreviousWeek(LocalDate.now());
        //System.out.println(lastWeek.getDayOfMonth() + " " + lastWeek.getMonthValue() + " " + lastWeek.getYear());

        return convertScoreListFromObjectArrayList(scoreRepository.findScoresInRange(lastWeek));
    }

    /**
     * Responsible for getting scores recorded in the last recent month (last 30 days) from the database.
     *
     * @return sorted monthly Score list
     */
    public List<ScoreDTO> findMonthlyScores() {
        LocalDate lastMonth = getPreviousMonth(LocalDate.now());
        //System.out.println(lastMonth.getDayOfMonth() + " " + lastMonth.getMonthValue() + " " + lastMonth.getYear());

        return convertScoreListFromObjectArrayList(scoreRepository.findScoresInRange(lastMonth));
    }

    private LocalDate getPreviousWeek(LocalDate date) {
        return date.minusDays(7);
    }

    private LocalDate getPreviousMonth(LocalDate date) {
        return date.minusMonths(1);
    }

    private List<ScoreDTO> convertScoreListFromObjectArrayList(List<Object[]> returnedList) {
        List<ScoreDTO> scoreDTOList = new ArrayList<>();

        returnedList.forEach(scoreDTOObject -> scoreDTOList.add(new ScoreDTO((String) scoreDTOObject[0], (int) scoreDTOObject[1], ((LocalDate) scoreDTOObject[2]).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))));
        return scoreDTOList;
    }

}
