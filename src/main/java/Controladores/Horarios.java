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

public class Horarios implements Initializable {

    @FXML
    Text Username; // Texto que muestra el nombre de usuario
    @FXML
    private AnchorPane Panel_Desplegable; // Panel desplegable para opciones adicionales
    @FXML
    private Button Desplegable; // Botón para desplegar el panel
    @FXML
    private Button Cerrar; // Botón para cerrar la ventana
    @FXML
    private ImageView imagenperfil; // Imagen de perfil del usuario

    private boolean Panel_Visible = false; // Estado de visibilidad del panel desplegable
    private boolean Cerrardesplegar = false; // Estado de visibilidad del botón de cerrar

    @FXML
    private Button botoncarrito; // Botón para acceder al carrito
    @FXML
    private Button usuariosadmin; // Botón para gestionar usuarios
    @FXML
    private Button pedidosadmin; // Botón para gestionar pedidos
    @FXML
    private Accordion administradores; // Accordion que contiene opciones para administradores
    @FXML
    private TitledPane titledpaneadmin; // Panel titulado para administradores
    @FXML
    private VBox Vboxadmin; // Contenedor vertical para opciones de administradores
    @FXML
    private Button reservaadmin; // Botón para gestionar reservas
    @FXML
    private Button menuadmin; // Botón para gestionar el menú

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Método que se ejecuta al inicializar la clase

        Username.textProperty().bind(Login.bannerusuarioProperty()); // Vincular el nombre de usuario al banner

        // Configuración de visibilidad y permisos según el tipo de usuario
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
            verificarPermisos();
        }

        if (Login.tipo.equals("usuarios")) {
            administradores.setVisible(false);
            titledpaneadmin.setVisible(false);
            Vboxadmin.setVisible(false);
        }
        
        	 
        

        // Cargar la imagen de perfil desde la ruta especificada
        cargarImagenPerfil();
    }

    private void verificarPermisos() {
        // Método para verificar los permisos del usuario
        try {
            if (permisos(2, "lectura") == 1) {
                reservaadmin.setDisable(false); // Habilitar botón de reservas si tiene permiso
            } else {
                reservaadmin.setDisable(true); // Deshabilitar botón de reservas si no tiene permiso
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprimir error si hay problemas con la base de datos
        }

        try {
            if (permisos(1, "lectura") == 1) {
                menuadmin.setDisable(false); // Habilitar botón de menú si tiene permiso
            } else {
                menuadmin.setDisable(true); // Deshabilitar botón de menú si no tiene permiso
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprimir error si hay problemas con la base de datos
        }

        try {
            if (permisos(3, "lectura") == 1) {
                pedidosadmin.setDisable(false); // Habilitar botón de pedidos si tiene permiso
            } else {
                pedidosadmin.setDisable(true); // Deshabilitar botón de pedidos si no tiene permiso
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprimir error si hay problemas con la base de datos
        }
    }


    private void cargarImagenPerfil() {
        // Método para cargar la imagen de perfil desde la ruta especificada
        String rutaImagen = "file:src/main/resources/imagenes/" + Login.datos_login.getRuta();
        Image imagen = new Image(rutaImagen);

        // Configurar un listener para cargar la imagen cuando cambie la ruta en Login
        Login.imagenProperty().addListener((observable, oldValue, newValue) -> {
            cargarImagen(newValue); // Cargar la nueva imagen
        });

        // Si hay una ruta de imagen válida, asignarla a la propiedad de la imagen
        if (Login.datos_login.getRuta() != null) {
            Login.imagen.set(Login.datos_login.getRuta());
        }

        // Configurar un rectángulo con esquinas redondeadas para la imagen de perfil
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(
            imagenperfil.getFitWidth() - 5,  // Ancho del rectángulo
            imagenperfil.getFitHeight() - 5  // Alto del rectángulo
        );
        clip.setArcWidth(30);  // Radio de las esquinas horizontales
        clip.setArcHeight(30); // Radio de las esquinas verticales

        // Establecer el clip para la imagen de perfil
        imagenperfil.setClip(clip);

        if (imagen.isError()) {
            System.err.println("Error al cargar la imagen desde la ruta: " + rutaImagen);
        } else {
            imagenperfil.setImage(imagen); // Asignar la imagen cargada al ImageView
        }
    }

    public int permisos(int nombreModulo, String tipoPermiso) throws SQLException {
        // Método para verificar los permisos del usuario en un módulo específico
        String sql = "SELECT " + tipoPermiso + " FROM permisos WHERE id_empleado = ? AND id_modulo = ?";
        int valor = 0;

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, Login.datos_login.getIdUsuario());
            sentencia.setInt(2, nombreModulo);

            ResultSet ejecuta = sentencia.executeQuery();

            if (ejecuta.next()) {
                valor = ejecuta.getInt(tipoPermiso); // Obtener el valor del permiso
            } else {
                System.out.println("No valor encontrado id_empleado = " + Login.datos_login.getIdUsuario() + " and id_modulo = " + nombreModulo);
            }
        }

        return valor; // Retornar el valor del permiso
    }

    public void perfil() throws IOException {
        // Método para abrir la ventana del perfil
        if (!(Login.tipo.equals("usuarios"))) {
            cerrar(); // Cerrar ventana actual si no es usuario
            Mostrar_Login(); // Mostrar ventana de login
        } else {
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
            cerrar(); // Cerrar ventana actual
        }
    }

    public void cerrar() {
        // Método para cerrar la ventana actual
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    private void cargarImagen(String nuevaRuta) {
        // Método para cargar la imagen de perfil desde una nueva ruta
        String rutaImagen = "file:src/main/resources/imagenes/" + nuevaRuta;
        Image imagen = new Image(rutaImagen);
        if (imagen.isError()) {
            System.err.println("Error al cargar la imagen desde la ruta: " + rutaImagen);
        } else {
            imagenperfil.setImage(imagen); // Asignar la nueva imagen al ImageView
        }
    }

    public void Pantalla_Principal() throws IOException {
        // Método para abrir la ventana de la pantalla principal
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
 cerrar(); // Cerrar ventana actual
    }

    public void Despliega() {
        // Método para desplegar u ocultar el panel de opciones
        Cerrardesplegar = !Cerrardesplegar;
        Panel_Visible = !Panel_Visible;
        Cerrar.setVisible(Cerrardesplegar);
        Panel_Desplegable.setVisible(Panel_Visible);
    }

    public void Carta() throws IOException {
        // Método para abrir la ventana de la carta
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
        Pane cartapane = cargador.load();
        Scene cartaScene = new Scene(cartapane, 600, 500);
        cartaScene.setFill(Color.TRANSPARENT);

        Stage cartaStage = new Stage();
        cartaStage.setResizable(false);
        cartaStage.initStyle(StageStyle.DECORATED);
        cartaStage.setScene(cartaScene);
        cartaStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        cartaStage.setTitle("CARTA");
        cartaStage.show();
        cerrar(); // Cerrar ventana actual
    }

    public void Ubicacion() throws IOException {
        // Método para abrir la ventana de ubicación
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Ubicacion.fxml"));
        Pane ubicacionpane = cargador.load();
        Scene ubicacionScene = new Scene(ubicacionpane, 600, 500);
        ubicacionScene.setFill(Color.TRANSPARENT);

        Stage ubicacionStage = new Stage();
        ubicacionStage.setResizable(false);
        ubicacionStage.initStyle(StageStyle.DECORATED);
        ubicacionStage.setScene(ubicacionScene);
        ubicacionStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        ubicacionStage.setTitle("UBICACION");
        ubicacionStage.show();
        cerrar(); // Cerrar ventana actual
    }

    public void ReservaAdmin() throws IOException {
        // Método para abrir la ventana de gestión de reservas para administradores
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/ReservaAdmin.fxml"));
        Pane reservaadmin = cargador.load();
        Scene reservaadminScene = new Scene(reservaadmin, 600, 500);
        reservaadminScene.setFill(Color.TRANSPARENT);
        Stage reservaadminStage = new Stage();
        reservaadminStage.setResizable(false);
        reservaadminStage.initStyle(StageStyle.DECORATED);
        reservaadminStage.setScene(reservaadminScene);
        reservaadminStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        reservaadminStage.setTitle("PANEL DE GESTION DE RESERVAS");
        reservaadminStage.show();
        cerrar(); // Cerrar ventana actual
    }

    public void Reserva() throws IOException {
        // Método para abrir la ventana de reservas
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
        cerrar(); // Cerrar ventana actual
    }

    public void Mostrar_Login() {
        // Método para mostrar la ventana de login
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
            cerrar(); // Cerrar ventana actual
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir error si no se puede cargar la vista
        }
    }

    public void carrito() throws IOException {
        // Método para abrir la ventana del carrito
        try {
            cerrar(); // Cerrar ventana actual
            Carrito.ventanaanterior = 3; // Establecer la ventana anterior
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carrito.fxml"));
            AnchorPane carritoPane = cargador.load();

            Stage carritoStage = new Stage();
            carritoStage.initStyle(StageStyle.TRANSPARENT);
            carritoStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(carritoPane, 800, 623);
            carritoStage.setScene(scene);
            carritoStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
            carritoStage.setTitle("CARRITO");
            carritoStage.show(); // Mostrar la ventana del carrito
        } catch (IOException e) {
            e.printStackTrace();} // Imprimir error si no se puede cargar la vista 
    }

    public void Gestionpedidos() throws IOException {
        // Método para abrir la ventana de gestión de pedidos
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/GestionPedidos.fxml"));
        Pane gestiondepedidospanel = cargador.load();
        Scene gestiondepedidosScene = new Scene(gestiondepedidospanel, 600, 500);
        
        gestiondepedidosScene.setFill(Color.TRANSPARENT);
        Stage gestiondepedidosStage = new Stage();
        gestiondepedidosStage.setResizable(false);
        gestiondepedidosStage.initStyle(StageStyle.DECORATED);
        gestiondepedidosStage.setScene(gestiondepedidosScene);
        gestiondepedidosStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        gestiondepedidosStage.setTitle("PANEL DE GESTION DE PEDIDOS");
        gestiondepedidosStage.show();
        cerrar(); // Cerrar ventana actual
    }

    public void Gestion_usuarios() throws IOException {
        // Método para abrir la ventana de gestión de usuarios
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
        Pane gestiondeusuariopanel = cargador.load();
        Scene gestiondeusuarioScene = new Scene(gestiondeusuariopanel, 600, 500);
        gestiondeusuarioScene.setFill(Color.TRANSPARENT);
        Stage gestiondeusuariosStage = new Stage();
        gestiondeusuariosStage.setResizable(false);
        gestiondeusuariosStage.initStyle(StageStyle.DECORATED);
        gestiondeusuariosStage.setScene(gestiondeusuarioScene);
        gestiondeusuariosStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        gestiondeusuariosStage.setTitle("PANEL DE GESTION DE USUARIOS");
        gestiondeusuariosStage.show();
        cerrar(); // Cerrar ventana actual
    }
}