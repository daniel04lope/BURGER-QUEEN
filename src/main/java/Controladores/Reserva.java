package Controladores;

import javafx.scene.control.TextArea;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    @FXML
    private Button reservaadmin;
    @FXML
    private Button menuadmin;
    @FXML
    private Button usuariosadmin;
    @FXML
    private Button pedidosadmin;
    @FXML
    private Accordion administradores;
    @FXML
    private TitledPane titledpaneadmin;
    @FXML
    private VBox Vboxadmin;
    @FXML
    private Button botoncarrito;
    private boolean drawerVisible = false;
    private boolean Cerrardesplegar = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	Username.textProperty().bind(Login.bannerusuarioProperty());
    	
    	 if (Login.tipo.equals("administradores")) {
             administradores.setVisible(true);
             titledpaneadmin.setVisible(true);
             Vboxadmin.setVisible(true);
             usuariosadmin.setDisable(false);
             botoncarrito.setVisible(false);
             pedidosadmin.setDisable(false);
             menuadmin.setDisable(false);
             reservaadmin.setDisable(false);
         }

         if (Login.tipo.equals("empleados")) {
             administradores.setVisible(true);
             titledpaneadmin.setVisible(true);
             Vboxadmin.setVisible(true);
             botoncarrito.setVisible(false);
             System.out.println("llegue");

             // Verificar permisos para cada botón
             try {
 				if (permisos(2, "lectura") == 1) {
 				    reservaadmin.setDisable(false);
 				} else {
 				    reservaadmin.setDisable(true);
 				}
 			} catch (SQLException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}

             try {
 				if (permisos(1, "lectura") == 1) {
 				    menuadmin.setDisable(false);
 				} else {
 				    menuadmin.setDisable(true);
 				}
 			} catch (SQLException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}

             try {
 				if (permisos(3, "lectura") == 1) {
 				    pedidosadmin.setDisable(false);
 				} else {
 				    pedidosadmin.setDisable(true);
 				}
 			} catch (SQLException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
         }

         if (Login.tipo.equals("usuarios")) {
             administradores.setVisible(false);
             titledpaneadmin.setVisible(false);
             Vboxadmin.setVisible(false);
         }

         try {
             System.out.println(permisos(1, "lectura"));
         } catch (SQLException e) {
             e.printStackTrace();
         }
    
    	
    }
    
    public void Despliega() {
        Cerrardesplegar = !Cerrardesplegar;
        drawerVisible = !drawerVisible;
        Cerrar.setVisible(Cerrardesplegar);
        drawer.setVisible(drawerVisible);
    }
    
    public void Gestion_usuarios() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
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
    
    public void Ubicacion() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Ubicacion.fxml"));
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
    public void Horarios() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Horarios.fxml"));
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

    public void insertReserva() {
      
    	if (Login.datos_login.getIdUsuario() == 0) {
            Mostrar_Login();
           
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

        String nombreCliente = Login.datos_login.getNombre();
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

 
    
    
    public int permisos(int nombreModulo, String tipoPermiso) throws SQLException {
        String sql = "SELECT " + tipoPermiso + " FROM permisos WHERE id_empleado = ? AND id_modulo = ?";
        int valor = 0;

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, Login.datos_login.getIdUsuario());
            sentencia.setInt(2, nombreModulo);
            
            System.out.println("Cadena: " + sentencia);
            
            ResultSet ejecuta = sentencia.executeQuery();

            if (ejecuta.next()) {
                valor = ejecuta.getInt(tipoPermiso);
                System.out.println("Valor: " + valor);
            } else {
                System.out.println("No valor encontrado id_empleado = " + Login.datos_login.getIdUsuario() + " and id_modulo = " + nombreModulo);
            }
        }

        return valor;
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
            cerrar();
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
