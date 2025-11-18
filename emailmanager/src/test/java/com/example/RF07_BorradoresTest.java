package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

// RF07 – Borradores (guardar, editar y luego enviar)
class RF07_BorradoresTest {

    @Test
    void guardarEditarYEnviarBorrador() {
        // Store y servicio
        InMemoryEmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        // Remitente y destinatario
        Contacto remitente = new Contacto("Yo","yo@demo.com");
        Contacto ana = new Contacto("Ana","ana@demo.com");

        // Creamos un borrador
        Email borrador = svc.crear("Borrador inicial", "Texto", remitente, List.of(ana));

        // Lo guardamos en la bandeja BORRADORES
        svc.guardarBorrador(borrador);
        assertEquals(1, store.bandeja(Bandeja.BORRADORES).size());

        // Editamos el asunto y contenido del borrador
        borrador.setAsunto("Borrador final");
        borrador.setContenido("Texto actualizado");

        // Enviamos el borrador
        svc.enviar(borrador);

        // Verificamos que está en ENVIADOS
        assertEquals(1, store.bandeja(Bandeja.ENVIADOS).size());
    }
}
