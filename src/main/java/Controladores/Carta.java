package Controladores;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Carta {
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
	  public void toggleDrawer() {
	    	System.out.println("Funciona");
	    	Cerrardesplegar=!Cerrardesplegar;
	        drawerVisible = !drawerVisible;
	        Cerrar.setVisible(Cerrardesplegar);
	        drawer.setVisible(drawerVisible);
	    }
	  public void cerrar() {
	        // Obtener la referencia del Stage a partir del botón
	        Stage stage = (Stage) Cerrar.getScene().getWindow();
	        stage.close(); // Cerrar la ventana
	    }
	  public void Pantalla_Principal() throws IOException {

    	  FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
          Pane principal = loader.load();

          // Crear la escena del login con fondo transparente
          Scene principalScene = new Scene(principal, 600, 500);
          principalScene.getStylesheets().add(getClass().getResource("Pantalla_Principal.css").toExternalForm());
          principalScene.setFill(Color.TRANSPARENT);

          // Crear un nuevo Stage para el login y configurarlo sin decoración y transparente
          Stage PrincipalStage = new Stage();
          PrincipalStage.initStyle(StageStyle.DECORATED);
          PrincipalStage.setScene(principalScene);
          PrincipalStage.setTitle("CARTA");
          PrincipalStage.show();
          cerrar();  
	  }
	  
}
