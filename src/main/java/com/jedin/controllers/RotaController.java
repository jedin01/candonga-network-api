package com.jedin.controllers;

import com.jedin.Repositories.ParagemRepository;
import com.jedin.Repositories.RotaRepository;
import com.jedin.dtos.RotaDTO;
import com.jedin.models.Paragem;
import com.jedin.models.Rota;
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
@RequestMapping("/api/rota")
@CrossOrigin(origins = "*")
public class RotaController {

    @Autowired
    private RotaRepository rotaRepository;

    @Autowired
    private ParagemRepository paragemRepository;

    // Listar todas as rotas
    @GetMapping
    public ResponseEntity<List<RotaDTO>> listarTodas() {
        try {
            List<Rota> rotas = rotaRepository.findAll();
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rota por IDs das paragens
    @GetMapping("/{origemId}/{destinoId}")
    public ResponseEntity<RotaDTO> buscarPorParagens(
        @PathVariable Long origemId,
        @PathVariable Long destinoId
    ) {
        try {
            Optional<Rota> rota =
                rotaRepository.findByParagemOrigemIdAndParagemDestinoId(
                    origemId,
                    destinoId
                );
            if (rota.isPresent()) {
                return ResponseEntity.ok(convertToDTO(rota.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Criar nova rota
    @PostMapping
    public ResponseEntity<RotaDTO> criar(@Valid @RequestBody RotaDTO rotaDTO) {
        try {
            // Verificar se as paragens existem
            Optional<Paragem> paragemOrigem = paragemRepository.findById(
                rotaDTO.getParagemOrigemId()
            );
            Optional<Paragem> paragemDestino = paragemRepository.findById(
                rotaDTO.getParagemDestinoId()
            );

            if (!paragemOrigem.isPresent() || !paragemDestino.isPresent()) {
                return ResponseEntity.badRequest().build();
            }

            // Verificar se já existe rota entre essas paragens
            if (
                rotaRepository.existsRotaBetweenParagens(
                    rotaDTO.getParagemOrigemId(),
                    rotaDTO.getParagemDestinoId()
                )
            ) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Verificar se origem e destino são diferentes
            if (
                rotaDTO
                    .getParagemOrigemId()
                    .equals(rotaDTO.getParagemDestinoId())
            ) {
                return ResponseEntity.badRequest().build();
            }

            Rota rota = convertToEntity(
                rotaDTO,
                paragemOrigem.get(),
                paragemDestino.get()
            );
            Rota rotaSalva = rotaRepository.save(rota);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                convertToDTO(rotaSalva)
            );
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Atualizar rota
    @PutMapping("/{origemId}/{destinoId}")
    public ResponseEntity<RotaDTO> atualizar(
        @PathVariable Long origemId,
        @PathVariable Long destinoId,
        @Valid @RequestBody RotaDTO rotaDTO
    ) {
        try {
            Optional<Rota> rotaExistente =
                rotaRepository.findByParagemOrigemIdAndParagemDestinoId(
                    origemId,
                    destinoId
                );
            if (!rotaExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Rota rota = rotaExistente.get();
            rota.setPreco(rotaDTO.getPreco());
            rota.setDistancia(rotaDTO.getDistancia());

            Rota rotaAtualizada = rotaRepository.save(rota);
            return ResponseEntity.ok(convertToDTO(rotaAtualizada));
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Deletar rota
    @DeleteMapping("/{origemId}/{destinoId}")
    public ResponseEntity<Void> deletar(
        @PathVariable Long origemId,
        @PathVariable Long destinoId
    ) {
        try {
            Optional<Rota> rota =
                rotaRepository.findByParagemOrigemIdAndParagemDestinoId(
                    origemId,
                    destinoId
                );
            if (!rota.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            rotaRepository.delete(rota.get());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rotas por paragem de origem
    @GetMapping("/origem/{origemId}")
    public ResponseEntity<List<RotaDTO>> buscarPorOrigem(
        @PathVariable Long origemId
    ) {
        try {
            List<Rota> rotas = rotaRepository.findByParagemOrigemId(origemId);
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rotas por paragem de destino
    @GetMapping("/destino/{destinoId}")
    public ResponseEntity<List<RotaDTO>> buscarPorDestino(
        @PathVariable Long destinoId
    ) {
        try {
            List<Rota> rotas = rotaRepository.findByParagemDestinoId(destinoId);
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar todas as rotas de uma paragem (origem e destino)
    @GetMapping("/paragem/{paragemId}")
    public ResponseEntity<List<RotaDTO>> buscarTodasRotasParagem(
        @PathVariable Long paragemId
    ) {
        try {
            List<Rota> rotas = rotaRepository.findAllRotasByParagemId(
                paragemId
            );
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rotas por faixa de preço
    @GetMapping("/preco")
    public ResponseEntity<List<RotaDTO>> buscarPorFaixaPreco(
        @RequestParam BigDecimal precoMin,
        @RequestParam BigDecimal precoMax
    ) {
        try {
            List<Rota> rotas = rotaRepository.findByPrecoRange(
                precoMin,
                precoMax
            );
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rotas por faixa de distância
    @GetMapping("/distancia")
    public ResponseEntity<List<RotaDTO>> buscarPorFaixaDistancia(
        @RequestParam BigDecimal distanciaMin,
        @RequestParam BigDecimal distanciaMax
    ) {
        try {
            List<Rota> rotas = rotaRepository.findByDistanciaRange(
                distanciaMin,
                distanciaMax
            );
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rotas mais baratas
    @GetMapping("/mais-baratas")
    public ResponseEntity<List<RotaDTO>> buscarMaisBaratas() {
        try {
            List<Rota> rotas = rotaRepository.findAllByOrderByPrecoAsc();
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rotas mais curtas
    @GetMapping("/mais-curtas")
    public ResponseEntity<List<RotaDTO>> buscarMaisCurtas() {
        try {
            List<Rota> rotas = rotaRepository.findAllByOrderByDistanciaAsc();
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rotas com melhor custo-benefício
    @GetMapping("/melhor-custo-beneficio")
    public ResponseEntity<List<RotaDTO>> buscarMelhorCustoBeneficio() {
        try {
            List<Rota> rotas = rotaRepository.findByMelhorCustoBeneficio();
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rotas por categoria de origem
    @GetMapping("/categoria-origem/{categoriaId}")
    public ResponseEntity<List<RotaDTO>> buscarPorCategoriaOrigem(
        @PathVariable Long categoriaId
    ) {
        try {
            List<Rota> rotas = rotaRepository.findByParagemOrigemCategoriaId(
                categoriaId
            );
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rotas por categoria de destino
    @GetMapping("/categoria-destino/{categoriaId}")
    public ResponseEntity<List<RotaDTO>> buscarPorCategoriaDestino(
        @PathVariable Long categoriaId
    ) {
        try {
            List<Rota> rotas = rotaRepository.findByParagemDestinoCategoriaId(
                categoriaId
            );
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rotas entre categorias específicas
    @GetMapping("/categorias/{categoriaOrigemId}/{categoriaDestinoId}")
    public ResponseEntity<List<RotaDTO>> buscarEntreCategorias(
        @PathVariable Long categoriaOrigemId,
        @PathVariable Long categoriaDestinoId
    ) {
        try {
            List<Rota> rotas = rotaRepository.findByCategorias(
                categoriaOrigemId,
                categoriaDestinoId
            );
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar destinos disponíveis a partir de uma origem
    @GetMapping("/destinos-disponiveis/{origemId}")
    public ResponseEntity<List<Paragem>> buscarDestinosDisponiveis(
        @PathVariable Long origemId
    ) {
        try {
            List<Paragem> destinos = rotaRepository.findDestinosFromOrigem(
                origemId
            );
            return ResponseEntity.ok(destinos);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar origens que chegam a um destino
    @GetMapping("/origens-disponiveis/{destinoId}")
    public ResponseEntity<List<Paragem>> buscarOrigensDisponiveis(
        @PathVariable Long destinoId
    ) {
        try {
            List<Paragem> origens = rotaRepository.findOrigensToDestino(
                destinoId
            );
            return ResponseEntity.ok(origens);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rotas diretas entre duas paragens
    @GetMapping("/diretas/{paragem1Id}/{paragem2Id}")
    public ResponseEntity<List<RotaDTO>> buscarRotasDiretas(
        @PathVariable Long paragem1Id,
        @PathVariable Long paragem2Id
    ) {
        try {
            List<Rota> rotas = rotaRepository.findRotasDirectasBetweenParagens(
                paragem1Id,
                paragem2Id
            );
            List<RotaDTO> rotasDTO = rotas
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(rotasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Obter estatísticas de preços
    @GetMapping("/estatisticas/precos")
    public ResponseEntity<List<Object[]>> obterEstatisticasPrecos() {
        try {
            List<Object[]> estatisticas = rotaRepository.getPrecoStatistics();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Obter estatísticas de distâncias
    @GetMapping("/estatisticas/distancias")
    public ResponseEntity<List<Object[]>> obterEstatisticasDistancias() {
        try {
            List<Object[]> estatisticas =
                rotaRepository.getDistanciaStatistics();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Contar rotas por paragem de origem
    @GetMapping("/count/origem")
    public ResponseEntity<List<Object[]>> contarRotasPorOrigem() {
        try {
            List<Object[]> contagem =
                rotaRepository.countRotasByParagemOrigem();
            return ResponseEntity.ok(contagem);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Contar rotas por paragem de destino
    @GetMapping("/count/destino")
    public ResponseEntity<List<Object[]>> contarRotasPorDestino() {
        try {
            List<Object[]> contagem =
                rotaRepository.countRotasByParagemDestino();
            return ResponseEntity.ok(contagem);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Verificar se existe rota entre duas paragens
    @GetMapping("/verificar/{origemId}/{destinoId}")
    public ResponseEntity<Boolean> verificarRotaExiste(
        @PathVariable Long origemId,
        @PathVariable Long destinoId
    ) {
        try {
            boolean existe = rotaRepository.existsRotaBetweenParagens(
                origemId,
                destinoId
            );
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Buscar rotas com conexões
    @GetMapping("/conexoes")
    public ResponseEntity<List<Object[]>> buscarRotasComConexoes() {
        try {
            List<Object[]> conexoes = rotaRepository.findRotasWithConnections();
            return ResponseEntity.ok(conexoes);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // Métodos de conversão
    private RotaDTO convertToDTO(Rota rota) {
        return new RotaDTO(
            rota.getParagemOrigem().getId(),
            rota.getParagemDestino().getId(),
            rota.getPreco(),
            rota.getDistancia(),
            rota.getParagemOrigem().getLatitude(),
            rota.getParagemOrigem().getLongitude(),
            rota.getParagemDestino().getLatitude(),
            rota.getParagemDestino().getLongitude(),
            rota.getParagemOrigem().getCategoriaParagem().getNome(),
            rota.getParagemDestino().getCategoriaParagem().getNome()
        );
    }

    private Rota convertToEntity(
        RotaDTO dto,
        Paragem paragemOrigem,
        Paragem paragemDestino
    ) {
        return new Rota(
            paragemOrigem,
            paragemDestino,
            dto.getPreco(),
            dto.getDistancia()
        );
    }
}
