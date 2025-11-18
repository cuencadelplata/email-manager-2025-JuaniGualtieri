package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

// RF02 – Envío de correos
class RF02_EnvioCorreosTest {

    @Test
    void alEnviarCorreoDebeIrABandejaEnviados() {
        // Store en memoria para las bandejas
        InMemoryEmailStore store = new InMemoryEmailStore();
        // Servicio principal, con el store inyectado
        EmailService svc = new EmailService(store);

        // Remitente y destinatario
        Contacto juan = new Contacto("Juan","juan@demo.com");
        Contacto ana  = new Contacto("Ana", "ana@demo.com");

        // Creamos el email
        Email e = svc.crear("Prueba", "Texto", juan, List.of(ana));

        // Acción principal del RF02: enviar el correo
        svc.enviar(e);

        // Verificamos que el correo se encuentre en la bandeja ENVIADOS
        assertEquals(1, store.bandeja(Bandeja.ENVIADOS).size());
        assertSame(e, store.bandeja(Bandeja.ENVIADOS).get(0));
    }

    @Test
    void enviarSinDestinatariosLanzaError() {
        // Store y servicio
        InMemoryEmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        // Email sin destinatarios (lista vacía)
        Email e = svc.crear("Asunto", "Texto", new Contacto("J","j@demo.com"), List.of());

        // Regla de negocio: no se puede enviar un mail sin destinatarios → lanza excepción
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> svc.enviar(e));

        // Verificamos que el mensaje mencione el problema de "al menos un destinatario"
        assertTrue(ex.getMessage().toLowerCase().contains("al menos un destinatario"));
    }
}
