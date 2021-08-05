package tr.metu.ceng.construction.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Responsible for holding action taken by the player.
 * When a player wants to play a card, client will make a request with this DTO.
 * Includes card to be played, token of the player, id of the player and game, state of the table.
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
