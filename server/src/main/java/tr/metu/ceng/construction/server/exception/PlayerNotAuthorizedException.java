package tr.metu.ceng.construction.server.exception;

public class PlayerNotAuthorizedException extends RuntimeException {
    public PlayerNotAuthorizedException(String errorMessage) {
        super(errorMessage);
    }
}
