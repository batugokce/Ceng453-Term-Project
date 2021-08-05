package tr.metu.ceng.construction.client.scene.utils;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tr.metu.ceng.construction.client.DTO.CardDTO;
import tr.metu.ceng.construction.client.DTO.PlayerActionDTO;
import tr.metu.ceng.construction.client.DTO.TableStateStagesDTO;
import tr.metu.ceng.construction.client.component.LeftMenuComponent;
import tr.metu.ceng.construction.client.constant.ColorConstants;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.client.constant.StageConstants;
import tr.metu.ceng.construction.client.controller.LoginController;
import tr.metu.ceng.construction.client.controller.RegisterController;
import tr.metu.ceng.construction.client.scene.MainMenuScene;

import java.util.List;
import java.util.Set;

import static tr.metu.ceng.construction.client.constant.MessageConstants.SUCCESSFUL_LOGIN;
import static tr.metu.ceng.construction.client.constant.MessageConstants.SUCCESSFUL_REGISTRATION;

public class CommonItems {

    private static final int LABEL_FONT_SIZE = 15;
    private static final int ELEMENT_SPACING = 10;
    private static final int LABEL_INFO_WIDTH = 400;

    private static final LoginController loginController = new LoginController();
    private static final RegisterController registerController = new RegisterController();

    /**
     * Handles a POST HTTP request and sets table states in game constants.
     * @param action this is the action made by the player
     * @param webClient this is the created web client with the target URL
     * @return a DTO which includes before table state and final table state
     */
    public static TableStateStagesDTO getTableStateStagesDTO(PlayerActionDTO action, WebClient webClient) {
        action.getTableState().setEventType(null); // send it as null so that if any new changes occur, we can realize it.
        TableStateStagesDTO tableStateStagesDTO =  webClient.post()
                .body(Mono.just(action), PlayerActionDTO.class)
                .retrieve()
                .bodyToMono(TableStateStagesDTO.class)
                .block();

        if (null != tableStateStagesDTO) {
            GameConstants.BEFORE_TABLE_STATE = tableStateStagesDTO.getTableStateBeforeComputeMove();
            GameConstants.TABLE_STATE = tableStateStagesDTO.getFinalTableState();
        }

        return tableStateStagesDTO;
    }

    public static Pane createAuthenticationScene(String title, int titleFontSize, int labelFontSize, Font buttonFont, boolean isRegistrationScene) {
        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(30);
        menu.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.MENU_BACKGROUND_COLOR), CornerRadii.EMPTY, new Insets(0))));

        // Create a pane and set its properties
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER); // Set center alignment
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5); // Set vGap to 5.5px

        // Place nodes in the pane
        Label label = new Label("Username:");
        label.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));
        label.setFont(Font.font(labelFontSize));
        pane.add(label, 0, 0);
        TextField usernameTextField = new TextField();
        pane.add(usernameTextField, 1, 0);

        Label label2 = new Label("Password:");
        label2.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));
        label2.setFont(Font.font(labelFontSize));
        pane.add(label2, 0, 1);

        // text field to show password as unmasked
        final TextField passwordTextField = new TextField();
        pane.add(passwordTextField, 1, 1);
        // Set initial state
        passwordTextField.setManaged(false);
        passwordTextField.setVisible(false);

        // Actual password field
        final PasswordField passwordField = new PasswordField();
        pane.add(passwordField, 1, 1);

        CheckBox checkBox = new CheckBox("Show/Hide password");
        checkBox.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));
        pane.add(checkBox, 0, 2);

        // Bind properties. Display only one component (textField or passwordField)
        // on the scene at a time.
        passwordTextField.managedProperty().bind(checkBox.selectedProperty());
        passwordTextField.visibleProperty().bind(checkBox.selectedProperty());

        passwordField.managedProperty().bind(checkBox.selectedProperty().not());
        passwordField.visibleProperty().bind(checkBox.selectedProperty().not());

        // Bind the textField and passwordField text values bidirectionally.
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());

        TextField emailTextField = null;
        if (isRegistrationScene) {
            Label label3 = new Label("Email Address:");
            label3.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));
            label3.setFont(Font.font(labelFontSize));
            pane.add(label3, 0, 3);
            emailTextField = new TextField();
            pane.add(emailTextField, 1, 3);
        }

        // prepare info label
        Label infoLabel = new Label();
        infoLabel.setFont(Font.font(LABEL_FONT_SIZE));
        infoLabel.setAlignment(Pos.CENTER);
        infoLabel.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));
        infoLabel.setPrefWidth(LABEL_INFO_WIDTH + ELEMENT_SPACING);

        Button button = new Button(title);
        button.setFont(buttonFont);
        if (isRegistrationScene) {
            TextField finalEmailTextField = emailTextField;
            button.setOnAction(e -> {
                if (usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty() || finalEmailTextField.getText().isEmpty()) {
                    //show error  message
                    processResponse(infoLabel, "Please fill all the fields!");
                } else {
                    String responseMessage = registerController.register(usernameTextField.getText(), passwordTextField.getText(), finalEmailTextField.getText());
                    //show response message on the screen
                    processResponse(infoLabel, responseMessage);
                    if (responseMessage.equals(SUCCESSFUL_REGISTRATION)) {
                        //redirect to the main screen
                        StageConstants.stage.getScene().setRoot(MainMenuScene.draw());
                    }

                }
            });
        } else {
            button.setOnAction(e -> {
                if (usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
                    //show error  message
                    processResponse(infoLabel, "Please fill all the fields!");
                } else {
                    String responseMessage = loginController.login(usernameTextField.getText(), passwordTextField.getText());
                    //show response message on the screen
                    processResponse(infoLabel, responseMessage);
                    if (responseMessage.equals(SUCCESSFUL_LOGIN)) {
                        //redirect to the home screen where player can start a new game
                        StageConstants.stage.getScene().setRoot(MainMenuScene.createStartScene());
                    }
                }
            });
        }

        Button backButton = new Button("Back");
        backButton.setFont(buttonFont);
        backButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(MainMenuScene.draw()));

        HBox hBox = new HBox(15); // Create an HBox with 15px spacing
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(15, 15, 15, 15));
        hBox.getChildren().add(button);
        hBox.getChildren().add(backButton);

        // title label for the register menu
        Label labelTitle = new Label(title);
        labelTitle.setFont(Font.font(titleFontSize));
        labelTitle.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        menu.getChildren().addAll(labelTitle, pane, hBox, infoLabel);

        return menu;
    }

    public static void updateBoxes(Set<CardDTO> playerCardDTOS, int opponentCardsNumber, int faceDownCardsNumber, List<CardDTO> faceUpCardDTOList, LeftMenuComponent leftMenuComponent, Integer turn, boolean isFinal, int sleepAmount) {
        try {
            Thread.yield();
            Thread.sleep(sleepAmount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            GameConstants.playerCardBox.setCards(playerCardDTOS, turn);
            GameConstants.opponentCardBox.setNOfCards(opponentCardsNumber);
            GameConstants.middleCardBox.setCards(faceUpCardDTOList);
            if (isFinal) GameConstants.faceDownCardBox.setNumberOfCards(faceDownCardsNumber);
            GameConstants.leftMenuComponent.setAll(leftMenuComponent, isFinal);
        });
    }

    private static void processResponse(Label label, String text) {
        // when a response comes from the server, it is processed in this method.
        label.setText(text);
    }
}
