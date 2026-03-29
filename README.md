# ThermoMarket API

Backend de e-commerce construido con Spring Boot para una tienda de termos en Mexico.

Este proyecto esta enfocado a portafolio profesional y a la vez modela un flujo real de negocio:

- catalogo de productos
- clientes y direcciones
- pedidos
- inventario
- imagenes de producto con S3
- filtros y paginacion para frontend

## Demo Scope

Actualmente el proyecto ya incluye:

- API REST con Spring Boot 3
- MySQL local con Docker
- migraciones con Flyway
- productos con atributos de catalogo real
- seed data de termos demo
- cursor pagination para catalogo
- filtros por marca, categoria, color y capacidad
- endpoint de opciones de filtro
- gestion de imagenes de producto con metadata en BD
- generacion de presigned URLs para subida a S3
- relacion entre ordenes y snapshot de producto

## Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Flyway
- MySQL
- Docker Compose
- AWS S3
- OpenAPI / Swagger
- Maven

## Why This Project Is Strong For Portfolio

No es un CRUD generico.

Este proyecto toca varias areas que suelen llamar la atencion en entrevistas:

- modelado de dominio de e-commerce
- arquitectura backend modular
- persistencia relacional
- migraciones versionadas
- paginacion orientada a catalogo
- integracion con almacenamiento cloud
- separacion entre metadata e imagenes reales
- base lista para integracion con frontend React

## Business Context

ThermoMarket API modela una tienda de termos donde los usuarios pueden:

- consultar catalogo
- filtrar productos
- ver detalle de producto
- crear clientes y direcciones
- generar pedidos

Desde administracion, el sistema esta preparado para:

- gestionar catalogo
- administrar imagenes de producto
- enriquecer el inventario
- evolucionar hacia checkout real y shipping

## Current Modules

### Catalog

- productos
- imagen principal por producto
- imagenes secundarias
- filtros dinamicos
- cursor pagination

### Customers

- clientes
- direcciones

### Orders

- creacion de pedidos
- snapshot de producto al momento de compra
- estados base de orden

### Shared

- excepciones
- manejo global de errores
- entidades base con auditoria

### Storage

- configuracion S3
- presigned URLs

## Project Structure

```text
src/main/java/com/ascencio/thermomarket
├── catalog
├── config
├── customer
├── order
├── shared
└── storage
```

## Product Model

Los productos ya no son genericos. Actualmente incluyen:

- `name`
- `slug`
- `description`
- `brand`
- `category`
- `color`
- `material`
- `capacityOz`
- `imageAlt`
- `price`
- `stock`
- `active`
- `primaryImageUrl`

Esto permite que el frontend muestre un catalogo mucho mas realista desde el inicio.

## API Highlights

### Products

- `GET /api/products`
- `GET /api/products/all`
- `GET /api/products/filters`
- `GET /api/products/{id}`
- `POST /api/products`
- `PUT /api/products/{id}`

### Product Images

- `GET /api/products/{productId}/images`
- `POST /api/products/{productId}/images/presign`
- `POST /api/products/{productId}/images`
- `PUT /api/products/{productId}/images/{imageId}`
- `DELETE /api/products/{productId}/images/{imageId}`

### Customers

- `GET /api/customers`
- `GET /api/customers/{id}`
- `POST /api/customers`
- `GET /api/customers/{customerId}/addresses`
- `POST /api/customers/{customerId}/addresses`

### Orders

- `GET /api/orders`
- `GET /api/orders/{id}`
- `POST /api/orders`

## Catalog Query Features

El endpoint principal de catalogo soporta:

- `limit`
- `cursor`
- `search`
- `active`
- `brand`
- `category`
- `color`
- `capacityOz`

Ejemplos:

```http
GET /api/products?limit=12
GET /api/products?limit=12&search=negro
GET /api/products?limit=12&category=Termos
GET /api/products?limit=12&brand=Rustic%20Thermal
GET /api/products?limit=12&color=Negro
GET /api/products?limit=12&capacityOz=30
```

## Product Image Flow

Las imagenes no se guardan como blobs en MySQL.

El flujo implementado es:

1. El frontend pide una URL firmada al backend.
2. El backend genera un `storageKey` y una `uploadUrl`.
3. El frontend sube el archivo directamente a S3.
4. El backend registra la metadata en MySQL.
5. El catalogo devuelve `primaryImageUrl` para mostrar la principal.

Esto hace que el sistema sea mucho mas escalable y limpio.

## Local Development

### 1. Start MySQL With Docker

```powershell
docker compose up -d
```

### 2. Set Environment Variables

PowerShell:

```powershell
$env:APP_PROFILE='local'
$env:DB_HOST='localhost'
$env:DB_PORT='3307'
$env:DB_NAME='termos'
$env:DB_USERNAME='thermo_app'
$env:DB_PASSWORD='ThermoApp99!'
```

### 3. Optional S3 Variables For Local Image Upload Flow

```powershell
$env:AWS_ACCESS_KEY_ID='YOUR_ACCESS_KEY_ID'
$env:AWS_SECRET_ACCESS_KEY='YOUR_SECRET_ACCESS_KEY'
$env:S3_BUCKET_NAME='thermomarket-assets-dev'
$env:S3_REGION='us-east-1'
```

### 4. Run The API

```powershell
mvn spring-boot:run
```

### 5. Open Swagger

- `http://localhost:8080/swagger-ui.html`

## Important Local Docs

- [Local Dev Guide](c:/Users/pepej/Desktop/InventoryService/docs/local-dev-guide.md)
- [Database Setup](c:/Users/pepej/Desktop/InventoryService/docs/database-setup.md)
- [S3 Setup](c:/Users/pepej/Desktop/InventoryService/docs/s3-setup.md)
- [Frontend AI Brief](c:/Users/pepej/Desktop/InventoryService/docs/frontend-ai-brief.md)
- [Project Blueprint](c:/Users/pepej/Desktop/InventoryService/docs/portfolio-project-blueprint.md)

## Sample Product Request

```json
{
  "name": "Termo Negro Premium 40 oz",
  "slug": "termo-negro-premium-40-oz",
  "description": "Termo premium de 40 oz con asa lateral y mayor capacidad para trayectos largos.",
  "brand": "Rustic Thermal",
  "category": "Termos",
  "color": "Negro",
  "material": "Acero inoxidable 304",
  "capacityOz": 40,
  "imageAlt": "Termo Negro Premium 40 oz color negro",
  "price": 429.00,
  "stock": 18,
  "active": true
}
```

## Security Notes

- No guardar access keys de AWS dentro del repositorio.
- En local usar variables de entorno.
- En produccion usar IAM Role sobre EC2.
- Mantener el bucket S3 sin acceso publico directo.
- Usar presigned URLs para upload.

## Roadmap

### Short Term

- integrar frontend React con el catalogo actual
- agregar carrito
- agregar checkout
- incluir seed de clientes demo

### Medium Term

- JWT y roles
- tracking de orden
- shipping
- validaciones de stock mas finas
- tests de integracion

### Future

- CloudFront para servir imagenes
- order lifecycle mas completo
- panel admin
- analytics de ventas

## Frontend Direction

El frontend companion recomendado para este backend es:

- React
- Vite
- TypeScript
- React Router
- TanStack Query
- Zustand

El contrato backend ya esta pensado para ese flujo.

## Portfolio Pitch

`ThermoMarket API es un backend de e-commerce con Spring Boot para venta de termos, con catalogo paginado y filtrable, pedidos, clientes, MySQL con Docker, migraciones Flyway y gestion de imagenes en AWS S3 mediante presigned URLs.`

## Status

Proyecto en construccion activa, pero con base funcional real para:

- backend local reproducible
- integracion con frontend
- demo tecnica en portafolio

