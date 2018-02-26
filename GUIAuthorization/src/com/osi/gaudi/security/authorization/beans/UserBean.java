package com.osi.gaudi.security.authorization.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;

import com.osi.gaudi.cfg.Configurator;
import com.osi.gaudi.security.authorization.entity.User;
import com.osi.gaudi.security.authorization.util.PropertiesLoader;
import com.osi.gaudi.security.authorization.utils.AppPropiedades;
import com.osi.gaudi.security.authorization.utils.FacesUtils;
import com.osi.gaudi.security.authorization.ws.IAuthorization;
import com.osi.gaudi.security.util.EEncryptingAlgorithm;
import com.osi.gaudi.security.util.EncryptingUtil;
import com.osi.security.authentication.UserDTO;
import com.osi.security.authentication.UserResponseDTO;

public class UserBean {
	private String id;
	private String name;
	private String lastName;
	private String mail;
	private int blocked;
	private String description;
	
	private static PropertiesLoader loader = PropertiesLoader.getInstance();
	private static Properties properties = loader.getProperties("app.properties");

	private boolean userEmpty = true;

	private String userParamLdap;

	InitialContext context;
	IAuthorization authorization;
	private AuthtMediator authentication;

	private List<UserDTO> usersFind;

	List<User> usersWithoutRole = new ArrayList<User>();
	
	private static Log log = LogFactory.getLog(UserBean.class);

	public UserBean() throws Exception {
		this.setUp();
		this.usersFind = new ArrayList<UserDTO>();
//		this.setUpAuthentication(ServiceLocator.TIPO_ORIGEN_DA);
	}

	public void setUp() throws Exception {
		authorization = (IAuthorization) getAuthorization();
	}

	public void findUserLdap() {
		
		if (this.getUserParamLdap() != null && !this.getUserParamLdap().equals("")) {
			
			this.usersFind = new ArrayList<UserDTO>();
			UserResponseDTO responseDTO = null;
			
			String userNameEncrip = EncryptingUtil.encrypt(this.getUserParamLdap(), EEncryptingAlgorithm.AES);
			
			authentication = new AuthtMediator();
			responseDTO = authentication.getUser(this.getUserParamLdap(), userNameEncrip);
			
			if(responseDTO != null && responseDTO.getResponseCode() == 3){
				
				if (responseDTO.getUserDTO() != null) {
					usersFind.add(responseDTO.getUserDTO());
				}
			} else {
				FacesUtils.addMensajePagina("Usuario no encontrado");
			}
		} else {
			FacesUtils.addMensajePagina("Debe ingresar un criterio");
		}
	}

	public void addUser() {
		try {
			for (UserDTO userDto : usersFind) {
				User user = new User();
				user.setId(userDto.getUserName().trim());
				user.setName(userDto.getName().trim());
				user.setLastName(userDto.getSurname().trim());
				user.setMail(userDto.getEmail().trim());
				User userAuthorization = authorization.getUser(userDto.getUserName().trim());
				if (userAuthorization == null) {
					authorization.insert(user);
					FacesUtils.addMensajePagina("El usuario " + userDto.getUserName() + " fue registrado correctamente");
				} else {
					FacesUtils.addMensajePagina("El usuario " + userDto.getUserName() + " se encuentra registrado en el sistema");
				}
			}
		} catch (Exception e) {
			if (e.getCause() instanceof ConstraintViolationException)
				FacesUtils.addMensajePagina("No es posible agregar el usuario porque ya existe"); //$NON-NLS-1$
		}
	}
	
	public void addUserExtern() throws Exception {
		try {
			User user = new User();
			user.setId(this.getMail().trim());
			user.setName(this.getName().trim());
			user.setLastName(this.getLastName().trim());
			user.setMail(this.getMail().trim());
			
			authorization.insert(user);
			FacesUtils.addMensajePagina("El usuario "+ user.getMail() + " fue registrado");
			
		} catch (Exception e) {
			if (e.getCause() instanceof ConstraintViolationException){
				//FacesUtils.addMensajePagina("No es posible agregar el usuario porque ya existe"); //$NON-NLS-1$
			}else{
				FacesUtils.addMensajePagina("Se presento un error al crear el usuario. Consulte con el administrador del Sistema"); //$NON-NLS-1$
		    	e.printStackTrace();
		    	throw e;
			}
		}
	}

	public void populateBean(User entity, UserBean bean) throws IllegalAccessException, InvocationTargetException {
		BeanUtils.copyProperties(bean, entity);
	}

	public void populateEntity(UserBean bean, User entity) throws IllegalAccessException, InvocationTargetException {
		BeanUtils.copyProperties(entity, bean);
	}

	public void findUsersToDepure() {
		User[] list = authorization.getUsersWithoutRole();
		usersWithoutRole = new ArrayList<User>();
		for (User user : list) {
			usersWithoutRole.add(user);
		}

	}

	public void depureUsers() {
		for (User userRemove : usersWithoutRole) {
			this.authorization.deleteUser(userRemove);
			FacesUtils.addMensajePagina("Se eliminó el usuario: " + userRemove.getId());
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getBlocked() {
		return blocked;
	}

	public void setBlocked(int blocked) {
		this.blocked = blocked;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isUserEmpty() {
		return userEmpty;
	}

	public void setUserEmpty(boolean userEmpty) {
		this.userEmpty = userEmpty;
	}

	public InitialContext getContext() {
		try {
			
			Hashtable<String, String> environment = new Hashtable<String, String>();
			environment.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
			environment.put("java.naming.provider.url", properties.getProperty("url.jnp.privider"));
			environment.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			context = new InitialContext(environment);
		} catch (NamingException ex) {
			log.error("Error in CTX looking up %s because of %s while %s" + ex.getRemainingName() + ex.getCause() + ex.getExplanation());
		}
		return context;
	}

	private Object lookup(String jndiName) {
		Object service = null;
		try {
			service = getContext().lookup(jndiName);
		} catch (NamingException ex) {
			log.error("Error in looking up %s because of %s while %s" + jndiName + ex.getCause() + ex.getExplanation());
		}
		return service;
	}

	public IAuthorization getAuthorization() {
		return (IAuthorization) lookup("Authorization/remote");
	}

	public void setAuthorization(IAuthorization authorization) {
		this.authorization = authorization;
	}

	public String getUserParamLdap() {
		return userParamLdap;
	}

	public void setUserParamLdap(String userParamLdap) {
		this.userParamLdap = userParamLdap;
	}

	public List<UserDTO> getUsersFind() {
		return usersFind;
	}

	public void setUsersFind(List<UserDTO> usersFind) {
		this.usersFind = usersFind;
	}

	public List<User> getUsersWithoutRole() {
		return usersWithoutRole;
	}

	public void setUsersWithoutRole(List<User> usersWithoutRole) {
		this.usersWithoutRole = usersWithoutRole;
	}	
}
