package tr.metu.ceng.construction.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.metu.ceng.construction.server.enums.Rank;
import tr.metu.ceng.construction.server.enums.Suit;
import tr.metu.ceng.construction.server.model.Card;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    private Set<Card> cards;

    @BeforeEach
    void setUp() {
        cards = new HashSet<>();
        cards.add(new Card(Suit.DIAMONDS, Rank.TEN));
    }

    @Test
    void testGetRandomCardSingleCase() {
        Card randomCard = cardService.getRandomCard(cards);
        assertNotNull(randomCard);
    }

    @Test
    void testGetRandomCardMultipleCase() {
        cards.add(new Card(Suit.SPADES, Rank.THREE));
        cards.add(new Card(Suit.HEARTS, Rank.KING));
        cards.add(new Card(Suit.CLUBS, Rank.KING));
        cards.add(new Card(Suit.HEARTS, Rank.TWO));
        cards.add(new Card(Suit.DIAMONDS, Rank.QUEEN));

        Card randomCard = cardService.getRandomCard(cards);
        assertNotNull(randomCard);
    }

    @Test
    void testGetRandomCardEmptyCase() {
        Card randomCard = cardService.getRandomCard(new HashSet<>());
        assertNull(randomCard);
    }

    @Test
    void testFindHighestScoredCard() {
        Card highestScoredCard = cardService.findHighestScoredCard(cards);
        assertEquals(Suit.DIAMONDS, highestScoredCard.getSuit());
        assertEquals(Rank.TEN, highestScoredCard.getRank());
    }

    @Test
    void testFindHighestScoredCardEqualValue() {
        cards.remove(new Card(Suit.DIAMONDS, Rank.TEN));
        cards.add(new Card(Suit.SPADES, Rank.JACK));

        Card highestScoredCard = cardService.findHighestScoredCard(cards);
        assertNotNull(highestScoredCard);
    }

    @Test
    void testFindHighestScoredCardNull() {
        Card highestScoredCard = cardService.findHighestScoredCard(new HashSet<>());
        assertNull(highestScoredCard);
    }
}