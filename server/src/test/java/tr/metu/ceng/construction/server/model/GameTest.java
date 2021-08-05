package tr.metu.ceng.construction.server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameTest {

    private Game game1, game2;
    private Player player1, player2;

    @BeforeEach
    void setUp() {
        game1 = new Game();
        game2 = new Game();

        player1 = new Player();
        player2 = new Player();
    }

    @Test
    void equalsTest() {
        game1.setId(1L);
        game1.setLevel(1);
        game1.setPlayer1(player1);
        game2.setId(1L);
        game2.setLevel(1);
        game2.setPlayer1(player1);

        assertEquals(game1, game2);
    }

    @Test
    void hashCodeTest() {
        game1.setId(1L);
        game1.setLevel(1);
        game1.setPlayer1(player1);
        game2.setId(1L);
        game2.setLevel(1);
        game2.setPlayer1(player1);

        assertEquals(game1.hashCode(), game2.hashCode());
    }

    @Test
    void notEqualsTestId() {
        game1.setId(1L);
        game1.setLevel(1);
        game1.setPlayer1(player1);
        game2.setId(2L);
        game2.setLevel(1);
        game2.setPlayer1(player1);

        assertNotEquals(game1, game2);
    }

    @Test
    void notEqualsTestLevel() {
        game1.setId(1L);
        game1.setLevel(1);
        game1.setPlayer1(player1);
        game2.setId(1L);
        game2.setLevel(2);
        game2.setPlayer1(player1);

        assertNotEquals(game1, game2);
    }

    @Test
    void multiplayerSuccessTest() {
        game1.setLevel(4);

        assertTrue(game1.isMultiPlayer());
    }

    @Test
    void multiplayerFalseTest() {
        game1.setLevel(2);

        assertFalse(game1.isMultiPlayer());
    }

    @Test
    void testGetters() {
        game1 = new Game(player1, player2, 1);
        game1.setId(1L);

        assertEquals(1L, game1.getId());
        assertEquals(1, game1.getLevel());
        assertEquals(player1, game1.getPlayer1());
        assertEquals(player2, game1.getPlayer2());
    }
}