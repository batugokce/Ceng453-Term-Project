package tr.metu.ceng.construction.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.metu.ceng.construction.server.DTO.PlayerActionDTO;
import tr.metu.ceng.construction.server.DTO.TableStateDTO;
import tr.metu.ceng.construction.server.DTO.TableStateStagesDTO;
import tr.metu.ceng.construction.server.enums.EventType;
import tr.metu.ceng.construction.server.enums.PlayerType;
import tr.metu.ceng.construction.server.enums.Rank;
import tr.metu.ceng.construction.server.exception.*;
import tr.metu.ceng.construction.server.model.Card;
import tr.metu.ceng.construction.server.model.Game;
import tr.metu.ceng.construction.server.repository.GameRepository;

import java.util.*;

import static tr.metu.ceng.construction.server.common.CommonConstants.*;
import static tr.metu.ceng.construction.server.common.Messages.*;
import static tr.metu.ceng.construction.server.utils.GameUtilities.areCardsFinished;
import static tr.metu.ceng.construction.server.utils.GameUtilities.calculatePointToAdd;

/**
 * Responsible for bluffing pisti specific arrangements in the level 3 or multi-player game.
 */
@RequiredArgsConstructor
@Service
public class BluffingPistiService {

    private final ComputerMoveService computerMoveService;
    private final SinglePlayerService singlePlayerService;
    private final GameRepository gameRepository;
    private final CommonService commonService;

    /**
     * Takes action of the player, process that bluffing claim and make some changes on the table.
     *
     * @param action action taken by the player
     * @return state of the table
     * @throws PlayerNotFoundException when the player is not found
     * @throws GameNotFoundException when the game is not found
     * @throws PlayerNotAuthorizedException when the player is not authorized, token is incorrect
     * @throws CardNotBelongToPlayerException when the player does not have the card
     */
    public TableStateStagesDTO bluffCard(PlayerActionDTO action) {
        TableStateDTO tableState = commonService.checkCommonConstraints(action);
        Game game = gameRepository.findById(action.getGameId()).orElse(null);
        // parse required fields such as table state, player cards etc. from the player's action.
        Set<Card> playerCards = action.getPlayerNo().equals(PlayerType.PLAYER1) ? tableState.getPlayer1Cards() : tableState.getPlayer2Cards();
        Card cardToPlay = new Card(action.getSuit(), action.getRank());

        if (!game.isBluffingPisti()) {
            // standard pisti
            throw new NotBluffingPistiException(GAME_NOT_BLUFFING);
        }

        List<Card> faceUpCards = tableState.getFaceUpCards();
        if (faceUpCards == null ||faceUpCards.size() != 1) {
            throw new FaceUpCardsNotValidException(FACE_UP_CARDS_NOT_VALID);
        }

        // return the state of the table including face-up cards, face-down cards, scores etc.
        return processPlayerBluffing(game, playerCards, cardToPlay, action);
    }

    /**
     * When a player makes bluffing about the card he played as if it has same rank with the top card on the face-up cards,
     * this method is called.
     * It takes status of the entire table, action of the player. Then, it makes some changes on the {@code TableStateDTO}.
     * All business logic in the bluffing pisti for level-3 game is controlled from this method.
     *
     * @param game the game object
     * @param playerCards card set of the player
     * @param cardToPlay the card that player wants to play in his/her hand
     * @param playerAction entity that client sent to backend
     * @return a dto that includes two tables states for both before compute move and after computer move
     */
    public TableStateStagesDTO processPlayerBluffing(Game game, Set<Card> playerCards, Card cardToPlay, PlayerActionDTO playerAction) {
        TableStateDTO tableState = playerAction.getTableState();
        TableStateStagesDTO stages = new TableStateStagesDTO();

        //determine randomly whether computer will be challenged player or not
        Random random = new Random();
        // generate a random number using nextBoolean method.
        boolean isChallenged = random.nextBoolean();

        // process bluffing
        processBluff(tableState, playerCards, cardToPlay, isChallenged);
        // give turn to computer
        tableState.setTurn(2);
        stages.setTableStateBeforeComputeMove(new TableStateDTO(tableState));
        // then computer will make its move
        computerMoveService.makeComputerMove(game, tableState);
        // give turn to player1
        tableState.setTurn(1);

        // check cards of the players have finished or not
        if (areCardsFinished(tableState)) {
            // if player cards finished, check if there is any card to deal or not
            singlePlayerService.checkGameIsOver(game, tableState);
        }
        stages.setFinalTableState(tableState);
        stages.getTableStateBeforeComputeMove().setFaceDownCards(new HashSet<>()); // to reduce size of the body
        return stages;
    }

    private void processBluff(TableStateDTO tableState, Set<Card> playerCards, Card cardToPlay, boolean isChallenged) {
        playerCards.remove(cardToPlay);

        Card lastCardOnMiddle = tableState.getFaceUpCards().get(0);
        // check whether bluff is true or not (there is a really pisti/double pisti situation
        boolean isBluffTrue = cardToPlay.canCapture(lastCardOnMiddle);

        if (isChallenged) {
            // check if bluff is true (the card played is actually a card of the same rank)
            if (isBluffTrue) {
                tableState.setEventType(EventType.BLUFF_CHALLENGED_AND_PISTI);
                adjustTableStateForPlayerScoring(tableState, cardToPlay, tableState.getFaceUpCards(), lastCardOnMiddle, BLUFFING_CHALLENGED_DOUBLE_PISTI_POINT, BLUFFING_CHALLENGED_PISTI_POINT);

            } else {
                // the challenging player scores 20 or 40 points instead.
                // In that case, both cards remain on the table and play continues as normal.
                tableState.setEventType(EventType.BLUFF_CHALLENGED_AND_NOT_PISTI);
                adjustTableStateForComputerScoring(tableState, cardToPlay, lastCardOnMiddle);
            }
        } else {
            // the player of the card automatically scores a Pişti or Double Pişti and captures both cards,
            // and adds the cards to his capture pile
            tableState.setEventType(EventType.BLUFF_NOT_CHALLENGED);
            adjustTableStateForPlayerScoring(tableState, cardToPlay, tableState.getFaceUpCards(), lastCardOnMiddle, DOUBLE_PISTI_POINT, PISTI_POINT);
        }
    }

    private void adjustTableStateForPlayerScoring(TableStateDTO tableState, Card cardToPlay, List<Card> faceUpCards, Card lastCardOnMiddle, int doublePistiPoint, int pistiPoint) {
        if (lastCardOnMiddle.getRank().equals(Rank.JACK)){
            // the player of that face-down cards scores doublePistiPoint for the Double Pişti.
            tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + doublePistiPoint);
        } else {
            // the player of that face-down cards scores pistiPoint for the Pişti.
            tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + pistiPoint);
        }
        tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + calculatePointToAdd(faceUpCards, cardToPlay));
        tableState.setCapturedCardsNumberByPlayer1(tableState.getCapturedCardsNumberByPlayer1() + faceUpCards.size() + 1);
        tableState.setFaceUpCards(new ArrayList<>());
    }

    private void adjustTableStateForComputerScoring(TableStateDTO tableState, Card cardToPlay, Card lastCardOnMiddle) {
        if (lastCardOnMiddle.getRank().equals(Rank.JACK)){
            // the computer scores 40 points for the Double Pişti.
            tableState.setComputerLevelScore(tableState.getComputerLevelScore() + BLUFFING_CHALLENGED_DOUBLE_PISTI_POINT);
        } else {
            // the computer scores 20 points for the Pişti.
            tableState.setComputerLevelScore(tableState.getComputerLevelScore() + BLUFFING_CHALLENGED_PISTI_POINT);
        }
        //both cards remain on the table
        tableState.getFaceUpCards().add(cardToPlay);
    }

}
