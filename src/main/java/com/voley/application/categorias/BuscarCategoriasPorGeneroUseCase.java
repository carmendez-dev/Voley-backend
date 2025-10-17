package com.voley.application.categorias;

import com.voley.domain.Categoria;
import com.voley.domain.GeneroCategoria;
import com.voley.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para buscar categorías por género
 */
@Component
public class BuscarCategoriasPorGeneroUseCase {
    
    private final CategoriaService categoriaService;
    
    @Autowired
    public BuscarCategoriasPorGeneroUseCase(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    
    /**
     * Ejecuta la búsqueda de categorías por género
     */
    public List<Categoria> ejecutar(GeneroCategoria genero) {
        return categoriaService.buscarPorGenero(genero);
    }
}