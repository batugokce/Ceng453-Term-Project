package tr.metu.ceng.construction.server.utils;

import tr.metu.ceng.construction.common.CardForMultiplayer;
import tr.metu.ceng.construction.common.MultiplayerEvent;
import tr.metu.ceng.construction.common.TableStateForMultiplayer;
import tr.metu.ceng.construction.server.common.CommonConstants;
import tr.metu.ceng.construction.common.Rank;
import tr.metu.ceng.construction.common.Suit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MultiplayerUtilities {

    public static TableStateForMultiplayer prepareGameTable(String player1Username, String player2Username, int player1CumulativeScore, int player2CumulativeScore) {
        TableStateForMultiplayer table = new TableStateForMultiplayer();
        table.setPlayer1Username(player1Username);
        table.setPlayer2Username(player2Username);
        table.setPlayer1Cards(new HashSet<>());
        table.setPlayer2Cards(new HashSet<>());
        table.setFaceUpCards(new ArrayList<>());
        table.setFaceDownCards(prepareDeck());
        table.setPlayer1CumulativeScore(player1CumulativeScore);
        table.setPlayer2CumulativeScore(player2CumulativeScore);
        table.setEventType(MultiplayerEvent.START);

        putCardToCenter(table);
        dealCardsToPlayers(table);

        return table;
    }

    public static void putCardToCenter(TableStateForMultiplayer tableState) {
        // pick four random cards and add them to the faceUpCards
        for (int i = 0; i < CommonConstants.TOTAL_DEALING_CARD_NUMBER; i++) {
            CardForMultiplayer randomCard = getRandomCard(tableState.getFaceDownCards());
            tableState.getFaceDownCards().remove(randomCard); // remove it from faceDownCards
            tableState.getFaceUpCards().add(randomCard);
        }
    }

    public static void dealCardsToPlayers(TableStateForMultiplayer tableState) {
        // pick four random cards and add them to the player1 deck
        for (int i = 0; i < CommonConstants.TOTAL_DEALING_CARD_NUMBER; i++) {
            CardForMultiplayer randomCard = getRandomCard(tableState.getFaceDownCards());
            tableState.getFaceDownCards().remove(randomCard); // remove it from faceDownCards
            tableState.getPlayer1Cards().add(randomCard);
        }

        // pick four random cards and add them to the player2 deck
        for (int i = 0; i < CommonConstants.TOTAL_DEALING_CARD_NUMBER; i++) {
            CardForMultiplayer randomCard = getRandomCard(tableState.getFaceDownCards());
            tableState.getFaceDownCards().remove(randomCard); // remove it from faceDownCards
            tableState.getPlayer2Cards().add(randomCard);
        }
    }

    private static CardForMultiplayer getRandomCard(Set<CardForMultiplayer> cards) {
        int randomIndex = new Random().nextInt(cards.size());

        int currentIndex = 0;
        for (CardForMultiplayer card : cards) {
            if (currentIndex == randomIndex) {
                return card;
            }
            currentIndex++;
        }

        return new ArrayList<>(cards).get(randomIndex);
    }

    /* Creates standard 52-card deck */
    public static Set<CardForMultiplayer> prepareDeck() {
        Set<CardForMultiplayer> deck = new HashSet<>();

        deck.addAll(prepareThirteenCardsOfSuit(Suit.CLUBS));
        deck.addAll(prepareThirteenCardsOfSuit(Suit.DIAMONDS));
        deck.addAll(prepareThirteenCardsOfSuit(Suit.HEARTS));
        deck.addAll(prepareThirteenCardsOfSuit(Suit.SPADES));

        return deck;
    }

    private static Set<CardForMultiplayer> prepareThirteenCardsOfSuit(Suit suit) {
        Set<CardForMultiplayer> cards = new HashSet<>();

        for (Rank rank : Rank.values()) {
            cards.add(new CardForMultiplayer(suit, rank));
        }

        return cards;
    }

}
