package com.osi.gaudi.security.authorization.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIData;
import javax.faces.event.ValueChangeEvent;
import javax.naming.InitialContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;

import com.osi.gaudi.security.authorization.entity.AdministratorDomain;
import com.osi.gaudi.security.authorization.entity.AdministratorDomainId;
import com.osi.gaudi.security.authorization.entity.Domain;
import com.osi.gaudi.security.authorization.entity.User;
import com.osi.gaudi.security.authorization.helper.UserHelper;
import com.osi.gaudi.security.authorization.seguridad.EServices;
import com.osi.gaudi.security.authorization.seguridad.ServiceLocator;
import com.osi.gaudi.security.authorization.utils.FacesUtils;
import com.osi.gaudi.security.authorization.ws.IAuthorization;

public class DomainBean {

	private long id;
	private String name;
	private String shortName;
	private String description;

	private List<Domain> domainList;
	private String shortNameDomain;

	private long idDomain;

	private List<User> usersAdminDomainList;
	private List<UserHelper> usersByDomain;
	private String useradmin;
	private boolean renderPaginatorUser;
	private boolean renderDelete;

	private boolean renderPaginator = false;
	private final int rowsNumber = 10;

	private boolean viewPopupUser;

	Domain domain = new Domain();
	Domain domainUpd = new Domain();

	private boolean userAdminDomainEmpty;

	private static Log log = LogFactory.getLog(DomainBean.class);
	
	private UIData domains;

	public UIData getDomains() {
		return domains;
	}

	public void setDomains(UIData domains) {
		this.domains = domains;
	}

	InitialContext context;
	private IAuthorization authorization;

	public DomainBean() {
		try {
			setUp();
			this.getDomainList();
		} catch (Exception e) {
			log.error(e.getMessage(), e);	
		}
	}

	private void populateDomainList() {
		Domain[] list = authorization.getDomains();
		this.domainList = new ArrayList<Domain>();
		for (Domain domain : list) {
			this.domainList.add(domain);
		}
		if (domainList.size() > rowsNumber) {
			this.renderPaginator = true;
		} else {
			this.renderPaginator = false;
		}
	}

	public void reloadUsersByDomain(ValueChangeEvent e) {
		this.usersByDomain = new ArrayList<UserHelper>();
		this.setIdDomain(new Long(e.getNewValue().toString()));
		this.populateUsersByDomain();
	}

	public void populateUsersByDomain() {
		this.usersByDomain = new ArrayList<UserHelper>();
		if (this.idDomain != 0) {
			User[] users = authorization.getUsersAdminByDomain(this.idDomain);
			for (User user : users) {
				UserHelper userHelper = new UserHelper();
				userHelper.setId(user.getId());
				userHelper.setName(user.getName());
				userHelper.setLastName(user.getLastName());
				this.usersByDomain.add(userHelper);
			}
			if (usersByDomain.size() == 0) {
				this.renderDelete = false;
			} else {
				this.renderDelete = true;
			}
		} else {
			FacesUtils.addMensajePagina("Debe seleccionar un dominio");
		}
	}

	public void setUp() throws Exception {
		authorization =  getAuthorization();
	}

	public void showPopupUser() {
		if (this.idDomain != 0) {
			this.viewPopupUser = true;
			this.populateUserAdminList();
		} else {
			FacesUtils.addMensajePagina("Debe seleccionar un dominio");
		}
	}

	public void closePopupUser() {
		this.viewPopupUser = false;
	}

	public void populateUserAdminList() {
		usersAdminDomainList = new ArrayList<User>();
		User[] usersTmp = authorization.getUsersAdminDomain();
		for (User user : usersTmp) {
			this.usersAdminDomainList.add(user);
		}
		if (usersAdminDomainList.size() == 0) {
			this.userAdminDomainEmpty = true;
		} else {
			this.userAdminDomainEmpty = false;
			if (usersAdminDomainList.size() > rowsNumber) {
				this.renderPaginator = true;
			} else {
				this.renderPaginator = false;
			}
		
		}

	}

	public void saveAdminDomain() {

		if (this.useradmin != null && !this.useradmin.equals("")){
			
			try {
				AdministratorDomain administratorDomain = new AdministratorDomain();
				AdministratorDomainId administratorDomainId = new AdministratorDomainId();
				administratorDomainId.setIdDomain(this.idDomain);
				administratorDomainId.setIdUser(this.getUseradmin());
				administratorDomain.setAdministratorDomainId(administratorDomainId);
				authorization.insert(administratorDomain);
				FacesUtils.addMensajePagina("Se agregó el Administrador correctamente");
				populateUsersByDomain();
			} catch (Exception e) {
				if (e.getCause().getCause().getCause() instanceof ConstraintViolationException) {
					this.setUseradmin("");
					FacesUtils.addMensajePagina("No es posible guardar el Administrador porque ya existe"); //$NON-NLS-1$
				}
			}
		}else{
			FacesUtils.addMensajePagina("Debe seleccionar un usuario"); //$NON-NLS-1$
		}
	}

	public void deleteAdminDomain() {
	 	int cont=0;
		for (UserHelper usersByDom : this.usersByDomain) {
			if (usersByDom.isSelected()) {
				AdministratorDomain administratorDomain = new AdministratorDomain();
				AdministratorDomainId administratorDomainId = new AdministratorDomainId();
				administratorDomainId.setIdDomain(this.idDomain);
				administratorDomainId.setIdUser(usersByDom.getId());
				administratorDomain.setAdministratorDomainId(administratorDomainId);
				authorization.deleteFromAdministratorDomain(administratorDomain);
				FacesUtils.addMensajePagina("Se eliminó el Administrador correctamente");
				this.populateUsersByDomain();
				cont++;
			 }
		}
		if(cont==0){
			FacesUtils.addMensajePagina("Debe seleccionar por lo menos un usuario");
		}
			 
	}

	public void addUser() {
		String userId = (String) FacesUtils.getRequestParameter("userId");
		this.useradmin = userId;
		this.viewPopupUser = false;
	}

	public String verAction() {
		int index = getDomains().getRowIndex();
		this.domainUpd = this.domainList.get(index);
		return "updateDomain";
	}

	public void deleteDomain() {
		int index = getDomains().getRowIndex();
		Domain objectDelete = this.domainList.get(index);
		try {
			authorization.deleteDomain(objectDelete);
			this.populateDomainList();
			FacesUtils.addMensajePagina("Se eliminó el dominio satisfactoriamente"); //$NON-NLS-1$
		} catch (Exception e) {
			if (e.getCause().getCause().getCause() instanceof ConstraintViolationException)
				FacesUtils.addMensajePagina("No es posible eliminar el Dominio porque existen registros asociados"); //$NON-NLS-1$
		}
	}

	public void addDomain() {
		if (this.validate(domain.getName(), domain.getShortName(), "Add")) {
			authorization.insert(domain);
			this.populateDomainList();
			FacesUtils.addMensajePagina("Se adicionó el dominio satisfactoriamente"); //$NON-NLS-1$
			this.domain = new Domain();
		}
	}

	public boolean validate(String name, String shortName, String action) {
		if (name == null || name.equals("")) {
			if (action.equals("Add")) {
				FacesUtils.addMensajePagina("domainForm:domainList:" + "name" + action, "Nombre es requerido"); //$NON-NLS-1$
			} else {
				FacesUtils.addMensajePagina("domainForm:domainList:" + getDomains().getRowIndex() + ":" + "name" + action, "Nombre es requerido"); //$NON-NLS-1$
			}
			return false;
		} else if (shortName.equals("")) {
			if (action.equals("Add")) {
				FacesUtils.addMensajePagina("domainForm:domainList:" + "shortName" + action, "Nombre corto es requerido"); //$NON-NLS-1$
			} else {
				FacesUtils.addMensajePagina("domainForm:domainList:" + getDomains().getRowIndex() + ":" + "shortName" + action, "Nombre corto es requerido"); //$NON-NLS-1$
			}
			return false;
		} else {
			return true;
		}
	}

	public String updateDomain() {
		if (validate(domainUpd.getName(), domainUpd.getShortName(), "Upd")) {
			authorization.modify(domainUpd);
			FacesUtils.addMensajePagina("El dominio se actualizó exitosamente"); //$NON-NLS-1$
			this.populateDomainList();
			return "return";
		} else {
			return "";
		}
	}

	public void populateBean(User entity, UserBean bean) throws IllegalAccessException, InvocationTargetException {
		BeanUtils.copyProperties(bean, entity);
	}

	public void populateEntity(UserBean bean, User entity) throws IllegalAccessException, InvocationTargetException {
		BeanUtils.copyProperties(entity, bean);
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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<User> getUsersAdminDomainList() {
		return usersAdminDomainList;
	}

	public void setUsersAdminDomainList(List<User> usersAdminDomainList) {
		this.usersAdminDomainList = usersAdminDomainList;
	}

	public String getUseradmin() {
		return useradmin;
	}

	public void setUseradmin(String useradmin) {
		this.useradmin = useradmin;
	}

	public Domain getDomainUpd() {
		return domainUpd;
	}

	public void setDomainUpd(Domain domainUpd) {
		this.domainUpd = domainUpd;
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

	public String getShortNameDomain() {
		return shortNameDomain;
	}

	public void setShortNameDomain(String shortNameDomain) {
		this.shortNameDomain = shortNameDomain;
	}

	public boolean isViewPopupUser() {
		return viewPopupUser;
	}

	public void setViewPopupUser(boolean viewPopupUser) {
		this.viewPopupUser = viewPopupUser;
	}

	public boolean isRenderPaginatorUser() {
		return renderPaginatorUser;
	}

	public void setRenderPaginatorUser(boolean renderPaginatorUser) {
		this.renderPaginatorUser = renderPaginatorUser;
	}

	public List<Domain> getDomainList() {
		this.populateDomainList();
		return domainList;
	}

	public void setDomainList(List<Domain> domainList) {
		this.domainList = domainList;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public long getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(long idDomain) {
		this.idDomain = idDomain;
	}

	public List<UserHelper> getUsersByDomain() {
		return usersByDomain;
	}

	public void setUsersByDomain(List<UserHelper> usersByDomain) {
		this.usersByDomain = usersByDomain;
	}

	public boolean isRenderDelete() {
		return renderDelete;
	}

	public void setRenderDelete(boolean renderDelete) {
		this.renderDelete = renderDelete;
	}

	public boolean isUserAdminDomainEmpty() {
		return userAdminDomainEmpty;
	}

	public void setUserAdminDomainEmpty(boolean userAdminDomainEmpty) {
		this.userAdminDomainEmpty = userAdminDomainEmpty;
	}

}
