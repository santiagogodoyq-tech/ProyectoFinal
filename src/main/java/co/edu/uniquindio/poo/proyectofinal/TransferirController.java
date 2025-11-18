package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;

public class TransferirController {

    @FXML private ComboBox<String> comboCuentaTipo;
    @FXML private TextField inputIdDestino;
    @FXML private TextField inputMonto;
    @FXML private Label labelEstado;
    @FXML private Button btnVolver;
    @FXML private Button btnTransferir;

    @FXML private CheckBox checkProgramar;
    @FXML private DatePicker inputFecha;
    @FXML private HBox boxFecha;

    private Empresa empresa;
    private Cliente cliente;
    private Cuenta cuenta;

    private Cuenta cuentaDestino;

    @FXML
    public void initialize() {
        this.empresa = AppData.empresa;
        this.cliente = AppData.clienteActual;

        if (cliente == null) {
            labelEstado.setText("Error: sin cliente.");
            return;
        }

        LinkedList<Cuenta> lista = cliente.getListaCuentas();
        cuenta = lista.stream().findFirst().orElse(null);

        if (cuenta == null) {
            labelEstado.setText("No se encontró la cuenta del cliente.");
        }

        boxFecha.setVisible(false);
        boxFecha.setManaged(false);
        btnTransferir.setOnAction(event -> {transferir();});
        btnVolver.setOnAction(event -> {volver();});
    }

    @FXML
    private void toggleProgramar() {
        boolean activo = checkProgramar.isSelected();
        boxFecha.setVisible(activo);
        boxFecha.setManaged(activo);
    }

    private void buscarCuentaDestino() {
        String id = inputIdDestino.getText();
        if (id == null || id.isEmpty()) {
            cuentaDestino = null;
            return;
        }
        cuentaDestino = AppData.empresa.getListaCuentas().stream().filter(x -> x.getCodigo().equals(id)).findFirst().orElse(null);
    }

    @FXML
    private void transferir() {

        buscarCuentaDestino();

        if (cuentaDestino == null) {
            labelEstado.setText("Cuenta destino no encontrada.");
            return;
        }

        if (cuentaDestino.equals(cuenta)) {
            labelEstado.setText("No puede transferir a la misma cuenta.");
            return;
        }

        String idMonedero = comboCuentaTipo.getValue().equals("Monedero Diario") ? "1" : "2";
        LinkedList<Monedero> listaMonederos = cuenta.getListaMonedero();
        Monedero mon = listaMonederos.stream().filter(x -> x.getId().equals(idMonedero))
                .findFirst().orElse(null);

        if (mon == null) {
            labelEstado.setText("No se encontró el monedero.");
            return;
        }

        double monto;

        try { monto = Double.parseDouble(inputMonto.getText()); }
        catch (Exception e) {
            labelEstado.setText("Monto inválido.");
            return;
        }

        if (monto <= 0) {
            labelEstado.setText("Monto debe ser mayor a 0.");
            return;
        }

        if (mon.getSaldo() < monto) {
            labelEstado.setText("Saldo insuficiente.");
            return;
        }

        if (checkProgramar.isSelected()) {

            if (inputFecha.getValue() == null) {
                labelEstado.setText("Seleccione una fecha.");
                return;
            }

            LocalDate fechaProg = inputFecha.getValue();

            empresa.transferenciaProgramada(
                    monto,
                    cuenta,
                    cuentaDestino,
                    fechaProg,
                    idMonedero,
                    idMonedero
            );

            labelEstado.setText("Transferencia programada para: " + fechaProg);
            return;
        }

        empresa.transferir(
                monto,
                cuenta,
                cuentaDestino,
                LocalDate.now(),
                idMonedero,
                idMonedero
        );

        labelEstado.setText("Transferencia realizada con éxito.");
    }

    @FXML
    private void volver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cuenta.fxml"));
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
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
