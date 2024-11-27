package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pantalla_principal implements Initializable {
    
    @FXML
    Text Username;
    @FXML
    private AnchorPane Panel_Desplegable;
    @FXML
    private Button Desplegable;
    @FXML
    private Button Cerrar;
    @FXML
    private ImageView imagenperfil;

    private boolean Panel_Visible = false;
    private boolean Cerrardesplegar = false;

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Pantalla_principal inicializado correctamente");
       
        
        Username.textProperty().bind(Login.bannerusuarioProperty());

    }
    
    
    public String obtenerPermisosPorUsuarioYModulo(int idUsuario, String nombreModulo) {
        // Consulta SQL
        String query = """
            SELECT p.lectura, p.escritura, p.control_total 
            FROM permisos p
            INNER JOIN modulos m ON p.id_modulo = m.id_modulo
            INNER JOIN empleados e ON p.id_empleado = e.id_empleado
            INNER JOIN usuarios u ON e.id_empleado = u.id_usuario
            WHERE u.id_usuario = ? AND m.nombre_modulo = ?;
        """;

        try (Connection conn = util.Conexiones.dameConexion("burger-queen");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Establecer parámetros de la consulta
            stmt.setInt(1, idUsuario);
            stmt.setString(2, nombreModulo);

            try (ResultSet rs = stmt.executeQuery()) {
                StringBuilder permisos = new StringBuilder();

                // Recorrer los resultados
                while (rs.next()) {
                    int lectura = rs.getInt("lectura");
                    int escritura = rs.getInt("escritura");
                    int controlTotal = rs.getInt("control_total");

                    // Agregar permisos a la cadena
                    if (lectura == 1) {
                        permisos.append("Lectura, ");
                    }
                    if (escritura == 1) {
                        permisos.append("Escritura, ");
                    }
                    if (controlTotal == 1) {
                        permisos.append("Control Total, ");
                    }
                }

                // Eliminar la última coma y espacio, si existen
                if (permisos.length() > 0) {
                    permisos.setLength(permisos.length() - 2);
                }

                // Retornar permisos o "Sin permisos"
                return permisos.length() > 0 ? permisos.toString() : "Sin permisos";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al obtener permisos";
        }
    }


    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }
    

    public void Despliega() {
        System.out.println("Funciona");
        Cerrardesplegar = !Cerrardesplegar;
        Panel_Visible = !Panel_Visible;
        Cerrar.setVisible(Cerrardesplegar);
        Panel_Desplegable.setVisible(Panel_Visible);
    }
    
    public void Carta() throws IOException {
    System.out.println( obtenerPermisosPorUsuarioYModulo(36,"CARTA" ));
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
}
