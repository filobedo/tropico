package exceptions;

public class MissingParsingObjectException extends Exception {
    public MissingParsingObjectException(String errorMessage) {
        super(errorMessage);
    }
}
