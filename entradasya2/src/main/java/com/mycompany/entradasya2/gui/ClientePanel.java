package com.mycompany.entradasya2.gui;

import com.mycompany.entradasya2.usuario.Cliente;
import com.mycompany.entradasya2.usuario.ClienteCRUD;
import javax.swing.*;
import java.awt.*;

public class ClientePanel extends JPanel {
    private final ClienteCRUD clienteCRUD;
    private JTextField nombreField;
    private JTextField telefonoField;
    private JTextField emailField;
    private static final Color BACKGROUND_COLOR = new Color(230, 240, 250); // Soft light blue
    private static final Color BUTTON_COLOR = new Color(200, 220, 240); // Slightly darker blue for buttons
    
    public ClientePanel() {
        clienteCRUD = new ClienteCRUD();
        setupUI();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        
        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        JLabel titleLabel = new JLabel("Gestión de Clientes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);
        
        // Campos del formulario
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(nombreLabel, gbc);
        
        gbc.gridx = 1;
        nombreField = new JTextField(20);
        nombreField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(nombreField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel telefonoLabel = new JLabel("Teléfono:");
        telefonoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(telefonoLabel, gbc);
        
        gbc.gridx = 1;
        telefonoField = new JTextField(20);
        telefonoField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(telefonoField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(emailField, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton guardarButton = new JButton("Guardar");
        JButton limpiarButton = new JButton("Limpiar");
        
        // Estilo de botones
        configureButton(guardarButton);
        configureButton(limpiarButton);
        
        guardarButton.addActionListener(e -> guardarCliente());
        limpiarButton.addActionListener(e -> limpiarCampos());
        
        buttonPanel.add(guardarButton);
        buttonPanel.add(limpiarButton);
        
        // Agregar paneles al panel principal
        add(formPanel, BorderLayout.CENTER);
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
    
    private void guardarCliente() {
        if (validarCampos()) {
            Cliente cliente = new Cliente();
            cliente.setNombre(nombreField.getText());
            cliente.setTelefono(telefonoField.getText());
            cliente.setEmail(emailField.getText());
            
            clienteCRUD.crear(cliente);
            limpiarCampos();
        }
    }
    
    private boolean validarCampos() {
        if (nombreField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El email es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void limpiarCampos() {
        nombreField.setText("");
        telefonoField.setText("");
        emailField.setText("");
    }
}
