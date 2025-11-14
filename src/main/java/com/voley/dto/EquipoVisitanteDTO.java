package com.voley.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para EquipoVisitante
 * 
 * @author Sistema Voley
 * @version 1.0
 */
public class EquipoVisitanteDTO {
    
    private Long idEquipoVisitante;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    // Constructores
    public EquipoVisitanteDTO() {
    }
    
    public EquipoVisitanteDTO(Long idEquipoVisitante, String nombre) {
        this.idEquipoVisitante = idEquipoVisitante;
        this.nombre = nombre;
    }
    
    // Getters y Setters
    public Long getIdEquipoVisitante() {
        return idEquipoVisitante;
    }
    
    public void setIdEquipoVisitante(Long idEquipoVisitante) {
        this.idEquipoVisitante = idEquipoVisitante;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
