package com.voley.application.categorias;

import com.voley.domain.Categoria;
import com.voley.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Caso de uso para obtener una categoría por ID
 */
@Component
public class ObtenerCategoriaPorIdUseCase {
    
    private final CategoriaService categoriaService;
    
    @Autowired
    public ObtenerCategoriaPorIdUseCase(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    
    /**
     * Ejecuta la búsqueda de una categoría por ID
     */
    public Optional<Categoria> ejecutar(Long id) {
        return categoriaService.buscarPorId(id);
    }
}