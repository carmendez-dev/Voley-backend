package com.voley.domain;

import jakarta.persistence.*;

/**
 * Entidad de dominio para Tipo de Acción
 * Catálogo de tipos de acciones en el juego (Saque, Recepción, Ataque, etc.)
 */
@Entity
@Table(name = "tipo_accion")
public class TipoAccion {
    
    @Id
    @Column(name = "id_tipo_accion")
    private Integer idTipoAccion;
    
    @Column(name = "descripcion", nullable = false, length = 50)
    private String descripcion;
    
    // Constructores
    public TipoAccion() {}
    
    public TipoAccion(Integer idTipoAccion, String descripcion) {
        this.idTipoAccion = idTipoAccion;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Integer getIdTipoAccion() {
        return idTipoAccion;
    }
    
    public void setIdTipoAccion(Integer idTipoAccion) {
        this.idTipoAccion = idTipoAccion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
