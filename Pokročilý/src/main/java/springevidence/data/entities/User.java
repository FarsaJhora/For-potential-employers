package springevidence.data.entities;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Třída reprezentující uživatele v systému.
 * Obsahuje informace o uživatelském jménu, příjmení, adrese, telefonním čísle,
 * emailu, hesle, datu narození a roli uživatele.
 */
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Jméno nesmí být prázdné")
    @Size(max = 50, message = "Jméno může mít maximálně 50 znaků")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Příjmení nesmí být prázdné")
    @Size(max = 50, message = "Příjmení může mít maximálně 50 znaků")
    @Column(nullable = false)
    private String lastName;

    @Size(max = 255, message = "Adresa může mít maximálně 255 znaků")
    private String address;

    @Size(max = 20, message = "Telefonní číslo může mít maximálně 20 znaků")
    private String phoneNumber;

    @NotBlank(message = "Email nesmí být prázdný")
    @Email(message = "Email musí být ve správném formátu")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Heslo nesmí být prázdné")
    @Size(min = 8, message = "Heslo musí mít alespoň 8 znaků")
    @Column(nullable = false)
    private String password;

    @Past(message = "Datum narození musí být v minulosti")
    private LocalDate dateOfBirth;

    @NotNull(message = "Role musí být zadána")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Insurance> insurances;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InsuranceEvent> insuranceEvents;

    public User() {}

    public User(String firstName, String lastName, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }

    public User(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Long getId() {
    return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Vrací heslo uživatele.
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<Insurance> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<Insurance> insurances) {
        this.insurances = insurances;
    }

    public List<InsuranceEvent> getInsuranceEvents() {
        return insuranceEvents;
    }

    public void setInsuranceEvents(List<InsuranceEvent> insuranceEvents) {
        this.insuranceEvents = insuranceEvents;
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", role=" + role +
               '}';
    }
}