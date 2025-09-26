package com.voley.dto;

import com.voley.domain.Pago;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class PagoCreateDTO {
    
    @NotNull(message = "El usuario_id es obligatorio")
    private Long usuarioId;
    
    @NotNull(message = "El periodo_mes es obligatorio")
    @Min(value = 1, message = "El mes debe ser entre 1 y 12")
    @Max(value = 12, message = "El mes debe ser entre 1 y 12")
    private Integer periodoMes;
    
    @NotNull(message = "El periodo_anio es obligatorio")
    @Min(value = 2020, message = "El año debe ser válido")
    private Integer periodoAnio;
    
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;
    
    private String comprobante; // Opcional
    
    @NotNull(message = "El estado es obligatorio")
    private String estado;
    
    private String observaciones; // Opcional
    
    @NotNull(message = "El método de pago es obligatorio")
    private String metodoPago;
    
    // Constructor vacío
    public PagoCreateDTO() {}
    
    // Constructor completo
    public PagoCreateDTO(Long usuarioId, Integer periodoMes, Integer periodoAnio, 
                        BigDecimal monto, String comprobante, String estado, 
                        String observaciones, String metodoPago) {
        this.usuarioId = usuarioId;
        this.periodoMes = periodoMes;
        this.periodoAnio = periodoAnio;
        this.monto = monto;
        this.comprobante = comprobante;
        this.estado = estado;
        this.observaciones = observaciones;
        this.metodoPago = metodoPago;
    }
    
    // Getters y Setters
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Integer getPeriodoMes() {
        return periodoMes;
    }
    
    public void setPeriodoMes(Integer periodoMes) {
        this.periodoMes = periodoMes;
    }
    
    public Integer getPeriodoAnio() {
        return periodoAnio;
    }
    
    public void setPeriodoAnio(Integer periodoAnio) {
        this.periodoAnio = periodoAnio;
    }
    
    public BigDecimal getMonto() {
        return monto;
    }
    
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    
    public String getComprobante() {
        return comprobante;
    }
    
    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    // Método para validar el estado
    public boolean esEstadoValido() {
        if (estado == null) return false;
        try {
            Pago.EstadoPago.valueOf(estado.toLowerCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}