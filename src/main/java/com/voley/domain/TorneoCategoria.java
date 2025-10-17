package com.voley.domain;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Entidad que representa la relación entre Torneo y Categoría
 * Tabla de unión torneo_categoria
 */
@Entity
@Table(name = "torneo_categoria")
public class TorneoCategoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_torneo_categoria")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_torneo", referencedColumnName = "id_torneo")
    private Torneo torneo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    private Categoria categoria;
    
    // Constructores
    public TorneoCategoria() {}
    
    public TorneoCategoria(Torneo torneo, Categoria categoria) {
        this.torneo = torneo;
        this.categoria = categoria;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Torneo getTorneo() {
        return torneo;
    }
    
    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TorneoCategoria that = (TorneoCategoria) o;
        return Objects.equals(torneo, that.torneo) && Objects.equals(categoria, that.categoria);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(torneo, categoria);
    }
    
    @Override
    public String toString() {
        return "TorneoCategoria{" +
                "id=" + id +
                ", torneo=" + (torneo != null ? torneo.getId() : null) +
                ", categoria=" + (categoria != null ? categoria.getIdCategoria() : null) +
                '}';
    }
}