package model;

import java.util.LinkedList;

public class Cliente {
    private String id;
    private String nombre;
    private String email;
    private String celular;

    private LinkedList<Cuenta> ListaCuentas;
    public Cliente(String id, String nombre, String email, String celular) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.celular = celular;
        this.ListaCuentas = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public LinkedList<Cuenta> getListaCuentas() {
        return ListaCuentas;
    }

    public void setListaCuentas(LinkedList<Cuenta> listaCuentas) {
        ListaCuentas = listaCuentas;
    }
}
