package springevidence.models.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Globální zpracování výjimek pro aplikaci.
 * Obsahuje metody pro zpracování specifických výjimek a zobrazení chybových stránek.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExists(UserAlreadyExistsException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("user", new springevidence.data.entities.User());
        return "account/register";
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public String handleInvalidCredentials(InvalidCredentialsException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "account/login";
    }

    @ExceptionHandler(Exception.class)
    public String handleOtherExceptions(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Došlo k neočekávané chybě: " + ex.getMessage());
        return "error/general";
    }
}