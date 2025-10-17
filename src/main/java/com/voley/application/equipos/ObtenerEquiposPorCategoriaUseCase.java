package com.voley.application.equipos;

import com.voley.domain.CategoriaEquipo;
import com.voley.repository.CategoriaEquipoRepository;
import com.voley.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para obtener equipos de una categoría
 * 
 * Recupera todos los equipos asociados a una categoría específica.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class ObtenerEquiposPorCategoriaUseCase {
    
    private final CategoriaEquipoRepository categoriaEquipoRepository;
    private final CategoriaRepository categoriaRepository;
    
    @Autowired
    public ObtenerEquiposPorCategoriaUseCase(CategoriaEquipoRepository categoriaEquipoRepository,
                                            CategoriaRepository categoriaRepository) {
        this.categoriaEquipoRepository = categoriaEquipoRepository;
        this.categoriaRepository = categoriaRepository;
    }
    
    /**
     * Ejecuta el caso de uso para obtener equipos por categoría
     * @param idCategoria ID de la categoría
     * @return Lista de relaciones con equipos de esa categoría
     * @throws IllegalArgumentException si el ID es inválido
     * @throws RuntimeException si no se encuentra la categoría
     */
    public List<CategoriaEquipo> ejecutar(Long idCategoria) {
        // Validar ID
        if (idCategoria == null || idCategoria <= 0) {
            throw new IllegalArgumentException("El ID de la categoría debe ser un número positivo");
        }
        
        // Verificar que la categoría existe
        if (!categoriaRepository.existe(idCategoria)) {
            throw new RuntimeException("No existe categoría con ID: " + idCategoria);
        }
        
        // Obtener equipos de la categoría
        return categoriaEquipoRepository.buscarEquiposPorCategoria(idCategoria);
    }
}