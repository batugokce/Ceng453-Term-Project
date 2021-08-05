package tr.metu.ceng.construction.client.controller;

import org.junit.jupiter.api.Test;
import tr.metu.ceng.construction.client.DTO.ScoreDTO;
import tr.metu.ceng.construction.client.constant.RequestConstants;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class LeaderBoardTest {

    @Test
    void testLeaderBoardAllTimes() {
        LeaderBoardController leaderBoardController = new LeaderBoardController();
        List<ScoreDTO> overallScores = leaderBoardController.getScoresBySpecificUri(RequestConstants.ALL_TIMES);

        assertNotNull(overallScores);
    }

    @Test
    void testLeaderBoardWeekly() {
        LeaderBoardController leaderBoardController = new LeaderBoardController();
        List<ScoreDTO> overallScores = leaderBoardController.getScoresBySpecificUri(RequestConstants.WEEKLY);

        assertNotNull(overallScores);
    }

    @Test
    void testLeaderBoardMonthly() {
        LeaderBoardController leaderBoardController = new LeaderBoardController();
        List<ScoreDTO> overallScores = leaderBoardController.getScoresBySpecificUri(RequestConstants.MONTHLY);

        assertNotNull(overallScores);
    }
}
