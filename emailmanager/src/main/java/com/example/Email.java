package com.example;

import java.util.Objects;

/**
 * Representa un correo electrónico dentro del sistema.
 * Contiene remitente, destinatarios, asunto, contenido y estados (leído/favorito).
 */
public class Email {

    private String asunto;
    private String contenido;
    private Contacto remitente;
    private final Destinatarios destinatarios = new Destinatarios();

    private boolean leido;
    private boolean favorito;

    /**
     * Constructor que normaliza valores nulos y valida el remitente.
     */
    public Email(String asunto, String contenido, Contacto remitente) {
        this.asunto = Objects.requireNonNullElse(asunto, "");
        this.contenido = Objects.requireNonNullElse(contenido, "");
        setRemitente(remitente);
    }

    // Getters
    public String getAsunto() { return asunto; }
    public String getContenido() { return contenido; }
    public Contacto getRemitente() { return remitente; }
    public Destinatarios getDestinatarios() { return destinatarios; }
    public boolean isLeido() { return leido; }
    public boolean isFavorito() { return favorito; }

    // Setters con normalización
    public void setAsunto(String asunto) { this.asunto = (asunto == null ? "" : asunto); }
    public void setContenido(String texto) { this.contenido = (texto == null ? "" : texto); }

    /**
     * Valida remitente obligatorio.
     */
    public void setRemitente(Contacto remitente) {
        if (remitente == null)
            throw new IllegalArgumentException("Remitente obligatorio");
        this.remitente = remitente;
    }

    /** Agrega destinatarios delegando validación a Destinatarios. */
    public void agregarDestinatario(Contacto c) { destinatarios.agregar(c); }

    /** Marca el email como leído/no leído. */
    public void marcarLeido(boolean v) { this.leido = v; }

    /** Marca el email como favorito/no favorito. */
    public void marcarFavorito(boolean v) { this.favorito = v; }

    /**
     * Representación legible del correo.
     */
    @Override
    public String toString() {
        return "Email{asunto='" + asunto + "', de=" + remitente
                + ", para=" + destinatarios.ver() + "}";
    }
}
