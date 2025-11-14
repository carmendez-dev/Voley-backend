package com.voley.repository;

import com.voley.domain.ResultadoAccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad ResultadoAccion
 */
@Repository
public interface ResultadoAccionRepository extends JpaRepository<ResultadoAccion, Integer> {
}
