package model;

public class Oro extends Cuenta {
    public Oro(Cliente cliente, String codigo, String nombre, double saldo) {
        super(cliente, codigo, nombre);

    }
    public void aplicarDescuento (){
        this.descuento = 0.1;
        this.puntosMonedero += 50;
    }
    @Override
    public double asignarPorcentaje() {
        return 0.025;
    }
}
