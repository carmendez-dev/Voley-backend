package com.voley.service;

import com.voley.domain.Usuario;
import com.voley.dto.UsuarioRequestDTO;
import com.voley.dto.UsuarioResponseDTO;
import com.voley.repository.UsuarioRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Usuario
 * Contiene la lógica de negocio
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioRepositoryPort usuarioRepository;
    private final PagoService pagoService;
    
    @Autowired
    public UsuarioServiceImpl(UsuarioRepositoryPort usuarioRepository, @Lazy PagoService pagoService) {
        this.usuarioRepository = usuarioRepository;
        this.pagoService = pagoService;
    }
    
    @Override
    public Usuario crearUsuario(Usuario usuario) {
        // Validar que la cédula no esté en uso
        if (usuarioRepository.existsByCedula(usuario.getCedula())) {
            throw new IllegalArgumentException("Ya existe un usuario con la cédula: " + usuario.getCedula());
        }
        
        // Validar que el email no esté en uso
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        
        // Guardar el usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        // Generar pagos automáticamente para el nuevo usuario
        try {
            pagoService.generarPagosParaNuevoUsuario(usuarioGuardado);
        } catch (Exception e) {
            // Log del error pero no fallar la creación del usuario
            System.err.println("Error generando pagos para el nuevo usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return usuarioGuardado;
    }
    
    @Override
    public UsuarioResponseDTO crearUsuarioConDTO(UsuarioRequestDTO usuarioDTO) {
        // Validar unicidad
        if (usuarioRepository.existsByCedula(usuarioDTO.getCedula())) {
            throw new IllegalArgumentException("Ya existe un usuario con la cédula: " + usuarioDTO.getCedula());
        }
        
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuarioDTO.getEmail());
        }
        
        // Convertir DTO a entidad
        Usuario usuario = convertirDTOAEntidad(usuarioDTO);
        
        // Guardar
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        // Generar pagos automáticamente
        try {
            pagoService.generarPagosParaNuevoUsuario(usuarioGuardado);
        } catch (Exception e) {
            System.err.println("Error generando pagos: " + e.getMessage());
        }
        
        // Convertir a DTO de respuesta
        return convertirEntidadADTO(usuarioGuardado);
    }
    
    @Override
    public UsuarioResponseDTO actualizarUsuarioConDTO(Long id, UsuarioRequestDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe un usuario con ID: " + id));
        
        // Validar unicidad solo si cambió la cédula o email
        if (!usuarioExistente.getCedula().equals(usuarioDTO.getCedula()) && 
            usuarioRepository.existsByCedula(usuarioDTO.getCedula())) {
            throw new IllegalArgumentException("Ya existe otro usuario con la cédula: " + usuarioDTO.getCedula());
        }
        
        if (!usuarioExistente.getEmail().equals(usuarioDTO.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Ya existe otro usuario con el email: " + usuarioDTO.getEmail());
        }
        
        // Actualizar campos
        actualizarCamposDeEntidad(usuarioExistente, usuarioDTO);
        
        // Guardar
        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        
        return convertirEntidadADTO(usuarioActualizado);
    }
    
    @Override
    public Optional<UsuarioResponseDTO> obtenerUsuarioCompletoDTO(Long id) {
        return usuarioRepository.findById(id)
            .map(this::convertirEntidadADTO);
    }
    
    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    @Override
    public Optional<Usuario> obtenerUsuarioPorCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula);
    }
    
    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
    
    @Override
    public List<UsuarioResponseDTO> obtenerTodosLosUsuariosDTO() {
        return usuarioRepository.findAll().stream()
            .map(this::convertirEntidadADTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<UsuarioResponseDTO> obtenerUsuariosPorEstadoDTO(Usuario.EstadoUsuario estado) {
        return usuarioRepository.findByEstado(estado).stream()
            .map(this::convertirEntidadADTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe un usuario con ID: " + id));
        
        // Validar cédula si se está cambiando
        if (!usuario.getCedula().equals(usuarioActualizado.getCedula()) 
            && usuarioRepository.existsByCedula(usuarioActualizado.getCedula())) {
            throw new IllegalArgumentException("Ya existe un usuario con la cédula: " + usuarioActualizado.getCedula());
        }
        
        // Validar email si se está cambiando
        if (!usuario.getEmail().equals(usuarioActualizado.getEmail()) 
            && usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuarioActualizado.getEmail());
        }
        
        // Actualizar campos con nueva estructura
        usuario.setPrimerNombre(usuarioActualizado.getPrimerNombre());
        usuario.setSegundoNombre(usuarioActualizado.getSegundoNombre());
        usuario.setTercerNombre(usuarioActualizado.getTercerNombre());
        usuario.setPrimerApellido(usuarioActualizado.getPrimerApellido());
        usuario.setSegundoApellido(usuarioActualizado.getSegundoApellido());
        usuario.setFechaNacimiento(usuarioActualizado.getFechaNacimiento());
        usuario.setCedula(usuarioActualizado.getCedula());
        usuario.setGenero(usuarioActualizado.getGenero());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setCelular(usuarioActualizado.getCelular());
        usuario.setDireccion(usuarioActualizado.getDireccion());
        usuario.setContactoEmergencia(usuarioActualizado.getContactoEmergencia());
        usuario.setPeso(usuarioActualizado.getPeso());
        usuario.setAltura(usuarioActualizado.getAltura());
        usuario.setEstado(usuarioActualizado.getEstado());
        
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("No existe un usuario con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
    
    @Override
    public List<Usuario> obtenerUsuariosPorEstado(Usuario.EstadoUsuario estado) {
        return usuarioRepository.findByEstado(estado);
    }
    
    @Override
    public List<Usuario> obtenerUsuariosPorTipo(Usuario.TipoUsuario tipo) {
        // Método de compatibilidad - devolver todos los usuarios ya que no hay campo tipo
        return usuarioRepository.findAll();
    }
    
    @Override
    public Usuario cambiarEstadoUsuario(Long id, Usuario.EstadoUsuario nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe un usuario con ID: " + id));
        
        usuario.setEstado(nuevoEstado);
        return usuarioRepository.save(usuario);
    }
    
    // Métodos de búsqueda avanzada
    @Override
    public List<Usuario> obtenerUsuariosPorRangoPeso(Float pesoMin, Float pesoMax) {
        return usuarioRepository.findByPesoBetween(pesoMin, pesoMax);
    }
    
    @Override
    public List<Usuario> obtenerUsuariosPorRangoAltura(Float alturaMin, Float alturaMax) {
        return usuarioRepository.findByAlturaBetween(alturaMin, alturaMax);
    }
    
    @Override
    public List<Usuario> buscarUsuariosPorNombres(String nombre) {
        return usuarioRepository.findByPrimerNombreContainingIgnoreCaseOrSegundoNombreContainingIgnoreCaseOrTercerNombreContainingIgnoreCase(
            nombre, nombre, nombre);
    }
    
    @Override
    public List<Usuario> buscarUsuariosPorApellidos(String apellido) {
        return usuarioRepository.findByPrimerApellidoContainingIgnoreCaseOrSegundoApellidoContainingIgnoreCase(
            apellido, apellido);
    }
    
    @Override
    public Map<String, Object> obtenerEstadisticasUsuarios() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        List<Usuario> todosLosUsuarios = usuarioRepository.findAll();
        
        // Estadísticas básicas
        estadisticas.put("totalUsuarios", todosLosUsuarios.size());
        estadisticas.put("usuariosActivos", todosLosUsuarios.stream().filter(u -> u.getEstado() == Usuario.EstadoUsuario.Activo).count());
        estadisticas.put("usuariosInactivos", todosLosUsuarios.stream().filter(u -> u.getEstado() == Usuario.EstadoUsuario.Inactivo).count());
        
        // Estadísticas por género
        Map<String, Long> porGenero = todosLosUsuarios.stream()
            .collect(Collectors.groupingBy(u -> u.getGenero().toString(), Collectors.counting()));
        estadisticas.put("porGenero", porGenero);
        
        // Estadísticas físicas
        double pesoPromedio = todosLosUsuarios.stream()
            .filter(u -> u.getPeso() != null)
            .mapToDouble(Usuario::getPeso)
            .average().orElse(0.0);
        estadisticas.put("pesoPromedio", pesoPromedio);
        
        double alturaPromedio = todosLosUsuarios.stream()
            .filter(u -> u.getAltura() != null)
            .mapToDouble(Usuario::getAltura)
            .average().orElse(0.0);
        estadisticas.put("alturaPromedio", alturaPromedio);
        
        return estadisticas;
    }
    
    // Métodos de conversión entre DTOs y Entidades
    private Usuario convertirDTOAEntidad(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setPrimerNombre(dto.getPrimerNombre());
        usuario.setSegundoNombre(dto.getSegundoNombre());
        usuario.setTercerNombre(dto.getTercerNombre());
        usuario.setPrimerApellido(dto.getPrimerApellido());
        usuario.setSegundoApellido(dto.getSegundoApellido());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setCedula(dto.getCedula());
        usuario.setGenero(dto.getGenero());
        usuario.setEmail(dto.getEmail());
        usuario.setCelular(dto.getCelular());
        usuario.setDireccion(dto.getDireccion());
        usuario.setContactoEmergencia(dto.getContactoEmergencia());
        usuario.setPeso(dto.getPeso());
        usuario.setAltura(dto.getAltura());
        usuario.setEstado(Usuario.EstadoUsuario.Activo); // Por defecto activo
        return usuario;
    }
    
    private UsuarioResponseDTO convertirEntidadADTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setPrimerNombre(usuario.getPrimerNombre());
        dto.setSegundoNombre(usuario.getSegundoNombre());
        dto.setTercerNombre(usuario.getTercerNombre());
        dto.setPrimerApellido(usuario.getPrimerApellido());
        dto.setSegundoApellido(usuario.getSegundoApellido());
        
        // Campos calculados
        dto.setNombreCompleto(usuario.getNombresCompletos());
        dto.setApellidosCompletos(usuario.getApellidosCompletos());
        
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setCedula(usuario.getCedula());
        dto.setGenero(usuario.getGenero());
        dto.setEmail(usuario.getEmail());
        dto.setCelular(usuario.getCelular());
        dto.setDireccion(usuario.getDireccion());
        dto.setContactoEmergencia(usuario.getContactoEmergencia());
        dto.setEstado(usuario.getEstado());
        dto.setPeso(usuario.getPeso());
        dto.setAltura(usuario.getAltura());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        dto.setUpdatedAt(usuario.getUpdatedAt());
        
        // Calcular edad
        if (usuario.getFechaNacimiento() != null) {
            dto.setEdad(Period.between(usuario.getFechaNacimiento(), LocalDate.now()).getYears());
        }
        
        // Calcular IMC
        if (usuario.getPeso() != null && usuario.getAltura() != null && usuario.getAltura() > 0) {
            float imc = usuario.getPeso() / (usuario.getAltura() * usuario.getAltura());
            dto.setImc(imc);
        }
        
        return dto;
    }
    
    private void actualizarCamposDeEntidad(Usuario entidad, UsuarioRequestDTO dto) {
        entidad.setPrimerNombre(dto.getPrimerNombre());
        entidad.setSegundoNombre(dto.getSegundoNombre());
        entidad.setTercerNombre(dto.getTercerNombre());
        entidad.setPrimerApellido(dto.getPrimerApellido());
        entidad.setSegundoApellido(dto.getSegundoApellido());
        entidad.setFechaNacimiento(dto.getFechaNacimiento());
        entidad.setCedula(dto.getCedula());
        entidad.setGenero(dto.getGenero());
        entidad.setEmail(dto.getEmail());
        entidad.setCelular(dto.getCelular());
        entidad.setDireccion(dto.getDireccion());
        entidad.setContactoEmergencia(dto.getContactoEmergencia());
        entidad.setPeso(dto.getPeso());
        entidad.setAltura(dto.getAltura());
    }
}