package springevidence.models.exceptions;

/**
 * Výjimka pro neplatné přihlašovací údaje.
 * Používá se při pokusu o přihlášení s neplatným uživatelským jménem nebo heslem.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCredentialsException(Throwable cause) {
        super(cause);
    }
}
