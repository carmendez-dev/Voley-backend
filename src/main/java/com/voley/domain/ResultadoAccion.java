package com.voley.domain;

import jakarta.persistence.*;

/**
 * Entidad de dominio para Resultado de Acción
 * Catálogo de resultados de acciones (Punto, Error)
 */
@Entity
@Table(name = "resultado_accion")
public class ResultadoAccion {
    
    @Id
    @Column(name = "id_resultado_accion")
    private Integer idResultadoAccion;
    
    @Column(name = "descripcion", nullable = false, length = 50)
    private String descripcion;
    
    // Constructores
    public ResultadoAccion() {}
    
    public ResultadoAccion(Integer idResultadoAccion, String descripcion) {
        this.idResultadoAccion = idResultadoAccion;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Integer getIdResultadoAccion() {
        return idResultadoAccion;
    }
    
    public void setIdResultadoAccion(Integer idResultadoAccion) {
        this.idResultadoAccion = idResultadoAccion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
