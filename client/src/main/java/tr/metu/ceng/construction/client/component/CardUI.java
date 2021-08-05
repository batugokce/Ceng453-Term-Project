package tr.metu.ceng.construction.client.component;

import javafx.scene.control.Button;
import lombok.Getter;
import tr.metu.ceng.construction.client.DTO.CardDTO;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.client.scene.MultiPlayerGameScene;

import static tr.metu.ceng.construction.client.scene.GameScene.bluffButton;
import static tr.metu.ceng.construction.client.scene.GameScene.playButton;

/**
 * Represents a card object in the user interface.
 */
@Getter
public class CardUI extends Button {

    private CardDTO card;

    /**
     * Constructor for closed card
     */
    public CardUI() {
        this.setText("Card");
        this.setPrefSize(80, 120);
        this.setDisable(true);
        this.setOpacity(1);
    }

    /**
     * Parametrized constructor
     * @param card is the card dto object
     * @param disable determines whether this button is disabled or not
     */
    public CardUI(CardDTO card, boolean disable) {
        this.card = card;
        this.setText(card.getSuit() + "\n" + card.getRank());
        this.setPrefSize(80, 120);
        this.setDisable(disable);
        this.setOpacity(1);
    }

    /**
     * Sets picked card's rank and suit as a game constant.
     */
    @Override
    public void fire() {
        GameConstants.PICKED_CARD_RANK = this.card.getRank();
        GameConstants.PICKED_CARD_SUIT = this.card.getSuit();

        //if a card has picked, make visible the play button
        if (GameConstants.isMultiplayerGameStarted) {
            MultiPlayerGameScene.playButton.setVisible(true);
        } else {
            playButton.setVisible(true);
        }

        if (GameConstants.isMultiplayerGameStarted && GameConstants.TABLE_STATE_MULTIPLAYER.canBluff()) {
            MultiPlayerGameScene.bluffButton.setVisible(true);
        } else if (GameConstants.TABLE_STATE.isGameBluffingPistiAndCanBluff()) {
            //if a card has picked, game is in bluffing mode and player can bluff, make visible the bluff button
            bluffButton.setVisible(true);
        }
    }

}
