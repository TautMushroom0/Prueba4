package com.osi.gaudi.security.authorization.beans;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.faces.component.UIData;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.osi.gaudi.security.authorization.entity.Domain;
import com.osi.gaudi.security.authorization.entity.GroupPermission;
import com.osi.gaudi.security.authorization.entity.GroupRole;
import com.osi.gaudi.security.authorization.entity.Permission;
import com.osi.gaudi.security.authorization.entity.PermissionRole;
import com.osi.gaudi.security.authorization.entity.Role;
import com.osi.gaudi.security.authorization.seguridad.EServices;
import com.osi.gaudi.security.authorization.seguridad.ServiceLocator;
import com.osi.gaudi.security.authorization.util.PropertiesLoader;
import com.osi.gaudi.security.authorization.utils.AppPropiedades;
import com.osi.gaudi.security.authorization.utils.FacesUtils;
import com.osi.gaudi.security.authorization.ws.IAuthorization;

public class SincPermissionBean {

private static Log log = LogFactory.getLog(SincPermissionBean.class);
	
	private PropertiesLoader loader = PropertiesLoader.getInstance();
	private Properties properties = loader.getProperties("app.properties");
	private  final String VERSION_APP = properties.getProperty("aplication.version.dist");
	private List<Domain> domainList;
	
    private String serverInterchange;
    private String versionAplicacion;
    private long dominio;
	InitialContext context;
	private IAuthorization authorization;	
	private UIData domain;
	
	private boolean renderPaginator = false;
	private final int rowsNumber = 10;

	public SincPermissionBean() {
		try {
			this.setUp();
//			URL endpoint = new URL(URL_ENDPOINT);				
//			this.serverInterchange=endpoint.getHost();
			
			this.serverInterchange = "/Authorization/";
			this.versionAplicacion = VERSION_APP;
			this.populateDomainList();
			
		} catch (MalformedURLException emf) {
			log.error(emf.getMessage(), emf);
			FacesUtils.addMensajePagina("Ocurrio un error en parametrizacion de servidor remoto "+emf.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			FacesUtils.addMensajePagina("Ocurrio un error inicializando componente de permisos");
		}
	}

	public String getVersionAplicacion() {
		return versionAplicacion;
	}

	public void setVersionAplicacion(String versionAplicacion) {
		this.versionAplicacion = versionAplicacion;
	}

	public void setUp() throws Exception {
		authorization =  getAuthorization();
	}
	private void populateDomainList() {
		try {
			
			System.out.println("Entra en populateDomainList");
//			this.domainList =ServiceRemote.getInstance().getDomains();
			
			Domain[] list = authorization.getDomains();
			System.out.println("list size: "+list.length);
			this.domainList = Arrays.asList(list);
			
			if (domainList.size() > rowsNumber) {
				this.renderPaginator = true;
			} else {
				this.renderPaginator = false;
			}
		}catch (Exception e) {
				log.error(e.getMessage(), e);
				FacesUtils.addMensajePagina("Ocurrio un error en consulta de dominios ");
		}
	}
	

	public void sincronizar(){
		
		try {
			Domain dom =  (Domain) getDomain().getRowData();
			dominio=dom.getId();
			Domain domain = new Domain();
			domain.setName(dom.getName());
			domain.setShortName(dom.getShortName());
			domain.setDescription(dom.getDescription());
			
			
			log.info("INICIANDO PROCESO DE SINCRONIZACION DE DATOS PARA DOMINIO:  "+ dom.getName());
			if(getDomainLocal(domain.getName())==null){
				log.info("creando dominio: "+domain.getName());
				addDomain(domain);
				domain = getDomainLocal(domain.getName());
				
				System.out.println("Entra en sincronizar 1");
//				List<GroupPermission> groupPermission = ServiceRemote.getInstance().getGroupPermissionsbyDomain(dominio);				
				List<GroupPermission> groupPermission = null;				

				log.info("ADICIONANDO GRUPO DE PERMISOS AL DOMINIO:  "+ dom.getName());
				addGroupPermissionsbyDomainLocal(domain,groupPermission);
				
				List<GroupPermission> groupPermissionLocal=getGroupPermissionsbyDomain(domain.getId());
				
				for(GroupPermission gp:groupPermission){
					
					System.out.println("Entra en sincronizar 2");
//					List<Permission> permissions = ServiceRemote.getInstance().getPermissionsByDomainGrpPermiss(gp.getId(),domain.getShortName());
					List<Permission> permissions = null;
					
					
					log.info("ADICIONANDO PERMISOS A GRUPO PERMISO: "+ gp.getName()+" DEL DOMINIO:  "+ dom.getName());
					lab_fpl:for(GroupPermission gpl:groupPermissionLocal){
							if(gpl.getName().equals(gp.getName())){
								addPermissionsToGroupPermissionbyDomain(domain,gpl,permissions);
								break lab_fpl; 
							}
					}
				}
				
				System.out.println("Entra en sincronizar 3");
//				List<GroupRole> groupRole = ServiceRemote.getInstance().getGroupRolesbyDomain(dominio);
				List<GroupRole> groupRole = null;
				
				
				//adiciona grupo roles
				log.info("ADICIONANDO GRUPO DE ROLES AL DOMINIO:  "+ dom.getName());
				addGroupRolesbyDomainLocal(domain,groupRole);
				List<GroupRole>  groupRoleLocal =  getGroupRolebyDomain(domain.getId());
				for(GroupRole gr:groupRole){
				
					System.out.println("Entra en sincronizar 4");
//					List<Role> roles = ServiceRemote.getInstance().getRolesByDomainGrpRole(gr.getId(),domain.getShortName());
					List<Role> roles = null;
					
					
					
					//adiciona grupo rol a roles
					log.info("ADICIONANDO ROLES A GRUPO ROL: "+ gr.getName()+" DEL DOMINIO:  "+ dom.getName());
					lab_fpl:for(GroupRole grl:groupRoleLocal){
						if(grl.getName().equals(gr.getName())){
							addRolToGroupRolbyDomain(domain, grl, roles);
							break lab_fpl; 
						}
					}
					
					log.info("ADICIONANDO PERMISOS A ROL");
					
					List<Role> rolesLocal =  getRolesByDomain(domain.getShortName());
					
					for(GroupPermission gpl:groupPermissionLocal){
						
						List<Permission>  permissionLocal=getPermissionsByDomainGrpPermiss(gpl.getId(),domain.getShortName());
						if(permissionLocal!=null && !permissionLocal.isEmpty()){
							for(Role role:roles){
								
								System.out.println("Entra en sincronizar 5");
//								List<Permission>  permissionRole =ServiceRemote.getInstance().getPermissionRole(role.getId(),domain.getShortName());
								List<Permission>  permissionRole = null;
								
								
								lab_rl:for(Role rol:rolesLocal){
									if(role.getName().equals(rol.getName())){
											addPermissionToRol(rol,permissionRole,permissionLocal);
										break lab_rl;
									}	
								}
							}
						}
					}
				
					
				}
				
				log.info("FINALIZANDO PROCESO DE SINCRONIZACION DE DATOS PARA DOMINIO:  "+ dom.getName());
				FacesUtils.addMensajePagina("Procesos de sincronización finalizado satisfactoriamente");
			}else{
				log.info("YA EXISTE PARAMETRIZACION PARA DOMINIO:  "+ dom.getName());
				FacesUtils.addMensajePagina("YA EXISTE PARAMETRIZACION PARA DOMINIO:  "+ dom.getName());
			}
			
			
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			FacesUtils.addMensajePagina("No fue posible cargar listado dominios. Consulte con el administrador del sistema");
		}
		
	}
	/**
	 * Permite adicionar un dominio en sistema local
	 * @param domain, objeto que representa un dominio 
	 * */
	
	public void addDomain(Domain domain) {
		try {
			authorization.insert(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	/**
	 * Permite consultar un grupo de permisos por dominio en sistema local
	 * @param domain, codigo de dominio 
	 * @return GroupPermission, listado de grupo de permisos 
	 * */
	public List<GroupPermission>  getGroupPermissionsbyDomain(long domain) {
		try {
			return Arrays.asList(authorization.getGroupPermissionsbyDomain(domain));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * Permite adicionar permisos a rol en sistema local
	 * @param domain, codigo de dominio 
	 * @return GroupPermission, listado de grupo de permisos 
	 * */
	public List<GroupPermission>  addPermissionToRol(Role role, List<Permission> permissionsRole,  List<Permission> permissionsLocal) {
		try {
			for(Permission per: permissionsRole){
				lab_pl:for(Permission perLocal: permissionsLocal){
				   if(perLocal.getName().equals(per.getName())){
						PermissionRole	pr= new PermissionRole();
					    pr.setPermission(perLocal);
					    pr.setRole(role);
					    authorization.insert(pr);
				    break lab_pl;
				   }
				}   
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	/**
	 * Permite consultar un grupo de permisos por igGrupoPermiso en sistema local
	 * @param igGrupoPermiso, codigo de Grupo Permiso 
	 * @return GroupPermission, objeto de tipo GroupPermission 
	 * */
	public GroupPermission  getGroupPermissions(long igGrupoPermiso) {
		try {
			return authorization.getGroupPermission(igGrupoPermiso);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * Permite consultar un grupo de roles por igGrupoRole en sistema local
	 * @param igGrupoRole, codigo de Grupo Rol 
	 * @return GroupRole, objeto de tipo grupo rol 
	 * */
	public GroupRole  getGroupRole(long igGrupoRole) {
		try {
			return authorization.getGroupRole(igGrupoRole);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * Permite consultar roles por dominion en sistema local
	 * @param domain, nombre de dominio 
	 * @return GroupRole, objeto de tipo grupo rol 
	 * */
	public List<Role>  getRolesByDomain(String domain) {
		try {
			return Arrays.asList(authorization.getRolesByDomain(domain));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	/**
	 * Permite consultar roles por dominio en sistema local
	 * @param domain, nombre de dominio 
	 * @return GroupRole, objeto de tipo grupo rol 
	 * */
	public List<Permission>  getPermissionsByDomainGrpPermiss(long groupPermission, String shortNameDomain) {
		try {
			return Arrays.asList(authorization.getPermissionsByDomainGrpPermiss(groupPermission, shortNameDomain));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * Permite consultar el listado de grupo de roles por dominio en sistema local
	 * @param domain, codigo de dominio 
	 * @return GroupRole, listado de grupo de roles 
	 * */
	public List<GroupRole>  getGroupRolebyDomain(long domain) {
		try {
			return Arrays.asList(authorization.getGroupRolesbyDomain(domain));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * Permite consultar un dominio en sistema local
	 * @param domain, String que representa el nombre de dominio
	 * @return Domain, objeto de tipo Domain que existe en sistema local
	 * */
	public Domain getDomainLocal(String domain) {
		try {
			Domain [] domains =authorization.getDomains();
			for(Domain dom: domains){
				if(dom.getName().equals(domain)){
					log.info("Ya existe el dominio "+domain);
					return dom;
				}	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	/**
	 * Permite adicionar un grupo de permiso a un dominio en sistema local
	 * @param domain, objeto de tipo Domain que representa un dominio
	 * @param groupPermission, listado de grupo de permisos que se adicionan a un dominio
	 * */
	public void  addGroupPermissionsbyDomainLocal(Domain domain, List<GroupPermission> groupPermission){
		try {
			GroupPermission gPermission=null;
			for(GroupPermission gPerm: groupPermission){
				gPermission=new GroupPermission();
				gPermission.setName(gPerm.getName());
				gPermission.setDomain(domain);
				authorization.insert(gPermission);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Permite adicionar un permiso a un grupo de permisos en sistema local
	 * @param domain, objeto de tipo Domain que representa un dominio
	 * @param groupPermission, objeto que representa un grupo de roles
	 * @param permissions, listado de permisos que se adicionan a un grupo de permisos
	 * */
	public void  addPermissionsToGroupPermissionbyDomain(Domain domain,GroupPermission groupPermission,List<Permission> permissions){
		try {
			Permission permission=null;
			for(Permission perm: permissions){
				permission= new Permission();
				permission.setName(perm.getName());
				permission.setDescription(perm.getDescription());
				permission.setDomain(domain);
				permission.setGroupPermission(groupPermission);
				authorization.insert(permission);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Permite adicionar un grupo de roles a un dominio en sistema local
	 * @param domain, objeto de tipo Domain que representa un dominio
	 * @param groupRole, lista de grupo roles que se adicionan al dominio
	 * */
	public void  addGroupRolesbyDomainLocal(Domain domain, List<GroupRole>  groupRole){
		try {
			GroupRole grupoRole=null;
			for(GroupRole gRol: groupRole){
				grupoRole= new GroupRole();
				grupoRole.setName(gRol.getName());
				grupoRole.setDomain(domain);
				authorization.insert(grupoRole);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	/**
	 * Permite adicionar un rol  a grupo de roles en sistema local
	 * @param domain, objeto principal de tipo Domain
	 * @param groupRole, objeto que representa el grupo de roles
	 * @param roles, lista de roles que se adicionan al grupo rol
	 * */
	public void  addRolToGroupRolbyDomain(Domain domain,GroupRole groupRole,List<Role> roles){
		try {
			Role role=null;
			for(Role rol: roles){
				role= new Role();
				role.setName(rol.getName());
				role.setDescription(rol.getDescription());
				role.setDomain(domain);
				role.setGroupRole(groupRole);				
				authorization.insert(role);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * @return the dominio
	 */
	public long getDominio() {
		return dominio;
	}

	/**
	 * @param dominio the dominio to set
	 */
	public void setDominio(long dominio) {
		this.dominio = dominio;
	}

	public UIData getDomain() {
		return domain;
	}

	public void setDomain(UIData domain) {
		this.domain = domain;
	}




	/**
	 * @return the domainList
	 */
	public List<Domain> getDomainList() {
		return domainList;
	}




	/**
	 * @param domainList the domainList to set
	 */
	public void setDomainList(List<Domain> domainList) {
		this.domainList = domainList;
	}

	/**
	 * @return the renderPaginator
	 */
	public boolean isRenderPaginator() {
		return renderPaginator;
	}

	/**
	 * @param renderPaginator the renderPaginator to set
	 */
	public void setRenderPaginator(boolean renderPaginator) {
		this.renderPaginator = renderPaginator;
	}

	/**
	 * @return the rowsNumber
	 */
	public int getRowsNumber() {
		return rowsNumber;
	}

	/**
	 * @return the serverInterchange
	 */
	public String getServerInterchange() {
		return serverInterchange;
	}

	/**
	 * @param serverInterchange the serverInterchange to set
	 */
	public void setServerInterchange(String serverInterchange) {
		this.serverInterchange = serverInterchange;
	}

	/**
	 * @return the authorization
	 * @throws Exception 
	 */
	public IAuthorization getAuthorization() throws Exception {
		if(authorization==null){
			authorization= (IAuthorization) ServiceLocator.getInstance().getServiceAuthorization(EServices.Authorization.getRemoteName());
		}
		return authorization;
	}

	/**
	 * @param authorization the authorization to set
	 */
	public void setAuthorization(IAuthorization authorization) {
		this.authorization = authorization;
	}
	
	
}
