package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Editar_usuarios implements Initializable {
	 @FXML
	    private TextField nombre, apellidos, email, username, password, telefono, direccion, posicion;
	    @FXML
	    private ComboBox<String> tipo, estado;
	    @FXML
	    private DatePicker fechanacimiento;
	    @FXML
	    private CheckBox lectura_reserva, escritura_reserva, lectura_pedidos, escritura_pedidos, lectura_carta, escritura_carta;
	    @FXML
	    private ImageView imageView;
	    @FXML
	    private Button Cerrar, guardar, cancelar, btnSeleccionarFoto;
	    @FXML
	    private Text texto_posicion, texto_reserva, texto_pedidos, texto_carta;

	    private File fotoSeleccionada;

	
	
	public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }
	  public void cancelarFormulario() {
	        Stage stage = (Stage) cancelar.getScene().getWindow();
	        stage.close();
	    }
	  
	  private String guardarImagen() {
	        if (fotoSeleccionada == null) {
	            return null;
	        }

	        Path destinoCarpeta = Paths.get("src/main/resources");
	        String nombreArchivo = fotoSeleccionada.getName();
	        Path destinoArchivo = destinoCarpeta.resolve(nombreArchivo);

	        try {
	            Files.copy(fotoSeleccionada.toPath(), destinoArchivo);
	            return "src/main/resources/" + nombreArchivo;
	        } catch (IOException e) {
	            mostrarError("Error al guardar imagen", "No se pudo guardar la imagen seleccionada.");
	            e.printStackTrace();
	            return null;
	        }
	    }
	  
	  
	  
	   public void mostrarError(String titulo, String mensaje) {
	        Alert alerta = new Alert(Alert.AlertType.ERROR);
	        alerta.setTitle(titulo);
	        alerta.setHeaderText(null);
	        alerta.setContentText(mensaje);
	        alerta.showAndWait();
	    }
	   
	   public void seleccionarFoto() {
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));
	        fotoSeleccionada = fileChooser.showOpenDialog(null);
	        if (fotoSeleccionada != null) {
	            imageView.setImage(new javafx.scene.image.Image(fotoSeleccionada.toURI().toString()));
	        }
	    }
	   @Override
	    public void initialize(URL location, ResourceBundle resources) {
	        tipo.getItems().addAll("Empleado", "Administrador");
	        estado.getItems().addAll("Activo", "Inactivo", "Suspendido");

	        tipo.setOnAction(event -> {
	            boolean esAdministrador = "Administrador".equals(tipo.getValue());
	            posicion.setVisible(!esAdministrador);
	            texto_posicion.setVisible(!esAdministrador);
	            lectura_reserva.setVisible(!esAdministrador);
	            escritura_reserva.setVisible(!esAdministrador);
	            texto_reserva.setVisible(!esAdministrador);
	            lectura_pedidos.setVisible(!esAdministrador);
	            escritura_pedidos.setVisible(!esAdministrador);
	            texto_carta.setVisible(!esAdministrador);
	            lectura_carta.setVisible(!esAdministrador);
	            escritura_carta.setVisible(!esAdministrador);
	            texto_pedidos.setVisible(!esAdministrador);
	        });
	    }
	   
	   
	   public boolean validarCampos() {
	        if (nombre.getText().isEmpty() || apellidos.getText().isEmpty() || email.getText().isEmpty() ||
	                username.getText().isEmpty() || password.getText().isEmpty() || telefono.getText().isEmpty() ||
	                direccion.getText().isEmpty() || fechanacimiento.getValue() == null || tipo.getValue() == null || estado.getValue() == null) {
	            mostrarError("Error", "Hay campos necesarios vacíos.");
	            return false;
	        }

	        if (!Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$").matcher(email.getText()).matches()) {
	            mostrarError("Error", "Correo electrónico inválido. Ejemplo: prueba@prueba.com");
	            return false;
	        }

	        if (password.getText().length() < 6) {
	            mostrarError("Error", "La contraseña debe tener al menos 6 caracteres.");
	            return false;
	        }

	        if ("Empleado".equals(tipo.getValue()) && posicion.getText().isEmpty()) {
	            mostrarError("Error", "El campo posición es obligatorio para empleados.");
	            return false;
	        }

	        return true;
	    }

	   public void actualizarPermisos(Connection conexion, String username) throws SQLException {
		    // Get user id
		    String obtenerIdSql = "SELECT id_empleado FROM empleados WHERE username = ? LIMIT 1";
		    PreparedStatement pstObtenerId = conexion.prepareStatement(obtenerIdSql);
		    pstObtenerId.setString(1, username);
		    var rs = pstObtenerId.executeQuery();
		    int idEmpleado = -1;

		    if (rs.next()) {
		        idEmpleado = rs.getInt("id_empleado");
		    } else {
		        // If the user is an admin, you might need another query to get the admin ID.
		        return; // Exit if no employee ID found.
		    }

		    // Update permissions
		    String updatePermisosSql = "UPDATE permisos SET lectura = ?, escritura = ? WHERE id_empleado = ? AND id_modulo = ?";
		    PreparedStatement pstUpdatePermisos = conexion.prepareStatement(updatePermisosSql);
		    
		    // Example for updating the 'reserva' permissions (repeat for each module/permission)
		    pstUpdatePermisos.setInt(1, lectura_reserva.isSelected() ? 1 : 0);
		    pstUpdatePermisos.setInt(2, escritura_reserva.isSelected() ? 1 : 0);
		    pstUpdatePermisos.setInt(3, idEmpleado); // Employee ID
		    pstUpdatePermisos.setInt(4, 1); // Replace with the actual module ID (1 for 'reserva')
		    pstUpdatePermisos.executeUpdate();

		    // Repeat the above for other modules like 'pedidos', 'carta', etc.
		}

	   
	    public void actualizar() {
	        if (!validarCampos()) {
	            mostrarError("Error", "Verifique que todos los campos sean correctos.");
	            return;
	        }

	        try {
	            Connection conexion = util.Conexiones.dameConexion("burger-queen");

	            // Check if the user exists
	            String checkUserSql = "SELECT id_empleado FROM empleados WHERE username = ? UNION SELECT id_admin FROM administradores WHERE username = ?";
	            PreparedStatement pstCheckUser = conexion.prepareStatement(checkUserSql);
	            pstCheckUser.setString(1, username.getText());
	            pstCheckUser.setString(2, username.getText());
	            var rs = pstCheckUser.executeQuery();

	            if (!rs.next()) {
	                mostrarError("Error", "El usuario no existe.");
	                return;
	            }

	            // Determine if the user is an employee or admin
	            boolean esEmpleado = "Empleado".equals(tipo.getValue());
	            String rutaImagenGuardada = guardarImagen();
	            String sql;

	            if (esEmpleado) {
	                sql = "UPDATE empleados SET nombre = ?, apellido = ?, email = ?, password = ?, telefono = ?, direccion = ?, estado = ?, fecha_nacimiento = ?, posicion = ?, ruta = ? WHERE username = ?";
	            } else {
	                sql = "UPDATE administradores SET nombre = ?, apellido = ?, email = ?, password = ?, telefono = ?, direccion = ?, estado = ?, fecha_nacimiento = ?, ruta = ? WHERE username = ?";
	            }

	            PreparedStatement pst = conexion.prepareStatement(sql);
	            pst.setString(1, nombre.getText());
	            pst.setString(2, apellidos.getText());
	            pst.setString(3, email.getText());
	            pst.setString(4, password.getText());
	            pst.setString(5, telefono.getText());
	            pst.setString(6, direccion.getText());
	            pst.setString(7, estado.getValue());
	            pst.setString(8, fechanacimiento.getValue().toString());

	            if (esEmpleado) {
	                pst.setString(9, posicion.getText());
	                pst.setString(10, rutaImagenGuardada);
	            } else {
	                pst.setString(9, rutaImagenGuardada);
	            }
	            pst.setString(11, username.getText()); // Use username as the identifier

	            int filasAfectadas = pst.executeUpdate();

	            if (filasAfectadas > 0 && esEmpleado) {
	                // Update permissions for employee
	                actualizarPermisos(conexion, username.getText());
	            }

	            conexion.close();
	            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
	            alerta.setTitle("ACTUALIZADO");
	            alerta.setHeaderText(null);
	            alerta.setContentText("El usuario ha sido actualizado correctamente.");
	            alerta.showAndWait();

	        } catch (SQLException e) {
	            e.printStackTrace();
	            mostrarError("Error SQL", "No se pudo actualizar el usuario en la base de datos.");
	        }
	    }


}
