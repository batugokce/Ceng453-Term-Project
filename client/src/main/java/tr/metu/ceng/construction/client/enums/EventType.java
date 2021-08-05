package tr.metu.ceng.construction.client.enums;

/**
 * Represents the situation of last move in the game
 */
public enum EventType {
    PLAYER1_CAPTURED("PLAYER1 CAPTURED"),
    COMPUTER_CAPTURED("COMPUTER CAPTURED"),
    PLAYER1_WIN("GAME OVER!\n PLAYER1 WON"),
    COMPUTER_WIN("GAME OVER!\n COMPUTER WON"),
    START_GAME("GAME STARTED"),
    LEVEL_UP("LEVEL UP :)"),
    RESET_CARDS("RESET CARDS"),
    CHEAT("CHEAT"),
    BLUFF_CHALLENGED_AND_PISTI("BLUFF IS CHALLENGED\n AND PISTI"),
    BLUFF_CHALLENGED_AND_NOT_PISTI("BLUFF IS CHALLENGED\n AND NOT PISTI"),
    BLUFF_NOT_CHALLENGED("BLUFF IS NOT CHALLENGED"),
    MATCHMAKING("MATCHMAKING");

    private final String eventMessage;

    EventType(String eventMessage){
        this.eventMessage = eventMessage;
    }

    @Override
    public String toString() {
        return eventMessage;
    }

    /**
     * @returns whether game has finished or not
     */
    public boolean isGameOver() {
        return this.equals(PLAYER1_WIN) || this.equals(COMPUTER_WIN);
    }
}
