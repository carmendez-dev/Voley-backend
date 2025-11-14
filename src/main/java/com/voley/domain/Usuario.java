package com.voley.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "primer_nombre", nullable = false, length = 100)
    private String primerNombre;
    
    @Column(name = "segundo_nombre", nullable = false, length = 100)
    private String segundoNombre = "";  // Default empty string para cumplir NOT NULL
    
    @Column(name = "tercer_nombre", nullable = false, length = 100)
    private String tercerNombre = "";   // Default empty string para cumplir NOT NULL
    
    @Column(name = "primer_apellido", nullable = false, length = 100)
    private String primerApellido;
    
    @Column(name = "segundo_apellido", nullable = false, length = 100)
    private String segundoApellido = "";  // Default empty string para cumplir NOT NULL

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    
    @Column(name = "cedula", nullable = false, unique = true, length = 20)
    private String cedula;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false, length = 20)
    private Genero genero;
    
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;
    
    @Column(name = "celular", nullable = false, length = 20)
    private String celular;
    
    @Column(name = "direccion", nullable = false, columnDefinition = "TEXT")
    private String direccion;
    
    @Column(name = "contacto_emergencia", nullable = false, length = 20)
    private String contactoEmergencia;
    
    @Column(name = "peso")
    private Float peso;
    
    @Column(name = "altura")
    private Float altura;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoUsuario estado = EstadoUsuario.Activo;
    
    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relación con pagos
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pago> pagos = new ArrayList<>();
    
    // Constructores
    public Usuario() {}
    
    // Constructor con campos individuales de nombres y apellidos
    public Usuario(String primerNombre, String segundoNombre, String tercerNombre,
                   String primerApellido, String segundoApellido, LocalDate fechaNacimiento, 
                   String cedula, Genero genero, String email, String celular, 
                   String direccion, String contactoEmergencia) {
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre != null ? segundoNombre : "";
        this.tercerNombre = tercerNombre != null ? tercerNombre : "";
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido != null ? segundoApellido : "";
        this.fechaNacimiento = fechaNacimiento;
        this.cedula = cedula;
        this.genero = genero;
        this.email = email;
        this.celular = celular;
        this.direccion = direccion;
        this.contactoEmergencia = contactoEmergencia;
        
        this.fechaRegistro = LocalDate.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor de compatibilidad (mantener para no romper código existente)
    public Usuario(String nombres, String apellidos, LocalDate fechaNacimiento, 
                   String cedula, Genero genero, String email, String celular, 
                   String direccion, String contactoEmergencia) {
        // Parsear nombres (asumiendo formato "Primer Segundo Tercer")
        String[] partesNombres = nombres.trim().split("\\s+");
        this.primerNombre = partesNombres.length > 0 ? partesNombres[0] : "";
        this.segundoNombre = partesNombres.length > 1 ? partesNombres[1] : "";
        this.tercerNombre = partesNombres.length > 2 ? partesNombres[2] : "";
        
        // Parsear apellidos (asumiendo formato "Primer Segundo")
        String[] partesApellidos = apellidos.trim().split("\\s+");
        this.primerApellido = partesApellidos.length > 0 ? partesApellidos[0] : "";
        this.segundoApellido = partesApellidos.length > 1 ? partesApellidos[1] : "";
        
        this.fechaNacimiento = fechaNacimiento;
        this.cedula = cedula;
        this.genero = genero;
        this.email = email;
        this.celular = celular;
        this.direccion = direccion;
        this.contactoEmergencia = contactoEmergencia;
        this.fechaRegistro = LocalDate.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Métodos auxiliares para construir nombres y apellidos completos
    public String getNombresCompletos() {
        StringBuilder sb = new StringBuilder();
        if (primerNombre != null && !primerNombre.trim().isEmpty()) {
            sb.append(primerNombre.trim());
        }
        if (segundoNombre != null && !segundoNombre.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(segundoNombre.trim());
        }
        if (tercerNombre != null && !tercerNombre.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(tercerNombre.trim());
        }
        return sb.toString();
    }
    
    public String getApellidosCompletos() {
        StringBuilder sb = new StringBuilder();
        if (primerApellido != null && !primerApellido.trim().isEmpty()) {
            sb.append(primerApellido.trim());
        }
        if (segundoApellido != null && !segundoApellido.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(segundoApellido.trim());
        }
        return sb.toString();
    }
    
    // Métodos de ciclo de vida JPA
    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDate.now();
        }
        updatedAt = LocalDateTime.now();
        
        // Asegurar valores por defecto para campos NOT NULL
        if (segundoNombre == null) segundoNombre = "";
        if (tercerNombre == null) tercerNombre = "";
        if (segundoApellido == null) segundoApellido = "";
        if (estado == null) estado = EstadoUsuario.Activo;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    // Getters y Setters para campos individuales de nombres
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { 
        this.primerNombre = primerNombre;
    }
    
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { 
        this.segundoNombre = segundoNombre;
    }
    
    public String getTercerNombre() { return tercerNombre; }
    public void setTercerNombre(String tercerNombre) { 
        this.tercerNombre = tercerNombre;
    }
    
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { 
        this.primerApellido = primerApellido;
    }
    
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { 
        this.segundoApellido = segundoApellido;
    }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    
    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getContactoEmergencia() { return contactoEmergencia; }
    public void setContactoEmergencia(String contactoEmergencia) { this.contactoEmergencia = contactoEmergencia; }
    
    public Float getPeso() { return peso; }
    public void setPeso(Float peso) { this.peso = peso; }
    
    public Float getAltura() { return altura; }
    public void setAltura(Float altura) { this.altura = altura; }
    

    
    public EstadoUsuario getEstado() { return estado; }
    public void setEstado(EstadoUsuario estado) { this.estado = estado; }
    
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Métodos de compatibilidad para código existente
    @Transient
    public String getNombres() { return getNombresCompletos(); }
    @Transient
    public void setNombres(String nombres) { 
        // Parsear y asignar nombres individuales
        String[] partes = nombres.trim().split("\\s+");
        this.primerNombre = partes.length > 0 ? partes[0] : "";
        this.segundoNombre = partes.length > 1 ? partes[1] : "";
        this.tercerNombre = partes.length > 2 ? partes[2] : "";
    }
    
    @Transient
    public String getApellidos() { 
        return getApellidosCompletos(); 
    }
    @Transient
    public void setApellidos(String apellidosParam) {
        // Parsear y asignar apellidos individuales
        String[] partes = apellidosParam.trim().split("\\s+");
        this.primerApellido = partes.length > 0 ? partes[0] : "";
        this.segundoApellido = partes.length > 1 ? partes[1] : "";
    }
    
    // Método de compatibilidad para createdAt
    @Transient
    public LocalDateTime getCreatedAt() { return updatedAt; } // Usar updatedAt como fallback
    @Transient
    public void setCreatedAt(LocalDateTime createdAt) { this.updatedAt = createdAt; }
    
    // Métodos para manejar pagos
    public List<Pago> getPagos() { return pagos; }
    public void setPagos(List<Pago> pagos) { this.pagos = pagos; }
    
    public void agregarPago(Pago pago) {
        pagos.add(pago);
        pago.setUsuario(this);
    }
    
    public String getNombreCompleto() {
        return getNombresCompletos() + " " + getApellidosCompletos();
    }
    
    public boolean estaActivo() {
        return EstadoUsuario.Activo.equals(this.estado);
    }
    
    // Métodos de compatibilidad para TipoUsuario (eliminado de la nueva tabla)
    @Transient
    public TipoUsuario getTipo() { 
        // Retornar un valor por defecto o basado en alguna lógica
        return TipoUsuario.jugador; 
    }
    
    @Transient
    public void setTipo(TipoUsuario tipo) { 
        // No hacer nada ya que el campo no existe en la nueva tabla
    }
    
    // Enums
    public enum Genero {
        Masculino, Femenino, Otro
    }
    
    public enum TipoUsuario {
        jugador, profesor
    }
    
    public enum EstadoUsuario {
        Activo, Inactivo
    }
}