package Controladores;

import Modelos.ReservaObjeto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ModificarReserva implements Initializable {

    // Elementos de la interfaz de usuario
    @FXML
    private AnchorPane modificarReservaPane;

    @FXML
    private TextField txtNombreCliente; // Campo para el nombre del cliente
    @FXML
    private DatePicker dpFechaReserva; // Selector de fecha para la reserva
    @FXML
    private TextField txtHoraReserva; // Campo para la hora de la reserva
    @FXML
    private TextField txtNumeroPersonas; // Campo para el número de personas
    @FXML
    private TextArea txtNotas; // Área de texto para notas adicionales
    @FXML
    private ComboBox<String> cbEstado; // ComboBox para el estado de la reserva
    @FXML
    private ComboBox<Integer> cbMesa; // ComboBox para seleccionar la mesa

    @FXML
    private Button btnGuardar; // Botón para guardar cambios
    @FXML
    private Button btnCancelar; // Botón para cancelar
    @FXML
    private Button Cerrar; // Botón para cerrar la ventana

    private ReservaObjeto reservaObjeto; // Objeto que representa la reserva a modificar
    
    // Método que se ejecuta al inicializar la interfaz
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Verificar el tipo de usuario y establecer permisos
        if (Login.tipo.equals("administradores")) {
            // Los administradores tienen acceso completo
        }
        if (Login.tipo.equals("empleados")) {
            // Verificar permisos para cada botón
            try {
                if (permisos(2, "escritura") == 0) {
                    // Si no tiene permiso de escritura, deshabilitar campos
                    btnGuardar.setDisable(true);
                    btnCancelar.setDisable(true);
                    txtNombreCliente.setEditable(false);
                    dpFechaReserva.setDisable(true);
                    txtHoraReserva.setEditable(false);
                    txtNumeroPersonas.setEditable(false);
                    txtNotas.setEditable(false);
                    cbEstado.setDisable(true);
                    cbMesa.setDisable(true);
                } else {
                    // Si tiene permiso, habilitar campos
                    btnGuardar.setDisable(false);
                    btnCancelar.setDisable(false);
                    txtNombreCliente.setEditable(true);
                    dpFechaReserva.setDisable(false);
                    txtHoraReserva.setEditable(true);
                    txtNumeroPersonas.setEditable(true);
                    txtNotas.setEditable(true);
                    cbEstado.setDisable(false);
                    cbMesa.setDisable(false);
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Manejo de excepciones de SQL
            }
        }

        if (Login.tipo.equals("usuarios")) {
            // Los usuarios no tienen permisos para editar
            btnGuardar.setDisable(true);
            btnCancelar.setDisable(true);
            txtNombreCliente.setEditable(false);
            dpFechaReserva.setEditable(false);
            txtHoraReserva.setEditable(false);
            txtNumeroPersonas.setEditable(false);
            txtNotas.setEditable(false);
            cbEstado.setEditable(false);
            cbMesa.setEditable(false);
        }

        try {
            // Imprimir permisos de lectura para depuración
            System.out.println(permisos(1, "lectura"));
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

    // Método para establecer la reserva a modificar
    public void setReserva(ReservaObjeto reservaObjeto) {
        this.reservaObjeto = reservaObjeto;
        
        // Cargar los datos de la reserva en los campos de texto
        txtNombreCliente.setText(reservaObjeto.getNombreCliente());
        dpFechaReserva.setValue(reservaObjeto.getFechaReserva());
        txtHoraReserva.setText(reservaObjeto.getHoraReserva().toString());
        txtNumeroPersonas.setText(String.valueOf(reservaObjeto.getNumeroPersonas()));
        txtNotas.setText(reservaObjeto.getNotas());
        
        // Agregar estados y mesas al ComboBox
        cbEstado.getItems().addAll("pendiente", "confirmada", "cancelada");
        for (int i = 1; i <= 24; i++) {
            cbMesa.getItems().addAll(i);
        }
        cbEstado.setValue(reservaObjeto.getEstado());
        cbMesa.setValue(reservaObjeto.getMesa());
        
        // Listener para manejar el cambio de estado
        cbEstado.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("cancelada".equals(newValue) || "pendiente".equals(newValue)) {
                cbMesa.setValue(0); // Resetear mesa si el estado es cancelada o pendiente
            }
        });
    }

    // Método para guardar la reserva
    @FXML
    private void guardarReserva() throws IOException {
        if (validarCampos()) {
            // Actualizar los datos de la reserva
            reservaObjeto.setNombreCliente(txtNombreCliente.getText());
            reservaObjeto.setFechaReserva(dpFechaReserva.getValue());
            reservaObjeto.setHoraReserva(LocalTime.parse(txtHoraReserva.getText()));
            reservaObjeto.setNumeroPersonas(Integer.parseInt(txtNumeroPersonas.getText()));
            reservaObjeto.setNotas(txtNotas.getText());
            reservaObjeto.setEstado(cbEstado.getValue());
            reservaObjeto.setMesa(cbMesa.getValue());

            // Actualizar la reserva en la base de datos
            boolean actualizado = actualizarReservaEnBD(reservaObjeto);
            if (actualizado) {
                mostrarAlerta("Éxito", "La reserva ha sido actualizada correctamente.");
            } else {
                mostrarAlerta("Error", "No se pudo actualizar la reserva. Intente nuevamente.");
            }

            cerrar(); // Cerrar la ventana
            ReservaAdmin(); // Volver a la pantalla de administración de reservas
        }
    }

    // Método para cargar la pantalla de administración de reservas
    public void ReservaAdmin() throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/ReservaAdmin.fxml"));
        Pane reservapane = cargador.load();
        Scene reservaScene = new Scene(reservapane, 600, 500);
        reservaScene.setFill(Color.TRANSPARENT);
        Stage reservaStage = new Stage();
        reservaStage.setResizable(false);
        reservaStage.initStyle(StageStyle.DECORATED);
        reservaStage.setScene(reservaScene);
        reservaStage.setTitle("PANEL DE GESTION DE RESERVAS");
        reservaStage.show();
        cerrar(); // Cerrar la ventana actual
    }

    // Método para validar los campos de entrada
    private boolean validarCampos() {
        if (txtNombreCliente.getText().isEmpty() || dpFechaReserva.getValue() == null || txtHoraReserva.getText().isEmpty() ||
            txtNumeroPersonas.getText().isEmpty() || cbEstado.getValue() == null) {
            mostrarAlerta("Error", "Por favor, complete todos los campos.");
            return false; // Retornar false si hay campos vacíos
        }
        return true; // Retornar true si todos los campos son válidos
    }

    // Método para mostrar alertas al usuario
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        if (titulo.equals("Error")) {
            alerta.setAlertType(Alert.AlertType.ERROR); // Cambiar tipo de alerta si es un error
        }
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait(); // Mostrar la alerta y esperar a que el usuario la cierre
    }

    // Método para actualizar la reserva en la base de datos
    private boolean actualizarReservaEnBD(ReservaObjeto reservaObjeto) {
        String sql = "UPDATE reservas SET nombre_cliente = ?, fecha_reserva = ?, hora_reserva = ?, " +
                     "numero_personas = ?, notas = ?, estado = ?, mesa = ? WHERE id_reserva = ?";
        
        reservaObjeto.setMesa(cbMesa.getValue()); // Establecer la mesa seleccionada
        
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                // Establecer los parámetros de la consulta
                ps.setString(1, reservaObjeto.getNombreCliente());
                ps.setDate(2, java.sql.Date.valueOf(reservaObjeto.getFechaReserva())); 
                ps.setTime(3, java.sql.Time.valueOf(reservaObjeto.getHoraReserva())); 
                ps.setInt(4, reservaObjeto.getNumeroPersonas());
                ps.setString(5, reservaObjeto.getNotas());
                ps.setString(6, reservaObjeto.getEstado());
                ps.setInt(7, reservaObjeto.getMesa());
                ps.setInt(8, reservaObjeto.getIdReserva());

                int filasAfectadas = ps.executeUpdate(); // Ejecutar la actualización
                return filasAfectadas > 0; // Retornar true si se actualizó correctamente
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Hubo un problema al intentar actualizar la reserva: " + e.getMessage());
            return false; // Retornar false si hubo un error
        }
    }

    // Método para cancelar la operación y cerrar la ventana
    @FXML
    private void cancelar() throws IOException {
        cerrar(); // Cerrar la ventana actual
        ReservaAdmin(); // Volver a la pantalla de administración de reservas
    }
    
    // Método para cerrar la ventana actual
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close(); // Cerrar la ventana
    }
}