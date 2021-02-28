package exceptions;

/**
 * Exception thrown when objects are missing in the configuration file
 */
public class MissingParsingObjectException extends Exception {
    public MissingParsingObjectException(String errorMessage) {
        super(errorMessage);
    }
}
