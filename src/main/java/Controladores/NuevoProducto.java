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
    
    
    
    
    private String rutaImagen; // Para almacenar la ruta del archivo seleccionado
    
    

    @FXML
    public void initialize() {
        // Inicialización de cualquier cosa si es necesario
    }

    
    
    
    
    @FXML
    private void onSeleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");

        File initialDirectory = new File("/BURGER-QUEEN/src/main/resources");  // ruta
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }

        // Filtro para tipos de archivos de imagen
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg")
        );

        // Mostrar el diálogo de selección de archivo
        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            // Verificar el tamaño del archivo (10 MB en bytes)
            final long TAMANO_MAXIMO_MB = 10 * 1024 * 1024;
            if (archivoSeleccionado.length() > TAMANO_MAXIMO_MB) {
                // Mostrar una alerta si el archivo es demasiado grande
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Archivo demasiado grande");
                alert.setHeaderText(null);
                alert.setContentText("La imagen seleccionada supera el límite de 10 MB. Por favor, elige una imagen más pequeña.");
                alert.showAndWait();
                return; // Salir del método si el archivo supera el tamaño permitido
            }

            // Obtener la ruta absoluta del archivo seleccionado
            rutaImagen = archivoSeleccionado.getAbsolutePath();
            System.out.println(rutaImagen);

            // Mostrar la imagen seleccionada en el ImageView
            imageView.setImage(new Image(archivoSeleccionado.toURI().toString()));
        }
    }


    
    @FXML
    private void onGuardar() {
        // Obtener los datos desde los campos de texto
        String nombre = nombreField.getText().trim().toLowerCase();
        String precio = precioField.getText().trim().toLowerCase();
        String categoria = categoriaField.getText().trim().toLowerCase();
        String alergenos = alergenosField.getText().trim().toLowerCase();
        String peso = pesoField.getText().trim().toLowerCase();
        String descripcion = descripcionArea.getText().trim().toLowerCase();

        // Validación de campos vacíos
        if (nombre.isEmpty() || precio.isEmpty() || categoria.isEmpty() || peso.isEmpty() || rutaImagen == null || descripcion.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos Vacíos", "Por favor, complete todos los campos.");
            return;
        }

        // Validación de longitudes de texto
        if (nombre.length() > 100 || descripcion.length() > 500 || categoria.length() > 50 || alergenos.length() > 100) {
            mostrarAlerta(AlertType.WARNING, "Texto demasiado largo", "Los campos tienen una longitud máxima:\n" +
                    "Nombre: 100 caracteres\n" +
                    "Descripción: 500 caracteres\n" +
                    "Categoría: 50 caracteres\n" +
                    "Alérgenos: 100 caracteres.");
            return;
        }

        // Validación de formato numérico para precio y peso
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

        // Validar la imagen seleccionada
        File archivoImagen = new File(rutaImagen);
        if (!archivoImagen.exists() || (!rutaImagen.endsWith(".jpg") && !rutaImagen.endsWith(".jpeg") && !rutaImagen.endsWith(".png"))) {
            mostrarAlerta(AlertType.WARNING, "Imagen no válida", "Seleccione una imagen válida en formato .jpg, .jpeg o .png.");
            return;
        }

        // Copiar la imagen al directorio de recursos
        String nombreArchivo = archivoImagen.getName();
        Path rutaDestino = Paths.get("src/main/resources", nombreArchivo);

        try {
            Files.copy(archivoImagen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error al copiar la imagen", "No se pudo copiar la imagen a la carpeta de recursos.");
            return;
        }

        // Crear un objeto Producto con los datos validados
        Producto nuevoProducto = new Producto(nombre, descripcion, precioDouble, categoria, alergenos, pesoDouble);
        nuevoProducto.setRuta(nombreArchivo);

        // Guardar el producto en la base de datos
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

                // Ejecutar la inserción
                int filasAfectadas = stmt.executeUpdate();

                if (filasAfectadas > 0) {
                    mostrarAlerta(AlertType.INFORMATION, "Guardado", "Producto guardado correctamente:\n" +
                            "Nombre: " + nuevoProducto.getNombre() + "\n" +
                            "Precio: " + nuevoProducto.getPrecio());
                    cerrar();
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

    // Método auxiliar para mostrar alertas
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

   



    @FXML
    private void onCancelar() {
        // Lógica para cancelar la acción y limpiar los campos si es necesario
        nombreField.clear();
        precioField.clear();
        categoriaField.clear();
        alergenosField.clear();
        pesoField.clear();
        descripcionArea.clear();
        imageView.setImage(null);
        rutaImagen = null;
        cerrar();
    }

    

    
    
    public void Pantalla_Principal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principal = loader.load();
        
        Scene principalScene = new Scene(principal, 600, 500);
        principalScene.getStylesheets().add(getClass().getResource("Pantalla_Principal.css").toExternalForm());
        principalScene.setFill(Color.TRANSPARENT);
        
        Stage PrincipalStage = new Stage();
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("CARTA");
        PrincipalStage.show();
        cerrar();
    }
    
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }
}
