package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Cliente;

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
        btnRegistrar.setOnAction(event -> registrarCliente());
    }

    private void registrarCliente() {

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

        Cliente nuevoCliente = new Cliente(id, nombre, email, celular, contrasena);

        mostrarAlerta(Alert.AlertType.INFORMATION,
                "Cliente registrado correctamente:\n" + nombre);

        inputId.clear();
        inputNombre.clear();
        inputEmail.clear();
        inputCelular.clear();
        inputContrasena.clear();
    }


    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
