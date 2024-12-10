package com.mycompany.entradasya2.gui;

import com.mycompany.entradasya2.eventos.Evento;
import com.mycompany.entradasya2.eventos.EventoCRUD;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class BuscarEventoPanel extends JPanel {
    private final JTextField searchField;
    private final JTable resultTable;
    private final DefaultTableModel tableModel;
    private final EventoCRUD eventoCRUD;
    
    public BuscarEventoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240));
        eventoCRUD = new EventoCRUD();

        // Panel superior para búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(new Color(240, 240, 240));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Label de búsqueda
        JLabel searchLabel = new JLabel("Buscar por título, descripción o ubicación:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        searchPanel.add(searchLabel, BorderLayout.NORTH);

        // Campo de búsqueda y botón
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBackground(new Color(240, 240, 240));
        
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(searchField, BorderLayout.CENTER);

        JButton searchButton = new JButton("Buscar");
        styleButton(searchButton);
        inputPanel.add(searchButton, BorderLayout.EAST);

        searchPanel.add(inputPanel, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        // Tabla de resultados
        String[] columns = {"ID", "Título", "Descripción", "Ubicación", "Fecha", "Capacidad", "Precio"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);
        styleTable(resultTable);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior para botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton updateButton = new JButton("Actualizar");
        styleButton(updateButton);
        buttonPanel.add(updateButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Configurar acciones
        searchButton.addActionListener(e -> realizarBusqueda());
        searchField.addActionListener(e -> realizarBusqueda());
        updateButton.addActionListener(e -> buscarEventoActionPerformed());
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(20);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void realizarBusqueda() {
        String criterio = searchField.getText().trim();
        System.out.println("Iniciando búsqueda con criterio: " + criterio);
        
        if (criterio.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese un criterio de búsqueda",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Limpiar tabla
        tableModel.setRowCount(0);
        
        // Realizar búsqueda
        try {
            System.out.println("Ejecutando búsqueda en EventoCRUD...");
            List<Evento> resultados = eventoCRUD.buscar(criterio);
            System.out.println("Búsqueda completada. Resultados encontrados: " + resultados.size());
            
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron resultados para: " + criterio,
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Agregar resultados a la tabla
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                for (Evento evento : resultados) {
                    Object[] row = {
                        evento.getId(),
                        evento.getTitulo(),
                        evento.getDescripcion(),
                        evento.getUbicacion(),
                        evento.getFechaEvento().format(formatter),
                        evento.getCapacidadTotal(),
                        String.format("$%.2f", evento.getPrecio())
                    };
                    tableModel.addRow(row);
                    System.out.println("Agregado a tabla: " + evento.getTitulo());
                }
            }
        } catch (IllegalArgumentException e) {
            String errorMsg = "Error en los criterios de búsqueda: " + e.getMessage();
            System.err.println(errorMsg);
            JOptionPane.showMessageDialog(this,
                errorMsg,
                "Error de entrada",
                JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            String errorMsg = "Error durante la búsqueda: " + e.getMessage();
            System.err.println(errorMsg);
            JOptionPane.showMessageDialog(this,
                errorMsg,
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarEventoActionPerformed() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un evento para actualizar", "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Evento evento = eventoCRUD.obtenerPorId(id);
            
            if (evento != null) {
                ActualizarEventoDialog actualizarEventoDialog = new ActualizarEventoDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this), 
                    evento);
                actualizarEventoDialog.setVisible(true);
                
                if (actualizarEventoDialog.isActualizado()) {
                    JOptionPane.showMessageDialog(this, "Evento actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    realizarBusqueda(); // Actualizar la tabla después de la edición
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún evento con el ID especificado", "Evento no encontrado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un número válido para el ID", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar el evento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
