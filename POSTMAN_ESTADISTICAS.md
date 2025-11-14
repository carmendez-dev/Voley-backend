# 🧪 Guía de Pruebas Postman - Estadísticas

## 📋 Colección de Endpoints

### Base URL
```
http://localhost:8080
```

---

## 📊 ENDPOINTS DE ESTADÍSTICAS

### 1️⃣ Estadísticas Generales (Dashboard)
```http
GET http://localhost:8080/api/estadisticas/generales
```

**Descripción**: Obtiene todas las estadísticas para el dashboard principal.

**Respuesta esperada**:
```json
{
  "totalPartidos": 25,
  "partidosGanados": 15,
  "partidosPerdidos": 8,
  "partidosWalkover": 1,
  "partidosWalkoverContra": 1,
  "partidosPendientes": 0,
  "totalSetsJugados": 78,
  "setsGanados": 45,
  "setsPerdidos": 33,
  "totalPuntos": 1850,
  "totalErrores": 420
}
```

---

### 2️⃣ Estadísticas de un Partido
```http
GET http://localhost:8080/api/estadisticas/partido/1
```

**Descripción**: Obtiene estadísticas detalladas de un partido específico.

**Respuesta esperada**:
```json
{
  "idPartido": 1,
  "equipoLocal": "Equipo A",
  "equipoVisitante": "Equipo B",
  "resultado": "ganado",
  "setsGanadosLocal": 3,
  "setsGanadosVisitante": 1,
  "puntosLocal": 95,
  "erroresLocal": 18,
  "puntosPorTipoLocal": [
    {
      "tipoAccion": "Ataque",
      "cantidad": 45
    },
    {
      "tipoAccion": "Saque",
      "cantidad": 25
    },
    {
      "tipoAccion": "Bloqueo",
      "cantidad": 15
    }
  ],
  "erroresPorTipoLocal": [
    {
      "tipoAccion": "Saque",
      "cantidad": 8
    },
    {
      "tipoAccion": "Recepción",
      "cantidad": 6
    }
  ],
  "puntosVisitante": 78,
  "erroresVisitante": 22,
  "puntosPorTipoVisitante": [
    {
      "tipoAccion": "Ataque",
      "cantidad": 38
    }
  ],
  "erroresPorTipoVisitante": [
    {
      "tipoAccion": "Saque",
      "cantidad": 10
    }
  ]
}
```

---

### 3️⃣ Estadísticas de un Jugador en un Partido
```http
GET http://localhost:8080/api/estadisticas/partido/1/jugador/5
```

**Descripción**: Obtiene estadísticas de un jugador específico en un partido.

**Respuesta esperada**:
```json
{
  "idRoster": 5,
  "nombreJugador": "Juan Pérez García",
  "idPartido": 1,
  "equipoLocal": "Equipo A",
  "equipoVisitante": "Equipo B",
  "totalPuntos": 28,
  "totalErrores": 5,
  "puntosPorTipo": [
    {
      "tipoAccion": "Ataque",
      "cantidad": 15
    },
    {
      "tipoAccion": "Saque",
      "cantidad": 8
    },
    {
      "tipoAccion": "Bloqueo",
      "cantidad": 5
    }
  ],
  "erroresPorTipo": [
    {
      "tipoAccion": "Saque",
      "cantidad": 3
    },
    {
      "tipoAccion": "Ataque",
      "cantidad": 2
    }
  ]
}
```

---

## 🔄 Flujo de Prueba Completo

### Escenario 1: Dashboard Principal

```bash
# Paso 1: Ver estadísticas generales
GET /api/estadisticas/generales

# Analizar:
# - ¿Cuántos partidos hemos jugado?
# - ¿Cuál es nuestra tasa de victoria?
# - ¿Cuántos sets hemos ganado?
# - ¿Cuál es nuestro ratio puntos/errores?
```

### Escenario 2: Análisis Post-Partido

```bash
# Paso 1: Ver estadísticas del partido
GET /api/estadisticas/partido/1

# Analizar:
# - ¿Ganamos o perdimos?
# - ¿Cuántos sets ganamos?
# - ¿En qué tipo de acción fuimos más efectivos?
# - ¿Dónde cometimos más errores?

# Paso 2: Ver rendimiento de jugadores clave
GET /api/estadisticas/partido/1/jugador/5
GET /api/estadisticas/partido/1/jugador/8
GET /api/estadisticas/partido/1/jugador/12

# Comparar:
# - ¿Quién anotó más puntos?
# - ¿Quién tuvo menos errores?
# - ¿Quién fue más efectivo en ataques?
```

### Escenario 3: Evaluación de Jugador

```bash
# Ver rendimiento del jugador en múltiples partidos
GET /api/estadisticas/partido/1/jugador/5
GET /api/estadisticas/partido/2/jugador/5
GET /api/estadisticas/partido/3/jugador/5

# Analizar evolución:
# - ¿Está mejorando?
# - ¿En qué acción es más fuerte?
# - ¿Dónde necesita mejorar?
```

---

## 📊 Ejemplos de Cálculos

### Tasa de Victoria
```javascript
tasaVictoria = (partidosGanados / totalPartidos) * 100
Ejemplo: (15 / 25) * 100 = 60%
```

### Efectividad del Equipo
```javascript
efectividad = (puntos / (puntos + errores)) * 100
Ejemplo: (95 / (95 + 18)) * 100 = 84.07%
```

### Efectividad del Jugador
```javascript
efectividadJugador = (totalPuntos / (totalPuntos + totalErrores)) * 100
Ejemplo: (28 / (28 + 5)) * 100 = 84.85%
```

### Contribución al Equipo
```javascript
contribucion = (puntosJugador / puntosEquipo) * 100
Ejemplo: (28 / 95) * 100 = 29.47%
```

---

## ✅ Checklist de Pruebas

### Estadísticas Generales
- [ ] Obtener estadísticas generales
- [ ] Verificar que los totales sean correctos
- [ ] Verificar que la suma de resultados = total partidos
- [ ] Verificar que sets ganados + perdidos ≤ total sets

### Estadísticas de Partido
- [ ] Obtener estadísticas de un partido existente
- [ ] Verificar que los puntos por tipo sumen el total
- [ ] Verificar que los errores por tipo sumen el total
- [ ] Verificar que sets ganados sean consistentes con el resultado
- [ ] Intentar obtener estadísticas de partido inexistente (debe fallar)

### Estadísticas de Jugador
- [ ] Obtener estadísticas de un jugador en un partido
- [ ] Verificar que los puntos por tipo sumen el total
- [ ] Verificar que los errores por tipo sumen el total
- [ ] Intentar obtener estadísticas de jugador que no jugó (debe fallar)
- [ ] Intentar obtener estadísticas con IDs inválidos (debe fallar)

---

## 🎯 Casos de Prueba Específicos

### Caso 1: Partido Dominante
```bash
GET /api/estadisticas/partido/1

# Esperar:
# - setsGanadosLocal > setsGanadosVisitante
# - puntosLocal > puntosVisitante
# - erroresLocal < erroresVisitante
# - resultado = "ganado"
```

### Caso 2: Partido Cerrado
```bash
GET /api/estadisticas/partido/2

# Esperar:
# - setsGanadosLocal ≈ setsGanadosVisitante
# - puntosLocal ≈ puntosVisitante
# - Diferencia de puntos < 10
```

### Caso 3: Jugador Estrella
```bash
GET /api/estadisticas/partido/1/jugador/5

# Esperar:
# - totalPuntos > 20
# - totalErrores < 5
# - Efectividad > 80%
# - Puntos de ataque > otros tipos
```

### Caso 4: Jugador en Desarrollo
```bash
GET /api/estadisticas/partido/1/jugador/12

# Esperar:
# - totalPuntos < 15
# - totalErrores > 5
# - Efectividad < 70%
```

---

## 📈 Interpretación de Resultados

### Estadísticas Generales

**Buenas señales**:
- Tasa de victoria > 60%
- Ratio puntos/errores > 4:1
- Sets ganados > sets perdidos
- Pocos walkoverContra

**Señales de alerta**:
- Tasa de victoria < 40%
- Ratio puntos/errores < 3:1
- Muchos walkoverContra
- Muchos partidos pendientes

### Estadísticas de Partido

**Partido exitoso**:
- Resultado = "ganado"
- Sets ganados ≥ 3
- Efectividad > 80%
- Errores de saque < 10

**Áreas de mejora**:
- Muchos errores en un tipo específico
- Pocos puntos de bloqueo
- Baja efectividad en recepción

### Estadísticas de Jugador

**Jugador destacado**:
- Efectividad > 80%
- Contribución > 25%
- Errores < 5
- Puntos distribuidos en varios tipos

**Jugador necesita apoyo**:
- Efectividad < 60%
- Muchos errores en un tipo
- Pocos puntos totales
- Contribución < 10%

---

## 💡 Tips para Análisis

1. **Comparar con promedios**: Usa las estadísticas generales como referencia
2. **Buscar patrones**: Identifica tipos de acción problemáticos
3. **Evolución temporal**: Compara estadísticas de múltiples partidos
4. **Contexto del rival**: Considera la fortaleza del equipo visitante
5. **Condiciones del juego**: Ten en cuenta si fue local o visitante

---

## 🔍 Códigos de Estado HTTP

- **200 OK**: Estadísticas obtenidas correctamente
- **404 Not Found**: Partido o jugador no encontrado
- **500 Internal Server Error**: Error al calcular estadísticas

---

**Nota**: Reemplaza los IDs de ejemplo (1, 5, 8, etc.) con IDs reales de tu base de datos.

**Tip**: Usa variables de entorno en Postman para los IDs frecuentes:
```
{{baseUrl}} = http://localhost:8080
{{idPartido}} = 1
{{idJugador}} = 5
```
