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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
	    Text Username;
	    @FXML
	    private AnchorPane Panel_Desplegable;
	    @FXML
	    private Button Desplegable;
	   
	    @FXML
	    private ImageView imagenperfil;

	    private boolean Panel_Visible = false;
	    private boolean Cerrardesplegar = false;


	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
	        try {
	            inicializarListado();
	        } catch (SQLException e) {
	            e.printStackTrace();
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
	                clienteLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
	                AnchorPane.setLeftAnchor(clienteLabel, 10.0);
	                AnchorPane.setTopAnchor(clienteLabel, 30.0);
	                reservaPane.getChildren().add(clienteLabel);

	                Label fechaHoraLabel = new Label("Fecha: " + reservaObjeto.getFechaReserva() + " | Hora: " + reservaObjeto.getHoraReserva());
	                fechaHoraLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
	                AnchorPane.setLeftAnchor(fechaHoraLabel, 10.0);
	                AnchorPane.setTopAnchor(fechaHoraLabel, 50.0);
	                reservaPane.getChildren().add(fechaHoraLabel);

	                Label personasLabel = new Label("Personas: " + reservaObjeto.getNumeroPersonas());
	                personasLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
	                AnchorPane.setLeftAnchor(personasLabel, 10.0);
	                AnchorPane.setTopAnchor(personasLabel, 70.0);
	                reservaPane.getChildren().add(personasLabel);

	                Label notasLabel = new Label("Notas: " + (reservaObjeto.getNotas() != null ? reservaObjeto.getNotas() : "Sin notas"));
	                notasLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
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
	                
	                

	                
	                Button btnEliminar = new Button("Eliminar");
	                btnEliminar.setStyle("-fx-background-color: #FF4C4C; -fx-text-fill: white; -fx-font-weight: bold;");
	                btnEliminar.setOnAction(event -> eliminarReserva(reservaObjeto));
	                AnchorPane.setRightAnchor(btnEliminar, 10.0);
	                AnchorPane.setTopAnchor(btnEliminar, 10.0);
	                reservaPane.getChildren().add(btnEliminar);

	               
	                Button btnModificar = new Button("Modificar");
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
	    
	    public void Reserva() throws IOException {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Reservas.fxml"));
	        Pane registro = loader.load();
	        Scene loginScene = new Scene(registro, 600, 500);
	        loginScene.setFill(Color.TRANSPARENT);
	        Stage loginStage = new Stage();
	        loginStage.setResizable(false);
	        loginStage.initStyle(StageStyle.DECORATED);
	        loginStage.setScene(loginScene);
	        loginStage.setTitle("Reservas");
	        loginStage.show();
	        cerrar();
	    }
	    
	    public void Pantalla_Principal() throws IOException {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
	        Pane principal = loader.load();
	        Scene principalScene = new Scene(principal, 600, 500);
	        
	        principalScene.setFill(Color.TRANSPARENT);
	        Stage PrincipalStage = new Stage();
	        PrincipalStage.initStyle(StageStyle.DECORATED);
	        PrincipalStage.setScene(principalScene);
	        PrincipalStage.setTitle("CARTA");
	        PrincipalStage.show();
	        cerrar();
	    }
	    
	    public void Ubicacion() throws IOException {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Ubicacion.fxml"));
	        Pane registro = loader.load();
	        Scene loginScene = new Scene(registro, 600, 500);
	        loginScene.setFill(Color.TRANSPARENT);

	        Stage loginStage = new Stage();
	        loginStage.setResizable(false);
	        loginStage.initStyle(StageStyle.DECORATED);
	        loginStage.setScene(loginScene);
	        loginStage.setTitle("CARTA");
	        loginStage.show();
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
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/ModificaReserva.fxml"));
	            AnchorPane ModificarReservaPane = loader.load();
	            
	            ModificarReserva ModificarReservaController = loader.getController();
	            ModificarReservaController.setReserva(reservaObjeto);

	            Stage ModificarReservaStage = new Stage();
	            ModificarReservaStage.initStyle(StageStyle.DECORATED);
	            ModificarReservaStage.initModality(Modality.APPLICATION_MODAL);
	            Scene scene = new Scene(ModificarReservaPane, 669, 600);
	            ModificarReservaStage.setScene(scene);

	            ModificarReservaStage.setTitle("Modificar Reserva");
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
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
	            Pane login = loader.load();
	            Scene loginScene = new Scene(login, 450, 600);
	            loginScene.setFill(Color.TRANSPARENT);
	            Stage loginStage = new Stage();
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
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
	        Pane registro = loader.load();
	        Scene loginScene = new Scene(registro, 600, 500);
	        loginScene.setFill(Color.TRANSPARENT);

	        Stage loginStage = new Stage();
	        loginStage.setResizable(false);
	        loginStage.initStyle(StageStyle.DECORATED);
	        loginStage.setScene(loginScene);
	        loginStage.setTitle("CARTA");
	        loginStage.show();
	        cerrar();  
	    }
	 public void Gestion_usuarios() throws IOException {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
	        Pane registro = loader.load();
	        Scene loginScene = new Scene(registro, 600, 500);
	        loginScene.setFill(Color.TRANSPARENT);
	        Stage loginStage = new Stage();
	        loginStage.setResizable(false);
	        loginStage.initStyle(StageStyle.DECORATED);
	        loginStage.setScene(loginScene);
	        loginStage.setTitle("Reservas");
	        loginStage.show();
	        cerrar();  
	    }

}
