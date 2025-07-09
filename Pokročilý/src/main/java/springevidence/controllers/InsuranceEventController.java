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

import springevidence.data.entities.InsuranceEvent;
import springevidence.models.services.InsuranceEventService;
import springevidence.models.services.InsuranceService;
import springevidence.models.services.UserService;

/**
 * InsuranceEventController obstarává veškerou logiku spojenou s událostmi pojištění.
 * Obsahuje jak metody pro zobrazení událostí v HTML šablonách pomocí Thymeleaf,
 * tak i REST API pro práci s událostmi v JSON formátu.
 */
@Controller
@RequestMapping("/events")
public class InsuranceEventController {

    private final InsuranceEventService eventService;
    private final InsuranceService insuranceService;
    private final UserService userService;

    public InsuranceEventController(InsuranceEventService eventService, InsuranceService insuranceService, UserService userService) {
        this.eventService = eventService;
        this.insuranceService = insuranceService;
        this.userService = userService;
    }

    // =======================
    // THYMELEAF (WEB)
    // =======================

    /**
     * Zobrazí seznam všech událostí.
     *
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení seznamu událostí
     */
    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "events/list";
    }

    /**
     * Zobrazí formulář pro vytvoření nové události.
     *
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení formuláře
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new InsuranceEvent());
        model.addAttribute("insurances", insuranceService.getAllInsurances());
        model.addAttribute("users", userService.getAllUsers());
        return "events/create";
    }

    /**
     * Zpracuje vytvoření nové události.
     *
     * @param insuranceId ID pojištění, ke kterému se událost vztahuje
     * @param userId ID uživatele, který událost vytvořil
     * @param event Událost k vytvoření
     * @return Přesměrování na seznam událostí
     */
    @PostMapping
    public String createEvent(@RequestParam Long insuranceId, @RequestParam Long userId, @ModelAttribute InsuranceEvent event) {
        eventService.createEvent(insuranceId, userId, event);
        return "redirect:/events";
    }

    /**
     * Zobrazí formulář pro úpravu existující události.
     *
     * @param id ID události k úpravě
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení formuláře
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        InsuranceEvent event = eventService.getEventById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event", event);
        model.addAttribute("insurances", insuranceService.getAllInsurances());
        model.addAttribute("users", userService.getAllUsers());
        return "events/edit";
    }

    /**
     * Zpracuje aktualizaci existující události.
     *
     * @param id ID události k aktualizaci
     * @param event Událost s aktualizovanými daty
     * @return Přesměrování na seznam událostí
     */
    @PostMapping("/update/{id}")
    public String updateEvent(@PathVariable Long id, @ModelAttribute InsuranceEvent event) {
        eventService.updateEvent(id, event);
        return "redirect:/events";
    }

    /**
     * Odstraní událost.
     *
     * @param id ID události k odstranění
     * @return Přesměrování na seznam událostí
     */
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/events";
    }

    /**
     * Zobrazí detail události.
     *
     * @param id ID události k zobrazení
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení detailu události
     */
    @GetMapping("/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        InsuranceEvent event = eventService.getEventById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event", event);
        return "events/detail";
    }

    // =======================
    // REST API (JSON)
    // =======================

    /**
     * Získá seznam všech událostí ve formátu JSON.
     *
     * @return Seznam událostí
     */
    @GetMapping("/api")
    @ResponseBody
    public List<InsuranceEvent> getAllEventsApi() {
        return eventService.getAllEvents();
    }

    /**
     * Získá událost podle ID ve formátu JSON.
     *
     * @param id ID události
     * @return Událost nebo 404 Not Found
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<InsuranceEvent> getEventApi(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Vytvoří nové pojištění ve formátu JSON.
     *
     * @param insuranceId ID pojištění, ke kterému se událost vztahuje
     * @param userId ID uživatele, který událost vytvořil
     * @param event Událost k vytvoření
     * @return Vytvořená událost
     */
    @PostMapping("/api")
    @ResponseBody
    public InsuranceEvent createEventApi(@RequestParam Long insuranceId, @RequestParam Long userId, @RequestBody InsuranceEvent event) {
        return eventService.createEvent(insuranceId, userId, event);
    }

    /**
     * Aktualizuje existující událost ve formátu JSON.
     *
     * @param id ID události k aktualizaci
     * @param event Událost s aktualizovanými daty
     * @return Aktualizovaná událost nebo 404 Not Found
     */
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<InsuranceEvent> updateEventApi(@PathVariable Long id, @RequestBody InsuranceEvent event) {
        return ResponseEntity.ok(eventService.updateEvent(id, event));
    }

    /**
     * Odstraní událost podle ID ve formátu JSON.
     *
     * @param id ID události k odstranění
     * @return 200 OK nebo 404 Not Found
     */
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteEventApi(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
}