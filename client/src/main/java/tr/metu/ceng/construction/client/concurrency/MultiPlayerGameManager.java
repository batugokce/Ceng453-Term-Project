package tr.metu.ceng.construction.client.concurrency;

import javafx.application.Platform;
import tr.metu.ceng.construction.client.DTO.CardDTO;
import tr.metu.ceng.construction.client.component.LeftMenuForMultiplayer;
import tr.metu.ceng.construction.client.controller.SaveScoreController;
import tr.metu.ceng.construction.client.scene.GameEndScene;
import tr.metu.ceng.construction.client.scene.MatchMakingScene;
import tr.metu.ceng.construction.common.*;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.client.constant.StageConstants;
import tr.metu.ceng.construction.client.controller.MultiPlayerController;
import tr.metu.ceng.construction.client.scene.MultiPlayerGameScene;
import tr.metu.ceng.construction.common.TableStateForMultiplayer;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Responsible from managing multi-player game such as starting a new thread for the game,
 * processing different type of incoming messages like matching, start or game over, and
 * starting or interrupting message sender and receiver threads.
 */
public class MultiPlayerGameManager {

    private MultiPlayerController multiPlayerController;
    private boolean isGameActive = true;
    private Thread receiverThread;

    /**
     * This methods starts a new thread and handles matching players and also starting game.
     */
    public void startMultiPlayerGame() {
        new Thread(() -> {
            multiPlayerController = new MultiPlayerController();
            multiPlayerController.writeUsernameAndCumulativeScore();

            try {
                TableStateForMultiplayer tableState = multiPlayerController.getTableState();
                System.out.println(tableState);

                // set constants
                GameConstants.TABLE_STATE_MULTIPLAYER = tableState;
                GameConstants.isMultiplayerGameStarted = true;

                String otherPlayerUsername;
                if (GameConstants.USERNAME.equals(tableState.getPlayer1Username())) {
                    GameConstants.WHICH_PLAYER_YOU_ARE = PlayerType.PLAYER1;
                    otherPlayerUsername = tableState.getPlayer2Username();
                } else {
                    GameConstants.WHICH_PLAYER_YOU_ARE = PlayerType.PLAYER2;
                    otherPlayerUsername = tableState.getPlayer1Username();
                }

                StageConstants.stage.getScene().setRoot(MatchMakingScene.drawMatchedScene(otherPlayerUsername));
                System.out.println("match made");

                Thread.sleep(1500);
                // start threads for receiving data
                startThreads();

                Platform.runLater(() -> {
                    StageConstants.stage.getScene().setRoot(MultiPlayerGameScene.drawGameScene());
                    System.out.println("game started");
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    /**
     * start thread for listening server messages
     */
    public void startThreads() {
        receiverThread = createThreadForReceivingMessages();
        receiverThread.start();
    }

    /**
     * interrupts existing threads
     */
    public void interruptThreads() {
        isGameActive = false;
        receiverThread.interrupt();
    }

    // when a new table state reaches, this method will process it
    private void processTableState(TableStateForMultiplayer tableState) {
        Set<CardDTO> playerCards;
        int opponentCardNumber;

        if (tableState.getEventType() != null && isGameOverByEventType(tableState)) {
            // game has finished
            if (GameConstants.WHICH_PLAYER_YOU_ARE.equals(PlayerType.PLAYER1)) {
                tableState.setPlayer1CumulativeScore(tableState.getPlayer1LevelScore() * 10 + tableState.getPlayer1CumulativeScore());
                new SaveScoreController().saveScore(tableState.getPlayer1CumulativeScore());
            } else {
                tableState.setPlayer2CumulativeScore(tableState.getPlayer2LevelScore() * 10 + tableState.getPlayer2CumulativeScore());
                new SaveScoreController().saveScore(tableState.getPlayer2CumulativeScore());
            }

            isGameActive = false;
            StageConstants.stage.getScene().setRoot(GameEndScene.draw(tableState));
        } else if (tableState.getEventType() != null && tableState.getEventType().equals(MultiplayerEvent.BLUFF)) {
            if ((tableState.getTurn() == 1 && GameConstants.WHICH_PLAYER_YOU_ARE.equals(PlayerType.PLAYER1)) || (tableState.getTurn() == 2 && GameConstants.WHICH_PLAYER_YOU_ARE.equals(PlayerType.PLAYER2)) ) {
                GameConstants.isMultiplayerGameAndBluffingChallengeTurn = true;
                MultiPlayerGameScene.bluffChallengedButton.setVisible(true);
                MultiPlayerGameScene.bluffNotChallengedButton.setVisible(true);
            }
        }

        if (GameConstants.WHICH_PLAYER_YOU_ARE.equals(PlayerType.PLAYER1)) {
            playerCards = tableState.getPlayer1Cards().stream().map(CardForMultiplayer::mapToNormalCard).collect(Collectors.toSet());
            opponentCardNumber = tableState.getPlayer2Cards().size();
        } else {
            playerCards = tableState.getPlayer2Cards().stream().map(CardForMultiplayer::mapToNormalCard).collect(Collectors.toSet());
            opponentCardNumber = tableState.getPlayer1Cards().size();
        }

        // prepare a new left menu object
        LeftMenuForMultiplayer leftMenuComponent = new LeftMenuForMultiplayer(tableState.getCapturedCardsNumberByPlayer1(),
                tableState.getCapturedCardsNumberByPlayer2(), tableState.getPlayer1LevelScore(), tableState.getPlayer2LevelScore(),
                tableState.getPlayer1CumulativeScore(), tableState.getPlayer2CumulativeScore(), tableState.getEventType(), tableState.getTurn());

        MultiPlayerGameScene.updateBoxes(playerCards, opponentCardNumber, tableState.getFaceDownCards().size(),
                tableState.getFaceUpCards().stream().map(CardForMultiplayer::mapToNormalCard).collect(Collectors.toList()),
                leftMenuComponent, tableState.getTurn(), 0);
    }

    // check whether game over condition occurred
    private boolean isGameOverByEventType(TableStateForMultiplayer tableState) {
        return tableState.getEventType().equals(MultiplayerEvent.PLAYER1_WIN) ||
                tableState.getEventType().equals(MultiplayerEvent.PLAYER2_WIN);
    }

    // creates a new thread to listen messages from server
    private Thread createThreadForReceivingMessages() {
        return new Thread(() -> {
            while (isGameActive) {
                TableStateForMultiplayer tableState = multiPlayerController.getTableState();

                GameConstants.TABLE_STATE_MULTIPLAYER = tableState;
                System.out.println("new table state has been received");
                processTableState(tableState);
            }
        });
    }

}
