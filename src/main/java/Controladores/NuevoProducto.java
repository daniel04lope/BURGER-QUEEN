package Controladores;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Modelos.Producto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NuevoProducto { 
    
    @FXML
    private Button Cerrar; // Botón para cerrar la ventana
    
    @FXML
    private Button Guardar; // Botón para guardar el nuevo producto
    
    @FXML
    private Button Cancelar; // Botón para cancelar la operación
    
    @FXML
    private Button seleccionarImagenButton; // Botón para seleccionar una imagen
    
    @FXML
    private TextField nombreField; // Campo de texto para el nombre del producto
    
    @FXML
    private TextField precioField; // Campo de texto para el precio del producto
    
    @FXML
    private TextField categoriaField; // Campo de texto para la categoría del producto
    
    @FXML
    private TextField alergenosField; // Campo de texto para los alérgenos del producto
    
    @FXML
    private TextField pesoField; // Campo de texto para el peso del producto
    
    @FXML
    private TextArea descripcionArea; // Área de texto para la descripción del producto
    
    @FXML
    private ImageView imageView; // Vista de imagen para mostrar la imagen seleccionada
    
    private String rutaImagen; // Ruta de la imagen seleccionada
    
    @FXML
    public void initialize() {
        // Método de inicialización (actualmente vacío)
    }

    @FXML
    private void onSeleccionarImagen() {
        FileChooser fileChooser = new FileChooser(); // Crear un FileChooser para seleccionar imágenes
        fileChooser.setTitle("Seleccionar imagen");

        // Establecer el directorio inicial para el selector de archivos
        File directoriopordefecto = new File("/BURGER-QUEEN/src/main/resources");  
        if (directoriopordefecto.exists()) {
            fileChooser.setInitialDirectory(directoriopordefecto);
        }

        // Filtros para mostrar solo archivos de imagen
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg")
        );

        // Mostrar el diálogo de selección de archivos y obtener el archivo seleccionado
        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            // Limitar el tamaño máximo del archivo a 10 MB
            final long TAMANO_MAXIMO_MB = 10 * 1024 * 1024;
            if (archivoSeleccionado.length() > TAMANO_MAXIMO_MB) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Archivo demasiado grande");
                alert.setHeaderText(null);
                alert.setContentText("La imagen seleccionada supera el límite de 10 MB. Por favor, elige una imagen más pequeña.");
                alert.showAndWait();
                return; 
            }

            // Guardar la ruta de la imagen seleccionada
            rutaImagen = archivoSeleccionado.getAbsolutePath();
            System.out.println(rutaImagen);

            // Mostrar la imagen seleccionada en el ImageView
            imageView.setImage(new Image(archivoSeleccionado.toURI().toString()));
        }
    }

    public void Carta() throws IOException {
        // Cargar la vista de la carta
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
        Pane cartapane = cargador.load();
        Scene cartaScene = new Scene(cartapane, 600, 500);
        cartaScene.setFill(Color.TRANSPARENT);

        Stage cartaStage = new Stage(); // Crear una nueva ventana para la carta
        cartaStage.setResizable(false);
        cartaStage.initStyle(StageStyle.DECORATED);
        cartaStage.setScene(cartaScene);
        cartaStage.setTitle("CARTA");
        cartaStage.show();
        cerrar(); // Cerrar la ventana actual
    
    }
    
    @FXML
    private void onGuardar() {
        // Obtener los valores de los campos de texto
        String nombre = nombreField.getText().trim().toLowerCase();
        String precio = precioField.getText().trim().toLowerCase();
        String categoria = categoriaField.getText().trim().toLowerCase();
        String alergenos = alergenosField.getText().trim().toLowerCase();
        String peso = pesoField.getText().trim().toLowerCase();
        String descripcion = descripcionArea.getText().trim().toLowerCase();

        // Validar que todos los campos estén completos
        if (nombre.isEmpty() || precio.isEmpty() || categoria.isEmpty() || peso.isEmpty() || rutaImagen == null || descripcion.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos Vacíos", "Por favor, complete todos los campos.");
            return;
        }

        // Validar la longitud de los campos
        if (nombre.length() > 100 || descripcion.length() > 500 || categoria.length() > 50 || alergenos.length() > 100) {
            mostrarAlerta(AlertType.WARNING, "Texto demasiado largo", "Los campos tienen una longitud máxima: Nombre (100 caracteres), Descripción (500 caracteres), Categoría (50 caracteres), Alérgenos (100 caracteres).");
            return;
        }

        // Validar que el precio y el peso sean números válidos
        double precioDouble;
        double pesoDouble;
        try {
            precioDouble = Double.parseDouble(precio);
            pesoDouble = Double.parseDouble(peso);

            if (precioDouble <= 0 || pesoDouble <= 0) {
                mostrarAlerta(AlertType.WARNING, "Valores no válidos", "El precio y el peso deben ser valores positivos.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "Formato Incorrecto", "El precio y el peso deben ser números válidos.");
            return;
        }

        // Validar que la imagen seleccionada sea válida
        File archivoImagen = new File(rutaImagen);
        if (!archivoImagen.exists() || (!rutaImagen.endsWith(".jpg") && !rutaImagen.endsWith(".jpeg") && !rutaImagen.endsWith(".png"))) {
            mostrarAlerta(AlertType.WARNING, "Imagen no válida", "Seleccione una imagen válida en formato .jpg, .jpeg o .png.");
            return;
        }

        // Copiar la imagen a la carpeta de recursos
        String nombreArchivo = archivoImagen.getName();
        Path rutaDestino = Paths.get("src/main/resources", nombreArchivo);

        try {
            Files.copy(archivoImagen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error al copiar la imagen", "No se pudo copiar la imagen a la carpeta de recursos.");
            return;
        }

        // Crear un nuevo objeto Producto
        Producto nuevoProducto = new Producto(nombre, descripcion, precioDouble, categoria, alergenos, pesoDouble);
        nuevoProducto.setRuta(nombreArchivo);

        // Guardar el nuevo producto en la base de datos
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sql = "INSERT INTO carta (nombre, descripcion, precio, categoria, alergenos, peso, ruta) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, nuevoProducto.getNombre());
                stmt.setString(2, nuevoProducto.getDescripcion());
                stmt.setDouble(3, nuevoProducto.getPrecio());
                stmt.setString(4, nuevoProducto.getCategoria());
                stmt.setString(5, nuevoProducto.getAlergenos());
                stmt.setDouble(6, nuevoProducto.getPeso());
                stmt.setString(7, nuevoProducto.getRuta());

                // Ejecutar la consulta y verificar si se guardó correctamente
                int filasAfectadas = stmt.executeUpdate();

                if (filasAfectadas > 0) {
                    mostrarAlerta(AlertType.INFORMATION, "Guardado", "Producto guardado correctamente:\n" +
                            "Nombre: " + nuevoProducto.getNombre() + "\n" +
                            "Precio: " + nuevoProducto.getPrecio());
                    cerrar();
                    Carta(); // Mostrar la carta después de guardar
                } else {
                    mostrarAlerta(AlertType.ERROR, "Error", "No se pudo guardar el producto. Inténtelo de nuevo.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error en la base de datos", "Hubo un problema al guardar el producto.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error de conexión", "No se pudo conectar a la base de datos.");
        }
    }

    // Método para mostrar alertas
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para regresar a la carta
    public void flechaatras() throws IOException {
        cerrar(); // Cerrar la ventana actual
        Carta(); // Mostrar la carta
    }

    // Método para mostrar la pantalla principal
    public void Pantalla_Principal() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principalpane = cargador.load();
        
        Scene principalScene = new Scene(principalpane, 600, 500);
        principalScene.getStylesheets().add(getClass().getResource("Pantalla_Principal.css").toExternalForm());
        principalScene.setFill(Color.TRANSPARENT);
        
        Stage PrincipalStage = new Stage(); // Crear una nueva ventana para la pantalla principal
        PrincipalStage.setResizable(false);
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("PANTALLA PRINCIPAL");
        PrincipalStage.show();
        cerrar(); // Cerrar la ventana actual
    }
    
    // Método para cerrar la ventana
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }
}