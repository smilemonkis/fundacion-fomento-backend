# ESPECIFICACIÓN TÉCNICA DE API: FUNDACIÓN FOMENTO
**Fecha:** 2026
**Versión:** 1.0.0
**Tecnología:** Spring Boot / Java / OpenAPI 3.0

---

## 1. RESUMEN DEL SISTEMA
Esta API centraliza la operación de la Fundación Fomento, permitiendo la gestión de usuarios, aliados estratégicos y el seguimiento de proyectos sociales.

**Base URL Local:** `http://localhost:8080/api/v1`
**Swagger UI:** `/swagger-ui.html`

---

## 2. ARQUITECTURA DE ENDPOINTS

### 🔐 SEGURIDAD (AUTH)
Control de acceso mediante JWT.
- `POST /auth/login`: Autenticación de usuarios.
- `POST /auth/logout`: Revocación de sesión.

### 👥 MÓDULO DE USUARIOS
Gestión del personal y roles de la plataforma.
- `GET /usuarios`: Listado maestro (Paginado).
- `POST /usuarios`: Registro de nuevos perfiles.
- `PUT /usuarios/{id}/activar`: Habilitar acceso.
- `PUT /usuarios/{id}/desactivar`: Suspensión (Borrado lógico).

### 🏗️ PROYECTOS Y CONVOCATORIAS
Núcleo operativo de la fundación.

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| GET | `/proyectos/activos` | Lista de proyectos en ejecución. |
| GET | `/proyectos/codigo/{c}`| Búsqueda por identificador único. |
| POST | `/proyectos` | Creación de nuevas iniciativas. |
| GET | `/convocatorias/activas`| Visualización de procesos abiertos. |

### 🤝 ALIADOS (NATURALES Y JURÍDICOS)
Segmentación de benefactores según su naturaleza legal.
- **Persona Natural:** Acceso vía `/aliados-naturales`. Búsqueda por documento.
- **Persona Jurídica:** Acceso vía `/aliados-juridicos`. Búsqueda por NIT.

### 💳 TRANSACCIONES (DONACIONES E INSCRIPCIONES)
Flujos con estados de aprobación.

#### Donaciones
- **Registro:** `POST /donaciones`.
- **Gestión:** `/aprobar`, `/rechazar`, `/cancelar`.
- **Filtros:** Historial por usuario o por proyecto específico.

#### Inscripciones
- **Aplicación:** `POST /inscripciones` (Registro a convocatorias).
- **Validación:** `/aprobar` o `/rechazar` por parte del administrador.

---

## 3. ESQUEMAS DE DATOS (DTOs)
La API utiliza modelos estandarizados para la transferencia de datos:
- **Entrada:** `Create...Request`, `Update...Request`.
- **Salida:** `...Response` (Incluye metadatos de paginación y ordenamiento).
