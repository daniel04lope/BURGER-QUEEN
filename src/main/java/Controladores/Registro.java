package Controladores;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JOptionPane;

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
	
public void registrarse () {
	String nombrestring = Nombre.getText().toString();
	String apellidosstring = Apellidos.getText().toString();
	String usernamestring = Username.getText().toString();
	String emailstring = Email.getText().toString();
	String passwordstring = Password.getText().toString();
	String telefonostring = Telefono.getText().toString();
	String direccionstring = Direccion.getText().toString();
	LocalDate nacimiento = Fecha_Nacimiento.getValue();
	
	try {
		util.Conexiones.insertarpersona(nombrestring, apellidosstring, emailstring, usernamestring, passwordstring, telefonostring, direccionstring,nacimiento);
		JOptionPane.showMessageDialog(null, "Registro exitoso para: " + emailstring);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		JOptionPane.showMessageDialog(null, "Fallo a la hora del registro");
	}
}
    
}
