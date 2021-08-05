package tr.metu.ceng.construction.server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.metu.ceng.construction.common.CardForMultiplayer;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tr.metu.ceng.construction.common.Rank.*;
import static tr.metu.ceng.construction.common.Suit.*;
import static tr.metu.ceng.construction.server.common.CommonConstants.*;
import static tr.metu.ceng.construction.server.common.CommonConstants.TEN_DIAMONDS_EXTRA_POINT;

@ExtendWith(MockitoExtension.class)
public class CardForMultiplayerTest {

    private CardForMultiplayer card1, card2;

    @BeforeEach
    void setUp() {
        card1 = new CardForMultiplayer();
        card2 = new CardForMultiplayer();
    }

    @Test
    void testEquals() {
        card1 = new CardForMultiplayer(CLUBS, TWO);
        card2 = new CardForMultiplayer(CLUBS, TWO);

        assertEquals(card1, card2);
    }

    @Test
    void testHashCode() {
        card1 = new CardForMultiplayer(CLUBS, TWO);
        card2 = new CardForMultiplayer(CLUBS, TWO);

        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void testNotEquals() {
        card1 = new CardForMultiplayer(CLUBS, TWO);
        card1 = new CardForMultiplayer(CLUBS, THREE);

        assertNotEquals(card1, card2);
    }

    @Test
    void testEqualsWithDifferentObject() {
        card1 = new CardForMultiplayer(CLUBS, TWO);

        assertNotEquals(card1, null);
    }

    @Test
    void testGetterSetter() {
        card1.setRank(TWO);
        card1.setSuit(CLUBS);

        assertEquals(TWO, card1.getRank());
        assertEquals(CLUBS, card1.getSuit());
    }

    @Test
    void compareRankTest() {
        card1 = new CardForMultiplayer(CLUBS, THREE);
        card2 = new CardForMultiplayer(SPADES, THREE);

        assertTrue(card1.compareRank(card2));
    }

    @Test
    void compareRankTestNotEqual() {
        card1.setRank(TWO);
        card1.setSuit(CLUBS);
        card2.setRank(THREE);
        card2.setSuit(CLUBS);

        assertFalse(card1.compareRank(card2));
    }

    @Test
    void canCaptureTestSameRank() {
        card1.setRank(THREE);
        card1.setSuit(CLUBS);
        card2.setRank(THREE);
        card2.setSuit(SPADES);

        assertTrue(card1.canCapture(card2));
    }

    @Test
    void canCaptureTestJack() {
        card1.setRank(JACK);
        card1.setSuit(CLUBS);
        card2.setRank(THREE);
        card2.setSuit(SPADES);

        assertTrue(card1.canCapture(card2));
    }

    @Test
    void canCaptureFailTest() {
        card1.setRank(FOUR);
        card1.setSuit(CLUBS);
        card2.setRank(THREE);
        card2.setSuit(SPADES);

        assertFalse(card1.canCapture(card2));
    }

    @Test
    void calculateExtraPointsTestJack() {
        card1.setRank(JACK);
        card1.setSuit(CLUBS);

        assertEquals(JACK_EXTRA_POINT, card1.calculateExtraPoints());
    }

    @Test
    void calculateExtraPointsTestAce() {
        card1.setRank(ACE);
        card1.setSuit(CLUBS);

        assertEquals(ACE_EXTRA_POINT, card1.calculateExtraPoints());
    }

    @Test
    void calculateExtraPointsTestTwoClubs() {
        card1.setRank(TWO);
        card1.setSuit(CLUBS);

        assertEquals(TWO_CLUBS_EXTRA_POINT, card1.calculateExtraPoints());
    }

    @Test
    void calculateExtraPointsTestTenDiamonds() {
        card1.setRank(TEN);
        card1.setSuit(DIAMONDS);

        assertEquals(TEN_DIAMONDS_EXTRA_POINT, card1.calculateExtraPoints());
    }

    @Test
    void calculateExtraPointsTestNoExtraPoint() {
        card1.setRank(NINE);
        card1.setSuit(CLUBS);

        assertEquals(0, card1.calculateExtraPoints());
    }
}
