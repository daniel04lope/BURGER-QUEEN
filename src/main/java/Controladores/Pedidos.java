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
