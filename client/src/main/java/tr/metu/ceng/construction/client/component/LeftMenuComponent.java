package tr.metu.ceng.construction.client.component;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.client.enums.EventType;

import static tr.metu.ceng.construction.client.constant.GameConstants.COMPUTER_TURN;
import static tr.metu.ceng.construction.client.constant.GameConstants.YOUR_TURN;
import static tr.metu.ceng.construction.client.constant.StageConstants.LABEL_FONT_SIZE;

/**
 * Represents left menu component in the user interface.
 *
 * This component will be used to display some information to user such as level, scores, captured cards etc.
 */
@Getter
@Setter
@NoArgsConstructor
public class LeftMenuComponent extends VBox {
    private Integer level;

    private Integer capturedCardsNumberByPlayer1;

    private Integer capturedCardsNumberByComputer;

    private Integer player1LevelScore;

    private Integer computerLevelScore;

    private Integer player1CumulativeScore;

    private EventType eventType;

    private Integer turn;

    /**
     * Parametrized constructor for this component
     * @param level level of the game
     * @param capturedCardsNumberByPlayer1 determines number of cards captured by player1
     * @param capturedCardsNumberByComputer determines number of cards captured by computer
     * @param player1LevelScore holds level score of player1
     * @param computerLevelScore holds level score of computer
     * @param player1CumulativeScore holds previous level scores of player1
     * @param eventType holds a string that describes an event
     * @param turn determines turn, i.e, player1's turn or computer's turn etc.
     */
    public LeftMenuComponent(Integer level, Integer capturedCardsNumberByPlayer1, Integer capturedCardsNumberByComputer, Integer player1LevelScore, Integer computerLevelScore, Integer player1CumulativeScore, EventType eventType, Integer turn) {
        this.level = level;
        this.capturedCardsNumberByPlayer1 = capturedCardsNumberByPlayer1;
        this.capturedCardsNumberByComputer = capturedCardsNumberByComputer;
        this.player1LevelScore = player1LevelScore;
        this.computerLevelScore = computerLevelScore;
        this.player1CumulativeScore = player1CumulativeScore;
        this.eventType = eventType;
        this.turn = turn;
    }

    /**
     * Setter functions for the component
     * @param newLeftMenuComponent another component that will be copied
     * @param isFinal determines if it is final or not
     */
    public void setAll(LeftMenuComponent newLeftMenuComponent, boolean isFinal) {
        this.capturedCardsNumberByPlayer1 = newLeftMenuComponent.getCapturedCardsNumberByPlayer1();
        this.player1LevelScore = newLeftMenuComponent.getPlayer1LevelScore();
        this.player1CumulativeScore = newLeftMenuComponent.getPlayer1CumulativeScore();
        this.eventType = newLeftMenuComponent.getEventType();
        this.turn = newLeftMenuComponent.getTurn();

        if (isFinal) {
            this.level = newLeftMenuComponent.getLevel();
            this.capturedCardsNumberByComputer = newLeftMenuComponent.getCapturedCardsNumberByComputer();
            this.computerLevelScore = newLeftMenuComponent.getComputerLevelScore();

            // if event type does not change after computer move, set event type to null
            if (this.eventType != null && newLeftMenuComponent.getEventType().equals(EventType.PLAYER1_CAPTURED)) {
                this.eventType = null;
            }
        }
        this.getChildren().clear();
        this.createComponents();
    }

    private void createComponents() {
        HBox playerCapturedCardsNumberComponent = new HBox();
        Label playerCapturedCardsNumberLabel = new Label();
        playerCapturedCardsNumberLabel.setText("Your captured card #: ");
        Label playerCapturedCardsNumber = new Label();
        playerCapturedCardsNumber.setText(this.capturedCardsNumberByPlayer1.toString());
        playerCapturedCardsNumberComponent.setSpacing(10);
        playerCapturedCardsNumberComponent.getChildren().addAll(playerCapturedCardsNumberLabel, playerCapturedCardsNumber);

        HBox playerLevelScoreComponent = new HBox();
        Label playerLevelScoreLabel = new Label();
        playerLevelScoreLabel.setText("Your level score: ");
        Label playerLevelScore = new Label();
        playerLevelScore.setText(this.player1LevelScore.toString());
        playerLevelScoreComponent.setSpacing(10);
        playerLevelScoreComponent.getChildren().addAll(playerLevelScoreLabel, playerLevelScore);

        HBox levelComponent = new HBox();
        Label levelLabel = new Label();
        levelLabel.setText("Level: ");
        Label levelVariable = new Label();
        levelVariable.setText(this.level.toString());
        levelComponent.setSpacing(10);
        levelComponent.getChildren().addAll(levelLabel, levelVariable);

        HBox computerCapturedCardsNumberComponent = new HBox();
        Label computerCapturedCardsNumberLabel = new Label();
        computerCapturedCardsNumberLabel.setText("Computer's captured card #: ");
        Label computerCapturedCardsNumber = new Label();
        computerCapturedCardsNumber.setText(this.capturedCardsNumberByComputer.toString());
        computerCapturedCardsNumberComponent.setSpacing(10);
        computerCapturedCardsNumberComponent.getChildren().addAll(computerCapturedCardsNumberLabel, computerCapturedCardsNumber);

        HBox computerLevelScoreComponent = new HBox();
        Label computerLevelScoreLabel = new Label();
        computerLevelScoreLabel.setText("Computer level score: ");
        Label computerLevelScoreVariable = new Label();
        computerLevelScoreVariable.setText(this.computerLevelScore.toString());
        computerLevelScoreComponent.setSpacing(10);
        computerLevelScoreComponent.getChildren().addAll(computerLevelScoreLabel, computerLevelScoreVariable);

        HBox playerCumulativeScoreComponent = new HBox();
        Label playerCumulativeScoreLabel = new Label();
        playerCumulativeScoreLabel.setText("Your previous score: ");
        Label playerCumulativeScore = new Label();
        playerCumulativeScore.setText(this.getPlayer1CumulativeScore().toString());
        playerCumulativeScoreComponent.setSpacing(10);
        playerCumulativeScoreComponent.getChildren().addAll(playerCumulativeScoreLabel, playerCumulativeScore);

        Label turnLabel =  new Label();
        turnLabel.setFont(Font.font(LABEL_FONT_SIZE));
        turnLabel.setPadding(new Insets(20, 10, 10, 10));
        if (this.eventType != null && this.eventType.isGameOver()) {
            // do nothing
        }
        else if (this.turn == 1) {
            // player turn
            turnLabel.setText(YOUR_TURN);

        } else if (this.turn == 2) {
            // computer turn
            turnLabel.setText(COMPUTER_TURN);
        }

        Label eventTypeLabel = new Label();
        eventTypeLabel.setFont(Font.font(LABEL_FONT_SIZE));
        eventTypeLabel.setPadding(new Insets(10, 10, 10, 10));
        if (this.eventType != null) {
            eventTypeLabel.setText(this.eventType.toString());
        }

        this.getChildren().addAll(levelComponent, playerCapturedCardsNumberComponent, computerCapturedCardsNumberComponent,
                playerLevelScoreComponent, computerLevelScoreComponent, playerCumulativeScoreComponent, turnLabel, eventTypeLabel, GameConstants.faceDownCardBox);
    }
}
