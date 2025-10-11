package com.jedin.Repositories;

import com.jedin.models.CategoriaParagem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaParagemRepository
    extends JpaRepository<CategoriaParagem, Long> {
    // Buscar por nome (case insensitive)
    Optional<CategoriaParagem> findByNomeIgnoreCase(String nome);

    // Buscar categorias que contêm o texto no nome
    List<CategoriaParagem> findByNomeContainingIgnoreCase(String nome);

    // Buscar categorias que contêm o texto na descrição
    List<CategoriaParagem> findByDescricaoContainingIgnoreCase(
        String descricao
    );

    // Buscar categorias por nome ou descrição
    @Query(
        "SELECT cp FROM CategoriaParagem cp WHERE " +
            "LOWER(cp.nome) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(cp.descricao) LIKE LOWER(CONCAT('%', :texto, '%'))"
    )
    List<CategoriaParagem> findByNomeOrDescricaoContaining(
        @Param("texto") String texto
    );

    // Verificar se existe categoria com o mesmo nome
    boolean existsByNomeIgnoreCase(String nome);

    // Buscar categorias ordenadas por nome
    List<CategoriaParagem> findAllByOrderByNomeAsc();

    // Contar paragens por categoria
    @Query(
        "SELECT cp, COUNT(p) FROM CategoriaParagem cp LEFT JOIN cp.paragens p GROUP BY cp"
    )
    List<Object[]> countParagensByCategoria();
}
