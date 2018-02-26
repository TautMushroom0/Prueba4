//package com.osi.gaudi.security.authorization.business.manager;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//
//import javax.xml.namespace.QName;
//
//import com.osi.gaudi.security.authorization.clientews.GroupPermissionArray;
//import com.osi.gaudi.security.authorization.clientews.GroupRoleArray;
//import com.osi.gaudi.security.authorization.clientews.PermissionArray;
//import com.osi.gaudi.security.authorization.clientews.RoleArray;
//import com.osi.gaudi.security.authorization.entity.Domain;
//import com.osi.gaudi.security.authorization.entity.GroupPermission;
//import com.osi.gaudi.security.authorization.entity.GroupRole;
//import com.osi.gaudi.security.authorization.entity.Permission;
//import com.osi.gaudi.security.authorization.entity.Role;
//import com.osi.gaudi.security.authorization.util.PropertiesLoader;
//
//public class ServiceRemote {
//	private static ServiceRemote serviceRemote;
//	private com.osi.gaudi.security.authorization.clientews.IAuthorization authorization;
//	
//	private PropertiesLoader loader = PropertiesLoader.getInstance();
//	private Properties properties = loader.getProperties("app.properties");
//	
//	private  final String URL_ENDPOINT      = properties.getProperty("urlEndPoint");
//	private  final String SERVICE_NAME      = properties.getProperty("serviceName");
//	private  final String TARGET_NAME_SPACE = properties.getProperty("targetNameSpace");
//	
//	private ServiceRemote() {
//		URL endpoint_new;
//		try {
//			endpoint_new = new URL(URL_ENDPOINT);
//			
//		    QName qname = new QName(TARGET_NAME_SPACE, SERVICE_NAME);
//		    com.osi.gaudi.security.authorization.clientews.ServiceAuthorization sa = new com.osi.gaudi.security.authorization.clientews.ServiceAuthorization(endpoint_new,qname);
//		    authorization =sa.getPortAuthorization();
//			
//		} catch (MalformedURLException emf) {
//			// TODO Auto-generated catch block
//			emf.printStackTrace();
//			System.err.printf("Error accediendo a servicio web URL_ENDPOINT:: "+URL_ENDPOINT);
//		}
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	public static final ServiceRemote getInstance() {
//		if (serviceRemote == null) {
//			serviceRemote = new ServiceRemote();
//		}
//		return serviceRemote;
//	}
//	
//
//	public List<Domain> getDomains(){ 
//
//		com.osi.gaudi.security.authorization.clientews.DomainArray dominios=  authorization.getDomains();
//		Domain dominio = null;
//		List<Domain> listDomain=null;
//	
//		if(dominios!=null){
//			listDomain= new ArrayList<Domain>();
//			for(com.osi.gaudi.security.authorization.clientews.Domain  dom: dominios.getItem()){
//				dominio= new  Domain();
//				dominio.setId(dom.getId());
//				dominio.setName(dom.getName());
//				dominio.setShortName(dom.getShortName());
//				dominio.setDescription(dom.getDescription());
//				listDomain.add(dominio);
//			}	
//		}
//		return listDomain;
//		
//	}
//
//	public List<GroupPermission> getGroupPermissionsbyDomain(long domain){ 
//
//		GroupPermissionArray permissions=  authorization.getGroupPermissionsbyDomain(domain);
//		GroupPermission groupPermission = null;
//		List<GroupPermission> listGroupPermission=null;
//	
//		if(permissions!=null){
//			listGroupPermission= new ArrayList<GroupPermission>();
//			for(com.osi.gaudi.security.authorization.clientews.GroupPermission  per: permissions.getItem()){
//				groupPermission= new  GroupPermission();
//				groupPermission.setId(per.getId());
//				groupPermission.setName(per.getName());
//				
//				listGroupPermission.add(groupPermission);
//			}	
//		}
//		return listGroupPermission;
//		
//	}
//	
//	public List<Permission> getPermissionsByDomainGrpPermiss(long groupPermissionKey, String shortNameDomain){ 
//
//		PermissionArray permissions=  authorization.getPermissionsByDomainGrpPermiss(groupPermissionKey, shortNameDomain);
//		Permission permission = null;
//		List<Permission> listPermission=null;
//	
//		if(permissions!=null){
//			listPermission= new ArrayList<Permission>();
//			for(com.osi.gaudi.security.authorization.clientews.Permission  per: permissions.getItem()){
//				permission= new  Permission();
//				permission.setId(per.getId());
//				permission.setName(per.getName());
//				permission.setDescription(per.getDescription());
//				
//				listPermission.add(permission);
//			}	
//		}
//		return listPermission;
//		
//	}
//	public List<GroupRole> getGroupRolesbyDomain(long domain){ 
//
//		GroupRoleArray roles=  authorization.getGroupRolesbyDomain(domain);
//		GroupRole groupRole = null;
//		List<GroupRole> listGroupRole=null;
//	
//		if(roles!=null){
//			listGroupRole= new ArrayList<GroupRole>();
//			for(com.osi.gaudi.security.authorization.clientews.GroupRole  per: roles.getItem()){
//				groupRole= new  GroupRole();
//				groupRole.setId(per.getId());
//				groupRole.setName(per.getName());
//				
//				listGroupRole.add(groupRole);
//			}	
//		}
//		return listGroupRole;
//		
//	}
//	public GroupRole getGroupRoles(long idGroupRole){ 
//
//		com.osi.gaudi.security.authorization.clientews.GroupRole gRol=  authorization.getGroupRole(idGroupRole);
//		GroupRole groupRole = new  GroupRole();
//		
//			if(gRol!=null){
//				groupRole.setId(gRol.getId());
//				groupRole.setName(gRol.getName());				
//			}		
//	
//		return groupRole;
//		
//	}
//	
//	public GroupPermission getGroupPermission(long idGroupPermission){ 
//
//		com.osi.gaudi.security.authorization.clientews.GroupPermission gPermission=  authorization.getGroupPermission(idGroupPermission);
//		GroupPermission groupPermission = new  GroupPermission();
//		
//			if(gPermission!=null){
//				groupPermission.setId(gPermission.getId());
//				groupPermission.setName(gPermission.getName());
//				
//			}		
//	
//		return groupPermission;
//		
//	}
//	
//	public 	List<Permission>  getPermissionRole(long idRole, String shortNameDomain){ 
//
//		PermissionArray gPermissionRole=  authorization.getPermissionsRole(idRole, shortNameDomain);
//		
//		Permission permission = null;
//		List<Permission> listPermission=null;
//	
//		if(gPermissionRole!=null){
//			listPermission= new ArrayList<Permission>();
//			for(com.osi.gaudi.security.authorization.clientews.Permission  per: gPermissionRole.getItem()){
//				permission= new  Permission();
//				permission.setId(per.getId());
//				permission.setName(per.getName());
//				permission.setDescription(per.getDescription());
//				
//				listPermission.add(permission);
//			}	
//		}
//		return listPermission;
//		
//	}
//	public List<Role> getRolesByDomainGrpRole(long grupoRoleKey, String shortNameDomain){ 
//
//		RoleArray roles=  authorization.getRolesByDomainGrpRole(grupoRoleKey,shortNameDomain);
//		Role role = null;
//		List<Role> listRole=null;
//	
//		if(roles!=null){
//			listRole= new ArrayList<Role>();
//			for(com.osi.gaudi.security.authorization.clientews.Role  per: roles.getItem()){
//				role= new  Role();
//				role.setId(per.getId());
//				role.setName(per.getName());
//				role.setDescription(per.getDescription());
//				
//				listRole.add(role);
//			}	
//		}
//		return listRole;		
//	}
//	
//	/**
//	 * @return the authorization
//	 */
//	public com.osi.gaudi.security.authorization.clientews.IAuthorization getAuthorization() {
//		return authorization;
//	}
//
//	/**
//	 * @param authorization the authorization to set
//	 */
//	public void setAuthorization(com.osi.gaudi.security.authorization.clientews.IAuthorization authorization) {
//		this.authorization = authorization;
//	}
//}
