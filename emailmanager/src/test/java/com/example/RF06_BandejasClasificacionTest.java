package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

// RF06 – Bandejas y clasificación (mover y restaurar correos)
class RF06_BandejasClasificacionTest {

    @Test
    void moverYRestaurarCorreos() {
        // Store y servicio
        InMemoryEmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        // Remitente y destinatario
        Contacto a = new Contacto("A","a@demo.com");
        Contacto b = new Contacto("B","b@demo.com");

        // Creamos un email y lo recibimos en ENTRADA
        Email e = svc.crear("Asunto", "Texto", a, List.of(b));
        svc.recibirEnEntrada(e);
        assertEquals(1, store.bandeja(Bandeja.ENTRADA).size());

        // Movemos el correo de ENTRADA a ELIMINADOS
        svc.mover(Bandeja.ENTRADA, Bandeja.ELIMINADOS, e);
        assertEquals(0, store.bandeja(Bandeja.ENTRADA).size());
        assertEquals(1, store.bandeja(Bandeja.ELIMINADOS).size());

        // Restauramos el correo desde ELIMINADOS a ENTRADA
        svc.restaurarDeEliminados(e);
        assertEquals(1, store.bandeja(Bandeja.ENTRADA).size());
        assertEquals(0, store.bandeja(Bandeja.ELIMINADOS).size());
    }
}
