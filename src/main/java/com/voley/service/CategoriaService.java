package com.voley.service;

import com.voley.domain.Categoria;
import com.voley.domain.GeneroCategoria;
import com.voley.repository.CategoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de categorías que maneja la lógica de negocio
 * Coordina las operaciones entre los casos de uso y el repositorio
 */
@Service
@Transactional
public class CategoriaService {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);
    
    private final CategoriaRepository categoriaRepository;
    
    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }
    
    /**
     * Crea una nueva categoría
     * Permite el mismo nombre si el género es diferente
     */
    public Categoria crearCategoria(Categoria categoria) {
        logger.info("Creando nueva categoría: {} - {}", categoria.getNombre(), categoria.getGenero());
        
        // Validar datos
        categoria.validar();
        
        // Verificar que no exista la combinación nombre + género
        if (categoriaRepository.existePorNombreYGenero(categoria.getNombre(), categoria.getGenero())) {
            throw new IllegalArgumentException(
                String.format("Ya existe una categoría con el nombre '%s' y género '%s'", 
                             categoria.getNombre(), 
                             categoria.getGenero() != null ? categoria.getGenero().getDescripcion() : "Sin género")
            );
        }
        
        Categoria categoriaGuardada = categoriaRepository.guardar(categoria);
        logger.info("Categoría creada exitosamente con ID: {}", categoriaGuardada.getIdCategoria());
        
        return categoriaGuardada;
    }
    
    /**
     * Actualiza una categoría existente
     * Permite el mismo nombre si el género es diferente
     */
    public Categoria actualizarCategoria(Long id, Categoria categoriaActualizada) {
        logger.info("Actualizando categoría con ID: {}", id);
        
        // Verificar que existe
        Optional<Categoria> categoriaExistente = categoriaRepository.buscarPorId(id);
        if (categoriaExistente.isEmpty()) {
            throw new IllegalArgumentException("No existe categoría con ID: " + id);
        }
        
        Categoria categoria = categoriaExistente.get();
        
        // Validar datos actualizados
        categoriaActualizada.validar();
        
        // Verificar que no exista otra con el mismo nombre y género
        if (categoriaRepository.existePorNombreYGenero(
                categoriaActualizada.getNombre(), 
                categoriaActualizada.getGenero(), 
                id)) {
            throw new IllegalArgumentException(
                String.format("Ya existe otra categoría con el nombre '%s' y género '%s'", 
                             categoriaActualizada.getNombre(),
                             categoriaActualizada.getGenero() != null ? categoriaActualizada.getGenero().getDescripcion() : "Sin género")
            );
        }
        
        // Actualizar campos
        categoria.setNombre(categoriaActualizada.getNombre());
        categoria.setGenero(categoriaActualizada.getGenero());
        
        Categoria categoriaGuardada = categoriaRepository.guardar(categoria);
        logger.info("Categoría actualizada exitosamente: {}", id);
        
        return categoriaGuardada;
    }
    
    /**
     * Elimina una categoría
     */
    public void eliminarCategoria(Long id) {
        logger.info("Eliminando categoría con ID: {}", id);
        
        if (!categoriaRepository.existe(id)) {
            throw new IllegalArgumentException("No existe categoría con ID: " + id);
        }
        
        // TODO: Verificar si la categoría está asociada a torneos
        // En el futuro se podría agregar esta validación
        
        categoriaRepository.eliminar(id);
        logger.info("Categoría eliminada exitosamente: {}", id);
    }
    
    /**
     * Busca una categoría por ID
     */
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorId(Long id) {
        logger.debug("Buscando categoría con ID: {}", id);
        return categoriaRepository.buscarPorId(id);
    }
    
    /**
     * Obtiene todas las categorías
     */
    @Transactional(readOnly = true)
    public List<Categoria> obtenerTodasLasCategorias() {
        logger.debug("Obteniendo todas las categorías");
        return categoriaRepository.buscarTodas();
    }
    
    /**
     * Busca categorías por género
     */
    @Transactional(readOnly = true)
    public List<Categoria> buscarPorGenero(GeneroCategoria genero) {
        logger.debug("Obteniendo categorías con género: {}", genero);
        return categoriaRepository.buscarPorGenero(genero);
    }
    
    /**
     * Busca categorías por nombre
     */
    @Transactional(readOnly = true)
    public List<Categoria> buscarPorNombre(String nombre) {
        logger.debug("Buscando categorías que contengan: {}", nombre);
        return categoriaRepository.buscarPorNombre(nombre);
    }
    
    /**
     * Obtiene estadísticas básicas de categorías
     */
    @Transactional(readOnly = true)
    public CategoriaEstadisticas obtenerEstadisticas() {
        logger.debug("Obteniendo estadísticas de categorías");
        
        long total = categoriaRepository.contar();
        long masculinas = categoriaRepository.contarPorGenero(GeneroCategoria.Masculino);
        long femeninas = categoriaRepository.contarPorGenero(GeneroCategoria.Femenino);
        long mixtas = categoriaRepository.contarPorGenero(GeneroCategoria.Mixto);
        
        return new CategoriaEstadisticas(total, masculinas, femeninas, mixtas);
    }
    
    /**
     * Clase interna para estadísticas de categorías
     */
    public static class CategoriaEstadisticas {
        private final long total;
        private final long masculinas;
        private final long femeninas;
        private final long mixtas;
        
        public CategoriaEstadisticas(long total, long masculinas, long femeninas, long mixtas) {
            this.total = total;
            this.masculinas = masculinas;
            this.femeninas = femeninas;
            this.mixtas = mixtas;
        }
        
        // Getters
        public long getTotal() { return total; }
        public long getMasculinas() { return masculinas; }
        public long getFemeninas() { return femeninas; }
        public long getMixtas() { return mixtas; }
    }
}