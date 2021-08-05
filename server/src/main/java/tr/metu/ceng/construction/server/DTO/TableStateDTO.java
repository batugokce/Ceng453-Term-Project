package tr.metu.ceng.construction.server.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.metu.ceng.construction.server.enums.EventType;
import tr.metu.ceng.construction.server.model.Card;

import java.util.List;
import java.util.Set;

/**
 * Hold the general state of the table.
 * This DTO will be used between client and server to hold the state of the table.
 * Includes face-up-cards, face-down-cards, player cards, player scores etc.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableStateDTO {

    public TableStateDTO(TableStateDTO tableStateDTO) {
        this.player1Cards = Set.copyOf(tableStateDTO.player1Cards);
        if (null != tableStateDTO.player2Cards) {
            this.player2Cards = Set.copyOf(tableStateDTO.player2Cards);
        }
        if (null != tableStateDTO.computerCards) {
            this.computerCards = Set.copyOf(tableStateDTO.computerCards);
        }
        this.capturedCardsNumberByPlayer1 = tableStateDTO.capturedCardsNumberByPlayer1;
        this.faceUpCards = List.copyOf(tableStateDTO.faceUpCards);
        this.player1LevelScore = tableStateDTO.player1LevelScore;
        this.setPlayer1CumulativeScore(tableStateDTO.player1CumulativeScore);
        this.turn = tableStateDTO.turn;
        this.eventType = tableStateDTO.eventType;
    }

    /**
     * Holds ID of the game.
     */
    private Long gameId;

    /**
     * Holds the set of cards that belong to player1.
     */
    private Set<Card> player1Cards;

    /**
     * Holds the set of cards that belong to player2.
     * It may be null if game level is four (multi-player).
     */
    private Set<Card> player2Cards;

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
    private List<Card> faceUpCards;

    /**
     * Holds the set of the cards that will be dealt to players.
     */
    private Set<Card> faceDownCards;

    /**
     * Holds the set of cards that belong to computer.
     * If the game level is four (game is multiplayer), then this attribute may be null.
     */
    private Set<Card> computerCards;

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

}
