package Controladores;

import java.io.IOException;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import Modelos.Producto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Carta implements Initializable {
    @FXML
    private GridPane panel;
    @FXML
    Text Username;
    @FXML
    private AnchorPane Panel_Desplegable;
    @FXML
    private Button Desplegable;
    @FXML
    private Button Cerrar;

    private boolean drawerVisible = false;
    private boolean Cerrardesplegar = false;

    public void Despliega() {
        System.out.println("Funciona");
        Cerrardesplegar = !Cerrardesplegar;
        drawerVisible = !drawerVisible;
        Cerrar.setVisible(Cerrardesplegar);
        Panel_Desplegable.setVisible(drawerVisible);
    }

    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    public void Pantalla_Principal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principal = loader.load();

        Scene principalScene = new Scene(principal, 600, 500);
        principalScene.getStylesheets().add(getClass().getResource("Pantalla_Principal.css").toExternalForm());
        principalScene.setFill(Color.TRANSPARENT);

        Stage PrincipalStage = new Stage();
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("CARTA");
        PrincipalStage.show();
        cerrar();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Username.setText(Login.bannerusuario);
        CargarCarta();
    }

    public void CargarCarta() {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            System.out.println("Iniciando");
            String sql = "SELECT nombre, descripcion, precio, categoria, peso, ruta FROM carta";

            Statement sentencia = conexion.createStatement();
            ResultSet productos = sentencia.executeQuery(sql);

            int row = 0;
            int column = 0;

            while (productos.next()) {
                Producto productobjeto = new Producto();
                productobjeto.setNombre(productos.getString("nombre"));
                productobjeto.setPrecio(productos.getDouble("precio"));
                productobjeto.setCategoria(productos.getString("categoria"));
                productobjeto.setPeso(productos.getDouble("peso"));
                productobjeto.setDescripcion(productos.getString("descripcion"));
                productobjeto.setRuta(productos.getString("ruta"));

                Button btnProducto = new Button();
                VBox item = new VBox();
                btnProducto.setPrefSize(100, 100);
                btnProducto.setStyle("-fx-background-color: TRANSPARENT;");
                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/" + productobjeto.getRuta())));
                imageView.setFitHeight(100.0);
                imageView.setFitWidth(100.0);
                imageView.setPreserveRatio(true);
                btnProducto.setGraphic(imageView);

                Text nombre = new Text(productobjeto.getNombre());
                nombre.setWrappingWidth(100);
                nombre.setTextAlignment(TextAlignment.CENTER);

                item.getChildren().addAll(btnProducto, nombre);
                panel.add(item, column, row);
                btnProducto.setOnAction(event -> mostrarItemFocus(productobjeto));

                if (column == 2) {
                    column = 0;
                    row++;
                } else {
                    column++;
                }

                GridPane.setVgrow(item, javafx.scene.layout.Priority.ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Reserva() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Reservas.fxml"));
        Pane registro = loader.load();

        Scene loginScene = new Scene(registro, 600, 500);
        loginScene.setFill(Color.TRANSPARENT);

        Stage loginStage = new Stage();
        loginStage.initStyle(StageStyle.DECORATED);
        loginStage.setScene(loginScene);
        loginStage.setTitle("Reservas");
        loginStage.show();
        cerrar();
    }

    public void Mostrar_Login() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
            Pane login = loader.load();

            Scene loginScene = new Scene(login, 450, 600);
            loginScene.setFill(Color.TRANSPARENT);

            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);
            loginStage.setScene(loginScene);
            loginStage.setTitle("LOGIN");
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarItemFocus(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/ItemFocus.fxml"));
            AnchorPane itemFocusPane = loader.load();

            ItemFocus itemFocusController = loader.getController();
            itemFocusController.setProducto(producto);

            Stage itemFocusStage = new Stage();
            itemFocusStage.initStyle(StageStyle.TRANSPARENT);
            itemFocusStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(itemFocusPane, 800, 623);
            itemFocusStage.setScene(scene);

            itemFocusStage.setTitle("DETALLES DEL PRODUCTO");

            itemFocusStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
