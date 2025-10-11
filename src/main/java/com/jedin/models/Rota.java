package com.jedin.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rota")
public class Rota {

    @EmbeddedId
    private RotaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("paragemOrigemId")
    @JoinColumn(name = "paragem_origem_id")
    private Paragem paragemOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("paragemDestinoId")
    @JoinColumn(name = "paragem_destino_id")
    private Paragem paragemDestino;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal distancia;

    // Constructors
    public Rota() {}

    public Rota(
        Paragem paragemOrigem,
        Paragem paragemDestino,
        BigDecimal preco,
        BigDecimal distancia
    ) {
        this.id = new RotaId(paragemOrigem.getId(), paragemDestino.getId());
        this.paragemOrigem = paragemOrigem;
        this.paragemDestino = paragemDestino;
        this.preco = preco;
        this.distancia = distancia;
    }

    // Getters and Setters
    public RotaId getId() {
        return id;
    }

    public void setId(RotaId id) {
        this.id = id;
    }

    public Paragem getParagemOrigem() {
        return paragemOrigem;
    }

    public void setParagemOrigem(Paragem paragemOrigem) {
        this.paragemOrigem = paragemOrigem;
        if (this.id == null) {
            this.id = new RotaId();
        }
        this.id.setParagemOrigemId(paragemOrigem.getId());
    }

    public Paragem getParagemDestino() {
        return paragemDestino;
    }

    public void setParagemDestino(Paragem paragemDestino) {
        this.paragemDestino = paragemDestino;
        if (this.id == null) {
            this.id = new RotaId();
        }
        this.id.setParagemDestinoId(paragemDestino.getId());
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

    @Override
    public String toString() {
        return (
            "Rota{" +
            "id=" +
            id +
            ", preco=" +
            preco +
            ", distancia=" +
            distancia +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rota rota = (Rota) o;

        return id != null ? id.equals(rota.id) : rota.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    // Embedded ID class
    @Embeddable
    public static class RotaId implements java.io.Serializable {

        @Column(name = "paragem_origem_id")
        private Long paragemOrigemId;

        @Column(name = "paragem_destino_id")
        private Long paragemDestinoId;

        public RotaId() {}

        public RotaId(Long paragemOrigemId, Long paragemDestinoId) {
            this.paragemOrigemId = paragemOrigemId;
            this.paragemDestinoId = paragemDestinoId;
        }

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RotaId rotaId = (RotaId) o;

            if (
                paragemOrigemId != null
                    ? !paragemOrigemId.equals(rotaId.paragemOrigemId)
                    : rotaId.paragemOrigemId != null
            ) return false;
            return paragemDestinoId != null
                ? paragemDestinoId.equals(rotaId.paragemDestinoId)
                : rotaId.paragemDestinoId == null;
        }

        @Override
        public int hashCode() {
            int result = paragemOrigemId != null
                ? paragemOrigemId.hashCode()
                : 0;
            result =
                31 * result +
                (paragemDestinoId != null ? paragemDestinoId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return (
                "RotaId{" +
                "paragemOrigemId=" +
                paragemOrigemId +
                ", paragemDestinoId=" +
                paragemDestinoId +
                '}'
            );
        }
    }
}
