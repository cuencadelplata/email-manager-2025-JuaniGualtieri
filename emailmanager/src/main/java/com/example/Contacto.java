package com.example;

import java.util.Objects;

/**
 * Representa un contacto dentro del sistema.
 * Se usa como remitente o como destinatario.
 * La identidad del contacto está definida por su email.
 */
public class Contacto {

    private String nombre;
    private String email;

    /**
     * Constructor con validaciones delegadas a los setters.
     */
    public Contacto(String nombre, String email) {
        setNombre(nombre);
        setEmail(email);
    }

    public String getNombre() { return nombre; }
    public String getEmail()  { return email; }

    /**
     * Valida que el nombre no sea nulo ni vacío.
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        this.nombre = nombre;
    }

    /**
     * Valida que el email sea válido y lo normaliza a minúsculas.
     */
    public void setEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@"))
            throw new IllegalArgumentException("Email inválido");
        this.email = email.toLowerCase();
    }

    /**
     * Dos contactos son iguales si tienen el mismo email.
     */
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contacto)) return false;
        return Objects.equals(email, ((Contacto) o).email);
    }

    @Override public int hashCode() { return Objects.hash(email); }

    /**
     * Representación legible: "Nombre <email>".
     */
    @Override public String toString() { return nombre + " <" + email + ">"; }
}
