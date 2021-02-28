package exceptions;

/**
 * Exception thrown when keys are missing in the configuration file
 */
public class MissingParsingKeysException extends Exception {
    public MissingParsingKeysException(String errorMessage) {
        super(errorMessage);
    }
}
