package tr.metu.ceng.construction.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.metu.ceng.construction.client.DTO.CardDTO;

import java.io.Serializable;

/**
 * Represents a playing card. It has suit and rank attributes.
 * A card entity should belong to a deck in a game entity or a player entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class CardForMultiplayer implements Serializable {

    private static final long serialVersionUID = 6529685098267757692L;

    /**
     * Suit can take 4 values; clubs, diamonds, hearts or spades.
     */
    private Suit suit;

    /**
     * Rank can take 13 values; from 2 to ace.
     */
    private Rank rank;

    /**
     * Create a card object using suit and rank attributes.
     * @param suit a suit from the {@code Suit} enum.
     * @param rank a rank from the {@code Rank} enum.
     */
    public CardForMultiplayer(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public CardDTO mapToNormalCard() {
        return new CardDTO(this.suit.toString(), this.rank.toString());
    }

}
