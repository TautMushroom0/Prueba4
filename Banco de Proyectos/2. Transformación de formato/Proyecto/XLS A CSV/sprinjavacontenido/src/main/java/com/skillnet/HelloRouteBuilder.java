package com.skillnet;
		 
import java.awt.Window.Type; 

import org.apache.camel.Converter;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxp.XmlConverter;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.omg.CORBA.ExceptionList;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;

		
public class HelloRouteBuilder extends RouteBuilder {
		
	
 
	/**
	 * Client route
	 */


	    @Override
	    public void configure() throws Exception {

	    	
	    	//Elegimos la ruta donde esta almacenado el archivo que se va a convertir
	    from("file:in?noop=true")
	    //le indicamos que nos transforme con .transform().body() 
	    .transform().body()
	    //indicamos la ruta de salida del archivo, especificando el formato con fileName=name.formato
	    .to("file:out/?fileName=otro.csv");
	    
	    
	    
	    }
}
	