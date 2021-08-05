package tr.metu.ceng.construction.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Responsible for holding action taken by the player.
 * When a player wants to play a card, client will send this DTO to its socket.
 * Includes card to be played, player type, and action made.
 */
@Getter
@Setter
@NoArgsConstructor
public class PlayerActionForMultiplayer implements Serializable {

    private static final long serialVersionUID = 6529685095267757690L;

    private Rank rank;
    private Suit suit;
    private PlayerType playerNo;
    private ActionMove actionMove;

}
