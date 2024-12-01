package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Modelos.DetallePedido;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GestionPedidos implements Initializable {
    
    @FXML
    Text Username;
	@FXML
	private ScrollPane scrollPanePedidosEnCurso;
	@FXML
	private ScrollPane scrollPanePedidosFinalizados;
	 @FXML
	    private ImageView imagenperfil;

	@FXML
	private VBox vboxPedidosEnCurso;
	@FXML
	private VBox vboxPedidosFinalizados;
	
    @FXML
    private Button Cerrar;
    
    private boolean Panel_Visible = false;
    private boolean Cerrardesplegar = false;
    @FXML
    private AnchorPane Panel_Desplegable;
    @FXML
    private Button Desplegable;
   
    @FXML
    private Button menuadmin;
    @FXML
    private Button usuariosadmin;
    @FXML
    private Button pedidosadmin;
    
    @FXML
    private Accordion administradores;
    @FXML
    private TitledPane titledpaneadmin;
    @FXML
    private Button reservaadmin;
   
    @FXML
    private VBox Vboxadmin;
    
    private int ocultar =3;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
        	
        	 if (Login.tipo.equals("administradores")) {
                 administradores.setVisible(true);
                 titledpaneadmin.setVisible(true);
                 Vboxadmin.setVisible(true);
                 usuariosadmin.setDisable(false);
                 reservaadmin.setDisable(false);
                 
                 menuadmin.setDisable(false);
                 
             }

             if (Login.tipo.equals("empleados")) {
                 administradores.setVisible(true);
                 titledpaneadmin.setVisible(true);
                 Vboxadmin.setVisible(true);
                
                 System.out.println("llegue");

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
     				if (permisos(1, "lectura") == 1) {
     				    menuadmin.setDisable(false);
     				} else {
     				    menuadmin.setDisable(true);
     				}
     			} catch (SQLException e) {
     				// TODO Auto-generated catch block
     				e.printStackTrace();
     			}

                 try {
     				if (permisos(3, "escritura") == 0) {
     				    ocultar=0;
     				} else {
     				    ocultar=1;
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
        	
        	
        	
        	 Username.textProperty().bind(Login.bannerusuarioProperty());
            cargarPedidosEnCurso();
            cargarPedidosFinalizados();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        
        // Cargar la imagen de perfil desde la ruta especificada
        String rutaImagen = "file:src/main/resources/imagenes/" + Login.datos_login.getRuta();
        Image imagen = new Image(rutaImagen);

        // Configurar un listener para cargar la imagen cuando cambie la ruta en Login
        Login.imagenProperty().addListener((observable, oldValue, newValue) -> {
            cargarImagen(newValue);
        });

        // Si hay una ruta de imagen válida, asignarla a la propiedad de la imagen
        if (Login.datos_login.getRuta() != null) {
            Login.imagen.set(Login.datos_login.getRuta());
        }

        // Configurar un rectángulo con esquinas redondeadas para la imagen de perfil
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(
            imagenperfil.getFitWidth()-5,  // Ancho del rectángulo
            imagenperfil.getFitHeight()-5  // Alto del rectángulo
        );
        clip.setArcWidth(30);  // Radio de las esquinas horizontales
        clip.setArcHeight(30); // Radio de las esquinas verticales

        // Establecer el clip para la imagen de perfil
        imagenperfil.setClip(clip);
        
        
        if (imagen.isError()) {
            System.err.println("Error al cargar la imagen desde la ruta: " + rutaImagen);
        } else {
            imagenperfil.setImage(imagen);
        }
    }

    public void perfil() throws IOException {
    	if (!(Login.tipo.equals("usuarios"))) {
        		
        		cerrar();
        		Mostrar_Login();
        	}
    	else {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/perfil.fxml"));
            Pane perfilpane = cargador.load();
            Scene perfilScene = new Scene(perfilpane, 600, 500);
           
            perfilScene.setFill(Color.TRANSPARENT);
            Stage perfilStage = new Stage();
            perfilStage.setResizable(false);
            perfilStage.initStyle(StageStyle.DECORATED);
            perfilStage.setScene(perfilScene);
            perfilStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
            perfilStage.setTitle("PERFIL");
            perfilStage.show();
            cerrar();
    	}
        }
    
    private void cargarImagen(String nuevaRuta) {
        String rutaImagen = "file:src/main/resources/imagenes/" + nuevaRuta;
        Image imagen = new Image(rutaImagen);
        if (imagen.isError()) {
            System.err.println("Error al cargar la imagen desde la ruta: " + rutaImagen);
        } else {
            imagenperfil.setImage(imagen); 
        }
    }
    public void Mostrar_Login() {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
            Pane login = cargador.load();
            Scene loginScene = new Scene(login, 450, 600);
            loginScene.setFill(Color.TRANSPARENT);
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);
            loginStage.setScene(loginScene);
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
            loginStage.setTitle("LOGIN");
            loginStage.show();
            cerrar();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cargarPedidosEnCurso() throws SQLException {
        vboxPedidosEnCurso.getChildren().clear(); // Limpiar el VBox antes de cargar
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sql = "SELECT * FROM pedidos WHERE estado = 'en_curso'";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                int idPedido = resultado.getInt("id_pedido");

                // Crear el AnchorPane con estilos
                AnchorPane pedidoPane = new AnchorPane();
                pedidoPane.setPrefSize(1, 1); // Ajustar el tamaño fijo para el AnchorPane
                pedidoPane.setStyle("-fx-background-color: #A6234E; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #FFFFFF");

                // Sombra para el panel
                DropShadow dropShadow = new DropShadow();
                dropShadow.setOffsetX(1.0);
                dropShadow.setOffsetY(2.0);
                dropShadow.setColor(Color.BLACK);
                pedidoPane.setEffect(dropShadow);

                // Crear un VBox para alinear los botones dentro del AnchorPane
                AnchorPane vboxBotones = new AnchorPane();
                 // Espacio entre botones
                vboxBotones.setPrefWidth(130); // Establecer el ancho fijo para el VBox

                // Botón del pedido
                Button botonPedido = new Button("Pedido: " + idPedido);
                
                botonPedido.setStyle("-fx-background-color: #D28383; -fx-text-fill: white; -fx-font-weight: bold;");
                botonPedido.setOnAction(event -> mostrarDetallesPedido(idPedido));
                AnchorPane.setLeftAnchor(botonPedido, 10.0);
                AnchorPane.setTopAnchor(botonPedido, 10.0);
                

                // Botón para finalizar el pedido
                Button btnFinalizar = new Button("Finalizar Pedido");
                if (ocultar==0) {btnFinalizar.setDisable(true);}
                btnFinalizar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                btnFinalizar.setOnAction(event -> finalizarPedido(idPedido));
                AnchorPane.setLeftAnchor(btnFinalizar, 10.0);
                AnchorPane.setTopAnchor(btnFinalizar, 40.0);
                AnchorPane.setBottomAnchor(btnFinalizar, 10.0);   // Margen inferior de 10 píxeles

                // Agregar los botones al VBox
                vboxBotones.getChildren().addAll(botonPedido, btnFinalizar);

                // Agregar el VBox al AnchorPane
                pedidoPane.getChildren().add(vboxBotones);

                // Agregar el AnchorPane al VBox principal
                vboxPedidosEnCurso.getChildren().add(pedidoPane);
            }
        }
    }
    
  
    public void Carta() throws IOException {
        
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
        Pane cartapanel = cargador.load();
        Scene cartaScene = new Scene(cartapanel, 600, 500);
        cartaScene.setFill(Color.TRANSPARENT);

        Stage cartaStage = new Stage();
        cartaStage.setResizable(false);
        cartaStage.initStyle(StageStyle.DECORATED);
        cartaStage.setScene(cartaScene);
        cartaStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        cartaStage.setTitle("CARTA");
        cartaStage.show();
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
        horariosStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        horariosStage.setTitle("HORARIOS");
        horariosStage.show();
        cerrar();
    }
    
    public void Ubicacion() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Ubicacion.fxml"));
        Pane ubicacionpane = cargador.load();
        Scene ubicacionScene = new Scene(ubicacionpane, 600, 500);
        ubicacionScene.setFill(Color.TRANSPARENT);

        Stage ubicacionStage = new Stage();
        ubicacionStage.setResizable(false);
        ubicacionStage.initStyle(StageStyle.DECORATED);
        ubicacionStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        ubicacionStage.setScene(ubicacionScene);
        ubicacionStage.setTitle("UBIACION");
        ubicacionStage.show();
        cerrar();  
    }
    
    public void ReservaAdmin() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/ReservaAdmin.fxml"));
        Pane reservapane = cargador.load();
        Scene resevaScene = new Scene(reservapane, 600, 500);
        resevaScene.setFill(Color.TRANSPARENT);
        Stage reservaStage = new Stage();
        reservaStage.setResizable(false);
        reservaStage.initStyle(StageStyle.DECORATED);
        reservaStage.setScene(resevaScene);
        reservaStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        reservaStage.setTitle("PANEL DE GESTION DE RESERVAS");
        reservaStage.show();
        cerrar();
         
    }
    
    public void Gestionpedidos() throws IOException {
  	  FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/GestionPedidos.fxml"));
        Pane gestionpedidospane = cargador.load();
        Scene gestionpedidosScene = new Scene(gestionpedidospane, 600, 500);
       
        gestionpedidosScene.setFill(Color.TRANSPARENT);
        Stage gestionpedidosStage = new Stage();
        gestionpedidosStage.setResizable(false);
        gestionpedidosStage.initStyle(StageStyle.DECORATED);
        gestionpedidosStage.setScene(gestionpedidosScene);
        gestionpedidosStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        gestionpedidosStage.setTitle("PANEL DE GESTION DE PEDIDOS");
        gestionpedidosStage.show();
        cerrar();
  	
  	
  	
  }
    
    
    public void Despliega() {
        System.out.println("Funciona");
        Cerrardesplegar = !Cerrardesplegar;
        Panel_Visible = !Panel_Visible;
        Cerrar.setVisible(Cerrardesplegar);
        Panel_Desplegable.setVisible(Panel_Visible);
    }
    public void Pantalla_Principal() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principalpane = cargador.load();
        Scene principalScene = new Scene(principalpane, 600, 500);
       
        principalScene.setFill(Color.TRANSPARENT);
        Stage PrincipalStage = new Stage();
        PrincipalStage.setResizable(false);
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.setScene(principalScene);
        PrincipalStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        PrincipalStage.setTitle("PANTALLA PRINCIPAL");
        PrincipalStage.show();
        cerrar();
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
    public void Gestion_usuarios() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
        Pane gestionusuariospane = cargador.load();
        Scene gestionusuariosScene = new Scene(gestionusuariospane, 600, 500);
        gestionusuariosScene.setFill(Color.TRANSPARENT);
        Stage gestionusuariosStage = new Stage();
        gestionusuariosStage.setResizable(false);
        gestionusuariosStage.initStyle(StageStyle.DECORATED);
        gestionusuariosStage.setScene(gestionusuariosScene);
        gestionusuariosStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        gestionusuariosStage.setTitle("PANEL DE GESTION DE USUARIOS");
        gestionusuariosStage.show();
        cerrar();  
    }
    public void Reserva() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Reservas.fxml"));
        Pane reservapane = cargador.load();
        Scene reservaScene = new Scene(reservapane, 600, 500);
        reservaScene.setFill(Color.TRANSPARENT);
        Stage reservaStage = new Stage();
        reservaStage.setResizable(false);
        reservaStage.initStyle(StageStyle.DECORATED);
        reservaStage.setScene(reservaScene);
        reservaStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        reservaStage.setTitle("RESERVAS");
        reservaStage.show();
        cerrar();  
    }
    private void cargarPedidosFinalizados() throws SQLException {
        vboxPedidosFinalizados.getChildren().clear(); // Limpiar el VBox antes de cargar
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sql = "SELECT * FROM pedidos WHERE estado = 'finalizado'";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                int idPedido = resultado.getInt("id_pedido");

                // Crear el AnchorPane con estilos
                AnchorPane pedidoPane = new AnchorPane();
                pedidoPane.setPrefSize(1, 1); // Ajustar el tamaño fijo para el AnchorPane
                pedidoPane.setStyle("-fx-background-color: #A6234E; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #FFFFFF");

                // Sombra para el panel
                DropShadow dropShadow = new DropShadow();
                dropShadow.setOffsetX(1.0);
                dropShadow.setOffsetY(2.0);
                dropShadow.setColor(Color.BLACK);
                pedidoPane.setEffect(dropShadow);

                // Crear un VBox para alinear los botones dentro del AnchorPane
                AnchorPane vboxBotones = new AnchorPane();                
                vboxBotones.setPrefWidth(130); // Establecer el ancho fijo para el VBox

                // Botón del pedido
                Button botonPedido = new Button("Pedido: " + idPedido);
               
                botonPedido.setStyle("-fx-background-color: #D28383; -fx-text-fill: white; -fx-font-weight: bold;");
                botonPedido.setOnAction(event -> mostrarDetallesPedido(idPedido));
                AnchorPane.setLeftAnchor(botonPedido, 10.0);
                AnchorPane.setTopAnchor(botonPedido, 10.0);
               

                // Botón para eliminar el pedido
                Button btnEliminar = new Button("Eliminar Pedido");
                if (ocultar==0) {btnEliminar.setDisable(true);}
                btnEliminar.setStyle("-fx-background-color: #FF4C4C; -fx-text-fill: white; -fx-font-weight: bold;");
                btnEliminar.setOnAction(event -> eliminarPedido(idPedido));
                AnchorPane.setLeftAnchor(btnEliminar, 10.0);
                AnchorPane.setTopAnchor(btnEliminar, 40.0);
                
                

                // Botón para devolver a "Pedidos en curso"
                Button btnDevolver = new Button("Devolver a Pedidos en Curso");
                if (ocultar==0) {btnDevolver.setDisable(true);}
                btnDevolver.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                btnDevolver.setOnAction(event -> devolverPedido(idPedido));
                AnchorPane.setLeftAnchor(btnDevolver, 10.0);
                AnchorPane.setTopAnchor(btnDevolver, 70.0);
                AnchorPane.setBottomAnchor(btnDevolver, 10.0);   // Margen

                // Agregar los botones al VBox
                vboxBotones.getChildren().addAll(botonPedido, btnEliminar, btnDevolver);

                // Agregar el VBox al AnchorPane
                pedidoPane.getChildren().add(vboxBotones);

                // Agregar el AnchorPane al VBox principal
                vboxPedidosFinalizados.getChildren().add(pedidoPane);
            }
        }
    }


    private void mostrarDetallesPedido(int idPedido) {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sql = "SELECT ca.nombre,ci.Detalles, ci.cantidad, ci.precio_unitario " +
                         "FROM carta ca " +
                         "JOIN carrito_items ci ON ci.id_plato = ca.id_producto " +
                         "WHERE ci.id_carrito = (SELECT id_carrito FROM pedidos WHERE id_pedido = ? and ci.estado is not null and ci.estado ='Tramitado')";

            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, idPedido);
            ResultSet resultado = sentencia.executeQuery();

            List<DetallePedido> detalles = new ArrayList<>();

            while (resultado.next()) {
                DetallePedido detalle = new DetallePedido(
                		
                    idPedido,
                    resultado.getString("nombre")+""+resultado.getString("Detalles"),
                    resultado.getInt("cantidad"),
                    resultado.getDouble("precio_unitario")
                );
                detalles.add(detalle);
                System.out.println(detalle);
            }

            if (!detalles.isEmpty()) {
                // Cargar la vista FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/DetallesPedido.fxml"));
                AnchorPane detallesPane = loader.load();

                // Obtener el controlador
                DetallesPedidoController controller = loader.getController();

                // Pasar la lista de detalles al controlador
                controller.setDetalles(detalles, idPedido);

                // Mostrar la ventana en un nuevo stage
                Stage detallesStage = new Stage();
                detallesStage.initStyle(StageStyle.UTILITY);
                detallesStage.initModality(Modality.APPLICATION_MODAL);
                detallesStage.setScene(new Scene(detallesPane));
                detallesStage.setTitle("Detalles del Pedido");
                detallesStage.show();
            } else {
                System.out.println("No se encontraron detalles para el pedido con ID: " + idPedido);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    private void finalizarPedido(int idPedido) {
        actualizarEstadoPedido(idPedido, "finalizado");
    }

    private void devolverPedido(int idPedido) {
        actualizarEstadoPedido(idPedido, "en_curso");
    }

    private void eliminarPedido(int idPedido) {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            // Eliminar datos relacionados en carrito_items
            String sqlEliminarCarritoItems = "DELETE FROM carrito_items WHERE id_carrito = (SELECT id_carrito FROM pedidos WHERE id_pedido = ?) and estado = 'Tramitado'";
            PreparedStatement sentenciaEliminarCarritoItems = conexion.prepareStatement(sqlEliminarCarritoItems);
            sentenciaEliminarCarritoItems.setInt(1, idPedido);
            int resultadoCarritoItems = sentenciaEliminarCarritoItems.executeUpdate();
            if (resultadoCarritoItems > 0) {
                System.out.println("Datos de carrito_items eliminados para el pedido: " + idPedido);
            }

            // Eliminar el pedido
            String sqlEliminarPedido = "DELETE FROM pedidos WHERE id_pedido = ?";
            PreparedStatement sentenciaEliminarPedido = conexion.prepareStatement(sqlEliminarPedido);
            sentenciaEliminarPedido.setInt(1, idPedido);
            int resultadoPedido = sentenciaEliminarPedido.executeUpdate();
            if (resultadoPedido > 0) {
                System.out.println("Pedido eliminado.");
                cargarPedidosFinalizados();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void actualizarEstadoPedido(int idPedido, String nuevoEstado) {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sql = "UPDATE pedidos SET estado = ? WHERE id_pedido = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, nuevoEstado);
            sentencia.setInt(2, idPedido);
            int resultado = sentencia.executeUpdate();
            if (resultado > 0) {
                System.out.println("Estado del pedido actualizado a: " + nuevoEstado);
                cargarPedidosEnCurso();
                cargarPedidosFinalizados();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }
}
