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
import tr.metu.ceng.construction.server.exception.FaceUpCardsNotValidException;
import tr.metu.ceng.construction.server.exception.NotBluffingPistiException;
import tr.metu.ceng.construction.server.model.Card;
import tr.metu.ceng.construction.server.model.Game;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.repository.GameRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static tr.metu.ceng.construction.server.enums.Rank.*;
import static tr.metu.ceng.construction.server.enums.Suit.*;

@ExtendWith(MockitoExtension.class)
class BluffingPistiServiceTest {

    @Mock
    ComputerMoveService computerMoveService;
    @Mock
    SinglePlayerService singlePlayerService;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private CommonService commonService;
    @InjectMocks
    private BluffingPistiService bluffingPistiService;

    private PlayerActionDTO action;
    private TableStateDTO tableState;
    private Game game;

    @BeforeEach
    void setUp() {
        action = new PlayerActionDTO();
        tableState = new TableStateDTO();
        Player player = new Player();
        game = new Game();
        game.setLevel(3);
        action.setPlayerId(1L);
        action.setGameId(1L);
        action.setToken("token1");
        action.setPlayerNo(PlayerType.PLAYER1);
        player.setToken("token1");
        action.setSuit(DIAMONDS);
        action.setRank(ACE);
        List<Card> faceUpCards = new ArrayList<>();
        faceUpCards.add(new Card(DIAMONDS, TWO));
        tableState.setFaceUpCards(faceUpCards);
        action.setTableState(tableState);
        tableState.setComputerLevelScore(0);
        tableState.setPlayer1LevelScore(0);
        tableState.setLevel(game.getLevel());
        tableState.setComputerCards(Set.of(new Card(CLUBS,FOUR)));
    }


    @Test
    void bluffCardTestNotBluffing() {
        game.setLevel(2);
        when(commonService.checkCommonConstraints(any())).thenReturn(tableState);
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        assertThrows(NotBluffingPistiException.class, () -> bluffingPistiService.bluffCard(action));
    }

    @Test
    void bluffCardTestNotFaceUpCard() {
        tableState.setFaceUpCards(List.of(new Card(DIAMONDS, TWO), new Card(CLUBS, THREE)));

        when(commonService.checkCommonConstraints(any())).thenReturn(tableState);
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        assertThrows(FaceUpCardsNotValidException.class, () -> bluffingPistiService.bluffCard(action));
    }

    @Test
    void bluffCardTestSuccessChallengedAndNotPisti() {
        Set<Card> cards = new HashSet<>();
        cards.add(new Card(DIAMONDS, ACE));
        tableState.setPlayer1Cards(cards);
        when(commonService.checkCommonConstraints(any())).thenReturn(tableState);
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        TableStateStagesDTO stages = bluffingPistiService.bluffCard(action);
        assertTrue(isEitherClaimTrueOrNotChallenged(stages.getFinalTableState().getEventType()));
    }

    @Test
    void bluffCardTestSuccessChallengedAndPisti() {
        List<Card> faceUpCards = new ArrayList<>();
        faceUpCards.add(new Card(SPADES, TWO));
        tableState.setFaceUpCards(faceUpCards);
        action.setTableState(tableState);
        action.setSuit(SPADES);
        action.setRank(TWO);

        Set<Card> cards = new HashSet<>();
        cards.add(new Card(SPADES, TWO));
        tableState.setPlayer1Cards(cards);
        when(commonService.checkCommonConstraints(any())).thenReturn(tableState);
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        TableStateStagesDTO stages = bluffingPistiService.bluffCard(action);
        assertTrue(isEitherClaimFalseOrNotChallenged(stages.getFinalTableState().getEventType()));
    }

    private boolean isEitherClaimTrueOrNotChallenged(EventType eventType) {
        return eventType.equals(EventType.BLUFF_CHALLENGED_AND_NOT_PISTI) || eventType.equals(EventType.BLUFF_NOT_CHALLENGED);
    }

    private boolean isEitherClaimFalseOrNotChallenged(EventType eventType) {
        return eventType.equals(EventType.BLUFF_CHALLENGED_AND_PISTI) || eventType.equals(EventType.BLUFF_NOT_CHALLENGED);
    }
}