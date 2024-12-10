=================================================================
                    EntradasYa - Sistema de Gestión de Eventos
                              Documentación Completa
=================================================================

Versión: 2.0
Fecha de última actualización: Diciembre 2024
Desarrollado por: Equipo EntradasYa

ÍNDICE
======
1. Introducción
2. Requisitos del Sistema
3. Instalación y Configuración
4. Arquitectura del Sistema
5. Base de Datos
6. Funcionalidades del Sistema
7. Guía de Usuario
8. Administración del Sistema
9. Seguridad
10. Solución de Problemas
11. Preguntas Frecuentes
12. Glosario
13. Información de Contacto y Soporte

1. INTRODUCCIÓN
===============
EntradasYa es un sistema integral de gestión de eventos diseñado para facilitar la organización, administración y venta de entradas para todo tipo de eventos. Esta plataforma proporciona una solución completa que permite a los organizadores gestionar sus eventos de manera eficiente y a los usuarios finales adquirir entradas de forma sencilla y segura.

1.1 Características Principales
-----------------------------
- Gestión completa del ciclo de vida de eventos
- Interface gráfica intuitiva y fácil de usar
- Sistema de estados para control de eventos
- Gestión de capacidad y disponibilidad
- Reportes y estadísticas
- Sistema de búsqueda y filtrado avanzado
- Gestión de usuarios y permisos
- Historial de transacciones
- Copias de seguridad automáticas

1.2 Público Objetivo
-------------------
- Organizadores de eventos
- Administradores de venues
- Personal de venta de entradas
- Gestores de eventos
- Administradores del sistema

2. REQUISITOS DEL SISTEMA
=========================

2.1 Requisitos de Hardware
-------------------------
- Procesador: Intel Core i3 o superior
- Memoria RAM: 4GB mínimo (8GB recomendado)
- Espacio en disco: 500MB para la aplicación
- Resolución de pantalla: 1366x768 o superior
- Conexión a Internet: 1Mbps mínimo

2.2 Requisitos de Software
-------------------------
- Sistema Operativo:
  * Windows 10/11 (64 bits)
  * macOS 10.14 o superior
  * Linux (distribuciones principales)

- Java Development Kit (JDK):
  * Versión: 8 o superior
  * Tipo: Oracle JDK o OpenJDK
  * Variables de entorno configuradas

- Base de Datos:
  * MySQL 5.7 o superior
  * MariaDB 10.3 o superior

- IDE Recomendado:
  * NetBeans 12.0 o superior
  * Plugins necesarios instalados

2.3 Requisitos de Red
--------------------
- Puerto 3306 disponible para MySQL
- Firewall configurado para permitir conexiones
- Acceso a Internet para actualizaciones

3. INSTALACIÓN Y CONFIGURACIÓN
==============================

3.1 Preparación del Entorno
--------------------------
1. Instalar JDK:
   - Descargar el instalador de Java de oracle.com
   - Ejecutar el instalador
   - Verificar la instalación: java -version
   - Configurar JAVA_HOME

2. Instalar MySQL:
   - Descargar MySQL Community Server
   - Ejecutar el instalador
   - Configurar usuario root
   - Verificar el servicio MySQL

3. Configurar IDE:
   - Instalar NetBeans
   - Configurar JDK en NetBeans
   - Instalar plugins necesarios

3.2 Configuración de la Base de Datos
-----------------------------------
1. Crear la base de datos:
   ```sql
   CREATE DATABASE entradasya;
   USE entradasya;
   ```

2. Ejecutar script de inicialización:
   - Ubicación: /scripts/init_database.sql
   - Contiene estructura de tablas
   - Datos iniciales necesarios

3. Configurar conexión:
   - Host: localhost
   - Puerto: 3306
   - Usuario: root
   - Contraseña: [vacía]
   - Base de datos: entradasya

3.3 Configuración del Proyecto
----------------------------
1. Clonar repositorio:
   ```bash
   git clone https://github.com/tu-usuario/entradasya.git
   cd entradasya
   ```

2. Importar proyecto en NetBeans:
   - File -> Open Project
   - Seleccionar directorio del proyecto
   - Esperar indexación

3. Configurar dependencias:
   - Verificar pom.xml
   - Actualizar dependencias Maven
   - Resolver conflictos si existen

4. ARQUITECTURA DEL SISTEMA
==========================

4.1 Capas de la Aplicación
-------------------------
1. Presentación (GUI):
   - Interfaces de usuario
   - Formularios
   - Ventanas de diálogo
   - Paneles de control

2. Lógica de Negocio:
   - Gestión de eventos
   - Validaciones
   - Procesamiento de datos
   - Reglas de negocio

3. Acceso a Datos:
   - Conexión a base de datos
   - Operaciones CRUD
   - Gestión de transacciones

4.2 Componentes Principales
-------------------------
1. MainWindow:
   - Ventana principal
   - Menú de navegación
   - Panel de control

2. EventoManager:
   - Gestión de eventos
   - Estados y transiciones
   - Validaciones

3. DatabaseConnection:
   - Pool de conexiones
   - Transacciones
   - Gestión de errores

5. BASE DE DATOS
================

5.1 Modelo de Datos
------------------
1. Tabla eventos:
   ```sql
   CREATE TABLE eventos (
       id INT PRIMARY KEY AUTO_INCREMENT,
       titulo VARCHAR(100) NOT NULL,
       descripcion TEXT,
       fecha DATE NOT NULL,
       hora TIME NOT NULL,
       lugar VARCHAR(200),
       capacidad INT NOT NULL,
       precio DECIMAL(10,2),
       estado VARCHAR(20) NOT NULL,
       fecha_creacion TIMESTAMP
   );
   ```

2. Tabla usuarios:
   ```sql
   CREATE TABLE usuarios (
       id INT PRIMARY KEY AUTO_INCREMENT,
       nombre VARCHAR(100),
       email VARCHAR(100) UNIQUE,
       password VARCHAR(255),
       rol VARCHAR(50),
       activo BOOLEAN
   );
   ```

3. Tabla entradas:
   ```sql
   CREATE TABLE entradas (
       id INT PRIMARY KEY AUTO_INCREMENT,
       evento_id INT,
       usuario_id INT,
       fecha_compra TIMESTAMP,
       estado VARCHAR(50),
       FOREIGN KEY (evento_id) REFERENCES eventos(id),
       FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
   );
   ```

5.2 Relaciones y Restricciones
----------------------------
- Eventos -> Entradas (1:N)
- Usuarios -> Entradas (1:N)
- Estados válidos: ACTIVO, INACTIVO
- Precios: Siempre positivos
- Capacidad: Mayor que cero

6. FUNCIONALIDADES DEL SISTEMA
==============================

6.1 Gestión de Eventos
---------------------
1. Crear Evento:
   - Campos obligatorios:
     * Título
     * Fecha
     * Hora
     * Capacidad
     * Estado
   - Validaciones:
     * Fecha futura
     * Capacidad > 0
     * Precio >= 0

2. Actualizar Evento:
   - Modificar información
   - Cambiar estado
   - Actualizar capacidad
   - Modificar precios

3. Eliminar Evento:
   - Verificación de seguridad
   - Eliminación lógica
   - Registro de auditoría

6.2 Estados de Eventos
--------------------
1. ACTIVO:
   - Evento visible
   - Permite venta de entradas
   - Aparece en búsquedas

2. INACTIVO:
   - Evento oculto
   - No permite ventas
   - Solo visible para admin

6.3 Gestión de Entradas
----------------------
1. Venta de entradas:
   - Verificación de disponibilidad
   - Cálculo de precios
   - Generación de comprobante

2. Cancelación:
   - Política de reembolso
   - Actualización de disponibilidad
   - Registro de cancelación

7. GUÍA DE USUARIO
==================

7.1 Inicio del Sistema
--------------------
1. Ejecutar la aplicación:
   - Desde NetBeans: Run Project
   - Desde JAR: java -jar entradasya.jar

2. Pantalla principal:
   - Barra de menú superior
   - Lista de eventos
   - Panel de filtros
   - Botones de acción

7.2 Crear Nuevo Evento
--------------------
1. Hacer clic en "Nuevo Evento"

2. Completar formulario:
   - Título: Nombre del evento
   - Descripción: Detalles
   - Fecha: Seleccionar del calendario
   - Hora: Formato 24h
   - Lugar: Ubicación
   - Capacidad: Número entero
   - Precio: Valor decimal
   - Estado: ACTIVO por defecto

3. Validaciones:
   - Campos obligatorios
   - Formato de fecha
   - Valores numéricos
   - Estado válido

4. Guardar:
   - Verificar datos
   - Confirmar operación
   - Mensaje de éxito

7.3 Actualizar Evento
-------------------
1. Seleccionar evento de la lista

2. Hacer clic en "Actualizar"

3. Modificar campos:
   - Todos los campos editables
   - Estado: ACTIVO/INACTIVO
   - Validaciones automáticas

4. Guardar cambios:
   - Confirmar modificaciones
   - Actualización en BD
   - Refresh de lista

7.4 Filtros y Búsqueda
---------------------
1. Filtros disponibles:
   - Por estado
   - Por fecha
   - Por precio
   - Por capacidad

2. Búsqueda:
   - Por título
   - Por descripción
   - Por ubicación

8. ADMINISTRACIÓN DEL SISTEMA
============================

8.1 Mantenimiento
---------------
1. Respaldos:
   - Programación diaria
   - Almacenamiento seguro
   - Verificación de integridad

2. Limpieza:
   - Eventos antiguos
   - Registros temporales
   - Caché del sistema

8.2 Monitoreo
-----------
1. Rendimiento:
   - Uso de memoria
   - Tiempo de respuesta
   - Conexiones activas

2. Errores:
   - Log de errores
   - Alertas automáticas
   - Diagnóstico

9. SEGURIDAD
============

9.1 Autenticación
---------------
1. Usuarios:
   - Roles definidos
   - Permisos específicos
   - Contraseñas seguras

2. Sesiones:
   - Timeout
   - Cierre automático
   - Control de acceso

9.2 Datos
--------
1. Validación:
   - Entrada de usuarios
   - Tipos de datos
   - Formatos válidos

2. Protección:
   - SQL Injection
   - XSS
   - CSRF

10. SOLUCIÓN DE PROBLEMAS
=========================

10.1 Problemas Comunes
--------------------
1. Error de conexión:
   - Verificar MySQL activo
   - Comprobar credenciales
   - Revisar firewall

2. Error al guardar:
   - Validar campos
   - Verificar permisos
   - Comprobar conexión

3. Estado inválido:
   - Solo ACTIVO/INACTIVO
   - Mayúsculas requeridas
   - Verificar longitud

10.2 Diagnóstico
--------------
1. Logs:
   - Ubicación: /logs/
   - Formato: fecha_error.log
   - Nivel: INFO, ERROR

2. Herramientas:
   - Monitor MySQL
   - Visor de logs
   - Debugger Java

11. PREGUNTAS FRECUENTES
========================

11.1 Generales
------------
P: ¿Cómo cambio el estado de un evento?
R: Desde la ventana de actualización, seleccionar nuevo estado y guardar.

P: ¿Por qué no puedo guardar un evento?
R: Verificar campos obligatorios y formatos válidos.

P: ¿Cómo recupero un evento eliminado?
R: Contactar al administrador del sistema.

11.2 Técnicas
-----------
P: ¿Cómo configuro la base de datos?
R: Seguir sección 3.2 de este manual.

P: ¿Qué hacer si hay error de conexión?
R: Verificar servicio MySQL y credenciales.

12. GLOSARIO
============

- Evento: Unidad principal del sistema
- Estado: ACTIVO o INACTIVO
- Capacidad: Número máximo de entradas
- GUI: Interface gráfica de usuario
- CRUD: Create, Read, Update, Delete
- JDK: Java Development Kit
- IDE: Entorno de desarrollo integrado

13. INFORMACIÓN DE CONTACTO Y SOPORTE
====================================

Soporte Técnico:
- Email: soporte@entradasya.com
- Teléfono: +XX-XXXX-XXXX
- Horario: 9:00 - 18:00 (L-V)

Reportar Problemas:
1. Documentar el error
2. Capturar pantalla
3. Enviar log de errores
4. Describir pasos de reproducción

=================================================================
                    Fin de la Documentación
=================================================================
