package com.voley.application.setpartido;

import com.voley.adapter.SetPartidoJpaRepository;
import com.voley.domain.SetPartido;
import com.voley.dto.SetPartidoDTO;
import com.voley.dto.SetPartidoUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Actualizar SetPartido
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class ActualizarSetPartidoUseCase {
    
    private final SetPartidoJpaRepository setPartidoRepository;
    
    @Autowired
    public ActualizarSetPartidoUseCase(SetPartidoJpaRepository setPartidoRepository) {
        this.setPartidoRepository = setPartidoRepository;
    }
    
    @Transactional
    public SetPartidoDTO ejecutar(Long id, SetPartidoUpdateDTO dto) {
        SetPartido setPartido = setPartidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe set con ID: " + id));
        
        // Actualizar campos solo si vienen en el DTO
        if (dto.getNumeroSet() != null) {
            setPartido.setNumeroSet(dto.getNumeroSet());
        }
        
        if (dto.getPuntosLocal() != null) {
            setPartido.setPuntosLocal(dto.getPuntosLocal());
        }
        
        if (dto.getPuntosVisitante() != null) {
            setPartido.setPuntosVisitante(dto.getPuntosVisitante());
        }
        
        setPartido.validar();
        SetPartido actualizado = setPartidoRepository.save(setPartido);
        
        return convertirADTO(actualizado);
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
