package model;

public class Plata extends Cuenta{
    public Plata(Cliente cliente, String codigo, String nombre, String contraseña){
        super( cliente, codigo, nombre, contraseña);
    }

    @Override
    public void aplicarDescuento() {
        this.descuento =0.05;

    }
    @Override
    public double asignarPorcentaje() {
        return 0.05;
    }
    @Override
    public String setRango() {
        return "Plata";
    }
}
