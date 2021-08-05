package tr.metu.ceng.construction.server.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.model.Score;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ScoreRepositoryTest {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private static final String USERNAME = "username";
    private static final int SCORE = 151;
    private static final LocalDate CREATED_TIME = LocalDate.now();
    private static Long id;

    @Test
    @Rollback(false)
    @Order(1)
    public void testCreateScore() {
        Player ownerPlayer = playerRepository.findByUsername(USERNAME);
        Score newScore = new Score(SCORE, CREATED_TIME, ownerPlayer);

        Score savedScore = scoreRepository.save(newScore);
        id = savedScore.getId();

        assertEquals(USERNAME, savedScore.getOwnerPlayer().getUsername());
        assertEquals(SCORE, savedScore.getScore());
        assertEquals(CREATED_TIME, savedScore.getCreatedDate());
    }

    @Test
    @Rollback(false)
    @Order(2)
    public void testRetrieveScore() {
        Score score = scoreRepository.findById(id).orElse(null);

        assertNotNull(score);
        assertEquals(CREATED_TIME, score.getCreatedDate());
    }

    @Test
    @Rollback(false)
    @Order(3)
    public void testUpdateScore() {
        Score score = scoreRepository.findById(id).orElse(null);

        if (score != null) {
            score.setScore(SCORE - 100);
            scoreRepository.save(score);
        }

        scoreRepository.findById(id).ifPresent(updatedScore -> assertEquals(SCORE - 100, updatedScore.getScore()));
    }

    @Test
    @Rollback(false)
    @Order(4)
    public void testDeleteScore() {
        scoreRepository.deleteById(id);

        Score deletedScore = scoreRepository.findById(id).orElse(null);
        assertNull(deletedScore);
    }

}