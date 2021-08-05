package tr.metu.ceng.construction.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * Hold the general state of the table for multiplayer game.
 * This DTO will be used between client and server to hold the state of the table.
 * Includes face-up-cards, face-down-cards, player cards, player scores etc.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableStateForMultiplayer implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;

    /**
     * Holds ID of player1
     */
    private String player1Username;

    /**
     * Holds ID of player2
     */
    private String player2Username;

    /**
     * Holds the set of cards that belong to player1.
     */
    private Set<CardForMultiplayer> player1Cards;

    /**
     * Holds the set of cards that belong to player2.
     * It may be null if game level is four (multi-player).
     */
    private Set<CardForMultiplayer> player2Cards;

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
    private List<CardForMultiplayer> faceUpCards;

    /**
     * Holds the set of the cards that will be dealt to players.
     */
    private Set<CardForMultiplayer> faceDownCards;

    /**
     * Hold the level score of player1
     */
    private Integer player1LevelScore = 0;

    /**
     * Holds the level score of player2
     * Can be null if the game level is not four (game is single player)
     */
    private Integer player2LevelScore = 0;

    /**
     * Holds cumulative score of player
     */
    private Integer player1CumulativeScore;

    /**
     * Holds cumulative score of player
     */
    private Integer player2CumulativeScore;

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
    private MultiplayerEvent eventType;

    /**
     * @return if game is bluffing pisti and face-up cards' size on the table is one.
     */
    public boolean canBluff() {
        return faceUpCards.size() == 1;
    }

}
