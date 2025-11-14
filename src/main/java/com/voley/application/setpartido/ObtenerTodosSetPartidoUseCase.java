package com.voley.application.setpartido;

import com.voley.adapter.SetPartidoJpaRepository;
import com.voley.domain.SetPartido;
import com.voley.dto.SetPartidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso: Obtener todos los SetPartido
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class ObtenerTodosSetPartidoUseCase {
    
    private final SetPartidoJpaRepository setPartidoRepository;
    
    @Autowired
    public ObtenerTodosSetPartidoUseCase(SetPartidoJpaRepository setPartidoRepository) {
        this.setPartidoRepository = setPartidoRepository;
    }
    
    @Transactional(readOnly = true)
    public List<SetPartidoDTO> ejecutar() {
        return setPartidoRepository.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
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
