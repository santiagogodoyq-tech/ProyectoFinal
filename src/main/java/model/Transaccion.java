package model;

import java.time.LocalDate;

public class Transaccion {
    private String id;
    private double monto;
    private Cuenta salida;
    private Cuenta entrada;
    private LocalDate fecha;
    public Transaccion(String id,double monto, Cuenta salida, Cuenta entrada,  LocalDate fecha) {
        this.id = id;
        this.monto = monto;
        this.salida = salida;
        this.entrada = entrada;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Cuenta getEntrada() {
        return entrada;
    }

    public void setEntrada(Cuenta entrada) {
        this.entrada = entrada;
    }

    public Cuenta getSalida() {
        return salida;
    }

    public void setSalida(Cuenta salida) {
        this.salida = salida;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
