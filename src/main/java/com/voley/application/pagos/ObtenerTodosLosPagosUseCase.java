package com.voley.application.pagos;

import com.voley.domain.Pago;
import com.voley.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener todos los pagos
 * Encapsula la lógica de negocio para la obtención de todos los pagos
 */
@Component
public class ObtenerTodosLosPagosUseCase {
    
    private final PagoService pagoService;
    
    @Autowired
    public ObtenerTodosLosPagosUseCase(PagoService pagoService) {
        this.pagoService = pagoService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener todos los pagos
     * 
     * @return Lista de todos los pagos registrados
     */
    public List<Pago> ejecutar() {
        return pagoService.obtenerTodosLosPagos();
    }
}