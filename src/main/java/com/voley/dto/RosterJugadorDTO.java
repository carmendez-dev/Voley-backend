package com.voley.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO para RosterJugador
 * 
 * @author Sistema Voley
 * @version 1.0
 */
public class RosterJugadorDTO {
    
    private Long idRoster;
    
    @NotNull(message = "El ID de inscripción es obligatorio")
    private Long idInscripcion;
    
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;
    
    private LocalDate fechaRegistro;
    
    // Información adicional para respuestas
    private String nombreJugador;
    private String emailJugador;
    private String nombreEquipo;
    private String nombreTorneo;
    private String nombreCategoria;
    
    // Constructores
    public RosterJugadorDTO() {
    }
    
    public RosterJugadorDTO(Long idInscripcion, Long idUsuario) {
        this.idInscripcion = idInscripcion;
        this.idUsuario = idUsuario;
    }
    
    // Getters y Setters
    public Long getIdRoster() {
        return idRoster;
    }
    
    public void setIdRoster(Long idRoster) {
        this.idRoster = idRoster;
    }
    
    public Long getIdInscripcion() {
        return idInscripcion;
    }
    
    public void setIdInscripcion(Long idInscripcion) {
        this.idInscripcion = idInscripcion;
    }
    
    public Long getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public String getNombreJugador() {
        return nombreJugador;
    }
    
    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }
    
    public String getEmailJugador() {
        return emailJugador;
    }
    
    public void setEmailJugador(String emailJugador) {
        this.emailJugador = emailJugador;
    }
    
    public String getNombreEquipo() {
        return nombreEquipo;
    }
    
    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }
    
    public String getNombreTorneo() {
        return nombreTorneo;
    }
    
    public void setNombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }
    
    public String getNombreCategoria() {
        return nombreCategoria;
    }
    
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
