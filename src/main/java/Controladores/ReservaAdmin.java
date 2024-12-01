package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ResourceBundle;

import Modelos.ReservaObjeto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ReservaAdmin implements Initializable {
	
	@FXML
	private GridPane Listado;
	@FXML
    private Button Cerrar;	
	
	private Reserva reserva;
	
	

	    @FXML
	    private AnchorPane Panel_Desplegable;
	    @FXML
	    private Button Desplegable;
	   
	   

	    private boolean Panel_Visible = false;
	    private boolean Cerrardesplegar = false;
	    @FXML
	    private Button botoncarrito;
	    @FXML
	    private Accordion administradores;
	    @FXML
	    private TitledPane titledpaneadmin;
	    @FXML
	    private VBox Vboxadmin;
	  
	    
	    @FXML
	    private Button menuadmin;
	    @FXML
	    private Button usuariosadmin;
	    @FXML
	    private Button pedidosadmin;
	    
	    Button btnModificar;
	    Button btnEliminar;
	    int oculta =3;

	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
	       
	        
	        
	        
	        if (Login.tipo.equals("administradores")) {
	            administradores.setVisible(true);
	            titledpaneadmin.setVisible(true);
	            Vboxadmin.setVisible(true);
	            usuariosadmin.setDisable(false);
	            botoncarrito.setVisible(false);
	            pedidosadmin.setDisable(false);
	            menuadmin.setDisable(false);
	          
	        }

	        if (Login.tipo.equals("empleados")) {
	            administradores.setVisible(true);
	            titledpaneadmin.setVisible(true);
	            Vboxadmin.setVisible(true);
	            botoncarrito.setVisible(false);
	          
	            System.out.println("llegue");

	            // Verificar permisos para cada botón
	            try {
					if (permisos(2, "lectura") == 1) {
					  
					} else {
					   
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
					if (permisos(2, "escritura") == 1) {
					    
					  
					    oculta =1;
					} else {
					   
					   
					    oculta=0;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            try {
					if (permisos(2, "lectura") == 1) {
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
	        
	        
	        try {
	            inicializarListado();
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
	    
	    
	    
	    private void cargarImagen(String nuevaRuta) {
	        String rutaImagen = "file:src/main/resources/imagenes/" + nuevaRuta;
	        Image imagen = new Image(rutaImagen);
	      
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
	    
	    
	    public void Gestionpedidos() throws IOException {
	    	  FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/GestionPedidos.fxml"));
	          Pane panegestiondepedido = cargador.load();
	          Scene gestionpedidoScene = new Scene(panegestiondepedido, 600, 500);
	         
	          gestionpedidoScene.setFill(Color.TRANSPARENT);
	          Stage gestiondepedidoStage = new Stage();
	          gestiondepedidoStage.setResizable(false);
	          gestiondepedidoStage.initStyle(StageStyle.DECORATED);
	          gestiondepedidoStage.setScene(gestionpedidoScene);
	          gestiondepedidoStage.setTitle("PANEL DE GESTION DE PEDIDOS");
	          gestiondepedidoStage.show();
	          cerrar();
	    	
	    	
	    	
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
	            perfilStage.setTitle("PERFIL");
	            perfilStage.show();
	            cerrar();
	    	}
	        }
	    private void inicializarListado() throws SQLException {
	        Listado.setHgap(10);
	        Listado.setVgap(10);
	        Listado.setPadding(new Insets(10, 10, 10, 10));
	        Listado.setStyle("-fx-background-color: #D28383;");

	        try {
	            Muestra_reservas();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public void Muestra_reservas() throws SQLException {
	        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
	            String sqlReservas = "SELECT id_reserva, nombre_cliente, fecha_reserva, hora_reserva, numero_personas, notas, estado, fecha_creacion, mesa FROM reservas";
	            PreparedStatement sentenciaReservas = conexion.prepareStatement(sqlReservas);
	            ResultSet resultadoReservas = sentenciaReservas.executeQuery();
	            
	            DropShadow dropShadow = new DropShadow();
	            dropShadow.setOffsetX(1.0);
	            dropShadow.setOffsetY(2.0);
	            dropShadow.setColor(Color.BLACK); 

	            

	            int row = 0;
	            while (resultadoReservas.next()) {
	                
	                int idReserva = resultadoReservas.getInt("id_reserva");
	                String nombreCliente = resultadoReservas.getString("nombre_cliente");
	                Date fechaReserva = resultadoReservas.getDate("fecha_reserva");
	                Time horaReserva = resultadoReservas.getTime("hora_reserva");
	                int numeroPersonas = resultadoReservas.getInt("numero_personas");
	                String notas = resultadoReservas.getString("notas");
	                String estado = resultadoReservas.getString("estado");
	                Date fechaCreacion = resultadoReservas.getDate("fecha_creacion");
	                int mesa= resultadoReservas.getInt("mesa");

	                ReservaObjeto reservaObjeto = new ReservaObjeto(idReserva, nombreCliente, fechaReserva.toLocalDate(), horaReserva.toLocalTime(),
	                        numeroPersonas, notas, estado, fechaCreacion.toLocalDate(),mesa);

	                
	                AnchorPane reservaPane = new AnchorPane();
	                reservaPane.setPrefSize(450, 200); 
	                reservaPane.setStyle("-fx-background-color: #A6234E; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #FFFFFF");
	                
	                reservaPane.setEffect(dropShadow);
	                
	                Label idReservaLabel = new Label("ID Reserva: " + reservaObjeto.getIdReserva());
	                idReservaLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
	                AnchorPane.setLeftAnchor(idReservaLabel, 10.0);
	                AnchorPane.setTopAnchor(idReservaLabel, 10.0);
	                
	                reservaPane.getChildren().add(idReservaLabel);
	                

	                Label clienteLabel = new Label("Cliente: " + reservaObjeto.getNombreCliente());
	                clienteLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;-fx-font-weight: bold");
	                AnchorPane.setLeftAnchor(clienteLabel, 10.0);
	                AnchorPane.setTopAnchor(clienteLabel, 30.0);
	                reservaPane.getChildren().add(clienteLabel);

	                Label fechaHoraLabel = new Label("Fecha: " + reservaObjeto.getFechaReserva() + " | Hora: " + reservaObjeto.getHoraReserva());
	                fechaHoraLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;-fx-font-weight: bold");
	                AnchorPane.setLeftAnchor(fechaHoraLabel, 10.0);
	                AnchorPane.setTopAnchor(fechaHoraLabel, 50.0);
	                reservaPane.getChildren().add(fechaHoraLabel);

	                Label personasLabel = new Label("Personas: " + reservaObjeto.getNumeroPersonas());
	                personasLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;-fx-font-weight: bold");
	                AnchorPane.setLeftAnchor(personasLabel, 10.0);
	                AnchorPane.setTopAnchor(personasLabel, 70.0);
	                reservaPane.getChildren().add(personasLabel);

	                Label notasLabel = new Label("Notas: " + (reservaObjeto.getNotas() != null ? reservaObjeto.getNotas() : "Sin notas"));
	                notasLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;-fx-font-weight: bold");
	                AnchorPane.setLeftAnchor(notasLabel, 10.0);
	                AnchorPane.setTopAnchor(notasLabel, 90.0);
	                reservaPane.getChildren().add(notasLabel);
	                
	                Label mesaLabel = new Label("Mesa: " + reservaObjeto.getMesa());
	                mesaLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
	                AnchorPane.setLeftAnchor(mesaLabel, 10.0);
	                AnchorPane.setTopAnchor(mesaLabel, 110.0);
	                reservaPane.getChildren().add(mesaLabel);

	                Label estadoLabel = new Label("Estado: " + reservaObjeto.getEstado());
	                estadoLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
	                AnchorPane.setLeftAnchor(estadoLabel, 10.0);
	                AnchorPane.setTopAnchor(estadoLabel, 130.0);
	                reservaPane.getChildren().add(estadoLabel);
	                
	                
	                
	                
	                btnEliminar = new Button("Eliminar");
	                if (oculta ==0) {btnEliminar.setVisible(false);}
	                btnEliminar.setStyle("-fx-background-color: #FF4C4C; -fx-text-fill: white; -fx-font-weight: bold;");
	                btnEliminar.setOnAction(event -> eliminarReserva(reservaObjeto));
	                AnchorPane.setRightAnchor(btnEliminar, 10.0);
	                AnchorPane.setTopAnchor(btnEliminar, 10.0);
	                reservaPane.getChildren().add(btnEliminar);

	               
	                btnModificar = new Button("Modificar");
	                if (oculta ==0) {btnModificar.setVisible(false);}
	                btnModificar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
	                btnModificar.setOnAction(event -> modificarReserva(reservaObjeto));
	                AnchorPane.setRightAnchor(btnModificar, 10.0);
	                AnchorPane.setTopAnchor(btnModificar, 50.0);
	                reservaPane.getChildren().add(btnModificar);

	               

	                Listado.add(reservaPane, 0, row);
	                row++;
	            }
	        }
	    }
	    
	    public void carrito () throws IOException {
	    	try {
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
	    
	    public void Reserva() throws IOException {
	        FXMLLoader cargado = new FXMLLoader(getClass().getResource("/Vistas/Reservas.fxml"));
	        Pane reserva = cargado.load();
	        Scene reservaScene = new Scene(reserva, 600, 500);
	        reservaScene.setFill(Color.TRANSPARENT);
	        Stage reservaStage = new Stage();
	        reservaStage.setResizable(false);
	        reservaStage.initStyle(StageStyle.DECORATED);
	        reservaStage.setScene(reservaScene);
	        reservaStage.setTitle("RESERVA");
	        reservaStage.show();
	        cerrar();
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
	    
	    public void Ubicacion() throws IOException {
	        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Ubicacion.fxml"));
	        Pane ubicacionpane = cargador.load();
	        Scene ubicacionScene = new Scene(ubicacionpane, 600, 500);
	        ubicacionScene.setFill(Color.TRANSPARENT);

	        Stage ubicacionStage = new Stage();
	        ubicacionStage.setResizable(false);
	        ubicacionStage.initStyle(StageStyle.DECORATED);
	        ubicacionStage.setScene(ubicacionScene);
	        ubicacionStage.setTitle("UBICACION");
	        ubicacionStage.show();
	        cerrar();  
	    }
	    
	    private void eliminarReserva(ReservaObjeto reservaObjeto) {
	        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
	            String sqlEliminar = "DELETE FROM reservas WHERE id_reserva = ?";
	            PreparedStatement sentencia = conexion.prepareStatement(sqlEliminar);
	            sentencia.setInt(1, reservaObjeto.getIdReserva());
	            int resultado = sentencia.executeUpdate();
	            if (resultado > 0) {
	                System.out.println("Reserva eliminada con éxito.");
	                Listado.getChildren().clear();
	                Muestra_reservas();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    private void modificarReserva(ReservaObjeto reservaObjeto) {
	        System.out.println("Modificar reserva con ID: " + reservaObjeto.getIdReserva());
	        try {
	        	cerrar();
	            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/ModificaReserva.fxml"));
	            AnchorPane ModificarReservaPane = cargador.load();
	            
	            ModificarReserva ModificarReservaController = cargador.getController();
	            ModificarReservaController.setReserva(reservaObjeto);

	            Stage ModificarReservaStage = new Stage();
	            ModificarReservaStage.initStyle(StageStyle.DECORATED);
	            ModificarReservaStage.initModality(Modality.APPLICATION_MODAL);
	            Scene scene = new Scene(ModificarReservaPane, 669, 600);
	            ModificarReservaStage.setScene(scene);

	            ModificarReservaStage.setTitle("MODIFICAR RESERVA");
	            ModificarReservaStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    
	    
	    
	public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }
	
	 public void Mostrar_Login() {
	        try {
	            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
	            Pane login = cargador.load();
	            Scene loginScene = new Scene(login, 450, 600);
	            loginScene.setFill(Color.TRANSPARENT);
	            Stage loginStage = new Stage();
	            loginStage.setResizable(false);
	            loginStage.initStyle(StageStyle.TRANSPARENT);
	            loginStage.setScene(loginScene);
	            loginStage.setTitle("LOGIN");
	            loginStage.show();
	            cerrar();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public void Despliega() {
	        System.out.println("Funciona");
	        Cerrardesplegar = !Cerrardesplegar;
	        Panel_Visible = !Panel_Visible;
	        Cerrar.setVisible(Cerrardesplegar);
	        Panel_Desplegable.setVisible(Panel_Visible);
	    }
	 
	   public void Carta() throws IOException {
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
	 public void Gestion_usuarios() throws IOException {
	        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
	        Pane gestiondeusuariopane = cargador.load();
	        Scene gestiondeusuariosScene = new Scene(gestiondeusuariopane, 600, 500);
	        gestiondeusuariosScene.setFill(Color.TRANSPARENT);
	        Stage gestiondeusuariosStage = new Stage();
	        gestiondeusuariosStage.setResizable(false);
	        gestiondeusuariosStage.initStyle(StageStyle.DECORATED);
	        gestiondeusuariosStage.setScene(gestiondeusuariosScene);
	        gestiondeusuariosStage.setTitle("PANEL DE GESTION DE USUARIOS");
	        gestiondeusuariosStage.show();
	        cerrar();  
	    }

}
