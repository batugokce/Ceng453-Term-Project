package tr.metu.ceng.construction.server.exception;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}