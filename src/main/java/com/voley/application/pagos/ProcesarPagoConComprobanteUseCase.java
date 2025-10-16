package com.voley.application.pagos;

import com.voley.domain.Pago;
import com.voley.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para procesar el pago de un usuario con comprobante
 * Encapsula la lógica de negocio para marcar un pago como pagado incluyendo el comprobante
 */
@Component
public class ProcesarPagoConComprobanteUseCase {
    
    private final PagoService pagoService;
    
    @Autowired
    public ProcesarPagoConComprobanteUseCase(PagoService pagoService) {
        this.pagoService = pagoService;
    }
    
    /**
     * Ejecuta el caso de uso para procesar un pago con comprobante
     * 
     * @param pagoId El ID del pago a procesar
     * @param monto El monto pagado
     * @param metodoPago El método de pago utilizado
     * @param rutaComprobante La ruta del archivo de comprobante (opcional)
     * @return El pago actualizado con estado "pagado"
     * @throws IllegalArgumentException Si los parámetros son inválidos
     */
    public Pago ejecutar(Long pagoId, Double monto, String metodoPago, String rutaComprobante) {
        validarParametros(pagoId, monto, metodoPago);
        return pagoService.marcarComoPagadoConComprobante(pagoId, monto, metodoPago, rutaComprobante);
    }
    
    /**
     * Ejecuta el caso de uso para procesar un pago sin comprobante (compatibilidad)
     */
    public Pago ejecutar(Long pagoId, Double monto, String metodoPago) {
        return ejecutar(pagoId, monto, metodoPago, null);
    }
    
    private void validarParametros(Long pagoId, Double monto, String metodoPago) {
        if (pagoId == null) {
            throw new IllegalArgumentException("El ID del pago no puede ser nulo");
        }
        
        if (pagoId <= 0) {
            throw new IllegalArgumentException("El ID del pago debe ser un número positivo");
        }
        
        if (monto == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El método de pago no puede estar vacío");
        }
    }
}