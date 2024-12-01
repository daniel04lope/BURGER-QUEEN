package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class perfil implements Initializable {
    @FXML
    private Accordion administradores;
    @FXML
    private TitledPane titledpaneadmin;
    @FXML
    private VBox Vboxadmin;
    private boolean Panel_Visible = false;
    private boolean Cerrardesplegar = false;
    @FXML
    private Button reservaadmin;
    @FXML
    private Button menuadmin;
    @FXML
    private Button usuariosadmin;
    @FXML
    private Button pedidosadmin;
    @FXML
    Text Username;
    @FXML
    private AnchorPane Panel_Desplegable;
    @FXML
    private Button Desplegable;
    @FXML
    private Button Cerrar;
    @FXML
    private ImageView imagenperfil;
    @FXML
    private Button botoncarrito;
    @FXML
    private TextField txtnombre, txtapellido, txtemail, txtusername, txtpassword, txttelefono, txtdireccion;
    @FXML
    private DatePicker txtfechanacimiento;
    private String rutaImagenSeleccionada;
    boolean hayimagen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	
    
    	
        txtnombre.setText(Login.datos_login.getNombre());
        txtapellido.setText(Login.datos_login.getApellido());
        txtemail.setText(Login.datos_login.getEmail());
        txtusername.setText(Login.datos_login.getUsername());
        txtpassword.setText(Login.datos_login.getPassword());
        txttelefono.setText(Login.datos_login.getTelefono());
        txtdireccion.setText(Login.datos_login.getDireccion());
        txtfechanacimiento.setValue(Login.datos_login.getFechaNacimiento());

        if (Login.datos_login.getRuta() != null && !Login.datos_login.getRuta().isEmpty()) {
            try {
                String rutaImagen = "file:src/main/resources/imagenes/" + Login.datos_login.getRuta();
                Image imagen = new Image(rutaImagen);
                if (!imagen.isError()) {
                    imagenperfil.setImage(imagen);
                } else {
                    System.err.println("Error al cargar la imagen desde la ruta: " + rutaImagen);
                }
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró una ruta válida para la imagen.");
        }
    }
    public void Mostrar_Login() {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
            Pane login = cargador.load();
            Scene loginScene = new Scene(login, 450, 600);
            loginScene.setFill(Color.TRANSPARENT);
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);
            loginStage.setScene(loginScene);
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.setTitle("LOGIN");
            loginStage.show();
            cerrar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Reserva() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Reservas.fxml"));
        Pane reservapane = cargador.load();
        Scene reservaScene = new Scene(reservapane, 600, 500);
        reservaScene.setFill(Color.TRANSPARENT);
        Stage reservaStage = new Stage();
        reservaStage.setResizable(false);
        reservaStage.initStyle(StageStyle.DECORATED);
        reservaStage.setScene(reservaScene);
        reservaStage.setTitle("RESERVAS");
        reservaStage.show();
        cerrar();
    }
    public void Horarios() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Horarios.fxml"));
        Pane horariospane = cargador.load();
        Scene horariosScene = new Scene(horariospane, 600, 500);
        horariosScene.setFill(Color.TRANSPARENT);

        Stage horariosStage = new Stage();
        horariosStage.setResizable(false);
        horariosStage.initStyle(StageStyle.DECORATED);
        horariosStage.setScene(horariosScene);
        horariosStage.setTitle("HORARIOS");
        horariosStage.show();
        cerrar();
    }
    public void Ubicacion() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Ubicacion.fxml"));
        Pane ubicacionpane = cargador.load();
        Scene ubicacionScene = new Scene(ubicacionpane, 600, 500);
        ubicacionScene.setFill(Color.TRANSPARENT);

        Stage ubicacionStage = new Stage();
        ubicacionStage.setResizable(false);
        ubicacionStage.initStyle(StageStyle.DECORATED);
        ubicacionStage.setScene(ubicacionScene);
        ubicacionStage.setTitle("UBICACION");
        ubicacionStage.show();
        cerrar();
    }
    public void Gestion_usuarios() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Gestion_usuarios.fxml"));
        Pane gestiondeusuariopane = cargador.load();
        Scene gestiondeusuariosScene = new Scene(gestiondeusuariopane, 600, 500);
        gestiondeusuariosScene.setFill(Color.TRANSPARENT);
        Stage gestiondeusuariosStage = new Stage();
        gestiondeusuariosStage.setResizable(false);
        gestiondeusuariosStage.initStyle(StageStyle.DECORATED);
        gestiondeusuariosStage.setScene(gestiondeusuariosScene);
        gestiondeusuariosStage.setTitle("PANEL DE GESTION DE USUARIOS");
        gestiondeusuariosStage.show();
        cerrar();
    }
    
    
    
    public void flechaatras() throws IOException {
    	cerrar();
    	Pantalla_Principal();
    }




    public void Carta() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
        Pane cartapane = cargador.load();
        Scene cartaScene = new Scene(cartapane, 600, 500);
        cartaScene.setFill(Color.TRANSPARENT);

        Stage cartaStage = new Stage();
        cartaStage.setResizable(false);
        cartaStage.initStyle(StageStyle.DECORATED);
        cartaStage.setScene(cartaScene);
        cartaStage.setTitle("CARTA");
        cartaStage.show();
        cerrar();
    }


    public void guardar() throws SQLException {
        // Validaciones
        if (txtnombre.getText().isEmpty() || txtapellido.getText().isEmpty() || txtemail.getText().isEmpty() ||
            txtusername.getText().isEmpty() || txtpassword.getText().isEmpty() || txttelefono.getText().isEmpty() ||
            txtdireccion.getText().isEmpty() || txtfechanacimiento.getValue() == null) {
            mostrarAlerta("Error", "Por favor, complete todos los campos obligatorios.");
            return;
        }

        // Validación de formato de email
        if (!txtemail.getText().matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            mostrarAlerta("Error", "El formato del email es incorrecto.");
            return;
        }

        String nombre = txtnombre.getText();
        String apellidos = txtapellido.getText();
        String email = txtemail.getText();
        String username = txtusername.getText();
        String password = txtpassword.getText();
        String telefono = txttelefono.getText();
        String direccion = txtdireccion.getText();
        LocalDate fechaNacimiento = txtfechanacimiento.getValue();
        Date fechana = Date.valueOf(fechaNacimiento);

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sql;
            PreparedStatement sentencia;

            if (rutaImagenSeleccionada != null && !rutaImagenSeleccionada.isEmpty()) {
                sql = "UPDATE usuarios SET nombre = ?, apellido = ?, email = ?, username = ?, password = ?, telefono = ?, direccion = ?, fecha_nacimiento = ?, ruta = ? WHERE id_usuario = ?";
                sentencia = conexion.prepareStatement(sql);
                sentencia.setString(9, rutaImagenSeleccionada);
                hayimagen = true;
            } else {
                sql = "UPDATE usuarios SET nombre = ?, apellido = ?, email = ?, username = ?, password = ?, telefono = ?, direccion = ?, fecha_nacimiento = ? WHERE id_usuario = ?";
                sentencia = conexion.prepareStatement(sql);
                hayimagen = false;
            }

            sentencia.setString(1, nombre);
            sentencia.setString(2, apellidos);
            sentencia.setString(3, email);
            sentencia.setString(4, username);
            sentencia.setString(5, password);
            sentencia.setString(6, telefono);
            sentencia.setString(7, direccion);
            sentencia.setDate(8, fechana);

            if (hayimagen) {
                sentencia.setString(9, rutaImagenSeleccionada);
            }
            sentencia.setInt(10, Login.datos_login.getIdUsuario());

            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Éxito", "Datos actualizados correctamente.");
                if (rutaImagenSeleccionada != null && !rutaImagenSeleccionada.isEmpty()) {
                    Login.datos_login.setRuta(rutaImagenSeleccionada);
                }
            } else {
                mostrarAlerta("Error", "No se actualizaron registros.");
            }
        }
    }

    public void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File archivo = fileChooser.showOpenDialog(null);

        if (archivo != null) {
            Image imagen = new Image(archivo.toURI().toString());
            imagenperfil.setImage(imagen);

            Path carpetaDestino = Paths.get("src/main/resources/imagenes");
            String nombreArchivo = archivo.getName();
            Path archivoDestino = carpetaDestino.resolve(nombreArchivo);

            try {
                if (!Files.exists(carpetaDestino)) {
                    Files.createDirectories(carpetaDestino);
                }
                Files.copy(archivo.toPath(), archivoDestino, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Imagen guardada en: " + archivoDestino.toString());
                rutaImagenSeleccionada = nombreArchivo;
            } catch (IOException e) {
                System.err.println("Error al guardar la imagen: " + e.getMessage());
            }
        } else {
            System.out.println("No se seleccionó ninguna imagen.");
        }
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void Pantalla_Principal() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principal = cargador.load();
        Scene principalScene = new Scene(principal, 600, 500);
        principalScene.setFill(Color.TRANSPARENT);
        Stage PrincipalStage = new Stage();
        PrincipalStage.setResizable(false);
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("PANTALLA PRINCIPAL");
        PrincipalStage.show();
        cerrar();
    }

    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    public void Gestionpedidos() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/GestionPedidos.fxml"));
        Pane gestionpedidospane = cargador.load();
        Scene gestionpedidosScene = new Scene(gestionpedidospane, 600, 500);
        gestionpedidosScene.setFill(Color.TRANSPARENT);
        Stage gestionpedidosStage = new Stage();
        gestionpedidosStage.setResizable(false);
        gestionpedidosStage.initStyle(StageStyle.DECORATED);
        gestionpedidosStage.setScene(gestionpedidosScene);
        gestionpedidosStage.setTitle("PANEL DE GESTION DE PEDIDOS");
        gestionpedidosStage.show();
        cerrar();
    }

    public void Despliega() {
        Cerrardesplegar = !Cerrardesplegar;
        Panel_Visible = !Panel_Visible;
        Cerrar.setVisible(Cerrardesplegar);
        Panel_Desplegable.setVisible(Panel_Visible);
    }

    public void menuadmin() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/MenuAdmin.fxml"));
        AnchorPane menuAdmin = cargador.load();
        Scene menuAdminScene = new Scene(menuAdmin, 600, 500);
        menuAdminScene.setFill(Color.TRANSPARENT);
        Stage menuAdminStage = new Stage();
        menuAdminStage.setResizable(false);
        menuAdminStage.initStyle(StageStyle.DECORATED);
        menuAdminStage.setScene(menuAdminScene);
        menuAdminStage.setTitle("PANEL DE MENU ADMINISTRATIVO");
        menuAdminStage.show();
        cerrar();
    }

    public void UsuariosAdmin() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/UsuariosAdmin.fxml"));
        AnchorPane usuariosAdmin = cargador.load();
        Scene usuariosAdminScene = new Scene(usuariosAdmin, 600, 500);
        usuariosAdminScene.setFill(Color.TRANSPARENT);
        Stage usuariosAdminStage = new Stage();
        usuariosAdminStage.setResizable(false);
        usuariosAdminStage.initStyle(StageStyle.DECORATED);
        usuariosAdminStage.setScene(usuariosAdminScene);
        usuariosAdminStage.setTitle("PANEL DE USUARIOS ADMINISTRATIVOS");
        usuariosAdminStage.show();
        cerrar();
    }

    public void ReservaAdmin() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/ReservasAdmin.fxml"));
        AnchorPane reservasAdmin = cargador.load();
        Scene reservasAdminScene = new Scene(reservasAdmin, 600, 500);
        reservasAdminScene.setFill(Color.TRANSPARENT);
        Stage reservasAdminStage = new Stage();
        reservasAdminStage.setResizable(false);
        reservasAdminStage.initStyle(StageStyle.DECORATED);
        reservasAdminStage.setScene(reservasAdminScene);
        reservasAdminStage.setTitle("PANEL DE RESERVAS ADMINISTRATIVAS");
        reservasAdminStage.show();
        cerrar();
    }
    public void carrito() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carrito.fxml"));
        AnchorPane carritoPane = cargador.load();
        Scene carritoScene = new Scene(carritoPane, 600, 500);
        carritoScene.setFill(Color.TRANSPARENT);
        Stage carritoStage = new Stage();
        carritoStage.setResizable(false);
        carritoStage.initStyle(StageStyle.DECORATED);
        carritoStage.setScene(carritoScene);
        carritoStage.setTitle("CARRITO DE COMPRAS");
        carritoStage.show();
    }

}
