package noelle.libraries.exceptions;

public final class InvalidHashException extends RuntimeException {

    public InvalidHashException(String message) {
        super(message);
    }

    public InvalidHashException(String message, Throwable cause) {
        super(message, cause);
    }
}
