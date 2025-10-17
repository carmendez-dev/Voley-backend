package com.voley.application.equipos;

import com.voley.domain.CategoriaEquipo;
import com.voley.domain.Categoria;
import com.voley.domain.Equipo;
import com.voley.repository.CategoriaEquipoRepository;
import com.voley.repository.CategoriaRepository;
import com.voley.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para asignar un equipo a una categoría
 * 
 * Crea la relación many-to-many entre una categoría y un equipo.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
@Transactional
public class AsignarEquipoCategoriaUseCase {
    
    private final CategoriaEquipoRepository categoriaEquipoRepository;
    private final CategoriaRepository categoriaRepository;
    private final EquipoRepository equipoRepository;
    
    @Autowired
    public AsignarEquipoCategoriaUseCase(CategoriaEquipoRepository categoriaEquipoRepository,
                                        CategoriaRepository categoriaRepository,
                                        EquipoRepository equipoRepository) {
        this.categoriaEquipoRepository = categoriaEquipoRepository;
        this.categoriaRepository = categoriaRepository;
        this.equipoRepository = equipoRepository;
    }
    
    /**
     * Ejecuta el caso de uso para asignar un equipo a una categoría
     * @param idCategoria ID de la categoría
     * @param idEquipo ID del equipo
     * @return Relación creada
     * @throws IllegalArgumentException si los IDs son inválidos
     * @throws RuntimeException si no se encuentran las entidades o ya existe la relación
     */
    public CategoriaEquipo ejecutar(Long idCategoria, Long idEquipo) {
        // Validar IDs
        validarIds(idCategoria, idEquipo);
        
        // Verificar que la categoría existe
        Categoria categoria = categoriaRepository.buscarPorId(idCategoria)
                .orElseThrow(() -> new RuntimeException("No existe categoría con ID: " + idCategoria));
        
        // Verificar que el equipo existe
        Equipo equipo = equipoRepository.buscarPorId(idEquipo)
                .orElseThrow(() -> new RuntimeException("No existe equipo con ID: " + idEquipo));
        
        // Verificar que no existe ya la relación
        if (categoriaEquipoRepository.existeRelacion(idCategoria, idEquipo)) {
            throw new RuntimeException("El equipo ya está asignado a esta categoría");
        }
        
        // Crear y guardar la relación
        CategoriaEquipo categoriaEquipo = new CategoriaEquipo(categoria, equipo);
        
        return categoriaEquipoRepository.guardar(categoriaEquipo);
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