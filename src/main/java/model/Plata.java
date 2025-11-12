package model;

public class Plata extends Cuenta{
    public Plata(Cliente cliente, String codigo, String nombre, double saldo){
        super( cliente, codigo, nombre);
    }

    @Override
    public void aplicarDescuento() {
        this.descuento =0.05;

    }
}
