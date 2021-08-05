package tr.metu.ceng.construction.client.scene;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import reactor.core.publisher.Mono;
import tr.metu.ceng.construction.client.constant.ColorConstants;
import tr.metu.ceng.construction.client.constant.StageConstants;
import tr.metu.ceng.construction.client.controller.ForgotPasswordController;

/**
 * Responsible for preparing scene of 'Forgot password' interface
 */
public class ForgotPasswordScene {

    private static final Font buttonFont = new Font(14);
    private static final int BUTTON_WIDTH = 140;
    private static final int BUTTON_HEIGHT = 40;
    private static final int LABEL_FONT_SIZE = 20;
    private static final int LABEL_WIDTH = 60;
    private static final int FIELD_HEIGHT = 30;
    private static final int ELEMENT_SPACING = 20;

    /**
     * Prepares a pane as a root for 'Forgot Password' scene.
     *
     * @return a pane to use in that scene
     */
    public static Pane draw() {
        // main pane for the scene
        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(30);
        menu.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.MENU_BACKGROUND_COLOR), CornerRadii.EMPTY, new Insets(0))));

        // prepare e-mail row
        Label emailLabel = new Label("E-mail");
        emailLabel.setFont(Font.font(LABEL_FONT_SIZE));
        emailLabel.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));
        emailLabel.setPrefWidth(LABEL_WIDTH);

        TextField emailField = new TextField();
        emailField.setPrefHeight(FIELD_HEIGHT);
        emailField.setPrefWidth(BUTTON_WIDTH*2 - LABEL_WIDTH);

        HBox emailInputBox = new HBox();
        emailInputBox.setAlignment(Pos.CENTER);
        emailInputBox.setSpacing(ELEMENT_SPACING);
        emailInputBox.getChildren().addAll(emailLabel, emailField);

        // prepare info label
        Label infoLabel = new Label();
        infoLabel.setFont(Font.font(LABEL_FONT_SIZE));
        infoLabel.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));
        infoLabel.setPrefWidth(2*BUTTON_WIDTH + ELEMENT_SPACING);

        // prepare two buttons and their actions
        Button backButton = new Button("Back");
        backButton.setFont(buttonFont);
        backButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        backButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(MainMenuScene.draw()));

        Button sendMailButton = new Button("Send Mail");
        sendMailButton.setFont(buttonFont);
        sendMailButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        sendMailButton.setOnAction(e -> {
            infoLabel.setText("Please wait..");
            Mono<Boolean> responseMono = new ForgotPasswordController().forgotPassword(emailField.getText());
            responseMono.subscribe(response -> processResponse(response, infoLabel));
        });

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(ELEMENT_SPACING);
        buttonBox.getChildren().addAll(backButton, sendMailButton);

        // add all children to parent menu element
        menu.getChildren().addAll(emailInputBox, buttonBox, infoLabel);
        return menu;
    }

    private static void processResponse(boolean response, Label label) {
        // when a response comes from the server, it is processed in this method.
        if (response) {
            Platform.runLater(() -> label.setText("E-mail has been sent successfully."));
        } else {
            Platform.runLater(() -> label.setText("No account has been found."));
        }
    }
}
