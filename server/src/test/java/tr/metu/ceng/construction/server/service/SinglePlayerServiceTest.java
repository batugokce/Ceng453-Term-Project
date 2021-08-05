package tr.metu.ceng.construction.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.metu.ceng.construction.server.DTO.PlayerActionDTO;
import tr.metu.ceng.construction.server.DTO.TableStateDTO;
import tr.metu.ceng.construction.server.DTO.TableStateStagesDTO;
import tr.metu.ceng.construction.server.enums.EventType;
import tr.metu.ceng.construction.server.enums.PlayerType;
import tr.metu.ceng.construction.server.model.Card;
import tr.metu.ceng.construction.server.model.Game;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.repository.GameRepository;
import tr.metu.ceng.construction.server.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static tr.metu.ceng.construction.server.enums.Rank.*;
import static tr.metu.ceng.construction.server.enums.Suit.*;
import static tr.metu.ceng.construction.server.common.CommonConstants.*;

@ExtendWith(MockitoExtension.class)
class SinglePlayerServiceTest {

    @Mock
    private GameService gameService;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private ComputerMoveService computerMoveService;
    @Mock
    private ScoreService scoreService;
    @InjectMocks
    private SinglePlayerService singlePlayerService;

    private PlayerActionDTO action;
    private TableStateDTO tableState;
    private Game game;
    private Set<Card> playerCards;
    Card cardToPlay = new Card(CLUBS, TWO);

    @BeforeEach
    void setUp() {
        // prepare environment before each test
        action = new PlayerActionDTO();
        tableState = new TableStateDTO();
        Player player = new Player();
        game = new Game();
        action.setPlayerId(1L);
        action.setGameId(1L);
        action.setToken("token1");
        action.setPlayerNo(PlayerType.PLAYER1);
        action.setTableState(tableState);
        player.setCumulativeScore(0);
        tableState.setLevel(1);
        tableState.setComputerCards(Set.of(new Card(DIAMONDS, EIGHT)));
        tableState.setPlayer1LevelScore(0);
        tableState.setCapturedCardsNumberByPlayer1(0);
        tableState.setCapturedCardsNumberByPlayer2(0);
        tableState.setCapturedCardsNumberByComputer(0);
        game.setLevel(1);
        game.setPlayer1(player);
        playerCards = new HashSet<>();
        playerCards.add(cardToPlay);
        tableState.setPlayer1Cards(playerCards);
    }


    @Test
    void processPlayerMoveTestEmptyFaceUp() {
        tableState.setFaceUpCards(new ArrayList<>());

        singlePlayerService.processPlayerMove(game, playerCards, cardToPlay, action);

        assertTrue(tableState.getFaceUpCards().contains(cardToPlay));
        assertFalse(playerCards.contains(cardToPlay));
        verify(computerMoveService).makeComputerMove(any(), any());
    }

    @Test
    void processPlayerMoveTestNonEmptyFaceUp() {
        List<Card> faceUpCards = new ArrayList<>();
        faceUpCards.add(new Card(CLUBS, FOUR));
        tableState.setFaceUpCards(faceUpCards);

        singlePlayerService.processPlayerMove(game, playerCards, cardToPlay, action);

        assertTrue(playerCards.isEmpty());
        assertEquals(2, tableState.getFaceUpCards().size());
        verify(computerMoveService).makeComputerMove(any(), any());
    }

    @Test
    void processPlayerMoveTestCapturedCard() {
        List<Card> faceUpCards = new ArrayList<>();
        faceUpCards.add(new Card(DIAMONDS, cardToPlay.getRank()));
        tableState.setFaceUpCards(faceUpCards);

        singlePlayerService.processPlayerMove(game, playerCards, cardToPlay, action);

        assertTrue(playerCards.isEmpty());
        assertTrue(tableState.getFaceUpCards().isEmpty());
        assertNotEquals(0, tableState.getPlayer1LevelScore());
        verify(computerMoveService).makeComputerMove(any(), any());
    }

    @Test
    void processPlayerMoveTestCapturedCardDoublePisti() {
        List<Card> faceUpCards = new ArrayList<>();
        faceUpCards.add(new Card(DIAMONDS, JACK));
        tableState.setFaceUpCards(faceUpCards);

        singlePlayerService.processPlayerMove(game, playerCards, new Card(CLUBS, JACK), action);

        assertEquals(1, playerCards.size());
        assertTrue(tableState.getFaceUpCards().isEmpty());
        assertEquals(22, tableState.getPlayer1LevelScore());
        verify(computerMoveService).makeComputerMove(any(), any());
    }

    @Test
    void processPlayerMoveTestDealingCards() {
        tableState.setComputerCards(new HashSet<>());
        tableState.setFaceUpCards(new ArrayList<>());
        tableState.setFaceDownCards(Set.of(new Card(SPADES, TWO), new Card(SPADES, NINE)));

        singlePlayerService.processPlayerMove(game, playerCards, cardToPlay, action);

        verify(computerMoveService).makeComputerMove(any(), any());
        verify(gameService).dealCards(any(), any());
    }

    @Test
    void processPlayerMoveTestContinuingGame() {
        tableState.setComputerCards(new HashSet<>());
        tableState.setFaceUpCards(new ArrayList<>());
        tableState.setFaceDownCards(new HashSet<>());
        tableState.setComputerLevelScore(20);
        tableState.setPlayer1LevelScore(20);

        singlePlayerService.processPlayerMove(game, playerCards, cardToPlay, action);

        verify(computerMoveService).makeComputerMove(any(), any());
        verify(gameService).resetCards(any(), any());
    }

    @Test
    void processPlayerMoveTestComputerWon() {
        tableState.setComputerCards(new HashSet<>());
        tableState.setFaceUpCards(new ArrayList<>());
        tableState.setFaceDownCards(new HashSet<>());
        tableState.setComputerLevelScore(PASSING_SCORE);
        tableState.setPlayer1LevelScore(20);

        TableStateStagesDTO stages = singlePlayerService.processPlayerMove(game, playerCards, cardToPlay, action);

        verify(computerMoveService).makeComputerMove(any(), any());
        assertEquals(EventType.COMPUTER_WIN, stages.getFinalTableState().getEventType());
    }

    @Test
    void processPlayerMoveTestPlayerWon() {
        tableState.setComputerCards(new HashSet<>());
        tableState.setFaceUpCards(new ArrayList<>());
        tableState.setFaceDownCards(new HashSet<>());
        tableState.setComputerLevelScore(20);
        tableState.setPlayer1LevelScore(PASSING_SCORE);

        singlePlayerService.processPlayerMove(game, playerCards, cardToPlay, action);

        verify(computerMoveService).makeComputerMove(any(), any());
        verify(gameService).resetCards(any(), any());
        verify(gameRepository).save(game);
        assertEquals(2, game.getLevel());
    }
}