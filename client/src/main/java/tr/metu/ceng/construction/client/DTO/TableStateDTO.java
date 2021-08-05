package tr.metu.ceng.construction.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.metu.ceng.construction.client.enums.EventType;

import java.util.List;
import java.util.Set;

import static tr.metu.ceng.construction.client.constant.GameConstants.BLUFFING_AND_SINGLEPLAYER_GAME_LEVEL;
import static tr.metu.ceng.construction.client.constant.GameConstants.MULTIPLAYER_GAME_LEVEL;

/**
 * Hold the general state of the table.
 * This DTO will be used between client and server to hold the state of the table.
 * Includes face-up-cards, face-down-cards, player cards, player scores etc.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableStateDTO {

    /**
     * Holds ID of the game.
     */
    private Long gameId;

    /**
     * Holds the set of cards that belong to player1.
     */
    private Set<CardDTO> player1Cards;

    /**
     * Holds the set of cards that belong to player2.
     * It may be null if game level is four (multi-player).
     */
    private Set<CardDTO> player2Cards;

    /**
     * Holds the size of the list of cards that was captured by the player1.
     */
    private Integer capturedCardsNumberByPlayer1 = 0;

    /**
     * Holds the size of the list of cards that was captured by the player2.
     */
    private Integer capturedCardsNumberByPlayer2 = 0;

    /**
     * Holds the list of the cards that are at the center of the table.
     */
    private List<CardDTO> faceUpCards;

    /**
     * Holds the set of the cards that will be dealt to players.
     */
    private Set<CardDTO> faceDownCards;

    /**
     * Holds the set of cards that belong to computer.
     * If the game level is four (game is multiplayer), then this attribute may be null.
     */
    private Set<CardDTO> computerCards;

    /**
     * Holds the size of the list of cards that was captured by the computer.
     * If the game level is four (game is multiplayer), then this attribute may be null.
     */
    private Integer capturedCardsNumberByComputer;

    /**
     * Hold the level score of player1
     */
    private Integer player1LevelScore;

    /**
     * Holds the level score of player2
     * Can be null if the game level is not four (game is single player)
     */
    private Integer player2LevelScore;

    /**
     * Holds the level score of computer
     */
    private Integer computerLevelScore;

    /**
     * Holds cumulative score of player
     */
    private Integer player1CumulativeScore;

    /**
     * Holds the level of the game
     * Can take values from 1 to 4
     */
    private Integer level;

    /**
     * Indicates which player is in the turn.
     * This variable can take only two values; 1 or 2.
     * If this is 1, then player1's move is waited.
     * Otherwise, it is the turn of the player2/computer.
     */
    private Integer turn = 1;

    /**
     * Holds the situation of last move in the game
     */
    private EventType eventType;

    /**
     * @return if game is bluffing pisti and face-up cards' size on the table is one.
     */
    public boolean isGameBluffingPistiAndCanBluff() {
        return (level.equals(BLUFFING_AND_SINGLEPLAYER_GAME_LEVEL) || level.equals(MULTIPLAYER_GAME_LEVEL)) && faceUpCards.size() == 1 ;
    }

}
