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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
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

    @FXML
    private Accordion administradores;
    @FXML
    private TitledPane titledpaneadmin;
    @FXML
    private VBox Vboxadmin;
    
    private boolean Panel_Visible = false;
    private boolean Cerrardesplegar = false;
    @FXML
    private Button reservaadmin;
    @FXML
    private Button menuadmin;
    @FXML
    private Button usuariosadmin;
    @FXML
    private Button pedidosadmin;

    public void initialize(URL location, ResourceBundle resources) {
    	
        System.out.println("Pantalla_principal inicializado correctamente");
     
        Username.textProperty().bind(Login.bannerusuarioProperty());
        
      if(Login.tipo.equals("administradores")) {
    	  
    	  administradores.setVisible(true);
    	  titledpaneadmin.setVisible(true);
    	  Vboxadmin.setVisible(true);  
    	  usuariosadmin.setDisable(false);
      }     
       
      if (Login.tipo.equals("empleados") && permisos(Login.datos_login.getIdUsuario(), "RESERVA", "lectura")==true) {
    	  administradores.setVisible(true);
    	  titledpaneadmin.setVisible(true);
    	  Vboxadmin.setVisible(true);  
    	  reservaadmin.setDisable(false);
    	  
      }
   
      
      if (Login.tipo.equals("empleados") && !permisos(Login.datos_login.getIdUsuario(), "CARTA", "lectura")==true) {
    	  administradores.setVisible(true);
    	  titledpaneadmin.setVisible(true);
    	  Vboxadmin.setVisible(true);  
    	 menuadmin.setDisable(false);
    	  
      }
  
      
      
      if (Login.tipo.equals("empleados") && permisos(Login.datos_login.getIdUsuario(), "PEDIDO", "lectura")==true) {
    	  administradores.setVisible(true);
    	  titledpaneadmin.setVisible(true);
    	  Vboxadmin.setVisible(true);  
    	  pedidosadmin.setDisable(false);
      } 
      
      
      if (Login.tipo.equals("usuarios")){
    	  administradores.setVisible(false);
    	  titledpaneadmin.setVisible(false);
    	  Vboxadmin.setVisible(false);  
    	  
      }
        
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
    public static boolean permisos(int idEmpleado, String nombreModulo, String tipoPermiso) {
        String query = "SELECT COUNT(*) FROM permisos p " +
                       "JOIN modulos m ON p.id_modulo = m.id_modulo " +
                       "WHERE p.id_empleado = ? AND m.nombre_modulo = ? AND " + tipoPermiso + " = 1";

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen");
             PreparedStatement preparedStatement = conexion.prepareStatement(query)) {

            preparedStatement.setInt(1, idEmpleado);
            preparedStatement.setString(2, nombreModulo);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Devuelve true si hay al menos un permiso
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; 
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
    System.out.println(permisos(36, "CARTA", "escritura"));
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
