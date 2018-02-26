package com.osi.gaudi.security.authorization.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIData;
import javax.faces.component.UIMessages;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;

import com.osi.gaudi.security.authorization.comparator.DescListPermissionComparator;
import com.osi.gaudi.security.authorization.comparator.LastNameComparator;
import com.osi.gaudi.security.authorization.comparator.NameComparator;
import com.osi.gaudi.security.authorization.comparator.NamePermissionListComparator;
import com.osi.gaudi.security.authorization.comparator.UserNameComparator;
import com.osi.gaudi.security.authorization.entity.Domain;
import com.osi.gaudi.security.authorization.entity.GroupRole;
import com.osi.gaudi.security.authorization.entity.Permission;
import com.osi.gaudi.security.authorization.entity.PermissionRole;
import com.osi.gaudi.security.authorization.entity.Role;
import com.osi.gaudi.security.authorization.entity.User;
import com.osi.gaudi.security.authorization.entity.UserRole;
import com.osi.gaudi.security.authorization.helper.PermissionHelper;
import com.osi.gaudi.security.authorization.helper.UserHelper;
import com.osi.gaudi.security.authorization.seguridad.EServices;
import com.osi.gaudi.security.authorization.seguridad.ServiceLocator;
import com.osi.gaudi.security.authorization.utils.FacesUtils;
import com.osi.gaudi.security.authorization.ws.IAuthorization;

public class RoleBean {

	private long id;
	private String name;
	private String lastName;
	private String description;

	private String shortNameDomain;
	private long idDomain = 0;

	private long groupRoleKey = 0;

	private Role role = new Role();
	private Role roleUpd = new Role();
	private List<Role> roleList;
	private List<SelectItem> groupRoleList;
	private UIData roles;
	private UIData user;
	private UIData userFind;
	InitialContext context;
	IAuthorization authorization;

	private boolean viewPopupPermission;
	private boolean viewPopupRole;
	private boolean viewPopupUser;

	private String roleName;
	private long idRole;

	private boolean viewPermissionList;
	private boolean permissionNoAtaEmpty;

	private boolean renderPaginator;
	private boolean renderPagUserAssociate;
	
	private boolean renderPaginatorPermission;
	private boolean renderPaginatorPermNoAta;
	private boolean renderPaginatorUser;
	private final int rowsNumber = 10;

	private List<PermissionHelper> permNoAttach;
	private List<PermissionHelper> permissions;
	private List<UserHelper> users;
	private List<UserHelper> usersAssociate;
	private List<UserHelper> usersNoAttach;
	private boolean permissionsEmpty = true;
	private boolean usersEmpty = true;	
	private boolean usersFindEmpty = true;
	private boolean usersTmpEmpty = true;
	private boolean roleListEmpty;

	private String userName;
	
	private List<User> usersFind;
	private List<User> usersTemp = new ArrayList<User>();
	private static Log log = LogFactory.getLog(RoleBean.class);
	private UIMessages errorNombre;

	public RoleBean() {
		try {
			this.groupRoleList = new ArrayList<SelectItem>();
//			this.groupRoleList.add(new SelectItem(new Long(0),"----"));
			this.setUp();
		} catch (Exception e) {
			log.error(e.getMessage(), e);	
		}
	}

	public void setUp() throws Exception {
		authorization = getAuthorization();
	}

	public void reloadGroupRoleList(ValueChangeEvent evt) {
		this.permissionsEmpty=true;
		this.setRoleName("");
		if ((Long)evt.getNewValue() != 0) {
			this.groupRoleList = new ArrayList<SelectItem>();
			this.permissions = new ArrayList<PermissionHelper>();
			this.renderPaginatorPermission = false;
			this.users = new ArrayList<UserHelper>();
			this.renderPaginatorUser = false;
			this.roleName = "";
			this.setIdDomain(new Long(evt.getNewValue().toString()));
			if (this.getIdDomain() != 0) {
				Domain domain = authorization.getDomain(this.getIdDomain());
				this.setShortNameDomain(domain.getShortName());
				GroupRole[] list = authorization.getGroupRolesbyDomain(this.getIdDomain());
				List<GroupRole> groupRoles = new ArrayList<GroupRole>();
				for (GroupRole groupRole : list) {
					groupRoles.add(groupRole);
				}
//				this.groupRoleList.add(new SelectItem(new Long(0),"----"));
				for (GroupRole groupRole : groupRoles) {
					this.groupRoleList.add(new SelectItem(groupRole.getId(), groupRole.getName()));
				}
			}
			this.roleList = new ArrayList<Role>();
		} else {
			this.setIdDomain(new Long(0));
			this.roleList = new ArrayList<Role>();
			this.groupRoleList = new ArrayList<SelectItem>();
//			this.groupRoleList.add(new SelectItem(new Long(0),"----"));
			this.roleName="";
			this.permissions = new ArrayList<PermissionHelper>();
			this.users = new ArrayList<UserHelper>();
		}
	}

	public void reloadRoleList(ValueChangeEvent evt) {
		this.permissionsEmpty=true;
		this.setRoleName("");
		if (evt.getNewValue() != null) {
			this.roleList = new ArrayList<Role>();
			this.permissions = new ArrayList<PermissionHelper>();
			this.renderPaginatorPermission = false;
			this.setGroupRoleKey(Long.valueOf(evt.getNewValue().toString()).longValue());
			Role[] list = authorization.getRolesByDomainGrpRole(this.getGroupRoleKey(), this.getShortNameDomain());
			for (Role role : list) {
				this.roleList.add(role);
			}
			if (roleList.size() > rowsNumber) {
				this.renderPaginator = true;
			} else {
				this.renderPaginator = false;
			}
			if (roleList.size() == 0) {
				this.roleListEmpty = true;
			} else {
				this.roleListEmpty = false;
			}
		}
	}

	public void populateRoleList() {
		this.roleList = new ArrayList<Role>();
		Role[] list = authorization.getRolesByDomainGrpRole(this.getGroupRoleKey(), this.getShortNameDomain());
		for (Role role : list) {
			this.roleList.add(role);
		}
		if (roleList.size() > rowsNumber) {
			this.renderPaginator = true;
		} else {
			this.renderPaginator = false;
		}
	}

	public String deleteRole() {
		int index = getRoles().getRowIndex();
		Role roleDel = this.roleList.get(index);
		try {
			authorization.deleteRole(roleDel);
			FacesUtils.addMensajePagina("Rol eliminado satisfactoriamente");
			this.populateRoleList();
			return "regresar";
		} catch (Exception e) {
			if (e.getCause().getCause().getCause() instanceof ConstraintViolationException)
				FacesUtils.addMensajePagina("No es posible eliminar el Rol porque existen registros asociados"); //$NON-NLS-1$
			return "";
		}
	}

	public String addRole() {
		if (this.getIdDomain() != 0 && this.getGroupRoleKey() != 0) {
			Role role = new Role();
			role.setDomain(authorization.getDomain(this.getIdDomain()));
			role.setGroupRole(authorization.getGroupRole(this.getGroupRoleKey()));
			role.setName(this.getName());
			role.setDescription(this.getDescription());
			authorization.insert(role);
			this.populateRoleList();
			FacesUtils.addMensajePagina("Rol adicionado satisfactoriamente");
			return "regresar";
		} else {
			return "";
		}

	}

	public String updateRole() {
		Role objectModify = this.roleUpd;
		objectModify.setDomain(authorization.getDomain(this.getIdDomain()));
		objectModify.setGroupRole(authorization.getGroupRole(this.getGroupRoleKey()));
		if (validate(objectModify.getName(), "Upd")) {
			authorization.modify(objectModify);
			FacesUtils.addMensajePagina("Rol actualizado satisfactoriamente");
			this.populateRoleList();
			return "regresar";
		} else {
			return "";
		}
	}

	public boolean validate(String name, String action) {
		if (name == null || name.equals("")) {
			if (action.equals("Add")) {
				FacesUtils.addMensajePagina("domainForm:" + "name" + action, "Nombre es requerido"); //$NON-NLS-1$
			} else {
				FacesUtils.addMensajePagina("domainForm:" + "name" + action, "Nombre es requerido"); //$NON-NLS-1$
			}
			return false;
		} else {
			return true;
		}
	}

	public String verAction() {
		int index = roles.getRowIndex();
		this.roleUpd = this.roleList.get(index);
		return "detalle";
	}

	public void showPopupRole() {
		if (this.getIdDomain() != 0) {
			if (this.getGroupRoleKey() != 0) {
				Domain domainTmp = authorization.getDomain(this.getIdDomain());
				this.setShortNameDomain(domainTmp.getShortName());
				this.viewPopupRole = true;				
				populateRoleList();
			} else {
				FacesUtils.addMensajePagina("Debe seleccionar un Grupo Rol");
			}
		} else {
			FacesUtils.addMensajePagina("Debe seleccionar un Dominio");
		}
	}

	public void loadRoleName() {
		this.viewPopupRole = false;
		this.idRole = new Long((String) FacesUtils.getRequestParameter("roleKey"));
		this.role = authorization.getRoleById(this.idRole);
		Role roleTmp = authorization.getRoleById(this.idRole);
		this.roleName = roleTmp.getName();
		this.viewPermissionList = true;
		this.populatePermissions();
		this.populateUsers();
	}

	public void closePopupRole() {
		this.viewPopupRole = false;
	}

	public void populatePermissions() {
		this.permissions = new ArrayList<PermissionHelper>();
		Permission[] list = authorization.getPermissions(this.idRole, this.getShortNameDomain());
		List<Permission> permissionsTmp = new ArrayList<Permission>();
		for (Permission permission : list) {
			permissionsTmp.add(permission);
		}
		for (Permission permission : permissionsTmp) {
			PermissionHelper permissionHelper = new PermissionHelper();
			try {
				BeanUtils.copyProperties(permissionHelper, permission);
			} catch (IllegalAccessException e) {
				log.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				log.error(e.getMessage(), e);
			}
			this.permissions.add(permissionHelper);
		}
		if (permissions.isEmpty()) {
			this.setPermissionsEmpty(true);
			FacesUtils.addMensajePagina("No hay permisos asociados");
		} else {
			this.setPermissionsEmpty(false);
		}
		if (this.permissions.size() > rowsNumber) {
			this.renderPaginatorPermission = true;
		} else {
			this.renderPaginatorPermission = false;
		}
	}

	public void showPanel() {
		this.setViewPopupPermission(true);
		this.setDescription("");
		this.populatePermNotAttach();
	}

	private void populatePermNotAttach() {
		this.permNoAttach = new ArrayList<PermissionHelper>();
		Permission[] list = authorization.getPermNotAttachRole(this.getIdDomain(), this.getIdRole());
		List<Permission> permNoAttachTmp = new ArrayList<Permission>();
		for (Permission permission : list) {
			permNoAttachTmp.add(permission);
		}

		for (Permission permission : permNoAttachTmp) {
			PermissionHelper permissionHelper = new PermissionHelper();
			try {
				BeanUtils.copyProperties(permissionHelper, permission);
			} catch (IllegalAccessException e) {
				log.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				log.error(e.getMessage(), e);
			}
			this.permNoAttach.add(permissionHelper);
		}
		if (this.permNoAttach.size() > this.rowsNumber) {
			this.renderPaginatorPermNoAta = true;
		} else {
			this.renderPaginatorPermNoAta = false;
		}
		if (this.permNoAttach.size() == 0) {
			this.permissionNoAtaEmpty = true;
		} else {
			this.permissionNoAtaEmpty = false;
		}
	}
	public void findAll() {
		this.populatePermNotAttach();
	}
	public void findDescPermNoAttach() {
		this.permNoAttach = new ArrayList<PermissionHelper>();
		Permission[] list = authorization.getPermNotAttachRole(this.getIdDomain(), this.getIdRole());
		List<Permission> permNoAttachTmp = new ArrayList<Permission>();
		for (Permission permission : list) {
			permNoAttachTmp.add(permission);
		}

		for (Permission permission : permNoAttachTmp) {
			PermissionHelper permissionHelper = new PermissionHelper();
			try {
				BeanUtils.copyProperties(permissionHelper, permission);
			} catch (IllegalAccessException e) {
				log.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				log.error(e.getMessage(), e);
			}
			if (permission.getDescription().trim().equals(this.description)) {
				this.permNoAttach.add(permissionHelper);
			}			
		}
		if (this.permNoAttach.size() > this.rowsNumber) {
			this.renderPaginatorPermNoAta = true;
		} else {
			this.renderPaginatorPermNoAta = false;
		}
		if (this.permNoAttach.size() == 0) {
			this.permissionNoAtaEmpty = true;
		} else {
			this.permissionNoAtaEmpty = false;
		}
	}

	public void addPermissionsToRole() {
		this.setViewPopupPermission(false);
		this.setDescription("");
		List<Permission> permissionsSelected = new ArrayList<Permission>();
		for (PermissionHelper element : permNoAttach) {
			if (element.isSelected()) {
				Permission permissionTmp = new Permission();
				try {
					BeanUtils.copyProperties(permissionTmp, element);
					permissionsSelected.add(permissionTmp);
				} catch (IllegalAccessException e) {
					log.error(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		for (Permission permission : permissionsSelected) {
			PermissionRole permissionRole = new PermissionRole();
			permissionRole.setPermission(permission);
			permissionRole.setRole(this.getRole());
			authorization.insert(permissionRole);
			FacesUtils.addMensajePagina("Se asoció el permiso: " + permission.getDescription());
		}
		this.populatePermissions();
	}

	public void deletePermissionsFromRole() {
		for (PermissionHelper permissionHelper : this.permissions) {
			if (permissionHelper.isSelected()) {
				Permission permission = new Permission();
				try {
					BeanUtils.copyProperties(permission, permissionHelper);
				} catch (IllegalAccessException e) {
					log.error(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					log.error(e.getMessage(), e);
				}
				authorization.deleteFromPermissionRole(permission.getId(), this.getRole().getId());
				FacesUtils.addMensajePagina("Se desasoció el permiso: " + permission.getDescription());
			}
		}
		this.populatePermissions();
	}

	// MÃ©todos para adicionar usuarios al rol

	public void populateUsers() {
		this.users = new ArrayList<UserHelper>();
		User[] usersArray = authorization.getUsers(this.idRole, this.getShortNameDomain());
		
		if (usersArray != null) {
			for (int i = 0; i < usersArray.length; i++) {
				User userTmp = usersArray[i];
				UserHelper userHelper = new UserHelper();
				userHelper.setId(userTmp.getId());
				userHelper.setLastName(userTmp.getLastName());
				userHelper.setName(userTmp.getName());
				this.users.add(userHelper);
			}
		}
		if (this.users.size() == 0) {
			this.usersEmpty = true;
		} else {
			this.usersEmpty = false;
		}
		if (this.users.size() > rowsNumber) {
			this.renderPaginatorUser = true;
		} else {
			this.renderPaginatorUser = false;
		}
	}

	public void showPanelUser() {
		
		if (this.getIdDomain() != 0) {
			if (this.getGroupRoleKey() != 0) {
				if (this.getIdRole() >0) {
					this.viewPopupUser = true;
					setUserName("");
					this.usersFind = new ArrayList<User>();
					this.usersTemp.clear();
					this.usersEmpty=true;
					this.usersTmpEmpty=true;
					this.usersFindEmpty=true;
				}
				else{
					FacesUtils.addMensajePagina("Debe ingresar un Rol");
				}
			} else {
				FacesUtils.addMensajePagina("Debe seleccionar un Grupo Rol");
			}
		} else {
			FacesUtils.addMensajePagina("Debe seleccionar un Dominio");
		}
	}

	public void addUserToRole() {
		String userName="";
		try {			
			boolean flag=false;
				iter:	for (User user: this.usersTemp) {
							if(validateUserDuplicate(user.getId())){
								UserRole userRole = new UserRole();
								userRole.setRole(authorization.getRoleById(this.getIdRole()));
								userRole.setUser(user);
								userName=user.getId();
								authorization.insert(userRole);								
							}
							else{			
								FacesUtils.addMensajePagina("El usuario con login : " + user.getId() +" Ya esta asociado al rol "+ this.getRoleName());
								break iter;
							}	
							flag=true;
						}
					if(flag){
						this.usersTemp.clear();						
						this.viewPopupUser = false;												
						FacesUtils.addMensajePagina("La asociación de usuarios se ha realizado satisfactoriamente");
					}
					this.populateUsers();
						
		} catch (Exception e) {
			if (e.getCause().getCause().getCause() instanceof ConstraintViolationException)
				FacesUtils.addMensajePagina("No es posible agregar el usuario " + userName+ " porque ya existe"); //$NON-NLS-1$
		}
	}
	

	/**
		 * Metodo que permite validar que un usuario no posea varios roles
		 * @return retorno variable booleana que indica si ya posee un rol
		 */
	public boolean validateUserDuplicate(String userName){
		boolean retorno= true;
		Role[] rolesByUser= authorization.getRolesByUserName(userName);
		roles: for (Role role : rolesByUser) {
			    if(this.getIdRole()==role.getId()){
			    	  retorno = false;
					  break roles; 
			    }
		}
		return retorno;		
	}
	

	public void deleteUsersFromRole() {
		int cont=0;
		for (UserHelper userAttach : this.users) {
			if (userAttach.isSelected()) {
				authorization.deleteFromUserRole(userAttach.getId(), this.getIdRole());
				FacesUtils.addMensajePagina("Se desasoció el usuario: " + userAttach.getId());
				cont++;
				this.populateUsers();
			}			
		}
		if(cont==0){
			FacesUtils.addMensajePagina("Debe seleccionar por lo menos un usuario");
		}
		
		
	}

	public void findUser() {
		this.usersFind = new ArrayList<User>();
		User user = authorization.getUser(this.getUserName());
		if (user != null) {
			this.usersFind.add(user);
		} else {
			FacesUtils.addMensajePagina("No se encontró el usuario "+ this.getUserName()+" en la Base de Datos");
			this.userName = "";
		}
		if (this.usersFind.size() == 0) {
			this.usersFindEmpty = true;
		} else {
			this.usersFindEmpty = false;
		}
		if (this.usersTemp.size() == 0) {
			this.usersTmpEmpty = true;
		} else {
			this.usersTmpEmpty = false;
		}		
	}
	public void orderListUserName(){
		  Collections.sort(users, new UserNameComparator());  
	}
	public void orderListName(){
		  Collections.sort(users, new NameComparator());  
	}
	public void orderListLastName(){
		  Collections.sort(users, new LastNameComparator());  
	}
	public void orderListUserNameAssoc(){
		  Collections.sort(usersAssociate, new UserNameComparator());  
	}
	public void orderListNameAssoc(){
		  Collections.sort(usersAssociate, new NameComparator());  
	}
	public void orderListLastNameAssoc(){
		  Collections.sort(usersAssociate, new LastNameComparator());  
	}
	public void orderListNamePermission(){
		  Collections.sort(permissions, new NamePermissionListComparator());  
	}
	public void orderListDescPermission(){
		  Collections.sort(permissions, new DescListPermissionComparator());  
	}
	public void orderListNamePermNoAttach(){
		  Collections.sort(permNoAttach, new NamePermissionListComparator());  
	}
	public void orderListDescPermNoAttach(){
		  Collections.sort(permNoAttach, new DescListPermissionComparator());  
	}
	
	/**
	 * Metodo que permite realizar busqueda de permisos por descripción
	 */
	public void findDescription(){
		if (!this.description.equals("")) {
			this.permissions = new ArrayList<PermissionHelper>();
			Permission[] list = authorization.getPermissions(this.idRole, this
					.getShortNameDomain());
			List<Permission> permissionsTmp = new ArrayList<Permission>();
			for (Permission permission : list) {
				permissionsTmp.add(permission);
			}			
			for (Permission permission : permissionsTmp) {
				PermissionHelper permissionHelper = new PermissionHelper();
				try {
					BeanUtils.copyProperties(permissionHelper, permission);
				} catch (IllegalAccessException e) {
					log.error(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					log.error(e.getMessage(), e);
				}
				if (permission.getDescription().trim().equals(this.description)) {
					this.permissions.add(permissionHelper);
				}
			}
			
			if (permissions.isEmpty()) {
				this.setPermissionsEmpty(true);	
				FacesUtils.addMensajePagina("No se encuentran permisos asociados segun criterio de busqueda");
			} else {
				this.setPermissionsEmpty(false);
			}
			if (this.permissions.size() > rowsNumber) {
				this.renderPaginatorPermission = true;
			} else {
				this.renderPaginatorPermission = false;
			}
		} else {
			FacesUtils.addMensajePagina("Debe ingresar descripción");
		}
		
	}
	
	public void addUsersListTempRole() {
		this.usersAssociate= new ArrayList<UserHelper>();
		for (UserHelper userAttach : this.users) {
			if (userAttach.isSelected()) {
				usersAssociate.add(userAttach);				
				this.populateUsers();
			}
		}
	}

	/**
		 * Metodo que permite filtrar busqueda de usuarios por Id, nombre, apellido
		 */
	public void findUserByAll(){
		 populateUsers();		  
		  
	        if(!this.userName.equals("")&&!this.name.equals("")&&!this.lastName.equals("")){
	        	this.users=getObject(this.users, 1);		        	
	        }
	        else {
	        	if(!this.userName.equals("")&&!this.name.equals("")){
	        		this.users=getObject(this.users, 2);
	        	}
	        	else {		        		
	        		if(!this.userName.equals("")&&!this.lastName.equals("")){
	        			this.users=getObject(this.users, 3);
	        		}
	        		else {
	        			if(!this.name.equals("")&&!this.lastName.equals("")){
	        				this.users=getObject(this.users, 4);
	        			}else {
	        				if(!this.userName.equals("")){
		        				this.users=getObject(this.users, 5);
		        			}else {
		        				if(!this.name.equals("")){
			        				this.users=getObject(this.users, 6);
			        			}else {
			        				if(!this.lastName.equals("")){
				        				this.users=getObject(this.users, 7);
				        			}else {
				        				FacesUtils.addMensajePagina("Debe ingresar un criterio de busqueda");	
									}	
								}	
							}		
						}	
					}
				}
			}
	        if (this.users.size() > rowsNumber) {
				this.renderPaginatorUser = true;
			} else {
				this.renderPaginatorUser = false;
			}
	        if(this.users.size()==0){
	        	this.usersEmpty=true;
        		FacesUtils.addMensajePagina("No existen usuarios relacionados al criterio de busqueda");        		
        	}
	}

	
	/**
	 * Metodo que permite limpiar los campos de busqueda de usuarios
	 */
	public void clearField(){
		setUserName("");
		setName("");
		setLastName("");		
	}
	
	/**
		 * Metodo que permite realizar busqueda de usuarios segun id, nombre, apellido
		 * @param usersHelper lista de usuarios
		 * @param combination bandera que permite realizar la busqueda
		 * @return usersHelperTmp  lista de usuarios que coinciden con el criterio de busqueda
		 */
	 public List<UserHelper> getObject(List<UserHelper> usersHelper,int combination) {
		 
		 List<UserHelper> usersHelperTmp = new ArrayList<UserHelper>();
		 for (Iterator iterator = usersHelper.iterator(); iterator.hasNext();) {
			 UserHelper userHelper= (UserHelper) iterator.next();
			switch (combination) {
			case 1:
				if (userHelper.getId().trim().equalsIgnoreCase(this.userName.trim())&& 
					userHelper.getName().trim().equalsIgnoreCase(this.name.trim())&& 
					userHelper.getLastName().trim().equalsIgnoreCase(this.lastName.trim())) {
					usersHelperTmp.add(userHelper);
				}
				break;
			case 2:
				if (userHelper.getId().trim().equalsIgnoreCase(this.userName.trim())&& 
					userHelper.getName().trim().equalsIgnoreCase(this.name.trim())) {
					usersHelperTmp.add(userHelper);
				}

				break;
			case 3:if (userHelper.getId().trim().equalsIgnoreCase(this.userName.trim())&& 
					userHelper.getLastName().trim().equalsIgnoreCase(this.lastName.trim())) {				
				    usersHelperTmp.add(userHelper);
				}

				break;
			case 4:
				if (userHelper.getName().trim().equalsIgnoreCase(this.name.trim())&&
					userHelper.getLastName().trim().equalsIgnoreCase(this.lastName.trim())) {
					usersHelperTmp.add(userHelper);				
				}

				break;
			case 5:
				if (userHelper.getId().trim().equalsIgnoreCase(this.userName.trim())) {
					usersHelperTmp.add(userHelper);
				}

				break;
			case 6:
				if (userHelper.getName().trim().equalsIgnoreCase(this.name.trim())) {
					usersHelperTmp.add(userHelper);					
				}

				break;
			case 7:
				if (userHelper.getLastName().trim().equalsIgnoreCase(this.lastName.trim())) {
					usersHelperTmp.add(userHelper);
				}
				break;
			default:log.error("Opcion desconocida");
				break;
			}

		}
		return usersHelperTmp;
	}
	 
    
	 
	public void closePopupUser() {
		this.viewPopupUser = false;
		this.populateUsers();
	}

	public boolean isViewPopupPermission() {
		return viewPopupPermission;
	}

	public void setViewPopupPermission(boolean viewPopupPermission) {
		this.viewPopupPermission = viewPopupPermission;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public void populateBean(User entity, UserBean bean) throws IllegalAccessException, InvocationTargetException {
		BeanUtils.copyProperties(bean, entity);
	}

	public void populateEntity(UserBean bean, User entity) throws IllegalAccessException, InvocationTargetException {
		BeanUtils.copyProperties(entity, bean);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getGroupRoleKey() {
		return groupRoleKey;
	}

	public void setGroupRoleKey(long groupRoleKey) {
		this.groupRoleKey = groupRoleKey;
	}

	public List<PermissionHelper> getPermNoAttach() {
		return permNoAttach;
	}

	public void setPermNoAttach(List<PermissionHelper> permNoAttach) {
		this.permNoAttach = permNoAttach;
	}

	public IAuthorization getAuthorization() throws Exception {
		if(authorization==null){
			authorization= (IAuthorization) ServiceLocator.getInstance().getServiceAuthorization(EServices.Authorization.getRemoteName());
		}
		return authorization;
	}

	/**
	 * Metodo que permite adicionar a la lista de usuarios por asociar
	 */
	public void addListUsers(){
		int index = getUserFind().getRowIndex();
		User user=usersFind.get(index);
		if(validateDuplicUserList(this.usersTemp, user.getId())){
				usersTemp.add(usersFind.get(index));
			 	usersFind.remove(index);
				usersFindEmpty=true;
				usersTmpEmpty=false;
				setUserName("");			
		}
		else {
			FacesUtils.addMensajePagina("El usuario: "+ this.usersFind.get(index).getId()+ " ya se adicionó a la lista de usuarios por asociar");
		}
	}
	/**
	 * Metodo que permite validar la duplicidad del usuario en la lista de usuarios por asociar
	 */
	public boolean validateDuplicUserList(List<User> userList, String userName){
		boolean retorno= true;
		users: for (User userL : userList) {
			    if(userL.getId().equals(userName.trim())){
			    	  retorno = false;
					  break users; 
			    }
		}
		return retorno;		
	}
	/**
	 * Metodo que permite borrar usuarios de la lista de usuarios por asociar 
	 */
	public void deleteListUsers(){
			int index = getUser().getRowIndex();
			User userDel = this.usersTemp.get(index);
			this.usersTemp.remove(userDel);
			this.usersFind.clear();
			this.usersEmpty=true;
			setUserName("");
			if(usersTemp.size()==0){
				this.usersTmpEmpty=true;	
			}			
			
		}
	public void setAuthorization(IAuthorization authorization) {
		this.authorization = authorization;
	}

	public UIData getRoles() {
		return roles;
	}

	public void setRoles(UIData roles) {
		this.roles = roles;
	}

	public Role getRoleUpd() {
		return roleUpd;
	}

	public void setRoleUpd(Role roleUpd) {
		this.roleUpd = roleUpd;
	}

	public long getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(long idDomain) {
		this.idDomain = idDomain;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public String getShortNameDomain() {
		return shortNameDomain;
	}

	public void setShortNameDomain(String shortNameDomain) {
		this.shortNameDomain = shortNameDomain;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isPermissionsEmpty() {
		return permissionsEmpty;
	}

	public void setPermissionsEmpty(boolean permissionsEmpty) {
		this.permissionsEmpty = permissionsEmpty;
	}

	public List<PermissionHelper> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionHelper> permissions) {
		this.permissions = permissions;
	}

	public List<SelectItem> getGroupRoleList() {
		return groupRoleList;
	}

	public void setGroupRoleList(List<SelectItem> groupRoleList) {
		this.groupRoleList = groupRoleList;
	}

	public boolean isViewPopupRole() {
		return viewPopupRole;
	}

	public void setViewPopupRole(boolean viewPopupRole) {
		this.viewPopupRole = viewPopupRole;
	}

	public boolean isRenderPaginator() {
		return renderPaginator;
	}

	public void setRenderPaginator(boolean renderPaginator) {
		this.renderPaginator = renderPaginator;
	}

	public int getRowsNumber() {
		return rowsNumber;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public long getIdRole() {
		return idRole;
	}

	public void setIdRole(long idRole) {
		this.idRole = idRole;
	}

	public boolean isViewPermissionList() {
		return viewPermissionList;
	}

	public void setViewPermissionList(boolean viewPermissionList) {
		this.viewPermissionList = viewPermissionList;
	}

	public boolean isRenderPaginatorPermission() {
		return renderPaginatorPermission;
	}

	public void setRenderPaginatorPermission(boolean renderPaginatorPermission) {
		this.renderPaginatorPermission = renderPaginatorPermission;
	}

	public boolean isRenderPaginatorPermNoAta() {
		return renderPaginatorPermNoAta;
	}

	public void setRenderPaginatorPermNoAta(boolean renderPaginatorPermNoAta) {
		this.renderPaginatorPermNoAta = renderPaginatorPermNoAta;
	}

	public boolean isPermissionNoAtaEmpty() {
		return permissionNoAtaEmpty;
	}

	public void setPermissionNoAtaEmpty(boolean permissionNoAtaEmpty) {
		this.permissionNoAtaEmpty = permissionNoAtaEmpty;
	}

	public List<UserHelper> getUsers() {
		return users;
	}

	public void setUsers(List<UserHelper> users) {
		this.users = users;
	}

	public List<UserHelper> getUsersNoAttach() {
		return usersNoAttach;
	}

	public void setUsersNoAttach(List<UserHelper> usersNoAttach) {
		this.usersNoAttach = usersNoAttach;
	}

	public boolean isUsersEmpty() {
		return usersEmpty;
	}

	public void setUsersEmpty(boolean usersEmpty) {
		this.usersEmpty = usersEmpty;
	}

	public boolean isRenderPaginatorUser() {
		return renderPaginatorUser;
	}

	public void setRenderPaginatorUser(boolean renderPaginatorUser) {
		this.renderPaginatorUser = renderPaginatorUser;
	}

	public boolean isViewPopupUser() {
		return viewPopupUser;
	}

	public void setViewPopupUser(boolean viewPopupUser) {
		this.viewPopupUser = viewPopupUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<User> getUsersFind() {
		return usersFind;
	}

	public void setUsersFind(List<User> usersFind) {
		this.usersFind = usersFind;
	}

	public boolean isUsersFindEmpty() {
		return usersFindEmpty;
	}

	public void setUsersFindEmpty(boolean usersFindEmpty) {
		this.usersFindEmpty = usersFindEmpty;
	}

	public boolean isRoleListEmpty() {
		return roleListEmpty;
	}

	public void setRoleListEmpty(boolean roleListEmpty) {
		this.roleListEmpty = roleListEmpty;
	}

	public UIMessages getErrorNombre() {
		return errorNombre;
	}

	public void setErrorNombre(UIMessages errorNombre) {
		this.errorNombre = errorNombre;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isRenderPagUserAssociate() {
		return renderPagUserAssociate;
	}

	public void setRenderPagUserAssociate(boolean renderPagUserAssociate) {
		this.renderPagUserAssociate = renderPagUserAssociate;
	}

	public List<UserHelper> getUsersAssociate() {
		return usersAssociate;
	}

	public void setUsersAssociate(List<UserHelper> usersAssociate) {
		this.usersAssociate = usersAssociate;
	}

	public List<User> getUsersTemp() {
		return usersTemp;
	}

	public void setUsersTemp(List<User> usersTemp) {
		this.usersTemp = usersTemp;
	}

	public boolean isUsersTmpEmpty() {
		return usersTmpEmpty;
	}

	public void setUsersTmpEmpty(boolean usersTmpEmpty) {
		this.usersTmpEmpty = usersTmpEmpty;
	}

	public UIData getUser() {
		return user;
	}

	public void setUser(UIData user) {
		this.user = user;
	}

	public UIData getUserFind() {
		return userFind;
	}

	public void setUserFind(UIData userFind) {
		this.userFind = userFind;
	}
}
