package co.edu.uniquindio.poo.proyectofinal;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

public class LoginController {

    @FXML private TextField inputDocumento;
    @FXML private PasswordField inputContrasena;
    @FXML private Button btnLogin;

    private Empresa empresa;
    private Stage stage;

    public void inicializar(Empresa empresa, Stage stage) {
        this.empresa = empresa;
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        btnLogin.setOnAction(e -> login());
    }

    private void login() {

        String documento = inputDocumento.getText();
        String contraseña = inputContrasena.getText();

        if (documento.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        Cuenta cuenta = empresa.buscarCuenta(documento).orElse(null);

        if (cuenta == null) {
            mostrarAlerta("Error", "No existe ninguna cuenta con ese documento.");
            return;
        }

        if (!cuenta.getContraseña().equals(contraseña)) {
            mostrarAlerta("Error", "La contraseña es incorrecta.");
            return;
        }

        mostrarAlerta("Bienvenido", "Inicio de sesión exitoso.");

    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
