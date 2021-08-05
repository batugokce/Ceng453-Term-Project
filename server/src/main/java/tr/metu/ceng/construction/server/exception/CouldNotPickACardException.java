package tr.metu.ceng.construction.server.exception;

public class CouldNotPickACardException extends RuntimeException {
    public CouldNotPickACardException(String errorMessage) {
        super(errorMessage);
    }
}
