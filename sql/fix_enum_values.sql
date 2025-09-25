-- Script para corregir los valores del enum EstadoPago en la base de datos
-- Convierte los valores en minúsculas a mayúsculas para coincidir con el enum Java

USE voley;

-- Ver los valores actuales
SELECT estado, COUNT(*) as cantidad 
FROM pagos 
GROUP BY estado;

-- Actualizar los estados a mayúsculas
UPDATE pagos SET estado = 'ATRASO' WHERE estado = 'atraso';
UPDATE pagos SET estado = 'PENDIENTE' WHERE estado = 'pendiente';
UPDATE pagos SET estado = 'PAGADO' WHERE estado = 'pagado';
UPDATE pagos SET estado = 'RECHAZADO' WHERE estado = 'rechazado';

-- Verificar los cambios
SELECT estado, COUNT(*) as cantidad 
FROM pagos 
GROUP BY estado;

-- Opcional: Ver todos los registros
-- SELECT * FROM pagos;