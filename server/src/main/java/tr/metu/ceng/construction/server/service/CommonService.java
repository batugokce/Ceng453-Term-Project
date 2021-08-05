package tr.metu.ceng.construction.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

import java.util.Set;

import static tr.metu.ceng.construction.server.common.Messages.*;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    /**
     * Checks null conditions of game, player, or token when playing a card or making bluffing in the game.
     *
     * @param action the entity that includes card played, game id, token of the player.
     * @return status of the incoming table.
     */
    public TableStateDTO checkCommonConstraints(PlayerActionDTO action) {
        Player player = playerRepository.findById(action.getPlayerId()).orElse(null);
        Game game = gameRepository.findById(action.getGameId()).orElse(null);

        if (player == null) { // player does not exist in the database
            throw new PlayerNotFoundException(PLAYER_NOT_EXIST);
        } else if (game == null) { // game does not exist in the database
            throw new GameNotFoundException(GAME_NOT_EXIST);
        } else if (!action.getToken().equals(player.getToken())) { // token mismatch, unauthorized request
            throw new PlayerNotAuthorizedException(PLAYER_NOT_AUTHORIZED);
        }

        // parse required fields such as table state, player cards etc. from the player's action.
        TableStateDTO tableState = action.getTableState();
        Set<Card> playerCards = action.getPlayerNo().equals(PlayerType.PLAYER1) ? tableState.getPlayer1Cards() : tableState.getPlayer2Cards();
        Card cardToPlay = new Card(action.getSuit(), action.getRank());

        if (!playerCards.contains(cardToPlay)) {
            // if player does not have that card, throw an exception
            throw new CardNotBelongToPlayerException(CARD_NOT_BELONG_TO_PLAYER);
        }
        return tableState;
    }
}
