package tr.metu.ceng.construction.server.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import tr.metu.ceng.construction.server.model.Game;
import tr.metu.ceng.construction.server.model.Player;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private static final String USERNAME = "username";
    private static final int LEVEL = 1;
    private static Long id;

    @Test
    @Rollback(false)
    @Order(1)
    public void testCreateGame() {
        Player player1 = playerRepository.findByUsername(USERNAME);
        Game newGame = new Game(player1, null, LEVEL);

        Game savedGame = gameRepository.save(newGame);
        id = savedGame.getId();

        assertEquals(USERNAME, savedGame.getPlayer1().getUsername());
        assertEquals(LEVEL, savedGame.getLevel());
    }

    @Test
    @Rollback(false)
    @Order(2)
    public void testRetrieveGame() {
        Game game = gameRepository.findById(id).orElse(null);

        assertNotNull(game);
    }

    @Test
    @Rollback(false)
    @Order(3)
    public void testUpdateGame() {
        Game game = gameRepository.findById(id).orElse(null);

        int level = 2;
        if (game != null) {
            game.setLevel(level);
            gameRepository.save(game);
        }

        gameRepository.findById(id).ifPresent(updatedGame -> assertEquals(level, updatedGame.getLevel()));
    }

    @Test
    @Rollback(false)
    @Order(4)
    public void testDeleteGame() {
        gameRepository.deleteById(id);

        Game deletedGame = gameRepository.findById(id).orElse(null);
        assertNull(deletedGame);
    }

}