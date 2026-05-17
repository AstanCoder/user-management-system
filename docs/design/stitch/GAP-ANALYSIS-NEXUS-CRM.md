# Análisis de brechas: sistema actual vs. diseño Nexus CRM (Stitch)

**Proyecto Stitch:** Professional Contact Manager  
**ID:** `15376703574118871324`  
**Fecha del análisis:** 2026-05-17  
**Alcance:** Contraste entre el repositorio `user-management-system` y las 10 pantallas del diseño (9 capturadas + design system documentado vía API).

---

## 1. Resumen ejecutivo

El diseño **Nexus CRM** describe un producto completo de gestión de relaciones con clientes: autenticación, directorio de contactos enriquecido, ficha de detalle con actividad y notas, administración de usuarios con roles, navegación global (dashboard, analytics, settings, admin) y un design system corporativo claro (tema claro, Inter, Material Symbols).

El sistema **actual** es un **CRUD mínimo de contactos** sin autenticación, sin módulo de usuarios, sin enriquecimiento del modelo de contacto (empresa, cargo, ubicación, avatar, etiquetas, notas, actividades) y con una UI funcional pero alejada del diseño (tema oscuro genérico, tabla simple, formulario modal en la misma página).

| Dimensión | Diseño Nexus CRM | Sistema actual | Brecha |
|-----------|------------------|----------------|--------|
| Bounded contexts | Contactos + Usuarios + Auth + (futuro) Analytics/Settings | Solo `contact` | **Alta** |
| Entidades de dominio | 8+ agregados/VOs | 1 agregado (`Contact`) | **Alta** |
| Endpoints REST | 20+ estimados | 5 (`/api/contacts`) | **Alta** |
| Pantallas/rutas UI | 10+ flujos | 1 (`/contacts`) | **Alta** |
| Seguridad | Login, roles, RBAC | Ninguna | **Crítica** |
| Design system | Systematic Professional (MD3-like) | CSS ad hoc oscuro | **Alta** |

---

## 2. Activos de diseño almacenados

Todos los assets descargados viven bajo `docs/design/stitch/`:

| Recurso | Ubicación |
|---------|-----------|
| Capturas PNG (9 pantallas) | `docs/design/stitch/screens/` |
| HTML exportado por Stitch | `docs/design/stitch/html/` |
| Metadatos API (`get_screen`) | `docs/design/stitch/metadata/` |
| Design system (tokens + guía) | `docs/design/stitch/design-system.json`, `DESIGN.md` |

### 2.1 Inventario de pantallas

| # | Pantalla | Archivo imagen | Dispositivo |
|---|----------|----------------|-------------|
| — | Design System (*Systematic Professional*) | — (solo JSON/MD) | — |
| 1 | Autenticación - Nexus CRM | `screens/01-auth-mobile.png` | Mobile |
| 2 | Detalle de Contacto | `screens/02-contact-detail-mobile.png` | Mobile |
| 3 | Lista de Contactos | `screens/03-contact-list-mobile.png` | Mobile |
| 4 | Gestionar Contacto | `screens/04-manage-contact-mobile.png` | Mobile |
| 5 | Gestión de Usuarios | `screens/05-user-management-mobile.png` | Mobile |
| 6 | Autenticación (Desktop) | `screens/06-auth-desktop.png` | Desktop |
| 7 | Directorio de Contactos (Desktop) | `screens/07-contact-directory-desktop.png` | Desktop |
| 8 | Detalle de Contacto (Desktop) | `screens/08-contact-detail-desktop.png` | Desktop |
| 9 | Gestión de Usuarios (Desktop) | `screens/09-user-management-desktop.png` | Desktop |

> **Nota:** La pantalla “Design System” del proyecto Stitch (`asset-stub-assets-…`) no devolvió screenshot vía `get_screen`; el contenido del sistema de diseño se recuperó con `list_design_systems` y está en `design-system.json` / `DESIGN.md`.

### 2.2 Identidad visual del diseño (Design System)

- **Nombre:** Systematic Professional — minimalismo corporativo, grid 12 columnas, max-width 1280px.
- **Tipografía:** Inter (`display-lg`, `headline-md`, `body-md`, `label-sm/md`).
- **Colores semánticos:** paleta Material-like (`primary` #051125, `primary-container` #1b263b, `secondary` #47607e, fondo `#f9faf5`).
- **Componentes prescritos:** botones primario/secundario, inputs con focus ring, chips de etiquetas, tablas de alta densidad, avatares circulares con iniciales, cards con borde 1px.
- **Iconografía:** Material Symbols Outlined.
- **Modo:** claro por defecto (`colorMode: LIGHT`); soporte `darkMode: class` en HTML.

El frontend actual usa variables CSS oscuras (`--bg: #0f1419`) sin Inter, sin tokens semánticos ni componentes compartidos — **incompatibilidad visual total** con el diseño.

---

## 3. Visión funcional del diseño (por módulo)

### 3.1 Autenticación (`01-auth-mobile`, `06-auth-desktop`)

**Lo que muestra el diseño:**

- Marca **Nexus CRM** con icono `contacts`.
- Formulario: email, contraseña, enlace “¿Olvidaste tu contraseña?”.
- CTA “Iniciar Sesión”.
- Enlace “Registrarse” (registro de cuenta).
- Desktop: layout split (panel visual + formulario).

**Implicaciones de dominio:**

- Entidad `User` (o `Account`) con credenciales.
- Flujos: login, registro, recuperación de contraseña.
- Sesión/JWT o cookies; posible integración SSO futura (no visible, pero el patrón CRM lo admite).

### 3.2 Directorio / Lista de contactos (`03-contact-list-mobile`, `07-contact-directory-desktop`)

**Lo que muestra el diseño:**

- **Shell de aplicación:** sidebar (desktop) con usuario logueado (nombre, rol “Global Admin”), navegación: Dashboard, Contacts (activo), Analytics, Settings, Admin Panel.
- **Top bar:** logo Nexus CRM, tabs secundarios (Directory, Teams, Reports), avatar usuario, notificaciones.
- **Directorio:** título “Contact Directory”, subtítulo, **búsqueda** (“Search contacts…”), botón **Add Contact**.
- **Vista de tarjetas (bento grid)** en desktop; filas con avatar, nombre, email, **etiquetas** (ej. “Key Client”, “Tech”, “Partner”).
- Acciones por tarjeta: menú `more_vert`.
- **Paginación** en lista móvil.
- **Bottom navigation** móvil: Home, Directory (activo), Admin, Profile.

**Campos visibles por contacto (más allá del CRUD actual):**

- Avatar/foto o iniciales.
- Nombre completo.
- Cargo (`VP of Operations`, etc.).
- Empresa implícita en tags o subtítulo.
- Email.
- Etiquetas de segmentación.

### 3.3 Detalle de contacto (`02-contact-detail-mobile`, `08-contact-detail-desktop`)

**Lo que muestra el diseño:**

- Navegación contextual: “Back to Directory”, botón **Edit**.
- **Hero:** avatar grande, nombre, cargo + empresa (“VP of Strategic Partnerships at CyberDyne Systems”).
- **Acciones rápidas:** Call, Email, Schedule Meeting (calendario).
- **Información de contacto:**
  - Email (tipo “Work”).
  - Teléfono móvil.
  - **Office Location** (dirección multilínea).
- **Segmentation Tags:** chips (“Key Decision Maker”, “Enterprise”, “Q4 Target”), acción “Add Tag”.
- **Pestañas:** Activity Log | Notes | Company Intel.
- **Activity Log:** línea de tiempo (Discovery Call, Email Sent, Internal Note) con autor, fecha, descripción; filtro; input “Log a call, note, or meeting…”.
- **Notes:** feed con autor (avatar iniciales), timestamp, cuerpo; filtro; composer “Save Note”.

**Implicaciones:**

- El contacto deja de ser un registro plano; es un **hub** con actividades, notas y metadatos CRM.
- Nuevas entidades: `Activity`, `Note`, `Tag`/`Segment`, posible `Company` embebida o referenciada.
- Integraciones futuras: telefonía, email, calendario (aunque en MVP pueden ser `mailto:` / `tel:`).

### 3.4 Gestionar contacto (`04-manage-contact-mobile`)

**Formulario del diseño:**

| Campo | En diseño | En sistema actual |
|-------|-----------|-------------------|
| firstName | Sí | Sí |
| lastName | Sí | Sí |
| company (Empresa) | Sí | **No** |
| jobTitle (Cargo) | Sí | **No** |
| phone | Sí (con icono) | Sí (opcional) |
| email | Sí | Sí |
| notes (textarea) | Sí | **No** |

- Header tipo wizard: cancelar / título “Agregar contacto” o editar / guardar.
- Sin avatar upload en el HTML analizado (avatar en listado/detalle sí).

### 3.5 Gestión de usuarios (`05-user-management-mobile`, `09-user-management-desktop`)

**Lo que muestra el diseño:**

- Título “Gestión de Usuarios” / Admin Panel.
- **KPIs:** Total Users (+12 this week), Active Admins (2 Pending), System Editors (Stable).
- Búsqueda de usuarios, filtro, botón **Invite User** / añadir.
- Tabla: User (avatar, nombre, email), **Role**, **Status**, **Last Active**, Actions (editar permisos, eliminar).
- **Roles:** Admin, Editor, Viewer.
- **Estados:** Active, Pending, Inactive (inferido por badges).
- Misma shell de navegación global.

**Implicaciones:**

- Segundo bounded context `user` (o `identity`) completo.
- RBAC con al menos 3 roles.
- Auditoría de última actividad.
- Flujo de invitación de usuarios.

### 3.6 Módulos referenciados pero sin pantalla dedicada

El diseño **menciona** en navegación pero **no entrega** mockups en este proyecto:

- **Dashboard** (`space_dashboard`)
- **Analytics** (`analytics`)
- **Settings** (`settings`)
- **Teams** (tab en directorio)
- **Reports** (tab en directorio)

Deben planificarse como fases posteriores o alcance explícitamente excluido.

---

## 4. Inventario del sistema actual

### 4.1 Backend (Java / Spring Boot)

**Bounded context único:** `com.example.contact`

**Agregado `Contact`:**

```
id (UUID), firstName, lastName, email (VO), phone (VO opcional), createdAt, updatedAt
```

**API REST** (`ContactController`, base `/api/contacts`):

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/contacts` | Lista todos (sin paginación ni búsqueda) |
| GET | `/api/contacts/{id}` | Detalle |
| POST | `/api/contacts` | Crear |
| PUT | `/api/contacts/{id}` | Actualizar |
| DELETE | `/api/contacts/{id}` | Eliminar |

**Persistencia:** tabla `contacts` (Liquibase `001-create-contacts-table.yaml`).

**Eventos de dominio:** `ContactCreated` publicado; sin consumidores visibles para actividad/notificaciones.

**No existe:**

- Spring Security / JWT / sesiones.
- Módulo `user`, `role`, `activity`, `note`, `tag`, `company`.
- Paginación, ordenación, filtros, búsqueda full-text.
- Upload de archivos (avatars).
- CORS está configurado; API abierta.

### 4.2 Frontend (Next.js 15)

**Rutas:**

- `/` → redirect a `/contacts`
- `/contacts` → página única con tabla + modales create/edit/delete

**Componentes UI:**

- `ContactList` — tabla HTML simple (Name, Email, Phone, Edit/Delete).
- `ContactForm` — modal con 4 campos.
- `ConfirmDeleteDialog` — confirmación básica.

**Arquitectura hexagonal espejada** en `frontend/src/contact/` (domain, application, infrastructure, interfaces) — **alineada con Aarch.md**, pero solo para contactos.

**No existe:**

- Layout autenticado (sidebar, top bar, bottom nav).
- Rutas `/login`, `/contacts/[id]`, `/admin/users`, etc.
- Design tokens del Stitch design system.
- Tailwind / librería de componentes (el diseño usa Tailwind + MD tokens).

### 4.3 Operaciones y DX

- Docker Compose, PostgreSQL, Swagger — **adecuado** para evolucionar.
- Tests backend (ArchUnit, controller, domain) y frontend (Vitest) — base sólida para extender.

---

## 5. Brechas detalladas

### 5.1 Estructura / arquitectura de módulos

| Módulo diseñado | Paquete/carpeta propuesto | Estado actual | Acción requerida |
|-----------------|---------------------------|---------------|------------------|
| Identity / Auth | `com.example.identity` + `src/identity/` | Ausente | Nuevo BC: login, registro, tokens, `CurrentUser` port |
| User management | `com.example.user` + `src/user/` | Ausente | CRUD usuarios, roles, invitaciones |
| Contact (enriquecido) | `com.example.contact` (existente) | Parcial | Extender agregado y casos de uso |
| Activity & Notes | `com.example.contact` o subdominio | Ausente | Entidades + repos + APIs anidadas bajo contacto |
| Tagging / Segments | `com.example.contact` | Ausente | Tabla M:N `contact_tags` |
| Company | `com.example.contact` o `company` | Ausente | VO o entidad `Company` referenciada |
| Analytics | — | Ausente | Fuera de MVP; placeholder en nav |
| Settings | — | Ausente | Fuera de MVP |
| Shared UI / Design System | `frontend/src/shared/ui/` | Mínimo (`globals.css`) | Tokens, Button, Input, Chip, Avatar, Sidebar |

**Dependencias entre capas:** mantener reglas de Aarch.md — los nuevos BC no deben importar JPA en domain; auth en `interfaces` con filtros/guards equivalentes.

### 5.2 Modelo de datos / entidades

#### Contacto — campos faltantes

| Campo / concepto | Tipo sugerido | Pantalla origen |
|------------------|---------------|-----------------|
| `company` | `String` o FK → `Company` | Gestionar contacto, detalle |
| `jobTitle` | `String` | Gestionar contacto, lista, detalle |
| `notes` (resumen) | `Text` o solo vía entidad `Note` | Formulario, detalle |
| `avatarUrl` | `String` / storage | Lista, detalle |
| `officeLocation` | `Address` VO (calle, ciudad, CP, país) | Detalle |
| `mobilePhone` / `workPhone` | separar tipos de teléfono | Detalle (hoy solo un `phone`) |
| `tags` | colección `Tag` | Lista, detalle |
| `ownerId` / `assignedTo` | FK User | Implícito CRM |
| `status` | enum (Active, Archived) | Buenas prácticas CRM |

#### Nuevas entidades

**`User`**

```
id, email, passwordHash, firstName, lastName, avatarUrl, role, status, lastActiveAt, createdAt, updatedAt
```

**`Role`** (enum o tabla): `ADMIN`, `EDITOR`, `VIEWER`

**`Note`**

```
id, contactId, authorId, body, createdAt, updatedAt
```

**`Activity`**

```
id, contactId, type (CALL|EMAIL|MEETING|NOTE|...), title, description, occurredAt, createdBy, metadata JSON
```

**`Tag`**

```
id, name, color?, slug
contact_tags (contact_id, tag_id)
```

**`Company`** (opcional fase 2)

```
id, name, industry?, website?
```

#### Migraciones Liquibase estimadas

1. `002-alter-contacts-add-crm-fields.yaml`
2. `003-create-tags-and-contact-tags.yaml`
3. `004-create-notes.yaml`
4. `005-create-activities.yaml`
5. `006-create-users-and-roles.yaml`
6. `007-create-sessions-or-refresh-tokens.yaml` (según estrategia auth)

### 5.3 API REST — endpoints faltantes

#### Autenticación

| Endpoint | Propósito |
|----------|-----------|
| `POST /api/auth/login` | Login email/password |
| `POST /api/auth/register` | Registro |
| `POST /api/auth/forgot-password` | Recuperación |
| `POST /api/auth/reset-password` | Reset con token |
| `POST /api/auth/logout` | Invalidar sesión |
| `GET /api/auth/me` | Usuario actual |

#### Contactos (extensión)

| Endpoint | Propósito |
|----------|-----------|
| `GET /api/contacts?search=&page=&size=&sort=` | Búsqueda y paginación |
| `GET /api/contacts?tag=` | Filtro por etiqueta |
| `POST /api/contacts/{id}/avatar` | Subida avatar |
| `GET/POST/PUT/DELETE /api/contacts/{id}/notes` | Notas |
| `GET/POST /api/contacts/{id}/activities` | Timeline |
| `PUT /api/contacts/{id}/tags` | Asignar etiquetas |

#### Usuarios (admin)

| Endpoint | Propósito |
|----------|-----------|
| `GET /api/users` | Lista con búsqueda/filtros |
| `GET /api/users/stats` | KPIs (total, admins, editors) |
| `POST /api/users` | Crear / invitar |
| `PUT /api/users/{id}` | Actualizar rol/estado |
| `DELETE /api/users/{id}` | Desactivar/eliminar |
| `POST /api/users/invite` | Invitación por email |

#### Autorización

- Rutas `/api/users/**` → rol `ADMIN`.
- Mutaciones de contactos → `ADMIN` o `EDITOR`.
- `VIEWER` → solo lectura.

### 5.4 Frontend — rutas y UX faltantes

| Ruta diseñada | Implementación actual | Gap |
|---------------|----------------------|-----|
| `/login` | No existe | Pantalla auth + guard |
| `/register` | No existe | Enlace en diseño |
| `/contacts` (directorio) | Tabla básica | Rediseño cards/grid + search |
| `/contacts/new` | Modal en misma página | Página dedicada o drawer |
| `/contacts/[id]` | No existe | Detalle completo con tabs |
| `/contacts/[id]/edit` | Modal | Página gestionar contacto |
| `/admin/users` | No existe | Gestión usuarios + KPIs |
| Layout con sidebar | No existe | `AppShell` responsive |
| Bottom nav móvil | No existe | 4 ítems del diseño |

**Estados UI del diseño no implementados:**

- Loading skeletons en grid de contactos.
- Empty states ilustrados.
- Paginación numérica.
- Toasts / snackbars tras acciones.
- Badges de rol y estado en usuarios.
- Tabs Activity / Notes / Company Intel.
- Composer de actividad en timeline.

### 5.5 Seguridad y multi-usuario

| Capacidad | Diseño | Actual |
|-----------|--------|--------|
| Autenticación | Sí | No |
| Autorización por rol | Admin, Editor, Viewer | No |
| Usuario en sesión en sidebar | Alex Rivera, Global Admin | No |
| Aislamiento de datos por usuario/equipo | Implícito (Teams tab) | No |
| Auditoría last active | Sí en tabla usuarios | No |

**Brecha crítica:** cualquier cliente puede llamar a la API sin credenciales.

### 5.6 Design system e implementación visual

| Token / patrón | Diseño | Actual (`globals.css`) |
|----------------|--------|------------------------|
| Background | `#f9faf5` claro | `#0f1419` oscuro |
| Primary | `#051125` / `#1b263b` | `#3b82f6` azul genérico |
| Tipografía | Inter + escala MD3 | system-ui |
| Iconos | Material Symbols | Ninguno |
| Layout max-width | 1280px centrado | 960px |
| Cards | borde + superficie blanca | tabla oscura |
| Grid | 12 columnas bento | tabla 4 columnas |
| Componentes | chips, avatares, sidebar | botones `.btn` custom |

**Acción:** adoptar `DESIGN.md` como fuente de verdad; configurar Tailwind con tokens del JSON o CSS variables en `shared/ui/theme.css`; migrar páginas progresivamente.

### 5.7 Funcionalidades transversales

| Funcionalidad | En diseño | En código |
|---------------|-----------|-----------|
| i18n (ES en auth, EN en otras) | Mixto ES/EN | Solo EN en UI |
| Notificaciones (icono campana) | Sí | No |
| Paginación | Sí (lista móvil) | No (lista completa) |
| Búsqueda | Contactos y usuarios | No |
| Filtros | Tags, notas, usuarios | No |
| Integración calendario/email | Botones en detalle | No (podría ser enlace) |
| Dashboard / Analytics | Nav only | No |
| Teams / Reports | Tabs | No |
| Registro de actividad automática | Timeline | Solo `ContactCreated` event |

### 5.8 Infraestructura y calidad

Lo existente **sí soporta** la evolución:

- Hexagonal architecture + tests.
- Liquibase para migraciones incrementales.
- OpenAPI/Swagger para documentar nuevos endpoints.
- Docker Compose.

**Faltante para producción alineada al diseño:**

- Object storage (S3/MinIO) para avatares.
- Email service (invitaciones, reset password).
- Rate limiting en auth.
- Política CORS restringida post-login.

---

## 6. Matriz pantalla → estado de implementación

| Pantalla Stitch | Funcionalidad clave | Backend | Frontend |
|-----------------|---------------------|---------|----------|
| Design System | Tokens, componentes | N/A | ❌ No implementado |
| Auth mobile/desktop | Login, registro, recovery | ❌ | ❌ |
| Lista contactos mobile | Lista rica, bottom nav, paginación | ⚠️ Lista plana sin paginación | ⚠️ Tabla simple |
| Directorio desktop | Grid, search, sidebar, tags | ❌ Campos/tags | ❌ |
| Detalle contacto | Tabs, actividad, notas, ubicación | ❌ | ❌ |
| Gestionar contacto | Form extendido | ⚠️ 4 campos | ⚠️ 4 campos |
| Gestión usuarios | CRUD, roles, KPIs, invite | ❌ | ❌ |

**Leyenda:** ✅ completo · ⚠️ parcial · ❌ ausente

---

## 7. Propuesta de fases de implementación

### Fase 0 — Fundamentos UI (1–2 sprints)

- Importar design system (`DESIGN.md` → Tailwind theme + `shared/ui`).
- `AppShell`: sidebar desktop, top bar, bottom nav móvil.
- Reemplazar tema oscuro por tema claro Nexus.

### Fase 1 — Auth + usuarios base (2–3 sprints)

- BC `identity`: User, Role, login JWT, `/api/auth/*`.
- Pantallas login/register; guard en Next.js middleware.
- BC `user`: listado admin básico (sin invite email al inicio).

### Fase 2 — Contactos enriquecidos (2 sprints)

- Migración: company, jobTitle, officeLocation, avatarUrl.
- APIs búsqueda + paginación.
- UI: directorio cards, página detalle (sin tabs aún), formulario completo.

### Fase 3 — Notas, actividades y tags (2–3 sprints)

- Entidades Note, Activity, Tag.
- APIs anidadas; tabs en detalle; composer de notas/actividad.

### Fase 4 — Admin avanzado y pulido (1–2 sprints)

- KPIs usuarios, invite, filtros, estados Pending/Inactive.
- i18n ES/EN coherente con diseño.
- Hardening seguridad (RBAC completo).

### Fase 5 — Módulos futuros (backlog)

- Dashboard, Analytics, Settings, Teams, Reports (solo si se especifican).

---

## 8. Checklist de aceptación “diseño alcanzado”

Para considerar que el sistema **equivale** al diseño Nexus CRM en alcance MVP:

- [ ] Usuario puede iniciar sesión y ver su nombre/rol en sidebar.
- [ ] Directorio muestra contactos en cards con avatar, cargo, email y chips de tags.
- [ ] Búsqueda y paginación funcionan en directorio.
- [ ] Detalle de contacto incluye información extendida, tags, tabs Activity/Notes.
- [ ] Crear/editar contacto incluye empresa, cargo, notas iniciales.
- [ ] Admin puede listar usuarios, ver KPIs, asignar roles y desactivar cuentas.
- [ ] UI usa tokens Systematic Professional (claro, Inter, Material Symbols).
- [ ] Layout responsive: sidebar desktop + bottom nav móvil.
- [ ] API protegida; VIEWER solo lectura; ADMIN gestiona usuarios.

---

## 9. Referencias en el repositorio

| Artefacto | Ruta |
|-----------|------|
| Arquitectura hexagonal | `Aarch.md` |
| Agregado Contact actual | `backend/.../domain/model/Contact.java` |
| API contactos | `backend/.../interfaces/rest/ContactController.java` |
| UI contactos actual | `frontend/src/app/contacts/page.tsx` |
| Esquema DB | `backend/src/main/resources/db/changelog/changes/001-create-contacts-table.yaml` |
| Capturas diseño | `docs/design/stitch/screens/*.png` |
| Design system | `docs/design/stitch/DESIGN.md` |

---

## 10. Conclusión

El repositorio implementa correctamente un **núcleo hexagonal de lista de contactos** (CRUD + validaciones + tests), pero el diseño Stitch **Nexus CRM** exige evolucionar hacia un **CRM multi-módulo** con identidad, autorización, modelo de contacto enriquecido, timeline de actividades, notas, etiquetas y una experiencia de aplicación completa (shell, rutas, design system claro).

La brecha no es solo visual: es estructural (nuevos bounded contexts), de datos (6+ tablas nuevas), de API (~15 endpoints adicionales) y de producto (roles, búsqueda, paginación, admin). Las capturas y HTML en `docs/design/stitch/` deben servir como referencia de aceptación visual durante la implementación por fases descrita arriba.
