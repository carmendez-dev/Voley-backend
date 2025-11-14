package com.voley.application.equipovisitante;

import com.voley.adapter.EquipoVisitanteJpaRepository;
import com.voley.domain.EquipoVisitante;
import com.voley.dto.EquipoVisitanteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Caso de uso: Obtener equipo visitante por ID
 */
@Service
@Transactional(readOnly = true)
public class ObtenerEquipoVisitantePorIdUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ObtenerEquipoVisitantePorIdUseCase.class);
    
    private final EquipoVisitanteJpaRepository equipoVisitanteRepository;
    
    @Autowired
    public ObtenerEquipoVisitantePorIdUseCase(EquipoVisitanteJpaRepository equipoVisitanteRepository) {
        this.equipoVisitanteRepository = equipoVisitanteRepository;
    }
    
    public Optional<EquipoVisitanteDTO> ejecutar(Long id) {
        logger.debug("Obteniendo equipo visitante con ID: {}", id);
        
        return equipoVisitanteRepository.findById(id)
            .map(this::convertirADTO);
    }
    
    private EquipoVisitanteDTO convertirADTO(EquipoVisitante equipo) {
        return new EquipoVisitanteDTO(
            equipo.getIdEquipoVisitante(),
            equipo.getNombre()
        );
    }
}
