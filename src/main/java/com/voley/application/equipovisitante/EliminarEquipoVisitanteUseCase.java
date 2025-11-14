package com.voley.application.equipovisitante;

import com.voley.adapter.EquipoVisitanteJpaRepository;
import com.voley.domain.EquipoVisitante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Eliminar EquipoVisitante
 */
@Service
@Transactional
public class EliminarEquipoVisitanteUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(EliminarEquipoVisitanteUseCase.class);
    
    private final EquipoVisitanteJpaRepository equipoVisitanteRepository;
    
    @Autowired
    public EliminarEquipoVisitanteUseCase(EquipoVisitanteJpaRepository equipoVisitanteRepository) {
        this.equipoVisitanteRepository = equipoVisitanteRepository;
    }
    
    public void ejecutar(Long id) {
        logger.info("Eliminando equipo visitante ID: {}", id);
        
        EquipoVisitante equipo = equipoVisitanteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe equipo visitante con ID: " + id));
        
        equipoVisitanteRepository.delete(equipo);
        
        logger.info("Equipo visitante eliminado exitosamente");
    }
}
