package com.voley.application.pagos;

import com.voley.domain.Pago;
import com.voley.domain.Usuario;
import com.voley.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener pagos por usuario
 * Encapsula la lógica de negocio para la búsqueda de pagos por usuario
 */
@Component
public class ObtenerPagosPorUsuarioUseCase {
    
    private final PagoService pagoService;
    
    @Autowired
    public ObtenerPagosPorUsuarioUseCase(PagoService pagoService) {
        this.pagoService = pagoService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener pagos por usuario
     * 
     * @param usuario El usuario cuyos pagos se buscan
     * @return Lista de pagos del usuario
     * @throws IllegalArgumentException Si el usuario es inválido
     */
    public List<Pago> ejecutar(Usuario usuario) {
        validarUsuario(usuario);
        return pagoService.obtenerPagosPorUsuario(usuario);
    }
    
    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
    }
}