package exceptions;

public class MissingParsingKeysException extends Exception {
    public MissingParsingKeysException(String errorMessage) {
        super(errorMessage);
    }
}
