package tr.metu.ceng.construction.server.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.metu.ceng.construction.server.enums.Rank;
import tr.metu.ceng.construction.server.enums.Suit;

import java.util.Objects;

import static tr.metu.ceng.construction.server.common.CommonConstants.*;


/**
 * Represents a playing card. It has suit and rank attributes.
 * A card entity should belong to a deck in a game entity or a player entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class Card {

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
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
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
        if (!(otherObject instanceof Card)) return false;
        Card card = (Card) otherObject;
        return suit == card.suit &&
                rank == card.rank;
    }

    /**
     * Returns a hash code for this {@code Card}.
     *
     * @return a hash code value for this object depending on suit and rank.
     */
    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    /**
     * Compares ranks of two cards.
     * If both cards have the same rank, returns true.
     *
     * @param otherCard this is the card to compare rank.
     * @return a boolean that indicates if two objects have equal rank or not.
     */
    public boolean compareRank(Card otherCard) {
        return rank == otherCard.rank;
    }

    /**
     * Checks if this card can capture other card.
     * If this card is jack or both cards have the same rank, returns true.
     *
     * @param otherCard this is the card to compare rank.
     * @return a boolean that indicates if the card can capture other card or not.
     */
    public boolean canCapture(Card otherCard) {
        return this.getRank().equals(Rank.JACK) || this.compareRank(otherCard);
    }

    /**
     * Returns the extra point of the individual card.
     * If the card is jack or ace, then it has 1 point.
     * If the card is two of clubs, then it has 2 points.
     * If the card is 10 of diamonds, then it has 3 points.
     * Otherwise, the card does not have extra point. It just contributes total number of the cards in the deck.
     *
     * @return extra point of the card.
     */
    public int calculateExtraPoints() {
        if (rank.equals(Rank.JACK)) {
            return JACK_EXTRA_POINT;
        } else if (rank.equals(Rank.ACE)) {
            return ACE_EXTRA_POINT;
        } else if (rank.equals(Rank.TWO) && suit.equals(Suit.CLUBS)) {
            return TWO_CLUBS_EXTRA_POINT;
        } else if (rank.equals(Rank.TEN) && suit.equals(Suit.DIAMONDS)) {
            return TEN_DIAMONDS_EXTRA_POINT;
        } else {
            return NO_EXTRA_POINT;
        }
    }

}
