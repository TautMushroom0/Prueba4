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

import com.osi.gaudi.security.authorization.entity.GroupRole;
import com.osi.gaudi.security.authorization.entity.Role;
import com.osi.gaudi.security.authorization.entity.User;
import com.osi.gaudi.security.authorization.seguridad.EServices;
import com.osi.gaudi.security.authorization.seguridad.ServiceLocator;
import com.osi.gaudi.security.authorization.utils.FacesUtils;
import com.osi.gaudi.security.authorization.ws.IAuthorization;


public class GroupRoleBean {
	
	private long id;
	private String name;
	
	private String shortNameDomain;
	private long idDomain;
	
	private GroupRole groupRole = new GroupRole();
	private GroupRole groupRoleUpd = new GroupRole();
	
	private boolean renderPaginator = false;
	private final int rowsNumber = 10;
	
	private List<GroupRole> groupRoleList;
	
	InitialContext context;
	
	private IAuthorization authorization;
	
	private static Log log = LogFactory.getLog(GroupRoleBean.class);
	
	private UIData groupRoles;

	public GroupRoleBean() {
		try {
			this.setUp();
		} catch (Exception e) {
			log.error(e.getMessage(), e);		
		}
		this.groupRoleList = new ArrayList<GroupRole>();
	}
	
	public long getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(long idDomain) {
		this.idDomain = idDomain;
	}

	public List<GroupRole> getGroupRoleList() {
		return groupRoleList;
	}
	
	public UIData getGroupRoles() {
		return groupRoles;
	}

	public void setGroupRoles(UIData groupRoles) {
		this.groupRoles = groupRoles;
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
		int index = getGroupRoles().getRowIndex();
		this.groupRoleUpd = this.groupRoleList.get(index);
		
		return "updateGroupRole";
	}

	public void reloadGroupRoleList(ValueChangeEvent evt){
		this.groupRoleList = new ArrayList<GroupRole>();
		this.setIdDomain(new Long(evt.getNewValue().toString()));
		GroupRole[] list = authorization.getGroupRolesbyDomain(this.getIdDomain());
		for (GroupRole groupRole : list) {
			this.groupRoleList.add(groupRole);
		}
		if (groupRoleList.size()>rowsNumber) {
			this.renderPaginator = true;
		}else{
			this.renderPaginator = false;
		}
	}
	
	public void populateGroupRoleList(){
		this.groupRoleList = new ArrayList<GroupRole>();
		GroupRole[] list = authorization.getGroupRolesbyDomain(this.getIdDomain());
		for (GroupRole groupRole : list) {
			this.groupRoleList.add(groupRole);
		}
		if (groupRoleList.size()>rowsNumber) {
			this.renderPaginator = true;
		}else{
			this.renderPaginator = false;
		}
	}
	
	public void deleteGroupRole(){
		int index = getGroupRoles().getRowIndex();
		GroupRole objectDelete = this.groupRoleList.get(index);
		try {
			authorization.deleteGroupRole(objectDelete);
			this.populateGroupRoleList();
			this.resetBeans();
			FacesUtils.addMensajePagina("GrupoRol eliminado satisfactoriamente");
		} catch (Exception e) {
			if(e.getCause().getCause().getCause() instanceof ConstraintViolationException)
				FacesUtils.addMensajePagina("No es posible eliminar el Grupo Rol porque existen registros asociados"); //$NON-NLS-1$
		}
	}
	
	public void addGroupRole(){
		if(validate(groupRole.getName(),"Add")){
			groupRole.setDomain(authorization.getDomain(this.getIdDomain()));
			authorization.insert(groupRole);
			this.populateGroupRoleList();
			this.resetBeans();
			FacesUtils.addMensajePagina("GrupoRol adicionado satisfactoriamente");
			this.groupRole = new GroupRole();
		}
	}
	
	public String updateGroupRole(){
		if(validate(groupRoleUpd.getName(),"Upd")){
		authorization.modify(groupRoleUpd);
		FacesUtils.addMensajePagina("GrupoRol actualizado satisfactoriamente");
		this.resetBeans();
		this.populateGroupRoleList();
		return "return";
		}else{
			return "";
		}
	}
	
	public void resetBeans(){
		RoleBean role = (RoleBean)FacesUtils.getManagedBean("roleBean");
		role.setGroupRoleList(new ArrayList<SelectItem>());
		role.setRoleList(new ArrayList<Role>());
		role.setIdDomain(0);
	}
	
	public boolean validate(String name, String action) {
		if (name == null || name.equals(""))  {
			if(action.equals("Add")){
				FacesUtils.addMensajePagina("domainForm:groupRoleList:"+"name"+action,"Nombre es requerido"); //$NON-NLS-1$
			}else{
			FacesUtils.addMensajePagina("domainForm:groupRoleList:"+getGroupRoles().getRowIndex()+":"+"name"+action,"Nombre es requerido"); //$NON-NLS-1$
			}
			return false;
		} else{
			return true;
		}
	}

	public void setGroupRoleList(List<GroupRole> groupRoleList) {
		this.groupRoleList = groupRoleList;
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

	public GroupRole getGroupRole() {
		return groupRole;
	}

	public void setGroupRole(GroupRole groupRole) {
		this.groupRole = groupRole;
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

	public GroupRole getGroupRoleUpd() {
		return groupRoleUpd;
	}

	public void setGroupRoleUpd(GroupRole groupRoleUpd) {
		this.groupRoleUpd = groupRoleUpd;
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
