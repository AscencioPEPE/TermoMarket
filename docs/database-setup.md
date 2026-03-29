# Database Setup

## Local

This project uses the `local` profile by default.

Expected local database:

- engine: MySQL
- database: `termos`
- host: `localhost`
- port: `3306`

Create the database:

```sql
CREATE DATABASE termos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Default local connection values:

- `DB_HOST=localhost`
- `DB_PORT=3306`
- `DB_NAME=termos`
- `DB_USERNAME=root`
- `DB_PASSWORD=`

Run locally:

```powershell
$env:APP_PROFILE='local'
mvn spring-boot:run
```

If your MySQL user or password is different:

```powershell
$env:DB_USERNAME='your_user'
$env:DB_PASSWORD='your_password'
$env:APP_PROFILE='local'
mvn spring-boot:run
```

## Production

Use the `prod` profile for AWS RDS MySQL.

Required variables:

- `PROD_DB_HOST`
- `PROD_DB_PORT`
- `PROD_DB_NAME`
- `PROD_DB_USERNAME`
- `PROD_DB_PASSWORD`

Example:

```powershell
$env:APP_PROFILE='prod'
$env:PROD_DB_HOST='your-rds-endpoint'
$env:PROD_DB_PORT='3306'
$env:PROD_DB_NAME='termos'
$env:PROD_DB_USERNAME='admin'
$env:PROD_DB_PASSWORD='secret'
mvn spring-boot:run
```
