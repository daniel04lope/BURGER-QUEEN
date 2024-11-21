package Controladores;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Nuevo_usuario implements Initializable {

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

    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    public void seleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));
        fotoSeleccionada = fileChooser.showOpenDialog(null);
        if (fotoSeleccionada != null) {
            imageView.setImage(new javafx.scene.image.Image(fotoSeleccionada.toURI().toString()));
        }
    }

    public void registrar() {
        if (!validarCampos()) {
            mostrarError("Error", "Verifique que todos los campos sean correctos.");
            return;
        }

        try {
            Connection conexion = util.Conexiones.dameConexion("burger-queen");
            String sql;
            boolean esEmpleado = "Empleado".equals(tipo.getValue());
            String rutaImagenGuardada = guardarImagen();

            if (esEmpleado) {
                sql = "INSERT INTO empleados (nombre, apellido, email, username, password, telefono, direccion, estado, fecha_nacimiento, posicion, ruta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            } else {
                sql = "INSERT INTO administradores (nombre, apellido, email, username, password, telefono, direccion, estado, fecha_nacimiento, ruta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            }

            PreparedStatement pst = conexion.prepareStatement(sql);
            pst.setString(1, nombre.getText());
            pst.setString(2, apellidos.getText());
            pst.setString(3, email.getText());
            pst.setString(4, username.getText());
            pst.setString(5, password.getText());
            pst.setString(6, telefono.getText());
            pst.setString(7, direccion.getText());
            pst.setString(8, estado.getValue());
            pst.setString(9, fechanacimiento.getValue().toString());

            if (esEmpleado) {
                pst.setString(10, posicion.getText());
                pst.setString(11, rutaImagenGuardada);
            } else {
                pst.setString(10, rutaImagenGuardada);
            }

            int filasAfectadas = pst.executeUpdate();

            if (filasAfectadas > 0 && esEmpleado) {
                asignarPermisos(conexion, username.getText());
            }

            conexion.close();
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("CREADO");
            alerta.setHeaderText(null);
            alerta.setContentText("El usuario ha sido creado correctamente.");
            alerta.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error SQL", "No se pudo registrar al usuario en la base de datos.");
        }
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

    public void asignarPermisos(Connection conexion, String username) throws SQLException {
        String obtenerIdSql = "SELECT id_empleado FROM empleados WHERE username = ? LIMIT 1";
        PreparedStatement pstObtenerId = conexion.prepareStatement(obtenerIdSql);
        pstObtenerId.setString(1, username);
        var rs = pstObtenerId.executeQuery();
        int idEmpleado = -1;

        if (rs.next()) {
            idEmpleado = rs.getInt("id_empleado");
        }

        String obtenerModulosSql = "SELECT id_modulo FROM modulos";
        PreparedStatement pstObtenerModulos = conexion.prepareStatement(obtenerModulosSql);
        var rsModulos = pstObtenerModulos.executeQuery();

        while (rsModulos.next()) {
            int idModulo = rsModulos.getInt("id_modulo");
            String insertarPermisosSql = "INSERT INTO permisos (id_empleado, id_modulo, lectura, escritura) VALUES (?, ?, ?, ?)";
            PreparedStatement pstPermisos = conexion.prepareStatement(insertarPermisosSql);

            pstPermisos.setInt(1, idEmpleado);
            pstPermisos.setInt(2, idModulo);
            pstPermisos.setInt(3, lectura_reserva.isSelected() ? 1 : 0);
            pstPermisos.setInt(4, escritura_reserva.isSelected() ? 1 : 0);

            pstPermisos.executeUpdate();
        }
    }

    public void mostrarError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void cancelarFormulario() {
        Stage stage = (Stage) cancelar.getScene().getWindow();
        stage.close();
    }
}
