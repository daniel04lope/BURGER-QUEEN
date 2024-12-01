package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class perfil implements Initializable {
	 	@FXML
	    private Accordion administradores;
	    @FXML
	    private TitledPane titledpaneadmin;
	    @FXML
	    private VBox Vboxadmin;
	
		private boolean Panel_Visible = false;
	    private boolean Cerrardesplegar = false;
	 	@FXML
	    private Button reservaadmin;
	    @FXML
	    private Button menuadmin;
	    @FXML
	    private Button usuariosadmin;
	    @FXML
	    private Button pedidosadmin;
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
	    @FXML
	    private Button botoncarrito;
	    @FXML
	    private TextField txtnombre,txtapellido,txtemail,txtusername,txtpassword,txttelefono,txtdireccion;
	    @FXML
	    private DatePicker txtfechanacimiento;
	    

	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
	      
	        txtnombre.setText(Login.datos_login.getNombre());
	        txtapellido.setText(Login.datos_login.getApellido());
	        txtemail.setText(Login.datos_login.getEmail());
	        txtusername.setText(Login.datos_login.getUsername());
	        txtpassword.setText(Login.datos_login.getPassword());
	        txttelefono.setText(Login.datos_login.getTelefono());
	        txtdireccion.setText(Login.datos_login.getDireccion());
	        txtfechanacimiento.setValue(Login.datos_login.getFechaNacimiento());

	       
	        if (Login.datos_login.getRuta() != null && !Login.datos_login.getRuta().isEmpty()) {
	            try {
	               
	                String rutaImagen = "file:imagenes/" + Login.datos_login.getRuta();
	                Image imagen = new Image(rutaImagen);

	                
	                if (!imagen.isError()) {
	                    imagenperfil.setImage(imagen);
	                } else {
	                    System.err.println("Error al cargar la imagen desde la ruta: " + rutaImagen);
	                }
	            } catch (Exception e) {
	                System.err.println("Error al cargar la imagen: " + e.getMessage());
	            }
	        } else {
	            System.out.println("No se encontró una ruta válida para la imagen.");
	        }
	    }
	    
	    
	    
	    public void guardar() throws SQLException {
	        String nombre = txtnombre.getText();
	        String apellidos = txtapellido.getText();
	        String email = txtemail.getText();
	        String username = txtusername.getText();
	        String password = txtpassword.getText();
	        String telefono = txttelefono.getText();
	        String direccion = txtdireccion.getText();
	        LocalDate fechaNacimiento = txtfechanacimiento.getValue();
	        Date fechana = Date.valueOf(fechaNacimiento);

	        // Obtener la ruta de la imagen desde el campo de texto o desde la variable de Login
	        String rutaImagen = Login.datos_login.getRuta();  // Suponiendo que la ruta se almacena allí

	        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
	            String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, email = ?, username = ?, password = ?, telefono = ?, direccion = ?, fecha_nacimiento = ?, ruta = ? WHERE id_usuario = ?";

	            PreparedStatement sentencia = conexion.prepareStatement(sql);
	            sentencia.setString(1, nombre);
	            sentencia.setString(2, apellidos);
	            sentencia.setString(3, email);
	            sentencia.setString(4, username);
	            sentencia.setString(5, password);
	            sentencia.setString(6, telefono);
	            sentencia.setString(7, direccion);
	            sentencia.setDate(8, fechana);

	            // Establecer la ruta de la imagen
	            if (rutaImagen != null && !rutaImagen.isEmpty()) {
	                sentencia.setString(9, rutaImagen);
	            } else {
	                sentencia.setNull(9, java.sql.Types.VARCHAR);  // Si la ruta está vacía, establece NULL
	            }

	            // Establecer el ID de usuario
	            sentencia.setInt(10, Login.datos_login.getIdUsuario());

	            // Ejecutar la actualización
	            int filasAfectadas = sentencia.executeUpdate();
	            if (filasAfectadas > 0) {
	                System.out.println("Datos actualizados correctamente.");
	            } else {
	                System.out.println("No se actualizaron registros.");
	            }
	        }
	    }

	    
	    @FXML
	    public void seleccionarImagen() {
	        // Crea un FileChooser
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif"));
	        
	        // Abre el FileChooser y espera a que el usuario seleccione un archivo
	        File archivo = fileChooser.showOpenDialog(null);
	        
	        if (archivo != null) {
	            // Carga la imagen seleccionada y la muestra en la ImageView
	            Image imagen = new Image(archivo.toURI().toString());
	            imagenperfil.setImage(imagen);

	            // Define la carpeta destino
	            Path carpetaDestino = Paths.get("src/main/resources/imagenes");

	            // Extrae el nombre original del archivo (incluye la extensión)
	            String nombreArchivo = archivo.getName();
	            Path archivoDestino = carpetaDestino.resolve(nombreArchivo);

	            try {
	                // Crea la carpeta 'imagenes' si no existe
	                if (!Files.exists(carpetaDestino)) {
	                    Files.createDirectories(carpetaDestino);
	                }

	                // Copia el archivo seleccionado a la carpeta de destino
	                Files.copy(archivo.toPath(), archivoDestino, StandardCopyOption.REPLACE_EXISTING);
	                System.out.println("Imagen guardada en: " + archivoDestino.toString());
	            } catch (IOException e) {
	                System.err.println("Error al guardar la imagen: " + e.getMessage());
	            }
	        } else {
	            System.out.println("No se seleccionó ninguna imagen.");
	        }
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
    
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
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
    public void Ubicacion() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Ubicacion.fxml"));
        Pane ubiacionpane = cargador.load();
        Scene ubicacionScene = new Scene(ubiacionpane, 600, 500);
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
        Pane reservapane = cargador.load();
        Scene reservaScene = new Scene(reservapane, 600, 500);
        reservaScene.setFill(Color.TRANSPARENT);
        Stage reservaStage = new Stage();
        reservaStage.setResizable(false);
        reservaStage.initStyle(StageStyle.DECORATED);
        reservaStage.setScene(reservaScene);
        reservaStage.setTitle("PANEL DE GESTION DE RESERVAS");
        reservaStage.show();
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
    public void carrito () throws IOException {
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
