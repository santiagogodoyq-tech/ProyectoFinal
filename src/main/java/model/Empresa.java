package model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

public class Empresa {
    private int count;
    private LinkedList<Cliente> ListaClientes;
    private LinkedList<Cuenta> ListaCuentas;
    private LinkedList<Transaccion> ListaTransacciones; //lo vamos a hacer con gmail o con whatsapp lo de enviar mensaje?
    private String nombre;
    private String direccion;
    private String NIT;

    public Empresa(String nombre, String direccion, String NIT) {
        this.count = 0;
        this.nombre = nombre;
        this.direccion = direccion;
        this.NIT = NIT;
        this.ListaClientes = new LinkedList<>();
        this.ListaCuentas = new LinkedList<>();
        this.ListaTransacciones = new LinkedList<>();
    }

    public LinkedList<Cliente> getListaClientes() {
        return ListaClientes;
    }

    public void setListaClientes(LinkedList<Cliente> listaClientes) {
        ListaClientes = listaClientes;
    }

    public LinkedList<Cuenta> getListaCuentas() {
        return ListaCuentas;
    }

    public void setListaCuentas(LinkedList<Cuenta> listaCuentas) {
        ListaCuentas = listaCuentas;
    }

    public LinkedList<Transaccion> getListaTransacciones() {
        return ListaTransacciones;
    }

    public void setListaTransacciones(LinkedList<Transaccion> listaTransacciones) {
        ListaTransacciones = listaTransacciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNIT() {
        return NIT;
    }

    public void setNIT(String NIT) {
        this.NIT = NIT;
    }

    public String agregarCliente(Cliente cliente) {
        String respuesta;
        Optional<Cliente> clienteBuscado;
        clienteBuscado = buscarCliente(cliente.getId());
        if (clienteBuscado.isEmpty()) {
            ListaClientes.add(cliente);
            respuesta = "el Cliente se a agregado con exito";
        } else {
            respuesta = "el cliente ya esta registrado";
        }
        return respuesta;
    }

    public Optional<Cliente> buscarCliente(String id) {
        return ListaClientes.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    public String eliminarCliente(Cliente cliente) {
        String respuesta;
        Optional<Cliente> clienteBuscado;
        clienteBuscado = buscarCliente(cliente.getId());
        if (clienteBuscado.isPresent()) {
            ListaClientes.remove(cliente);
            respuesta = "el Cliente se a retirado con exito";
        } else {
            respuesta = "el cliente no existe";
        }
        return respuesta;
    }

    public String agregarCuenta(Cuenta cuenta) {
        String respuesta;
        Optional<Cuenta> cuentaBuscado;
        cuentaBuscado = buscarCuenta(cuenta.getCodigo());
        if (cuentaBuscado.isEmpty()) {
            ListaCuentas.add(cuenta);
            respuesta = "la cuenta se a agregado con exito";
        } else {
            respuesta = "la cuenta ya esta registrado";
        }
        return respuesta;
    }

    public Optional<Cuenta> buscarCuenta(String id) {
        return ListaCuentas.stream().filter(x -> x.getCodigo().equals(id)).findFirst();
    }

    public String eliminarCuenta(Cuenta cuenta) {
        String respuesta;
        Optional<Cuenta> cuentaBuscado;
        cuentaBuscado = buscarCuenta(cuenta.getCodigo());
        if (cuentaBuscado.isPresent()) {
            ListaCuentas.remove(cuenta);
            respuesta = "la cuenta se a eliminado con exito";
        } else {
            respuesta = "la cuenta no existe";
        }
        return respuesta;
    }

    public void agregarTransaccion(Cuenta salida, Cuenta entrada, double monto, LocalDate fecha) {
        count++;
        String countS = Integer.toString(count);
        Transaccion transaccion = new Transaccion(countS, monto, salida, entrada, fecha);
        ListaTransacciones.add(transaccion);
    }

    //depositar dinero
    public void depositar(double monto, Cuenta cuenta, Cuenta cuenta2, LocalDate fecha, String id) {
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        double saldo = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        if (monto > 0) {
            saldo += monto;
            agregarTransaccion(cuenta, cuenta2, monto, fecha);
            cuenta.setPuntosMonedero(cuenta.getPuntosMonedero() + (int) (monto / 100));

        }else {
            System.out.println("el monto a depositar debe ser mayor a 0");
        }
    }
    public void retirar(double monto, Cuenta cuenta, Cuenta cuenta2, LocalDate fecha, String id){
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        double saldo = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        if (monto > 0 && saldo >= monto){
         double nuevoSaldo = saldo - monto;
         listaMonedero.forEach(m -> {if(m.getId().equals(id)){m.setSaldo(nuevoSaldo);}});
         cuenta.setListaMonedero(listaMonedero);
         agregarTransaccion( cuenta, cuenta2, monto, fecha);
         cuenta.setPuntosMonedero(cuenta.getPuntosMonedero() + (int) (monto / 100)*2);
     } else{
         System.out.println("el monto a retirar debe ser mayor a 0");
     }

    }
    public double consultarSaldo(Cuenta cuenta, String id) {
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        double saldo = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        return saldo;
    }
    public void consultarHistorial (Cuenta cuenta) {
        LinkedList<Transaccion> cuentaTransacciones = cuenta.getListaTransacciones();
        cuentaTransacciones.stream().forEach(System.out::println);
        for (Transaccion transaccion : cuenta.getListaTransacciones()) {
            System.out.println (transaccion);
        }
    }
    public void transferir (double monto, Cuenta cuenta, Cuenta cuenta2, LocalDate fecha, String id, String id2){
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        double saldo = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        LinkedList<Monedero> listaMonedero2 = cuenta2.getListaMonedero();
        double saldo2 = listaMonedero2.stream().filter(x -> x.getId().equals(id2)).findFirst().get().getSaldo();
        if (monto > 0 && saldo >= monto) {
            double nuevoSaldoOrigen = saldo - monto;
            double nuevoSaldoDestino = saldo2 + monto;
            listaMonedero.forEach(x -> {if(x.getId().equals(id)){x.setSaldo(nuevoSaldoOrigen);}});
            cuenta.setListaMonedero(listaMonedero);
            listaMonedero2.forEach(x->{if(x.getId().equals(id)){x.setSaldo(nuevoSaldoDestino);}});
            cuenta2.setListaMonedero(listaMonedero2);
            agregarTransaccion(cuenta, cuenta2, monto, fecha);
            cuenta.setPuntosMonedero(cuenta.getPuntosMonedero()+(int)((monto/100)*3));
        } else {
            System.out.println("el monto a transferir debe ser mayor a 0");
        }
    }
  public Cuenta actualizarRango (Cuenta cuenta, String id) {
      LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
      double saldo = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        Cuenta cuentaExp = cuenta;
   int puntos = cuenta.getPuntosMonedero();
   if (puntos <= 500 && !(cuenta instanceof Bronce)){
       Cuenta nuevaCuenta = new Bronce(cuenta.getCliente(),cuenta.getCodigo(), cuenta.getNombre(), saldo);
       cuentaExp = nuevaCuenta;
       cuentaExp.setPuntosMonedero(cuenta.getPuntosMonedero());
       cuentaExp.setListaTransacciones(cuenta.getListaTransacciones());
       ListaCuentas.remove(cuenta);
       ListaCuentas.add(cuentaExp);
   }else if (puntos > 500 && puntos <= 1000 && !(cuenta instanceof Plata)){
       Cuenta nuevaCuenta = new Plata(cuenta.getCliente(),cuenta.getCodigo(), cuenta.getNombre(), saldo);
       cuentaExp = nuevaCuenta;
       cuentaExp.setPuntosMonedero(cuenta.getPuntosMonedero());
       cuentaExp.setListaTransacciones(cuenta.getListaTransacciones());
       ListaCuentas.remove(cuenta);
       ListaCuentas.add(cuentaExp);}
   else if (puntos > 1000 && puntos <= 5000 && !(cuenta instanceof Oro)){
       Cuenta nuevaCuenta = new Oro(cuenta.getCliente(),cuenta.getCodigo(), cuenta.getNombre(), saldo);
       cuentaExp = nuevaCuenta;
       cuentaExp.setPuntosMonedero(cuenta.getPuntosMonedero());
       cuentaExp.setListaTransacciones(cuenta.getListaTransacciones());
       ListaCuentas.remove(cuenta);
       ListaCuentas.add(cuentaExp);}
      else if (puntos <= 5000 && !(cuenta instanceof Platino)){
          Cuenta nuevaCuenta = new Platino(cuenta.getCliente(),cuenta.getCodigo(), cuenta.getNombre(), saldo);
          cuentaExp = nuevaCuenta;
          cuentaExp.setPuntosMonedero(cuenta.getPuntosMonedero());
          cuentaExp.setListaTransacciones(cuenta.getListaTransacciones());
          ListaCuentas.remove(cuenta);
          ListaCuentas.add(cuentaExp);}
   return cuentaExp;
  }

}