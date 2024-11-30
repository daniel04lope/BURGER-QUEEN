package Modelos;

public class DetallePedido {

    private int idPedido;
    private String nombre;
    private int cantidad;
    private double precioUnitario;

    public DetallePedido(int idItem, String nombre, int cantidad, double precioUnitario) {
        this.idPedido = idItem;
        this.nombre = nombre.toUpperCase();
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int nuevoIdPedido) {
        this.idPedido = nuevoIdPedido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

	@Override
	public String toString() {
		return "DetallePedido [idPedido=" + idPedido + ", nombre=" + nombre + ", cantidad=" + cantidad + ", precioUnitario="
				+ precioUnitario + "]";
	}
    
    
}

