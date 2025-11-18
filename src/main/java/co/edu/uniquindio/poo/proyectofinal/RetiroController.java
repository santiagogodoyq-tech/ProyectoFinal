package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;

public class RetiroController {

    @FXML
    private ComboBox<String> comboCuenta;

    @FXML
    private TextField inputMonto;

    @FXML
    private Button btnConfirmar;

    @FXML
    private Button btnVolver;

    private Cliente clienteActual;
    private Empresa empresa;
    private Cuenta cuenta;

    @FXML
    public void initialize() {
        this.clienteActual = AppData.clienteActual;
        this.empresa = AppData.empresa;

        if (clienteActual != null) {
            LinkedList<Cuenta> listaCuenta = clienteActual.getListaCuentas();
            this.cuenta = listaCuenta.stream().findFirst().orElse(null);
        }

        btnConfirmar.setOnAction(event -> realizarRetiro());
        btnVolver.setOnAction(event -> volver());
    }

    private Monedero obtenerMonedero() {

        if (cuenta == null) {
            mostrar("Error", "No se encontró ninguna cuenta.");
            return null;
        }

        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        String idDiario = "1";
        String idAhorro = "2";

        if (comboCuenta.getValue() == null) {
            mostrar("Advertencia", "Debe seleccionar un monedero.");
            return null;
        }

        Monedero mon = null;

        if (comboCuenta.getValue().equals("Monedero Diario")) {
            mon = listaMonedero.stream()
                    .filter(x -> x.getId().equals(idDiario))
                    .findFirst().orElse(null);
        } else {
            mon = listaMonedero.stream()
                    .filter(x -> x.getId().equals(idAhorro))
                    .findFirst().orElse(null);
        }

        if (mon == null) {
            mostrar("Error", "No se encontró el monedero seleccionado.");
        }

        return mon;
    }

    public void realizarRetiro() {

        if (empresa == null || clienteActual == null || cuenta == null) {
            mostrar("Error", "No hay datos suficientes.");
            return;
        }

        if (comboCuenta.getValue() == null) {
            mostrar("Advertencia", "Seleccione un monedero.");
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(inputMonto.getText());
        } catch (Exception e) {
            mostrar("Error", "Monto inválido.");
            return;
        }

        if (monto <= 0) {
            mostrar("Error", "El monto debe ser mayor a 0.");
            return;
        }

        Monedero mon = obtenerMonedero();
        if (mon == null) return;

        if (mon.getSaldo() < monto) {
            mostrar("Error", "Saldo insuficiente.");
            return;
        }

        String id = comboCuenta.getValue().equals("Monedero Diario") ? "1" : "2";

        AppData.empresa.retirar(monto, cuenta, LocalDate.now(), id);

        mostrar("Éxito", "Retiro realizado con éxito.");

        inputMonto.clear();
        comboCuenta.getSelectionModel().clearSelection();
        volver();
    }

    private void volver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cuenta.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            mostrar("Error", "No se pudo cargar Cuenta.fxml");
        }
    }

    private void mostrar(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.show();
    }
}
