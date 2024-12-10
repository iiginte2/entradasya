package com.mycompany.entradasya2;

import com.mycompany.entradasya2.usuario.CConnection;

public class TestConnection {
    public static void main(String[] args) {
        // Test the database connection
        if (CConnection.testConnection()) {
            System.out.println("Conexión exitosa a la base de datos");
        } else {
            System.err.println("Error al conectar a la base de datos");
        }
    }
}
