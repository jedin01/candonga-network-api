package com.jedin.controllers;

import com.jedin.Repositories.CategoriaParagemRepository;
import com.jedin.Repositories.ParagemRepository;
import com.jedin.dtos.ParagemDTO;
import com.jedin.models.CategoriaParagem;
import com.jedin.models.Paragem;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paragem")
@CrossOrigin(origins = "*")
public class ParagemController {

    @Autowired
    private ParagemRepository paragemRepository;

    @Autowired
    private CategoriaParagemRepository categoriaParagemRepository;

    // Listar todas as paragens
    @GetMapping
    public ResponseEntity<List<ParagemDTO>> listarTodas() {
        try {
            List<Paragem> paragens =
                paragemRepository.findAllOrderByCategoria();
            List<ParagemDTO> paragensDTO = paragens
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(paragensDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar paragem por ID
    @GetMapping("/{id}")
    public ResponseEntity<ParagemDTO> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Paragem> paragem = paragemRepository.findById(id);
            if (paragem.isPresent()) {
                return ResponseEntity.ok(convertToDTO(paragem.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Criar nova paragem
    @PostMapping
    public ResponseEntity<ParagemDTO> criar(
        @Valid @RequestBody ParagemDTO paragemDTO
    ) {
        try {
            // Verificar se a categoria existe
            Optional<CategoriaParagem> categoria =
                categoriaParagemRepository.findById(
                    paragemDTO.getCategoriaParagemId()
                );
            if (!categoria.isPresent()) {
                return ResponseEntity.badRequest().build();
            }

            // Verificar se já existe paragem na mesma localização
            Optional<Paragem> paragemExistente =
                paragemRepository.findByLatitudeAndLongitude(
                    paragemDTO.getLatitude(),
                    paragemDTO.getLongitude()
                );
            if (paragemExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Paragem paragem = convertToEntity(paragemDTO, categoria.get());
            Paragem paragemSalva = paragemRepository.save(paragem);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                convertToDTO(paragemSalva)
            );
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Atualizar paragem
    @PutMapping("/{id}")
    public ResponseEntity<ParagemDTO> atualizar(
        @PathVariable Long id,
        @Valid @RequestBody ParagemDTO paragemDTO
    ) {
        try {
            Optional<Paragem> paragemExistente = paragemRepository.findById(id);
            if (!paragemExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Verificar se a categoria existe
            Optional<CategoriaParagem> categoria =
                categoriaParagemRepository.findById(
                    paragemDTO.getCategoriaParagemId()
                );
            if (!categoria.isPresent()) {
                return ResponseEntity.badRequest().build();
            }

            // Verificar se existe outra paragem na mesma localização
            Optional<Paragem> paragemMesmaLocalizacao =
                paragemRepository.findByLatitudeAndLongitude(
                    paragemDTO.getLatitude(),
                    paragemDTO.getLongitude()
                );
            if (
                paragemMesmaLocalizacao.isPresent() &&
                !paragemMesmaLocalizacao.get().getId().equals(id)
            ) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Paragem paragem = paragemExistente.get();
            paragem.setLatitude(paragemDTO.getLatitude());
            paragem.setLongitude(paragemDTO.getLongitude());
            paragem.setCategoriaParagem(categoria.get());

            Paragem paragemAtualizada = paragemRepository.save(paragem);
            return ResponseEntity.ok(convertToDTO(paragemAtualizada));
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Deletar paragem
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            Optional<Paragem> paragem = paragemRepository.findById(id);
            if (!paragem.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Verificar se a paragem está sendo usada em rotas
            Paragem p = paragem.get();
            if (
                (p.getRotasOrigem() != null && !p.getRotasOrigem().isEmpty()) ||
                (p.getRotasDestino() != null && !p.getRotasDestino().isEmpty())
            ) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            paragemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar paragens por categoria
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ParagemDTO>> buscarPorCategoria(
        @PathVariable Long categoriaId
    ) {
        try {
            List<Paragem> paragens = paragemRepository.findByCategoriaParagemId(
                categoriaId
            );
            List<ParagemDTO> paragensDTO = paragens
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(paragensDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar paragens por nome da categoria
    @GetMapping("/categoria/nome/{nomeCategoria}")
    public ResponseEntity<List<ParagemDTO>> buscarPorNomeCategoria(
        @PathVariable String nomeCategoria
    ) {
        try {
            List<Paragem> paragens =
                paragemRepository.findByCategoriaParagemNome(nomeCategoria);
            List<ParagemDTO> paragensDTO = paragens
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(paragensDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar paragens em uma área específica
    @GetMapping("/area")
    public ResponseEntity<List<ParagemDTO>> buscarPorArea(
        @RequestParam BigDecimal latMin,
        @RequestParam BigDecimal latMax,
        @RequestParam BigDecimal longMin,
        @RequestParam BigDecimal longMax
    ) {
        try {
            List<Paragem> paragens = paragemRepository.findParagensInArea(
                latMin,
                latMax,
                longMin,
                longMax
            );
            List<ParagemDTO> paragensDTO = paragens
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(paragensDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar paragens próximas
    @GetMapping("/proximas")
    public ResponseEntity<List<ParagemDTO>> buscarProximas(
        @RequestParam BigDecimal latitude,
        @RequestParam BigDecimal longitude,
        @RequestParam(defaultValue = "0.01") BigDecimal raio
    ) {
        try {
            List<Paragem> paragens = paragemRepository.findParagensNearby(
                latitude,
                longitude,
                raio,
                raio
            );
            List<ParagemDTO> paragensDTO = paragens
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(paragensDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar paragens conectadas (com rotas)
    @GetMapping("/conectadas")
    public ResponseEntity<List<ParagemDTO>> buscarConectadas() {
        try {
            List<Paragem> paragens = paragemRepository.findParagensConnected();
            List<ParagemDTO> paragensDTO = paragens
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(paragensDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar paragens isoladas (sem rotas)
    @GetMapping("/isoladas")
    public ResponseEntity<List<ParagemDTO>> buscarIsoladas() {
        try {
            List<Paragem> paragens = paragemRepository.findParagensIsolated();
            List<ParagemDTO> paragensDTO = paragens
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(paragensDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar paragens que são origem de rotas
    @GetMapping("/origem-rotas")
    public ResponseEntity<List<ParagemDTO>> buscarOrigensDeRotas() {
        try {
            List<Paragem> paragens =
                paragemRepository.findParagensWithRotasOrigem();
            List<ParagemDTO> paragensDTO = paragens
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(paragensDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar paragens que são destino de rotas
    @GetMapping("/destino-rotas")
    public ResponseEntity<List<ParagemDTO>> buscarDestinosDeRotas() {
        try {
            List<Paragem> paragens =
                paragemRepository.findParagensWithRotasDestino();
            List<ParagemDTO> paragensDTO = paragens
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(paragensDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Obter estatísticas de paragens por categoria
    @GetMapping("/estatisticas")
    public ResponseEntity<List<Object[]>> obterEstatisticas() {
        try {
            List<Object[]> estatisticas =
                paragemRepository.countParagensByCategoria();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar paragens por categoria com contagem de rotas
    @GetMapping("/categoria/{categoriaId}/com-rotas")
    public ResponseEntity<List<Object[]>> buscarPorCategoriaComRotas(
        @PathVariable Long categoriaId
    ) {
        try {
            List<Object[]> resultado =
                paragemRepository.findParagensWithRotaCountByCategoria(
                    categoriaId
                );
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Verificar se coordenadas já existem
    @GetMapping("/verificar-coordenadas")
    public ResponseEntity<Boolean> verificarCoordenadasExistem(
        @RequestParam BigDecimal latitude,
        @RequestParam BigDecimal longitude
    ) {
        try {
            Optional<Paragem> paragem =
                paragemRepository.findByLatitudeAndLongitude(
                    latitude,
                    longitude
                );
            return ResponseEntity.ok(paragem.isPresent());
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Métodos de conversão
    private ParagemDTO convertToDTO(Paragem paragem) {
        return new ParagemDTO(
            paragem.getId(),
            paragem.getLatitude(),
            paragem.getLongitude(),
            paragem.getCategoriaParagem().getId(),
            paragem.getCategoriaParagem().getNome()
        );
    }

    private Paragem convertToEntity(
        ParagemDTO dto,
        CategoriaParagem categoria
    ) {
        Paragem paragem = new Paragem();
        paragem.setId(dto.getId());
        paragem.setLatitude(dto.getLatitude());
        paragem.setLongitude(dto.getLongitude());
        paragem.setCategoriaParagem(categoria);
        return paragem;
    }
}
