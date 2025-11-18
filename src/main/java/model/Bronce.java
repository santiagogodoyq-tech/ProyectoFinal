package model;

public class Bronce extends Cuenta {
     public Bronce( Cliente cliente, String codigo, String nombre, String contraseña){
         super(cliente, codigo, nombre, contraseña);
     }
     public void aplicarDescuento (){
         this.descuento = 0.0;
     }

    @Override
    public double asignarPorcentaje() {
        return 0.1;
    }
    @Override
    public String setRango() {
         return "Bronce";
    }

    @Override
    public void setRango(String rango) {

    }
}
