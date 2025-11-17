package model;

import java.time.LocalDate;

public class RegistroPuntos {
    private int puntosMonedero;
    private Transaccion transaccion;
    private LocalDate fecha;
    public RegistroPuntos(int puntosMonedero, Transaccion transaccion) {
        this.puntosMonedero = puntosMonedero;
        this.transaccion = transaccion;
        this.fecha = transaccion.getFecha();
    }

    public int getPuntosMonedero() {
        return puntosMonedero;
    }

    public void setPuntosMonedero(int puntosMonedero) {
        this.puntosMonedero = puntosMonedero;
    }

    public Transaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }
}
