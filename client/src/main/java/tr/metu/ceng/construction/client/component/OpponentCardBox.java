package tr.metu.ceng.construction.client.component;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a box that holds cards of the opponent player.
 */
@Getter
public class OpponentCardBox extends HBox {

    private int nOfCards;

    /**
     * Constructor for the component. It just needs the number of the cards that will be inserted in this box
     * @param nOfCards number of cards
     */
    public OpponentCardBox(int nOfCards) {
        this.setPrefSize(600, 150);
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.nOfCards = nOfCards;

        List<CardUI> cards = Stream.generate(CardUI::new).limit(nOfCards).collect(Collectors.toList());
        this.getChildren().addAll(cards);
    }

    /**
     * Decrements number of cards by one
     */
    public void decrement() {
        this.nOfCards--;
        this.getChildren().remove(this.getChildren().size()-1);
    }

    /**
     * Increments number of cards by one
     */
    public void increment() {
        this.nOfCards++;
        this.getChildren().add(new CardUI());
    }

    /**
     * Setter function for the component
     * @param nOfCards number of cards that will be inserted in the box
     */
    public void setNOfCards(int nOfCards) {
        this.nOfCards = 0;
        this.getChildren().clear();

        for (int i = 0; i < nOfCards; i++) {
            this.increment();
        }
    }

}
