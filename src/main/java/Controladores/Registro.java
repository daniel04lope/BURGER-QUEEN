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
    private Button Cerrar; // Botón para cerrar la ventana
    @FXML
    private TextField Nombre; // Campo para el nombre
    @FXML
    private TextField Apellidos; // Campo para los apellidos
    @FXML
    private TextField Username; // Campo para el nombre de usuario
    @FXML
    private TextField Email; // Campo para el email
    @FXML
    private TextField Password; // Campo para la contraseña
    @FXML
    private TextField Telefono; // Campo para el teléfono
    @FXML
    private TextField Direccion; // Campo para la dirección
    @FXML
    private DatePicker Fecha_Nacimiento; // Selector de fecha para la fecha de nacimiento
    @FXML
    private CheckBox Terms; // Checkbox para aceptar términos y condiciones
    private Usuario usuario; // Objeto Usuario para almacenar los datos del nuevo usuario

    // Método para cerrar la ventana actual
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    // Método para cancelar el registro y mostrar la ventana de inicio de sesión
    public void cancelar() {
        cerrar();
        Mostrar_Login();
    }

    // Método para registrar un nuevo usuario
    public void registrarse() {
        // Verificar que se acepten los términos y condiciones
        if (!Terms.isSelected()) {
            JOptionPane.showMessageDialog(null, "Debes aceptar los términos y condiciones para registrarte.");
            return;
        }

        // Obtener los datos ingresados por el usuario
        String nombrestring = Nombre.getText();
        String apellidosstring = Apellidos.getText();
        String usernamestring = Username.getText();
        String emailstring = Email.getText();
        String passwordstring = Password.getText();
        String telefonostring = Telefono.getText();
        String direccionstring = Direccion.getText();
        LocalDate nacimiento = Fecha_Nacimiento.getValue();

        // Crear un nuevo objeto Usuario
        usuario = new Usuario(nombrestring, apellidosstring, emailstring, usernamestring, passwordstring, "Activo", telefonostring, direccionstring, nacimiento);

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            // Verificar si el email ya está registrado
            String consultaEmail = "SELECT COUNT(*) AS total FROM usuarios WHERE email = ?";
            try (PreparedStatement verificaEmail = conexion.prepareStatement(consultaEmail)) {
                verificaEmail.setString(1, emailstring);
                ResultSet resultado = verificaEmail.executeQuery();

                if (resultado.next() && resultado.getInt("total") > 0) {
                    JOptionPane.showMessageDialog(null, "El correo electrónico ya está registrado. No se puede completar el registro.");
                    return;
                }
            }

            // Insertar el nuevo usuario en la base de datos
            util.Conexiones.insertarpersona(usuario.getNombre(), usuario.getApellido(), usuario.getEmail(), usuario.getUsername(),
                    usuario.getPassword(), usuario.getTelefono(), usuario.getDireccion(), usuario.getFechaNacimiento());

            JOptionPane.showMessageDialog(null, "Registro exitoso para: " + emailstring);
            cerrar();
            Mostrar_Login();

            // Obtener el ID del nuevo usuario
            String sql = "SELECT id_usuario FROM usuarios WHERE email = ?";
            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setString(1, emailstring);
                ResultSet muestra = sentencia.executeQuery();

                if (muestra.next()) {
                    usuario.setIdUsuario(muestra.getInt("id_usuario"));
                    CreaCarrito(); // Crear un carrito para el nuevo usuario
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo recuperar el ID de usuario.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // Método para mostrar la ventana de inicio de sesión
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

    // Método para crear un carrito para el nuevo usuario
    public void CreaCarrito() {
        String sql = "INSERT INTO carrito (id_cliente, id_carrito) VALUES (?, ?)";
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen");
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, usuario.getIdUsuario());
            sentencia.setInt(2, usuario.getIdUsuario());
            int muestra = sentencia.executeUpdate();
            System.out.println("Carrito creado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}