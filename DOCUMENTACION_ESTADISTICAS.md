# 📊 Documentación Módulo de Estadísticas

## 📦 Descripción General
Módulo para generar estadísticas completas del sistema, incluyendo estadísticas de partidos, jugadores y datos generales para el dashboard.

## 🌐 Endpoints API

### 1. Estadísticas Generales (Dashboard)

```http
GET /api/estadisticas/generales
```

**Descripción**: Obtiene estadísticas generales del sistema para mostrar en el dashboard principal.

**Respuesta exitosa (200)**:
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

**Campos**:
- `totalPartidos`: Total de partidos registrados
- `partidosGanados`: Partidos ganados por el equipo local
- `partidosPerdidos`: Partidos perdidos
- `partidosWalkover`: Partidos ganados por walkover (rival no se presentó)
- `partidosWalkoverContra`: Partidos perdidos por walkover (no nos presentamos)
- `partidosPendientes`: Partidos aún no jugados
- `totalSetsJugados`: Total de sets jugados
- `setsGanados`: Sets ganados
- `setsPerdidos`: Sets perdidos
- `totalPuntos`: Total de puntos anotados
- `totalErrores`: Total de errores cometidos

---

### 2. Estadísticas de un Partido

```http
GET /api/estadisticas/partido/{idPartido}
```

**Descripción**: Obtiene estadísticas detalladas de un partido específico, incluyendo puntos y errores por tipo de acción para ambos equipos.

**Parámetros**:
- `idPartido` (path): ID del partido

**Respuesta exitosa (200)**:
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
    },
    {
      "tipoAccion": "Defensa",
      "cantidad": 10
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
    },
    {
      "tipoAccion": "Ataque",
      "cantidad": 4
    }
  ],
  "puntosVisitante": 78,
  "erroresVisitante": 22,
  "puntosPorTipoVisitante": [
    {
      "tipoAccion": "Ataque",
      "cantidad": 38
    },
    {
      "tipoAccion": "Saque",
      "cantidad": 20
    },
    {
      "tipoAccion": "Bloqueo",
      "cantidad": 12
    },
    {
      "tipoAccion": "Defensa",
      "cantidad": 8
    }
  ],
  "erroresPorTipoVisitante": [
    {
      "tipoAccion": "Saque",
      "cantidad": 10
    },
    {
      "tipoAccion": "Recepción",
      "cantidad": 7
    },
    {
      "tipoAccion": "Ataque",
      "cantidad": 5
    }
  ]
}
```

**Campos**:
- `idPartido`: ID del partido
- `equipoLocal`: Nombre del equipo local
- `equipoVisitante`: Nombre del equipo visitante
- `resultado`: Resultado del partido (ganado, perdido, walkover, etc.)
- `setsGanadosLocal`: Sets ganados por el equipo local
- `setsGanadosVisitante`: Sets ganados por el equipo visitante
- `puntosLocal`: Total de puntos del equipo local
- `erroresLocal`: Total de errores del equipo local
- `puntosPorTipoLocal`: Desglose de puntos por tipo de acción (ordenado por cantidad)
- `erroresPorTipoLocal`: Desglose de errores por tipo de acción (ordenado por cantidad)
- `puntosVisitante`: Total de puntos del equipo visitante
- `erroresVisitante`: Total de errores del equipo visitante
- `puntosPorTipoVisitante`: Desglose de puntos por tipo de acción
- `erroresPorTipoVisitante`: Desglose de errores por tipo de acción

---

### 3. Estadísticas de un Jugador en un Partido

```http
GET /api/estadisticas/partido/{idPartido}/jugador/{idRoster}
```

**Descripción**: Obtiene estadísticas detalladas de un jugador específico en un partido, incluyendo puntos y errores por tipo de acción.

**Parámetros**:
- `idPartido` (path): ID del partido
- `idRoster` (path): ID del roster del jugador

**Respuesta exitosa (200)**:
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

**Campos**:
- `idRoster`: ID del roster del jugador
- `nombreJugador`: Nombre completo del jugador
- `idPartido`: ID del partido
- `equipoLocal`: Nombre del equipo local
- `equipoVisitante`: Nombre del equipo visitante
- `totalPuntos`: Total de puntos anotados por el jugador
- `totalErrores`: Total de errores cometidos por el jugador
- `puntosPorTipo`: Desglose de puntos por tipo de acción (ordenado por cantidad)
- `erroresPorTipo`: Desglose de errores por tipo de acción (ordenado por cantidad)

---

## 📊 Casos de Uso

### Dashboard Principal

```http
GET /api/estadisticas/generales
```

Muestra:
- Resumen de partidos (ganados, perdidos, walkover)
- Resumen de sets
- Total de puntos y errores

### Análisis de Partido

```http
GET /api/estadisticas/partido/1
```

Muestra:
- Resultado del partido
- Sets ganados por cada equipo
- Comparativa de puntos y errores
- Desglose por tipo de acción para ambos equipos
- Identificar fortalezas y debilidades

### Rendimiento Individual

```http
GET /api/estadisticas/partido/1/jugador/5
```

Muestra:
- Contribución del jugador al partido
- Efectividad en cada tipo de acción
- Áreas de mejora (tipos de acción con más errores)

---

## 📈 Métricas Calculadas

### A Nivel de Sistema
- **Tasa de victoria**: `partidosGanados / totalPartidos * 100`
- **Efectividad en sets**: `setsGanados / totalSetsJugados * 100`
- **Ratio puntos/errores**: `totalPuntos / totalErrores`

### A Nivel de Partido
- **Efectividad del equipo local**: `puntosLocal / (puntosLocal + erroresLocal) * 100`
- **Efectividad del equipo visitante**: `puntosVisitante / (puntosVisitante + erroresVisitante) * 100`
- **Acción más efectiva**: Tipo de acción con más puntos
- **Acción más problemática**: Tipo de acción con más errores

### A Nivel de Jugador
- **Efectividad individual**: `totalPuntos / (totalPuntos + totalErrores) * 100`
- **Contribución al equipo**: `puntosJugador / puntosEquipo * 100`
- **Especialidad**: Tipo de acción con más puntos

---

## 🎯 Ejemplos de Visualización

### Gráfico de Torta - Resultados de Partidos
```
Ganados: 60%
Perdidos: 32%
Walkover: 4%
WalkoverContra: 4%
```

### Gráfico de Barras - Puntos por Tipo de Acción
```
Ataque:    ████████████████████ 45
Saque:     ████████████ 25
Bloqueo:   ████████ 15
Defensa:   █████ 10
```

### Tabla Comparativa - Equipo Local vs Visitante
```
Métrica          | Local | Visitante
-----------------|-------|----------
Puntos           | 95    | 78
Errores          | 18    | 22
Efectividad      | 84%   | 78%
Sets Ganados     | 3     | 1
```

### Ranking de Jugadores
```
Jugador              | Puntos | Errores | Efectividad
---------------------|--------|---------|------------
Juan Pérez           | 28     | 5       | 84.8%
María González       | 25     | 4       | 86.2%
Carlos Rodríguez     | 22     | 6       | 78.6%
```

---

## ⚠️ Manejo de Errores

### Códigos de respuesta:
- **200 OK**: Estadísticas obtenidas exitosamente
- **404 Not Found**: Partido o jugador no encontrado
- **500 Internal Server Error**: Error al calcular estadísticas

### Mensajes de error comunes:
- "Partido no encontrado"
- "No se encontraron acciones para el jugador en este partido"
- "Error al calcular estadísticas"

---

## 💡 Notas Importantes

1. **Rendimiento**: Las estadísticas se calculan en tiempo real, puede tomar unos segundos para partidos con muchas acciones
2. **Datos requeridos**: Para obtener estadísticas de un partido, debe tener al menos un set registrado
3. **Jugadores**: Solo se pueden obtener estadísticas de jugadores que hayan participado en el partido
4. **Ordenamiento**: Los resultados por tipo de acción están ordenados de mayor a menor cantidad
5. **Equipo visitante**: Las estadísticas del equipo visitante se calculan a partir de acciones con `idRoster = 0`

---

## 🔄 Flujo de Uso Típico

```bash
# 1. Ver estadísticas generales del dashboard
GET /api/estadisticas/generales

# 2. Analizar un partido específico
GET /api/estadisticas/partido/1

# 3. Ver rendimiento de un jugador en ese partido
GET /api/estadisticas/partido/1/jugador/5

# 4. Comparar con otro jugador
GET /api/estadisticas/partido/1/jugador/8
```

---

**Versión:** 1.0  
**Última actualización:** 2025-11-13
