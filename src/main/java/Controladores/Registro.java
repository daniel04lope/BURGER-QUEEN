package Controladores;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JOptionPane;



import Modelos.Empleado;
import Modelos.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Registro {
	 @FXML
	 Button Cerrar; 
	 @FXML
	 TextField Nombre;
	 @FXML
	 TextField Apellidos;
	 @FXML
	 TextField Username;
	 @FXML
	 TextField Email;
	 @FXML
	 TextField Password;
	 @FXML
	 TextField Telefono;
	 @FXML
	 TextField Direccion;
	 @FXML
	 	DatePicker Fecha_Nacimiento;
	public void cerrar() {
        // Obtener la referencia del Stage a partir del botón
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close(); // Cerrar la ventana
    }
	
	public void registrarse() {
	    String nombrestring = Nombre.getText().toString();
	    String apellidosstring = Apellidos.getText().toString();
	    String usernamestring = Username.getText().toString();
	    String emailstring = Email.getText().toString();
	    String passwordstring = Password.getText().toString();
	    String telefonostring = Telefono.getText().toString();
	    String direccionstring = Direccion.getText().toString();
	    LocalDate nacimiento = Fecha_Nacimiento.getValue();

	    Usuario usuario = new Usuario(nombrestring, apellidosstring, emailstring, usernamestring, passwordstring, "Activo", telefonostring, direccionstring, nacimiento);

	    try {
	        util.Conexiones.insertarpersona(usuario.getNombre(), usuario.getApellido(), usuario.getEmail(), usuario.getUsername(), usuario.getPassword(), usuario.getTelefono(), usuario.getDireccion(), usuario.getFechaNacimiento());
	        JOptionPane.showMessageDialog(null, "Registro exitoso para: " + emailstring);

	        String sql = "SELECT id_usuario FROM usuarios WHERE email = ?";
	        try (Connection conexion = util.Conexiones.dameConexion("burger-queen");
	             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

	            // Establece el parámetro en el PreparedStatement
	            sentencia.setString(1, emailstring);

	            ResultSet muestra = sentencia.executeQuery();

	            // Mueve el cursor al primer registro
	            if (muestra.next()) {
	                usuario.setIdUsuario(muestra.getInt("id_usuario"));
	               
	            } else {
	                JOptionPane.showMessageDialog(null, "No se pudo recuperar el ID de usuario.");
	            }

	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, e.getMessage());
	    }
	}

    
}
