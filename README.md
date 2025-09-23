<div align="center">

<h1>Candonga Network API</h1>

<p><strong>A comprehensive urban transport network management system</strong></p>

<p>
  <a href="https://laravel.com/"><img src="https://img.shields.io/badge/Laravel-12.x-FF2D20?style=flat-square&logo=laravel&logoColor=white" alt="Laravel"></a>
  <a href="https://php.net/"><img src="https://img.shields.io/badge/PHP-8.2+-777BB4?style=flat-square&logo=php&logoColor=white" alt="PHP"></a>
  <a href="https://mysql.com/"><img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white" alt="MySQL"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-green.svg?style=flat-square" alt="License"></a>
</p>

</div>

## Overview

The **Candonga Network API** is a RESTful web service built with Laravel for managing urban transportation networks. It provides comprehensive functionality for managing districts, transport stops, routes, and categorization systems for both stops and vehicles.

### Key Features

<div align="center">

<table>
<tr>
<td width="50%">

**District Management**
- Complete CRUD operations for city districts
- Hierarchical organization of urban areas

**Transport Stops**
- Geospatial management with coordinates
- Integration with district mapping

</td>
<td width="50%">

**Route Planning**
- Define routes between stops
- Distance and cost calculations

**Categorization System**
- Flexible categorization for stops
- Vehicle classification management

</td>
</tr>
</table>

</div>

---

## Technology Stack

<div align="center">

### Core Framework
**Laravel 12.x** • **PHP 8.2+** • **MySQL 8.0**

### Authentication & Security
**Laravel Sanctum** • **Laravel Policies**

### Development Tools
**Laravel Sail** • **Vite** • **Tailwind CSS** • **PHPUnit** • **Laravel Pint**

</div>

---

## Quick Start

### Prerequisites

<div align="center">

<table>
<tr>
<th>Docker (Recommended)</th>
<th>Local Installation</th>
</tr>
<tr>
<td>

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

</td>
<td>

- PHP 8.2 or higher
- Composer
- Node.js 18 or higher
- MySQL 8.0 or higher

</td>
</tr>
</table>

</div>

### Installation

<details>
<summary><strong>Using Docker (Click to expand)</strong></summary>

```bash
# Clone the repository
git clone <repository-url>
cd candonga-network-api

# Copy environment file
cp .env.example .env

# Start Docker containers
./vendor/bin/sail up -d

# Run database migrations
./vendor/bin/sail artisan migrate

# Generate application key
./vendor/bin/sail artisan key:generate
```

</details>

<details>
<summary><strong>Local Installation (Click to expand)</strong></summary>

```bash
# Clone and install dependencies
git clone <repository-url>
cd candonga-network-api
composer install
npm install

# Environment setup
cp .env.example .env
php artisan key:generate

# Database setup (configure .env first)
php artisan migrate

# Start development server
php artisan serve
```

</details>

### Development Workflow

<div align="center">

For rapid development, use the integrated development command:

```bash
composer run dev
```

This command concurrently runs:
**Laravel development server** • **Queue worker** • **Real-time logs** • **Vite development server**

</div>

---

## API Documentation

### Base URL

<div align="center">

| Environment | URL |
|-------------|-----|
| **Local Development** | `http://localhost:8000/api` |
| **Docker Environment** | `http://localhost/api` |

</div>

### Available Resources

<div align="center">

| Resource | Endpoint | Description |
|----------|----------|-------------|
| **Districts** | `/api/bairros` | District management |
| **Stops** | `/api/paragens` | Transport stop management |
| **Routes** | `/api/rotas` | Route management |
| **Stop Categories** | `/api/categoria-paragens` | Stop categorization |
| **Vehicle Categories** | `/api/categoria-veiculos` | Vehicle categorization |

</div>

### HTTP Methods

<div align="center">

All resources support standard REST operations:

`GET` • `POST` • `PUT` • `DELETE`

</div>

### Example Requests

<details>
<summary><strong>Create a Transport Stop</strong></summary>

```http
POST /api/paragens
Content-Type: application/json

{
  "nome": "Central Terminal",
  "latitude": -8.838333,
  "longitude": 13.234444,
  "id_bairro": 1
}
```

</details>

<details>
<summary><strong>Create a Route</strong></summary>

```http
POST /api/rotas
Content-Type: application/json

{
  "origem_id": 1,
  "destino_id": 2,
  "distancia": 5.5,
  "custo": 150.00
}
```

</details>

<div align="center">

> **📖 Complete Documentation**  
> For detailed API documentation, see [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)

</div>

---

## Project Structure

```
candonga-network-api/
├── app/
│   ├── Http/Controllers/Api/     # API Controllers
│   ├── Models/                   # Eloquent Models
│   └── Policies/                 # Authorization Policies
├── database/
│   ├── factories/                # Model Factories
│   ├── migrations/               # Database Migrations
│   └── seeders/                  # Database Seeders
├── routes/
│   └── api.php                   # API Routes Definition
├── tests/                        # Automated Tests
├── compose.yaml                  # Docker Compose Configuration
└── README.md                     # Project Documentation
```

### Core Models

<div align="center">

| Model | Purpose |
|-------|---------|
| **Bairro** | City districts with associated stops |
| **Paragem** | Transport stops with geospatial data |
| **Rota** | Route connections between stops |
| **CategoriaParagem** | Stop categorization system |
| **CategoriaVeiculo** | Vehicle categorization system |

</div>

---

## Development

### Available Scripts

<div align="center">

<table>
<tr>
<th>Composer</th>
<th>NPM</th>
<th>Artisan</th>
</tr>
<tr>
<td>

```bash
# Full development
composer run dev

# Run tests
composer run test
```

</td>
<td>

```bash
# Frontend dev
npm run dev

# Production build
npm run build
```

</td>
<td>

```bash
# Generate key
php artisan key:generate

# Run migrations
php artisan migrate

# Real-time logs
php artisan pail
```

</td>
</tr>
</table>

</div>

### Testing

<details>
<summary><strong>Test Commands</strong></summary>

```bash
# Using Composer script
composer run test

# Direct PHPUnit execution
php artisan test

# With coverage report
php artisan test --coverage
```

</details>

### Docker Operations

<details>
<summary><strong>Sail Commands</strong></summary>

```bash
# Container management
./vendor/bin/sail up -d          # Start containers
./vendor/bin/sail down           # Stop containers
./vendor/bin/sail shell          # Access container

# Application commands
./vendor/bin/sail artisan <command>    # Run Artisan
./vendor/bin/sail logs                 # View logs
```

<div align="center">

**Service Ports:** Application (80) • MySQL (3306) • Vite (5173)

</div>

</details>

---

## Contributing

<div align="center">

We welcome contributions to improve the Candonga Network API.

### Development Process

**Fork** → **Branch** → **Code** → **Test** → **Pull Request**

</div>

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Follow** Laravel conventions and add comprehensive tests
4. **Commit** your changes (`git commit -m 'Add amazing feature'`)
5. **Push** to the branch (`git push origin feature/amazing-feature`)
6. **Open** a Pull Request

### Code Standards

<div align="center">

- Follow Laravel coding conventions
- Use Laravel Pint for code formatting  
- Write comprehensive tests for new features
- Update documentation as needed
- Maintain backward compatibility when possible

</div>

---

## Support

<div align="center">

| Resource | Link |
|----------|------|
| **Documentation** | [API Documentation](./API_DOCUMENTATION.md) |
| **Issues** | [Repository Issues](../../issues) |
| **Discussions** | [Repository Discussions](../../discussions) |

</div>

---

## License

<div align="center">

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

</div>

---

<div align="center">

<h3>Built with Laravel Framework</h3>
<p><em>A robust foundation for urban transport management</em></p>

**⭐ If this project helped you, please consider giving it a star**

</div>