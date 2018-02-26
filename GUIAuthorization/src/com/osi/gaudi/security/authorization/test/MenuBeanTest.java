package com.osi.gaudi.security.authorization.test;

import java.util.Hashtable;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.osi.gaudi.security.authorization.beans.MenuBean;
import com.osi.gaudi.security.authorization.business.IPermission;
import com.osi.gaudi.security.authorization.entity.Permission;

public class MenuBeanTest extends TestCase {

	InitialContext contexto;
	IPermission iPermission;
	MenuBean menubean;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		contexto = getContext();

		Object service = contexto.lookup("PermissionAction/remote"); //$NON-NLS-1$
		iPermission = (IPermission) service;
		
		List<Permission> perList = iPermission.getPermissions("avargas", "CECOM");
		
		menubean = new MenuBean(perList);
	}
	
	public InitialContext getContext() {
		InitialContext contexto = null;
		try {
			Hashtable<String, String> environment = new Hashtable<String, String>();
			environment.put("java.naming.factory.initial", //$NON-NLS-1$
					"org.jnp.interfaces.NamingContextFactory"); //$NON-NLS-1$
			environment.put("java.naming.provider.url", "localhost:1099"); //$NON-NLS-1$ //$NON-NLS-2$
			environment.put("java.naming.factory.url.pkgs", //$NON-NLS-1$
					"org.jboss.naming:org.jnp.interfaces"); //$NON-NLS-1$
			contexto = new InitialContext(environment);
		} catch (NamingException ex) {
			System.err.printf("Error in CTX looking up %s because of %s while %s", ex //$NON-NLS-1$
					.getRemainingName(), ex.getCause(), ex.getExplanation());
		}
		return contexto;
	}

	
	public void testLlenarListaMenu() {
		menubean.llenarListaMenu();
	}

}
