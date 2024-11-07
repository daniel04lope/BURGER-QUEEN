package Controladores;

import javafx.scene.control.TextArea;


import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;



public class Reserva {
	@FXML
    Text Username;
   @FXML
   private AnchorPane drawer;

   @FXML
   private Button Desplegable;
   @FXML
   private Button Cerrar;
   @FXML
   private TextField Nombre;
   @FXML
   private DatePicker Fecha_Reserva;
   @FXML
   private TextField Hora;
   @FXML
   private TextField Personas;
   @FXML
   private TextArea Datos_Adicionales;
   
   private boolean drawerVisible = false;
   private boolean Cerrardesplegar = false;
 

	  public void toggleDrawer() {
	    	System.out.println("Funciona");
	    	Cerrardesplegar=!Cerrardesplegar;
	        drawerVisible = !drawerVisible;
	        Cerrar.setVisible(Cerrardesplegar);
	        drawer.setVisible(drawerVisible);
	    }
	  public void cerrar() {
	        // Obtener la referencia del Stage a partir del botón
	        Stage stage = (Stage) Cerrar.getScene().getWindow();
	        stage.close(); // Cerrar la ventana
	    }
	  public void Pantalla_Principal() throws IOException {

    	  FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
          Pane principal = loader.load();
          
          // Crear la escena del login con fondo transparente
          Scene principalScene = new Scene(principal, 600, 500);
          principalScene.getStylesheets().add(getClass().getResource("Pantalla_Principal.css").toExternalForm());
          principalScene.setFill(Color.TRANSPARENT);

          // Crear un nuevo Stage para el login y configurarlo sin decoración y transparente
          Stage PrincipalStage = new Stage();
          PrincipalStage.initStyle(StageStyle.DECORATED);
          PrincipalStage.setScene(principalScene);
          PrincipalStage.setTitle("CARTA");
          PrincipalStage.show();
          cerrar();  
	  }
	    public void Carta() throws IOException{
	    	
	    	  FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
	          Pane registro = loader.load();
	         
	          
	          // Crear la escena del login con fondo transparente
	          Scene loginScene = new Scene(registro, 600, 500);
	          loginScene.setFill(Color.TRANSPARENT);
	          
	          // Crear un nuevo Stage para el login y configurarlo sin decoración y transparente
	          Stage loginStage = new Stage();
	          loginStage.initStyle(StageStyle.DECORATED);
	          loginStage.setScene(loginScene);
	          loginStage.setTitle("CARTA");
	          loginStage.show();
	          cerrar();  
	}
	
	    public void insertReserva() {
	     
	        ValidationSupport validationSupport = new ValidationSupport();
	        validationSupport.registerValidator(Nombre, Validator.createEmptyValidator("Nombre es requerido"));
	        validationSupport.registerValidator(Fecha_Reserva, Validator.createEmptyValidator("Fecha de reserva es requerida"));
	        validationSupport.registerValidator(Hora, Validator.createRegexValidator("Hora debe estar en formato HH:mm", "\\d{2}:\\d{2}", Severity.ERROR));
	        validationSupport.registerValidator(Personas, Validator.createRegexValidator("Número de personas debe ser un número", "\\d+", Severity.ERROR));
	    
	        if (validationSupport.isInvalid()) {
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Formulario incompleto");
	            alert.setHeaderText(null);
	            alert.setContentText("Por favor, complete todos los campos requeridos correctamente.");
	            alert.showAndWait();
	            return;
	        }
	        
	       
	        String nombreCliente = Nombre.getText();
	        LocalDate fechaReserva = Fecha_Reserva.getValue();
	        LocalTime horaReserva = LocalTime.parse(Hora.getText());
	        int numeroPersonas = Integer.parseInt(Personas.getText());
	        String datosAdicionales = Datos_Adicionales.getText();
	        
	        // Step 4: Insert data into the database
	        String insertQuery = "INSERT INTO reservas (nombre_cliente, fecha_reserva, hora_reserva, numero_personas, notas, estado) " +
	                             "VALUES (?, ?, ?, ?, ?, 'pendiente')";
	        
	        try (Connection conn = util.Conexiones.dameConexion("burger-queen");  // Use dameConexion method
	             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
	             
	            stmt.setString(1, nombreCliente);
	            stmt.setDate(2, java.sql.Date.valueOf(fechaReserva));
	            stmt.setTime(3, java.sql.Time.valueOf(horaReserva));
	            stmt.setInt(4, numeroPersonas);
	            stmt.setString(5, datosAdicionales);
	            
	            int rowsAffected = stmt.executeUpdate();
	            
	            if (rowsAffected > 0) {
	                Alert successAlert = new Alert(AlertType.INFORMATION);
	                successAlert.setTitle("Reserva guardada");
	                successAlert.setHeaderText(null);
	                successAlert.setContentText("La reserva se ha registrado exitosamente.");
	                successAlert.showAndWait();
	             
	            } else {
	                throw new SQLException("No se pudo guardar la reserva.");
	            }
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	            Alert errorAlert = new Alert(AlertType.ERROR);
	            errorAlert.setTitle("Error en la base de datos");
	            errorAlert.setHeaderText(null);
	            errorAlert.setContentText("Ocurrió un error al intentar guardar la reserva.");
	            errorAlert.showAndWait();
	        }
	   
	    }

	   

	    

	
}
