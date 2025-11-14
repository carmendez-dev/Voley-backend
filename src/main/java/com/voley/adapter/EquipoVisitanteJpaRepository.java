package com.voley.adapter;

import com.voley.domain.EquipoVisitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para EquipoVisitante
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Repository
public interface EquipoVisitanteJpaRepository extends JpaRepository<EquipoVisitante, Long> {
    
    /**
     * Busca equipos visitantes por nombre (búsqueda parcial, case-insensitive)
     */
    List<EquipoVisitante> findByNombreContainingIgnoreCase(String nombre);
    
    /**
     * Busca un equipo visitante por nombre exacto
     */
    Optional<EquipoVisitante> findByNombre(String nombre);
    
    /**
     * Verifica si existe un equipo visitante con el nombre dado
     */
    boolean existsByNombre(String nombre);
}
