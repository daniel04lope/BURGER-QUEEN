package Controladores;

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
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import Modelos.Administrador;
import Modelos.Empleado;

public class Nuevo_usuario implements Initializable {

    // Declaración de los elementos de la interfaz de usuario
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

    private File fotoSeleccionada; // Variable para almacenar la foto seleccionada

    // Método que se ejecuta al inicializar la interfaz
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Agregar opciones a los ComboBox de tipo y estado
        tipo.getItems().addAll("Empleado", "Administrador");
        estado.getItems().addAll("Activo", "Inactivo", "Suspendido");

        // Listener para manejar el cambio en el tipo de usuario
        tipo.setOnAction(event -> {
            boolean esAdministrador = "Administrador".equals(tipo.getValue());
            // Mostrar u ocultar campos según el tipo de usuario
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

    // Método para cerrar la ventana
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    // Método para seleccionar una foto del sistema de archivos
    public void seleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));
        fotoSeleccionada = fileChooser.showOpenDialog(null);
        if (fotoSeleccionada != null) {
            imageView.setImage(new javafx.scene.image.Image(fotoSeleccionada.toURI().toString())); // Mostrar la imagen seleccionada
        }
    }

    // Método para registrar un nuevo usuario
    public void registrar() throws IOException {
        if (!validarCampos()) { // Validar campos antes de continuar
            mostrarError("Error", "Verifique que todos los campos sean correctos.");
            return;
        }

        try {
            Connection conexion = util.Conexiones.dameConexion("burger-queen");
            String sql;
            boolean esEmpleado = "Empleado".equals(tipo.getValue());
            String rutaImagenGuardada = guardarImagen(); // Guardar la imagen seleccionada

            // Si la ruta de la imagen es null, mostrar error y salir
            if (rutaImagenGuardada == null) {
                mostrarError("Error", "Debe seleccionar una imagen para el usuario.");
                return;
            }

            // Crear un Administrador o Empleado dependiendo del tipo seleccionado
            if (esEmpleado) {
                Empleado empleado = new Empleado(
                    nombre.getText(),
                    apellidos.getText(),
                    email.getText(),
                    username.getText(),
                    password.getText(),
                    posicion.getText(),
                    estado.getValue(),
                    telefono.getText(),
                    direccion.getText(),
                    java.sql.Date.valueOf(fechanacimiento.getValue()),
                    lectura_reserva.isSelected(),
                    escritura_reserva.isSelected(),
                    lectura_pedidos.isSelected() && escritura_pedidos.isSelected()
                );

                
                // Insertar el empleado en la base de datos
                sql = "INSERT INTO empleados (nombre, apellido, email, username, password, telefono, direccion, estado, fecha_nacimiento, posicion, ruta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, empleado.getNombre());
                pst.setString(2, empleado.getApellido());
                pst.setString(3, empleado.getEmail());
                pst.setString(4, empleado.getUsername());
                pst.setString(5, empleado.getPassword());
                pst.setString(6, empleado.getTelefono());
                pst.setString(7, empleado.getDireccion());
                pst.setString(8, empleado.getEstado());
                pst.setDate(9, new java.sql.Date(empleado.getFechaNacimiento().getTime()));
                pst.setString(10, empleado.getPosicion());
                pst.setString(11, rutaImagenGuardada);
                pst.executeUpdate();

                // Obtener el ID del nuevo empleado
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    int idEmpleado = rs.getInt(1);
                    guardarPermisos(conexion, idEmpleado); // Guardar permisos para el empleado
                }
            } else {
                Administrador admin = new Administrador();
                admin.setNombre(nombre.getText());
                admin.setApellido(apellidos.getText());
                admin.setEmail(email.getText());
                admin.setUsername(username.getText());
                admin.setPassword(password.getText());
                admin.setTelefono(telefono.getText());
                admin.setDireccion(direccion.getText());
                admin.setEstado(estado.getValue());
                admin.setFechaNacimiento(java.sql.Date.valueOf(fechanacimiento.getValue()));
                admin.setRuta(rutaImagenGuardada);

                // Insertar el administrador en la base de datos
                sql = "INSERT INTO administradores (nombre, apellido, email, username, password, telefono, direccion, estado, fecha_nacimiento, ruta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, admin.getNombre());
                pst.setString(2, admin.getApellido());
                pst.setString(3, admin.getEmail());
                pst.setString(4, admin.getUsername());
                pst.setString(5, admin.getPassword());
                pst.setString(6, admin.getTelefono());
                pst.setString(7, admin.getDireccion());
                pst.setString(8, admin.getEstado());
                pst.setDate(9, new java.sql.Date(admin.getFechaNacimiento().getTime()));
                pst.setString(10, admin.getRuta());
                pst.executeUpdate();

                // Obtener el ID del nuevo administrador
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    int idAdmin = rs.getInt(1);
                    guardarPermisos(conexion, idAdmin); // Guardar permisos para el administrador
                }
            }

            conexion.close();
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("CREADO");
            alerta.setHeaderText(null);
            alerta.setContentText("El usuario ha sido creado correctamente.");
            alerta.showAndWait();
            cerrar(); // Cerrar la ventana actual
            Gestion_usuarios(); // Volver a la pantalla de gestión de usuarios

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error SQL", "No se pudo registrar al usuario en la base de datos.");
        }
    }

    // Método para guardar la imagen seleccionada en el sistema de archivos
    private String guardarImagen() {
        if (fotoSeleccionada == null) {
            return null; // Retornar null si no se ha seleccionado una imagen
        }

        Path destinoCarpeta = Paths.get("src/main/resources/imagenes");

        // Asegurarse de que la carpeta existe
        if (!Files.exists(destinoCarpeta)) {
            try {
                Files.createDirectories(destinoCarpeta); // Crear la carpeta si no existe
            } catch (IOException e) {
                mostrarError("Error al crear carpeta", "No se pudo crear la carpeta de destino para las imágenes.");
                e.printStackTrace();
                return null; // Retornar null si hubo un error al crear la carpeta
            }
        }

        // Generar un nombre único para la imagen
        String extension = "";
        String nombreOriginal = fotoSeleccionada.getName();
        int puntoIndex = nombreOriginal.lastIndexOf('.');
        if (puntoIndex > 0 && puntoIndex < nombreOriginal.length() - 1) {
            extension = nombreOriginal.substring(puntoIndex); // Obtener la extensión del archivo
        }

        String nombreUnico = "imagen_" + System.currentTimeMillis() + extension; // Crear un nombre único
        Path destinoArchivo = destinoCarpeta.resolve(nombreUnico); // Ruta del archivo destino

        try {
            Files.copy(fotoSeleccionada.toPath(), destinoArchivo); // Copiar la imagen a la carpeta destino
            return nombreUnico; // Retornar el nombre único de la imagen guardada
        } catch (IOException e) {
            mostrarError("Error al guardar imagen", "No se pudo guardar la imagen seleccionada.");
            e.printStackTrace();
            return null; // Retornar null si hubo un error al guardar la imagen
        }
    }

    // Método para guardar los permisos del usuario en la base de datos
    private void guardarPermisos(Connection conexion, int idEmpleado) throws SQLException {
        String insertarPermisosSql = "INSERT INTO permisos (id_empleado, id_modulo, lectura, escritura) VALUES (?, ?, ?, ?)";
        PreparedStatement pstPermisos = conexion.prepareStatement(insertarPermisosSql);

        // Asignar permisos según los checkboxes seleccionados
        int lecturaReserva = lectura_reserva.isSelected() ? 1 : 0;
        int escrituraReserva = escritura_reserva.isSelected() ? 1 : 0;
        int lecturaPedidos = lectura_pedidos.isSelected() ? 1 : 0;
        int escrituraPedidos = escritura_pedidos.isSelected() ? 1 : 0;
        int lecturaCarta = lectura_carta.isSelected() ? 1 : 0;
        int escrituraCarta = escritura_carta.isSelected() ? 1 : 0;

        // Obtener los IDs de los módulos
        String obtenerIdsModulosSql = "SELECT id_modulo FROM modulos";
        PreparedStatement pstObtenerIdsModulos = conexion.prepareStatement(obtenerIdsModulosSql);
        ResultSet rsModulos = pstObtenerIdsModulos.executeQuery();

        while (rsModulos.next()) {
            int idModulo = rsModulos.getInt("id_modulo");

            // Configurar los valores del prepared statement según el idModulo
            pstPermisos.setInt(1, idEmpleado);
            pstPermisos.setInt(2, idModulo);
            pstPermisos.setInt(3, idModulo == 1 ? lecturaReserva : (idModulo == 2 ? lecturaPedidos : lecturaCarta));
            pstPermisos.setInt(4, idModulo == 1 ? escrituraReserva : (idModulo == 2 ? escrituraPedidos : escrituraCarta));

            // Insertar los permisos en la tabla permisos
            pstPermisos.addBatch(); // Agregar a batch
        }

        // Ejecutar todas las inserciones en batch
        pstPermisos.executeBatch();
    }

    // Método para cargar la pantalla de gestión de usuarios
    public void Gestion_usuarios() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
        Pane gestiondeusuariospane = cargador.load();
        Scene gestiondeusuariosScene = new Scene(gestiondeusuariospane, 600, 500);
        gestiondeusuariosScene.setFill(Color.TRANSPARENT);
        Stage gestiondeusuariosStage = new Stage();
        gestiondeusuariosStage.setResizable(false);
        gestiondeusuariosStage.initStyle(StageStyle.DECORATED);
        gestiondeusuariosStage.setScene(gestiondeusuariosScene);
        gestiondeusuariosStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        gestiondeusuariosStage.setTitle("PANEL DE GESTION DE USUARIOS");
        gestiondeusuariosStage.show();
        cerrar(); // Cerrar la ventana actual
    }

    // Método para manejar la acción de flecha atrás
    public void flechaatras() throws IOException {
        cerrar(); // Cerrar la ventana actual
        Gestion_usuarios(); // Volver a la pantalla de gestión de usuarios
    }

    // Método para validar los campos de entrada
    public boolean validarCampos() {
        if (nombre.getText().isEmpty() || apellidos.getText().isEmpty() || email.getText().isEmpty() ||
                username.getText().isEmpty() || password.getText().isEmpty() || telefono.getText().isEmpty() ||
                direccion.getText().isEmpty() || fechanacimiento.getValue() == null || tipo.getValue() == null || estado.getValue() == null) {
            mostrarError("Error", "Hay campos necesarios vacíos.");
            return false; // Retornar false si hay campos vacíos
        }

        if (fotoSeleccionada == null) {
            mostrarError("Error", "Debe seleccionar una imagen para el usuario.");
            return false; // Retornar false si no se ha seleccionado una imagen
        }

        if (!Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_ +&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$").matcher(email.getText()).matches()) {
            mostrarError("Error", "Correo electrónico inválido. Ejemplo: prueba@prueba.com");
            return false; // Retornar false si el correo no es válido
        }

        if (password.getText().length() < 6) {
            mostrarError("Error", "La contraseña debe tener al menos 6 caracteres.");
            return false; // Retornar false si la contraseña es demasiado corta
        }

        if ("Empleado".equals(tipo.getValue()) && posicion.getText().isEmpty()) {
            mostrarError("Error", "El campo posición es obligatorio para empleados.");
            return false; // Retornar false si el campo posición está vacío para empleados
        }

        return true; // Retornar true si todos los campos son válidos
    }

    // Método para mostrar un mensaje de error
    public void mostrarError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait(); // Mostrar la alerta y esperar a que el usuario la cierre
    }

    // Método para cancelar el formulario y volver a la gestión de usuarios
    public void cancelarFormulario() throws IOException {
        Stage stage = (Stage) cancelar.getScene().getWindow();
        stage.close(); // Cerrar la ventana actual
        Gestion_usuarios(); // Volver a la pantalla de gestión de usuarios
    }
}