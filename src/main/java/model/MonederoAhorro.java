package model;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonederoAhorro extends Monedero{
    private int retirosRealizados;
    private static final int LIMITESRETIROS = 5;

    public MonederoAhorro(String nombre, String id, double saldo){
        super(nombre, id, saldo);
        this.retirosRealizados = 0;

    }
    public boolean retirar (Cuenta cuenta){
        boolean flag = false;
        if(retirosRealizados <= LIMITESRETIROS){
            System.out.println("limite de retiros alcanzado. Meta de ahorros protegida ");
            flag = true;
        }
        return flag;
    }
    public void tiempoRetiros(Cuenta cuenta){
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        long delay = Duration.ofDays(30).toMillis();
        int puntosAg = 0;
        if(cuenta instanceof Bronce){
            puntosAg = 15;
        }else if(cuenta instanceof Plata){
            puntosAg = 30;
        }else if(cuenta instanceof Oro){
            puntosAg = 60;
        }else{
            puntosAg = 120;
        }
        if(retirosRealizados >= LIMITESRETIROS){
            int finalPuntosAg = puntosAg;
            scheduler.schedule(()->{ cuenta.setPuntosMonedero(cuenta.getPuntosMonedero()+ finalPuntosAg);},delay, TimeUnit.MILLISECONDS);
        }
    }

    public int getRetirosRealizados() {
        return retirosRealizados;
    }


}
