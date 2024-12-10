package com.mycompany.entradasya2;

import com.mycompany.entradasya2.usuario.Cliente;
import com.mycompany.entradasya2.usuario.ClienteCRUD;
import java.util.List;
import javax.swing.JOptionPane;

public class Entradasya2 {
    private static final ClienteCRUD crud = new ClienteCRUD();
    
    public static void main(String[] args) {
        String[] opciones = {
            "Crear Cliente",
            "Buscar Cliente",
            "Listar Todos los Clientes",
            "Actualizar Cliente",
            "Eliminar Cliente",
            "Salir"
        };
        
        while (true) {
            String opcion = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione una opción",
                "Gestión de Clientes",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );
            
            if (opcion == null || opcion.equals("Salir")) {
                break;
            }
            
            switch (opcion) {
                case "Crear Cliente" -> crearCliente();
                case "Buscar Cliente" -> buscarCliente();
                case "Listar Todos los Clientes" -> listarClientes();
                case "Actualizar Cliente" -> actualizarCliente();
                case "Eliminar Cliente" -> eliminarCliente();
            }
        }
    }
    
    private static void crearCliente() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del cliente:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            String telefono = JOptionPane.showInputDialog("Ingrese el teléfono del cliente:");
            String email = JOptionPane.showInputDialog("Ingrese el email del cliente:");
            
            // Create Cliente object with correct parameters
            Cliente nuevoCliente = new Cliente(nombre, telefono, email);
            crud.crear(nuevoCliente);
        }
    }
    
    private static void buscarCliente() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del cliente a buscar:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            // Convert String to int for ID
            int clienteId = Integer.parseInt(nombre);
            Cliente cliente = crud.buscar(clienteId);
            if (cliente != null) {
                JOptionPane.showMessageDialog(null, """
                    Cliente encontrado:
                    Nombre: %s
                    Teléfono: %s
                    Email: %s
                    """.formatted(cliente.getNombre(), cliente.getTelefono(), cliente.getEmail()));
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún cliente con ese nombre");
            }
        }
    }
    
    private static void listarClientes() {
        List<Cliente> clientes = crud.listarTodos();
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay clientes registrados");
            return;
        }
        
        StringBuilder mensaje = new StringBuilder("Lista de Clientes:\n\n");
        for (Cliente cliente : clientes) {
            mensaje.append("Nombre: ").append(cliente.getNombre())
                  .append("\nTeléfono: ").append(cliente.getTelefono())
                  .append("\nEmail: ").append(cliente.getEmail())
                  .append("\n\n");
        }
        
        JOptionPane.showMessageDialog(null, mensaje.toString());
    }
    
    private static void actualizarCliente() {
        String nombreBuscar = JOptionPane.showInputDialog("Ingrese el nombre del cliente a actualizar:");
        if (nombreBuscar != null && !nombreBuscar.trim().isEmpty()) {
            // Convert String to int for ID
            int clienteId = Integer.parseInt(nombreBuscar);
            Cliente clienteExistente = crud.buscar(clienteId);
            if (clienteExistente != null) {
                String nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre:", clienteExistente.getNombre());
                String nuevoTelefono = JOptionPane.showInputDialog("Ingrese el nuevo teléfono:", clienteExistente.getTelefono());
                String nuevoEmail = JOptionPane.showInputDialog("Ingrese el nuevo email:", clienteExistente.getEmail());
                
                // Create Cliente object with correct parameters
                Cliente clienteActualizado = new Cliente(clienteExistente.getId(), nuevoNombre != null ? nuevoNombre : clienteExistente.getNombre(), nuevoTelefono != null ? nuevoTelefono : clienteExistente.getTelefono(), nuevoEmail != null ? nuevoEmail : clienteExistente.getEmail());
                
                crud.actualizar(clienteActualizado);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún cliente con ese nombre");
            }
        }
    }
    
    private static void eliminarCliente() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del cliente a eliminar:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            // Convert String to int for ID
            int clienteId = Integer.parseInt(nombre);
            Cliente cliente = crud.buscar(clienteId);
            if (cliente != null) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Está seguro de eliminar al cliente " + cliente.getNombre() + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    crud.eliminar(clienteId);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún cliente con ese nombre");
            }
        }
    }
}
