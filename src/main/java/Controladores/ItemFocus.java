package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import Modelos.Producto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
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
    private Button Modificar;
    @FXML
    private Button Eliminar;
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
    private String complemento;
    private String bebida;
    
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
        if (producto != null) {
            cargarProducto();
        }
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        this.precioBase = producto.getPrecio();
        this.precioTotal = precioBase;
        Pclasica.setSelected(true);
        complemento = "Patatas clasicas";
        System.out.println(complemento);        
        Cocacola.setSelected(true);
        bebida = "Cocacola";
        System.out.println(bebida);
        
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
        
        if ("bebida".equals(producto.getCategoria())) {
            tabla.getTabs().remove(DescripcionTab);
        }
        
        
        
        System.out.println(producto.getCategoria());
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
    private void complemento(ActionEvent event) {
    	if(Npollo.isSelected()) {
    		complemento = "Nuggets de pollo";
    	} else if(Acebolla.isSelected()) {
    		complemento = "Aros de cebolla";
    	} else if(Psupreme.isSelected()) {
    		complemento = "Patatas supreme";
    	} else if(Pclasica.isSelected()) {
    		complemento = "Patatas clasicas";
    	}
    	
    	System.out.println(complemento);
    }
    
    @FXML
    private void bebida(ActionEvent event) {
    	if(Cocacola.isSelected()) {
    		bebida = "Cocacola";
    	} else if(Fanta.isSelected()) {
    		bebida = "Fanta de naranja";
    	} else if(Nestea.isSelected()) {
    		bebida = "Nestea";
    	} else if(CcZero.isSelected()) {
    		bebida = "Cocacola Zero";
    	}
    	
    	System.out.println(bebida);
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
    
    public void mostrarModificaProducto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/ModificaProducto.fxml"));
            AnchorPane ModificarProductoPane = loader.load();

            // Obtener el controlador de la vista de ModificarProducto
            ModificarProducto ModificarProductoController = loader.getController();

            // Pasar el producto actual al controlador de ModificarProducto
            ModificarProductoController.setProducto(producto);

            // Configurar la ventana y mostrar la interfaz
            Stage ModificarProductoStage = new Stage();
            ModificarProductoStage.initStyle(StageStyle.TRANSPARENT);
            ModificarProductoStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(ModificarProductoPane, 800, 600);
            ModificarProductoStage.setScene(scene);

            ModificarProductoStage.setTitle("Modificar Producto");
            ModificarProductoStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void eliminaProducto() {
    	try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sql = "DELETE FROM carta WHERE id_producto = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {                
                stmt.setInt(1, producto.getIdProducto());

                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    mostrarAlerta(AlertType.INFORMATION, "Borrado", "Producto borrado correctamente.");
                    cerrar();
                } else {
                    mostrarAlerta(AlertType.ERROR, "Error", "No se pudo borrar el producto.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error de conexión", "No se pudo conectar a la base de datos.");
        }
    	cerrar();
    }
    
    public void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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
            loginStage.setTitle("LOGIN");
            loginStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
