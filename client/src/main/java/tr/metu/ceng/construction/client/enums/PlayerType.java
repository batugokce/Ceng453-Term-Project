package tr.metu.ceng.construction.client.enums;

/**
 * Holds different player types.
 */
public enum PlayerType {
    PLAYER1("PLAYER1"),
    PLAYER2("PLAYER2"),
    COMPUTER("COMPUTER");

    private String playerType;
    PlayerType(String playerType){
        this.playerType = playerType;
    }
    @Override
    public String toString() {
        return playerType;
    }

}
