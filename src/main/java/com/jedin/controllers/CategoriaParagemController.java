package com.jedin.controllers;

import com.jedin.Repositories.CategoriaParagemRepository;
import com.jedin.dtos.CategoriaParagemDTO;
import com.jedin.models.CategoriaParagem;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categoria-paragem")
@CrossOrigin(origins = "*")
public class CategoriaParagemController {

    @Autowired
    private CategoriaParagemRepository categoriaParagemRepository;

    // Listar todas as categorias de paragem
    @GetMapping
    public ResponseEntity<List<CategoriaParagemDTO>> listarTodas() {
        try {
            List<CategoriaParagem> categorias =
                categoriaParagemRepository.findAllByOrderByNomeAsc();
            List<CategoriaParagemDTO> categoriasDTO = categorias
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(categoriasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar categoria por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaParagemDTO> buscarPorId(
        @PathVariable Long id
    ) {
        try {
            Optional<CategoriaParagem> categoria =
                categoriaParagemRepository.findById(id);
            if (categoria.isPresent()) {
                return ResponseEntity.ok(convertToDTO(categoria.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Criar nova categoria de paragem
    @PostMapping
    public ResponseEntity<CategoriaParagemDTO> criar(
        @Valid @RequestBody CategoriaParagemDTO categoriaParagemDTO
    ) {
        try {
            // Verificar se já existe categoria com o mesmo nome
            if (
                categoriaParagemRepository.existsByNomeIgnoreCase(
                    categoriaParagemDTO.getNome()
                )
            ) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            CategoriaParagem categoria = convertToEntity(categoriaParagemDTO);
            CategoriaParagem categoriaSalva = categoriaParagemRepository.save(
                categoria
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(
                convertToDTO(categoriaSalva)
            );
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Atualizar categoria de paragem
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaParagemDTO> atualizar(
        @PathVariable Long id,
        @Valid @RequestBody CategoriaParagemDTO categoriaParagemDTO
    ) {
        try {
            Optional<CategoriaParagem> categoriaExistente =
                categoriaParagemRepository.findById(id);
            if (!categoriaExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Verificar se existe outra categoria com o mesmo nome
            Optional<CategoriaParagem> categoriaComMesmoNome =
                categoriaParagemRepository.findByNomeIgnoreCase(
                    categoriaParagemDTO.getNome()
                );
            if (
                categoriaComMesmoNome.isPresent() &&
                !categoriaComMesmoNome.get().getId().equals(id)
            ) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            CategoriaParagem categoria = categoriaExistente.get();
            categoria.setNome(categoriaParagemDTO.getNome());
            categoria.setDescricao(categoriaParagemDTO.getDescricao());

            CategoriaParagem categoriaAtualizada =
                categoriaParagemRepository.save(categoria);
            return ResponseEntity.ok(convertToDTO(categoriaAtualizada));
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Deletar categoria de paragem
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            Optional<CategoriaParagem> categoria =
                categoriaParagemRepository.findById(id);
            if (!categoria.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Verificar se a categoria está sendo usada por alguma paragem
            CategoriaParagem cat = categoria.get();
            if (cat.getParagens() != null && !cat.getParagens().isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            categoriaParagemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar categorias por nome
    @GetMapping("/buscar/nome")
    public ResponseEntity<List<CategoriaParagemDTO>> buscarPorNome(
        @RequestParam String nome
    ) {
        try {
            List<CategoriaParagem> categorias =
                categoriaParagemRepository.findByNomeContainingIgnoreCase(nome);
            List<CategoriaParagemDTO> categoriasDTO = categorias
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(categoriasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar categorias por descrição
    @GetMapping("/buscar/descricao")
    public ResponseEntity<List<CategoriaParagemDTO>> buscarPorDescricao(
        @RequestParam String descricao
    ) {
        try {
            List<CategoriaParagem> categorias =
                categoriaParagemRepository.findByDescricaoContainingIgnoreCase(
                    descricao
                );
            List<CategoriaParagemDTO> categoriasDTO = categorias
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(categoriasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar categorias por nome ou descrição
    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaParagemDTO>> buscarPorTexto(
        @RequestParam String texto
    ) {
        try {
            List<CategoriaParagem> categorias =
                categoriaParagemRepository.findByNomeOrDescricaoContaining(
                    texto
                );
            List<CategoriaParagemDTO> categoriasDTO = categorias
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(categoriasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Obter estatísticas das categorias
    @GetMapping("/estatisticas")
    public ResponseEntity<List<Object[]>> obterEstatisticas() {
        try {
            List<Object[]> estatisticas =
                categoriaParagemRepository.countParagensByCategoria();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Verificar se nome já existe
    @GetMapping("/verificar-nome")
    public ResponseEntity<Boolean> verificarNomeExiste(
        @RequestParam String nome
    ) {
        try {
            boolean existe = categoriaParagemRepository.existsByNomeIgnoreCase(
                nome
            );
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Métodos de conversão
    private CategoriaParagemDTO convertToDTO(CategoriaParagem categoria) {
        return new CategoriaParagemDTO(
            categoria.getId(),
            categoria.getNome(),
            categoria.getDescricao()
        );
    }

    private CategoriaParagem convertToEntity(CategoriaParagemDTO dto) {
        CategoriaParagem categoria = new CategoriaParagem();
        categoria.setId(dto.getId());
        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        return categoria;
    }
}
