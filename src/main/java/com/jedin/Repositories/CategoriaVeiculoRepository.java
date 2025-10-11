package com.jedin.Repositories;

import com.jedin.models.CategoriaVeiculo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaVeiculoRepository
    extends JpaRepository<CategoriaVeiculo, Long> {
    // Buscar por nome (case insensitive)
    Optional<CategoriaVeiculo> findByNomeIgnoreCase(String nome);

    // Buscar categorias que contêm o texto no nome
    List<CategoriaVeiculo> findByNomeContainingIgnoreCase(String nome);

    // Verificar se existe categoria com o mesmo nome
    boolean existsByNomeIgnoreCase(String nome);

    // Buscar categorias ordenadas por nome
    List<CategoriaVeiculo> findAllByOrderByNomeAsc();

    // Buscar categorias por parte do nome
    @Query(
        "SELECT cv FROM CategoriaVeiculo cv WHERE " +
            "LOWER(cv.nome) LIKE LOWER(CONCAT('%', :texto, '%'))"
    )
    List<CategoriaVeiculo> findByNomeContaining(@Param("texto") String texto);

    // Contar total de categorias
    @Query("SELECT COUNT(cv) FROM CategoriaVeiculo cv")
    long countTotal();
}
