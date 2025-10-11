package com.jedin.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "paragem")
public class Paragem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_paragem_id", nullable = false)
    private CategoriaParagem categoriaParagem;

    @OneToMany(
        mappedBy = "paragemOrigem",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Rota> rotasOrigem;

    @OneToMany(
        mappedBy = "paragemDestino",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Rota> rotasDestino;

    // Constructors
    public Paragem() {}

    public Paragem(BigDecimal latitude, BigDecimal longitude, CategoriaParagem categoriaParagem) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.categoriaParagem = categoriaParagem;
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

    public CategoriaParagem getCategoriaParagem() {
        return categoriaParagem;
    }

    public void setCategoriaParagem(CategoriaParagem categoriaParagem) {
        this.categoriaParagem = categoriaParagem;
    }

    public List<Rota> getRotasOrigem() {
        return rotasOrigem;
    }

    public void setRotasOrigem(List<Rota> rotasOrigem) {
        this.rotasOrigem = rotasOrigem;
    }

    public List<Rota> getRotasDestino() {
        return rotasDestino;
    }

    public void setRotasDestino(List<Rota> rotasDestino) {
        this.rotasDestino = rotasDestino;
    }

    @Override
    public String toString() {
        return (
            "Paragem{" +
            "id=" + id +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", categoriaParagem=" + (categoriaParagem != null ? categoriaParagem.getId() : null) +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Paragem paragem = (Paragem) o;

        return id != null ? id.equals(paragem.id) : paragem.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
