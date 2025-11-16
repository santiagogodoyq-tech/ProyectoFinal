package model;

import java.util.LinkedList;

public abstract class Cuenta {
    protected Cliente cliente;
    protected String codigo;
    protected String nombre;
    protected LinkedList<Monedero> ListaMonedero;
    protected int puntosMonedero;
    protected LinkedList<Transaccion> ListaTransacciones;
    protected double descuento;
    protected double porcentajeTran;
    protected String beneficioActivo;
    protected LocalDate fechaExpiracionBeneficio;


    public Cuenta(Cliente cliente, String codigo, String nombre) {
        this.cliente = cliente;
        this.codigo = codigo;
        this.nombre = nombre;
        this.ListaMonedero = new LinkedList<>();
        this.puntosMonedero = 0;
        this.ListaTransacciones = new LinkedList<>();
        this.descuento = 0;
        this.porcentajeTran = asignarPorcentaje();
    }

    public double getPorcentajeTran() {
        return porcentajeTran;
    }

    public void setPorcentajeTran(double porcentajeTran) {
        this.porcentajeTran = porcentajeTran;
    }

    public LinkedList<Monedero> getListaMonedero() {
        return ListaMonedero;
    }

    public void setListaMonedero(LinkedList<Monedero> listaMonedero) {
        ListaMonedero = listaMonedero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public int getPuntosMonedero() {
        return puntosMonedero;
    }

    public void setPuntosMonedero(int puntosMonedero) {
        this.puntosMonedero = puntosMonedero;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LinkedList<Transaccion> getListaTransacciones() {
        return ListaTransacciones;
    }

    public void setListaTransacciones(LinkedList<Transaccion> listaTransacciones) {
        ListaTransacciones = listaTransacciones;
    }

    public String getBeneficioActivo() {
        return beneficioActivo;
    }

    public void setBeneficioActivo(String beneficioActivo) {
        this.beneficioActivo = beneficioActivo;
    }

    public LocalDate getFechaExpiracionBeneficio() {
        return fechaExpiracionBeneficio;
    }

    public void setFechaExpiracionBeneficio(LocalDate fechaExpiracionBeneficio) {
        this.fechaExpiracionBeneficio = fechaExpiracionBeneficio;
    }

    public abstract void aplicarDescuento ();
    public abstract double asignarPorcentaje();
}
