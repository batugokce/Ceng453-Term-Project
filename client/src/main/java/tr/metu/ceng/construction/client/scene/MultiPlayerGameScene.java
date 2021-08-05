package tr.metu.ceng.construction.client.scene;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import tr.metu.ceng.construction.client.DTO.CardDTO;
import tr.metu.ceng.construction.client.component.*;
import tr.metu.ceng.construction.client.constant.ColorConstants;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.client.constant.NetworkConstants;
import tr.metu.ceng.construction.common.ActionMove;
import tr.metu.ceng.construction.common.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static tr.metu.ceng.construction.client.constant.GameConstants.*;
import static tr.metu.ceng.construction.client.constant.StageConstants.LABEL_FONT_SIZE;

/**
 * Scene that will be used during multiplayer game
 */
public class MultiPlayerGameScene {

    public static Button playButton = new Button("Play Card");
    public static Button bluffButton = new Button("Bluff");
    public static Button bluffChallengedButton = new Button("Challenge");
    public static Button bluffNotChallengedButton = new Button("Not Challenge");


    /**
     * Prepares an element to display in the scene
     *
     * @return a pane to be displayed
     */
    public static Pane drawGameScene() {
        TableStateForMultiplayer tableState = GameConstants.TABLE_STATE_MULTIPLAYER;

        HBox root = new HBox();
        root.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.MENU_BACKGROUND_COLOR), CornerRadii.EMPTY, new Insets(0))));

        LeftMenuForMultiplayer leftMenuComponent = new LeftMenuForMultiplayer(tableState.getCapturedCardsNumberByPlayer1(),
                tableState.getCapturedCardsNumberByPlayer2(), tableState.getPlayer1LevelScore(), tableState.getPlayer2LevelScore(),
                tableState.getPlayer1CumulativeScore(), tableState.getPlayer2CumulativeScore(), tableState.getEventType(), tableState.getTurn());

        leftMenuComponent.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.ALMOST_WHITE), CornerRadii.EMPTY, new Insets(0))));
        leftMenuComponent.setPrefSize(250, 600);

        // below long codes are for preparing left menu component elements
        HBox levelComponent = new HBox();
        Label levelLabel = new Label();
        levelLabel.setText("Level: ");
        Label levelVariable = new Label();
        levelVariable.setText("4");
        levelComponent.setSpacing(10);
        levelComponent.getChildren().addAll(levelLabel, levelVariable);

        HBox player1CapturedCardsNumberComponent = new HBox();
        Label player1CapturedCardsNumberLabel = new Label();
        player1CapturedCardsNumberLabel.setText("Player1 captured card #: ");
        Label playerCapturedCardsNumber = new Label();
        playerCapturedCardsNumber.setText(leftMenuComponent.getCapturedCardsNumberByPlayer1().toString());
        player1CapturedCardsNumberComponent.setSpacing(10);
        player1CapturedCardsNumberComponent.getChildren().addAll(player1CapturedCardsNumberLabel, playerCapturedCardsNumber);

        HBox player2CapturedCardsNumberComponent = new HBox();
        Label player2CapturedCardsNumberLabel = new Label();
        player2CapturedCardsNumberLabel.setText("Player2 captured card #: ");
        Label player2CapturedCardsNumber = new Label();
        player2CapturedCardsNumber.setText(leftMenuComponent.getCapturedCardsNumberByPlayer2().toString());
        player2CapturedCardsNumberComponent.setSpacing(10);
        player2CapturedCardsNumberComponent.getChildren().addAll(player2CapturedCardsNumberLabel, player2CapturedCardsNumber);

        HBox player1LevelScoreComponent = new HBox();
        Label player1LevelScoreLabel = new Label();
        player1LevelScoreLabel.setText("Player1 level score: ");
        Label player1LevelScore = new Label();
        player1LevelScore.setText(leftMenuComponent.getPlayer1LevelScore().toString());
        player1LevelScoreComponent.setSpacing(10);
        player1LevelScoreComponent.getChildren().addAll(player1LevelScoreLabel, player1LevelScore);

        HBox player2LevelScoreComponent = new HBox();
        Label player2LevelScoreLabel = new Label();
        player2LevelScoreLabel.setText("Player2 level score: ");
        Label player2LevelScoreVariable = new Label();
        player2LevelScoreVariable.setText(leftMenuComponent.getPlayer2LevelScore().toString());
        player2LevelScoreComponent.setSpacing(10);
        player2LevelScoreComponent.getChildren().addAll(player2LevelScoreLabel, player2LevelScoreVariable);

        HBox player1CumulativeScoreComponent = new HBox();
        Label player1CumulativeScoreLabel = new Label();
        player1CumulativeScoreLabel.setText("Player1 previous score: ");
        Label player1CumulativeScore = new Label();
        player1CumulativeScore.setText(leftMenuComponent.getPlayer1CumulativeScore().toString());
        player1CumulativeScoreComponent.setSpacing(10);
        player1CumulativeScoreComponent.getChildren().addAll(player1CumulativeScoreLabel, player1CumulativeScore);

        HBox player2CumulativeScoreComponent = new HBox();
        Label player2CumulativeScoreLabel = new Label();
        player2CumulativeScoreLabel.setText("Player2 previous score: ");
        Label player2CumulativeScore = new Label();
        player2CumulativeScore.setText(tableState.getPlayer2CumulativeScore().toString());
        player2CumulativeScoreComponent.setSpacing(10);
        player2CumulativeScoreComponent.getChildren().addAll(player2CumulativeScoreLabel, player2CumulativeScore);

        Label turnLabel =  new Label();
        turnLabel.setFont(Font.font(LABEL_FONT_SIZE));
        turnLabel.setPadding(new Insets(20, 10, 10, 10));
        if (leftMenuComponent.getTurn() == 1) {
            turnLabel.setText(PLAYER1_TURN);
        } else if (leftMenuComponent.getTurn() == 2) {
            turnLabel.setText(PLAYER2_TURN);
        }

        Label eventTypeLabel = new Label();
        eventTypeLabel.setFont(Font.font(LABEL_FONT_SIZE));
        eventTypeLabel.setPadding(new Insets(10, 10, 10, 10));
        if (tableState.getEventType() != null) {
            eventTypeLabel.setText(tableState.getEventType().toString());
        }

        FaceDownCardBox faceDownCardBox = new FaceDownCardBox(tableState.getFaceDownCards().size());
        leftMenuComponent.getChildren().addAll(levelComponent, player1CapturedCardsNumberComponent, player2CapturedCardsNumberComponent,
                player1LevelScoreComponent, player2LevelScoreComponent, player1CumulativeScoreComponent, player2CumulativeScoreComponent,  turnLabel, eventTypeLabel, faceDownCardBox);
        leftMenuComponent.setPadding(new Insets(10, 10, 10, 10));

        // prepare a game play box for displaying cards of players
        VBox gamePlayBox = new VBox();
        gamePlayBox.setPrefSize(600, 600);

        PlayerCardBox playerCardBox;
        OpponentCardBox opponentCardBox;

        if (WHICH_PLAYER_YOU_ARE == PlayerType.PLAYER1) {
            playerCardBox = new PlayerCardBox(tableState.getPlayer1Cards().stream().map(CardForMultiplayer::mapToNormalCard).collect(Collectors.toSet()), false);
            opponentCardBox = new OpponentCardBox(tableState.getPlayer2Cards().size());
        } else {
            playerCardBox = new PlayerCardBox(tableState.getPlayer2Cards().stream().map(CardForMultiplayer::mapToNormalCard).collect(Collectors.toSet()), true);
            opponentCardBox = new OpponentCardBox(tableState.getPlayer1Cards().size());
        }

        MiddleCardBox middleCardBox = new MiddleCardBox(tableState.getFaceUpCards().stream().map(CardForMultiplayer::mapToNormalCard).collect(Collectors.toList()));
        updateConstants(playerCardBox, opponentCardBox, middleCardBox, leftMenuComponent, faceDownCardBox);

        // prepare labels for both players
        Label yourLabel = new Label();
        yourLabel.setPadding(new Insets(-10, 0, 5, 250));
        yourLabel.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        Label opponentLabel = new Label();
        opponentLabel.setPadding(new Insets(5, 0, -10, 250));
        opponentLabel.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        if (WHICH_PLAYER_YOU_ARE == PlayerType.PLAYER1) {
            yourLabel.setText(PlayerType.PLAYER1 + " - " + tableState.getPlayer1Username());
            opponentLabel.setText(PlayerType.PLAYER2 + " - " + tableState.getPlayer2Username());
        } else {
            yourLabel.setText(PlayerType.PLAYER2 + " - " + tableState.getPlayer2Username());
            opponentLabel.setText(PlayerType.PLAYER1 + " - " + tableState.getPlayer1Username());
        }

        // prepare two buttons and their actions
        HBox buttons = new HBox();
        HBox challengeButtons = new HBox();
        playButton.setVisible(false);
        playButton.setFont(Font.font(LABEL_FONT_SIZE));
        playButton.setOnAction(e -> executeAction(ActionMove.PLAY_CARD));

        bluffButton.setFont(Font.font(LABEL_FONT_SIZE));
        bluffButton.setVisible(false);
        bluffButton.setOnAction(e -> executeAction(ActionMove.BLUFF));

        bluffChallengedButton.setFont(Font.font(LABEL_FONT_SIZE));
        bluffChallengedButton.setVisible(false);
        bluffChallengedButton.setOnAction(e -> executeAction(ActionMove.BLUFF_CHALLENGE));

        bluffNotChallengedButton.setFont(Font.font(LABEL_FONT_SIZE));
        bluffNotChallengedButton.setVisible(false);
        bluffNotChallengedButton.setOnAction(e -> executeAction(ActionMove.BLUFF_NOT_CHALLENGE));

        buttons.setSpacing(10);
        buttons.setPadding(new Insets(0, 0, 10, 150));
        buttons.getChildren().addAll(playButton, bluffButton, bluffChallengedButton, bluffNotChallengedButton);

        gamePlayBox.getChildren().addAll(opponentCardBox, opponentLabel, middleCardBox, buttons, challengeButtons, yourLabel, playerCardBox);

        root.getChildren().addAll(leftMenuComponent, gamePlayBox);

        handleCheating(root);

        return root;
    }

    // handle cheating when pressed ctrl + 9
    private static void handleCheating(Pane root) {
        final KeyCombination keyComb = new KeyCodeCombination(KeyCode.DIGIT9,
                KeyCombination.CONTROL_DOWN);

        root.setOnKeyPressed(keyEvent -> {
            if (keyComb.match(keyEvent)) {
                //System.out.println("Ctrl+9 pressed");
                executeAction(ActionMove.CHEAT);
            }
        });
    }

    /**
     * Executes a player move
     * @param actionMove checks if the action is play or bluff button, or cheat shortcut
     */
    private static void executeAction(ActionMove actionMove) {
        PlayerActionForMultiplayer action = new PlayerActionForMultiplayer();
        // card is played
        if (actionMove.equals(ActionMove.PLAY_CARD) || actionMove.equals(ActionMove.BLUFF)) {
            action.setRank(Rank.valueOf(PICKED_CARD_RANK.toString()));
            action.setSuit(Suit.valueOf(PICKED_CARD_SUIT.toString()));
        }

        action.setActionMove(actionMove);
        action.setPlayerNo(WHICH_PLAYER_YOU_ARE);
        try {
            NetworkConstants.WRITE_TO_SOCKET.writeObject(action);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // reset picked card and hide buttons
        PICKED_CARD_RANK = null;
        PICKED_CARD_SUIT = null;
        playButton.setVisible(false);
        bluffButton.setVisible(false);
        bluffChallengedButton.setVisible(false);
        bluffNotChallengedButton.setVisible(false);
        if(actionMove.equals(ActionMove.BLUFF_CHALLENGE) || actionMove.equals(ActionMove.BLUFF_NOT_CHALLENGE)) {
            GameConstants.isMultiplayerGameAndBluffingChallengeTurn = false;
        }

    }

    // Updates game constants
    private static void updateConstants(PlayerCardBox playerCardBox, OpponentCardBox opponentCardBox, MiddleCardBox middleCardBox, LeftMenuForMultiplayer leftMenuComponent, FaceDownCardBox faceDownCardBox) {
        GameConstants.playerCardBox = playerCardBox;
        GameConstants.opponentCardBox = opponentCardBox;
        GameConstants.middleCardBox = middleCardBox;
        GameConstants.faceDownCardBox = faceDownCardBox;
        GameConstants.leftMenuComponentForMultiplayer = leftMenuComponent;
    }

    /**
     * When a new table state reaches, this method updates components in the screen
     *
     * @param playerCardDTOS deck of the player
     * @param opponentCardsNumber number of cards that opponent has
     * @param faceDownCardsNumber number of cards in face-down deck
     * @param faceUpCardDTOList list of cards in face-up deck
     * @param leftMenuComponent left menu component
     * @param turn which player will play next
     * @param sleepAmount how much time will be waited before update
     */
    public static void updateBoxes(Set<CardDTO> playerCardDTOS, int opponentCardsNumber, int faceDownCardsNumber, List<CardDTO> faceUpCardDTOList, LeftMenuForMultiplayer leftMenuComponent, Integer turn, int sleepAmount) {
        try {
            Thread.yield();
            Thread.sleep(sleepAmount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // decides whose turn is it, this variable will be used to enable/disable cards
        int whoseTurn;
        if ((WHICH_PLAYER_YOU_ARE.equals(PlayerType.PLAYER1) && (turn == 1)) || (WHICH_PLAYER_YOU_ARE.equals(PlayerType.PLAYER2) && (turn == 2))) {
            whoseTurn = 1;
        } else {
            whoseTurn = 2;
        }

        Platform.runLater(() -> {
            GameConstants.playerCardBox.setCards(playerCardDTOS, whoseTurn);
            GameConstants.opponentCardBox.setNOfCards(opponentCardsNumber);
            GameConstants.middleCardBox.setCards(faceUpCardDTOList);
            GameConstants.faceDownCardBox.setNumberOfCards(faceDownCardsNumber);
            GameConstants.leftMenuComponentForMultiplayer.setAll(leftMenuComponent);
        });
    }


}
