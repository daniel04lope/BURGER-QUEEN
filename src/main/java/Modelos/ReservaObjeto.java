package Modelos;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaObjeto {
    private int idReserva;
    private String nombreCliente;
    private LocalDate fechaReserva; 
    private LocalTime horaReserva;  
    private int numeroPersonas;
    private String notas; 
    private String estado;
    private LocalDate fechaCreacion; 
    private String telefono;
    


	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}


	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	private int mesa;

    
    public ReservaObjeto(int idReserva2, String nombreCliente2, LocalDate localDate, LocalTime localTime, int numeroPersonas2, String notas2, String estado2, LocalDate localDate2, int mesa2, String telefono) {}

    
    public ReservaObjeto(int idReserva, String nombreCliente, LocalDate fechaReserva, LocalTime horaReserva,
                   int numeroPersonas, String notas, String estado, LocalDate fechaCreacion, int mesa) {
        this.idReserva = idReserva;
        this.nombreCliente = nombreCliente;
        this.fechaReserva = fechaReserva;
        this.horaReserva = horaReserva;
        this.numeroPersonas = numeroPersonas;
        this.notas = notas;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.mesa = mesa;
    }

    
    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public LocalTime getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(LocalTime horaReserva) {
        this.horaReserva = horaReserva;
    }

    public int getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(int numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public int getMesa() {
		return mesa;
	}


	public void setMesa(int mesa) {
		this.mesa = mesa;
	}

    
    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", fechaReserva=" + fechaReserva +
                ", horaReserva=" + horaReserva +
                ", numeroPersonas=" + numeroPersonas +
                ", notas='" + notas + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}

