package com.voley.dto;

import com.voley.domain.InscripcionEquipo.EstadoInscripcion;

/**
 * DTO para actualización parcial de InscripcionEquipo
 * Permite actualizar solo estado y observaciones
 * 
 * @author Sistema Voley
 * @version 1.0
 */
public class ActualizarInscripcionDTO {
    
    private EstadoInscripcion estado;
    private String observaciones;
    
    // Constructores
    public ActualizarInscripcionDTO() {
    }
    
    public ActualizarInscripcionDTO(EstadoInscripcion estado, String observaciones) {
        this.estado = estado;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters
    public EstadoInscripcion getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoInscripcion estado) {
        this.estado = estado;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
