package com.voley.service;

import com.voley.adapter.TorneoCategoriaJpaRepository;
import com.voley.adapter.TorneoJpaRepository;
import com.voley.adapter.CategoriaJpaRepository;
import com.voley.domain.Categoria;
import com.voley.domain.Torneo;
import com.voley.domain.TorneoCategoria;
import com.voley.dto.CategoriaBasicaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar la relación entre torneos y categorías
 */
@Service
@Transactional
public class TorneoCategoriaService {
    
    private static final Logger logger = LoggerFactory.getLogger(TorneoCategoriaService.class);
    
    private final TorneoCategoriaJpaRepository torneoCategoriaRepository;
    private final TorneoJpaRepository torneoRepository;
    private final CategoriaJpaRepository categoriaRepository;
    
    @Autowired
    public TorneoCategoriaService(
            TorneoCategoriaJpaRepository torneoCategoriaRepository,
            TorneoJpaRepository torneoRepository,
            CategoriaJpaRepository categoriaRepository) {
        this.torneoCategoriaRepository = torneoCategoriaRepository;
        this.torneoRepository = torneoRepository;
        this.categoriaRepository = categoriaRepository;
    }
    
    /**
     * Asocia una categoría a un torneo
     */
    public TorneoCategoria asociarCategoriaATorneo(Long torneoId, Long categoriaId) {
        logger.info("Asociando categoría {} al torneo {}", categoriaId, torneoId);
        
        // Verificar que el torneo existe
        Optional<Torneo> torneo = torneoRepository.findById(torneoId);
        if (torneo.isEmpty()) {
            throw new IllegalArgumentException("No existe torneo con ID: " + torneoId);
        }
        
        // Verificar que la categoría existe
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);
        if (categoria.isEmpty()) {
            throw new IllegalArgumentException("No existe categoría con ID: " + categoriaId);
        }
        
        // Verificar que no existe ya la relación
        if (torneoCategoriaRepository.existsByTorneoIdAndCategoriaId(torneoId, categoriaId)) {
            throw new IllegalArgumentException("La categoría ya está asociada al torneo");
        }
        
        // Crear la relación
        TorneoCategoria torneoCategoria = new TorneoCategoria(torneo.get(), categoria.get());
        TorneoCategoria resultado = torneoCategoriaRepository.save(torneoCategoria);
        
        logger.info("Categoría {} asociada exitosamente al torneo {}", categoriaId, torneoId);
        return resultado;
    }
    
    /**
     * Desasocia una categoría de un torneo
     */
    public void desasociarCategoriaDelTorneo(Long torneoId, Long categoriaId) {
        logger.info("Desasociando categoría {} del torneo {}", categoriaId, torneoId);
        
        Optional<TorneoCategoria> relacion = torneoCategoriaRepository.findByTorneoIdAndCategoriaId(torneoId, categoriaId);
        if (relacion.isEmpty()) {
            throw new IllegalArgumentException("No existe la asociación entre el torneo y la categoría");
        }
        
        torneoCategoriaRepository.delete(relacion.get());
        logger.info("Categoría {} desasociada exitosamente del torneo {}", categoriaId, torneoId);
    }
    
    /**
     * Obtiene todas las categorías de un torneo
     */
    @Transactional(readOnly = true)
    public List<Categoria> obtenerCategoriasPorTorneo(Long torneoId) {
        logger.debug("Obteniendo categorías del torneo {}", torneoId);
        
        // Verificar que el torneo existe
        if (!torneoRepository.existsById(torneoId)) {
            throw new IllegalArgumentException("No existe torneo con ID: " + torneoId);
        }
        
        List<TorneoCategoria> relaciones = torneoCategoriaRepository.findByTorneoId(torneoId);
        return relaciones.stream()
                .map(TorneoCategoria::getCategoria)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene información básica de las categorías de un torneo (ID y nombre)
     */
    @Transactional(readOnly = true)
    public List<CategoriaBasicaDTO> obtenerCategoriasBasicasPorTorneo(Long torneoId) {
        logger.debug("Obteniendo información básica de categorías del torneo {}", torneoId);
        
        // Verificar que el torneo existe
        if (!torneoRepository.existsById(torneoId)) {
            throw new IllegalArgumentException("No existe torneo con ID: " + torneoId);
        }
        
        List<TorneoCategoria> relaciones = torneoCategoriaRepository.findByTorneoId(torneoId);
        return relaciones.stream()
                .map(relacion -> {
                    Categoria categoria = relacion.getCategoria();
                    Torneo torneo = relacion.getTorneo();
                    return new CategoriaBasicaDTO(
                        categoria.getIdCategoria(), 
                        categoria.getNombre(),
                        torneo.getId(),
                        torneo.getNombre()
                    );
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todos los torneos de una categoría
     */
    @Transactional(readOnly = true)
    public List<Torneo> obtenerTorneosPorCategoria(Long categoriaId) {
        logger.debug("Obteniendo torneos de la categoría {}", categoriaId);
        
        // Verificar que la categoría existe
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new IllegalArgumentException("No existe categoría con ID: " + categoriaId);
        }
        
        List<TorneoCategoria> relaciones = torneoCategoriaRepository.findByCategoriaId(categoriaId);
        return relaciones.stream()
                .map(TorneoCategoria::getTorneo)
                .collect(Collectors.toList());
    }
    
    /**
     * Verifica si una categoría está asociada a un torneo
     */
    @Transactional(readOnly = true)
    public boolean estaAsociada(Long torneoId, Long categoriaId) {
        return torneoCategoriaRepository.existsByTorneoIdAndCategoriaId(torneoId, categoriaId);
    }
    
    /**
     * Cuenta las categorías de un torneo
     */
    @Transactional(readOnly = true)
    public long contarCategoriasPorTorneo(Long torneoId) {
        return torneoCategoriaRepository.countByTorneoId(torneoId);
    }
    
    /**
     * Cuenta los torneos de una categoría
     */
    @Transactional(readOnly = true)
    public long contarTorneosPorCategoria(Long categoriaId) {
        return torneoCategoriaRepository.countByCategoriaId(categoriaId);
    }
    
    /**
     * Elimina todas las categorías de un torneo
     */
    public void eliminarTodasLasCategoriasDeTorneo(Long torneoId) {
        logger.info("Eliminando todas las categorías del torneo {}", torneoId);
        torneoCategoriaRepository.deleteByTorneoId(torneoId);
    }
    
    /**
     * Elimina todos los torneos de una categoría
     */
    public void eliminarTodosLosTorneosDeCategoria(Long categoriaId) {
        logger.info("Eliminando todos los torneos de la categoría {}", categoriaId);
        torneoCategoriaRepository.deleteByCategoriaId(categoriaId);
    }
}