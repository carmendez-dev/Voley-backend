package com.voley.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad de dominio para Profesor
 */
@Entity
@Table(name = "profesor")
public class Profesor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesor")
    private Integer idProfesor;
    
    @Column(name = "primer_nombre", nullable = false, length = 50)
    private String primerNombre;
    
    @Column(name = "segundo_nombre", length = 50)
    private String segundoNombre;
    
    @Column(name = "primer_apellido", nullable = false, length = 50)
    private String primerApellido;
    
    @Column(name = "segundo_apellido", length = 50)
    private String segundoApellido;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "celular", length = 20)
    private String celular;
    
    @Column(name = "contacto_emergencia", length = 100)
    private String contactoEmergencia;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false)
    private Genero genero;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado = Estado.Activo;
    
    @Column(name = "cedula", length = 20)
    private String cedula;
    
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    
    @Column(name = "update_at")
    private LocalDateTime updateAt;
    
    @Column(name = "password", nullable = false, length = 200)
    private String password;
    
    /**
     * Enum para género
     */
    public enum Genero {
        Masculino,
        Femenino
    }
    
    /**
     * Enum para estado
     */
    public enum Estado {
        Activo,
        Inactivo
    }
    
    // Constructores
    public Profesor() {}
    
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateAt = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Integer getIdProfesor() {
        return idProfesor;
    }
    
    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }
    
    public String getPrimerNombre() {
        return primerNombre;
    }
    
    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }
    
    public String getSegundoNombre() {
        return segundoNombre;
    }
    
    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }
    
    public String getPrimerApellido() {
        return primerApellido;
    }
    
    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }
    
    public String getSegundoApellido() {
        return segundoApellido;
    }
    
    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCelular() {
        return celular;
    }
    
    public void setCelular(String celular) {
        this.celular = celular;
    }
    
    public String getContactoEmergencia() {
        return contactoEmergencia;
    }
    
    public void setContactoEmergencia(String contactoEmergencia) {
        this.contactoEmergencia = contactoEmergencia;
    }
    
    public Genero getGenero() {
        return genero;
    }
    
    public void setGenero(Genero genero) {
        this.genero = genero;
    }
    
    public Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
    
    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Obtiene el nombre completo del profesor
     */
    public String getNombreCompleto() {
        StringBuilder nombre = new StringBuilder();
        nombre.append(primerNombre);
        if (segundoNombre != null && !segundoNombre.isEmpty()) {
            nombre.append(" ").append(segundoNombre);
        }
        nombre.append(" ").append(primerApellido);
        if (segundoApellido != null && !segundoApellido.isEmpty()) {
            nombre.append(" ").append(segundoApellido);
        }
        return nombre.toString();
    }
}
