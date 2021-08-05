package tr.metu.ceng.construction.server.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.metu.ceng.construction.common.CardForMultiplayer;
import tr.metu.ceng.construction.common.Rank;
import tr.metu.ceng.construction.common.Suit;
import tr.metu.ceng.construction.common.TableStateForMultiplayer;
import tr.metu.ceng.construction.server.DTO.TableStateDTO;
import tr.metu.ceng.construction.server.model.Card;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static tr.metu.ceng.construction.server.enums.Rank.*;
import static tr.metu.ceng.construction.server.enums.Suit.*;
import static tr.metu.ceng.construction.server.common.CommonConstants.*;
import static tr.metu.ceng.construction.server.utils.GameUtilities.*;

@ExtendWith(MockitoExtension.class)
class GameUtilitiesTest {

    private TableStateDTO tableState;

    @BeforeEach
    void setUp() {
        tableState = new TableStateDTO();
    }

    @Test
    void calculatePointToAddTest() {
        List<Card> faceUpCards = List.of(new Card(CLUBS, FIVE), new Card(DIAMONDS, ACE));
        Card cardToPlay = new Card(CLUBS, ACE);

        int score = calculatePointToAdd(faceUpCards, cardToPlay);

        assertEquals(ACE_EXTRA_POINT * 2, score);
    }

    @Test
    void calculatePointToAddTestForMultiplayer() {
        List<CardForMultiplayer> faceUpCards = List.of(new CardForMultiplayer(Suit.CLUBS, Rank.FIVE), new CardForMultiplayer(Suit.DIAMONDS, Rank.ACE));
        CardForMultiplayer cardToPlay = new CardForMultiplayer(Suit.CLUBS, Rank.ACE);

        int score = calculatePointToAdd(faceUpCards, cardToPlay);

        assertEquals(ACE_EXTRA_POINT * 2, score);
    }

    @Test
    void areCardsFinishedTestFinishedMultiPlayer() {
        tableState.setLevel(4);
        tableState.setPlayer2Cards(new HashSet<>());
        tableState.setPlayer1Cards(new HashSet<>());

        assertTrue(areCardsFinished(tableState));
    }

    @Test
    void areCardsFinishedTestFinishedMultiPlayer2() {
        TableStateForMultiplayer tableStateForMultiplayer = new TableStateForMultiplayer();
        tableStateForMultiplayer.setPlayer2Cards(new HashSet<>());
        tableStateForMultiplayer.setPlayer1Cards(new HashSet<>());

        assertTrue(areCardsFinished(tableStateForMultiplayer));
    }

    @Test
    void areCardsFinishedTestFinishedSinglePlayer() {
        tableState.setLevel(1);
        tableState.setComputerCards(new HashSet<>());
        tableState.setPlayer1Cards(new HashSet<>());

        assertTrue(areCardsFinished(tableState));
    }

    @Test
    void areCardsFinishedTestNotFinishedSinglePlayer() {
        tableState.setLevel(1);
        tableState.setComputerCards(Set.of(new Card(CLUBS, TWO)));

        assertFalse(areCardsFinished(tableState));
    }

    @Test
    void givePointForMostCapturedCardInSinglePlayerTestPlayerWon() {
        tableState.setCapturedCardsNumberByPlayer1(20);
        tableState.setCapturedCardsNumberByComputer(10);
        tableState.setPlayer1LevelScore(0);
        tableState.setComputerLevelScore(0);

        givePointForMostCapturedCardInSinglePlayer(tableState);

        assertEquals(EXTRA_POINTS_BY_CARD_COUNT, tableState.getPlayer1LevelScore());
        assertEquals(0, tableState.getComputerLevelScore());
    }

    @Test
    void givePointForMostCapturedCardInSinglePlayerTestComputerWon() {
        tableState.setCapturedCardsNumberByPlayer1(10);
        tableState.setCapturedCardsNumberByComputer(20);
        tableState.setPlayer1LevelScore(0);
        tableState.setComputerLevelScore(0);

        givePointForMostCapturedCardInSinglePlayer(tableState);

        assertEquals(0, tableState.getPlayer1LevelScore());
        assertEquals(EXTRA_POINTS_BY_CARD_COUNT, tableState.getComputerLevelScore());

    }

    @Test
    void givePointForMostCapturedCardInMultiPlayerTestPlayer1Won() {
        tableState.setCapturedCardsNumberByPlayer1(20);
        tableState.setCapturedCardsNumberByPlayer2(10);
        tableState.setPlayer1LevelScore(0);
        tableState.setPlayer2LevelScore(0);

        givePointForMostCapturedCardInMultiPlayer(tableState);

        assertEquals(EXTRA_POINTS_BY_CARD_COUNT, tableState.getPlayer1LevelScore());
        assertEquals(0, tableState.getPlayer2LevelScore());
    }

    @Test
    void givePointForMostCapturedCardInMultiPlayerTestPlayer2Won() {
        tableState.setCapturedCardsNumberByPlayer1(10);
        tableState.setCapturedCardsNumberByPlayer2(20);
        tableState.setPlayer1LevelScore(0);
        tableState.setPlayer2LevelScore(0);

        givePointForMostCapturedCardInMultiPlayer(tableState);

        assertEquals(0, tableState.getPlayer1LevelScore());
        assertEquals(EXTRA_POINTS_BY_CARD_COUNT, tableState.getPlayer2LevelScore());
    }

    @Test
    void givePointForPlayer1CapturedCardTest() {
        TableStateForMultiplayer tableStateForMultiplayer = new TableStateForMultiplayer();
        tableStateForMultiplayer.setCapturedCardsNumberByPlayer1(20);
        tableStateForMultiplayer.setCapturedCardsNumberByPlayer2(10);
        tableStateForMultiplayer.setPlayer1LevelScore(0);
        tableStateForMultiplayer.setPlayer2LevelScore(0);

        givePointForMostCapturedCardInSinglePlayer(tableStateForMultiplayer);

        assertEquals(EXTRA_POINTS_BY_CARD_COUNT, tableStateForMultiplayer.getPlayer1LevelScore());
        assertEquals(0, tableStateForMultiplayer.getPlayer2LevelScore());
    }

    @Test
    void givePointForPlayer2CapturedCardTest() {
        TableStateForMultiplayer tableStateForMultiplayer = new TableStateForMultiplayer();
        tableStateForMultiplayer.setCapturedCardsNumberByPlayer1(10);
        tableStateForMultiplayer.setCapturedCardsNumberByPlayer2(20);
        tableStateForMultiplayer.setPlayer1LevelScore(0);
        tableStateForMultiplayer.setPlayer2LevelScore(0);

        givePointForMostCapturedCardInSinglePlayer(tableStateForMultiplayer);

        assertEquals(EXTRA_POINTS_BY_CARD_COUNT, tableStateForMultiplayer.getPlayer2LevelScore());
        assertEquals(0, tableStateForMultiplayer.getPlayer1LevelScore());
    }

    @Test
    void givePointForMostCapturedCardTestPlayer2Won() {
        tableState.setCapturedCardsNumberByPlayer1(10);
        tableState.setCapturedCardsNumberByPlayer2(20);
        tableState.setPlayer1LevelScore(0);
        tableState.setPlayer2LevelScore(0);

        givePointForMostCapturedCardInMultiPlayer(tableState);

        assertEquals(0, tableState.getPlayer1LevelScore());
        assertEquals(EXTRA_POINTS_BY_CARD_COUNT, tableState.getPlayer2LevelScore());
    }

    @Test
    void clearLevelScoresTest() {
        tableState.setPlayer1LevelScore(10);
        tableState.setPlayer2LevelScore(20);
        tableState.setComputerLevelScore(25);

        clearLevelScores(tableState);

        assertEquals(0, tableState.getPlayer1LevelScore());
        assertEquals(0, tableState.getPlayer2LevelScore());
        assertEquals(0, tableState.getComputerLevelScore());
    }

    @Test
    void clearNumberOfCapturedCardsTest() {
        tableState.setCapturedCardsNumberByPlayer1(10);
        tableState.setCapturedCardsNumberByPlayer2(20);
        tableState.setCapturedCardsNumberByComputer(25);

        clearNumberOfCapturedCards(tableState);

        assertEquals(0, tableState.getCapturedCardsNumberByPlayer1());
        assertEquals(0, tableState.getCapturedCardsNumberByPlayer2());
        assertEquals(0, tableState.getCapturedCardsNumberByComputer());
    }



}