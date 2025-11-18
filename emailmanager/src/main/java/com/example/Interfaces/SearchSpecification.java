package com.example.Interfaces;

import com.example.Email;

/**
 * Interfaz funcional para criterios de búsqueda.
 * Permite implementar búsquedas dinámicas → RF04 y RF09.
 */
public interface SearchSpecification {

    /** Devuelve true si el email cumple la condición implementada. */
    boolean matches(Email e);
}
