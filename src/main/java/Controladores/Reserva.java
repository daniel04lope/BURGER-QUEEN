package Controladores;

import javafx.scene.control.TextArea;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.Notifications;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class Reserva implements Initializable {
    @FXML
    private Text Username;
    @FXML
    private AnchorPane drawer;
    @FXML
    private Button Desplegable;
    @FXML
    private Button Cerrar;
    @FXML
    private TextField Nombre;
    @FXML
    private DatePicker Fecha_Reserva;
    @FXML
    private TextField Hora;
    @FXML
    private TextField Personas;
    @FXML
    private TextArea Datos_Adicionales;

    private boolean drawerVisible = false;
    private boolean Cerrardesplegar = false;

    public void Despliega() {
        Cerrardesplegar = !Cerrardesplegar;
        drawerVisible = !drawerVisible;
        Cerrar.setVisible(Cerrardesplegar);
        drawer.setVisible(drawerVisible);
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

    public void Carta() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
        Pane registro = loader.load();
        Scene loginScene = new Scene(registro, 600, 500);
        loginScene.setFill(Color.TRANSPARENT);
        Stage loginStage = new Stage();
        loginStage.initStyle(StageStyle.DECORATED);
        loginStage.setScene(loginScene);
        loginStage.setTitle("CARTA");
        loginStage.show();
        cerrar();
    }

    public void insertReserva() {
        if (Nombre.getText() == null || Nombre.getText().trim().isEmpty()) {
            MostrarNotificaciones("Nombre es requerido", AlertType.ERROR);
            return;
        }

        if (Fecha_Reserva.getValue() == null) {
            MostrarNotificaciones("Fecha de reserva es requerida", AlertType.ERROR);
            return;
        }

        if (!Hora.getText().matches("\\d{2}:\\d{2}")) {
            MostrarNotificaciones("Hora debe estar en formato HH:mm", AlertType.ERROR);
            return;
        }

        if (!Personas.getText().matches("\\d+")) {
            MostrarNotificaciones("Número de personas debe ser un número", AlertType.ERROR);
            return;
        }

        String nombreCliente = Nombre.getText();
        LocalDate fechaReserva = Fecha_Reserva.getValue();
        LocalTime horaReserva = LocalTime.parse(Hora.getText());
        int numeroPersonas = Integer.parseInt(Personas.getText());
        String datosAdicionales = Datos_Adicionales.getText();

        String insertQuery = "INSERT INTO reservas (nombre_cliente, fecha_reserva, hora_reserva, numero_personas, notas, estado) " +
                             "VALUES (?, ?, ?, ?, ?, 'pendiente')";

        try (Connection conn = util.Conexiones.dameConexion("burger-queen");
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, nombreCliente);
            stmt.setDate(2, java.sql.Date.valueOf(fechaReserva));
            stmt.setTime(3, java.sql.Time.valueOf(horaReserva));
            stmt.setInt(4, numeroPersonas);
            stmt.setString(5, datosAdicionales);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                Platform.runLater(() -> MostrarNotificaciones("La reserva se ha registrado exitosamente.", AlertType.INFORMATION));
            } else {
                throw new SQLException("No se pudo guardar la reserva.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            MostrarNotificaciones("Ocurrió un error al intentar guardar la reserva.", AlertType.ERROR);
        }
    }

    private void MostrarNotificaciones(String message, AlertType alertType) {
        Notifications notification = Notifications.create().title(alertType == AlertType.ERROR ? "Error en el formulario" : "Reserva guardada").text(message).hideAfter(javafx.util.Duration.seconds(5));
        if (alertType == AlertType.ERROR) {
            notification.showError();
        } else {
            notification.showInformation();
            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Reserva guardada");
            successAlert.setHeaderText(null);
            successAlert.setContentText("La reserva se ha registrado exitosamente.");
            successAlert.initModality(Modality.APPLICATION_MODAL);
            successAlert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Username.setText(Login.bannerusuario);
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
    public void carrito () throws IOException {
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
}
