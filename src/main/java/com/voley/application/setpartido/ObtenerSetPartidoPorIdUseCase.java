package com.voley.application.setpartido;

import com.voley.adapter.SetPartidoJpaRepository;
import com.voley.domain.SetPartido;
import com.voley.dto.SetPartidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Caso de uso: Obtener SetPartido por ID
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class ObtenerSetPartidoPorIdUseCase {
    
    private final SetPartidoJpaRepository setPartidoRepository;
    
    @Autowired
    public ObtenerSetPartidoPorIdUseCase(SetPartidoJpaRepository setPartidoRepository) {
        this.setPartidoRepository = setPartidoRepository;
    }
    
    @Transactional(readOnly = true)
    public Optional<SetPartidoDTO> ejecutar(Long id) {
        return setPartidoRepository.findById(id)
            .map(this::convertirADTO);
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
