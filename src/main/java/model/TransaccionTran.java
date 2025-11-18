package model;

import java.time.LocalDate;

public class TransaccionTran extends Transaccion {
    private Cuenta entrada;
    public TransaccionTran(String id, double monto, Cuenta salida, LocalDate fecha, Cuenta entrada) {
        super(id,monto,salida,fecha);
    }

    public Cuenta getEntrada() {
        return entrada;
    }

    public void setEntrada(Cuenta entrada) {
        this.entrada = entrada;
    }
    @Override
    public void setTipo(){
        tipo = "Programada";
    }
}
