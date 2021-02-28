package exceptions;

/**
 * Exception thrown when events are missing in the configuration file
 */
public class MissingEventsException extends Exception {
    public MissingEventsException(String errorMessage) {
        super(errorMessage);
    }
}
