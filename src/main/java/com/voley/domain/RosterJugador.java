package com.voley.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad RosterJugador - Representa la relación entre inscripción y jugadores
 * Tabla: roster_jugador
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Entity
@Table(name = "roster_jugador", 
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"id_inscripcion", "id_usuario"},
           name = "uk_inscripcion_usuario"
       ))
public class RosterJugador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_roster")
    private Long idRoster;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inscripcion", nullable = false)
    private InscripcionEquipo inscripcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;
    
    // Constructores
    public RosterJugador() {
        this.fechaRegistro = LocalDate.now();
    }
    
    public RosterJugador(InscripcionEquipo inscripcion, Usuario usuario) {
        this.inscripcion = inscripcion;
        this.usuario = usuario;
        this.fechaRegistro = LocalDate.now();
    }
    
    // Getters y Setters
    public Long getIdRoster() {
        return idRoster;
    }
    
    public void setIdRoster(Long idRoster) {
        this.idRoster = idRoster;
    }
    
    public InscripcionEquipo getInscripcion() {
        return inscripcion;
    }
    
    public void setInscripcion(InscripcionEquipo inscripcion) {
        this.inscripcion = inscripcion;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    // Métodos de negocio
    public boolean esRegistroReciente() {
        return fechaRegistro != null && 
               fechaRegistro.isAfter(LocalDate.now().minusDays(7));
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RosterJugador that = (RosterJugador) o;
        return Objects.equals(inscripcion, that.inscripcion) && 
               Objects.equals(usuario, that.usuario);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(inscripcion, usuario);
    }
    
    @Override
    public String toString() {
        return "RosterJugador{" +
                "idRoster=" + idRoster +
                ", inscripcion=" + (inscripcion != null ? inscripcion.getIdInscripcion() : null) +
                ", usuario=" + (usuario != null ? usuario.getId() : null) +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
