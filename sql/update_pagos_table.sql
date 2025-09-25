-- Script para actualizar la tabla pagos con las nuevas columnas

USE voley;

-- Agregar las columnas faltantes a la tabla pagos
ALTER TABLE pagos 
ADD COLUMN descripcion VARCHAR(500) NULL AFTER observaciones,
ADD COLUMN fecha_vencimiento DATE NULL AFTER fecha_registro,
ADD COLUMN fecha_pago DATE NULL AFTER fecha_vencimiento,
ADD COLUMN metodo_pago VARCHAR(50) NULL AFTER fecha_pago;

-- Verificar la estructura actualizada
DESCRIBE pagos;

-- Opcional: Agregar algunos datos de prueba
-- INSERT INTO pagos (usuario_id, usuario_nombre, periodo_mes, periodo_anio, monto, estado, fecha_registro, fecha_vencimiento, descripcion)
-- VALUES (1, 'Juan Pérez', 9, 2025, 50000.00, 'PENDIENTE', '2025-09-01', '2025-09-30', 'Cuota mensual - SEPTEMBER 2025');