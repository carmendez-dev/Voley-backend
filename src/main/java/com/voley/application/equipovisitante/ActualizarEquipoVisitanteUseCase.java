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
 * Caso de uso: Actualizar EquipoVisitante
 */
@Service
@Transactional
public class ActualizarEquipoVisitanteUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ActualizarEquipoVisitanteUseCase.class);
    
    private final EquipoVisitanteJpaRepository equipoVisitanteRepository;
    
    @Autowired
    public ActualizarEquipoVisitanteUseCase(EquipoVisitanteJpaRepository equipoVisitanteRepository) {
        this.equipoVisitanteRepository = equipoVisitanteRepository;
    }
    
    public EquipoVisitanteDTO ejecutar(Long id, EquipoVisitanteDTO dto) {
        logger.info("Actualizando equipo visitante ID: {}", id);
        
        // Buscar equipo existente
        EquipoVisitante equipo = equipoVisitanteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe equipo visitante con ID: " + id));
        
        // Validar que no exista otro equipo con el mismo nombre
        if (!equipo.getNombre().equals(dto.getNombre()) && 
            equipoVisitanteRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe un equipo visitante con el nombre: " + dto.getNombre());
        }
        
        // Actualizar datos
        equipo.setNombre(dto.getNombre());
        equipo.validar();
        
        // Guardar
        EquipoVisitante actualizado = equipoVisitanteRepository.save(equipo);
        
        logger.info("Equipo visitante actualizado exitosamente");
        
        return convertirADTO(actualizado);
    }
    
    private EquipoVisitanteDTO convertirADTO(EquipoVisitante equipo) {
        return new EquipoVisitanteDTO(
            equipo.getIdEquipoVisitante(),
            equipo.getNombre()
        );
    }
}
