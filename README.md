# ThermoMarket API

ThermoMarket API is a Spring Boot backend for a tumbler-focused e-commerce platform.

The system is designed around a practical catalog and order workflow:

- product catalog with structured product attributes
- customer and address management
- order creation with product snapshot data
- product image management backed by Amazon S3
- cursor-based catalog navigation for frontend consumption

## Overview

The current implementation covers the core backend foundation required for an online tumbler store:

- product management
- customer management
- shipping address management
- order creation
- inventory-aware product selection
- product image registration and storage integration

The catalog is modeled with product-facing attributes that are useful for filtering and merchandising, including brand, category, color, material, capacity, and image metadata.

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Security
- Flyway
- MySQL
- Docker Compose
- AWS S3
- OpenAPI / Swagger
- Maven

## Core Features

### Catalog

- cursor-based pagination
- text search
- filters by:
  - brand
  - category
  - color
  - capacity
- dynamic filter option endpoint
- primary image resolution per product

### Products

Each product currently includes:

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

### Customers

- customer registration
- customer lookup
- address registration
- address listing per customer

### Orders

- create order from selected products
- shipping address validation
- product snapshot persistence on order items
- stock validation during order creation

### Product Images

- generate presigned upload URLs for S3
- register image metadata after upload
- define primary image
- reorder images
- remove image metadata and object from S3

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

## API Surface

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

## Catalog Query Parameters

`GET /api/products` supports the following query parameters:

- `limit`
- `cursor`
- `search`
- `active`
- `brand`
- `category`
- `color`
- `capacityOz`

Examples:

```http
GET /api/products?limit=12
GET /api/products?limit=12&search=black
GET /api/products?limit=12&category=Termos
GET /api/products?limit=12&brand=Rustic%20Thermal
GET /api/products?limit=12&color=Negro
GET /api/products?limit=12&capacityOz=30
```

## Product Filter Options

The catalog also exposes a dedicated endpoint for filter options:

```http
GET /api/products/filters
```

Example response:

```json
{
  "brands": ["Rustic Thermal"],
  "categories": ["Termos"],
  "colors": ["Azul marino", "Blanco", "Negro"],
  "capacitiesOz": [20, 30, 40]
}
```

## Product Image Flow

Product images are not stored in MySQL as blobs.

The current flow is:

1. The client requests a presigned upload URL from the backend.
2. The backend generates a storage key and presigned S3 upload URL.
3. The client uploads the image directly to S3.
4. The backend stores image metadata in MySQL.
5. The product response resolves the primary image URL for catalog usage.

This keeps binary storage separate from relational metadata and makes the image pipeline easier to evolve.

## Local Development

### Start MySQL

```powershell
docker compose up -d
```

### Local Database Variables

```powershell
$env:APP_PROFILE='local'
$env:DB_HOST='localhost'
$env:DB_PORT='3307'
$env:DB_NAME='termos'
$env:DB_USERNAME='thermo_app'
$env:DB_PASSWORD='ThermoApp99!'
```

### Optional S3 Variables

```powershell
$env:AWS_ACCESS_KEY_ID='YOUR_ACCESS_KEY_ID'
$env:AWS_SECRET_ACCESS_KEY='YOUR_SECRET_ACCESS_KEY'
$env:S3_BUCKET_NAME='thermomarket-assets-dev'
$env:S3_REGION='us-east-1'
```

### Run the Application

```powershell
mvn spring-boot:run
```

### Swagger UI

- `http://localhost:8080/swagger-ui.html`

## Sample Product Payload

```json
{
  "name": "Black Premium Tumbler 40 oz",
  "slug": "black-premium-tumbler-40-oz",
  "description": "Premium 40 oz tumbler with side handle and extended thermal capacity.",
  "brand": "Rustic Thermal",
  "category": "Termos",
  "color": "Black",
  "material": "304 stainless steel",
  "capacityOz": 40,
  "imageAlt": "Black Premium Tumbler 40 oz",
  "price": 429.00,
  "stock": 18,
  "active": true
}
```

## Product Images and Formats

The image pipeline is intended for modern web delivery:

- WebP is recommended as the primary catalog format
- PNG can be used when source transparency is needed
- metadata is stored in the application database
- binary assets live in S3

This setup keeps the backend ready for future additions such as thumbnails, transformed image variants, or CDN delivery through CloudFront.

## Security Notes

- store AWS credentials in environment variables for local development
- use IAM roles for production workloads on AWS
- keep S3 buckets non-public by default
- use presigned URLs for controlled upload flows

## Seed Data

The project includes seed data for a sample tumbler catalog so the frontend can be integrated against realistic product data from the beginning.

## Reference Docs

- [Local Dev Guide](c:/Users/pepej/Desktop/InventoryService/docs/local-dev-guide.md)
- [Database Setup](c:/Users/pepej/Desktop/InventoryService/docs/database-setup.md)
- [S3 Setup](c:/Users/pepej/Desktop/InventoryService/docs/s3-setup.md)
- [Frontend AI Brief](c:/Users/pepej/Desktop/InventoryService/docs/frontend-ai-brief.md)

## Next Areas of Expansion

- shopping cart
- checkout flow
- JWT authentication and roles
- shipment lifecycle
- analytics and admin workflows

