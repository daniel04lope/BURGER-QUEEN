package Controladores;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane; // Importar para usar JOptionPane

import Modelos.Usuario;

public class Login {
    
    public static String tipo = ""; // Tipo de usuario (administrador, empleado, usuario)
    
    @FXML
    Button Cerrar; // Botón para cerrar la ventana de login
    @FXML
    TextField Email; // Campo de texto para ingresar el email
    @FXML
    PasswordField Password; // Campo de texto para ingresar la contraseña

    private Pantalla_principal controladorPantallaPrincipal; // Controlador de la pantalla principal
    String nombreusuario; // Nombre de usuario obtenido al iniciar sesión
 
    public static Usuario datos_login = new Usuario(); // Objeto que almacena los datos del usuario
    
    public static StringProperty banner = new SimpleStringProperty(); // Propiedad para el banner de usuario
    
    public static StringProperty bannerusuarioProperty() {
        return banner; // Método para obtener la propiedad del banner
    }

    public static StringProperty imagen = new SimpleStringProperty(); // Propiedad para la imagen de perfil
    
    public static StringProperty imagenProperty() {
        return imagen; // Método para obtener la propiedad de la imagen
    }

    public Login() {
        // Constructor de la clase Login
    }

    @FXML
    public void cerrar() {
        // Método para cerrar la ventana de login
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }
    
    public void cancela() throws IOException {
        // Método para cancelar el login y volver a la pantalla principal
        cerrar(); // Cerrar la ventana de login
        Pantalla_Principal(); // Abrir la pantalla principal
    }

    public void iniciarSesion() throws SQLException, IOException {
        // Método para iniciar sesión
        String emailString = Email.getText(); // Obtener el email ingresado
        String passwordString = Password.getText(); // Obtener la contraseña ingresada

        Connection conexion = util.Conexiones.dameConexion("burger-queen"); // Conectar a la base de datos

        // Verificar credenciales del usuario en la tabla de usuarios
        if (verificarCredencialesUsuario(conexion, "usuarios", emailString, passwordString)) {
            banner.set(nombreusuario); // Establecer el nombre de usuario en el banner
            JOptionPane.showMessageDialog(null, "Login exitoso para el usuario: " + emailString); // Mostrar mensaje de éxito
            tipo = "usuarios"; // Establecer el tipo de usuario
            Pantalla_Principal(); // Abrir la pantalla principal

            // Obtener los datos del usuario desde la base de datos
            try {
                PreparedStatement sentencia = conexion.prepareStatement(
                    "SELECT id_usuario, nombre, apellido, email, username, password, fecha_registro, estado, telefono, direccion, fecha_nacimiento, ruta FROM usuarios WHERE email = ?"
                );
                sentencia.setString(1, emailString);
                ResultSet ejecuta = sentencia.executeQuery();

                if (ejecuta.next()) {
                    // Establecer los datos del usuario en el objeto datos_login
                    datos_login.setNombre(ejecuta.getString("nombre"));
                    datos_login.setIdUsuario(ejecuta.getInt("id_usuario"));
                    datos_login.setApellido(ejecuta.getString("apellido"));
                    datos_login.setEmail(ejecuta.getString("email"));
                    datos_login.setDireccion(ejecuta.getString("direccion"));
                    datos_login.setEstado(ejecuta.getString("estado"));
                    datos_login.setPassword(ejecuta.getString("password"));
                    datos_login.setTelefono(ejecuta.getString("telefono"));
                    datos_login.setUsername(ejecuta.getString("username"));
                    datos_login.setFechaRegistro(ejecuta.getTimestamp("fecha_registro"));
                    datos_login.setFechaNacimiento(ejecuta.getDate("fecha_nacimiento").toLocalDate());
                    datos_login.setRuta(ejecuta.getString("ruta"));
                    imagen.set(datos_login.getRuta()); // Establecer la ruta de la imagen
                    System.out.println(datos_login.getRuta());
                } else {
                    System.out.println("No se encontró un usuario con el email especificado.");
                }

                // Ajustar el banner de usuario
                if (nombreusuario.length() < 15) {
                    banner.set(nombreusuario);
                } else {
                    banner.set(nombreusuario.substring(0, 12) + "...");
                }

            } catch (Exception e) {
                e.printStackTrace(); // Manejo de excepciones
            }

            cerrar(); // Cerrar la ventana de login
        } else if (verificarCredencialesUsuario(conexion, "empleados", emailString, passwordString)) {
            tipo = "empleados"; // Establecer el tipo de usuario como empleado

            // Obtener los datos del empleado desde la base de datos
            try {
                PreparedStatement sentencia = conexion.prepareStatement(
                    "SELECT id_empleado, nombre, apellido, email, username, password, fecha_contratacion, estado, telefono, direccion, fecha_nacimiento, ruta FROM empleados WHERE email = ?"
                );
                sentencia.setString(1, emailString);
                ResultSet ejecuta = sentencia.executeQuery();

                if (ejecuta.next()) {
                    // Establecer los datos del empleado en el objeto datos_login
                    datos_login.setNombre(ejecuta.getString("nombre"));
                    datos_login.setIdUsuario(ejecuta.getInt("id_empleado"));
                    datos_login.setApellido(ejecuta.getString("apellido"));
                    datos_login.setEmail(ejecuta.getString("email"));
                    datos_login.setDireccion(ejecuta.getString("direccion"));
                    datos_login.setEstado(ejecuta.getString("estado"));
                    datos_login.setPassword(ejecuta.getString("password"));
                    datos_login.setTelefono(ejecuta.getString("telefono"));
                    datos_login.setUsername(ejecuta.getString("username"));
                    datos_login.setFechaRegistro(ejecuta.getTimestamp("fecha_contratacion"));
                    datos_login.setFechaNacimiento(ejecuta.getDate("fecha_nacimiento").toLocalDate());
                    datos_login.setRuta(ejecuta.getString("ruta"));
                } else {
                    System.out.println("No se encontró un empleado con el email especificado.");
                }

                // Ajustar el banner de usuario
                if (nombreusuario.length() < 15) {
                    banner.set(nombreusuario);
                } else {
                    banner.set(nombreusuario.substring(0, 12) + "...");
                }

            } catch (Exception e) {
                e.printStackTrace(); // Manejo de excepciones
            }

            JOptionPane.showMessageDialog(null, "Login exitoso para el empleado: " + emailString); // Mensaje de éxito
            cerrar(); // Cerrar la ventana de login
            Pantalla_Principal(); // Abrir la pantalla principal

        } else if (verificarCredencialesUsuario(conexion, "administradores", emailString, passwordString)) {
            tipo = "administradores"; // Establecer el tipo de usuario como administrador

            // Obtener los datos del administrador desde la base de datos
            try {
                PreparedStatement sentencia = conexion.prepareStatement(
                    "SELECT id_admin, nombre, apellido, email, username, password, fecha_contratacion, estado, telefono, direccion, fecha_nacimiento, ruta FROM administradores WHERE email = ?"
                );
                sentencia.setString(1, emailString);
                ResultSet ejecuta = sentencia.executeQuery();

                if (ejecuta.next()) {
                    // Establecer los datos del administrador en el objeto datos_login
                    datos_login.setNombre(ejecuta.getString("nombre"));
                    datos_login.setIdUsuario(ejecuta.getInt("id_admin"));
                    datos_login.setApellido(ejecuta.getString("apellido"));
                    datos_login.setEmail(ejecuta.getString("email"));
                    datos_login.setPassword(ejecuta.getString("password"));
                    datos_login.setTelefono(ejecuta.getString("telefono"));
                    datos_login.setDireccion(ejecuta.getString("direccion"));
                    datos_login.setEstado(ejecuta.getString("estado"));
                    datos_login.setUsername(ejecuta.getString("username"));
                    datos_login.setFechaRegistro(ejecuta.getTimestamp("fecha_contratacion"));
                    datos_login.setFechaNacimiento(ejecuta.getDate("fecha_nacimiento").toLocalDate());
                    datos_login.setRuta(ejecuta.getString("ruta"));
                } else {
                    System.out.println("No se encontró un administrador con el email especificado.");
                }

                // Ajustar el banner de usuario
                if (nombreusuario.length() < 15) {
                    banner.set(nombreusuario);
                } else {
                    banner.set(nombreusuario.substring(0, 12) + "...");
                }

            } catch (Exception e) {
                e.printStackTrace(); // Manejo de excepciones
            }

            JOptionPane.showMessageDialog(null, "Login exitoso para el administrador: " + emailString); // Mensaje de éxito
            cerrar(); // Cerrar la ventana de login
            Pantalla_Principal(); // Abrir la pantalla principal
        } else {
            JOptionPane.showMessageDialog(null, "Login fallido: Correo o contraseña inválidos"); // Mensaje de error
        }
    }

    private boolean verificarCredencialesUsuario(Connection conexion, String tableName, String email, String password) throws SQLException {
        // Método para verificar las credenciales del usuario en la base de datos
        String query = "SELECT password, username FROM " + tableName + " WHERE email = ?";

        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, email); // Establecer el email en la consulta
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password"); // Obtener la contraseña almacenada
                    nombreusuario = resultSet.getString("username"); // Obtener el nombre de usuario
                    return password.equals(storedPassword); // Comparar la contraseña ingresada con la almacenada
                }
            }
        }
        return false; // Retornar false si no se encontraron credenciales válidas
    }

    public void Registro() throws IOException {
        // Método para abrir la ventana de registro
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Registro.fxml"));
        Pane registro = cargador.load();

        Scene registroScene = new Scene(registro, 450, 600);
        registroScene.setFill(Color.TRANSPARENT); // Establecer el fondo transparente

        Stage registroStage = new Stage();
        registroStage.initStyle(StageStyle.TRANSPARENT); // Establecer el estilo de la ventana
        registroStage.setScene(registroScene);
        registroStage.initModality(Modality.APPLICATION_MODAL); // Hacer que la ventana sea modal
        registroStage.setTitle("REGISTRO"); // Título de la ventana
        registroStage.show(); // Mostrar la ventana de registro
        cerrar(); // Cerrar la ventana de login
    }
    
    public void Pantalla_Principal() throws IOException {
        // Método para abrir la pantalla principal
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principal = cargador.load();
        Scene principalScene = new Scene(principal, 600, 500);
        principalScene.setFill(Color.TRANSPARENT); // Establecer el fondo transparente
        Stage PrincipalStage = new Stage();
        PrincipalStage.setResizable(false); // No permitir redimensionar la ventana
        PrincipalStage.initStyle(StageStyle.DECORATED); // Establecer el estilo de la ventana
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("PANTALLA PRINCIPAL"); // Título de la ventana
        PrincipalStage.show(); // Mostrar la pantalla principal
        cerrar(); // Cerrar la ventana de login
    }
}