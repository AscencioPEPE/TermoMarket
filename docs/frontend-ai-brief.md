# Frontend AI Brief

Use this brief with the React-focused AI instance.

## Project Goal

Build a professional e-commerce frontend for tumbler sales in Mexico.
The frontend consumes the Spring Boot backend of `ThermoMarket API`.

This is not a generic store mockup.
It should feel like a serious product portfolio piece with strong UX and a believable shopping flow.

## Frontend Scope

### Public Store

- home page
- product catalog
- product detail page
- search and filters
- shopping cart
- checkout form
- order confirmation page

### Optional Admin Later

- product management
- customer order review
- order status tracking

## Tech Direction

- React
- Vite
- TypeScript
- React Router
- TanStack Query
- Zustand or Context for cart state
- Tailwind CSS or a well-structured CSS approach

## Design Direction

The UI should feel modern and intentional, not like a default template.

- clean product cards
- strong typography
- warm industrial brand tone
- ecommerce visuals that fit tumblers and physical products
- responsive mobile-first layout

Avoid:

- generic admin-looking colors
- random gradients with no brand logic
- overdesigned animations
- boilerplate landing page sections

## Backend Contract Available Now

### Products

- `GET /api/products`
  Supports:
  - `limit`
  - `cursor`
  - `search`
  - `active`

- `GET /api/products/{id}`
- `POST /api/products`
- `PUT /api/products/{id}`

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

## Example Product Catalog Response

```json
{
  "items": [
    {
      "id": 12,
      "name": "Termo Negro 30 oz",
      "slug": "termo-negro-30-oz",
      "description": "Termo de acero inoxidable",
      "price": 349.99,
      "stock": 20,
      "active": true,
      "createdAt": "2026-03-29T18:00:00Z",
      "updatedAt": "2026-03-29T18:00:00Z"
    }
  ],
  "nextCursor": "cHJvZHVjdDoxMg",
  "hasNext": true,
  "limit": 12
}
```

## Frontend Priorities

1. Create a clean API client layer.
2. Build catalog listing with cursor pagination.
3. Add product search.
4. Add product detail page.
5. Implement cart state.
6. Implement checkout form using customer plus address creation.
7. Submit orders to backend.

## Expected Folder Direction

- `src/app`
- `src/features/catalog`
- `src/features/product`
- `src/features/cart`
- `src/features/checkout`
- `src/features/orders`
- `src/shared`

## Instruction For The Frontend AI

Build the app in a modular and production-like way.
Prefer reusable UI and typed API contracts.
Assume the backend will continue evolving, so keep the API layer isolated.
Use cursor pagination for the product list instead of classic page numbers.
Do not generate fake backend logic when the API already exists.

## What To Build First

1. app shell and routing
2. product catalog page
3. product detail page
4. cart
5. checkout flow
