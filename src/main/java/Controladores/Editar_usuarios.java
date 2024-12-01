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

    private File fotoSeleccionada; // Variable para almacenar la foto seleccionada por el usuario
    private String rutaImagenAntigua; // Variable para almacenar la ruta de la imagen anterior

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar los ComboBox con opciones de tipo y estado
        tipo.getItems().addAll("Empleado", "Administrador");
        estado.getItems().addAll("Activo", "Inactivo", "Suspendido");

        int idUsuario = Gestion_usuarios.idtraspaso; // Obtener el ID del usuario a editar

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            // Consultar si el usuario es un empleado
            String sqlEmpleado = "SELECT * FROM empleados WHERE id_empleado = ?";
            String sqlAdmin = "SELECT * FROM administradores WHERE id_admin = ?";
            ResultSet resultado;

            PreparedStatement stmt = conexion.prepareStatement(sqlEmpleado);
            stmt.setInt(1, idUsuario);
            resultado = stmt.executeQuery();

            boolean esEmpleado = resultado.next(); // Verificar si el resultado contiene un empleado
            if (!esEmpleado) {
                // Si no es empleado, verificar si es administrador
                stmt = conexion.prepareStatement(sqlAdmin);
                stmt.setInt(1, idUsuario);
                resultado = stmt.executeQuery();
                if (!resultado.next()) {
                    mostrarError("Error", "El usuario no existe."); // Mostrar error si no se encuentra el usuario
                    return;
                }
            }

            // Cargar los datos del usuario en los campos de texto
            cargarDatosUsuario(resultado, esEmpleado);
            rutaImagenAntigua = resultado.getString("ruta"); // Guardar la ruta de la imagen anterior
            cargarImagen(rutaImagenAntigua); // Cargar la imagen

            // Configurar el comportamiento del ComboBox tipo
            tipo.setOnAction(event -> {
                boolean esAdministrador = "Administrador".equals(tipo.getValue());
                toggleFieldsVisibility(esAdministrador); // Mostrar u ocultar campos según el tipo de usuario
            });

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error SQL", "No se pudo cargar el usuario."); // Mostrar error en caso de problemas con la base de datos
        }
    }

    private void cargarDatosUsuario(ResultSet resultado, boolean esEmpleado) throws SQLException {
        // Cargar los datos del usuario en los campos de texto
        nombre.setText(resultado.getString("nombre"));
        apellidos.setText(resultado.getString("apellido"));
        email.setText(resultado.getString("email"));
        username.setText(resultado.getString("username"));
        password.setText(resultado.getString("password"));
        telefono.setText(resultado.getString("telefono"));
        direccion.setText(resultado.getString("direccion"));
        estado.setValue(resultado.getString("estado"));
        fechanacimiento.setValue(resultado.getDate("fecha_nacimiento").toLocalDate());

        // Cargar la imagen del usuario
        String rutaImagen = resultado.getString("ruta");
        cargarImagen(rutaImagen);

        if (esEmpleado) {
            // Si el usuario es un empleado, cargar su posición y permisos
            tipo.setValue("Empleado");
            posicion.setText(resultado.getString("posicion"));
            texto_posicion.setVisible(true);
            posicion.setVisible(true);
            toggleFieldsVisibility(false); // Mostrar campos específicos de empleados
            cargarPermisos(resultado.getInt(resultado.getInt("id_empleado"))); // Cargar los permisos del empleado
        } else {
            // Si el usuario es un administrador, ocultar el campo de posición
            tipo.setValue("Administrador");
            posicion.setVisible(false);
            toggleFieldsVisibility(true); // Mostrar campos específicos de administradores
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
            mostrarError("Error SQL", "No se pudo cargar los permisos."); // Mostrar error si no se pueden cargar los permisos
        }
    }

    private void toggleFieldsVisibility(boolean esAdministrador) {
        // Mostrar u ocultar campos según el tipo de usuario
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
        // Cargar la imagen del usuario desde la ruta especificada
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            Path ruta = Paths.get(rutaImagen);
            if (Files.exists(ruta)) {
                imageView.setImage(new Image(ruta.toUri().toString())); // Mostrar la imagen si existe
            } else {
                cargarImagenPorDefecto(); // Cargar imagen por defecto si no existe
            }
        } else {
            cargarImagenPorDefecto(); // Cargar imagen por defecto si la ruta está vacía
        }
    }

    private void cargarImagenPorDefecto() {
        // Cargar una imagen por defecto si no se encuentra la imagen del usuario
        String rutaPorDefecto = "src/main/resources/imagenes/perfil.png";
        Path ruta = Paths.get(rutaPorDefecto);
        if (Files.exists(ruta)) {
            imageView.setImage(new Image(ruta.toUri().toString())); // Mostrar la imagen por defecto
        }
    }

    public void flechaatras() throws IOException {
        // Cerrar la ventana actual y volver a la gestión de usuarios
        cerrar();
        Gestion_usuarios();
    }

    public void cerrar() throws IOException {
        // Cerrar la ventana actual
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    public void cancelarFormulario() throws IOException {
        // Cancelar el formulario y volver a la gestión de usuarios
        Gestion_usuarios();
        Stage stage = (Stage) cancelar.getScene().getWindow();
        stage.close();
    }

    public void seleccionarFoto() {
        // Abrir un diálogo para seleccionar una imagen
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));

        fotoSeleccionada = fileChooser.showOpenDialog(null);

        if (fotoSeleccionada != null) {
            imageView.setImage(new Image(fotoSeleccionada.toURI().toString())); // Mostrar la imagen seleccionada
        }
    }

    private String guardarImagen(int idUsuario) {
        // Guardar la imagen seleccionada en la carpeta de imágenes
        if (fotoSeleccionada == null) {
            return null; // Si no se ha seleccionado ninguna imagen, devolver null
        }

        Path destinoCarpeta = Paths.get("src/main/resources/imagenes");

        // Asegurarse de que la carpeta existe
        if (!Files.exists(destinoCarpeta)) {
            try {
                Files.createDirectories(destinoCarpeta); // Crear la carpeta si no existe
            } catch (IOException e) {
                mostrarError("Error al crear directorio", "No se pudo crear el directorio para las imágenes."); // Mostrar error si no se puede crear la carpeta
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
            destinoArchivo = destinoCarpeta.resolve(nuevoNombre); // Modificar el nombre del archivo
            contador++;
        }

        try {
            Files.copy(fotoSeleccionada.toPath(), destinoArchivo); // Copiar la imagen a la carpeta de destino
            return "imagenes/" + destinoArchivo.getFileName().toString(); // Devolver la ruta relativa de la imagen
        } catch (IOException e) {
            mostrarError("Error al guardar imagen", "No se pudo guardar la imagen seleccionada."); // Mostrar error si no se puede guardar la imagen
            e.printStackTrace();
            return null;
        }
    }  

    public void mostrarError(String titulo, String mensaje) {
        // Mostrar un cuadro de diálogo de error
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public boolean validarCampos() {
        // Validar que todos los campos obligatorios estén completos
        if (nombre.getText().isEmpty() || apellidos.getText().isEmpty() || email.getText().isEmpty() ||
                username.getText().isEmpty() || password.getText().isEmpty() || telefono.getText().isEmpty() ||
                direccion.getText().isEmpty() || fechanacimiento.getValue() == null || tipo.getValue() == null || estado.getValue() == null) {
            mostrarError("Campos incompletos", "Por favor, complete todos los campos."); // Mostrar error si hay campos vacíos
            return false;
        }

        // Validar que el número de teléfono tenga 9 dígitos
        if (!Pattern.matches("\\d{9}", telefono.getText())) {
            mostrarError("Teléfono incorrecto", "El número de teléfono debe tener 9 dígitos."); // Mostrar error si el teléfono es incorrecto
            return false;
        }

        return true; // Todos los campos son válidos
    }

    public void guardar() throws IOException {
        // Guardar los cambios realizados en el usuario
        if (!validarCampos()) return; // Validar campos antes de guardar

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
            stmt.setString (10, estado.getValue());
            stmt.setString(11, imagenRuta); // Usar la ruta de la imagen anterior si no se seleccionó una nueva
            stmt.setInt(12, Gestion_usuarios.idtraspaso);
            stmt.executeUpdate(); // Ejecutar la actualización en la base de datos

            Stage stage = (Stage) guardar.getScene().getWindow();
            stage.close(); // Cerrar la ventana actual
            cerrar();
            Gestion_usuarios(); // Volver a la gestión de usuarios

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error SQL", "No se pudo guardar los cambios."); // Mostrar error si no se puede guardar
        }
    }

    public void Gestion_usuarios() throws IOException {
        // Cargar la vista de gestión de usuarios
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
        Pane registro = loader.load();
        Scene loginScene = new Scene(registro, 600, 500);
        loginScene.setFill(Color.TRANSPARENT);
        Stage loginStage = new Stage();
        loginStage.setResizable(false);
        loginStage.initStyle(StageStyle.DECORATED);
        loginStage.setScene(loginScene);
        loginStage.setTitle("Reservas");
        loginStage.show(); // Mostrar la nueva ventana
        cerrar(); // Cerrar la ventana actual
    }

    public void actualizar() throws IOException {
        // Actualizar los datos del usuario
        if (!validarCampos()) {
            return; // Validar campos antes de actualizar
        }

        int idUsuario = Gestion_usuarios.idtraspaso; // Obtener el ID del usuario a actualizar
        boolean esEmpleado = "Empleado".equals(tipo.getValue());
        boolean esAdministrador = "Administrador".equals(tipo.getValue());

        String rutaImagenGuardada = guardarImagen(idUsuario); // Guardar la imagen seleccionada

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            if (esEmpleado) {
                actualizarEmpleado(conexion, idUsuario, rutaImagenGuardada); // Actualizar datos del empleado
            } else {
                actualizarAdministrador(conexion, idUsuario, rutaImagenGuardada); // Actualizar datos del administrador
            }

            // Actualizar permisos después de la actualización
            actualizarPermisos(conexion, idUsuario);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Actualizado");
            alerta.setHeaderText(null);
            alerta.setContentText("El usuario ha sido actualizado correctamente."); // Mostrar mensaje de éxito
            alerta.showAndWait();

            cerrar(); // Cerrar la ventana actual
            Gestion_usuarios(); // Volver a la gestión de usuarios
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error SQL", "No se pudo actualizar el usuario: " + e.getMessage()); // Mostrar error si no se puede actualizar
        }
    }

    private void actualizarEmpleado(Connection conexion, int idUsuario, String rutaImagenGuardada) throws SQLException {
        // Actualizar los datos del empleado en la base de datos
        String sqlUpdate = "UPDATE empleados SET nombre = ?, apellido = ?, email = ?, username = ?, password = ?, telefono = ?, direccion = ?, estado = ?, fecha_nacimiento = ?, ruta = ?, posicion = ? WHERE id_empleado = ?";
        try (PreparedStatement stmtUpdate = conexion.prepareStatement(sqlUpdate)) {
            stmtUpdate.setString(1, nombre.getText());
            stmtUpdate.setString(2, apellidos.getText());
            stmtUpdate.setString(3, email.getText());
            stmtUpdate.setString(4, username.getText());
            stmtUpdate.setString(5, password.getText());
            stmtUpdate.setString(6, telefono.getText());
            stmtUpdate.setString(7, direccion.getText());
            stmtUpdate.setString(8, estado.getValue());
            stmtUpdate.setDate(9, java.sql.Date.valueOf(fechanacimiento.getValue()));
            stmtUpdate.setString(10, rutaImagenGuardada);
            stmtUpdate.setString(11, posicion.getText());
            stmtUpdate.setInt(12, idUsuario);
            
            int rowsAffected = stmtUpdate.executeUpdate(); // Ejecutar la actualización
            if (rowsAffected == 0) {
                throw new SQLException("No se actualizó ningún registro de empleado."); // Lanzar error si no se actualizó
            }
        }
    }

    private void actualizarAdministrador(Connection conexion, int idUsuario, String rutaImagenGuardada) throws SQLException {
        // Actualizar los datos del administrador en la base de datos
        String sqlUpdate = "UPDATE administradores SET nombre = ?, apellido = ?, email = ?, username = ?, password = ?, telefono = ?, direccion = ?, estado = ?, fecha_nacimiento = ?, ruta = ? WHERE id _admin = ?";
        try (PreparedStatement stmtUpdate = conexion.prepareStatement(sqlUpdate)) {
            stmtUpdate.setString(1, nombre.getText());
            stmtUpdate.setString(2, apellidos.getText());
            stmtUpdate.setString(3, email.getText());
            stmtUpdate.setString(4, username.getText());
            stmtUpdate.setString(5, password.getText());
            stmtUpdate.setString(6, telefono.getText());
            stmtUpdate.setString(7, direccion.getText());
            stmtUpdate.setString(8, estado.getValue());
            stmtUpdate.setDate(9, java.sql.Date.valueOf(fechanacimiento.getValue()));
            stmtUpdate.setString(10, rutaImagenGuardada);
            stmtUpdate.setInt(11, idUsuario);
            
            int rowsAffected = stmtUpdate.executeUpdate(); // Ejecutar la actualización
            if (rowsAffected == 0) {
                throw new SQLException("No se actualizó ningún registro de administrador."); // Lanzar error si no se actualizó
            }
        }
    }

    private void actualizarPermisos(Connection conexion, int idUsuario) throws SQLException {
        // Eliminar permisos existentes del usuario
        String sqlEliminarPermisos = "DELETE FROM permisos WHERE id_empleado = ?";
        try (PreparedStatement stmtEliminarPermisos = conexion.prepareStatement(sqlEliminarPermisos)) {
            stmtEliminarPermisos.setInt(1, idUsuario);
            stmtEliminarPermisos.executeUpdate(); // Ejecutar la eliminación de permisos
        }

        // Insertar nuevos permisos para el usuario
        String sqlInsertPermisos = "INSERT INTO permisos (id_empleado, id_modulo, lectura, escritura) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmtInsertPermisos = conexion.prepareStatement(sqlInsertPermisos)) {
            // Módulo 1
            stmtInsertPermisos.setInt(1, idUsuario);
            stmtInsertPermisos.setInt(2, 1); // ID del módulo 1
            stmtInsertPermisos.setInt(3, lectura_carta.isSelected() ? 1 : 0); // Establecer lectura
            stmtInsertPermisos.setInt(4, escritura_carta.isSelected() ? 1 : 0); // Establecer escritura
            stmtInsertPermisos.executeUpdate(); // Ejecutar la inserción de permisos

            // Módulo 2
            stmtInsertPermisos.setInt(1, idUsuario);
            stmtInsertPermisos.setInt(2, 2); // ID del módulo 2
            stmtInsertPermisos.setInt(3, lectura_reserva.isSelected() ? 1 : 0); // Establecer lectura
            stmtInsertPermisos.setInt(4, escritura_reserva.isSelected() ? 1 : 0); // Establecer escritura
            stmtInsertPermisos.executeUpdate(); // Ejecutar la inserción de permisos

            // Módulo 3
            stmtInsertPermisos.setInt(1, idUsuario);
            stmtInsertPermisos.setInt(2, 3); // ID del módulo 3
            stmtInsertPermisos.setInt(3, lectura_pedidos.isSelected() ? 1 : 0); // Establecer lectura
            stmtInsertPermisos.setInt(4, escritura_pedidos.isSelected() ? 1 : 0); // Establecer escritura
            stmtInsertPermisos.executeUpdate(); // Ejecutar la inserción de permisos
        }
    }
}