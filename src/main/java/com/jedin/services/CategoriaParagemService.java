package com.jedin.services;

import com.jedin.Repositories.CategoriaParagemRepository;
import com.jedin.dtos.CategoriaParagemDTO;
import com.jedin.models.CategoriaParagem;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@BrowserCallable
@AnonymousAllowed
@Service
public class CategoriaParagemService {

    @Autowired
    private CategoriaParagemRepository categoriaParagemRepository;

    // Listar todas as categorias
    public List<CategoriaParagemDTO> listarTodas() {
        List<CategoriaParagem> categorias =
            categoriaParagemRepository.findAllByOrderByNomeAsc();
        return categorias
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Buscar categoria por ID
    public Optional<CategoriaParagemDTO> buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        Optional<CategoriaParagem> categoria =
            categoriaParagemRepository.findById(id);
        return categoria.map(this::convertToDTO);
    }

    // Criar nova categoria
    public CategoriaParagemDTO criar(@Valid CategoriaParagemDTO categoriaDTO) {
        if (categoriaDTO == null) {
            throw new IllegalArgumentException(
                "Dados da categoria não podem ser nulos"
            );
        }

        // Verificar se o nome já existe
        if (
            categoriaParagemRepository.existsByNomeIgnoreCase(
                categoriaDTO.getNome().trim()
            )
        ) {
            throw new IllegalArgumentException(
                "Já existe uma categoria com este nome"
            );
        }

        CategoriaParagem categoria = convertToEntity(categoriaDTO);
        categoria.setNome(categoria.getNome().trim());

        if (categoria.getDescricao() != null) {
            categoria.setDescricao(categoria.getDescricao().trim());
        }

        CategoriaParagem categoriaSalva = categoriaParagemRepository.save(
            categoria
        );
        return convertToDTO(categoriaSalva);
    }

    // Atualizar categoria
    public CategoriaParagemDTO atualizar(
        Long id,
        @Valid CategoriaParagemDTO categoriaDTO
    ) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        if (categoriaDTO == null) {
            throw new IllegalArgumentException(
                "Dados da categoria não podem ser nulos"
            );
        }

        CategoriaParagem categoriaExistente = categoriaParagemRepository
            .findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Categoria não encontrada com ID: " + id
                )
            );

        // Verificar se o novo nome já existe em outra categoria
        String novoNome = categoriaDTO.getNome().trim();
        if (
            !categoriaExistente.getNome().equalsIgnoreCase(novoNome) &&
            categoriaParagemRepository.existsByNomeIgnoreCase(novoNome)
        ) {
            throw new IllegalArgumentException(
                "Já existe uma categoria com este nome"
            );
        }

        categoriaExistente.setNome(novoNome);
        if (categoriaDTO.getDescricao() != null) {
            categoriaExistente.setDescricao(categoriaDTO.getDescricao().trim());
        } else {
            categoriaExistente.setDescricao(null);
        }

        CategoriaParagem categoriaAtualizada = categoriaParagemRepository.save(
            categoriaExistente
        );
        return convertToDTO(categoriaAtualizada);
    }

    // Deletar categoria
    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        if (!categoriaParagemRepository.existsById(id)) {
            throw new IllegalArgumentException(
                "Categoria não encontrada com ID: " + id
            );
        }

        try {
            categoriaParagemRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(
                "Não é possível deletar esta categoria pois existem paragens associadas a ela"
            );
        }
    }

    // Buscar por nome
    public List<CategoriaParagemDTO> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return List.of();
        }

        List<CategoriaParagem> categorias =
            categoriaParagemRepository.findByNomeContainingIgnoreCase(
                nome.trim()
            );
        return categorias
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Buscar por descrição
    public List<CategoriaParagemDTO> buscarPorDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            return List.of();
        }

        List<CategoriaParagem> categorias =
            categoriaParagemRepository.findByDescricaoContainingIgnoreCase(
                descricao.trim()
            );
        return categorias
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Buscar por nome ou descrição
    public List<CategoriaParagemDTO> buscar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return List.of();
        }

        List<CategoriaParagem> categorias =
            categoriaParagemRepository.findByNomeOrDescricaoContaining(
                texto.trim()
            );
        return categorias
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Obter estatísticas
    public List<CategoriaStatistics> obterEstatisticas() {
        List<Object[]> stats =
            categoriaParagemRepository.countParagensByCategoria();
        return stats
            .stream()
            .map(row ->
                new CategoriaStatistics(
                    (String) row[0], // nome da categoria
                    ((Number) row[1]).longValue() // contagem de paragens
                )
            )
            .collect(Collectors.toList());
    }

    // Verificar se nome existe
    public boolean verificarNomeExiste(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        return categoriaParagemRepository.existsByNomeIgnoreCase(nome.trim());
    }

    // Contar total de categorias
    public long contarTotal() {
        return categoriaParagemRepository.count();
    }

    // Conversão para DTO
    private CategoriaParagemDTO convertToDTO(CategoriaParagem categoria) {
        if (categoria == null) {
            return null;
        }

        CategoriaParagemDTO dto = new CategoriaParagemDTO();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        dto.setDescricao(categoria.getDescricao());
        return dto;
    }

    // Conversão para Entity
    private CategoriaParagem convertToEntity(CategoriaParagemDTO dto) {
        if (dto == null) {
            return null;
        }

        CategoriaParagem categoria = new CategoriaParagem();
        categoria.setId(dto.getId());
        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        return categoria;
    }

    // Classe auxiliar para estatísticas
    public static class CategoriaStatistics {

        private final String nomeCategoria;
        private final long quantidadeParagens;

        public CategoriaStatistics(
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
}
