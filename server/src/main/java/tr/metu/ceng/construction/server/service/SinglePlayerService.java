package tr.metu.ceng.construction.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.metu.ceng.construction.server.DTO.PlayerActionDTO;
import tr.metu.ceng.construction.server.DTO.TableStateDTO;
import tr.metu.ceng.construction.server.DTO.TableStateStagesDTO;
import tr.metu.ceng.construction.server.enums.EventType;
import tr.metu.ceng.construction.server.enums.Rank;
import tr.metu.ceng.construction.server.model.Card;
import tr.metu.ceng.construction.server.model.Game;
import tr.metu.ceng.construction.server.repository.GameRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static tr.metu.ceng.construction.server.utils.GameUtilities.*;
import static tr.metu.ceng.construction.server.common.CommonConstants.*;

/**
 * Responsible for move of the player in a single player game.
 */
@RequiredArgsConstructor
@Service
public class SinglePlayerService {

    private final GameService gameService;
    private final GameRepository gameRepository;
    private final ScoreService scoreService;
    private final ComputerMoveService computerMoveService;

    /**
     * When a player wants to play a card, this method is called.
     * It takes status of the entire table, action of the player. Then, it makes some changes on the {@code TableStateDTO}.
     * All business logic in a single player game is controlled from this method.
     *
     * @param game the game object
     * @param playerCards card set of the player
     * @param cardToPlay the card that player wants to play in his/her hand
     * @param playerAction entity that client sent to backend
     * @return a dto that includes two tables states for both before compute move and after computer move
     */
    public TableStateStagesDTO processPlayerMove(Game game, Set<Card> playerCards, Card cardToPlay, PlayerActionDTO playerAction) {
        TableStateDTO tableState = playerAction.getTableState();
        TableStateStagesDTO stages = new TableStateStagesDTO();

        // action of the player is executed
        executePlayerMove(playerCards, cardToPlay, tableState);
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
            checkGameIsOver(game, tableState);
        }
        stages.setFinalTableState(tableState);
        stages.getTableStateBeforeComputeMove().setFaceDownCards(new HashSet<>()); // to reduce size of the body
        return stages;
    }

    private void executePlayerMove(Set<Card> playerCards, Card cardToPlay, TableStateDTO tableState) {
        // remove the card from player's deck
        playerCards.remove(cardToPlay);
        List<Card> faceUpCards = tableState.getFaceUpCards();
        if (faceUpCards.size() != 0)  {
            // if face up cards (middle cards) are not empty, then check if player capture it or not
            Card lastCardOnMiddle = faceUpCards.get(faceUpCards.size()-1);

            if (cardToPlay.canCapture(lastCardOnMiddle)) {
                // if player capture the face-up-cards, then adjust the score
                tableState.setEventType(EventType.PLAYER1_CAPTURED);
                if (faceUpCards.size() == 1 && lastCardOnMiddle.getRank().equals(Rank.JACK)){
                    tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + DOUBLE_PISTI_POINT);
                } else if (faceUpCards.size() == 1) {
                    tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + PISTI_POINT);
                }
                tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + calculatePointToAdd(faceUpCards, cardToPlay));
                tableState.setCapturedCardsNumberByPlayer1(tableState.getCapturedCardsNumberByPlayer1() + faceUpCards.size() + 1);
                tableState.setFaceUpCards(new ArrayList<>());
            } else {
                faceUpCards.add(cardToPlay);
            }
        }
        else {
            // if there is no card on the middle, just add the card into the middle (face-up cards)
            faceUpCards.add(cardToPlay);
        }
    }

    public void checkGameIsOver(Game game, TableStateDTO tableState ) {
        if (tableState.getFaceDownCards().isEmpty()) {
            // all cards have been dealt, hand is over, check if any player passes 151
            // if any player passes 151, then level up or game finish and create a score entity

            givePointForMostCapturedCardInSinglePlayer(tableState);

            if (tableState.getComputerLevelScore() >= PASSING_SCORE) {
                // computer passed 151, game is over, player lost
                // get zero score from that and further levels
                scoreService.saveScore(game.getPlayer1().getCumulativeScore(), game.getPlayer1());
                tableState.setEventType(EventType.COMPUTER_WIN);

            } else if (tableState.getPlayer1LevelScore() >= PASSING_SCORE) {
                // player1 passed 151, level up
                tableState.setEventType(EventType.LEVEL_UP);
                levelUp(game, tableState);
            } else {
                // none of the players could pass 151, prepare new game in the same level
                tableState.setEventType(EventType.RESET_CARDS);
                gameService.resetCards(game, tableState);
            }
        } else {
            // there are still cards to be dealt on table, deal these cards and continue
            gameService.dealCards(game, tableState);
        }
    }

    private void levelUp(Game game, TableStateDTO tableState) {
        int level = game.getLevel();
        //LevelX-Score = UserPoints – OpponentPoints
        int levelScore = tableState.getPlayer1LevelScore() - tableState.getComputerLevelScore();
        tableState.setPlayer1CumulativeScore(game.getPlayer1().getCumulativeScore() + (level * levelScore));
        game.getPlayer1().setCumulativeScore(game.getPlayer1().getCumulativeScore() + (level * levelScore));
        gameService.resetCards(game, tableState);
        clearLevelScores(tableState);
        game.setLevel(level + 1);
        tableState.setLevel(level + 1);
        gameRepository.save(game);
        if (tableState.getLevel().equals(MULTIPLAYER_GAME_LEVEL)) {
            tableState.setEventType(EventType.MATCHMAKING);
        }
    }

}
