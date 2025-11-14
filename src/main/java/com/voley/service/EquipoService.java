package com.voley.service;

import com.voley.application.equipos.*;
import com.voley.domain.Equipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio principal para operaciones con Equipos
 * 
 * Orquesta todos los casos de uso relacionados con equipos.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
public class EquipoService {
    
    private final CrearEquipoUseCase crearEquipoUseCase;
    private final ObtenerTodosEquiposUseCase obtenerTodosEquiposUseCase;
    private final ObtenerEquipoPorIdUseCase obtenerEquipoPorIdUseCase;
    private final ActualizarEquipoUseCase actualizarEquipoUseCase;
    private final EliminarEquipoUseCase eliminarEquipoUseCase;
    private final BuscarEquiposPorNombreUseCase buscarEquiposPorNombreUseCase;
    
    @Autowired
    public EquipoService(CrearEquipoUseCase crearEquipoUseCase,
                        ObtenerTodosEquiposUseCase obtenerTodosEquiposUseCase,
                        ObtenerEquipoPorIdUseCase obtenerEquipoPorIdUseCase,
                        ActualizarEquipoUseCase actualizarEquipoUseCase,
                        EliminarEquipoUseCase eliminarEquipoUseCase,
                        BuscarEquiposPorNombreUseCase buscarEquiposPorNombreUseCase) {
        this.crearEquipoUseCase = crearEquipoUseCase;
        this.obtenerTodosEquiposUseCase = obtenerTodosEquiposUseCase;
        this.obtenerEquipoPorIdUseCase = obtenerEquipoPorIdUseCase;
        this.actualizarEquipoUseCase = actualizarEquipoUseCase;
        this.eliminarEquipoUseCase = eliminarEquipoUseCase;
        this.buscarEquiposPorNombreUseCase = buscarEquiposPorNombreUseCase;
    }
    
    // ===== OPERACIONES CRUD PARA EQUIPOS =====
    
    /**
     * Crea un nuevo equipo
     */
    public Equipo crearEquipo(String nombre, String descripcion) {
        return crearEquipoUseCase.ejecutar(nombre, descripcion);
    }
    
    /**
     * Obtiene todos los equipos
     */
    public List<Equipo> obtenerTodosEquipos() {
        return obtenerTodosEquiposUseCase.ejecutar();
    }
    
    /**
     * Obtiene un equipo por ID
     */
    public Equipo obtenerEquipoPorId(Long id) {
        return obtenerEquipoPorIdUseCase.ejecutar(id);
    }
    
    /**
     * Actualiza un equipo existente
     */
    public Equipo actualizarEquipo(Long id, String nombre, String descripcion) {
        return actualizarEquipoUseCase.ejecutar(id, nombre, descripcion);
    }
    
    /**
     * Elimina un equipo por ID
     */
    public void eliminarEquipo(Long id) {
        eliminarEquipoUseCase.ejecutar(id);
    }
    
    /**
     * Busca equipos por nombre
     */
    public List<Equipo> buscarEquiposPorNombre(String nombre) {
        return buscarEquiposPorNombreUseCase.ejecutar(nombre);
    }
}