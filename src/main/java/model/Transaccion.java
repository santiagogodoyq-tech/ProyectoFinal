package model;

import java.time.LocalDate;
//perdon, que paso? DE QUE? no vio lo que le paso al codigo? yO LO VEO NOR JAJAJAJAJAJAJ BUENO  okey mañana le cuento que paso
public abstract class Transaccion{
    protected String id;
    protected double monto;
    protected Cuenta salida;
    protected LocalDate fecha;
    protected String tipo;
    public Transaccion(String id,double monto, Cuenta salida, LocalDate fecha) {
        this.id = id;
        this.monto = monto;
        this.salida = salida;
        this.fecha = fecha;
        setTipo();
    }
    public String getTipo(){
        return tipo;
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
    public abstract void setTipo();
}
