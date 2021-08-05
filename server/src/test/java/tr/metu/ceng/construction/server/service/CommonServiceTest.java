package tr.metu.ceng.construction.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.metu.ceng.construction.server.DTO.PlayerActionDTO;
import tr.metu.ceng.construction.server.DTO.TableStateDTO;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static tr.metu.ceng.construction.server.enums.Rank.THREE;
import static tr.metu.ceng.construction.server.enums.Rank.TWO;
import static tr.metu.ceng.construction.server.enums.Suit.CLUBS;

@ExtendWith(MockitoExtension.class)
class CommonServiceTest {

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private GameRepository gameRepository;
    @InjectMocks
    private CommonService commonService;

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
    void checkCommonConstraintsTestNullPlayer() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        assertThrows(PlayerNotFoundException.class, () -> commonService.checkCommonConstraints(action));
    }

    @Test
    void checkCommonConstraintsTestNullGame() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));
        when(gameRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> commonService.checkCommonConstraints(action));
    }

    @Test
    void checkCommonConstraintsTestIncorrectToken() {
        action.setToken("token2");
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        assertThrows(PlayerNotAuthorizedException.class, () -> commonService.checkCommonConstraints(action));
    }

    @Test
    void checkCommonConstraintsTestCardDoesNotExist() {
        tableState.setPlayer1Cards(Set.of(new Card(CLUBS, TWO)));
        action.setTableState(tableState);
        action.setSuit(CLUBS);
        action.setRank(THREE);
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        assertThrows(CardNotBelongToPlayerException.class, () -> commonService.checkCommonConstraints(action));
    }
}