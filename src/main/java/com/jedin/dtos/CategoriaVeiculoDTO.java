package com.jedin.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaVeiculoDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    // Constructors
    public CategoriaVeiculoDTO() {}

    public CategoriaVeiculoDTO(String nome) {
        this.nome = nome;
    }

    public CategoriaVeiculoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
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

    @Override
    public String toString() {
        return (
            "CategoriaVeiculoDTO{" + "id=" + id + ", nome='" + nome + '\'' + '}'
        );
    }
}
