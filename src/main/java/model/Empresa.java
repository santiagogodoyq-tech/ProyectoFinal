package model;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

public class Empresa {
    private int count;
    private LinkedList<Cliente> ListaClientes;
    private LinkedList<Cuenta> ListaCuentas;
    private LinkedList<Transaccion> ListaTransacciones;
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
        Transaccion transaccion = new TransaccionTran(countS, monto, salida, fecha, entrada);
        ListaTransacciones.add(transaccion);
    }

    public void agregarTransaccion(Cuenta salida, double monto, LocalDate fecha) {
        count++;
        String countS = Integer.toString(count);
        Transaccion transaccion = new TransaccionDepRet(countS, monto, salida, fecha);
        ListaTransacciones.add(transaccion);
    }

    public void depositar(double monto, Cuenta cuenta, LocalDate fecha, String id) {
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        double saldo = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        String idTrans = Integer.toString(count);
        if (monto > 0) {
            saldo += monto;
            cuenta.setPuntosMonedero(cuenta.getPuntosMonedero() + (int) (monto / 100));
            agregarTransaccion(cuenta, monto, fecha);
            Transaccion transaccion = ListaTransacciones.stream()
                    .filter(x -> x.getId().equals(idTrans))
                    .findFirst()
                    .orElse(null);

            RegistroPuntos registro = new RegistroPuntos(
                    cuenta.getPuntosMonedero() + (int) (monto / 100),
                    transaccion
            );

            cuenta.getListaRegistroPuntos().add(registro);
            EmailService emailService = new EmailService();
            emailService.enviarCorreo(
                    cuenta.getCliente().getEmail(),
                    "Depósito realizado",
                    "Se ha depositado $" + monto + " en el monedero " +listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getId()
            );

        } else {
            System.out.println("el monto a depositar debe ser mayor a 0");
        }
    }

    public void retirar(double monto, Cuenta cuenta, LocalDate fecha, String id) {
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        double saldo = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        Optional<Monedero> monedero = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst();
        Monedero monedero1 = monedero.orElse(null);
        boolean funcionar = false;
        String idTrans = Integer.toString(count);
        if (monedero1 instanceof MonederoAhorro) {
            funcionar = ((MonederoAhorro) monedero1).retirar(cuenta);
        }
        if (funcionar) {
            if (monto > 0 && saldo >= monto) {
                double nuevoSaldo = saldo - monto;
                listaMonedero.forEach(m -> {
                    if (m.getId().equals(id)) {
                        m.setSaldo(nuevoSaldo);
                    }
                });
                cuenta.setListaMonedero(listaMonedero);
                cuenta.setPuntosMonedero(cuenta.getPuntosMonedero() + (int) (monto / 100) * 2);
                agregarTransaccion(cuenta, monto, fecha);
                Transaccion transaccion = ListaTransacciones.stream()
                        .filter(x -> x.getId().equals(idTrans))
                        .findFirst()
                        .orElse(null);

                RegistroPuntos registro = new RegistroPuntos(
                        cuenta.getPuntosMonedero() + (int) (monto / 100),
                        transaccion
                );
            } else {
                System.out.println("el monto a retirar debe ser mayor a 0");
            }
        } else if (!(monedero1 instanceof MonederoAhorro)) {
            if (monto > 0 && saldo >= monto) {
                double nuevoSaldo = saldo - monto;
                listaMonedero.forEach(m -> {
                    if (m.getId().equals(id)) {
                        m.setSaldo(nuevoSaldo);
                    }
                });
                cuenta.setListaMonedero(listaMonedero);
                cuenta.setPuntosMonedero(cuenta.getPuntosMonedero() + (int) (monto / 100) * 2);
                agregarTransaccion(cuenta, monto, fecha);
                Transaccion transaccion = ListaTransacciones.stream()
                        .filter(x -> x.getId().equals(idTrans))
                        .findFirst()
                        .orElse(null);

                RegistroPuntos registro = new RegistroPuntos(
                        cuenta.getPuntosMonedero() + (int) (monto / 100),
                        transaccion
                );
                EmailService emailService = new EmailService();
                emailService.enviarCorreo(
                        cuenta.getCliente().getEmail(),  // correo del dueño
                        "Retiro realizado",
                        "Se ha realizado un retiro de $" + monto + " en su monedero " + listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getId()
                );
            } else {
                System.out.println("el monto a retirar debe ser mayor a 0");
            }
        }
    }

    public double consultarSaldo(Cuenta cuenta, String id) {
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        double saldo = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        return saldo;
    }

    public void consultarHistorial(Cuenta cuenta) {
        LinkedList<Transaccion> cuentaTransacciones = cuenta.getListaTransacciones();
        cuentaTransacciones.stream().forEach(System.out::println);
        for (Transaccion transaccion : cuenta.getListaTransacciones()) {
            System.out.println(transaccion);
        }
    }

    public void transferir(double monto, Cuenta cuenta, Cuenta cuenta2, LocalDate fecha, String id, String id2) {
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        double saldo = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        LinkedList<Monedero> listaMonedero2 = cuenta2.getListaMonedero();
        double saldo2 = listaMonedero2.stream().filter(x -> x.getId().equals(id2)).findFirst().get().getSaldo();
        String idTrans = Integer.toString(count);
        if (monto > 0 && saldo >= monto) {
            double nuevoSaldoOrigen = saldo - monto - (monto * cuenta.getPorcentajeTran());
            double nuevoSaldoDestino = saldo2 + monto;
            listaMonedero.forEach(x -> {
                if (x.getId().equals(id)) {
                    x.setSaldo(nuevoSaldoOrigen);
                }
            });
            cuenta.setListaMonedero(listaMonedero);
            listaMonedero2.forEach(x -> {
                if (x.getId().equals(id)) {
                    x.setSaldo(nuevoSaldoDestino);
                }
            });
            cuenta2.setListaMonedero(listaMonedero2);
            agregarTransaccion(cuenta, cuenta2, monto, fecha);
            Transaccion transaccion = ListaTransacciones.stream()
                    .filter(x -> x.getId().equals(idTrans))
                    .findFirst()
                    .orElse(null);

            RegistroPuntos registro = new RegistroPuntos(
                    cuenta.getPuntosMonedero() + (int) (monto / 100),
                    transaccion
            );
            cuenta.setPuntosMonedero(cuenta.getPuntosMonedero() + (int) ((monto / 100) * 3));
            EmailService emailService = new EmailService();

            emailService.enviarCorreo(
                    cuenta.getCliente().getEmail(),
                    "Transferencia realizada",
                    "Has transferido $" + monto +
                            " desde el monedero " + listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getId() +
                            " a la cuenta " + cuenta.getCodigo()
            );
            emailService.enviarCorreo(
                    cuenta2.getCliente().getEmail(),
                    "Has recibido una transferencia",
                    "Has recibido $" + monto +
                            " en el monedero " + listaMonedero2.stream().filter(x -> x.getId().equals(id2)).findFirst().get().getId() +
                            " proveniente de la cuenta " + cuenta2.getCodigo()
            );

        } else {
            System.out.println("el monto a transferir debe ser mayor a 0");
        }
    }

    public Cuenta actualizarRango(Cuenta cuenta, String id) {
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        double saldo = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        Cuenta cuentaExp = cuenta;
        int puntos = cuenta.getPuntosMonedero();
        if (puntos <= 500 && !(cuenta instanceof Bronce)) {
            Cuenta nuevaCuenta = new Bronce(cuenta.getCliente(), cuenta.getCodigo(), cuenta.getNombre(), cuenta.getContraseña());
            cuentaExp = nuevaCuenta;
            cuentaExp.setPuntosMonedero(cuenta.getPuntosMonedero());
            cuentaExp.setListaTransacciones(cuenta.getListaTransacciones());
            cuentaExp.setListaMonedero(cuenta.getListaMonedero());
            ListaCuentas.remove(cuenta);
            ListaCuentas.add(cuentaExp);
        } else if (puntos > 500 && puntos <= 1000 && !(cuenta instanceof Plata)) {
            Cuenta nuevaCuenta = new Plata(cuenta.getCliente(), cuenta.getCodigo(), cuenta.getNombre(), cuenta.getContraseña());
            cuentaExp = nuevaCuenta;
            cuentaExp.setPuntosMonedero(cuenta.getPuntosMonedero());
            cuentaExp.setListaTransacciones(cuenta.getListaTransacciones());
            cuentaExp.setListaMonedero(cuenta.getListaMonedero());
            ListaCuentas.remove(cuenta);
            ListaCuentas.add(cuentaExp);
        } else if (puntos > 1000 && puntos <= 5000 && !(cuenta instanceof Oro)) {
            Cuenta nuevaCuenta = new Oro(cuenta.getCliente(), cuenta.getCodigo(), cuenta.getNombre(), cuenta.getContraseña());
            cuentaExp = nuevaCuenta;
            cuentaExp.setPuntosMonedero(cuenta.getPuntosMonedero());
            cuentaExp.setListaTransacciones(cuenta.getListaTransacciones());
            cuentaExp.setListaMonedero(cuenta.getListaMonedero());
            ListaCuentas.remove(cuenta);
            ListaCuentas.add(cuentaExp);
        } else if (puntos <= 5000 && !(cuenta instanceof Platino)) {
            Cuenta nuevaCuenta = new Platino(cuenta.getCliente(), cuenta.getCodigo(), cuenta.getNombre(), cuenta.getContraseña());
            cuentaExp = nuevaCuenta;
            cuentaExp.setPuntosMonedero(cuenta.getPuntosMonedero());
            cuentaExp.setListaTransacciones(cuenta.getListaTransacciones());
            cuentaExp.setListaMonedero(cuenta.getListaMonedero());
            ListaCuentas.remove(cuenta);
            ListaCuentas.add(cuentaExp);
        }
        return cuentaExp;
    }

    public void transferenciaProgramada(double monto, @NotNull Cuenta cuenta, Cuenta cuenta2, LocalDate fechaIngresada, String id, String id2) {
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        double saldo = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        LinkedList<Monedero> listaMonedero2 = cuenta2.getListaMonedero();
        double saldo2 = listaMonedero2.stream().filter(x -> x.getId().equals(id2)).findFirst().get().getSaldo();
        LocalDate fecha = LocalDate.now();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        long delay = Duration.between(fecha, fechaIngresada).toMillis();
        scheduler.schedule(() -> {
            transferir(monto, cuenta, cuenta2, fechaIngresada, id, id2);
        }, delay, TimeUnit.MILLISECONDS);
        if (delay <= 0) {
            transferir(monto, cuenta, cuenta2, fechaIngresada, id, id2);
        }
    }

    public String canjearBeneficio(Cuenta cuenta, int puntosCanjear, String id) {
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        double saldo = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo();
        int puntosActuales = cuenta.getPuntosMonedero();
        String respuesta = "";
        if (puntosActuales < puntosCanjear) {
            return "no tienes suficientes puntos para canjear este beneificio.";

        }
        LocalDate hoy = LocalDate.now();
        LocalDate expiracion;
        if (puntosCanjear == 100) {
            cuenta.setDescuento(0.10);
            cuenta.setPuntosMonedero(puntosActuales - 100);
            cuenta.setBeneficioActivo("descuento 10%");
            expiracion = hoy.plusDays(7);
            cuenta.setFechaExpiracionBeneficio(expiracion);
            return "10% de decuento en sus transferencias, valido hasta" + expiracion;
        } else if (puntosCanjear == 500) {
            cuenta.setDescuento(1);
            cuenta.setPuntosMonedero(puntosActuales - 500);
            cuenta.setBeneficioActivo("retiros gratis.");
            expiracion = hoy.plusDays(22);
            cuenta.setFechaExpiracionBeneficio(expiracion);
            return "Retiros gratuitos hasta" + expiracion;
        } else if (puntosCanjear == 1000) {
            if (cuenta instanceof Oro) {
                listaMonedero.forEach(x -> {
                    if (x.getId().equals(id)) {
                        x.setSaldo(x.getSaldo()
                                + 50000);
                    }
                });
                cuenta.setBeneficioActivo("bono 50000.");
            } else if (cuenta instanceof Platino) {
                    listaMonedero.forEach(x -> {
                        if (x.getId().equals(id)) {
                            x.setSaldo(x.getSaldo()
                                    + 75000);
                        }
                    });
                    cuenta.setBeneficioActivo("bono 75000.");
                } else if (cuenta instanceof Plata) {
                        listaMonedero.forEach(x -> {
                            if (x.getId().equals(id)) {
                                x.setSaldo(x.getSaldo()
                                        + 35000);
                            }
                        });
                        cuenta.setBeneficioActivo("bono 35000.");

                    }
                    cuenta.setPuntosMonedero(puntosActuales - 1000);
                    expiracion = hoy.plusDays(30);
                    cuenta.setFechaExpiracionBeneficio(expiracion);
                    return "haz recibido el bono de dinero";

                }


        return respuesta;
    }

    public boolean beneficioVigente(Cuenta cuenta) {
        if (cuenta.getBeneficioActivo() == null) return false;
        if (cuenta.getFechaExpiracionBeneficio() == null) return false;

        LocalDate hoy = LocalDate.now();


        if (hoy.isAfter(cuenta.getFechaExpiracionBeneficio())) {
            cuenta.setBeneficioActivo(null);
            cuenta.setDescuento(0);
            return false;
        }

        return true;
    }

    public void iniciarVerificadorBeneficios() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
        scheduler.scheduleAtFixedRate(() -> {
            for (Cuenta cuenta : ListaCuentas) {
                if (cuenta.getBeneficioActivo() != null &&
                        cuenta.getFechaExpiracionBeneficio() != null) {

                    LocalDate hoy = LocalDate.now();

                    if (hoy.isAfter(cuenta.getFechaExpiracionBeneficio())) {
                        cuenta.setBeneficioActivo(null);
                        cuenta.setDescuento(0);
                        cuenta.setFechaExpiracionBeneficio(null);
                        System.out.println("Beneficio expirado para la cuenta: " + cuenta.getCodigo());
                    }
                }
            }
        }, 0, 1, TimeUnit.DAYS);
    }


}