package springevidence.models.services;

import java.util.List;
import java.util.Optional;

import springevidence.data.entities.Insurance;

/**
 * Interface pro službu správy pojištění.
 * Obsahuje metody pro vytváření, aktualizaci, mazání a získávání informací o pojištěních.
 */
public interface InsuranceService {
    Insurance createInsurance(Long userId, Insurance insurance);
    Insurance updateInsurance(Long id, Insurance insurance);
    void deleteInsurance(Long id);
    Optional<Insurance> getInsuranceById(Long id);
    List<Insurance> getAllInsurances();
    List<Insurance> getInsurancesByUserId(Long userId);
}
