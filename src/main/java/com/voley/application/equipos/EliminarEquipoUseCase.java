package com.voley.application.equipos;

import com.voley.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para eliminar un equipo
 * 
 * Elimina un equipo del sistema.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
@Transactional
public class EliminarEquipoUseCase {
    
    private final EquipoRepository equipoRepository;
    
    @Autowired
    public EliminarEquipoUseCase(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }
    
    /**
     * Ejecuta el caso de uso para eliminar un equipo
     * @param id ID del equipo a eliminar
     * @throws IllegalArgumentException si el ID es inválido
     * @throws RuntimeException si no se encuentra el equipo
     */
    public void ejecutar(Long id) {
        // Validar ID
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del equipo debe ser un número positivo");
        }
        
        // Verificar que el equipo existe
        if (!equipoRepository.existePorId(id)) {
            throw new RuntimeException("No existe equipo con ID: " + id);
        }
        
        // Eliminar el equipo
        equipoRepository.eliminar(id);
    }
}