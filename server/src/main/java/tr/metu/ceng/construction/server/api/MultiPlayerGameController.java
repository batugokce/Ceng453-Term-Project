package tr.metu.ceng.construction.server.api;

import tr.metu.ceng.construction.common.*;
import tr.metu.ceng.construction.server.common.CommonConstants;
import tr.metu.ceng.construction.common.ActionMove;
import tr.metu.ceng.construction.server.utils.MultiplayerUtilities;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.Thread.sleep;
import static tr.metu.ceng.construction.server.common.CommonConstants.*;
import static tr.metu.ceng.construction.server.utils.GameUtilities.*;

/**
 * Responsible from maintaining reading from / writing to streams of the
 * players sockets
 */
public class MultiPlayerGameController implements Runnable {

    private final Socket player1Socket;
    private final Socket player2Socket;
    private boolean isGameOver = false;
    ObjectOutputStream writeStreamToPlayer1, writeStreamToPlayer2;
    ObjectInputStream readStreamFromPlayer1, readStreamFromPlayer2;
    private String player1Username, player2Username;
    private int player1CumulativeScore, player2CumulativeScore;
    private TableStateForMultiplayer tableState;
    private CardForMultiplayer bluffedCard;

    /**
     * Constructor of MultiPlayerGameController.
     * @param player1Socket represents socket for player1
     * @param player2Socket represents socket for player2
     */
    public MultiPlayerGameController(Socket player1Socket, Socket player2Socket) {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;
    }

    /**
     * Overrides the run function of Java Thread class.
     * It enables initialization and continuation of players' communication.
     */
    @Override
    public void run() {
        try {
            writeStreamToPlayer1 = new ObjectOutputStream(player1Socket.getOutputStream());
            readStreamFromPlayer1 = new ObjectInputStream(player1Socket.getInputStream());

            writeStreamToPlayer2 = new ObjectOutputStream(player2Socket.getOutputStream());
            readStreamFromPlayer2 = new ObjectInputStream(player2Socket.getInputStream());

            sleep(3000);
            // send a start message so that players can start a multi-player game
            /*String startMessage = CommonConstants.START + CommonConstants.SPLIT_TOKEN + " ";
            writeStreamToPlayer1.writeObject(startMessage);
            writeStreamToPlayer2.writeObject(startMessage);*/

            System.out.println("info of player are being fetched..");
            getPlayerInfo(readStreamFromPlayer1, readStreamFromPlayer2);

            System.out.println("first table state is being sent..");
            tableState = MultiplayerUtilities.prepareGameTable(player1Username, player2Username, player1CumulativeScore, player2CumulativeScore);
            writeStreamToPlayer1.writeObject(tableState);
            writeStreamToPlayer2.writeObject(tableState);
            sleep(200);

            System.out.println("table state has been sent");

            new Thread(() -> waitInputFromPlayers(readStreamFromPlayer1, writeStreamToPlayer1, writeStreamToPlayer2)).start();
            new Thread(() -> waitInputFromPlayers(readStreamFromPlayer2, writeStreamToPlayer1, writeStreamToPlayer2)).start();


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void getPlayerInfo(ObjectInputStream readStream1, ObjectInputStream readStream2) {
        try {
            String message = (String) readStream1.readObject();
            String[] splitMessage = message.split(CommonConstants.SPLIT_TOKEN);
            player1Username = splitMessage[0];
            player1CumulativeScore = Integer.parseInt(splitMessage[1]);

            message = (String) readStream2.readObject();
            splitMessage = message.split(CommonConstants.SPLIT_TOKEN);
            player2Username = splitMessage[0];
            player2CumulativeScore = Integer.parseInt(splitMessage[1]);

            System.out.println(player1Username);
            System.out.println(player2Username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitInputFromPlayers(ObjectInputStream readStream, ObjectOutputStream writeStream1, ObjectOutputStream writeStream2) {
        while (!isGameOver) {
            try {
                PlayerActionForMultiplayer action = (PlayerActionForMultiplayer) readStream.readObject();
                // process tableState then send it to players
                if (action.getActionMove().equals(ActionMove.BLUFF)) {
                    System.out.println(action.getPlayerNo() + " bluffed");
                    bluffedCard = new CardForMultiplayer(action.getSuit(), action.getRank());
                    tableState.setEventType(MultiplayerEvent.BLUFF);
                    if (action.getPlayerNo().equals(PlayerType.PLAYER1)) {
                        tableState.setTurn(2);
                    } else {
                        tableState.setTurn(1);
                    }
                } else if (action.getActionMove().equals(ActionMove.BLUFF_CHALLENGE) || action.getActionMove().equals(ActionMove.BLUFF_NOT_CHALLENGE)) {
                    System.out.println(action.getPlayerNo() + " " + action.getActionMove());
                    processBluff(bluffedCard, action.getActionMove(), action.getPlayerNo());
                    bluffedCard = null;
                } else {
                    processPlayerMove(action.getRank(), action.getSuit(), action.getPlayerNo(), action.getActionMove());
                }

                writeStream1.reset();
                writeStream2.reset();

                writeStream1.writeObject(tableState);
                writeStream2.writeObject(tableState);

                tableState.setEventType(null);

            } catch (Exception exception) {
                isGameOver = true;
                exception.printStackTrace();
            }
        }
    }

    private void processPlayerMove(Rank rank, Suit suit, PlayerType playerType, ActionMove actionMove) {
        if (actionMove.equals(ActionMove.CHEAT)) {
            // cheated, set player level score to passing score
            if (playerType.equals(PlayerType.PLAYER1)) {
                tableState.setPlayer1LevelScore(PASSING_SCORE);
            } else {
                tableState.setPlayer2LevelScore(PASSING_SCORE);
            }
            setGameOverAndWinType();
        }  else if (actionMove.equals(ActionMove.PLAY_CARD)) {
            // player played card
            Set<CardForMultiplayer> playerCards;
            List<CardForMultiplayer> faceUpCards = tableState.getFaceUpCards();
            CardForMultiplayer cardToPlay = new CardForMultiplayer(suit, rank);

            if (playerType.equals(PlayerType.PLAYER1)) {
                playerCards = tableState.getPlayer1Cards();
                tableState.setTurn(2);
            } else {
                playerCards = tableState.getPlayer2Cards();
                tableState.setTurn(1);
            }

            playerCards.remove(cardToPlay);
            if (faceUpCards.size() != 0) {
                CardForMultiplayer lastCardOnMiddle = faceUpCards.get(faceUpCards.size() - 1);

                if (cardToPlay.canCapture(lastCardOnMiddle)) {
                    tableState.setEventType(playerType.equals(PlayerType.PLAYER1) ? MultiplayerEvent.PLAYER1_CAPTURED : MultiplayerEvent.PLAYER2_CAPTURED);

                    if (playerType.equals(PlayerType.PLAYER1)) {
                        if (faceUpCards.size() == 1 && lastCardOnMiddle.getRank().equals(Rank.JACK)) {
                            tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + DOUBLE_PISTI_POINT);
                        } else if (faceUpCards.size() == 1) {
                            tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + PISTI_POINT);
                        }

                        tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + calculatePointToAdd(faceUpCards, cardToPlay));
                        tableState.setCapturedCardsNumberByPlayer1(tableState.getCapturedCardsNumberByPlayer1() + faceUpCards.size() + 1);
                    } else {
                        if (faceUpCards.size() == 1 && lastCardOnMiddle.getRank().equals(Rank.JACK)) {
                            tableState.setPlayer2LevelScore(tableState.getPlayer2LevelScore() + DOUBLE_PISTI_POINT);
                        } else if (faceUpCards.size() == 1) {
                            tableState.setPlayer2LevelScore(tableState.getPlayer2LevelScore() + PISTI_POINT);
                        }

                        tableState.setPlayer2LevelScore(tableState.getPlayer2LevelScore() + calculatePointToAdd(faceUpCards, cardToPlay));
                        tableState.setCapturedCardsNumberByPlayer2(tableState.getCapturedCardsNumberByPlayer2() + faceUpCards.size() + 1);
                    }

                    tableState.setFaceUpCards(new ArrayList<>());
                } else {
                    faceUpCards.add(cardToPlay);
                }
            } else {
                faceUpCards.add(cardToPlay);
            }

            if (areCardsFinished(tableState)) {
                dealCardsOrEndTheGame(tableState);
            }
        }
    }

    private void processBluff(CardForMultiplayer bluffedCard, ActionMove actionMove, PlayerType opponentPlayerType) {
        boolean isChallenged = false;
        PlayerType playerType;
        if (actionMove.equals(ActionMove.BLUFF_CHALLENGE)) {
            // bluff is challenged
            isChallenged = true;
        }

        Set<CardForMultiplayer> playerCards;
        if (opponentPlayerType.equals(PlayerType.PLAYER1)) {
            playerType = PlayerType.PLAYER2;
            playerCards = tableState.getPlayer2Cards();
            tableState.setTurn(1);
        } else {
            playerType = PlayerType.PLAYER1;
            playerCards = tableState.getPlayer1Cards();
            tableState.setTurn(2);
        }

        playerCards.remove(bluffedCard);

        CardForMultiplayer lastCardOnMiddle = tableState.getFaceUpCards().get(0);
        // check whether bluff is true or not (there is a really pisti/double pisti situation
        boolean isBluffTrue = bluffedCard.canCapture(lastCardOnMiddle);

        if (isChallenged) {
            // check if bluff is true (the card played is actually a card of the same rank)
            if (isBluffTrue) {
                tableState.setEventType(MultiplayerEvent.BLUFF_CHALLENGED_AND_PISTI);
                adjustTableStateForPlayerScoring(playerType, tableState, bluffedCard, tableState.getFaceUpCards(), lastCardOnMiddle, BLUFFING_CHALLENGED_DOUBLE_PISTI_POINT, BLUFFING_CHALLENGED_PISTI_POINT);

            } else {
                // the challenging player scores 20 or 40 points instead.
                // In that case, both cards remain on the table and play continues as normal.
                tableState.setEventType(MultiplayerEvent.BLUFF_CHALLENGED_AND_NOT_PISTI);
                adjustTableStateForOpponentScoring(opponentPlayerType, tableState, bluffedCard, lastCardOnMiddle);
            }
        } else {
            // the player of the card automatically scores a Pişti or Double Pişti and captures both cards,
            // and adds the cards to his capture pile
            tableState.setEventType(MultiplayerEvent.BLUFF_NOT_CHALLENGED);
            adjustTableStateForPlayerScoring(playerType, tableState, bluffedCard, tableState.getFaceUpCards(), lastCardOnMiddle, DOUBLE_PISTI_POINT, PISTI_POINT);
        }

        if (areCardsFinished(tableState)) {
            dealCardsOrEndTheGame(tableState);
        }
    }

    private void adjustTableStateForPlayerScoring(PlayerType playerType, TableStateForMultiplayer tableState, CardForMultiplayer bluffedCard, List<CardForMultiplayer> faceUpCards, CardForMultiplayer lastCardOnMiddle, int doublePistiPoint, int pistiPoint) {
        if (playerType.equals(PlayerType.PLAYER1)) {
            if (lastCardOnMiddle.getRank().equals(Rank.JACK)){
                // the player of that face-down cards scores doublePistiPoint for the Double Pişti.
                tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + doublePistiPoint);
            } else {
                // the player of that face-down cards scores pistiPoint for the Pişti.
                tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + pistiPoint);
            }
            tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + calculatePointToAdd(faceUpCards, bluffedCard));
            tableState.setCapturedCardsNumberByPlayer1(tableState.getCapturedCardsNumberByPlayer1() + faceUpCards.size() + 1);
        } else {
            if (lastCardOnMiddle.getRank().equals(Rank.JACK)){
                // the player of that face-down cards scores doublePistiPoint for the Double Pişti.
                tableState.setPlayer2LevelScore(tableState.getPlayer2LevelScore() + doublePistiPoint);
            } else {
                // the player of that face-down cards scores pistiPoint for the Pişti.
                tableState.setPlayer2LevelScore(tableState.getPlayer2LevelScore() + pistiPoint);
            }
            tableState.setPlayer2LevelScore(tableState.getPlayer2LevelScore() + calculatePointToAdd(faceUpCards, bluffedCard));
            tableState.setCapturedCardsNumberByPlayer2(tableState.getCapturedCardsNumberByPlayer2() + faceUpCards.size() + 1);
        }

        tableState.setFaceUpCards(new ArrayList<>());
    }

    private void adjustTableStateForOpponentScoring(PlayerType opponentPlayerType,TableStateForMultiplayer tableState, CardForMultiplayer bluffedCard, CardForMultiplayer lastCardOnMiddle) {
        if (opponentPlayerType.equals(PlayerType.PLAYER1)) {
            if (lastCardOnMiddle.getRank().equals(Rank.JACK)) {
                // the other player scores 40 points for the Double Pişti.
                tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + BLUFFING_CHALLENGED_DOUBLE_PISTI_POINT);
            } else {
                // the other player scores 20 points for the Pişti.
                tableState.setPlayer1LevelScore(tableState.getPlayer1LevelScore() + BLUFFING_CHALLENGED_PISTI_POINT);
            }

        } else {
            if (lastCardOnMiddle.getRank().equals(Rank.JACK)) {
                // the other player scores 40 points for the Double Pişti.
                tableState.setPlayer2LevelScore(tableState.getPlayer2LevelScore() + BLUFFING_CHALLENGED_DOUBLE_PISTI_POINT);
            } else {
                // the other player scores 20 points for the Pişti.
                tableState.setPlayer2LevelScore(tableState.getPlayer2LevelScore() + BLUFFING_CHALLENGED_PISTI_POINT);
            }
        }
        //both cards remain on the table
        tableState.getFaceUpCards().add(bluffedCard);
    }

    private void dealCardsOrEndTheGame(TableStateForMultiplayer tableState) {
        if (tableState.getFaceDownCards().isEmpty()) {
            // hand is over, there is no card to deal, check scores

            givePointForMostCapturedCardInSinglePlayer(tableState);
            if (tableState.getPlayer1LevelScore() >= PASSING_SCORE || tableState.getPlayer2LevelScore() >= PASSING_SCORE) {
                setGameOverAndWinType();
            }
            else {
                tableState.setEventType(MultiplayerEvent.HAND_IS_OVER);
                prepareNewHand(tableState);
            }
        } else {
            MultiplayerUtilities.dealCardsToPlayers(tableState);
        }

    }

    private void setGameOverAndWinType() {
        if (tableState.getPlayer1LevelScore() >= PASSING_SCORE) {
            tableState.setEventType(MultiplayerEvent.PLAYER1_WIN);
            isGameOver = true;
        } else if (tableState.getPlayer2LevelScore() >= PASSING_SCORE) {
            tableState.setEventType(MultiplayerEvent.PLAYER2_WIN);
            isGameOver = true;
        }
    }

    private void prepareNewHand(TableStateForMultiplayer tableState) {
        tableState.setFaceDownCards(MultiplayerUtilities.prepareDeck());
        tableState.setFaceUpCards(new ArrayList<>());

        MultiplayerUtilities.putCardToCenter(tableState);
        MultiplayerUtilities.dealCardsToPlayers(tableState);
        clearNumberOfCapturedCards(tableState);
    }

    private void clearNumberOfCapturedCards(TableStateForMultiplayer tableState) {
        tableState.setCapturedCardsNumberByPlayer1(0);
        tableState.setCapturedCardsNumberByPlayer2(0);
    }

}
