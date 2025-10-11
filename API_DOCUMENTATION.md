# Documentação da API - Sistema de Transporte Indika

## Visão Geral

Esta API fornece endpoints para gerenciar um sistema de transporte com categorias de paragens, paragens, categorias de veículos e rotas.

## Estrutura do Banco de Dados

### Entidades

1. **CategoriaParagem** - Categorias de pontos de paragem
   - `id` (Long) - Identificador único
   - `nome` (String) - Nome da categoria
   - `descricao` (String) - Descrição da categoria

2. **CategoriaVeiculo** - Categorias de veículos
   - `id` (Long) - Identificador único
   - `nome` (String) - Nome da categoria

3. **Paragem** - Pontos de paragem do sistema
   - `id` (Long) - Identificador único
   - `latitude` (BigDecimal) - Coordenada de latitude
   - `longitude` (BigDecimal) - Coordenada de longitude
   - `categoria_paragem_id` (Long) - Referência à categoria da paragem

4. **Rota** - Rotas entre paragens
   - `paragem_origem_id` (Long) - ID da paragem de origem
   - `paragem_destino_id` (Long) - ID da paragem de destino
   - `preco` (BigDecimal) - Preço da rota
   - `distancia` (BigDecimal) - Distância da rota

## Endpoints da API

### 1. Categoria de Paragem (`/api/categoria-paragem`)

#### GET `/api/categoria-paragem`
Lista todas as categorias de paragem ordenadas por nome.
- **Resposta**: Lista de `CategoriaParagemDTO`

#### GET `/api/categoria-paragem/{id}`
Busca uma categoria específica por ID.
- **Parâmetros**: `id` (Long) - ID da categoria
- **Resposta**: `CategoriaParagemDTO`

#### POST `/api/categoria-paragem`
Cria uma nova categoria de paragem.
- **Body**: `CategoriaParagemDTO`
- **Resposta**: `CategoriaParagemDTO` criada
- **Status**: 201 (Created), 409 (Conflict se nome já existe)

#### PUT `/api/categoria-paragem/{id}`
Atualiza uma categoria existente.
- **Parâmetros**: `id` (Long) - ID da categoria
- **Body**: `CategoriaParagemDTO`
- **Resposta**: `CategoriaParagemDTO` atualizada

#### DELETE `/api/categoria-paragem/{id}`
Remove uma categoria de paragem.
- **Parâmetros**: `id` (Long) - ID da categoria
- **Status**: 204 (No Content), 409 (Conflict se categoria tem paragens)

#### GET `/api/categoria-paragem/buscar/nome?nome={nome}`
Busca categorias por nome (parcial, case-insensitive).
- **Parâmetros Query**: `nome` (String)
- **Resposta**: Lista de `CategoriaParagemDTO`

#### GET `/api/categoria-paragem/buscar/descricao?descricao={descricao}`
Busca categorias por descrição (parcial, case-insensitive).
- **Parâmetros Query**: `descricao` (String)
- **Resposta**: Lista de `CategoriaParagemDTO`

#### GET `/api/categoria-paragem/buscar?texto={texto}`
Busca categorias por nome ou descrição.
- **Parâmetros Query**: `texto` (String)
- **Resposta**: Lista de `CategoriaParagemDTO`

#### GET `/api/categoria-paragem/estatisticas`
Obtém estatísticas de paragens por categoria.
- **Resposta**: Lista de arrays com [categoria, contagem]

#### GET `/api/categoria-paragem/verificar-nome?nome={nome}`
Verifica se um nome de categoria já existe.
- **Parâmetros Query**: `nome` (String)
- **Resposta**: Boolean

### 2. Categoria de Veículo (`/api/categoria-veiculo`)

#### GET `/api/categoria-veiculo`
Lista todas as categorias de veículo ordenadas por nome.
- **Resposta**: Lista de `CategoriaVeiculoDTO`

#### GET `/api/categoria-veiculo/{id}`
Busca uma categoria específica por ID.
- **Parâmetros**: `id` (Long) - ID da categoria
- **Resposta**: `CategoriaVeiculoDTO`

#### POST `/api/categoria-veiculo`
Cria uma nova categoria de veículo.
- **Body**: `CategoriaVeiculoDTO`
- **Resposta**: `CategoriaVeiculoDTO` criada
- **Status**: 201 (Created), 409 (Conflict se nome já existe)

#### PUT `/api/categoria-veiculo/{id}`
Atualiza uma categoria existente.
- **Parâmetros**: `id` (Long) - ID da categoria
- **Body**: `CategoriaVeiculoDTO`
- **Resposta**: `CategoriaVeiculoDTO` atualizada

#### DELETE `/api/categoria-veiculo/{id}`
Remove uma categoria de veículo.
- **Parâmetros**: `id` (Long) - ID da categoria
- **Status**: 204 (No Content)

#### GET `/api/categoria-veiculo/buscar/nome?nome={nome}`
Busca categorias por nome (parcial, case-insensitive).
- **Parâmetros Query**: `nome` (String)
- **Resposta**: Lista de `CategoriaVeiculoDTO`

#### GET `/api/categoria-veiculo/buscar?texto={texto}`
Busca categorias por parte do nome.
- **Parâmetros Query**: `texto` (String)
- **Resposta**: Lista de `CategoriaVeiculoDTO`

#### GET `/api/categoria-veiculo/count`
Obtém contagem total de categorias.
- **Resposta**: Long

#### GET `/api/categoria-veiculo/verificar-nome?nome={nome}`
Verifica se um nome de categoria já existe.
- **Parâmetros Query**: `nome` (String)
- **Resposta**: Boolean

#### GET `/api/categoria-veiculo/buscar-exato?nome={nome}`
Busca categoria por nome exato.
- **Parâmetros Query**: `nome` (String)
- **Resposta**: `CategoriaVeiculoDTO`

### 3. Paragem (`/api/paragem`)

#### GET `/api/paragem`
Lista todas as paragens ordenadas por categoria.
- **Resposta**: Lista de `ParagemDTO`

#### GET `/api/paragem/{id}`
Busca uma paragem específica por ID.
- **Parâmetros**: `id` (Long) - ID da paragem
- **Resposta**: `ParagemDTO`

#### POST `/api/paragem`
Cria uma nova paragem.
- **Body**: `ParagemDTO`
- **Resposta**: `ParagemDTO` criada
- **Status**: 201 (Created), 409 (Conflict se coordenadas já existem)

#### PUT `/api/paragem/{id}`
Atualiza uma paragem existente.
- **Parâmetros**: `id` (Long) - ID da paragem
- **Body**: `ParagemDTO`
- **Resposta**: `ParagemDTO` atualizada

#### DELETE `/api/paragem/{id}`
Remove uma paragem.
- **Parâmetros**: `id` (Long) - ID da paragem
- **Status**: 204 (No Content), 409 (Conflict se paragem tem rotas)

#### GET `/api/paragem/categoria/{categoriaId}`
Busca paragens por categoria.
- **Parâmetros**: `categoriaId` (Long) - ID da categoria
- **Resposta**: Lista de `ParagemDTO`

#### GET `/api/paragem/categoria/nome/{nomeCategoria}`
Busca paragens por nome da categoria.
- **Parâmetros**: `nomeCategoria` (String) - Nome da categoria
- **Resposta**: Lista de `ParagemDTO`

#### GET `/api/paragem/area?latMin={latMin}&latMax={latMax}&longMin={longMin}&longMax={longMax}`
Busca paragens em uma área específica (bounding box).
- **Parâmetros Query**: 
  - `latMin` (BigDecimal) - Latitude mínima
  - `latMax` (BigDecimal) - Latitude máxima
  - `longMin` (BigDecimal) - Longitude mínima
  - `longMax` (BigDecimal) - Longitude máxima
- **Resposta**: Lista de `ParagemDTO`

#### GET `/api/paragem/proximas?latitude={lat}&longitude={long}&raio={raio}`
Busca paragens próximas de uma coordenada.
- **Parâmetros Query**: 
  - `latitude` (BigDecimal) - Latitude de referência
  - `longitude` (BigDecimal) - Longitude de referência
  - `raio` (BigDecimal) - Raio de busca (padrão: 0.01)
- **Resposta**: Lista de `ParagemDTO`

#### GET `/api/paragem/conectadas`
Busca paragens que têm rotas (conectadas).
- **Resposta**: Lista de `ParagemDTO`

#### GET `/api/paragem/isoladas`
Busca paragens sem rotas (isoladas).
- **Resposta**: Lista de `ParagemDTO`

#### GET `/api/paragem/origem-rotas`
Busca paragens que são origem de rotas.
- **Resposta**: Lista de `ParagemDTO`

#### GET `/api/paragem/destino-rotas`
Busca paragens que são destino de rotas.
- **Resposta**: Lista de `ParagemDTO`

#### GET `/api/paragem/estatisticas`
Obtém estatísticas de paragens por categoria.
- **Resposta**: Lista de arrays com [categoria, contagem]

#### GET `/api/paragem/categoria/{categoriaId}/com-rotas`
Busca paragens por categoria com contagem de rotas.
- **Parâmetros**: `categoriaId` (Long) - ID da categoria
- **Resposta**: Lista de arrays com [paragem, rotasOrigem, rotasDestino]

#### GET `/api/paragem/verificar-coordenadas?latitude={lat}&longitude={long}`
Verifica se coordenadas já existem.
- **Parâmetros Query**: 
  - `latitude` (BigDecimal)
  - `longitude` (BigDecimal)
- **Resposta**: Boolean

### 4. Rota (`/api/rota`)

#### GET `/api/rota`
Lista todas as rotas.
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/{origemId}/{destinoId}`
Busca rota específica entre duas paragens.
- **Parâmetros**: 
  - `origemId` (Long) - ID da paragem de origem
  - `destinoId` (Long) - ID da paragem de destino
- **Resposta**: `RotaDTO`

#### POST `/api/rota`
Cria uma nova rota.
- **Body**: `RotaDTO`
- **Resposta**: `RotaDTO` criada
- **Status**: 201 (Created), 409 (Conflict se rota já existe)

#### PUT `/api/rota/{origemId}/{destinoId}`
Atualiza uma rota existente.
- **Parâmetros**: 
  - `origemId` (Long) - ID da paragem de origem
  - `destinoId` (Long) - ID da paragem de destino
- **Body**: `RotaDTO`
- **Resposta**: `RotaDTO` atualizada

#### DELETE `/api/rota/{origemId}/{destinoId}`
Remove uma rota.
- **Parâmetros**: 
  - `origemId` (Long) - ID da paragem de origem
  - `destinoId` (Long) - ID da paragem de destino
- **Status**: 204 (No Content)

#### GET `/api/rota/origem/{origemId}`
Busca rotas por paragem de origem.
- **Parâmetros**: `origemId` (Long) - ID da paragem de origem
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/destino/{destinoId}`
Busca rotas por paragem de destino.
- **Parâmetros**: `destinoId` (Long) - ID da paragem de destino
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/paragem/{paragemId}`
Busca todas as rotas de uma paragem (origem e destino).
- **Parâmetros**: `paragemId` (Long) - ID da paragem
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/preco?precoMin={min}&precoMax={max}`
Busca rotas por faixa de preço.
- **Parâmetros Query**: 
  - `precoMin` (BigDecimal) - Preço mínimo
  - `precoMax` (BigDecimal) - Preço máximo
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/distancia?distanciaMin={min}&distanciaMax={max}`
Busca rotas por faixa de distância.
- **Parâmetros Query**: 
  - `distanciaMin` (BigDecimal) - Distância mínima
  - `distanciaMax` (BigDecimal) - Distância máxima
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/mais-baratas`
Busca rotas ordenadas por preço crescente.
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/mais-curtas`
Busca rotas ordenadas por distância crescente.
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/melhor-custo-beneficio`
Busca rotas com melhor custo-benefício (menor preço por km).
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/categoria-origem/{categoriaId}`
Busca rotas por categoria de origem.
- **Parâmetros**: `categoriaId` (Long) - ID da categoria
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/categoria-destino/{categoriaId}`
Busca rotas por categoria de destino.
- **Parâmetros**: `categoriaId` (Long) - ID da categoria
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/categorias/{categoriaOrigemId}/{categoriaDestinoId}`
Busca rotas entre categorias específicas.
- **Parâmetros**: 
  - `categoriaOrigemId` (Long) - ID da categoria de origem
  - `categoriaDestinoId` (Long) - ID da categoria de destino
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/destinos-disponiveis/{origemId}`
Busca destinos disponíveis a partir de uma origem.
- **Parâmetros**: `origemId` (Long) - ID da paragem de origem
- **Resposta**: Lista de `Paragem`

#### GET `/api/rota/origens-disponiveis/{destinoId}`
Busca origens que chegam a um destino.
- **Parâmetros**: `destinoId` (Long) - ID da paragem de destino
- **Resposta**: Lista de `Paragem`

#### GET `/api/rota/diretas/{paragem1Id}/{paragem2Id}`
Busca rotas diretas entre duas paragens.
- **Parâmetros**: 
  - `paragem1Id` (Long) - ID da primeira paragem
  - `paragem2Id` (Long) - ID da segunda paragem
- **Resposta**: Lista de `RotaDTO`

#### GET `/api/rota/estatisticas/precos`
Obtém estatísticas de preços (min, max, média).
- **Resposta**: Array com [precoMin, precoMax, precoMedio]

#### GET `/api/rota/estatisticas/distancias`
Obtém estatísticas de distâncias (min, max, média).
- **Resposta**: Array com [distanciaMin, distanciaMax, distanciaMedia]

#### GET `/api/rota/count/origem`
Conta rotas por paragem de origem.
- **Resposta**: Lista de arrays com [paragemId, contagem]

#### GET `/api/rota/count/destino`
Conta rotas por paragem de destino.
- **Resposta**: Lista de arrays com [paragemId, contagem]

#### GET `/api/rota/verificar/{origemId}/{destinoId}`
Verifica se existe rota entre duas paragens.
- **Parâmetros**: 
  - `origemId` (Long) - ID da paragem de origem
  - `destinoId` (Long) - ID da paragem de destino
- **Resposta**: Boolean

#### GET `/api/rota/conexoes`
Busca rotas com conexões (que compartilham paragens).
- **Resposta**: Lista de arrays com [rota1, rota2]

## DTOs (Data Transfer Objects)

### CategoriaParagemDTO
```json
{
  "id": 1,
  "nome": "Terminal",
  "descricao": "Terminal de transporte público"
}
```

### CategoriaVeiculoDTO
```json
{
  "id": 1,
  "nome": "Ônibus"
}
```

### ParagemDTO
```json
{
  "id": 1,
  "latitude": -23.550520,
  "longitude": -46.633308,
  "categoriaParagemId": 1,
  "categoriaParagemNome": "Terminal"
}
```

### RotaDTO
```json
{
  "paragemOrigemId": 1,
  "paragemDestinoId": 2,
  "preco": 5.50,
  "distancia": 10.5,
  "paragemOrigemLatitude": -23.550520,
  "paragemOrigemLongitude": -46.633308,
  "paragemDestinoLatitude": -23.560520,
  "paragemDestinoLongitude": -46.643308,
  "paragemOrigemCategoria": "Terminal",
  "paragemDestinoCategoria": "Ponto de Ônibus"
}
```

## Códigos de Status HTTP

- **200 OK**: Operação bem-sucedida
- **201 Created**: Recurso criado com sucesso
- **204 No Content**: Recurso deletado com sucesso
- **400 Bad Request**: Dados de entrada inválidos
- **404 Not Found**: Recurso não encontrado
- **409 Conflict**: Conflito (ex: nome já existe, coordenadas já existem)
- **500 Internal Server Error**: Erro interno do servidor

## Validações

### CategoriaParagemDTO
- `nome`: Obrigatório, máximo 100 caracteres
- `descricao`: Opcional, máximo 255 caracteres

### CategoriaVeiculoDTO
- `nome`: Obrigatório, máximo 100 caracteres

### ParagemDTO
- `latitude`: Obrigatória, entre -90 e 90
- `longitude`: Obrigatória, entre -180 e 180
- `categoriaParagemId`: Obrigatório, deve existir

### RotaDTO
- `paragemOrigemId`: Obrigatório, deve existir
- `paragemDestinoId`: Obrigatório, deve existir e ser diferente da origem
- `preco`: Obrigatório, maior que zero
- `distancia`: Obrigatória, positiva

## Configuração CORS

Todas as APIs estão configuradas com `@CrossOrigin(origins = "*")` para permitir acesso de qualquer origem durante o desenvolvimento.

## Base URL

Todas as URLs são relativas a: `http://localhost:8080`

## Exemplos de Uso

### Criar uma categoria de paragem:
```bash
POST /api/categoria-paragem
Content-Type: application/json

{
  "nome": "Terminal Rodoviário",
  "descricao": "Terminal principal da cidade"
}
```

### Criar uma paragem:
```bash
POST /api/paragem
Content-Type: application/json

{
  "latitude": -23.550520,
  "longitude": -46.633308,
  "categoriaParagemId": 1
}
```

### Criar uma rota:
```bash
POST /api/rota
Content-Type: application/json

{
  "paragemOrigemId": 1,
  "paragemDestinoId": 2,
  "preco": 5.50,
  "distancia": 10.5
}
```
