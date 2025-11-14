# 🧪 Guía de Pruebas Postman - Acción de Juego

## 📋 Colección de Endpoints

### Base URL
```
http://localhost:8080
```

---

## 📚 CATÁLOGOS (Solo Lectura)

### 1️⃣ Listar Tipos de Acción
```http
GET http://localhost:8080/api/tipos-accion
```

**Respuesta esperada:**
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
  },
  {
    "idTipoAccion": 4,
    "descripcion": "Bloqueo"
  },
  {
    "idTipoAccion": 5,
    "descripcion": "Defensa"
  },
  {
    "idTipoAccion": 6,
    "descripcion": "Colocación"
  }
]
```

### 2️⃣ Obtener Tipo de Acción por ID
```http
GET http://localhost:8080/api/tipos-accion/1
```

### 3️⃣ Listar Resultados de Acción
```http
GET http://localhost:8080/api/resultados-accion
```

**Respuesta esperada:**
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

### 4️⃣ Obtener Resultado de Acción por ID
```http
GET http://localhost:8080/api/resultados-accion/1
```

---

## 🎮 ACCIONES DE JUEGO (CRUD Completo)

### 5️⃣ Listar Todas las Acciones
```http
GET http://localhost:8080/api/acciones-juego
```

### 6️⃣ Obtener Acción por ID
```http
GET http://localhost:8080/api/acciones-juego/1
```

### 7️⃣ Obtener Acciones por Set
```http
GET http://localhost:8080/api/acciones-juego/set/1
```

### 8️⃣ Crear Acción - Punto de Saque Local
```http
POST http://localhost:8080/api/acciones-juego
Content-Type: application/json

{
  "idSetPartido": 1,
  "idTipoAccion": 1,
  "idResultadoAccion": 1,
  "idRoster": 5,
  "posicionVisitante": 0
}
```

### 9️⃣ Crear Acción - Error de Recepción Local
```http
POST http://localhost:8080/api/acciones-juego
Content-Type: application/json

{
  "idSetPartido": 1,
  "idTipoAccion": 2,
  "idResultadoAccion": 2,
  "idRoster": 7,
  "posicionVisitante": 0
}
```

### 🔟 Crear Acción - Punto de Ataque Local
```http
POST http://localhost:8080/api/acciones-juego
Content-Type: application/json

{
  "idSetPartido": 1,
  "idTipoAccion": 3,
  "idResultadoAccion": 1,
  "idRoster": 8,
  "posicionVisitante": 0
}
```

### 1️⃣1️⃣ Crear Acción - Punto de Bloqueo Local
```http
POST http://localhost:8080/api/acciones-juego
Content-Type: application/json

{
  "idSetPartido": 1,
  "idTipoAccion": 4,
  "idResultadoAccion": 1,
  "idRoster": 6,
  "posicionVisitante": 0
}
```

### 1️⃣2️⃣ Crear Acción - Punto del Equipo Visitante
```http
POST http://localhost:8080/api/acciones-juego
Content-Type: application/json

{
  "idSetPartido": 1,
  "idTipoAccion": 3,
  "idResultadoAccion": 1,
  "idRoster": 0,
  "posicionVisitante": 4
}
```

### 1️⃣3️⃣ Crear Acción - Error del Equipo Visitante
```http
POST http://localhost:8080/api/acciones-juego
Content-Type: application/json

{
  "idSetPartido": 1,
  "idTipoAccion": 1,
  "idResultadoAccion": 2,
  "idRoster": 0,
  "posicionVisitante": 2
}
```

### 1️⃣4️⃣ Actualizar Acción
```http
PUT http://localhost:8080/api/acciones-juego/1
Content-Type: application/json

{
  "idSetPartido": 1,
  "idTipoAccion": 3,
  "idResultadoAccion": 1,
  "idRoster": 5,
  "posicionVisitante": 0
}
```

### 1️⃣5️⃣ Eliminar Acción
```http
DELETE http://localhost:8080/api/acciones-juego/1
```

---

## 🔄 Flujo de Prueba Completo

### Paso 1: Verificar Catálogos
```bash
# Obtener tipos de acción disponibles
GET /api/tipos-accion

# Obtener resultados disponibles
GET /api/resultados-accion
```

### Paso 2: Registrar Acciones de un Rally
```bash
# 1. Saque del equipo local (Punto)
POST /api/acciones-juego
{
  "idSetPartido": 1,
  "idTipoAccion": 1,
  "idResultadoAccion": 1,
  "idRoster": 5,
  "posicionVisitante": 0
}

# 2. Recepción del visitante (Error)
POST /api/acciones-juego
{
  "idSetPartido": 1,
  "idTipoAccion": 2,
  "idResultadoAccion": 2,
  "idRoster": 0,
  "posicionVisitante": 3
}
```

### Paso 3: Consultar Acciones del Set
```bash
GET /api/acciones-juego/set/1
```

### Paso 4: Corregir una Acción
```bash
# Si se registró mal el tipo de acción
PUT /api/acciones-juego/1
{
  "idSetPartido": 1,
  "idTipoAccion": 3,
  "idResultadoAccion": 1,
  "idRoster": 5,
  "posicionVisitante": 0
}
```

### Paso 5: Eliminar Acción Duplicada
```bash
DELETE /api/acciones-juego/2
```

---

## ✅ Checklist de Pruebas

### Catálogos
- [ ] Listar todos los tipos de acción
- [ ] Obtener tipo de acción por ID
- [ ] Listar todos los resultados de acción
- [ ] Obtener resultado de acción por ID

### CRUD Acciones
- [ ] Crear acción de equipo local (punto)
- [ ] Crear acción de equipo local (error)
- [ ] Crear acción de equipo visitante (punto)
- [ ] Crear acción de equipo visitante (error)
- [ ] Listar todas las acciones
- [ ] Obtener acción por ID
- [ ] Obtener acciones por set
- [ ] Actualizar acción existente
- [ ] Eliminar acción

### Validaciones
- [ ] Intentar crear acción con set inexistente (debe fallar)
- [ ] Intentar crear acción con tipo inexistente (debe fallar)
- [ ] Intentar crear acción con resultado inexistente (debe fallar)
- [ ] Intentar crear acción con roster inexistente (debe fallar)
- [ ] Intentar actualizar acción inexistente (debe fallar)
- [ ] Intentar eliminar acción inexistente (debe fallar)

---

## 📊 Ejemplos de Respuestas

### Acción de Equipo Local
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

### Acción de Equipo Visitante
```json
{
  "idAccionJuego": 2,
  "idSetPartido": 1,
  "idTipoAccion": 3,
  "tipoAccionDescripcion": "Ataque",
  "idResultadoAccion": 1,
  "resultadoAccionDescripcion": "Punto",
  "idRoster": 0,
  "nombreJugador": null,
  "posicionVisitante": 4
}
```

---

## 🎯 Escenarios de Prueba

### Escenario 1: Rally Completo
1. Saque local → Punto
2. Recepción visitante → Error
3. Ataque local → Punto
4. Bloqueo local → Punto

### Escenario 2: Corrección de Errores
1. Registrar acción con tipo incorrecto
2. Actualizar con el tipo correcto
3. Verificar que se actualizó correctamente

### Escenario 3: Análisis de Set
1. Registrar múltiples acciones
2. Consultar todas las acciones del set
3. Filtrar por tipo de acción
4. Calcular estadísticas

---

## 💡 Tips para Pruebas

1. **Orden de pruebas**: Primero verifica los catálogos, luego crea acciones
2. **IDs válidos**: Asegúrate de usar IDs existentes de sets y rosters
3. **Equipo local vs visitante**: 
   - Local: `idRoster > 0`, `posicionVisitante = 0`
   - Visitante: `idRoster = 0`, `posicionVisitante > 0`
4. **Posiciones válidas**: 1-6 para visitante, 0 para local
5. **Consulta por set**: Útil para ver el desarrollo del juego

---

## 🔍 Códigos de Estado HTTP

- **200 OK**: Consulta exitosa
- **201 Created**: Acción creada
- **204 No Content**: Acción eliminada
- **400 Bad Request**: Datos inválidos
- **404 Not Found**: Recurso no encontrado
- **500 Internal Server Error**: Error del servidor

---

**Nota**: Reemplaza los IDs de ejemplo con los IDs reales de tu base de datos.
