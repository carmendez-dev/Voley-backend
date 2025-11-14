package com.voley.adapter;

import com.voley.domain.Categoria;
import com.voley.domain.GeneroCategoria;
import com.voley.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA que implementa el repositorio de categorías
 * Conecta el dominio con la infraestructura de persistencia
 */
@Component
public class CategoriaJpaAdapter implements CategoriaRepository {
    
    private final CategoriaJpaRepository jpaRepository;
    
    @Autowired
    public CategoriaJpaAdapter(CategoriaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Categoria guardar(Categoria categoria) {
        categoria.validar();
        return jpaRepository.save(categoria);
    }
    
    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        return jpaRepository.findById(id);
    }
    
    @Override
    public List<Categoria> buscarTodas() {
        return jpaRepository.findAllOrderByNombre();
    }
    
    @Override
    public List<Categoria> buscarPorGenero(GeneroCategoria genero) {
        return jpaRepository.findByGeneroOrderByNombre(genero);
    }
    
    @Override
    public List<Categoria> buscarPorNombre(String nombre) {
        return jpaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Override
    public Optional<Categoria> buscarPorNombreExacto(String nombre) {
        return jpaRepository.findByNombreIgnoreCase(nombre);
    }
    
    @Override
    public boolean existePorNombre(String nombre) {
        return jpaRepository.existsByNombreIgnoreCase(nombre);
    }
    
    @Override
    public boolean existePorNombre(String nombre, Long idExcluir) {
        return jpaRepository.existsByNombreIgnoreCaseAndIdNot(nombre, idExcluir);
    }
    
    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public void eliminar(Categoria categoria) {
        jpaRepository.delete(categoria);
    }
    
    @Override
    public boolean existe(Long id) {
        return jpaRepository.existsById(id);
    }
    
    @Override
    public long contar() {
        return jpaRepository.count();
    }
    
    @Override
    public long contarPorGenero(GeneroCategoria genero) {
        return jpaRepository.countByGenero(genero);
    }
    
    @Override
    public boolean existePorNombreYGenero(String nombre, GeneroCategoria genero) {
        return jpaRepository.existsByNombreAndGenero(nombre, genero);
    }
    
    @Override
    public boolean existePorNombreYGenero(String nombre, GeneroCategoria genero, Long idExcluir) {
        return jpaRepository.existsByNombreAndGeneroAndIdNot(nombre, genero, idExcluir);
    }
}