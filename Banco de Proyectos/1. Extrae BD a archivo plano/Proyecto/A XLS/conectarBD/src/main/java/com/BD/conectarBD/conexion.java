package com.BD.conectarBD;


// librerias necesarias 
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


import javax.activation.DataSource;





public class conexion {
	public static void main(String[] args) throws Exception {
	    Connection con = null;
	    
	    //String URL= "";
	    //La ip y la base de datos a donde se va a conectar
	    String sURL = "jdbc:mysql://192.168.1.207:3306/MPskillnet";
	    con = DriverManager.getConnection(sURL,"root","sk1lln3t");
	    
	    //una vez conectado se ingresa la consulta que se quiere realizar
	   // try (PreparedStatement stmt = con.prepareStatement("select * from Trabajador INTO OUTFILE '/home/miguel/Escritorio/DEFINITIVO.csv' fields terminated by ';' optionally enclosed by '\"' lines terminated by '\r\n';")) {
	      try (PreparedStatement stmt = con.prepareStatement("select * from Trabajador INTO OUTFILE '/tmp/Trabajador.xls';")){;
	    
	        ResultSet rs = stmt.executeQuery();
	     
	       //mientras la respuesta tenga algun valor me imprimira que conecto 
	        while (rs.next())
	        {
	          System.out.print("Conecto");//rs.getString("rute"));
	        }	
	        
	        
	       // excepcion en el caso que no conecte 
	      } catch (SQLException sqle) { 
	        System.out.println("Error en la ejecución:" 
	          + sqle.getErrorCode() + " " + sqle.getMessage());    
	      }
	    
	}
	
}
