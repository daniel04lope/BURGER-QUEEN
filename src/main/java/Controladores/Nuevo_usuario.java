package Controladores;

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

    public void registrar() throws IOException {
        if (!validarCampos()) {
            mostrarError("Error", "Verifique que todos los campos sean correctos.");
            return;
        }

        try {
            Connection conexion = util.Conexiones.dameConexion("burger-queen");
            String sql;
            boolean esEmpleado = "Empleado".equals(tipo.getValue());
            String rutaImagenGuardada = guardarImagen();

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
            cerrar();
            Gestion_usuarios();

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

        // Asegurarse de que la carpeta existe
        if (!Files.exists(destinoCarpeta)) {
            try {
                Files.createDirectories(destinoCarpeta);
            } catch (IOException e) {
                mostrarError("Error al crear carpeta", "No se pudo crear la carpeta de destino para las imágenes.");
                e.printStackTrace();
                return null;
            }
        }

        // Generar un nombre único para la imagen
        String extension = "";
        String nombreOriginal = fotoSeleccionada.getName();
        int puntoIndex = nombreOriginal.lastIndexOf('.');
        if (puntoIndex > 0 && puntoIndex < nombreOriginal.length() - 1) {
            extension = nombreOriginal.substring(puntoIndex);
        }

        String nombreUnico = "imagen_" + System.currentTimeMillis() + extension;
        Path destinoArchivo = destinoCarpeta.resolve(nombreUnico);

        try {
            Files.copy(fotoSeleccionada.toPath(), destinoArchivo);
            return "src/main/resources/" + nombreUnico;
        } catch (IOException e) {
            mostrarError("Error al guardar imagen", "No se pudo guardar la imagen seleccionada.");
            e.printStackTrace();
            return null;
        }
    }

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
            pstPermisos.addBatch();
        }

        // Ejecutar todas las inserciones en batch
        pstPermisos.executeBatch();
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

    public void flechaatras() throws IOException {
        cerrar();
        Gestion_usuarios();
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

    public void mostrarError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void cancelarFormulario() throws IOException {
        Stage stage = (Stage) cancelar.getScene().getWindow();
        stage.close();
        Gestion_usuarios();
    }
}