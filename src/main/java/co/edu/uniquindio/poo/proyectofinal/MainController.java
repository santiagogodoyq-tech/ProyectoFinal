package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {

    @FXML private Button btnRegistrarCliente;
    @FXML private Button btnConsultarCliente;
    @FXML private Button btnRealizarTransaccion;
    @FXML private Button btnSalir;

    @FXML
    public void initialize() {
        btnRegistrarCliente.setOnAction(e -> abrirRegistrarCliente());
        btnConsultarCliente.setOnAction(e -> abrirConsultar());
        btnRealizarTransaccion.setOnAction(e -> abrirTransaccion());
        btnSalir.setOnAction(e -> System.exit(0));
    }

    private void abrirRegistrarCliente() {
        // Cargar escena RegistrarCliente.fxml
    }

    private void abrirConsultar() {
        // ...
    }

    private void abrirTransaccion() {
        // ...
    }
}

