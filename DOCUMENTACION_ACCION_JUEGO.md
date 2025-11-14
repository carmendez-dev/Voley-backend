# 📋 Documentación Módulo Acción de Juego

## 📦 Descripción General
Módulo para registrar y gestionar las acciones realizadas durante los sets de un partido de voleibol (saques, recepciones, ataques, bloqueos, etc.).

## 🗂️ Estructura del Módulo

### Entidades de Dominio

#### 1. **AccionJuego** (accion_juego)
Representa cada acción realizada durante un set.

**Campos:**
- `idAccionJuego` (Long): ID único de la acción
- `setPartido` (SetPartido): Set al que pertenece la acción
- `tipoAccion` (TipoAccion): Tipo de acción (Saque, Recepción, etc.)
- `resultadoAccion` (ResultadoAccion): Resultado (Punto, Error)
- `rosterJugador` (RosterJugador): Jugador que realizó la acción (0 si es rival)
- `posicionVisitante` (Byte): Posición del rival (1-6, 0 si es local)

#### 2. **TipoAccion** (tipo_accion) - Catálogo
Tipos de acciones disponibles.

**Valores:**
- 1: Saque
- 2: Recepción
- 3: Ataque
- 4: Bloqueo
- 5: Defensa
- 6: Colocación

#### 3. **ResultadoAccion** (resultado_accion) - Catálogo
Resultados posibles de una acción.

**Valores:**
- 1: Punto
- 2: Error

### Archivos Implementados

```
📁 domain/
  ├── AccionJuego.java
  ├── TipoAccion.java
  └── ResultadoAccion.java

📁 repository/
  ├── AccionJuegoRepository.java
  ├── TipoAccionRepository.java
  └── ResultadoAccionRepository.java

📁 dto/
  └── AccionJuegoDTO.java

📁 service/
  └── AccionJuegoService.java

📁 controller/
  ├── AccionJuegoController.java
  ├── TipoAccionController.java
  └── ResultadoAccionController.java
```

## 🌐 Endpoints API

### Acciones de Juego

#### 1. Listar todas las acciones
```http
GET /api/acciones-juego
```

**Respuesta exitosa (200):**
```json
[
  {
    "idAccionJuego": 1,
    "idSetPartido": 1,
    "idTipoAccion": 1,
    "tipoAccionDescripcion": "Saque",
    "idResultadoAccion": 1,
    "resultadoAccionDescripcion": "Punto",
    "idRoster": 5,
    "nombreJugador": "Juan Pérez",
    "posicionVisitante": 0
  }
]
```

#### 2. Obtener acción por ID
```http
GET /api/acciones-juego/{id}
```

**Respuesta exitosa (200):**
```json
{
  "idAccionJuego": 1,
  "idSetPartido": 1,
  "idTipoAccion": 1,
  "tipoAccionDescripcion": "Saque",
  "idResultadoAccion": 1,
  "resultadoAccionDescripcion": "Punto",
  "idRoster": 5,
  "nombreJugador": "Juan Pérez",
  "posicionVisitante": 0
}
```

#### 3. Obtener acciones por set
```http
GET /api/acciones-juego/set/{idSetPartido}
```

**Respuesta exitosa (200):**
```json
[
  {
    "idAccionJuego": 1,
    "idSetPartido": 1,
    "idTipoAccion": 1,
    "tipoAccionDescripcion": "Saque",
    "idResultadoAccion": 1,
    "resultadoAccionDescripcion": "Punto",
    "idRoster": 5,
    "nombreJugador": "Juan Pérez",
    "posicionVisitante": 0
  }
]
```

#### 4. Crear nueva acción
```http
POST /api/acciones-juego
Content-Type: application/json

{
  "idSetPartido": 1,
  "idTipoAccion": 1,
  "idResultadoAccion": 1,
  "idRoster": 5,
  "posicionVisitante": 0
}
```

**Respuesta exitosa (201):**
```json
{
  "idAccionJuego": 1,
  "idSetPartido": 1,
  "idTipoAccion": 1,
  "tipoAccionDescripcion": "Saque",
  "idResultadoAccion": 1,
  "resultadoAccionDescripcion": "Punto",
  "idRoster": 5,
  "nombreJugador": "Juan Pérez",
  "posicionVisitante": 0
}
```

#### 5. Actualizar acción
```http
PUT /api/acciones-juego/{id}
Content-Type: application/json

{
  "idSetPartido": 1,
  "idTipoAccion": 3,
  "idResultadoAccion": 1,
  "idRoster": 5,
  "posicionVisitante": 0
}
```

**Respuesta exitosa (200):**
```json
{
  "idAccionJuego": 1,
  "idSetPartido": 1,
  "idTipoAccion": 3,
  "tipoAccionDescripcion": "Ataque",
  "idResultadoAccion": 1,
  "resultadoAccionDescripcion": "Punto",
  "idRoster": 5,
  "nombreJugador": "Juan Pérez",
  "posicionVisitante": 0
}
```

#### 6. Eliminar acción
```http
DELETE /api/acciones-juego/{id}
```

**Respuesta exitosa (204):** Sin contenido

### Catálogos

#### Tipos de Acción

**Listar todos:**
```http
GET /api/tipos-accion
```

**Respuesta (200):**
```json
[
  {
    "idTipoAccion": 1,
    "descripcion": "Saque"
  },
  {
    "idTipoAccion": 2,
    "descripcion": "Recepción"
  },
  {
    "idTipoAccion": 3,
    "descripcion": "Ataque"
  }
]
```

**Obtener por ID:**
```http
GET /api/tipos-accion/{id}
```

#### Resultados de Acción

**Listar todos:**
```http
GET /api/resultados-accion
```

**Respuesta (200):**
```json
[
  {
    "idResultadoAccion": 1,
    "descripcion": "Punto"
  },
  {
    "idResultadoAccion": 2,
    "descripcion": "Error"
  }
]
```

**Obtener por ID:**
```http
GET /api/resultados-accion/{id}
```

## 📊 Modelo de Datos

### Relaciones
- **AccionJuego** → **SetPartido** (Many-to-One)
- **AccionJuego** → **TipoAccion** (Many-to-One)
- **AccionJuego** → **ResultadoAccion** (Many-to-One)
- **AccionJuego** → **RosterJugador** (Many-to-One)

### Reglas de Negocio
1. Una acción debe pertenecer a un set válido
2. El tipo de acción y resultado deben existir en los catálogos
3. **Si `idRoster = 0` o `null`, la acción es del equipo visitante** (no tiene roster asociado)
4. **Si `idRoster > 0`, la acción es del equipo local** y debe existir el roster
5. Si `posicionVisitante > 0`, indica la posición del jugador rival (1-6)
6. Si `posicionVisitante = 0`, la acción es del equipo local

## ✅ Validaciones

### Al crear/actualizar acción:
- ✓ Set de partido debe existir
- ✓ Tipo de acción debe existir
- ✓ Resultado de acción debe existir
- ✓ Roster de jugador debe existir
- ✓ Posición visitante debe estar entre 0 y 6

## 🎯 Casos de Uso Comunes

### 1. Registrar punto de saque
```json
POST /api/acciones-juego
{
  "idSetPartido": 1,
  "idTipoAccion": 1,
  "idResultadoAccion": 1,
  "idRoster": 5,
  "posicionVisitante": 0
}
```

### 2. Registrar error de ataque
```json
POST /api/acciones-juego
{
  "idSetPartido": 1,
  "idTipoAccion": 3,
  "idResultadoAccion": 2,
  "idRoster": 8,
  "posicionVisitante": 0
}
```

### 3. Registrar punto del equipo visitante
```json
POST /api/acciones-juego
{
  "idSetPartido": 1,
  "idTipoAccion": 3,
  "idResultadoAccion": 1,
  "idRoster": 0,
  "posicionVisitante": 4
}
```

### 4. Ver todas las acciones de un set
```http
GET /api/acciones-juego/set/1
```

## ⚠️ Manejo de Errores

### Códigos de respuesta:
- **200 OK**: Operación exitosa
- **201 Created**: Acción creada exitosamente
- **204 No Content**: Acción eliminada exitosamente
- **400 Bad Request**: Datos inválidos
- **404 Not Found**: Acción no encontrada
- **500 Internal Server Error**: Error del servidor

### Mensajes de error comunes:
- "Acción de juego no encontrada con ID: {id}"
- "Set de partido no encontrado"
- "Tipo de acción no encontrado"
- "Resultado de acción no encontrado"
- "Roster de jugador no encontrado"

## 🔄 Flujo Completo de Ejemplo

```bash
# 1. Obtener catálogos
GET /api/tipos-accion
GET /api/resultados-accion

# 2. Registrar acciones durante el set
POST /api/acciones-juego
{
  "idSetPartido": 1,
  "idTipoAccion": 1,
  "idResultadoAccion": 1,
  "idRoster": 5,
  "posicionVisitante": 0
}

# 3. Ver todas las acciones del set
GET /api/acciones-juego/set/1

# 4. Corregir una acción si fue mal registrada
PUT /api/acciones-juego/1
{
  "idSetPartido": 1,
  "idTipoAccion": 3,
  "idResultadoAccion": 1,
  "idRoster": 5,
  "posicionVisitante": 0
}

# 5. Eliminar acción duplicada
DELETE /api/acciones-juego/2
```

## 📝 Notas Importantes

1. **Acciones del equipo local**: `idRoster > 0` y `posicionVisitante = 0`
   - El roster debe existir en la base de datos
   - Se incluye el nombre del jugador en la respuesta
2. **Acciones del equipo visitante**: `idRoster = 0` (o `null`) y `posicionVisitante > 0`
   - No requiere roster asociado
   - El campo `nombreJugador` será `null`
   - La posición indica qué jugador visitante realizó la acción (1-6)
3. Los catálogos (TipoAccion y ResultadoAccion) son de solo lectura
4. Las acciones se ordenan por ID al consultar por set
5. El campo `idRoster` puede ser 0 para indicar acciones del equipo visitante

---
**Versión:** 1.0  
**Última actualización:** 2025-11-13
