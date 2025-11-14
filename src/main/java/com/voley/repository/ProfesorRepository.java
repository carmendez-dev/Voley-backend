package com.voley.repository;

import com.voley.domain.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Profesor
 */
@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {
    
    /**
     * Busca un profesor por email
     */
    Optional<Profesor> findByEmail(String email);
    
    /**
     * Busca un profesor por cédula
     */
    Optional<Profesor> findByCedula(String cedula);
    
    /**
     * Busca profesores por estado
     */
    List<Profesor> findByEstado(Profesor.Estado estado);
}
