package com.skillnet;
		
import org.apache.camel.builder.RouteBuilder;
		
public class HelloRouteBuilder extends RouteBuilder {
		
	@Override
	public void configure() throws Exception {
		
		
		
		//Indico la direccion de origen, es decir donde el programa va a tomar
		// el archivo; Noop=true es para indicar que solo lo copie.
		from("file:in?noop=true")
		//Indica la carpeta de salida con la extencion yo tipo de archivo
		.to("file:out?FileName=salida.txt&fileExist=Append");//&fileExist=Append
	}
	
}	
	