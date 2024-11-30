package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Horarios implements Initializable {
	 @FXML
	    Text Username;
	    @FXML
	    private AnchorPane Panel_Desplegable;
	    @FXML
	    private Button Desplegable;
	    @FXML
	    private Button Cerrar;
	    @FXML
	    private ImageView imagenperfil;

	    private boolean Panel_Visible = false;
	    private boolean Cerrardesplegar = false;
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
	    @FXML
	    private Button reservaadmin;
	    @FXML
	    private Button menuadmin;
	    
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub
			
			 Username.textProperty().bind(Login.bannerusuarioProperty());
			 
			 if (Login.tipo.equals("administradores")) {
		            administradores.setVisible(true);
		            titledpaneadmin.setVisible(true);
		            Vboxadmin.setVisible(true);
		            usuariosadmin.setDisable(false);
		            botoncarrito.setVisible(false);
		            pedidosadmin.setDisable(false);
		            menuadmin.setDisable(false);
		            reservaadmin.setDisable(false);
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
	    
	    
	    public void cerrar() {
	        Stage stage = (Stage) Cerrar.getScene().getWindow();
	        stage.close();
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
	        PrincipalStage.setTitle("PANTALLA PRINCIPAL");
	        PrincipalStage.show();
	        cerrar();
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

	    public void ReservaAdmin() throws IOException {
	        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/ReservaAdmin.fxml"));
	        Pane reservaadmin = cargador.load();
	        Scene reservaadminScene = new Scene(reservaadmin, 600, 500);
	        reservaadminScene.setFill(Color.TRANSPARENT);
	        Stage reservaadminStage = new Stage();
	        reservaadminStage.setResizable(false);
	        reservaadminStage.initStyle(StageStyle.DECORATED);
	        reservaadminStage.setScene(reservaadminScene);
	        reservaadminStage.setTitle("PANEL DE GESTION DE RESERVAS");
	        reservaadminStage.show();
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
	            loginStage.setScene(loginScene);
	            loginStage.initModality(Modality.APPLICATION_MODAL);
	            loginStage.setTitle("LOGIN");
	            loginStage.show();
	            cerrar();
	        } catch (Exception e) {
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
	    public void Gestionpedidos() throws IOException {
	    	  FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/GestionPedidos.fxml"));
	          Pane gestiondepedidospanel = cargador.load();
	          Scene gestiondepedidosScene = new Scene(gestiondepedidospanel, 600, 500);
	         
	          gestiondepedidosScene.setFill(Color.TRANSPARENT);
	          Stage gestiondepedidosStage = new Stage();
	          gestiondepedidosStage.setResizable(false);
	          gestiondepedidosStage.initStyle(StageStyle.DECORATED);
	          gestiondepedidosStage.setScene(gestiondepedidosScene);
	          gestiondepedidosStage.setTitle("PANEL DE GESTION DE PEDIDOS");
	          gestiondepedidosStage.show();
	          cerrar();
	    	
	    	
	    	
	    }

	    public void Gestion_usuarios() throws IOException {
	        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
	        Pane gestiondeusuariopanel = cargador.load();
	        Scene gestiondeusuarioScene = new Scene(gestiondeusuariopanel, 600, 500);
	        gestiondeusuarioScene.setFill(Color.TRANSPARENT);
	        Stage gestiondeusuariosStage = new Stage();
	        gestiondeusuariosStage.setResizable(false);
	        gestiondeusuariosStage.initStyle(StageStyle.DECORATED);
	        gestiondeusuariosStage.setScene(gestiondeusuarioScene);
	        gestiondeusuariosStage.setTitle("PANEL DE GESTION DE USUARIOS");
	        gestiondeusuariosStage.show();
	        cerrar();
	    }


}
