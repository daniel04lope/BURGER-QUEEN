package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import Modelos.Producto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Carta implements Initializable {
    @FXML
    private GridPane panel;
    @FXML
    Text Username;
    @FXML
    private AnchorPane Panel_Desplegable;
    @FXML
    private Button Desplegable;
    @FXML
    private Button Cerrar;

    private boolean drawerVisible = false;
    private boolean Cerrardesplegar = false;
    @FXML
    private Button reservaadmin;
    Button crear;
    
    @FXML
    private Button  botoncarrito;
    @FXML
    private Button usuariosadmin;
    @FXML
    private Button pedidosadmin;
    @FXML
    private Accordion administradores;
    @FXML
    private TitledPane titledpaneadmin;
    @FXML
    private VBox Vboxadmin;
    public void Despliega() {
        Cerrardesplegar = !Cerrardesplegar;
        drawerVisible = !drawerVisible;
        Cerrar.setVisible(Cerrardesplegar);
        Panel_Desplegable.setVisible(drawerVisible);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Username.textProperty().bind(Login.bannerusuarioProperty());
        CargarCarta();
        
        
        if (Login.tipo.equals("administradores")) {
            administradores.setVisible(true);
            titledpaneadmin.setVisible(true);
            Vboxadmin.setVisible(true);
            usuariosadmin.setDisable(false);
            
            pedidosadmin.setDisable(false);
            botoncarrito.setVisible(false);
            reservaadmin.setDisable(false);
            crear.setVisible(true);
        }
        if (Login.tipo.equals("empleados")) {
            administradores.setVisible(true);
            titledpaneadmin.setVisible(true);
            Vboxadmin.setVisible(true);
            System.out.println("llegue");
            botoncarrito.setVisible(false);

            // Verificar permisos para cada botón
            try {
				if (permisos(2, "lectura") == 1) {
				    reservaadmin.setDisable(false);
				} else {
				    reservaadmin.setDisable(true);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            try {
				if (permisos(1, "escritura") == 1) {
					 crear.setVisible(true);
				} else {
					 crear.setVisible(false);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            try {
				if (permisos(3, "lectura") == 1) {
				    pedidosadmin.setDisable(false);
				} else {
				    pedidosadmin.setDisable(true);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        if (Login.tipo.equals("usuarios")) {
            administradores.setVisible(false);
            titledpaneadmin.setVisible(false);
            Vboxadmin.setVisible(false);
        }

        try {
            System.out.println(permisos(1, "lectura"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int permisos(int nombreModulo, String tipoPermiso) throws SQLException {
        String sql = "SELECT " + tipoPermiso + " FROM permisos WHERE id_empleado = ? AND id_modulo = ?";
        int valor = 0;

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, Login.datos_login.getIdUsuario());
            sentencia.setInt(2, nombreModulo);
            
            System.out.println("Cadena: " + sentencia);
            
            ResultSet ejecuta = sentencia.executeQuery();

            if (ejecuta.next()) {
                valor = ejecuta.getInt(tipoPermiso);
                System.out.println("Valor: " + valor);
            } else {
                System.out.println("No valor encontrado id_empleado = " + Login.datos_login.getIdUsuario() + " and id_modulo = " + nombreModulo);
            }
        }

        return valor;
    }
    
    public void Ubicacion() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Ubicacion.fxml"));
        Pane ubicacion = cargador.load();
        Scene ubicacionScene = new Scene(ubicacion, 600, 500);
        ubicacionScene.setFill(Color.TRANSPARENT);
       
        Stage ubicacionStage = new Stage();
       
        ubicacionStage.setResizable(false);
        ubicacionStage.initStyle(StageStyle.DECORATED);
        ubicacionStage.setScene(ubicacionScene);
        ubicacionStage.setTitle("UBICACION");
        ubicacionStage.show();
        cerrar();  
    }

    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    public void Pantalla_Principal() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principal = cargador.load();
        Scene principalScene = new Scene(principal, 600, 500);
        principalScene.setFill(Color.TRANSPARENT);
        Stage PrincipalStage = new Stage();
        PrincipalStage.setResizable(false);
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("PANTALLA PRINCIPAL");
        PrincipalStage.show();
        cerrar();
    }

   

    public void CargarCarta() {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sql = "SELECT id_producto, nombre, descripcion, precio, categoria, peso, alergenos, ruta FROM carta";
            Statement sentencia = conexion.createStatement();
            ResultSet productos = sentencia.executeQuery(sql);

            int fila = 0;
            int columna = 0;

            while (productos.next()) {
                Producto productobjeto = new Producto();
                productobjeto.setIdProducto(productos.getInt("id_producto"));
                productobjeto.setNombre(productos.getString("nombre"));
                productobjeto.setPrecio(productos.getDouble("precio"));
                productobjeto.setCategoria(productos.getString("categoria"));
                productobjeto.setPeso(productos.getDouble("peso"));
                productobjeto.setAlergenos("\nALERGENOS:\n" + productos.getString("alergenos"));
                productobjeto.setDescripcion(productos.getString("descripcion"));
                productobjeto.setRuta(productos.getString("ruta"));

                Button btnProducto = new Button();
                VBox item = new VBox();
                btnProducto.setPrefSize(100, 100);
                btnProducto.setStyle("-fx-background-color: TRANSPARENT;");
                ImageView imagen = new ImageView(new Image(getClass().getResourceAsStream("/" + productobjeto.getRuta())));
                imagen.setFitHeight(100.0);
                imagen.setFitWidth(100.0);
                imagen.setPreserveRatio(true);
                btnProducto.setGraphic(imagen);

                Text nombre = new Text(productobjeto.getNombre().toUpperCase());
                nombre.setWrappingWidth(100);
                nombre.setTextAlignment(TextAlignment.CENTER);
                nombre.setStyle("-fx-font-family:Insaniburger");
                item.getChildren().addAll(btnProducto, nombre);
                panel.add(item, columna, fila);
                btnProducto.setOnAction(event -> mostrarItemFocus(productobjeto));

                if (columna == 2) {
                    columna = 0;
                    fila++;
                } else {
                    columna++;
                }

                GridPane.setVgrow(item, javafx.scene.layout.Priority.ALWAYS);
            }

           crear = new Button();
            crear.setVisible(false);
            Image imagen = new Image("/SUMAR.png");
            ImageView imageView = new ImageView(imagen);
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            crear.setGraphic(imageView);
            
            
            crear.setStyle("-fx-background-color: A6234E; -fx-border-color: FFFFFF; -fx-background-radius: 50; -fx-border-radius: 50;");
            crear.setPrefSize(100, 100);

            VBox otro = new VBox();
            otro.setSpacing(10);
            otro.getChildren().add(crear);
            VBox.setMargin(crear, new Insets(5, 5, 5, 5));
            panel.add(otro, columna, fila);

            crear.setOnAction(event -> mostrarNuevoProducto());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarNuevoProducto() {
        try {
        	cerrar();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/NuevoProducto.fxml"));
            AnchorPane NuevoProductoPane = cargador.load();
            Stage NuevoProductoStage = new Stage();
            NuevoProductoStage.initStyle(StageStyle.TRANSPARENT);
            NuevoProductoStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(NuevoProductoPane, 800, 600);
            NuevoProductoStage.setScene(scene);
            NuevoProductoStage.setTitle("NUEVO PRODUCTO");
            NuevoProductoStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void iratras() throws IOException {
        
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
        Pane cartapane = cargador.load();
        Scene cartaScene = new Scene(cartapane, 600, 500);
        cartaScene.setFill(Color.TRANSPARENT);

        Stage cartaStage = new Stage();
        cartaStage.setResizable(false);
        cartaStage.initStyle(StageStyle.DECORATED);
        cartaStage.setScene(cartaScene);
        cartaStage.setTitle("CARTA");
        cartaStage.show();
        cerrar();  
    }

    public void Reserva() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Reservas.fxml"));
        Pane reserva = cargador.load();
        Scene reservaScene = new Scene(reserva, 600, 500);
        reservaScene.setFill(Color.TRANSPARENT);
        Stage reservaStage = new Stage();
        reservaStage.setResizable(false);
        reservaStage.initStyle(StageStyle.DECORATED);
        reservaStage.setScene(reservaScene);
        reservaStage.setTitle("RESERVAS");
        reservaStage.show();
        cerrar();
    }

    public void Mostrar_Login() {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
            Pane login = cargador.load();
            Scene loginScene = new Scene(login, 450, 600);
            loginScene.setFill(Color.TRANSPARENT);
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);
            loginStage.setResizable(false);
            loginStage.setScene(loginScene);
            loginStage.setTitle("LOGIN");
            loginStage.show();
            cerrar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarItemFocus(Producto producto) {
        try {
        	cerrar();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/ItemFocus.fxml"));
            AnchorPane itemFocusPane = cargador.load();
            ItemFocus itemFocusControlador = cargador.getController();
            itemFocusControlador.setProducto(producto);
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
  

    public void carrito() throws IOException {
        try {
        	cerrar();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carrito.fxml"));
            AnchorPane carritoPane = cargador.load();
            Stage carritoStage = new Stage();
            carritoStage.initStyle(StageStyle.TRANSPARENT);
            carritoStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(carritoPane, 800, 623);
            carritoStage.setScene(scene);
            carritoStage.setTitle("CARRITO");
            carritoStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ReservaAdmin() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/ReservaAdmin.fxml"));
        Pane reserva = cargador.load();
        Scene reservaScene = new Scene(reserva, 600, 500);
        reservaScene.setFill(Color.TRANSPARENT);
        Stage resevaStage = new Stage();
        resevaStage.setResizable(false);
        resevaStage.initStyle(StageStyle.DECORATED);
        resevaStage.setScene(reservaScene);
        resevaStage.setTitle("PANEL DE RESERVAS");
        resevaStage.show();
        cerrar();
    }

    public void Gestion_usuarios() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
        Pane gestionusuariopane = cargador.load();
        Scene gestionusuarioScene = new Scene(gestionusuariopane, 600, 500);
        gestionusuarioScene.setFill(Color.TRANSPARENT);
        Stage gestionusuariostage = new Stage();
        gestionusuariostage.setResizable(false);
        gestionusuariostage.initStyle(StageStyle.DECORATED);
        gestionusuariostage.setScene(gestionusuarioScene);
        gestionusuariostage.setTitle("PANEL DE GESTION DE USUARIOS");
        gestionusuariostage.show();
        cerrar();
    }
    public void Gestionpedidos() throws IOException {
  	  FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/GestionPedidos.fxml"));
        Pane gestionpedidospane = cargador.load();
        Scene gestiondepedidosScene = new Scene(gestionpedidospane, 600, 500);
       
        gestiondepedidosScene.setFill(Color.TRANSPARENT);
        Stage gestionpedidosStage = new Stage();
        gestionpedidosStage.setResizable(false);
        gestionpedidosStage.initStyle(StageStyle.DECORATED);
        gestionpedidosStage.setScene(gestiondepedidosScene);
        gestionpedidosStage.setTitle("PANEL DE PEDIDOS");
        gestionpedidosStage.show();
        cerrar();
  }
    
    public void Horarios() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Horarios.fxml"));
        Pane horariospane = cargador.load();
        Scene horariosScene = new Scene(horariospane, 600, 500);
        horariosScene.setFill(Color.TRANSPARENT);
        Stage horariosStage = new Stage();
        horariosStage.setResizable(false);
        horariosStage.initStyle(StageStyle.DECORATED);
        horariosStage.setScene(horariosScene);
        horariosStage.setTitle("HORARIOS");
        horariosStage.show();
        cerrar();
    }
}
