# 🧪 Guía Postman - Módulo Partido

## 📋 Colección de Pruebas Rápidas

### Base URL
```
http://localhost:8080/api/partidos
```

---

## 🎯 Endpoints para Probar

### 1. Crear Partido 📝

```
POST http://localhost:8080/api/partidos
Content-Type: application/json

{
  "idInscripcionLocal": 1,
  "idEquipoVisitante": 1,
  "fecha": "2024-12-20T18:00:00",
  "ubicacion": "Polideportivo Municipal"
}
```

---

### 2. Obtener Todos los Partidos 📋

```
GET http://localhost:8080/api/partidos
```

---

### 3. Filtrar Partidos Pendientes 🕐

```
GET http://localhost:8080/api/partidos?resultado=pendientes
```

---

### 4. Filtrar Partidos Ganados 🏆

```
GET http://localhost:8080/api/partidos?resultado=Ganado
```

---

### 5. Obtener Partido por ID 🔍

```
GET http://localhost:8080/api/partidos/1
```

---

### 6. Actualizar Partido ✏️ (Actualización Parcial)

```
PUT http://localhost:8080/api/partidos/1
Content-Type: application/json

{
  "fecha": "2024-12-21T19:00:00",
  "ubicacion": "Estadio Central"
}
```

**Nota**: Todos los campos son opcionales. Solo envía los que quieres actualizar:
- `idInscripcionLocal` - Cambiar equipo local
- `idEquipoVisitante` - Cambiar equipo visitante  
- `fecha` - Cambiar fecha/hora
- `ubicacion` - Cambiar ubicación
- `resultado` - Cambiar resultado (o usa el endpoint específico)

---

### 7. Cambiar Solo el Resultado 🔄

```
PUT http://localhost:8080/api/partidos/1/resultado
Content-Type: application/json

{
  "resultado": "Ganado"
}
```

---

### 8. Eliminar Partido 🗑️

```
DELETE http://localhost:8080/api/partidos/1
```

---

## 🎯 Flujo de Prueba Completo

```bash
# Paso 1: Crear equipo visitante
POST /api/equipos-visitantes
{"nombre": "Club Rival"}

# Paso 2: Crear partido
POST /api/partidos
{
  "idInscripcionLocal": 1,
  "idEquipoVisitante": 1,
  "fecha": "2024-12-25T18:00:00",
  "ubicacion": "Polideportivo"
}

# Paso 3: Ver partido creado
GET /api/partidos/1

# Paso 4: Ver todos los pendientes
GET /api/partidos?resultado=pendientes

# Paso 5: Actualizar resultado
PUT /api/partidos/1/resultado
{"resultado": "Ganado"}

# Paso 6: Ver partidos ganados
GET /api/partidos?resultado=Ganado

# Paso 7: Eliminar partido
DELETE /api/partidos/1
```

---

## 📊 Resultados Válidos

- `Pendiente` - Por defecto al crear
- `Ganado` - Victoria del equipo local
- `Perdido` - Derrota del equipo local
- `Walkover` - Victoria por incomparecencia del rival
- `WalkoverContra` - Derrota por incomparecencia propia

---

## ✅ Checklist de Pruebas

- [ ] Crear partido exitosamente
- [ ] Obtener todos los partidos
- [ ] Filtrar por resultado Pendiente
- [ ] Filtrar por resultado Ganado
- [ ] Filtrar por resultado Perdido
- [ ] Filtrar por resultado Walkover
- [ ] Filtrar por resultado WalkoverContra
- [ ] Obtener partido por ID
- [ ] Actualizar partido completo
- [ ] Cambiar solo resultado
- [ ] Eliminar partido
- [ ] Error: Inscripción no existe
- [ ] Error: Equipo visitante no existe
- [ ] Error: Partido no encontrado
- [ ] Error: Resultado inválido

---

**¡Listo para probar! 🚀**
