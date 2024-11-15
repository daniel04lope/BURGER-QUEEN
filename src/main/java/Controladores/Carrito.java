package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.scene.image.Image;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
	       
	        String sqlCarritoItems = "SELECT id_plato FROM carrito_items WHERE id_carrito = ?";
	        PreparedStatement sentenciaCarritoItems = conexion.prepareStatement(sqlCarritoItems);
	        sentenciaCarritoItems.setInt(1, Login.datos_login.getIdUsuario()); 
	        ResultSet carritoItems = sentenciaCarritoItems.executeQuery();

	        int row = 0;

	        
	        while (carritoItems.next()) {
	            int idProducto = carritoItems.getInt("id_plato"); 

	           
	            String sqlCarta = "SELECT c.id_producto, c.nombre, c.descripcion, c.precio, c.categoria, c.alergenos, c.peso, c.ruta, GROUP_CONCAT(a.nombre SEPARATOR ', ') AS alergenos "
	                            + "FROM carta c "
	                            + "LEFT JOIN carta_alergeno ca ON c.id_producto = ca.CARTA_ID "
	                            + "LEFT JOIN alergeno a ON ca.ALERGENO_ID = a.ID "
	                            + "WHERE c.id_producto = ? "
	                            + "GROUP BY c.id_producto";

	            PreparedStatement sentenciaCarta = conexion.prepareStatement(sqlCarta);
	            sentenciaCarta.setInt(1, idProducto); 
	            ResultSet productoDetalles = sentenciaCarta.executeQuery();

	            if (productoDetalles.next()) {
	                
	                AnchorPane producto = new AnchorPane();
	                producto.setPrefSize(450, 90);
	                producto.setStyle("-fx-background-color: A6234E; -fx-background-radius: 20; -fx-border-radius: 20;-fx-border-color: FFFFFF");

	              
	                Label itemName = new Label(productoDetalles.getString("nombre"));
	                itemName.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
	                AnchorPane.setLeftAnchor(itemName, 10.0);
	                AnchorPane.setTopAnchor(itemName, 10.0);
	                producto.getChildren().add(itemName);

	              
	                Label priceLabel = new Label(productoDetalles.getDouble("precio") + " €");
	                priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
	                AnchorPane.setRightAnchor(priceLabel, 10.0);
	                AnchorPane.setTopAnchor(priceLabel, 10.0);
	                producto.getChildren().add(priceLabel);

	              /*
	                Label descripcionLabel = new Label(productoDetalles.getString("descripcion"));
	                descripcionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
	                AnchorPane.setLeftAnchor(descripcionLabel, 10.0);
	                AnchorPane.setTopAnchor(descripcionLabel, 30.0);
	                producto.getChildren().add(descripcionLabel);*/

	              
	                Label alergenosLabel = new Label("Alérgenos: " + productoDetalles.getString("alergenos"));
	                alergenosLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
	                AnchorPane.setLeftAnchor(alergenosLabel, 10.0);
	                AnchorPane.setTopAnchor(alergenosLabel, 50.0);
	                producto.getChildren().add(alergenosLabel);

	             
	                Button editButton = new Button();
	                editButton.setStyle("-fx-background-color: transparent;"); 
	                AnchorPane.setLeftAnchor(editButton, 10.0);
	                AnchorPane.setBottomAnchor(editButton, 10.0);
	                producto.getChildren().add(editButton);

	            
	                Button deleteButton = new Button();
	                deleteButton.setStyle("-fx-background-color: transparent;"); 
	               
	                ImageView deleteIcon = new ImageView(new Image("/basura.png"));
	                deleteIcon.setFitWidth(20);
	                deleteIcon.setFitHeight(20); 
	                deleteIcon.setPreserveRatio(true); 

	             
	                deleteButton.setGraphic(deleteIcon);

	               
	                AnchorPane.setRightAnchor(deleteButton, 10.0);
	                AnchorPane.setBottomAnchor(deleteButton, 10.0);
	                producto.getChildren().add(deleteButton);


	               
	                Listado.add(producto, 0, row);
	                row++;
	                GridPane.setVgrow(producto, javafx.scene.layout.Priority.ALWAYS);
	            }
	            
	            productoDetalles.close();
	       
	            sentenciaCarta.close();
	        }
	      
	        carritoItems.close();
	    
	        sentenciaCarritoItems.close();
	    }
	}


}
