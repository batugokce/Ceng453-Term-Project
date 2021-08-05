package tr.metu.ceng.construction.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
import tr.metu.ceng.construction.server.repository.GameRepository;
import tr.metu.ceng.construction.server.repository.PlayerRepository;

import java.util.Set;


/**
 * Responsible for handling player's actions.
 */
@Service
@RequiredArgsConstructor
public class PlayCardService {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final SinglePlayerService singlePlayerService;
    private final CommonService commonService;

    /**
     * Takes action of the player, process that action and make some changes on the table.
     * If it is a single player game, it calls corresponding method from {@code SinglePlayerService}.
     * Otherwise, it calls another method from {@code MultiPlayerService}.
     *
     * @param action action taken by the player
     * @return state of the table
     * @throws PlayerNotFoundException when the player is not found
     * @throws GameNotFoundException when the game is not found
     * @throws PlayerNotAuthorizedException when the player is not authorized, token is incorrect
     * @throws CardNotBelongToPlayerException when the player does not have the card
     */
    public TableStateStagesDTO playCard(PlayerActionDTO action) {
        TableStateDTO tableState = commonService.checkCommonConstraints(action);

        Game game = gameRepository.findById(action.getGameId()).orElse(null);
        // parse required fields such as table state, player cards etc. from the player's action.
        Set<Card> playerCards = action.getPlayerNo().equals(PlayerType.PLAYER1) ? tableState.getPlayer1Cards() : tableState.getPlayer2Cards();
        Card cardToPlay = new Card(action.getSuit(), action.getRank());

        // check if the game is online or not
        if (game != null && !game.isMultiPlayer()) {
            return singlePlayerService.processPlayerMove(game, playerCards, cardToPlay, action);
        }

        // return the state of the table including face-up cards, face-down cards, scores etc.
        return new TableStateStagesDTO(tableState, tableState);
    }

}
