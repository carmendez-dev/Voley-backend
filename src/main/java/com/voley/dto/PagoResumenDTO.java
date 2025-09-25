package com.voley.dto;

import com.voley.domain.Pago;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para mostrar información resumida de los pagos
 * Incluye solo los campos necesarios para la vista de todos los pagos
 */
public class PagoResumenDTO {
    
    private Long id;
    private String nombreUsuario;
    private String cedulaUsuario;
    private String periodoMesAnio;
    private BigDecimal monto;
    private String comprobante;
    private LocalDate fechaRegistro;
    private String estadoPago;
    private String observaciones;
    
    // Constructor vacío
    public PagoResumenDTO() {}
    
    // Constructor que convierte desde Pago
    public PagoResumenDTO(Pago pago) {
        this.id = pago.getId();
        this.nombreUsuario = pago.getUsuarioNombre();
        // Usar el campo usuario_cedula directamente si está disponible, sino obtener del usuario
        try {
            this.cedulaUsuario = pago.getUsuario() != null ? pago.getUsuario().getCedula() : "N/A";
        } catch (Exception e) {
            this.cedulaUsuario = "N/A";
        }
        this.periodoMesAnio = obtenerNombreMes(pago.getPeriodoMes()) + " " + pago.getPeriodoAnio();
        this.monto = pago.getMonto();
        this.comprobante = pago.getComprobante();
        this.fechaRegistro = pago.getFechaRegistro();
        this.estadoPago = pago.getEstado() != null ? pago.getEstado().name() : "pendiente";
        this.observaciones = pago.getObservaciones();
    }
    
    // Método auxiliar para convertir número de mes a nombre
    private String obtenerNombreMes(Integer mes) {
        if (mes == null) return "N/A";
        
        String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        
        if (mes >= 1 && mes <= 12) {
            return meses[mes - 1];
        }
        return "Mes " + mes;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public String getCedulaUsuario() {
        return cedulaUsuario;
    }
    
    public void setCedulaUsuario(String cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }
    
    public String getPeriodoMesAnio() {
        return periodoMesAnio;
    }
    
    public void setPeriodoMesAnio(String periodoMesAnio) {
        this.periodoMesAnio = periodoMesAnio;
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
    
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public String getEstadoPago() {
        return estadoPago;
    }
    
    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}