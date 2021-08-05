package tr.metu.ceng.construction.common;

public enum MultiplayerEvent {
    START("START"),
    PLAYER1_CAPTURED("PLAYER1 CAPTURED"),
    PLAYER2_CAPTURED("PLAYER2 CAPTURED"),
    PLAYER1_WIN("PLAYER1 WIN"),
    PLAYER2_WIN("PLAYER2 WIN"),
    HAND_IS_OVER("HAND IS OVER"),
    BLUFF("BLUFF"),
    BLUFF_CHALLENGED_AND_PISTI("BLUFF CHALLENGED\n AND PISTI"),
    BLUFF_CHALLENGED_AND_NOT_PISTI("BLUFF CHALLENGED\n AND NOT PISTI"),
    BLUFF_NOT_CHALLENGED("BLUFF NOT CHALLENGED");

    private final String eventMessage;

    MultiplayerEvent(String eventMessage){
        this.eventMessage = eventMessage;
    }

    @Override
    public String toString() {
        return eventMessage;
    }
}