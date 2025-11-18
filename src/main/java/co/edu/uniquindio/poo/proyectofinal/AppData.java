package co.edu.uniquindio.poo.proyectofinal;

import model.*;

public class AppData {
    public static int count = 0;
    public static Empresa empresa;
    public static Cliente clienteActual;
    public static Cliente clientePrueba;
    public static Cuenta cuentaPrueba;

    static {
        empresa = new Empresa("Point Master Bank", "washingtong", "12344");
        clientePrueba = new Cliente("1092458884","Sara Benjumea Gallego","sbenjumeagallego@gmail.com","3244933362","frison");
        cuentaPrueba = new Bronce(clientePrueba, clientePrueba.getId(), clientePrueba.getNombre(), clientePrueba.getContraseña());
        empresa.agregarCuenta(cuentaPrueba);
        clientePrueba.getListaCuentas().add(cuentaPrueba);
    }

}
