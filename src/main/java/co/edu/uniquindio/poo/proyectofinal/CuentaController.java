package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.util.LinkedList;

public class CuentaController {

    @FXML private Label lblNombreCliente;
    @FXML private Label lblRango;

    @FXML private Label lblSaldoPrincipal;
    @FXML private Label lblSaldoAhorros;

    @FXML private Button btnCerrarSesion;
    @FXML private Button btnDepositar;
    @FXML private Button btnRetirar;
    @FXML private Button btnTransferir;
    @FXML private Button btnHistorialTransacciones;
    @FXML private Button btnHistorialPuntos;
    @FXML private Button btnCanjearPuntos;

    private Cliente cliente;
    private Cuenta cuenta;

    @FXML
    public void initialize() {
        btnDepositar.setOnAction(e -> abrirVentanaDeposito());
        btnRetirar.setOnAction(e -> abrirVentanaRetiro());
        btnTransferir.setOnAction(e -> abrirVentanaTransferencia());
        btnHistorialTransacciones.setOnAction(e -> abrirHistorialTransacciones());
        btnHistorialPuntos.setOnAction(e -> abrirHistorialPuntos());
        btnCanjearPuntos.setOnAction(e -> abrirCanje());
        btnCerrarSesion.setOnAction(e -> cerrarSesion());
    }

    public void setCliente(Cliente cliente) {

        this.cliente = cliente;
        cliente = AppData.clienteActual;

        if (cliente == null) {
            mostrarAlerta("Error grave", "No hay cliente en sesión.");
            return;
        }else {  LinkedList<Cuenta> listaCuenta = cliente.getListaCuentas();
        this.cuenta = listaCuenta.stream().findFirst().orElse(null);}
        actualizarRango();
        actualizarVista();
    }

    private void actualizarVista() {
        LinkedList<Cuenta> listaCuenta = cliente.getListaCuentas();
        Cuenta cuenta = listaCuenta.stream().findFirst().orElse(null);

        lblNombreCliente.setText(cliente.getNombre());

        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        String id = "1";
        String id1 = "2";

        lblSaldoPrincipal.setText(String.format("$ %.2f",
                listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().get().getSaldo()));

        lblSaldoAhorros.setText(String.format("$ %.2f",
                listaMonedero.stream().filter(x -> x.getId().equals(id1)).findFirst().get().getSaldo()));
    }


    private void abrirVentanaDeposito() {
        abrirVentana("DepositView.fxml", "Depositar Dinero");
    }

    private void abrirVentanaRetiro() {
        abrirVentana("retirar.fxml", "Retirar Dinero");
    }

    private void abrirVentanaTransferencia() {
        abrirVentana("transferir.fxml", "Transferir Dinero");
    }

    private void abrirHistorialTransacciones() {
        abrirVentana("historialTransacciones.fxml", "Historial de Transacciones");
    }

    private void abrirHistorialPuntos() {
        abrirVentana("HistorialPuntos.fxml", "Historial de Puntos");
    }

    private void abrirCanje() {
        abrirVentana("canjear_puntos.fxml", "Canje de Puntos");
    }

    private void abrirVentana(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/proyectofinal/"+fxml));
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana: " + fxml);
        }
    }

    private void cerrarSesion() {
        try {
            AppData.clienteActual = null;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("inicio.fxml"));
            Stage stage = (Stage) lblNombreCliente.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cerrar sesión.");
        }
    }
    private void mostrarAlerta(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
