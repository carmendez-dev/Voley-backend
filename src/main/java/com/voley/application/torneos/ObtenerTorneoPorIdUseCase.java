package com.voley.application.torneos;

import com.voley.domain.Torneo;
import com.voley.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Caso de uso para obtener un torneo por su ID
 * Encapsula la lógica de negocio para la consulta de un torneo específico
 */
@Component
public class ObtenerTorneoPorIdUseCase {
    
    private final TorneoService torneoService;
    
    @Autowired
    public ObtenerTorneoPorIdUseCase(TorneoService torneoService) {
        this.torneoService = torneoService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener un torneo por ID
     * 
     * @param id El ID del torneo a buscar
     * @return Optional con el torneo encontrado o vacío si no existe
     * @throws IllegalArgumentException Si el ID es nulo o inválido
     */
    public Optional<Torneo> ejecutar(Long id) {
        validarId(id);
        return torneoService.obtenerTorneoPorId(id);
    }
    
    private void validarId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del torneo no puede ser nulo");
        }
        
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del torneo debe ser mayor a cero");
        }
    }
}