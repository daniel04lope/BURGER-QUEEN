package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

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
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.Conexiones;

public class ItemFocus implements Initializable {
    @FXML
    private AnchorPane Panel_Desplegable;
    @FXML
    private TabPane tabla;
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
    private Button Carrito;    
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
    private Producto complemento;
    private Producto bebida;
    
    public void Despliega() {
    	System.out.println("Funciona");
    	Cerrardesplegar = !Cerrardesplegar;
        drawerVisible = !drawerVisible;
        Cerrar.setVisible(Cerrardesplegar);
        Panel_Desplegable.setVisible(drawerVisible);
    }
    
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }
    
    public void Pantalla_Principal() throws IOException {
  	  FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principal = loader.load();

        Scene principalScene = new Scene(principal, 600, 500);
        principalScene.getStylesheets().add(getClass().getResource("Pantalla_Principal.css").toExternalForm());
        principalScene.setFill(Color.TRANSPARENT);

        Stage PrincipalStage = new Stage();
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("CARTA");
        PrincipalStage.show();
        cerrar();
	  }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Username.setText(Login.bannerusuario);

        if (producto != null) {  // Verificación para evitar el NullPointerException
            cargarProducto();

           
        }
        
    }


    public void setProducto(Producto producto) {
        this.producto = producto;
        this.precioBase = producto.getPrecio();
        this.precioTotal = precioBase;
        
            
       
     
        
        
        cargarProducto();
    }

    private void cargarProducto() {
    	
    	
    	
        if (producto != null) {
        	 Nombre.setText(producto.getNombre());
             Descripcion.setText(producto.getDescripcion());
             DescripcionTab.setText(producto.getNombre());
             
             actualizarBotonPedido();
             imagen.setImage(new Image(getClass().getResourceAsStream("/" + producto.getRuta())));
              
             
        }
        if ("Bebida".equals(producto.getCategoria())) {
            tabla.getTabs().remove(DescripcionTab);
        }
        
        
        
    }
    
    private void actualizarBotonPedido() {
        Carrito.setText("Añadir a mi pedido - " + String.format("%.2f €", precioTotal));
    }
    
    @FXML
    private void aniadirextra1_20(ActionEvent event) {
    	if(cantExtras < 1) {
    		precioTotal += 1.20;
            cantExtras++;
            tipoExtra = "Extra queso + bacon";
            actualizarBotonPedido();
    	}        
    }
    
    @FXML
    private void complemento(ActionEvent event) throws SQLException {
     
        if (complemento == null) {
            complemento = new Producto(); 
        }
        
        

        try (Connection conexioncomplementos = util.Conexiones.dameConexion("burger-queen")) {
            PreparedStatement sentenciacomplementos = conexioncomplementos.prepareStatement("SELECT id_producto, nombre, descripcion, precio, categoria, peso FROM carta WHERE id_producto = ?");

            ResultSet ejecutar = null;

            if (Npollo.isSelected()) {
                sentenciacomplementos.setInt(1, 43);
                ejecutar = sentenciacomplementos.executeQuery();
            } else if (Acebolla.isSelected()) {
                sentenciacomplementos.setInt(1, 46);
                ejecutar = sentenciacomplementos.executeQuery();
            } else if (Psupreme.isSelected()) {
                sentenciacomplementos.setInt(1, 48);
                ejecutar = sentenciacomplementos.executeQuery();
            } else if (Pclasica.isSelected()) {
                sentenciacomplementos.setInt(1, 47);
                ejecutar = sentenciacomplementos.executeQuery();
            }

            if (ejecutar.next()) {
                complemento.setIdProducto(ejecutar.getInt("id_producto"));
                complemento.setNombre(ejecutar.getString("nombre"));
                complemento.setPrecio(ejecutar.getDouble("precio"));
                complemento.setDescripcion(ejecutar.getString("descripcion"));
                complemento.setPeso(ejecutar.getDouble("peso"));
                complemento.setCategoria(ejecutar.getString("categoria"));
            }
        }
        System.out.println(complemento.getNombre());
    }

    
    @FXML
    private void bebida(ActionEvent event) throws SQLException {
    	
    	

        if (bebida == null) {
            bebida = new Producto(); 
        }
        
        

        try (Connection conexioncomplementos = util.Conexiones.dameConexion("burger-queen")) {
            PreparedStatement sentenciacomplementos = conexioncomplementos.prepareStatement("SELECT id_producto, nombre, descripcion, precio, categoria, peso FROM carta WHERE id_producto = ?");

            ResultSet ejecutar = null;
    	if(Cocacola.isSelected()) {
    		 sentenciacomplementos.setInt(1, 44);
    		 ejecutar = sentenciacomplementos.executeQuery();
    	} else if(Fanta.isSelected()) {
    		 sentenciacomplementos.setInt(1, 49);
    		 ejecutar = sentenciacomplementos.executeQuery();
    		
    	} else if(Nestea.isSelected()) {
    		 sentenciacomplementos.setInt(1, 50);
    		 ejecutar = sentenciacomplementos.executeQuery();
    		
    	} else if(CcZero.isSelected()) {
    		 sentenciacomplementos.setInt(1, 51);
    		 ejecutar = sentenciacomplementos.executeQuery();
    		
    	}
    	

        if (ejecutar.next()) {
            bebida.setIdProducto(ejecutar.getInt("id_producto"));
            bebida.setNombre(ejecutar.getString("nombre"));
            bebida.setPrecio(ejecutar.getDouble("precio"));
            bebida.setDescripcion(ejecutar.getString("descripcion"));
            bebida.setPeso(ejecutar.getDouble("peso"));
            bebida.setCategoria(ejecutar.getString("categoria"));
        }
       
        System.out.println(bebida.getNombre());
        }
        
        
    	
    }
    
    @FXML
    private void aniadirextra1B(ActionEvent event) {
    	if(cantExtras < 1) {
    		precioTotal += 1;
            cantExtras++;
            tipoExtra = "Extra Bacon";
            actualizarBotonPedido();
    	}   
    }
    
    @FXML
    private void aniadirextra1Q() {
    	if(cantExtras < 1) {
    		precioTotal += 1;
            cantExtras++;
            tipoExtra = "Extra Queso";
            actualizarBotonPedido();
    	}   
    }
    
    public void Carta() throws IOException {
  	  FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
        Pane registro = loader.load();

        Scene loginScene = new Scene(registro, 600, 500);
        loginScene.setFill(Color.TRANSPARENT);
        
        Stage loginStage = new Stage();
        loginStage.initStyle(StageStyle.DECORATED);
        loginStage.setScene(loginScene);
        loginStage.setTitle("CARTA");
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
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.setScene(loginScene);
          
          
            loginStage.setTitle("LOGIN");
            loginStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void metercarrito() throws SQLException {
        if (Login.datos_login.getIdUsuario() == 0) {
            Mostrar_Login();
        } else {
            try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
                
                PreparedStatement sentencia = conexion.prepareStatement(
                    "SELECT id_producto, nombre, descripcion, precio, categoria, peso FROM carta WHERE id_producto = ?");
                sentencia.setInt(1, producto.getIdProducto());

                ResultSet resultado = sentencia.executeQuery();

                if (resultado.next()) {
                    PreparedStatement insertaproductobase = conexion.prepareStatement(
                        "INSERT INTO carrito_items (id_carrito, id_plato, cantidad, precio_unitario, Detalles) VALUES (?,?,?,?,?)");
                    insertaproductobase.setInt(1, Login.datos_login.getIdUsuario());
                    insertaproductobase.setInt(2, resultado.getInt("id_producto"));
                    insertaproductobase.setInt(3, 1);
                    insertaproductobase.setDouble(4, precioTotal);
                    insertaproductobase.setString(5, "");

                    int ejecutainsertar = insertaproductobase.executeUpdate();
                    System.out.println("Producto base añadido al carrito con éxito.");
                }

              
                if (complemento != null) {
                    PreparedStatement insertacomplemento = conexion.prepareStatement(
                        "INSERT INTO carrito_items (id_carrito, id_plato, cantidad, precio_unitario, Detalles) VALUES (?,?,?,?,?)");
                    insertacomplemento.setInt(1, Login.datos_login.getIdUsuario());
                    insertacomplemento.setInt(2, complemento.getIdProducto());
                    insertacomplemento.setInt(3, 1); 
                    insertacomplemento.setDouble(4, complemento.getPrecio()); 
                    insertacomplemento.setString(5, "");

                    int ejecutarInsertComplemento = insertacomplemento.executeUpdate();
                    System.out.println("Complemento añadido al carrito con éxito.");
                }
                
                
                if (bebida != null) {
                    PreparedStatement insertacomplemento = conexion.prepareStatement(
                        "INSERT INTO carrito_items (id_carrito, id_plato, cantidad, precio_unitario, Detalles) VALUES (?,?,?,?,?)");
                    insertacomplemento.setInt(1, Login.datos_login.getIdUsuario());
                    insertacomplemento.setInt(2, bebida.getIdProducto());
                    insertacomplemento.setInt(3, 1); 
                    insertacomplemento.setDouble(4, bebida.getPrecio()); 
                    insertacomplemento.setString(5, "");

                    int ejecutarInsertComplemento = insertacomplemento.executeUpdate();
                    System.out.println("Complemento añadido al carrito con éxito.");
                }
            }
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
}

   

    


