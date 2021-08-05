package tr.metu.ceng.construction.client.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import static tr.metu.ceng.construction.client.constant.StageConstants.LABEL_FONT_SIZE;

/**
 * Represents face-down card box component in the user interface.
 *
 * This component will be used to display the face-down cards as closed deck and their size
 */
public class FaceDownCardBox extends VBox {
    private Integer numberOfCards;

    /**
     * Parametrized constructor for the component
     * @param numberOfCards that will be in the box
     */
    public FaceDownCardBox(Integer numberOfCards) {
        this.numberOfCards = numberOfCards;
        draw();
    }

    /**
     * Setter function for the component
     * @param nOfCards that will be inserted to the box
     */
    public void setNumberOfCards(int nOfCards) {
        this.numberOfCards = nOfCards;
        this.getChildren().clear();
        draw();
    }

    private void draw() {
        StackPane stackCards = new StackPane();
        stackCards.setPrefSize(400, 120);
        stackCards.setAlignment(Pos.CENTER);

        // add a representative card to stack pane
        stackCards.getChildren().add(new CardUI());

        // add size to below as label
        Label numberLabel = new Label();
        numberLabel.setText(this.numberOfCards.toString());
        numberLabel.setAlignment(Pos.CENTER);

        // add informative label
        Label infoLabel = new Label();
        infoLabel.setText("Face-Down Cards");
        infoLabel.setFont(Font.font(LABEL_FONT_SIZE));
        infoLabel.setAlignment(Pos.CENTER);

        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(40, 10, 10, 10));
        this.getChildren().addAll(infoLabel, stackCards, numberLabel);
    }
}
