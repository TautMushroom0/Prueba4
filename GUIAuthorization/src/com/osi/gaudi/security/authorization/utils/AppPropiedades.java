package com.osi.gaudi.security.authorization.utils;

import java.util.Properties;

import com.osi.gaudi.security.authorization.util.PropertiesLoader;

public class AppPropiedades {
	
	
	private static PropertiesLoader loader = PropertiesLoader.getInstance();
	private static Properties properties = loader.getProperties("app.properties");
	
	public static String getSsoURLLogout(){
		return properties.getProperty("ssoURLLogout");
	}
	
	public static String getWsdlAuthentication(){
		return properties.getProperty("wsdlAuthentication");
	}
	
	public static String getWsdlAuthenticationLDAP(){
		return properties.getProperty("wsdlAuthenticationLDAP");
	}
	
	public static boolean getIntegraLoginAdmin(){
		return Boolean.valueOf(properties.getProperty("integraLoginAdmin")).booleanValue();
	}
}
