# Nexus CRM

Contact and user management CRM built with **hexagonal architecture (DDD)** on the backend and a mirrored structure on the frontend.

| Layer | Stack |
|-------|--------|
| Backend | Java 21, Spring Boot 3.3, JPA, Liquibase, JWT, MapStruct |
| Frontend | Next.js 15, React 19, TypeScript, Tailwind CSS, Vitest |
| Data | PostgreSQL 16 |
| Local ops | Docker Compose (Postgres, Mailhog, backend, frontend) |

Architecture rules and layer layout: [Aarch.md](./Aarch.md)  
Application flows (auth, contacts, admin): [docs/flows/application-flows.md](./docs/flows/application-flows.md)  
Guide for automated agents: [AGENTS.md](./AGENTS.md)  
UI reference (Stitch exports): [docs/design/stitch/README.md](./docs/design/stitch/README.md)

## Bounded contexts

| Context | Backend package | Frontend module | Responsibility |
|---------|-----------------|-----------------|----------------|
| **identity** | `com.example.identity` | `frontend/src/identity` | Login, register, JWT, password reset |
| **contact** | `com.example.contact` | `frontend/src/contact` | Contacts, notes, activities, tags, avatars |
| **user** | `com.example.user` | `frontend/src/user` | Admin user CRUD and invitations |

## Quick start (Docker)

```bash
cp .env.example .env
docker compose -f docker/docker-compose.yml up --build
```

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000/login |
| Backend API | http://localhost:8080/api |
| Mailhog (captured email) | http://localhost:8025 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |

### Seeded admin

| Field | Value |
|-------|-------|
| Email | `admin@nexuscrm.com` |
| Password | `Admin123!` |

## Local development

### Prerequisites

- JDK 21
- Node.js 20+
- PostgreSQL 16 (or Postgres via Docker)

### Backend

```bash
docker run -d --name contacts-db \
  -e POSTGRES_DB=contacts -e POSTGRES_USER=contacts -e POSTGRES_PASSWORD=contacts \
  -p 5432:5432 postgres:16-alpine

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

Open http://localhost:3000/login

### Email (password reset / invites)

With Docker Compose, SMTP targets **Mailhog** (`mailhog:1025`). Read messages at http://localhost:8025.

For `./gradlew :backend:bootRun` without Compose, run Mailhog locally (`docker run -p 1025:1025 -p 8025:8025 mailhog/mailhog`) and set `MAIL_HOST=localhost`, `MAIL_PORT=1025` in `.env`, or use Mailtrap credentials (see `.env.example`).

## Roles

| Role | Contacts | User admin |
|------|----------|------------|
| **ADMIN** | Full CRUD + avatar | Full access (`/api/users`) |
| **EDITOR** | Create, update, delete, avatar | No access |
| **VIEWER** | Read only (list, detail) | No access |

JWT is sent as `Authorization: Bearer <token>`. Public routes: `/api/auth/**`, Swagger, static avatars.

## Frontend routes

| Route | Access |
|-------|--------|
| `/login`, `/register`, `/forgot-password`, `/reset-password` | Public |
| `/contacts` | Authenticated |
| `/contacts/new`, `/contacts/[id]/edit` | Authenticated (mutations require EDITOR+ on API) |
| `/contacts/[id]` | Authenticated |
| `/admin/users` | Authenticated; nav visible only for ADMIN |

Route protection: Next.js `middleware.ts` (cookie) plus client layout auth refresh.

## API overview

Base path: `/api`. Details and schemas: Swagger UI.

### Auth (`/api/auth`)

| Method | Path | Auth |
|--------|------|------|
| POST | `/login` | Public |
| POST | `/register` | Public |
| GET | `/me` | Bearer |
| POST | `/forgot-password` | Public |
| POST | `/reset-password` | Public |
| POST | `/logout` | Bearer |

### Contacts (`/api/contacts`)

| Method | Path | Role |
|--------|------|------|
| GET | `/` | Any authenticated |
| GET | `/{id}` | Any authenticated |
| POST | `/` | ADMIN, EDITOR |
| PUT | `/{id}` | ADMIN, EDITOR |
| DELETE | `/{id}` | ADMIN, EDITOR |
| POST | `/{id}/avatar` | ADMIN, EDITOR |
| GET/POST | `/{id}/notes` | GET: any; POST: ADMIN, EDITOR |
| GET/POST | `/{id}/activities` | GET: any; POST: ADMIN, EDITOR |
| PUT | `/{id}/tags` | ADMIN, EDITOR |

### Users (`/api/users`) — ADMIN only

| Method | Path |
|--------|------|
| GET | `/` |
| GET | `/stats` |
| POST | `/` |
| POST | `/invite` |
| PUT | `/{id}` |
| DELETE | `/{id}` |

Request bodies for contacts are validated and sanitized (trim names, lowercase email).

## Tests

```bash
./gradlew :backend:test
cd frontend && npm test
cd frontend && npm run typecheck && npm run lint
```

Hexagonal dependency rules are enforced in `backend/src/test/java/com/example/architecture/HexagonalArchitectureTest.java`.

## Project layout

```
backend/src/main/java/com/example/
  contact/     # domain, application, interfaces, infrastructure
  identity/
  user/
frontend/src/
  contact/     # mirrored hexagon + App Router pages under app/(app)/contacts
  identity/
  user/
  shared/      # UI kit, apiFetch, authStorage
docker/        # docker-compose.yml
docs/          # flows, design assets, architecture diagram
Aarch.md       # architecture authority
AGENTS.md      # agent runbook
```

## Configuration

Copy `.env.example` to `.env`. Important variables:

- `JWT_SECRET` — at least 32 characters in production
- `NEXT_PUBLIC_API_URL` — browser-facing API origin (e.g. `http://localhost:8080`)
- `APP_CORS_ALLOWED_ORIGINS` — must include the frontend origin
- `SPRING_DATASOURCE_*` — Postgres connection for the backend
