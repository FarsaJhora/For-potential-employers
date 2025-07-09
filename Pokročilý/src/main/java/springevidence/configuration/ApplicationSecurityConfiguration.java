package springevidence.configuration;

import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import springevidence.data.entities.User;
import springevidence.data.repositories.UserRepository;

/**
 * Konfigurace bezpečnosti aplikace.
 * Tato třída nastavuje bezpečnostní pravidla pro webovou aplikaci.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class ApplicationSecurityConfiguration {

    /**
     * Konfiguruje bezpečnostní filtr řetězce pro aplikaci.
     * Všechny požadavky jsou povoleny, přihlašovací stránka je nastavena na "/account/login".
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                    .requestMatchers("/account/**").authenticated() // vše pod /account vyžaduje login
                        .anyRequest().permitAll()
                    .and()
                .formLogin()
                    .loginPage("/account/login")
                    .loginProcessingUrl("/account/login")
                    .defaultSuccessUrl("/account/home", true)
                    .failureUrl("/account/login?error=true")
                    .usernameParameter("email")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/account/logout")
                        .logoutSuccessUrl("/") // třeba na homepage
                    .and()
                .build();
    }
    
    /**
     * Vytváří UserDetailsService pro načítání uživatelských dat z databáze.
     * Používá UserRepository k vyhledání uživatele podle emailu.
     *
     * @param userRepository repozitář pro správu uživatelů
     * @return UserDetailsService instance
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> userRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        getAuthorities(user)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    /**
     * Vytváří instanci PasswordEncoder pro šifrování hesel.
     * Používá BCryptPasswordEncoder pro bezpečné šifrování hesel.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
