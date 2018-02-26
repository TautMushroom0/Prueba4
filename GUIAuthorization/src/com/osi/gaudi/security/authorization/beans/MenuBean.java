package com.osi.gaudi.security.authorization.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.faces.el.MethodBinding;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icesoft.faces.component.ext.taglib.MethodBindingString;
import com.icesoft.faces.component.menubar.MenuItem;
//import com.osi.gaudi.cfg.Configurator;
import com.osi.gaudi.security.authorization.comparator.GroupPermissionComparator;
import com.osi.gaudi.security.authorization.entity.GroupPermission;
import com.osi.gaudi.security.authorization.entity.Permission;
import com.osi.gaudi.security.authorization.seguridad.EServices;
import com.osi.gaudi.security.authorization.seguridad.ServiceLocator;
import com.osi.gaudi.security.authorization.util.PropertiesLoader;
import com.osi.gaudi.security.authorization.utils.AppPropiedades;
import com.osi.gaudi.security.authorization.utils.FacesUtils;
import com.osi.gaudi.security.authorization.ws.IAuthorization;


@SuppressWarnings("deprecation")
public class MenuBean {

	private List<MenuItem> menuModel;
	private IdentityBean identity;
	private List<Permission> permissions = new ArrayList<Permission>();
//	private final boolean INTEGRA_LOGINADMIN = Configurator.getInstance().getBool("sistema", "integraLoginAdmin", true);
	private final boolean INTEGRA_LOGINADMIN = AppPropiedades.getIntegraLoginAdmin();
	
	private static PropertiesLoader loader = PropertiesLoader.getInstance();
	private static Properties properties = loader.getProperties("app.properties");
	
	InitialContext context;
	private IAuthorization authorization;
	
	String userName;
	
	private boolean permissionsEmpty = false;

	private static Log log = LogFactory.getLog(MenuBean.class);
	
	
	public MenuBean() {
		log.info("Iniciando MenuBean");
		try {
			this.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);		
		}
		
		
		identity = (IdentityBean) FacesUtils.getManagedBean("identity");
		permissions = (identity.getPermissions()!= null ? identity.getPermissions() : permissions) ;
		
		if (permissions.size()==0) {
			this.permissionsEmpty=true;
		}
		llenarListaMenu();
	}
	public void setUp() throws Exception {
		authorization = getAuthorization();
	}
	public List<MenuItem> getMenuModel() {
		List<MenuItem> newList = new ArrayList<MenuItem>();
		for (MenuItem cada : menuModel) {			
			if(!String.valueOf(cada.getValue()).equalsIgnoreCase("Cuentas Us. Ext.")){
				newList.add(cada);
			}
		}
		return newList;
	}

	public void setMenuModel(List<MenuItem> menuModel) {
		this.menuModel = menuModel;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public MenuBean(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public void llenarListaMenu() {
		this.menuModel = new ArrayList<MenuItem>();
		Set<GroupPermission> groupPermission = new HashSet<GroupPermission>();
		// permissions = identity.getPermissions();
		for (Permission permissionElement : permissions) {
			if (!groupPermission.contains(permissionElement.getGroupPermission())) {
				groupPermission.add(permissionElement.getGroupPermission());
			}
		}
		List<GroupPermission> groupPermissionList = new ArrayList<GroupPermission>(groupPermission);
		Collections.sort(groupPermissionList,new GroupPermissionComparator());
		for (GroupPermission groupPermissionElement : groupPermissionList) {
			MenuItem result = new MenuItem();
			result.setValue(groupPermissionElement.getName());

			Iterator<Permission> it = permissions.iterator();
			while (it.hasNext()) {
				Permission permissionElement = (Permission) it.next();
				if (permissionElement.getGroupPermission().equals(groupPermissionElement)) {
					MenuItem child = new MenuItem();
					child.setValue(permissionElement.getDescription());
					MethodBinding action = new MethodBindingString(permissionElement.getName());
					child.setAction(action);
					result.getChildren().add(child);
				}
			}
			this.menuModel.add(result);
		}
		
		//Agrega El Item de Volver a LoginAdmin.
		if (INTEGRA_LOGINADMIN) {
			System.out.println("Entra a INTEGRA_LOGINADMIN" + INTEGRA_LOGINADMIN);
			MenuItem laItem = new MenuItem();
			laItem.setValue(properties.getProperty("urlLoginAdminLabel"));
			laItem.setLink(properties.getProperty("urlLoginAdminLink"));
			this.menuModel.add(laItem);
		}
	}
	
	public IAuthorization getAuthorization() throws Exception {
		if(authorization==null){
			authorization= (IAuthorization) ServiceLocator.getInstance().getServiceAuthorization(EServices.Authorization.getRemoteName());
		}
		return authorization;
	}

	public boolean isPermissionsEmpty() {
		return permissionsEmpty;
	}

	public void setPermissionsEmpty(boolean permissionsEmpty) {
		this.permissionsEmpty = permissionsEmpty;
	}

}
