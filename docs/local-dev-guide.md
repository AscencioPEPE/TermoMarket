# Local Development Guide

## Recommended Local Setup

Use Docker for MySQL and run Spring Boot from your IDE or terminal.

Why this setup is better:

- reproducible local database
- no dependency on a fragile local MySQL installation
- same database name and credentials for the whole team
- easy reset when migrations change

## Start MySQL With Docker

From the project root:

```powershell
docker compose up -d
```

Check container status:

```powershell
docker compose ps
```

Stop the database:

```powershell
docker compose down
```

Stop and remove volume data:

```powershell
docker compose down -v
```

## Default Docker Credentials

- host: `localhost`
- port: `3307`
- database: `termos`
- user: `thermo_app`
- password: `ThermoApp99!`
- root password: `Ascencio99`

## Run The Backend

Using environment variables in PowerShell:

```powershell
$env:APP_PROFILE='local'
$env:DB_HOST='localhost'
$env:DB_PORT='3307'
$env:DB_NAME='termos'
$env:DB_USERNAME='thermo_app'
$env:DB_PASSWORD='ThermoApp99!'
mvn spring-boot:run
```

If you prefer `cmd`:

```bat
set APP_PROFILE=local
set DB_HOST=localhost
set DB_PORT=3307
set DB_NAME=termos
set DB_USERNAME=thermo_app
set DB_PASSWORD=ThermoApp99!
mvn spring-boot:run
```

## Verify It Is Running

- API base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Suggested Next Backend Steps

1. Add seed data for products.
2. Add JWT authentication and roles.
3. Add cart module.
4. Add order status updates.
5. Add shipping entity and tracking fields.
6. Add integration tests for products and orders.

## Reset Database Cleanly

When you change migrations during early development:

```powershell
docker compose down -v
docker compose up -d
```

Then run the backend again and let Flyway recreate the schema.
