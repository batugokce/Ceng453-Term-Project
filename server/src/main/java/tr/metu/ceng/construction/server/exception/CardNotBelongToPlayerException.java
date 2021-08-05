package tr.metu.ceng.construction.server.exception;

public class CardNotBelongToPlayerException extends RuntimeException {
    public CardNotBelongToPlayerException(String errorMessage) {
        super(errorMessage);
    }
}