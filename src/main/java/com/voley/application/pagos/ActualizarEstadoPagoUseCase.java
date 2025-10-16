package com.voley.application.pagos;

import com.voley.domain.Pago;
import com.voley.repository.PagoRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para actualizar el estado de un pago (Clean Architecture)
 */
@Component
public class ActualizarEstadoPagoUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ActualizarEstadoPagoUseCase.class);
    
    private final PagoRepositoryPort pagoRepository;
    
    @Autowired
    public ActualizarEstadoPagoUseCase(PagoRepositoryPort pagoRepository) {
        this.pagoRepository = pagoRepository;
    }
    
    /**
     * Ejecuta la actualización del estado de un pago
     * 
     * @param pagoId ID del pago a actualizar
     * @param nuevoEstado Nuevo estado del pago
     * @param observaciones Observaciones adicionales
     * @return Pago actualizado
     * @throws IllegalArgumentException si el pago no existe o el estado es inválido
     */
    public Pago ejecutar(Long pagoId, Pago.EstadoPago nuevoEstado, String observaciones) {
        logger.info("Actualizando estado del pago ID: {} a estado: {}", pagoId, nuevoEstado);
        
        // Validaciones
        if (pagoId == null) {
            throw new IllegalArgumentException("El ID del pago es obligatorio");
        }
        
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado es obligatorio");
        }
        
        // Buscar el pago
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el pago con ID: " + pagoId));
        
        // Validar que no se esté intentando marcar como pagado usando este método
        if (nuevoEstado == Pago.EstadoPago.pagado) {
            throw new IllegalArgumentException("Para marcar un pago como pagado debe usar el endpoint /procesar con monto y método de pago");
        }
        
        // Actualizar el estado
        Pago.EstadoPago estadoAnterior = pago.getEstado();
        pago.setEstado(nuevoEstado);
        
        // Actualizar observaciones si se proporcionan
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            String observacionesExistentes = pago.getObservaciones();
            if (observacionesExistentes != null && !observacionesExistentes.trim().isEmpty()) {
                pago.setObservaciones(observacionesExistentes + " | " + observaciones.trim());
            } else {
                pago.setObservaciones(observaciones.trim());
            }
        }
        
        // Guardar los cambios
        Pago pagoActualizado = pagoRepository.save(pago);
        
        logger.info("Estado del pago ID: {} actualizado de {} a {}", 
                   pagoId, estadoAnterior, nuevoEstado);
        
        return pagoActualizado;
    }
}