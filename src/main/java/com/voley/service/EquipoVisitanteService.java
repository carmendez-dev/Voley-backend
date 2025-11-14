package com.voley.service;

import com.voley.application.equipovisitante.*;
import com.voley.dto.EquipoVisitanteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar EquipoVisitante
 * Orquesta los casos de uso
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
public class EquipoVisitanteService {
    
    private final CrearEquipoVisitanteUseCase crearUseCase;
    private final ObtenerTodosEquiposVisitantesUseCase obtenerTodosUseCase;
    private final ObtenerEquipoVisitantePorIdUseCase obtenerPorIdUseCase;
    private final BuscarEquiposVisitantesPorNombreUseCase buscarPorNombreUseCase;
    private final ActualizarEquipoVisitanteUseCase actualizarUseCase;
    private final EliminarEquipoVisitanteUseCase eliminarUseCase;
    
    @Autowired
    public EquipoVisitanteService(
            CrearEquipoVisitanteUseCase crearUseCase,
            ObtenerTodosEquiposVisitantesUseCase obtenerTodosUseCase,
            ObtenerEquipoVisitantePorIdUseCase obtenerPorIdUseCase,
            BuscarEquiposVisitantesPorNombreUseCase buscarPorNombreUseCase,
            ActualizarEquipoVisitanteUseCase actualizarUseCase,
            EliminarEquipoVisitanteUseCase eliminarUseCase) {
        this.crearUseCase = crearUseCase;
        this.obtenerTodosUseCase = obtenerTodosUseCase;
        this.obtenerPorIdUseCase = obtenerPorIdUseCase;
        this.buscarPorNombreUseCase = buscarPorNombreUseCase;
        this.actualizarUseCase = actualizarUseCase;
        this.eliminarUseCase = eliminarUseCase;
    }
    
    public EquipoVisitanteDTO crear(EquipoVisitanteDTO dto) {
        return crearUseCase.ejecutar(dto);
    }
    
    public List<EquipoVisitanteDTO> obtenerTodos() {
        return obtenerTodosUseCase.ejecutar();
    }
    
    public Optional<EquipoVisitanteDTO> obtenerPorId(Long id) {
        return obtenerPorIdUseCase.ejecutar(id);
    }
    
    public List<EquipoVisitanteDTO> buscarPorNombre(String nombre) {
        return buscarPorNombreUseCase.ejecutar(nombre);
    }
    
    public EquipoVisitanteDTO actualizar(Long id, EquipoVisitanteDTO dto) {
        return actualizarUseCase.ejecutar(id, dto);
    }
    
    public void eliminar(Long id) {
        eliminarUseCase.ejecutar(id);
    }
}
