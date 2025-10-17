package com.voley.application.equipos;

import com.voley.repository.EquipoRepository;
import com.voley.repository.CategoriaEquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para eliminar un equipo
 * 
 * Elimina un equipo del sistema y todas sus relaciones con categorías.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
@Transactional
public class EliminarEquipoUseCase {
    
    private final EquipoRepository equipoRepository;
    private final CategoriaEquipoRepository categoriaEquipoRepository;
    
    @Autowired
    public EliminarEquipoUseCase(EquipoRepository equipoRepository, 
                                CategoriaEquipoRepository categoriaEquipoRepository) {
        this.equipoRepository = equipoRepository;
        this.categoriaEquipoRepository = categoriaEquipoRepository;
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
        
        // Eliminar todas las relaciones del equipo con categorías
        categoriaEquipoRepository.eliminarTodasRelacionesEquipo(id);
        
        // Eliminar el equipo
        equipoRepository.eliminar(id);
    }
}