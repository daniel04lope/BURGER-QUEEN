package Modelos;

import java.sql.Timestamp;
import java.util.Date;

public class Empleado {
    private int idEmpleado;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
    private String password;
    private Timestamp fechaContratacion;
    private String posicion;
    private String estado;
    private String telefono;
    private String direccion;
    private Date fechaNacimiento;
    private boolean lectura;
    private boolean escritura;
    private boolean controlTotal;
    private String ruta;
    
    // Constructor
    public Empleado() {}

    public Empleado( String nombre, String apellido, String email, String username, String password,
                     String posicion, String estado, String telefono, 
                    String direccion, Date fechaNacimiento, boolean lectura, boolean escritura, boolean controlTotal) {
       
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.username = username;
        this.password = password;
        this.posicion = posicion;
        this.estado = estado;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.lectura = lectura;
        this.escritura = escritura;
        this.controlTotal = controlTotal;
    }

    // Getters and Setters
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Timestamp fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public boolean isLectura() {
        return lectura;
    }

    public void setLectura(boolean lectura) {
        this.lectura = lectura;
    }

    public boolean isEscritura() {
        return escritura;
    }

    public void setEscritura(boolean escritura) {
        this.escritura = escritura;
    }

    public boolean isControlTotal() {
        return controlTotal;
    }

    public void setControlTotal(boolean controlTotal) {
        this.controlTotal = controlTotal;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "idEmpleado=" + idEmpleado +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fechaContratacion=" + fechaContratacion +
                ", posicion='" + posicion + '\'' +
                ", estado='" + estado + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", lectura=" + lectura +
                ", escritura=" + escritura +
                ", controlTotal=" + controlTotal +
                '}';
    }
    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
