package springevidence.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import springevidence.data.entities.User;

/**
 * Repositář pro správu uživatelů.
 * Umožňuje vyhledávat uživatele podle emailu.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}