package Controladores;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.File;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nuevo_usuario implements Initializable {

    @FXML
    private Button Cerrar, btnSeleccionarFoto, guardar, cancelar;
    
    @FXML
    private TextField nombre, apellidos, email, username, telefono, direccion;
    
    @FXML
    private PasswordField password;
    
    @FXML
    private ComboBox<String> tipo, estado;
    
    @FXML
    private DatePicker fechanacimiento;
    
    @FXML
    private CheckBox lectura_reserva, escritura_reserva, lectura_pedidos, escritura_pedidos;
    
    @FXML
    private ImageView imageView;

    // Variable para almacenar la foto seleccionada
    private File fotoSeleccionada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Rellenar ComboBox de tipo y estado
        tipo.getItems().add("Empleado");
        tipo.getItems().add("Administrador");
        
        estado.getItems().add("Activo");
        estado.getItems().add("Inactivo");
        estado.getItems().add("Suspendido");

        // Acción para seleccionar una foto
        btnSeleccionarFoto.setOnAction(event -> seleccionarFoto());
        
        // Acción para cerrar el formulario
        Cerrar.setOnAction(event -> cerrar());
        
        // Acción para guardar el usuario
        guardar.setOnAction(event -> registrar());
        
        // Acción para cancelar el formulario
        cancelar.setOnAction(event -> cancelarFormulario());
    }

    // Método para cerrar la ventana
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    // Método para seleccionar la foto
    private void seleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));
        fotoSeleccionada = fileChooser.showOpenDialog(null);
        
        if (fotoSeleccionada != null) {
            imageView.setImage(new javafx.scene.image.Image(fotoSeleccionada.toURI().toString()));
        }
    }

    // Método para registrar un nuevo usuario con validaciones
    private void registrar() {
        // Validar campos vacíos
        if (nombre.getText().isEmpty() || apellidos.getText().isEmpty() || email.getText().isEmpty() ||
            username.getText().isEmpty() || password.getText().isEmpty() || telefono.getText().isEmpty() ||
            direccion.getText().isEmpty() || fechanacimiento.getValue() == null || tipo.getValue() == null || estado.getValue() == null) {
            showErrorAlert("Error", "Todos los campos deben ser completos.");
            return;
        }

        // Validar formato de correo electrónico
        if (!esEmailValido(email.getText())) {
            showErrorAlert("Error", "El correo electrónico no tiene un formato válido.");
            return;
        }

        // Validar la contraseña (mínimo 6 caracteres)
        if (password.getText().length() < 6) {
            showErrorAlert("Error", "La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        // Validar el tipo de usuario y el estado
        String tipoUsuario = tipo.getValue();
        String estadoUsuario = estado.getValue();

        String nombreUsuario = nombre.getText();
        String apellidoUsuario = apellidos.getText();
        String emailUsuario = email.getText();
        String usernameUsuario = username.getText();
        String passwordUsuario = password.getText();
        String telefonoUsuario = telefono.getText();
        String direccionUsuario = direccion.getText();
        String fechaNacimiento = fechanacimiento.getValue().toString();

        try {
            // Establecer conexión a la base de datos
            Connection conexion = util.Conexiones.dameConexion("burger-queen");

            // Insertar en la tabla 'empleados' o 'administradores' según el tipo de usuario
            String sql = "";
            if ("Empleado".equals(tipoUsuario)) {
                sql = "INSERT INTO empleados (nombre, apellido, email, username, password, telefono, direccion, estado, fecha_nacimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            } else if ("Administrador".equals(tipoUsuario)) {
                sql = "INSERT INTO administradores (nombre, apellido, email, username, password, telefono, direccion, estado, fecha_nacimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            }

            PreparedStatement pst = conexion.prepareStatement(sql);
            pst.setString(1, nombreUsuario);
            pst.setString(2, apellidoUsuario);
            pst.setString(3, emailUsuario);
            pst.setString(4, usernameUsuario);
            pst.setString(5, passwordUsuario);
            pst.setString(6, telefonoUsuario);
            pst.setString(7, direccionUsuario);
            pst.setString(8, estadoUsuario);
            pst.setString(9, fechaNacimiento);

            int filasAfectadas = pst.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Usuario registrado exitosamente");
                // Aquí se puede agregar el código para guardar la foto si es necesario

                // Asignar permisos para los módulos
                asignarPermisos(conexion, usernameUsuario, tipoUsuario);
            }

            conexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para validar el formato de email
    private boolean esEmailValido(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Método para mostrar una alerta de error
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para asignar permisos
    private void asignarPermisos(Connection con, String username, String tipoUsuario) {
        try {
            // Obtener ID del usuario recién insertado (empleado o admin)
            String getUserIdSql = "SELECT id_empleado FROM empleados WHERE username = ? LIMIT 1";
            PreparedStatement pstGetId = con.prepareStatement(getUserIdSql);
            pstGetId.setString(1, username);
            var rs = pstGetId.executeQuery();
            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt("id_empleado");
            }

            // Obtener módulos disponibles
            String sqlModulos = "SELECT id_modulo FROM modulos";
            PreparedStatement pstModulos = con.prepareStatement(sqlModulos);
            var rsModulos = pstModulos.executeQuery();

            while (rsModulos.next()) {
                int moduloId = rsModulos.getInt("id_modulo");
                
                // Asignar permisos
                String sqlPermisos = "INSERT INTO permisos (id_empleado, id_modulo, lectura, escritura) VALUES (?, ?, ?, ?)";
                PreparedStatement pstPermisos = con.prepareStatement(sqlPermisos);
                
                // Si es administrador, se le dan permisos completos en todos los módulos
                if ("Administrador".equals(tipoUsuario)) {
                    pstPermisos.setInt(1, userId);
                    pstPermisos.setInt(2, moduloId);
                    pstPermisos.setInt(3, 1); // Lectura
                    pstPermisos.setInt(4, 1); // Escritura
                } else if ("Empleado".equals(tipoUsuario)) {
                    // Asignar permisos según las opciones seleccionadas en la interfaz
                    pstPermisos.setInt(1, userId);
                    pstPermisos.setInt(2, moduloId);
                    
                    // Asignar permisos según los checkboxes de lectura y escritura
                    int lectura = lectura_reserva.isSelected() ? 1 : 0;
                    int escritura = escritura_reserva.isSelected() ? 1 : 0;
                    
                    pstPermisos.setInt(3, lectura);
                    pstPermisos.setInt(4, escritura);
                }
                pstPermisos.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para cancelar y cerrar el formulario
    private void cancelarFormulario() {
        Stage stage = (Stage) cancelar.getScene().getWindow();
        stage.close();
    }
}
