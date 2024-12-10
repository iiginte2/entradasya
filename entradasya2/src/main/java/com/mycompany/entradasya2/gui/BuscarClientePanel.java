package com.mycompany.entradasya2.gui;

import com.mycompany.entradasya2.usuario.Cliente;
import com.mycompany.entradasya2.usuario.ClienteCRUD;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class BuscarClientePanel extends JPanel {
    private final JTextField searchField;
    private final JTable resultTable;
    private final DefaultTableModel tableModel;
    private final ClienteCRUD clienteCRUD;
    
    public BuscarClientePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(230, 240, 250));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        clienteCRUD = new ClienteCRUD();
        
        // Panel superior para búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setOpaque(false);
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Buscar");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Configurar tabla de resultados
        String[] columns = {"ID", "Nombre", "Email", "Teléfono"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        JButton updateButton = new JButton("Actualizar");
        updateButton.addActionListener(e -> actualizarCliente());
        buttonPanel.add(updateButton);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setOpaque(false);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Agregar componentes al panel
        add(mainPanel, BorderLayout.CENTER);
        
        // Agregar listener al botón de búsqueda
        searchButton.addActionListener(e -> realizarBusqueda());
        
        // Agregar listener para búsqueda al presionar Enter
        searchField.addActionListener(e -> realizarBusqueda());
    }
    
    private void realizarBusqueda() {
        String criterio = searchField.getText().trim();
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
        List<Cliente> resultados = clienteCRUD.buscar(criterio);
        
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No se encontraron resultados para: " + criterio,
                "Sin resultados",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Agregar resultados a la tabla
            for (Cliente cliente : resultados) {
                Object[] row = {
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getEmail(),
                    cliente.getTelefono()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void actualizarCliente() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione un cliente para actualizar",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener el cliente seleccionado
        Cliente cliente = new Cliente();
        cliente.setId((Integer) tableModel.getValueAt(selectedRow, 0));
        cliente.setNombre((String) tableModel.getValueAt(selectedRow, 1));
        cliente.setEmail((String) tableModel.getValueAt(selectedRow, 2));
        cliente.setTelefono((String) tableModel.getValueAt(selectedRow, 3));
        
        // Mostrar diálogo de actualización
        ActualizarClienteDialog dialog = new ActualizarClienteDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            cliente
        );
        dialog.setVisible(true);
        
        // Actualizar tabla si se modificó el cliente
        if (dialog.isActualizado()) {
            realizarBusqueda();
        }
    }
}
