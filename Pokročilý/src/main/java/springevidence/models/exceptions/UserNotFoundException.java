package springevidence.models.exceptions;

/**
 * Výjimka vyhazovaná při pokusu o práci s uživatelem, který nebyl nalezen.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }


    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}   