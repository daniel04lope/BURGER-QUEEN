package Controladores;

import javafx.scene.control.TextArea;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.Notifications;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class Reserva implements Initializable {
    @FXML
    private Text Username; // Muestra el nombre de usuario
    @FXML
    private AnchorPane drawer; // Panel desplegable
    @FXML
    private Button Desplegable; // Botón para desplegar el panel
    @FXML
    private Button Cerrar; // Botón para cerrar la ventana
    @FXML
    private TextField Nombre; // Campo para el nombre del cliente
    @FXML
    private DatePicker Fecha_Reserva; // Selector de fecha para la reserva
    @FXML
    private TextField Hora; // Campo para la hora de la reserva
    @FXML
    private ImageView imagenperfil; // Imagen de perfil del usuario
    @FXML
    private TextField Personas; // Campo para el número de personas
    @FXML
    private TextArea Datos_Adicionales; // Campo para notas adicionales sobre la reserva
    @FXML
    private Button reservaadmin; // Botón para gestionar reservas
    @FXML
    private Button menuadmin; // Botón para gestionar el menú
    @FXML
    private Button usuariosadmin; // Botón para gestionar usuarios
    @FXML
    private Button pedidosadmin; // Botón para gestionar pedidos
    @FXML
    private Accordion administradores; // Accordion para opciones de administración
    @FXML
    private TitledPane titledpaneadmin; // TitledPane para el panel de administradores
    @FXML
    private VBox Vboxadmin; // VBox que contiene botones de administración
    @FXML
    private Button botoncarrito; // Botón para acceder al carrito
    
    @FXML
    private Button hacerreserva; // Botón para hacer una reserva
    private boolean drawerVisible = false; // Estado de visibilidad del panel
    private boolean Cerrardesplegar = false; // Estado del botón de cerrar

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Vincular el nombre de usuario al texto del banner
        Username.textProperty().bind(Login.bannerusuarioProperty());
        
        // Configuración según el tipo de usuario
        if (Login.tipo.equals("administradores")) {
            administradores.setVisible(true); // Mostrar opciones de administración
            titledpaneadmin.setVisible(true);
            Vboxadmin.setVisible(true);
            usuariosadmin.setDisable(false); // Habilitar gestión de usuarios
            botoncarrito.setVisible(false); // Ocultar botón de carrito
            pedidosadmin.setDisable(false); // Habilitar gestión de pedidos
            menuadmin.setDisable(false); // Habilitar gestión de menú
            reservaadmin.setDisable(false); // Habilitar gestión de reservas
        }

        if (Login.tipo.equals("empleados")) {
            administradores.setVisible(true); // Mostrar opciones de administración
            titledpaneadmin.setVisible(true);
            Vboxadmin.setVisible(true);
            botoncarrito.setVisible(false); // Ocultar botón de carrito
            hacerreserva.setVisible(false); // Ocultar botón de hacer reserva
            System.out.println("llegue");

            // Verificar permisos para cada botón
            try {
                if (permisos(2, "lectura") == 1) {
                    reservaadmin.setDisable(false); // Habilitar si tiene permiso
                } else {
                    reservaadmin.setDisable(true); // Deshabilitar si no tiene permiso
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Manejar excepciones
            }

            try {
                if (permisos(1, "lectura") == 1) {
                    menuadmin.setDisable(false); // Habilitar si tiene permiso
                } else {
                    menuadmin.setDisable(true); // Deshabilitar si no tiene permiso
                } }catch (SQLException e) {
                e.printStackTrace(); // Manejar excepciones
            }
                

            try {
                if (permisos(3, "lectura") == 1) {
                    pedidosadmin.setDisable(false); // Habilitar si tiene permiso
                } else {
                    pedidosadmin.setDisable(true); // Deshabilitar si no tiene permiso
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
            System.out.println(permisos(1, "lectura")); // Imprimir permisos
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar excepciones
        }

        // Cargar la imagen de perfil desde la ruta especificada
        String rutaImagen = "file:src/main/resources/imagenes/" + Login.datos_login.getRuta();
        Image imagen = new Image(rutaImagen);

        // Configurar un listener para cargar la imagen cuando cambie la ruta en Login
        Login.imagenProperty().addListener((observable, oldValue, newValue) -> {
            cargarImagen(newValue); // Cargar nueva imagen
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
            System.err.println("Error al cargar la imagen desde la ruta: " + rutaImagen); // Manejar error de carga
        } else {
            imagenperfil.setImage(imagen); // Asignar imagen cargada
        }
    }

    public void perfil() throws IOException {
        if (!(Login.tipo.equals("usuarios"))) {
            cerrar(); // Cerrar ventana actual
            Mostrar_Login(); // Mostrar ventana de login
        } else {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/perfil.fxml"));
            Pane perfilpane = cargador.load(); // Cargar vista de perfil
            Scene perfilScene = new Scene(perfilpane, 600, 500);
            perfilScene.setFill(Color.TRANSPARENT); // Hacer fondo transparente
            Stage perfilStage = new Stage();
            perfilStage.setResizable(false);
            perfilStage.initStyle(StageStyle.DECORATED);
            perfilStage.setScene(perfilScene);
            perfilStage.setTitle("PERFIL");
            perfilStage.show(); // Mostrar ventana de perfil
            cerrar(); // Cerrar ventana actual
        }
    }

    public void Despliega() {
        Cerrardesplegar = !Cerrardesplegar; // Alternar estado de cerrar
        drawerVisible = !drawerVisible; // Alternar visibilidad del panel
        Cerrar.setVisible(Cerrardesplegar); // Mostrar/ocultar botón de cerrar
        drawer.setVisible(drawerVisible); // Mostrar/ocultar panel
    }

    private void cargarImagen(String nuevaRuta) {
        String rutaImagen = "file:src/main/resources/imagenes/" + nuevaRuta; // Nueva ruta de imagen
        Image imagen = new Image(rutaImagen);
        if (imagen.isError()) {
            System.err.println("Error al cargar la imagen desde la ruta: " + rutaImagen); // Manejar error de carga
        } else {
            imagenperfil.setImage(imagen); // Asignar nueva imagen
        }
    }

    public void Gestion_usuarios() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
        Pane gestiondeusuariospane = cargador.load(); // Cargar vista de gestión de usuarios
        Scene gestiondeusuariosScene = new Scene(gestiondeusuariospane, 600, 500);
        gestiondeusuariosScene.setFill(Color.TRANSPARENT); // Hacer fondo transparente
        Stage gestiondeusuariosStage = new Stage();
        gestiondeusuariosStage.setResizable(false);
        gestiondeusuariosStage .initStyle(StageStyle.DECORATED);
        gestiondeusuariosStage.setScene(gestiondeusuariosScene);
        gestiondeusuariosStage.setTitle("PANEL DE GESTION DE USUARIOS");
        gestiondeusuariosStage.show(); // Mostrar ventana de gestión de usuarios
        cerrar(); // Cerrar ventana actual
    }

    public void Ubicacion() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Ubicacion.fxml"));
        Pane ubicacionpane = cargador.load(); // Cargar vista de ubicación
        Scene ubiacionScene = new Scene(ubicacionpane, 600, 500);
        ubiacionScene.setFill(Color.TRANSPARENT); // Hacer fondo transparente

        Stage ubicacionStage = new Stage();
        ubicacionStage.setResizable(false);
        ubicacionStage.initStyle(StageStyle.DECORATED);
        ubicacionStage.setScene(ubiacionScene);
        ubicacionStage.setTitle("UBICACION");
        ubicacionStage.show(); // Mostrar ventana de ubicación
        cerrar(); // Cerrar ventana actual
    }

    public void ReservaAdmin() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/ReservaAdmin.fxml"));
        Pane reservapane = cargador.load(); // Cargar vista de gestión de reservas
        Scene reservaScene = new Scene(reservapane, 600, 500);
        reservaScene.setFill(Color.TRANSPARENT); // Hacer fondo transparente
        Stage reservaadminStage = new Stage();
        reservaadminStage.setResizable(false);
        reservaadminStage.initStyle(StageStyle.DECORATED);
        reservaadminStage.setScene(reservaScene);
        reservaadminStage.setTitle("PANEL DE GESTION DE RESERVAS");
        reservaadminStage.show(); // Mostrar ventana de gestión de reservas
        cerrar(); // Cerrar ventana actual
    }

    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow(); // Obtener la ventana actual
        stage.close(); // Cerrar la ventana
    }

    public void Pantalla_Principal() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principal = cargador.load(); // Cargar vista de pantalla principal
        Scene principalScene = new Scene(principal, 600, 500);
       
        principalScene.setFill(Color.TRANSPARENT); // Hacer fondo transparente
        Stage PrincipalStage = new Stage();
        PrincipalStage.setResizable(false);
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("PANTALLA PRINCIPAL");
        PrincipalStage.show(); // Mostrar ventana de pantalla principal
        cerrar(); // Cerrar ventana actual
    }

    public void Horarios() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Horarios.fxml"));
        Pane horariospane = cargador.load(); // Cargar vista de horarios
        Scene horariosScene = new Scene(horariospane, 600, 500);
        horariosScene.setFill(Color.TRANSPARENT); // Hacer fondo transparente
        Stage horariosStage = new Stage();
        horariosStage.setResizable(false);
        horariosStage.initStyle(StageStyle.DECORATED);
        horariosStage.setScene(horariosScene);
        horariosStage.setTitle("HORARIOS");
        horariosStage.show(); // Mostrar ventana de horarios
        cerrar(); // Cerrar ventana actual
    }

    public void Carta() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
        Pane cartapane = cargador.load(); // Cargar vista de carta
        Scene cartaScene = new Scene(cartapane, 600, 500);
        cartaScene.setFill(Color.TRANSPARENT); // Hacer fondo transparente

        Stage cartaStage = new Stage();
        cartaStage.setResizable(false);
        cartaStage.initStyle(StageStyle.DECORATED);
        cartaStage.setScene(cartaScene);
        cartaStage.setTitle("CARTA");
        cartaStage.show(); // Mostrar ventana de carta
        cerrar(); // Cerrar ventana actual
    }

    public void insertReserva() {
        if (Login.datos_login.getIdUsuario() == 0) {
            Mostrar_Login(); // Mostrar login si no hay usuario
            return; // Salir del método
        }

        if (Fecha_Reserva.getValue() == null) {
            MostrarNotificaciones("Fecha de reserva es requerida", AlertType.ERROR); // Notificar error
            return; // Salir del método
        }

        if (!Hora.getText().matches("\\d{2}:\\d{2}")) {
            MostrarNotificaciones("Hora debe estar en formato HH:mm ", AlertType.ERROR); // Notificar error de formato de hora
            return; // Salir del método
        }

        if (!Personas.getText().matches("\\d+")) {
            MostrarNotificaciones("Número de personas debe ser un número", AlertType.ERROR); // Notificar error de número de personas
            return; // Salir del método
        }

        String nombreCliente = Login.datos_login.getNombre(); // Obtener nombre del cliente
        LocalDate fechaReserva = Fecha_Reserva.getValue(); // Obtener fecha de reserva
        LocalTime horaReserva = LocalTime.parse(Hora.getText()); // Obtener hora de reserva
        int numeroPersonas = Integer.parseInt(Personas.getText()); // Obtener número de personas
        String datosAdicionales = Datos_Adicionales.getText(); // Obtener datos adicionales

        String insertQuery = "INSERT INTO reservas (nombre_cliente, fecha_reserva, hora_reserva, numero_personas, notas, estado,telefono) " +
                             "VALUES (?, ?, ?, ?, ?, 'pendiente',?)"; // Consulta SQL para insertar reserva

        try (Connection conn = util.Conexiones.dameConexion("burger-queen"); // Conexión a la base de datos
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, nombreCliente); // Asignar nombre del cliente
            stmt.setDate(2, java.sql.Date.valueOf(fechaReserva)); // Asignar fecha de reserva
            stmt.setTime(3, java.sql.Time.valueOf(horaReserva)); // Asignar hora de reserva
            stmt.setInt(4, numeroPersonas); // Asignar número de personas
            stmt.setString(5, datosAdicionales); // Asignar datos adicionales
            stmt.setString(6, Login.datos_login.getTelefono());

            int rowsAffected = stmt.executeUpdate(); // Ejecutar la consulta
            if (rowsAffected > 0) {
                Platform.runLater(() -> MostrarNotificaciones("La reserva se ha registrado exitosamente.", AlertType.INFORMATION)); // Notificar éxito
            } else {
                throw new SQLException("No se pudo guardar la reserva."); // Lanzar excepción si no se guardó
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Manejar excepciones
            MostrarNotificaciones("Ocurrió un error al intentar guardar la reserva.", AlertType.ERROR); // Notificar error
        }
    }

    private void MostrarNotificaciones(String message, AlertType alertType) {
        Notifications notification = Notifications.create().title(alertType == AlertType.ERROR ? "Error en el formulario" : "Reserva guardada").text(message).hideAfter(javafx.util.Duration.seconds(5)); // Crear notificación
        if (alertType == AlertType.ERROR) {
            notification.showError(); // Mostrar notificación de error
        } else {
            notification.showInformation(); // Mostrar notificación de éxito
            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Reserva guardada");
            successAlert.setHeaderText(null);
            successAlert.setContentText("La reserva se ha registrado exitosamente."); // Mensaje de éxito
            successAlert.initModality(Modality.APPLICATION_MODAL);
            successAlert.showAndWait(); // Mostrar alerta de éxito
        }
    }

    public int permisos(int nombreModulo, String tipoPermiso) throws SQLException {
        String sql = "SELECT " + tipoPermiso + " FROM permisos WHERE id_empleado = ? AND id_modulo = ?"; // Consulta SQL para permisos
        int valor = 0;

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) { // Conexión a la base de datos
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, Login.datos_login.getIdUsuario()); // Asignar ID de usuario
            sentencia.setInt(2, nombreModulo); // Asignar ID de módulo
            
            System.out.println("Cadena: " + sentencia); // Imprimir consulta
            
            ResultSet ejecuta = sentencia.executeQuery(); // Ejecutar consulta

            if (ejecuta.next()) {
                valor = ejecuta.getInt(tipoPermiso); // Obtener valor de permiso
                System.out.println("Valor: " + valor); // Imprimir valor
            } else {
                System.out.println("No valor encontrado id_empleado = " + Login.datos_login.getIdUsuario() + " and id_modulo = " + nombreModulo); // Mensaje si no se encuentra valor
            }
        }

        return valor; // Retornar valor de permiso
    }

    public void Mostrar_Login() {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml")); // Cargar vista de login
            Pane login = cargador.load();
            Scene loginScene = new Scene(login, 450, 600); // Crear escena de login
            loginScene.setFill(Color.TRANSPARENT); // Hacer fondo transparente
            Stage loginStage = new Stage();
            loginStage.setResizable(false);
            loginStage.initStyle(StageStyle.TRANSPARENT); // Estilo de ventana transparente
            loginStage.setScene(loginScene);
            loginStage.setTitle("LOGIN");
            loginStage.show(); // Mostrar ventana de login
            cerrar(); // Cerrar ventana actual
        } catch (Exception e) {
            e.printStackTrace(); // Manejar excepciones
        }
    }

    public void carrito() throws IOException {
        try {
            cerrar(); // Cerrar ventana actual
            Carrito.ventanaanterior = 5; // Establecer ventana anterior
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carrito.fxml")); // Cargar vista de carrito
            AnchorPane carritoPane = cargador.load();
            Stage carritoStage = new Stage();
            carritoStage.initStyle(StageStyle.TRANSPARENT); // Estilo de ventana transparente
            carritoStage.initModality(Modality.APPLICATION_MODAL); // Modalidad de ventana
            Scene scene = new Scene(carritoPane, 800, 623); // Crear escena de carrito
            carritoStage.setScene(scene);
            carritoStage.setTitle("CARRITO");
            carritoStage.show(); // Mostrar ventana de carrito
        } catch (IOException e) {
            e.printStackTrace(); // Manejar excepciones
        }
    }

    public void Gestionpedidos() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/GestionPedidos.fxml")); // Cargar vista de gestión de pedidos
        Pane principal = loader.load();
        Scene principalScene = new Scene(principal, 600, 500);
       
        principalScene.setFill(Color.TRANSPARENT); // Hacer fondo transparente
        Stage PrincipalStage = new Stage();
        PrincipalStage.setResizable(false);
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("PANEL DE GESTION DE PEDIDOS");
        PrincipalStage.show(); // Mostrar ventana de gestión de pedidos
        cerrar(); // Cerrar ventana actual
    }
}