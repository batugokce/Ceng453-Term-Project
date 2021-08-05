package tr.metu.ceng.construction.server.exception;

public class GameNotMultiPlayerException extends RuntimeException {
    public GameNotMultiPlayerException(String errorMessage) {
        super(errorMessage);
    }
}
