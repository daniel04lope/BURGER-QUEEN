package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import Modelos.Producto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ItemFocus implements Initializable {
    @FXML
    private AnchorPane Panel_Desplegable; // Panel desplegable para opciones adicionales
    @FXML
    private TabPane tabla; // TabPane que contiene diferentes pestañas para el producto

    @FXML
    private Button Desplegable; // Botón para desplegar el panel
    @FXML
    private Button Cerrar; // Botón para cerrar la ventana
    @FXML
    private Button Modificar; // Botón para modificar el producto
    @FXML
    private Button Eliminar; // Botón para eliminar el producto
    @FXML
    private ImageView imagen; // Imagen del producto
    @FXML
    private Label Nombre; // Label para mostrar el nombre del producto
    @FXML
    private Label Descripcion; // Label para mostrar la descripción del producto
    @FXML
    private Tab DescripcionTab; // Pestaña de descripción del producto
    @FXML
    private Button Carrito; // Botón para añadir el producto al carrito
    @FXML
    private RadioButton Pclasica; // Opción de extra: pizza clásica
    @FXML
    private RadioButton Psupreme; // Opción de extra: pizza supreme
    @FXML
    private RadioButton Acebolla; // Opción de extra: cebolla
    @FXML
    private RadioButton Npollo; // Opción de extra: pollo
    @FXML
    private RadioButton Cocacola; // Opción de bebida: Coca-Cola
    @FXML
    private RadioButton Fanta; // Opción de bebida: Fanta
    @FXML
    private RadioButton Nestea; // Opción de bebida: Nestea
    @FXML
    private RadioButton CcZero; // Opción de bebida: Coca-Cola Zero

    private boolean drawerVisible = false; // Estado de visibilidad del panel desplegable
    private boolean Cerrardesplegar = false; // Estado de visibilidad del botón de cerrar

    private Producto producto; // Objeto que representa el producto
    private double precioBase; // Precio base del producto
    private double precioTotal; // Precio total del producto (incluyendo extras)
    private int cantExtras; // Cantidad de extras seleccionados
    static String tipoExtra; // Tipo de extra seleccionado

    private Producto complemento; // Objeto que representa un complemento
    private Producto bebida; // Objeto que representa una bebida

    public void Despliega() {
        // Método para desplegar u ocultar el panel de opciones
        System.out.println("Funciona");
        Cerrardesplegar = !Cerrardesplegar;
        drawerVisible = !drawerVisible;
        Cerrar.setVisible(Cerrardesplegar);
        Panel_Desplegable.setVisible(drawerVisible);
    }

    public void cerrar() {
        // Método para cerrar la ventana actual
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }

    public void Pantalla_Principal() throws IOException {
        // Método para abrir la ventana de la pantalla principal
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principal = cargador.load();

        Scene principalScene = new Scene(principal, 600, 500);
        principalScene.getStylesheets().add(getClass().getResource("Pantalla_Principal.css").toExternalForm());
        principalScene.setFill(Color.TRANSPARENT);

        Stage PrincipalStage = new Stage();
        PrincipalStage.setResizable(false);
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("PANTALLA PRINCIPAL");
        PrincipalStage.show();
        cerrar(); // Cerrar la ventana actual
    }

    public void flechaatras() throws IOException {
        // Método para volver a la carta
        cerrar();
 // Llama al método Carta para mostrar la vista de la carta
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Método que se ejecuta al inicializar la clase
        if (producto != null) {
            cargarProducto(); // Cargar los detalles del producto si está disponible
        }

        // Configuración de visibilidad de botones según el tipo de usuario
        if (Login.tipo.equals("administradores")) {
            Modificar.setVisible(true);
            Eliminar.setVisible(true);
            Carrito.setDisable(true);
        }
        if (Login.tipo.equals("empleados")) {
            Carrito.setDisable(true);
            try {
                if (permisos(1, "escritura") == 1) {
                    Modificar.setVisible(true);
                    Eliminar.setVisible(true);
                } else {
                    Modificar.setVisible(false);
                    Eliminar.setVisible(false);
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Imprimir error si hay problemas con la base de datos
            }
        }
    }

    public void setProducto(Producto producto) {
        // Método para establecer el producto y cargar sus detalles
        this.producto = producto;
        this.precioBase = producto.getPrecio();
        this.precioTotal = precioBase;
        cargarProducto(); // Cargar los detalles del producto
    }

    public int permisos(int nombreModulo, String tipoPermiso) throws SQLException {
        // Método para verificar los permisos del usuario en un módulo específico
        String sql = "SELECT " + tipoPermiso + " FROM permisos WHERE id_empleado = ?";
        int valor = 0;

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, Login.datos_login.getIdUsuario());
            ResultSet ejecuta = sentencia.executeQuery();

            if (ejecuta.next()) {
                valor = ejecuta.getInt(tipoPermiso); // Obtener el valor del permiso
            } else {
                System.out.println("No valor encontrado id_empleado = " + Login.datos_login.getIdUsuario());
            }
        }

        return valor; // Retornar el valor del permiso
    }

    private void cargarProducto() {
        // Método para cargar los detalles del producto en la interfaz
        if (producto != null) {
            Nombre.setText(producto.getNombre());
            Nombre.setStyle("-fx-font-family:Insaniburger");
            Descripcion.setText(producto.getDescripcion() +"\n"+ producto.getAlergenos());
            Descripcion.setStyle("-fx-font-family:Insaniburger");
            DescripcionTab.setText(producto.getNombre());
            Carrito.setText("Añadir a mi pedido - " + String.format("%.2f €", precioTotal));
            imagen.setImage(new Image(getClass().getResourceAsStream("/" + producto.getRuta())));
        }
        if ("Bebida".equals(producto.getCategoria())) {
            tabla.getTabs().remove(DescripcionTab); // Eliminar la pestaña de descripción si es una bebida
        }
    }

    public void carrito() throws IOException {
        // Método para abrir la ventana del carrito
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carrito.fxml"));
            AnchorPane carritoPane = cargador.load();
            Stage carritoStage = new Stage();
            carritoStage.initStyle(StageStyle.TRANSPARENT);
            carritoStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(carritoPane, 800, 623);
            carritoStage.setScene(scene);
            carritoStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
            carritoStage.setTitle("CARRITO");
            carritoStage.show(); // Mostrar la ventana del carrito
        } catch (IOException e) {
            e.printStackTrace(); // Imprimir error si no se puede cargar la vista
        }
    }

    @FXML
    private void aniadirextra1_20(ActionEvent event) {
        // Método para añadir un extra de queso y bacon
        if (cantExtras < 1) {
            precioTotal += 1.20; // Incrementar el precio total
            cantExtras++;
            tipoExtra = " + Extra queso + bacon"; // Actualizar el tipo de extra
            Carrito.setText("Añadir a mi pedido - " + String.format("%.2f €", precioTotal));
        }
    }

    public void complemento(ActionEvent event) throws SQLException {
        // Método para seleccionar un complemento
        if (complemento == null) {
            complemento = new Producto(); // Inicializar el objeto complemento
        }

        try (Connection conexioncomplementos = util.Conexiones.dameConexion("burger-queen")) {
            PreparedStatement sentenciacomplementos = conexioncomplementos.prepareStatement("SELECT id_producto, nombre, descripcion, precio, categoria, peso FROM carta WHERE id_producto = ?");
            ResultSet ejecutar = null;

 if (Npollo.isSelected()) {
                sentenciacomplementos.setInt(1, 43);
                ejecutar = sentenciacomplementos.executeQuery();
            } else if (Acebolla.isSelected()) {
                sentenciacomplementos.setInt(1, 46);
                ejecutar = sentenciacomplementos.executeQuery();
            } else if (Psupreme.isSelected()) {
                sentenciacomplementos.setInt(1, 48);
                ejecutar = sentenciacomplementos.executeQuery();
            } else if (Pclasica.isSelected()) {
                sentenciacomplementos.setInt(1, 47);
                ejecutar = sentenciacomplementos.executeQuery();
            }

            if (ejecutar.next()) {
                complemento.setIdProducto(ejecutar.getInt("id_producto"));
                complemento.setNombre(ejecutar.getString("nombre"));
                complemento.setPrecio(ejecutar.getDouble("precio"));
                complemento.setDescripcion(ejecutar.getString("descripcion"));
                complemento.setPeso(ejecutar.getDouble("peso"));
                complemento.setCategoria(ejecutar.getString("categoria"));
            }
        }
        System.out.println(complemento.getNombre());
    }

    @FXML
    public void bebida(ActionEvent event) throws SQLException {
        // Método para seleccionar una bebida
        if (bebida == null) {
            bebida = new Producto(); // Inicializar el objeto bebida
        }

        try (Connection conexioncomplementos = util.Conexiones.dameConexion("burger-queen")) {
            PreparedStatement sentenciacomplementos = conexioncomplementos.prepareStatement("SELECT id_producto, nombre, descripcion, precio, categoria, peso FROM carta WHERE id_producto = ?");
            ResultSet ejecutar = null;

            if (Cocacola.isSelected()) {
                sentenciacomplementos.setInt(1, 44);
                ejecutar = sentenciacomplementos.executeQuery();
            } else if (Fanta.isSelected()) {
                sentenciacomplementos.setInt(1, 49);
                ejecutar = sentenciacomplementos.executeQuery();
            } else if (Nestea.isSelected()) {
                sentenciacomplementos.setInt(1, 50);
                ejecutar = sentenciacomplementos.executeQuery();
            } else if (CcZero.isSelected()) {
                sentenciacomplementos.setInt(1, 51);
                ejecutar = sentenciacomplementos.executeQuery();
            }

            if (ejecutar.next()) {
                bebida.setIdProducto(ejecutar.getInt("id_producto"));
                bebida.setNombre(ejecutar.getString("nombre"));
                bebida.setPrecio(ejecutar.getDouble("precio"));
                bebida.setDescripcion(ejecutar.getString("descripcion"));
                bebida.setPeso(ejecutar.getDouble("peso"));
                bebida.setCategoria(ejecutar.getString("categoria"));
            }
        }
        System.out.println(bebida.getNombre());
    }

    @FXML
    private void aniadirextra1B(ActionEvent event) {
        // Método para añadir un extra de bacon
        if (cantExtras < 1) {
            precioTotal += 1.20; // Incrementar el precio total
            cantExtras++;
            tipoExtra = " + Extra Bacon"; // Actualizar el tipo de extra
            Carrito.setText("Añadir a mi pedido - " + String.format("%.2f €", precioTotal));
        }
    }

    public void metercarrito() throws SQLException {
        // Método para añadir el producto al carrito
        if (Login.datos_login.getIdUsuario() == 0) {
            Mostrar_Login(); // Mostrar ventana de login si el usuario no está autenticado
        } else {
            try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
                // Consulta para contar los pedidos en curso del usuario
                PreparedStatement cuentaPedidos = conexion.prepareStatement("SELECT COUNT(*) FROM pedidos WHERE id_usuario = ?");
                cuentaPedidos.setInt(1, Login.datos_login.getIdUsuario());
                ResultSet resultadoCuenta = cuentaPedidos.executeQuery();

                int cantidadPedidosEnCurso = 0;
                if (resultadoCuenta.next()) {
                    cantidadPedidosEnCurso = resultadoCuenta.getInt(1);
                }

                // Si hay un pedido en curso, mostrar alerta y salir
                if (cantidadPedidosEnCurso > 0) {
                    mostrarAlerta(AlertType.WARNING, "Pedido en curso", "Por favor, espera a que tu pedido actual sea completado antes de realizar un nuevo pedido.");
                    return; // Salir del método sin procesar el nuevo pedido
                } else {
                    // Si no hay pedidos en curso, proceder a insertar el nuevo pedido
                    PreparedStatement sentencia = conexion.prepareStatement("SELECT id_producto, nombre, descripcion, precio, categoria, peso FROM carta WHERE id_producto = ?");
                    sentencia.setInt(1, producto.getIdProducto());

                    ResultSet resultado = sentencia.executeQuery();

                    if (resultado.next()) {
                        PreparedStatement insertaproductobase = conexion.prepareStatement(
                            "INSERT INTO carrito_items (id_carrito, id_plato, cantidad, precio_unitario, Detalles, estado) VALUES (?,?,?,?,?,'pendiente')");
                        insertaproductobase.setInt(1, Login.datos_login.getIdUsuario());
                        insertaproductobase.setInt(2, resultado.getInt("id_producto"));
                        insertaproductobase.setInt(3, 1);
                        insertaproductobase.setDouble(4, precioTotal);
                        insertaproductobase.setString(5, tipoExtra);

                        int ejecutainsertar = insertaproductobase.executeUpdate();
                        System.out.println("Producto base añadido al carrito con éxito.");
                    }

                    // Procesar complementos
                    if (complemento != null) {
                        PreparedStatement insertacomplemento = conexion.prepareStatement(
                            "INSERT INTO carrito_items (id_carrito, id_plato, cantidad, precio_unitario, Detalles, estado) VALUES (?,?,?,?,?,'pendiente')");
                        insertacomplemento.setInt(1, Login.datos_login.getIdUsuario());
                        insertacomplemento.setInt(2, complemento.getIdProducto());
                        insertacomplemento.setInt(3, 1);
                        insertacomplemento.setDouble(4, complemento.getPrecio());
                        insertacomplemento.setString(5, "");

                        int ejecutarInsertComplemento = insertacomplemento.executeUpdate();
                        System.out.println("Complemento añadido al carrito con éxito.");
                    }

                    // Procesar bebida
                    if (bebida != null) {
                        PreparedStatement insertacomplemento = conexion.prepareStatement(
                            "INSERT INTO carrito_items (id_carrito, id_plato, cantidad, precio_unitario, Detalles, estado) VALUES (?,?,?,?,?,'pendiente')");
                        insertacomplemento.setInt(1, Login.datos_login.getIdUsuario());
                        insertacomplemento.setInt(2, bebida.getIdProducto());
                        insertacomplemento.setInt(3, 1);
                        insertacomplemento.setDouble(4, bebida.getPrecio());
                        insertacomplemento.setString(5, "");

                        int ejecutarInsertComplemento = insertacomplemento.executeUpdate();
                        System.out.println("Complemento añadido al carrito con éxito.");
                    }

                    // Notificación de éxito
                    Notifications notification = Notifications.create()
                        .title("Producto añadido")
                        .text("El producto ha sido agregado")
                        .hideAfter(javafx.util.Duration.seconds(5));
                    notification.showInformation();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta(AlertType.ERROR, "Error de conexión", "No se pudo conectar a la base de datos.");
            }
        }
    }

    @FXML
    private void aniadirextra1Q() {
        // Método para añadir un extra de queso
        if (cantExtras < 1) {
            precioTotal += 1.20; // Incrementar el precio total
            cantExtras++;
            tipoExtra = " + Extra Queso"; // Actualizar el tipo de extra
            Carrito.setText("Añadir a mi pedido - " + String.format("%.2f €", precioTotal));
        }
    }

    public void sinextras() {
        // Método para reiniciar la selección de extras
        if (cantExtras > 0) {
            cantExtras = 0; // Reiniciar la cantidad de extras
            tipoExtra = "";
            precioTotal = precioBase; // Reiniciar el precio total al precio base
            Carrito.setText("Añadir a mi pedido - " + String.format("%.2f €", precioTotal));
        }
    }

    public void Carta() throws IOException {
        // Método para abrir la ventana de la carta
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
        Pane carta = cargador.load();

        Scene cartaScene = new Scene(carta, 600, 500);
        cartaScene.setFill(Color.TRANSPARENT);

        Stage cartaStage = new Stage();
        cartaStage.setResizable(false);
        cartaStage.initStyle(StageStyle.DECORATED);
        cartaStage.setScene(cartaScene);
        cartaStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
        cartaStage.setTitle("CARTA");
        cartaStage.show();
        cerrar(); // Cerrar la ventana actual
    }

    public void mostrarModificaProducto() {
        // Método para mostrar la ventana de modificación de producto
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/ModificaProducto.fxml"));
            AnchorPane ModificarProductoPane = loader.load();

            ModificarProducto Controller = loader.getController();
            Controller.setProducto(producto); // Pasar el producto a modificar

            Stage ModificarProductoStage = new Stage();
            ModificarProductoStage.initStyle(StageStyle.TRANSPARENT);
            ModificarProductoStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(ModificarProductoPane, 800, 600);
            ModificarProductoStage.setScene(scene);
            ModificarProductoStage.getIcons().add(new Image(getClass().getResourceAsStream("/icono.png")));
            ModificarProductoStage.setTitle("Modificar Producto");
            ModificarProductoStage.show(); // Mostrar la ventana de modificación

        } catch (IOException e) {
            e.printStackTrace(); // Imprimir error si no se puede cargar la vista
        }
    }

    public void eliminaProducto() throws IOException {
        // Método para eliminar un producto de la carta
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sql = "DELETE FROM carta WHERE id_producto = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setInt(1, producto.getIdProducto());

                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    mostrarAlerta(AlertType.INFORMATION, "Borrado", "Producto borrado correctamente."); // Notificación de éxito
                    cerrar(); // Cerrar la ventana actual
                } else {
                    mostrarAlerta(AlertType.ERROR, "Error", "No se pudo borrar el producto."); // Notificación de error
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error de conexión", "No se pudo conectar a la base de datos."); // Notificación de error de conexión
        }
        cerrar(); // Cerrar la ventana actual
         // Volver a la vista de la carta
    }

    public void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        // Método para mostrar una alerta
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait(); // Mostrar la alerta y esperar a que se cierre
    }

    public void Mostrar_Login() {
        // Método para mostrar la ventana de login
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
            Pane login = cargador.load();

            Scene loginScene = new Scene(login, 450, 600);
            loginScene.setFill(Color.TRANSPARENT);

            Stage loginStage = new Stage();
            loginStage.setResizable(false);
            loginStage.initStyle(StageStyle.TRANSPARENT);
            loginStage.setScene(loginScene);
            loginStage.setTitle("LOGIN");
            loginStage.show(); // Mostrar la ventana de login
            cerrar(); // Cerrar la ventana actual

        } catch (Exception e) {
            e.printStackTrace(); // Imprimir error si no se puede cargar la vista
        }
    }
}