# ShareWay - Driver Management Service

Driver Management microservice of **ShareWay**, a university platform for scheduled shared rides with verified drivers.
It covers user registration and login, driver registration and document verification, vehicles and driver availability.

## Tech stack

- Java 25, Spring Boot 4.1 (Spring Framework 7, Spring Security 7, Hibernate 7, Jackson 3)
- PostgreSQL 16 (H2 in memory for tests)
- JWT (jjwt), springdoc-openapi 3 (Swagger UI)
- Cucumber 7 + JUnit Platform, MockMvc

## Architecture

Layered DDD layout under `com.fundatech.shareway.drivermanagement`:

| Package | Responsibility |
|---|---|
| `interfaces.rest` | REST controllers and request/response DTOs (`record`s with Bean Validation) |
| `application` | Application services that orchestrate the use cases |
| `domain.model` | Aggregates, entities and enums |
| `domain.event` | Domain events |
| `domain.repository` | Ports the domain needs (repositories, storage) |
| `infrastructure.persistence` | Spring Data JPA repositories + adapters implementing the domain ports |
| `infrastructure.messaging` | Domain event publishing on top of Spring application events |
| `infrastructure.storage` | Local disk storage for driver documents |
| `infrastructure.security` | JWT issuing/validation and the authentication filter |
| `infrastructure.config` | Security, CORS and OpenAPI configuration |
| `shared.exception` | Global exception handler and shared exceptions |

> **Pragmatic shortcut:** domain entities carry JPA annotations instead of having separate persistence models.
> This keeps the code small; the domain still talks to persistence only through the ports in `domain.repository`.

## Running locally

Prerequisites: JDK 25 and Docker.

```bash
# 1. Start PostgreSQL (host port 5433)
docker compose up -d db

# 2. Run the service on http://localhost:8081
./mvnw spring-boot:run
```

Or run everything in containers:

```bash
docker compose up --build
```

Tests use the `test` profile with an in-memory H2 database, so they do **not** need Docker:

```bash
./mvnw verify
```

Swagger UI: http://localhost:8081/swagger-ui.html · OpenAPI document: http://localhost:8081/v3/api-docs

### Configuration

| Variable | Default | Description |
|---|---|---|
| `SERVER_PORT` | `8081` | HTTP port |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5433/driver_management_db` | JDBC URL |
| `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` | `shareway` / `shareway` | Database credentials |
| `JWT_SECRET` | development-only value | HMAC secret for JWTs (32+ characters). **Override it outside development.** |
| `STORAGE_PATH` | `./storage` | Directory for uploaded documents |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:4200,http://localhost:5173,http://localhost:3000` | Comma-separated allowed origins |

## API

All business endpoints live under `/api/v1`. Protected endpoints expect `Authorization: Bearer <token>`.

| Method | Path | Access | Description |
|---|---|---|---|
| GET | `/actuator/health` | Public | Health check |
| GET | `/v3/api-docs` | Public | OpenAPI document |
| GET | `/swagger-ui.html` | Public | Swagger UI |

### Error format

Every error uses the same body:

```json
{
  "timestamp": "2026-10-03T07:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/auth/register",
  "fieldErrors": [{ "field": "email", "message": "must be a well-formed email address" }]
}
```

| Status | When |
|---|---|
| 400 | Invalid input (Bean Validation or domain rule) |
| 401 | Missing, invalid or expired token; bad credentials |
| 403 | Authenticated but the role is not allowed |
| 404 | Resource not found or not owned by the caller |
| 409 | Duplicate data or conflicting state |

## Git workflow (GitFlow)

- `main`: base project only.
- `develop`: integration branch.
- `feature/<name>`: created from an up-to-date `develop`, merged back with `git merge --no-ff`, never deleted.
- `fix/<name>`: corrections after integration, same flow as features.

Feature branches, in order: `feature/project-setup`, `feature/user-auth`, `feature/us16-driver-registration`,
`feature/us17-document-upload`, `feature/us18-vehicle-registration`, `feature/us19-driver-availability`.

Every branch must pass `mvn -B clean verify` before it is merged. CI runs the same command on pushes and pull requests to `main` and `develop`.

## Commit convention

[Conventional Commits](https://www.conventionalcommits.org/) in English: `type(scope): description`, imperative mood, subject of 72 characters or less.
Types: `feat`, `fix`, `test`, `docs`, `chore`, `ci`, `build`, `refactor`.
