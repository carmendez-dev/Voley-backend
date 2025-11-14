package com.voley.service;

import com.voley.application.setpartido.*;
import com.voley.dto.SetPartidoDTO;
import com.voley.dto.SetPartidoUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar SetPartido
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
public class SetPartidoService {
    
    private final CrearSetPartidoUseCase crearUseCase;
    private final ObtenerTodosSetPartidoUseCase obtenerTodosUseCase;
    private final ObtenerSetPartidoPorIdUseCase obtenerPorIdUseCase;
    private final ObtenerSetsPorPartidoUseCase obtenerPorPartidoUseCase;
    private final ActualizarSetPartidoUseCase actualizarUseCase;
    private final EliminarSetPartidoUseCase eliminarUseCase;
    
    @Autowired
    public SetPartidoService(
            CrearSetPartidoUseCase crearUseCase,
            ObtenerTodosSetPartidoUseCase obtenerTodosUseCase,
            ObtenerSetPartidoPorIdUseCase obtenerPorIdUseCase,
            ObtenerSetsPorPartidoUseCase obtenerPorPartidoUseCase,
            ActualizarSetPartidoUseCase actualizarUseCase,
            EliminarSetPartidoUseCase eliminarUseCase) {
        this.crearUseCase = crearUseCase;
        this.obtenerTodosUseCase = obtenerTodosUseCase;
        this.obtenerPorIdUseCase = obtenerPorIdUseCase;
        this.obtenerPorPartidoUseCase = obtenerPorPartidoUseCase;
        this.actualizarUseCase = actualizarUseCase;
        this.eliminarUseCase = eliminarUseCase;
    }
    
    public SetPartidoDTO crear(SetPartidoDTO dto) {
        return crearUseCase.ejecutar(dto);
    }
    
    public List<SetPartidoDTO> obtenerTodos() {
        return obtenerTodosUseCase.ejecutar();
    }
    
    public Optional<SetPartidoDTO> obtenerPorId(Long id) {
        return obtenerPorIdUseCase.ejecutar(id);
    }
    
    public List<SetPartidoDTO> obtenerPorPartido(Long idPartido) {
        return obtenerPorPartidoUseCase.ejecutar(idPartido);
    }
    
    public SetPartidoDTO actualizar(Long id, SetPartidoUpdateDTO dto) {
        return actualizarUseCase.ejecutar(id, dto);
    }
    
    public void eliminar(Long id) {
        eliminarUseCase.ejecutar(id);
    }
}
