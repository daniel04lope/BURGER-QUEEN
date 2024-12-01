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
import javafx.geometry.Insets;
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
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Gestion_usuarios implements Initializable {

    @FXML
    Text Username;
    @FXML
    private AnchorPane Panel_Desplegable;
    @FXML
    private Button Desplegable;
    @FXML
    private Button Cerrar;
    @FXML
    GridPane Listado;
    @FXML
    private ImageView imagenperfil;
    static int idtraspaso;

    private boolean Panel_Visible = false;
    private boolean Cerrardesplegar = false;
    @FXML
    private Button pedidosadmin;

    public void carrito() throws IOException {
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
    public void ReservaAdmin() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/ReservaAdmin.fxml"));
        Pane reservaadmin = cargador.load();
        Scene reservaadminScene = new Scene(reservaadmin, 600, 500);
        reservaadminScene.setFill(Color.TRANSPARENT);
        Stage reservaadminStage = new Stage();
        reservaadminStage.setResizable(false);
        reservaadminStage.initStyle(StageStyle.DECORATED);
        reservaadminStage.setScene(reservaadminScene);
        reservaadminStage.setTitle("PANEL DE RESERVAS");
        reservaadminStage.show();
         cerrar();
    }

    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    public void Despliega() {
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

    public void Muestra_usuarios() throws SQLException {
        // Limpia el contenido actual del GridPane antes de añadir los nuevos elementos
        Listado.getChildren().clear();

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sqlUsuarios = "SELECT id_empleado AS id, nombre, apellido, email, estado, 'Empleado' AS tipo FROM empleados UNION ALL SELECT id_admin AS id, nombre, apellido, email, estado, 'Administrador' AS tipo FROM administradores";
            PreparedStatement sentenciaUsuarios = conexion.prepareStatement(sqlUsuarios);
            ResultSet resultadoUsuarios = sentenciaUsuarios.executeQuery();
            int row = 0;

            while (resultadoUsuarios.next()) {
                int idUsuario = resultadoUsuarios.getInt("id");
                String nombre = resultadoUsuarios.getString("nombre");
                String apellido = resultadoUsuarios.getString("apellido");
                String email = resultadoUsuarios.getString("email");
                String estado = resultadoUsuarios.getString("estado");
                String tipo = resultadoUsuarios.getString("tipo");

                AnchorPane usuarioPanel = new AnchorPane();
                usuarioPanel.setPrefSize(450, 100);
                usuarioPanel.setStyle("-fx-background-color: A6234E; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: FFFFFF");

                Label nombreUsuario = new Label(nombre + " " + apellido + " (" + tipo + ")");
                nombreUsuario.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
                AnchorPane.setLeftAnchor(nombreUsuario, 10.0);
                AnchorPane.setTopAnchor(nombreUsuario, 10.0);
                usuarioPanel.getChildren().add(nombreUsuario);

                Label emailUsuario = new Label("Email: " + email);
                emailUsuario.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
                AnchorPane.setLeftAnchor(emailUsuario, 10.0);
                AnchorPane.setTopAnchor(emailUsuario, 35.0);
                usuarioPanel.getChildren().add(emailUsuario);

                Label estadoUsuario = new Label("Estado: " + estado);
                estadoUsuario.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
                AnchorPane.setLeftAnchor(estadoUsuario, 10.0);
                AnchorPane.setTopAnchor(estadoUsuario, 60.0);
                usuarioPanel.getChildren().add(estadoUsuario);

                Button botonModificar = new Button();
                botonModificar.setStyle("-fx-background-color: transparent;");
                ImageView iconoModificar = new ImageView(new Image("/lapiz.png"));
                iconoModificar.setFitWidth(20);
                iconoModificar.setFitHeight(20);
                iconoModificar.setPreserveRatio(true);
                botonModificar.setGraphic(iconoModificar);
                botonModificar.setUserData(idUsuario);
                botonModificar.setOnAction(e -> {
                    idtraspaso = (int) botonModificar.getUserData();
                    muestraeditar();
                });
                AnchorPane.setRightAnchor(botonModificar, 80.0);
                AnchorPane.setBottomAnchor(botonModificar, 10.0);
                usuarioPanel.getChildren().add(botonModificar);

                Button botonEliminar = new Button();
                botonEliminar.setStyle("-fx-background-color: transparent;");
                ImageView iconoEliminar = new ImageView(new Image("/basura.png"));
                iconoEliminar.setFitWidth(20);
                iconoEliminar.setFitHeight(20);
                iconoEliminar.setPreserveRatio(true);
                botonEliminar.setGraphic(iconoEliminar);
                botonEliminar.setUserData(idUsuario);
                botonEliminar.setOnAction(e -> {
                    try {
                        try {
							eliminarUsuario((int) botonEliminar.getUserData());
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                AnchorPane.setRightAnchor(botonEliminar, 10.0);
                AnchorPane.setBottomAnchor(botonEliminar, 10.0);
                usuarioPanel.getChildren().add(botonEliminar);

                Listado.add(usuarioPanel, 0, row);
                GridPane.setMargin(usuarioPanel, new Insets(10, 0, 10, 0));
                row++;
                GridPane.setVgrow(usuarioPanel, javafx.scene.layout.Priority.ALWAYS);
            }
            resultadoUsuarios.close();
            sentenciaUsuarios.close();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Username.textProperty().bind(Login.bannerusuarioProperty());
        try {
            Muestra_usuarios();
            
            
            
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void nuevo_usuario() {
        try {
        	cerrar();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Nuevo_usuarios.fxml"));
            AnchorPane nuevousuarioPane = cargador.load();
            Stage nuevousarioStage = new Stage();
            nuevousarioStage.initStyle(StageStyle.TRANSPARENT);
            nuevousarioStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(nuevousuarioPane, 800, 623);
            nuevousarioStage.setScene(scene);
            nuevousarioStage.setTitle("NUEVO USUARIO");
            nuevousarioStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Gestion_usuarios() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
        Pane gestionusuariopane = cargador.load();
        Scene gestionusuarioScene = new Scene(gestionusuariopane, 600, 500);
        gestionusuarioScene.setFill(Color.TRANSPARENT);
        Stage gestiousuarioStage = new Stage();
        gestiousuarioStage.setResizable(false);
        gestiousuarioStage.initStyle(StageStyle.DECORATED);
        gestiousuarioStage.setScene(gestionusuarioScene);
        gestiousuarioStage.setTitle("PANEL DE GESTION DE USUARIOS");
        gestiousuarioStage.show();
        cerrar();
    }
    public void Gestionpedidos() throws IOException {
  	  FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/GestionPedidos.fxml"));
        Pane gestionpedidos = cargador.load();
        Scene gestionpedidosScene = new Scene(gestionpedidos, 600, 500);
       
        gestionpedidosScene.setFill(Color.TRANSPARENT);
        Stage gestionpedidosStage = new Stage();
        gestionpedidosStage.setResizable(false);
        gestionpedidosStage.initStyle(StageStyle.DECORATED);
        gestionpedidosStage.setScene(gestionpedidosScene);
        gestionpedidosStage.setTitle("GESTION DE PEDIDOS");
        gestionpedidosStage.show();
        cerrar();
  	
  	
  	
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

    private void eliminarUsuario(int idUsuario) throws IOException, SQLException {
        String sqlEliminarEmpleado = "DELETE FROM empleados WHERE id_empleado = ?";
        String sqlEliminarAdmin = "DELETE FROM administradores WHERE id_admin = ?";
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            try (PreparedStatement stmt = conexion.prepareStatement(sqlEliminarEmpleado)) {
                stmt.setInt(1, idUsuario);
                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas == 0) { // Si no se eliminó como empleado, intentar como administrador
                    try (PreparedStatement stmtAdmin = conexion.prepareStatement(sqlEliminarAdmin)) {
                        stmtAdmin.setInt(1, idUsuario);
                        stmtAdmin.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Llama a Muestra_usuarios para actualizar el contenido sin cerrar la ventana
        Muestra_usuarios();
    }



    private void muestraeditar() {
        try {
        	cerrar();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Editar_usuarios.fxml"));
            AnchorPane muestraeditarPane = cargador.load();
            Stage muestraeditarStage = new Stage();
            muestraeditarStage.initStyle(StageStyle.TRANSPARENT);
            muestraeditarStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(muestraeditarPane, 800, 623);
            muestraeditarStage.setScene(scene);
            muestraeditarStage.setTitle("EDITAR USUARIOS");
            muestraeditarStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cerrar();
    }
    
   
}
