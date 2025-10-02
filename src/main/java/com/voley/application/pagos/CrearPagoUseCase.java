package com.voley.application.pagos;

import com.voley.domain.Pago;
import com.voley.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Caso de uso para crear un nuevo pago
 * Encapsula la lógica de negocio para la creación de pagos
 */
@Component
public class CrearPagoUseCase {
    
    private final PagoService pagoService;
    
    @Autowired
    public CrearPagoUseCase(PagoService pagoService) {
        this.pagoService = pagoService;
    }
    
    /**
     * Ejecuta el caso de uso para crear un pago
     * 
     * @param pago El pago a crear
     * @return El pago creado
     * @throws IllegalArgumentException Si los datos del pago son inválidos
     */
    public Pago ejecutar(Pago pago) {
        validarPago(pago);
        return pagoService.crearPago(pago);
    }
    
    private void validarPago(Pago pago) {
        if (pago == null) {
            throw new IllegalArgumentException("El pago no puede ser nulo");
        }
        
        if (pago.getUsuario() == null) {
            throw new IllegalArgumentException("El usuario del pago no puede ser nulo");
        }
        
        if (pago.getMonto() == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        
        if (pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        
        if (pago.getFechaVencimiento() == null) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser nula");
        }
        
        if (pago.getFechaVencimiento().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha actual");
        }
    }
}