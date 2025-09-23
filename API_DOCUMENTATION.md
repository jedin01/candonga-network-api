# Candonga Network API - Documentação

Esta é a documentação da API REST para o sistema Candonga Network, desenvolvida em Laravel.

## Endpoints Disponíveis

### Bairros

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/bairros` | Lista todos os bairros |
| POST | `/api/bairros` | Cria um novo bairro |
| GET | `/api/bairros/{id}` | Mostra um bairro específico |
| PUT | `/api/bairros/{id}` | Atualiza um bairro |
| DELETE | `/api/bairros/{id}` | Remove um bairro |

**Campos necessários para criação:**
- `nome` (string, obrigatório, máx: 255)

### Paragens

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/paragens` | Lista todas as paragens |
| POST | `/api/paragens` | Cria uma nova paragem |
| GET | `/api/paragens/{id}` | Mostra uma paragem específica |
| PUT | `/api/paragens/{id}` | Atualiza uma paragem |
| DELETE | `/api/paragens/{id}` | Remove uma paragem |

**Campos necessários para criação:**
- `nome` (string, obrigatório, máx: 255)
- `latitude` (numeric, obrigatório)
- `longitude` (numeric, obrigatório)
- `id_bairro` (integer, obrigatório)

### Rotas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/rotas` | Lista todas as rotas |
| POST | `/api/rotas` | Cria uma nova rota |
| GET | `/api/rotas/{id}` | Mostra uma rota específica |
| PUT | `/api/rotas/{id}` | Atualiza uma rota |
| DELETE | `/api/rotas/{id}` | Remove uma rota |

**Campos necessários para criação:**
- `origem_id` (integer, obrigatório, deve existir em paragens)
- `destino_id` (integer, obrigatório, deve existir em paragens, deve ser diferente da origem)
- `distancia` (numeric, obrigatório, mín: 0)
- `custo` (numeric, obrigatório, mín: 0)

### Categoria de Paragens

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/categoria-paragens` | Lista todas as categorias de paragem |
| POST | `/api/categoria-paragens` | Cria uma nova categoria de paragem |
| GET | `/api/categoria-paragens/{id}` | Mostra uma categoria de paragem específica |
| PUT | `/api/categoria-paragens/{id}` | Atualiza uma categoria de paragem |
| DELETE | `/api/categoria-paragens/{id}` | Remove uma categoria de paragem |

**Campos necessários para criação:**
- `nome` (string, obrigatório, máx: 255)
- `descricao` (string, opcional, máx: 500)

### Categoria de Veículos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/categoria-veiculos` | Lista todas as categorias de veículo |
| POST | `/api/categoria-veiculos` | Cria uma nova categoria de veículo |
| GET | `/api/categoria-veiculos/{id}` | Mostra uma categoria de veículo específica |
| PUT | `/api/categoria-veiculos/{id}` | Atualiza uma categoria de veículo |
| DELETE | `/api/categoria-veiculos/{id}` | Remove uma categoria de veículo |

**Campos necessários para criação:**
- `nome` (string, obrigatório, máx: 255)

## Formatos de Resposta

### Sucesso
```json
{
  "id": 1,
  "nome": "Nome do recurso",
  "created_at": "2024-01-01T00:00:00.000000Z",
  "updated_at": "2024-01-01T00:00:00.000000Z"
}
```

### Erro 404 - Recurso não encontrado
```json
{
  "message": "Recurso não encontrado"
}
```

### Erro 422 - Validação
```json
{
  "message": "The given data was invalid.",
  "errors": {
    "campo": [
      "Mensagem de erro específica"
    ]
  }
}
```

## Códigos de Status HTTP

- `200` - OK (Sucesso)
- `201` - Created (Recurso criado com sucesso)
- `404` - Not Found (Recurso não encontrado)
- `422` - Unprocessable Entity (Erro de validação)
- `500` - Internal Server Error (Erro interno do servidor)

## Exemplos de Uso

### Criar uma nova paragem
```bash
POST /api/paragens
Content-Type: application/json

{
  "nome": "Paragem Central",
  "latitude": -8.838333,
  "longitude": 13.234444,
  "id_bairro": 1
}
```

### Criar uma nova rota
```bash
POST /api/rotas
Content-Type: application/json

{
  "origem_id": 1,
  "destino_id": 2,
  "distancia": 5.5,
  "custo": 150.00
}
```

### Listar todas as rotas com relacionamentos
```bash
GET /api/rotas
```

Resposta:
```json
[
  {
    "id": 1,
    "origem_id": 1,
    "destino_id": 2,
    "distancia": 5.5,
    "custo": 150.00,
    "created_at": "2024-01-01T00:00:00.000000Z",
    "updated_at": "2024-01-01T00:00:00.000000Z",
    "origem": {
      "id": 1,
      "nome": "Paragem A",
      "latitude": -8.838333,
      "longitude": 13.234444,
      "id_bairro": 1
    },
    "destino": {
      "id": 2,
      "nome": "Paragem B", 
      "latitude": -8.848333,
      "longitude": 13.244444,
      "id_bairro": 2
    }
  }
]
```

## Observações

- Todos os endpoints retornam JSON
- Para atualizações (PUT), todos os campos são opcionais (usam validação `sometimes`)
- As rotas incluem os relacionamentos `origem` e `destino` quando listadas ou exibidas
- IDs são auto-incrementais
- Timestamps `created_at` e `updated_at` são gerenciados automaticamente pelo Laravel