package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

// RF01 – Creación de correos
class RF01_CreacionCorreoTest {

    @Test
    void crearCorreoConRemitenteYVariosDestinatarios() {
        // Servicio de emails con un store en memoria (simula las bandejas)
        EmailService svc = new EmailService(new InMemoryEmailStore());

        // Creamos remitente y dos destinatarios
        Contacto remitente = new Contacto("Juan", "juan@demo.com");
        Contacto ana = new Contacto("Ana", "ana@demo.com");
        Contacto nico = new Contacto("Nico", "nico@demo.com");

        // Creamos el email a través del servicio
        Email e = svc.crear("Asunto X", "Contenido Y", remitente, List.of(ana, nico));

        // Verificamos que los datos del correo coinciden
        assertEquals("Asunto X", e.getAsunto());
        assertEquals("Contenido Y", e.getContenido());
        assertEquals(remitente, e.getRemitente());

        // Verificamos que haya 2 destinatarios y que sean Ana y Nico
        assertEquals(2, e.getDestinatarios().ver().size());
        assertTrue(e.getDestinatarios().ver().contains(ana));
        assertTrue(e.getDestinatarios().ver().contains(nico));
    }

    @Test
    void alCrearContenidoNuloSeNormalizaAStringVacio() {
        // Servicio de emails
        EmailService svc = new EmailService(new InMemoryEmailStore());

        // Creamos un email con contenido null
        Email e = svc.crear("Hola", null, new Contacto("A","a@demo.com"), List.of());

        // La lógica interna debe convertir null en "" (string vacío)
        assertEquals("", e.getContenido());
    } 
}
