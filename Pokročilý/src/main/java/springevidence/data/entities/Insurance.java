package springevidence.data.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Třída reprezentující entitu pojištění.
 * Obsahuje informace o pojištění, jako je název, popis, částka pojištění,
 * platnost a vztah k uživateli a událostem pojištění.
 */
@Entity
@Table(name = "insurance")
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Název pojištění nesmí být prázdný")
    @Size(max = 100, message = "Název může mít maximálně 100 znaků")
    @Column(nullable = false)
    private String name;

    @Size(max = 1000, message = "Popis může mít maximálně 1000 znaků")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Částka pojištění musí být zadána")
    @DecimalMin(value = "0.0", inclusive = false, message = "Částka pojištění musí být větší než 0")
    @Column(nullable = false)
    private BigDecimal insuranceAmount;

    @NotNull(message = "Datum platnosti od musí být zadáno")
    @FutureOrPresent(message = "Datum platnosti od musí být dnešní nebo budoucí")
    @Column(nullable = false)
    private LocalDate validFrom;

    @NotNull(message = "Datum platnosti do musí být zadáno")
    @FutureOrPresent(message = "Datum platnosti do musí být v budoucnu")
    @Column(nullable = false)
    private LocalDate validTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "insurance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InsuranceEvent> insuranceEvents;

    // Konstruktor bez parametrů
    public Insurance() {}

    // Volitelný konstruktor
    public Insurance(String name, String description, BigDecimal insuranceAmount,
                     LocalDate validFrom, LocalDate validTo, User user) {
        this.name = name;
        this.description = description;
        this.insuranceAmount = insuranceAmount;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.user = user;
    }

    // Gettery a settery

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<InsuranceEvent> getInsuranceEvents() {
        return insuranceEvents;
    }

    public void setInsuranceEvents(List<InsuranceEvent> insuranceEvents) {
        this.insuranceEvents = insuranceEvents;
    }

    @Override
    public String toString() {
        return "Insurance{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", insuranceAmount=" + insuranceAmount +
               ", validFrom=" + validFrom +
               ", validTo=" + validTo +
               '}';
    }
}