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

    private Producto producto;
    private String rutaImagen;
    private String rutaImagenOriginal;

   
    public void setProducto(Producto producto) {
        this.producto = producto;
        cargarDatosProducto();
    }

    

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

            if (rutaImagen != null) {
                imageView.setImage(new Image(getClass().getResourceAsStream("/" + rutaImagen)));
            }
        }
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	if (Login.tipo.equals("administradores")) {
          
        }

        if (Login.tipo.equals("empleados")) {
            
            System.out.println("llegue");

            // Verificar permisos para cada botón
         

            try {
				if (permisos(1, "lectura") == 0) {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

         
        }

        if (Login.tipo.equals("usuarios")) {
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
            System.out.println(permisos(1, "lectura"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
    }
    
    public int permisos(int nombreModulo, String tipoPermiso) throws SQLException {
        String sql = "SELECT " + tipoPermiso + " FROM permisos WHERE id_empleado = ? AND id_modulo = ?";
        int valor = 0;

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

        return valor;
    }

    @FXML
    public void onSeleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");

        File initialDirectory = new File("/BURGER-QUEEN/src/main/resources");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg"));

        File archivoSeleccionado = fileChooser.showOpenDialog(null);
        if (archivoSeleccionado != null) {
            final long TAMANO_MAXIMO_MB = 10 * 1024 * 1024;
            if (archivoSeleccionado.length() > TAMANO_MAXIMO_MB) {
                mostrarAlerta(AlertType.WARNING, "Archivo demasiado grande", "La imagen supera el límite de 10 MB.");
                return;
            }

            rutaImagen = archivoSeleccionado.getAbsolutePath();
            imageView.setImage(new Image(archivoSeleccionado.toURI().toString()));
        }
    }

    public void onGuardar() {
       
        String nombre = nombreField.getText();
        String precio = precioField.getText();
        String categoria = categoriaField.getText();
        String alergenos = alergenosField.getText();
        String peso = pesoField.getText();
        String descripcion = descripcionArea.getText();

      
        if (nombre.isEmpty() || precio.isEmpty() || categoria.isEmpty() || peso.isEmpty() || descripcion.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos Vacíos", "Por favor, complete todos los campos obligatorios.");
            return;
        }

        
        if (nombre.length() > 100 || descripcion.length() > 500 || categoria.length() > 50 ) {
            mostrarAlerta(AlertType.WARNING, "Texto demasiado largo", "Los campos tienen una longitud máxima:\n" +
                    "- Nombre: 100 caracteres\n" +
                    "- Descripción: 500 caracteres\n" +
                    "- Categoría: 50 caracteres\n" );
            return;
        }

       
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

       
        String nombreArchivo;
        if (!rutaImagen.equals(rutaImagenOriginal)) { 
            File archivoImagen = new File(rutaImagen);

            if (!archivoImagen.exists() || (!rutaImagen.endsWith(".jpg") && !rutaImagen.endsWith(".jpeg") && !rutaImagen.endsWith(".png"))) {
                mostrarAlerta(AlertType.WARNING, "Imagen no válida", "Seleccione una imagen válida en formato .jpg, .jpeg o .png.");
                return;
            }

            Path rutaDestino = Paths.get("src/main/resources", archivoImagen.getName());
            try {
                Files.copy(archivoImagen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta(AlertType.ERROR, "Error al copiar imagen", "No se pudo copiar la imagen a la carpeta de recursos.");
                return;
            }

            nombreArchivo = archivoImagen.getName();
        } else {
            nombreArchivo = rutaImagenOriginal; 
        }

        
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precioDouble);
        producto.setCategoria(categoria);
        producto.setAlergenos(alergenos);
        producto.setPeso(pesoDouble);
        producto.setRuta(nombreArchivo);

        
        actualizarProductoEnBaseDatos();
    }

    public void actualizarProductoEnBaseDatos() {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sql = "UPDATE carta SET nombre = ?, descripcion = ?, precio = ?, categoria = ?, alergenos = ?, peso = ?, ruta = ? WHERE id_producto = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, producto.getNombre());
                stmt.setString(2, producto.getDescripcion());
                stmt.setDouble(3, producto.getPrecio());
                stmt.setString(4, producto.getCategoria());
                stmt.setString(5, producto.getAlergenos());
                stmt.setDouble(6, producto.getPeso());
                stmt.setString(7, producto.getRuta());
                stmt.setInt(8, producto.getIdProducto());

                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    mostrarAlerta(AlertType.INFORMATION, "Guardado", "Producto modificado correctamente.");
                    cerrar();
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

    @FXML
    public void onCancelar() {
        cerrar();
    }

   public void cerrar() {
        Stage stage = (Stage) Cancelar.getScene().getWindow();
        stage.close();
    }
   
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
       cerrar();
   }

   private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
	    Alert alert = new Alert(tipo);
	    alert.setTitle(titulo);
	    alert.setHeaderText(null);
	    alert.setContentText(mensaje);
	    alert.showAndWait();
	}




}
