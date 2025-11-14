# 📚 Documentación Módulo SetPartido

## 📋 Descripción General
El módulo **SetPartido** gestiona los sets individuales dentro de un partido de voleibol. Cada partido puede tener entre 3 y 5 sets, y este módulo permite registrar y actualizar los puntos de cada set.

---

## 🗂️ Estructura del Módulo

### 📦 Archivos Implementados

```
voley-backend/
├── src/main/java/com/voley/
│   ├── domain/
│   │   └── SetPartido.java                    # Entidad JPA
│   ├── dto/
│   │   ├── SetPartidoDTO.java                 # DTO para transferencia
│   │   └── SetPartidoUpdateDTO.java           # DTO para actualizaciones
│   ├── adapter/
│   │   └── SetPartidoJpaRepository.java       # Repositorio JPA
│   ├── application/setpartido/
│   │   ├── CrearSetPartidoUseCase.java        # Crear set
│   │   ├── ObtenerTodosSetPartidoUseCase.java # Listar todos
│   │   ├── ObtenerSetPartidoPorIdUseCase.java # Obtener por ID
│   │   ├── ObtenerSetsPorPartidoUseCase.java  # Obtener por partido
│   │   ├── ActualizarSetPartidoUseCase.java   # Actualizar set
│   │   └── EliminarSetPartidoUseCase.java     # Eliminar set
│   ├── service/
│   │   └── SetPartidoService.java             # Servicio de negocio
│   ├── controller/
│   │   └── SetPartidoController.java          # Controlador REST
│   └── config/
│       └── SetPartidoConfiguracion.java       # Configuración beans
```

---

## 🗄️ Modelo de Datos

### Tabla: `set_partido`

```sql
CREATE TABLE `set_partido` (
  `id_set_partido` int(11) NOT NULL AUTO_INCREMENT,
  `id_partido` int(11) NOT NULL,
  `numero_set` tinyint(4) NOT NULL,
  `puntos_local` tinyint(4) NOT NULL DEFAULT 0,
  `puntos_visitante` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_set_partido`),
  KEY `fk_partido` (`id_partido`),
  CONSTRAINT `fk_partido` FOREIGN KEY (`id_partido`) 
    REFERENCES `partido` (`id_partido`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### Campos

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id_set_partido` | int(11) | ID único del set (PK, auto-increment) |
| `id_partido` | int(11) | ID del partido al que pertenece (FK) |
| `numero_set` | tinyint(4) | Número del set (1-5) |
| `puntos_local` | tinyint(4) | Puntos del equipo local |
| `puntos_visitante` | tinyint(4) | Puntos del equipo visitante |

---

## 🌐 Endpoints API

### Base URL: `/api/sets`

### 1. ✅ Crear Set
**POST** `/api/sets`

Crea un nuevo set para un partido.

**Request Body:**
```json
{
  "idPartido": 1,
  "numeroSet": 1,
  "puntosLocal": 25,
  "puntosVisitante": 23
}
```

**Response (201 Created):**
```json
{
  "mensaje": "Set creado exitosamente",
  "set": {
    "idSetPartido": 1,
    "idPartido": 1,
    "numeroSet": 1,
    "puntosLocal": 25,
    "puntosVisitante": 23,
    "nombreEquipoLocal": "Equipo A",
    "nombreEquipoVisitante": "Equipo B",
    "ganador": "Local",
    "finalizado": true
  },
  "timestamp": "2025-11-13T17:30:00"
}
```

---

### 2. 📋 Listar Todos los Sets
**GET** `/api/sets`

Obtiene todos los sets registrados.

**Response (200 OK):**
```json
{
  "sets": [
    {
      "idSetPartido": 1,
      "idPartido": 1,
      "numeroSet": 1,
      "puntosLocal": 25,
      "puntosVisitante": 23,
      "nombreEquipoLocal": "Equipo A",
      "nombreEquipoVisitante": "Equipo B",
      "ganador": "Local",
      "finalizado": true
    }
  ],
  "total": 1,
  "timestamp": "2025-11-13T17:30:00"
}
```

---

### 3. 🔍 Obtener Set por ID
**GET** `/api/sets/{id}`

Obtiene un set específico por su ID.

**Response (200 OK):**
```json
{
  "set": {
    "idSetPartido": 1,
    "idPartido": 1,
    "numeroSet": 1,
    "puntosLocal": 25,
    "puntosVisitante": 23,
    "nombreEquipoLocal": "Equipo A",
    "nombreEquipoVisitante": "Equipo B",
    "ganador": "Local",
    "finalizado": true
  },
  "timestamp": "2025-11-13T17:30:00"
}
```

---

### 4. 🎯 Obtener Sets por Partido
**GET** `/api/sets/partido/{idPartido}`

Obtiene todos los sets de un partido específico.

**Response (200 OK):**
```json
{
  "sets": [
    {
      "idSetPartido": 1,
      "idPartido": 1,
      "numeroSet": 1,
      "puntosLocal": 25,
      "puntosVisitante": 23,
      "ganador": "Local",
      "finalizado": true
    },
    {
      "idSetPartido": 2,
      "idPartido": 1,
      "numeroSet": 2,
      "puntosLocal": 23,
      "puntosVisitante": 25,
      "ganador": "Visitante",
      "finalizado": true
    }
  ],
  "total": 2,
  "idPartido": 1,
  "timestamp": "2025-11-13T17:30:00"
}
```

---

### 5. ✏️ Actualizar Set
**PUT** `/api/sets/{id}`

Actualiza los datos de un set existente.

**Request Body:**
```json
{
  "puntosLocal": 26,
  "puntosVisitante": 24
}
```

**Response (200 OK):**
```json
{
  "mensaje": "Set actualizado exitosamente",
  "set": {
    "idSetPartido": 1,
    "idPartido": 1,
    "numeroSet": 1,
    "puntosLocal": 26,
    "puntosVisitante": 24,
    "nombreEquipoLocal": "Equipo A",
    "nombreEquipoVisitante": "Equipo B",
    "ganador": "Local",
    "finalizado": true
  },
  "timestamp": "2025-11-13T17:30:00"
}
```

---

### 6. 🗑️ Eliminar Set
**DELETE** `/api/sets/{id}`

Elimina un set del sistema.

**Response (200 OK):**
```json
{
  "mensaje": "Set eliminado exitosamente",
  "id": 1,
  "timestamp": "2025-11-13T17:30:00"
}
```

---

## ✅ Validaciones Implementadas

### Validaciones de Negocio

1. **Partido Obligatorio**
   - El set debe estar asociado a un partido existente

2. **Número de Set**
   - Debe estar entre 1 y 5
   - No puede haber duplicados para el mismo partido

3. **Puntos**
   - No pueden ser negativos
   - Deben ser números enteros

4. **Reglas de Voleibol**
   - Sets 1-4: Se gana con 25 puntos y diferencia de 2
   - Set 5 (decisivo): Se gana con 15 puntos y diferencia de 2

---

## 🎯 Lógica de Negocio

### Determinación del Ganador

```java
public String getGanador() {
    if (puntosLocal > puntosVisitante) {
        return "Local";
    } else if (puntosVisitante > puntosLocal) {
        return "Visitante";
    }
    return "Empate";
}
```

### Verificación de Set Finalizado

```java
public boolean estaFinalizado() {
    // Set normal: gana el primero en llegar a 25 con diferencia de 2
    if (numeroSet < 5) {
        return (puntosLocal >= 25 || puntosVisitante >= 25) && 
               Math.abs(puntosLocal - puntosVisitante) >= 2;
    }
    // Set decisivo (5to): gana el primero en llegar a 15 con diferencia de 2
    return (puntosLocal >= 15 || puntosVisitante >= 15) && 
           Math.abs(puntosLocal - puntosVisitante) >= 2;
}
```

---

## 🔄 Casos de Uso Comunes

### 1. Registrar Sets de un Partido Completo

```bash
# Set 1
POST /api/sets
{
  "idPartido": 1,
  "numeroSet": 1,
  "puntosLocal": 25,
  "puntosVisitante": 20
}

# Set 2
POST /api/sets
{
  "idPartido": 1,
  "numeroSet": 2,
  "puntosLocal": 23,
  "puntosVisitante": 25
}

# Set 3
POST /api/sets
{
  "idPartido": 1,
  "numeroSet": 3,
  "puntosLocal": 25,
  "puntosVisitante": 22
}
```

### 2. Actualizar Puntos Durante el Juego

```bash
PUT /api/sets/1
{
  "puntosLocal": 15,
  "puntosVisitante": 12
}
```

### 3. Consultar Resultado de un Partido

```bash
GET /api/sets/partido/1
```

---

## ⚠️ Manejo de Errores

### Errores Comunes

| Código | Error | Causa |
|--------|-------|-------|
| 400 | Bad Request | Datos inválidos o validación fallida |
| 404 | Not Found | Set o partido no encontrado |
| 409 | Conflict | Set duplicado para el mismo partido |
| 500 | Internal Server Error | Error del servidor |

### Ejemplos de Respuestas de Error

**Set no encontrado (404):**
```json
{
  "error": "No se encontró el set con ID: 999",
  "timestamp": "2025-11-13T17:30:00"
}
```

**Validación fallida (400):**
```json
{
  "error": "El número de set debe estar entre 1 y 5",
  "timestamp": "2025-11-13T17:30:00"
}
```

**Set duplicado (400):**
```json
{
  "error": "Ya existe el set 1 para este partido",
  "timestamp": "2025-11-13T17:30:00"
}
```

---

## 🔗 Relaciones

### SetPartido → Partido
- **Tipo:** Many-to-One
- **Cardinalidad:** Muchos sets pertenecen a un partido
- **Fetch:** LAZY
- **Cascade:** Ninguno (los sets no afectan al partido)

---

## 📊 Ejemplo de Flujo Completo

### Escenario: Registrar un partido completo de voleibol

```bash
# 1. Crear el partido (asumiendo que ya existe con ID 1)

# 2. Registrar Set 1
POST /api/sets
{
  "idPartido": 1,
  "numeroSet": 1,
  "puntosLocal": 25,
  "puntosVisitante": 20
}

# 3. Registrar Set 2
POST /api/sets
{
  "idPartido": 1,
  "numeroSet": 2,
  "puntosLocal": 23,
  "puntosVisitante": 25
}

# 4. Registrar Set 3
POST /api/sets
{
  "idPartido": 1,
  "numeroSet": 3,
  "puntosLocal": 25,
  "puntosVisitante": 22
}

# 5. Consultar todos los sets del partido
GET /api/sets/partido/1

# Respuesta:
{
  "sets": [
    {
      "numeroSet": 1,
      "puntosLocal": 25,
      "puntosVisitante": 20,
      "ganador": "Local"
    },
    {
      "numeroSet": 2,
      "puntosLocal": 23,
      "puntosVisitante": 25,
      "ganador": "Visitante"
    },
    {
      "numeroSet": 3,
      "puntosLocal": 25,
      "puntosVisitante": 22,
      "ganador": "Local"
    }
  ],
  "total": 3
}

# Resultado: Equipo Local gana 2-1
```

---

## 🎓 Notas Técnicas

### Arquitectura Hexagonal
- **Domain:** Entidad SetPartido con lógica de negocio
- **Application:** Casos de uso independientes
- **Adapter:** Repositorio JPA
- **Controller:** API REST

### Transaccionalidad
- Operaciones de escritura: `@Transactional`
- Operaciones de lectura: `@Transactional(readOnly = true)`

### Logging
- Todas las operaciones están registradas con SLF4J
- Nivel INFO para operaciones exitosas
- Nivel ERROR para excepciones

---

## ✅ Estado del Módulo

- ✅ Entidad JPA configurada
- ✅ DTOs de transferencia y actualización
- ✅ Repositorio con consultas personalizadas
- ✅ 6 casos de uso implementados
- ✅ Servicio de negocio completo
- ✅ Controlador REST con 6 endpoints
- ✅ Validaciones de negocio
- ✅ Manejo de errores
- ✅ Configuración de beans
- ✅ Compilación exitosa

---

## 🚀 Próximos Pasos Sugeridos

1. Implementar endpoint para actualizar múltiples sets a la vez
2. Agregar estadísticas de sets por equipo
3. Implementar validación de secuencia de sets
4. Agregar endpoint para calcular ganador del partido automáticamente
5. Implementar notificaciones cuando un set finaliza

---

**Documentación generada:** 13 de noviembre de 2025  
**Versión del módulo:** 1.0  
**Estado:** ✅ Producción
