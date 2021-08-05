package tr.metu.ceng.construction.client.component;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import tr.metu.ceng.construction.client.DTO.CardDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a box for the middle/face-up cards
 */
public class MiddleCardBox extends Pane {

    /**
     * Parametrized constructor for the component
     * @param cards that will be in the box
     */
    public MiddleCardBox(List<CardDTO> cards) {
        List<CardUI> cardUIs = cards.stream().map(card -> new CardUI(card, true)).collect(Collectors.toList());
        this.setPrefSize(600, 300);
        this.putOnTopOfEachOther(cardUIs);
        this.getChildren().addAll(cardUIs);
    }

    /**
     * Setter function for the component
     * @param cards that will be inserted to the box
     */
    public void setCards(List<CardDTO> cards) {
        List<CardUI> cardUIs = cards.stream().map(card -> new CardUI(card, true)).collect(Collectors.toList());

        this.getChildren().clear();
        this.putOnTopOfEachOther(cardUIs);
        this.getChildren().addAll(cardUIs);
    }

    private void putOnTopOfEachOther(List<CardUI> cardUIs) {
        if (cardUIs != null) {
            double increaseX = 180.0;
            for (CardUI cardUI : cardUIs) {
                double xPos = (cardUI.getLayoutX() + increaseX);
                increaseX += 5;
                cardUI.setLayoutX(xPos);
                cardUI.setLayoutY(cardUI.getLayoutY() + 50.0);
                //System.out.println(String.format("move right, x: %1.0f", xPos));
            }
        }
    }
}
