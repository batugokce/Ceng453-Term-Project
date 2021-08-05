package tr.metu.ceng.construction.server.exception;

public class FaceUpCardsNotValidException extends RuntimeException {
    public FaceUpCardsNotValidException(String errorMessage) {
        super(errorMessage);
    }
}
