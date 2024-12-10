package com.mycompany.entradasya2.gui;

import com.mycompany.entradasya2.usuario.Cliente;
import com.mycompany.entradasya2.usuario.ClienteCRUD;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListarClientesPanel extends JPanel {
    private final ClienteCRUD clienteCRUD;
    private JTable clientesTable;
    private DefaultTableModel tableModel;
    private static final Color BACKGROUND_COLOR = new Color(230, 240, 250);
    private static final Color BUTTON_COLOR = new Color(200, 220, 240);

    public ListarClientesPanel() {
        clienteCRUD = new ClienteCRUD();
        setupUI();
        cargarClientes();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Título
        JLabel titleLabel = new JLabel("Lista de Clientes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Tabla
        String[] columnNames = {"ID", "Nombre", "Teléfono", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        clientesTable = new JTable(tableModel);
        clientesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        clientesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        clientesTable.setRowHeight(25);
        clientesTable.setBackground(Color.WHITE);
        clientesTable.setGridColor(new Color(220, 220, 220));

        // Scroll pane para la tabla
        JScrollPane scrollPane = new JScrollPane(clientesTable);
        scrollPane.setBackground(BACKGROUND_COLOR);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton refreshButton = new JButton("Actualizar Lista");
        configureButton(refreshButton);
        refreshButton.addActionListener(e -> cargarClientes());

        buttonPanel.add(refreshButton);

        // Agregar componentes
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void configureButton(JButton button) {
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setOpaque(true);
    }

    private void cargarClientes() {
        // Limpiar tabla
        tableModel.setRowCount(0);

        // Obtener lista de clientes
        List<Cliente> clientes = clienteCRUD.listarTodos();

        // Agregar clientes a la tabla
        for (Cliente cliente : clientes) {
            Object[] row = {
                cliente.getId(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getEmail()
            };
            tableModel.addRow(row);
        }
    }
}
