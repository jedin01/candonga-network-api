package com.jedin.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class RotaDTO {

    @NotNull(message = "Paragem de origem é obrigatória")
    private Long paragemOrigemId;

    @NotNull(message = "Paragem de destino é obrigatória")
    private Long paragemDestinoId;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "Preço deve ser maior que zero"
    )
    private BigDecimal preco;

    @NotNull(message = "Distância é obrigatória")
    @Positive(message = "Distância deve ser positiva")
    private BigDecimal distancia;

    // Campos adicionais para exibição
    private BigDecimal paragemOrigemLatitude;
    private BigDecimal paragemOrigemLongitude;
    private BigDecimal paragemDestinoLatitude;
    private BigDecimal paragemDestinoLongitude;
    private String paragemOrigemCategoria;
    private String paragemDestinoCategoria;

    // Constructors
    public RotaDTO() {}

    public RotaDTO(
        Long paragemOrigemId,
        Long paragemDestinoId,
        BigDecimal preco,
        BigDecimal distancia
    ) {
        this.paragemOrigemId = paragemOrigemId;
        this.paragemDestinoId = paragemDestinoId;
        this.preco = preco;
        this.distancia = distancia;
    }

    public RotaDTO(
        Long paragemOrigemId,
        Long paragemDestinoId,
        BigDecimal preco,
        BigDecimal distancia,
        BigDecimal paragemOrigemLatitude,
        BigDecimal paragemOrigemLongitude,
        BigDecimal paragemDestinoLatitude,
        BigDecimal paragemDestinoLongitude,
        String paragemOrigemCategoria,
        String paragemDestinoCategoria
    ) {
        this.paragemOrigemId = paragemOrigemId;
        this.paragemDestinoId = paragemDestinoId;
        this.preco = preco;
        this.distancia = distancia;
        this.paragemOrigemLatitude = paragemOrigemLatitude;
        this.paragemOrigemLongitude = paragemOrigemLongitude;
        this.paragemDestinoLatitude = paragemDestinoLatitude;
        this.paragemDestinoLongitude = paragemDestinoLongitude;
        this.paragemOrigemCategoria = paragemOrigemCategoria;
        this.paragemDestinoCategoria = paragemDestinoCategoria;
    }

    // Getters and Setters
    public Long getParagemOrigemId() {
        return paragemOrigemId;
    }

    public void setParagemOrigemId(Long paragemOrigemId) {
        this.paragemOrigemId = paragemOrigemId;
    }

    public Long getParagemDestinoId() {
        return paragemDestinoId;
    }

    public void setParagemDestinoId(Long paragemDestinoId) {
        this.paragemDestinoId = paragemDestinoId;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getDistancia() {
        return distancia;
    }

    public void setDistancia(BigDecimal distancia) {
        this.distancia = distancia;
    }

    public BigDecimal getParagemOrigemLatitude() {
        return paragemOrigemLatitude;
    }

    public void setParagemOrigemLatitude(BigDecimal paragemOrigemLatitude) {
        this.paragemOrigemLatitude = paragemOrigemLatitude;
    }

    public BigDecimal getParagemOrigemLongitude() {
        return paragemOrigemLongitude;
    }

    public void setParagemOrigemLongitude(BigDecimal paragemOrigemLongitude) {
        this.paragemOrigemLongitude = paragemOrigemLongitude;
    }

    public BigDecimal getParagemDestinoLatitude() {
        return paragemDestinoLatitude;
    }

    public void setParagemDestinoLatitude(BigDecimal paragemDestinoLatitude) {
        this.paragemDestinoLatitude = paragemDestinoLatitude;
    }

    public BigDecimal getParagemDestinoLongitude() {
        return paragemDestinoLongitude;
    }

    public void setParagemDestinoLongitude(BigDecimal paragemDestinoLongitude) {
        this.paragemDestinoLongitude = paragemDestinoLongitude;
    }

    public String getParagemOrigemCategoria() {
        return paragemOrigemCategoria;
    }

    public void setParagemOrigemCategoria(String paragemOrigemCategoria) {
        this.paragemOrigemCategoria = paragemOrigemCategoria;
    }

    public String getParagemDestinoCategoria() {
        return paragemDestinoCategoria;
    }

    public void setParagemDestinoCategoria(String paragemDestinoCategoria) {
        this.paragemDestinoCategoria = paragemDestinoCategoria;
    }

    @Override
    public String toString() {
        return (
            "RotaDTO{" +
            "paragemOrigemId=" +
            paragemOrigemId +
            ", paragemDestinoId=" +
            paragemDestinoId +
            ", preco=" +
            preco +
            ", distancia=" +
            distancia +
            ", paragemOrigemLatitude=" +
            paragemOrigemLatitude +
            ", paragemOrigemLongitude=" +
            paragemOrigemLongitude +
            ", paragemDestinoLatitude=" +
            paragemDestinoLatitude +
            ", paragemDestinoLongitude=" +
            paragemDestinoLongitude +
            ", paragemOrigemCategoria='" +
            paragemOrigemCategoria +
            '\'' +
            ", paragemDestinoCategoria='" +
            paragemDestinoCategoria +
            '\'' +
            '}'
        );
    }
}
