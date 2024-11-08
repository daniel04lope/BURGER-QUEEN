package Controladores;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

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
