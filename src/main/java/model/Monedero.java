package model;

public abstract class Monedero {
    String nombre;
    String id;
    double saldo;
    public Monedero(String nombre, String id, double saldo) {
        this.nombre = nombre;
        this.id = id;
        this.saldo = saldo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
