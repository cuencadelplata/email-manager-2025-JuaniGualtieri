package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.Interfaces.ContactRepository;

// RF03 – Gestión de contactos (crear, editar, eliminar)
class RF03_ContactosTest {

    @Test
    void crearEditarEliminarContactos() {
        // Repositorio de contactos en memoria
        ContactRepository repo = new InMemoryContactRepository();

        // Creamos y agregamos un contacto
        Contacto a = new Contacto("Ana", "ana@demo.com");
        repo.agregar(a);

        // Verificamos que se haya guardado correctamente
        assertEquals(1, repo.cantidad());
        assertTrue(repo.porEmail("ana@demo.com").isPresent());

        // Editamos nombre y email (cambiando también la "clave" de búsqueda)
        repo.editar("ana@demo.com", "Ana Maria", "ana.m@demo.com");

        // El email viejo ya no debe existir
        assertTrue(repo.porEmail("ana@demo.com").isEmpty());

        // El nuevo email debe existir y tener el nombre actualizado
        assertEquals("Ana Maria", repo.porEmail("ana.m@demo.com").get().getNombre());

        // Eliminamos el contacto
        repo.eliminar("ana.m@demo.com");

        // Verificamos que el repositorio queda vacío
        assertEquals(0, repo.cantidad());
    }
}
