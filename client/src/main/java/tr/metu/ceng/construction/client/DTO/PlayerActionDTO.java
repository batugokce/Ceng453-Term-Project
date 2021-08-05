package tr.metu.ceng.construction.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.metu.ceng.construction.client.enums.Rank;
import tr.metu.ceng.construction.client.enums.PlayerType;
import tr.metu.ceng.construction.client.enums.Suit;

/**
 * Responsible for holding action taken by the player.
 * When a player wants to play a card, client will make a request with this DTO.
 * Includes card to be played, token of the player, id of the player and game, state of the table.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerActionDTO {

    private Rank rank;
    private Suit suit;
    private String token;
    private Long gameId;
    private Long playerId;
    private PlayerType playerNo;
    private TableStateDTO tableState;

}
