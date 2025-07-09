package springevidence.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import springevidence.data.entities.Insurance;

/**
 * Repositář pro správu pojištění.
 * Umožňuje vyhledávat pojištění podle ID uživatele.
 */
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    List<Insurance> findByUserId(Long userId);
}