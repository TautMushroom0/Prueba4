package com.osi.gaudi.security.authorization.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIData;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;

import com.osi.gaudi.security.authorization.comparator.DescriptionPermissionComparator;
import com.osi.gaudi.security.authorization.comparator.NamePermissionComparator;
import com.osi.gaudi.security.authorization.entity.Domain;
import com.osi.gaudi.security.authorization.entity.GroupPermission;
import com.osi.gaudi.security.authorization.entity.Permission;
import com.osi.gaudi.security.authorization.entity.User;
import com.osi.gaudi.security.authorization.seguridad.EServices;
import com.osi.gaudi.security.authorization.seguridad.ServiceLocator;
import com.osi.gaudi.security.authorization.utils.FacesUtils;
import com.osi.gaudi.security.authorization.ws.IAuthorization;

public class PermissionBean {

	private long id;
	private String name;
	private String description;
	private String shortNameDomain;
	private long idDomain = 0;

	private long groupPermissionKey = 0;
	private Permission permission = new Permission();
	private Permission permissionUpd = new Permission();
	private List<Permission> permissionList;
	private List<Permission> permissionListHelper;
	private List<SelectItem> groupPermissionList;
	
	private boolean renderPaginator = false;
	private boolean permissionEmpty = true;
	private final int rowsNumber = 10;

	InitialContext context;
	private IAuthorization authorization;

	private static Log log = LogFactory.getLog(PermissionBean.class);
	private UIData permissions;

	public PermissionBean() {
		try {
			this.setUp();
		} catch (Exception e) {
			log.error(e.getMessage(), e);		
		}
		this.permissionList = new ArrayList<Permission>();
		this.groupPermissionList = new ArrayList<SelectItem>();
	}
	
	public long getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(long idDomain) {
		this.idDomain = idDomain;
	}

	public List<Permission> getPermissionList() {
		return permissionList;
	}

	public UIData getPermissions() {
		return permissions;
	}

	public void setPermissions(UIData permissions) {
		this.permissions = permissions;
	}

	public String getShortNameDomain() {
		return shortNameDomain;
	}

	public void setShortNameDomain(String shortNameDomain) {
		this.shortNameDomain = shortNameDomain;
	}

	public void setUp() throws Exception {
		authorization = getAuthorization();
	}
	
	public String verAction(){
		int index = getPermissions().getRowIndex();
		this.permissionUpd = this.permissionList.get(index);

		return "updatePermission";
	}

	public void reloadGroupPermissionList(ValueChangeEvent evt) {
		if (evt.getNewValue() != null) {
			this.groupPermissionList = new ArrayList<SelectItem>();
			this.setIdDomain(new Long(evt.getNewValue().toString()));
			if (this.getIdDomain() != 0) {
				Domain domain = authorization.getDomain(this.getIdDomain());
				this.setShortNameDomain(domain.getShortName());
				GroupPermission[] list = authorization.getGroupPermissionsbyDomain(this.getIdDomain());
				List<GroupPermission> groupPermissions = new ArrayList<GroupPermission>();
				for (GroupPermission groupPermission1 : list) {
					groupPermissions.add(groupPermission1);
				}
				for (GroupPermission groupPermission : groupPermissions) {
					this.groupPermissionList.add(new SelectItem(groupPermission.getId(), groupPermission.getName()));
				}
			}
			this.permissionList = new ArrayList<Permission>();
		}
		if(this.permissionList.size()>0){
			permissionEmpty=false;	
		}
		else{
			permissionEmpty=true;
		}
		if(this.permissionList.size()>this.rowsNumber){
			this.renderPaginator = true;
		}else{
			this.renderPaginator = false;
		}
	}

	public void reloadPermissionList(ValueChangeEvent evt) {
		this.permissionList = new ArrayList<Permission>();
		if (evt.getNewValue() != null) {
			this.permissionList = new ArrayList<Permission>();
			this.setGroupPermissionKey(Long.valueOf(evt.getNewValue().toString()).longValue());
			Permission[] list = authorization.getPermissionsByDomainGrpPermiss(this.getGroupPermissionKey(), this.getShortNameDomain());
			for (Permission permission : list) {
				this.permissionList.add(permission);
			}
		}
		if(this.permissionList.size()>0){
			permissionEmpty=false;	
		}		
		else{
			permissionEmpty=true;
		}
		if(this.permissionList.size()>this.rowsNumber){
			this.renderPaginator = true;
		}else{
			this.renderPaginator = false;
		}
	}

	public void populatePermissionList() {
		this.permissionList = new ArrayList<Permission>();
		Permission[] list = authorization.getPermissionsByDomainGrpPermiss(this.getGroupPermissionKey(), this.getShortNameDomain());
		for (Permission permission : list) {
			this.permissionList.add(permission);
		}
	}

	public void deletePermission() {
		try {
			int index = getPermissions().getRowIndex();
			Permission objectDelete = this.permissionList.get(index);
			authorization.deletePermission(objectDelete);
			this.populatePermissionList();
			FacesUtils.addMensajePagina("Permiso eliminado satisfactoriamente");
		} catch (Exception e) {
			if (e.getCause().getCause().getCause() instanceof ConstraintViolationException){
				FacesUtils.addMensajePagina("No es posible eliminar el permiso");
			}
		}
		
	}

	public void addPermission() {
		if(this.getIdDomain()!=0 && this.getGroupPermissionKey()!=0){
		permission.setDomain(authorization.getDomain(this.getIdDomain()));
		permission.setGroupPermission(authorization.getGroupPermission(this.getGroupPermissionKey()));
		authorization.insert(permission);
		this.populatePermissionList();
		FacesUtils.addMensajePagina("Permiso adicionado satisfactoriamente");
		this.permission = new Permission();
		}else{
			FacesUtils.addMensajePagina("Debe seleccionar un Dominio y un Grupo Permiso");
		}
		
	}

	public String updatePermission() {
		authorization.modify(this.permissionUpd);
		FacesUtils.addMensajePagina("Permiso actualizado satisfactoriamente");
		this.populatePermissionList();
		return "return";
	}

	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
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

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	
	public long getGroupPermissionKey() {
		return groupPermissionKey;
	}

	public void setGroupPermissionKey(long groupPermissionKey) {
		this.groupPermissionKey = groupPermissionKey;
	}

	public List<SelectItem> getGroupPermissionList() {
		return groupPermissionList;
	}

	public void setGroupPermissionList(List<SelectItem> groupPermissionList) {
		this.groupPermissionList = groupPermissionList;
	}
	
	/**
	 * Metodo que permite realizar busqueda de permisos por descripciï¿½n
	 */
	public void findDescription(){
		if(!this.description.equals("")){
			this.permissionListHelper = new ArrayList<Permission>();
			for (Iterator iterator = permissionList.iterator(); iterator.hasNext();) {
				Permission permission = (Permission) iterator.next();
				if(permission.getDescription().trim().equals(this.description)){
					permissionListHelper.add(permission);
				}				
			} 
			permissionList=permissionListHelper;
			if(permissionList.size()==0){
				permissionEmpty=true;	
				FacesUtils.addMensajePagina("No existen elementos asociados al criterio de busqueda");
			}				
			else {
				permissionEmpty=false;
			}
		}
		else{
			FacesUtils.addMensajePagina("Debe ingresar descripción");
		}
	}
	public void loadPermissionList() {
		if (idDomain!=0) {
			if (groupPermissionKey!=0) {
				this.permissionList = new ArrayList<Permission>();
				Permission[] list = authorization.getPermissionsByDomainGrpPermiss(this.getGroupPermissionKey(), this.getShortNameDomain());
				for (Permission permission : list) {
					this.permissionList.add(permission);
				}
				if(permissionList.size()==0){
					permissionEmpty=true;
					FacesUtils.addMensajePagina("No existen elementos relacionados segun Dominio y Grupo Permiso");
				}
				else {
					permissionEmpty=false;
				}
			}
			else{
				FacesUtils.addMensajePagina("Debe seleccionar Grupo Permiso");
			}
		}
		else {
			FacesUtils.addMensajePagina("Debe seleccionar Dominio");
		}
		if(this.permissionList.size()>this.rowsNumber){
			this.renderPaginator = true;
		}else{
			this.renderPaginator = false;
		}
	}
	
	public void orderListPermissionByName() {
		Collections.sort(permissionList, new NamePermissionComparator());
	}
	public void orderListPermissionByDesc() {
		Collections.sort(permissionList, new DescriptionPermissionComparator());
	}
	public IAuthorization getAuthorization() throws Exception {
		if(authorization==null){
			authorization= (IAuthorization) ServiceLocator.getInstance().getServiceAuthorization(EServices.Authorization.getRemoteName());
		}
		return authorization;
	}


	public void setAuthorization(IAuthorization authorization) {
		this.authorization = authorization;
	}

	public Permission getPermissionUpd() {
		return permissionUpd;
	}

	public void setPermissionUpd(Permission permissionUpd) {
		this.permissionUpd = permissionUpd;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Permission> getPermissionListHelper() {
		return permissionListHelper;
	}

	public void setPermissionListHelper(List<Permission> permissionListHelper) {
		this.permissionListHelper = permissionListHelper;
	}

	public boolean isPermissionEmpty() {
		return permissionEmpty;
	}

	public void setPermissionEmpty(boolean permissionEmpty) {
		this.permissionEmpty = permissionEmpty;
	}
}
