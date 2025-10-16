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
        // Asignar valores automáticos antes de validar
        asignarValoresAutomaticos(pago);
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
    
    /**
     * Asigna valores automáticos al pago antes de la validación
     */
    private void asignarValoresAutomaticos(Pago pago) {
        if (pago == null) {
            return;
        }
        
        // Asignar fecha de vencimiento automáticamente si no se proporciona
        if (pago.getFechaVencimiento() == null) {
            if (pago.getPeriodoMes() != null && pago.getPeriodoAnio() != null) {
                // Crear fecha del período (primer día del mes)
                LocalDate fechaPeriodo = LocalDate.of(pago.getPeriodoAnio(), pago.getPeriodoMes(), 1);
                // Calcular fecha de vencimiento (último día del mes)
                LocalDate fechaVencimiento = fechaPeriodo.withDayOfMonth(fechaPeriodo.lengthOfMonth());
                pago.setFechaVencimiento(fechaVencimiento);
            }
        }
        
        // Asignar fecha de registro automáticamente si no se proporciona
        if (pago.getFechaRegistro() == null) {
            pago.setFechaRegistro(LocalDate.now());
        }
        
        // Si el estado es "pagado", establecer fecha_pago automáticamente
        if (Pago.EstadoPago.pagado.equals(pago.getEstado()) && pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDate.now());
        }
    }
}