package com.voley.dto;

import com.voley.domain.Partido.ResultadoPartido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO para Partido
 * 
 * @author Sistema Voley
 * @version 1.0
 */
public class PartidoDTO {
    
    private Long idPartido;
    
    @NotNull(message = "La inscripción local es obligatoria")
    private Long idInscripcionLocal;
    
    @NotNull(message = "El equipo visitante es obligatorio")
    private Long idEquipoVisitante;
    
    @NotNull(message = "La fecha del partido es obligatoria")
    private LocalDateTime fecha;
    
    @Size(max = 255, message = "La ubicación no puede exceder 255 caracteres")
    private String ubicacion;
    
    private ResultadoPartido resultado;
    
    // Información adicional para respuestas
    private String nombreEquipoLocal;
    private String nombreEquipoVisitante;
    private String nombreTorneo;
    private String nombreCategoria;
    
    // Constructores
    public PartidoDTO() {
    }
    
    public PartidoDTO(Long idInscripcionLocal, Long idEquipoVisitante, 
                      LocalDateTime fecha, String ubicacion) {
        this.idInscripcionLocal = idInscripcionLocal;
        this.idEquipoVisitante = idEquipoVisitante;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
    }
    
    // Getters y Setters
    public Long getIdPartido() {
        return idPartido;
    }
    
    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }
    
    public Long getIdInscripcionLocal() {
        return idInscripcionLocal;
    }
    
    public void setIdInscripcionLocal(Long idInscripcionLocal) {
        this.idInscripcionLocal = idInscripcionLocal;
    }
    
    public Long getIdEquipoVisitante() {
        return idEquipoVisitante;
    }
    
    public void setIdEquipoVisitante(Long idEquipoVisitante) {
        this.idEquipoVisitante = idEquipoVisitante;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public String getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public ResultadoPartido getResultado() {
        return resultado;
    }
    
    public void setResultado(ResultadoPartido resultado) {
        this.resultado = resultado;
    }
    
    public String getNombreEquipoLocal() {
        return nombreEquipoLocal;
    }
    
    public void setNombreEquipoLocal(String nombreEquipoLocal) {
        this.nombreEquipoLocal = nombreEquipoLocal;
    }
    
    public String getNombreEquipoVisitante() {
        return nombreEquipoVisitante;
    }
    
    public void setNombreEquipoVisitante(String nombreEquipoVisitante) {
        this.nombreEquipoVisitante = nombreEquipoVisitante;
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
