package tr.metu.ceng.construction.client.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import tr.metu.ceng.construction.client.constant.ColorConstants;

public class MatchMakingScene {

    private static final String WAIT_LABEL_TEXT = "Congrats! You have reached multi-player level.\nPlease wait for matching with another player...";
    private static final String MATCHED_LABEL_TEXT = "You have matched with ";
    private static final String PREPARING_LABEL_TEXT = "\nGame is being prepared...";
    private static final int FONT_SIZE = 20;


    public static Pane drawMatchingScene() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(30);
        root.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.MENU_BACKGROUND_COLOR), CornerRadii.EMPTY, new Insets(0))));

        Label waitLabel = new Label();
        waitLabel.setText(WAIT_LABEL_TEXT);
        waitLabel.setFont(Font.font(FONT_SIZE));
        waitLabel.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));
        waitLabel.setPadding(new Insets(10, 10, 10, 10));

        HBox waitingHBox = new HBox(10);
        ProgressIndicator progressIndicator = new ProgressIndicator();
        waitingHBox.getChildren().add(progressIndicator);
        waitingHBox.setPadding(new Insets(20, 10, 10, 20));
        waitingHBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(waitLabel, waitingHBox);
        return root;
    }

    public static Pane drawMatchedScene(String player2Username) {
        HBox root = new HBox();
        root.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.MENU_BACKGROUND_COLOR), CornerRadii.EMPTY, new Insets(0))));
        root.setAlignment(Pos.CENTER);

        Label matchedLabel = new Label();
        matchedLabel.setText(MATCHED_LABEL_TEXT + player2Username + PREPARING_LABEL_TEXT);
        matchedLabel.setFont(Font.font(FONT_SIZE));
        matchedLabel.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));
        matchedLabel.setPadding(new Insets(10, 10, 10, 10));
        matchedLabel.setAlignment(Pos.CENTER);

        root.getChildren().add(matchedLabel);
        return root;
    }

}
