package co.edu.uniquindio.poo.proyectofinal;

import model.Cliente;
import model.Cuenta;
import model.Bronce;
import model.Empresa;

import java.time.LocalDate;

public class AppData {

    public static Empresa empresa;
    
    public static void init() {

        empresa = new Empresa("Coint Master Bank", "washingtong", "12344");

    }
}

