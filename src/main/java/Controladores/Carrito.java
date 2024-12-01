package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Carrito implements Initializable {

    public static int ventanaanterior;
    @FXML
    GridPane Listado;
    @FXML
    private Button Cerrar;
    @FXML
    private Text Username;
    @FXML
    private Button Nugguest;
    @FXML
    private Button Patatas;
    @FXML
    private Button Cocacola;
    @FXML
    GridPane factura;

    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }
    public void flechaatras() throws IOException {
        cerrar();
       
        
        switch (ventanaanterior) {
		case 1: {
			Pantalla_Principal();
			break;
			
		}
		case 2:{
			
			Carta();
			break;
		}
		case 3: {
			Horarios();
			break;
			
		}
		case 4:{
			Ubicacion();
			break;
			
		}
		case 5: {
			Reserva();
			break;
			
		}
		case 6: {
			perfil();
			break;
			
		}
		
		
		
		
		}
    }
    
    public void Carta() throws IOException {
    
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Carta.fxml"));
        Pane carta = cargador.load();
        Scene loginScene = new Scene(carta, 600, 500);
        loginScene.setFill(Color.TRANSPARENT);

        Stage stagecarta = new Stage();
        stagecarta.setResizable(false);
        stagecarta.initStyle(StageStyle.DECORATED);
        stagecarta.setScene(loginScene);
        stagecarta.setTitle("CARTA");
        stagecarta.show();
        cerrar();  
    }
    
    public void perfil() throws IOException {
    	if (!(Login.tipo.equals("usuarios"))) {
        		
        		cerrar();
        		Mostrar_Login();
        	}
    	else {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/perfil.fxml"));
            Pane perfilpane = cargador.load();
            Scene perfilScene = new Scene(perfilpane, 600, 500);
           
            perfilScene.setFill(Color.TRANSPARENT);
            Stage perfilStage = new Stage();
            perfilStage.setResizable(false);
            perfilStage.initStyle(StageStyle.DECORATED);
            perfilStage.setScene(perfilScene);
            perfilStage.setTitle("PERFIL");
            perfilStage.show();
            cerrar();
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
    
    public void Pantalla_Principal() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
        Pane principal = cargador.load();
        Scene principalScene = new Scene(principal, 600, 500);
       
        principalScene.setFill(Color.TRANSPARENT);
        Stage PrincipalStage = new Stage();
        PrincipalStage.initStyle(StageStyle.DECORATED);
        PrincipalStage.setScene(principalScene);
        PrincipalStage.setTitle("PANTALLA PRINCIPAL");
        PrincipalStage.show();
        cerrar();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        
        Connection conn=util.Conexiones.dameConexion("burger-queen");
      

        Nugguest.setOnAction(event -> {
            if (Login.datos_login.getIdUsuario() == 0) {
                Mostrar_Login();
            } else {
                try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
                    PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO carrito_items(id_carrito, id_plato, cantidad, precio_unitario,estado) VALUES (?,43,1,2,'pendiente')");
                    sentencia.setInt(1, Login.datos_login.getIdUsuario());
                    int ejecuta = sentencia.executeUpdate();
                    Muestra_productos();
                    Factura();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        Cocacola.setOnAction(event -> {
            if (Login.datos_login.getIdUsuario() == 0) {
                Mostrar_Login();
            } else {
                try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
                    PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO carrito_items(id_carrito, id_plato, cantidad, precio_unitario,estado) VALUES (?,44,1,5,'pendiente')");
                    sentencia.setInt(1, Login.datos_login.getIdUsuario());
                    int ejecuta = sentencia.executeUpdate();
                    Muestra_productos();
                    Factura();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        Patatas.setOnAction(event -> {
            if (Login.datos_login.getIdUsuario() == 0) {
                Mostrar_Login();
            } else {
                try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
                    PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO carrito_items(id_carrito, id_plato, cantidad, precio_unitario,estado) VALUES (?,45,1,1,'pendiente')");
                    sentencia.setInt(1, Login.datos_login.getIdUsuario());
                    int ejecuta = sentencia.executeUpdate();
                    Muestra_productos();
                    Factura();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            Muestra_productos();
            Factura();
        } catch (SQLException e) {
            e.printStackTrace();
        }
     
    }

    public void Muestra_productos() throws SQLException {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String sqlObtenerCarrito = "SELECT id_carrito FROM carrito WHERE id_cliente = ? AND estado = 'pendiente'";
            PreparedStatement sentenciaObtenerCarrito = conexion.prepareStatement(sqlObtenerCarrito);
            sentenciaObtenerCarrito.setInt(1, Login.datos_login.getIdUsuario());
            ResultSet resultadoCarrito = sentenciaObtenerCarrito.executeQuery();

            if (!resultadoCarrito.next()) {
                System.out.println("No se encontró un carrito pendiente para el usuario.");
                return;
            }

            int idCarrito = resultadoCarrito.getInt("id_carrito");
            resultadoCarrito.close();
            sentenciaObtenerCarrito.close();

            // Filtrar productos solo con estado 'pendiente'
            String sqlCarritoItems = "SELECT id_plato, precio_unitario, id_item, Detalles FROM carrito_items WHERE id_carrito = ? AND estado = 'pendiente'";
            PreparedStatement sentenciaCarritoItems = conexion.prepareStatement(sqlCarritoItems);
            sentenciaCarritoItems.setInt(1, idCarrito);
            ResultSet carritoItems = sentenciaCarritoItems.executeQuery();

            int row = 0;

            while (carritoItems.next()) {
                int idProducto = carritoItems.getInt("id_plato");
                double precio = carritoItems.getDouble("precio_unitario");
                int idItem = carritoItems.getInt("id_item");
                String Detalles = carritoItems.getString("Detalles");

                if (Detalles == null) {
                    Detalles = "";
                }

                String sqlCarta = "SELECT c.id_producto, c.nombre, c.descripcion, c.precio, c.categoria, c.peso, c.ruta, c.alergenos FROM carta c WHERE c.id_producto = ?";
                PreparedStatement sentenciaCarta = conexion.prepareStatement(sqlCarta);
                sentenciaCarta.setInt(1, idProducto);
                ResultSet productoDetalles = sentenciaCarta.executeQuery();

                if (productoDetalles.next()) {
                    AnchorPane producto = new AnchorPane();
                    producto.setPrefSize(450, 90);
                    producto.setStyle("-fx-background-color: A6234E; -fx-background-radius: 20; -fx-border-radius: 20;-fx-border-color: FFFFFF");

                    Label nombreProducto = new Label(productoDetalles.getString("nombre") + Detalles);
                    nombreProducto.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
                    AnchorPane.setLeftAnchor(nombreProducto, 10.0);
                    AnchorPane.setTopAnchor(nombreProducto, 10.0);
                    producto.getChildren().add(nombreProducto);

                    Label precioEtiqueta = new Label(precio + " €");
                    precioEtiqueta.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
                    AnchorPane.setRightAnchor(precioEtiqueta, 10.0);
                    AnchorPane.setTopAnchor(precioEtiqueta, 10.0);
                    producto.getChildren().add(precioEtiqueta);

                    String alergenos = productoDetalles.getString("alergenos");
                    Label alergenosLabel = new Label("Alérgenos: " + (alergenos != null ? alergenos : "Ninguno"));
                    alergenosLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
                    AnchorPane.setLeftAnchor(alergenosLabel, 10.0);
                    AnchorPane.setTopAnchor(alergenosLabel, 50.0);
                    producto.getChildren().add(alergenosLabel);

                    Button botonBorrar = new Button();
                    botonBorrar.setStyle("-fx-background-color: transparent;");
                    ImageView iconoBorrar = new ImageView(new Image("/basura.png"));
                    iconoBorrar.setFitWidth(20);
                    iconoBorrar.setFitHeight(20);
                    iconoBorrar.setPreserveRatio(true);
                    botonBorrar.setGraphic(iconoBorrar);

                    botonBorrar.setUserData(idItem);
                    botonBorrar.setOnAction(event -> {
                        int idItemEliminar = (int) botonBorrar.getUserData();
                        try (Connection conexionEliminar = util.Conexiones.dameConexion("burger-queen")) {
                            PreparedStatement sentenciaEliminar = conexionEliminar.prepareStatement("DELETE FROM carrito_items WHERE id_item = ?");
                            sentenciaEliminar.setInt(1, idItemEliminar);
                            int resultado = sentenciaEliminar.executeUpdate();

                            if (resultado > 0) {
                                System.out.println("Producto eliminado del carrito con éxito.");
                                Listado.getChildren().clear();
                                Muestra_productos();
                                Factura();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                    AnchorPane.setRightAnchor(botonBorrar, 10.0);
                    AnchorPane.setBottomAnchor(botonBorrar, 10.0);
                    producto.getChildren().add(botonBorrar);

                    Listado.add(producto, 0, row);
                    row++;
                    GridPane.setVgrow(producto, javafx.scene.layout.Priority.ALWAYS);
                }

                productoDetalles.close();
                sentenciaCarta.close();
            }

            carritoItems.close();
            sentenciaCarritoItems.close();
        }
    }
    public void Ubicacion() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Ubicacion.fxml"));
        Pane ubiacionpane = cargador.load();
        Scene ubicacionScene = new Scene(ubiacionpane, 600, 500);
        ubicacionScene.setFill(Color.TRANSPARENT);

        Stage ubicacionStage = new Stage();
        ubicacionStage.setResizable(false);
        ubicacionStage.initStyle(StageStyle.DECORATED);
        ubicacionStage.setScene(ubicacionScene);
        ubicacionStage.setTitle("UBICACION");
        ubicacionStage.show();
        cerrar();  
    }

    public void Factura() throws SQLException {
        // Limpiar el contenedor de factura
        factura.getChildren().clear();
        Double total = 0.0;
        DecimalFormat df = new DecimalFormat("#.00");

        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            // Obtener el carrito pendiente para el usuario actual
            String sqlObtenerCarrito = "SELECT id_carrito FROM carrito WHERE id_cliente = ? AND estado = 'pendiente'";
            PreparedStatement sentenciaObtenerCarrito = conexion.prepareStatement(sqlObtenerCarrito);
            sentenciaObtenerCarrito.setInt(1, Login.datos_login.getIdUsuario());
            ResultSet resultadoCarrito = sentenciaObtenerCarrito.executeQuery();

            if (!resultadoCarrito.next()) {
                System.out.println("No se encontró un carrito pendiente para el usuario.");
                return;
            }

            int idCarrito = resultadoCarrito.getInt("id_carrito");
            resultadoCarrito.close();
            sentenciaObtenerCarrito.close();

            // Obtener los elementos del carrito con estado "Pendiente"
            String sqlCarritoItems = "SELECT id_plato, precio_unitario FROM carrito_items WHERE id_carrito = ? AND estado = 'Pendiente'";
            PreparedStatement sentenciaCarritoItems = conexion.prepareStatement(sqlCarritoItems);
            sentenciaCarritoItems.setInt(1, idCarrito);
            ResultSet carritoItems = sentenciaCarritoItems.executeQuery();

            // Inicializar fila en el GridPane
            int row = 0;

            while (carritoItems.next()) {
                int idProducto = carritoItems.getInt("id_plato");
                Double precio = carritoItems.getDouble("precio_unitario");
                total += precio;

                // Obtener el nombre del producto desde la tabla "carta"
                String sqlNombreProducto = "SELECT nombre FROM carta WHERE id_producto = ?";
                PreparedStatement sentenciaNombreProducto = conexion.prepareStatement(sqlNombreProducto);
                sentenciaNombreProducto.setInt(1, idProducto);
                ResultSet productoDetalles = sentenciaNombreProducto.executeQuery();

                if (productoDetalles.next()) {
                    String nombreProducto = productoDetalles.getString("nombre");

                    // Crear etiquetas para el nombre y el precio del producto
                    Label productoNombreFactura = new Label(nombreProducto);
                    productoNombreFactura.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
                    
                    Label productoPrecioFactura = new Label(df.format(precio) + " €");
                    productoPrecioFactura.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

                    // Añadir al GridPane en la fila actual
                    factura.add(productoNombreFactura, 0, row);
                    factura.add(productoPrecioFactura, 1, row);
                    row++;
                }

                productoDetalles.close();
                sentenciaNombreProducto.close();
            }

            // Mostrar el total en la última fila del GridPane
            Label totalLabel = new Label("Total:");
            totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
            Label totalAmount = new Label(df.format(total) + " €");
            totalAmount.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

            factura.add(totalLabel, 0, row);
            factura.add(totalAmount, 1, row);

            carritoItems.close();
            sentenciaCarritoItems.close();
        }
    }

    
    public void resetearcarrito() {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            // Consulta para contar los pedidos en curso del usuario
            PreparedStatement cuentaPedidos = conexion.prepareStatement(
                "SELECT COUNT(*) FROM pedidos WHERE id_usuario = ? AND estado = 'en_curso'");
            cuentaPedidos.setInt(1, Login.datos_login.getIdUsuario());
            ResultSet resultadoCuenta = cuentaPedidos.executeQuery();

            int cantidadPedidosEnCurso = 0;
            if (resultadoCuenta.next()) {
                cantidadPedidosEnCurso = resultadoCuenta.getInt(1);
            }

            // Si hay un pedido en curso, mostrar alerta y salir
            if (cantidadPedidosEnCurso > 0) {
                mostrarAlerta(AlertType.WARNING, "Pedido en curso", 
                    "Por favor, espera a que tu pedido actual sea completado antes de realizar un nuevo pedido.");
                return; // Salir del método sin procesar el nuevo pedido
            }

            // Obtener el ID del carrito del usuario con estado 'pendiente'
            String sqlObtenerCarrito = "SELECT id_carrito FROM carrito WHERE id_cliente = ? AND estado = 'pendiente'";
            PreparedStatement sentenciaObtenerCarrito = conexion.prepareStatement(sqlObtenerCarrito);
            sentenciaObtenerCarrito.setInt(1, Login.datos_login.getIdUsuario());
            ResultSet resultadoCarrito = sentenciaObtenerCarrito.executeQuery();

            if (!resultadoCarrito.next()) {
                System.out.println("No se encontró un carrito pendiente para el usuario.");
                return;
            }

            int idCarrito = resultadoCarrito.getInt("id_carrito");
            resultadoCarrito.close();
            sentenciaObtenerCarrito.close();

            // Crear un nuevo pedido en la tabla pedidos
            String sqlInsertarPedido = "INSERT INTO pedidos (id_carrito, id_usuario, estado) VALUES (?, ?, 'en_curso')";
            PreparedStatement sentenciaInsertarPedido = conexion.prepareStatement(sqlInsertarPedido, PreparedStatement.RETURN_GENERATED_KEYS);
            sentenciaInsertarPedido.setInt(1, idCarrito);
            sentenciaInsertarPedido.setInt(2, Login.datos_login.getIdUsuario());
            int filasAfectadas = sentenciaInsertarPedido.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("No se pudo crear el pedido.");
                return;
            }

            ResultSet clavesGeneradas = sentenciaInsertarPedido.getGeneratedKeys();
            int idPedido = 0;
            if (clavesGeneradas.next()) {
                idPedido = clavesGeneradas.getInt(1);
            }
            clavesGeneradas.close();
            sentenciaInsertarPedido.close();

            // Actualizar los productos en carrito_items para marcarlos como "Tramitado"
            String sqlActualizarCarritoItems = "UPDATE carrito_items SET estado = 'Tramitado' WHERE id_carrito = ? AND estado = 'Pendiente'";
            PreparedStatement sentenciaActualizarCarritoItems = conexion.prepareStatement(sqlActualizarCarritoItems);
            sentenciaActualizarCarritoItems.setInt(1, idCarrito);
            int productosActualizados = sentenciaActualizarCarritoItems.executeUpdate();

            if (productosActualizados > 0) {
                System.out.println("Carrito procesado con éxito. Pedido creado con ID: " + idPedido);
            } else {
                System.out.println("No se encontraron productos pendientes para procesar.");
            }

            sentenciaActualizarCarritoItems.close();

            // Refrescar la vista del carrito
            Listado.getChildren().clear();
            factura.getChildren().clear();
            Muestra_productos();
            Factura();
            
            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Pedido completado");
            successAlert.setHeaderText(null);
            successAlert.setContentText("OPERACION COMPLETADA");
            successAlert.initModality(Modality.APPLICATION_MODAL);
            successAlert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
    
   


    
   
    }




