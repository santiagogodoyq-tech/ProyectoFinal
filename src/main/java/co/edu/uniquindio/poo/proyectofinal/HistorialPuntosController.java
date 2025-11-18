package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.util.LinkedList;

public class HistorialPuntosController {

    @FXML
    private TableView<RegistroPuntos> tablaPuntos;

    @FXML
    private TableColumn<RegistroPuntos, String> columnaFecha;

    @FXML
    private TableColumn<RegistroPuntos, String> columnaTransaccion;

    @FXML
    private TableColumn<RegistroPuntos, Integer> columnaPuntos;

    @FXML
    private Button btnVolver;

    private Cliente clienteActual;
    private Empresa empresa;
    private Cuenta cuenta;

    @FXML
    public void initialize() {

        this.clienteActual = AppData.clienteActual;
        this.empresa = AppData.empresa;

        if (clienteActual == null || empresa == null) {
            mostrar("Error", "No hay datos suficientes.");
            return;
        }

        LinkedList<Cuenta> lista = clienteActual.getListaCuentas();
        this.cuenta = lista.stream().findFirst().orElse(null);

        configurarColumnas();
        cargarHistorial();

        btnVolver.setOnAction(e -> volver());
    }

    private void configurarColumnas() {
        columnaFecha.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getTransaccion().getFecha().toString()
                ));

        columnaTransaccion.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getTransaccion().getId()
                ));

        columnaPuntos.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(
                        c.getValue().getPuntosMonedero()
                ).asObject());
    }

    private void cargarHistorial() {

        if (cuenta == null) {
            mostrar("Error", "No se encontró la cuenta.");
            return;
        }

        tablaPuntos.getItems().clear();

        LinkedList<RegistroPuntos> registros = cuenta.getListaRegistroPuntos();

        tablaPuntos.getItems().addAll(registros);
    }

    private void volver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cuenta.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            mostrar("Error", "No se pudo volver a la cuenta.");
        }
    }

    private void mostrar(String t, String m) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(t);
        alert.setContentText(m);
        alert.show();
    }
}
