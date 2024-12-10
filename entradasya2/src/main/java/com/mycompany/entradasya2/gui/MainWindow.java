package com.mycompany.entradasya2.gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private static final Color BACKGROUND_COLOR = new Color(230, 240, 250); // Soft light blue
    private static final Color MENU_COLOR = new Color(200, 220, 240); // Slightly darker blue for menu
    
    public MainWindow() {
        setTitle("EntradasYa - Sistema de Gestión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        
        // Establecer color de fondo
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Crear el panel principal con CardLayout para la navegación
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BACKGROUND_COLOR);
        
        // Crear el menú superior
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(MENU_COLOR);
        
        JMenu menuClientes = new JMenu("Clientes");
        JMenu menuEventos = new JMenu("Eventos");
        
        // Items del menú Clientes
        JMenuItem itemNuevoCliente = new JMenuItem("Nuevo Cliente");
        JMenuItem itemBuscarCliente = new JMenuItem("Buscar Cliente");
        JMenuItem itemListarClientes = new JMenuItem("Listar Clientes");
        
        // Configurar colores de los items del menú
        configureMenuItem(itemNuevoCliente);
        configureMenuItem(itemBuscarCliente);
        configureMenuItem(itemListarClientes);
        
        menuClientes.add(itemNuevoCliente);
        menuClientes.add(itemBuscarCliente);
        menuClientes.add(itemListarClientes);
        
        // Items del menú Eventos
        JMenuItem itemNuevoEvento = new JMenuItem("Nuevo Evento");
        JMenuItem itemBuscarEvento = new JMenuItem("Buscar Evento");
        JMenuItem itemListarEventos = new JMenuItem("Listar Eventos");
        
        // Configurar colores de los items del menú de eventos
        configureMenuItem(itemNuevoEvento);
        configureMenuItem(itemBuscarEvento);
        configureMenuItem(itemListarEventos);
        
        menuEventos.add(itemNuevoEvento);
        menuEventos.add(itemBuscarEvento);
        menuEventos.add(itemListarEventos);
        
        menuBar.add(menuClientes);
        menuBar.add(menuEventos);
        setJMenuBar(menuBar);
        
        // Crear los paneles
        ClientePanel clientePanel = new ClientePanel();
        ListarClientesPanel listarClientesPanel = new ListarClientesPanel();
        EventoPanel eventoPanel = new EventoPanel();
        ListarEventosPanel listarEventosPanel = new ListarEventosPanel();
        BuscarClientePanel buscarClientePanel = new BuscarClientePanel();
        BuscarEventoPanel buscarEventoPanel = new BuscarEventoPanel();
        JPanel homePanel = createHomePanel();
        
        // Agregar paneles al cardPanel
        cardPanel.add(homePanel, "HOME");
        cardPanel.add(clientePanel, "NUEVO_CLIENTE");
        cardPanel.add(listarClientesPanel, "LISTAR_CLIENTES");
        cardPanel.add(eventoPanel, "NUEVO_EVENTO");
        cardPanel.add(listarEventosPanel, "LISTAR_EVENTOS");
        cardPanel.add(buscarClientePanel, "BUSCAR_CLIENTE");
        cardPanel.add(buscarEventoPanel, "BUSCAR_EVENTO");
        
        // Agregar listeners a los items del menú
        itemNuevoCliente.addActionListener(e -> cardLayout.show(cardPanel, "NUEVO_CLIENTE"));
        itemListarClientes.addActionListener(e -> cardLayout.show(cardPanel, "LISTAR_CLIENTES"));
        itemBuscarCliente.addActionListener(e -> cardLayout.show(cardPanel, "BUSCAR_CLIENTE"));
        itemNuevoEvento.addActionListener(e -> cardLayout.show(cardPanel, "NUEVO_EVENTO"));
        itemListarEventos.addActionListener(e -> cardLayout.show(cardPanel, "LISTAR_EVENTOS"));
        itemBuscarEvento.addActionListener(e -> cardLayout.show(cardPanel, "BUSCAR_EVENTO"));
        
        // Agregar el panel principal al frame
        add(cardPanel);
        
        // Mostrar el panel inicial
        cardLayout.show(cardPanel, "HOME");
    }
    
    private void configureMenuItem(JMenuItem item) {
        item.setBackground(BACKGROUND_COLOR);
        item.setOpaque(true);
    }
    
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Panel central con mensaje de bienvenida
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Bienvenido a EntradasYa");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Sistema de Gestión de Eventos y Clientes");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        welcomePanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 10, 10);
        welcomePanel.add(subtitleLabel, gbc);
        
        panel.add(welcomePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
