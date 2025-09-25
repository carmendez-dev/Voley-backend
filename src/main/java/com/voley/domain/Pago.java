package com.voley.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;
    
    @Column(name = "usuario_nombre", nullable = false, length = 200)
    private String usuarioNombre;
    
    @Column(name = "periodo_mes", nullable = false)
    private Integer periodoMes;
    
    @Column(name = "periodo_anio", nullable = false)
    private Integer periodoAnio;
    
    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;
    
    @Column(name = "comprobante")
    private String comprobante;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPago estado = EstadoPago.pendiente;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;
    
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
    
    @Column(name = "fecha_pago")
    private LocalDate fechaPago;
    
    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Enum para los estados de pago
    public enum EstadoPago {
        atraso, pendiente, pagado, rechazado
    }
    
    // Constructor vacío
    public Pago() {}
    
    // Constructor para crear pagos automáticos
    public Pago(Usuario usuario, Integer periodoMes, Integer periodoAnio, BigDecimal monto) {
        this.usuario = usuario;
        this.usuarioNombre = usuario.getNombreCompleto();
        this.periodoMes = periodoMes;
        this.periodoAnio = periodoAnio;
        this.monto = monto;
        this.estado = EstadoPago.pendiente;
        this.fechaRegistro = LocalDate.now();
    }
    
    // Métodos de callback para auditoría
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (fechaRegistro == null) {
            fechaRegistro = LocalDate.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Métodos de negocio
    public void marcarComoEnAtraso() {
        this.estado = EstadoPago.atraso;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void marcarComoPagado(String metodoPago) {
        this.estado = EstadoPago.pagado;
        this.metodoPago = metodoPago;
        this.fechaPago = LocalDate.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void marcarComoRechazado(String observaciones) {
        this.estado = EstadoPago.rechazado;
        this.observaciones = observaciones;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean esPendiente() {
        return EstadoPago.pendiente.equals(this.estado);
    }
    
    public boolean estaAtrasado() {
        return EstadoPago.atraso.equals(this.estado);
    }
    
    public boolean estaPagado() {
        return EstadoPago.pagado.equals(this.estado);
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            this.usuarioNombre = usuario.getNombreCompleto();
        }
    }
    
    public String getUsuarioNombre() {
        return usuarioNombre;
    }
    
    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
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
    
    public EstadoPago getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public LocalDate getFechaPago() {
        return fechaPago;
    }
    
    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}