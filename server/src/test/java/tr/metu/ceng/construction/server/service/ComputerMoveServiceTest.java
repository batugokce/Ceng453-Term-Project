package tr.metu.ceng.construction.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.metu.ceng.construction.server.DTO.TableStateDTO;
import tr.metu.ceng.construction.server.enums.Rank;
import tr.metu.ceng.construction.server.enums.Suit;
import tr.metu.ceng.construction.server.exception.CouldNotPickACardException;
import tr.metu.ceng.construction.server.exception.GameNotFoundException;
import tr.metu.ceng.construction.server.model.Card;
import tr.metu.ceng.construction.server.model.Game;
import tr.metu.ceng.construction.server.model.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ComputerMoveServiceTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private ComputerMoveService computerMoveService;

    private Game game;
    private TableStateDTO tableState;

    @BeforeEach
    void setUp() {
        game = prepareSinglePlayerGame();
        tableState = prepareTableState();
    }

    @Test
    void testMakeComputerMoveForEasyLevelNotMatched() {
        Mockito.when(cardService.getRandomCard(tableState.getComputerCards())).thenReturn(new Card(Suit.SPADES, Rank.THREE));

        computerMoveService.makeComputerMove(game, tableState);

        Mockito.verify(cardService, Mockito.times(1)).getRandomCard(tableState.getComputerCards());
        assertEquals(3, tableState.getFaceUpCards().size());
        assertEquals(3, tableState.getComputerCards().size());
    }

    @Test
    void testMakeComputerMoveForEasyLevelMatched() {
        Mockito.when(cardService.getRandomCard(tableState.getComputerCards())).thenReturn(new Card(Suit.DIAMONDS, Rank.TEN));

        computerMoveService.makeComputerMove(game, tableState);

        Mockito.verify(cardService, Mockito.times(1)).getRandomCard(tableState.getComputerCards());
        assertEquals(0, tableState.getFaceUpCards().size());
        assertEquals(3, tableState.getComputerCards().size());
        assertEquals(3, tableState.getCapturedCardsNumberByComputer());
        assertEquals(4, tableState.getComputerLevelScore());
    }

    @Test
    void testMakeComputerMoveForEasyLevelNoFaceUpCards() {
        tableState.setFaceUpCards(new ArrayList<>());
        Mockito.when(cardService.getRandomCard(tableState.getComputerCards())).thenReturn(new Card(Suit.DIAMONDS, Rank.TEN));

        computerMoveService.makeComputerMove(game, tableState);

        Mockito.verify(cardService, Mockito.times(1)).getRandomCard(tableState.getComputerCards());
        assertEquals(1, tableState.getFaceUpCards().size());
        assertEquals(3, tableState.getComputerCards().size());
    }

    @Test
    void testMakeComputerMoveForNormalLevelMatched() {
        game.setLevel(2);

        computerMoveService.makeComputerMove(game, tableState);

        assertEquals(0, tableState.getFaceUpCards().size());
        assertEquals(3, tableState.getComputerCards().size());
        assertEquals(3, tableState.getCapturedCardsNumberByComputer());
        assertEquals(4, tableState.getComputerLevelScore());
    }

    @Test
    void testMakeComputerMoveForNormalLevelPlayJack() {
        game.setLevel(2);
        tableState.getComputerCards().remove(new Card(Suit.DIAMONDS, Rank.TEN));
        tableState.getComputerCards().add(new Card(Suit.CLUBS, Rank.NINE));

        computerMoveService.makeComputerMove(game, tableState);

        assertEquals(0, tableState.getFaceUpCards().size());
        assertEquals(3, tableState.getComputerCards().size());
        assertEquals(3, tableState.getCapturedCardsNumberByComputer());
        assertEquals(2, tableState.getComputerLevelScore());
    }

    @Test
    void testMakeComputerMoveForNormalLevelPlayRandom() {
        game.setLevel(2);
        tableState.getComputerCards().remove(new Card(Suit.DIAMONDS, Rank.TEN));
        tableState.getComputerCards().remove(new Card(Suit.CLUBS, Rank.JACK));
        tableState.getComputerCards().add(new Card(Suit.CLUBS, Rank.NINE));
        tableState.getComputerCards().add(new Card(Suit.DIAMONDS, Rank.SEVEN));

        Mockito.when(cardService.getRandomCard(tableState.getComputerCards())).thenReturn(new Card(Suit.DIAMONDS, Rank.SEVEN));

        computerMoveService.makeComputerMove(game, tableState);

        Mockito.verify(cardService, Mockito.times(1)).getRandomCard(tableState.getComputerCards());

        assertEquals(3, tableState.getFaceUpCards().size());
        assertEquals(3, tableState.getComputerCards().size());
        assertEquals(0, tableState.getCapturedCardsNumberByComputer());
        assertEquals(0, tableState.getComputerLevelScore());
    }

    @Test
    void testMakeComputerMoveForHardLevelNoFaceUpCards() {
        game.setLevel(3);
        tableState.setFaceUpCards(new ArrayList<>());

        Set<Card> cardSet = new HashSet<>(tableState.getComputerCards());
        cardSet.remove(new Card(Suit.CLUBS, Rank.JACK));
        Mockito.when(cardService.getRandomCard(cardSet)).thenReturn(new Card(Suit.SPADES, Rank.THREE));

        computerMoveService.makeComputerMove(game, tableState);

        Mockito.verify(cardService, Mockito.times(1)).getRandomCard(cardSet);

        assertEquals(1, tableState.getFaceUpCards().size());
        assertEquals(3, tableState.getComputerCards().size());
    }

    @Test
    void testMakeComputerMoveForHardLevelOneFaceUpCardDoublePisti() {
        game.setLevel(3);
        tableState.getFaceUpCards().remove(new Card(Suit.SPADES, Rank.ACE));
        tableState.getFaceUpCards().remove(new Card(Suit.HEARTS, Rank.TEN));
        tableState.getFaceUpCards().add(new Card(Suit.SPADES, Rank.JACK));

        computerMoveService.makeComputerMove(game, tableState);

        assertEquals(0, tableState.getFaceUpCards().size());
        assertEquals(3, tableState.getComputerCards().size());
        assertEquals(2, tableState.getCapturedCardsNumberByComputer());
        assertEquals(22, tableState.getComputerLevelScore());
    }

    @Test
    void testMakeComputerMoveForHardLevelOneFaceUpCardPisti() {
        game.setLevel(3);
        tableState.getFaceUpCards().remove(new Card(Suit.SPADES, Rank.ACE));

        computerMoveService.makeComputerMove(game, tableState);

        assertEquals(0, tableState.getFaceUpCards().size());
        assertEquals(3, tableState.getComputerCards().size());
        assertEquals(2, tableState.getCapturedCardsNumberByComputer());
        assertEquals(13, tableState.getComputerLevelScore());
    }

    @Test
    void testMakeComputerMoveForHardLevelOneFaceUpCardHighestPointYield() {
        game.setLevel(3);
        tableState.getFaceUpCards().remove(new Card(Suit.SPADES, Rank.ACE));
        tableState.getComputerCards().remove(new Card(Suit.CLUBS, Rank.JACK));
        tableState.getComputerCards().add(new Card(Suit.CLUBS, Rank.TEN));

        Set<Card> matchingCards = new HashSet<>();
        matchingCards.add(new Card(Suit.DIAMONDS, Rank.TEN));
        matchingCards.add( new Card(Suit.CLUBS, Rank.TEN));
        Mockito.when(cardService.findHighestScoredCard(matchingCards)).thenReturn(new Card(Suit.DIAMONDS, Rank.TEN));

        computerMoveService.makeComputerMove(game, tableState);

        Mockito.verify(cardService, Mockito.times(1)).findHighestScoredCard(matchingCards);

        assertEquals(0, tableState.getFaceUpCards().size());
        assertEquals(3, tableState.getComputerCards().size());
        assertEquals(2, tableState.getCapturedCardsNumberByComputer());
        assertEquals(13, tableState.getComputerLevelScore());
    }

    @Test
    void testMakeComputerMoveForHardLevelMultipleFaceUpCardYieldHighest() {
        game.setLevel(3);
        tableState.getComputerCards().add(new Card(Suit.CLUBS, Rank.TEN));

        Set<Card> matchingCards = new HashSet<>();
        matchingCards.add(new Card(Suit.DIAMONDS, Rank.TEN));
        matchingCards.add( new Card(Suit.CLUBS, Rank.TEN));
        Mockito.when(cardService.findHighestScoredCard(matchingCards)).thenReturn(new Card(Suit.DIAMONDS, Rank.TEN));

        computerMoveService.makeComputerMove(game, tableState);

        Mockito.verify(cardService, Mockito.times(1)).findHighestScoredCard(matchingCards);

        assertEquals(0, tableState.getFaceUpCards().size());
        assertEquals(4, tableState.getComputerCards().size());
        assertEquals(3, tableState.getCapturedCardsNumberByComputer());
        assertEquals(4, tableState.getComputerLevelScore());
    }

    @Test
    void testMakeComputerMoveForHardLevelMultipleFaceUpCardMatching() {
        game.setLevel(3);

        computerMoveService.makeComputerMove(game, tableState);

        assertEquals(0, tableState.getFaceUpCards().size());
        assertEquals(3, tableState.getComputerCards().size());
        assertEquals(3, tableState.getCapturedCardsNumberByComputer());
        assertEquals(4, tableState.getComputerLevelScore());
    }

    @Test
    void testMakeComputerMoveForHardLevelMultipleFaceUpCardPickJack() {
        game.setLevel(3);
        tableState.getComputerCards().remove(new Card(Suit.DIAMONDS, Rank.TEN));

        computerMoveService.makeComputerMove(game, tableState);

        assertEquals(0, tableState.getFaceUpCards().size());
        assertEquals(2, tableState.getComputerCards().size());
        assertEquals(3, tableState.getCapturedCardsNumberByComputer());
        assertEquals(2, tableState.getComputerLevelScore());
    }

    @Test
    void testMakeComputerMoveForHardLevelMultipleFaceUpCardPlayRandom() {
        game.setLevel(3);
        tableState.getComputerCards().remove(new Card(Suit.DIAMONDS, Rank.TEN));
        tableState.getComputerCards().remove(new Card(Suit.CLUBS, Rank.JACK));
        tableState.getComputerCards().add(new Card(Suit.CLUBS, Rank.NINE));
        tableState.getComputerCards().add(new Card(Suit.DIAMONDS, Rank.SEVEN));

        Mockito.when(cardService.getRandomCard(tableState.getComputerCards())).thenReturn(new Card(Suit.SPADES, Rank.THREE));

        computerMoveService.makeComputerMove(game, tableState);

        Mockito.verify(cardService, Mockito.times(1)).getRandomCard(tableState.getComputerCards());

        assertEquals(3, tableState.getFaceUpCards().size());
        assertEquals(3, tableState.getComputerCards().size());
        assertEquals(0, tableState.getCapturedCardsNumberByComputer());
        assertEquals(0, tableState.getComputerLevelScore());
    }

    @Test
    void testMakeComputerMoveGameNull() {
        assertThrows(GameNotFoundException.class, () -> computerMoveService.makeComputerMove(null, tableState));
    }

    @Test
    void testMakeComputerMoveCouldNotPickACard() {
        Mockito.when(cardService.getRandomCard(tableState.getComputerCards())).thenReturn(null);
        assertThrows(CouldNotPickACardException.class, () -> computerMoveService.makeComputerMove(game, tableState));
    }

    private TableStateDTO prepareTableState() {
        tableState = new TableStateDTO();

        Set<Card> player1Cards = new HashSet<>();
        tableState.setPlayer1Cards(player1Cards);

        List<Card> faceUpCards = new ArrayList<>();
        faceUpCards.add(new Card(Suit.SPADES, Rank.ACE));
        faceUpCards.add(new Card(Suit.HEARTS, Rank.TEN));
        tableState.setFaceUpCards(faceUpCards);

        Set<Card> faceDownCards = new HashSet<>();
        tableState.setFaceDownCards(faceDownCards);

        Set<Card> computerCards = new HashSet<>();
        computerCards.add(new Card(Suit.DIAMONDS, Rank.TEN));
        computerCards.add(new Card(Suit.CLUBS, Rank.JACK));
        computerCards.add(new Card(Suit.SPADES, Rank.THREE));
        computerCards.add(new Card(Suit.HEARTS, Rank.KING));
        tableState.setComputerCards(computerCards);

        tableState.setCapturedCardsNumberByPlayer1(6);

        tableState.setComputerLevelScore(0);

        tableState.setCapturedCardsNumberByComputer(0);

        return tableState;
    }

    private Game prepareSinglePlayerGame() {
        game = new Game();
        game.setId(1L);
        game.setLevel(1);

        Player player = new Player();
        player.setUsername("user");

        game.setPlayer1(player);
        game.setPlayer2(null);
        game.getPlayer1().setCumulativeScore(0);

        return game;
    }
}