package tr.metu.ceng.construction.server.enums;

/**
 * Represents the situation of last move in the game
 */
public enum EventType {
    PLAYER1_CAPTURED,
    COMPUTER_CAPTURED,
    PLAYER1_WIN,
    COMPUTER_WIN,
    START_GAME,
    LEVEL_UP,
    RESET_CARDS, //none of the players could pass 151
    CHEAT,
    BLUFF_CHALLENGED_AND_PISTI,
    BLUFF_CHALLENGED_AND_NOT_PISTI,
    BLUFF_NOT_CHALLENGED,
    MATCHMAKING
}
