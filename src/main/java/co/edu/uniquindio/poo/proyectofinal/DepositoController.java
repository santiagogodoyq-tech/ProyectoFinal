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

public class DepositoController {

    @FXML private ComboBox<String> comboCuenta;
    @FXML private TextField inputMonto;
    @FXML private Button btnConfirmar;
    @FXML private Button btnVolver;

    private Cliente clienteActual;
    private Empresa empresa;
    private Cuenta cuenta;

    @FXML
    public void initialize() {

        // Cargar datos desde AppData
        clienteActual = AppData.clienteActual;
        empresa = AppData.empresa;

        if (clienteActual != null) {
            cuenta = clienteActual.getListaCuentas().stream().findFirst().orElse(null);
        }

        btnConfirmar.setOnAction(e -> realizarDeposito());
        btnVolver.setOnAction(e -> volver());
    }

    private Monedero obtenerMonedero() {

        if (comboCuenta.getValue() == null) {
            mostrar("Error", "Debe seleccionar un monedero.");
            return null;
        }

        String id = comboCuenta.getValue().equals("Monedero Diario") ? "1" : "2";

        return cuenta.getListaMonedero()
                .stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void realizarDeposito() {

        if (clienteActual == null || cuenta == null || empresa == null) {
            mostrar("Error", "No hay datos suficientes.");
            return;
        }

        if (comboCuenta.getValue() == null) {
            mostrar("Error", "Seleccione una cuenta.");
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
            mostrar("Error", "El monto debe ser mayor que cero.");
            return;
        }

        Monedero mon = obtenerMonedero();
        if (mon == null) {
            mostrar("Error", "No se encontró el monedero.");
            return;
        }

        String id = mon.getId();

        AppData.empresa.depositar(monto, cuenta, LocalDate.now(), id);

        mostrar("Éxito", "Depósito realizado con éxito.");

        inputMonto.clear();
        comboCuenta.getSelectionModel().clearSelection();
        volver();
    }

    private void volver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cuenta.fxml"));
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            mostrar("Error", "No se pudo cargar Cuenta.fxml");
        }
    }

    private void mostrar(String t, String m) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(t);
        alert.setHeaderText(null);
        alert.setContentText(m);
        alert.show();
    }
}
