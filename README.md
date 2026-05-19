# Nexus CRM

Contact and user management CRM built with **hexagonal architecture (DDD)** on the backend and a mirrored structure on the frontend.

| Layer | Stack |
|-------|--------|
| Backend | Java 21, Spring Boot 3.3, JPA, Liquibase, JWT, MapStruct |
| Frontend | Next.js 15, React 19, TypeScript, Tailwind CSS, Vitest |
| Data | PostgreSQL 16 |
| Local ops | Docker Compose (Postgres, Mailhog, backend, frontend) |

Architecture rules and layer layout: [docs/Aarch.md](./docs/Aarch.md)  
UI and design system reference: [docs/DESIGN.md](./docs/DESIGN.md)  
Guide for automated agents: [AGENTS.md](./AGENTS.md)

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

To populate realistic demo data (1200+ contacts and related entities), run:

```bash
docker compose -f docker/docker-compose.yml --profile seed up --build
```

Optional overrides:

```bash
APP_SEED_TARGET_CONTACTS=1800 APP_SEED_TARGET_USERS=40 \
docker compose -f docker/docker-compose.yml --profile seed up --build
```

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000/login |
| Backend API | http://localhost:8080/api |
| Mailhog (captured email) | http://localhost:8025 |
| MinIO API | http://localhost:9000 |
| MinIO Console | http://localhost:9001 |
| imgproxy | http://localhost:8082 |
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

Invitation emails include an accept link built from `APP_FRONTEND_BASE_URL` (default `http://localhost:3000`). Set this to the origin users open in the browser — e.g. `http://localhost:3002` if the frontend runs on a non-default port.

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

## End-to-end use cases (frontend -> backend)

### 1) Auth login

- Frontend page: `/login` (`frontend/src/app/(auth)/login/page.tsx`).
- Client flow: React Query mutation calls `identityDependencies.loginUseCase`, then invalidates `authQueryKeys.session` and redirects to `/contacts`.
- HTTP layer: `FetchAuthGateway.login()` -> `POST /api/auth/login`.
- Backend flow: `AuthController.login()` -> `LoginUseCase` -> JWT response; frontend stores session (`nexus_auth`) and cookie `nexus_auth=1`.

### 2) Contact listing, search, and advanced filters

- Frontend page: `/contacts` (`frontend/src/app/(app)/contacts/page.tsx`).
- UX flow: debounced text search + advanced filters (email, phone, comma-separated tags) + pagination controls.
- Hook/gateway: `useContactDirectory()` -> `contactGateway.listPaged()` with `search`, `email`, `phone`, repeated `tagNames`, `page`, `size`.
- Backend flow: `GET /api/contacts` in `ContactController.list()` -> `ListContactsUseCase` with `ContactSearchQuery`.

### 3) Contact detail loading

- Frontend page: `/contacts/[id]` (`frontend/src/app/(app)/contacts/[id]/page.tsx`).
- Hook: `useContactDetail(contactId)` loads base contact via `contactGateway.getById()`.
- Backend flow: `GET /api/contacts/{id}` in `ContactController.getById()`.
- Returned model includes mapped `avatarUrl`, tags, and recent activity state used by detail UI.

### 4) Activities pagination, filter, and infinite scroll

- Frontend page: `/contacts/[id]` activity tab with `IntersectionObserver` sentinel for auto-load.
- Hook: `useInfiniteQuery` in `useContactDetail()` calls `contactGateway.listActivities(contactId, { page, size: 10, activityType })`.
- Backend flow: `GET /api/contacts/{contactId}/activities?page=&size=&activityType=` in `ContactActivitiesController.list()`.
- Behavior: filter changes query key (`contactId + activityType`) and resets paging; scrolling loads next page until `totalPages` is reached.

### 5) Tags assignment

- Frontend pages: create/edit contact forms (`/contacts/new`, `/contacts/[id]/edit`) submit `tags` list.
- HTTP layer: `contactGateway.assignTags(contactId, tagNames)` -> `PUT /api/contacts/{contactId}/tags`.
- Backend flow: `ContactTagsController.assign()` -> `AssignTagsUseCase`.
- Contract: tag names are sent as `{"tagNames":["..."]}` and backend returns resolved tag set for the contact.

### 6) Avatar upload and serving via MinIO + imgproxy signed URL

- Frontend pages: create/edit contact forms upload image file and call `contactGateway.uploadAvatar(contactId, file)`.
- HTTP layer: `POST /api/contacts/{id}/avatar` (`multipart/form-data`, field `file`).
- Backend storage flow:
  - `ContactController.uploadAvatar()` delegates to upload use case.
  - `MinioAvatarStorageAdapter` stores object as `minio://<bucket>/avatars/<contactId>.<ext>`.
  - `AvatarUrlRestMapper` resolves persisted value with `ResolveAvatarUrlPort`.
  - `ImgproxyAvatarUrlResolverAdapter` signs `/rs:fill:256:256:0/<encoded-s3-source>.webp` and returns `APP_IMGPROXY_BASE_URL/<signature>/...`.
- Runtime dependencies: `docker/docker-compose.yml` includes `minio`, `minio-init`, and `imgproxy`; backend receives `APP_AVATAR_*` and `APP_IMGPROXY_*`.

### 7) User admin invitation, resend, and complete

- Invite flow:
  - Frontend page: `/admin/users/invite`.
  - API call: `POST /api/users/invite`.
  - Backend: `UserController.invite()` sends invitation email with tokenized activation link (`{APP_FRONTEND_BASE_URL}/accept-invitation?token=...`).
- Resend flow:
  - Frontend page: `/admin/users` action menu for `INVITED` users.
  - API call: `POST /api/users/{id}/resend-invitation`.
  - Backend: `UserController.resendInvitation()`.
- Complete invitation flow:
  - Frontend page: `/accept-invitation?token=...`.
  - API call: `POST /api/auth/complete-invitation`.
  - Backend: `AuthController.completeInvitation()` activates account and returns JWT session.

## Tests

```bash
./gradlew :backend:test
cd frontend && npm run typecheck && npm run lint && npm test
```

Hexagonal dependency rules are enforced in `backend/src/test/java/com/example/architecture/HexagonalArchitectureTest.java`.

### CI

On every push and pull request, [`.github/workflows/ci.yml`](.github/workflows/ci.yml) runs backend tests, frontend typecheck/lint/tests, and a Docker Compose smoke test (stack up, health checks, login + `/api/auth/me`). See [AGENTS.md](./AGENTS.md#continuous-integration) for job details.

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
docs/          # architecture and design reference
  Aarch.md     # hexagonal architecture authority
  DESIGN.md    # UI design system (colors, typography, components)
  architecture/hexagonal-overview.png
AGENTS.md      # agent runbook
```

## Configuration

Copy `.env.example` to `.env`. Important variables:

- `JWT_SECRET` — at least 32 characters in production
- `NEXT_PUBLIC_API_URL` — browser-facing API origin (e.g. `http://localhost:8080`)
- `APP_CORS_ALLOWED_ORIGINS` — must include the frontend origin
- `APP_FRONTEND_BASE_URL` — public frontend origin for invitation email links (e.g. `http://localhost:3000`; maps to `app.frontend.base-url` in the backend)
- `APP_MAIL_FROM` — sender address for transactional email (invitations, password reset)
- `SPRING_DATASOURCE_*` — Postgres connection for the backend
- `APP_SEED_TARGET_CONTACTS` / `APP_SEED_TARGET_USERS` — demo dataset size when running with `--profile seed`
