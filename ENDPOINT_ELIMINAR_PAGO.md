# 🗑️ Endpoint para Eliminar Pagos

## Nuevo Endpoint Creado: DELETE /api/pagos/{id}

### ✅ **Funcionalidad Implementada**

Este endpoint permite eliminar un pago, pero **SOLO** si el pago no está en estado "pagado".

### 📋 **Características del Endpoint**

#### **URL**
```
DELETE /api/pagos/{id}
```

#### **Parámetros**
- `id` (Path Variable): ID del pago a eliminar

#### **Casos de Uso**
- ✅ **Elimina pagos pendientes**: Pagos con estado "pendiente"
- ✅ **Elimina pagos en atraso**: Pagos con estado "atraso"  
- ✅ **Elimina pagos rechazados**: Pagos con estado "rechazado"
- ❌ **NO elimina pagos pagados**: Pagos con estado "pagado" (protegidos)

#### **Respuestas**

##### **✅ Eliminación Exitosa (200 OK)**
```json
{
    "success": true,
    "message": "Pago eliminado exitosamente",
    "timestamp": "2025-10-02",
    "pagoId": 123
}
```

##### **❌ Error de Validación (400 Bad Request)**
```json
{
    "success": false,
    "message": "El ID del pago debe ser un número positivo",
    "timestamp": "2025-10-02",
    "pagoId": -1
}
```

##### **⚠️ Pago ya Pagado (409 Conflict)**
```json
{
    "success": false,
    "message": "No se puede eliminar un pago que ya está pagado",
    "timestamp": "2025-10-02",
    "pagoId": 123
}
```

##### **🔍 Pago No Encontrado (400 Bad Request)**
```json
{
    "success": false,
    "message": "No existe un pago con el ID proporcionado",
    "timestamp": "2025-10-02",
    "pagoId": 999
}
```

##### **💥 Error Interno (500 Internal Server Error)**
```json
{
    "success": false,
    "message": "Error interno del servidor",
    "timestamp": "2025-10-02",
    "pagoId": 123
}
```

### 🔧 **Implementación con Casos de Uso**

#### **Caso de Uso: EliminarPagoUseCase**
- **Archivo**: `EliminarPagoUseCase.java`
- **Paquete**: `com.voley.application.pagos`
- **Validaciones**:
  1. Verifica que el ID sea válido
  2. Verifica que el pago exista
  3. Verifica que el pago no esté en estado "pagado"

#### **Controlador Actualizado**
- **Archivo**: `PagoController.java`
- **Nuevo método**: `eliminarPago(@PathVariable Long id)`
- **Inyección**: `EliminarPagoUseCase` como dependencia

### 🛡️ **Protecciones Implementadas**

1. **Validación de ID**: Solo acepta IDs positivos válidos
2. **Verificación de Existencia**: Confirma que el pago existe antes de eliminar
3. **Protección de Pagos**: No permite eliminar pagos ya procesados
4. **Logging**: Registra todas las operaciones para auditoría
5. **Manejo de Excepciones**: Respuestas HTTP apropiadas para cada caso

### 🧪 **Pruebas Recomendadas**

#### **Casos de Prueba**
1. **Eliminar pago pendiente** → Debe funcionar ✅
2. **Eliminar pago en atraso** → Debe funcionar ✅  
3. **Intentar eliminar pago pagado** → Debe fallar con 409 ❌
4. **ID inválido (negativo)** → Debe fallar con 400 ❌
5. **ID inexistente** → Debe fallar con 400 ❌

#### **Comandos curl de Ejemplo**
```bash
# Eliminar un pago pendiente (debería funcionar)
curl -X DELETE http://localhost:8080/api/pagos/1

# Intentar eliminar un pago pagado (debería fallar)
curl -X DELETE http://localhost:8080/api/pagos/2

# ID inválido (debería fallar)  
curl -X DELETE http://localhost:8080/api/pagos/-1
```

### 📊 **Arquitectura Utilizada**

```
HTTP Request → PagoController → EliminarPagoUseCase → PagoService → Repository
```

Esta implementación sigue los principios de **Clean Architecture** manteniendo la lógica de negocio en el caso de uso y dejando al controlador solo el manejo de HTTP.

## 🔗 **Endpoints de Pagos Disponibles**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/pagos` | Obtener todos los pagos |
| POST | `/api/pagos` | Crear un nuevo pago |
| GET | `/api/pagos/{id}` | Obtener pago por ID |
| PUT | `/api/pagos/{id}/pagar` | Marcar pago como pagado |
| PUT | `/api/pagos/verificar-atrasos` | Verificar pagos atrasados |
| **DELETE** | **`/api/pagos/{id}`** | **🆕 Eliminar pago (nuevo)** |