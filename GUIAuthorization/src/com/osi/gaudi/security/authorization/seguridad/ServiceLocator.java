package com.osi.gaudi.security.authorization.seguridad;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import com.osi.gaudi.security.authorization.beans.AuthtMediator;
import com.osi.gaudi.security.authorization.util.PropertiesLoader;
import com.osi.gaudi.security.authorization.utils.AppPropiedades;
import com.osi.security.authentication.AuthenticationLocator;
import com.osi.security.authentication.AuthenticationPortType;

public class ServiceLocator {
	
//	private static Log log = LogFactory.getLog(ServiceLocator.class);
	
	private static ServiceLocator serviceLocator;
	
	public static String URL_ORIGEN_DA = AppPropiedades.getWsdlAuthentication();
	public static String URL_ORIGEN_LDAP = AppPropiedades.getWsdlAuthenticationLDAP();
	
	private static PropertiesLoader loader = PropertiesLoader.getInstance();
	private static Properties properties = loader.getProperties("app.properties");
	
	private InitialContext 	contexto;

	private ServiceLocator() {
		
		try {
			
			Hashtable<String, String> environment = new Hashtable<String, String>();
	        environment.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
	        environment.put("java.naming.provider.url", properties.getProperty("url.jnp.privider"));
	        environment.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
	        contexto = new InitialContext(environment);
		}  catch (NamingException ex) {
			System.err.printf("Error in CTX looking up %s because of %s while %s",
					ex.getRemainingName(),ex.getCause(), ex.getExplanation());
		}
	}

	/**
	 * 
	 * @return
	 */
	public static final ServiceLocator getInstance() {
		if (serviceLocator == null) {
			serviceLocator = new ServiceLocator();
		}
		return serviceLocator;
	}
	
	/**
	 * 
	 * @return
	 */
	public InitialContext getContexto() {
		return contexto;
	}

	/**
	 * CLIENTE VIEJO
	 * @param serviceName Authentication
	 * @return
	 * @throws ServiceLocatorException
	 */
	public AuthenticationPortType getServiceAuthentication(String tipoOrigenDatos) {
		
		AuthenticationLocator locator = new AuthenticationLocator();
		AuthenticationPortType port = null;
		
		String urlServicio = null;
		
		//valide que tipo de fuente de datos se va a consultar
		if(tipoOrigenDatos.equalsIgnoreCase(AuthtMediator.TIPO_DA)){
			
			//cuando se consulta la informacion del directorio activo
			urlServicio = ServiceLocator.URL_ORIGEN_DA;
		}else if(tipoOrigenDatos.equalsIgnoreCase(AuthtMediator.TIPO_LDAP)){
			
			//cuando se consulta la informacion del LDAP
			urlServicio = ServiceLocator.URL_ORIGEN_LDAP;
		}
		
		try{
			
//			log.error("Paht: "+Configurator.getInstance().getString("sistema",urlServicio, null));
			port = locator.getAuthenticationHttpSoap11Endpoint(new URL(urlServicio));
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return port;		
	}
	
	/**
	 * 
	 * @param serviceName Authorization
	 * @return
	 * @throws ServiceLocatorException
	 */
	public Object getServiceAuthorization(String serviceName) throws Exception {
		String newService = serviceName;
		try {
			Object service = getContexto().lookup(newService);
			return service;
		} catch (NamingException e) {
			throw new Exception("Cannot locate service Authorization :: " + newService);
		}
	}
}
