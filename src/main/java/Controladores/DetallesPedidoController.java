package Controladores;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Modelos.DetallePedido;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DetallesPedidoController implements Initializable {

    @FXML
    private TableView<DetallePedido> tablaDetalles;

    @FXML
    private Label Id;

    @FXML
    private TableColumn<DetallePedido, String> columnaNombre;

    @FXML
    private TableColumn<DetallePedido, Integer> columnaCantidad;

    @FXML
    private Button cerrarVentana;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar las columnas de la tabla para mostrar los detalles del pedido.
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }
    
    public void setDetalles(List<DetallePedido> detalles, int idPedido) {
        // Establecer el ID del pedido en la etiqueta correspondiente.
        Id.setText("" + idPedido);

        if (detalles != null && !detalles.isEmpty()) {
            // Agrupar los productos por nombre y sumar las cantidades.
            Map<String, DetallePedido> productosAgrupados = detalles.stream()
                .collect(Collectors.toMap(
                    DetallePedido::getNombre,
                    p -> new DetallePedido(p.getIdPedido(), p.getNombre(), p.getCantidad(), p.getPrecioUnitario()),
                    (p1, p2) -> {
                        p1.setCantidad(p1.getCantidad() + p2.getCantidad());
                        return p1;
                    }
                ));

            // Convertir los valores agrupados en una lista y asignarla a la tabla.
            tablaDetalles.setItems(FXCollections.observableArrayList(productosAgrupados.values()));
        } else {
            System.out.println("La lista de detalles está vacía o es nula.");
        }
    }

    @FXML
    public void cerrarVentana() {
        // Cerrar la ventana actual.
        Stage stage = (Stage) cerrarVentana.getScene().getWindow();
        stage.close();
    }
}