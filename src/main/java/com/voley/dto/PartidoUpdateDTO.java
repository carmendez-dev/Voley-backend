package com.voley.dto;

import com.voley.domain.Partido.ResultadoPartido;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO para actualizar Partido
 * Todos los campos son opcionales para permitir actualizaciones parciales
 * 
 * @author Sistema Voley
 * @version 1.0
 */
public class PartidoUpdateDTO {
    
    private Long idInscripcionLocal;
    
    private Long idEquipoVisitante;
    
    private LocalDateTime fecha;
    
    @Size(max = 255, message = "La ubicación no puede exceder 255 caracteres")
    private String ubicacion;
    
    private ResultadoPartido resultado;
    
    // Constructores
    public PartidoUpdateDTO() {
    }
    
    // Getters y Setters
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
}
