-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS entradasya;
USE entradasya;

-- Tabla de clientes
CREATE TABLE IF NOT EXISTS clientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de eventos
CREATE TABLE IF NOT EXISTS eventos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    descripcion TEXT,
    fecha_evento DATETIME NOT NULL,
    ubicacion VARCHAR(200) NOT NULL,
    capacidad_total INT NOT NULL CHECK (capacidad_total > 0),
    entradas_disponibles INT NOT NULL CHECK (entradas_disponibles >= 0),
    precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    imagen_url VARCHAR(255),
    estado ENUM('activo', 'cancelado', 'finalizado') DEFAULT 'activo',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_entradas CHECK (entradas_disponibles <= capacidad_total)
);

-- Tabla de categorías de eventos
CREATE TABLE IF NOT EXISTS categorias (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

-- Tabla de relación eventos-categorías
CREATE TABLE IF NOT EXISTS evento_categoria (
    evento_id INT,
    categoria_id INT,
    PRIMARY KEY (evento_id, categoria_id),
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE CASCADE
);

-- Tabla de ventas de entradas
CREATE TABLE IF NOT EXISTS ventas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    evento_id INT NOT NULL,
    cliente_id INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('pendiente', 'completada', 'cancelada') DEFAULT 'pendiente',
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE RESTRICT,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE RESTRICT
);

-- Insertar algunas categorías básicas
INSERT IGNORE INTO categorias (nombre, descripcion) VALUES
('Conciertos', 'Eventos musicales en vivo'),
('Deportes', 'Eventos deportivos'),
('Teatro', 'Obras de teatro y espectáculos'),
('Conferencias', 'Charlas y seminarios'),
('Festivales', 'Festivales de música y cultura');

-- Trigger para actualizar entradas_disponibles al realizar una venta
DELIMITER //
CREATE TRIGGER IF NOT EXISTS after_venta_insert
AFTER INSERT ON ventas
FOR EACH ROW
BEGIN
    UPDATE eventos 
    SET entradas_disponibles = entradas_disponibles - NEW.cantidad
    WHERE id = NEW.evento_id;
END //
DELIMITER ;

-- Trigger para validar la disponibilidad de entradas antes de una venta
DELIMITER //
CREATE TRIGGER IF NOT EXISTS before_venta_insert
BEFORE INSERT ON ventas
FOR EACH ROW
BEGIN
    DECLARE entradas_disp INT;
    SELECT entradas_disponibles INTO entradas_disp
    FROM eventos WHERE id = NEW.evento_id;
    
    IF entradas_disp < NEW.cantidad THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No hay suficientes entradas disponibles';
    END IF;
END //
DELIMITER ;
