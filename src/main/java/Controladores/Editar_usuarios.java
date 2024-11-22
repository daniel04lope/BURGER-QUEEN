package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private Text texto_carta, texto_reserva, texto_pedidos, texto_posicion;

    private File fotoSeleccionada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tipo.getItems().addAll("Empleado", "Administrador");
        estado.getItems().addAll("Activo", "Inactivo", "Suspendido");

        int idUsuario = Gestion_usuarios.idtraspaso;

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sqlEmpleado = "SELECT * FROM empleados WHERE id_empleado = ?";
            String sqlAdmin = "SELECT * FROM administradores WHERE id_admin = ?";
            ResultSet resultado;

            PreparedStatement stmt = conexion.prepareStatement(sqlEmpleado);
            stmt.setInt(1, idUsuario);
            resultado = stmt.executeQuery();

            boolean esEmpleado = resultado.next();
            if (!esEmpleado) {
                stmt = conexion.prepareStatement(sqlAdmin);
                stmt.setInt(1, idUsuario);
                resultado = stmt.executeQuery();
                if (!resultado.next()) {
                    mostrarError("Error", "El usuario no existe.");
                    return;
                }
            }

            nombre.setText(resultado.getString("nombre"));
            apellidos.setText(resultado.getString("apellido"));
            email.setText(resultado.getString("email"));
            username.setText(resultado.getString("username"));
            password.setText(resultado.getString("password"));
            telefono.setText(resultado.getString("telefono"));
            direccion.setText(resultado.getString("direccion"));
            estado.setValue(resultado.getString("estado"));
            fechanacimiento.setValue(resultado.getDate("fecha_nacimiento").toLocalDate());

            if (esEmpleado) {
                tipo.setValue("Empleado");
                posicion.setText(resultado.getString("posicion"));
                texto_posicion.setVisible(true);
                posicion.setVisible(true);
                lectura_reserva.setVisible(true);
                escritura_reserva.setVisible(true);
                texto_carta.setVisible(true);
                texto_reserva.setVisible(true);
                texto_pedidos.setVisible(true);
                lectura_pedidos.setVisible(true);
                escritura_pedidos.setVisible(true);
                lectura_carta.setVisible(true);
                escritura_carta.setVisible(true);
            } else {
                tipo.setValue("Administrador");
                posicion.setVisible(false);
                lectura_reserva.setVisible(false);
                escritura_reserva.setVisible(false);
                texto_carta.setVisible(false);
                texto_reserva.setVisible(false);
                texto_pedidos.setVisible(false);
                lectura_pedidos.setVisible(false);
                escritura_pedidos.setVisible(false);
                lectura_carta.setVisible(false);
                escritura_carta.setVisible(false);
                texto_posicion.setVisible(false);
            }

            cargarImagen(resultado.getString("ruta"));

            tipo.setOnAction(event -> {
                boolean esAdministrador = "Administrador".equals(tipo.getValue());
                posicion.setVisible(!esAdministrador);
                lectura_reserva.setVisible(!esAdministrador);
                escritura_reserva.setVisible(!esAdministrador);
                texto_carta.setVisible(!esAdministrador);
                texto_reserva.setVisible(!esAdministrador);
                texto_pedidos.setVisible(!esAdministrador);
                lectura_pedidos.setVisible(!esAdministrador);
                escritura_pedidos.setVisible(!esAdministrador);
                lectura_carta.setVisible(!esAdministrador);
                escritura_carta.setVisible(!esAdministrador);
                texto_posicion.setVisible(!esAdministrador);
            });

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error SQL", "No se pudo cargar el usuario.");
        }
    }

    private void cargarImagen(String rutaImagen) {
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            Path ruta = Paths.get("src/main/resources/" + rutaImagen);
            if (Files.exists(ruta)) {
                imageView.setImage(new javafx.scene.image.Image(ruta.toUri().toString()));
            } else {
                cargarImagenPorDefecto();
            }
        } else {
            cargarImagenPorDefecto();
        }
    }

    private void cargarImagenPorDefecto() {
        String rutaPorDefecto = "src/main/resources/imagenes/perfil.png";
        Path ruta = Paths.get(rutaPorDefecto);
        if (Files.exists(ruta)) {
            imageView.setImage(new javafx.scene.image.Image(ruta.toUri().toString()));
        } 
    }

    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    public void cancelarFormulario() {
        Stage stage = (Stage) cancelar.getScene().getWindow();
        stage.close();
    }

    public void seleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));

        fotoSeleccionada = fileChooser.showOpenDialog(null);

        if (fotoSeleccionada != null) {
            imageView.setImage(new javafx.scene.image.Image(fotoSeleccionada.toURI().toString()));
        }
    }

    private String guardarImagen() {
        if (fotoSeleccionada == null) {
            return null;
        }

        Path destinoCarpeta = Paths.get("src/main/resources/imagenes");

        if (!Files.exists(destinoCarpeta)) {
            try {
                Files.createDirectories(destinoCarpeta);
            } catch (IOException e) {
                mostrarError("Error al crear directorio", "No se pudo crear el directorio para las imágenes.");
                return null;
            }
        }

        String nombreArchivo = System.currentTimeMillis() + "_" + fotoSeleccionada.getName();
        Path destinoArchivo = destinoCarpeta.resolve(nombreArchivo);

        try {
            Files.copy(fotoSeleccionada.toPath(), destinoArchivo);
            return "imagenes/" + nombreArchivo;
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

    public boolean validarCampos() {
        if (nombre.getText().isEmpty() || apellidos.getText().isEmpty() || email.getText().isEmpty() ||
                username.getText().isEmpty() || password.getText().isEmpty() || telefono.getText().isEmpty() ||
                direccion.getText().isEmpty() || fechanacimiento.getValue() == null || tipo.getValue() == null || estado.getValue() == null) {
            mostrarError("Campos incompletos", "Por favor, complete todos los campos.");
            return false;
        }

        if (!Pattern.matches("\\d{9}", telefono.getText())) {
            mostrarError("Teléfono incorrecto", "El número de teléfono debe tener 9 dígitos.");
            return false;
        }

        return true;
    }

    public void guardar() {
        if (!validarCampos()) return;

        String imagenRuta = guardarImagen();
        String sql = "UPDATE empleados SET nombre = ?, apellido = ?, email = ?, username = ?, password = ?, telefono = ?, direccion = ?, fecha_nacimiento = ?, tipo = ?, estado = ?, ruta_imagen = ?, posicion = ? WHERE id_empleado = ?";

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, nombre.getText());
            stmt.setString(2, apellidos.getText());
            stmt.setString(3, email.getText());
            stmt.setString(4, username.getText());
            stmt.setString(5, password.getText());
            stmt.setString(6, telefono.getText());
            stmt.setString(7, direccion.getText());
            stmt.setDate(8, java.sql.Date.valueOf(fechanacimiento.getValue()));
            stmt.setString(9, tipo.getValue());
            stmt.setString(10, estado.getValue());
            stmt.setString(11, imagenRuta);
            stmt.setString(12, posicion.getText());
            stmt.setInt(13, Gestion_usuarios.idtraspaso);
            stmt.executeUpdate();

            if ("Empleado".equals(tipo.getValue())) {
            }

            Stage stage = (Stage) guardar.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error SQL", "No se pudo guardar los cambios.");
        }
    }
    
    
    public void actualizar() {
        // Primero, validar que todos los campos sean correctos
        if (!validarCampos()) {
            return;
        }

        // Obtener el ID del usuario seleccionado para actualizar
        int idUsuario = Gestion_usuarios.idtraspaso;

        // Guardar la imagen seleccionada y obtener la ruta donde se almacenará
        String rutaImagenGuardada = guardarImagen();

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            // Determinar si el usuario es empleado o administrador
            String sql;
            boolean esEmpleado = "Empleado".equals(tipo.getValue());

            // Definir la consulta SQL según el tipo de usuario
            if (esEmpleado) {
                sql = "UPDATE empleados SET nombre = ?, apellido = ?, email = ?, password = ?, telefono = ?, direccion = ?, estado = ?, fecha_nacimiento = ?, posicion = ?, ruta = ? WHERE id_empleado = ?";
            } else {
                sql = "UPDATE administradores SET nombre = ?, apellido = ?, email = ?, password = ?, telefono = ?, direccion = ?, estado = ?, fecha_nacimiento = ?, ruta = ? WHERE id_admin = ?";
            }

            // Preparar la sentencia SQL para la actualización
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
                // Si es un empleado, incluir también el campo "posición" y la ruta de la imagen
                pst.setString(9, posicion.getText());
                pst.setString(10, rutaImagenGuardada); // Ruta de la imagen guardada
                pst.setInt(11, idUsuario); // El ID del empleado
            } else {
                // Si es un administrador, no se incluye el campo "posición"
                pst.setString(9, rutaImagenGuardada); // Ruta de la imagen guardada
                pst.setInt(10, idUsuario); // El ID del administrador
            }

            // Ejecutar la sentencia de actualización
            int filasAfectadas = pst.executeUpdate();

            // Si se actualizó correctamente, mostrar un mensaje de éxito
            if (filasAfectadas > 0) {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Actualizado");
                alerta.setHeaderText(null);
                alerta.setContentText("El usuario ha sido actualizado correctamente.");
                alerta.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Si ocurre un error SQL, mostrar un mensaje de error
            mostrarError("Error SQL", "No se pudo actualizar el usuario.");
        }
    }
}
