package com.voley.application.categorias;

import com.voley.domain.Categoria;
import com.voley.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para buscar categorías por nombre
 */
@Component
public class BuscarCategoriasPorNombreUseCase {
    
    private final CategoriaService categoriaService;
    
    @Autowired
    public BuscarCategoriasPorNombreUseCase(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    
    /**
     * Ejecuta la búsqueda de categorías por nombre
     */
    public List<Categoria> ejecutar(String nombre) {
        return categoriaService.buscarPorNombre(nombre);
    }
}