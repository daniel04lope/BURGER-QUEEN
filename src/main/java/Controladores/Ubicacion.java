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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Ubicacion implements Initializable {

    @FXML
    Text Username; // Texto para mostrar el nombre de usuario
    @FXML
    private AnchorPane Panel_Desplegable; // Panel que se despliega
    @FXML
    private Button Desplegable; // Botón para desplegar el panel
    @FXML
    private Button Cerrar; // Botón para cerrar la ventana
    @FXML
    private ImageView imagenperfil; // Imagen de perfil del usuario

    @FXML
    private Accordion administradores; // Accordion para opciones de administración
    @FXML
    private TitledPane titledpaneadmin; // TitledPane para el panel de administradores
    @FXML
    private VBox Vboxadmin; // VBox que contiene botones de administración
    
    private boolean Panel_Visible = false; // Estado de visibilidad del panel desplegable
    private boolean Cerrardesplegar = false; // Estado del botón de cerrar
    @FXML
    private Button reservaadmin; // Botón para gestionar reservas
    @FXML
    private Button menuadmin; // Botón para gestionar el menú
    @FXML
    private Button usuariosadmin; // Botón para gestionar usuarios
    @FXML
    private Button pedidosadmin; // Botón para gestionar pedidos
    @FXML
    private Button botoncarrito; // Botón para acceder al carrito
    @FXML
    private WebView webView; // WebView para mostrar Google Maps

    // URL de Google Maps que se puede modificar o pasar dinámicamente
    private String googleMapsUrl = "https://www.google.com/maps/place/Burger+Queen/@38.530303,-8.8699675,17z/data=!3m2!4b1!5s0xd1942599711639d:0xad6eb38a6fd5d7d6!4m6!3m5!1s0xd194361a43e444b:0x866e61fc20791b9b!8m2!3d38.530303!4d-8.8673926!16s%2Fg%2F11rzdb4fgz?authuser=0&entry=ttu&g_ep=EgoyMDI0MTExOS4yIKXMDSoASAFQAw%3D%3D";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Username.textProperty().bind(Login.bannerusuarioProperty()); // Vincular el nombre de usuario desde Login

        // Aseguramos que el WebView se inicializa correctamente
        if (webView == null) {
            System.out.println("WebView no se ha inicializado correctamente.");
            return;
        }

        WebEngine webEngine = webView.getEngine(); // Obtener el motor del WebView

        // Habilitar JavaScript
        webEngine.setJavaScriptEnabled(true);

        // Cargar la URL de Google Maps
        webEngine.load(googleMapsUrl);

        // Verificar si la carga fue exitosa
        webEngine.setOnAlert(event -> {
            System.out.println("Mensaje de alerta desde el WebEngine: " + event.getData());
        });
        
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
            try {
                if (permisos(2, "lectura ") == 1) {
                    reservaadmin.setDisable(false); // Habilitar botón de reservas si tiene permiso
                } else {
                    reservaadmin.setDisable(true); // Deshabilitar botón de reservas si no tiene permiso
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Manejar excepciones
            }

            try {
                if (permisos(1, "lectura") == 1) {
                    menuadmin.setDisable(false); // Habilitar botón de menú si tiene permiso
                } else {
                    menuadmin.setDisable(true); // Deshabilitar botón de menú si no tiene permiso
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Manejar excepciones
            }

            try {
                if (permisos(3, "lectura") == 1) {
                    pedidosadmin.setDisable(false); // Habilitar botón de pedidos si tiene permiso
                } else {
                    pedidosadmin.setDisable(true); // Deshabilitar botón de pedidos si no tiene permiso
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Manejar excepciones
            }
        }

        if (Login.tipo.equals("usuarios")) {
            administradores.setVisible(false); // Ocultar opciones de administración
            titledpaneadmin.setVisible(false);
            Vboxadmin.setVisible(false);
        }

        try {
            System.out.println(permisos(1, "lectura")); // Imprimir permisos de lectura
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar excepciones
        }
        
        // Cargar la imagen de perfil desde la ruta especificada
        String rutaImagen = "file:src/main/resources/imagenes/" + Login.datos_login.getRuta();
        Image imagen = new Image(rutaImagen);

        // Configurar un listener para cargar la imagen cuando cambie la ruta en Login
        Login.imagenProperty().addListener((observable, oldValue, newValue) -> {
            cargarImagen(newValue); // Cargar nueva imagen si cambia la ruta
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

    private void cargarImagen(String nuevaRuta) {
        String rutaImagen = "file:src/main/resources/imagenes/" + nuevaRuta;
        Image imagen = new Image(rutaImagen);
        if (imagen.isError()) {
            System.err.println("Error al cargar la imagen desde la ruta: " + rutaImagen);
        } else {
            imagenperfil.setImage(imagen); // Asignar la nueva imagen al ImageView
        }
    }
    
    public void perfil() throws IOException {
        if (!(Login.tipo.equals("usuarios"))) {
            cerrar(); // Cerrar la ventana actual
            Mostrar_Login(); // Mostrar la ventana de login
        } else {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/perfil.fxml"));
            Pane perfilpane = cargador.load();
            Scene perfilScene = new Scene(perfilpane, 600, 500);
            perfilScene.setFill(Color.TRANSPARENT);
            Stage perfilStage = new Stage();
            perfilStage.setResizable(false);
            perfilStage.initStyle(StageStyle.DECORATED);
            perfilStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
            perfilStage.setScene(perfilScene);
            perfilStage.setTitle("PERFIL");
            perfilStage.show();
            cerrar(); // Cerrar la ventana actual
        }
    }

    // Método para actualizar la URL del mapa dinámicamente
    public void cargarMapa(String nuevaUrl) {
        WebEngine webEngine = webView.getEngine();
        if (webEngine != null && nuevaUrl != null && !nuevaUrl.isEmpty()) {
            // Cambiar la URL del mapa
            webEngine.load(nuevaUrl);
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

        return valor; // Retornar el valor del permiso
    }

    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close(); // Cerrar la ventana
    }

    public void Pantalla_Principal() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principal = cargador.load();
        Scene principalScene = new Scene(principal, 600, 500);

        principalScene.setFill(Color.TRANSPARENT);
        
        Stage PrincipalStage = new Stage();
        PrincipalStage.setResizable(false);
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("PANTALLA PRINCIPAL");
        PrincipalStage.show();
        cerrar(); // Cerrar la ventana actual
    }

    public void Despliega() {
        System.out.println("Funciona");
        Cerrardesplegar = !Cerrardesplegar; // Alternar estado de despliegue
        Panel_Visible = !Panel_Visible; // Alternar visibilidad del panel
        Cerrar.setVisible(Cerrardesplegar); // Mostrar u ocultar botón de cerrar
        Panel_Desplegable.setVisible(Panel_Visible); // Mostrar u ocultar panel desplegable
    }

    public void Carta() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
        Pane cartapane = cargador.load();
        Scene cartaScene = new Scene(cartapane, 600, 500);
        cartaScene.setFill(Color.TRANSPARENT);

        Stage cartaStage = new Stage();
        cartaStage.setResizable(false);
        cartaStage.initStyle(StageStyle.DECORATED);
        cartaStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        cartaStage.setScene(cartaScene);
        cartaStage.setTitle("CARTA");
        cartaStage.show();
        cerrar(); // Cerrar la ventana actual
    }

    public void ReservaAdmin() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/ReservaAdmin.fxml"));
        Pane reservapane = cargador.load();
        Scene reservaScene = new Scene(reservapane, 600, 500);
        reservaScene.setFill(Color.TRANSPARENT);
        Stage reservaStage = new Stage();
        reservaStage.setResizable(false);
        reservaStage.initStyle(StageStyle.DECORATED);
        reservaStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        reservaStage.setScene(reservaScene);
        reservaStage.setTitle("PANEL DE GESTION DE RESERVAS");
        reservaStage.show();
        cerrar(); // Cerrar la ventana actual
    }

    public void Reserva() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Reservas.fxml"));
        Pane reservapane = cargador.load();
        Scene reservaScene = new Scene(reservapane, 600, 500);
        reservaScene.setFill(Color.TRANSPARENT);
        Stage reservaStage = new Stage();
        reservaStage.setResizable(false);
        reservaStage.initStyle(StageStyle.DECORATED);
        reservaStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        reservaStage.setScene(reservaScene);
        reservaStage.setTitle("RESERVA");
        reservaStage.show();
        cerrar(); // Cerrar la ventana actual
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
            cerrar(); // Cerrar la ventana actual
        } catch (Exception e) {
 
            e.printStackTrace(); // Manejar excepciones
        }
    }

    public void Horarios() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Horarios.fxml"));
        Pane horariospane = cargador.load();
        Scene horariosScene = new Scene(horariospane, 600, 500);
        horariosScene.setFill(Color.TRANSPARENT);
        Stage horariosStage = new Stage();
        horariosStage.setResizable(false);
        horariosStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        horariosStage.initStyle(StageStyle.DECORATED);
        horariosStage.setScene(horariosScene);
        horariosStage.setTitle("HORARIOS");
        horariosStage.show();
        cerrar(); // Cerrar la ventana actual
    }

    public void carrito() throws IOException {
        try {
            cerrar(); // Cerrar la ventana actual
            Carrito.ventanaanterior = 4; // Establecer la ventana anterior
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carrito.fxml"));
            AnchorPane carritoPane = cargador.load();

            Stage carritoStage = new Stage();
            carritoStage.initStyle(StageStyle.TRANSPARENT);
            carritoStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(carritoPane, 800, 623);
            carritoStage.setScene(scene);

            carritoStage.setTitle("CARRITO");
            carritoStage.show(); // Mostrar la ventana del carrito
        } catch (IOException e) {
            e.printStackTrace(); // Manejar excepciones
        }
    }

    public void Gestionpedidos() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/GestionPedidos.fxml"));
        Pane gestiondepedidospane = cargador.load();
        Scene gestiondepedidosScene = new Scene(gestiondepedidospane, 600, 500);
       
        gestiondepedidosScene.setFill(Color.TRANSPARENT);
        Stage gestiondepedidosStage = new Stage();
        
        gestiondepedidosStage.initStyle(StageStyle.DECORATED);
        gestiondepedidosStage.setResizable(false);
        gestiondepedidosStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        gestiondepedidosStage.setScene(gestiondepedidosScene);
        gestiondepedidosStage.setTitle("PANEL DE GESTION DE PEDIDOS");
        gestiondepedidosStage.show();
        cerrar(); // Cerrar la ventana actual
    }

    public void Gestion_usuarios() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
        Pane gestiondeusuariopane = cargador.load();
        Scene gestiondeusuarioScene = new Scene(gestiondeusuariopane, 600, 500);
        gestiondeusuarioScene.setFill(Color.TRANSPARENT);
        Stage gestiondeusuariosStage = new Stage();
        gestiondeusuariosStage.setResizable(false);
        gestiondeusuariosStage.initStyle(StageStyle.DECORATED);
        gestiondeusuariosStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        gestiondeusuariosStage.setScene(gestiondeusuarioScene);
        gestiondeusuariosStage.setTitle("PANEL DE GESTION DE USUARIOS");
        gestiondeusuariosStage.show();
        cerrar(); // Cerrar la ventana actual
    }
}