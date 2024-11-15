package Controladores;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Modelos.Usuario;
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
    
    @FXML
    Button Cerrar;
    @FXML
    TextField Email;
    @FXML
    PasswordField Password;
    private Pantalla_principal controladorPantallaPrincipal;
    String nombreusuario;
    static String bannerusuario;
    public static Usuario datos_login = new Usuario();
    public Login() {
    }

    @FXML
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    public void iniciarSesion() throws SQLException, IOException {
        String emailString = Email.getText();
        String passwordString = Password.getText();

        Connection conexion = util.Conexiones.dameConexion("burger-queen");

        if (verificarCredencialesUsuario(conexion, "usuarios", emailString, passwordString)) {
            Login.bannerusuario = nombreusuario;
            JOptionPane.showMessageDialog(null, "Login exitoso para el usuario: " + emailString);
            try {
                PreparedStatement sentencia = conexion.prepareStatement(
                    "SELECT id_usuario, nombre, apellido, email, username, fecha_registro, estado, telefono, direccion, fecha_nacimiento FROM usuarios WHERE email = ?"
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
                    datos_login.setUsername(ejecuta.getString("username"));
                    datos_login.setFechaRegistro(ejecuta.getTimestamp("fecha_registro"));
                    datos_login.setFechaNacimiento(ejecuta.getDate("fecha_nacimiento").toLocalDate());
                    System.out.println("Datos guardados correctamente");
                } else {
                    System.out.println("No se encontró un usuario con el email especificado.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            cerrar();
        } else if (verificarCredencialesUsuario(conexion, "empleados", emailString, passwordString)) {
            Login.bannerusuario = nombreusuario;
            JOptionPane.showMessageDialog(null, "Login exitoso para el empleado: " + emailString);
            cerrar();
        } else if (verificarCredencialesUsuario(conexion, "administradores", emailString, passwordString)) {
            Login.bannerusuario = nombreusuario;
            JOptionPane.showMessageDialog(null, "Login exitoso para el administrador: " + emailString);
            cerrar();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Registro.fxml"));
        Pane registro = loader.load();

        Scene loginScene = new Scene(registro, 450, 600);
        loginScene.setFill(Color.TRANSPARENT);

        Stage loginStage = new Stage();
        loginStage.initStyle(StageStyle.TRANSPARENT);
        loginStage.setScene(loginScene);
        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.setTitle("REGISTRO");
        loginStage.show();
        cerrar();
    }
}
