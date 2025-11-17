package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class ClienteController {

    @FXML private Button btnNuevaCuenta;
    @FXML private Button btnIniciarSesion;
    @FXML private Label lblNombreCliente;

    @FXML
    public void initialize() {

        btnNuevaCuenta.setOnAction(e -> abrirVentana("client-register.fxml", btnNuevaCuenta));
        btnIniciarSesion.setOnAction(e -> abrirVentana("Login.fxml", btnIniciarSesion));
    }

    public void setNombreCliente(String nombre) {
        lblNombreCliente.setText("Bienvenido, " + nombre);
    }

    private void abrirVentana(String ruta, Button boton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load();

            Stage stage = (Stage) boton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

