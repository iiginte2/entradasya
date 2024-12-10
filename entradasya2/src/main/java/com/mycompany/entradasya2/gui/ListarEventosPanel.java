package com.mycompany.entradasya2.gui;

import com.mycompany.entradasya2.eventos.EventoCRUD;
import com.mycompany.entradasya2.eventos.Evento;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import javax.swing.border.EmptyBorder;

public class ListarEventosPanel extends JPanel {
    private final EventoCRUD eventoCRUD;
    private final JTable tabla;
    private final DefaultTableModel modelo;
    private final JButton btnRefrescar;
    private final JButton btnActualizar;
    private static final Color BACKGROUND_COLOR = new Color(230, 240, 250);
    private static final String[] COLUMNAS = {
        "ID", "Título", "Fecha", "Ubicación", "Capacidad", "Disponibles", "Precio", "Estado"
    };

    public ListarEventosPanel() {
        eventoCRUD = new EventoCRUD();
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Panel superior con título y botón de refrescar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Título
        JLabel titleLabel = new JLabel("Lista de Eventos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.WEST);

        // Botón de refrescar
        btnRefrescar = new JButton("Refrescar");
        styleButton(btnRefrescar);
        
        // Botón de actualizar
        btnActualizar = new JButton("Actualizar Evento");
        styleButton(btnActualizar);
        
        // Panel para los botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnRefrescar);
        
        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Crear tabla
        modelo = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        
        // Configurar aspecto de la tabla
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.setRowHeight(25);
        tabla.setGridColor(Color.LIGHT_GRAY);
        tabla.setSelectionBackground(new Color(200, 220, 240));
        tabla.setSelectionForeground(Color.BLACK);

        // Configurar renderizador de fechas
        tabla.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof LocalDateTime dateTime) {
                    value = dateTime.format(formatter);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        
        // Configurar renderizador de precios
        tabla.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof BigDecimal precio) {
                    value = currencyFormatter.format(precio);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        
        // Configurar renderizador de estado
        tabla.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    if ("ACTIVO".equals(value.toString())) {
                        c.setForeground(new Color(0, 128, 0)); // Verde oscuro
                    } else {
                        c.setForeground(Color.RED);
                    }
                }
                return c;
            }
        });

        // Scroll pane para la tabla
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Configurar acción del botón refrescar
        btnRefrescar.addActionListener(e -> cargarEventos());
        
        // Configurar acción del botón actualizar
        btnActualizar.addActionListener(e -> actualizarEventoSeleccionado());

        // Cargar eventos inicialmente
        cargarEventos();
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }

    private void cargarEventos() {
        // Limpiar tabla
        modelo.setRowCount(0);
        
        // Obtener eventos
        List<Evento> eventos = eventoCRUD.listarTodos();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        
        // Agregar eventos a la tabla
        for (Evento evento : eventos) {
            modelo.addRow(new Object[]{
                evento.getId(),
                evento.getTitulo(),
                evento.getFechaEvento().format(formatter),
                evento.getUbicacion(),
                evento.getCapacidadTotal(),
                evento.getEntradasDisponibles(),
                String.format("$%.2f", evento.getPrecio()),
                evento.getEstado()
            });
        }
    }
    
    private void actualizarEventoSeleccionado() {
        int selectedRow = tabla.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione un evento para actualizar",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Obtener datos del evento seleccionado
            Integer id = (Integer) modelo.getValueAt(selectedRow, 0);
            String titulo = (String) modelo.getValueAt(selectedRow, 1);
            String fechaStr = (String) modelo.getValueAt(selectedRow, 2);
            String ubicacion = (String) modelo.getValueAt(selectedRow, 3);
            Integer capacidad = (Integer) modelo.getValueAt(selectedRow, 4);
            Integer disponibles = (Integer) modelo.getValueAt(selectedRow, 5);
            String precioStr = ((String) modelo.getValueAt(selectedRow, 6))
                .replace("$", "")
                .replace(",", "")
                .trim();
            
            // Convertir fecha string a LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            LocalDateTime fechaEvento = LocalDateTime.parse(fechaStr, formatter);
            
            // Crear objeto evento con los datos actuales
            Evento evento = new Evento(
                titulo,
                "", // descripción (se actualizará en el diálogo)
                fechaEvento,
                ubicacion,
                capacidad,
                new BigDecimal(precioStr)
            );
            evento.setId(id);
            evento.setEntradasDisponibles(disponibles);
            
            // Mostrar diálogo de actualización
            ActualizarEventoDialog dialog = new ActualizarEventoDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                evento
            );
            dialog.setVisible(true);
            
            // Si se actualizó el evento, recargar la tabla
            if (dialog.isActualizado()) {
                cargarEventos();
            }
        } catch (Exception e) {
            String errorMsg = "Error al preparar el evento para actualización: " + e.getMessage();
            System.err.println(errorMsg);
            JOptionPane.showMessageDialog(this,
                errorMsg,
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
