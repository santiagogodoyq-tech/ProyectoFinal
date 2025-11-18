package model;

public class Oro extends Cuenta {
    public Oro(Cliente cliente, String codigo, String nombre, String contraseña) {
        super(cliente, codigo, nombre, contraseña);

    }
    public void aplicarDescuento (){
        this.descuento = 0.1;
        this.puntosMonedero += 50;
    }
    @Override
    public double asignarPorcentaje() {
        return 0.025;
    }
    @Override
    public String setRango() {
        return "Oro";
    }

    @Override
    public void setRango(String rango) {

    }
}
