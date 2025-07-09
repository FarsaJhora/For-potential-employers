package springevidence.models.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) pro události spojené s pojištěním.
 * Slouží k přenosu dat mezi vrstvami aplikace.
 */
public class InsuranceEventDTO {
    private Long id;
    private String description;
    private LocalDate date;
    private Long insuranceId;

    private java.time.LocalDate eventDate;

    public java.time.LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(java.time.LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public InsuranceEventDTO() {
    }

    public InsuranceEventDTO(Long id, String description, LocalDate date, Long insuranceId) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.insuranceId = insuranceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Long insuranceId) {
        this.insuranceId = insuranceId;
    }
}
