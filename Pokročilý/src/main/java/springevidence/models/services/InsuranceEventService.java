package springevidence.models.services;

import java.util.List;
import java.util.Optional;

import springevidence.data.entities.InsuranceEvent;

/**
 * Služba pro správu událostí spojených s pojištěním.
 * Obsahuje metody pro vytváření, aktualizaci, mazání a získávání informací o událostech.
 */
public interface InsuranceEventService {
    InsuranceEvent createEvent(Long insuranceId, Long userId, InsuranceEvent event);
    InsuranceEvent updateEvent(Long id, InsuranceEvent event);
    void deleteEvent(Long id);
    Optional<InsuranceEvent> getEventById(Long id);
    List<InsuranceEvent> getAllEvents();
    List<InsuranceEvent> getEventsByInsuranceId(Long insuranceId);
    List<InsuranceEvent> getEventsByUserId(Long userId);
}