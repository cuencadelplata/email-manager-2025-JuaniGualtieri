package com.example.Interfaces;

import com.example.Bandeja;
import com.example.Email;

import java.util.List;

/**
 * Abstracción del almacenamiento de correos.
 * Permite cambiar entre almacenamiento en memoria, BD, archivos, etc.
 * Implementa el principio SOLID → Inversión de Dependencias.
 */
public interface EmailStore {

    /** Devuelve la lista viva de correos en una bandeja. */
    List<Email> bandeja(Bandeja tipo);

    /** Agrega un correo a la bandeja indicada. */
    void agregar(Bandeja tipo, Email e);

    /** Remueve un correo de la bandeja y devuelve si realmente estaba. */
    boolean remover(Bandeja tipo, Email e);
}
