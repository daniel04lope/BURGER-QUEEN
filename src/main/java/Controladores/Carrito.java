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
			

			   if (Login.datos_login.getIdUsuario() == 0) {
		            Mostrar_Login();
		               
			   }
			   else {
			
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
			
		}});
		
		   
		
		Cocacola.setOnAction(event -> {
			
			if (Login.datos_login.getIdUsuario() == 0) {
	            Mostrar_Login();
	               
		   }
			
			else {
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
			
		}});
		
		Patatas.setOnAction(event ->{

			if (Login.datos_login.getIdUsuario() == 0) {
	            Mostrar_Login();
	               
		   }
			else {
			
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
			
		}});	
			
		   
		
		
		
		
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
	        
	        // Obtener el ID del carrito asociado al usuario
	        String sqlObtenerCarrito = "SELECT id_carrito FROM carrito WHERE id_cliente = ? AND estado = 'pendiente'";
	        PreparedStatement sentenciaObtenerCarrito = conexion.prepareStatement(sqlObtenerCarrito);
	        sentenciaObtenerCarrito.setInt(1, Login.datos_login.getIdUsuario());
	        ResultSet resultadoCarrito = sentenciaObtenerCarrito.executeQuery();

	        if (!resultadoCarrito.next()) {
	            System.out.println("No se encontró un carrito pendiente para el usuario.");
	            return;
	        }

	        int idCarrito = resultadoCarrito.getInt("id_carrito");
	        resultadoCarrito.close();
	        sentenciaObtenerCarrito.close();

	        // Obtener los ítems del carrito
	        String sqlCarritoItems = "SELECT id_plato, precio_unitario, id_item FROM carrito_items WHERE id_carrito = ?";
	        PreparedStatement sentenciaCarritoItems = conexion.prepareStatement(sqlCarritoItems);
	        sentenciaCarritoItems.setInt(1, idCarrito);
	        ResultSet carritoItems = sentenciaCarritoItems.executeQuery();

	        int row = 0;

	        while (carritoItems.next()) {
	            int idProducto = carritoItems.getInt("id_plato");
	            double precio = carritoItems.getDouble("precio_unitario");
	            int idItem = carritoItems.getInt("id_item");

	            // Obtener los detalles del producto
	            String sqlCarta = "SELECT c.id_producto, c.nombre, c.descripcion, c.precio, c.categoria, c.peso, c.ruta, " +
	                    "c.alergenos " +
	                    "FROM carta c " +
	                    "WHERE c.id_producto = ?";
	            PreparedStatement sentenciaCarta = conexion.prepareStatement(sqlCarta);
	            sentenciaCarta.setInt(1, idProducto);
	            ResultSet productoDetalles = sentenciaCarta.executeQuery();

	            if (productoDetalles.next()) {
	                // Crear el panel del producto
	                AnchorPane producto = new AnchorPane();
	                producto.setPrefSize(450, 90);
	                producto.setStyle("-fx-background-color: A6234E; -fx-background-radius: 20; -fx-border-radius: 20;-fx-border-color: FFFFFF");

	                // Nombre del producto
	                Label nombreProducto = new Label(productoDetalles.getString("nombre"));
	                nombreProducto.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
	                AnchorPane.setLeftAnchor(nombreProducto, 10.0);
	                AnchorPane.setTopAnchor(nombreProducto, 10.0);
	                producto.getChildren().add(nombreProducto);

	                // Precio
	                Label precioEtiqueta = new Label(precio + " €");
	                precioEtiqueta.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
	                AnchorPane.setRightAnchor(precioEtiqueta, 10.0);
	                AnchorPane.setTopAnchor(precioEtiqueta, 10.0);
	                producto.getChildren().add(precioEtiqueta);

	                // Alérgenos
	                String alergenos = productoDetalles.getString("alergenos");
	                Label alergenosLabel = new Label("Alérgenos: " + (alergenos != null ? alergenos : "Ninguno"));
	                alergenosLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
	                AnchorPane.setLeftAnchor(alergenosLabel, 10.0);
	                AnchorPane.setTopAnchor(alergenosLabel, 50.0);
	                producto.getChildren().add(alergenosLabel);

	                // Botón de borrar
	                Button botonBorrar = new Button();
	                botonBorrar.setStyle("-fx-background-color: transparent;");
	                ImageView iconoBorrar = new ImageView(new Image("/basura.png"));
	                iconoBorrar.setFitWidth(20);
	                iconoBorrar.setFitHeight(20);
	                iconoBorrar.setPreserveRatio(true);
	                botonBorrar.setGraphic(iconoBorrar);

	                botonBorrar.setUserData(idItem);
	                botonBorrar.setOnAction(event -> {
	                    int idItemEliminar = (int) botonBorrar.getUserData();
	                    try (Connection conexionEliminar = util.Conexiones.dameConexion("burger-queen")) {
	                        PreparedStatement sentenciaEliminar = conexionEliminar.prepareStatement(
	                                "DELETE FROM carrito_items WHERE id_item = ?");
	                        sentenciaEliminar.setInt(1, idItemEliminar);
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

	                AnchorPane.setRightAnchor(botonBorrar, 10.0);
	                AnchorPane.setBottomAnchor(botonBorrar, 10.0);
	                producto.getChildren().add(botonBorrar);

	                // Agregar el producto al listado
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
	    Double total = 0.0; 
	    int facturaRow = 0;

	    try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
	        String sqlCarritoItems = "SELECT id_plato, precio_unitario FROM carrito_items WHERE id_carrito = ?";
	        PreparedStatement sentenciaCarritoItems = conexion.prepareStatement(sqlCarritoItems);
	        sentenciaCarritoItems.setInt(1, Login.datos_login.getIdUsuario());
	        ResultSet carritoItems = sentenciaCarritoItems.executeQuery();

	  
	        while (carritoItems.next()) {
	            int idProducto = carritoItems.getInt("id_plato");
	            Double precio = carritoItems.getDouble("precio_unitario");
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
