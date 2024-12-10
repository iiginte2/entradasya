package com.mycompany.entradasya2.gui;

import com.mycompany.entradasya2.eventos.Evento;
import com.mycompany.entradasya2.eventos.EventoCRUD;
import java.awt.*;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.*;

public class ActualizarEventoDialog extends JDialog {
    private final JTextField txtTitulo = new JTextField();
    private final JTextArea txtDescripcion = new JTextArea();
    private final JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
    private final JTextField txtUbicacion = new JTextField();
    private final JTextField txtCapacidad = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JComboBox<String> comboEstado = new JComboBox<>(new String[]{"ACTIVO", "INACTIVO"});
    private final EventoCRUD eventoCRUD;
    private final Evento evento;
    private boolean actualizado = false;
    
    public ActualizarEventoDialog(JFrame parent, Evento evento) {
        super(parent, "Actualizar Evento", true);
        this.evento = evento;
        this.eventoCRUD = new EventoCRUD();
        
        initComponents();
        
        // Inicializar campos con los datos del evento
        txtTitulo.setText(evento.getTitulo());
        txtDescripcion.setText(evento.getDescripcion());
        dateSpinner.setValue(Date.from(evento.getFechaEvento().atZone(ZoneId.systemDefault()).toInstant()));
        txtUbicacion.setText(evento.getUbicacion());
        txtCapacidad.setText(String.valueOf(evento.getCapacidadTotal()));
        txtPrecio.setText(evento.getPrecio().toString());
        comboEstado.setSelectedItem(evento.getEstado().toUpperCase());
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        add(txtTitulo, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        txtDescripcion.setRows(3);
        add(new JScrollPane(txtDescripcion), gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        add(dateSpinner, gbc);

        // Location
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1;
        add(txtUbicacion, gbc);

        // Capacity
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Capacidad:"), gbc);
        gbc.gridx = 1;
        add(txtCapacidad, gbc);

        // Price
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        add(txtPrecio, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        add(comboEstado, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton guardarButton = new JButton("Guardar");
        JButton cancelarButton = new JButton("Cancelar");
        buttonPanel.add(guardarButton);
        buttonPanel.add(cancelarButton);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        guardarButton.addActionListener(e -> guardar());
        cancelarButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(null);
    }

    private void guardar() {
        try {
            // Validar campos obligatorios
            if (txtTitulo.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("El título es obligatorio");
            }
            if (txtDescripcion.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("La descripción es obligatoria");
            }
            if (txtUbicacion.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("La ubicación es obligatoria");
            }
            if (dateSpinner.getValue() == null) {
                throw new IllegalArgumentException("La fecha es obligatoria");
            }

            // Validar capacidad
            int capacidad;
            try {
                capacidad = Integer.parseInt(txtCapacidad.getText().trim());
                if (capacidad <= 0) {
                    throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La capacidad debe ser un número válido");
            }

            // Validar precio
            BigDecimal precio;
            try {
                String precioStr = txtPrecio.getText().trim().replace("$", "").replace(",", "");
                precio = new BigDecimal(precioStr);
                if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("El precio debe ser mayor que cero");
                }
                // Validar que el precio no exceda el límite de DECIMAL(10,2)
                if (precio.compareTo(new BigDecimal("99999999.99")) > 0) {
                    throw new IllegalArgumentException("El precio no puede exceder de $99,999,999.99");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El precio debe ser un número válido");
            }

            // Validar estado
            String estado = comboEstado.getSelectedItem().toString().toUpperCase();
            if (!estado.equals("ACTIVO") && !estado.equals("INACTIVO")) {
                JOptionPane.showMessageDialog(this, "Estado inválido. Debe ser ACTIVO o INACTIVO.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            evento.setEstado(estado);

            // Actualizar objeto evento
            evento.setTitulo(txtTitulo.getText().trim());
            evento.setDescripcion(txtDescripcion.getText().trim());
            evento.setFechaEvento(((Date) dateSpinner.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
            evento.setUbicacion(txtUbicacion.getText().trim());
            evento.setCapacidadTotal(capacidad);
            evento.setEntradasDisponibles(capacidad);
            evento.setPrecio(precio);
            
            // Guardar en base de datos
            if (eventoCRUD.actualizar(evento)) {
                actualizado = true;
                JOptionPane.showMessageDialog(this,
                    "Evento actualizado exitosamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                throw new RuntimeException("No se pudo actualizar el evento en la base de datos");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                "Error de validación: " + e.getMessage(),
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar el evento: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isActualizado() {
        return actualizado;
    }
}
