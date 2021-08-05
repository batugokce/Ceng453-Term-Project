package tr.metu.ceng.construction.client.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import tr.metu.ceng.construction.client.constant.ColorConstants;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.common.MultiplayerEvent;
import tr.metu.ceng.construction.common.PlayerType;
import tr.metu.ceng.construction.common.TableStateForMultiplayer;

/**
 * Scene that will be displayed at the end of multiplayer game
 */
public class GameEndScene {

    private static final int TITLE_FONT_SIZE = 50;
    private static final int DETAIL_FONT_SIZE = 25;

    /**
     * Prepares an element to display in the scene
     *
     * @param tableState final state of table
     * @return a pane to be displayed
     */
    public static Pane draw(TableStateForMultiplayer tableState) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(30);
        root.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.MENU_BACKGROUND_COLOR), CornerRadii.EMPTY, new Insets(0))));

        // title label for the main menu
        Label winnerLabel = new Label();
        winnerLabel.setFont(Font.font(TITLE_FONT_SIZE));
        winnerLabel.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        if (tableState.getEventType().equals(MultiplayerEvent.PLAYER1_WIN)) {
            winnerLabel.setText("Winner: Player1 - " + tableState.getPlayer1Username());
        } else if (tableState.getEventType().equals(MultiplayerEvent.PLAYER2_WIN)) {
            winnerLabel.setText("Winner: Player2 - " + tableState.getPlayer2Username());
        }

        Label player1ScoreLabel = new Label("Player1 Level Score: " + tableState.getPlayer1LevelScore().toString());
        player1ScoreLabel.setFont(Font.font(DETAIL_FONT_SIZE));
        player1ScoreLabel.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        Label player2ScoreLabel = new Label("Player2 Level Score: " + tableState.getPlayer2LevelScore().toString());
        player2ScoreLabel.setFont(Font.font(DETAIL_FONT_SIZE));
        player2ScoreLabel.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        Label overallScoreLabel = new Label();
        overallScoreLabel.setFont(Font.font(DETAIL_FONT_SIZE));
        overallScoreLabel.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        if (GameConstants.WHICH_PLAYER_YOU_ARE.equals(PlayerType.PLAYER1)) {
            overallScoreLabel.setText("Your Overall Score: " + tableState.getPlayer1CumulativeScore());
        } else {
            overallScoreLabel.setText("Your Overall Score: " + tableState.getPlayer2CumulativeScore());
        }

        root.getChildren().addAll(winnerLabel, player1ScoreLabel, player2ScoreLabel, overallScoreLabel);

        return root;
    }
}
