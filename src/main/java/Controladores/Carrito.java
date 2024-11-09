package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Carrito implements Initializable {

    @FXML
    Text Nombre_de_usuario;
    @FXML
    GridPane Listado;
  
    @FXML
    private Button Cerrar;
    
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
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
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			Muestra_productos();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void Muestra_productos() throws SQLException {
		
		 try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
		System.out.println("Iniciando");
        String sql = "SELECT id_item, id_carrito, id_plato, cantidad, precio_unitario  FROM carrito_items where id_carrito= ? ";

      PreparedStatement sentencia = conexion.prepareStatement(sql);
       sentencia.setInt(1, Login.datos_login.getIdUsuario()); 
       
        ResultSet productos = sentencia.executeQuery();
        	
        int row = 0;
        int column = 0;
		
        while (productos.next()) {
        AnchorPane producto = new AnchorPane();
        producto.setPrefSize(450, 90);
        producto.setStyle("-fx-background-color: FFFFFF;-fx-background-radius: 20;-fx-border-radius: 20;");
        Listado.add(producto, column, row);
        
        System.out.println("Llego");
        
        
        if (column == 2) {
            column = 0;
            row++;
        } else {
            column++;
        }
        GridPane.setVgrow(producto, javafx.scene.layout.Priority.ALWAYS);
        	
        	
        }
        
	}
}

}
