package com.voley.application.equipovisitante;

import com.voley.adapter.EquipoVisitanteJpaRepository;
import com.voley.domain.EquipoVisitante;
import com.voley.dto.EquipoVisitanteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso: Obtener todos los equipos visitantes
 */
@Service
@Transactional(readOnly = true)
public class ObtenerTodosEquiposVisitantesUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ObtenerTodosEquiposVisitantesUseCase.class);
    
    private final EquipoVisitanteJpaRepository equipoVisitanteRepository;
    
    @Autowired
    public ObtenerTodosEquiposVisitantesUseCase(EquipoVisitanteJpaRepository equipoVisitanteRepository) {
        this.equipoVisitanteRepository = equipoVisitanteRepository;
    }
    
    public List<EquipoVisitanteDTO> ejecutar() {
        logger.debug("Obteniendo todos los equipos visitantes");
        
        List<EquipoVisitante> equipos = equipoVisitanteRepository.findAll();
        
        return equipos.stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    private EquipoVisitanteDTO convertirADTO(EquipoVisitante equipo) {
        return new EquipoVisitanteDTO(
            equipo.getIdEquipoVisitante(),
            equipo.getNombre()
        );
    }
}
