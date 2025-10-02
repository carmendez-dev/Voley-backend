# Arquitectura de Casos de Uso - Clean Architecture

## 📁 Estructura de Carpetas

```
src/main/java/com/voley/application/
├── pagos/
│   ├── CrearPagoUseCase.java
│   ├── ObtenerTodosLosPagosUseCase.java
│   ├── ObtenerPagoPorIdUseCase.java
│   ├── ObtenerPagosPorUsuarioUseCase.java
│   ├── ObtenerPagosPorEstadoUseCase.java
│   ├── ProcesarPagoUseCase.java
│   └── VerificarPagosEnAtrasoUseCase.java
└── usuarios/
    ├── CrearUsuarioUseCase.java
    ├── ObtenerTodosLosUsuariosUseCase.java
    ├── ObtenerUsuarioPorIdUseCase.java
    ├── ObtenerUsuarioPorCedulaUseCase.java
    ├── ObtenerUsuarioPorEmailUseCase.java
    ├── ActualizarUsuarioUseCase.java
    └── EliminarUsuarioUseCase.java
```

## 🏗️ Principios de Clean Architecture

### Separación de Responsabilidades
- **Controladores**: Solo manejan HTTP requests/responses
- **Casos de Uso**: Contienen la lógica de negocio específica
- **Servicios**: Implementan reglas de negocio complejas
- **Repositorios**: Manejan persistencia de datos

### Beneficios de esta Arquitectura

1. **Testabilidad**: Cada caso de uso puede ser probado de forma independiente
2. **Mantenibilidad**: Lógica de negocio centralizada y organizada
3. **Reutilización**: Los casos de uso pueden ser utilizados desde diferentes controladores
4. **Flexibilidad**: Fácil modificación de reglas de negocio sin afectar otros componentes

## 📋 Casos de Uso Implementados

### Para Usuarios

#### CrearUsuarioUseCase
- **Propósito**: Crear un nuevo usuario con validaciones específicas
- **Validaciones**:
  - Campos obligatorios (nombres, cédula, email, celular)
  - Formato de email válido
  - Formato de cédula válido

#### ObtenerUsuarioPorIdUseCase
- **Propósito**: Buscar usuario por ID
- **Validaciones**: ID válido y positivo

#### ObtenerUsuarioPorCedulaUseCase
- **Propósito**: Buscar usuario por cédula
- **Validaciones**: Formato de cédula (8-15 dígitos)

#### ObtenerUsuarioPorEmailUseCase
- **Propósito**: Buscar usuario por email
- **Validaciones**: Formato de email válido

#### ActualizarUsuarioUseCase
- **Propósito**: Actualizar datos de un usuario existente
- **Validaciones**: Validaciones condicionales para campos actualizados

#### EliminarUsuarioUseCase
- **Propósito**: Eliminar un usuario del sistema
- **Validaciones**: ID válido

### Para Pagos

#### CrearPagoUseCase
- **Propósito**: Crear un nuevo pago
- **Validaciones**:
  - Pago no nulo
  - Usuario asociado válido
  - Monto mayor a cero
  - Fecha de vencimiento válida

#### ObtenerTodosLosPagosUseCase
- **Propósito**: Obtener lista completa de pagos
- **Sin validaciones específicas**

#### ObtenerPagoPorIdUseCase
- **Propósito**: Buscar pago por ID
- **Validaciones**: ID válido y positivo

#### ObtenerPagosPorUsuarioUseCase
- **Propósito**: Obtener pagos de un usuario específico
- **Validaciones**: Usuario válido con ID no nulo

#### ObtenerPagosPorEstadoUseCase
- **Propósito**: Filtrar pagos por estado
- **Validaciones**: Estado no nulo

#### ProcesarPagoUseCase
- **Propósito**: Marcar un pago como pagado
- **Validaciones**:
  - ID de pago válido
  - Monto mayor a cero
  - Método de pago no vacío

#### VerificarPagosEnAtrasoUseCase
- **Propósito**: Ejecutar verificación manual de pagos atrasados
- **Sin parámetros**: Procesa todos los pagos automáticamente

#### EliminarPagoUseCase
- **Propósito**: Eliminar un pago que no esté pagado
- **Validaciones**:
  - ID de pago válido y positivo
  - Pago existe en la base de datos
  - Estado del pago no sea "pagado"
- **Excepciones**:
  - `IllegalArgumentException` para parámetros inválidos
  - `IllegalStateException` si se intenta eliminar un pago pagado

## 🔄 Migración desde Controladores Tradicionales

### Controladores Originales
- `/api/usuarios` - Controlador tradicional
- `/api/pagos` - Controlador tradicional

### Nuevos Controladores con Casos de Uso
- `/api/v2/usuarios` - UsuarioUseCaseController
- `/api/v2/pagos` - PagoUseCaseController

## 💡 Cómo Usar los Casos de Uso

### Ejemplo en Controlador
```java
@RestController
public class MiControlador {
    
    private final CrearUsuarioUseCase crearUsuarioUseCase;
    
    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioCreado = crearUsuarioUseCase.ejecutar(usuario);
            return ResponseEntity.ok(usuarioCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
```

### Ejemplo en Test
```java
@ExtendWith(MockitoExtension.class)
class CrearUsuarioUseCaseTest {
    
    @Mock
    private UsuarioService usuarioService;
    
    @InjectMocks
    private CrearUsuarioUseCase crearUsuarioUseCase;
    
    @Test
    void deberiaCrearUsuarioExitosamente() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNombres("Juan");
        usuario.setCedula("12345678");
        
        // When
        Usuario resultado = crearUsuarioUseCase.ejecutar(usuario);
        
        // Then
        assertNotNull(resultado);
        verify(usuarioService).crearUsuario(usuario);
    }
}
```

## 🚀 Próximos Pasos

1. **Migrar controladores existentes** para usar casos de uso
2. **Crear tests unitarios** para cada caso de uso
3. **Agregar casos de uso adicionales** según necesidades del negocio
4. **Implementar patrones de Command/Query** para mayor separación

## 📚 Recursos Adicionales

- [Clean Architecture por Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [CQRS Pattern](https://martinfowler.com/bliki/CQRS.html)