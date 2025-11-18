package com.example;

import com.example.Interfaces.ContactRepository;

import java.util.*;

/**
 * Implementación en memoria del repositorio de contactos.
 * Clave principal = email del contacto.
 */
public class InMemoryContactRepository implements ContactRepository {

    private final Map<String, Contacto> porEmail = new HashMap<>();

    @Override
    public void agregar(Contacto c) {
        if (c == null) throw new IllegalArgumentException("Contacto nulo");
        porEmail.put(c.getEmail(), c); // Sobrescribe si ya existe el email
    }

    @Override
    public void editar(String emailClave, String nuevoNombre, String nuevoEmail) {
        Contacto actual = porEmail.get(emailClave == null ? "" : emailClave.toLowerCase());
        if (actual == null)
            throw new NoSuchElementException("No existe: " + emailClave);

        // Actualiza nombre si es válido
        if (nuevoNombre != null && !nuevoNombre.isBlank())
            actual.setNombre(nuevoNombre);

        // Actualiza email y cambia la clave del mapa
        if (nuevoEmail != null && !nuevoEmail.isBlank() && !nuevoEmail.equalsIgnoreCase(emailClave)) {
            porEmail.remove(emailClave.toLowerCase());
            actual.setEmail(nuevoEmail);
            porEmail.put(actual.getEmail(), actual);
        }
    }

    @Override
    public void eliminar(String email) {
        if (email != null)
            porEmail.remove(email.toLowerCase()); // Eliminar inexistente NO falla
    }

    @Override
    public Optional<Contacto> porEmail(String email) {
        if (email == null) return Optional.empty();
        return Optional.ofNullable(porEmail.get(email.toLowerCase()));
    }

    @Override
    public List<Contacto> listar() {
        return new ArrayList<>(porEmail.values());
    }

    @Override
    public int cantidad() { return porEmail.size(); }
}
