# Agent runbook — Nexus CRM

Instructions for automated agents that review, run, test, or modify this repository. Human contributors can use the same commands; product context lives in [README.md](./README.md).

## What this system is

Monorepo CRM: Spring Boot API + Next.js UI + PostgreSQL. Three bounded contexts (`contact`, `identity`, `user`) follow hexagonal architecture. See [docs/Aarch.md](./docs/Aarch.md) for dependency rules.

## Repository map

| Path | Purpose |
|------|---------|
| `backend/` | Spring Boot application (`:backend` Gradle subproject) |
| `frontend/` | Next.js App Router UI |
| `docker/docker-compose.yml` | Full stack: postgres, mailhog, backend, frontend |
| `.env.example` | Environment template (copy to `.env`) |
| `docs/Aarch.md` | Hexagonal architecture rules and layer boundaries |
| `docs/DESIGN.md` | UI design system (colors, typography, components) |
| `docs/architecture/` | Architecture diagrams referenced from `Aarch.md` |

Do not treat `backend/bin/` as source; it is IDE/build output and should not be committed.

## Prerequisites

| Tool | Version |
|------|---------|
| JDK | 21 |
| Node.js | 20+ |
| Docker | Optional but recommended for full stack |

## Run full stack (preferred for E2E)

```bash
cp .env.example .env
docker compose -f docker/docker-compose.yml up --build
```

Optional demo data population (only when explicitly requested):

```bash
docker compose -f docker/docker-compose.yml --profile seed up --build
```

`backend-seed` runs once, inserts realistic demo records (1200 contacts by default), and exits.  
Customize volume with `APP_SEED_TARGET_CONTACTS` and `APP_SEED_TARGET_USERS`.

Wait until backend responds on port 8080 and frontend on 3000.

| Check | Command / URL |
|-------|----------------|
| API health | `curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api-docs` → `200` |
| UI | Open http://localhost:3000/login |
| Mailhog | http://localhost:8025 |

### Default credentials

- Email: `admin@nexuscrm.com`
- Password: `Admin123!`

## Run backend only (local JVM)

Requires Postgres on `localhost:5432` (database `contacts`, user/password `contacts`) or adjust `SPRING_DATASOURCE_*`.

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/contacts
export SPRING_DATASOURCE_USERNAME=contacts
export SPRING_DATASOURCE_PASSWORD=contacts
./gradlew :backend:bootRun
```

Liquibase runs migrations on startup. Schema must match `ddl-auto: validate`.

## Run frontend only (local)

```bash
cd frontend
npm ci
export NEXT_PUBLIC_API_URL=http://localhost:8080
npm run dev
```

`NEXT_PUBLIC_API_URL` must match the API origin the browser can reach (CORS: `APP_CORS_ALLOWED_ORIGINS`).

## Verification commands (run before claiming success)

From repository root:

```bash
./gradlew :backend:test
cd frontend && npm ci && npm run typecheck && npm run lint && npm test
```

Optional full build:

```bash
./gradlew :backend:build
cd frontend && npm run build
```

### Architecture tests

`HexagonalArchitectureTest` asserts domain packages do not depend on Spring or JPA for `contact`, `identity`, and `user`.

## API smoke test (curl)

Login and call a protected endpoint:

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@nexuscrm.com","password":"Admin123!"}' \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['token'])")

curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/auth/me
curl -s -H "Authorization: Bearer $TOKEN" 'http://localhost:8080/api/contacts?page=0&size=5'
```

Password-reset email (check Mailhog UI):

```bash
curl -s -X POST http://localhost:8080/api/auth/forgot-password \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@nexuscrm.com"}'
```

## Frontend auth model (for UI tests)

- JWT and profile stored in `localStorage` key `nexus_auth`
- Cookie `nexus_auth=1` set for Next.js middleware on `/contacts` and `/admin`
- `apiFetch` attaches `Authorization: Bearer` and redirects to `/login` on 401

Automated browser tests must set both storage and cookie if they bypass the login page.

## Authorization matrix (API)

| Endpoint group | ADMIN | EDITOR | VIEWER |
|----------------|-------|--------|--------|
| `GET /api/contacts*` (read) | yes | yes | yes |
| `POST|PUT|DELETE /api/contacts*` | yes | yes | no |
| `/api/users/**` | yes | no | no |
| `/api/auth/**` (except `/me`) | public | public | public |

Method-level `@PreAuthorize` on controllers is authoritative.

## Critical end-to-end flows (frontend -> backend)

### Auth and session bootstrap

- `/login` calls `POST /api/auth/login`, stores `nexus_auth`, and redirects to `/contacts`.
- `/accept-invitation?token=...` calls `POST /api/auth/complete-invitation` and behaves like login on success.
- `/api/auth/me` powers current session/profile refresh in authenticated layouts.

### Contact directory with advanced filters

- `/contacts` uses `useContactDirectory()` -> `contactGateway.listPaged()` -> `GET /api/contacts`.
- Supported query params: `search`, `email`, `phone`, repeated `tagNames`, `page`, `size`, optional `sort`.
- Pagination is server-driven (`totalElements`, `totalPages`, `page`, `size`).

### Contact detail + activities timeline

- `/contacts/[id]` loads base contact from `GET /api/contacts/{id}`.
- Activities tab uses infinite query against `GET /api/contacts/{contactId}/activities?page=&size=10&activityType=`.
- Infinite scroll is IntersectionObserver-based; stops when `nextPage >= totalPages`.
- Activity actions:
  - log: `POST /api/contacts/{contactId}/activities`
  - confirm: `PATCH /api/contacts/{contactId}/activities/{activityId}/confirm`
  - delete: `DELETE /api/contacts/{contactId}/activities/{activityId}`

### Tags assignment

- Contact create/edit submits tags via `PUT /api/contacts/{contactId}/tags` with `{"tagNames":[...]}`.
- Keep UI and backend in sync by invalidating both detail and directory query keys after mutations.

### Avatar upload + delivery pipeline (MinIO + imgproxy)

- Upload endpoint: `POST /api/contacts/{id}/avatar` (`multipart/form-data`, field name `file`).
- Storage adapter: `MinioAvatarStorageAdapter` writes `minio://<bucket>/avatars/<contactId>.<ext>`.
- URL resolver: `ImgproxyAvatarUrlResolverAdapter` signs imgproxy URLs from S3/MinIO source and returns public transformed URL.
- Compose stack requirements: `minio`, `minio-init`, `imgproxy` must be up; backend env must include `APP_AVATAR_*` and `APP_IMGPROXY_*`.

### User administration: invite, resend, complete

- Admin invite page calls `POST /api/users/invite`.
- Admin users table can resend only for `INVITED` records: `POST /api/users/{id}/resend-invitation`.
- Invite acceptance is completed by `/accept-invitation` -> `POST /api/auth/complete-invitation`.

## Common failure modes

| Symptom | Likely cause |
|---------|----------------|
| 401 on all API calls except auth | Missing or expired JWT |
| CORS error in browser | `APP_CORS_ALLOWED_ORIGINS` does not include frontend URL |
| Backend fails on startup | Postgres not ready or Liquibase drift |
| Empty Mailhog | `MAIL_HOST` not pointing to Mailhog in local bootRun |
| Frontend API errors | `NEXT_PUBLIC_API_URL` wrong for the environment |
| 403 on contact mutations | User has VIEWER role |

## Making changes safely

1. Read [docs/Aarch.md](./docs/Aarch.md) before moving code across layers.
2. Backend: domain must stay free of Spring/JPA imports.
3. Frontend: keep HTTP in `infrastructure`, UI in `interfaces`, orchestration in `application`.
4. After Java changes, run `./gradlew :backend:test`.
5. After TypeScript changes, run `npm run typecheck`, `npm run lint`, `npm test` in `frontend/`.
6. Do not commit secrets; use `.env` locally only.

## Swagger

When the backend is running: http://localhost:8080/swagger-ui.html — use for request/response schemas and trying endpoints interactively.

## Related documentation

- [docs/Aarch.md](./docs/Aarch.md) — architecture constraints, layer boundaries, and backend ↔ frontend mapping
- [docs/DESIGN.md](./docs/DESIGN.md) — design system reference (palette, typography, layout, components)
- [docs/architecture/hexagonal-overview.png](./docs/architecture/hexagonal-overview.png) — hexagonal architecture diagram
