package com.mycompany.entradasya2.test;

import com.mycompany.entradasya2.eventos.Evento;
import com.mycompany.entradasya2.eventos.EventoCRUD;
import java.util.List;

public class TestBuscarEvento {
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de búsqueda de eventos...");
        
        EventoCRUD eventoCRUD = new EventoCRUD();
        String criterio = "hola";
        
        System.out.println("Buscando eventos con criterio: " + criterio);
        List<Evento> eventos = eventoCRUD.buscar(criterio);
        
        System.out.println("\nResultados encontrados: " + eventos.size());
        
        if (eventos.isEmpty()) {
            System.out.println("No se encontraron eventos.");
        } else {
            System.out.println("\nListado de eventos encontrados:");
            for (Evento evento : eventos) {
                System.out.println("\n--- Evento ---");
                System.out.println("ID: " + evento.getId());
                System.out.println("Título: " + evento.getTitulo());
                System.out.println("Descripción: " + evento.getDescripcion());
                System.out.println("Ubicación: " + evento.getUbicacion());
                System.out.println("Fecha: " + evento.getFechaEvento());
                System.out.println("Capacidad: " + evento.getCapacidadTotal());
                System.out.println("Precio: $" + evento.getPrecio());
                System.out.println("Estado: " + evento.getEstado());
            }
        }
    }
}
