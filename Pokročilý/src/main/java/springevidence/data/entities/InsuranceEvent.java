package springevidence.data.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

/**
 * Třída reprezentující událost pojištění.
 * Obsahuje informace o události, jako je název, popis, datum události,
 * částka škody a vztah k pojištění a uživateli.
 */
@Entity
@Table(name = "insurance_event")
public class InsuranceEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Název události nesmí být prázdný")
    @Size(max = 100, message = "Název může mít maximálně 100 znaků")
    @Column(nullable = false)
    private String title;

    @Size(max = 1000, message = "Popis může mít maximálně 1000 znaků")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Datum události musí být zadáno")
    @PastOrPresent(message = "Datum události nemůže být v budoucnu")
    @Column(nullable = false)
    private LocalDate eventDate;

    @NotNull(message = "Částka škody musí být zadána")
    @DecimalMin(value = "0.0", inclusive = true, message = "Částka škody nemůže být záporná")
    @Column(nullable = false)
    private BigDecimal damageAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id", nullable = false)
    private Insurance insurance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Konstruktor bez parametrů
    public InsuranceEvent() {}

    // Volitelný konstruktor
    public InsuranceEvent(String title, String description, LocalDate eventDate,
                          BigDecimal damageAmount, Insurance insurance, User user) {
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.damageAmount = damageAmount;
        this.insurance = insurance;
        this.user = user;
    }

    // Gettery a settery

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public BigDecimal getDamageAmount() {
        return damageAmount;
    }

    public void setDamageAmount(BigDecimal damageAmount) {
        this.damageAmount = damageAmount;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "InsuranceEvent{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", eventDate=" + eventDate +
               ", damageAmount=" + damageAmount +
               '}';
    }
}