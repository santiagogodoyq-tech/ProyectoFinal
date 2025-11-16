package model;

public class Bronce extends Cuenta {
     public Bronce( Cliente cliente, String codigo, String nombre, double saldo){
         super(cliente, codigo, nombre);
     }
     public void aplicarDescuento (){
         this.descuento = 0.0;
     }

    @Override
    public double asignarPorcentaje() {
        return 0.1;
    }

}
