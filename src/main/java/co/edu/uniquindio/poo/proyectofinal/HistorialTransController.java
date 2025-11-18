package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Transaccion;
import model.Cliente;
import model.Cuenta;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class HistorialTransController {
    @FXML private Button btnVolver;
    @FXML private TableView<Transaccion> tablaTrans;
    @FXML private TableColumn<Transaccion, String> colId;
    @FXML private TableColumn<Transaccion, String> colFecha;
    @FXML private TableColumn<Transaccion, String> colMonto;
    @FXML private TableColumn<Transaccion, String> colTipo;
    @FXML private TableColumn<Transaccion, String> colCuenta;

    private Cliente cliente;
    private Cuenta cuenta;

    @FXML
    public void initialize() {

        cliente = AppData.clienteActual;

        if (cliente == null) {
            System.out.println("ERROR: No hay cliente.");
            return;
        }

        cuenta = cliente.getListaCuentas().stream().findFirst().orElse(null);

        if (cuenta == null) {
            System.out.println("ERROR: cliente sin cuentas.");
            return;
        }
        btnVolver.setOnAction(e -> volver());
        configurarColumnas();
        cargarTransacciones();
    }

    private void configurarColumnas() {

        colId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));

        colFecha.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                ));

        colMonto.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty("$" + data.getValue().getMonto()));

        colTipo.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));

        colCuenta.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(AppData.clienteActual.getListaCuentas().stream().findFirst().get().getCodigo()));
    }

    private void cargarTransacciones() {

        LinkedList<Transaccion> lista = AppData.empresa.getListaTransacciones();

        if (lista == null) {
            System.out.println("NO HAY TRANSACCIONES");
            return;
        }

        tablaTrans.getItems().setAll(lista);
    }

    @FXML
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
