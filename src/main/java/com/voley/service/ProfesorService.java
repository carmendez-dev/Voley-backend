package com.voley.service;

import com.voley.domain.Profesor;
import com.voley.dto.ProfesorDTO;
import com.voley.dto.CrearProfesorDTO;
import com.voley.dto.CambiarPasswordDTO;
import com.voley.repository.ProfesorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar Profesores
 */
@Service
public class ProfesorService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProfesorService.class);
    
    private final ProfesorRepository profesorRepository;
    
    @Autowired
    public ProfesorService(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }
    
    /**
     * Encripta una contraseña usando SHA-256
     */
    private String encriptarPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar contraseña", e);
        }
    }
    
    /**
     * Crear un nuevo profesor
     */
    @Transactional
    public ProfesorDTO crear(CrearProfesorDTO dto) {
        logger.info("Creando profesor: {} {}", dto.getPrimerNombre(), dto.getPrimerApellido());
        
        // Validar que no exista un profesor con la misma cédula
        if (dto.getCedula() != null && profesorRepository.findByCedula(dto.getCedula()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un profesor con la cédula: " + dto.getCedula());
        }
        
        // Validar que no exista un profesor con el mismo email
        if (dto.getEmail() != null && profesorRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un profesor con el email: " + dto.getEmail());
        }
        
        // Crear entidad
        Profesor profesor = new Profesor();
        profesor.setPrimerNombre(dto.getPrimerNombre());
        profesor.setSegundoNombre(dto.getSegundoNombre());
        profesor.setPrimerApellido(dto.getPrimerApellido());
        profesor.setSegundoApellido(dto.getSegundoApellido());
        profesor.setEmail(dto.getEmail());
        profesor.setCelular(dto.getCelular());
        profesor.setContactoEmergencia(dto.getContactoEmergencia());
        profesor.setGenero(Profesor.Genero.valueOf(dto.getGenero()));
        profesor.setCedula(dto.getCedula());
        profesor.setPassword(encriptarPassword(dto.getPassword()));
        profesor.setEstado(Profesor.Estado.Activo);
        
        // Guardar
        Profesor guardado = profesorRepository.save(profesor);
        
        logger.info("Profesor creado con ID: {}", guardado.getIdProfesor());
        
        return convertirADTO(guardado);
    }
    
    /**
     * Obtener todos los profesores
     */
    @Transactional(readOnly = true)
    public List<ProfesorDTO> obtenerTodos() {
        return profesorRepository.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtener profesor por ID
     */
    @Transactional(readOnly = true)
    public Optional<ProfesorDTO> obtenerPorId(Integer id) {
        return profesorRepository.findById(id)
            .map(this::convertirADTO);
    }
    
    /**
     * Obtener profesor por cédula
     */
    @Transactional(readOnly = true)
    public Optional<ProfesorDTO> obtenerPorCedula(String cedula) {
        return profesorRepository.findByCedula(cedula)
            .map(this::convertirADTO);
    }
    
    /**
     * Obtener profesores por estado
     */
    @Transactional(readOnly = true)
    public List<ProfesorDTO> obtenerPorEstado(String estado) {
        Profesor.Estado estadoEnum = Profesor.Estado.valueOf(estado);
        return profesorRepository.findByEstado(estadoEnum).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Actualizar profesor
     */
    @Transactional
    public ProfesorDTO actualizar(Integer id, ProfesorDTO dto) {
        logger.info("Actualizando profesor ID: {}", id);
        
        Profesor profesor = profesorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe profesor con ID: " + id));
        
        // Validar cédula única si se está cambiando
        if (dto.getCedula() != null && !dto.getCedula().equals(profesor.getCedula())) {
            if (profesorRepository.findByCedula(dto.getCedula()).isPresent()) {
                throw new IllegalArgumentException("Ya existe un profesor con la cédula: " + dto.getCedula());
            }
        }
        
        // Validar email único si se está cambiando
        if (dto.getEmail() != null && !dto.getEmail().equals(profesor.getEmail())) {
            if (profesorRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Ya existe un profesor con el email: " + dto.getEmail());
            }
        }
        
        // Actualizar campos
        if (dto.getPrimerNombre() != null) {
            profesor.setPrimerNombre(dto.getPrimerNombre());
        }
        if (dto.getSegundoNombre() != null) {
            profesor.setSegundoNombre(dto.getSegundoNombre());
        }
        if (dto.getPrimerApellido() != null) {
            profesor.setPrimerApellido(dto.getPrimerApellido());
        }
        if (dto.getSegundoApellido() != null) {
            profesor.setSegundoApellido(dto.getSegundoApellido());
        }
        if (dto.getEmail() != null) {
            profesor.setEmail(dto.getEmail());
        }
        if (dto.getCelular() != null) {
            profesor.setCelular(dto.getCelular());
        }
        if (dto.getContactoEmergencia() != null) {
            profesor.setContactoEmergencia(dto.getContactoEmergencia());
        }
        if (dto.getGenero() != null) {
            profesor.setGenero(Profesor.Genero.valueOf(dto.getGenero()));
        }
        if (dto.getEstado() != null) {
            profesor.setEstado(Profesor.Estado.valueOf(dto.getEstado()));
        }
        if (dto.getCedula() != null) {
            profesor.setCedula(dto.getCedula());
        }
        
        Profesor actualizado = profesorRepository.save(profesor);
        
        logger.info("Profesor actualizado: {}", actualizado.getIdProfesor());
        
        return convertirADTO(actualizado);
    }
    
    /**
     * Cambiar contraseña de profesor por cédula
     */
    @Transactional
    public void cambiarPassword(String cedula, CambiarPasswordDTO dto) {
        logger.info("Cambiando contraseña para profesor con cédula: {}", cedula);
        
        Profesor profesor = profesorRepository.findByCedula(cedula)
            .orElseThrow(() -> new IllegalArgumentException("No existe profesor con cédula: " + cedula));
        
        // Validar nueva contraseña
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña es obligatoria");
        }
        
        if (dto.getPassword().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        
        // Encriptar y actualizar contraseña
        profesor.setPassword(encriptarPassword(dto.getPassword()));
        
        profesorRepository.save(profesor);
        
        logger.info("Contraseña actualizada exitosamente para profesor: {}", profesor.getNombreCompleto());
    }
    
    /**
     * Eliminar profesor
     */
    @Transactional
    public void eliminar(Integer id) {
        logger.info("Eliminando profesor ID: {}", id);
        
        Profesor profesor = profesorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe profesor con ID: " + id));
        
        profesorRepository.delete(profesor);
        
        logger.info("Profesor eliminado: {}", id);
    }
    
    /**
     * Convertir entidad a DTO
     */
    private ProfesorDTO convertirADTO(Profesor profesor) {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setIdProfesor(profesor.getIdProfesor());
        dto.setPrimerNombre(profesor.getPrimerNombre());
        dto.setSegundoNombre(profesor.getSegundoNombre());
        dto.setPrimerApellido(profesor.getPrimerApellido());
        dto.setSegundoApellido(profesor.getSegundoApellido());
        dto.setEmail(profesor.getEmail());
        dto.setCelular(profesor.getCelular());
        dto.setContactoEmergencia(profesor.getContactoEmergencia());
        dto.setGenero(profesor.getGenero().name());
        dto.setEstado(profesor.getEstado().name());
        dto.setCedula(profesor.getCedula());
        dto.setFechaRegistro(profesor.getFechaRegistro());
        dto.setUpdateAt(profesor.getUpdateAt());
        dto.setNombreCompleto(profesor.getNombreCompleto());
        return dto;
    }
}
