package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

// RF08 – Marcar como leído / no leído
class RF08_LeidoNoLeidoTest {

    @Test
    void marcarComoLeidoYNoLeido() {
        // Servicio con store en memoria
        EmailService svc = new EmailService(new InMemoryEmailStore()); // se crea el servicio y se inyecta store en memoria

        // Creamos un email (remitente + 1 destinatario)
        Email e = svc.crear(
                "Asunto",
                "Contenido",
                new Contacto("J","j@demo.com"),
                List.of(new Contacto("A","a@demo.com"))
        );

        // Al crear el email, por defecto debe estar NO leído
        assertFalse(e.isLeido());

        // Lo marcamos como leído
        svc.marcarLeido(e, true);
        assertTrue(e.isLeido());

        // Lo volvemos a marcar como NO leído
        svc.marcarLeido(e, false);
        assertFalse(e.isLeido());
    }
}
