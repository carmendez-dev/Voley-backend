# 📚 Documentación - Módulo EquipoVisitante

## ✅ Módulo Completado

Se ha implementado el módulo completo de **EquipoVisitante** siguiendo la arquitectura hexagonal del proyecto.

---

## 📦 Archivos Creados (11 total)

### Domain Layer
- ✅ `domain/EquipoVisitante.java` - Entidad

### Adapter Layer
- ✅ `adapter/EquipoVisitanteJpaRepository.java` - Repositorio JPA

### Application Layer - Casos de Uso (6)
- ✅ `application/equipovisitante/CrearEquipoVisitanteUseCase.java`
- ✅ `application/equipovisitante/ObtenerTodosEquiposVisitantesUseCase.java`
- ✅ `application/equipovisitante/ObtenerEquipoVisitantePorIdUseCase.java`
- ✅ `application/equipovisitante/BuscarEquiposVisitantesPorNombreUseCase.java`
- ✅ `application/equipovisitante/ActualizarEquipoVisitanteUseCase.java`
- ✅ `application/equipovisitante/EliminarEquipoVisitanteUseCase.java`

### Service Layer
- ✅ `service/EquipoVisitanteService.java` - Orquestador

### Controller Layer
- ✅ `controller/EquipoVisitanteController.java` - API REST

### DTO Layer
- ✅ `dto/EquipoVisitanteDTO.java` - Objeto de transferencia

---

## 🌐 API REST Endpoints

### Base URL
```
http://localhost:8080/api/equipos-visitantes
```

### Endpoints Disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/equipos-visitantes` | Crear equipo visitante |
| GET | `/api/equipos-visitantes` | Obtener todos |
| GET | `/api/equipos-visitantes?nombre={nombre}` | Buscar por nombre |
| GET | `/api/equipos-visitantes/{id}` | Obtener por ID |
| PUT | `/api/equipos-visitantes/{id}` | Actualizar |
| DELETE | `/api/equipos-visitantes/{id}` | Eliminar |

---

## 📊 Modelo de Datos

### Tabla: equipo_visitante

```sql
CREATE TABLE `equipo_visitante` (
  `id_equipo_visitante` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`id_equipo_visitante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### Entidad Java

```java
@Entity
@Table(name = "equipo_visitante")
public class EquipoVisitante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo_visitante")
    private Long idEquipoVisitante;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
}
```

---

## 🎯 Ejemplos de Uso

### 1. Crear Equipo Visitante

**Request:**
```http
POST http://localhost:8080/api/equipos-visitantes
Content-Type: application/json

{
  "nombre": "Club Deportivo Visitante"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Equipo visitante creado exitosamente",
  "timestamp": "2025-11-13T12:00:00",
  "data": {
    "idEquipoVisitante": 1,
    "nombre": "Club Deportivo Visitante"
  }
}
```

---

### 2. Obtener Todos los Equipos Visitantes

**Request:**
```http
GET http://localhost:8080/api/equipos-visitantes
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Equipos visitantes obtenidos exitosamente",
  "timestamp": "2025-11-13T12:00:00",
  "total": 2,
  "data": [
    {
      "idEquipoVisitante": 1,
      "nombre": "Club Deportivo Visitante"
    },
    {
      "idEquipoVisitante": 2,
      "nombre": "Equipo Externo FC"
    }
  ]
}
```

---

### 3. Buscar por Nombre

**Request:**
```http
GET http://localhost:8080/api/equipos-visitantes?nombre=Deportivo
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Equipos visitantes obtenidos exitosamente",
  "timestamp": "2025-11-13T12:00:00",
  "total": 1,
  "data": [
    {
      "idEquipoVisitante": 1,
      "nombre": "Club Deportivo Visitante"
    }
  ]
}
```

---

### 4. Obtener por ID

**Request:**
```http
GET http://localhost:8080/api/equipos-visitantes/1
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Equipo visitante encontrado",
  "timestamp": "2025-11-13T12:00:00",
  "data": {
    "idEquipoVisitante": 1,
    "nombre": "Club Deportivo Visitante"
  }
}
```

---

### 5. Actualizar

**Request:**
```http
PUT http://localhost:8080/api/equipos-visitantes/1
Content-Type: application/json

{
  "nombre": "Club Deportivo Visitante Actualizado"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Equipo visitante actualizado exitosamente",
  "timestamp": "2025-11-13T12:00:00",
  "data": {
    "idEquipoVisitante": 1,
    "nombre": "Club Deportivo Visitante Actualizado"
  }
}
```

---

### 6. Eliminar

**Request:**
```http
DELETE http://localhost:8080/api/equipos-visitantes/1
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Equipo visitante eliminado exitosamente",
  "timestamp": "2025-11-13T12:00:00"
}
```

---

## ✅ Validaciones Implementadas

### Validaciones de Negocio
- ✅ Nombre obligatorio
- ✅ Nombre no puede exceder 100 caracteres
- ✅ No puede haber equipos visitantes con el mismo nombre (duplicados)

### Validaciones DTO
```java
@NotBlank(message = "El nombre es obligatorio")
@Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
private String nombre;
```

---

## ⚠️ Manejo de Errores

### Error: Nombre Duplicado (400)
```json
{
  "success": false,
  "message": "Ya existe un equipo visitante con el nombre: Club Deportivo",
  "timestamp": "2025-11-13T12:00:00"
}
```

### Error: No Encontrado (404)
```json
{
  "success": false,
  "message": "Equipo visitante no encontrado con ID: 999",
  "timestamp": "2025-11-13T12:00:00"
}
```

### Error: Validación (400)
```json
{
  "success": false,
  "message": "El nombre es obligatorio",
  "timestamp": "2025-11-13T12:00:00"
}
```

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

## ✅ Características

- ✅ CRUD completo
- ✅ Validación de duplicados
- ✅ Búsqueda por nombre (case-insensitive)
- ✅ Arquitectura hexagonal
- ✅ Casos de uso separados
- ✅ Logging completo
- ✅ Manejo de errores estandarizado
- ✅ CORS configurado
- ✅ Validaciones con Bean Validation

---

## 📊 Compilación

```bash
./mvnw clean compile -DskipTests
# ✅ BUILD SUCCESS - 129 archivos compilados
```

---

## 🚀 Aplicación Corriendo

```
✅ Puerto: 8080
✅ Endpoints: 6
✅ Casos de Uso: 6
✅ Estado: RUNNING
```

---

**Fecha:** 13 de Noviembre, 2025
**Versión:** 1.0
**Arquitectura:** Hexagonal
