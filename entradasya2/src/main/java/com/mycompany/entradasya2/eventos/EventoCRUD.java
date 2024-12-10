package com.mycompany.entradasya2.eventos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class EventoCRUD {
    private static final Logger LOGGER = Logger.getLogger(EventoCRUD.class.getName());
    private Connection conn;

    public EventoCRUD() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            String url = "jdbc:mysql://localhost:3306/entradasya";
            String user = "root";
            String password = "";
            conn = DriverManager.getConnection(url, user, password);
            createTableIfNotExists();
        } catch (ClassNotFoundException e) {
            logError("Error al cargar el driver de MySQL", e);
            JOptionPane.showMessageDialog(null, 
                "Error al cargar el driver de MySQL",
                "Error de Driver",
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            logError("Error al conectar con la base de datos", e);
            JOptionPane.showMessageDialog(null, 
                "Error al conectar con la base de datos",
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logError(String message, Throwable e) {
        LOGGER.log(Level.SEVERE, "{0}: {1}", new Object[]{message, e.getMessage()});
        LOGGER.log(Level.FINE, "Stacktrace completo:", e);
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS eventos (
                id INT AUTO_INCREMENT PRIMARY KEY,
                titulo VARCHAR(100) NOT NULL,
                descripcion TEXT,
                fecha_evento DATETIME NOT NULL,
                ubicacion VARCHAR(200) NOT NULL,
                capacidad_total INT NOT NULL,
                entradas_disponibles INT NOT NULL,
                precio DECIMAL(10,2) NOT NULL,
                imagen_url VARCHAR(255),
                estado VARCHAR(20) DEFAULT 'ACTIVO',
                fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            logError("Error al crear la tabla eventos", e);
            JOptionPane.showMessageDialog(null, 
                "Error al crear la tabla eventos",
                "Error de Base de Datos",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean crear(Evento evento) {
        String sql = """
            INSERT INTO eventos (titulo, descripcion, fecha_evento, ubicacion,
                               capacidad_total, entradas_disponibles, precio, 
                               imagen_url, estado, fecha_creacion)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, evento.getTitulo());
            pstmt.setString(2, evento.getDescripcion());
            pstmt.setTimestamp(3, Timestamp.valueOf(evento.getFechaEvento()));
            pstmt.setString(4, evento.getUbicacion());
            pstmt.setInt(5, evento.getCapacidadTotal());
            pstmt.setInt(6, evento.getEntradasDisponibles());
            pstmt.setBigDecimal(7, evento.getPrecio());
            pstmt.setString(8, evento.getImagenUrl());
            pstmt.setString(9, evento.getEstado().toUpperCase());
            pstmt.setTimestamp(10, Timestamp.valueOf(evento.getFechaCreacion()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        evento.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            logError("Error al crear evento", e);
            throw new RuntimeException("Error al crear evento en la base de datos: " + 
                (e.getMessage() != null ? e.getMessage() : "Error desconocido"));
        }
        return false;
    }

    public List<Evento> listarTodos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM eventos WHERE estado = 'ACTIVO' ORDER BY fecha_evento";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Evento evento = new Evento(
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getTimestamp("fecha_evento").toLocalDateTime(),
                    rs.getString("ubicacion"),
                    rs.getInt("capacidad_total"),
                    rs.getBigDecimal("precio")
                );
                evento.setId(rs.getInt("id"));
                evento.setEntradasDisponibles(rs.getInt("entradas_disponibles"));
                evento.setImagenUrl(rs.getString("imagen_url"));
                evento.setEstado(rs.getString("estado"));
                evento.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
                eventos.add(evento);
            }
        } catch (SQLException e) {
            logError("Error al listar eventos", e);
            throw new RuntimeException("Error al obtener los eventos de la base de datos: " + 
                (e.getMessage() != null ? e.getMessage() : "Error desconocido"));
        }
        return eventos;
    }

    public List<Evento> buscar(String criterio) {
        List<Evento> eventos = new ArrayList<>();
        
        if (conn == null) {
            logError("No hay conexión con la base de datos. Intentando reconectar...", null);
            JOptionPane.showMessageDialog(null, 
                "No hay conexión con la base de datos. Intentando reconectar...",
                "Error de Conexión",
                JOptionPane.WARNING_MESSAGE);
            try {
                String url = "jdbc:mysql://localhost:3306/entradasya";
                String user = "root";
                String password = "";
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                logError("Error al reconectar con la base de datos", e);
                JOptionPane.showMessageDialog(null, 
                    "Error al reconectar con la base de datos",
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
                return eventos;
            }
        }

        String sql = """
            SELECT * FROM eventos 
            WHERE estado = 'ACTIVO' 
            AND (titulo LIKE ? OR descripcion LIKE ? OR ubicacion LIKE ?)
            ORDER BY fecha_evento
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchTerm = "%" + criterio + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Evento evento = new Evento(
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getTimestamp("fecha_evento").toLocalDateTime(),
                        rs.getString("ubicacion"),
                        rs.getInt("capacidad_total"),
                        rs.getBigDecimal("precio")
                    );
                    evento.setId(rs.getInt("id"));
                    evento.setEntradasDisponibles(rs.getInt("entradas_disponibles"));
                    evento.setImagenUrl(rs.getString("imagen_url"));
                    evento.setEstado(rs.getString("estado"));
                    evento.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
                    eventos.add(evento);
                }
            }
        } catch (SQLException e) {
            logError("Error al buscar eventos", e);
            throw new RuntimeException("Error al buscar eventos en la base de datos: " + 
                (e.getMessage() != null ? e.getMessage() : "Error desconocido"));
        }
        
        return eventos;
    }

    public boolean actualizar(Evento evento) {
        String sql = """
            UPDATE eventos 
            SET titulo = ?, descripcion = ?, fecha_evento = ?, ubicacion = ?,
                capacidad_total = ?, entradas_disponibles = ?, precio = ?, estado = ?
            WHERE id = ?
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, evento.getTitulo());
            pstmt.setString(2, evento.getDescripcion());
            pstmt.setTimestamp(3, Timestamp.valueOf(evento.getFechaEvento()));
            pstmt.setString(4, evento.getUbicacion());
            pstmt.setInt(5, evento.getCapacidadTotal());
            pstmt.setInt(6, evento.getEntradasDisponibles());
            pstmt.setBigDecimal(7, evento.getPrecio());
            pstmt.setString(8, evento.getEstado().toUpperCase());
            pstmt.setInt(9, evento.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logError("Error al actualizar evento", e);
            throw new RuntimeException("Error al actualizar evento en la base de datos: " + 
                (e.getMessage() != null ? e.getMessage() : "Error desconocido"));
        }
    }

    public Evento obtenerPorId(int id) {
        String sql = "SELECT * FROM eventos WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Evento evento = new Evento(
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getTimestamp("fecha_evento").toLocalDateTime(),
                        rs.getString("ubicacion"),
                        rs.getInt("capacidad_total"),
                        rs.getBigDecimal("precio")
                    );
                    evento.setId(rs.getInt("id"));
                    evento.setEntradasDisponibles(rs.getInt("entradas_disponibles"));
                    evento.setEstado(rs.getString("estado"));
                    return evento;
                }
            }
        } catch (SQLException e) {
            logError("Error al obtener evento por ID", e);
            throw new RuntimeException("Error al obtener el evento de la base de datos: " + 
                (e.getMessage() != null ? e.getMessage() : "Error desconocido"));
        }
        return null;
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logError("Error al cerrar la conexión", e);
                JOptionPane.showMessageDialog(null, 
                    "Error al cerrar la conexión",
                    "Error de Base de Datos",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
