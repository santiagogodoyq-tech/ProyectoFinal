package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Cuenta;
import model.Monedero;

import static co.edu.uniquindio.poo.proyectofinal.AppData.empresa;

public class CanjearPuntosController {

    @FXML
    private ComboBox<String> comboMonedero;

    @FXML
    private Label labelPuntos;

    @FXML
    private TextField inputPuntos;

    @FXML
    private Label labelResultado;

    private Cuenta cuenta; // ESTA se pasa desde la escena anterior

    // ============================
    //   MÉTODO PARA RECIBIR LA CUENTA
    // ============================
    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;

        // Mostrar puntos
        labelPuntos.setText("Puntos actuales: " + cuenta.getPuntosMonedero());

        // Llenar combo con los id de los monederos
        for (Monedero m : cuenta.getListaMonedero()) {
            comboMonedero.getItems().add(m.getId());
        }
    }

    // ============================
    //   CANJEAR BENEFICIO
    // ============================
    @FXML
    private void canjear() {
        try {
            String idMonedero = comboMonedero.getValue();
            int puntos = Integer.parseInt(inputPuntos.getText());

            if (idMonedero == null) {
                labelResultado.setText("Seleccione un monedero.");
                return;
            }

            String respuesta = AppData.empresa.canjearBeneficio(cuenta, puntos, idMonedero);

            labelResultado.setText(respuesta);

            // actualizar puntos visualmente
            labelPuntos.setText("Puntos actuales: " + cuenta.getPuntosMonedero());

        } catch (Exception e) {
            labelResultado.setText("Error: ingrese un número válido.");
        }
    }
}
