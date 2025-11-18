package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

// RF04 – Búsqueda de correos en entrada por texto libre
class RF04_BusquedaTextoLibreTest {

    @Test
    void buscarEnEntradaPorTextoLibre() {
        // Store y servicio
        InMemoryEmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        // Contactos: dos de UCP y un alumno
        Contacto ucp = new Contacto("Soporte UCP", "soporte@ucp.edu.ar");
        Contacto prof = new Contacto("Profesor", "jose@ucp.edu.ar");
        Contacto alumno = new Contacto("Alumno", "alumno@demo.com");

        // Tres correos con distintas combinaciones de remitente/destinatarios
        Email e1 = svc.crear("Prueba UCP", "Estamos en Paradigmas 2", ucp, List.of(alumno));
        Email e2 = svc.crear("Recordatorio", "Examen parcial", prof, List.of(alumno));
        Email e3 = svc.crear("Hola", "Mensaje cualquiera", alumno, List.of(prof));

        // Simulamos que llegan a la bandeja ENTRADA
        svc.recibirEnEntrada(e1);
        svc.recibirEnEntrada(e2);
        svc.recibirEnEntrada(e3);

        //  búsqueda por “UCP” 
        // El sistema busca en asunto, contenido, remitente y destinatarios
        var porUcp = svc.buscarEnEntradaPorTexto("UCP");
        assertEquals(3, porUcp.size());
        assertTrue(porUcp.contains(e1));
        assertTrue(porUcp.contains(e2));
        assertTrue(porUcp.contains(e3));

        //  búsqueda por alumno@demo.com 
        // El alumno aparece como remitente y destinatario en distintos correos
        var porAlumno = svc.buscarEnEntradaPorTexto("alumno@demo.com");
        assertEquals(3, porAlumno.size());
        assertTrue(porAlumno.contains(e1));
        assertTrue(porAlumno.contains(e2));
        assertTrue(porAlumno.contains(e3));
    }
}
