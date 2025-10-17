package com.voley.application.torneos;

import com.voley.domain.Torneo;
import com.voley.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para buscar torneos por nombre
 * Encapsula la lógica de negocio para la búsqueda de torneos
 */
@Component
public class BuscarTorneosPorNombreUseCase {
    
    private final TorneoService torneoService;
    
    @Autowired
    public BuscarTorneosPorNombreUseCase(TorneoService torneoService) {
        this.torneoService = torneoService;
    }
    
    /**
     * Ejecuta el caso de uso para buscar torneos por nombre
     * 
     * @param nombre El nombre o parte del nombre a buscar
     * @return Lista de torneos que coinciden con la búsqueda
     * @throws IllegalArgumentException Si el nombre es inválido
     */
    public List<Torneo> ejecutar(String nombre) {
        validarNombre(nombre);
        return torneoService.buscarTorneosPorNombre(nombre.trim());
    }
    
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }
        
        if (nombre.trim().length() < 2) {
            throw new IllegalArgumentException("El nombre de búsqueda debe tener al menos 2 caracteres");
        }
    }
}