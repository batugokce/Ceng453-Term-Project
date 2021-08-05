package tr.metu.ceng.construction.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.metu.ceng.construction.client.enums.Rank;
import tr.metu.ceng.construction.client.enums.Suit;

/**
 * Represents a playing card. It has suit and rank attributes.
 * A card should belong to a deck in a game or a player.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {
    /**
     * Suit can take 4 values; clubs, diamonds, hearts or spades.
     */
    private Suit suit;

    /**
     * Rank can take 13 values; from 2 to ace.
     */
    private Rank rank;

    public CardDTO(String suit, String rank) {
        this.suit = Suit.valueOf(suit);
        this.rank = Rank.valueOf(rank);
    }

    /**
     * This method checks whether a given object is equals to this card or not.
     * If suit and rank of two cards are same, these two cards are identical.
     *
     * @param otherObject this is the object to check equality.
     * @return a boolean that indicates if two objects are equal or not.
     */
    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (!(otherObject instanceof CardDTO)) return false;
        CardDTO card = (CardDTO) otherObject;
        return suit == card.suit &&
                rank == card.rank;
    }
}
