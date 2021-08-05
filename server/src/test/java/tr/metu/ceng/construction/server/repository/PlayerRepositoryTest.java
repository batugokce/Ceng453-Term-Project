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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    private static final String USERNAME = "user";
    private static final String EMAIL = "user@gmail.com";
    private static final String TOKEN = "token";
    private static final String PASSWORD = "pass";

    @Test
    @Rollback(false)
    @Order(1)
    public void testCreatePlayer() {
        Player savedPlayer = playerRepository.save(new Player(null, USERNAME, PASSWORD, EMAIL, TOKEN, 0, "", new ArrayList<>()));

        assertNotNull(savedPlayer.getId());
        assertEquals(USERNAME, savedPlayer.getUsername());
        assertEquals(EMAIL, savedPlayer.getEmailAddress());
        assertEquals(TOKEN, savedPlayer.getToken());
    }

    @Test
    @Rollback(false)
    @Order(2)
    public void testFindPlayerByUsername() {
        Player player = playerRepository.findByUsername(USERNAME);
        assertEquals(USERNAME, player.getUsername());
    }

    @Test
    @Rollback(false)
    @Order(3)
    public void testUpdatePlayer() {
        Player player = playerRepository.findByUsername(USERNAME);

        int cumulativeScore = 20;
        player.setCumulativeScore(cumulativeScore);

        playerRepository.save(player);

        Player updatedPlayer = playerRepository.findByUsername(USERNAME);
        assertEquals(cumulativeScore, updatedPlayer.getCumulativeScore());
    }

    @Test
    @Rollback(false)
    @Order(4)
    void existsByUsernameTrue() {
        assertTrue(playerRepository.existsByUsername(USERNAME));
    }

    @Test
    @Rollback(false)
    @Order(5)
    public void testDeletePlayer() {
        playerRepository.deleteByUsername(USERNAME);

        Player deletedPlayer = playerRepository.findByUsername(USERNAME);
        assertNull(deletedPlayer);
    }

    @Test
    @Rollback(false)
    @Order(6)
    void existsByUsernameFalse() {
        assertFalse(playerRepository.existsByUsername(USERNAME));
    }

}