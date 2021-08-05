package tr.metu.ceng.construction.client.scene;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import tr.metu.ceng.construction.client.DTO.PlayerActionDTO;
import tr.metu.ceng.construction.client.DTO.TableStateDTO;
import tr.metu.ceng.construction.client.DTO.TableStateStagesDTO;
import tr.metu.ceng.construction.client.component.*;
import tr.metu.ceng.construction.client.concurrency.MultiPlayerGameManager;
import tr.metu.ceng.construction.client.constant.ColorConstants;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.client.constant.StageConstants;
import tr.metu.ceng.construction.client.controller.BluffingPistiController;
import tr.metu.ceng.construction.client.controller.CheatController;
import tr.metu.ceng.construction.client.controller.PlayCardController;
import tr.metu.ceng.construction.client.controller.StartGameController;
import tr.metu.ceng.construction.client.enums.PlayerType;

import static tr.metu.ceng.construction.client.constant.GameConstants.*;
import static tr.metu.ceng.construction.client.constant.StageConstants.LABEL_FONT_SIZE;
import static tr.metu.ceng.construction.client.enums.PlayerType.COMPUTER;
import static tr.metu.ceng.construction.client.enums.PlayerType.PLAYER1;
import static tr.metu.ceng.construction.client.scene.utils.CommonItems.updateBoxes;

/**
 * Responsible for preparing a scene of game
 */
public class GameScene {

    public static Button playButton = new Button("Play Card");
    public static Button bluffButton = new Button("Bluff");
    private static final CheatController cheatController = new CheatController();
    private static MultiPlayerGameManager multiPlayerGameManager = new MultiPlayerGameManager();

    /**
     * Prepares a pane as a root element for game scene
     *
     * @return a pane to use in that scene
     */
    public static Pane draw() {
        TableStateDTO initialTableState = new StartGameController().startGame();

        HBox root = new HBox();
        root.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.MENU_BACKGROUND_COLOR), CornerRadii.EMPTY, new Insets(0))));

        LeftMenuComponent leftMenuComponent = new LeftMenuComponent(initialTableState.getLevel(), initialTableState.getCapturedCardsNumberByPlayer1(),
                initialTableState.getCapturedCardsNumberByComputer(), initialTableState.getPlayer1LevelScore(), initialTableState.getComputerLevelScore(),
                initialTableState.getPlayer1CumulativeScore(), initialTableState.getEventType(), initialTableState.getTurn());

        leftMenuComponent.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.ALMOST_WHITE), CornerRadii.EMPTY, new Insets(0))));
        leftMenuComponent.setPrefSize(250, 600);

        HBox levelComponent = new HBox();
        Label levelLabel = new Label();
        levelLabel.setText("Level: ");
        Label levelVariable = new Label();
        levelVariable.setText(leftMenuComponent.getLevel().toString());
        levelComponent.setSpacing(10);
        levelComponent.getChildren().addAll(levelLabel, levelVariable);

        HBox playerCapturedCardsNumberComponent = new HBox();
        Label playerCapturedCardsNumberLabel = new Label();
        playerCapturedCardsNumberLabel.setText("Your captured card #: ");
        Label playerCapturedCardsNumber = new Label();
        playerCapturedCardsNumber.setText(leftMenuComponent.getCapturedCardsNumberByPlayer1().toString());
        playerCapturedCardsNumberComponent.setSpacing(10);
        playerCapturedCardsNumberComponent.getChildren().addAll(playerCapturedCardsNumberLabel, playerCapturedCardsNumber);

        HBox computerCapturedCardsNumberComponent = new HBox();
        Label computerCapturedCardsNumberLabel = new Label();
        computerCapturedCardsNumberLabel.setText("Computer's captured card #: ");
        Label computerCapturedCardsNumber = new Label();
        computerCapturedCardsNumber.setText(leftMenuComponent.getCapturedCardsNumberByComputer().toString());
        computerCapturedCardsNumberComponent.setSpacing(10);
        computerCapturedCardsNumberComponent.getChildren().addAll(computerCapturedCardsNumberLabel, computerCapturedCardsNumber);

        HBox playerLevelScoreComponent = new HBox();
        Label playerLevelScoreLabel = new Label();
        playerLevelScoreLabel.setText("Your level score: ");
        Label playerLevelScore = new Label();
        playerLevelScore.setText(leftMenuComponent.getPlayer1LevelScore().toString());
        playerLevelScoreComponent.setSpacing(10);
        playerLevelScoreComponent.getChildren().addAll(playerLevelScoreLabel, playerLevelScore);

        HBox computerLevelScoreComponent = new HBox();
        Label computerLevelScoreLabel = new Label();
        computerLevelScoreLabel.setText("Computer level score: ");
        Label computerLevelScoreVariable = new Label();
        computerLevelScoreVariable.setText(leftMenuComponent.getComputerLevelScore().toString());
        computerLevelScoreComponent.setSpacing(10);
        computerLevelScoreComponent.getChildren().addAll(computerLevelScoreLabel, computerLevelScoreVariable);

        HBox playerCumulativeScoreComponent = new HBox();
        Label playerCumulativeScoreLabel = new Label();
        playerCumulativeScoreLabel.setText("Your previous score: ");
        Label playerCumulativeScore = new Label();
        playerCumulativeScore.setText(leftMenuComponent.getPlayer1CumulativeScore().toString());
        playerCumulativeScoreComponent.setSpacing(10);
        playerCumulativeScoreComponent.getChildren().addAll(playerCumulativeScoreLabel, playerCumulativeScore);

        Label turnLabel =  new Label();
        turnLabel.setFont(Font.font(LABEL_FONT_SIZE));
        turnLabel.setPadding(new Insets(20, 10, 10, 10));
        if (leftMenuComponent.getTurn() == 1) {
            // player turn
            turnLabel.setText(YOUR_TURN);

        } else if (leftMenuComponent.getTurn() == 2) {
            // computer turn
            turnLabel.setText(COMPUTER_TURN);
        }

        Label eventTypeLabel = new Label();
        eventTypeLabel.setFont(Font.font(LABEL_FONT_SIZE));
        eventTypeLabel.setPadding(new Insets(10, 10, 10, 10));
        if (leftMenuComponent.getEventType() != null) {
            eventTypeLabel.setText(leftMenuComponent.getEventType().toString());
        }

        FaceDownCardBox faceDownCardBox = new FaceDownCardBox(initialTableState.getFaceDownCards().size());
        leftMenuComponent.getChildren().addAll(levelComponent, playerCapturedCardsNumberComponent, computerCapturedCardsNumberComponent,
                playerLevelScoreComponent, computerLevelScoreComponent, playerCumulativeScoreComponent, turnLabel, eventTypeLabel, faceDownCardBox);
        leftMenuComponent.setPadding(new Insets(10, 10, 10, 10));

        // prepare game play box which consists of player cards and middle cards
        VBox gamePlayBox = new VBox();
        gamePlayBox.setPrefSize(600, 600);

        PlayerCardBox playerCardBox = new PlayerCardBox(initialTableState.getPlayer1Cards());
        OpponentCardBox opponentCardBox = new OpponentCardBox(initialTableState.getComputerCards().size());
        MiddleCardBox middleCardBox = new MiddleCardBox(initialTableState.getFaceUpCards());
        updateConstants(playerCardBox, opponentCardBox, middleCardBox, leftMenuComponent, faceDownCardBox);

        Label player1Label = new Label();
        player1Label.setText(PLAYER1.toString());
        player1Label.setPadding(new Insets(-10, 0, 5, 250));
        player1Label.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        Label player2Label = new Label();
        player2Label.setText(COMPUTER.toString());
        player2Label.setPadding(new Insets(5, 0, -10, 250));
        player2Label.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        HBox buttons = new HBox();
        // prepare two buttons and their actions
        playButton.setVisible(false);
        playButton.setFont(Font.font(LABEL_FONT_SIZE));
        playButton.setOnAction(e -> executeButtonAction(true));

        bluffButton.setFont(Font.font(LABEL_FONT_SIZE));
        bluffButton.setVisible(false);
        bluffButton.setOnAction(e -> executeButtonAction(false));

        buttons.setSpacing(10);
        buttons.setPadding(new Insets(0, 0, 5, 350));
        buttons.getChildren().addAll(playButton, bluffButton);

        gamePlayBox.getChildren().addAll(opponentCardBox, player2Label, middleCardBox, buttons, player1Label, playerCardBox);

        root.getChildren().addAll(leftMenuComponent, gamePlayBox);

        handleCheating(root);

        return root;
    }

    private static void updateConstants(PlayerCardBox playerCardBox, OpponentCardBox opponentCardBox, MiddleCardBox middleCardBox, LeftMenuComponent leftMenuComponent, FaceDownCardBox faceDownCardBox) {
        GameConstants.playerCardBox = playerCardBox;
        GameConstants.opponentCardBox = opponentCardBox;
        GameConstants.middleCardBox = middleCardBox;
        GameConstants.faceDownCardBox = faceDownCardBox;
        GameConstants.leftMenuComponent = leftMenuComponent;
    }

    // handle cheating when pressed ctrl + 9
    private static void handleCheating(Pane root) {
        final KeyCombination keyComb = new KeyCodeCombination(KeyCode.DIGIT9,
                KeyCombination.CONTROL_DOWN);

        root.setOnKeyPressed(keyEvent -> {
            if (keyComb.match(keyEvent)) {
                //System.out.println("Ctrl+9 pressed");

                TableStateDTO cheatedTableState = cheatController.cheat(GameConstants.TABLE_STATE);
                LeftMenuComponent leftMenuComponent =  new LeftMenuComponent(cheatedTableState.getLevel(), cheatedTableState.getCapturedCardsNumberByPlayer1(), cheatedTableState.getCapturedCardsNumberByComputer(),
                        cheatedTableState.getPlayer1LevelScore(), cheatedTableState.getComputerLevelScore(), cheatedTableState.getPlayer1CumulativeScore(), cheatedTableState.getEventType(), cheatedTableState.getTurn());
                new Thread(() -> updateBoxes(cheatedTableState.getPlayer1Cards(), cheatedTableState.getComputerCards().size(), cheatedTableState.getFaceDownCards().size(), cheatedTableState.getFaceUpCards(), leftMenuComponent, cheatedTableState.getTurn(), true, 0)).start();
                // if multi-player game level has been reached and not started to it yet
                if (cheatedTableState.getLevel().equals(MULTIPLAYER_GAME_LEVEL) && !isMultiplayerGameStarted) {
                    StageConstants.stage.getScene().setRoot(MatchMakingScene.drawMatchingScene());
                    multiPlayerGameManager.startMultiPlayerGame();
                }
            }
        });
    }

    /**
     * Makes request to the playCard or bluff APIs according to the pressed button type.
     */
    private static void executeButtonAction(boolean isButtonActionPlayCard) {
        PlayerActionDTO action = new PlayerActionDTO(PICKED_CARD_RANK, PICKED_CARD_SUIT, GameConstants.TOKEN, GameConstants.GAME_ID, GameConstants.PLAYER_ID, PlayerType.PLAYER1, GameConstants.TABLE_STATE);
        TableStateStagesDTO tableStateStagesDTO;
        TableStateDTO prevTableState;
        TableStateDTO tableStateDTO;
        if (isButtonActionPlayCard) {
            // button action is playing card.
            tableStateStagesDTO = new PlayCardController().playCard(action);
        } else {
            // button action is bluffing.
            tableStateStagesDTO = new BluffingPistiController().bluffCard(action);
        }
        // if multi-player game level has been reached and not started to it yet
        if (tableStateStagesDTO.getFinalTableState().getLevel() == MULTIPLAYER_GAME_LEVEL && !isMultiplayerGameStarted) {
            StageConstants.stage.getScene().setRoot(MatchMakingScene.drawMatchingScene());
            multiPlayerGameManager.startMultiPlayerGame();
        } else {
            prevTableState = tableStateStagesDTO.getTableStateBeforeComputeMove();
            tableStateDTO = tableStateStagesDTO.getFinalTableState();
            LeftMenuComponent prevLeftMenuComponent =  new LeftMenuComponent(prevTableState.getLevel(), prevTableState.getCapturedCardsNumberByPlayer1(), prevTableState.getCapturedCardsNumberByComputer(),
                    prevTableState.getPlayer1LevelScore(), prevTableState.getComputerLevelScore(), prevTableState.getPlayer1CumulativeScore(), prevTableState.getEventType(), prevTableState.getTurn());
            LeftMenuComponent leftMenuComponent =  new LeftMenuComponent(tableStateDTO.getLevel(), tableStateDTO.getCapturedCardsNumberByPlayer1(), tableStateDTO.getCapturedCardsNumberByComputer(),
                    tableStateDTO.getPlayer1LevelScore(), tableStateDTO.getComputerLevelScore(), tableStateDTO.getPlayer1CumulativeScore(), tableStateDTO.getEventType(), tableStateDTO.getTurn());
            new Thread(() -> updateBoxes(prevTableState.getPlayer1Cards(), prevTableState.getComputerCards().size(), prevTableState.getFaceDownCards().size(), prevTableState.getFaceUpCards(), prevLeftMenuComponent, prevTableState.getTurn(), false, 0)).start();
            new Thread(() -> updateBoxes(tableStateDTO.getPlayer1Cards(), tableStateDTO.getComputerCards().size(), tableStateDTO.getFaceDownCards().size(), tableStateDTO.getFaceUpCards(), leftMenuComponent, tableStateDTO.getTurn(),  true, 1000)).start();

            /* reset picked card and hide play and bluff buttons */
            PICKED_CARD_RANK = null;
            PICKED_CARD_SUIT = null;
            playButton.setVisible(false);
            bluffButton.setVisible(false);
        }

    }
}
