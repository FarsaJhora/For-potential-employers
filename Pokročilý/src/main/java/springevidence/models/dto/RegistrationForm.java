package springevidence.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) pro registrační formulář.
 * Slouží k přenosu dat mezi vrstvami aplikace.
 */
public class RegistrationForm {

    @NotBlank(message = "E-mail nesmí být prázdný.")
    @Email(message = "Zadejte platný e-mail.")
    private String email;

    @NotBlank(message = "Heslo nesmí být prázdné.")
    @Size(min = 6, message = "Heslo musí mít alespoň 6 znaků.")
    private String password;

    @NotBlank(message = "Potvrzení hesla nesmí být prázdné.")
    private String confirmPassword;

    // Gettery a settery

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}