package com.osi.gaudi.security.authorization.seguridad;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;

import com.osi.gaudi.exception.Failure;
import com.osi.gaudi.security.authorization.beans.AuthtMediator;
import com.osi.gaudi.security.authorization.beans.IdentityBean;
import com.osi.gaudi.security.authorization.entity.Permission;
import com.osi.gaudi.security.authorization.entity.Role;
import com.osi.gaudi.security.authorization.utils.AppPropiedades;
import com.osi.gaudi.security.authorization.utils.FacesUtils;
import com.osi.gaudi.security.authorization.ws.IAuthorization;
import com.osi.gaudi.security.util.EEncryptingAlgorithm;
import com.osi.gaudi.security.util.EncryptingUtil;
import com.osi.security.authentication.AuthenticationPortType;
import com.osi.security.authentication.UserDTO;
import com.osi.security.authentication.UserResponseDTO;

public class Authenticator {
	private static Log log = LogFactory.getLog(Authenticator.class);
	
//	public static final String APP_NAME = Configurator.getInstance().getString("sistema","applicationName", "GUIAuthorization");
	public static final String APP_NAME = "GUIAuthorization";

	private String userName;
	private IAuthorization authorization;
	private AuthenticationPortType authentication;
	
	InitialContext contexto;
	
	public Authenticator() {
		log.info("Iniciando Autenticador");
		try {
			this.setUp();
			this.setUpAuthentication();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public List<Permission> permissionsReturn(String userName) {
		return this.getPermissions(userName);
	}
	
	public boolean authenticate(IdentityBean identity) throws RemoteException {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		// this.userName = (String)
		// session.getAttribute(SecurityFilter.ATTRIB_USER_NAME);
		boolean autenticado = false;
		Assertion assertion1 = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
		if (assertion1 != null && assertion1.getPrincipal() != null) {
			this.userName = assertion1.getPrincipal().getName();
		}
		if (this.userName != null) {
			log.info("Estableciendo login automatico con user: " + this.userName);
			session.removeAttribute("identity");
			session.removeAttribute("menuBean");
			try {
				List<Permission> permissions = getPermissions(this.userName);
				UserDTO user = (UserDTO) getUser(this.userName);
				List<Role> roles = getRoles(this.userName);
				if (user != null) {
					identity.setDisplayName(user.getName() + " " + user.getSurname());
					identity.setIdentifier(user.getIdentification());
					identity.setEmail(user.getEmail());
					identity.setTitle(user.getTitle());
					identity.setArea(user.getArea());
				} else {
					identity.setDisplayName(this.userName);
				}
				identity.setPermissions(permissions);
				identity.setRoles(roles);
				identity.setUserName(this.userName);
				FacesUtils.addBeanToSession("identity", identity);

				autenticado = true;

			} catch (Failure e) {
				log.error(e.getMessage(), e);
			}
		}
		return autenticado;
	}
	
	private List<Permission> getPermissions(String userName){
		return Arrays.asList(authorization.getPermissionsUser(userName, APP_NAME));
	}

	public void setUp() throws Exception {
		authorization = (IAuthorization) getAuthorization();
	}
	
	public void setUpAuthentication() throws Exception {
		authentication = getAuthentication();
	}

	/**
	 * Obtiene la lista de roles de un usuario
	 * 
	 * @param userName
	 *            Identificador del usuario
	 */
	private List<Role> getRoles(String userName) {
		return Arrays.asList(authorization.getRoles(userName, APP_NAME));
	}

	private UserDTO getUser(String userName) throws RemoteException {
		UserDTO usuario = null;
		String usuarioConsultaAuth = null;
		if (userName != null && !userName.isEmpty()) {
			usuarioConsultaAuth = EncryptingUtil.encrypt(userName, EEncryptingAlgorithm.AES);
			UserResponseDTO usuarioAuth = authentication.getUser(usuarioConsultaAuth);
			if (usuarioAuth != null && usuarioAuth.getUserDTO() != null) {
				usuario = usuarioAuth.getUserDTO();
			}
		}
		return usuario;
	}
	
	public List<Role> getRolesAll(String userName){
		return Arrays.asList(authorization.getRoles(userName, APP_NAME));
	}

	public IAuthorization getAuthorization() throws Exception {
		if(authorization==null){
			authorization=(IAuthorization) ServiceLocator.getInstance().getServiceAuthorization(EServices.Authorization.getRemoteName());
		}
		return authorization; 
	}
	
	
	public String logout(){
		
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		
//		String ssoURLLogout = Configurator.getInstance().getString("sistema","ssoURLLogout", "https://desarrollo.colsanitas.com:8443/sso/logout"); 
		String ssoURLLogout = AppPropiedades.getSsoURLLogout();
		
		try {
			context.redirect(ssoURLLogout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HttpSession session = (HttpSession) context.getSession(false);
		for(Enumeration<String> enume = session.getAttributeNames(); enume.hasMoreElements(); ){
			
			String variableSesion = (String) enume.nextElement();
			session.removeAttribute(variableSesion);
		}
		
		return "salir";
	}
	
	public AuthenticationPortType getAuthentication() {
		if (authentication == null) {
			authentication = ServiceLocator.getInstance().getServiceAuthentication(AuthtMediator.TIPO_DA);
		}
		return authentication;
	}
}
