package co.edu.uniquindio.poo.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Cuenta;
import model.Monedero;

import java.util.LinkedList;

public class CanjearPuntosController {

    @FXML
    private ComboBox<String> comboMonedero;

    @FXML
    private ComboBox<Integer> comboBeneficio;

    @FXML
    private Label labelPuntos;

    @FXML
    private TextField inputPuntos;

    @FXML
    private Label labelResultado;
    protected Cuenta cuenta;


    public void setCuenta() {
        LinkedList<Cuenta> listaCuentas = AppData.clienteActual.getListaCuentas();
        cuenta = listaCuentas.stream().findFirst().orElse(null);
        labelPuntos.setText("Puntos actuales: " + cuenta.getPuntosMonedero());

        // Cargar monederos
        for (Monedero m : cuenta.getListaMonedero()) {
            comboMonedero.getItems().add(m.getId());
        }

        // ⭐ Cargar beneficios disponibles
        comboBeneficio.getItems().addAll(100, 500, 1000);
    }

    @FXML
    private void canjear() {
        try {
            String idMonedero = comboMonedero.getValue();
            Integer puntos = comboBeneficio.getValue();

            if (idMonedero == null) {
                labelResultado.setText("Seleccione un monedero.");
                return;
            }

            if (puntos == null) {
                labelResultado.setText("Seleccione un beneficio.");
                return;
            }

            String respuesta = AppData.empresa.canjearBeneficio(cuenta, puntos, idMonedero);

            labelResultado.setText(respuesta);

            labelPuntos.setText("Puntos actuales: " + cuenta.getPuntosMonedero());

        } catch (Exception e) {
            labelResultado.setText("Ocurrió un error.");
        }
    }
}
