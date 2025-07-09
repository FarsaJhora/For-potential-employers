package springevidence.models.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import springevidence.data.entities.User;
import springevidence.data.entities.UserRole;
import springevidence.data.repositories.UserRepository;
import springevidence.models.dto.UserDTO;
import springevidence.models.dto.mappers.UserMapper;
import springevidence.models.exceptions.UserAlreadyExistsException;
import springevidence.models.exceptions.UserNotFoundException;

/**
 * UserService poskytuje služby pro správu uživatelů v aplikaci.
 * Obsahuje metody pro získání, vytvoření, aktualizaci a odstranění uživatelů.
 * Také poskytuje metody pro registraci nových uživatelů a práci s uživatelskými daty ve formátu DTO.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Získá uživatele podle ID.
     *
     * @param id ID uživatele
     * @return Uživatel nebo prázdný Optional, pokud uživatel neexistuje
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Získá uživatele podle ID.
     *
     * @param id ID uživatele
     * @return Uživatel nebo prázdný Optional, pokud uživatel neexistuje
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * Získá všechny uživatele ve formátu DTO.
     *
     * @return Seznam uživatelů ve formátu DTO
     */
    public List<UserDTO> findAllDTO() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Získá uživatele podle ID ve formátu DTO.
     *
     * @param id ID uživatele
     * @return Uživatel ve formátu DTO
     * @throws IllegalArgumentException pokud uživatel s daným ID neexistuje
     */
    public UserDTO findByIdDTO(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Uživatel nenalezen"));
        return userMapper.toDTO(user);
    }

    /**
     * Vytvoří nového uživatele.
     *
     * @param user Uživatel k vytvoření
     * @return Vytvořený uživatel
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Aktualizuje existujícího uživatele.
     *
     * @param id ID uživatele k aktualizaci
     * @param user Uživatel s aktualizovanými daty
     * @return Aktualizovaný uživatel
     * @throws UserNotFoundException pokud uživatel s daným ID neexistuje
     */
    public User updateUser(Long id, User user) {
        User existing = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Uživatel s ID " + id + " nebyl nalezen"));

        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setAddress(user.getAddress());
        existing.setPhoneNumber(user.getPhoneNumber());
        existing.setEmail(user.getEmail());
        existing.setDateOfBirth(user.getDateOfBirth());
        existing.setRole(user.getRole());

        return userRepository.save(existing);
    }

    /**
     * Odstraní uživatele podle ID.
     *
     * @param id ID uživatele k odstranění
     * @throws UserNotFoundException pokud uživatel s daným ID neexistuje
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Uživatel s ID " + id + " nebyl nalezen");
        }
        userRepository.deleteById(id);
    }

    /**
     * Získá uživatele podle ID.
     *
     * @param id ID uživatele
     * @return Uživatel nebo prázdný Optional, pokud uživatel neexistuje
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Získá všechny uživatele.
     *
     * @return Seznam všech uživatelů
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Získá uživatele podle e-mailu.
     *
     * @param email E-mail uživatele
     * @return Uživatel nebo prázdný Optional, pokud uživatel neexistuje
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Získá uživatele podle ID.
     *
     * @param id ID uživatele
     * @return Uživatel nebo prázdný Optional, pokud uživatel neexistuje
     */
    public Optional<User> findById(Long id) {
        if (userRepository.existsById(id)) {
            return userRepository.findById(id);
        }
        return Optional.empty();
    }

    /**
     * Odstraní uživatele podle ID.
     *
     * @param id ID uživatele k odstranění
     * @throws UserNotFoundException pokud uživatel s daným ID neexistuje
     */
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Uživatel s ID " + id + " nebyl nalezen");
        }
        throw new UnsupportedOperationException("deleteById not implemented yet");
    }

    /**
     * Registruje nového uživatele.
     *
     * @param user Uživatel k registraci
     * @throws UserAlreadyExistsException pokud uživatel s daným e-mailem již existuje
     * @throws IllegalArgumentException pokud heslo je prázdné
     */
    public void register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Uživatel s tímto emailem již existuje");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Heslo nesmí být prázdné");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole(UserRole.USER);

        userRepository.save(user);
    }
}