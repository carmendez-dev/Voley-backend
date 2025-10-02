package com.voley.application.pagos;

import com.voley.domain.Pago;
import com.voley.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Caso de uso para obtener un pago por ID
 * Encapsula la lógica de negocio para la búsqueda de pagos por ID
 */
@Component
public class ObtenerPagoPorIdUseCase {
    
    private final PagoService pagoService;
    
    @Autowired
    public ObtenerPagoPorIdUseCase(PagoService pagoService) {
        this.pagoService = pagoService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener un pago por ID
     * 
     * @param id El ID del pago a buscar
     * @return Optional con el pago si existe, Optional.empty() si no existe
     * @throws IllegalArgumentException Si el ID es inválido
     */
    public Optional<Pago> ejecutar(Long id) {
        validarId(id);
        return pagoService.obtenerPagoPorId(id);
    }
    
    private void validarId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del pago no puede ser nulo");
        }
        
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del pago debe ser un número positivo");
        }
    }
}