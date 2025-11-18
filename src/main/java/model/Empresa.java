package model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    public LinkedList<Cliente> getListaClientes() { return ListaClientes; }
    public void setListaClientes(LinkedList<Cliente> listaClientes) { ListaClientes = listaClientes; }
    public LinkedList<Cuenta> getListaCuentas() { return ListaCuentas; }
    public void setListaCuentas(LinkedList<Cuenta> listaCuentas) { ListaCuentas = listaCuentas; }
    public LinkedList<Transaccion> getListaTransacciones() { return ListaTransacciones; }
    public void setListaTransacciones(LinkedList<Transaccion> listaTransacciones) { ListaTransacciones = listaTransacciones; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getNIT() { return NIT; }
    public void setNIT(String NIT) { this.NIT = NIT; }

    public String agregarCliente(Cliente cliente) {
        Optional<Cliente> clienteBuscado = buscarCliente(cliente.getId());
        if (clienteBuscado.isEmpty()) {
            ListaClientes.add(cliente);
            return "El Cliente se ha agregado con éxito";
        } else {
            return "El cliente ya está registrado";
        }
    }

    public Optional<Cliente> buscarCliente(String id) {
        return ListaClientes.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    public String eliminarCliente(Cliente cliente) {
        Optional<Cliente> clienteBuscado = buscarCliente(cliente.getId());
        if (clienteBuscado.isPresent()) {
            ListaClientes.remove(cliente);
            return "El Cliente se ha retirado con éxito";
        } else {
            return "El cliente no existe";
        }
    }

    public String agregarCuenta(Cuenta cuenta) {
        Optional<Cuenta> cuentaBuscado = buscarCuenta(cuenta.getCodigo());
        if (cuentaBuscado.isEmpty()) {
            ListaCuentas.add(cuenta);
            LinkedList<Monedero> listaMonedero = new LinkedList<>();
            Monedero monedero = new MonederoAhorro("ahorro", "1", 0);
            Monedero monedero1 = new MonederoDiario("diario", "2", 0);
            listaMonedero.add(monedero);
            listaMonedero.add(monedero1);
            cuenta.setListaMonedero(listaMonedero);
            return "La cuenta se ha agregado con éxito";
        } else {
            return "La cuenta ya está registrada";
        }
    }

    public Optional<Cuenta> buscarCuenta(String id) {
        return ListaCuentas.stream().filter(x -> x.getCodigo().equals(id)).findFirst();
    }

    public String eliminarCuenta(Cuenta cuenta) {
        Optional<Cuenta> cuentaBuscado = buscarCuenta(cuenta.getCodigo());
        if (cuentaBuscado.isPresent()) {
            ListaCuentas.remove(cuenta);
            return "La cuenta se ha eliminado con éxito";
        } else {
            return "La cuenta no existe";
        }
    }

    public Transaccion agregarTransaccion(Cuenta salida, Cuenta entrada, double monto, LocalDate fecha) {
        count++;
        String id = Integer.toString(count);
        Transaccion transaccion = new TransaccionTran(id, monto, salida, fecha, entrada);
        ListaTransacciones.add(transaccion);
        if (salida != null) salida.getListaTransacciones().add(transaccion);
        if (entrada != null) entrada.getListaTransacciones().add(transaccion);
        return transaccion;
    }

    public Transaccion agregarTransaccion(Cuenta salida, double monto, LocalDate fecha) {
        count++;
        String id = Integer.toString(count);
        Transaccion transaccion = new TransaccionDepRet(id, monto, salida, fecha);
        ListaTransacciones.add(transaccion);
        if (salida != null) salida.getListaTransacciones().add(transaccion);
        return transaccion;
    }

    public void depositar(double monto, Cuenta cuenta, LocalDate fecha, String idMonedero) {
        if (cuenta == null) {
            System.out.println("Cuenta nula en depositar");
            return;
        }
        if (monto <= 0) {
            System.out.println("El monto a depositar debe ser mayor a 0");
            return;
        }

        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        Optional<Monedero> opt = listaMonedero.stream().filter(x -> x.getId().equals(idMonedero)).findFirst();

        if (opt.isEmpty()) {
            System.out.println("Monedero no encontrado: " + idMonedero);
            return;
        }

        Monedero monedero = opt.get();

        monedero.setSaldo(monedero.getSaldo() + monto);

        int puntosGanados = (int) (monto / 100);
        cuenta.setPuntosMonedero(cuenta.getPuntosMonedero() + puntosGanados);
        Transaccion transaccion = agregarTransaccion(cuenta, monto, fecha);

        RegistroPuntos registro = new RegistroPuntos(cuenta.getPuntosMonedero(), transaccion);
        cuenta.getListaRegistroPuntos().add(registro);

        if (cuenta.getCliente() != null && cuenta.getCliente().getEmail() != null) {
            EmailService emailService = new EmailService();
            emailService.enviarCorreo(
                    cuenta.getCliente().getEmail(),
                    "Depósito realizado",
                    "Se ha depositado $" + monto + " en el monedero " + monedero.getId()
            );
        }
    }

    public void retirar(double monto, Cuenta cuenta, LocalDate fecha, String idMonedero) {
        if (cuenta == null) {
            System.out.println("Cuenta nula en retirar");
            return;
        }
        if (monto <= 0) {
            System.out.println("El monto a retirar debe ser mayor a 0");
            return;
        }

        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        Monedero monedero = listaMonedero.stream().filter(x -> x.getId().equals(idMonedero)).findFirst().orElse(null);
        if (monedero == null) {
            System.out.println("Monedero no encontrado: " + idMonedero);
            return;
        }

        double saldo = monedero.getSaldo();

        boolean permitir = false;
        if (monedero instanceof MonederoAhorro) {
            permitir = ((MonederoAhorro) monedero).retirar(cuenta);
        }

        if (permitir) {
            System.out.println("Operación de retiro no permitida por reglas del monedero");
            return;
        }

        if (saldo < monto) {
            System.out.println("Saldo insuficiente");
            return;
        }
        if(!permitir){
            monedero.setSaldo(saldo - monto);

            int puntosGanados = (int) (monto / 100) * 2;
            cuenta.setPuntosMonedero(cuenta.getPuntosMonedero() + puntosGanados);

            Transaccion transaccion = agregarTransaccion(cuenta, monto, fecha);
            RegistroPuntos registro = new RegistroPuntos(cuenta.getPuntosMonedero(), transaccion);
            cuenta.getListaRegistroPuntos().add(registro);
        }else if(permitir && monedero instanceof MonederoDiario){
            monedero.setSaldo(saldo - monto);

            int puntosGanados = (int) (monto / 100) * 2;
            cuenta.setPuntosMonedero(cuenta.getPuntosMonedero() + puntosGanados);

            Transaccion transaccion = agregarTransaccion(cuenta, monto, fecha);
            RegistroPuntos registro = new RegistroPuntos(cuenta.getPuntosMonedero(), transaccion);
            cuenta.getListaRegistroPuntos().add(registro);
        }
        if (cuenta.getCliente() != null && cuenta.getCliente().getEmail() != null) {
            EmailService emailService = new EmailService();
            emailService.enviarCorreo(
                    cuenta.getCliente().getEmail(),
                    "Retiro realizado",
                    "Se ha realizado un retiro de $" + monto + " en su monedero " + monedero.getId()
            );
        }
    }

    public double consultarSaldo(Cuenta cuenta, String idMonedero) {
        if (cuenta == null) return 0;
        Optional<Monedero> opt = cuenta.getListaMonedero().stream().filter(x -> x.getId().equals(idMonedero)).findFirst();
        return opt.map(Monedero::getSaldo).orElse(0.0);
    }

    public void consultarHistorial(Cuenta cuenta) {
        if (cuenta == null) return;
        for (Transaccion t : cuenta.getListaTransacciones()) {
            System.out.println(t);
        }
    }

    public void transferir(double monto, Cuenta origen, Cuenta destino, LocalDate fecha, String idMonederoOrigen, String idMonederoDestino) {
        if (origen == null || destino == null) {
            System.out.println("Cuentas inválidas para transferencia");
            return;
        }
        if (monto <= 0) {
            System.out.println("El monto a transferir debe ser mayor a 0");
            return;
        }

        Optional<Monedero> optOrigen = origen.getListaMonedero().stream().filter(x -> x.getId().equals(idMonederoOrigen)).findFirst();
        Optional<Monedero> optDestino = destino.getListaMonedero().stream().filter(x -> x.getId().equals(idMonederoDestino)).findFirst();

        if (optOrigen.isEmpty() || optDestino.isEmpty()) {
            System.out.println("Monedero origen o destino no encontrado");
            return;
        }

        Monedero mOrigen = optOrigen.get();
        Monedero mDestino = optDestino.get();

        double saldoOrigen = mOrigen.getSaldo();
        double saldoDestino = mDestino.getSaldo();

        if (saldoOrigen < monto) {
            System.out.println("Saldo insuficiente para transferencia");
            return;
        }

        double comision = monto * origen.getPorcentajeTran();
        double nuevoSaldoOrigen = saldoOrigen - monto - comision;
        double nuevoSaldoDestino = saldoDestino + monto;

        mOrigen.setSaldo(nuevoSaldoOrigen);
        mDestino.setSaldo(nuevoSaldoDestino);

        Transaccion transaccion = agregarTransaccion(origen, destino, monto, fecha);

        int puntosGanados = (int) ((monto / 100) * 3);
        origen.setPuntosMonedero(origen.getPuntosMonedero() + puntosGanados);

        RegistroPuntos registro = new RegistroPuntos(origen.getPuntosMonedero(), transaccion);
        origen.getListaRegistroPuntos().add(registro);

        EmailService emailService = new EmailService();
        if (origen.getCliente() != null && origen.getCliente().getEmail() != null) {
            emailService.enviarCorreo(
                    origen.getCliente().getEmail(),
                    "Transferencia realizada",
                    "Has transferido $" + monto +
                            " desde el monedero " + mOrigen.getId() +
                            " a la cuenta " + destino.getCodigo()
            );
        }
        if (destino.getCliente() != null && destino.getCliente().getEmail() != null) {
            emailService.enviarCorreo(
                    destino.getCliente().getEmail(),
                    "Has recibido una transferencia",
                    "Has recibido $" + monto +
                            " en el monedero " + mDestino.getId() +
                            " proveniente de la cuenta " + origen.getCodigo()
            );
        }
    }

    public Cuenta actualizarRango(Cuenta cuenta, String id) {
        if (cuenta == null) return null;
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        Optional<Monedero> opt = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst();
        double saldo = opt.map(Monedero::getSaldo).orElse(0.0);
        Cuenta cuentaExp = cuenta;
        int puntos = cuenta.getPuntosMonedero();

        if (puntos <= 500 && !(cuenta instanceof Bronce)) {
            Cuenta nuevaCuenta = new Bronce(cuenta.getCliente(), cuenta.getCodigo(), cuenta.getNombre(), cuenta.getContraseña());
            cuentaExp = nuevaCuenta;
        } else if (puntos > 500 && puntos <= 1000 && !(cuenta instanceof Plata)) {
            Cuenta nuevaCuenta = new Plata(cuenta.getCliente(), cuenta.getCodigo(), cuenta.getNombre(), cuenta.getContraseña());
            cuentaExp = nuevaCuenta;
        } else if (puntos > 1000 && pointsLessOrEqual(puntos, 5000) && !(cuenta instanceof Oro)) {
            Cuenta nuevaCuenta = new Oro(cuenta.getCliente(), cuenta.getCodigo(), cuenta.getNombre(), cuenta.getContraseña());
            cuentaExp = nuevaCuenta;
        } else if (puntos <= 5000 && !(cuenta instanceof Platino)) {
            Cuenta nuevaCuenta = new Platino(cuenta.getCliente(), cuenta.getCodigo(), cuenta.getNombre(), cuenta.getContraseña());
            cuentaExp = nuevaCuenta;
        }

        if (cuentaExp != cuenta) {
            cuentaExp.setPuntosMonedero(cuenta.getPuntosMonedero());
            cuentaExp.setListaTransacciones(cuenta.getListaTransacciones());
            cuentaExp.setListaMonedero(cuenta.getListaMonedero());
            ListaCuentas.remove(cuenta);
            ListaCuentas.add(cuentaExp);
        }

        return cuentaExp;
    }

    private boolean pointsLessOrEqual(int points, int limit) {
        return points <= limit;
    }

    public void transferenciaProgramada(double monto, @NotNull Cuenta cuenta, Cuenta cuenta2, LocalDate fechaIngresada, String id, String id2) {
        if (cuenta == null || cuenta2 == null) {
            System.out.println("Cuentas inválidas para programación");
            return;
        }

        LocalDate hoy = LocalDate.now();
        long days = ChronoUnit.DAYS.between(hoy, fechaIngresada);
        if (days <= 0) {
            // fecha pasada o hoy -> ejecutar de inmediato
            transferir(monto, cuenta, cuenta2, fechaIngresada, id, id2);
            return;
        }

        long delayMillis = days * 24L * 60L * 60L * 1000L;
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> transferir(monto, cuenta, cuenta2, fechaIngresada, id, id2),
                delayMillis, TimeUnit.MILLISECONDS);
        scheduler.shutdown();
    }

    public String canjearBeneficio(Cuenta cuenta, int puntosCanjear, String id) {
        if (cuenta == null) return "Cuenta inválida";
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        Optional<Monedero> opt = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst();
        if (opt.isEmpty()) return "Monedero no encontrado";
        int puntosActuales = cuenta.getPuntosMonedero();
        if (puntosActuales < puntosCanjear) return "No tienes suficientes puntos para canjear este beneficio.";

        LocalDate hoy = LocalDate.now();
        LocalDate expiracion;

        if (puntosCanjear == 100) {
            cuenta.setDescuento(0.10);
            cuenta.setPuntosMonedero(puntosActuales - 100);
            cuenta.setBeneficioActivo("descuento 10%");
            expiracion = hoy.plusDays(7);
            cuenta.setFechaExpiracionBeneficio(expiracion);
            return "10% de descuento en sus transferencias, válido hasta " + expiracion;
        } else if (puntosCanjear == 500) {
            cuenta.setDescuento(1);
            cuenta.setPuntosMonedero(puntosActuales - 500);
            cuenta.setBeneficioActivo("retiros gratis.");
            expiracion = hoy.plusDays(22);
            cuenta.setFechaExpiracionBeneficio(expiracion);
            return "Retiros gratuitos hasta " + expiracion;
        } else if (puntosCanjear == 1000) {
            Monedero monedero = opt.get();
            if (cuenta instanceof Oro) {
                monedero.setSaldo(monedero.getSaldo() + 50000);
                cuenta.setBeneficioActivo("bono 50000.");
            } else if (cuenta instanceof Platino) {
                monedero.setSaldo(monedero.getSaldo() + 75000);
                cuenta.setBeneficioActivo("bono 75000.");
            } else if (cuenta instanceof Plata) {
                monedero.setSaldo(monedero.getSaldo() + 35000);
                cuenta.setBeneficioActivo("bono 35000.");
            } else {
                return "Tu rango no aplica para este bono";
            }
            cuenta.setPuntosMonedero(puntosActuales - 1000);
            expiracion = hoy.plusDays(30);
            cuenta.setFechaExpiracionBeneficio(expiracion);
            return "Has recibido el bono de dinero, válido hasta " + expiracion;
        }

        return "Opción de canje no reconocida";
    }
    public boolean beneficioVigente(Cuenta cuenta) {
        if (cuenta == null) return false;
        if (cuenta.getBeneficioActivo() == null) return false;
        if (cuenta.getFechaExpiracionBeneficio() == null) return false;

        LocalDate hoy = LocalDate.now();

        if (hoy.isAfter(cuenta.getFechaExpiracionBeneficio())) {
            cuenta.setBeneficioActivo(null);
            cuenta.setDescuento(0);
            cuenta.setFechaExpiracionBeneficio(null);
            return false;
        }
        return true;
    }

    public void iniciarVerificadorBeneficios() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
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
