package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;

public class ClientController {

    @FXML
    private TextField inputId;

    @FXML
    private TextField inputNombre;

    @FXML
    private TextField inputEmail;

    @FXML
    private TextField inputCelular;

    @FXML
    private PasswordField inputContrasena;

    @FXML
    private Button btnRegistrar;

    @FXML
    public void initialize() {
        btnRegistrar.setOnAction(event -> {
            try {
                registrarCliente();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void registrarCliente() throws IOException {

        String id = inputId.getText().trim();
        String nombre = inputNombre.getText().trim();
        String email = inputEmail.getText().trim();
        String celular = inputCelular.getText().trim();
        String contrasena = inputContrasena.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || email.isEmpty() ||
                celular.isEmpty() || contrasena.isEmpty()) {

            mostrarAlerta(Alert.AlertType.ERROR, "Todos los campos son obligatorios.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            mostrarAlerta(Alert.AlertType.ERROR, "Correo electrónico no válido.");
            return;
        }

        if (!celular.matches("\\d+")) {
            mostrarAlerta(Alert.AlertType.ERROR, "El celular debe contener solo números.");
            return;
        }
        String count = String.valueOf(AppData.count);
        Cliente nuevoCliente = new Cliente(id, nombre, email, celular, contrasena);
        AppData.empresa.agregarCliente(nuevoCliente);
        Cuenta nuevoCuenta;
        nuevoCuenta = new Bronce(nuevoCliente, count, nombre, contrasena);
        AppData.empresa.agregarCuenta(nuevoCuenta);
        AppData.clienteActual.getListaCuentas().add(nuevoCuenta);
        AppData.count++;
        mostrarAlerta(Alert.AlertType.INFORMATION,
                AppData.empresa.getNombre());

        mostrarAlerta(Alert.AlertType.INFORMATION,
                "Cliente registrado correctamente:\n" + nombre);

        inputId.clear();
        inputNombre.clear();
        inputEmail.clear();
        inputCelular.clear();
        inputContrasena.clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("inicio.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) btnRegistrar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
