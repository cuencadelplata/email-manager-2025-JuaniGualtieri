package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class RF04_BusquedaTextoLibreTest {

    @Test
    void buscarEnEntradaPorTextoLibre() {
        InMemoryEmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        Contacto ucp = new Contacto("Soporte UCP", "soporte@ucp.edu.ar");
        Contacto prof = new Contacto("Profesor", "jose@ucp.edu.ar");
        Contacto alumno = new Contacto("Alumno", "alumno@demo.com");

        Email e1 = svc.crear("Prueba UCP", "Estamos en Paradigmas 2", ucp, List.of(alumno));
        Email e2 = svc.crear("Recordatorio", "Examen parcial", prof, List.of(alumno));
        Email e3 = svc.crear("Hola", "Mensaje cualquiera", alumno, List.of(prof));

        svc.recibirEnEntrada(e1);
        svc.recibirEnEntrada(e2);
        svc.recibirEnEntrada(e3);

        var porUcp = svc.buscarEnEntradaPorTexto("UCP");
        assertEquals(2, porUcp.size());
        assertTrue(porUcp.contains(e1));
        assertTrue(porUcp.contains(e2));

        var porAlumno = svc.buscarEnEntradaPorTexto("alumno@demo.com");
        assertEquals(2, porAlumno.size()); // aparece como destinatario y remitente en distintos correos
    }
}
