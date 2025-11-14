package com.voley.application.equipovisitante;

import com.voley.adapter.EquipoVisitanteJpaRepository;
import com.voley.domain.EquipoVisitante;
import com.voley.dto.EquipoVisitanteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Crear EquipoVisitante
 */
@Service
@Transactional
public class CrearEquipoVisitanteUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(CrearEquipoVisitanteUseCase.class);
    
    private final EquipoVisitanteJpaRepository equipoVisitanteRepository;
    
    @Autowired
    public CrearEquipoVisitanteUseCase(EquipoVisitanteJpaRepository equipoVisitanteRepository) {
        this.equipoVisitanteRepository = equipoVisitanteRepository;
    }
    
    public EquipoVisitanteDTO ejecutar(EquipoVisitanteDTO dto) {
        logger.info("Creando equipo visitante: {}", dto.getNombre());
        
        // Validar que no exista un equipo con el mismo nombre
        if (equipoVisitanteRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe un equipo visitante con el nombre: " + dto.getNombre());
        }
        
        // Crear entidad
        EquipoVisitante equipo = new EquipoVisitante(dto.getNombre());
        equipo.validar();
        
        // Guardar
        EquipoVisitante guardado = equipoVisitanteRepository.save(equipo);
        
        logger.info("Equipo visitante creado con ID: {}", guardado.getIdEquipoVisitante());
        
        return convertirADTO(guardado);
    }
    
    private EquipoVisitanteDTO convertirADTO(EquipoVisitante equipo) {
        return new EquipoVisitanteDTO(
            equipo.getIdEquipoVisitante(),
            equipo.getNombre()
        );
    }
}
