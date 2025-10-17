package com.voley.adapter;

import com.voley.domain.CategoriaEquipo;
import com.voley.repository.CategoriaEquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA para el repositorio de CategoriaEquipo
 * 
 * Implementa la interfaz CategoriaEquipoRepository usando Spring Data JPA
 * Maneja las relaciones many-to-many entre categorías y equipos
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class CategoriaEquipoJpaAdapter implements CategoriaEquipoRepository {
    
    private final CategoriaEquipoJpaRepository categoriaEquipoJpaRepository;
    
    @Autowired
    public CategoriaEquipoJpaAdapter(CategoriaEquipoJpaRepository categoriaEquipoJpaRepository) {
        this.categoriaEquipoJpaRepository = categoriaEquipoJpaRepository;
    }
    
    @Override
    public CategoriaEquipo guardar(CategoriaEquipo categoriaEquipo) {
        return categoriaEquipoJpaRepository.save(categoriaEquipo);
    }
    
    @Override
    public Optional<CategoriaEquipo> buscarPorId(Long id) {
        return categoriaEquipoJpaRepository.findById(id);
    }
    
    @Override
    public List<CategoriaEquipo> buscarTodos() {
        return categoriaEquipoJpaRepository.findAll();
    }
    
    @Override
    public void eliminar(Long id) {
        categoriaEquipoJpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existePorId(Long id) {
        return categoriaEquipoJpaRepository.existsById(id);
    }
    
    @Override
    public List<CategoriaEquipo> buscarEquiposPorCategoria(Long idCategoria) {
        return categoriaEquipoJpaRepository.findEquiposConDatosByCategoria(idCategoria);
    }
    
    @Override
    public List<CategoriaEquipo> buscarCategoriasPorEquipo(Long idEquipo) {
        return categoriaEquipoJpaRepository.findCategoriasConDatosByEquipo(idEquipo);
    }
    
    @Override
    public boolean existeRelacion(Long idCategoria, Long idEquipo) {
        return categoriaEquipoJpaRepository.existsByCategoriaIdCategoriaAndEquipoIdEquipo(idCategoria, idEquipo);
    }
    
    @Override
    public Optional<CategoriaEquipo> buscarRelacion(Long idCategoria, Long idEquipo) {
        return categoriaEquipoJpaRepository.findByCategoriaIdCategoriaAndEquipoIdEquipo(idCategoria, idEquipo);
    }
    
    @Override
    public void eliminarRelacion(Long idCategoria, Long idEquipo) {
        Optional<CategoriaEquipo> relacion = buscarRelacion(idCategoria, idEquipo);
        relacion.ifPresent(categoriaEquipo -> categoriaEquipoJpaRepository.delete(categoriaEquipo));
    }
    
    @Override
    public long contarEquiposPorCategoria(Long idCategoria) {
        return categoriaEquipoJpaRepository.countEquiposByCategoria(idCategoria);
    }
    
    @Override
    public long contarCategoriasPorEquipo(Long idEquipo) {
        return categoriaEquipoJpaRepository.countCategoriasByEquipo(idEquipo);
    }
    
    @Override
    public void eliminarTodasRelacionesCategoria(Long idCategoria) {
        List<CategoriaEquipo> relaciones = categoriaEquipoJpaRepository.findByCategoriaIdCategoria(idCategoria);
        categoriaEquipoJpaRepository.deleteAll(relaciones);
    }
    
    @Override
    public void eliminarTodasRelacionesEquipo(Long idEquipo) {
        List<CategoriaEquipo> relaciones = categoriaEquipoJpaRepository.findByEquipoIdEquipo(idEquipo);
        categoriaEquipoJpaRepository.deleteAll(relaciones);
    }
}