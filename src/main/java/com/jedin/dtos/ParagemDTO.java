package com.jedin.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class ParagemDTO {

    private Long id;

    @NotNull(message = "Latitude é obrigatória")
    @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90")
    private BigDecimal latitude;

    @NotNull(message = "Longitude é obrigatória")
    @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180")
    private BigDecimal longitude;

    @NotNull(message = "Categoria de paragem é obrigatória")
    private Long categoriaParagemId;

    private String categoriaParagemNome;

    // Constructors
    public ParagemDTO() {}

    public ParagemDTO(BigDecimal latitude, BigDecimal longitude, Long categoriaParagemId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.categoriaParagemId = categoriaParagemId;
    }

    public ParagemDTO(Long id, BigDecimal latitude, BigDecimal longitude, Long categoriaParagemId, String categoriaParagemNome) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.categoriaParagemId = categoriaParagemId;
        this.categoriaParagemNome = categoriaParagemNome;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Long getCategoriaParagemId() {
        return categoriaParagemId;
    }

    public void setCategoriaParagemId(Long categoriaParagemId) {
        this.categoriaParagemId = categoriaParagemId;
    }

    public String getCategoriaParagemNome() {
        return categoriaParagemNome;
    }

    public void setCategoriaParagemNome(String categoriaParagemNome) {
        this.categoriaParagemNome = categoriaParagemNome;
    }

    @Override
    public String toString() {
        return (
            "ParagemDTO{" +
            "id=" + id +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", categoriaParagemId=" + categoriaParagemId +
            ", categoriaParagemNome='" + categoriaParagemNome + '\'' +
            '}'
        );
    }
}
