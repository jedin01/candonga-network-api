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

---

## Overview

The **Candonga Network API** is a RESTful web service built with Laravel for managing urban transportation networks. It provides comprehensive functionality for managing districts, transport stops, routes, and categorization systems for both stops and vehicles.

---

## Key Features

<div>

<table>
<tr>
<th width="50%">Districts & Stops</th>
<th width="50%">Routes & Categories</th>
</tr>
<tr>
<td>

- **District Management**
  - Full CRUD for city districts  
  - Hierarchical organization of areas  

- **Transport Stops**
  - Geospatial management (latitude/longitude)  
  - Linked to districts  

</td>
<td>

- **Route Planning**
  - Define connections between stops  
  - Distance & cost calculations  

- **Categorization**
  - Stop categories (e.g., terminal, secondary)  
  - Vehicle categories (e.g., minibus, taxi)  

</td>
</tr>
</table>

</div>

---

## Quick Start

### Prerequisites

<div>

<table>
<tr>
<th>Docker (Recommended)</th>
<th>Local Installation</th>
</tr>
<tr>
<td align="left">

- [Docker](https://docs.docker.com/get-docker/)  
- [Docker Compose](https://docs.docker.com/compose/install/)  

</td>
<td align="left">

- PHP **8.2+**  
- Composer  
- Node.js **18+**  
- MySQL **8.0+**  

</td>
</tr>
</table>

</div>

---

## API Documentation

### Base URLs

<div>

| Environment | URL |
|-------------|-----|
| **Local** | `http://localhost:8000/api` |
| **Docker** | `http://localhost/api` |

</div>

### Available Resources

<div align="center">

| Resource | Endpoint | Methods | Description |
|----------|----------|---------|-------------|
| **Districts** | `/api/bairros` | `GET, POST, PUT, DELETE` | Manage city districts |
| **Stops** | `/api/paragens` | `GET, POST, PUT, DELETE` | Manage transport stops |
| **Routes** | `/api/rotas` | `GET, POST, PUT, DELETE` | Define connections between stops |
| **Stop Categories** | `/api/categoria-paragens` | `GET, POST, PUT, DELETE` | Classify transport stops |
| **Vehicle Categories** | `/api/categoria-veiculos` | `GET, POST, PUT, DELETE` | Classify vehicles |

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
│   └── api.php                   # API Routes
├── tests/                        # Automated Tests
├── compose.yaml                  # Docker Compose Config
└── README.md                     # Documentation

````

### Core Models

<div>

| Model | Description |
|-------|-------------|
| **Bairro** | Represents districts (with multiple stops) |
| **Paragem** | Transport stop with geospatial data |
| **Rota** | Route connection between two stops |
| **CategoriaParagem** | Categorization of stops |
| **CategoriaVeiculo** | Categorization of vehicles |

</div>

---

## 🛠 Development

### Available Scripts

<div align="center">

<table>
<tr>
<th>Composer</th>
<th>NPM</th>
<th>Artisan</th>
</tr>
<tr>
<td align="left">

```bash
# Development environment
composer run dev

# Run tests
composer run test
````

</td>
<td align="left">

```bash
# Frontend (Vite) development
npm run dev

# Production build
npm run build
```

</td>
<td align="left">

```bash
# Generate key
php artisan key:generate

# Run migrations
php artisan migrate

# Tail logs
php artisan pail
```

</td>
</tr>
</table>

</div>

---

## Contributing

<div>

**Fork → Branch → Code → Test → Pull Request**

</div>

### Code Standards

* Follow **Laravel conventions**
* Use **Laravel Pint** for formatting
* Write tests for new features
* Keep documentation updated
* Maintain backward compatibility

---

## 📖 Support

<div align="center">

| Resource          | Link                                        |
| ----------------- | ------------------------------------------- |
| **Documentation** | [API Documentation](./API_DOCUMENTATION.md) |
| **Issues**        | [GitHub Issues](../../issues)               |
| **Discussions**   | [GitHub Discussions](../../discussions)     |

</div>

---

## License

<div>

Licensed under the **MIT License** – see [LICENSE](LICENSE) for details.

</div>

---

<div align="center">

<h3>Built with Laravel Framework</h3>
<p><em>A robust foundation for urban transport management</em></p>

**⭐ If this project helped you, give it a star!**

</div>
