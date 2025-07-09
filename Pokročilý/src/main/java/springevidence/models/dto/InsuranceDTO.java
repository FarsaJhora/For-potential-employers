package springevidence.models.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) pro pojištění.
 * Slouží k přenosu dat mezi vrstvami aplikace.
 */
public class InsuranceDTO {
    private Long id;
    private String insuranceType;
    private String subject;
    private BigDecimal amount;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Long insuredPersonId;

    public InsuranceDTO() {
    }

    public InsuranceDTO(Long id, String insuranceType, String subject, BigDecimal amount, LocalDate validFrom, LocalDate validTo, Long insuredPersonId) {
        this.id = id;
        this.insuranceType = insuranceType;
        this.subject = subject;
        this.amount = amount;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.insuredPersonId = insuredPersonId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Long getInsuredPersonId() {
        return insuredPersonId;
    }

    public void setInsuredPersonId(Long insuredPersonId) {
        this.insuredPersonId = insuredPersonId;
    }


}