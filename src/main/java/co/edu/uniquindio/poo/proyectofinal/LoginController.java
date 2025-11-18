package co.edu.uniquindio.poo.proyectofinal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;

public class LoginController {

    @FXML private TextField inputDocumento;
    @FXML private PasswordField inputContrasena;
    @FXML private Button btnLogin;
    @FXML private Button btnVolver;

    private Empresa empresa;
    private Stage stage;

    public void inicializar(Empresa empresa, Stage stage) {
        this.empresa = empresa;
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        btnLogin.setOnAction(e -> {
            try {
                login();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnVolver.setOnAction(e -> volver());
    }

    private void login() throws IOException {

        String documento = inputDocumento.getText();
        String contraseña = inputContrasena.getText();

        if (documento.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        Cliente cuenta = AppData.empresa.buscarCliente(documento).orElse(null);

        if (cuenta == null) {
            mostrarAlerta("Error", "No existe ninguna cuenta con ese documento.");
            return;
        }

        if (!cuenta.getContraseña().equals(contraseña)) {
            mostrarAlerta("Error", "La contraseña es incorrecta.");
            return;
        }
        Cliente cliente = AppData.empresa.buscarCliente(documento).orElse(null);

        if (cliente != null) {
            AppData.clienteActual = cliente;
        }

        mostrarAlerta("Bienvenido", "Inicio de sesión exitoso.");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cuenta.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    private void iniciarSesion() throws IOException {

        String nombreCliente = "";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("cuenta.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    private void volver() {
        try {
            Stage stageAct = (Stage) btnVolver.getScene().getWindow(); // sirve cualquiera
            stageAct.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inicio.fxml"));
            Scene scene = new Scene(loader.load());

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

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
