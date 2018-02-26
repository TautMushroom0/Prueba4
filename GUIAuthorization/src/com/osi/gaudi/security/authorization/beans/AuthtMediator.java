package com.osi.gaudi.security.authorization.beans;

import java.rmi.RemoteException;
import java.util.Properties;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;


import bsh.This;

import com.osi.gaudi.cfg.Configurator;
import com.osi.gaudi.security.authorization.seguridad.ServiceLocator;
import com.osi.gaudi.security.authorization.util.PropertiesLoader;
import com.osi.security.authentication.AuthenticationPortType;
import com.osi.security.authentication.InfoAuthextDTO;
import com.osi.security.authentication.UserResponseDTO;

public class AuthtMediator {
	
	private AuthenticationPortType authentication;
	
	private static Log log = LogFactory.getLog(AuthtMediator.class);
	
	private PropertiesLoader loader = PropertiesLoader.getInstance();
	private Properties properties = loader.getProperties("app.properties");
	public static final String TIPO_DA = "DA";
	public static final String TIPO_LDAP = "LDAP";
	private static final int RESPUESTA_OK_SERVICE = 3;
	
	public AuthtMediator(){
		
		authentication = ServiceLocator.getInstance().getServiceAuthentication(AuthtMediator.TIPO_DA);
	}
	
	/**
	 * consultamos el usuario
	 * @param userNameEncrip
	 */
	public UserResponseDTO getUser(String userName, String userNameEncrip){
		
		//cargamos la variable de propiedades que define si esta activa la consulta al LDAP
		boolean consultaLDAP =  new Boolean(properties.getProperty("consulta.ldap")).booleanValue();
		boolean consultaDA =  new Boolean(properties.getProperty("consulta.da")).booleanValue();
		
		boolean userOkDA = false;
		boolean userOkLDAP = false;
		UserResponseDTO responseDTO = null;
		
		String origen = "";
	
		if(consultaDA){
		
			System.out.println("ENTRO EN CONSULTAR POR DA");
			try{
				
				log.error("Consultando: " + userName + " [ " + userNameEncrip + " ] " +
						"en DA [ " + ServiceLocator.URL_ORIGEN_DA);
				
				//cargue la consulta para que primero vaya a DA
				authentication = ServiceLocator.getInstance().getServiceAuthentication(AuthtMediator.TIPO_DA);
				responseDTO = authentication.getUser(userNameEncrip);
				
				//respuesta ok del servidor
				if(responseDTO.getResponseCode() == RESPUESTA_OK_SERVICE){
					userOkDA = true;
				}
				
			} catch (RemoteException e) {
				log.error("NO ENCONTRO EL USUARIO O ERROR AL CONSULTAR DA: " + e.getMessage());
				responseDTO = null;
			}
		}
		
			
		/**
		 * Si no trajo respuesta del DA y 
		 * si esta activa la consulta de LDAP
		 */
		if(responseDTO == null && consultaLDAP && !userOkDA){
			
			System.out.println("ENTRO EN CONSULTAR POR LDAP");
			
			try{
				
				log.error("Consultando: " + userName + " [ " + userNameEncrip + " ] " +
						"en LDAP [ " + ServiceLocator.URL_ORIGEN_LDAP);
				
				//cargue la consulta para que vaya a LDAP
				authentication = ServiceLocator.getInstance().getServiceAuthentication(TIPO_LDAP);
				responseDTO = authentication.getUser(userNameEncrip);
				
				//respuesta ok del servidor
				if(responseDTO.getResponseCode() == RESPUESTA_OK_SERVICE){
					userOkLDAP = true;
				}
			} catch (RemoteException e) {
				log.error("NO ENCONTRO EL USUARIO O ERROR AL CONSULTAR LDAP: " + e.getMessage());
			}
			
			if(responseDTO != null && userOkLDAP){
				origen = TIPO_LDAP;
				log.error("Usuario encontrado en " + TIPO_LDAP + " => " + responseDTO.getUserDTO().getTitle());
			}else{
				log.error("NO ENCONTRO EL USUARIO O ERROR AL CONSULTAR LDAP: " +  responseDTO.getMessage());
				responseDTO = null;
			}
		}else if(responseDTO != null && consultaLDAP){
			origen = TIPO_DA;
			log.error("Usuario encontrado en " + TIPO_DA + " => " + responseDTO.getUserDTO().getTitle());
		}
		
		if(responseDTO != null && responseDTO.getUserDTO().getTipo_identificacion() == null){
			responseDTO.getUserDTO().setTipo_identificacion(origen);
		}
		return responseDTO;
	}
	
	public UserResponseDTO changeUserStatus(String userEncryp, String estadoEncryp) throws RemoteException{
		
		return authentication.changeUserStatus(userEncryp, estadoEncryp);
	}
	
	public UserResponseDTO rememberPassword(String userEncryp) throws RemoteException{
		
		return authentication.rememberPassword(userEncryp);
	}
	
	public UserResponseDTO createInfAuthentication(InfoAuthextDTO dto) throws RemoteException{
		
		return authentication.createInfAuthentication(dto);
	}
}
