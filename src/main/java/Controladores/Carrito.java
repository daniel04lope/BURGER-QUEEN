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
    @FXML
    private Text Username;
    @FXML
    private Button Nugguest;
    @FXML
    private Button Patatas;
    @FXML
    private Button Cocacola;
    @FXML
    GridPane factura;
    
    
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
		Username.setText(Login.bannerusuario);
		
		Nugguest.setOnAction(event -> {
			
			try (Connection conexion = util.Conexiones.dameConexion("burger-queen")){
				PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO carrito_items(id_carrito, id_plato, cantidad, precio_unitario) VALUES (?,43,1,2)");
				sentencia.setInt(1, Login.datos_login.getIdUsuario());
				int ejecuta = sentencia.executeUpdate();
				Muestra_productos();
				Factura();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
		
		
		Cocacola.setOnAction(event -> {
			
			try (Connection conexion = util.Conexiones.dameConexion("burger-queen")){
				PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO carrito_items(id_carrito, id_plato, cantidad, precio_unitario) VALUES (?,44,1,5)");
				sentencia.setInt(1, Login.datos_login.getIdUsuario());
				int ejecuta = sentencia.executeUpdate();
				Muestra_productos();
				Factura();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
		
		Patatas.setOnAction(event ->{
			try (Connection conexion = util.Conexiones.dameConexion("burger-queen")){
				PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO carrito_items(id_carrito, id_plato, cantidad, precio_unitario) VALUES (?,45,1,1)");
				sentencia.setInt(1, Login.datos_login.getIdUsuario());
				int ejecuta = sentencia.executeUpdate();
				Muestra_productos();
				Factura();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});	
			
			
		
		
		
		
		try {
			
			Muestra_productos();
			Factura();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void Muestra_productos() throws SQLException {
	    try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
	       
	        String sqlCarritoItems = "SELECT id_plato,precio_unitario,id_item FROM carrito_items WHERE id_carrito = ?";
	        PreparedStatement sentenciaCarritoItems = conexion.prepareStatement(sqlCarritoItems);
	        sentenciaCarritoItems.setInt(1, Login.datos_login.getIdUsuario()); 
	        ResultSet carritoItems = sentenciaCarritoItems.executeQuery();

	        int row = 0;

	        
	        while (carritoItems.next()) {
	            int idProducto = carritoItems.getInt("id_plato"); 
	            int precio = carritoItems.getInt("precio_unitario");
	            int iditem =carritoItems.getInt("id_item");
	           
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

	              
	                Label Nombredelobjeto = new Label(productoDetalles.getString("nombre"));
	                Nombredelobjeto.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
	                AnchorPane.setLeftAnchor(Nombredelobjeto, 10.0);
	                AnchorPane.setTopAnchor(Nombredelobjeto, 10.0);
	                producto.getChildren().add(Nombredelobjeto);

	              
	                Label precioetiqueta = new Label(precio + " €");
	                precioetiqueta.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
	                AnchorPane.setRightAnchor(precioetiqueta, 10.0);
	                AnchorPane.setTopAnchor(precioetiqueta, 10.0);
	                producto.getChildren().add(precioetiqueta);

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

	          

	            
	                Button botondeborrar = new Button();
	                botondeborrar.setStyle("-fx-background-color: transparent;"); 
	               
	                ImageView iconodeborrar = new ImageView(new Image("/basura.png"));
	                iconodeborrar.setFitWidth(20);
	                iconodeborrar.setFitHeight(20); 
	                iconodeborrar.setPreserveRatio(true); 

	             
	                botondeborrar.setGraphic(iconodeborrar);

	               
	                	

	               
	                botondeborrar.setUserData(iditem);

	               
	                botondeborrar.setOnAction(event -> {
	                    int idProductoEliminar = (int) botondeborrar.getUserData();
	                    Connection conexioneliminar = util.Conexiones.dameConexion("burger-queen");
	                    try {
	                      
	                        PreparedStatement sentenciaEliminar = conexioneliminar.prepareStatement(
	                                "DELETE FROM `burger-queen`.`carrito_items` WHERE id_item = ? AND id_carrito = ?");
	                        sentenciaEliminar.setInt(1, idProductoEliminar);
	                        sentenciaEliminar.setInt(2, Login.datos_login.getIdUsuario());
	                        int resultado = sentenciaEliminar.executeUpdate();

	                        if (resultado > 0) {
	                            System.out.println("Producto eliminado del carrito con éxito.");
	                           
	                            Listado.getChildren().clear();
	                            Muestra_productos(); 
	                            Factura();
	                            
	                        }
	                    } catch (SQLException e) {
	                        e.printStackTrace();
	                    }
	                });
	               
	                AnchorPane.setRightAnchor(botondeborrar, 10.0);
	                AnchorPane.setBottomAnchor(botondeborrar, 10.0);
	                producto.getChildren().add(botondeborrar);
	                

	               
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
	
	public void Factura() throws SQLException {
	    factura.getChildren().clear(); 
	    int total = 0; 
	    int facturaRow = 0;

	    try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
	        String sqlCarritoItems = "SELECT id_plato, precio_unitario FROM carrito_items WHERE id_carrito = ?";
	        PreparedStatement sentenciaCarritoItems = conexion.prepareStatement(sqlCarritoItems);
	        sentenciaCarritoItems.setInt(1, Login.datos_login.getIdUsuario());
	        ResultSet carritoItems = sentenciaCarritoItems.executeQuery();

	  
	        while (carritoItems.next()) {
	            int idProducto = carritoItems.getInt("id_plato");
	            int precio = carritoItems.getInt("precio_unitario");
	            total += precio; 

	           
	            String sqlNombreProducto = "SELECT nombre FROM carta WHERE id_producto = ?";
	            PreparedStatement sentenciaNombreProducto = conexion.prepareStatement(sqlNombreProducto);
	            sentenciaNombreProducto.setInt(1, idProducto);
	            ResultSet productoDetalles = sentenciaNombreProducto.executeQuery();

	            if (productoDetalles.next()) {
	                String nombreProducto = productoDetalles.getString("nombre");

	                Text productoNombreFactura = new Text(nombreProducto);
	                productoNombreFactura.setStyle("-fx-font-size: 14px; -fx-fill: white;");
	                Text productoPrecioFactura = new Text(precio + " €");
	                productoPrecioFactura.setStyle("-fx-font-size: 14px; -fx-fill: white;");

	                
	                factura.add(productoNombreFactura, 0, facturaRow);
	                factura.add(productoPrecioFactura, 1, facturaRow);
	                facturaRow++;
	            }

	            productoDetalles.close();
	            sentenciaNombreProducto.close();
	        }

	       
	        Text totalLabel = new Text("Total:");
	        totalLabel.setStyle("-fx-font-size: 16px; -fx-fill: white; -fx-font-weight: bold;");
	        Text totalAmount = new Text(total + " €");
	        totalAmount.setStyle("-fx-font-size: 16px; -fx-fill: white; -fx-font-weight: bold;");
	        
	        factura.add(totalLabel, 0, facturaRow);
	        factura.add(totalAmount, 1, facturaRow);

	        carritoItems.close();
	        sentenciaCarritoItems.close();
	    }
	}

	

    
	



}
