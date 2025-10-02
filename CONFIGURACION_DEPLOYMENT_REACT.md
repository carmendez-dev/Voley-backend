# 🛠️ Configuración y Deployment - Frontend React

## 📦 **Instalación de Dependencias**

### **Paquetes Recomendados**
```bash
# Crear proyecto React
npx create-react-app voley-frontend
cd voley-frontend

# Instalar dependencias adicionales
npm install axios          # Para peticiones HTTP
npm install react-router-dom # Para navegación
npm install react-hook-form # Para formularios
npm install react-query     # Para manejo de estado servidor
npm install @mui/material   # Para componentes UI (opcional)
npm install react-toastify  # Para notificaciones
```

### **package.json - Scripts Útiles**
```json
{
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build",
    "test": "react-scripts test",
    "eject": "react-scripts eject",
    "start:dev": "REACT_APP_API_URL=http://localhost:8080 react-scripts start",
    "start:prod": "REACT_APP_API_URL=https://tu-api-produccion.com react-scripts start"
  }
}
```

## 🌍 **Variables de Entorno**

### **.env.development**
```env
REACT_APP_API_URL=http://localhost:8080
REACT_APP_ENV=development
REACT_APP_DEBUG=true
```

### **.env.production**
```env
REACT_APP_API_URL=https://tu-dominio-produccion.com
REACT_APP_ENV=production
REACT_APP_DEBUG=false
```

### **Configuración Dinámica (config.js)**
```javascript
const config = {
    apiUrl: process.env.REACT_APP_API_URL || 'http://localhost:8080',
    environment: process.env.REACT_APP_ENV || 'development',
    debug: process.env.REACT_APP_DEBUG === 'true',
    
    // Configuraciones específicas por ambiente
    pagination: {
        defaultPageSize: 10,
        maxPageSize: 100
    },
    
    // Timeouts para peticiones
    timeouts: {
        default: 10000, // 10 segundos
        upload: 30000,  // 30 segundos
        download: 60000 // 60 segundos
    }
};

export default config;
```

## 🔧 **Versión Mejorada con Axios y React Query**

### **API Service con Axios (apiService.js)**
```javascript
import axios from 'axios';
import config from '../config';

// Configuración base de Axios
const api = axios.create({
    baseURL: config.apiUrl,
    timeout: config.timeouts.default,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptores para requests
api.interceptors.request.use(
    (config) => {
        // Agregar token de autenticación si existe
        const token = localStorage.getItem('authToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        
        if (config.debug) {
            console.log('Request:', config);
        }
        
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Interceptores para responses
api.interceptors.response.use(
    (response) => {
        if (config.debug) {
            console.log('Response:', response);
        }
        return response;
    },
    (error) => {
        if (config.debug) {
            console.error('Error Response:', error);
        }
        
        // Manejo global de errores
        if (error.response?.status === 401) {
            // Redirigir a login
            localStorage.removeItem('authToken');
            window.location.href = '/login';
        }
        
        return Promise.reject(error);
    }
);

class ApiService {
    static async get(endpoint, config = {}) {
        const response = await api.get(endpoint, config);
        return response.data;
    }

    static async post(endpoint, data, config = {}) {
        const response = await api.post(endpoint, data, config);
        return response.data;
    }

    static async put(endpoint, data, config = {}) {
        const response = await api.put(endpoint, data, config);
        return response.data;
    }

    static async delete(endpoint, config = {}) {
        const response = await api.delete(endpoint, config);
        return response.data;
    }

    static async upload(endpoint, formData, onUploadProgress) {
        const response = await api.post(endpoint, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            timeout: config.timeouts.upload,
            onUploadProgress,
        });
        return response.data;
    }
}

export default ApiService;
```

### **React Query Setup (queryClient.js)**
```javascript
import { QueryClient } from 'react-query';

export const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: 5 * 60 * 1000, // 5 minutos
            cacheTime: 10 * 60 * 1000, // 10 minutos
            retry: 3,
            refetchOnWindowFocus: false,
        },
        mutations: {
            retry: 1,
        },
    },
});
```

### **Custom Hooks con React Query**

#### **useUsuarios.js**
```javascript
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { usuarioService } from '../services/usuarioService';
import { toast } from 'react-toastify';

export const useUsuarios = () => {
    return useQuery('usuarios', usuarioService.obtenerTodos, {
        onError: (error) => {
            toast.error('Error al cargar usuarios: ' + error.message);
        },
    });
};

export const useUsuario = (id) => {
    return useQuery(
        ['usuario', id], 
        () => usuarioService.obtenerPorId(id),
        {
            enabled: !!id,
            onError: (error) => {
                toast.error('Error al cargar usuario: ' + error.message);
            },
        }
    );
};

export const useCrearUsuario = () => {
    const queryClient = useQueryClient();
    
    return useMutation(usuarioService.crear, {
        onSuccess: (data) => {
            queryClient.invalidateQueries('usuarios');
            toast.success('Usuario creado exitosamente');
        },
        onError: (error) => {
            toast.error('Error al crear usuario: ' + error.message);
        },
    });
};

export const useEliminarUsuario = () => {
    const queryClient = useQueryClient();
    
    return useMutation(usuarioService.eliminar, {
        onSuccess: () => {
            queryClient.invalidateQueries('usuarios');
            toast.success('Usuario eliminado exitosamente');
        },
        onError: (error) => {
            toast.error('Error al eliminar usuario: ' + error.message);
        },
    });
};
```

#### **usePagos.js**
```javascript
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { pagoService } from '../services/pagoService';
import { toast } from 'react-toastify';

export const usePagos = () => {
    return useQuery('pagos', pagoService.obtenerTodos, {
        select: (data) => data.data || [], // Extraer solo los datos del array
        onError: (error) => {
            toast.error('Error al cargar pagos: ' + error.message);
        },
    });
};

export const usePagosPorUsuario = (usuarioId) => {
    return useQuery(
        ['pagos', 'usuario', usuarioId],
        () => pagoService.obtenerPorUsuario(usuarioId),
        {
            enabled: !!usuarioId,
            onError: (error) => {
                toast.error('Error al cargar pagos del usuario: ' + error.message);
            },
        }
    );
};

export const useProcesarPago = () => {
    const queryClient = useQueryClient();
    
    return useMutation(
        ({ id, datosPago }) => pagoService.procesar(id, datosPago),
        {
            onSuccess: () => {
                queryClient.invalidateQueries('pagos');
                toast.success('Pago procesado exitosamente');
            },
            onError: (error) => {
                toast.error('Error al procesar pago: ' + error.message);
            },
        }
    );
};

export const useEliminarPago = () => {
    const queryClient = useQueryClient();
    
    return useMutation(pagoService.eliminar, {
        onSuccess: () => {
            queryClient.invalidateQueries('pagos');
            toast.success('Pago eliminado exitosamente');
        },
        onError: (error) => {
            if (error.message.includes('ya está pagado')) {
                toast.error('No se puede eliminar un pago que ya está pagado');
            } else {
                toast.error('Error al eliminar pago: ' + error.message);
            }
        },
    });
};
```

### **Componente Mejorado con React Query**

#### **ListaPagosRQ.jsx**
```jsx
import React, { useState } from 'react';
import { usePagos, useProcesarPago, useEliminarPago } from '../hooks/usePagos';
import { toast } from 'react-toastify';

const ListaPagosRQ = () => {
    const { data: pagos, isLoading, error, refetch } = usePagos();
    const procesarPagoMutation = useProcesarPago();
    const eliminarPagoMutation = useEliminarPago();
    
    const [pagoSeleccionado, setPagoSeleccionado] = useState(null);

    const handleProcesarPago = async (id) => {
        const monto = prompt('Ingrese el monto del pago:');
        const metodoPago = prompt('Ingrese el método de pago:');

        if (!monto || !metodoPago) return;

        try {
            await procesarPagoMutation.mutateAsync({
                id,
                datosPago: {
                    monto: parseFloat(monto),
                    metodoPago,
                    comprobante: `COMP-${Date.now()}`,
                    observaciones: 'Pago procesado desde frontend'
                }
            });
        } catch (error) {
            // Error ya manejado en el hook
        }
    };

    const handleEliminarPago = async (id) => {
        if (!window.confirm('¿Estás seguro de eliminar este pago?')) {
            return;
        }

        try {
            await eliminarPagoMutation.mutateAsync(id);
        } catch (error) {
            // Error ya manejado en el hook
        }
    };

    if (isLoading) return <div className="loading-spinner">Cargando pagos...</div>;
    if (error) return <div className="error-message">Error: {error.message}</div>;

    return (
        <div className="pagos-container">
            <div className="pagos-header">
                <h2>Lista de Pagos</h2>
                <button onClick={() => refetch()} className="btn-refresh">
                    🔄 Actualizar
                </button>
            </div>
            
            <div className="pagos-stats">
                <div className="stat-card">
                    <span className="stat-number">{pagos?.length || 0}</span>
                    <span className="stat-label">Total Pagos</span>
                </div>
                <div className="stat-card">
                    <span className="stat-number">
                        {pagos?.filter(p => p.estado === 'pendiente').length || 0}
                    </span>
                    <span className="stat-label">Pendientes</span>
                </div>
                <div className="stat-card">
                    <span className="stat-number">
                        {pagos?.filter(p => p.estado === 'atraso').length || 0}
                    </span>
                    <span className="stat-label">En Atraso</span>
                </div>
                <div className="stat-card">
                    <span className="stat-number">
                        {pagos?.filter(p => p.estado === 'pagado').length || 0}
                    </span>
                    <span className="stat-label">Pagados</span>
                </div>
            </div>

            <div className="table-container">
                <table className="pagos-table">
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
                        {pagos?.map(pago => (
                            <tr key={pago.id} className={`row-${pago.estado}`}>
                                <td>{pago.id}</td>
                                <td>
                                    <div className="usuario-info">
                                        <span className="nombre">{pago.usuarioNombre}</span>
                                        {pago.usuario && (
                                            <span className="email">{pago.usuario.email}</span>
                                        )}
                                    </div>
                                </td>
                                <td>
                                    <span className="periodo">
                                        {pago.periodoMes.toString().padStart(2, '0')}/{pago.periodoAnio}
                                    </span>
                                </td>
                                <td>
                                    <span className="monto">
                                        ${pago.monto.toLocaleString('es-CO')}
                                    </span>
                                </td>
                                <td>
                                    <span className={`estado-badge estado-${pago.estado}`}>
                                        {pago.estado.toUpperCase()}
                                    </span>
                                </td>
                                <td>
                                    <span className="fecha">
                                        {new Date(pago.fechaVencimiento).toLocaleDateString('es-CO')}
                                    </span>
                                </td>
                                <td>
                                    <div className="acciones">
                                        {pago.estado === 'pendiente' && (
                                            <button 
                                                onClick={() => handleProcesarPago(pago.id)}
                                                className="btn btn-success"
                                                disabled={procesarPagoMutation.isLoading}
                                            >
                                                {procesarPagoMutation.isLoading ? '...' : '💳 Procesar'}
                                            </button>
                                        )}
                                        {pago.estado !== 'pagado' && (
                                            <button 
                                                onClick={() => handleEliminarPago(pago.id)}
                                                className="btn btn-danger"
                                                disabled={eliminarPagoMutation.isLoading}
                                            >
                                                {eliminarPagoMutation.isLoading ? '...' : '🗑️ Eliminar'}
                                            </button>
                                        )}
                                        <button 
                                            onClick={() => setPagoSeleccionado(pago)}
                                            className="btn btn-info"
                                        >
                                            👁️ Ver
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {pagoSeleccionado && (
                <ModalDetallePago 
                    pago={pagoSeleccionado} 
                    onClose={() => setPagoSeleccionado(null)} 
                />
            )}
        </div>
    );
};

export default ListaPagosRQ;
```

### **App Principal con React Query**

#### **App.jsx**
```jsx
import React from 'react';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import Navigation from './components/Navigation';
import ListaPagosRQ from './components/ListaPagosRQ';
import ListaUsuarios from './components/ListaUsuarios';
import Dashboard from './components/Dashboard';
import config from './config';

// Crear cliente de React Query
const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: 5 * 60 * 1000,
            cacheTime: 10 * 60 * 1000,
            retry: 3,
            refetchOnWindowFocus: false,
        },
    },
});

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <Router>
                <div className="App">
                    <Navigation />
                    
                    <main className="main-content">
                        <Routes>
                            <Route path="/" element={<Dashboard />} />
                            <Route path="/pagos" element={<ListaPagosRQ />} />
                            <Route path="/usuarios" element={<ListaUsuarios />} />
                        </Routes>
                    </main>

                    <ToastContainer
                        position="top-right"
                        autoClose={5000}
                        hideProgressBar={false}
                        newestOnTop={false}
                        closeOnClick
                        rtl={false}
                        pauseOnFocusLoss
                        draggable
                        pauseOnHover
                    />
                </div>
            </Router>

            {config.debug && <ReactQueryDevtools initialIsOpen={false} />}
        </QueryClientProvider>
    );
}

export default App;
```

## 🎨 **Estilos CSS Avanzados**

### **styles.css (Ampliado)**
```css
/* Variables CSS */
:root {
    --primary-color: #007bff;
    --success-color: #28a745;
    --danger-color: #dc3545;
    --warning-color: #ffc107;
    --info-color: #17a2b8;
    --secondary-color: #6c757d;
    --light-color: #f8f9fa;
    --dark-color: #343a40;
    --border-radius: 8px;
    --box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    --transition: all 0.3s ease;
}

/* Layout */
.App {
    min-height: 100vh;
    background-color: var(--light-color);
}

.main-content {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;
}

/* Componentes de carga */
.loading-spinner {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 200px;
    font-size: 18px;
    color: var(--secondary-color);
}

.loading-spinner::before {
    content: '';
    width: 20px;
    height: 20px;
    border: 2px solid var(--primary-color);
    border-top: 2px solid transparent;
    border-radius: 50%;
    margin-right: 10px;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}

/* Mensajes de error */
.error-message {
    background-color: #f8d7da;
    color: #721c24;
    padding: 15px;
    border-radius: var(--border-radius);
    border: 1px solid #f5c6cb;
    margin: 20px 0;
}

/* Contenedor de pagos */
.pagos-container {
    background: white;
    border-radius: var(--border-radius);
    box-shadow: var(--box-shadow);
    overflow: hidden;
}

.pagos-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    background: linear-gradient(135deg, var(--primary-color), #0056b3);
    color: white;
}

.btn-refresh {
    background: rgba(255,255,255,0.2);
    color: white;
    border: 1px solid rgba(255,255,255,0.3);
    padding: 8px 16px;
    border-radius: var(--border-radius);
    cursor: pointer;
    transition: var(--transition);
}

.btn-refresh:hover {
    background: rgba(255,255,255,0.3);
}

/* Estadísticas */
.pagos-stats {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 20px;
    padding: 20px;
    background: #f8f9fa;
    border-bottom: 1px solid #dee2e6;
}

.stat-card {
    background: white;
    padding: 20px;
    border-radius: var(--border-radius);
    text-align: center;
    box-shadow: 0 2px 5px rgba(0,0,0,0.05);
}

.stat-number {
    display: block;
    font-size: 2em;
    font-weight: bold;
    color: var(--primary-color);
}

.stat-label {
    display: block;
    font-size: 0.9em;
    color: var(--secondary-color);
    margin-top: 5px;
}

/* Tabla */
.table-container {
    overflow-x: auto;
}

.pagos-table {
    width: 100%;
    border-collapse: collapse;
}

.pagos-table th {
    background: #f8f9fa;
    padding: 15px;
    text-align: left;
    font-weight: 600;
    color: var(--dark-color);
    border-bottom: 2px solid var(--primary-color);
}

.pagos-table td {
    padding: 15px;
    border-bottom: 1px solid #dee2e6;
    vertical-align: middle;
}

.pagos-table tr:hover {
    background: #f8f9fa;
}

/* Estados de fila */
.row-atraso {
    background: rgba(220, 53, 69, 0.05);
}

.row-pagado {
    background: rgba(40, 167, 69, 0.05);
}

/* Información del usuario */
.usuario-info {
    display: flex;
    flex-direction: column;
}

.usuario-info .nombre {
    font-weight: 600;
    color: var(--dark-color);
}

.usuario-info .email {
    font-size: 0.85em;
    color: var(--secondary-color);
}

/* Período y monto */
.periodo {
    font-family: 'Courier New', monospace;
    background: var(--light-color);
    padding: 4px 8px;
    border-radius: 4px;
    font-weight: 600;
}

.monto {
    font-weight: 700;
    color: var(--success-color);
    font-size: 1.1em;
}

/* Estados de pago mejorados */
.estado-badge {
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 0.8em;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

.estado-pendiente {
    background: linear-gradient(135deg, #fff3cd, #ffeaa7);
    color: #856404;
    border: 1px solid #ffeaa7;
}

.estado-pagado {
    background: linear-gradient(135deg, #d1edff, #74b9ff);
    color: #0c5460;
    border: 1px solid #74b9ff;
}

.estado-atraso {
    background: linear-gradient(135deg, #f8d7da, #ff7675);
    color: #721c24;
    border: 1px solid #ff7675;
}

.estado-rechazado {
    background: linear-gradient(135deg, #f5c6cb, #e17055);
    color: #721c24;
    border: 1px solid #e17055;
}

/* Botones de acción */
.acciones {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
}

.btn {
    padding: 6px 12px;
    border: none;
    border-radius: var(--border-radius);
    font-size: 0.85em;
    font-weight: 500;
    cursor: pointer;
    transition: var(--transition);
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 4px;
}

.btn:disabled {
    opacity: 0.6;
    cursor: not-allowed;
}

.btn-success {
    background: var(--success-color);
    color: white;
}

.btn-success:hover:not(:disabled) {
    background: #218838;
    transform: translateY(-1px);
}

.btn-danger {
    background: var(--danger-color);
    color: white;
}

.btn-danger:hover:not(:disabled) {
    background: #c82333;
    transform: translateY(-1px);
}

.btn-info {
    background: var(--info-color);
    color: white;
}

.btn-info:hover:not(:disabled) {
    background: #138496;
    transform: translateY(-1px);
}

/* Responsive */
@media (max-width: 768px) {
    .pagos-header {
        flex-direction: column;
        gap: 15px;
        text-align: center;
    }
    
    .pagos-stats {
        grid-template-columns: repeat(2, 1fr);
    }
    
    .acciones {
        flex-direction: column;
    }
    
    .usuario-info .email {
        display: none;
    }
}

@media (max-width: 480px) {
    .main-content {
        padding: 10px;
    }
    
    .pagos-stats {
        grid-template-columns: 1fr;
    }
    
    .pagos-table th,
    .pagos-table td {
        padding: 8px;
        font-size: 0.9em;
    }
}

/* Animaciones */
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.pagos-container {
    animation: fadeIn 0.5s ease-out;
}

/* Toast personalizados */
.Toastify__toast--success {
    background: linear-gradient(135deg, var(--success-color), #20c997);
}

.Toastify__toast--error {
    background: linear-gradient(135deg, var(--danger-color), #e74c3c);
}

.Toastify__toast--warning {
    background: linear-gradient(135deg, var(--warning-color), #f39c12);
}
```

## 🚀 **Scripts de Deployment**

### **deploy.sh**
```bash
#!/bin/bash

# Script de deployment para producción
echo "🚀 Iniciando deployment..."

# Instalar dependencias
echo "📦 Instalando dependencias..."
npm ci

# Ejecutar tests
echo "🧪 Ejecutando tests..."
npm test -- --coverage --watchAll=false

# Build para producción
echo "🏗️ Generando build de producción..."
npm run build

# Subir a servidor (ejemplo con rsync)
echo "📤 Subiendo archivos al servidor..."
rsync -avz --delete ./build/ user@servidor:/ruta/frontend/

echo "✅ Deployment completado exitosamente!"
```

### **docker-compose.yml**
```yaml
version: '3.8'

services:
  frontend:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "3000:80"
    environment:
      - REACT_APP_API_URL=http://backend:8080
    depends_on:
      - backend
    networks:
      - voley-network

  backend:
    image: voley-backend:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=production
    networks:
      - voley-network

networks:
  voley-network:
    driver: bridge
```

### **Dockerfile**
```dockerfile
# Build stage
FROM node:18-alpine as build

WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production

COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine

COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

Este archivo completo proporciona todo lo necesario para que el equipo de frontend pueda trabajar eficientemente con tu API! 🎯