package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainController {

    @FXML private Button btnRegistrarCliente;
    @FXML private Button btnRegistradoCliente;

    @FXML
    public void initialize() {

        btnRegistrarCliente.setOnAction(e -> {
            try {
                abrirRegistrarCliente();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        btnRegistradoCliente.setOnAction(e -> {
            try {
                abrirConsultar();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void abrirRegistrarCliente() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client-register.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) btnRegistrarCliente.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void abrirConsultar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) btnRegistradoCliente.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
