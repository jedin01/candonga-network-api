package com.jedin.Repositories;

import com.jedin.models.CategoriaParagem;
import com.jedin.models.Paragem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParagemRepository extends JpaRepository<Paragem, Long> {
    // Buscar paragens por categoria
    List<Paragem> findByCategoriaParagem(CategoriaParagem categoriaParagem);

    // Buscar paragens por ID da categoria
    List<Paragem> findByCategoriaParagemId(Long categoriaParagemId);

    // Buscar paragens por nome da categoria
    @Query(
        "SELECT p FROM Paragem p WHERE p.categoriaParagem.nome = :nomeCategoria"
    )
    List<Paragem> findByCategoriaParagemNome(
        @Param("nomeCategoria") String nomeCategoria
    );

    // Buscar paragens em uma área específica (bounding box)
    @Query(
        "SELECT p FROM Paragem p WHERE " +
            "p.latitude BETWEEN :latMin AND :latMax AND " +
            "p.longitude BETWEEN :longMin AND :longMax"
    )
    List<Paragem> findParagensInArea(
        @Param("latMin") BigDecimal latMin,
        @Param("latMax") BigDecimal latMax,
        @Param("longMin") BigDecimal longMin,
        @Param("longMax") BigDecimal longMax
    );

    // Buscar paragens próximas de uma coordenada (aproximação simples)
    @Query(
        "SELECT p FROM Paragem p WHERE " +
            "ABS(p.latitude - :latitude) <= :deltaLat AND " +
            "ABS(p.longitude - :longitude) <= :deltaLong"
    )
    List<Paragem> findParagensNearby(
        @Param("latitude") BigDecimal latitude,
        @Param("longitude") BigDecimal longitude,
        @Param("deltaLat") BigDecimal deltaLat,
        @Param("deltaLong") BigDecimal deltaLong
    );

    // Verificar se existe paragem na mesma localização
    @Query(
        "SELECT p FROM Paragem p WHERE " +
            "p.latitude = :latitude AND p.longitude = :longitude"
    )
    Optional<Paragem> findByLatitudeAndLongitude(
        @Param("latitude") BigDecimal latitude,
        @Param("longitude") BigDecimal longitude
    );

    // Buscar paragens ordenadas por categoria
    @Query(
        "SELECT p FROM Paragem p ORDER BY p.categoriaParagem.nome ASC, p.id ASC"
    )
    List<Paragem> findAllOrderByCategoria();

    // Contar paragens por categoria
    @Query(
        "SELECT p.categoriaParagem.nome, COUNT(p) FROM Paragem p GROUP BY p.categoriaParagem.nome"
    )
    List<Object[]> countParagensByCategoria();

    // Buscar paragens que são origem de rotas
    @Query(
        "SELECT DISTINCT p FROM Paragem p WHERE p.id IN " +
            "(SELECT r.paragemOrigem.id FROM Rota r)"
    )
    List<Paragem> findParagensWithRotasOrigem();

    // Buscar paragens que são destino de rotas
    @Query(
        "SELECT DISTINCT p FROM Paragem p WHERE p.id IN " +
            "(SELECT r.paragemDestino.id FROM Rota r)"
    )
    List<Paragem> findParagensWithRotasDestino();

    // Buscar paragens conectadas (que têm rotas de origem ou destino)
    @Query(
        "SELECT DISTINCT p FROM Paragem p WHERE p.id IN " +
            "(SELECT r.paragemOrigem.id FROM Rota r) OR p.id IN " +
            "(SELECT r.paragemDestino.id FROM Rota r)"
    )
    List<Paragem> findParagensConnected();

    // Buscar paragens isoladas (sem rotas)
    @Query(
        "SELECT p FROM Paragem p WHERE p.id NOT IN " +
            "(SELECT r.paragemOrigem.id FROM Rota r) AND p.id NOT IN " +
            "(SELECT r.paragemDestino.id FROM Rota r)"
    )
    List<Paragem> findParagensIsolated();

    // Buscar paragens por categoria com contagem de rotas
    @Query(
        "SELECT p, " +
            "(SELECT COUNT(ro) FROM Rota ro WHERE ro.paragemOrigem.id = p.id) as rotasOrigem, " +
            "(SELECT COUNT(rd) FROM Rota rd WHERE rd.paragemDestino.id = p.id) as rotasDestino " +
            "FROM Paragem p WHERE p.categoriaParagem.id = :categoriaParagemId"
    )
    List<Object[]> findParagensWithRotaCountByCategoria(
        @Param("categoriaParagemId") Long categoriaParagemId
    );
}
