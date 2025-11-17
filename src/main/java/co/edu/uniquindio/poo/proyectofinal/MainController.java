package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
                throw new RuntimeException(ex);
            }
        });
        btnRegistradoCliente.setOnAction(e -> {
            try {
                abrirConsultar();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void abrirRegistrarCliente() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client-register.fxml"));
        Parent root = loader.load();

    }

    private void abrirConsultar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
    }
}

