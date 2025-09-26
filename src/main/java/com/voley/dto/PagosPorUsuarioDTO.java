package com.voley.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PagosPorUsuarioDTO {
    
    private UsuarioInfoDTO usuario;
    private Map<String, Object> resumen;
    private List<PagoDetalleDTO> pagos;
    
    // Constructor vacío
    public PagosPorUsuarioDTO() {
    }
    
    // Getters y Setters
    public UsuarioInfoDTO getUsuario() { return usuario; }
    public void setUsuario(UsuarioInfoDTO usuario) { this.usuario = usuario; }
    
    public Map<String, Object> getResumen() { return resumen; }
    public void setResumen(Map<String, Object> resumen) { this.resumen = resumen; }
    
    public List<PagoDetalleDTO> getPagos() { return pagos; }
    public void setPagos(List<PagoDetalleDTO> pagos) { this.pagos = pagos; }
    
    // Clase interna para información del usuario
    public static class UsuarioInfoDTO {
        private Long id;
        private String nombreCompleto;
        private String email;
        private String celular;
        private String estado;
        
        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getNombreCompleto() { return nombreCompleto; }
        public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getCelular() { return celular; }
        public void setCelular(String celular) { this.celular = celular; }
        
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
    
    // Clase interna para detalle de pagos
    public static class PagoDetalleDTO {
        private Long id;
        private String periodo;
        private BigDecimal monto;
        private String estado;
        private String fechaVencimiento;
        private String fechaPago;
        private String metodoPago;
        private String comprobante;
        private String observaciones;
        private boolean vencido;
        private int diasVencimiento;
        private String estadoTexto;
        private String fechaFormateada;
        
        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getPeriodo() { return periodo; }
        public void setPeriodo(String periodo) { this.periodo = periodo; }
        
        public BigDecimal getMonto() { return monto; }
        public void setMonto(BigDecimal monto) { this.monto = monto; }
        
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        
        public String getFechaVencimiento() { return fechaVencimiento; }
        public void setFechaVencimiento(String fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
        
        public String getFechaPago() { return fechaPago; }
        public void setFechaPago(String fechaPago) { this.fechaPago = fechaPago; }
        
        public String getMetodoPago() { return metodoPago; }
        public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
        
        public String getComprobante() { return comprobante; }
        public void setComprobante(String comprobante) { this.comprobante = comprobante; }
        
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
        
        public boolean isVencido() { return vencido; }
        public void setVencido(boolean vencido) { this.vencido = vencido; }
        
        public int getDiasVencimiento() { return diasVencimiento; }
        public void setDiasVencimiento(int diasVencimiento) { this.diasVencimiento = diasVencimiento; }
        
        public String getEstadoTexto() { return estadoTexto; }
        public void setEstadoTexto(String estadoTexto) { this.estadoTexto = estadoTexto; }
        
        public String getFechaFormateada() { return fechaFormateada; }
        public void setFechaFormateada(String fechaFormateada) { this.fechaFormateada = fechaFormateada; }
    }
}