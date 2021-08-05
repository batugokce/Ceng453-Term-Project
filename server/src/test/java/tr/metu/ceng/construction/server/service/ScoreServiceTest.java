package tr.metu.ceng.construction.server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.metu.ceng.construction.server.DTO.ScoreDTO;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.model.Score;
import tr.metu.ceng.construction.server.repository.PlayerRepository;
import tr.metu.ceng.construction.server.repository.ScoreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScoreServiceTest {

    @Mock
    private ScoreRepository scoreRepository;
    @Mock
    private PlayerRepository playerRepository;
    @InjectMocks
    private ScoreService scoreService;

    private final static int SCORE = 20;
    private final static Player PLAYER = new Player();

    @Test
    void saveScore() {
        Score scoreToSave = new Score(SCORE, LocalDate.now(), PLAYER);
        when(scoreRepository.save(any())).thenReturn(scoreToSave);

        Score scoreDB = scoreService.saveScore(SCORE, PLAYER);

        verify(scoreRepository).save(any());
        assertNotNull(scoreDB);
    }

    @Test
    void saveScoreByPlayerId() {
        Score scoreToSave = new Score(SCORE, LocalDate.now(), PLAYER);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(PLAYER));
        when(scoreRepository.save(any())).thenReturn(scoreToSave);

        scoreService.saveScore(SCORE, 1L);

        verify(scoreRepository).save(any());
    }

    @Test
    void findAllTimesScoresTest() {
        when(scoreRepository.findAllTimesScores()).thenReturn(new ArrayList<>());

        List<ScoreDTO> scoreDTOS = scoreService.findAllTimesScores();

        assertNotNull(scoreDTOS);
        assertEquals(0, scoreDTOS.size());
    }

    @Test
    void findMonthlyScoresTest() {
        when(scoreRepository.findScoresInRange(any())).thenReturn(new ArrayList<>());

        List<ScoreDTO> scoreDTOS = scoreService.findMonthlyScores();

        assertNotNull(scoreDTOS);
        assertEquals(0, scoreDTOS.size());
    }

    @Test
    void findWeeklyScoresTest() {
        when(scoreRepository.findScoresInRange(any())).thenReturn(new ArrayList<>());

        List<ScoreDTO> scoreDTOS = scoreService.findWeeklyScores();

        assertNotNull(scoreDTOS);
        assertEquals(0, scoreDTOS.size());
    }

}