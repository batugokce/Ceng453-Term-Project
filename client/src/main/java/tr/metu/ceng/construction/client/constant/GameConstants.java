package tr.metu.ceng.construction.client.constant;

import tr.metu.ceng.construction.client.DTO.TableStateDTO;
import tr.metu.ceng.construction.client.component.*;
import tr.metu.ceng.construction.client.enums.Rank;
import tr.metu.ceng.construction.client.enums.Suit;
import tr.metu.ceng.construction.common.PlayerType;
import tr.metu.ceng.construction.common.TableStateForMultiplayer;

/**
 * Holds some important values that will be used in the gameplay such as username, playerID, player cards etc.
 */
public class GameConstants {

    public static String USERNAME = null;
    public static String USERNAME_PLAYER2 = null;
    public static String TOKEN = null;
    public static Long PLAYER_ID;
    public static Long PLAYER2_ID;
    public static Long GAME_ID;
    public static String YOUR_TURN = "YOUR TURN";
    public static String COMPUTER_TURN = "COMPUTER TURN";
    public static String PLAYER1_TURN = "PLAYER1 TURN";
    public static String PLAYER2_TURN = "PLAYER2 TURN";
    public static Rank PICKED_CARD_RANK = null;
    public static Suit PICKED_CARD_SUIT = null;
    public static boolean isMultiplayerGameStarted = false;
    public static boolean isMultiplayerGameAndBluffingChallengeTurn = false;
    public static boolean isPlayerMatched = false;
    public static final int MULTIPLAYER_GAME_LEVEL = 4;
    public static final int BLUFFING_AND_SINGLEPLAYER_GAME_LEVEL = 3;

    // belows for multi-player game
    public static final String START = "START";
    public static final String MATCH = "MATCH";
    public static final String GAME_OVER = "END";
    public static final String ERROR = "ERROR";
    public static final String SPLIT_TOKEN = "-";

    public static TableStateForMultiplayer TABLE_STATE_MULTIPLAYER;
    public static PlayerType WHICH_PLAYER_YOU_ARE;

    public static PlayerCardBox playerCardBox;
    public static OpponentCardBox opponentCardBox;
    public static MiddleCardBox middleCardBox;
    public static LeftMenuComponent leftMenuComponent;
    public static LeftMenuForMultiplayer leftMenuComponentForMultiplayer;
    public static FaceDownCardBox faceDownCardBox;
    public static TableStateDTO TABLE_STATE;
    public static TableStateDTO BEFORE_TABLE_STATE;
}
