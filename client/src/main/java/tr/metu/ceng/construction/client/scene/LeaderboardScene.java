package tr.metu.ceng.construction.client.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import tr.metu.ceng.construction.client.DTO.ScoreDTO;
import tr.metu.ceng.construction.client.constant.ColorConstants;
import tr.metu.ceng.construction.client.constant.RequestConstants;
import tr.metu.ceng.construction.client.constant.StageConstants;
import tr.metu.ceng.construction.client.scene.utils.TableViewHelper;

/**
 * Responsible for preparing a scene of leaderboard table view
 */
public class LeaderboardScene {

    private static final Font buttonFont = new Font(14);
    private static final String title = "Click one of the below for displaying leaderboard table";
    private static final String ALL_TIMES = "All Times";
    private static final String WEEKLY = "Weekly";
    private static final String MONTHLY = "Monthly";
    private static final int TITLE_FONT_SIZE = 20;

    /**
     * Prepares a pane as a root element for leaderboard scene
     *
     * @return a pane including a title and buttons for redirecting to a table view scene for showing
     * weekly, monthly, or all times scores.
     */
    public static Pane start() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(30);
        root.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.MENU_BACKGROUND_COLOR), CornerRadii.EMPTY, new Insets(0))));

        //Create an HBox with 15px spacing
        HBox buttonsDisplay = new HBox(15);
        buttonsDisplay.setAlignment(Pos.CENTER);
        buttonsDisplay.setPadding(new Insets(15, 15, 15, 15));

        // title label for the register menu
        Label labelTitle = new Label(title);
        labelTitle.setFont(Font.font(TITLE_FONT_SIZE));
        labelTitle.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        // buttons for corresponding date range
        Button allTimesButton = new Button(ALL_TIMES);
        allTimesButton.setFont(buttonFont);
        allTimesButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(createTableViewByButtonLabel(ALL_TIMES)));

        Button weeklyButton = new Button(WEEKLY);
        weeklyButton.setFont(buttonFont);
        weeklyButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(createTableViewByButtonLabel(WEEKLY)));

        Button monthlyButton = new Button(MONTHLY);
        monthlyButton.setFont(buttonFont);
        monthlyButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(createTableViewByButtonLabel(MONTHLY)));

        Button backButton = new Button("Back");
        backButton.setFont(buttonFont);
        backButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(MainMenuScene.draw()));

        buttonsDisplay.getChildren().addAll(allTimesButton, weeklyButton, monthlyButton);
        root.getChildren().addAll(labelTitle, buttonsDisplay , backButton);
        return root;
    }

    /**
     * Creates a table view of ScoreDTOs according to the clicked button label.
     *
     * @param scoreListType this is the button label representing the date range of desired type of listing scores
     * @return a pane including a sortable table with the columns such as username, score and created date.
     */
    @SuppressWarnings("unchecked")
    public static Pane createTableViewByButtonLabel(String scoreListType) {
        // Create a TableView with a list of ScoreDTOs
        TableView<ScoreDTO> table = new TableView<>();
        // Add rows to the TableView
        table.getItems().addAll(TableViewHelper.getScoreDTOListByScoreListType(convertToURI(scoreListType)));

        // Add columns to the TableView
        table.getColumns().addAll(TableViewHelper.getNumberColumn(), TableViewHelper.getUsernameColumn(), TableViewHelper.getScoreColumn(),
                TableViewHelper.getCreatedDateColumn());

        // Set the column resize policy to constrained resize policy
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set the Placeholder for an empty table
        table.setPlaceholder(new Label("No visible columns and/or data exist."));

        Button backButton = new Button("Back");
        backButton.setFont(buttonFont);
        backButton.setOnAction(e -> StageConstants.stage.getScene().setRoot(start()));

        // title label for the register menu
        Label labelTitle = new Label(scoreListType);
        labelTitle.setFont(Font.font(TITLE_FONT_SIZE - 5));
        labelTitle.setTextFill(Paint.valueOf(ColorConstants.ALMOST_WHITE));

        // Create the VBox
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(30);
        root.setBackground(new Background(new BackgroundFill(Paint.valueOf(ColorConstants.MENU_BACKGROUND_COLOR), CornerRadii.EMPTY, new Insets(0))));
        // Add the Table, Label and Back button to the VBox
        root.getChildren().addAll(labelTitle, table, backButton);
        // Set the Padding and Border for the VBox
        root.setStyle("-fx-padding: 30;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;");

        return root;
    }

    // converts the label type to the API request URI portion
    private static String convertToURI(String scoreListType) {
        return switch (scoreListType) {
            case (ALL_TIMES) -> RequestConstants.ALL_TIMES;
            case (WEEKLY) -> RequestConstants.WEEKLY;
            case (MONTHLY) -> RequestConstants.MONTHLY;
            default -> null;
        };
    }
}
