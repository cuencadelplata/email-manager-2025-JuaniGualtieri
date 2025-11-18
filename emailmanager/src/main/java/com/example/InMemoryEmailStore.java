package com.example;

import com.example.Interfaces.EmailStore;

import java.util.*;

/**
 * Implementación de EmailStore en memoria.
 * Usa un EnumMap para almacenar listas separadas por bandeja.
 */
public class InMemoryEmailStore implements EmailStore {

    private final Map<Bandeja, List<Email>> porBandeja = new EnumMap<>(Bandeja.class);

    /** Inicializa cada bandeja con una lista vacía. */
    public InMemoryEmailStore() {
        for (Bandeja b : Bandeja.values())
            porBandeja.put(b, new ArrayList<>());
    }

    @Override
    public List<Email> bandeja(Bandeja tipo) {
        return porBandeja.get(tipo);
    }

    @Override
    public void agregar(Bandeja tipo, Email e) {
        porBandeja.get(tipo).add(e);
    }

    @Override
    public boolean remover(Bandeja tipo, Email e) {
        return porBandeja.get(tipo).remove(e);
    }
}
