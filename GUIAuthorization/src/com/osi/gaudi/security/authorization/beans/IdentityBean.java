package com.osi.gaudi.security.authorization.beans;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.osi.gaudi.security.authorization.entity.Permission;
import com.osi.gaudi.security.authorization.entity.Role;
import com.osi.gaudi.security.authorization.seguridad.Authenticator;

public class IdentityBean {
	
	private String userName;
	private String displayName;
	private String identifier;
	private String email;
	private String title;
	private String area;
	private List<Permission> permissions;
	private List<Role> roles;
	private List<Role> rolesAll;
	private Authenticator authenticator;
	private static Log log = LogFactory.getLog(MenuBean.class);
	
	public IdentityBean() throws RemoteException{
		log.info("Iniciando autenticacion");
		this.authenticator = new Authenticator();
		this.authenticator.authenticate(this);
	}


	public String logout(){ 
		return this.authenticator.logout();
	}
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<Role> getRolesAll() {
		return rolesAll;
	}
	public void setRolesAll(List<Role> rolesAll) {
		this.rolesAll = rolesAll;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
}
