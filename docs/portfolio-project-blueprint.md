# ThermoMarket API

## Vision

ThermoMarket API is a professional Spring Boot e-commerce backend focused on selling tumblers across Mexico.
It is designed as a portfolio project that looks like a real production-ready commercial platform instead of a basic CRUD demo.

## Project Positioning

This project should be presented as:

`Backend de e-commerce con Spring Boot para venta de termos, gestion de clientes, pedidos, inventario, pagos y envios nacionales en Mexico.`

The value for recruiters is not only the business idea. The value is the quality of the backend design:

- clean domain modeling
- secure authentication
- scalable module separation
- realistic order lifecycle
- shipping workflow
- API documentation
- automated testing

## Business Scope

The system manages the full backend lifecycle of an online tumbler store:

1. Product catalog
2. Customer accounts
3. Shopping cart
4. Order creation
5. Payment registration
6. Inventory updates
7. Shipping management
8. Admin reporting

## Main Modules

### 1. Auth

- user registration
- login
- JWT authentication
- roles: `ADMIN`, `CUSTOMER`

### 2. Customers

- customer profile
- shipping addresses
- purchase history

### 3. Catalog

- tumbler products
- variants by color, capacity, finish, or edition
- product images
- prices
- active or inactive products

### 4. Cart

- add products
- update quantities
- remove items
- calculate subtotal

### 5. Orders

- create order from cart
- order items snapshot
- order status lifecycle
- payment status

Suggested statuses:

- `PENDING`
- `PAID`
- `PREPARING`
- `SHIPPED`
- `DELIVERED`
- `CANCELLED`

### 6. Inventory

- available stock
- stock reservation
- stock discount after payment
- low stock alerts

### 7. Shipping

- customer shipping address
- shipping cost
- parcel service name
- tracking number
- shipping status

### 8. Admin

- sales dashboard
- top-selling products
- order monitoring
- customer metrics

## Recommended Tech Stack

### Core

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Validation
- PostgreSQL
- Maven

### Good Additions

- Flyway for migrations
- Lombok if you want to reduce boilerplate
- MapStruct for DTO mapping
- springdoc-openapi for Swagger UI
- Testcontainers for integration tests

## Recommended Architecture

Use a modular monolith structure.
It looks professional, scales well, and is perfect for portfolio and real business evolution.

Suggested package structure:

- `com.ascencio.thermomarket.auth`
- `com.ascencio.thermomarket.customer`
- `com.ascencio.thermomarket.catalog`
- `com.ascencio.thermomarket.cart`
- `com.ascencio.thermomarket.order`
- `com.ascencio.thermomarket.inventory`
- `com.ascencio.thermomarket.shipping`
- `com.ascencio.thermomarket.shared`

Inside each module:

- `controller`
- `service`
- `repository`
- `entity`
- `dto`
- `mapper`

## Suggested Domain Model

- `User`
- `Role`
- `Customer`
- `Address`
- `Product`
- `ProductVariant`
- `Cart`
- `CartItem`
- `Order`
- `OrderItem`
- `Payment`
- `InventoryItem`
- `Shipment`

## Professional Features That Add Value

- pagination and filtering for products
- search by product name or category
- role-based authorization
- exception handling with consistent API responses
- audit fields like `createdAt` and `updatedAt`
- stock validation before checkout
- Swagger documentation
- unit and integration tests

## MVP

Build this first:

1. Authentication with JWT
2. Product CRUD
3. Customer CRUD
4. Address CRUD
5. Cart endpoints
6. Order creation from cart
7. Inventory update on order
8. Shipment registration

## Version 2

After the MVP:

1. payment gateway simulation
2. sales analytics
3. email notifications
4. product ratings
5. discount coupons
6. order cancellation rules

## What Recruiters Will Notice

- It is a real business domain, not a school example.
- The API models real commercial workflows.
- Security, persistence, validation, and lifecycle rules are all visible.
- It is easy to explain in interviews because the flow is intuitive and concrete.

## Recommended Portfolio Pitch

`Desarrolle un backend de e-commerce con Spring Boot para una tienda de termos, incluyendo autenticacion JWT, catalogo de productos, carrito, pedidos, inventario y envios dentro de Mexico.`

## First Technical Milestone

The best first milestone is:

1. create the Spring Boot base project
2. configure PostgreSQL and Flyway
3. implement `Product`, `Customer`, `Address`, and `Order`
4. expose REST endpoints for catalog and orders
5. document the API with Swagger

## Important Direction

This project should now be treated as `Spring Boot only`.
The previous JavaFX or desktop direction is no longer part of the product vision.
