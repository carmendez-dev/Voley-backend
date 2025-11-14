package com.voley.application.partido;

import com.voley.adapter.EquipoVisitanteJpaRepository;
import com.voley.adapter.InscripcionEquipoJpaRepository;
import com.voley.adapter.PartidoJpaRepository;
import com.voley.domain.EquipoVisitante;
import com.voley.domain.InscripcionEquipo;
import com.voley.domain.Partido;
import com.voley.dto.PartidoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Crear Partido
 */
@Service
@Transactional
public class CrearPartidoUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(CrearPartidoUseCase.class);
    
    private final PartidoJpaRepository partidoRepository;
    private final InscripcionEquipoJpaRepository inscripcionRepository;
    private final EquipoVisitanteJpaRepository equipoVisitanteRepository;
    
    @Autowired
    public CrearPartidoUseCase(
            PartidoJpaRepository partidoRepository,
            InscripcionEquipoJpaRepository inscripcionRepository,
            EquipoVisitanteJpaRepository equipoVisitanteRepository) {
        this.partidoRepository = partidoRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.equipoVisitanteRepository = equipoVisitanteRepository;
    }
    
    public PartidoDTO ejecutar(PartidoDTO dto) {
        logger.info("Creando partido: local={}, visitante={}", 
                   dto.getIdInscripcionLocal(), dto.getIdEquipoVisitante());
        
        // Validar que la inscripción local existe
        InscripcionEquipo inscripcionLocal = inscripcionRepository
            .findById(dto.getIdInscripcionLocal())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe inscripción con ID: " + dto.getIdInscripcionLocal()));
        
        // Validar que el equipo visitante existe
        EquipoVisitante equipoVisitante = equipoVisitanteRepository
            .findById(dto.getIdEquipoVisitante())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe equipo visitante con ID: " + dto.getIdEquipoVisitante()));
        
        // Crear partido
        Partido partido = new Partido(
            inscripcionLocal,
            equipoVisitante,
            dto.getFecha(),
            dto.getUbicacion()
        );
        partido.validar();
        
        // Guardar
        Partido guardado = partidoRepository.save(partido);
        
        logger.info("Partido creado con ID: {}", guardado.getIdPartido());
        
        return convertirADTO(guardado);
    }
    
    private PartidoDTO convertirADTO(Partido partido) {
        PartidoDTO dto = new PartidoDTO();
        dto.setIdPartido(partido.getIdPartido());
        dto.setIdInscripcionLocal(partido.getInscripcionLocal().getIdInscripcion());
        dto.setIdEquipoVisitante(partido.getEquipoVisitante().getIdEquipoVisitante());
        dto.setFecha(partido.getFecha());
        dto.setUbicacion(partido.getUbicacion());
        dto.setResultado(partido.getResultado());
        
        // Información adicional
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
