package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;

public class CuentaController {

    // ====== LABELS PRINCIPALES ======
    @FXML private Label labelNombreCliente;
    @FXML private Label labelSaldo;
    @FXML private Label labelRango;
    @FXML private Label labelPuntos;

    // ====== CAMPOS DE OPERACIONES ======
    @FXML private TextField inputDepositar;
    @FXML private TextField inputRetirar;
    @FXML private TextField inputTransferirMonto;
    @FXML private TextField inputTransferirId;

    // ====== HISTORIAL ======
    @FXML private TextArea areaHistorial;
    @FXML private TextArea areaHistorialPuntos;

    private Cliente cliente;
    private Cuenta cuenta;

    // ===============================================================
    // ESTE MÉTODO LO LLAMAS DESPUÉS DE CARGAR EL FXML DESDE Login
    // ===============================================================
    public void setClienteYCuenta(Cliente cliente, Cuenta cuenta) {
        this.cliente = cliente;
        this.cuenta = cuenta;
        actualizarVista();
    }

    @FXML
    public void initialize() {
        // IMPORTANTE:
        // No puedes usar cliente o cuenta aquí porque todavía no existen.
        // Solo se inicializa cuando setClienteYCuenta es llamado.
    }

    // ===============================================================
    //             ACTUALIZAR DATOS EN PANTALLA
    // ===============================================================
    private void actualizarVista() {
        labelNombreCliente.setText(cliente.getNombre());
        labelSaldo.setText("$ " + cuenta.getSaldo());
        labelRango.setText(cuenta.getRango().getNombre());
        labelPuntos.setText("" + cuenta.getPuntos());

        cargarHistorial();
        cargarHistorialPuntos();
    }

    private void cargarHistorial() {
        StringBuilder sb = new StringBuilder();
        cuenta.getHistorial().forEach(t -> sb.append(t).append("\n"));
        areaHistorial.setText(sb.toString());
    }

    private void cargarHistorialPuntos() {
        StringBuilder sb = new StringBuilder();
        cuenta.getHistorialPuntos().forEach(p -> sb.append(p).append("\n"));
        areaHistorialPuntos.setText(sb.toString());
    }

    // ===============================================================
    //                      DEPÓSITO
    // ===============================================================
    @FXML
    private void depositar() {
        try {
            double monto = Double.parseDouble(inputDepositar.getText());

            if (monto <= 0) {
                alert("Error", "El monto debe ser mayor que 0");
                return;
            }

            cuenta.depositar(monto);
            actualizarVista();

        } catch (NumberFormatException e) {
            alert("Error", "Ingrese un número válido");
        }
    }

    // ===============================================================
    //                      RETIRO
    // ===============================================================
    @FXML
    private void retirar() {
        try {
            double monto = Double.parseDouble(inputRetirar.getText());

            if (monto <= 0) {
                alert("Error", "El monto debe ser mayor que 0");
                return;
            }

            if (!cuenta.retirar(monto)) {
                alert("Error", "No hay saldo suficiente");
                return;
            }

            actualizarVista();

        } catch (NumberFormatException e) {
            alert("Error", "Ingrese un número válido");
        }
    }

    // ===============================================================
    //                      TRANSFERENCIA
    // ===============================================================
    @FXML
    private void transferir() {

        try {
            double monto = Double.parseDouble(inputTransferirMonto.getText());
            String idDestino = inputTransferirId.getText();

            Cliente destino = AppData.empresa.buscarCliente(idDestino);

            if (destino == null) {
                alert("Error", "El cliente destino no existe");
                return;
            }

            if (!cuenta.transferir(destino.getCuenta(), monto)) {
                alert("Error", "Saldo insuficiente");
                return;
            }

            actualizarVista();

        } catch (NumberFormatException e) {
            alert("Error", "Monto inválido");
        }
    }

    // ===============================================================
    //                      CANJE DE PUNTOS
    // ===============================================================
    @FXML
    private void abrirCanjePuntos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("canje.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Canje de puntos");
            stage.show();

            // Si necesitas pasar el cliente:
            // CanjeController controller = loader.getController();
            // controller.setCliente(cliente);

        } catch (IOException e) {
            alert("Error", "No se pudo abrir la ventana de canje");
        }
    }

    // ===============================================================
    //                      CERRAR SESIÓN
    // ===============================================================
    @FXML
    private void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inicio.fxml"));
            Stage stage = (Stage) labelNombreCliente.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            alert("Error", "No se pudo cerrar sesión");
        }
    }

    // ===============================================================
    //                      ALERTAS
    // ===============================================================
    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setContentText(msg);
        a.show();
    }
}
