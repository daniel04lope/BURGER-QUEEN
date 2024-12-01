package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Modelos.Producto;

public class ModificarProducto implements Initializable {

    // Declaración de los componentes de la interfaz de usuario
    @FXML
    private Button Cerrar;
    
    @FXML
    private Button Guardar;
    
    @FXML
    private Button Cancelar;
    
    @FXML
    private Button seleccionarImagenButton;
    
    @FXML
    private TextField nombreField;
    
    @FXML
    private TextField precioField;
    
    @FXML
    private TextField categoriaField;
    
    @FXML
    private TextField alergenosField;
    
    @FXML
    private TextField pesoField;
    
    @FXML
    private TextArea descripcionArea;
    
    @FXML
    private ImageView imageView;

    // Variables para almacenar el producto y las rutas de la imagen
    private Producto producto;
    private String rutaImagen;
    private String rutaImagenOriginal;

    // Método para establecer el producto a modificar
    public void setProducto(Producto producto) {
        this.producto = producto;
        cargarDatosProducto(); // Cargar los datos del producto en la interfaz
    }

    // Método para cargar los datos del producto en los campos de texto
    public void cargarDatosProducto() {
        if (producto != null) {
            nombreField.setText(producto.getNombre());
            precioField.setText(String.valueOf(producto.getPrecio()));
            categoriaField.setText(producto.getCategoria());
            alergenosField.setText(producto.getAlergenos());
            pesoField.setText(String.valueOf(producto.getPeso()));
            descripcionArea.setText(producto.getDescripcion());
            rutaImagen = producto.getRuta();
            rutaImagenOriginal = rutaImagen; 

            // Cargar la imagen del producto en el ImageView
            if (rutaImagen != null) {
                imageView.setImage(new Image(getClass().getResourceAsStream("/" + rutaImagen)));
            }
        }
    }
    
    // Método que se ejecuta al inicializar la interfaz
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Verificar el tipo de usuario y establecer permisos
        if (Login.tipo.equals("administradores")) {
            // Los administradores tienen acceso completo
        }

        if (Login.tipo.equals("empleados")) {
            System.out.println("llegue");

            // Verificar permisos para cada botón
            try {
                if (permisos(1, "lectura") == 0) {
                    // Si no tiene permiso de lectura, deshabilitar campos
                    nombreField.setEditable(false);
                    precioField.setEditable(false);
                    categoriaField.setEditable(false);
                    alergenosField.setEditable(false);
                    pesoField.setEditable(false);
                    descripcionArea.setEditable(false);
                    seleccionarImagenButton.setDisable(true);
                    Guardar.setDisable(true);
                    Cancelar.setDisable(true);
                } else {
                    // Si tiene permiso, habilitar campos
                    nombreField.setEditable(true);
                    precioField.setEditable(true);
                    categoriaField.setEditable(true);
                    alergenosField.setEditable(true);
                    pesoField.setEditable(true);
                    descripcionArea.setEditable(true);
                    seleccionarImagenButton.setDisable(false);
                    Guardar.setDisable(false);
                    Cancelar.setDisable(false);
                }
            } catch (SQLException e) {
                // Manejo de excepciones de SQL
                e.printStackTrace();
            }
        }

        if (Login.tipo.equals("usuarios")) {
            // Los usuarios no tienen permisos para editar
            nombreField.setEditable(false);
            precioField.setEditable(false);
            categoriaField.setEditable(false);
            alergenosField.setEditable(false);
            pesoField.setEditable(false);
            descripcionArea.setEditable(false);
            seleccionarImagenButton.setDisable(true);
            Guardar.setDisable(true);
            Cancelar.setDisable(true);
        }

        try {
            // Imprimir permisos de lectura para depuración
            System.out.println(permisos (1, "lectura"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Método para verificar permisos de un usuario en un módulo específico
    public int permisos(int nombreModulo, String tipoPermiso) throws SQLException {
        String sql = "SELECT " + tipoPermiso + " FROM permisos WHERE id_empleado = ? AND id_modulo = ?";
        int valor = 0;

        // Conexión a la base de datos
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, Login.datos_login.getIdUsuario());
            sentencia.setInt(2, nombreModulo);
            
            System.out.println("Cadena: " + sentencia);
            
            ResultSet ejecuta = sentencia.executeQuery();

            if (ejecuta.next()) {
                valor = ejecuta.getInt(tipoPermiso);
                System.out.println("Valor: " + valor);
            } else {
                System.out.println("No valor encontrado id_empleado = " + Login.datos_login.getIdUsuario() + " and id_modulo = " + nombreModulo);
            }
        }

        return valor; // Retornar el valor del permiso
    }

    // Método para seleccionar una imagen desde el sistema de archivos
    @FXML
    public void onSeleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");

        // Establecer el directorio inicial para el selector de archivos
        File initialDirectory = new File("/BURGER-QUEEN/src/main/resources");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }

        // Filtros para tipos de archivos de imagen
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg"));

        // Mostrar el diálogo para seleccionar un archivo
        File archivoSeleccionado = fileChooser.showOpenDialog(null);
        if (archivoSeleccionado != null) {
            final long TAMANO_MAXIMO_MB = 10 * 1024 * 1024; // Tamaño máximo de 10 MB
            if (archivoSeleccionado.length() > TAMANO_MAXIMO_MB) {
                mostrarAlerta(AlertType.WARNING, "Archivo demasiado grande", "La imagen supera el límite de 10 MB.");
                return;
            }

            // Establecer la ruta de la imagen seleccionada y mostrarla
            rutaImagen = archivoSeleccionado.getAbsolutePath();
            imageView.setImage(new Image(archivoSeleccionado.toURI().toString()));
        }
    }

    // Método para guardar los cambios realizados en el producto
    public void onGuardar() {
        String nombre = nombreField.getText();
        String precio = precioField.getText();
        String categoria = categoriaField.getText();
        String alergenos = alergenosField.getText();
        String peso = pesoField.getText();
        String descripcion = descripcionArea.getText();

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || precio.isEmpty() || categoria.isEmpty() || peso.isEmpty() || descripcion.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos Vacíos", "Por favor, complete todos los campos obligatorios.");
            return;
        }

        // Validar la longitud de los campos
        if (nombre.length() > 100 || descripcion.length() > 500 || categoria.length() > 50) {
            mostrarAlerta(AlertType.WARNING, "Texto demasiado largo", "Los campos tienen una longitud máxima:\n" +
                    "- Nombre: 100 caracteres\n" +
                    "- Descripción: 500 caracteres\n" +
                    "- Categoría: 50 caracteres\n");
            return;
        }

        double precioDouble;
        double pesoDouble;
        try {
            // Convertir los valores de precio y peso a tipo double
            precioDouble = Double.parseDouble(precio);
            pesoDouble = Double.parseDouble(peso);

            // Validar que los valores sean positivos
            if (precioDouble <= 0 || pesoDouble <= 0) {
                mostrarAlerta(AlertType.WARNING, "Valores no válidos", "El precio y el peso deben ser valores positivos.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "Formato Incorrecto", "El precio y el peso deben ser números válidos.");
            return;
        }

        String nombreArchivo;
        // Verificar si se ha cambiado la imagen
        if (!rutaImagen.equals(rutaImagenOriginal)) { 
            File archivoImagen = new File(rutaImagen);

            // Validar que el archivo de imagen sea válido
            if (!archivoImagen.exists ()) {
                mostrarAlerta(AlertType.WARNING, "Imagen no válida", "Seleccione una imagen válida en formato .jpg, .jpeg o .png.");
                return;
            }

            Path rutaDestino = Paths.get("src/main/resources", archivoImagen.getName());
            try {
                // Copiar la imagen a la carpeta de recursos
                Files.copy(archivoImagen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta(AlertType.ERROR, "Error al copiar imagen", "No se pudo copiar la imagen a la carpeta de recursos.");
                return;
            }

            nombreArchivo = archivoImagen.getName(); // Obtener el nombre del archivo
        } else {
            nombreArchivo = rutaImagenOriginal; // Mantener el nombre original si no se cambió la imagen
        }

        // Actualizar los datos del producto
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precioDouble);
        producto.setCategoria(categoria);
        producto.setAlergenos(alergenos);
        producto.setPeso(pesoDouble);
        producto.setRuta(nombreArchivo);

        // Llamar al método para actualizar el producto en la base de datos
        actualizarProductoEnBaseDatos();
    }

    // Método para actualizar el producto en la base de datos
    public void actualizarProductoEnBaseDatos() {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sql = "UPDATE carta SET nombre = ?, descripcion = ?, precio = ?, categoria = ?, alergenos = ?, peso = ?, ruta = ? WHERE id_producto = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                // Establecer los parámetros de la consulta
                stmt.setString(1, producto.getNombre());
                stmt.setString(2, producto.getDescripcion());
                stmt.setDouble(3, producto.getPrecio());
                stmt.setString(4, producto.getCategoria());
                stmt.setString(5, producto.getAlergenos());
                stmt.setDouble(6, producto.getPeso());
                stmt.setString(7, producto.getRuta());
                stmt.setInt(8, producto.getIdProducto());

                int filasAfectadas = stmt.executeUpdate(); // Ejecutar la actualización
                if (filasAfectadas > 0) {
                    mostrarAlerta(AlertType.INFORMATION, "Guardado", "Producto modificado correctamente.");
                    cerrar(); // Cerrar la ventana si se guardó correctamente
                } else {
                    mostrarAlerta(AlertType.ERROR, "Error", "No se pudo actualizar el producto.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error en la base de datos", "Hubo un problema al actualizar el producto.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error de conexión", "No se pudo conectar a la base de datos.");
        }
    }

    // Método para cancelar la operación y cerrar la ventana
    @FXML
    public void onCancelar() {
        cerrar();
    }

    // Método para cerrar la ventana actual
    public void cerrar() {
        Stage stage = (Stage) Cancelar.getScene().getWindow();
        stage.close();
    }
   
    // Método para cargar la pantalla principal
    public void Pantalla_Principal() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principal = cargador.load();
       
        Scene principalScene = new Scene(principal, 600, 500);
        principalScene.getStylesheets().add(getClass().getResource("Pantalla_Principal.css").toExternalForm());
        principalScene.setFill(Color.TRANSPARENT);
       
        Stage PrincipalStage = new Stage();
        PrincipalStage.setResizable(false);
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("PANTALLA PRINCIPAL");
        PrincipalStage.show();
        cerrar(); // Cerrar la ventana actual
    }

    // Método para mostrar alertas al usuario
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait(); // Mostrar la alerta y esperar a que el usuario la cierre
    }
}