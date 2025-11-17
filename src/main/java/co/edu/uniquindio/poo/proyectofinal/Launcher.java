package co.edu.uniquindio.poo.proyectofinal;

import javafx.application.Application;
import model.*;

import java.time.LocalDate;
import java.util.LinkedList;

public class Launcher {
    public static void main(String[] args) {
        Empresa  empresa = new Empresa("Coint Master Bank", "washingtong", "12344");
        Cliente cliente = new Cliente("123456", "Sara", "sbenjumeagallego@gmail.com", "3205360033", "123");
        Cuenta cuenta = new Bronce( cliente, "32145", "Sara");
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        LocalDate fechaInicio = LocalDate.now();
        Monedero monedero = new MonederoAhorro("Sara","12356", 0);
        listaMonedero.add(monedero);
        cuenta.setListaMonedero(listaMonedero);
        empresa.depositar(20000, cuenta, fechaInicio, "12356");
        Application.launch(ClientApplication.class, args);
    }

}
