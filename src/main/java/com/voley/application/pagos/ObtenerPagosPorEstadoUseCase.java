package com.voley.application.pagos;

import com.voley.domain.Pago;
import com.voley.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener pagos por estado
 * Encapsula la lógica de negocio para la búsqueda de pagos por estado
 */
@Component
public class ObtenerPagosPorEstadoUseCase {
    
    private final PagoService pagoService;
    
    @Autowired
    public ObtenerPagosPorEstadoUseCase(PagoService pagoService) {
        this.pagoService = pagoService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener pagos por estado
     * 
     * @param estado El estado de los pagos a buscar
     * @return Lista de pagos con el estado especificado
     * @throws IllegalArgumentException Si el estado es inválido
     */
    public List<Pago> ejecutar(Pago.EstadoPago estado) {
        validarEstado(estado);
        return pagoService.obtenerPagosPorEstado(estado);
    }
    
    private void validarEstado(Pago.EstadoPago estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado del pago no puede ser nulo");
        }
    }
}