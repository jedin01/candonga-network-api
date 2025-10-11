package com.jedin.services;

import com.jedin.Repositories.CategoriaParagemRepository;
import com.jedin.Repositories.ParagemRepository;
import com.jedin.dtos.ParagemDTO;
import com.jedin.models.CategoriaParagem;
import com.jedin.models.Paragem;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@BrowserCallable
@AnonymousAllowed
@Service
public class ParagemService {

    @Autowired
    private ParagemRepository paragemRepository;

    @Autowired
    private CategoriaParagemRepository categoriaParagemRepository;

    // Listar todas as paragens
    public List<ParagemDTO> listarTodas() {
        List<Paragem> paragens = paragemRepository.findAllOrderByCategoria();
        return paragens
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Buscar paragem por ID
    public Optional<ParagemDTO> buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        Optional<Paragem> paragem = paragemRepository.findById(id);
        return paragem.map(this::convertToDTO);
    }

    // Criar nova paragem
    public ParagemDTO criar(@Valid ParagemDTO paragemDTO) {
        if (paragemDTO == null) {
            throw new IllegalArgumentException(
                "Dados da paragem não podem ser nulos"
            );
        }

        // Verificar se as coordenadas já existem
        if (
            paragemRepository
                .findByLatitudeAndLongitude(
                    paragemDTO.getLatitude(),
                    paragemDTO.getLongitude()
                )
                .isPresent()
        ) {
            throw new IllegalArgumentException(
                "Já existe uma paragem com estas coordenadas"
            );
        }

        // Verificar se a categoria existe
        CategoriaParagem categoria = categoriaParagemRepository
            .findById(paragemDTO.getCategoriaParagemId())
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Categoria de paragem não encontrada"
                )
            );

        Paragem paragem = convertToEntity(paragemDTO, categoria);
        Paragem paragemSalva = paragemRepository.save(paragem);
        return convertToDTO(paragemSalva);
    }

    // Atualizar paragem
    public ParagemDTO atualizar(Long id, @Valid ParagemDTO paragemDTO) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        if (paragemDTO == null) {
            throw new IllegalArgumentException(
                "Dados da paragem não podem ser nulos"
            );
        }

        Paragem paragemExistente = paragemRepository
            .findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Paragem não encontrada com ID: " + id
                )
            );

        // Verificar se as novas coordenadas já existem em outra paragem
        if (
            !paragemExistente.getLatitude().equals(paragemDTO.getLatitude()) ||
            !paragemExistente.getLongitude().equals(paragemDTO.getLongitude())
        ) {
            if (
                paragemRepository
                    .findByLatitudeAndLongitude(
                        paragemDTO.getLatitude(),
                        paragemDTO.getLongitude()
                    )
                    .isPresent()
            ) {
                throw new IllegalArgumentException(
                    "Já existe uma paragem com estas coordenadas"
                );
            }
        }

        // Verificar se a categoria existe
        CategoriaParagem categoria = categoriaParagemRepository
            .findById(paragemDTO.getCategoriaParagemId())
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Categoria de paragem não encontrada"
                )
            );

        paragemExistente.setLatitude(paragemDTO.getLatitude());
        paragemExistente.setLongitude(paragemDTO.getLongitude());
        paragemExistente.setCategoriaParagem(categoria);

        Paragem paragemAtualizada = paragemRepository.save(paragemExistente);
        return convertToDTO(paragemAtualizada);
    }

    // Deletar paragem
    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        if (!paragemRepository.existsById(id)) {
            throw new IllegalArgumentException(
                "Paragem não encontrada com ID: " + id
            );
        }

        try {
            paragemRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(
                "Não é possível deletar esta paragem pois existem rotas associadas a ela"
            );
        }
    }

    // Buscar paragens por categoria
    public List<ParagemDTO> buscarPorCategoria(Long categoriaId) {
        if (categoriaId == null) {
            throw new IllegalArgumentException(
                "ID da categoria não pode ser nulo"
            );
        }

        List<Paragem> paragens = paragemRepository.findByCategoriaParagemId(
            categoriaId
        );
        return paragens
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Buscar paragens por nome da categoria
    public List<ParagemDTO> buscarPorNomeCategoria(String nomeCategoria) {
        if (nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
            return List.of();
        }

        List<Paragem> paragens = paragemRepository.findByCategoriaParagemNome(
            nomeCategoria.trim()
        );
        return paragens
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Buscar paragens em uma área específica
    public List<ParagemDTO> buscarPorArea(
        BigDecimal latMin,
        BigDecimal latMax,
        BigDecimal longMin,
        BigDecimal longMax
    ) {
        if (
            latMin == null ||
            latMax == null ||
            longMin == null ||
            longMax == null
        ) {
            throw new IllegalArgumentException(
                "Todas as coordenadas da área devem ser fornecidas"
            );
        }

        List<Paragem> paragens = paragemRepository.findParagensInArea(
            latMin,
            latMax,
            longMin,
            longMax
        );
        return paragens
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Buscar paragens próximas
    public List<ParagemDTO> buscarProximas(
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal raio
    ) {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException(
                "Latitude e longitude são obrigatórias"
            );
        }

        if (raio == null) {
            raio = new BigDecimal("0.01"); // raio padrão
        }

        BigDecimal latMin = latitude.subtract(raio);
        BigDecimal latMax = latitude.add(raio);
        BigDecimal longMin = longitude.subtract(raio);
        BigDecimal longMax = longitude.add(raio);

        return buscarPorArea(latMin, latMax, longMin, longMax);
    }

    // Buscar paragens conectadas (que têm rotas)
    public List<ParagemDTO> buscarConectadas() {
        List<Paragem> paragens = paragemRepository.findParagensConnected();
        return paragens
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Buscar paragens isoladas (sem rotas)
    public List<ParagemDTO> buscarIsoladas() {
        List<Paragem> paragens = paragemRepository.findParagensIsolated();
        return paragens
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Buscar paragens que são origem de rotas
    public List<ParagemDTO> buscarOrigensDeRotas() {
        List<Paragem> paragens =
            paragemRepository.findParagensWithRotasOrigem();
        return paragens
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Buscar paragens que são destino de rotas
    public List<ParagemDTO> buscarDestinosDeRotas() {
        List<Paragem> paragens =
            paragemRepository.findParagensWithRotasDestino();
        return paragens
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Verificar se coordenadas já existem
    public boolean verificarCoordenadasExistem(
        BigDecimal latitude,
        BigDecimal longitude
    ) {
        if (latitude == null || longitude == null) {
            return false;
        }
        return paragemRepository
            .findByLatitudeAndLongitude(latitude, longitude)
            .isPresent();
    }

    // Contar total de paragens
    public long contarTotal() {
        return paragemRepository.count();
    }

    // Obter estatísticas de paragens por categoria
    public List<ParagemStatistics> obterEstatisticas() {
        List<Object[]> stats = paragemRepository.countParagensByCategoria();
        return stats
            .stream()
            .map(row ->
                new ParagemStatistics(
                    (String) row[0], // nome da categoria
                    ((Number) row[1]).longValue() // contagem de paragens
                )
            )
            .collect(Collectors.toList());
    }

    // Buscar paragens por categoria com contagem de rotas
    public List<ParagemComRotas> buscarPorCategoriaComRotas(Long categoriaId) {
        if (categoriaId == null) {
            throw new IllegalArgumentException(
                "ID da categoria não pode ser nulo"
            );
        }

        List<Object[]> results =
            paragemRepository.findParagensWithRotaCountByCategoria(categoriaId);
        return results
            .stream()
            .map(row ->
                new ParagemComRotas(
                    convertToDTO((Paragem) row[0]),
                    ((Number) row[1]).longValue(), // rotas como origem
                    ((Number) row[2]).longValue() // rotas como destino
                )
            )
            .collect(Collectors.toList());
    }

    // Conversão para DTO
    private ParagemDTO convertToDTO(Paragem paragem) {
        if (paragem == null) {
            return null;
        }

        ParagemDTO dto = new ParagemDTO();
        dto.setId(paragem.getId());
        dto.setLatitude(paragem.getLatitude());
        dto.setLongitude(paragem.getLongitude());
        dto.setCategoriaParagemId(paragem.getCategoriaParagem().getId());
        dto.setCategoriaParagemNome(paragem.getCategoriaParagem().getNome());
        return dto;
    }

    // Conversão para Entity
    private Paragem convertToEntity(
        ParagemDTO dto,
        CategoriaParagem categoria
    ) {
        if (dto == null) {
            return null;
        }

        Paragem paragem = new Paragem();
        paragem.setId(dto.getId());
        paragem.setLatitude(dto.getLatitude());
        paragem.setLongitude(dto.getLongitude());
        paragem.setCategoriaParagem(categoria);
        return paragem;
    }

    // Classes auxiliares
    public static class ParagemStatistics {

        private final String nomeCategoria;
        private final long quantidadeParagens;

        public ParagemStatistics(
            String nomeCategoria,
            long quantidadeParagens
        ) {
            this.nomeCategoria = nomeCategoria;
            this.quantidadeParagens = quantidadeParagens;
        }

        public String getNomeCategoria() {
            return nomeCategoria;
        }

        public long getQuantidadeParagens() {
            return quantidadeParagens;
        }
    }

    public static class ParagemComRotas {

        private final ParagemDTO paragem;
        private final long rotasComoOrigem;
        private final long rotasComoDestino;

        public ParagemComRotas(
            ParagemDTO paragem,
            long rotasComoOrigem,
            long rotasComoDestino
        ) {
            this.paragem = paragem;
            this.rotasComoOrigem = rotasComoOrigem;
            this.rotasComoDestino = rotasComoDestino;
        }

        public ParagemDTO getParagem() {
            return paragem;
        }

        public long getRotasComoOrigem() {
            return rotasComoOrigem;
        }

        public long getRotasComoDestino() {
            return rotasComoDestino;
        }

        public long getTotalRotas() {
            return rotasComoOrigem + rotasComoDestino;
        }
    }
}
