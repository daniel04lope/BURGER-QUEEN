package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
public class Comprobar_Conexion {

	public static void main(String[] args) {
		Connection conexion=util.Conexiones.dameConexion("burger-queen");
		try {
			System.out.println("La conexion con la base de datos "+conexion.getCatalog()+" es "+conexion.isValid(2));
			conexion.close();
			System.out.println("Se ha cerrado la conexion a proposito");
			System.out.println("La conexion con la base de datos "+conexion.getCatalog()+" es "+conexion.isValid(2));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
