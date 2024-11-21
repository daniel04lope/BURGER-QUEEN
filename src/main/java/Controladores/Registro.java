package Controladores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import Modelos.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Registro {
    @FXML
    private Button Cerrar;
    @FXML
    private TextField Nombre;
    @FXML
    private TextField Apellidos;
    @FXML
    private TextField Username;
    @FXML
    private TextField Email;
    @FXML
    private TextField Password;
    @FXML
    private TextField Telefono;
    @FXML
    private TextField Direccion;
    @FXML
    private DatePicker Fecha_Nacimiento;
    @FXML
    private CheckBox Terms;
    private Usuario usuario;

    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    public void registrarse() {
        if (!Terms.isSelected()) {
            JOptionPane.showMessageDialog(null, "Debes aceptar los términos y condiciones para registrarte.");
            return;
        }
        
        String nombrestring = Nombre.getText();
        String apellidosstring = Apellidos.getText();
        String usernamestring = Username.getText();
        String emailstring = Email.getText();
        String passwordstring = Password.getText();
        String telefonostring = Telefono.getText();
        String direccionstring = Direccion.getText();
        LocalDate nacimiento = Fecha_Nacimiento.getValue();

         usuario = new Usuario(nombrestring, apellidosstring, emailstring, usernamestring, passwordstring, "Activo", telefonostring, direccionstring, nacimiento);

        try {
            util.Conexiones.insertarpersona(usuario.getNombre(), usuario.getApellido(), usuario.getEmail(), usuario.getUsername(), usuario.getPassword(), usuario.getTelefono(), usuario.getDireccion(), usuario.getFechaNacimiento());
            
            JOptionPane.showMessageDialog(null, "Registro exitoso para: " + emailstring);
            cerrar();
            Mostrar_Login();

            String sql = "SELECT id_usuario FROM usuarios WHERE email = ?";
            try (Connection conexion = util.Conexiones.dameConexion("burger-queen");
                 PreparedStatement sentencia = conexion.prepareStatement(sql)) {

                sentencia.setString(1, emailstring);
                ResultSet muestra = sentencia.executeQuery();

                if (muestra.next()) {
                    usuario.setIdUsuario(muestra.getInt("id_usuario"));
                    CreaCarrito();
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo recuperar el ID de usuario.");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void Mostrar_Login() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
            Pane login = loader.load();
            Scene loginScene = new Scene(login, 450, 600);
            loginScene.setFill(Color.TRANSPARENT);

            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);
            loginStage.setScene(loginScene);
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.setTitle("LOGIN");
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void CreaCarrito() {
    	  String sql = "INSERT INTO carrito (id_cliente, id_carrito) VALUES (?,?)";
    	 try (Connection conexion = util.Conexiones.dameConexion("burger-queen");
                 PreparedStatement sentencia = conexion.prepareStatement(sql)){
    		 sentencia.setInt(1,usuario.getIdUsuario());
    		 sentencia.setInt(2, usuario.getIdUsuario());
    		 int muestra = sentencia.executeUpdate();
    		 System.out.println("Carrito creado correctamente");
    		 
    	 }
    	 
    	 catch (Exception e) {
			// TODO: handle exception
    		 e.printStackTrace();
		}
    	
    }
}
