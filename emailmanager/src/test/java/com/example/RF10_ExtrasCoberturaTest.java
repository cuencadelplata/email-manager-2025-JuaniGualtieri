package com.example;

import com.example.Filtros.Filtro;
import com.example.Interfaces.ContactRepository;
import com.example.Interfaces.EmailStore;
import com.example.Interfaces.SearchSpecification;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// RF10 – Tests extra para aumentar la cobertura (Filtro, Contacto, Repositorios, etc.)
class RF10_ExtrasCoberturaTest {

    // =========================
    //  Filtro (clase funcional)
    // =========================

    @Test
    void filtroMatchesDebeUsarElCriterio() {
        Contacto remitente = new Contacto("Juan", "juan@demo.com");
        Email e = new Email("Hola mundo", "contenido", remitente);

        // Filtro cuyo criterio debe matchear el asunto
        Filtro filtro = Filtro.asuntoContiene("Hola");
        assertTrue(filtro.matches(e));

        // En este caso no debería matchear
        Filtro filtroNo = Filtro.asuntoContiene("XYZ");
        assertFalse(filtroNo.matches(e));
    }

    @Test
    void filtroConstructorDebeValidarNombreYCriterio() {
        // Nombre vacío -> error
        assertThrows(IllegalArgumentException.class,
                () -> new Filtro("", email -> true));

        // Criterio null -> error
        assertThrows(IllegalArgumentException.class,
                () -> new Filtro("Filtro sin criterio", null));
    }

    @Test
    void filtroNormalizarDominioRamasDistintas() {
        Contacto c1 = new Contacto("A", "a@ucp.edu.ar");
        Contacto c2 = new Contacto("B", "b@gmail.com");

        Email e1 = new Email("x", "y", c1);
        Email e2 = new Email("x", "y", c2);

        // dominio sin @  -> el filtro lo normaliza agregando @
        Filtro f1 = Filtro.remitenteDominio("ucp.edu.ar");
        assertTrue(f1.matches(e1));
        assertFalse(f1.matches(e2));

        // dominio con @ -> lo deja igual
        Filtro f2 = Filtro.remitenteDominio("@gmail.com");
        assertTrue(f2.matches(e2));
        assertFalse(f2.matches(e1));

        // dominio nulo / vacío -> filtro trivial (siempre true)
        Filtro f3 = Filtro.remitenteDominio(null);
        assertTrue(f3.matches(e1));
        assertTrue(f3.matches(e2));
    }

    // =========================
    //  Contacto
    // =========================

    @Test
    void contactoEqualsHashCodeYToString() {
        Contacto c1 = new Contacto("Ana", "ana@demo.com");
        Contacto c2 = new Contacto("Ana Maria", "ana@demo.com"); // mismo mail
        Contacto c3 = new Contacto("Ana", "otra@demo.com");

        // equals basado en email
        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, "otra cosa");

        // hashCode consistente con equals
        assertEquals(c1.hashCode(), c2.hashCode());

        // toString debe contener nombre y email
        String texto = c1.toString();
        assertTrue(texto.contains("Ana"));
        assertTrue(texto.contains("ana@demo.com"));
    }

    @Test
    void contactoSettersValidanDatos() {
        Contacto c = new Contacto("Juan", "juan@demo.com");

        c.setNombre("Juan Carlos");
        assertEquals("Juan Carlos", c.getNombre());

        c.setEmail("nuevo@demo.com");
        assertEquals("nuevo@demo.com", c.getEmail());

        // Casos inválidos
        assertThrows(IllegalArgumentException.class, () -> c.setNombre("   "));
        assertThrows(IllegalArgumentException.class, () -> c.setEmail("sin-arroba"));
        assertThrows(IllegalArgumentException.class, () -> c.setEmail("   "));
    }

    // =========================
    //  InMemoryContactRepository
    // =========================

    @Test
    void contactRepositoryListarYPorEmailRamas() {
        ContactRepository repo = new InMemoryContactRepository();

        Contacto a = new Contacto("Ana", "ana@demo.com");
        Contacto b = new Contacto("Bob", "bob@demo.com");
        repo.agregar(a);
        repo.agregar(b);

        // listar()
        List<Contacto> todos = repo.listar();
        assertEquals(2, todos.size());
        assertTrue(todos.contains(a));
        assertTrue(todos.contains(b));

        // porEmail existente
        Optional<Contacto> ana = repo.porEmail("ana@demo.com");
        assertTrue(ana.isPresent());

        // porEmail inexistente
        assertTrue(repo.porEmail("noexiste@demo.com").isEmpty());

        // porEmail null
        assertTrue(repo.porEmail(null).isEmpty());
    }

    @Test
    void contactRepositoryEditarYEliminarRamasExtra() {
        ContactRepository repo = new InMemoryContactRepository();
        Contacto c = new Contacto("Pepe", "pepe@demo.com");
        repo.agregar(c);

        // editar solo nombre
        repo.editar("pepe@demo.com", "Jose", null);
        assertEquals("Jose", repo.porEmail("pepe@demo.com").get().getNombre());

        // editar solo email
        repo.editar("pepe@demo.com", null, "jose@demo.com");
        assertTrue(repo.porEmail("pepe@demo.com").isEmpty());
        assertTrue(repo.porEmail("jose@demo.com").isPresent());

        // eliminar inexistente (no debe explotar)
        repo.eliminar("noexiste@demo.com");
        assertEquals(1, repo.cantidad());

        // editar contacto inexistente -> excepción
        assertThrows(Exception.class,
                () -> repo.editar("inexistente@demo.com", "X", "Y"));
    }

    @Test
    void contactRepositoryAgregarMismoEmailSobrescribe() {
        ContactRepository repo = new InMemoryContactRepository();

        Contacto c1 = new Contacto("Nombre1", "igual@demo.com");
        Contacto c2 = new Contacto("Nombre2", "igual@demo.com");

        // Al agregar dos veces el mismo email, el segundo reemplaza al primero
        repo.agregar(c1);
        repo.agregar(c2);

        assertEquals(1, repo.cantidad());
        assertEquals("Nombre2", repo.porEmail("igual@demo.com").get().getNombre());
    }

    // =========================
    //  Email
    // =========================

    @Test
    void emailSettersYToString() {
        Contacto remitente = new Contacto("Rem", "rem@demo.com");
        Email e = new Email("Asunto", "Contenido", remitente);

        // Null en asunto/contenido se normaliza a "" (string vacío)
        e.setAsunto(null);
        e.setContenido(null);
        assertEquals("", e.getAsunto());
        assertEquals("", e.getContenido());

        // Cambiamos remitente por uno válido
        Contacto nuevo = new Contacto("Nuevo", "nuevo@demo.com");
        e.setRemitente(nuevo);
        assertEquals(nuevo, e.getRemitente());

        // setRemitente null -> debe lanzar excepción
        assertThrows(IllegalArgumentException.class, () -> e.setRemitente(null));

        // toString no debe romper y debe mencionar la palabra "Email"
        String s = e.toString();
        assertTrue(s.contains("Email"));
    }

    // =========================
    //  Destinatarios
    // =========================

    @Test
    void destinatariosEsVaciaYNoDuplica() {
        Destinatarios dest = new Destinatarios();

        // Lista vacía al inicio
        assertTrue(dest.esVacia());

        Contacto a = new Contacto("Ana", "ana@demo.com");
        dest.agregar(a);
        assertFalse(dest.esVacia());
        assertEquals(1, dest.ver().size());

        // Agregar el mismo contacto no debe duplicarlo
        dest.agregar(a);
        assertEquals(1, dest.ver().size());

        // Agregar null debe lanzar excepción
        assertThrows(IllegalArgumentException.class, () -> dest.agregar(null));
    }

    // =========================
    //  EmailService - ramas de búsqueda
    // =========================

    @Test
    void emailServiceBuscarEnEntradaPorTextoDebeManejarNullYBlanco() {
        EmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        // Texto null -> lista vacía
        assertTrue(svc.buscarEnEntradaPorTexto(null).isEmpty());

        // Texto en blanco -> lista vacía
        assertTrue(svc.buscarEnEntradaPorTexto("   ").isEmpty());
    }

    @Test
    void emailServiceBuscarEnEntradaConSpecificationVacia() {
        EmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        // Preparamos un correo en ENTRADA
        Contacto c = new Contacto("A", "a@demo.com");
        Email e1 = svc.crear("x", "y", c, List.of());
        svc.recibirEnEntrada(e1);

        // specification que nunca matchea → resultado vacío
        SearchSpecification specFalse = email -> false;
        assertTrue(svc.buscarEnEntrada(specFalse).isEmpty());

        // specification que siempre matchea -> debe devolver 1 correo
        SearchSpecification specTrue = email -> true;
        assertEquals(1, svc.buscarEnEntrada(specTrue).size());
    }
}
