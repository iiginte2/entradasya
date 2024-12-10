package com.mycompany.entradasya2.gui;

import com.mycompany.entradasya2.eventos.Evento;
import com.mycompany.entradasya2.eventos.EventoCRUD;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import javax.swing.SpinnerDateModel;

public class EventoPanel extends JPanel {
    private final JTextField txtTitulo;
    private final JTextArea txtDescripcion;
    private final JSpinner dateSpinner;
    private final JTextField txtUbicacion;
    private final JTextField txtCapacidad;
    private final JTextField txtPrecio;
    private final JButton btnGuardar;
    private final JButton btnLimpiar;
    private final EventoCRUD eventoCRUD;
    private static final Color BACKGROUND_COLOR = new Color(230, 240, 250);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);
    private static final int FIELD_WIDTH = 20;

    public EventoPanel() {
        eventoCRUD = new EventoCRUD();
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Panel principal con GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Título del panel
        JLabel titleLabel = new JLabel("Nuevo Evento");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 20, 5);
        mainPanel.add(titleLabel, gbc);

        // Resetear insets y gridwidth
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 1;

        // Campos del formulario
        txtTitulo = createTextField();
        txtDescripcion = new JTextArea(4, FIELD_WIDTH);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);

        // Initialize date spinner with current date and time
        Calendar calendar = Calendar.getInstance();
        Date initialDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 10); // Set maximum date to 10 years from now
        Date maxDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -20); // Set minimum date to 10 years before now
        Date minDate = calendar.getTime();
        
        SpinnerDateModel dateModel = new SpinnerDateModel(initialDate, minDate, maxDate, Calendar.MINUTE);
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy HH:mm");
        dateSpinner.setEditor(dateEditor);

        txtUbicacion = createTextField();
        txtCapacidad = createTextField();
        txtPrecio = createTextField();

        // Agregar campos al panel
        addFormField(mainPanel, "Título:", txtTitulo, gbc, 1);
        addFormField(mainPanel, "Descripción:", scrollDescripcion, gbc, 2);
        addFormField(mainPanel, "Fecha del Evento:", dateSpinner, gbc, 3);
        addFormField(mainPanel, "Ubicación:", txtUbicacion, gbc, 4);
        addFormField(mainPanel, "Capacidad:", txtCapacidad, gbc, 5);
        addFormField(mainPanel, "Precio:", txtPrecio, gbc, 6);

        // Panel para botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        btnGuardar = new JButton("Guardar");
        btnLimpiar = new JButton("Limpiar");

        styleButton(btnGuardar);
        styleButton(btnLimpiar);

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        // Agregar el panel principal con margen
        JPanel marginPanel = new JPanel(new BorderLayout());
        marginPanel.setBackground(BACKGROUND_COLOR);
        marginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        marginPanel.add(mainPanel, BorderLayout.CENTER);
        add(marginPanel, BorderLayout.CENTER);

        // Configurar acciones de los botones
        btnGuardar.addActionListener(e -> guardarEvento());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(FIELD_WIDTH);
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        return field;
    }

    private void addFormField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
        gbc.fill = GridBagConstraints.NONE;
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

    private void guardarEvento() {
        try {
            // Validate fields
            if (txtTitulo.getText().trim().isEmpty() || txtUbicacion.getText().trim().isEmpty() ||
                txtCapacidad.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos obligatorios.",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date selectedDate = (Date) dateSpinner.getValue();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this,
                    "Por favor seleccione la fecha del evento",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDateTime fechaEvento = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            int capacidad;
            BigDecimal precio;

            try {
                capacidad = Integer.parseInt(txtCapacidad.getText().trim());
                if (capacidad <= 0) {
                    JOptionPane.showMessageDialog(this,
                        "La capacidad debe ser mayor que cero.",
                        "Error de Validación",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un número válido para la capacidad",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                precio = new BigDecimal(txtPrecio.getText().trim());
                if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this,
                        "El precio debe ser mayor que cero.",
                        "Error de Validación",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un número válido para el precio",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Evento evento = new Evento(
                txtTitulo.getText().trim(),
                txtDescripcion.getText().trim(),
                fechaEvento,
                txtUbicacion.getText().trim(),
                capacidad,
                precio
            );

            if (eventoCRUD.crear(evento)) {
                JOptionPane.showMessageDialog(this, "Evento guardado exitosamente.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al guardar el evento en la base de datos.", 
                    "Error de Base de Datos", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Error en los datos ingresados: " + e.getMessage(), 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtDescripcion.setText("");
        dateSpinner.setValue(new Date()); // Reset to current date/time
        txtUbicacion.setText("");
        txtCapacidad.setText("");
        txtPrecio.setText("");
        txtTitulo.requestFocus();
    }
}
