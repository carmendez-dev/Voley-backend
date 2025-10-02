# 🧪 Testing Guide - Frontend React

## 📋 **Estrategia de Testing**

### **Tipos de Tests Implementados**
1. **Unit Tests**: Componentes individuales y funciones
2. **Integration Tests**: Interacción entre componentes
3. **API Tests**: Servicios y hooks personalizados
4. **E2E Tests**: Flujos completos de usuario

## 🔧 **Configuración de Testing**

### **Jest Configuration (jest.config.js)**
```javascript
module.exports = {
    testEnvironment: 'jsdom',
    setupFilesAfterEnv: ['<rootDir>/src/setupTests.js'],
    moduleNameMapping: {
        '^@/(.*)$': '<rootDir>/src/$1',
        '\\.(css|less|scss|sass)$': 'identity-obj-proxy',
    },
    collectCoverageFrom: [
        'src/**/*.{js,jsx}',
        '!src/index.js',
        '!src/reportWebVitals.js',
        '!src/**/*.test.{js,jsx}',
        '!src/**/__tests__/**',
    ],
    coverageThreshold: {
        global: {
            branches: 80,
            functions: 80,
            lines: 80,
            statements: 80,
        },
    },
    testMatch: [
        '<rootDir>/src/**/__tests__/**/*.{js,jsx}',
        '<rootDir>/src/**/*.{test,spec}.{js,jsx}',
    ],
};
```

### **Setup Tests (setupTests.js)**
```javascript
import '@testing-library/jest-dom';
import { server } from './mocks/server';

// Configurar MSW
beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

// Mock de localStorage
const localStorageMock = {
    getItem: jest.fn(),
    setItem: jest.fn(),
    removeItem: jest.fn(),
    clear: jest.fn(),
};
global.localStorage = localStorageMock;

// Mock de console para tests más limpios
global.console = {
    ...console,
    warn: jest.fn(),
    error: jest.fn(),
};

// Mock de window.confirm y window.alert
global.confirm = jest.fn(() => true);
global.alert = jest.fn();
```

## 🎭 **Mocks con MSW (Mock Service Worker)**

### **Instalación**
```bash
npm install --save-dev msw
```

### **Handlers (mocks/handlers.js)**
```javascript
import { rest } from 'msw';

const API_URL = 'http://localhost:8080';

export const handlers = [
    // Mock para obtener todos los pagos
    rest.get(`${API_URL}/api/pagos`, (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json({
                success: true,
                message: 'Pagos obtenidos exitosamente',
                timestamp: '2025-10-02',
                total: 2,
                data: [
                    {
                        id: 1,
                        usuarioId: 1,
                        usuarioNombre: 'Juan Carlos Pérez González',
                        periodoMes: 10,
                        periodoAnio: 2025,
                        monto: 50000.00,
                        estado: 'pendiente',
                        fechaRegistro: '2025-10-01',
                        fechaVencimiento: '2025-10-31',
                        fechaPago: null,
                        metodoPago: null,
                        observaciones: null,
                        usuario: {
                            id: 1,
                            nombreCompleto: 'Juan Carlos Pérez González',
                            email: 'juan.perez@email.com',
                            estado: 'ACTIVO',
                            tipo: 'JUGADOR'
                        }
                    },
                    {
                        id: 2,
                        usuarioId: 1,
                        usuarioNombre: 'Juan Carlos Pérez González',
                        periodoMes: 11,
                        periodoAnio: 2025,
                        monto: 50000.00,
                        estado: 'pagado',
                        fechaRegistro: '2025-10-01',
                        fechaVencimiento: '2025-11-30',
                        fechaPago: '2025-10-15',
                        metodoPago: 'TRANSFERENCIA_BANCARIA',
                        observaciones: 'Pago procesado'
                    }
                ]
            })
        );
    }),

    // Mock para eliminar pago
    rest.delete(`${API_URL}/api/pagos/:id`, (req, res, ctx) => {
        const { id } = req.params;
        
        // Simular error si el pago está pagado
        if (id === '2') {
            return res(
                ctx.status(409),
                ctx.json({
                    success: false,
                    message: 'No se puede eliminar un pago que ya está pagado',
                    timestamp: '2025-10-02',
                    pagoId: parseInt(id)
                })
            );
        }

        return res(
            ctx.status(200),
            ctx.json({
                success: true,
                message: 'Pago eliminado exitosamente',
                timestamp: '2025-10-02',
                pagoId: parseInt(id)
            })
        );
    }),

    // Mock para procesar pago
    rest.put(`${API_URL}/api/pagos/:id/pagar`, (req, res, ctx) => {
        const { id } = req.params;
        
        return res(
            ctx.status(200),
            ctx.json({
                success: true,
                message: 'Pago procesado exitosamente',
                timestamp: '2025-10-02',
                pago: {
                    id: parseInt(id),
                    estado: 'pagado',
                    fechaPago: '2025-10-02',
                    metodoPago: 'TRANSFERENCIA_BANCARIA'
                }
            })
        );
    }),

    // Mock para obtener usuarios
    rest.get(`${API_URL}/api/usuarios`, (req, res, ctx) => {
        return res(
            ctx.status(200),
            ctx.json([
                {
                    id: 1,
                    nombres: 'Juan Carlos',
                    apellidos: 'Pérez González',
                    email: 'juan.perez@email.com',
                    cedula: '12345678901',
                    celular: '3001234567',
                    estado: 'ACTIVO',
                    tipo: 'JUGADOR'
                }
            ])
        );
    }),

    // Mock para crear usuario
    rest.post(`${API_URL}/api/usuarios`, (req, res, ctx) => {
        return res(
            ctx.status(201),
            ctx.json({
                id: 2,
                nombres: 'María',
                apellidos: 'García López',
                email: 'maria.garcia@email.com',
                cedula: '98765432109',
                celular: '3009876543',
                estado: 'ACTIVO',
                tipo: 'JUGADOR'
            })
        );
    }),
];
```

### **Server Setup (mocks/server.js)**
```javascript
import { setupServer } from 'msw/node';
import { handlers } from './handlers';

export const server = setupServer(...handlers);
```

## 🧪 **Unit Tests**

### **Test de Servicio (services/__tests__/pagoService.test.js)**
```javascript
import { pagoService } from '../pagoService';

describe('PagoService', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });

    describe('obtenerTodos', () => {
        it('debe obtener todos los pagos exitosamente', async () => {
            const result = await pagoService.obtenerTodos();
            
            expect(result.success).toBe(true);
            expect(result.data).toHaveLength(2);
            expect(result.data[0]).toHaveProperty('id');
            expect(result.data[0]).toHaveProperty('usuarioNombre');
            expect(result.data[0]).toHaveProperty('estado');
        });
    });

    describe('eliminar', () => {
        it('debe eliminar un pago pendiente exitosamente', async () => {
            const result = await pagoService.eliminar(1);
            
            expect(result.success).toBe(true);
            expect(result.message).toBe('Pago eliminado exitosamente');
            expect(result.pagoId).toBe(1);
        });

        it('debe fallar al eliminar un pago pagado', async () => {
            await expect(pagoService.eliminar(2)).rejects.toThrow(
                'No se puede eliminar un pago que ya está pagado'
            );
        });
    });

    describe('procesar', () => {
        it('debe procesar un pago exitosamente', async () => {
            const datosPago = {
                monto: 50000,
                metodoPago: 'TRANSFERENCIA_BANCARIA'
            };

            const result = await pagoService.procesar(1, datosPago);
            
            expect(result.success).toBe(true);
            expect(result.pago.estado).toBe('pagado');
            expect(result.pago.fechaPago).toBe('2025-10-02');
        });
    });
});
```

### **Test de Hook (hooks/__tests__/usePagos.test.js)**
```javascript
import { renderHook, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from 'react-query';
import { usePagos, useEliminarPago } from '../usePagos';

const createWrapper = () => {
    const queryClient = new QueryClient({
        defaultOptions: {
            queries: { retry: false },
            mutations: { retry: false },
        },
    });

    return ({ children }) => (
        <QueryClientProvider client={queryClient}>
            {children}
        </QueryClientProvider>
    );
};

describe('usePagos', () => {
    it('debe cargar pagos exitosamente', async () => {
        const { result } = renderHook(() => usePagos(), {
            wrapper: createWrapper(),
        });

        expect(result.current.isLoading).toBe(true);

        await waitFor(() => {
            expect(result.current.isLoading).toBe(false);
        });

        expect(result.current.data).toHaveLength(2);
        expect(result.current.error).toBeNull();
    });
});

describe('useEliminarPago', () => {
    it('debe eliminar un pago exitosamente', async () => {
        const { result } = renderHook(() => useEliminarPago(), {
            wrapper: createWrapper(),
        });

        await waitFor(() => {
            result.current.mutate(1);
        });

        await waitFor(() => {
            expect(result.current.isSuccess).toBe(true);
        });
    });

    it('debe manejar error al eliminar pago pagado', async () => {
        const { result } = renderHook(() => useEliminarPago(), {
            wrapper: createWrapper(),
        });

        await waitFor(() => {
            result.current.mutate(2);
        });

        await waitFor(() => {
            expect(result.current.isError).toBe(true);
            expect(result.current.error.message).toContain('ya está pagado');
        });
    });
});
```

## 🎨 **Component Tests**

### **Test de Componente (components/__tests__/ListaPagos.test.jsx)**
```jsx
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from 'react-query';
import { toast } from 'react-toastify';
import ListaPagosRQ from '../ListaPagosRQ';

// Mock de react-toastify
jest.mock('react-toastify', () => ({
    toast: {
        success: jest.fn(),
        error: jest.fn()
    }
}));

const createWrapper = () => {
    const queryClient = new QueryClient({
        defaultOptions: {
            queries: { retry: false },
            mutations: { retry: false },
        },
    });

    return ({ children }) => (
        <QueryClientProvider client={queryClient}>
            {children}
        </QueryClientProvider>
    );
};

describe('ListaPagosRQ', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it('debe renderizar la lista de pagos', async () => {
        render(<ListaPagosRQ />, { wrapper: createWrapper() });

        // Verificar que muestra loading inicialmente
        expect(screen.getByText('Cargando pagos...')).toBeInTheDocument();

        // Esperar a que carguen los datos
        await waitFor(() => {
            expect(screen.getByText('Lista de Pagos')).toBeInTheDocument();
        });

        // Verificar que muestra los pagos
        expect(screen.getByText('Juan Carlos Pérez González')).toBeInTheDocument();
        expect(screen.getByText('10/2025')).toBeInTheDocument();
        expect(screen.getByText('$50,000')).toBeInTheDocument();
    });

    it('debe mostrar estadísticas correctas', async () => {
        render(<ListaPagosRQ />, { wrapper: createWrapper() });

        await waitFor(() => {
            expect(screen.getByText('2')).toBeInTheDocument(); // Total pagos
            expect(screen.getByText('1')).toBeInTheDocument(); // Pendientes
            expect(screen.getByText('1')).toBeInTheDocument(); // Pagados
        });
    });

    it('debe permitir eliminar un pago pendiente', async () => {
        global.confirm = jest.fn(() => true);
        
        render(<ListaPagosRQ />, { wrapper: createWrapper() });

        await waitFor(() => {
            expect(screen.getByText('Lista de Pagos')).toBeInTheDocument();
        });

        // Buscar el botón eliminar del pago pendiente
        const eliminarButtons = screen.getAllByText('🗑️ Eliminar');
        expect(eliminarButtons).toHaveLength(1);

        // Hacer click en eliminar
        fireEvent.click(eliminarButtons[0]);

        await waitFor(() => {
            expect(toast.success).toHaveBeenCalledWith('Pago eliminado exitosamente');
        });
    });

    it('debe manejar error al eliminar pago pagado', async () => {
        // Modificar el mock para que el pago 1 también sea pagado
        const { server } = require('../../mocks/server');
        const { rest } = require('msw');
        
        server.use(
            rest.delete('http://localhost:8080/api/pagos/1', (req, res, ctx) => {
                return res(
                    ctx.status(409),
                    ctx.json({
                        success: false,
                        message: 'No se puede eliminar un pago que ya está pagado'
                    })
                );
            })
        );

        global.confirm = jest.fn(() => true);
        
        render(<ListaPagosRQ />, { wrapper: createWrapper() });

        await waitFor(() => {
            expect(screen.getByText('Lista de Pagos')).toBeInTheDocument();
        });

        const eliminarButtons = screen.getAllByText('🗑️ Eliminar');
        fireEvent.click(eliminarButtons[0]);

        await waitFor(() => {
            expect(toast.error).toHaveBeenCalledWith(
                'No se puede eliminar un pago que ya está pagado'
            );
        });
    });

    it('debe procesar un pago correctamente', async () => {
        global.prompt = jest.fn()
            .mockReturnValueOnce('50000') // monto
            .mockReturnValueOnce('TRANSFERENCIA_BANCARIA'); // método

        render(<ListaPagosRQ />, { wrapper: createWrapper() });

        await waitFor(() => {
            expect(screen.getByText('Lista de Pagos')).toBeInTheDocument();
        });

        // Buscar el botón procesar del pago pendiente
        const procesarButton = screen.getByText('💳 Procesar');
        fireEvent.click(procesarButton);

        await waitFor(() => {
            expect(toast.success).toHaveBeenCalledWith('Pago procesado exitosamente');
        });
    });

    it('debe cancelar procesamiento si no se ingresa información', async () => {
        global.prompt = jest.fn().mockReturnValue(null);

        render(<ListaPagosRQ />, { wrapper: createWrapper() });

        await waitFor(() => {
            expect(screen.getByText('Lista de Pagos')).toBeInTheDocument();
        });

        const procesarButton = screen.getByText('💳 Procesar');
        fireEvent.click(procesarButton);

        // No debería llamar al toast si se cancela
        expect(toast.success).not.toHaveBeenCalled();
        expect(toast.error).not.toHaveBeenCalled();
    });
});
```

### **Test de Formulario (components/__tests__/FormularioUsuario.test.jsx)**
```jsx
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import FormularioUsuario from '../FormularioUsuario';

describe('FormularioUsuario', () => {
    const mockOnUsuarioCreado = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();
    });

    it('debe renderizar todos los campos del formulario', () => {
        render(<FormularioUsuario onUsuarioCreado={mockOnUsuarioCreado} />);

        expect(screen.getByLabelText('Nombres:')).toBeInTheDocument();
        expect(screen.getByLabelText('Apellidos:')).toBeInTheDocument();
        expect(screen.getByLabelText('Fecha de Nacimiento:')).toBeInTheDocument();
        expect(screen.getByLabelText('Cédula:')).toBeInTheDocument();
        expect(screen.getByLabelText('Email:')).toBeInTheDocument();
        expect(screen.getByLabelText('Celular:')).toBeInTheDocument();
        expect(screen.getByLabelText('Género:')).toBeInTheDocument();
        expect(screen.getByLabelText('Tipo:')).toBeInTheDocument();
        expect(screen.getByRole('button', { name: 'Crear Usuario' })).toBeInTheDocument();
    });

    it('debe validar campos requeridos', async () => {
        const user = userEvent.setup();
        render(<FormularioUsuario onUsuarioCreado={mockOnUsuarioCreado} />);

        const submitButton = screen.getByRole('button', { name: 'Crear Usuario' });
        await user.click(submitButton);

        // Los campos requeridos deberían mostrar validación del navegador
        const nombresInput = screen.getByLabelText('Nombres:');
        expect(nombresInput).toBeInvalid();
    });

    it('debe crear usuario exitosamente con datos válidos', async () => {
        const user = userEvent.setup();
        render(<FormularioUsuario onUsuarioCreado={mockOnUsuarioCreado} />);

        // Llenar el formulario
        await user.type(screen.getByLabelText('Nombres:'), 'María');
        await user.type(screen.getByLabelText('Apellidos:'), 'García López');
        await user.type(screen.getByLabelText('Fecha de Nacimiento:'), '1995-03-15');
        await user.type(screen.getByLabelText('Cédula:'), '98765432109');
        await user.type(screen.getByLabelText('Email:'), 'maria.garcia@email.com');
        await user.type(screen.getByLabelText('Celular:'), '3009876543');

        // Enviar formulario
        const submitButton = screen.getByRole('button', { name: 'Crear Usuario' });
        await user.click(submitButton);

        await waitFor(() => {
            expect(mockOnUsuarioCreado).toHaveBeenCalledWith({
                id: 2,
                nombres: 'María',
                apellidos: 'García López',
                email: 'maria.garcia@email.com',
                cedula: '98765432109',
                celular: '3009876543',
                estado: 'ACTIVO',
                tipo: 'JUGADOR'
            });
        });

        // Verificar que el formulario se limpió
        expect(screen.getByLabelText('Nombres:')).toHaveValue('');
        expect(screen.getByLabelText('Apellidos:')).toHaveValue('');
    });

    it('debe cambiar el género correctamente', async () => {
        const user = userEvent.setup();
        render(<FormularioUsuario onUsuarioCreado={mockOnUsuarioCreado} />);

        const generoSelect = screen.getByLabelText('Género:');
        await user.selectOptions(generoSelect, 'FEMENINO');

        expect(generoSelect).toHaveValue('FEMENINO');
    });

    it('debe cambiar el tipo correctamente', async () => {
        const user = userEvent.setup();
        render(<FormularioUsuario onUsuarioCreado={mockOnUsuarioCreado} />);

        const tipoSelect = screen.getByLabelText('Tipo:');
        await user.selectOptions(tipoSelect, 'ENTRENADOR');

        expect(tipoSelect).toHaveValue('ENTRENADOR');
    });
});
```

## 🎯 **Integration Tests**

### **Test de Integración (integration/__tests__/PagosFlow.test.jsx)**
```jsx
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { QueryClient, QueryClientProvider } from 'react-query';
import { BrowserRouter } from 'react-router-dom';
import App from '../../App';

const createWrapper = () => {
    const queryClient = new QueryClient({
        defaultOptions: {
            queries: { retry: false },
            mutations: { retry: false },
        },
    });

    return ({ children }) => (
        <QueryClientProvider client={queryClient}>
            <BrowserRouter>
                {children}
            </BrowserRouter>
        </QueryClientProvider>
    );
};

describe('Flujo completo de Pagos', () => {
    it('debe permitir ver, procesar y eliminar pagos', async () => {
        global.confirm = jest.fn(() => true);
        global.prompt = jest.fn()
            .mockReturnValueOnce('50000')
            .mockReturnValueOnce('TRANSFERENCIA_BANCARIA');

        render(<App />, { wrapper: createWrapper() });

        // Navegar a la sección de pagos
        const pagosLink = screen.getByText('Pagos');
        fireEvent.click(pagosLink);

        // Esperar a que carguen los pagos
        await waitFor(() => {
            expect(screen.getByText('Lista de Pagos')).toBeInTheDocument();
        });

        // Verificar que se muestran los pagos
        expect(screen.getByText('Juan Carlos Pérez González')).toBeInTheDocument();

        // Procesar un pago
        const procesarButton = screen.getByText('💳 Procesar');
        fireEvent.click(procesarButton);

        await waitFor(() => {
            expect(screen.getByText('Pago procesado exitosamente')).toBeInTheDocument();
        });

        // Eliminar un pago
        const eliminarButtons = screen.getAllByText('🗑️ Eliminar');
        if (eliminarButtons.length > 0) {
            fireEvent.click(eliminarButtons[0]);

            await waitFor(() => {
                expect(screen.getByText('Pago eliminado exitosamente')).toBeInTheDocument();
            });
        }
    });
});
```

## 🏃‍♂️ **Scripts de Testing**

### **package.json - Scripts**
```json
{
  "scripts": {
    "test": "react-scripts test",
    "test:coverage": "react-scripts test --coverage --watchAll=false",
    "test:ci": "CI=true react-scripts test --coverage --watchAll=false",
    "test:debug": "react-scripts --inspect-brk test --runInBand --no-cache",
    "test:unit": "react-scripts test --testPathPattern=__tests__",
    "test:integration": "react-scripts test --testPathPattern=integration",
    "test:e2e": "cypress run"
  }
}
```

### **Test Runner Script (test-runner.sh)**
```bash
#!/bin/bash

echo "🧪 Ejecutando suite completa de tests..."

# Tests unitarios
echo "📝 Ejecutando tests unitarios..."
npm run test:unit -- --coverage

# Tests de integración
echo "🔗 Ejecutando tests de integración..."
npm run test:integration

# Verificar coverage mínimo
echo "📊 Verificando coverage..."
npm run test:coverage

echo "✅ Todos los tests completados!"
```

## 📊 **Coverage Report**

El coverage debe mantener al menos:
- **Líneas**: 80%
- **Funciones**: 80%
- **Branches**: 80%
- **Statements**: 80%

### **Coverage Configuration (.coveragerc)**
```json
{
  "collectCoverageFrom": [
    "src/**/*.{js,jsx}",
    "!src/index.js",
    "!src/reportWebVitals.js",
    "!src/**/*.test.{js,jsx}",
    "!src/**/__tests__/**",
    "!src/**/stories/**"
  ],
  "coverageReporters": [
    "text",
    "text-summary",
    "html",
    "lcov"
  ],
  "coverageDirectory": "coverage"
}
```

Esta guía de testing completa asegura que el frontend React esté bien probado y sea mantenible! 🎯