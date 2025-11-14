package com.voley.application.setpartido;

import com.voley.adapter.SetPartidoJpaRepository;
import com.voley.adapter.PartidoJpaRepository;
import com.voley.domain.SetPartido;
import com.voley.domain.Partido;
import com.voley.dto.SetPartidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Crear SetPartido
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class CrearSetPartidoUseCase {
    
    private final SetPartidoJpaRepository setPartidoRepository;
    private final PartidoJpaRepository partidoRepository;
    
    @Autowired
    public CrearSetPartidoUseCase(SetPartidoJpaRepository setPartidoRepository,
                                  PartidoJpaRepository partidoRepository) {
        this.setPartidoRepository = setPartidoRepository;
        this.partidoRepository = partidoRepository;
    }
    
    @Transactional
    public SetPartidoDTO ejecutar(SetPartidoDTO dto) {
        // Validar que el partido existe
        Partido partido = partidoRepository.findById(dto.getIdPartido())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe partido con ID: " + dto.getIdPartido()));
        
        // Validar que no exista ya un set con ese número para ese partido
        SetPartido existente = setPartidoRepository.findByPartidoIdAndNumeroSet(
            dto.getIdPartido(), dto.getNumeroSet());
        if (existente != null) {
            throw new IllegalArgumentException(
                "Ya existe el set " + dto.getNumeroSet() + " para este partido");
        }
        
        // Crear entidad
        SetPartido setPartido = new SetPartido();
        setPartido.setPartido(partido);
        setPartido.setNumeroSet(dto.getNumeroSet());
        setPartido.setPuntosLocal(dto.getPuntosLocal());
        setPartido.setPuntosVisitante(dto.getPuntosVisitante());
        
        // Validar y guardar
        setPartido.validar();
        SetPartido guardado = setPartidoRepository.save(setPartido);
        
        // Convertir a DTO
        return convertirADTO(guardado);
    }
    
    private SetPartidoDTO convertirADTO(SetPartido setPartido) {
        SetPartidoDTO dto = new SetPartidoDTO();
        dto.setIdSetPartido(setPartido.getIdSetPartido());
        dto.setIdPartido(setPartido.getPartido().getIdPartido());
        dto.setNumeroSet(setPartido.getNumeroSet());
        dto.setPuntosLocal(setPartido.getPuntosLocal());
        dto.setPuntosVisitante(setPartido.getPuntosVisitante());
        dto.setNombreEquipoLocal(setPartido.getPartido().getInscripcionLocal().getEquipo().getNombre());
        dto.setNombreEquipoVisitante(setPartido.getPartido().getEquipoVisitante().getNombre());
        dto.setGanador(setPartido.getGanador());
        dto.setFinalizado(setPartido.estaFinalizado());
        return dto;
    }
}
