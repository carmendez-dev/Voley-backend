# 🌐 API Endpoints - Guía para Frontend React

## 📋 Información General

- **Base URL**: `http://localhost:8080`
- **Formato de respuesta**: JSON
- **Autenticación**: No implementada (por ahora)
- **CORS**: Habilitado para todos los orígenes (`*`)

## 🔗 Endpoints Disponibles

### 👥 **USUARIOS**

#### 1. **Crear Usuario**
```http
POST /api/usuarios
Content-Type: application/json

{
    "nombres": "Juan Carlos",
    "apellidos": "Pérez González",
    "fechaNacimiento": "1990-05-15",
    "cedula": "12345678901",
    "email": "juan.perez@email.com",
    "celular": "3001234567",
    "genero": "MASCULINO",
    "tipo": "JUGADOR",
    "estado": "ACTIVO"
}
```

**Respuesta Exitosa (201 Created):**
```json
{
    "id": 1,
    "nombres": "Juan Carlos",
    "apellidos": "Pérez González",
    "fechaNacimiento": "1990-05-15",
    "cedula": "12345678901",
    "email": "juan.perez@email.com",
    "celular": "3001234567",
    "genero": "MASCULINO",
    "tipo": "JUGADOR",
    "estado": "ACTIVO",
    "fechaRegistro": "2025-10-02T18:30:00"
}
```

#### 2. **Obtener Todos los Usuarios**
```http
GET /api/usuarios
```

**Respuesta:**
```json
[
    {
        "id": 1,
        "nombres": "Juan Carlos",
        "apellidos": "Pérez González",
        "email": "juan.perez@email.com",
        "cedula": "12345678901",
        "celular": "3001234567",
        "estado": "ACTIVO",
        "tipo": "JUGADOR"
    }
]
```

#### 3. **Obtener Usuario por ID**
```http
GET /api/usuarios/{id}
```

#### 4. **Obtener Usuario por Cédula**
```http
GET /api/usuarios/cedula/{cedula}
```

#### 5. **Obtener Usuario por Email**
```http
GET /api/usuarios/email/{email}
```

#### 6. **Actualizar Usuario**
```http
PUT /api/usuarios/{id}
Content-Type: application/json

{
    "nombres": "Juan Carlos Actualizado",
    "apellidos": "Pérez González",
    "email": "nuevo.email@email.com",
    "celular": "3009876543"
}
```

#### 7. **Eliminar Usuario**
```http
DELETE /api/usuarios/{id}
```

---

### 💰 **PAGOS**

#### 1. **Obtener Todos los Pagos**
```http
GET /api/pagos
```

**Respuesta Exitosa:**
```json
{
    "success": true,
    "message": "Pagos obtenidos exitosamente",
    "timestamp": "2025-10-02",
    "total": 2,
    "data": [
        {
            "id": 1,
            "usuarioId": 1,
            "usuarioNombre": "Juan Carlos Pérez González",
            "periodoMes": 10,
            "periodoAnio": 2025,
            "monto": 50000.00,
            "estado": "pendiente",
            "fechaRegistro": "2025-10-01",
            "fechaVencimiento": "2025-10-31",
            "fechaPago": null,
            "metodoPago": null,
            "observaciones": null,
            "usuario": {
                "id": 1,
                "nombreCompleto": "Juan Carlos Pérez González",
                "email": "juan.perez@email.com",
                "estado": "ACTIVO",
                "tipo": "JUGADOR"
            }
        }
    ]
}
```

#### 2. **Crear Pago**
```http
POST /api/pagos
Content-Type: application/json

{
    "usuarioId": 1,
    "periodoMes": 11,
    "periodoAnio": 2025,
    "monto": 50000.00,
    "fechaVencimiento": "2025-11-30",
    "observaciones": "Pago mensualidad noviembre"
}
```

#### 3. **Obtener Pago por ID**
```http
GET /api/pagos/{id}
```

#### 4. **Obtener Pagos por Usuario**
```http
GET /api/pagos/usuario/{usuarioId}
```

#### 5. **Procesar Pago (Marcar como Pagado)**
```http
PUT /api/pagos/{id}/pagar
Content-Type: application/json

{
    "monto": 50000.00,
    "metodoPago": "TRANSFERENCIA_BANCARIA",
    "comprobante": "COMP-2025-001",
    "observaciones": "Pago procesado correctamente"
}
```

**Respuesta:**
```json
{
    "success": true,
    "message": "Pago procesado exitosamente",
    "timestamp": "2025-10-02",
    "pago": {
        "id": 1,
        "estado": "pagado",
        "fechaPago": "2025-10-02",
        "metodoPago": "TRANSFERENCIA_BANCARIA",
        "comprobante": "COMP-2025-001"
    }
}
```

#### 6. **Verificar Pagos en Atraso**
```http
PUT /api/pagos/verificar-atrasos
```

**Respuesta:**
```json
{
    "success": true,
    "message": "Verificación de pagos en atraso completada",
    "timestamp": "2025-10-02",
    "pagosActualizados": 3
}
```

#### 7. **🆕 Eliminar Pago**
```http
DELETE /api/pagos/{id}
```

**Respuesta Exitosa:**
```json
{
    "success": true,
    "message": "Pago eliminado exitosamente",
    "timestamp": "2025-10-02",
    "pagoId": 1
}
```

**Error - Pago ya Pagado:**
```json
{
    "success": false,
    "message": "No se puede eliminar un pago que ya está pagado",
    "timestamp": "2025-10-02",
    "pagoId": 1
}
```

---

## ⚛️ **Código React - Ejemplos de Implementación**

### 🔧 **Configuración Base**

#### **API Service (apiService.js)**
```javascript
const API_BASE_URL = 'http://localhost:8080';

class ApiService {
    static async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers,
            },
            ...options,
        };

        try {
            const response = await fetch(url, config);
            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || `HTTP error! status: ${response.status}`);
            }
            
            return data;
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    }

    static async get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    }

    static async post(endpoint, body) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(body),
        });
    }

    static async put(endpoint, body) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body),
        });
    }

    static async delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
}

export default ApiService;
```

### 👥 **Servicios para Usuarios**

#### **usuarioService.js**
```javascript
import ApiService from './apiService.js';

export const usuarioService = {
    // Obtener todos los usuarios
    async obtenerTodos() {
        return ApiService.get('/api/usuarios');
    },

    // Obtener usuario por ID
    async obtenerPorId(id) {
        return ApiService.get(`/api/usuarios/${id}`);
    },

    // Obtener usuario por cédula
    async obtenerPorCedula(cedula) {
        return ApiService.get(`/api/usuarios/cedula/${cedula}`);
    },

    // Obtener usuario por email
    async obtenerPorEmail(email) {
        return ApiService.get(`/api/usuarios/email/${email}`);
    },

    // Crear usuario
    async crear(usuario) {
        return ApiService.post('/api/usuarios', usuario);
    },

    // Actualizar usuario
    async actualizar(id, usuario) {
        return ApiService.put(`/api/usuarios/${id}`, usuario);
    },

    // Eliminar usuario
    async eliminar(id) {
        return ApiService.delete(`/api/usuarios/${id}`);
    }
};
```

### 💰 **Servicios para Pagos**

#### **pagoService.js**
```javascript
import ApiService from './apiService.js';

export const pagoService = {
    // Obtener todos los pagos
    async obtenerTodos() {
        return ApiService.get('/api/pagos');
    },

    // Obtener pago por ID
    async obtenerPorId(id) {
        return ApiService.get(`/api/pagos/${id}`);
    },

    // Obtener pagos por usuario
    async obtenerPorUsuario(usuarioId) {
        return ApiService.get(`/api/pagos/usuario/${usuarioId}`);
    },

    // Crear pago
    async crear(pago) {
        return ApiService.post('/api/pagos', pago);
    },

    // Procesar pago
    async procesar(id, datosPago) {
        return ApiService.put(`/api/pagos/${id}/pagar`, datosPago);
    },

    // Verificar pagos en atraso
    async verificarAtrasos() {
        return ApiService.put('/api/pagos/verificar-atrasos');
    },

    // 🆕 Eliminar pago
    async eliminar(id) {
        return ApiService.delete(`/api/pagos/${id}`);
    }
};
```

### 📱 **Componentes React de Ejemplo**

#### **ListaPagos.jsx**
```jsx
import React, { useState, useEffect } from 'react';
import { pagoService } from '../services/pagoService.js';

const ListaPagos = () => {
    const [pagos, setPagos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        obtenerPagos();
    }, []);

    const obtenerPagos = async () => {
        try {
            setLoading(true);
            const response = await pagoService.obtenerTodos();
            setPagos(response.data || []);
            setError(null);
        } catch (err) {
            setError(err.message);
            console.error('Error al obtener pagos:', err);
        } finally {
            setLoading(false);
        }
    };

    const eliminarPago = async (id) => {
        if (!window.confirm('¿Estás seguro de eliminar este pago?')) {
            return;
        }

        try {
            await pagoService.eliminar(id);
            // Actualizar la lista después de eliminar
            await obtenerPagos();
            alert('Pago eliminado exitosamente');
        } catch (error) {
            if (error.message.includes('ya está pagado')) {
                alert('No se puede eliminar un pago que ya está pagado');
            } else {
                alert('Error al eliminar el pago: ' + error.message);
            }
        }
    };

    const procesarPago = async (id) => {
        const monto = prompt('Ingrese el monto del pago:');
        const metodoPago = prompt('Ingrese el método de pago:');

        if (!monto || !metodoPago) return;

        try {
            await pagoService.procesar(id, {
                monto: parseFloat(monto),
                metodoPago,
                comprobante: `COMP-${Date.now()}`,
                observaciones: 'Pago procesado desde frontend'
            });
            await obtenerPagos();
            alert('Pago procesado exitosamente');
        } catch (error) {
            alert('Error al procesar el pago: ' + error.message);
        }
    };

    if (loading) return <div>Cargando pagos...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div>
            <h2>Lista de Pagos</h2>
            <button onClick={obtenerPagos}>Actualizar</button>
            
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                        <th>Período</th>
                        <th>Monto</th>
                        <th>Estado</th>
                        <th>Fecha Vencimiento</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {pagos.map(pago => (
                        <tr key={pago.id}>
                            <td>{pago.id}</td>
                            <td>{pago.usuarioNombre}</td>
                            <td>{pago.periodoMes}/{pago.periodoAnio}</td>
                            <td>${pago.monto.toLocaleString()}</td>
                            <td>
                                <span className={`estado-${pago.estado}`}>
                                    {pago.estado.toUpperCase()}
                                </span>
                            </td>
                            <td>{pago.fechaVencimiento}</td>
                            <td>
                                {pago.estado === 'pendiente' && (
                                    <button 
                                        onClick={() => procesarPago(pago.id)}
                                        className="btn-procesar"
                                    >
                                        Procesar
                                    </button>
                                )}
                                {pago.estado !== 'pagado' && (
                                    <button 
                                        onClick={() => eliminarPago(pago.id)}
                                        className="btn-eliminar"
                                    >
                                        Eliminar
                                    </button>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ListaPagos;
```

#### **FormularioUsuario.jsx**
```jsx
import React, { useState } from 'react';
import { usuarioService } from '../services/usuarioService.js';

const FormularioUsuario = ({ onUsuarioCreado }) => {
    const [usuario, setUsuario] = useState({
        nombres: '',
        apellidos: '',
        fechaNacimiento: '',
        cedula: '',
        email: '',
        celular: '',
        genero: 'MASCULINO',
        tipo: 'JUGADOR',
        estado: 'ACTIVO'
    });

    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setUsuario({
            ...usuario,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            const nuevoUsuario = await usuarioService.crear(usuario);
            alert('Usuario creado exitosamente');
            
            // Limpiar formulario
            setUsuario({
                nombres: '',
                apellidos: '',
                fechaNacimiento: '',
                cedula: '',
                email: '',
                celular: '',
                genero: 'MASCULINO',
                tipo: 'JUGADOR',
                estado: 'ACTIVO'
            });

            // Callback para actualizar lista padre
            if (onUsuarioCreado) {
                onUsuarioCreado(nuevoUsuario);
            }
        } catch (error) {
            alert('Error al crear usuario: ' + error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h3>Crear Nuevo Usuario</h3>
            
            <div>
                <label>Nombres:</label>
                <input
                    type="text"
                    name="nombres"
                    value={usuario.nombres}
                    onChange={handleChange}
                    required
                />
            </div>

            <div>
                <label>Apellidos:</label>
                <input
                    type="text"
                    name="apellidos"
                    value={usuario.apellidos}
                    onChange={handleChange}
                    required
                />
            </div>

            <div>
                <label>Fecha de Nacimiento:</label>
                <input
                    type="date"
                    name="fechaNacimiento"
                    value={usuario.fechaNacimiento}
                    onChange={handleChange}
                    required
                />
            </div>

            <div>
                <label>Cédula:</label>
                <input
                    type="text"
                    name="cedula"
                    value={usuario.cedula}
                    onChange={handleChange}
                    required
                />
            </div>

            <div>
                <label>Email:</label>
                <input
                    type="email"
                    name="email"
                    value={usuario.email}
                    onChange={handleChange}
                    required
                />
            </div>

            <div>
                <label>Celular:</label>
                <input
                    type="text"
                    name="celular"
                    value={usuario.celular}
                    onChange={handleChange}
                    required
                />
            </div>

            <div>
                <label>Género:</label>
                <select name="genero" value={usuario.genero} onChange={handleChange}>
                    <option value="MASCULINO">Masculino</option>
                    <option value="FEMENINO">Femenino</option>
                </select>
            </div>

            <div>
                <label>Tipo:</label>
                <select name="tipo" value={usuario.tipo} onChange={handleChange}>
                    <option value="JUGADOR">Jugador</option>
                    <option value="ENTRENADOR">Entrenador</option>
                    <option value="ADMINISTRADOR">Administrador</option>
                </select>
            </div>

            <button type="submit" disabled={loading}>
                {loading ? 'Creando...' : 'Crear Usuario'}
            </button>
        </form>
    );
};

export default FormularioUsuario;
```

### 🎨 **Estilos CSS Sugeridos**

#### **styles.css**
```css
/* Estados de pago */
.estado-pendiente {
    background-color: #fff3cd;
    color: #856404;
    padding: 2px 8px;
    border-radius: 4px;
}

.estado-pagado {
    background-color: #d1edff;
    color: #0c5460;
    padding: 2px 8px;
    border-radius: 4px;
}

.estado-atraso {
    background-color: #f8d7da;
    color: #721c24;
    padding: 2px 8px;
    border-radius: 4px;
}

.estado-rechazado {
    background-color: #f5c6cb;
    color: #721c24;
    padding: 2px 8px;
    border-radius: 4px;
}

/* Botones */
.btn-procesar {
    background-color: #28a745;
    color: white;
    border: none;
    padding: 5px 10px;
    border-radius: 4px;
    cursor: pointer;
    margin-right: 5px;
}

.btn-eliminar {
    background-color: #dc3545;
    color: white;
    border: none;
    padding: 5px 10px;
    border-radius: 4px;
    cursor: pointer;
}

.btn-procesar:hover {
    background-color: #218838;
}

.btn-eliminar:hover {
    background-color: #c82333;
}

/* Tabla */
table {
    border-collapse: collapse;
    width: 100%;
    margin-top: 20px;
}

th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
}

th {
    background-color: #f2f2f2;
    font-weight: bold;
}

tr:nth-child(even) {
    background-color: #f9f9f9;
}

/* Formularios */
form div {
    margin-bottom: 15px;
}

label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
}

input, select {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    box-sizing: border-box;
}

button[type="submit"] {
    background-color: #007bff;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
}

button[type="submit"]:hover {
    background-color: #0056b3;
}

button[type="submit"]:disabled {
    background-color: #6c757d;
    cursor: not-allowed;
}
```

### 🚀 **Hook Personalizado para Manejo de Estado**

#### **useApi.js**
```javascript
import { useState, useEffect } from 'react';

export const useApi = (apiFunction, dependencies = []) => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const execute = async (...args) => {
        try {
            setLoading(true);
            setError(null);
            const result = await apiFunction(...args);
            setData(result);
            return result;
        } catch (err) {
            setError(err.message);
            throw err;
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        execute();
    }, dependencies);

    return { data, loading, error, execute };
};
```

### 📱 **Ejemplo de App Principal**

#### **App.jsx**
```jsx
import React, { useState } from 'react';
import ListaPagos from './components/ListaPagos';
import FormularioUsuario from './components/FormularioUsuario';
import './styles.css';

function App() {
    const [activeTab, setActiveTab] = useState('pagos');

    return (
        <div className="App">
            <header>
                <h1>Sistema de Gestión Voley Backend</h1>
                <nav>
                    <button 
                        onClick={() => setActiveTab('pagos')}
                        className={activeTab === 'pagos' ? 'active' : ''}
                    >
                        Pagos
                    </button>
                    <button 
                        onClick={() => setActiveTab('usuarios')}
                        className={activeTab === 'usuarios' ? 'active' : ''}
                    >
                        Usuarios
                    </button>
                </nav>
            </header>

            <main>
                {activeTab === 'pagos' && <ListaPagos />}
                {activeTab === 'usuarios' && <FormularioUsuario />}
            </main>
        </div>
    );
}

export default App;
```

## 📚 **Notas Importantes para el Frontend**

### ⚠️ **Manejo de Errores**
- Siempre manejar errores de red y del servidor
- Validar respuestas antes de usar los datos
- Mostrar mensajes de error amigables al usuario

### 🔄 **Estados de Carga**
- Implementar indicadores de carga para mejor UX
- Deshabilitar botones durante operaciones asíncronas

### ✅ **Validaciones**
- Validar datos en el frontend antes de enviar
- Manejar casos especiales (pagos pagados no se pueden eliminar)

### 🎯 **Optimizaciones**
- Implementar debounce para búsquedas
- Cachear datos cuando sea apropiado
- Usar React.memo para componentes que no cambian frecuentemente

Este archivo proporciona todo lo necesario para que el equipo de frontend pueda integrar perfectamente con tu API de voley backend! 🏐