package tr.metu.ceng.construction.client.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import tr.metu.ceng.construction.client.constant.ColorConstants;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.client.constant.StageConstants;
import tr.metu.ceng.construction.client.controller.LeaderBoardController;

/**
 * Responsible for preparing a scene of main menu
 */
public class MainMenuScene {

    private static final int BUTTON_WIDTH = 180;
    private static final int BUTTON_HEIGHT = 40;
    private static final int TITLE_FONT_SIZE = 50;
    private static final String TITLE = "Pişti Card Game";
    private static final Font buttonFont = new Font(14);
    private static final LeaderBoardController leaderBoardController = new LeaderBoardController();

    /**
     * Prepares a pane as a root element for main menu scene
     *
     * @return a pane to use in that scene
     */
    public static Pane draw() {
        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(30);
        menu.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.MENU_BACKGROUND_COLOR), CornerRadii.EMPTY, new Insets(0))));

        // title label for the main menu
        Label label = new Label(TITLE);
        label.setFont(Font.font(TITLE_FONT_SIZE));
        label.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        // prepare four menu buttons below; login, register, forgot password and leaderboard
        Button loginButton = new Button("Login");
        loginButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        loginButton.setFont(buttonFont);
        loginButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(LoginScene.draw()));

        Button registerButton = new Button("Register");
        registerButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        registerButton.setFont(buttonFont);
        registerButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(RegisterScene.draw()));

        Button forgotPassButton = new Button("Forgot Password");
        forgotPassButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        forgotPassButton.setFont(buttonFont);
        forgotPassButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(ForgotPasswordScene.draw()));

        Button leaderboardButton = new Button("Leaderboard");
        leaderboardButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        leaderboardButton.setFont(buttonFont);
        leaderboardButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(LeaderboardScene.start()));

        menu.getChildren().addAll(label, loginButton, registerButton, forgotPassButton, leaderboardButton);
        return menu;
    }

    /**
     * This method is used for creating a pane that will be shown after a successful login.
     * The scene consists of play and log-out buttons.
     * @return Main Menu Pane
     */
    public static Pane createStartScene() {
        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(30);
        menu.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.MENU_BACKGROUND_COLOR), CornerRadii.EMPTY, new Insets(0))));

        // title label for the main menu
        Label label = new Label(TITLE);
        label.setFont(Font.font(TITLE_FONT_SIZE));
        label.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        Label welcomeLabel =  new Label();
        if (GameConstants.USERNAME != null) {
            // title label for the main menu
            welcomeLabel = new Label("Hello, " + GameConstants.USERNAME);
            welcomeLabel.setFont(Font.font(TITLE_FONT_SIZE - 15));
            welcomeLabel.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));
        } else {
            System.out.println("error in login");
        }

        // prepare menu buttons below
        Button playButton = new Button("Play");
        playButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        playButton.setFont(buttonFont);
        playButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(GameScene.draw()));

        Button logoutButton = new Button("Logout");
        logoutButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        logoutButton.setFont(buttonFont);
        logoutButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(MainMenuScene.draw()));

        menu.getChildren().addAll(label, welcomeLabel, playButton, logoutButton);
        return menu;
    }
}
