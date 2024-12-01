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
import javafx.scene.image.Image;
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
    private String rutaImagenAntigua;

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

            cargarDatosUsuario(resultado, esEmpleado);
            rutaImagenAntigua = resultado.getString("ruta"); // Guardar la ruta de la imagen anterior
            cargarImagen(rutaImagenAntigua); // Cargar la imagen

            tipo.setOnAction(event -> {
                boolean esAdministrador = "Administrador".equals(tipo.getValue());
                toggleFieldsVisibility(esAdministrador);
            });

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error SQL", "No se pudo cargar el usuario.");
        }
    }

    private void cargarDatosUsuario(ResultSet resultado, boolean esEmpleado) throws SQLException {
        nombre.setText(resultado.getString("nombre"));
        apellidos.setText(resultado.getString("apellido"));
        email.setText(resultado.getString("email"));
        username.setText(resultado.getString("username"));
        password.setText(resultado.getString("password"));
        telefono.setText(resultado.getString("telefono"));
        direccion.setText(resultado.getString("direccion"));
        estado.setValue(resultado.getString("estado"));
        fechanacimiento.setValue(resultado.getDate("fecha_nacimiento").toLocalDate());

        // Cargar la imagen
        String rutaImagen = resultado.getString("ruta");
        cargarImagen(rutaImagen);

        if (esEmpleado) {
            tipo.setValue("Empleado");
            posicion.setText(resultado.getString("posicion"));
            texto_posicion.setVisible(true);
            posicion.setVisible(true);
            toggleFieldsVisibility(false);
            cargarPermisos(resultado.getInt("id_empleado"));
        } else {
            tipo.setValue("Administrador");
            posicion.setVisible(false);
            toggleFieldsVisibility(true);
        }
    }

    private void cargarPermisos(int idUsuario) {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String permisos = "SELECT * FROM permisos WHERE id_empleado = ?";
            PreparedStatement sentencia = conexion.prepareStatement(permisos);
            sentencia.setInt(1, idUsuario);
            ResultSet respuesta = sentencia.executeQuery();

            // Inicializar los CheckBoxes para todos los módulos
            lectura_carta.setSelected(false);
            escritura_carta.setSelected(false);
            lectura_reserva.setSelected(false);
            escritura_reserva.setSelected(false);
            lectura_pedidos.setSelected(false);
            escritura_pedidos.setSelected(false);

            // Recorrer los resultados y establecer el estado de los CheckBoxes
            while (respuesta.next()) {
                int idModulo = respuesta.getInt("id_modulo");
                int lectura = respuesta.getInt("lectura");
                int escritura = respuesta.getInt("escritura");

                switch (idModulo) {
                    case 1: // Módulo 1
                        lectura_carta.setSelected(lectura == 1);
                        escritura_carta.setSelected(escritura == 1);
                        break;
                    case 2: // Módulo 2
                        lectura_reserva.setSelected(lectura == 1);
                        escritura_reserva.setSelected(escritura == 1);
                        break;
                    case 3: // Módulo 3
                        lectura_pedidos.setSelected(lectura == 1);
                        escritura_pedidos.setSelected(escritura == 1);
                        break;
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error SQL", "No se pudo cargar los permisos.");
        }
    }

    private void toggleFieldsVisibility(boolean esAdministrador) {
        boolean visible = !esAdministrador;
        posicion.setVisible(visible);
        lectura_reserva.setVisible(visible);
        escritura_reserva.setVisible(visible);
        texto_carta.setVisible(visible);
        texto_reserva.setVisible(visible);
        texto_pedidos.setVisible(visible);
        lectura_pedidos.setVisible(visible);
        escritura_pedidos.setVisible(visible);
        lectura_carta.setVisible(visible);
        escritura_carta.setVisible(visible);
        texto_posicion.setVisible(visible);
    }

    private void cargarImagen(String rutaImagen) {
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            Path ruta = Paths.get("src/main/resources/",rutaImagen);
            System.out.println("Cargando imagen desde: " + rutaImagen);

            if (Files.exists(ruta)) {
                imageView.setImage(new Image(ruta.toUri().toString()));
            } else {
            	System.out.println("No existe la ruta");
                cargarImagenPorDefecto();
                
            }
        } else {
        	System.out.println("la imagen es nula o vacia");
            cargarImagenPorDefecto();
        }
    }

    private void cargarImagenPorDefecto() {
        String rutaPorDefecto = "src/main/resources/imagenes/perfil.png";
        Path ruta = Paths.get(rutaPorDefecto);
        if (Files.exists(ruta)) {
            imageView.setImage(new Image(ruta.toUri().toString()));
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
            imageView.setImage(new Image(fotoSeleccionada.toURI().toString()));
        }
    }

    private String guardarImagen(int idUsuario) {
        if (fotoSeleccionada == null) {
            return null;
        }

        Path destinoCarpeta = Paths.get("src/main/resources/imagenes");

        // Asegurarse de que la carpeta existe
        if (!Files.exists(destinoCarpeta)) {
            try {
                Files.createDirectories(destinoCarpeta);
            } catch (IOException e) {
                mostrarError("Error al crear directorio", "No se pudo crear el directorio para las imágenes.");
                return null;
            }
        }

        // Obtener el nombre original del archivo
        String nombreArchivoOriginal = fotoSeleccionada.getName();
        Path destinoArchivo = destinoCarpeta.resolve(nombreArchivoOriginal);

        // Verificar si el archivo ya existe y modificar el nombre si es necesario
        int contador = 1;
        while (Files.exists(destinoArchivo)) {
            String nuevoNombre = nombreArchivoOriginal.substring(0, nombreArchivoOriginal.lastIndexOf('.')) 
                               + "_" + contador 
                               + nombreArchivoOriginal.substring(nombreArchivoOriginal.lastIndexOf('.'));
            destinoArchivo = destinoCarpeta.resolve(nuevoNombre);
            contador++;
        }

        try {
            Files.copy(fotoSeleccionada.toPath(), destinoArchivo);
            return "imagenes/" + destinoArchivo.getFileName().toString(); // Devolver la ruta relativa
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

        // Verificar si se ha seleccionado una nueva imagen
        String imagenRuta = (fotoSeleccionada != null) ? guardarImagen(Gestion_usuarios.idtraspaso) : rutaImagenAntigua;

        String sql = "UPDATE empleados SET nombre = ?, apellido = ?, email = ?, username = ?, password = ?, telefono = ?, direccion = ?, fecha_nacimiento = ?, tipo = ?, estado = ?, ruta = ?, posicion = ? WHERE id_empleado = ?";

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
            stmt.setString(11, imagenRuta); // Usar la ruta de la imagen anterior si no se seleccionó una nueva
            stmt.setInt(12, Gestion_usuarios.idtraspaso);
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

        String rutaImagenGuardada = guardarImagen(idUsuario);

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            if (esEmpleado) {
                actualizarEmpleado(conexion, idUsuario, rutaImagenGuardada);
            } else {
                actualizarAdministrador(conexion, idUsuario, rutaImagenGuardada);
            }

            // Actualizar permisos después de la actualización
            actualizarPermisos(conexion, idUsuario);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Actualizado");
            alerta.setHeaderText(null);
            alerta.setContentText("El usuario ha sido actualizado correctamente.");
            alerta.showAndWait();

            cerrar();
            Gestion_usuarios();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error SQL", "No se pudo actualizar el usuario: " + e.getMessage());
        }
    }

    private void actualizarEmpleado(Connection conexion, int idUsuario, String rutaImagenGuardada) throws SQLException {
        // Insertar o actualizar empleado
        String sql = "INSERT INTO empleados (id_empleado, nombre, apellido, email, username, password, telefono, direccion, estado, fecha_nacimiento, ruta, posicion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE nombre = ?, apellido = ?, email = ?, username = ?, password = ?, telefono = ?, direccion = ?, estado = ?, fecha_nacimiento = ?, ruta = ?, posicion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setString(2, nombre.getText());
            stmt.setString(3, apellidos.getText());
            stmt.setString(4, email.getText());
            stmt.setString(5, username.getText());
            stmt.setString(6, password.getText());
            stmt.setString(7, telefono.getText());
            stmt.setString(8, direccion.getText());
            stmt.setString(9, estado.getValue());
            stmt.setDate(10, java.sql.Date.valueOf(fechanacimiento.getValue()));
            stmt.setString(11, rutaImagenGuardada);
            stmt.setString(12, posicion.getText()); // Añadir la posición

            // Parámetros para el UPDATE
            stmt.setString(13, nombre.getText());
            stmt.setString(14, apellidos.getText());
            stmt.setString(15, email.getText());
            stmt.setString(16, username.getText());
            stmt.setString(17, password.getText());
            stmt.setString(18, telefono.getText());
            stmt.setString(19, direccion.getText());
            stmt.setString(20, estado.getValue());
            stmt.setDate(21, java.sql.Date.valueOf(fechanacimiento.getValue()));
            stmt.setString(22, rutaImagenGuardada);
            stmt.setString(23, posicion.getText()); // Actualizar posición

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se actualizó ningún registro de empleado.");
            }
        }

        // Eliminar de administradores si el usuario ahora es empleado
        String eliminarAdmin = "DELETE FROM administradores WHERE id_admin = ?";
        try (PreparedStatement stmtEliminar = conexion.prepareStatement(eliminarAdmin)) {
            stmtEliminar.setInt(1, idUsuario);
            stmtEliminar.executeUpdate();
        }
    }

    private void actualizarAdministrador(Connection conexion, int idUsuario, String rutaImagenGuardada) throws SQLException {
        // Insertar o actualizar administrador
        String sql = "INSERT INTO administradores (id_admin, nombre, apellido, email, username, password, telefono, direccion, estado, fecha_nacimiento, ruta) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE nombre = ?, apellido = ?, email = ?, username = ?, password = ?, telefono = ?, direccion = ?, estado = ?, fecha_nacimiento = ?, ruta = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setString(2, nombre.getText());
            stmt.setString(3, apellidos.getText());
            stmt.setString(4, email.getText());
            stmt.setString(5, username.getText());
            stmt.setString(6, password.getText());
            stmt.setString(7, telefono.getText());
            stmt.setString(8, direccion.getText());
            stmt.setString(9, estado.getValue());
            stmt.setDate(10, java.sql.Date.valueOf(fechanacimiento.getValue()));
            stmt.setString(11, rutaImagenGuardada);

            // Parámetros para el UPDATE
            stmt.setString(12, nombre.getText());
            stmt.setString(13, apellidos.getText());
            stmt.setString(14, email.getText());
            stmt.setString(15, username.getText());
            stmt.setString(16, password.getText());
            stmt.setString(17, telefono.getText());
            stmt.setString(18, direccion.getText());
            stmt.setString(19, estado.getValue());
            stmt.setDate(20, java.sql.Date.valueOf(fechanacimiento.getValue()));
            stmt.setString(21, rutaImagenGuardada);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se actualizó ningún registro de administrador.");
            }
        }

        // Eliminar de empleados si el usuario ahora es administrador
        String eliminarEmpleado = "DELETE FROM empleados WHERE id_empleado = ?";
        try (PreparedStatement stmtEliminar = conexion.prepareStatement(eliminarEmpleado)) {
            stmtEliminar.setInt(1, idUsuario);
            stmtEliminar.executeUpdate();
        }
    }



    private void actualizarPermisos(Connection conexion, int idUsuario) throws SQLException {
        // Verificar si el usuario es un empleado
        String verificarEmpleado = "SELECT COUNT(*) FROM empleados WHERE id_empleado = ?";
        try (PreparedStatement verificarStmt = conexion.prepareStatement(verificarEmpleado)) {
            verificarStmt.setInt(1, idUsuario);
            ResultSet rs = verificarStmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // Si no es empleado, no se actualizan permisos
                System.out.println("El usuario no es un empleado. No se actualizarán permisos.");
                return;
            }
        }

        // Eliminar permisos existentes
        String sqlEliminarPermisos = "DELETE FROM permisos WHERE id_empleado = ?";
        try (PreparedStatement stmtEliminarPermisos = conexion.prepareStatement(sqlEliminarPermisos)) {
            stmtEliminarPermisos.setInt(1, idUsuario);
            stmtEliminarPermisos.executeUpdate();
        }

        // Insertar nuevos permisos
        String sqlInsertPermisos = "INSERT INTO permisos (id_empleado, id_modulo, lectura, escritura) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmtInsertPermisos = conexion.prepareStatement(sqlInsertPermisos)) {
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
    }


    }
