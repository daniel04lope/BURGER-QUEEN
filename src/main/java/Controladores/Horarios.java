package Controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Horarios implements Initializable {
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

	    public void Despliega() {
	        System.out.println("Funciona");
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

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub
			
			 Username.textProperty().bind(Login.bannerusuarioProperty());
		}
}
