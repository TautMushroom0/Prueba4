package com.osi.gaudi.security.authorization.utils;

import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("deprecation")
public class FacesUtils {

	public static String getRequestParameter(String parameterName){
		return (String) ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter(parameterName);
	}
	
	public static void addBeanToSession(String parameterName, Object parameterSession){
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(parameterName,parameterSession );
	}
	
	public static void addMensajePagina(String mensaje){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage mensajeFaces = new FacesMessage(mensaje);
		context.addMessage(null, mensajeFaces);
	}
	
	public static void addMensajePagina(String id,String mensaje){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage mensajeFaces = new FacesMessage(mensaje);
		context.addMessage(id, mensajeFaces);
	}
	
	public static Object getManagedBean(String beanName) {
		Object o = getValueBinding(getJsfEl(beanName)).getValue(FacesContext.getCurrentInstance());
		return o;
	}
	
	public static void resetManagedBean(String beanName) {
		getValueBinding(getJsfEl(beanName)).setValue(FacesContext.getCurrentInstance(), null);
	}
	
	
	public static void setManagedBeanInRequest(String beanName, Object managedBean) {
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(beanName, managedBean);
	}
	
	public static void setManagedBeanInSession(String beanName, Object managedBean) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(beanName, managedBean);
	}
	public static void deleteManagedBeanInRequest(String beanName){
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().remove(beanName);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String[]> obtenerMap(){
		return ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getContext()).getParameterMap();
	}
	
	@SuppressWarnings("unchecked")
	public static void setParameterInRequest(String parameterName, String[] value) {
		((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameterMap().put(parameterName, value);
	}
	
	private static String getJsfEl(String value) {
		return "#{" + value + "}";
	}
	
	private static ValueBinding getValueBinding(String el) {
		return getApplication().createValueBinding(el);
	}
	
	private static Application getApplication() {
		ApplicationFactory appFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
		return appFactory.getApplication();
	}

}
