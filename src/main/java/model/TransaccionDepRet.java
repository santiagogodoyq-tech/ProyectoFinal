package model;

import java.time.LocalDate;

public class TransaccionDepRet extends Transaccion {
    public TransaccionDepRet(String id, double monto, Cuenta salida, LocalDate fecha) {
        super(id,monto,salida,fecha);
    }
    @Override
    public void setTipo(){
        tipo = "DepRet";
    }
}
