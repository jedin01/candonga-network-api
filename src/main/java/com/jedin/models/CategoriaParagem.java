package com.jedin.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categoria_paragem")
public class CategoriaParagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @OneToMany(
        mappedBy = "categoriaParagem",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Paragem> paragens;

    // Constructors
    public CategoriaParagem() {}

    public CategoriaParagem(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Paragem> getParagens() {
        return paragens;
    }

    public void setParagens(List<Paragem> paragens) {
        this.paragens = paragens;
    }

    @Override
    public String toString() {
        return (
            "CategoriaParagem{" +
            "id=" +
            id +
            ", nome='" +
            nome +
            '\'' +
            ", descricao='" +
            descricao +
            '\'' +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoriaParagem that = (CategoriaParagem) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
