package tr.metu.ceng.construction.server.utils;

import lombok.RequiredArgsConstructor;
import tr.metu.ceng.construction.common.CardForMultiplayer;
import tr.metu.ceng.construction.common.TableStateForMultiplayer;
import tr.metu.ceng.construction.server.DTO.PlayerActionDTO;
import tr.metu.ceng.construction.server.DTO.TableStateDTO;
import tr.metu.ceng.construction.server.enums.PlayerType;
import tr.metu.ceng.construction.server.enums.Rank;
import tr.metu.ceng.construction.server.enums.Suit;
import tr.metu.ceng.construction.server.exception.CardNotBelongToPlayerException;
import tr.metu.ceng.construction.server.exception.GameNotFoundException;
import tr.metu.ceng.construction.server.exception.PlayerNotAuthorizedException;
import tr.metu.ceng.construction.server.exception.PlayerNotFoundException;
import tr.metu.ceng.construction.server.model.Card;
import tr.metu.ceng.construction.server.model.Game;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.repository.GameRepository;
import tr.metu.ceng.construction.server.repository.PlayerRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static tr.metu.ceng.construction.server.common.CommonConstants.EXTRA_POINTS_BY_CARD_COUNT;
import static tr.metu.ceng.construction.server.common.CommonConstants.MULTIPLAYER_GAME_LEVEL;
import static tr.metu.ceng.construction.server.common.Messages.*;
import static tr.metu.ceng.construction.server.common.Messages.CARD_NOT_BELONG_TO_PLAYER;

/**
 * Includes some common functionalities that may be called from other methods.
 */
public final class GameUtilities {
    /**
     * Calculates total points of cards
     *
     * @param faceUpCards cards that are on the middle of the table
     * @param card card that a player chose from his/her hand
     * @return total points of given cards
     */
    public static int calculatePointToAdd(List<Card> faceUpCards, Card card) {
        return faceUpCards
                .stream()
                .mapToInt(Card::calculateExtraPoints)
                .sum() + card.calculateExtraPoints();
    }

    /**
     * Calculates total points of cards for multiplayer game
     *
     * @param faceUpCards cards that are on the middle of the table
     * @param card card that a player chose from his/her hand
     * @return total points of given cards
     */
    public static int calculatePointToAdd(List<CardForMultiplayer> faceUpCards, CardForMultiplayer card) {
        return faceUpCards
                .stream()
                .mapToInt(CardForMultiplayer::calculateExtraPoints)
                .sum() + card.calculateExtraPoints();
    }

    /**
     * Checks whether cards of players finished or not.
     *
     * @param tableState status of the table
     * @return a boolean value that indicates if players' cards have finished or not
     */
    public static boolean areCardsFinished(TableStateDTO tableState) {
        if (tableState.getLevel() == MULTIPLAYER_GAME_LEVEL && tableState.getPlayer2Cards().isEmpty()) {
            return true;
        } else return tableState.getLevel() < MULTIPLAYER_GAME_LEVEL && tableState.getComputerCards().isEmpty();
    }

    /**
     * Checks whether cards of players finished or not for a multiplayer game
     *
     * @param tableState status of the table
     * @return a boolean value that indicates if players' cards have finished or not
     */
    public static boolean areCardsFinished(TableStateForMultiplayer tableState) {
        return tableState.getPlayer1Cards().isEmpty() && tableState.getPlayer2Cards().isEmpty();
    }

    /**
     * Finds the player who has captured more cards in the single player game, and adds 3 extra points to her/him.
     *
     * @param tableState status of the table
     */
    public static void givePointForMostCapturedCardInSinglePlayer(TableStateDTO tableState) {
        if (tableState.getCapturedCardsNumberByPlayer1() > tableState.getCapturedCardsNumberByComputer()) {
            tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + EXTRA_POINTS_BY_CARD_COUNT);
        } else if (tableState.getCapturedCardsNumberByPlayer1() < tableState.getCapturedCardsNumberByComputer()) {
            tableState.setComputerLevelScore(tableState.getComputerLevelScore() + EXTRA_POINTS_BY_CARD_COUNT);
        }
    }

    /**
     * Finds the player who has captured more cards in the single player game, and adds 3 extra points to her/him.
     * Works in multiplayer game
     *
     * @param tableState status of the table
     */
    public static void givePointForMostCapturedCardInSinglePlayer(TableStateForMultiplayer tableState) {
        if (tableState.getCapturedCardsNumberByPlayer1() > tableState.getCapturedCardsNumberByPlayer2()) {
            tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + EXTRA_POINTS_BY_CARD_COUNT);
        } else if (tableState.getCapturedCardsNumberByPlayer1() < tableState.getCapturedCardsNumberByPlayer2()) {
            tableState.setPlayer2LevelScore(tableState.getPlayer2LevelScore() + EXTRA_POINTS_BY_CARD_COUNT);
        }
    }

    /**
     * Finds the player who has captured more cards in the multi player game, and adds 3 extra points to her/him.
     *
     * @param tableState status of the table
     */
    public static void givePointForMostCapturedCardInMultiPlayer(TableStateDTO tableState) {
        if (tableState.getCapturedCardsNumberByPlayer1() > tableState.getCapturedCardsNumberByPlayer2()) {
            tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + EXTRA_POINTS_BY_CARD_COUNT);
        } else if (tableState.getCapturedCardsNumberByPlayer1() < tableState.getCapturedCardsNumberByPlayer2()) {
            tableState.setPlayer2LevelScore(tableState.getPlayer2LevelScore() + EXTRA_POINTS_BY_CARD_COUNT);
        }
    }

    /**
     * Clears level score attributes from a table state entity.
     *
     * @param tableState status of the table
     */
    public static void clearLevelScores(TableStateDTO tableState) {
        tableState.setPlayer1LevelScore(0);
        tableState.setPlayer2LevelScore(0);
        tableState.setComputerLevelScore(0);
    }

    /**
     * Clears fields that hold count of captured cards for each player.
     * @param tableState status of the table
     */
    public static void clearNumberOfCapturedCards(TableStateDTO tableState) {
        tableState.setCapturedCardsNumberByPlayer1(0);
        tableState.setCapturedCardsNumberByPlayer2(0);
        tableState.setCapturedCardsNumberByComputer(0);
    }

    /* Creates standard 52-card deck */
    public static Set<Card> prepareDeck() {
        Set<Card> deck = new HashSet<>();

        deck.addAll(prepareThirteenCardsOfSuit(Suit.CLUBS));
        deck.addAll(prepareThirteenCardsOfSuit(Suit.DIAMONDS));
        deck.addAll(prepareThirteenCardsOfSuit(Suit.HEARTS));
        deck.addAll(prepareThirteenCardsOfSuit(Suit.SPADES));

        return deck;
    }

    private static Set<Card> prepareThirteenCardsOfSuit(Suit suit) {
        Set<Card> cards = new HashSet<>();

        for (Rank rank : Rank.values()) {
            cards.add(new Card(suit, rank));
        }

        return cards;
    }

}
