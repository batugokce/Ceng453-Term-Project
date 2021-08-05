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
import tr.metu.ceng.construction.server.enums.PlayerType;
import tr.metu.ceng.construction.server.exception.CardNotBelongToPlayerException;
import tr.metu.ceng.construction.server.exception.GameNotFoundException;
import tr.metu.ceng.construction.server.exception.PlayerNotAuthorizedException;
import tr.metu.ceng.construction.server.exception.PlayerNotFoundException;
import tr.metu.ceng.construction.server.model.Card;
import tr.metu.ceng.construction.server.model.Game;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.repository.GameRepository;
import tr.metu.ceng.construction.server.repository.PlayerRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static tr.metu.ceng.construction.server.enums.Rank.*;
import static tr.metu.ceng.construction.server.enums.Suit.*;

@ExtendWith(MockitoExtension.class)
class PlayCardServiceTest {

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private SinglePlayerService singlePlayerService;
    @Mock
    private CommonService commonService;
    @InjectMocks
    private PlayCardService playCardService;

    private PlayerActionDTO action;
    private TableStateDTO tableState;
    private Player player;
    private Game game;

    @BeforeEach
    void setUp() {
        action = new PlayerActionDTO();
        tableState = new TableStateDTO();
        player = new Player();
        game = new Game();
        game.setLevel(1);
        action.setPlayerId(1L);
        action.setGameId(1L);
        action.setToken("token1");
        action.setPlayerNo(PlayerType.PLAYER1);
        player.setToken("token1");
    }

    @Test
    void playCardTestSinglePlayer() {
        tableState.setPlayer1Cards(Set.of(new Card(CLUBS, TWO)));
        action.setTableState(tableState);
        action.setSuit(CLUBS);
        action.setRank(TWO);
        game.setLevel(1);
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        when(commonService.checkCommonConstraints(any())).thenReturn(tableState);

        playCardService.playCard(action);

        verify(singlePlayerService).processPlayerMove(any(), anySet(), any(), any());
    }
}