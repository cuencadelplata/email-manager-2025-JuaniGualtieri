package com.example.Interfaces;

import com.example.Contacto;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del repositorio de contactos.
 * Permite agregar, editar, eliminar y buscar contactos.
 */
public interface ContactRepository {

    void agregar(Contacto c);

    /**
     * Edita un contacto identificado por su email clave.
     * Puede modificar nombre y/o email.
     */
    void editar(String emailClave, String nuevoNombre, String nuevoEmail);

    /** Elimina un contacto según email. Si no existe, no falla. */
    void eliminar(String email);

    /** Busca un contacto por email. */
    Optional<Contacto> porEmail(String email);

    /** Devuelve todos los contactos. */
    List<Contacto> listar();

    /** Cantidad total almacenada. */
    int cantidad();
}
