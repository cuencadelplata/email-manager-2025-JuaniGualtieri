package com.example.Interfaces;

import com.example.Bandeja;
import com.example.Email;

import java.util.List;

public interface EmailStore {
    List<Email> bandeja(Bandeja tipo);      // devolver la lista viva de esa bandeja
    void agregar(Bandeja tipo, Email e);
    boolean remover(Bandeja tipo, Email e);
}
