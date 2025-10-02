package com.voley.application.pagos;

import com.voley.domain.Pago;
import com.voley.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Caso de uso para eliminar un pago
 * Encapsula la lógica de negocio para eliminar pagos que no estén pagados
 */
@Component
public class EliminarPagoUseCase {
    
    private final PagoService pagoService;
    
    @Autowired
    public EliminarPagoUseCase(PagoService pagoService) {
        this.pagoService = pagoService;
    }
    
    /**
     * Ejecuta el caso de uso para eliminar un pago
     * Solo permite eliminar pagos que no estén en estado PAGADO
     * 
     * @param pagoId El ID del pago a eliminar
     * @return true si el pago fue eliminado exitosamente, false si no se pudo eliminar
     * @throws IllegalArgumentException Si el ID del pago es inválido
     * @throws IllegalStateException Si el pago está en estado PAGADO
     */
    public boolean ejecutar(Long pagoId) {
        validarPagoId(pagoId);
        
        // Verificar que el pago existe y obtener su estado
        Optional<Pago> pagoOptional = pagoService.obtenerPagoPorId(pagoId);
        
        if (pagoOptional.isEmpty()) {
            throw new IllegalArgumentException("No existe un pago con el ID proporcionado");
        }
        
        Pago pago = pagoOptional.get();
        
        // Verificar que el pago no esté pagado
        if (pago.getEstado() == Pago.EstadoPago.pagado) {
            throw new IllegalStateException("No se puede eliminar un pago que ya está pagado");
        }
        
        // Proceder con la eliminación
        return pagoService.eliminarPago(pagoId);
    }
    
    private void validarPagoId(Long pagoId) {
        if (pagoId == null) {
            throw new IllegalArgumentException("El ID del pago no puede ser nulo");
        }
        
        if (pagoId <= 0) {
            throw new IllegalArgumentException("El ID del pago debe ser un número positivo");
        }
    }
}