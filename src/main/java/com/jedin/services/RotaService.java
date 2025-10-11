package com.jedin.services;

import com.jedin.Repositories.ParagemRepository;
import com.jedin.Repositories.RotaRepository;
import com.jedin.dtos.RotaDTO;
import com.jedin.models.Paragem;
import com.jedin.models.Rota;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@BrowserCallable
@AnonymousAllowed
@Service
public class RotaService {

    @Autowired
    private RotaRepository rotaRepository;

    @Autowired
    private ParagemRepository paragemRepository;

    // Listar todas as rotas
    public List<RotaDTO> listarTodas() {
        List<Rota> rotas = rotaRepository.findAll();
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar rota por IDs das paragens
    public Optional<RotaDTO> buscarPorParagens(Long origemId, Long destinoId) {
        if (origemId == null || destinoId == null) {
            throw new IllegalArgumentException("IDs das paragens não podem ser nulos");
        }

        if (origemId.equals(destinoId)) {
            throw new IllegalArgumentException("Paragem de origem deve ser diferente da paragem de destino");
        }

        Optional<Rota> rota = rotaRepository.findByParagemOrigemIdAndParagemDestinoId(origemId, destinoId);
        return rota.map(this::convertToDTO);
    }

    // Criar nova rota
    public RotaDTO criar(@Valid RotaDTO rotaDTO) {
        if (rotaDTO == null) {
            throw new IllegalArgumentException("Dados da rota não podem ser nulos");
        }

        // Validações
        if (rotaDTO.getParagemOrigemId().equals(rotaDTO.getParagemDestinoId())) {
            throw new IllegalArgumentException("Paragem de origem deve ser diferente da paragem de destino");
        }

        // Verificar se as paragens existem
        Paragem paragemOrigem = paragemRepository.findById(rotaDTO.getParagemOrigemId())
                .orElseThrow(() -> new IllegalArgumentException("Paragem de origem não encontrada"));

        Paragem paragemDestino = paragemRepository.findById(rotaDTO.getParagemDestinoId())
                .orElseThrow(() -> new IllegalArgumentException("Paragem de destino não encontrada"));

        // Verificar se a rota já existe
        if (rotaRepository.existsRotaBetweenParagens(rotaDTO.getParagemOrigemId(), rotaDTO.getParagemDestinoId())) {
            throw new IllegalArgumentException("Já existe uma rota entre estas paragens");
        }

        Rota rota = new Rota(paragemOrigem, paragemDestino, rotaDTO.getPreco(), rotaDTO.getDistancia());
        Rota rotaSalva = rotaRepository.save(rota);
        return convertToDTO(rotaSalva);
    }

    // Atualizar rota
    public RotaDTO atualizar(Long origemId, Long destinoId, @Valid RotaDTO rotaDTO) {
        if (origemId == null || destinoId == null) {
            throw new IllegalArgumentException("IDs das paragens não podem ser nulos");
        }

        if (rotaDTO == null) {
            throw new IllegalArgumentException("Dados da rota não podem ser nulos");
        }

        Rota rotaExistente = rotaRepository.findByParagemOrigemIdAndParagemDestinoId(origemId, destinoId)
                .orElseThrow(() -> new IllegalArgumentException("Rota não encontrada"));

        rotaExistente.setPreco(rotaDTO.getPreco());
        rotaExistente.setDistancia(rotaDTO.getDistancia());

        Rota rotaAtualizada = rotaRepository.save(rotaExistente);
        return convertToDTO(rotaAtualizada);
    }

    // Deletar rota
    public void deletar(Long origemId, Long destinoId) {
        if (origemId == null || destinoId == null) {
            throw new IllegalArgumentException("IDs das paragens não podem ser nulos");
        }

        Rota rota = rotaRepository.findByParagemOrigemIdAndParagemDestinoId(origemId, destinoId)
                .orElseThrow(() -> new IllegalArgumentException("Rota não encontrada"));

        rotaRepository.delete(rota);
    }

    // Buscar rotas por origem
    public List<RotaDTO> buscarPorOrigem(Long origemId) {
        if (origemId == null) {
            throw new IllegalArgumentException("ID da paragem de origem não pode ser nulo");
        }

        List<Rota> rotas = rotaRepository.findByParagemOrigemId(origemId);
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar rotas por destino
    public List<RotaDTO> buscarPorDestino(Long destinoId) {
        if (destinoId == null) {
            throw new IllegalArgumentException("ID da paragem de destino não pode ser nulo");
        }

        List<Rota> rotas = rotaRepository.findByParagemDestinoId(destinoId);
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar todas as rotas de uma paragem
    public List<RotaDTO> buscarTodasRotasParagem(Long paragemId) {
        if (paragemId == null) {
            throw new IllegalArgumentException("ID da paragem não pode ser nulo");
        }

        List<Rota> rotas = rotaRepository.findAllRotasByParagemId(paragemId);
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar rotas por faixa de preço
    public List<RotaDTO> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) {
        if (precoMin == null || precoMax == null) {
            throw new IllegalArgumentException("Preços mínimo e máximo são obrigatórios");
        }

        if (precoMin.compareTo(precoMax) > 0) {
            throw new IllegalArgumentException("Preço mínimo deve ser menor ou igual ao preço máximo");
        }

        List<Rota> rotas = rotaRepository.findByPrecoRange(precoMin, precoMax);
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar rotas por faixa de distância
    public List<RotaDTO> buscarPorFaixaDistancia(BigDecimal distanciaMin, BigDecimal distanciaMax) {
        if (distanciaMin == null || distanciaMax == null) {
            throw new IllegalArgumentException("Distâncias mínima e máxima são obrigatórias");
        }

        if (distanciaMin.compareTo(distanciaMax) > 0) {
            throw new IllegalArgumentException("Distância mínima deve ser menor ou igual à distância máxima");
        }

        List<Rota> rotas = rotaRepository.findByDistanciaRange(distanciaMin, distanciaMax);
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar rotas mais baratas
    public List<RotaDTO> buscarMaisBaratas() {
        List<Rota> rotas = rotaRepository.findAllByOrderByPrecoAsc();
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar rotas mais curtas
    public List<RotaDTO> buscarMaisCurtas() {
        List<Rota> rotas = rotaRepository.findAllByOrderByDistanciaAsc();
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar rotas com melhor custo-benefício
    public List<RotaDTO> buscarMelhorCustoBeneficio() {
        List<Rota> rotas = rotaRepository.findByMelhorCustoBeneficio();
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar rotas por categoria de origem
    public List<RotaDTO> buscarPorCategoriaOrigem(Long categoriaId) {
        if (categoriaId == null) {
            throw new IllegalArgumentException("ID da categoria não pode ser nulo");
        }

        List<Rota> rotas = rotaRepository.findByParagemOrigemCategoriaId(categoriaId);
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar rotas por categoria de destino
    public List<RotaDTO> buscarPorCategoriaDestino(Long categoriaId) {
        if (categoriaId == null) {
            throw new IllegalArgumentException("ID da categoria não pode ser nulo");
        }

        List<Rota> rotas = rotaRepository.findByParagemDestinoCategoriaId(categoriaId);
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar rotas entre categorias
    public List<RotaDTO> buscarEntreCategorias(Long categoriaOrigemId, Long categoriaDestinoId) {
        if (categoriaOrigemId == null || categoriaDestinoId == null) {
            throw new IllegalArgumentException("IDs das categorias não podem ser nulos");
        }

        List<Rota> rotas = rotaRepository.findByCategorias(categoriaOrigemId, categoriaDestinoId);
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar destinos disponíveis a partir de uma origem
    public List<Paragem> buscarDestinosDisponiveis(Long origemId) {
        if (origemId == null) {
            throw new IllegalArgumentException("ID da paragem de origem não pode ser nulo");
        }

        return rotaRepository.findDestinosFromOrigem(origemId);
    }

    // Buscar origens disponíveis para um destino
    public List<Paragem> buscarOrigensDisponiveis(Long destinoId) {
        if (destinoId == null) {
            throw new IllegalArgumentException("ID da paragem de destino não pode ser nulo");
        }

        return rotaRepository.findOrigensToDestino(destinoId);
    }

    // Buscar rotas diretas entre duas paragens
    public List<RotaDTO> buscarRotasDiretas(Long paragem1Id, Long paragem2Id) {
        if (paragem1Id == null || paragem2Id == null) {
            throw new IllegalArgumentException("IDs das paragens não podem ser nulos");
        }

        List<Rota> rotas = rotaRepository.findRotasDirectasBetweenParagens(paragem1Id, paragem2Id);
        return rotas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Verificar se existe rota entre duas paragens
    public boolean verificarRotaExiste(Long origemId, Long destinoId) {
        if (origemId == null || destinoId == null) {
            return false;
        }
        return rotaRepository.existsRotaBetweenParagens(origemId, destinoId);
    }

    // Obter estatísticas de preços
    public RotaPrecoStatistics obterEstatisticasPrecos() {
        List<Object[]> stats = rotaRepository.getPrecoStatistics();
        if (stats.isEmpty()) {
            return new RotaPrecoStatistics(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        Object[] result = stats.get(0);
        BigDecimal precoMin = (BigDecimal) result[0];
        BigDecimal precoMax = (BigDecimal) result[1];
        BigDecimal precoMedio = (BigDecimal) result[2];

        return new RotaPrecoStatistics(
                precoMin != null ? precoMin : BigDecimal.ZERO,
                precoMax != null ? precoMax : BigDecimal.ZERO,
                precoMedio != null ? precoMedio : BigDecimal.ZERO
        );
    }

    // Obter estatísticas de distâncias
    public RotaDistanciaStatistics obterEstatisticasDistancias() {
        List<Object[]> stats = rotaRepository.getDistanciaStatistics();
        if (stats.isEmpty()) {
            return new RotaDistanciaStatistics(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        Object[] result = stats.get(0);
        BigDecimal distanciaMin = (BigDecimal) result[0];
        BigDecimal distanciaMax = (BigDecimal) result[1];
        BigDecimal distanciaMedia = (BigDecimal) result[2];

        return new RotaDistanciaStatistics(
                distanciaMin != null ? distanciaMin : BigDecimal.ZERO,
                distanciaMax != null ? distanciaMax : BigDecimal.ZERO,
                distanciaMedia != null ? distanciaMedia : BigDecimal.ZERO
        );
    }

    // Contar total de rotas
    public long contarTotal() {
        return rotaRepository.count();
    }

    // Conversão para DTO
    private RotaDTO convertToDTO(Rota rota) {
        if (rota == null) {
            return null;
        }

        RotaDTO dto = new RotaDTO();
        dto.setParagemOrigemId(rota.getParagemOrigem().getId());
        dto.setParagemDestinoId(rota.getParagemDestino().getId());
        dto.setPreco(rota.getPreco());
        dto.setDistancia(rota.getDistancia());

        // Informações adicionais das paragens
        dto.setParagemOrigemLatitude(rota.getParagemOrigem().getLatitude());
        dto.setParagemOrigemLongitude(rota.getParagemOrigem().getLongitude());
        dto.setParagemDestinoLatitude(rota.getParagemDestino().getLatitude());
        dto.setParagemDestinoLongitude(rota.getParagemDestino().getLongitude());
        dto.setParagemOrigemCategoria(rota.getParagemOrigem().getCategoriaParagem().getNome());
        dto.setParagemDestinoCategoria(rota.getParagemDestino().getCategoriaParagem().getNome());

        return dto;
    }

    // Classes auxiliares para estatísticas
    public static class RotaPrecoStatistics {
        private final BigDecimal precoMinimo;
        private final BigDecimal precoMaximo;
        private final BigDecimal precoMedio;

        public RotaPrecoStatistics(BigDecimal precoMinimo, BigDecimal precoMaximo, BigDecimal precoMedio) {
            this.precoMinimo = precoMinimo;
            this.precoMaximo = precoMaximo;
            this.precoMedio = precoMedio;
        }

        public BigDecimal getPrecoMinimo() { return precoMinimo; }
        public BigDecimal getPrecoMaximo() { return precoMaximo; }
        public BigDecimal getPrecoMedio() { return precoMedio; }
    }

    public static class RotaDistanciaStatistics {
        private final BigDecimal distanciaMinima;
        private final BigDecimal distanciaMaxima;
        private final BigDecimal distanciaMedia;

        public RotaDistanciaStatistics(BigDecimal distanciaMinima, BigDecimal distanciaMaxima, BigDecimal distanciaMedia) {
            this.distanciaMinima = distanciaMinima;
            this.distanciaMaxima = distanciaMaxima;
            this.distanciaMedia = distanciaMedia;
        }

        public BigDecimal getDistanciaMinima() { return distanciaMinima; }
        public BigDecimal getDistanciaMaxima() { return distanciaMaxima; }
        public BigDecimal getDistanciaMedia() { return distanciaMedia; }
    }
}
