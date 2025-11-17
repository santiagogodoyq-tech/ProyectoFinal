package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import javax.swing.*;
import java.io.IOException;

public class ClienteController {

    @FXML private Button btnNuevaCuenta;
    @FXML private Button btnIniciarSesion;
    @FXML private Label labelcampo;

    @FXML
    public void initialize() {
        if (AppData.clienteActual != null) {
            labelcampo.setText("Bienvenido/a, " + AppData.clienteActual.getNombre());
        }
        btnNuevaCuenta.setOnAction(e -> abrirVentana("client-register.fxml", btnNuevaCuenta));
        btnIniciarSesion.setOnAction(e -> abrirVentana("Login.fxml", btnIniciarSesion));
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

