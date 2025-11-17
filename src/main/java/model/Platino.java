package model;

public class Platino extends Cuenta{
    public Platino(Cliente cliente, String codigo, String nombre, String contraseña){
        super(cliente, codigo, nombre, contraseña);

    }

    @Override
    public void aplicarDescuento() {
        this.descuento = 0.5;
        this.puntosMonedero += 100;
    }
    @Override
    public double asignarPorcentaje() {
        return 0.01;
    }
}

