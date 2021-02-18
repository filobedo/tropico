package exceptions;

public class MissingEventsException extends Exception {
    public MissingEventsException(String errorMessage) {
        super(errorMessage);
    }
}
