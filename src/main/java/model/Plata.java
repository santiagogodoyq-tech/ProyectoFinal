package model;

public class Plata extends Cuenta{
    public Plata(Cliente cliente, String codigo, String nombre){
        super( cliente, codigo, nombre);
    }

    @Override
    public void aplicarDescuento() {
        this.descuento =0.05;

    }
    @Override
    public double asignarPorcentaje() {
        return 0.05;
    }
}
