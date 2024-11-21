<<<<<<< HEAD
package Controladores;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pedidos extends Application {
    @FXML
    private ListView<String> listaEnPreparacion;
    @FXML
    private ListView<String> listaListos;
    @FXML
    private Button botonMarcarListo;
    @FXML
    private Button botonEliminarPreparacion;
    @FXML
    private Button botonEliminarListo;
    @FXML
    private TextArea detallePedidoArea;  // Area para mostrar los detalles del pedido

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/pedidos.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Gestor de Pedidos - Burger Queen");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        // Cargar pedidos desde la base de datos
        cargarPedidosDesdeBD();

        // Configurar acciones de botones
        botonMarcarListo.setOnAction(e -> marcarComoListo());
        botonEliminarPreparacion.setOnAction(e -> eliminarPedidoEnPreparacion());
        botonEliminarListo.setOnAction(e -> eliminarPedidoListo());

        // Configurar eventos para hacer clic en los pedidos
        listaEnPreparacion.setOnMouseClicked(e -> {
            String selected = listaEnPreparacion.getSelectionModel().getSelectedItem();
            if (selected != null) {
                int idPedido = obtenerIdPedidoDeTexto(selected);
                mostrarDetallesPedido(idPedido);
            }
        });

        listaListos.setOnMouseClicked(e -> {
            String selected = listaListos.getSelectionModel().getSelectedItem();
            if (selected != null) {
                int idPedido = obtenerIdPedidoDeTexto(selected);
                mostrarDetallesPedido(idPedido);
            }
        });
    }

    private void cargarPedidosDesdeBD() {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String consulta = "SELECT id_pedido, id_carrito, id_usuario FROM pedidos";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                int idPedido = resultado.getInt("id_pedido");
                int idCarrito = resultado.getInt("id_carrito");
                int idUsuario = resultado.getInt("id_usuario");

                // Agregar a la lista de En Preparación
                listaEnPreparacion.getItems().add("Pedido: " + idPedido + " | Carrito: " + idCarrito + " | Usuario: " + idUsuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void marcarComoListo() {
        String selected = listaEnPreparacion.getSelectionModel().getSelectedItem();
        if (selected != null) {
            listaEnPreparacion.getItems().remove(selected);
            listaListos.getItems().add(selected);
        } else {
            mostrarAlerta("Por favor, selecciona un pedido en preparación.");
        }
    }

    private void eliminarPedidoEnPreparacion() {
        String selected = listaEnPreparacion.getSelectionModel().getSelectedItem();
        if (selected != null) {
            listaEnPreparacion.getItems().remove(selected);
        } else {
            mostrarAlerta("Por favor, selecciona un pedido en preparación.");
        }
    }

    private void eliminarPedidoListo() {
        String selected = listaListos.getSelectionModel().getSelectedItem();
        if (selected != null) {
            listaListos.getItems().remove(selected);
        } else {
            mostrarAlerta("Por favor, selecciona un pedido listo.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Método para obtener el ID del pedido desde el texto de la lista
    private int obtenerIdPedidoDeTexto(String texto) {
        try {
            String[] partes = texto.split("\\|");
            String idPedidoTexto = partes[0].replace("Pedido:", "").trim();
            return Integer.parseInt(idPedidoTexto);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al interpretar el ID del pedido.");
            return -1;
        }
    }

    // Método para mostrar los detalles del pedido en el TextArea
    private void mostrarDetallesPedido(int idPedido) {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String consulta = "SELECT p.nombre, p.precio, cp.cantidad " +
                              "FROM carrito_items cp " +
                              "JOIN productos p ON cp.id_producto = p.id_producto " +
                              "WHERE cp.id_carrito = ?";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idPedido);
            ResultSet resultado = sentencia.executeQuery();

            StringBuilder detalles = new StringBuilder();
            detalles.append("Detalles del Pedido ").append(idPedido).append(":\n");

            while (resultado.next()) {
                String nombre = resultado.getString("nombre");
                double precio = resultado.getDouble("precio");
                int cantidad = resultado.getInt("cantidad");

                detalles.append("- ").append(nombre)
                        .append(" | Precio: ").append(precio)
                        .append(" | Cantidad: ").append(cantidad)
                        .append("\n");
            }
//probando subida a github
            // Mostrar los detalles en el TextArea
            detallePedidoArea.setText(detalles.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar los detalles del pedido.");
        }
    }
}
=======
package Controladores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pedidos extends JFrame {
    private static final long serialVersionUID = 1L;

    // Listas para almacenar los pedidos en preparación y listos
    private DefaultListModel<String> modeloEnPreparacion;
    private DefaultListModel<String> modeloListos;
    private JList<String> listaEnPreparacion;
    private JList<String> listaListos;

    public Pedidos() throws SQLException {
        // Configuración de la ventana
        setTitle("Gestor de Pedidos - Burger Queen");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        // Inicializar los modelos y listas
        modeloEnPreparacion = new DefaultListModel<>();
        modeloListos = new DefaultListModel<>();
        listaEnPreparacion = new JList<>(modeloEnPreparacion);
        listaListos = new JList<>(modeloListos);

        // Panel de En Preparación
        JPanel panelEnPreparacion = new JPanel();
        panelEnPreparacion.setLayout(new BorderLayout());
        panelEnPreparacion.setBorder(BorderFactory.createTitledBorder("En Preparación"));
        
        JButton botonMarcarListo = new JButton("Marcar como Listo");
        JButton botonEliminarPreparacion = new JButton("Eliminar Pedido");

        panelEnPreparacion.add(new JScrollPane(listaEnPreparacion), BorderLayout.CENTER);
        JPanel panelBotonesPreparacion = new JPanel();
        panelBotonesPreparacion.add(botonMarcarListo);
        panelBotonesPreparacion.add(botonEliminarPreparacion);
        panelEnPreparacion.add(panelBotonesPreparacion, BorderLayout.SOUTH);

        // Panel de Listos
        JPanel panelListos = new JPanel();
        panelListos.setLayout(new BorderLayout());
        panelListos.setBorder(BorderFactory.createTitledBorder("Listos"));

        JButton botonEliminarListo = new JButton("Eliminar Pedido");
        
        panelListos.add(new JScrollPane(listaListos), BorderLayout.CENTER);
        JPanel panelBotonesListos = new JPanel();
        panelBotonesListos.add(botonEliminarListo);
        panelListos.add(panelBotonesListos, BorderLayout.SOUTH);

        // Añadir los paneles a la ventana
        add(panelEnPreparacion);
        add(panelListos);

        // Cargar pedidos desde la base de datos
        cargarPedidosDesdeBD();

        // Añadir acciones a los botones
        botonMarcarListo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = listaEnPreparacion.getSelectedIndex();
                if (selectedIndex != -1) {
                    String pedido = modeloEnPreparacion.getElementAt(selectedIndex);
                    modeloEnPreparacion.remove(selectedIndex);
                    modeloListos.addElement(pedido);
                }
            }
        });

        botonEliminarPreparacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = listaEnPreparacion.getSelectedIndex();
                if (selectedIndex != -1) {
                    modeloEnPreparacion.remove(selectedIndex);
                }
            }
        });

        botonEliminarListo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = listaListos.getSelectedIndex();
                if (selectedIndex != -1) {
                    modeloListos.remove(selectedIndex);
                }
            }
        });
    }

    // Método para cargar pedidos desde la base de datos
    private void cargarPedidosDesdeBD() {
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            String consulta = "SELECT id_pedido, estado FROM pedidos";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();

            // Iterar sobre los resultados de la consulta
            while (resultado.next()) {
                // Obtener id del pedido y estado
                int idPedido = resultado.getInt("id_pedido");
                String estado = resultado.getString("estado");

                // Agregar pedido a la lista correspondiente
                if (estado.equalsIgnoreCase("En preparación")) {
                    modeloEnPreparacion.addElement("Pedido: " + idPedido);
                } else if (estado.equalsIgnoreCase("Listo")) {
                    modeloListos.addElement("Pedido: " + idPedido);
                }
            }

            resultado.close();
            sentencia.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar pedidos desde la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Pedidos frame = new Pedidos();
                frame.setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
>>>>>>> branch 'Pablo' of https://github.com/daniel04lope/BURGER-QUEEN.git
