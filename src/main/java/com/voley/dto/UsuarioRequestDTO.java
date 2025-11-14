package com.voley.dto;

import com.voley.domain.Usuario.Genero;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

/**
 * DTO para crear o actualizar usuarios con la nueva estructura de nombres
 */
public class UsuarioRequestDTO {
    
    @NotBlank(message = "El primer nombre es requerido")
    @Size(max = 100, message = "El primer nombre no puede exceder 100 caracteres")
    private String primerNombre;
    
    @Size(max = 100, message = "El segundo nombre no puede exceder 100 caracteres")
    private String segundoNombre;
    
    @Size(max = 100, message = "El tercer nombre no puede exceder 100 caracteres")
    private String tercerNombre;
    
    @NotBlank(message = "El primer apellido es requerido")
    @Size(max = 100, message = "El primer apellido no puede exceder 100 caracteres")
    private String primerApellido;
    
    @Size(max = 100, message = "El segundo apellido no puede exceder 100 caracteres")
    private String segundoApellido;
    
    @NotNull(message = "La fecha de nacimiento es requerida")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;
    
    @NotBlank(message = "La cédula es requerida")
    @Size(max = 20, message = "La cédula no puede exceder 20 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "La cédula debe contener solo números")
    private String cedula;
    
    @NotNull(message = "El género es requerido")
    private Genero genero;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;
    
    @NotBlank(message = "El celular es requerido")
    @Size(max = 20, message = "El celular no puede exceder 20 caracteres")
    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "El celular debe contener solo números y símbolos válidos")
    private String celular;
    
    @NotBlank(message = "La dirección es requerida")
    private String direccion;
    
    @NotBlank(message = "El contacto de emergencia es requerido")
    @Size(max = 20, message = "El contacto de emergencia no puede exceder 20 caracteres")
    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "El contacto de emergencia debe contener solo números y símbolos válidos")
    private String contactoEmergencia;
    

    
    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    @DecimalMax(value = "1000.0", message = "El peso debe ser menor a 1000 kg")
    private Float peso;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "La altura debe ser mayor a 0")
    @DecimalMax(value = "3.0", message = "La altura debe ser menor a 3 metros")
    private Float altura;
    
    // Constructores
    public UsuarioRequestDTO() {}
    
    public UsuarioRequestDTO(String primerNombre, String segundoNombre, String tercerNombre,
                           String primerApellido, String segundoApellido, LocalDate fechaNacimiento,
                           String cedula, Genero genero, String email, String celular,
                           String direccion, String contactoEmergencia,
                           Float peso, Float altura) {
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
        this.peso = peso;
        this.altura = altura;
    }
    
    // Método auxiliar para obtener el nombre completo
    public String getNombreCompleto() {
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
    
    // Método auxiliar para obtener los apellidos completos
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
    
    // Getters y Setters
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
}