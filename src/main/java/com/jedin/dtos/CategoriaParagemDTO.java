package com.jedin.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaParagemDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    private String descricao;

    // Constructors
    public CategoriaParagemDTO() {}

    public CategoriaParagemDTO(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public CategoriaParagemDTO(Long id, String nome, String descricao) {
        this.id = id;
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

    @Override
    public String toString() {
        return (
            "CategoriaParagemDTO{" +
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
}
