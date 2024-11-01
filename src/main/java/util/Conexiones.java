package util;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;


public class Conexiones {

	public static Connection dameConexion(String bbdd) {
		Connection conn = null;
		
				try { // registro el driver de connection
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			// Establezco la conexion con la BBDD
	    try {
	    
	        conn = DriverManager.getConnection
	        		("jdbc:mysql://localhost:3306/"+bbdd,
	        				"root","root");
	        System.out.println("Conexion establecida");
	  
	    } catch (SQLException ex) {
	        ex.printStackTrace() ;
	        System.out.println("SQLException : " + ex.getMessage());
	    }
		return conn;
	}
	
	/**
	 * 
	 * @param bbdd : Nombre de la base de datos a la que nos conectaremos
	 * @param archivoScript , direccion y nombre del archivo de script a ejecutar
	 */
	public static void lanzaScript(String bbdd, String archivoScript) {
		        //archivoScript = "C:/utilidades/primero/ejercicios/primerodam.sql";

		        try {
		            Connection conn = dameConexion(bbdd);
		            
		            if (conn!=null) {
			            Statement stmt = conn.createStatement();
			            
			            // Leer el archivo SQL
			            String script = new String(Files.readAllBytes(Paths.get(archivoScript)),
			            		StandardCharsets.UTF_8);
			            
			            // Ejecutar el script SQL
			            stmt.execute(script);
			            
			            System.out.println("Script SQL ejecutado correctamente.");
			            
			            conn.close();
			            }else
			            	System.out.println("No se ha podido establecer la conexion");
		        } catch (SQLException | IOException e) {
		            e.printStackTrace();
		        }
		    		
	} // lanza script
	
	public void insertarempleado (String nombre,String apellidos,String email,String username,String password,String posicion,String telefono,String direccion,int lectura,int escritura,int control_total) {
		
		
	}
	

	public static void insertarpersona(String nombre, String apellidos, String email, String username, String password, String telefono, String direccion, LocalDate nacimiento) throws SQLException {
	    String sql = "INSERT INTO usuarios (nombre, apellido, email, username, password, telefono, direccion, fecha_nacimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	    try (Connection conexion = util.Conexiones.dameConexion("burger-queen");
	         PreparedStatement sentencia = conexion.prepareStatement(sql)) {

	        sentencia.setString(1, nombre);
	        sentencia.setString(2, apellidos);
	        sentencia.setString(3, email);
	        
	        sentencia.setString(4, username);
	        sentencia.setString(5, password);
	        sentencia.setString(6, telefono);
	        sentencia.setString(7, direccion);

	        // Convert LocalDate to java.sql.Date
	        Date fechaNacimiento = Date.valueOf(nacimiento);
	        sentencia.setDate(8, fechaNacimiento);

	        // Execute the update
	        sentencia.executeUpdate();
	    }
	}



public void insertaradministrador (String nombre,String apellidos,String email,String username,String password,String posicion,String telefono,String direccion) {
		
		
	}
}