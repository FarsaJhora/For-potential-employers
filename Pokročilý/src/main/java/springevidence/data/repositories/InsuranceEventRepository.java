package springevidence.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import springevidence.data.entities.InsuranceEvent;

/**
 * Repositář pro správu událostí pojištění.
 * Umožňuje vyhledávat události podle ID pojištění a ID uživatele.
 */
public interface InsuranceEventRepository extends JpaRepository<InsuranceEvent, Long> {
    List<InsuranceEvent> findByInsuranceId(Long insuranceId);
    List<InsuranceEvent> findByUserId(Long userId);
}
