package tr.metu.ceng.construction.server.model;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PlayerTest {

    private Player player1, player2;

    @BeforeEach
    void setUp() {
        player1 = new Player();
        player2 = new Player();
    }

    @Test
    void testEqualsAndHashCode() {
        player1.setId(1L);
        player1.setUsername("username");
        player2.setId(1L);
        player2.setUsername("username");

        assertEquals(player1, player2);
        assertEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    void testNotEquals() {
        player1.setId(1L);
        player1.setUsername("username");
        player2.setId(2L);
        player2.setUsername("otherUser");

        assertNotEquals(player1, player2);
        assertNotEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    void testGetterSetter() {
        String randomToken = RandomString.make(10);

        player1.setId(1L);
        player1.setUsername("username");
        player1.setPassword("pass");
        player1.setToken(randomToken);
        player1.setCumulativeScore(10);
        player1.setOverallScores(new ArrayList<>());
        player1.setGame(new Game());

        assertEquals(1L, player1.getId());
        assertEquals("username", player1.getUsername());
        assertEquals("pass", player1.getPassword());
        assertEquals(randomToken, player1.getToken());
        assertEquals(10, player1.getCumulativeScore());
        assertEquals(0, player1.getOverallScores().size());
        assertNotNull(player1.getGame());
    }

    @Test
    void testParametrizedConstructor() {
        player1 = new Player(new Game(), "user", "pass", "user@gmail.com", "token", 10, "", new ArrayList<>());

        assertEquals("user", player1.getUsername());
        assertEquals("pass", player1.getPassword());
    }

}