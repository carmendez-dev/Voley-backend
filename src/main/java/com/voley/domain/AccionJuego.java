package com.voley.domain;

import jakarta.persistence.*;

/**
 * Entidad de dominio para Acción de Juego
 * Representa cada acción realizada durante un set del partido
 */
@Entity
@Table(name = "accion_juego")
public class AccionJuego {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_accion_juego")
    private Long idAccionJuego;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_set_partido", nullable = false)
    private SetPartido setPartido;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_accion", nullable = false)
    private TipoAccion tipoAccion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_resultado_accion", nullable = false)
    private ResultadoAccion resultadoAccion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_roster", nullable = true)
    private RosterJugador rosterJugador;
    
    @Column(name = "posicion_visitante", nullable = false)
    private Byte posicionVisitante = 0;
    
    // Constructores
    public AccionJuego() {}
    
    // Getters y Setters
    public Long getIdAccionJuego() {
        return idAccionJuego;
    }
    
    public void setIdAccionJuego(Long idAccionJuego) {
        this.idAccionJuego = idAccionJuego;
    }
    
    public SetPartido getSetPartido() {
        return setPartido;
    }
    
    public void setSetPartido(SetPartido setPartido) {
        this.setPartido = setPartido;
    }
    
    public TipoAccion getTipoAccion() {
        return tipoAccion;
    }
    
    public void setTipoAccion(TipoAccion tipoAccion) {
        this.tipoAccion = tipoAccion;
    }
    
    public ResultadoAccion getResultadoAccion() {
        return resultadoAccion;
    }
    
    public void setResultadoAccion(ResultadoAccion resultadoAccion) {
        this.resultadoAccion = resultadoAccion;
    }
    
    public RosterJugador getRosterJugador() {
        return rosterJugador;
    }
    
    public void setRosterJugador(RosterJugador rosterJugador) {
        this.rosterJugador = rosterJugador;
    }
    
    public Byte getPosicionVisitante() {
        return posicionVisitante;
    }
    
    public void setPosicionVisitante(Byte posicionVisitante) {
        this.posicionVisitante = posicionVisitante;
    }
}
