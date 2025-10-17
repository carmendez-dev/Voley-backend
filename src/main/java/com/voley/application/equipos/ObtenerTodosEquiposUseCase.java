package com.voley.application.equipos;

import com.voley.domain.Equipo;
import com.voley.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para obtener todos los equipos
 * 
 * Recupera la lista completa de equipos ordenados por nombre.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class ObtenerTodosEquiposUseCase {
    
    private final EquipoRepository equipoRepository;
    
    @Autowired
    public ObtenerTodosEquiposUseCase(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }
    
    /**
     * Ejecuta el caso de uso para obtener todos los equipos
     * @return Lista de todos los equipos ordenados por nombre
     */
    public List<Equipo> ejecutar() {
        return equipoRepository.buscarTodos();
    }
}