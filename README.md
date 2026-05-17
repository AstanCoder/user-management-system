# Contact List Management System

Hexagonal (DDD) contact list application: **Java 21 / Spring Boot** backend, **Next.js** frontend, **PostgreSQL** + **Liquibase**, runnable with **Docker Compose**.

Architecture reference: [Aarch.md](./Aarch.md)

## Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 21, Spring Boot 3.3, MapStruct, Lombok, springdoc-openapi |
| Frontend | Next.js 15, React 19, TypeScript, Vitest |
| Database | PostgreSQL 16, Liquibase 4.31.1 |
| Ops | Docker Compose |

## Quick start (Docker)

```bash
cp .env.example .env
docker compose -f docker/docker-compose.yml up --build
```

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000/contacts |
| Backend API | http://localhost:8080/api/contacts |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |

## Local development

### Prerequisites

- JDK 21
- Node.js 20+
- PostgreSQL 16 (or use Docker for postgres only)

### Backend

```bash
# Start Postgres (example)
docker run -d --name contacts-db -e POSTGRES_DB=contacts -e POSTGRES_USER=contacts -e POSTGRES_PASSWORD=contacts -p 5432:5432 postgres:16-alpine

export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/contacts
export SPRING_DATASOURCE_USERNAME=contacts
export SPRING_DATASOURCE_PASSWORD=contacts

./gradlew :backend:bootRun
```

### Frontend

```bash
cd frontend
npm install
export NEXT_PUBLIC_API_URL=http://localhost:8080
npm run dev
```

Open http://localhost:3000/contacts

## Tests

```bash
./gradlew :backend:test
cd frontend && npm test
```

## Project structure

```
backend/     # com.example.contact — domain / application / interfaces / infrastructure
frontend/    # src/contact — mirrored hexagon + app/contacts
docker/      # docker-compose.yml
openspec/    # SDD specs and tasks
Aarch.md     # Architecture authority
```

## API

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/contacts` | List contacts |
| GET | `/api/contacts/{id}` | Get by id |
| POST | `/api/contacts` | Create |
| PUT | `/api/contacts/{id}` | Update |
| DELETE | `/api/contacts/{id}` | Delete |

Request bodies are validated and sanitized (trim names, lowercase email). See Swagger for schemas and examples.

## SDD

Specifications live under `openspec/changes/contact-list-system/`. Implementation follows `tasks.md` phases.
