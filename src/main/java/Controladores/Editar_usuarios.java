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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
   
    
    public void flechaatras() throws IOException {
    	cerrar();
    	Gestion_usuarios();
    }

    public void cerrar() throws IOException {
    	
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    public void cancelarFormulario() throws IOException {
    	Gestion_usuarios();
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

    private String guardarImagen(int idUsuario) {
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

        // Generar un nombre único utilizando ID del usuario y timestamp
        String nombreArchivo = idUsuario + "_" + System.currentTimeMillis() + "_" + fotoSeleccionada.getName();
        Path destinoArchivo = destinoCarpeta.resolve(nombreArchivo);

        try {
            // Guardar la nueva imagen
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

    public void guardar() throws IOException {
        if (!validarCampos()) return;

        // Guardar imagen con un nombre único basado en el ID del usuario
        String imagenRuta = guardarImagen(Gestion_usuarios.idtraspaso);

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
            stmt.setString(11, imagenRuta); // Guardar ruta de imagen
            stmt.setString(12, posicion.getText());
            stmt.setInt(13, Gestion_usuarios.idtraspaso);
            stmt.executeUpdate();

            Stage stage = (Stage) guardar.getScene().getWindow();
            stage.close();
            cerrar();
            Gestion_usuarios();
            
        
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error SQL", "No se pudo guardar los cambios.");
        }
    }
    public void Gestion_usuarios() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
        Pane registro = loader.load();
        Scene loginScene = new Scene(registro, 600, 500);
        loginScene.setFill(Color.TRANSPARENT);
        Stage loginStage = new Stage();
        loginStage.setResizable(false);
        loginStage.initStyle(StageStyle.DECORATED);
        loginStage.setScene(loginScene);
        loginStage.setTitle("Reservas");
        loginStage.show();
        cerrar();
    }

    
    public void actualizar() throws IOException {
        if (!validarCampos()) {
            return;
        }

        int idUsuario = Gestion_usuarios.idtraspaso;
        boolean esEmpleado = "Empleado".equals(tipo.getValue());
        boolean esAdministrador = "Administrador".equals(tipo.getValue());

        // Guardar imagen con un nombre único basado en el ID del usuario
        String rutaImagenGuardada = guardarImagen(idUsuario);

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            // Verificar el tipo actual del usuario
            String tipoActual = esEmpleado ? "Empleado" : "Administrador";

            if (tipoActual.equals(tipo.getValue())) {
                // Si el tipo de usuario no ha cambiado, hacer un UPDATE
                String sqlUpdate = "UPDATE empleados SET nombre = ?, apellido = ?, email = ?, username = ?, password = ?, telefono = ?, direccion = ?, estado = ?, fecha_nacimiento = ?, ruta = ?, posicion = ? WHERE id_empleado = ?";
                PreparedStatement stmtUpdate = conexion.prepareStatement(sqlUpdate);
                stmtUpdate.setString(1, nombre.getText());
                stmtUpdate.setString(2, apellidos.getText());
                stmtUpdate.setString(3, email.getText());
                stmtUpdate.setString(4, username.getText());
                stmtUpdate.setString(5, password.getText());
                stmtUpdate.setString(6, telefono.getText());
                stmtUpdate.setString(7, direccion.getText());
                stmtUpdate.setString(8, estado.getValue());
                stmtUpdate.setDate(9, java.sql.Date.valueOf(fechanacimiento.getValue()));
                stmtUpdate.setString(10, rutaImagenGuardada); // Guardar ruta de imagen
                stmtUpdate.setString(11, posicion.getText());
                stmtUpdate.setInt(12, idUsuario);
                stmtUpdate.executeUpdate();
            } else {
                // Si el tipo ha cambiado, mover el usuario a la tabla correspondiente
                if (esEmpleado) {
                    // Mover de administradores a empleados
                    String sqlEliminarAdmin = "DELETE FROM administradores WHERE id_admin = ?";
                    PreparedStatement stmtEliminarAdmin = conexion.prepareStatement(sqlEliminarAdmin);
                    stmtEliminarAdmin.setInt(1, idUsuario);
                    stmtEliminarAdmin.executeUpdate();

                    String sqlInsertEmpleado = "INSERT INTO empleados (id_empleado, nombre, apellido, email, username, password, telefono, direccion, estado, fecha_nacimiento, posicion, ruta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmtInsertEmpleado = conexion.prepareStatement(sqlInsertEmpleado);
                    stmtInsertEmpleado.setInt(1, idUsuario);
                    stmtInsertEmpleado.setString(2, nombre.getText());
                    stmtInsertEmpleado.setString(3, apellidos.getText());
                    stmtInsertEmpleado.setString(4, email.getText());
                    stmtInsertEmpleado.setString(5, username.getText());
                    stmtInsertEmpleado.setString(6, password.getText());
                    stmtInsertEmpleado.setString(7, telefono.getText());
                    stmtInsertEmpleado.setString(8, direccion.getText());
                    stmtInsertEmpleado.setString(9, estado.getValue());
                    stmtInsertEmpleado.setDate(10, java.sql.Date.valueOf(fechanacimiento.getValue()));
                    stmtInsertEmpleado.setString(11, posicion.getText());
                    stmtInsertEmpleado.setString(12, rutaImagenGuardada);
                    stmtInsertEmpleado.executeUpdate();
                } else if (esAdministrador) {
                    // Mover de empleados a administradores
                    String sqlEliminarEmpleado = "DELETE FROM empleados WHERE id_empleado = ?";
                    PreparedStatement stmtEliminarEmpleado = conexion.prepareStatement(sqlEliminarEmpleado);
                    stmtEliminarEmpleado.setInt(1, idUsuario);
                    stmtEliminarEmpleado.executeUpdate();

                    String sqlInsertAdmin = "INSERT INTO administradores (id_admin, nombre, apellido, email, username, password, telefono, direccion, estado, fecha_nacimiento, ruta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmtInsertAdmin = conexion.prepareStatement(sqlInsertAdmin);
                    stmtInsertAdmin.setInt(1, idUsuario);
                    stmtInsertAdmin.setString(2, nombre.getText());
                    stmtInsertAdmin.setString(3, apellidos.getText());
                    stmtInsertAdmin.setString(4, email.getText());
                    stmtInsertAdmin.setString(5, username.getText());
                    stmtInsertAdmin.setString (6, password.getText());
                    stmtInsertAdmin.setString(7, telefono.getText());
                    stmtInsertAdmin.setString(8, direccion.getText());
                    stmtInsertAdmin.setString(9, estado.getValue());
                    stmtInsertAdmin.setDate(10, java.sql.Date.valueOf(fechanacimiento.getValue()));
                    stmtInsertAdmin.setString(11, rutaImagenGuardada);
                    stmtInsertAdmin.executeUpdate();
                }
            }

            // Eliminar permisos existentes
            String sqlEliminarPermisos = "DELETE FROM permisos WHERE id_empleado = ?";
            PreparedStatement stmtEliminarPermisos = conexion.prepareStatement(sqlEliminarPermisos);
            stmtEliminarPermisos.setInt(1, idUsuario);
            stmtEliminarPermisos.executeUpdate();

            // Insertar nuevos permisos para los módulos 1, 2 y 3
            if (esEmpleado) {
                String sqlInsertPermisos = "INSERT INTO permisos (id_empleado, id_modulo, lectura, escritura) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtInsertPermisos = conexion.prepareStatement(sqlInsertPermisos);

                // Módulo 1
                stmtInsertPermisos.setInt(1, idUsuario);
                stmtInsertPermisos.setInt(2, 1); // ID del módulo 1
                stmtInsertPermisos.setInt(3, lectura_carta.isSelected() ? 1 : 0);
                stmtInsertPermisos.setInt(4, escritura_carta.isSelected() ? 1 : 0);
                stmtInsertPermisos.executeUpdate();

                // Módulo 2
                stmtInsertPermisos.setInt(1, idUsuario);
                stmtInsertPermisos.setInt(2, 2); // ID del módulo 2
                stmtInsertPermisos.setInt(3, lectura_reserva.isSelected() ? 1 : 0);
                stmtInsertPermisos.setInt(4, escritura_reserva.isSelected() ? 1 : 0);
                stmtInsertPermisos.executeUpdate();

                // Módulo 3
                stmtInsertPermisos.setInt(1, idUsuario);
                stmtInsertPermisos.setInt(2, 3); // ID del módulo 3
                stmtInsertPermisos.setInt(3, lectura_pedidos.isSelected() ? 1 : 0);
                stmtInsertPermisos.setInt(4, escritura_pedidos.isSelected() ? 1 : 0);
                stmtInsertPermisos.executeUpdate();
            }

            // Confirmación de éxito
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Actualizado");
            alerta.setHeaderText(null);
            alerta.setContentText("El usuario ha sido actualizado correctamente.");
            alerta.showAndWait();

            cerrar();
            Gestion_usuarios();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error SQL", "No se pudo actualizar el usuario.");
        }
    }

}