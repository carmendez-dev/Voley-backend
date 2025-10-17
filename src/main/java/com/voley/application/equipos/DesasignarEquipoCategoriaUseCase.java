package com.voley.application.equipos;

import com.voley.repository.CategoriaEquipoRepository;
import com.voley.repository.CategoriaRepository;
import com.voley.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para desasignar un equipo de una categoría
 * 
 * Elimina la relación many-to-many entre una categoría y un equipo.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
@Transactional
public class DesasignarEquipoCategoriaUseCase {
    
    private final CategoriaEquipoRepository categoriaEquipoRepository;
    private final CategoriaRepository categoriaRepository;
    private final EquipoRepository equipoRepository;
    
    @Autowired
    public DesasignarEquipoCategoriaUseCase(CategoriaEquipoRepository categoriaEquipoRepository,
                                           CategoriaRepository categoriaRepository,
                                           EquipoRepository equipoRepository) {
        this.categoriaEquipoRepository = categoriaEquipoRepository;
        this.categoriaRepository = categoriaRepository;
        this.equipoRepository = equipoRepository;
    }
    
    /**
     * Ejecuta el caso de uso para desasignar un equipo de una categoría
     * @param idCategoria ID de la categoría
     * @param idEquipo ID del equipo
     * @throws IllegalArgumentException si los IDs son inválidos
     * @throws RuntimeException si no se encuentran las entidades o no existe la relación
     */
    public void ejecutar(Long idCategoria, Long idEquipo) {
        // Validar IDs
        validarIds(idCategoria, idEquipo);
        
        // Verificar que la categoría existe
        if (!categoriaRepository.existe(idCategoria)) {
            throw new RuntimeException("No existe categoría con ID: " + idCategoria);
        }
        
        // Verificar que el equipo existe
        if (!equipoRepository.existePorId(idEquipo)) {
            throw new RuntimeException("No existe equipo con ID: " + idEquipo);
        }
        
        // Verificar que existe la relación
        if (!categoriaEquipoRepository.existeRelacion(idCategoria, idEquipo)) {
            throw new RuntimeException("El equipo no está asignado a esta categoría");
        }
        
        // Eliminar la relación
        categoriaEquipoRepository.eliminarRelacion(idCategoria, idEquipo);
    }
    
    /**
     * Valida los IDs de entrada
     */
    private void validarIds(Long idCategoria, Long idEquipo) {
        if (idCategoria == null || idCategoria <= 0) {
            throw new IllegalArgumentException("El ID de la categoría debe ser un número positivo");
        }
        
        if (idEquipo == null || idEquipo <= 0) {
            throw new IllegalArgumentException("El ID del equipo debe ser un número positivo");
        }
    }
}