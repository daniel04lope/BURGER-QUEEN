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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login {
	 private Pantalla_principal pantallaprincipal;
    @FXML
    Button Cerrar; // Botón de cerrar (ya definido en el FXML)
    @FXML
    TextField Email;
    @FXML
    PasswordField Password;
    private Pantalla_principal controladorPantallaPrincipal; // Cambiar a private para mejor encapsulación
    String nombreusuario;
  
    
    public void setVistaControlador(Pantalla_principal principal) {
        this.pantallaprincipal = principal;
    }
    // Constructor sin argumentos
    public Login() {
        // Constructor vacío para cumplir con la carga de FXML
    }

    // Método para cerrar la ventana de login
    @FXML
    public void cerrar() {
        // Obtener la referencia del Stage a partir del botón
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close(); // Cerrar la ventana
    }

    // Método para inyectar el controlador principal
    public void setControladorPantallaPrincipal(Pantalla_principal controlador) {
        this.controladorPantallaPrincipal = controlador;
    }

    public void iniciarSesion() throws SQLException {
    	  
        String emailString = Email.getText();
        String passwordString = Password.getText();

        Connection conexion = util.Conexiones.dameConexion("burger-queen");

        if (verificarCredencialesUsuario(conexion, "usuarios", emailString, passwordString)) {
        	pantallaprincipal.Username.setText(nombreusuario);
            
            JOptionPane.showMessageDialog(null, "Login exitoso para el usuario: " + emailString);
            
            cerrar();
        } else if (verificarCredencialesUsuario(conexion, "empleados", emailString, passwordString)) {
        	pantallaprincipal.Username.setText(nombreusuario);
            
            JOptionPane.showMessageDialog(null, "Login exitoso para el empleado: " + emailString);
           
            cerrar();
        } else if (verificarCredencialesUsuario(conexion, "administradores", emailString, passwordString)) {
        	pantallaprincipal.Username.setText(nombreusuario);
            
            JOptionPane.showMessageDialog(null, "Login exitoso para el administrador: " + emailString);
        
            cerrar();
        } else {
            JOptionPane.showMessageDialog(null, "Login fallido: Correo o contraseña inválidos");
            cerrar();
        }
    }

    // Método para comprobar credenciales
    private boolean verificarCredencialesUsuario(Connection conexion, String tableName, String email, String password) throws SQLException {
        String query = "SELECT password, username FROM " + tableName + " WHERE email = ?";

        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                // Si se encuentra el usuario
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    nombreusuario = resultSet.getString("username");
                    // Obtiene la contraseña almacenada
                    return password.equals(storedPassword); // Comparar directamente
                }
            }
        }
        return false; // Usuario no encontrado o contraseña incorrecta
    }
    
    public void Registro() throws IOException {
    	
    	  FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Registro.fxml"));
          Pane registro = loader.load();

          // Crear la escena del login con fondo transparente
          Scene loginScene = new Scene(registro, 450, 600);
          loginScene.setFill(Color.TRANSPARENT);

          // Crear un nuevo Stage para el login y configurarlo sin decoración y transparente
          Stage loginStage = new Stage();
          loginStage.initStyle(StageStyle.TRANSPARENT);
          loginStage.setScene(loginScene);
          loginStage.setTitle("REGISTRO");
          loginStage.show();
          cerrar();
    }
}
