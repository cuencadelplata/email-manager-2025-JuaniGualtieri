package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import com.example.Filtros.Filtro;

// RF05 – Filtros personalizados con programación funcional (Predicate<Email>)
class RF05_FiltrosPersonalizadosTest {

    @Test
    void filtrosSimplesYCombinados() {
        // Store + servicio (no usamos bandejas acá, pero mantenemos el patrón)
        InMemoryEmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        // Contactos: dos remitentes UCP, uno de Gmail y "yo" como destinatario
        Contacto u1 = new Contacto("Admin UCP", "admin@ucp.edu.ar");
        Contacto u2 = new Contacto("RRHH UCP", "rrhh@ucp.edu.ar");
        Contacto otro = new Contacto("Google", "no-reply@gmail.com");
        Contacto me = new Contacto("Yo", "yo@demo.com");

        // Tres correos con distintas combinaciones de remitente/asunto/contenido
        Email e1 = svc.crear("Importante: Inscripción", "Fechas UCP", u1, List.of(me));
        Email e2 = svc.crear("Aviso", "UCP informa novedades", u2, List.of(me));
        Email e3 = svc.crear("Promo", "Ofertas semanales", otro, List.of(me));

        // Lista "universo" sobre la que vamos a aplicar filtros dinámicamente
        List<Email> universo = List.of(e1, e2, e3);

        // Filtro simple: remitente de dominio ucp.edu.ar
        Filtro deUcp = Filtro.remitenteDominio("ucp.edu.ar");
        // Filtro simple: asunto contiene "Importante"
        Filtro asuntoImportante = Filtro.asuntoContiene("Importante");
        // Filtro simple: contenido contiene "ofertas"
        Filtro contenidoOfertas = Filtro.contenidoContiene("ofertas");

        // Filtro deUcp: deben quedar e1 y e2
        var soloUcp = deUcp.aplicar(universo);
        assertEquals(2, soloUcp.size());

        // Filtro combinado AND: deUcp AND asuntoImportante → solo e1
        var ucpEImportante = deUcp.and(asuntoImportante).aplicar(universo);
        assertEquals(1, ucpEImportante.size());
        assertTrue(ucpEImportante.contains(e1));

        // Filtro negado NOT: correos que NO sean de UCP → e3
        var noUcp = deUcp.not().aplicar(universo);
        assertEquals(1, noUcp.size());
        assertTrue(noUcp.contains(e3));

        // Filtro OR: deUcp OR contenidoOfertas
        // e1 y e2 entran por ser UCP, e3 entra por "Ofertas"
        var ucpOOfertas = deUcp.or(contenidoOfertas).aplicar(universo);
        assertEquals(3, ucpOOfertas.size()); // e1,e2 (ucp) + e3 (ofertas)
    }

    @Test
    void filtroPorDominioDeDestinatario() {
        // Remitente y dos destinatarios de dominios distintos
        Contacto me = new Contacto("Yo", "yo@demo.com");
        Contacto ana = new Contacto("Ana", "ana@ucp.edu.ar");
        Contacto bob = new Contacto("Bob", "bob@otro.com");

        // e1: destinataria Ana -> dominio @ucp.edu.ar
        Email e1 = new Email("Hola UCP", "Texto", me);
        e1.agregarDestinatario(ana);

        // e2: destinatario Bob -> otro dominio
        Email e2 = new Email("Hola Otro", "Texto", me);
        e2.agregarDestinatario(bob);

        // Filtro por dominio de los destinatarios: @ucp.edu.ar
        var filtroDestUcp = Filtro.destinatarioDominio("ucp.edu.ar");

        // Al aplicar el filtro solo debe quedar el correo que tenga un destinatario de UCP
        var res = filtroDestUcp.aplicar(List.of(e1, e2));
        assertEquals(1, res.size());
        assertTrue(res.contains(e1));
    }
}
