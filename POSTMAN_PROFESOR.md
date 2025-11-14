# 📮 Guía Postman - API Profesor

## 🌐 Configuración Base

**Base URL:** `http://localhost:8080/api/profesores`

---

## 📋 ENDPOINTS DISPONIBLES

### 1️⃣ Crear Profesor
**POST** `/api/profesores`

Crea un nuevo profesor en el sistema con contraseña encriptada.

**Request Body:**
```json
{
  "primerNombre": "Juan",
  "segundoNombre": "Carlos",
  "primerApellido": "Pérez",
  "segundoApellido": "García",
  "email": "juan.perez@universidad.edu",
  "celular": "3001234567",
  "contactoEmergencia": "María Pérez - 3007654321",
  "genero": "Masculino",
  "cedula": "12345678",
  "password": "miPassword123"
}
```

**Campos Obligatorios:**
- `primerNombre` (String, máx. 50 caracteres)
- `primerApellido` (String, máx. 50 caracteres)
- `genero` (String: "Masculino" o "Femenino")
- `password` (String, mín. 6 caracteres)

**Campos Opcionales:**
- `segundoNombre`, `segundoApellido`, `email`, `celular`, `contactoEmergencia`, `cedula`

**Response 201 Created:**
```json
{
  "success": true,
  "message": "Profesor creado exitosamente",
  "timestamp": "2025-11-13T22:45:00",
  "data": {
    "idProfesor": 1,
    "primerNombre": "Juan",
    "segundoNombre": "Carlos",
    "primerApellido": "Pérez",
    "segundoApellido": "García",
    "email": "juan.perez@universidad.edu",
    "celular": "3001234567",
    "contactoEmergencia": "María Pérez - 3007654321",
    "genero": "Masculino",
    "estado": "Activo",
    "cedula": "12345678",
    "fechaRegistro": "2025-11-13T22:45:00",
    "updateAt": "2025-11-13T22:45:00",
    "nombreCompleto": "Juan Carlos Pérez García"
  }
}
```

**Errores Comunes:**
```json
// 400 Bad Request - Cédula duplicada
{
  "success": false,
  "message": "Ya existe un profesor con la cédula: 12345678",
  "timestamp": "2025-11-13T22:45:00"
}

// 400 Bad Request - Email duplicado
{
  "success": false,
  "message": "Ya existe un profesor con el email: juan.perez@universidad.edu",
  "timestamp": "2025-11-13T22:45:00"
}
```

---

### 2️⃣ Obtener Todos los Profesores
**GET** `/api/profesores`

Obtiene la lista completa de profesores registrados.

**Request:**
```
GET http://localhost:8080/api/profesores
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Profesores obtenidos exitosamente",
  "timestamp": "2025-11-13T22:45:00",
  "total": 3,
  "data": [
    {
      "idProfesor": 1,
      "primerNombre": "Juan",
      "segundoNombre": "Carlos",
      "primerApellido": "Pérez",
      "segundoApellido": "García",
      "email": "juan.perez@universidad.edu",
      "celular": "3001234567",
      "contactoEmergencia": "María Pérez - 3007654321",
      "genero": "Masculino",
      "estado": "Activo",
      "cedula": "12345678",
      "fechaRegistro": "2025-11-13T22:45:00",
      "updateAt": "2025-11-13T22:45:00",
      "nombreCompleto": "Juan Carlos Pérez García"
    },
    {
      "idProfesor": 2,
      "primerNombre": "María",
      "primerApellido": "López",
      "email": "maria.lopez@universidad.edu",
      "genero": "Femenino",
      "estado": "Activo",
      "cedula": "87654321",
      "nombreCompleto": "María López"
    }
  ]
}
```

---

### 3️⃣ Filtrar Profesores por Estado
**GET** `/api/profesores?estado={estado}`

Filtra profesores según su estado.

**Parámetros Query:**
- `estado` (String): "Activo" o "Inactivo"

**Ejemplos:**

**Profesores Activos:**
```
GET http://localhost:8080/api/profesores?estado=Activo
```

**Profesores Inactivos:**
```
GET http://localhost:8080/api/profesores?estado=Inactivo
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Profesores obtenidos exitosamente",
  "timestamp": "2025-11-13T22:45:00",
  "total": 2,
  "data": [
    {
      "idProfesor": 1,
      "primerNombre": "Juan",
      "primerApellido": "Pérez",
      "estado": "Activo",
      "nombreCompleto": "Juan Carlos Pérez García"
    }
  ]
}
```

**Error 400 Bad Request:**
```json
{
  "success": false,
  "message": "Estado inválido. Use: Activo o Inactivo",
  "timestamp": "2025-11-13T22:45:00"
}
```

---

### 4️⃣ Obtener Profesor por ID
**GET** `/api/profesores/{id}`

Obtiene un profesor específico por su ID.

**Parámetros Path:**
- `id` (Integer): ID del profesor

**Ejemplo:**
```
GET http://localhost:8080/api/profesores/1
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Profesor encontrado",
  "timestamp": "2025-11-13T22:45:00",
  "data": {
    "idProfesor": 1,
    "primerNombre": "Juan",
    "segundoNombre": "Carlos",
    "primerApellido": "Pérez",
    "segundoApellido": "García",
    "email": "juan.perez@universidad.edu",
    "celular": "3001234567",
    "contactoEmergencia": "María Pérez - 3007654321",
    "genero": "Masculino",
    "estado": "Activo",
    "cedula": "12345678",
    "fechaRegistro": "2025-11-13T22:45:00",
    "updateAt": "2025-11-13T22:45:00",
    "nombreCompleto": "Juan Carlos Pérez García"
  }
}
```

**Error 404 Not Found:**
```json
{
  "success": false,
  "message": "Profesor no encontrado con ID: 999",
  "timestamp": "2025-11-13T22:45:00"
}
```

---

### 5️⃣ Obtener Profesor por Cédula (Carnet)
**GET** `/api/profesores/cedula/{cedula}`

Busca un profesor por su número de cédula o carnet.

**Parámetros Path:**
- `cedula` (String): Número de cédula del profesor

**Ejemplo:**
```
GET http://localhost:8080/api/profesores/cedula/12345678
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Profesor encontrado",
  "timestamp": "2025-11-13T22:45:00",
  "data": {
    "idProfesor": 1,
    "primerNombre": "Juan",
    "primerApellido": "Pérez",
    "cedula": "12345678",
    "estado": "Activo",
    "nombreCompleto": "Juan Carlos Pérez García"
  }
}
```

**Error 404 Not Found:**
```json
{
  "success": false,
  "message": "Profesor no encontrado con cédula: 99999999",
  "timestamp": "2025-11-13T22:45:00"
}
```

---

### 6️⃣ Actualizar Profesor
**PUT** `/api/profesores/{id}`

Actualiza la información de un profesor existente.

**Parámetros Path:**
- `id` (Integer): ID del profesor a actualizar

**Request Body (todos los campos son opcionales):**
```json
{
  "primerNombre": "Juan Carlos",
  "segundoNombre": "Alberto",
  "primerApellido": "Pérez",
  "segundoApellido": "García",
  "email": "juancarlos.perez@universidad.edu",
  "celular": "3009876543",
  "contactoEmergencia": "Ana Pérez - 3001112233",
  "genero": "Masculino",
  "estado": "Activo",
  "cedula": "12345678"
}
```

**Ejemplo:**
```
PUT http://localhost:8080/api/profesores/1
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Profesor actualizado exitosamente",
  "timestamp": "2025-11-13T22:45:00",
  "data": {
    "idProfesor": 1,
    "primerNombre": "Juan Carlos",
    "segundoNombre": "Alberto",
    "primerApellido": "Pérez",
    "segundoApellido": "García",
    "email": "juancarlos.perez@universidad.edu",
    "celular": "3009876543",
    "contactoEmergencia": "Ana Pérez - 3001112233",
    "genero": "Masculino",
    "estado": "Activo",
    "cedula": "12345678",
    "updateAt": "2025-11-13T22:50:00",
    "nombreCompleto": "Juan Carlos Alberto Pérez García"
  }
}
```

**Errores:**
```json
// 404 Not Found
{
  "success": false,
  "message": "No existe profesor con ID: 999",
  "timestamp": "2025-11-13T22:45:00"
}

// 400 Bad Request - Cédula duplicada
{
  "success": false,
  "message": "Ya existe un profesor con la cédula: 87654321",
  "timestamp": "2025-11-13T22:45:00"
}
```

---

### 7️⃣ Cambiar Contraseña por Cédula ⭐
**PUT** `/api/profesores/cedula/{cedula}/password`

**Endpoint especial** para cambiar la contraseña de un profesor buscándolo por su cédula (carnet).

**Parámetros Path:**
- `cedula` (String): Número de cédula del profesor

**Request Body:**
```json
{
  "password": "nuevaPassword456"
}
```

**Ejemplo:**
```
PUT http://localhost:8080/api/profesores/cedula/12345678/password
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Contraseña actualizada exitosamente",
  "timestamp": "2025-11-13T22:45:00"
}
```

**Errores:**
```json
// 404 Not Found - Profesor no existe
{
  "success": false,
  "message": "No existe profesor con cédula: 99999999",
  "timestamp": "2025-11-13T22:45:00"
}

// 400 Bad Request - Contraseña vacía
{
  "success": false,
  "message": "La nueva contraseña es obligatoria",
  "timestamp": "2025-11-13T22:45:00"
}

// 400 Bad Request - Contraseña muy corta
{
  "success": false,
  "message": "La contraseña debe tener al menos 6 caracteres",
  "timestamp": "2025-11-13T22:45:00"
}
```

---

### 8️⃣ Eliminar Profesor
**DELETE** `/api/profesores/{id}`

Elimina un profesor del sistema.

**Parámetros Path:**
- `id` (Integer): ID del profesor a eliminar

**Ejemplo:**
```
DELETE http://localhost:8080/api/profesores/1
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Profesor eliminado exitosamente",
  "timestamp": "2025-11-13T22:45:00"
}
```

**Error 404 Not Found:**
```json
{
  "success": false,
  "message": "No existe profesor con ID: 999",
  "timestamp": "2025-11-13T22:45:00"
}
```

---

## 🧪 COLECCIÓN DE PRUEBAS COMPLETA

### Flujo de Prueba Recomendado:

#### **Paso 1: Crear Profesor**
```http
POST http://localhost:8080/api/profesores
Content-Type: application/json

{
  "primerNombre": "Ana",
  "segundoNombre": "María",
  "primerApellido": "Rodríguez",
  "segundoApellido": "Martínez",
  "email": "ana.rodriguez@universidad.edu",
  "celular": "3101234567",
  "contactoEmergencia": "Pedro Rodríguez - 3107654321",
  "genero": "Femenino",
  "cedula": "11223344",
  "password": "password123"
}
```

#### **Paso 2: Obtener Todos**
```http
GET http://localhost:8080/api/profesores
```

#### **Paso 3: Buscar por Cédula**
```http
GET http://localhost:8080/api/profesores/cedula/11223344
```

#### **Paso 4: Actualizar Información**
```http
PUT http://localhost:8080/api/profesores/1
Content-Type: application/json

{
  "celular": "3109999999",
  "email": "ana.m.rodriguez@universidad.edu"
}
```

#### **Paso 5: Cambiar Contraseña por Cédula**
```http
PUT http://localhost:8080/api/profesores/cedula/11223344/password
Content-Type: application/json

{
  "password": "nuevaPassword789"
}
```

#### **Paso 6: Filtrar por Estado**
```http
GET http://localhost:8080/api/profesores?estado=Activo
```

#### **Paso 7: Obtener por ID**
```http
GET http://localhost:8080/api/profesores/1
```

---

## 📊 CASOS DE PRUEBA ADICIONALES

### Crear Profesor con Datos Mínimos
```json
{
  "primerNombre": "Carlos",
  "primerApellido": "Gómez",
  "genero": "Masculino",
  "password": "pass123"
}
```

### Crear Profesor Completo
```json
{
  "primerNombre": "Laura",
  "segundoNombre": "Patricia",
  "primerApellido": "Fernández",
  "segundoApellido": "Silva",
  "email": "laura.fernandez@universidad.edu",
  "celular": "3201234567",
  "contactoEmergencia": "Jorge Fernández - 3207654321",
  "genero": "Femenino",
  "cedula": "55667788",
  "password": "securePass456"
}
```

### Cambiar Estado a Inactivo
```json
{
  "estado": "Inactivo"
}
```

---

## ⚠️ NOTAS IMPORTANTES

1. **Seguridad de Contraseñas:**
   - Las contraseñas se encriptan automáticamente usando SHA-256
   - Nunca se devuelven en las respuestas de la API
   - Longitud mínima: 6 caracteres

2. **Validaciones:**
   - La cédula debe ser única en el sistema
   - El email debe ser único si se proporciona
   - Los campos de nombre tienen límite de 50 caracteres
   - El género solo acepta: "Masculino" o "Femenino"
   - El estado solo acepta: "Activo" o "Inactivo"

3. **Endpoint Especial:**
   - `/cedula/{cedula}/password` permite cambiar contraseña sin conocer el ID
   - Útil para sistemas donde los profesores solo conocen su carnet

4. **Campo Calculado:**
   - `nombreCompleto` se genera automáticamente concatenando nombres y apellidos

5. **Timestamps:**
   - `fechaRegistro` se establece automáticamente al crear
   - `updateAt` se actualiza automáticamente en cada modificación

---

## 🔧 CONFIGURACIÓN EN POSTMAN

### Variables de Entorno Sugeridas:
```
base_url = http://localhost:8080
api_path = /api/profesores
```

### Headers Globales:
```
Content-Type: application/json
Accept: application/json
```

---

## 📝 EJEMPLOS DE RESPUESTAS DE ERROR

### Error de Validación
```json
{
  "success": false,
  "message": "Ya existe un profesor con la cédula: 12345678",
  "timestamp": "2025-11-13T22:45:00"
}
```

### Error de Servidor
```json
{
  "success": false,
  "message": "Error interno del servidor",
  "timestamp": "2025-11-13T22:45:00"
}
```

### Recurso No Encontrado
```json
{
  "success": false,
  "message": "Profesor no encontrado con ID: 999",
  "timestamp": "2025-11-13T22:45:00"
}
```

---

## ✅ CHECKLIST DE PRUEBAS

- [ ] Crear profesor con datos completos
- [ ] Crear profesor con datos mínimos
- [ ] Obtener todos los profesores
- [ ] Filtrar por estado Activo
- [ ] Filtrar por estado Inactivo
- [ ] Buscar por ID existente
- [ ] Buscar por ID inexistente
- [ ] Buscar por cédula existente
- [ ] Buscar por cédula inexistente
- [ ] Actualizar información del profesor
- [ ] Cambiar contraseña por cédula
- [ ] Intentar crear con cédula duplicada
- [ ] Intentar crear con email duplicado
- [ ] Cambiar estado a Inactivo
- [ ] Eliminar profesor

---

**Documentación generada para:** Módulo Profesor - Sistema Voley Backend  
**Versión:** 1.0  
**Fecha:** Noviembre 2025
