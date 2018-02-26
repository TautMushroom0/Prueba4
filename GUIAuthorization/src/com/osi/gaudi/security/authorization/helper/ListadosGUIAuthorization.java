package com.osi.gaudi.security.authorization.helper;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.osi.gaudi.security.authorization.beans.IdentityBean;
import com.osi.gaudi.security.authorization.entity.Domain;
import com.osi.gaudi.security.authorization.entity.Role;
import com.osi.gaudi.security.authorization.seguridad.EServices;
import com.osi.gaudi.security.authorization.seguridad.ServiceLocator;
import com.osi.gaudi.security.authorization.utils.FacesUtils;
import com.osi.gaudi.security.authorization.ws.IAuthorization;

public class ListadosGUIAuthorization {

	private static Log log = LogFactory.getLog(ListadosGUIAuthorization.class);
	
	private List<SelectItem> domainList = new ArrayList<SelectItem>();

	InitialContext context;
	private IAuthorization authorization;

	public ListadosGUIAuthorization() {
		try {
			this.setUp();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	

	public void setAuthorization(IAuthorization authorization) {
		this.authorization = authorization;
	}

	
	public IAuthorization getAuthorization() throws Exception {
		if(authorization==null){
			authorization= (IAuthorization) ServiceLocator.getInstance().getServiceAuthorization(EServices.Authorization.getRemoteName());
		}
		return authorization;
	}
	public List<SelectItem> getDomainList() {
		boolean general = false;
		
		IdentityBean identity = (IdentityBean) FacesUtils.getManagedBean("identity");
		if (identity.getRoles() != null) {
			List<Domain> listDomains = new ArrayList<Domain>();
			for (Role roles : identity.getRoles()) {
				if (roles.getName().contains("Administrador General")) {
					this.llenarDomainList();
					listDomains = new ArrayList<Domain>();
					general = true;
					break;
				}
			}
			if (!general) {
				Domain[] list = authorization.getDomainsByAdministrator(identity.getUserName());
				for (Domain domain : list) {
					listDomains.add(domain);
				}
			}
			if (listDomains.size() > 0) {
				llenarDomainList(listDomains);
			}
		}
		return domainList;
	}

	public void llenarDomainList(List<Domain> listDomains) {
		this.domainList = new ArrayList<SelectItem>();
		this.domainList.add(new SelectItem(new Long(0),"----"));
		try {
			for (Domain domain : listDomains) {
				this.domainList.add(new SelectItem(domain.getId(), domain.getName()));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void llenarDomainList() {
		this.domainList = new ArrayList<SelectItem>();
		try {
			this.setUp();
			Domain[] list = authorization.getDomains();
			List<Domain> domains = new ArrayList<Domain>();
			for (Domain domain : list) {
				domains.add(domain);
			}
			this.domainList.add(new SelectItem(new Long(0),"----"));
			for (Domain domain : domains) {
				this.domainList.add(new SelectItem(domain.getId(), domain.getShortName()));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void setUp() throws Exception {
		authorization = getAuthorization();
	}

	public static List<SelectItem> populateRoleListSelectItem(List<Role> roleList) {
		List<SelectItem> respuesta = new ArrayList<SelectItem>();
		for (Role role : roleList) {
			respuesta.add(new SelectItem(role.getId(), role.getDescription()));
		}
		return respuesta;
	}
}
