package Controladores;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import Modelos.Usuario;
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

public class Login {
    
	public static String tipo="";
    @FXML
    Button Cerrar;
    @FXML
    TextField Email;
    @FXML
    PasswordField Password;
    private Pantalla_principal controladorPantallaPrincipal;
    String nombreusuario;
 
    public static Usuario datos_login = new Usuario();
    
    public static StringProperty banner = new SimpleStringProperty();
    
    public static StringProperty bannerusuarioProperty() {
        return banner;
    }
  public static StringProperty imagen = new SimpleStringProperty();
    
    public static StringProperty imagenProperty() {
        return imagen;
    }
    


    
    public Login() {
    }

    @FXML
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }
    
    public void cancela() throws IOException {
    	cerrar();
    	Pantalla_Principal();
    }
    
 

    public void iniciarSesion() throws SQLException, IOException {
        String emailString = Email.getText();
        String passwordString = Password.getText();

        Connection conexion = util.Conexiones.dameConexion("burger-queen");

        if (verificarCredencialesUsuario(conexion, "usuarios", emailString, passwordString)) {
            banner.set(nombreusuario);
            JOptionPane.showMessageDialog(null, "Login exitoso para el usuario: " + emailString);
            tipo = "usuarios";
            Pantalla_Principal();

            try {
                PreparedStatement sentencia = conexion.prepareStatement(
                    "SELECT id_usuario, nombre, apellido, email, username,password, fecha_registro, estado, telefono, direccion, fecha_nacimiento, ruta FROM usuarios WHERE email = ?"
                );
                sentencia.setString(1, emailString);
                ResultSet ejecuta = sentencia.executeQuery();

                if (ejecuta.next()) {
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
                    imagen.set(datos_login.getRuta());
                    System.out.println(datos_login.getRuta());
                } else {
                    System.out.println("No se encontró un usuario con el email especificado.");
                }

                if (nombreusuario.length() < 15) {
                    banner.set(nombreusuario);
                } else {
                    banner.set(nombreusuario.substring(0, 12) + "...");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            cerrar();
        } else if (verificarCredencialesUsuario(conexion, "empleados", emailString, passwordString)) {
            tipo = "empleados";

            try {
                PreparedStatement sentencia = conexion.prepareStatement(
                    "SELECT id_empleado, nombre, apellido, email, username,password, fecha_contratacion, estado, telefono, direccion, fecha_nacimiento, ruta FROM empleados WHERE email = ?"
                );
                sentencia.setString(1, emailString);
                ResultSet ejecuta = sentencia.executeQuery();

                if (ejecuta.next()) {
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

                if (nombreusuario.length() < 15) {
                    banner.set(nombreusuario);
                } else {
                    banner.set(nombreusuario.substring(0, 12) + "...");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Login exitoso para el empleado: " + emailString);
            cerrar();
            Pantalla_Principal();

        } else if (verificarCredencialesUsuario(conexion, "administradores", emailString, passwordString)) {
            tipo = "administradores";

            try {
                PreparedStatement sentencia = conexion.prepareStatement(
                    "SELECT id_admin, nombre, apellido, email, username,password, fecha_contratacion, estado, telefono, direccion, fecha_nacimiento, ruta FROM administradores WHERE email = ?"
                );
                sentencia.setString(1, emailString);
                ResultSet ejecuta = sentencia.executeQuery();

                if (ejecuta.next()) {
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

                if (nombreusuario.length() < 15) {
                    banner.set(nombreusuario);
                } else {
                    banner.set(nombreusuario.substring(0, 12) + "...");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Login exitoso para el administrador: " + emailString);
            cerrar();
            Pantalla_Principal();
        } else {
            JOptionPane.showMessageDialog(null, "Login fallido: Correo o contraseña inválidos");
        }
    }

    
    

    private boolean verificarCredencialesUsuario(Connection conexion, String tableName, String email, String password) throws SQLException {
        String query = "SELECT password, username FROM " + tableName + " WHERE email = ?";

        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    nombreusuario = resultSet.getString("username");
                    return password.equals(storedPassword);
                }
            }
        }
        return false;
    }
    
    
    
    
    public void Registro() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Registro.fxml"));
        Pane registro = cargador.load();

        Scene registroScene = new Scene(registro, 450, 600);
        registroScene.setFill(Color.TRANSPARENT);

        Stage registroStage = new Stage();
        registroStage.initStyle(StageStyle.TRANSPARENT);
        registroStage.setScene(registroScene);
        registroStage.initModality(Modality.APPLICATION_MODAL);
        registroStage.setTitle("REGISTRO");
        registroStage.show();
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
}