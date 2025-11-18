package com.example.Filtros;

import com.example.Email;
import com.example.Interfaces.SearchSpecification;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Clase funcional utilizada para RF05.
 * Representa un filtro compuesto por:
 *  - un nombre descriptivo
 *  - un criterio funcional (Predicate<Email>)
 * Implementa SearchSpecification para integrarse con EmailService.
 */
public class Filtro implements SearchSpecification {

    private final String nombre;
    private final Predicate<Email> criterio;

    /**
     * Constructor que valida nombre y criterio.
     */
    public Filtro(String nombre, Predicate<Email> criterio) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Nombre de filtro vacío");
        if (criterio == null)
            throw new IllegalArgumentException("Criterio nulo");

        this.nombre = nombre;
        this.criterio = criterio;
    }

    public String getNombre() { return nombre; }

    /** Implementación de SearchSpecification. */
    @Override
    public boolean matches(Email e) {
        return criterio.test(e);
    }

    /** Aplica el filtro sobre una lista arbitraria de correos. */
    public List<Email> aplicar(List<Email> emails) {
        return emails.stream().filter(criterio).collect(Collectors.toList());
    }


    /** Filtro compuesto: AND */
    public Filtro and(Filtro otro) {
        return new Filtro(this.nombre + " AND " + otro.nombre,
                this.criterio.and(otro.criterio));
    }

    /** Filtro compuesto: OR */
    public Filtro or(Filtro otro) {
        return new Filtro(this.nombre + " OR " + otro.nombre,
                this.criterio.or(otro.criterio));
    }

    /** Filtro negado: NOT */
    public Filtro not() {
        return new Filtro("NOT " + this.nombre, this.criterio.negate());
    }

    

    public static Filtro asuntoContiene(String texto) {
        String t = texto == null ? "" : texto.toLowerCase();
        return new Filtro("Asunto~'" + texto + "'",
                e -> e.getAsunto().toLowerCase().contains(t));
    }

    public static Filtro contenidoContiene(String texto) {
        String t = texto == null ? "" : texto.toLowerCase();
        return new Filtro("Contenido~'" + texto + "'",
                e -> e.getContenido().toLowerCase().contains(t));
    }

    public static Filtro remitenteDominio(String dominio) {
        String d = normalizarDominio(dominio);
        return new Filtro("Remitente@" + d,
                e -> e.getRemitente().getEmail().toLowerCase().endsWith(d));
    }

    public static Filtro destinatarioDominio(String dominio) {
        String d = normalizarDominio(dominio);
        return new Filtro("Para@" + d,
                e -> e.getDestinatarios().algunoCumple(
                        c -> c.getEmail().toLowerCase().endsWith(d)
                ));
    }

    /** Normaliza dominios y contempla casos nulos o vacíos. */
    private static String normalizarDominio(String dominio) {
        if (dominio == null || dominio.isBlank()) return "";
        String d = dominio.toLowerCase();
        return d.startsWith("@") ? d : "@" + d;
    }
}
