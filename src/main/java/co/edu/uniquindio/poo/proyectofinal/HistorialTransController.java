package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Transaccion;
import model.Cliente;
import model.Cuenta;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class HistorialTransController {

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
        Stage stage = (Stage) tablaTrans.getScene().getWindow();
        stage.close();
    }
}
