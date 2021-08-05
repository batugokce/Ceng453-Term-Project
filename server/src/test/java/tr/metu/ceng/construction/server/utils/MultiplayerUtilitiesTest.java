package tr.metu.ceng.construction.server.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.metu.ceng.construction.common.CardForMultiplayer;
import tr.metu.ceng.construction.common.Rank;
import tr.metu.ceng.construction.common.Suit;
import tr.metu.ceng.construction.common.TableStateForMultiplayer;
import tr.metu.ceng.construction.server.model.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tr.metu.ceng.construction.server.utils.MultiplayerUtilities.*;

@ExtendWith(MockitoExtension.class)
public class MultiplayerUtilitiesTest {

    private TableStateForMultiplayer tableState;
    private Player player1, player2;

    private static final String USERNAME1 = "user1";
    private static final String USERNAME2 = "user2";

    @BeforeEach
    void setUp() {
        player1 = new Player();
        player1.setUsername(USERNAME1);

        player2 = new Player();
        player2.setUsername(USERNAME2);
        tableState = new TableStateForMultiplayer();
    }

    @Test
    void testPrepareGameTable() {
        TableStateForMultiplayer tableState = prepareGameTable(player1.getUsername(), player2.getUsername(), 300, 400);

        assertEquals(40, tableState.getFaceDownCards().size());
        assertEquals(4, tableState.getPlayer1Cards().size());
        assertEquals(4, tableState.getPlayer2Cards().size());
        assertEquals(4, tableState.getFaceUpCards().size());
    }

    @Test
    void testDealCardsMultiPlayerGameSuccess() {
        TableStateForMultiplayer tableState = prepareTableState();

        dealCardsToPlayers(tableState);

        assertEquals(4, tableState.getPlayer1Cards().size());
        assertEquals(4, tableState.getPlayer2Cards().size());
        assertEquals(4, tableState.getFaceDownCards().size());
    }


    @Test
    void testPutRandomCardsToCenter() {
        TableStateForMultiplayer tableState = prepareTableState();

        putCardToCenter(tableState);

        assertEquals(4, tableState.getFaceUpCards().size());
        assertEquals(8, tableState.getFaceDownCards().size());
    }

    private TableStateForMultiplayer prepareTableState() {
        Set<CardForMultiplayer> player1Cards = new HashSet<>();
        tableState.setPlayer1Cards(player1Cards);

        List<CardForMultiplayer> faceUpCards = new ArrayList<>();
        tableState.setFaceUpCards(faceUpCards);

        Set<CardForMultiplayer> faceDownCards = new HashSet<>();
        faceDownCards.add(new CardForMultiplayer(Suit.SPADES, Rank.ACE));
        faceDownCards.add(new CardForMultiplayer(Suit.HEARTS, Rank.QUEEN));
        faceDownCards.add(new CardForMultiplayer(Suit.DIAMONDS, Rank.TEN));
        faceDownCards.add(new CardForMultiplayer(Suit.CLUBS, Rank.JACK));
        faceDownCards.add(new CardForMultiplayer(Suit.SPADES, Rank.THREE));
        faceDownCards.add(new CardForMultiplayer(Suit.HEARTS, Rank.KING));
        faceDownCards.add(new CardForMultiplayer(Suit.SPADES, Rank.SEVEN));
        faceDownCards.add(new CardForMultiplayer(Suit.SPADES, Rank.NINE));
        faceDownCards.add(new CardForMultiplayer(Suit.CLUBS, Rank.FOUR));
        faceDownCards.add(new CardForMultiplayer(Suit.CLUBS, Rank.SIX));
        faceDownCards.add(new CardForMultiplayer(Suit.DIAMONDS, Rank.JACK));
        faceDownCards.add(new CardForMultiplayer(Suit.HEARTS, Rank.THREE));
        tableState.setFaceDownCards(faceDownCards);

        Set<CardForMultiplayer> player2Cards = new HashSet<>();
        tableState.setPlayer2Cards(player2Cards);

        tableState.setCapturedCardsNumberByPlayer1(6);

        return tableState;
    }

    @Test
    void testPrepareDeck() {
        Set<CardForMultiplayer> deck = prepareDeck();

        assertEquals(52, deck.size());
    }

}
