package springevidence.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import springevidence.data.entities.User;
import springevidence.models.dto.RegistrationForm;
import springevidence.models.services.UserService;

/**
 * Controller pro správu účtů uživatelů.
 * Obsahuje metody pro přihlášení, registraci a zobrazení domovské stránky.
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Zobrazí formulář pro registraci nového uživatele.
     *
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení registračního formuláře
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "account/register";
    }

    /**
     * Zpracuje registraci nového uživatele.
     *
     * @param form Formulář s registračními údaji
     * @param bindingResult Výsledky validace formuláře
     * @param redirectAttributes Atributy pro přesměrování
     * @param model Model pro předání dat do šablony
     * @return Přesměrování na stránku přihlášení nebo zobrazení chyb v případě neúspěchu
     */
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("registrationForm") @Valid RegistrationForm form,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Hesla se neshodují.");
        }

        if (bindingResult.hasErrors()) {
            return "account/register";
        }

        try {
            User user = new User();
            user.setEmail(form.getEmail());
            user.setPassword(form.getPassword());
            userService.register(user);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "account/register";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Registrace proběhla úspěšně. Nyní se můžete přihlásit.");
        return "redirect:/account/login";
    }

    /**
     * Zobrazí přihlašovací formulář.
     *
     * @param error Chybová zpráva, pokud došlo k chybě při přihlášení
     * @param successMessage Zpráva o úspěšné registraci
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení přihlašovacího formuláře
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @ModelAttribute("successMessage") String successMessage,
                        Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Neplatný email nebo heslo.");
        }

        if (successMessage != null && !successMessage.isBlank()) {
            model.addAttribute("successMessage", successMessage);
        }

        return "account/login";
    }

    /**
     * Zobrazí domovskou stránku po úspěšném přihlášení.
     *
     * @param successMessage Zpráva o úspěšné registraci
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení domovské stránky
     */
    @GetMapping("/home")
    public String home(@ModelAttribute("successMessage") String successMessage, Model model) {
        if (successMessage != null && !successMessage.isBlank()) {
            model.addAttribute("successMessage", successMessage);
        }
        return "account/home";
    }
}