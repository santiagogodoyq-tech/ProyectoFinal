package model;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class EmpresaTest {
    private static final Logger LOG = Logger.getLogger(EmpresaTest.class.getName());

    Empresa empresa;
    Cliente cliente;
    Cuenta cuenta1, cuenta2;

    @BeforeEach
    void setUp() {
        empresa = new Empresa("BancoX", "Calle 1", "123");

        cliente = new Cliente("1", "Juan", "santigodoy512@gmail.com", "124124315", "12345");

        cuenta1 = new Bronce(cliente, "001", "Cuenta1", "pass1");
        cuenta2 = new Bronce(cliente, "002", "Cuenta2", "pass2");

        empresa.agregarCliente(cliente);
        empresa.agregarCuenta(cuenta1);
        empresa.agregarCuenta(cuenta2);
    }

    @Test
    void agregarCliente() {
        boolean revisar = false;
        LinkedList<Cliente> clientes = empresa.getListaClientes();
        if (clientes.stream().anyMatch(c -> c.equals(cliente))) {
            revisar = true;
        }
        assertTrue(revisar);
    }

    @Test
    void buscarCliente() {
        boolean revisar = false;
        Optional<Cliente> clienteb = empresa.buscarCliente(cliente.getId());
        if (clienteb.isPresent()) {
            revisar = true;
        }
        assertTrue(revisar);
    }

    @Test
    void eliminarCliente() {
        boolean revisar = false;
        empresa.eliminarCliente(cliente);
        Optional<Cliente> clienteb = empresa.buscarCliente(cliente.getId());
        if (clienteb.isPresent()) {
            revisar = true;
        }
        assertFalse(revisar);
        empresa.agregarCliente(cliente);
    }

    @Test
    void agregarCuenta() {
        boolean revisar = false;
        LinkedList<Cuenta> cuentas = empresa.getListaCuentas();
        if (cuentas.stream().anyMatch(c -> c.equals(cuenta1))) {
            revisar = true;
        }
        assertTrue(revisar);
    }

    @Test
    void buscarCuenta() {
        boolean revisar = false;
        Optional<Cuenta> cuentab = empresa.buscarCuenta(cuenta1.getCodigo());
        if (cuentab.isPresent()) {
            revisar = true;
        }
        assertTrue(revisar);
    }

    @Test
    void eliminarCuenta() {
        boolean revisar = false;
        empresa.eliminarCuenta(cuenta1);
        Optional<Cuenta> cuentab = empresa.buscarCuenta(cuenta1.getCodigo());
        if (cuentab.isPresent()) {
            revisar = true;
        }
        assertFalse(revisar);
        empresa.agregarCliente(cliente);
    }

    @Test
    void depositar() {
        double monto = 1000;
        String id = "1";
        empresa.depositar(monto, cuenta1, LocalDate.now(), id);
        LinkedList<Monedero> monederos = cuenta1.getListaMonedero();

        double montoMonedero = monederos.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        assertEquals(monto, montoMonedero);
    }

    @Test
    void retirar() {
        double monto = 5000;
        String id = "1";
        empresa.depositar(1000, cuenta1, LocalDate.now(), id);
        empresa.retirar(monto, cuenta1, LocalDate.now(), id);
        LinkedList<Monedero> monederos = cuenta1.getListaMonedero();
        double montoMonedero = monederos.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        assertEquals(monto, montoMonedero);
    }

    @Test
    void transferir() {
        double monto = 5000;
        String id = "1";
        empresa.depositar(1000, cuenta1, LocalDate.now(), id);
        empresa.transferir(monto,cuenta1,cuenta2,LocalDate.now(),id,id);

        LinkedList<Monedero> monederos = cuenta1.getListaMonedero();
        double montoMonedero = monederos.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();

        LinkedList<Monedero> monederos1 = cuenta2.getListaMonedero();
        double montoMonedero1 = monederos1.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        assertEquals(montoMonedero1, montoMonedero);

    }
}