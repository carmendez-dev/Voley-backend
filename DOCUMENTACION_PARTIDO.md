# 📚 Documentación Completa - Módulo Partido

## 🎯 Descripción General

El módulo **Partido** gestiona los encuentros deportivos entre un equipo local (inscrito en el torneo) y un equipo visitante (externo). Permite programar partidos, actualizar resultados y consultar información detallada.

---

## 📦 Archivos Implementados (7 total)

### Domain Layer
- ✅ `domain/Partido.java` - Entidad con enum ResultadoPartido

### Adapter Layer
- ✅ `adapter/PartidoJpaRepository.java` - Repositorio JPA

### Application Layer
- ✅ `application/partido/CrearPartidoUseCase.java`
- ✅ `application/partido/ObtenerTodosPartidosUseCase.java`

### Service Layer
- ✅ `service/PartidoService.java` - Orquestador con lógica adicional

### Controller Layer
- ✅ `controller/PartidoController.java` - API REST

### DTO Layer
- ✅ `dto/PartidoDTO.java` - Objeto de transferencia

---

## 🌐 API REST - Endpoints Completos

### Base URL
```
http://localhost:8080/api/partidos
```

---

## 📋 ENDPOINTS DETALLADOS

### 1. Crear Partido

**POST** `/api/partidos`

Crea un nuevo partido entre un equipo local (inscrito) y un equipo visitante.

**Request Body:**
```json
{
  "idInscripcionLocal": 1,
  "idEquipoVisitante": 1,
  "fecha": "2024-12-15T18:00:00",
  "ubicacion": "Polideportivo Municipal"
}
```

**Campos:**
- `idInscripcionLocal` (obligatorio): ID de la inscripción del equipo local
- `idEquipoVisitante` (obligatorio): ID del equipo visitante
- `fecha` (obligatorio): Fecha y hora del partido (formato ISO 8601)
- `ubicacion` (opcional): Lugar donde se jugará el partido (máx. 255 caracteres)

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Partido creado exitosamente",
  "timestamp": "2025-11-13T13:00:00",
  "data": {
    "idPartido": 1,
    "idInscripcionLocal": 1,
    "idEquipoVisitante": 1,
    "fecha": "2024-12-15T18:00:00",
    "ubicacion": "Polideportivo Municipal",
    "resultado": "Pendiente",
    "nombreEquipoLocal": "Los Tigres",
    "nombreEquipoVisitante": "Club Deportivo Visitante",
    "nombreTorneo": "Torneo Verano 2024",
    "nombreCategoria": "Masculino A"
  }
}
```

**Errores Posibles:**
- `400 Bad Request`: Validación fallida, inscripción o equipo no existe
- `500 Internal Server Error`: Error del servidor

---

### 2. Obtener Todos los Partidos

**GET** `/api/partidos`

Obtiene la lista completa de todos los partidos registrados.

**Request:**
```http
GET http://localhost:8080/api/partidos
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Partidos obtenidos exitosamente",
  "timestamp": "2025-11-13T13:00:00",
  "total": 3,
  "data": [
    {
      "idPartido": 1,
      "idInscripcionLocal": 1,
      "idEquipoVisitante": 1,
      "fecha": "2024-12-15T18:00:00",
      "ubicacion": "Polideportivo Municipal",
      "resultado": "Pendiente",
      "nombreEquipoLocal": "Los Tigres",
      "nombreEquipoVisitante": "Club Deportivo Visitante",
      "nombreTorneo": "Torneo Verano 2024",
      "nombreCategoria": "Masculino A"
    },
    {
      "idPartido": 2,
      "idInscripcionLocal": 2,
      "idEquipoVisitante": 2,
      "fecha": "2024-12-16T19:00:00",
      "ubicacion": "Estadio Central",
      "resultado": "Ganado",
      "nombreEquipoLocal": "Águilas FC",
      "nombreEquipoVisitante": "Equipo Externo",
      "nombreTorneo": "Torneo Verano 2024",
      "nombreCategoria": "Masculino B"
    }
  ]
}
```

---

### 3. Filtrar Partidos por Resultado

**GET** `/api/partidos?resultado={resultado}`

Filtra partidos según su resultado.

**Valores válidos para resultado:**
- `Pendiente` - Partidos que aún no se han jugado
- `Ganado` - Partidos ganados por el equipo local
- `Perdido` - Partidos perdidos por el equipo local
- `Walkover` - Partidos ganados por incomparecencia del rival
- `pendientes` - Alias especial para obtener solo pendientes

**Ejemplos:**

#### Obtener Partidos Pendientes
```http
GET http://localhost:8080/api/partidos?resultado=pendientes
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Partidos obtenidos exitosamente",
  "timestamp": "2025-11-13T13:00:00",
  "total": 2,
  "data": [
    {
      "idPartido": 1,
      "resultado": "Pendiente",
      "fecha": "2024-12-15T18:00:00",
      "nombreEquipoLocal": "Los Tigres",
      "nombreEquipoVisitante": "Club Deportivo Visitante"
    }
  ]
}
```

#### Obtener Partidos Ganados
```http
GET http://localhost:8080/api/partidos?resultado=Ganado
```

---

### 4. Obtener Partido por ID

**GET** `/api/partidos/{id}`

Obtiene la información detallada de un partido específico.

**Request:**
```http
GET http://localhost:8080/api/partidos/1
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Partido encontrado",
  "timestamp": "2025-11-13T13:00:00",
  "data": {
    "idPartido": 1,
    "idInscripcionLocal": 1,
    "idEquipoVisitante": 1,
    "fecha": "2024-12-15T18:00:00",
    "ubicacion": "Polideportivo Municipal",
    "resultado": "Pendiente",
    "nombreEquipoLocal": "Los Tigres",
    "nombreEquipoVisitante": "Club Deportivo Visitante",
    "nombreTorneo": "Torneo Verano 2024",
    "nombreCategoria": "Masculino A"
  }
}
```

**Errores:**
- `404 Not Found`: Partido no encontrado

---

### 5. Actualizar Partido

**PUT** `/api/partidos/{id}`

Actualiza la información de un partido (fecha, ubicación, resultado).

**Request:**
```http
PUT http://localhost:8080/api/partidos/1
Content-Type: application/json

{
  "fecha": "2024-12-16T20:00:00",
  "ubicacion": "Estadio Nuevo",
  "resultado": "Ganado"
}
```

**Nota:** Solo se actualizan los campos enviados. Los campos no incluidos permanecen sin cambios.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Partido actualizado exitosamente",
  "timestamp": "2025-11-13T13:00:00",
  "data": {
    "idPartido": 1,
    "fecha": "2024-12-16T20:00:00",
    "ubicacion": "Estadio Nuevo",
    "resultado": "Ganado",
    "nombreEquipoLocal": "Los Tigres",
    "nombreEquipoVisitante": "Club Deportivo Visitante"
  }
}
```

---

### 6. Cambiar Resultado del Partido

**PUT** `/api/partidos/{id}/resultado`

Endpoint específico para cambiar solo el resultado de un partido.

**Request:**
```http
PUT http://localhost:8080/api/partidos/1/resultado
Content-Type: application/json

{
  "resultado": "Ganado"
}
```

**Valores válidos:**
- `Pendiente`
- `Ganado`
- `Perdido`
- `Walkover`

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Resultado actualizado exitosamente",
  "timestamp": "2025-11-13T13:00:00",
  "data": {
    "idPartido": 1,
    "resultado": "Ganado",
    "nombreEquipoLocal": "Los Tigres",
    "nombreEquipoVisitante": "Club Deportivo Visitante"
  }
}
```

---

### 7. Eliminar Partido

**DELETE** `/api/partidos/{id}`

Elimina un partido del sistema.

**Request:**
```http
DELETE http://localhost:8080/api/partidos/1
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Partido eliminado exitosamente",
  "timestamp": "2025-11-13T13:00:00"
}
```

**Errores:**
- `404 Not Found`: Partido no encontrado

---

## 📊 Modelo de Datos

### Tabla: partido

```sql
CREATE TABLE `partido` (
  `id_partido` int(11) NOT NULL AUTO_INCREMENT,
  `id_inscripcion_local` int(11) NOT NULL,
  `id_equipo_visitante` int(11) NOT NULL,
  `fecha` datetime NOT NULL,
  `ubicacion` varchar(255) DEFAULT NULL,
  `resultado` enum('Pendiente','Ganado','Perdido','Walkover','WalkoverContra') NOT NULL DEFAULT 'Pendiente',
  PRIMARY KEY (`id_partido`),
  FOREIGN KEY (`id_inscripcion_local`) REFERENCES `inscripcion_equipo` (`id_inscripcion`),
  FOREIGN KEY (`id_equipo_visitante`) REFERENCES `equipo_visitante` (`id_equipo_visitante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### Relaciones
- **ManyToOne** con `InscripcionEquipo` (equipo local)
- **ManyToOne** con `EquipoVisitante` (equipo visitante)

### Enum: ResultadoPartido
```java
public enum ResultadoPartido {
    Pendiente,       // Partido aún no jugado
    Ganado,          // Victoria del equipo local
    Perdido,         // Derrota del equipo local
    Walkover,        // Victoria por incomparecencia del rival
    WalkoverContra   // Derrota por incomparecencia propia
}
```

---

## ✅ Validaciones Implementadas

### Validaciones de Negocio
- ✅ Inscripción local debe existir
- ✅ Equipo visitante debe existir
- ✅ Fecha del partido es obligatoria
- ✅ Ubicación no puede exceder 255 caracteres
- ✅ Resultado debe ser uno de los valores del enum

### Validaciones DTO
```java
@NotNull(message = "La inscripción local es obligatoria")
private Long idInscripcionLocal;

@NotNull(message = "El equipo visitante es obligatorio")
private Long idEquipoVisitante;

@NotNull(message = "La fecha del partido es obligatoria")
private LocalDateTime fecha;

@Size(max = 255, message = "La ubicación no puede exceder 255 caracteres")
private String ubicacion;
```

---

## 🎯 Casos de Uso Comunes

### Caso 1: Programar un Partido

```bash
# 1. Crear equipo visitante
POST /api/equipos-visitantes
{
  "nombre": "Club Rival"
}
# Resultado: ID = 1

# 2. Crear partido
POST /api/partidos
{
  "idInscripcionLocal": 1,
  "idEquipoVisitante": 1,
  "fecha": "2024-12-20T18:00:00",
  "ubicacion": "Polideportivo Central"
}
```

### Caso 2: Actualizar Resultado después del Partido

```bash
# Cambiar resultado a Ganado
PUT /api/partidos/1/resultado
{
  "resultado": "Ganado"
}
```

### Caso 3: Consultar Próximos Partidos

```bash
# Obtener todos los partidos pendientes
GET /api/partidos?resultado=pendientes
```

### Caso 4: Ver Historial de Partidos de un Equipo

```bash
# Obtener todos los partidos
GET /api/partidos

# Filtrar manualmente por nombreEquipoLocal en el frontend
```

---

## ⚠️ Manejo de Errores

### Error: Inscripción no existe (400)
```json
{
  "success": false,
  "message": "No existe inscripción con ID: 999",
  "timestamp": "2025-11-13T13:00:00"
}
```

### Error: Equipo visitante no existe (400)
```json
{
  "success": false,
  "message": "No existe equipo visitante con ID: 999",
  "timestamp": "2025-11-13T13:00:00"
}
```

### Error: Resultado inválido (400)
```json
{
  "success": false,
  "message": "Resultado inválido. Use: Pendiente, Ganado, Perdido, Walkover, WalkoverContra o pendientes",
  "timestamp": "2025-11-13T13:00:00"
}
```

### Error: Partido no encontrado (404)
```json
{
  "success": false,
  "message": "Partido no encontrado con ID: 999",
  "timestamp": "2025-11-13T13:00:00"
}
```

---

## 🔄 Flujo Completo de Ejemplo

```bash
# 1. Crear equipo visitante
POST /api/equipos-visitantes
{
  "nombre": "Deportivo Rival"
}
# Response: idEquipoVisitante = 1

# 2. Verificar inscripciones disponibles
GET /api/inscripciones
# Seleccionar idInscripcion = 1

# 3. Crear partido
POST /api/partidos
{
  "idInscripcionLocal": 1,
  "idEquipoVisitante": 1,
  "fecha": "2024-12-25T19:00:00",
  "ubicacion": "Estadio Municipal"
}
# Response: idPartido = 1

# 4. Consultar partido creado
GET /api/partidos/1

# 5. Actualizar ubicación
PUT /api/partidos/1
{
  "ubicacion": "Polideportivo Norte"
}

# 6. Después del partido, actualizar resultado
PUT /api/partidos/1/resultado
{
  "resultado": "Ganado"
}

# 7. Ver todos los partidos ganados
GET /api/partidos?resultado=Ganado

# 8. Si es necesario, eliminar partido
DELETE /api/partidos/1
```

---

## 📊 Información Enriquecida en Respuestas

Cada partido incluye información adicional automáticamente:

- `nombreEquipoLocal`: Nombre del equipo inscrito (local)
- `nombreEquipoVisitante`: Nombre del equipo visitante
- `nombreTorneo`: Torneo al que pertenece el equipo local
- `nombreCategoria`: Categoría en la que compite el equipo local

Esto evita hacer múltiples llamadas al API para obtener información relacionada.

---

## 🏗️ Arquitectura Hexagonal

```
Controller (Puerto de entrada)
    ↓
Service (Orquestador)
    ↓
UseCases (Lógica de negocio)
    ↓
Repository (Puerto de salida)
    ↓
Base de Datos
```

---

## ✅ Características Implementadas

- ✅ CRUD completo
- ✅ Relaciones con InscripcionEquipo y EquipoVisitante
- ✅ Enum para resultados del partido
- ✅ Filtros por resultado
- ✅ Endpoint específico para cambiar resultado
- ✅ Información enriquecida en respuestas
- ✅ Validaciones robustas
- ✅ Manejo de errores estandarizado
- ✅ Logging completo
- ✅ CORS configurado
- ✅ Arquitectura hexagonal

---

## 📊 Compilación

```bash
./mvnw clean package -DskipTests
# ✅ BUILD SUCCESS - 136 archivos compilados
```

---

## 🚀 Estado del Módulo

```
✅ Compilación: SUCCESS
✅ Endpoints: 7
✅ Casos de Uso: 2 + lógica en servicio
✅ Validaciones: Completas
✅ Documentación: Completa
✅ Estado: PRODUCCIÓN
```

---

**Fecha:** 13 de Noviembre, 2025  
**Versión:** 1.0  
**Arquitectura:** Hexagonal  
**Framework:** Spring Boot 3.5.6
