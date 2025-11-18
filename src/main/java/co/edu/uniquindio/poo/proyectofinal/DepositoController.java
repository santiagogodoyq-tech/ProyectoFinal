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
    LinkedList<Cuenta> listaCuenta = clienteActual.getListaCuentas();
    Cuenta cuenta = listaCuenta.stream().findFirst().orElse(null);
    // ============================
    //   RECIBIR CLIENTE + EMPRESA
    // ============================
    public void setData(Cliente cliente, Empresa empresa) {
        this.clienteActual = AppData.clienteActual;
        LinkedList<Cuenta> listaCuenta = cliente.getListaCuentas();
        Cuenta cuenta = listaCuenta.stream().findFirst().orElse(null);
        this.empresa = AppData.empresa;
    }

    @FXML
    public void initialize() {
        btnConfirmar.setOnAction(event -> realizarDeposito());
        btnVolver.setOnAction(event -> volver());
    }
//que es la combo box donde dice eso en el scen builder
    // ============================
    //   OBTENER MONEDERO PRINCIPAL
    // ============================
    private Monedero obtenerMonedero() {
        LinkedList<Monedero> listaMonedero = cuenta.getListaMonedero();
        String id = comboCuenta.getValue().equals("Monedero Diario") ? "1" : "2";
        Monedero monedero = null;
        if(comboCuenta == null){
            mostrarAlerta(Alert.AlertType.ERROR, "Se debe de selecionar un monedero.");
        } else if (comboCuenta.equals("Monedero Diario")) {
            monedero = listaMonedero.stream().filter(x->x.getId().equals(id)).findFirst().orElse(null);
        }else {
            monedero = listaMonedero.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
        }
        return monedero;
    }
//donde esta el obtener cuenta ahorros? es que recuerde que no es cuenta ahorros si no monedero o si era cuenta ahorros?, no es cuenta ahorros pero se supone que aqui hay un funcion definida para obtener el monedero ahorros que se llama obtener cuenta ahorros
private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
    Alert alert = new Alert(tipo);
    alert.setTitle("Información");
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}
    // ============================
    //       HACER DEPÓSITO
    // ============================
    public void realizarDeposito(){

        if (empresa == null || clienteActual == null) {
            mostrar("Error", "No hay datos suficientes.");
            return;
        }

        if (comboCuenta.getValue() == null) {
            mostrar("Advertencia", "Seleccione una cuenta.");
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

        if (comboCuenta.getValue().equals("Monedero Principal")) {
            Monedero mon = obtenerMonedero();
            if (mon != null) {
                String count = String.valueOf(AppData.count);
                AppData.empresa.depositar(monto,cuenta,LocalDate.now(),"1");
                mostrar("Éxito", "Depósito realizado.");
                AppData.count++;
            }
        } else {
            if (cuenta != null) {
                AppData.empresa.depositar(monto,cuenta, LocalDate.now(),"2");
                mostrar("Éxito", "Depósito realizado.");
            }
        }

        inputMonto.clear();
        comboCuenta.getSelectionModel().clearSelection();
    }

    private void volver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cuenta.fxml"));
            Scene scene = new Scene(loader.load());

            CuentaController controller = loader.getController();
 //que es setData?eso mismo estaba buscando, no lo necesitamos para eso estamos usando el appdata, aun que creo que eso significa que nos toca convertir todos los empresa a empresa appdata y todos los cliente en appdata.clienteactual, arreglado ya funciona sigamos con la siguiente scena o si quiere puede probar si funciona correctamente

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            mostrar("Error", "No se pudo cargar Cuenta.fxml");
        }
    }

    private void mostrar(String t, String m) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(t);
        alert.setContentText(m);
        alert.show();
    }
}
