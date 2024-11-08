package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import Modelos.Producto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ItemFocus implements Initializable {
    @FXML
    private AnchorPane drawer;
    @FXML
    private Button Desplegable;
    @FXML
    private Button Cerrar;
    @FXML
    private ImageView imagen;
    @FXML
    private Label Nombre;
    @FXML
    private Label Descripcion;
    @FXML
    private Tab DescripcionTab;
    @FXML
    private Button addToCartButton; // Botón de añadir al pedido    
    @FXML
    private RadioButton Pclasica;
    @FXML
    private RadioButton Psupreme;
    @FXML
    private RadioButton Acebolla;
    @FXML
    private RadioButton Npollo;
    @FXML
    private RadioButton Cocacola;
    @FXML
    private RadioButton Fanta;
    @FXML
    private RadioButton Nestea;
    @FXML
    private RadioButton CcZero;
	@FXML
	Text Username;
    

    private boolean drawerVisible = false;
    private boolean Cerrardesplegar = false;
    
    private Producto producto;
    private double precioBase;
    private double precioTotal;
    private int cantExtras;
    private String tipoExtra;
    private String complemento;
    private String bebida;
    
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
    

    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    Username.setText(Login.banneruser);
        // Verificar si el producto ha sido establecido y cargarlo
        if (producto != null) {
            cargarProducto();
        }
        
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        this.precioBase = producto.getPrecio(); // Suponiendo que el modelo Producto tiene un método getPrecio
        this.precioTotal = precioBase;
        Pclasica.setSelected(true);
        complemento="Patatas clasicas";
        System.out.println(complemento);        
        Cocacola.setSelected(true);
        bebida="Cocacola";
        System.out.println(bebida);
        
        
        cargarProducto(); // Cargar el producto en la interfaz
    }

    private void cargarProducto() {
        if (producto != null) {
        	 Nombre.setText(producto.getNombre());
             Descripcion.setText(producto.getDescripcion());
             DescripcionTab.setText(producto.getNombre());
             actualizarBotonPedido(); // Mostrar precio inicial en el botón
            // Puedes agregar código para cargar una imagen o información adicional aquí
             
             imagen.setImage(new Image(getClass().getResourceAsStream("/" + producto.getRuta())));

            
        }
    }
    
    private void actualizarBotonPedido() {
        addToCartButton.setText("Añadir a mi pedido - " + String.format("%.2f €", precioTotal));
    }
    
    @FXML
    private void aniadirextra1_20(ActionEvent event) {
    	if(cantExtras<1) {
    		precioTotal += 1.20;
            cantExtras++;
            tipoExtra="Extra queso + bacon";
            actualizarBotonPedido();
    	}        
    }
    
    @FXML
    private void complemento(ActionEvent event) {
    	if(Npollo.isSelected()) {
    		complemento="Nuggets de pollo";
    	}else if(Acebolla.isSelected()) {
    		complemento="Aros de cebolla";
    	}else if(Psupreme.isSelected()) {
    		complemento="Patatas supreme";
    	}else if(Pclasica.isSelected()) {
    		complemento="Patatas clasicas";
    	}
    	
    	System.out.println(complemento);
    	
    	
    }
    
    @FXML
    private void bebida(ActionEvent event) {
    	if(Cocacola.isSelected()) {
    		bebida="Cocacola";
    	}else if(Fanta.isSelected()) {
    		bebida="Fanta de naranja";
    	}else if(Nestea.isSelected()) {
    		bebida="Nestea";
    	}else if(CcZero.isSelected()) {
    		bebida="Cocacola Zero";
    	}
    	
    	System.out.println(bebida);
    	
    	
    }
    
    @FXML
    private void aniadirextra1B(ActionEvent event) {
    	if(cantExtras<1) {
    		precioTotal += 1;
            cantExtras++;
            tipoExtra="Extra Bacon";
            actualizarBotonPedido();
    	}   
    }
    
    @FXML
    private void aniadirextra1Q() {
    	if(cantExtras<1) {
    		precioTotal += 1;
            cantExtras++;
            tipoExtra="Extra Queso";
            actualizarBotonPedido();
    	}   
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

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
            Pane login = loader.load();
         
         
            // Crear la escena del login con fondo transparente
            Scene loginScene = new Scene(login, 450, 600);
            loginScene.setFill(Color.TRANSPARENT);

            // Crear un nuevo Stage para el login y configurarlo sin decoración y transparente
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);
            loginStage.setScene(loginScene);
            loginStage.setTitle("LOGIN");
            loginStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
}


