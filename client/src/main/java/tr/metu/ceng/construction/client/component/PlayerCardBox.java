package tr.metu.ceng.construction.client.component;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import tr.metu.ceng.construction.client.DTO.CardDTO;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.client.scene.MultiPlayerGameScene;

import java.util.Set;
import java.util.stream.Collectors;

import static tr.metu.ceng.construction.client.constant.GameConstants.isMultiplayerGameStarted;

/**
 * Represents a box for the player cards in the user interface
 */
public class PlayerCardBox extends HBox {

    /**
     * Constructor for the component
     * @param cards that will be inserted in the box
     */
    public PlayerCardBox(Set<CardDTO> cards) {
        Set<CardUI> cardUIs = cards.stream().map(card -> new CardUI(card, false)).collect(Collectors.toSet());
        this.setPrefSize(600, 150);
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(cardUIs);
    }

    public PlayerCardBox(Set<CardDTO> cards, boolean areCardsDisabled) {
        Set<CardUI> cardUIs = cards.stream().map(card -> new CardUI(card, false)).collect(Collectors.toSet());
        cardUIs.forEach(cardUI -> cardUI.setDisable(areCardsDisabled));
        this.setPrefSize(600, 150);
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(cardUIs);
    }

    /**
     * Removes a card from the box.
     * When the player plays a card, this method is called.
     * @param cardDTO that is played by the player
     */
    public void playCard(CardDTO cardDTO) {
        this.getChildren().removeIf(cardUI -> ((CardUI) cardUI).getCard().equals(cardDTO));
    }

    /**
     * Setter function for the component
     * @param cards that will be inserted in the box
     * @param turn determines which player's turn is it
     */
    public void setCards(Set<CardDTO> cards, Integer turn) {
        Set<CardUI> cardUIs = cards.stream().map(card -> new CardUI(card, false)).collect(Collectors.toSet());

        if (turn == 1) {
            // own turn
            if (isMultiplayerGameStarted && GameConstants.isMultiplayerGameAndBluffingChallengeTurn) {
                // bluffing mode on
                cardUIs.forEach(cardUI -> cardUI.setDisable(true));
            }
            else {
                cardUIs.forEach(cardUI -> cardUI.setDisable(false));
            }
        } else if (turn == 2){
            // component turn
            cardUIs.forEach(cardUI -> cardUI.setDisable(true));
        }

        this.getChildren().clear();
        this.getChildren().addAll(cardUIs);
    }


}
