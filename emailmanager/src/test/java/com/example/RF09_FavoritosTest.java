package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import com.example.Interfaces.SearchSpecification;

// RF09 – Marcar como favorito y buscar favoritos
class RF09_FavoritosTest {

    @Test
    void marcarFavoritoYBuscarFavoritosEnEntrada() {
        // Store en memoria y servicio
        InMemoryEmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        // Remitente y dos destinatarios
        Contacto r = new Contacto("R","r@demo.com");
        Contacto a = new Contacto("A","a@demo.com");
        Contacto b = new Contacto("B","b@demo.com");

        // Creamos dos correos distintos
        Email e1 = svc.crear("UCP", "Info", r, List.of(a));
        Email e2 = svc.crear("General", "Mensaje", r, List.of(b));

        // Ambos llegan a la bandeja ENTRADA
        svc.recibirEnEntrada(e1);
        svc.recibirEnEntrada(e2);

        // Marcamos SOLO e1 como favorito
        svc.marcarFavorito(e1, true);

        // e1 favorito, e2 no
        assertTrue(e1.isFavorito());
        assertFalse(e2.isFavorito());

        // Definimos una SearchSpecification funcional
        // que devuelve true SOLO para correos favoritos
        SearchSpecification soloFavoritos = Email::isFavorito;

        // Buscamos en ENTRADA usando la especificación anterior
        var favoritos = svc.buscarEnEntrada(soloFavoritos);

        // Debe haber exactamente un favorito
        assertEquals(1, favoritos.size());
        // Y ese favorito tiene que ser e1
        assertSame(e1, favoritos.get(0));
    }
}
