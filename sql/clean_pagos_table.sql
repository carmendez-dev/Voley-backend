-- Script para limpiar la tabla pagos y empezar de cero
-- CUIDADO: Esto eliminará todos los registros de pagos

USE voley;

-- Ver cuántos registros hay
SELECT COUNT(*) AS total_pagos FROM pagos;

-- Limpiar todos los pagos
DELETE FROM pagos;

-- Verificar que esté vacía
SELECT COUNT(*) AS total_pagos_despues FROM pagos;

-- Reiniciar el auto_increment si es necesario
ALTER TABLE pagos AUTO_INCREMENT = 1;