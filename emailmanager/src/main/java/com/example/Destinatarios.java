package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * Value Object que encapsula la lista de destinatarios de un email.
 * Evita duplicados, valida datos y centraliza la lógica asociada.
 */
public class Destinatarios {

    // Lista interna protegida. Nunca se expone directamente.
    private final List<Contacto> lista = new ArrayList<>();

    /**
     * Agrega un destinatario si no es nulo y no está duplicado.
     * Duplicados se evalúan por equals → email igual.
     */
    public void agregar(Contacto c) {
        if (c == null) throw new IllegalArgumentException("Destinatario nulo");
        if (!lista.contains(c)) lista.add(c);
    }

    /** Indica si no hay destinatarios. */
    public boolean esVacia() { return lista.isEmpty(); }

    /** Devuelve una vista inmutable de los destinatarios. */
    public List<Contacto> ver() { return Collections.unmodifiableList(lista); }

    /**
     * Evalúa si algún destinatario cumple una condición específica.
     * Se usa principalmente en filtros de dominio de destinatarios.
     */
    public boolean algunoCumple(Predicate<Contacto> p) {
        for (Contacto c : lista)
            if (p.test(c)) return true;
        return false;
    }
}
