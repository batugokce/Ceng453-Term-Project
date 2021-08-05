package tr.metu.ceng.construction.server.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    private Score score1, score2;
    private final Player player = new Player();

    @Test
    void testEquals() {
        score1 = new Score(20, LocalDate.now(), player);
        score1.setId(1L);
        score2 = new Score(20, LocalDate.now(), player);
        score2.setId(1L);

        assertEquals(score1, score2);
        assertEquals(score1.hashCode(), score2.hashCode());
    }

    @Test
    void testNotEquals() {
        score1 = new Score(20, LocalDate.now(), player);
        score1.setId(1L);
        score2 = new Score();
        score2.setId(2L);
        score2.setScore(50);

        assertNotEquals(score1, score2);
        assertNotEquals(score1.hashCode(), score2.hashCode());
    }

    @Test
    void testGetter() {
        score1 = new Score(20, LocalDate.now(), player);
        score1.setId(1L);

        assertEquals(1L, score1.getId());
        assertEquals(20, score1.getScore());
        assertEquals(LocalDate.now(), score1.getCreatedDate());
        assertEquals(player, score1.getOwnerPlayer());
    }
}