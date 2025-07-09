package springevidence.models.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import springevidence.data.entities.User;
import springevidence.data.entities.UserRole;
import springevidence.data.repositories.UserRepository;
import springevidence.models.dto.mappers.UserMapper;
import springevidence.models.exceptions.UserAlreadyExistsException;
import springevidence.models.exceptions.UserNotFoundException;

/**
 * Implementace služby pro správu uživatelů.
 * Rozšiřuje základní UserService.
 */
@Service
@Transactional
public class UserServiceImpl extends UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        super(userRepository, passwordEncoder, userMapper);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Vytvoří nového uživatele.
     */
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Aktualizuje existujícího uživatele.
     */
    @Override
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
     * Smaže uživatele podle ID.
     */
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Uživatel s ID " + id + " nebyl nalezen");
        }
        userRepository.deleteById(id);
    }

    /**
     * Získá uživatele podle ID.
     */
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Získá všechny uživatele.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Získá uživatele podle emailu.
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Registruje nového uživatele (včetně hashování hesla).
     */
    @Override
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

    /**
     * Vrací seznam všech uživatelů.
     */
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}