package com.voley.application.pagos;

import com.voley.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para verificar pagos en atraso
 * Encapsula la lógica de negocio para la verificación manual de pagos atrasados
 */
@Component
public class VerificarPagosEnAtrasoUseCase {
    
    private final PagoService pagoService;
    
    @Autowired
    public VerificarPagosEnAtrasoUseCase(PagoService pagoService) {
        this.pagoService = pagoService;
    }
    
    /**
     * Ejecuta el caso de uso para verificar y actualizar pagos en atraso
     */
    public void ejecutar() {
        pagoService.actualizarPagosEnAtraso();
    }
}