package springevidence.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import springevidence.data.entities.Insurance;
import springevidence.models.services.InsuranceService;
import springevidence.models.services.UserService;

/**
 * InsuranceController obstarává veškerou logiku spojenou s pojištěními.
 * Obsahuje jak metody pro zobrazení pojištění v HTML šablonách pomocí Thymeleaf,
 * tak i REST API pro práci s pojištěními v JSON formátu.
 */
@Controller
@RequestMapping("/insurances")
public class InsuranceController {

    private final InsuranceService insuranceService;
    private final UserService userService;

    public InsuranceController(InsuranceService insuranceService, UserService userService) {
        this.insuranceService = insuranceService;
        this.userService = userService;
    }

    // =======================
    // THYMELEAF (WEB)
    // =======================

    /**
     * Zobrazí seznam všech pojištění.
     *
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení seznamu pojištění
     */
    @GetMapping
    public String listInsurances(Model model) {
        model.addAttribute("insurances", insuranceService.getAllInsurances());
        return "insurances/list";
    }

    /**
     * Zobrazí formulář pro vytvoření nového pojištění.
     *
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení formuláře
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("insurance", new Insurance());
        model.addAttribute("users", userService.getAllUsers());
        return "insurances/create";
    }

    /**
     * Zpracuje vytvoření nového pojištění.
     *
     * @param userId ID uživatele, který pojištění vytvořil
     * @param insurance Pojištění k vytvoření
     * @return Přesměrování na seznam pojištění
     */
    @PostMapping
    public String createInsurance(@RequestParam Long userId, @ModelAttribute Insurance insurance) {
        insuranceService.createInsurance(userId, insurance);
        return "redirect:/insurances";
    }

    /**
     * Zobrazí formulář pro úpravu existujícího pojištění.
     *
     * @param id ID pojištění k úpravě
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení formuláře
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Insurance insurance = insuranceService.getInsuranceById(id).orElseThrow(() -> new RuntimeException("Insurance not found"));
        model.addAttribute("insurance", insurance);
        model.addAttribute("users", userService.getAllUsers());
        return "insurances/edit";
    }

    /**
     * Zpracuje aktualizaci existujícího pojištění.
     *
     * @param id ID pojištění k aktualizaci
     * @param insurance Pojištění s aktualizovanými daty
     * @return Přesměrování na seznam pojištění
     */
    @PostMapping("/update/{id}")
    public String updateInsurance(@PathVariable Long id, @ModelAttribute Insurance insurance) {
        insuranceService.updateInsurance(id, insurance);
        return "redirect:/insurances";
    }

    /**
     * Odstraní pojištění.
     *
     * @param id ID pojištění k odstranění
     * @return Přesměrování na seznam pojištění
     */
    @GetMapping("/delete/{id}")
    public String deleteInsurance(@PathVariable Long id) {
        insuranceService.deleteInsurance(id);
        return "redirect:/insurances";
    }

    /**
     * Zobrazí detail pojištění.
     *
     * @param id ID pojištění k zobrazení
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení detailu pojištění
     */
    @GetMapping("/{id}")
    public String viewInsurance(@PathVariable Long id, Model model) {
        Insurance insurance = insuranceService.getInsuranceById(id).orElseThrow(() -> new RuntimeException("Insurance not found"));
        model.addAttribute("insurance", insurance);
        return "insurances/detail";
    }

    // =======================
    // REST API (JSON)
    // =======================

    /**
     * Získá seznam všech pojištění ve formátu JSON.
     *
     * @return Seznam pojištění
     */
    @GetMapping("/api")
    @ResponseBody
    public List<Insurance> getAllInsurancesApi() {
        return insuranceService.getAllInsurances();
    }

    /**
     * Získá pojištění podle ID ve formátu JSON.
     *
     * @param id ID pojištění
     * @return Pojištění nebo 404 Not Found
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Insurance> getInsuranceApi(@PathVariable Long id) {
        return insuranceService.getInsuranceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Vytvoří nové pojištění ve formátu JSON.
     *
     * @param userId ID uživatele, který pojištění vytvořil
     * @param insurance Pojištění k vytvoření
     * @return Vytvořené pojištění
     */
    @PostMapping("/api")
    @ResponseBody
    public Insurance createInsuranceApi(@RequestParam Long userId, @RequestBody Insurance insurance) {
        return insuranceService.createInsurance(userId, insurance);
    }

    /**
     * Aktualizuje existující pojištění ve formátu JSON.
     *
     * @param id ID pojištění k aktualizaci
     * @param insurance Pojištění s aktualizovanými daty
     * @return Aktualizované pojištění nebo 404 Not Found
     */
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Insurance> updateInsuranceApi(@PathVariable Long id, @RequestBody Insurance insurance) {
        return ResponseEntity.ok(insuranceService.updateInsurance(id, insurance));
    }

    /**
     * Odstraní pojištění podle ID ve formátu JSON.
     *
     * @param id ID pojištění k odstranění
     * @return 200 OK nebo 404 Not Found
     */
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteInsuranceApi(@PathVariable Long id) {
        insuranceService.deleteInsurance(id);
        return ResponseEntity.ok().build();
    }
}