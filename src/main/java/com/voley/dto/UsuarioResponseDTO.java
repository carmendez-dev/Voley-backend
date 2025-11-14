package com.voley.dto;

import com.voley.domain.Usuario.Genero;
import com.voley.domain.Usuario.EstadoUsuario;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para respuestas de usuario con información completa
 */
public class UsuarioResponseDTO {
    
    private Long id;
    private String primerNombre;
    private String segundoNombre;
    private String tercerNombre;
    private String primerApellido;
    private String segundoApellido;
    private String nombreCompleto; // Campo calculado
    private String apellidosCompletos; // Campo calculado
    private LocalDate fechaNacimiento;
    private String cedula;
    private Genero genero;
    private String email;
    private String celular;
    private String direccion;
    private String contactoEmergencia;
    private EstadoUsuario estado;
    private Float peso;
    private Float altura;
    private LocalDate fechaRegistro;
    private LocalDateTime updatedAt;
    
    // Campos calculados adicionales
    private Integer edad;
    private Float imc; // Índice de Masa Corporal
    
    // Constructores
    public UsuarioResponseDTO() {}
    
    public UsuarioResponseDTO(Long id, String primerNombre, String segundoNombre, String tercerNombre,
                            String primerApellido, String segundoApellido, LocalDate fechaNacimiento,
                            String cedula, Genero genero, String email, String celular,
                            String direccion, String contactoEmergencia,
                            EstadoUsuario estado, Float peso, Float altura,
                            LocalDate fechaRegistro, LocalDateTime updatedAt) {
        this.id = id;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.tercerNombre = tercerNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.fechaNacimiento = fechaNacimiento;
        this.cedula = cedula;
        this.genero = genero;
        this.email = email;
        this.celular = celular;
        this.direccion = direccion;
        this.contactoEmergencia = contactoEmergencia;
        this.estado = estado;
        this.peso = peso;
        this.altura = altura;
        this.fechaRegistro = fechaRegistro;
        this.updatedAt = updatedAt;
        
        // Calcular campos derivados
        this.nombreCompleto = calcularNombreCompleto();
        this.apellidosCompletos = calcularApellidosCompletos();
        this.edad = calcularEdad();
        this.imc = calcularIMC();
    }
    
    // Métodos de cálculo
    private String calcularNombreCompleto() {
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
    
    private String calcularApellidosCompletos() {
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
    
    private Integer calcularEdad() {
        if (fechaNacimiento == null) return null;
        return LocalDate.now().getYear() - fechaNacimiento.getYear() - 
               (LocalDate.now().getDayOfYear() < fechaNacimiento.getDayOfYear() ? 1 : 0);
    }
    
    private Float calcularIMC() {
        if (peso == null || altura == null || altura == 0) return null;
        return peso / (altura * altura);
    }
    
    public String getNombreCompletoConApellidos() {
        return nombreCompleto + " " + apellidosCompletos;
    }
    
    public String getEstadoDescriptivo() {
        if (estado == null) return "Desconocido";
        return estado == EstadoUsuario.Activo ? "Usuario Activo" : "Usuario Inactivo";
    }
    
    public String getClasificacionIMC() {
        if (imc == null) return "No calculable";
        if (imc < 18.5) return "Bajo peso";
        if (imc < 25) return "Peso normal";
        if (imc < 30) return "Sobrepeso";
        return "Obesidad";
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }
    
    public String getTercerNombre() { return tercerNombre; }
    public void setTercerNombre(String tercerNombre) { this.tercerNombre = tercerNombre; }
    
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }
    
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    
    public String getApellidosCompletos() { return apellidosCompletos; }
    public void setApellidosCompletos(String apellidosCompletos) { this.apellidosCompletos = apellidosCompletos; }
    
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
    

    
    public EstadoUsuario getEstado() { return estado; }
    public void setEstado(EstadoUsuario estado) { this.estado = estado; }
    
    public Float getPeso() { return peso; }
    public void setPeso(Float peso) { this.peso = peso; }
    
    public Float getAltura() { return altura; }
    public void setAltura(Float altura) { this.altura = altura; }
    
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    

    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    
    public Float getImc() { return imc; }
    public void setImc(Float imc) { this.imc = imc; }
}