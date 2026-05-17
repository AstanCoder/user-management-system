# Agent runbook — Nexus CRM

Instructions for automated agents that review, run, test, or modify this repository. Human contributors can use the same commands; product context lives in [README.md](./README.md) and [docs/flows/application-flows.md](./docs/flows/application-flows.md).

## What this system is

Monorepo CRM: Spring Boot API + Next.js UI + PostgreSQL. Three bounded contexts (`contact`, `identity`, `user`) follow hexagonal architecture. See [Aarch.md](./Aarch.md) for dependency rules.

## Repository map

| Path | Purpose |
|------|---------|
| `backend/` | Spring Boot application (`:backend` Gradle subproject) |
| `frontend/` | Next.js App Router UI |
| `docker/docker-compose.yml` | Full stack: postgres, mailhog, backend, frontend |
| `.env.example` | Environment template (copy to `.env`) |
| `docs/flows/` | End-to-end application flows |
| `docs/design/stitch/` | UI reference screens and HTML (not runtime) |

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

1. Read [Aarch.md](./Aarch.md) before moving code across layers.
2. Backend: domain must stay free of Spring/JPA imports.
3. Frontend: keep HTTP in `infrastructure`, UI in `interfaces`, orchestration in `application`.
4. After Java changes, run `./gradlew :backend:test`.
5. After TypeScript changes, run `npm run typecheck`, `npm run lint`, `npm test` in `frontend/`.
6. Do not commit secrets; use `.env` locally only.

## Swagger

When the backend is running: http://localhost:8080/swagger-ui.html — use for request/response schemas and trying endpoints interactively.

## Related documentation

- [docs/flows/application-flows.md](./docs/flows/application-flows.md) — sequence and route-level flows
- [docs/README.md](./docs/README.md) — documentation index
- [docs/design/stitch/README.md](./docs/design/stitch/README.md) — design reference assets
