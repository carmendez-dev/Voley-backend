package com.voley.repository;

import com.voley.domain.TipoAccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad TipoAccion
 */
@Repository
public interface TipoAccionRepository extends JpaRepository<TipoAccion, Integer> {
}
