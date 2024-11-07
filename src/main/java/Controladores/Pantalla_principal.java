package Controladores;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class Pantalla_principal implements Initializable {
    @FXML
     Text Username;

    @FXML
    private AnchorPane drawer;

    @FXML
    private Button Desplegable;
    @FXML
    private Button Cerrar;

    private boolean drawerVisible = false;
    private boolean Cerrardesplegar = false;
   

  
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Muestra un mensaje en consola para asegurar que el controlador se cargó correctamente
        System.out.println("Pantalla_principal inicializado correctamente");

        // Configura el botón para que al hacer clic cambie la visibilidad del drawer
       
    }
    public void cerrar() {
        // Obtener la referencia del Stage a partir del botón
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close(); // Cerrar la ventana
    }
    public void toggleDrawer() {
    	System.out.println("Funciona");
    	Cerrardesplegar=!Cerrardesplegar;
        drawerVisible = !drawerVisible;
        Cerrar.setVisible(Cerrardesplegar);
        drawer.setVisible(drawerVisible);
    }
    
    public void Carta() throws IOException{
    	
    	  FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
          Pane registro = loader.load();
         
          
          // Crear la escena del login con fondo transparente
          Scene loginScene = new Scene(registro, 600, 500);
          loginScene.setFill(Color.TRANSPARENT);
          
          // Crear un nuevo Stage para el login y configurarlo sin decoración y transparente
          Stage loginStage = new Stage();
          loginStage.initStyle(StageStyle.DECORATED);
          loginStage.setScene(loginScene);
          loginStage.setTitle("CARTA");
          loginStage.show();
          cerrar();  
}
    
    public void Reserva() throws IOException{
    	

  	  FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Reservas.fxml"));
        Pane registro = loader.load();
       
        
        // Crear la escena del login con fondo transparente
        Scene loginScene = new Scene(registro, 600, 500);
        loginScene.setFill(Color.TRANSPARENT);
        
        // Crear un nuevo Stage para el login y configurarlo sin decoración y transparente
        Stage loginStage = new Stage();
        loginStage.initStyle(StageStyle.DECORATED);
        loginStage.setScene(loginScene);
        loginStage.setTitle("Reservas");
        loginStage.show();
        cerrar();  
}
    }    
