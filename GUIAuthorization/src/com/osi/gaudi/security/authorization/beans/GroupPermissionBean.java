package com.osi.gaudi.security.authorization.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIData;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;

import com.osi.gaudi.security.authorization.entity.GroupPermission;
import com.osi.gaudi.security.authorization.entity.Permission;
import com.osi.gaudi.security.authorization.entity.User;
import com.osi.gaudi.security.authorization.seguridad.EServices;
import com.osi.gaudi.security.authorization.seguridad.ServiceLocator;
import com.osi.gaudi.security.authorization.utils.FacesUtils;
import com.osi.gaudi.security.authorization.ws.IAuthorization;


public class GroupPermissionBean {
	
	private long id;
	private String name;
	
	private String shortNameDomain;
	private long idDomain;
	
	private static Log log = LogFactory.getLog(GroupPermissionBean.class);
	
	private GroupPermission groupPermission = new GroupPermission();
	private GroupPermission groupPermissionUpd = new GroupPermission();
	
	private List<GroupPermission> groupPermissionList;
	
	InitialContext context;
	private IAuthorization authorization;
	
	private boolean renderPaginator = false;
	private final int rowsNumber = 10;

	private UIData groupPermissions;

	public GroupPermissionBean() {
		try {
			this.setUp();
		} catch (Exception e) {
			log.error(e.getMessage(), e);	
		}
		this.groupPermissionList = new ArrayList<GroupPermission>();
	}
	
		
	public void setUp() throws Exception {
		authorization =  getAuthorization();
	}
	
	public String verAction(){
		int index = getGroupPermissions().getRowIndex();
		this.groupPermissionUpd = this.groupPermissionList.get(index);
		return "updateGroupPermission";
	}


	public void reloadGroupPermissionList(ValueChangeEvent evt){
		this.groupPermissionList = new ArrayList<GroupPermission>();
		this.setIdDomain(new Long(evt.getNewValue().toString()));
		GroupPermission[] list = authorization.getGroupPermissionsbyDomain(this.getIdDomain());
		for (GroupPermission groupPermission : list) {
			this.groupPermissionList.add(groupPermission);
		}
		if (groupPermissionList.size()>this.rowsNumber) {
			this.renderPaginator = true;
		}else{
			this.renderPaginator = false;
		}
	}
	
	public void populateGroupPermissionList(){
		this.groupPermissionList = new ArrayList<GroupPermission>();
		GroupPermission[] list = authorization.getGroupPermissionsbyDomain(this.getIdDomain());
		for (GroupPermission groupPermission : list) {
			this.groupPermissionList.add(groupPermission);
		}
		if (groupPermissionList.size()>this.rowsNumber) {
			this.renderPaginator = true;
		}else{
			this.renderPaginator = false;
		}
	}
	
	public void deleteGroupPermission(){
		int index = getGroupPermissions().getRowIndex();
		GroupPermission objectDelete = this.groupPermissionList.get(index);
		try {
			authorization.deleteGroupPermission(objectDelete);
			this.populateGroupPermissionList();
			this.resetBeans();
			FacesUtils.addMensajePagina("GrupoPermiso eliminado satisfactoriamente");
		} catch (Exception e) {
			if(e.getCause().getCause().getCause() instanceof ConstraintViolationException)
				FacesUtils.addMensajePagina("No es posible eliminar el Grupo Permiso porque existen registros asociados"); //$NON-NLS-1$
		}
		
	}
	
	public void addGroupPermission(){
		if(validate(groupPermission.getName(),"Add")){
			groupPermission.setDomain(authorization.getDomain(this.getIdDomain()));
			authorization.insert(groupPermission);
			this.populateGroupPermissionList();
			this.resetBeans();
			FacesUtils.addMensajePagina("GrupoPermiso adicionado satisfactoriamente");
			this.groupPermission = new GroupPermission();
		} 
	}
	
	public String updateGroupPermission(){
		if(validate(this.groupPermissionUpd.getName(),"Upd")){
		authorization.modify(this.groupPermissionUpd);
		this.resetBeans();
		FacesUtils.addMensajePagina("GrupoPermiso actualizado satisfactoriamente");
		this.populateGroupPermissionList();
		return "return";
		}else{
			return "";
		}
	}
	
	public void resetBeans(){
		PermissionBean permission = (PermissionBean)FacesUtils.getManagedBean("permissionBean");
		permission.setIdDomain(0);
		permission.setGroupPermissionList(new ArrayList<SelectItem>());
		permission.setPermissionList(new ArrayList<Permission>());
	}
	
	public boolean validate(String name, String action) {
		if (name == null || name.equals(""))  {
			if(action.equals("Add")){
				FacesUtils.addMensajePagina("domainForm:groupPermissionList:"+"name"+action,"Nombre es requerido"); //$NON-NLS-1$
			}else{
			FacesUtils.addMensajePagina("domainForm:groupPermissionList:"+getGroupPermissions().getRowIndex()+":"+"name"+action,"Nombre es requerido"); //$NON-NLS-1$
			}
			return false;
		} else{
			return true;
		}
	}

	public void setGroupPermissionList(List<GroupPermission> groupPermissionList) {
		this.groupPermissionList = groupPermissionList;
	}

	public void populateBean(User entity,UserBean bean) throws IllegalAccessException, InvocationTargetException{
		BeanUtils.copyProperties(bean, entity);
	}
	
	public void populateEntity(UserBean bean,User entity) throws IllegalAccessException, InvocationTargetException{
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

	public GroupPermission getGroupPermission() {
		return groupPermission;
	}

	public void setGroupPermission(GroupPermission groupPermission) {
		this.groupPermission = groupPermission;
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

	public long getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(long idDomain) {
		this.idDomain = idDomain;
	}

	public List<GroupPermission> getGroupPermissionList() {
		return groupPermissionList;
	}
	
	public UIData getGroupPermissions() {
		return groupPermissions;
	}

	public void setGroupPermissions(UIData groupPermissions) {
		this.groupPermissions = groupPermissions;
	}

	public String getShortNameDomain() {
		return shortNameDomain;
	}

	public void setShortNameDomain(String shortNameDomain) {
		this.shortNameDomain = shortNameDomain;
	}

	public GroupPermission getGroupPermissionUpd() {
		return groupPermissionUpd;
	}

	public void setGroupPermissionUpd(GroupPermission groupPermissionUpd) {
		this.groupPermissionUpd = groupPermissionUpd;
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
}
