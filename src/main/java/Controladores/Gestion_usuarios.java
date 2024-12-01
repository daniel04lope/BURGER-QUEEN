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
    Text Username; // Texto que muestra el nombre de usuario
    @FXML
    private AnchorPane Panel_Desplegable; // Panel desplegable para opciones adicionales
    @FXML
    private Button Desplegable; // Botón para desplegar el panel
    @FXML
    private Button Cerrar; // Botón para cerrar la ventana
    @FXML
    GridPane Listado; // GridPane para mostrar la lista de usuarios
    @FXML
    private ImageView imagenperfil; // Imagen de perfil del usuario
    static int idtraspaso; // ID del usuario a editar

    private boolean Panel_Visible = false; // Estado de visibilidad del panel desplegable
    private boolean Cerrardesplegar = false; // Estado de visibilidad del botón de cerrar

    @FXML
    private Button pedidosadmin; // Botón para gestionar pedidos

    public void carrito() throws IOException {
        // Método para abrir la ventana del carrito
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carrito.fxml"));
            AnchorPane carritoPane = cargador.load();
            Stage carritoStage = new Stage();
            carritoStage.initStyle(StageStyle.TRANSPARENT);
            carritoStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(carritoPane, 800, 623);
            carritoStage.setScene(scene);
            carritoStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
            carritoStage.setTitle("CARRITO");
            carritoStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Imprimir error si no se puede cargar la vista
        }
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

    public void Horarios() throws IOException {
        // Método para abrir la ventana de horarios
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Horarios.fxml"));
        Pane horariospane = cargador.load();
        Scene horariosScene = new Scene(horariospane, 600, 500);
        horariosScene.setFill(Color.TRANSPARENT);
        Stage horariosStage = new Stage();
        horariosStage.setResizable(false);
        horariosStage.initStyle(StageStyle.DECORATED);
        horariosStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        horariosStage.setScene(horariosScene);
        horariosStage.setTitle("HORARIOS");
        horariosStage.show();
        cerrar(); // Cerrar ventana actual
    }

    public void Ubicacion() throws IOException {
        // Método para abrir la ventana de ubicación
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Ubicacion.fxml"));
        Pane ubicacion = cargador.load();
        Scene ubicacionScene = new Scene(ubicacion, 600, 500);
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

    public void Reserva() throws IOException {
        // Método para abrir la ventana de reservas
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Reservas.fxml"));
        Pane reserva = cargador.load();
        Scene reservaScene = new Scene(reserva, 600, 500);
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

    public void ReservaAdmin() throws IOException {
        // Método para abrir la ventana de reservas para administradores
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/ReservaAdmin.fxml"));
        Pane reservaadmin = cargador.load();
        Scene reservaadminScene = new Scene(reservaadmin, 600, 500);
        reservaadminScene.setFill(Color.TRANSPARENT);
        Stage reservaadminStage = new Stage();
        reservaadminStage.setResizable(false);
        reservaadminStage.initStyle(StageStyle.DECORATED);
        reservaadminStage.setScene(reservaadminScene);
        reservaadminStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        reservaadminStage.setTitle("PANEL DE RESERVAS");
        reservaadminStage.show();
        cerrar(); // Cerrar ventana actual
    }

    public void cerrar() {
        // Método para cerrar la ventana actual
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
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

    public void Muestra_usuarios() throws SQLException {
        // Método para mostrar la lista de usuarios en el GridPane
        Listado.getChildren().clear(); // Limpiar el contenido actual del GridPane

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
                String tipo = resultadoUsuarios.getString("tipo"); // Crear un panel para mostrar la información del usuario
                AnchorPane usuarioPanel = new AnchorPane();
                usuarioPanel.setPrefSize(450, 100);
                usuarioPanel.setStyle("-fx-background-color: A6234E; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: FFFFFF");

                // Etiqueta para mostrar el nombre y tipo del usuario
                Label nombreUsuario = new Label(nombre + " " + apellido + " (" + tipo + ")");
                nombreUsuario.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
                AnchorPane.setLeftAnchor(nombreUsuario, 10.0);
                AnchorPane.setTopAnchor(nombreUsuario, 10.0);
                usuarioPanel.getChildren().add(nombreUsuario);

                // Etiqueta para mostrar el email del usuario
                Label emailUsuario = new Label("Email: " + email);
                emailUsuario.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
                AnchorPane.setLeftAnchor(emailUsuario, 10.0);
                AnchorPane.setTopAnchor(emailUsuario, 35.0);
                usuarioPanel.getChildren().add(emailUsuario);

                // Etiqueta para mostrar el estado del usuario
                Label estadoUsuario = new Label("Estado: " + estado);
                estadoUsuario.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
                AnchorPane.setLeftAnchor(estadoUsuario, 10.0);
                AnchorPane.setTopAnchor(estadoUsuario, 60.0);
                usuarioPanel.getChildren().add(estadoUsuario);

                // Botón para modificar el usuario
                Button botonModificar = new Button();
                botonModificar.setStyle("-fx-background-color: transparent;");
                ImageView iconoModificar = new ImageView(new Image("/lapiz.png"));
                iconoModificar.setFitWidth(20);
                iconoModificar.setFitHeight(20);
                iconoModificar.setPreserveRatio(true);
                botonModificar.setGraphic(iconoModificar);
                botonModificar.setUserData(idUsuario);
                botonModificar.setOnAction(e -> {
                    idtraspaso = (int) botonModificar.getUserData(); // Guardar el ID del usuario a editar
                    muestraeditar(); // Llamar al método para mostrar la ventana de edición
                });
                AnchorPane.setRightAnchor(botonModificar, 80.0);
                AnchorPane.setBottomAnchor(botonModificar, 10.0);
                usuarioPanel.getChildren().add(botonModificar);

                // Botón para eliminar el usuario
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
                        eliminarUsuario((int) botonEliminar.getUserData()); // Llamar al método para eliminar el usuario
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                });
                AnchorPane.setRightAnchor(botonEliminar, 10.0);
                AnchorPane.setBottomAnchor(botonEliminar, 10.0);
                usuarioPanel.getChildren().add(botonEliminar);

                // Añadir el panel del usuario al GridPane
                Listado.add(usuarioPanel, 0, row);
                GridPane.setMargin(usuarioPanel, new Insets(10, 0, 10, 0));
                row++;
                GridPane.setVgrow(usuarioPanel, javafx.scene.layout.Priority.ALWAYS); // Permitir que el panel crezca
            }
            resultadoUsuarios.close(); // Cerrar el ResultSet
            sentenciaUsuarios.close(); // Cerrar el PreparedStatement
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Método que se ejecuta al inicializar la clase
        Username.textProperty().bind(Login.bannerusuarioProperty()); // Vincular el nombre de usuario al banner
        try {
            Muestra_usuarios(); // Mostrar la lista de usuarios
            
            // Cargar la imagen de perfil desde la ruta especificada
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
        } catch (SQLException e) {
            e.printStackTrace(); // Imprimir error si hay problemas con la base de datos
        }
    }

    public void nuevo_usuario() {
        // Método para abrir la ventana de creación de un nuevo usuario
        try {
            cerrar(); // Cerrar ventana actual
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Nuevo_usuarios.fxml"));
            AnchorPane nuevousuarioPane = cargador.load();
            Stage nuevousarioStage = new Stage();
            nuevousarioStage.initStyle(StageStyle.TRANSPARENT);
            nuevousarioStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(nuevousuarioPane, 800, 623);
            nuevousarioStage.setScene(scene);
            nuevousarioStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
            nuevousarioStage.setTitle("NUEVO USUARIO");
            nuevousarioStage.show(); // Mostrar la ventana de nuevo usuario
        } catch (IOException e) {
            e.printStackTrace(); // Imprimir error si no se puede cargar la vista
        }
    }

    public void Gestion_usuarios() throws IOException {
        // Método para abrir la ventana de gestión de usuarios
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
        Pane gestionusuariopane = cargador.load();
        Scene gestionusuarioScene = new Scene(gestionusuariopane, 600, 500);
        gestionusuarioScene.setFill(Color.TRANSPARENT);
        Stage gestiousuarioStage = new Stage();
        gestiousuarioStage.setResizable(false);
        gestiousuarioStage.initStyle(StageStyle.DECORATED);
        gestiousuarioStage.setScene(gestionusuarioScene);
        gestiousuarioStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        gestiousuarioStage.setTitle("PANEL DE GESTION DE USUARIOS");
        gestiousuarioStage.show(); // Mostrar la ventana de gestión de usuarios
        cerrar(); // Cerrar ventana actual
    }

    public void Gestionpedidos() throws IOException {
        // Método para abrir la ventana de gestión de pedidos
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/GestionPedidos.fxml"));
        Pane gestionpedidos = cargador.load();
        Scene gestionpedidosScene = new Scene(gestionpedidos, 600, 500);
        gestionpedidosScene.setFill(Color.TRANSPARENT);
        Stage gestionpedidosStage = new Stage();
        gestionpedidosStage.setResizable(false);
        gestionpedidosStage.initStyle(StageStyle.DECORATED);
        gestionpedidosStage.setScene(gestionpedidosScene);
        gestionpedidosStage.setTitle("GESTION DE PEDIDOS");
        gestionpedidosStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        gestionpedidosStage.show(); // Mostrar la ventana de gestión de pedidos
        cerrar(); // Cerrar ventana actual
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

    private void eliminarUsuario(int idUsuario) throws IOException, SQLException {
        // Método para eliminar un usuario de la base de datos
        String sqlEliminarEmpleado = "DELETE FROM empleados WHERE id_empleado = ?";
        String sqlEliminarAdmin = "DELETE FROM administradores WHERE id_admin = ?";
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            try (PreparedStatement stmt = conexion.prepareStatement(sqlEliminarEmpleado)) {
                stmt.setInt(1, idUsuario);
                int filasAfectadas = stmt .executeUpdate();
                if (filasAfectadas == 0) { // Si no se eliminó como empleado, intentar como administrador
                    try (PreparedStatement stmtAdmin = conexion.prepareStatement(sqlEliminarAdmin)) {
                        stmtAdmin.setInt(1, idUsuario);
                        stmtAdmin.executeUpdate(); // Ejecutar la eliminación del administrador
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprimir error si hay problemas con la base de datos
        }
        
        // Llama a Muestra_usuarios para actualizar el contenido sin cerrar la ventana
        Muestra_usuarios(); // Actualizar la lista de usuarios
    }

    private void muestraeditar() {
        // Método para mostrar la ventana de edición de usuarios
        try {
            cerrar(); // Cerrar ventana actual
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Editar_usuarios.fxml"));
            AnchorPane muestraeditarPane = cargador.load();
            Stage muestraeditarStage = new Stage();
            muestraeditarStage.initStyle(StageStyle.TRANSPARENT);
            muestraeditarStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(muestraeditarPane, 800, 623);
            muestraeditarStage.setScene(scene);
            muestraeditarStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
            muestraeditarStage.setTitle("EDITAR USUARIOS");
            muestraeditarStage.show(); // Mostrar la ventana de edición
        } catch (IOException e) {
            e.printStackTrace(); // Imprimir error si no se puede cargar la vista
        }
    }
}