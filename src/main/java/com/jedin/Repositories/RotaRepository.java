package com.jedin.Repositories;

import com.jedin.models.Paragem;
import com.jedin.models.Rota;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RotaRepository extends JpaRepository<Rota, Rota.RotaId> {
    // Buscar rotas por paragem de origem
    List<Rota> findByParagemOrigem(Paragem paragemOrigem);

    // Buscar rotas por paragem de destino
    List<Rota> findByParagemDestino(Paragem paragemDestino);

    // Buscar rotas por ID da paragem de origem
    @Query("SELECT r FROM Rota r WHERE r.paragemOrigem.id = :paragemOrigemId")
    List<Rota> findByParagemOrigemId(
        @Param("paragemOrigemId") Long paragemOrigemId
    );

    // Buscar rotas por ID da paragem de destino
    @Query("SELECT r FROM Rota r WHERE r.paragemDestino.id = :paragemDestinoId")
    List<Rota> findByParagemDestinoId(
        @Param("paragemDestinoId") Long paragemDestinoId
    );

    // Buscar rota específica entre duas paragens
    @Query(
        "SELECT r FROM Rota r WHERE r.paragemOrigem.id = :origemId AND r.paragemDestino.id = :destinoId"
    )
    Optional<Rota> findByParagemOrigemIdAndParagemDestinoId(
        @Param("origemId") Long origemId,
        @Param("destinoId") Long destinoId
    );

    // Buscar rotas com preço menor ou igual ao especificado
    List<Rota> findByPrecoLessThanEqual(BigDecimal precoMaximo);

    // Buscar rotas com distância menor ou igual à especificada
    List<Rota> findByDistanciaLessThanEqual(BigDecimal distanciaMaxima);

    // Buscar rotas por faixa de preço
    @Query("SELECT r FROM Rota r WHERE r.preco BETWEEN :precoMin AND :precoMax")
    List<Rota> findByPrecoRange(
        @Param("precoMin") BigDecimal precoMin,
        @Param("precoMax") BigDecimal precoMax
    );

    // Buscar rotas por faixa de distância
    @Query(
        "SELECT r FROM Rota r WHERE r.distancia BETWEEN :distMin AND :distMax"
    )
    List<Rota> findByDistanciaRange(
        @Param("distMin") BigDecimal distMin,
        @Param("distMax") BigDecimal distMax
    );

    // Buscar rotas mais baratas (ordenadas por preço crescente)
    List<Rota> findAllByOrderByPrecoAsc();

    // Buscar rotas mais curtas (ordenadas por distância crescente)
    List<Rota> findAllByOrderByDistanciaAsc();

    // Buscar rotas com melhor custo-benefício (menor preço por km)
    @Query("SELECT r FROM Rota r ORDER BY (r.preco / r.distancia) ASC")
    List<Rota> findByMelhorCustoBeneficio();

    // Buscar rotas que partem de uma categoria específica de paragem
    @Query(
        "SELECT r FROM Rota r WHERE r.paragemOrigem.categoriaParagem.id = :categoriaId"
    )
    List<Rota> findByParagemOrigemCategoriaId(
        @Param("categoriaId") Long categoriaId
    );

    // Buscar rotas que chegam a uma categoria específica de paragem
    @Query(
        "SELECT r FROM Rota r WHERE r.paragemDestino.categoriaParagem.id = :categoriaId"
    )
    List<Rota> findByParagemDestinoCategoriaId(
        @Param("categoriaId") Long categoriaId
    );

    // Buscar rotas entre categorias específicas
    @Query(
        "SELECT r FROM Rota r WHERE r.paragemOrigem.categoriaParagem.id = :categoriaOrigemId AND r.paragemDestino.categoriaParagem.id = :categoriaDestinoId"
    )
    List<Rota> findByCategorias(
        @Param("categoriaOrigemId") Long categoriaOrigemId,
        @Param("categoriaDestinoId") Long categoriaDestinoId
    );

    // Buscar todas as rotas de uma paragem (origem e destino)
    @Query(
        "SELECT r FROM Rota r WHERE r.paragemOrigem.id = :paragemId OR r.paragemDestino.id = :paragemId"
    )
    List<Rota> findAllRotasByParagemId(@Param("paragemId") Long paragemId);

    // Contar rotas por paragem de origem
    @Query(
        "SELECT r.paragemOrigem.id, COUNT(r) FROM Rota r GROUP BY r.paragemOrigem.id"
    )
    List<Object[]> countRotasByParagemOrigem();

    // Contar rotas por paragem de destino
    @Query(
        "SELECT r.paragemDestino.id, COUNT(r) FROM Rota r GROUP BY r.paragemDestino.id"
    )
    List<Object[]> countRotasByParagemDestino();

    // Estatísticas de preços
    @Query("SELECT MIN(r.preco), MAX(r.preco), AVG(r.preco) FROM Rota r")
    List<Object[]> getPrecoStatistics();

    // Estatísticas de distâncias
    @Query(
        "SELECT MIN(r.distancia), MAX(r.distancia), AVG(r.distancia) FROM Rota r"
    )
    List<Object[]> getDistanciaStatistics();

    // Buscar rotas diretas entre duas paragens específicas
    @Query(
        "SELECT r FROM Rota r WHERE (r.paragemOrigem.id = :paragem1Id AND r.paragemDestino.id = :paragem2Id) OR (r.paragemOrigem.id = :paragem2Id AND r.paragemDestino.id = :paragem1Id)"
    )
    List<Rota> findRotasDirectasBetweenParagens(
        @Param("paragem1Id") Long paragem1Id,
        @Param("paragem2Id") Long paragem2Id
    );

    // Buscar rotas com conexões (rotas que compartilham paragens)
    @Query(
        "SELECT r1, r2 FROM Rota r1, Rota r2 WHERE r1.paragemDestino.id = r2.paragemOrigem.id AND r1.id != r2.id"
    )
    List<Object[]> findRotasWithConnections();

    // Verificar se existe rota entre duas paragens
    @Query(
        "SELECT COUNT(r) > 0 FROM Rota r WHERE r.paragemOrigem.id = :origemId AND r.paragemDestino.id = :destinoId"
    )
    boolean existsRotaBetweenParagens(
        @Param("origemId") Long origemId,
        @Param("destinoId") Long destinoId
    );

    // Buscar paragens destino disponíveis a partir de uma origem
    @Query(
        "SELECT DISTINCT r.paragemDestino FROM Rota r WHERE r.paragemOrigem.id = :paragemOrigemId"
    )
    List<Paragem> findDestinosFromOrigem(
        @Param("paragemOrigemId") Long paragemOrigemId
    );

    // Buscar paragens origem que chegam a um destino
    @Query(
        "SELECT DISTINCT r.paragemOrigem FROM Rota r WHERE r.paragemDestino.id = :paragemDestinoId"
    )
    List<Paragem> findOrigensToDestino(
        @Param("paragemDestinoId") Long paragemDestinoId
    );
}
