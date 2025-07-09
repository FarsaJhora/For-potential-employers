package springevidence.models.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import springevidence.data.entities.Insurance;
import springevidence.data.entities.User;
import springevidence.data.repositories.InsuranceRepository;
import springevidence.data.repositories.UserRepository;

/**
 * Implementace služby pro správu pojištění.
 * Obsahuje metody pro vytváření, aktualizaci, mazání a získávání informací o pojištěních.
 */
@Service
@Transactional
public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final UserRepository userRepository;

    public InsuranceServiceImpl(InsuranceRepository insuranceRepository, UserRepository userRepository) {
        this.insuranceRepository = insuranceRepository;
        this.userRepository = userRepository;
    }

    /**
     * Vytvoří nové pojištění a přiřadí ho uživateli.
     *
     * @param userId ID uživatele, ke kterému bude pojištění přiřazeno
     * @param insurance Pojištění, které má být vytvořeno
     * @return Vytvořené pojištění
     */
    @Override
    public Insurance createInsurance(Long userId, Insurance insurance) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        insurance.setUser(user);
        return insuranceRepository.save(insurance);
    }

    /**
     * Aktualizuje existující pojištění.
     *
     * @param id ID pojištění, které má být aktualizováno
     * @param insurance Nové hodnoty pojištění
     * @return Aktualizované pojištění
     */
    @Override
    public Insurance updateInsurance(Long id, Insurance insurance) {
        Insurance existing = insuranceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Insurance not found"));
        existing.setName(insurance.getName());
        existing.setDescription(insurance.getDescription());
        existing.setInsuranceAmount(insurance.getInsuranceAmount());
        existing.setValidFrom(insurance.getValidFrom());
        existing.setValidTo(insurance.getValidTo());
        return insuranceRepository.save(existing);
    }

    /**
     * Smaže pojištění podle jeho ID.
     *
     * @param id ID pojištění, které má být smazáno
     */
    @Override
    public void deleteInsurance(Long id) {
        insuranceRepository.deleteById(id);
    }

    /**
     * Získá pojištění podle jeho ID.
     *
     * @param id ID pojištění, které má být získáno
     * @return Volitelný objekt obsahující pojištění, pokud existuje
     */
    @Override
    public Optional<Insurance> getInsuranceById(Long id) {
        return insuranceRepository.findById(id);
    }

    /**
     * Získá všechna pojištění.
     *
     * @return Seznam všech pojištění
     */
    @Override
    public List<Insurance> getAllInsurances() {
        return insuranceRepository.findAll();
    }

    /**
     * Získá pojištění podle ID uživatele.
     *
     * @param userId ID uživatele, jehož pojištění mají být získána
     * @return Seznam pojištění spojených s daným uživatelem
     */
    @Override
    public List<Insurance> getInsurancesByUserId(Long userId) {
        return insuranceRepository.findByUserId(userId);
    }
}