package com.example;

/**
 * Enum que representa las distintas bandejas donde puede estar un correo.
 * Se utiliza como clave en el EmailStore.
 */
public enum Bandeja {
    ENTRADA,       // Correos recibidos
    ENVIADOS,      // Correos enviados por el usuario
    BORRADORES,    // Correos guardados sin enviar
    ELIMINADOS     // Correos eliminados (papelera)
}
