package com.voley.application.partido;

import com.voley.adapter.PartidoJpaRepository;
import com.voley.domain.Partido;
import com.voley.dto.PartidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ObtenerTodosPartidosUseCase {
    
    private final PartidoJpaRepository partidoRepository;
    
    @Autowired
    public ObtenerTodosPartidosUseCase(PartidoJpaRepository partidoRepository) {
        this.partidoRepository = partidoRepository;
    }
    
    public List<PartidoDTO> ejecutar() {
        return partidoRepository.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    private PartidoDTO convertirADTO(Partido partido) {
        PartidoDTO dto = new PartidoDTO();
        dto.setIdPartido(partido.getIdPartido());
        dto.setIdInscripcionLocal(partido.getInscripcionLocal().getIdInscripcion());
        dto.setIdEquipoVisitante(partido.getEquipoVisitante().getIdEquipoVisitante());
        dto.setFecha(partido.getFecha());
        dto.setUbicacion(partido.getUbicacion());
        dto.setResultado(partido.getResultado());
        dto.setNombreEquipoLocal(partido.getInscripcionLocal().getEquipo().getNombre());
        dto.setNombreEquipoVisitante(partido.getEquipoVisitante().getNombre());
        
        if (partido.getInscripcionLocal().getTorneoCategoria() != null) {
            dto.setNombreTorneo(partido.getInscripcionLocal().getTorneoCategoria()
                .getTorneo().getNombre());
            dto.setNombreCategoria(partido.getInscripcionLocal().getTorneoCategoria()
                .getCategoria().getNombre());
        }
        
        return dto;
    }
}