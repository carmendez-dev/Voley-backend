# Voley Backend

API REST para la gestión de usuarios del sistema de voley desarrollada con Spring Boot 3.5.6 y Java 21.

## 🏗️ Arquitectura

Este proyecto implementa **Arquitectura Hexagonal** (Puertos y Adaptadores) siguiendo los principios SOLID:

```
├── domain/           # Entidades del dominio
├── repository/       # Puertos (interfaces)
├── adapter/          # Adaptadores (implementaciones)
├── service/          # Lógica de negocio
├── controller/       # API REST
└── config/           # Configuraciones
```

## 🚀 Tecnologías

- **Java 21** - JDK LTS
- **Spring Boot 3.5.6** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **MySQL** - Base de datos principal
- **H2** - Base de datos para pruebas
- **Maven** - Gestión de dependencias
- **Spring Security** - Seguridad (deshabilitada para desarrollo)

## 📊 Modelo de Datos

### Usuario
- `id` (Long) - Identificador único
- `nombre` (String) - Nombre del usuario
- `apellido` (String) - Apellido del usuario
- `email` (String) - Correo electrónico (único)
- `telefono` (String) - Número de teléfono
- `genero` (Enum) - MASCULINO, FEMENINO, OTRO
- `tipoUsuario` (Enum) - JUGADOR, ENTRENADOR, ARBITRO, ADMINISTRADOR
- `estado` (Enum) - ACTIVO, INACTIVO, SUSPENDIDO
- `fechaCreacion` (LocalDateTime) - Fecha de creación
- `fechaActualizacion` (LocalDateTime) - Fecha de última actualización

## 🛠️ Configuración

### Base de Datos
```properties
# MySQL (Producción)
spring.datasource.url=jdbc:mysql://localhost:3306/voley
spring.datasource.username=root
spring.datasource.password=

# H2 (Pruebas)
spring.datasource.url=jdbc:h2:mem:testdb
```

### Servidor
```properties
server.port=8080
```

## 🔌 API Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/usuarios` | Listar todos los usuarios |
| GET | `/api/usuarios/{id}` | Obtener usuario por ID |
| POST | `/api/usuarios` | Crear nuevo usuario |
| PUT | `/api/usuarios/{id}` | Actualizar usuario existente |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario |

### Ejemplo de Usuario (JSON)
```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@email.com",
  "telefono": "+1234567890",
  "genero": "MASCULINO",
  "tipoUsuario": "JUGADOR",
  "estado": "ACTIVO"
}
```

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java 21+
- Maven 3.6+
- MySQL 5.5+ (para producción)

### Pasos
1. Clonar el repositorio:
```bash
git clone <url-del-repositorio>
cd voley-backend
```

2. Configurar la base de datos MySQL:
```sql
CREATE DATABASE voley;
```

3. Ejecutar la aplicación:
```bash
./mvnw spring-boot:run
```

4. La aplicación estará disponible en: `http://localhost:8080`

## 🧪 Pruebas

Ejecutar las pruebas unitarias:
```bash
./mvnw test
```

Las pruebas utilizan H2 en memoria para evitar dependencias externas.

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       ├── example/voley_backend/    # Aplicación principal
│   │       └── voley/                    # Dominio del negocio
│   │           ├── domain/               # Entidades
│   │           ├── repository/           # Puertos
│   │           ├── adapter/              # Adaptadores JPA
│   │           ├── service/              # Servicios de negocio
│   │           ├── controller/           # Controladores REST
│   │           └── config/               # Configuraciones
│   └── resources/
│       ├── application.properties        # Configuración principal
│       └── application-test.properties   # Configuración de pruebas
└── test/
    └── java/                            # Pruebas unitarias
```

## 🔒 Seguridad

La seguridad está **deshabilitada** para facilitar el desarrollo. En producción se debe:
- Habilitar autenticación JWT
- Configurar CORS apropiadamente
- Implementar autorización por roles
- Usar HTTPS

## 🤝 Contribución

1. Fork del proyecto
2. Crear una rama para la feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit de los cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👥 Autor

Desarrollado para el curso de Taller de Desarrollo de Software - Universidad 2025.