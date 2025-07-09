package springevidence.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import springevidence.data.entities.User;
import springevidence.models.services.UserService;

/**
 * Správa administrátorských funkcí, jako je správa uživatelů.
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Zobrazí seznam všech uživatelů.
     *
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení seznamu uživatelů
     */
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    /**
     * Zobrazí formulář pro vytvoření nového uživatele.
     *
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení formuláře
     */
    @GetMapping("/user/new")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user-form";
    }    

    /**
     * Uloží nového uživatele.
     *
     * @param user Uživatel k uložení
     * @return Přesměrování na seznam uživatelů
     */
    @GetMapping("/user/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Uživatel nenalezen"));
        model.addAttribute("user", user);
        return "admin/user-detail";
    }

    /**
     * Odstraní uživatele.
     *
     * @param id ID uživatele k odstranění
     * @return Přesměrování na seznam uživatelů
     */
    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    /**
     * Zobrazí dashboard s přehledem statistik.
     *
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení dashboardu
     */
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String dashboard(Model model) {
        model.addAttribute("insuranceTypeLabels", List.of("Byt", "Auto", "Cestovní"));
        model.addAttribute("insuranceTypeCounts", List.of(10, 5, 7));

        model.addAttribute("eventMonths", List.of("Leden", "Únor", "Březen"));
        model.addAttribute("eventCounts", List.of(3, 7, 2));

        model.addAttribute("amountMonths", List.of("Leden", "Únor", "Březen"));
        model.addAttribute("amountSums", List.of(12000, 25000, 18000));

        return "admin/dashboard";
    }
}
