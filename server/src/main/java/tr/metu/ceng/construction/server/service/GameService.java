package tr.metu.ceng.construction.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.metu.ceng.construction.server.common.CommonConstants;
import tr.metu.ceng.construction.server.enums.EventType;
import tr.metu.ceng.construction.server.enums.Rank;
import tr.metu.ceng.construction.server.enums.Suit;
import tr.metu.ceng.construction.server.exception.GameNotFoundException;
import tr.metu.ceng.construction.server.exception.GameNotMultiPlayerException;
import tr.metu.ceng.construction.server.exception.PlayerNotAuthorizedException;
import tr.metu.ceng.construction.server.exception.PlayerNotFoundException;
import tr.metu.ceng.construction.server.model.Card;
import tr.metu.ceng.construction.server.model.Game;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.repository.GameRepository;
import tr.metu.ceng.construction.server.DTO.TableStateDTO;
import tr.metu.ceng.construction.server.repository.PlayerRepository;
import tr.metu.ceng.construction.server.utils.GameUtilities;

import java.util.*;

import static tr.metu.ceng.construction.server.common.CommonConstants.MULTIPLAYER_GAME_LEVEL;
import static tr.metu.ceng.construction.server.common.CommonConstants.PASSING_SCORE;
import static tr.metu.ceng.construction.server.common.Messages.*;
import static tr.metu.ceng.construction.server.utils.GameUtilities.clearLevelScores;
import static tr.metu.ceng.construction.server.utils.GameUtilities.clearNumberOfCapturedCards;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final  PlayerRepository playerRepository;
    private final PlayerService playerService;
    private final CardService cardService;

    /**
     * Starts a game for the player with the given username by creating a new game.
     * Then resets cards in the table.
     *
     * @param username username of the player
     * @return state of the table
     * @throws PlayerNotFoundException if a player with that username cannot be found
     */
    public TableStateDTO startGame(String username) throws PlayerNotFoundException {
        Game newGame = createGame(username);
        //initialize table state
        TableStateDTO tableState = new TableStateDTO(newGame.getId(), new HashSet<>(), null, 0, 0, new ArrayList<>(), new HashSet<>(), new HashSet<>(), 0, 0, null, 0, 0, 1, 1, EventType.START_GAME);

        return resetCards(newGame, tableState);
    }

    /**
     * Creates a new game by setting game level to 1 and cumulative score of the player to 0.
     *
     * @param username username of the player
     * @return created game
     * @throws PlayerNotFoundException if a player with that username cannot be found
     * @throws PlayerNotAuthorizedException if the player does not have a token
     */
    public Game createGame(String username) throws PlayerNotFoundException, PlayerNotAuthorizedException{
        Game game = new Game();

        Player player1 = playerService.findByUsername(username);
        if (player1 == null) {
            throw new PlayerNotFoundException(PLAYER_NOT_EXIST);
        }
        if (player1.getToken() == null) {
            throw new PlayerNotAuthorizedException(PLAYER_NOT_AUTHORIZED);
        }
        game.setPlayer1(player1);
        game.setPlayer2(null); // it is single-player game at first
        game.setLevel(1);

        gameRepository.save(game);

        player1.setCumulativeScore(0);
        playerRepository.save(player1);

        return game;
    }

    /**
     * Resets all the cards in the table. When player starts new game or levels up, reset the cards by
     * setting faceDownCards to a 52-cards deck, dealing four cards to the players/computer and putting
     * four cards to the faceUpCards(to the center of table).
     * Also, clears the captured card number to zero for all players.
     *
     * @param game this is the game being played
     * @param tableState this is the current table state
     * @return updated table state
     * @throws PlayerNotFoundException if a player associated with that game is null
     * @throws GameNotFoundException if the passed game parameter is null
     */
    public TableStateDTO resetCards(Game game, TableStateDTO tableState) throws GameNotFoundException, PlayerNotFoundException {

        if (game != null) {
            Set<Card> deck = new HashSet<>(GameUtilities.prepareDeck());
            // assign all cards in created deck to be face-down cards in the game
            tableState.setFaceDownCards(deck);

            if (game.getPlayer1() == null) {
                throw new PlayerNotFoundException(PLAYER_NOT_EXIST);
            }
            // set player1 cards to empty
            tableState.setPlayer1Cards(new HashSet<>());

            // set computer or player2 cards to empty
            if (!game.isMultiPlayer()) {
                tableState.setComputerCards(new HashSet<>());
            } else if (game.getPlayer2() == null) {
                throw new PlayerNotFoundException(PLAYER_NOT_EXIST);
            } else {
                tableState.setPlayer2Cards(new HashSet<>());
            }

            tableState.setFaceUpCards(new ArrayList<>());

            dealCards(game, tableState);
            putRandomCardsToCenter(tableState);
            clearNumberOfCapturedCards(tableState);

            return tableState;
        } else {
            throw new GameNotFoundException(GAME_NOT_EXIST);
        }
    }

    /**
     * Deals four cards to player1, another four cards to  either the computer(if single-player game),
     * or player2(if multi-player game).
     *
     * @param game this is the game being played
     * @param tableState this is the current table state
     * @throws PlayerNotFoundException if a player associated with that game is null
     * @throws GameNotFoundException if the passed game parameter is null
     */
    public void dealCards(Game game, TableStateDTO tableState) throws GameNotFoundException, PlayerNotFoundException {
        if (game != null) {
            Set<Card> faceDownCards = tableState.getFaceDownCards();

            Card randomCard;
            for (int i = 0; i < CommonConstants.TOTAL_DEALING_CARD_NUMBER; i++) {
                // loop four times to pick four cards for player1
                randomCard = cardService.getRandomCard(faceDownCards);

                faceDownCards.remove(randomCard); // remove it from faceDownCards

                // deal picked cards to the player1
                tableState.getPlayer1Cards().add(randomCard);
            }

            boolean isMultiPlayer = game.isMultiPlayer();
            for (int i = 0; i < CommonConstants.TOTAL_DEALING_CARD_NUMBER; i++) {
                // loop four times to pick four cards for player2 or computer
                randomCard = cardService.getRandomCard(faceDownCards);

                faceDownCards.remove(randomCard); // remove it from faceDownCards

                if (isMultiPlayer) {
                    Player player2 = game.getPlayer2();
                    if (player2 != null) { // player2 exists
                        // deal picked card to the player2
                        tableState.getPlayer2Cards().add(randomCard);
                    }
                    else {
                        throw new PlayerNotFoundException(PLAYER_NOT_EXIST);
                    }

                } else {
                    // deal picked card to the computer
                    tableState.getComputerCards().add(randomCard);
                }
            }
        } else{
            throw new GameNotFoundException(GAME_NOT_EXIST);
        }

    }

    /**
     * Provides a cheating.
     * The player passes the level and gets a score of 151 from the current level.
     * This is valid only for single-player game levels.
     *
     * @param tableState the current status of the table
     * @return status of the table after cheating.
     * @throws PlayerNotFoundException if a player associated with that game is null
     * @throws GameNotFoundException if no game found with the id given in parameter
     * @throws GameNotMultiPlayerException if game is not multi-player currently
     *
     */
    public TableStateDTO cheat(TableStateDTO tableState) throws GameNotFoundException, PlayerNotFoundException, GameNotMultiPlayerException {
        Game game = gameRepository.findById(tableState.getGameId()).orElse(null);

        // game or player does not exist
        if (game == null) {
            throw new GameNotFoundException(GAME_NOT_EXIST);
        }
        else if (game.getPlayer1() == null) {
            throw new PlayerNotFoundException(PLAYER_NOT_EXIST);
        }

        // the level should not be four (multiplayer game)
        if (game.getLevel() != MULTIPLAYER_GAME_LEVEL) {
            //level up the player
            tableState.setPlayer1CumulativeScore(game.getPlayer1().getCumulativeScore() + PASSING_SCORE);
            game.getPlayer1().setCumulativeScore(game.getPlayer1().getCumulativeScore() + PASSING_SCORE);
            resetCards(game, tableState);
            clearLevelScores(tableState);
            game.setLevel(game.getLevel() + 1);
            gameRepository.save(game);

            playerRepository.save(game.getPlayer1());

            tableState.setTurn(1);
            tableState.setLevel(game.getLevel());
            tableState.setEventType(EventType.CHEAT);
            return tableState;
        }
        throw new GameNotMultiPlayerException(GAME_NOT_MULTIPLAYER);
    }

    /**
     * Adds four random cards to the face-up cards (center of the table) and
     * removes them from face-down cards.
     *
     * @param tableState this is the current table state
     */
    public void putRandomCardsToCenter(TableStateDTO tableState) {
        // pick four random cards and add them to the faceUpCards
        for (int i = 0; i < CommonConstants.TOTAL_DEALING_CARD_NUMBER; i++) {
            Card randomCard = cardService.getRandomCard(tableState.getFaceDownCards());

            tableState.getFaceDownCards().remove(randomCard); // remove it from faceDownCards
            tableState.getFaceUpCards().add(randomCard);
        }
    }
}
