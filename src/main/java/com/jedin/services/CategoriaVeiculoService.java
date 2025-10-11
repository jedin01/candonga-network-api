package com.jedin.services;

import com.jedin.Repositories.CategoriaVeiculoRepository;
import com.jedin.dtos.CategoriaVeiculoDTO;
import com.jedin.models.CategoriaVeiculo;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@BrowserCallable
@AnonymousAllowed
@Service
public class CategoriaVeiculoService {

    @Autowired
    private CategoriaVeiculoRepository categoriaVeiculoRepository;

    // Listar todas as categorias
    public List<CategoriaVeiculoDTO> listarTodas() {
        List<CategoriaVeiculo> categorias = categoriaVeiculoRepository.findAllByOrderByNomeAsc();
        return categorias.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar categoria por ID
    public Optional<CategoriaVeiculoDTO> buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        Optional<CategoriaVeiculo> categoria = categoriaVeiculoRepository.findById(id);
        return categoria.map(this::convertToDTO);
    }

    // Criar nova categoria
    public CategoriaVeiculoDTO criar(@Valid CategoriaVeiculoDTO categoriaDTO) {
        if (categoriaDTO == null) {
            throw new IllegalArgumentException("Dados da categoria não podem ser nulos");
        }

        // Verificar se o nome já existe
        if (categoriaVeiculoRepository.existsByNomeIgnoreCase(categoriaDTO.getNome().trim())) {
            throw new IllegalArgumentException("Já existe uma categoria com este nome");
        }

        CategoriaVeiculo categoria = convertToEntity(categoriaDTO);
        categoria.setNome(categoria.getNome().trim());

        CategoriaVeiculo categoriaSalva = categoriaVeiculoRepository.save(categoria);
        return convertToDTO(categoriaSalva);
    }

    // Atualizar categoria
    public CategoriaVeiculoDTO atualizar(Long id, @Valid CategoriaVeiculoDTO categoriaDTO) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        if (categoriaDTO == null) {
            throw new IllegalArgumentException("Dados da categoria não podem ser nulos");
        }

        CategoriaVeiculo categoriaExistente = categoriaVeiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com ID: " + id));

        // Verificar se o novo nome já existe em outra categoria
        String novoNome = categoriaDTO.getNome().trim();
        if (!categoriaExistente.getNome().equalsIgnoreCase(novoNome) &&
            categoriaVeiculoRepository.existsByNomeIgnoreCase(novoNome)) {
            throw new IllegalArgumentException("Já existe uma categoria com este nome");
        }

        categoriaExistente.setNome(novoNome);

        CategoriaVeiculo categoriaAtualizada = categoriaVeiculoRepository.save(categoriaExistente);
        return convertToDTO(categoriaAtualizada);
    }

    // Deletar categoria
    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        if (!categoriaVeiculoRepository.existsById(id)) {
            throw new IllegalArgumentException("Categoria não encontrada com ID: " + id);
        }

        categoriaVeiculoRepository.deleteById(id);
    }

    // Buscar por nome
    public List<CategoriaVeiculoDTO> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return List.of();
        }

        List<CategoriaVeiculo> categorias = categoriaVeiculoRepository.findByNomeContainingIgnoreCase(nome.trim());
        return categorias.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Buscar por texto (nome)
    public List<CategoriaVeiculoDTO> buscar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return List.of();
        }

        List<CategoriaVeiculo> categorias = categoriaVeiculoRepository.findByNomeContainingIgnoreCase(texto.trim());
        return categorias.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Verificar se nome existe
    public boolean verificarNomeExiste(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        return categoriaVeiculoRepository.existsByNomeIgnoreCase(nome.trim());
    }

    // Buscar categoria por nome exato
    public Optional<CategoriaVeiculoDTO> buscarPorNomeExato(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return Optional.empty();
        }

        Optional<CategoriaVeiculo> categoria = categoriaVeiculoRepository.findByNomeIgnoreCase(nome.trim());
        return categoria.map(this::convertToDTO);
    }

    // Contar total de categorias
    public long contarTotal() {
        return categoriaVeiculoRepository.count();
    }

    // Listar nomes das categorias
    public List<String> listarNomes() {
        return categoriaVeiculoRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(CategoriaVeiculo::getNome)
                .collect(Collectors.toList());
    }

    // Verificar se categoria está em uso
    public boolean categoriaEmUso(Long id) {
        if (id == null) {
            return false;
        }
        // Implementar lógica quando houver relação com veículos
        return false;
    }

    // Obter estatísticas básicas
    public CategoriaVeiculoStatistics obterEstatisticas() {
        long totalCategorias = categoriaVeiculoRepository.count();
        return new CategoriaVeiculoStatistics(totalCategorias);
    }

    // Conversão para DTO
    private CategoriaVeiculoDTO convertToDTO(CategoriaVeiculo categoria) {
        if (categoria == null) {
            return null;
        }

        CategoriaVeiculoDTO dto = new CategoriaVeiculoDTO();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        return dto;
    }

    // Conversão para Entity
    private CategoriaVeiculo convertToEntity(CategoriaVeiculoDTO dto) {
        if (dto == null) {
            return null;
        }

        CategoriaVeiculo categoria = new CategoriaVeiculo();
        categoria.setId(dto.getId());
        categoria.setNome(dto.getNome());
        return categoria;
    }

    // Classe auxiliar para estatísticas
    public static class CategoriaVeiculoStatistics {
        private final long totalCategorias;

        public CategoriaVeiculoStatistics(long totalCategorias) {
            this.totalCategorias = totalCategorias;
        }

        public long getTotalCategorias() {
            return totalCategorias;
        }
    }
}
