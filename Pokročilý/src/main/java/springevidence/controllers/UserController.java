package springevidence.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import springevidence.data.entities.User;
import springevidence.models.services.UserService;

/**
 * UserController obstarává veškerou logiku spojenou s uživateli.
 * Obsahuje jak metody pro zobrazení uživatelů v HTML šablonách pomocí Thymeleaf,
 * tak i REST API pro práci s uživateli v JSON formátu.
 */
@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // =======================
    // THYMELEAF (WEB)
    // =======================

    /**
     * Zobrazí seznam všech uživatelů.
     *
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení seznamu uživatelů
     */
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }

    /**
     * Zobrazí formulář pro vytvoření nového uživatele.
     *
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení formuláře
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "users/create";
    }

    /**
     * Zpracuje vytvoření nového uživatele.
     *
     * @param user Uživatel k vytvoření
     * @return Přesměrování na seznam uživatelů
     */
    @PostMapping
    public String createUser(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:/users";
    }

    /**
     * Zobrazí profil přihlášeného uživatele.
     *
     * @param user Přihlášený uživatel
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení profilu uživatele
     */
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user/profile";
    }

    /**
     * Zobrazí formulář pro úpravu existujícího uživatele.
     *
     * @param id ID uživatele k úpravě
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení formuláře
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "users/edit";
    }

    /**
     * Zpracuje aktualizaci existujícího uživatele.
     *
     * @param id ID uživatele k aktualizaci
     * @param user Uživatel s aktualizovanými daty
     * @return Přesměrování na seznam uživatelů
     */
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/users";
    }

    /**
     * Odstraní uživatele.
     *
     * @param id ID uživatele k odstranění
     * @return Přesměrování na seznam uživatelů
     */
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    /**
     * Zobrazí detail uživatele.
     *
     * @param id ID uživatele k zobrazení
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení detailu uživatele
     */
    @GetMapping("/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "users/detail";
    }

    // =======================
    // REST API (JSON)
    // =======================

    /**
     * Získá seznam všech uživatelů ve formátu JSON.
     *
     * @return Seznam uživatelů
     */
    @GetMapping("/api")
    @ResponseBody
    public List<User> getAllUsersApi() {
        return userService.getAllUsers();
    }

    /**
     * Získá uživatele podle ID ve formátu JSON.
     *
     * @param id ID uživatele
     * @return Uživatel nebo 404 Not Found
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserApi(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Vytvoří nového uživatele ve formátu JSON.
     *
     * @param user Uživatel k vytvoření
     * @return Vytvořený uživatel
     */
    @PostMapping("/api")
    @ResponseBody
    public User createUserApi(@RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * Aktualizuje existujícího uživatele ve formátu JSON.
     *
     * @param id ID uživatele k aktualizaci
     * @param user Uživatel s aktualizovanými daty
     * @return Aktualizovaný uživatel nebo 404 Not Found
     */
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<User> updateUserApi(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    /**
     * Odstraní uživatele podle ID ve formátu JSON.
     *
     * @param id ID uživatele k odstranění
     * @return 200 OK nebo 404 Not Found
     */
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUserApi(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}