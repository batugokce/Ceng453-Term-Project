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
import tr.metu.ceng.construction.server.exception.GameNotFoundException;
import tr.metu.ceng.construction.server.exception.GameNotMultiPlayerException;
import tr.metu.ceng.construction.server.exception.PlayerNotAuthorizedException;
import tr.metu.ceng.construction.server.exception.PlayerNotFoundException;
import tr.metu.ceng.construction.server.model.Card;
import tr.metu.ceng.construction.server.model.Game;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.repository.GameRepository;
import tr.metu.ceng.construction.server.repository.PlayerRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static tr.metu.ceng.construction.server.common.CommonConstants.PASSING_SCORE;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private CardService cardService;

    @InjectMocks
    private GameService gameService;

    private Player player1, player2;

    private static final String USERNAME1 = "user1";
    private static final String PASSWORD1 = "password1";
    private static final String TOKEN = "token";
    private static final String USERNAME2 = "user2";
    private static final String PASSWORD2 = "password2";

    @BeforeEach
    void setUp() {
        player1 = new Player();
        player1.setUsername(USERNAME1);
        player1.setPassword(PASSWORD1);
        player1.setToken(TOKEN);

        player2 = new Player();
        player2.setUsername(USERNAME2);
        player2.setPassword(PASSWORD2);
    }

    @Test
    void testCreateGameSuccess() {
        Mockito.when(playerService.findByUsername(USERNAME1)).thenReturn(player1);

        Game game = gameService.createGame(USERNAME1);

        Mockito.verify(gameRepository).save(game);
        Mockito.verify(playerRepository).save(player1);

        assertEquals(1, game.getLevel());
        assertEquals(player1, game.getPlayer1());
        assertNull(game.getPlayer2());
        assertEquals(0, player1.getCumulativeScore());
    }

    @Test
    void testCreateGamePlayerNull() {
        Mockito.when(playerService.findByUsername(USERNAME1)).thenReturn(null);

        assertThrows(PlayerNotFoundException.class, () -> gameService.createGame(USERNAME1));
    }

    @Test
    void testCreateGameTokenNull() {
        player1.setToken(null);
        Mockito.when(playerService.findByUsername(USERNAME1)).thenReturn(player1);

        assertThrows(PlayerNotAuthorizedException.class, () -> gameService.createGame(USERNAME1));
    }

    @Test
    void testDealCardsSinglePlayerGameSuccess() {
        Game game = prepareSinglePlayerGame();
        TableStateDTO tableState = prepareTableState();

        List<Card> cardList = new ArrayList<>(tableState.getFaceDownCards());
        Mockito.when(cardService.getRandomCard(tableState.getFaceDownCards())).thenReturn(cardList.get(0), cardList.get(1), cardList.get(2),
                cardList.get(3), cardList.get(4), cardList.get(5), cardList.get(6), cardList.get(7));

        gameService.dealCards(game, tableState);

        Mockito.verify(cardService, Mockito.times(8)).getRandomCard(tableState.getFaceDownCards());
        assertEquals(4, tableState.getPlayer1Cards().size());
        assertEquals(4, tableState.getComputerCards().size());
        assertEquals(4, tableState.getFaceDownCards().size());
    }

    @Test
    void testDealCardsMultiPlayerGameSuccess() {
        Game game = prepareMultiPlayerGame();
        TableStateDTO tableState = prepareTableState();

        List<Card> cardList = new ArrayList<>(tableState.getFaceDownCards());
        Mockito.when(cardService.getRandomCard(tableState.getFaceDownCards())).thenReturn(cardList.get(0), cardList.get(1), cardList.get(2),
                cardList.get(3), cardList.get(4), cardList.get(5), cardList.get(6), cardList.get(7));

        gameService.dealCards(game, tableState);

        Mockito.verify(cardService, Mockito.times(8)).getRandomCard(tableState.getFaceDownCards());
        assertEquals(4, tableState.getPlayer1Cards().size());
        assertEquals(4, tableState.getPlayer2Cards().size());
        assertEquals(4, tableState.getFaceDownCards().size());
    }

    @Test
    void testDealCardsMultiPlayerGamePlayer2Null() {
        Game game = prepareMultiPlayerGame();
        game.setPlayer2(null);
        TableStateDTO tableState = prepareTableState();

        List<Card> cardList = new ArrayList<>(tableState.getFaceDownCards());
        Mockito.when(cardService.getRandomCard(tableState.getFaceDownCards())).thenReturn(cardList.get(0), cardList.get(1), cardList.get(2),
                cardList.get(3));

        assertThrows(PlayerNotFoundException.class, () -> gameService.dealCards(game, tableState));
    }

    @Test
    void testDealCardsMultiPlayerGameNull() {
        TableStateDTO tableState = prepareTableState();

        assertThrows(GameNotFoundException.class, () -> gameService.dealCards(null, tableState));
    }

    @Test
    void testPutRandomCardsToCenter() {
        TableStateDTO tableState = prepareTableState();

        List<Card> cardList = new ArrayList<>(tableState.getFaceDownCards());
        Mockito.when(cardService.getRandomCard(tableState.getFaceDownCards())).thenReturn(cardList.get(0), cardList.get(1), cardList.get(2),
                cardList.get(3));

        gameService.putRandomCardsToCenter(tableState);

        Mockito.verify(cardService, Mockito.times(4)).getRandomCard(tableState.getFaceDownCards());
        assertEquals(4, tableState.getFaceUpCards().size());
        assertEquals(8, tableState.getFaceDownCards().size());
    }

    @Test
    void testResetCardsSinglePlayerSuccess() {
        Game game = prepareSinglePlayerGame();
        TableStateDTO tableState = prepareTableState();

        List<Card> cardList = new ArrayList<>(prepareDeck());
        doReturn(cardList.get(0), cardList.get(1), cardList.get(2),
                cardList.get(3), cardList.get(4), cardList.get(5), cardList.get(6), cardList.get(7),
                cardList.get(8), cardList.get(9), cardList.get(10), cardList.get(11)).when(cardService).getRandomCard(any());

        TableStateDTO updatedTableState = gameService.resetCards(game, tableState);

        Mockito.verify(cardService, Mockito.times(12)).getRandomCard(any());
        assertEquals(40, updatedTableState.getFaceDownCards().size());
        assertEquals(4, updatedTableState.getPlayer1Cards().size());
        assertEquals(4, updatedTableState.getComputerCards().size());
        assertEquals(4, updatedTableState.getFaceUpCards().size());
        assertEquals(0, updatedTableState.getCapturedCardsNumberByPlayer1());

    }

    @Test
    void testResetCardsMultiPlayerSuccess() {
        Game game = prepareMultiPlayerGame();
        TableStateDTO tableState = prepareTableState();

        List<Card> cardList = new ArrayList<>(prepareDeck());
        doReturn(cardList.get(0), cardList.get(1), cardList.get(2),
                cardList.get(3), cardList.get(4), cardList.get(5), cardList.get(6), cardList.get(7),
                cardList.get(8), cardList.get(9), cardList.get(10), cardList.get(11)).when(cardService).getRandomCard(any());

        TableStateDTO updatedTableState = gameService.resetCards(game, tableState);

        Mockito.verify(cardService, Mockito.times(12)).getRandomCard(any());
        assertEquals(40, updatedTableState.getFaceDownCards().size());
        assertEquals(4, updatedTableState.getPlayer1Cards().size());
        assertEquals(4, updatedTableState.getPlayer2Cards().size());
        assertEquals(4, updatedTableState.getFaceUpCards().size());
        assertEquals(0, updatedTableState.getCapturedCardsNumberByPlayer1());

    }

    @Test
    void testResetCardsGameNull() {
        TableStateDTO tableState = prepareTableState();

        assertThrows(GameNotFoundException.class, () -> gameService.resetCards(null, tableState));
    }

    @Test
    void testResetCardsPlayer1Null() {
        Game game = prepareSinglePlayerGame();
        game.setPlayer1(null);
        TableStateDTO tableState = prepareTableState();

        assertThrows(PlayerNotFoundException.class, () -> gameService.resetCards(game, tableState));
    }

    @Test
    void testResetCardsPlayer2Null() {
        Game game = prepareMultiPlayerGame();
        game.setPlayer2(null);
        TableStateDTO tableState = prepareTableState();

        assertThrows(PlayerNotFoundException.class, () -> gameService.resetCards(game, tableState));
    }

    @Test
    void testStartGame() {
        Mockito.when(playerService.findByUsername(USERNAME1)).thenReturn(player1);

        List<Card> cardList = new ArrayList<>(prepareDeck());
        doReturn(cardList.get(0), cardList.get(1), cardList.get(2),
                cardList.get(3), cardList.get(4), cardList.get(5), cardList.get(6), cardList.get(7),
                cardList.get(8), cardList.get(9), cardList.get(10), cardList.get(11)).when(cardService).getRandomCard(any());

        TableStateDTO tableState = gameService.startGame(USERNAME1);

        Mockito.verify(playerRepository).save(player1);
        Mockito.verify(cardService, Mockito.times(12)).getRandomCard(any());

        assertEquals(0, player1.getCumulativeScore());
        assertEquals(40, tableState.getFaceDownCards().size());
        assertEquals(4, tableState.getPlayer1Cards().size());
        assertEquals(4, tableState.getComputerCards().size());
        assertEquals(4, tableState.getFaceUpCards().size());
        assertEquals(0, tableState.getCapturedCardsNumberByPlayer1());
    }

    @Test
    void testCheatSuccess() {
        Game game = prepareSinglePlayerGame();
        int currentLevel = game.getLevel();
        TableStateDTO tableState = prepareTableState();
        tableState.setGameId(1L);

        Mockito.when(gameRepository.findById(1L)).thenReturn(java.util.Optional.of(game));

        List<Card> cardList = new ArrayList<>(prepareDeck());
        doReturn(cardList.get(0), cardList.get(1), cardList.get(2),
                cardList.get(3), cardList.get(4), cardList.get(5), cardList.get(6), cardList.get(7),
                cardList.get(8), cardList.get(9), cardList.get(10), cardList.get(11)).when(cardService).getRandomCard(any());

        TableStateDTO updatedTableState =  gameService.cheat(tableState);

        Mockito.verify(cardService, Mockito.times(12)).getRandomCard(any());
        assertEquals(40, updatedTableState.getFaceDownCards().size());
        assertEquals(4, updatedTableState.getPlayer1Cards().size());
        assertEquals(4, updatedTableState.getComputerCards().size());
        assertEquals(4, updatedTableState.getFaceUpCards().size());
        assertEquals(0, updatedTableState.getCapturedCardsNumberByPlayer1());
        assertEquals(currentLevel + 1, game.getLevel());
        assertEquals(PASSING_SCORE, game.getPlayer1().getCumulativeScore());
    }

    @Test
    void testCheatGameNotMultiPlayer() {
        Game game = prepareMultiPlayerGame();
        TableStateDTO tableState = prepareTableState();
        tableState.setGameId(1L);

        Mockito.when(gameRepository.findById(anyLong())).thenReturn(java.util.Optional.of(game));

        assertThrows(GameNotMultiPlayerException.class, () -> gameService.cheat(tableState));
    }

    @Test
    void testCheatGameNull() {
        TableStateDTO tableState = prepareTableState();
        tableState.setGameId(1L);

        Mockito.when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> gameService.cheat(tableState));
    }

    @Test
    void testCheatPlayer1Null() {
        Game game = prepareMultiPlayerGame();
        game.setPlayer1(null);
        TableStateDTO tableState = prepareTableState();
        tableState.setGameId(1L);

        Mockito.when(gameRepository.findById(1L)).thenReturn(java.util.Optional.of(game));

        assertThrows(PlayerNotFoundException.class, () -> gameService.cheat(tableState));
    }

    private TableStateDTO prepareTableState() {
        TableStateDTO tableState = new TableStateDTO();

        Set<Card> player1Cards = new HashSet<>();
        tableState.setPlayer1Cards(player1Cards);

        List<Card> faceUpCards = new ArrayList<>();
        tableState.setFaceUpCards(faceUpCards);

        Set<Card> faceDownCards = new HashSet<>();
        faceDownCards.add(new Card(Suit.SPADES, Rank.ACE));
        faceDownCards.add(new Card(Suit.HEARTS, Rank.QUEEN));
        faceDownCards.add(new Card(Suit.DIAMONDS, Rank.TEN));
        faceDownCards.add(new Card(Suit.CLUBS, Rank.JACK));
        faceDownCards.add(new Card(Suit.SPADES, Rank.THREE));
        faceDownCards.add(new Card(Suit.HEARTS, Rank.KING));
        faceDownCards.add(new Card(Suit.SPADES, Rank.SEVEN));
        faceDownCards.add(new Card(Suit.SPADES, Rank.NINE));
        faceDownCards.add(new Card(Suit.CLUBS, Rank.FOUR));
        faceDownCards.add(new Card(Suit.CLUBS, Rank.SIX));
        faceDownCards.add(new Card(Suit.DIAMONDS, Rank.JACK));
        faceDownCards.add(new Card(Suit.HEARTS, Rank.THREE));


        tableState.setFaceDownCards(faceDownCards);

        Set<Card> computerCards = new HashSet<>();
        tableState.setComputerCards(computerCards);

        Set<Card> player2Cards = new HashSet<>();
        tableState.setPlayer2Cards(player2Cards);

        tableState.setCapturedCardsNumberByPlayer1(6);

        return tableState;
    }

    private Game prepareSinglePlayerGame() {
        Game game = new Game();
        game.setId(1L);
        game.setLevel(1);
        game.setPlayer1(player1);
        game.setPlayer2(null);
        game.getPlayer1().setCumulativeScore(0);

        return game;
    }

    private Game prepareMultiPlayerGame() {
        Game game = new Game();
        game.setId(1L);
        game.setLevel(4);
        game.setPlayer1(player1);
        game.setPlayer2(player2);

        return game;
    }

    private Set<Card> prepareDeck() {
        Set<Card> deck = new HashSet<>();

        deck.addAll(prepareThirteenCardsOfSuit(Suit.CLUBS));
        deck.addAll(prepareThirteenCardsOfSuit(Suit.DIAMONDS));
        deck.addAll(prepareThirteenCardsOfSuit(Suit.HEARTS));
        deck.addAll(prepareThirteenCardsOfSuit(Suit.SPADES));

        return deck;
    }

    private Set<Card> prepareThirteenCardsOfSuit(Suit suit) {
        Set<Card> cards = new HashSet<>();

        for (Rank rank : Rank.values()) {
            cards.add(new Card(suit, rank));
        }

        return cards;
    }
}