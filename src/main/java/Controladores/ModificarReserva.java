package Controladores;

import Modelos.ReservaObjeto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ModificarReserva implements Initializable {

    @FXML
    private AnchorPane modificarReservaPane;

    @FXML
    private TextField txtNombreCliente;
    @FXML
    private DatePicker dpFechaReserva;
    @FXML
    private TextField txtHoraReserva;
    @FXML
    private TextField txtNumeroPersonas;
    @FXML
    private TextArea txtNotas;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private ComboBox<Integer> cbMesa;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button Cerrar;	

    private ReservaObjeto reservaObjeto;
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {

        if (Login.tipo.equals("administradores")) {
           
        }
        if (Login.tipo.equals("empleados")) {
          

            // Verificar permisos para cada botón
            try {
				if (permisos(2, "escritura") == 0) {
				   
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
					btnGuardar.setDisable(false);
					btnCancelar.setDisable(false);
					txtNombreCliente.setEditable(true);
					dpFechaReserva.setDisable(true);;
					txtHoraReserva.setEditable(true);
					txtNumeroPersonas.setEditable(true);
					txtNotas.setEditable(true);
					cbEstado.setDisable(false);
					cbMesa.setDisable(false);
				   
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


         
        }

        if (Login.tipo.equals("usuarios")) {
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

    public void setReserva(ReservaObjeto reservaObjeto) {
        this.reservaObjeto = reservaObjeto;
        
        txtNombreCliente.setText(reservaObjeto.getNombreCliente());
        dpFechaReserva.setValue(reservaObjeto.getFechaReserva());
        txtHoraReserva.setText(reservaObjeto.getHoraReserva().toString());
        txtNumeroPersonas.setText(String.valueOf(reservaObjeto.getNumeroPersonas()));
        txtNotas.setText(reservaObjeto.getNotas());
        cbEstado.getItems().addAll("pendiente", "confirmada", "cancelada");
        for(int i=1;i<=24;i++) {
        	cbMesa.getItems().addAll(i);
        }
        cbEstado.setValue(reservaObjeto.getEstado());
        cbMesa.setValue(reservaObjeto.getMesa());
        
        cbEstado.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("cancelada".equals(newValue)||"pendiente".equals(newValue)) {
                cbMesa.setValue(0); 
            }
        });
    }

    @FXML
    private void guardarReserva() {
        if (validarCampos()) {
            reservaObjeto.setNombreCliente(txtNombreCliente.getText());
            reservaObjeto.setFechaReserva(dpFechaReserva.getValue());
            reservaObjeto.setHoraReserva(LocalTime.parse(txtHoraReserva.getText()));
            reservaObjeto.setNumeroPersonas(Integer.parseInt(txtNumeroPersonas.getText()));
            reservaObjeto.setNotas(txtNotas.getText());
            reservaObjeto.setEstado(cbEstado.getValue());
            reservaObjeto.setMesa(cbMesa.getValue());

            
            boolean actualizado = actualizarReservaEnBD(reservaObjeto);
            if (actualizado) {
                mostrarAlerta("Éxito", "La reserva ha sido actualizada correctamente.");
            } else {
                mostrarAlerta("Error", "No se pudo actualizar la reserva. Intente nuevamente.");
            }

            
            cerrar();
        }
    }

    private boolean validarCampos() {
        if (txtNombreCliente.getText().isEmpty() || dpFechaReserva.getValue() == null || txtHoraReserva.getText().isEmpty() ||
            txtNumeroPersonas.getText().isEmpty() || cbEstado.getValue() == null) {
            mostrarAlerta("Error", "Por favor, complete todos los campos.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        if (titulo.equals("Error")) {
            alerta.setAlertType(Alert.AlertType.ERROR); 
        }
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private boolean actualizarReservaEnBD(ReservaObjeto reservaObjeto) {
        String sql = "UPDATE reservas SET nombre_cliente = ?, fecha_reserva = ?, hora_reserva = ?, " +
                     "numero_personas = ?, notas = ?, estado = ?, mesa = ? WHERE id_reserva = ?";
        
        
        reservaObjeto.setMesa(cbMesa.getValue()); 
        
        try (Connection conexion = util.Conexiones.dameConexion("burger-queen")) {
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, reservaObjeto.getNombreCliente());
                ps.setDate(2, java.sql.Date.valueOf(reservaObjeto.getFechaReserva())); 
                ps.setTime(3, java.sql.Time.valueOf(reservaObjeto.getHoraReserva())); 
                ps.setInt(4, reservaObjeto.getNumeroPersonas());
                ps.setString(5, reservaObjeto.getNotas());
                ps.setString(6, reservaObjeto.getEstado());
                ps.setInt(7, reservaObjeto.getMesa());
                ps.setInt(8, reservaObjeto.getIdReserva());

                int filasAfectadas = ps.executeUpdate();
                return filasAfectadas > 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Hubo un problema al intentar actualizar la reserva: " + e.getMessage());
            return false; 
        }
    }

    
    @FXML
    private void cancelar() {
        cerrar();
    }
    
    public void cerrar() {
        Stage stage = (Stage) Cerrar.getScene().getWindow();
        stage.close();
    }



    
}
