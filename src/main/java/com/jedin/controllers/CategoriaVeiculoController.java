package com.jedin.controllers;

import com.jedin.Repositories.CategoriaVeiculoRepository;
import com.jedin.dtos.CategoriaVeiculoDTO;
import com.jedin.models.CategoriaVeiculo;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categoria-veiculo")
@CrossOrigin(origins = "*")
public class CategoriaVeiculoController {

    @Autowired
    private CategoriaVeiculoRepository categoriaVeiculoRepository;

    // Listar todas as categorias de veículo
    @GetMapping
    public ResponseEntity<List<CategoriaVeiculoDTO>> listarTodas() {
        try {
            List<CategoriaVeiculo> categorias =
                categoriaVeiculoRepository.findAllByOrderByNomeAsc();
            List<CategoriaVeiculoDTO> categoriasDTO = categorias
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
    public ResponseEntity<CategoriaVeiculoDTO> buscarPorId(
        @PathVariable Long id
    ) {
        try {
            Optional<CategoriaVeiculo> categoria =
                categoriaVeiculoRepository.findById(id);
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

    // Criar nova categoria de veículo
    @PostMapping
    public ResponseEntity<CategoriaVeiculoDTO> criar(
        @Valid @RequestBody CategoriaVeiculoDTO categoriaVeiculoDTO
    ) {
        try {
            // Verificar se já existe categoria com o mesmo nome
            if (
                categoriaVeiculoRepository.existsByNomeIgnoreCase(
                    categoriaVeiculoDTO.getNome()
                )
            ) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            CategoriaVeiculo categoria = convertToEntity(categoriaVeiculoDTO);
            CategoriaVeiculo categoriaSalva = categoriaVeiculoRepository.save(
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

    // Atualizar categoria de veículo
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaVeiculoDTO> atualizar(
        @PathVariable Long id,
        @Valid @RequestBody CategoriaVeiculoDTO categoriaVeiculoDTO
    ) {
        try {
            Optional<CategoriaVeiculo> categoriaExistente =
                categoriaVeiculoRepository.findById(id);
            if (!categoriaExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Verificar se existe outra categoria com o mesmo nome
            Optional<CategoriaVeiculo> categoriaComMesmoNome =
                categoriaVeiculoRepository.findByNomeIgnoreCase(
                    categoriaVeiculoDTO.getNome()
                );
            if (
                categoriaComMesmoNome.isPresent() &&
                !categoriaComMesmoNome.get().getId().equals(id)
            ) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            CategoriaVeiculo categoria = categoriaExistente.get();
            categoria.setNome(categoriaVeiculoDTO.getNome());

            CategoriaVeiculo categoriaAtualizada =
                categoriaVeiculoRepository.save(categoria);
            return ResponseEntity.ok(convertToDTO(categoriaAtualizada));
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Deletar categoria de veículo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            Optional<CategoriaVeiculo> categoria =
                categoriaVeiculoRepository.findById(id);
            if (!categoria.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            categoriaVeiculoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar categorias por nome
    @GetMapping("/buscar/nome")
    public ResponseEntity<List<CategoriaVeiculoDTO>> buscarPorNome(
        @RequestParam String nome
    ) {
        try {
            List<CategoriaVeiculo> categorias =
                categoriaVeiculoRepository.findByNomeContainingIgnoreCase(nome);
            List<CategoriaVeiculoDTO> categoriasDTO = categorias
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

    // Buscar categorias por parte do nome
    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaVeiculoDTO>> buscarPorTexto(
        @RequestParam String texto
    ) {
        try {
            List<CategoriaVeiculo> categorias =
                categoriaVeiculoRepository.findByNomeContaining(texto);
            List<CategoriaVeiculoDTO> categoriasDTO = categorias
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

    // Obter contagem total
    @GetMapping("/count")
    public ResponseEntity<Long> obterContagem() {
        try {
            long count = categoriaVeiculoRepository.countTotal();
            return ResponseEntity.ok(count);
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
            boolean existe = categoriaVeiculoRepository.existsByNomeIgnoreCase(
                nome
            );
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar categoria por nome exato
    @GetMapping("/buscar-exato")
    public ResponseEntity<CategoriaVeiculoDTO> buscarPorNomeExato(
        @RequestParam String nome
    ) {
        try {
            Optional<CategoriaVeiculo> categoria =
                categoriaVeiculoRepository.findByNomeIgnoreCase(nome);
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

    // Métodos de conversão
    private CategoriaVeiculoDTO convertToDTO(CategoriaVeiculo categoria) {
        return new CategoriaVeiculoDTO(categoria.getId(), categoria.getNome());
    }

    private CategoriaVeiculo convertToEntity(CategoriaVeiculoDTO dto) {
        CategoriaVeiculo categoria = new CategoriaVeiculo();
        categoria.setId(dto.getId());
        categoria.setNome(dto.getNome());
        return categoria;
    }
}
