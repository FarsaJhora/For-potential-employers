package springevidence.models.exceptions;

/**
 * Výjimka pro situaci, kdy uživatel již existuje.
 * Používá se při pokusu o registraci uživatele s e-mailem, který již v systému existuje.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }


    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}