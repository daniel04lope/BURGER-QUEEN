package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Gestion_usuarios implements Initializable {

    @FXML
    Text Username;
    @FXML
    private AnchorPane Panel_Desplegable;
    @FXML
    private Button Desplegable;
    @FXML
    private Button Cerrar;
    @FXML
    GridPane Listado;
    static int idtraspaso;

    private boolean Panel_Visible = false;
    private boolean Cerrardesplegar = false;

    public void carrito() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Carrito.fxml"));
            AnchorPane itemFocusPane = loader.load();
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

    public void Mostrar_Login() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
            Pane login = loader.load();
            Scene loginScene = new Scene(login, 450, 600);
            loginScene.setFill(Color.TRANSPARENT);
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);
            loginStage.setScene(loginScene);
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.setTitle("LOGIN");
            loginStage.show();
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
        loginStage.setResizable(false);
        loginStage.initStyle(StageStyle.DECORATED);
        loginStage.setScene(loginScene);
        loginStage.setTitle("Reservas");
        loginStage.show();
        cerrar();
    }
    public void ReservaAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/ReservaAdmin.fxml"));
        Pane registro = loader.load();
        Scene loginScene = new Scene(registro, 600, 500);
        loginScene.setFill(Color.TRANSPARENT);
        Stage loginStage = new Stage();
        loginStage.setResizable(false);
        loginStage.initStyle(StageStyle.DECORATED);
        loginStage.setScene(loginScene);
        loginStage.setTitle("Reservas");
        loginStage.show();
         cerrar();
    }

    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    public void Despliega() {
        Cerrardesplegar = !Cerrardesplegar;
        Panel_Visible = !Panel_Visible;
        Cerrar.setVisible(Cerrardesplegar);
        Panel_Desplegable.setVisible(Panel_Visible);
    }

    public void Carta() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
        Pane registro = loader.load();
        Scene loginScene = new Scene(registro, 600, 500);
        loginScene.setFill(Color.TRANSPARENT);
        Stage loginStage = new Stage();
        loginStage.setResizable(false);
        loginStage.initStyle(StageStyle.DECORATED);
        loginStage.setScene(loginScene);
        loginStage.setTitle("CARTA");
        loginStage.show();
        cerrar();
    }

    public void Pantalla_Principal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principal = loader.load();
        Scene principalScene = new Scene(principal, 600, 500);
        
        principalScene.setFill(Color.TRANSPARENT);
        Stage PrincipalStage = new Stage();
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("CARTA");
        PrincipalStage.show();
        cerrar();
    }

    public void Muestra_usuarios() throws SQLException {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sqlUsuarios = "SELECT id_empleado AS id, nombre, apellido, email, estado, 'Empleado' AS tipo FROM empleados UNION ALL SELECT id_admin AS id, nombre, apellido, email, estado, 'Administrador' AS tipo FROM administradores";
            PreparedStatement sentenciaUsuarios = conexion.prepareStatement(sqlUsuarios);
            ResultSet resultadoUsuarios = sentenciaUsuarios.executeQuery();
            int row = 0;

            while (resultadoUsuarios.next()) {
                int idUsuario = resultadoUsuarios.getInt("id");
                String nombre = resultadoUsuarios.getString("nombre");
                String apellido = resultadoUsuarios.getString("apellido");
                String email = resultadoUsuarios.getString("email");
                String estado = resultadoUsuarios.getString("estado");
                String tipo = resultadoUsuarios.getString("tipo");

                AnchorPane usuarioPanel = new AnchorPane();
                usuarioPanel.setPrefSize(450, 100);
                usuarioPanel.setStyle("-fx-background-color: A6234E; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: FFFFFF");

                Label nombreUsuario = new Label(nombre + " " + apellido + " (" + tipo + ")");
                nombreUsuario.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
                AnchorPane.setLeftAnchor(nombreUsuario, 10.0);
                AnchorPane.setTopAnchor(nombreUsuario, 10.0);
                usuarioPanel.getChildren().add(nombreUsuario);

                Label emailUsuario = new Label("Email: " + email);
                emailUsuario.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
                AnchorPane.setLeftAnchor(emailUsuario, 10.0);
                AnchorPane.setTopAnchor(emailUsuario, 35.0);
                usuarioPanel.getChildren().add(emailUsuario);

                Label estadoUsuario = new Label("Estado: " + estado);
                estadoUsuario.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
                AnchorPane.setLeftAnchor(estadoUsuario, 10.0);
                AnchorPane.setTopAnchor(estadoUsuario, 60.0);
                usuarioPanel.getChildren().add(estadoUsuario);

                Button botonModificar = new Button();
                botonModificar.setStyle("-fx-background-color: transparent;");
                ImageView iconoModificar = new ImageView(new Image("/lapiz.png"));
                iconoModificar.setFitWidth(20);
                iconoModificar.setFitHeight(20);
                iconoModificar.setPreserveRatio(true);
                botonModificar.setGraphic(iconoModificar);
                botonModificar.setUserData(idUsuario);
                botonModificar.setOnAction(e -> {
                    idtraspaso = (int) botonModificar.getUserData();
                    muestraeditar();
                });
                AnchorPane.setRightAnchor(botonModificar, 80.0);
                AnchorPane.setBottomAnchor(botonModificar, 10.0);
                usuarioPanel.getChildren().add(botonModificar);

                Button botonEliminar = new Button();
                botonEliminar.setStyle("-fx-background-color: transparent;");
                ImageView iconoEliminar = new ImageView(new Image("/basura.png"));
                iconoEliminar.setFitWidth(20);
                iconoEliminar.setFitHeight(20);
                iconoEliminar.setPreserveRatio(true);
                botonEliminar.setGraphic(iconoEliminar);
                botonEliminar.setUserData(idUsuario);
                botonEliminar.setOnAction(e -> {
                    eliminarUsuario((int) botonEliminar.getUserData());
                    try {
                        Muestra_usuarios();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });
                AnchorPane.setRightAnchor(botonEliminar, 10.0);
                AnchorPane.setBottomAnchor(botonEliminar, 10.0);
                usuarioPanel.getChildren().add(botonEliminar);

                Listado.add(usuarioPanel, 0, row);
                GridPane.setMargin(usuarioPanel, new Insets(10, 0, 10, 0));
                row++;
                GridPane.setVgrow(usuarioPanel, javafx.scene.layout.Priority.ALWAYS);
            }
            resultadoUsuarios.close();
            sentenciaUsuarios.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Username.textProperty().bind(Login.bannerusuarioProperty());
        try {
            Muestra_usuarios();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void nuevo_usuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Nuevo_usuarios.fxml"));
            AnchorPane itemFocusPane = loader.load();
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

    private void eliminarUsuario(int idUsuario) {
        String sqlEliminarEmpleado = "DELETE FROM empleados WHERE id_empleado = ?";
        String sqlEliminarAdmin = "DELETE FROM administradores WHERE id_admin = ?";
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            try (PreparedStatement stmt = conexion.prepareStatement(sqlEliminarEmpleado)) {
                stmt.setInt(1, idUsuario);
                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas == 0) {
                    try (PreparedStatement stmtAdmin = conexion.prepareStatement(sqlEliminarAdmin)) {
                        stmtAdmin.setInt(1, idUsuario);
                        stmtAdmin.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void muestraeditar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Editar_usuarios.fxml"));
            AnchorPane itemFocusPane = loader.load();
            Stage itemFocusStage = new Stage();
            itemFocusStage.initStyle(StageStyle.TRANSPARENT);
            itemFocusStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(itemFocusPane, 800, 623);
            itemFocusStage.setScene(scene);
            itemFocusStage.setTitle("EDITAR USUARIOS");
            itemFocusStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
