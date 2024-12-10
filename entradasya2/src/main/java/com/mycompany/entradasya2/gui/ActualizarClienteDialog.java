package com.mycompany.entradasya2.gui;

import com.mycompany.entradasya2.usuario.Cliente;
import com.mycompany.entradasya2.usuario.ClienteCRUD;
import javax.swing.*;
import java.awt.*;

public class ActualizarClienteDialog extends JDialog {
    private final JTextField txtNombre;
    private final JTextField txtEmail;
    private final JTextField txtTelefono;
    private final Cliente cliente;
    private final ClienteCRUD clienteCRUD;
    private boolean actualizado = false;
    
    public ActualizarClienteDialog(JFrame parent, Cliente cliente) {
        super(parent, "Actualizar Cliente", true);
        this.cliente = cliente;
        this.clienteCRUD = new ClienteCRUD();
        
        setLayout(new BorderLayout(10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Campos
        txtNombre = new JTextField(20);
        txtEmail = new JTextField(20);
        txtTelefono = new JTextField(20);
        
        // Inicializar campos con datos actuales
        txtNombre.setText(cliente.getNombre());
        txtEmail.setText(cliente.getEmail());
        txtTelefono.setText(cliente.getTelefono());
        
        // Agregar campos al panel
        mainPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtNombre, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtEmail, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtTelefono, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        
        // Agregar paneles al diálogo
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void guardar() {
        // Validar campos
        if (txtNombre.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Los campos Nombre y Email son obligatorios.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Actualizar objeto cliente
        cliente.setNombre(txtNombre.getText().trim());
        cliente.setEmail(txtEmail.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        
        // Guardar en base de datos
        try {
            if (clienteCRUD.actualizar(cliente)) {
                actualizado = true;
                JOptionPane.showMessageDialog(this,
                    "Cliente actualizado exitosamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el cliente.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                "Error en los datos ingresados: " + e.getMessage(),
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isActualizado() {
        return actualizado;
    }
}
