package tr.metu.ceng.construction.client.component;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.common.MultiplayerEvent;

import static tr.metu.ceng.construction.client.constant.GameConstants.PLAYER1_TURN;
import static tr.metu.ceng.construction.client.constant.GameConstants.PLAYER2_TURN;
import static tr.metu.ceng.construction.client.constant.StageConstants.LABEL_FONT_SIZE;

@Getter
@Setter
@NoArgsConstructor
public class LeftMenuForMultiplayer extends VBox {
    private final Integer level = 4;

    private Integer capturedCardsNumberByPlayer1;

    private Integer capturedCardsNumberByPlayer2;

    private Integer player1LevelScore;

    private Integer player2LevelScore;

    private Integer player1CumulativeScore;

    private Integer player2CumulativeScore;

    private MultiplayerEvent eventType;

    private Integer turn;

    public LeftMenuForMultiplayer(Integer capturedCardsNumberByPlayer1, Integer capturedCardsNumberByPlayer2, Integer player1LevelScore, Integer player2LevelScore, Integer player1CumulativeScore, Integer player2CumulativeScore, MultiplayerEvent eventType, Integer turn) {
        this.capturedCardsNumberByPlayer1 = capturedCardsNumberByPlayer1;
        this.capturedCardsNumberByPlayer2 = capturedCardsNumberByPlayer2;
        this.player1LevelScore = player1LevelScore;
        this.player2LevelScore = player2LevelScore;
        this.player1CumulativeScore = player1CumulativeScore;
        this.player2CumulativeScore = player2CumulativeScore;
        this.eventType = eventType;
        this.turn = turn;
    }

    public void setAll(LeftMenuForMultiplayer leftMenu) {
        this.capturedCardsNumberByPlayer1 = leftMenu.capturedCardsNumberByPlayer1;
        this.capturedCardsNumberByPlayer2 = leftMenu.capturedCardsNumberByPlayer2;
        this.player1LevelScore = leftMenu.player1LevelScore;
        this.player2LevelScore = leftMenu.player2LevelScore;
        this.player1CumulativeScore = leftMenu.player1CumulativeScore;
        this.player2CumulativeScore = leftMenu.player2CumulativeScore;
        this.eventType = leftMenu.eventType;
        this.turn = leftMenu.turn;

        this.getChildren().clear();
        createComponents();
    }

    private void createComponents() {
        HBox levelComponent = new HBox();
        Label levelLabel = new Label();
        levelLabel.setText("Level: ");
        Label levelVariable = new Label();
        levelVariable.setText(this.level.toString());
        levelComponent.setSpacing(10);
        levelComponent.getChildren().addAll(levelLabel, levelVariable);

        HBox player1CapturedCardsNumberComponent = new HBox();
        Label player1CapturedCardsNumberLabel = new Label();
        player1CapturedCardsNumberLabel.setText("Player1 captured card #: ");
        Label playerCapturedCardsNumber = new Label();
        playerCapturedCardsNumber.setText(this.capturedCardsNumberByPlayer1.toString());
        player1CapturedCardsNumberComponent.setSpacing(10);
        player1CapturedCardsNumberComponent.getChildren().addAll(player1CapturedCardsNumberLabel, playerCapturedCardsNumber);

        HBox player2CapturedCardsNumberComponent = new HBox();
        Label player2CapturedCardsNumberLabel = new Label();
        player2CapturedCardsNumberLabel.setText("Player2 captured card #: ");
        Label player2CapturedCardsNumber = new Label();
        player2CapturedCardsNumber.setText(this.capturedCardsNumberByPlayer2.toString());
        player2CapturedCardsNumberComponent.setSpacing(10);
        player2CapturedCardsNumberComponent.getChildren().addAll(player2CapturedCardsNumberLabel, player2CapturedCardsNumber);

        HBox player1LevelScoreComponent = new HBox();
        Label player1LevelScoreLabel = new Label();
        player1LevelScoreLabel.setText("Player1 level score: ");
        Label player1LevelScore = new Label();
        player1LevelScore.setText(this.player1LevelScore.toString());
        player1LevelScoreComponent.setSpacing(10);
        player1LevelScoreComponent.getChildren().addAll(player1LevelScoreLabel, player1LevelScore);

        HBox player2LevelScoreComponent = new HBox();
        Label player2LevelScoreLabel = new Label();
        player2LevelScoreLabel.setText("Player2 level score: ");
        Label player2LevelScoreVariable = new Label();
        player2LevelScoreVariable.setText(this.player2LevelScore.toString());
        player2LevelScoreComponent.setSpacing(10);
        player2LevelScoreComponent.getChildren().addAll(player2LevelScoreLabel, player2LevelScoreVariable);

        HBox player1CumulativeScoreComponent = new HBox();
        Label player1CumulativeScoreLabel = new Label();
        player1CumulativeScoreLabel.setText("Player1 previous score: ");
        Label player1CumulativeScore = new Label();
        player1CumulativeScore.setText(this.player1CumulativeScore.toString());
        player1CumulativeScoreComponent.setSpacing(10);
        player1CumulativeScoreComponent.getChildren().addAll(player1CumulativeScoreLabel, player1CumulativeScore);

        HBox player2CumulativeScoreComponent = new HBox();
        Label player2CumulativeScoreLabel = new Label();
        player2CumulativeScoreLabel.setText("Player2 previous score: ");
        Label player2CumulativeScore = new Label();
        player2CumulativeScore.setText(this.player2CumulativeScore.toString());
        player2CumulativeScoreComponent.setSpacing(10);
        player2CumulativeScoreComponent.getChildren().addAll(player2CumulativeScoreLabel, player2CumulativeScore);

        Label turnLabel =  new Label();
        turnLabel.setFont(Font.font(LABEL_FONT_SIZE));
        turnLabel.setPadding(new Insets(20, 10, 10, 10));
        if (turn == 1) {
            turnLabel.setText(PLAYER1_TURN);
        } else if (turn == 2) {
            turnLabel.setText(PLAYER2_TURN);
        }

        Label eventTypeLabel = new Label();
        eventTypeLabel.setFont(Font.font(LABEL_FONT_SIZE));
        eventTypeLabel.setPadding(new Insets(10, 10, 10, 10));
        if (eventType != null) {
            eventTypeLabel.setText(eventType.toString());
        }

        this.getChildren().addAll(levelComponent, player1CapturedCardsNumberComponent, player2CapturedCardsNumberComponent,
                player1LevelScoreComponent, player2LevelScoreComponent, player1CumulativeScoreComponent, player2CumulativeScoreComponent,  turnLabel, eventTypeLabel, GameConstants.faceDownCardBox);
    }

}
