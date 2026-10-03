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
| `ADMIN_EMAIL` / `ADMIN_PASSWORD` | empty | Administrator account created on startup when both are set |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:4200,http://localhost:5173,http://localhost:3000` | Comma-separated allowed origins |

## API

All business endpoints live under `/api/v1`. Protected endpoints expect `Authorization: Bearer <token>`.

| Method | Path | Access | Description |
|---|---|---|---|
| GET | `/actuator/health` | Public | Health check |
| GET | `/v3/api-docs` | Public | OpenAPI document |
| GET | `/swagger-ui.html` | Public | Swagger UI |
| POST | `/api/v1/auth/register` | Public | Register a passenger account (`201`, `409` duplicate email) |
| POST | `/api/v1/auth/login` | Public | Log in, returns `{accessToken, tokenType, expiresIn}` (`401` bad credentials) |
| GET | `/api/v1/users/me` | Authenticated | Profile of the current user |
| POST | `/api/v1/users/me/driver-profile` | Authenticated | Register as a driver: role becomes `DRIVER`, status `PENDING_VERIFICATION` (`409` already a driver or license taken) |
| GET | `/api/v1/users/me/driver-profile` | Authenticated | Driver profile (`404` if not a driver) |
| POST | `/api/v1/drivers/me/documents` | DRIVER | Upload a document (multipart `type` + `file`; PDF/JPEG/PNG up to 5 MB) |
| GET | `/api/v1/drivers/me/documents` | DRIVER | List my documents |
| GET | `/api/v1/drivers/me/documents/{id}` | DRIVER | One of my documents (`404` if not mine) |
| PATCH | `/api/v1/driver-documents/{id}/review` | ADMIN | `{decision: APPROVED\|REJECTED, reason}`; `reason` is required to reject |
| POST | `/api/v1/drivers/me/vehicles` | DRIVER | Register a vehicle (`409` plate taken); verification is not required |
| GET | `/api/v1/drivers/me/vehicles` | DRIVER | List my vehicles |
| GET | `/api/v1/drivers/me/vehicles/{id}` | DRIVER | One of my vehicles (`404` if not mine) |
| PUT | `/api/v1/drivers/me/vehicles/{id}` | DRIVER | Update one of my vehicles (`404` if not mine, `409` plate taken) |

### Driver verification

Document types: `DRIVERS_LICENSE`, `NATIONAL_ID`, `CRIMINAL_RECORD`, `VEHICLE_REGISTRATION`; statuses: `PENDING`, `APPROVED`, `REJECTED`.
A file is accepted only if its declared content type is PDF, JPEG or PNG **and** its first bytes match that format.
Files are stored under `STORAGE_PATH` with a random UUID name.

When `DRIVERS_LICENSE`, `NATIONAL_ID` and `CRIMINAL_RECORD` are all approved, the driver becomes `VERIFIED` and a `DriverVerified`
domain event is published. Rejecting a document publishes `DocumentRejected`. A document can be reviewed only once (`409` otherwise);
the driver uploads a new one instead. Events are published through Spring application events and, for now, only logged.

### Vehicles

Body: `{plate, brand, model, year, color, seats}`. The plate has 6 letters or digits with an optional hyphen and is stored
in the canonical form `ABC-123` (so `abc123` and `ABC-123` are the same plate). `year` goes from 2000 to next year and `seats` from 1 to 8.

### Administrators

There is no public endpoint to create administrators. Set `ADMIN_EMAIL` and `ADMIN_PASSWORD` to create one on startup
(nothing is created when they are empty).

### Authentication

Tokens are stateless JWTs (HS256) valid for 60 minutes. The filter reloads the user from the database on every request,
so a role change (for example PASSENGER to DRIVER) applies immediately without logging in again.
Passwords are stored with BCrypt and must have 8 to 72 characters with at least one letter and one number.

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
