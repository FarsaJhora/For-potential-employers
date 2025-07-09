package springevidence.models.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import springevidence.data.entities.Insurance;
import springevidence.data.entities.InsuranceEvent;
import springevidence.data.entities.User;
import springevidence.data.repositories.InsuranceEventRepository;
import springevidence.data.repositories.InsuranceRepository;
import springevidence.data.repositories.UserRepository;

/**
 * Implementace služby pro správu událostí spojených s pojištěním.
 * Obsahuje metody pro vytváření, aktualizaci, mazání a získávání informací o událostech.
 */
@Service
@Transactional
public class InsuranceEventServiceImpl implements InsuranceEventService {

    private final InsuranceEventRepository eventRepository;
    private final InsuranceRepository insuranceRepository;
    private final UserRepository userRepository;

    public InsuranceEventServiceImpl(InsuranceEventRepository eventRepository,
                                     InsuranceRepository insuranceRepository,
                                     UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.insuranceRepository = insuranceRepository;
        this.userRepository = userRepository;
    }

    /**
     * Vytvoří novou událost spojenou s pojištěním.
     *
     * @param insuranceId ID pojištění, ke kterému se událost vztahuje
     * @param userId ID uživatele, který událost vytváří
     * @param event Událost, která má být vytvořena
     * @return Vytvořená událost
     */
    @Override
    public InsuranceEvent createEvent(Long insuranceId, Long userId, InsuranceEvent event) {
        Insurance insurance = insuranceRepository.findById(insuranceId)
            .orElseThrow(() -> new RuntimeException("Insurance not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        event.setInsurance(insurance);
        event.setUser(user);
        return eventRepository.save(event);
    }

    /**
     * Aktualizuje existující událost spojenou s pojištěním.
     *
     * @param id ID události, která má být aktualizována
     * @param event Nové hodnoty události
     * @return Aktualizovaná událost
     */
    @Override
    public InsuranceEvent updateEvent(Long id, InsuranceEvent event) {
        InsuranceEvent existing = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        existing.setTitle(event.getTitle());
        existing.setDescription(event.getDescription());
        existing.setEventDate(event.getEventDate());
        existing.setDamageAmount(event.getDamageAmount());
        return eventRepository.save(existing);
    }

    /**
     * Smaže událost podle jejího ID.
     *
     * @param id ID události, která má být smazána
     */
    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    /**
     * Získá událost podle jejího ID.
     *
     * @param id ID události, která má být získána
     * @return Volitelná událost
     */
    @Override
    public Optional<InsuranceEvent> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    /**
     * Získá všechny události spojené s pojištěním.
     *
     * @return Seznam všech událostí
     */
    @Override
    public List<InsuranceEvent> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Získá události spojené s konkrétním pojištěním podle jeho ID.
     *
     * @param insuranceId ID pojištění, pro které se mají získat události
     * @return Seznam událostí spojených s daným pojištěním
     */
    @Override
    public List<InsuranceEvent> getEventsByInsuranceId(Long insuranceId) {
        return eventRepository.findByInsuranceId(insuranceId);
    }

    /**
     * Získá události spojené s konkrétním uživatelem podle jeho ID.
     *
     * @param userId ID uživatele, pro kterého se mají získat události
     * @return Seznam událostí spojených s daným uživatelem
     */
    @Override
    public List<InsuranceEvent> getEventsByUserId(Long userId) {
        return eventRepository.findByUserId(userId);
    }
}