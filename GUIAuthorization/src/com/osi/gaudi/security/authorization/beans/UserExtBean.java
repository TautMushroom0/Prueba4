package com.osi.gaudi.security.authorization.beans;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import com.osi.gaudi.security.authorization.entity.User;
import com.osi.gaudi.security.authorization.utils.FacesUtils;
import com.osi.gaudi.security.util.EEncryptingAlgorithm;
import com.osi.gaudi.security.util.EncryptingUtil;
import com.osi.security.authentication.InfoAuthextDTO;
import com.osi.security.authentication.UserResponseDTO;


public class UserExtBean  extends UserBean{

	private static Log log = LogFactory.getLog(UserExtBean.class);
	private List<SelectItem> tiposIdentidadList = new ArrayList<SelectItem>();
	private List<SelectItem> estadoCuentaList = new ArrayList<SelectItem>();
	private String tipoIdentidad;
	private String numeroIdentidad;
	private String codigoAzimut;
	private String fullName;
	private String userName;
	private String estadoCuenta;
	private boolean viewPopupUser=true;
	private List<User> usersFindList;
	
	private boolean usersFindEmpty = true;
	private boolean userFind = false;
	
	public UserExtBean() throws Exception {
		super();
		setTipoIdentidadList();
		setEstadoCuentaList();
	}
	
	public void setTipoIdentidadList() {
		
		this.tiposIdentidadList.add(new SelectItem("0",""));
		this.tiposIdentidadList.add(new SelectItem("C","CEDULA DE CIUDADANIA"));
		this.tiposIdentidadList.add(new SelectItem("N","NUMERO DE IDENTIFICACION TRIBUTARIA"));
		this.tiposIdentidadList.add(new SelectItem("R","REGISTRO CIVIL"));
		this.tiposIdentidadList.add(new SelectItem("T","TARJETA DE IDENTIDAD"));
		this.tiposIdentidadList.add(new SelectItem("M","MENOR SIN IDENTIFICAR"));
		this.tiposIdentidadList.add(new SelectItem("E","CEDULA DE EXTRANJERIA"));
		this.tiposIdentidadList.add(new SelectItem("P","PASAPORTE"));
		this.tiposIdentidadList.add(new SelectItem("G","BEBE EN GESTACION"));
		
	}
	
	

	/**
	 * @return the tipoIdentidad
	 */
	public String getTipoIdentidad() {
		return tipoIdentidad;
	}

	/**
	 * @param tipoIdentidad the tipoIdentidad to set
	 */
	public void setTipoIdentidad(String tipoIdentidad) {
		this.tipoIdentidad = tipoIdentidad;
	}

	/**
	 * @return the numeroIdentidad
	 */
	public String getNumeroIdentidad() {
		return numeroIdentidad;
	}

	/**
	 * @param numeroIdentidad the numeroIdentidad to set
	 */
	public void setNumeroIdentidad(String numeroIdentidad) {
		this.numeroIdentidad = numeroIdentidad;
	}

	/**
	 * @return the tiposIdentidadList
	 */
	public List<SelectItem> getTiposIdentidadList() {
		return tiposIdentidadList;
	}

	/**
	 * @param tiposIdentidadList the tiposIdentidadList to set
	 */
	public void setTiposIdentidadList(List<SelectItem> tiposIdentidadList) {
		this.tiposIdentidadList = tiposIdentidadList;
	}
	
	public void showPanelUser() {
		this.viewPopupUser = true;
		setUserName("");
		this.usersFindList = new ArrayList<User>();
		this.usersFindEmpty=true;
		this.userFind=false;
	}
	/**
	 * Metodo que permite recuperar la informaciond e usuario
	 */
	public void seleccionarUsuario(){
		userName = (String) FacesUtils.getRequestParameter("userId");
		this.viewPopupUser = false;
		this.userFind=true;
		
	}
	
	/**
	 * Metodo que permite cerrar popup de usuarios y reiniciar la pangina
	 */
	public void cerrarPopup(){
		this.userName = null;
		this.viewPopupUser = false;
		this.userFind=false;
		
	}
	
    public void  actualizarEstadoCuenta(){
		
		try {
			String userEncryp = EncryptingUtil.encrypt(userName,EEncryptingAlgorithm.AES);
			String estadoEncryp = EncryptingUtil.encrypt(estadoCuenta,EEncryptingAlgorithm.AES);
			
			AuthtMediator authentication = new AuthtMediator();
			UserResponseDTO responseDTO = authentication.changeUserStatus(userEncryp, estadoEncryp);
			
			FacesUtils.addMensajePagina(responseDTO.getMessage());
			
		} catch (Exception e) {
			FacesUtils.addMensajePagina("Se presento un al consultar el usuario ",e.getMessage());
			log.error(e.getMessage(), e);
		}
	}
    
    /*Cambio Daniel Sandoval */
    public void  recordarPassword(){
		
		try {
			String userEncryp = EncryptingUtil.encrypt(userName,EEncryptingAlgorithm.AES);
			
			AuthtMediator authentication = new AuthtMediator();
			UserResponseDTO responseDTO = authentication.rememberPassword(userEncryp);
			
			FacesUtils.addMensajePagina(responseDTO.getMessage());
			
		} catch (Exception e) {
			FacesUtils.addMensajePagina("Se presento un error al recordarPassword ",e.getMessage());
			log.error(e.getMessage(), e);
		}
	}
	
	public void addInfoAuthentication() throws Exception{
		try {
			
			InfoAuthextDTO dto = new InfoAuthextDTO();
			
			dto.setAzimutCode(new Long(EncryptingUtil.decrypt(this.getCodigoAzimut(), EEncryptingAlgorithm.AES)).longValue());
			dto.setKeyQuestion("A");
			dto.setKeyAnswer("B");
			dto.setDocumentType(this.getTipoIdentidad());
			dto.setDocument(this.getNumeroIdentidad());
			dto.setNick(EncryptingUtil.encrypt(this.getMail(), EEncryptingAlgorithm.AES));
			
			AuthtMediator authentication = new AuthtMediator();
			UserResponseDTO responseDto = authentication.createInfAuthentication(dto);
						
			if ( responseDto.getResponseCode() != 21){
				FacesUtils.addMensajePagina(responseDto.getMessage());
				throw new Exception();
			}
			
			 			
		} catch (Exception e) {
			if (e.getCause() instanceof ConstraintViolationException){
			  FacesUtils.addMensajePagina("El usuario ya tiene una cuenta creada"); //$NON-NLS-1$
			}else{
				FacesUtils.addMensajePagina("Se presento un error al crear la cuenta. Consulte con el administrador del Sistema");
				log.error(e.getMessage(), e);
				throw new Exception();
			}
		}
	}

	/**
	 * @return the codigoAzimut
	 */
	public String getCodigoAzimut() {
		return codigoAzimut;
	}

	/**
	 * @param codigoAzimut the codigoAzimut to set
	 */
	public void setCodigoAzimut(String codigoAzimut) {
		this.codigoAzimut = codigoAzimut;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the estadoCuentaList
	 */
	public List<SelectItem> getEstadoCuentaList() {
		return estadoCuentaList;
	}

	/**
	 * @param estadoCuentaList the estadoCuentaList to set
	 */
	public void setEstadoCuentaList() {
		
		this.estadoCuentaList.add(new SelectItem("A","ACTIVO"));
		this.estadoCuentaList.add(new SelectItem("I","INACTIVO"));
		this.estadoCuentaList.add(new SelectItem("B","BLOQUEADO"));
		
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the viewPopupUser
	 */
	public boolean isViewPopupUser() {
		return viewPopupUser;
	}

	/**
	 * @param viewPopupUser the viewPopupUser to set
	 */
	public void setViewPopupUser(boolean viewPopupUser) {
		this.viewPopupUser = viewPopupUser;
	}

	/**
	 * @return the usersFindEmpty
	 */
	public boolean isUsersFindEmpty() {
		return usersFindEmpty;
	}

	/**
	 * @param usersFindEmpty the usersFindEmpty to set
	 */
	public void setUsersFindEmpty(boolean usersFindEmpty) {
		this.usersFindEmpty = usersFindEmpty;
	}

	/**
	 * @return the usersFindList
	 */
	public List<User> getUsersFindList() {
		return usersFindList;
	}

	/**
	 * @param usersFindList the usersFindList to set
	 */
	public void setUsersFindList(List<User> usersFindList) {
		this.usersFindList = usersFindList;
	}

	/**
	 * @return the estadoCuenta
	 */
	public String getEstadoCuenta() {
		return estadoCuenta;
	}

	/**
	 * @param estadoCuenta the estadoCuenta to set
	 */
	public void setEstadoCuenta(String estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	/**
	 * @return the userFind
	 */
	public boolean isUserFind() {
		return userFind;
	}

	/**
	 * @param userFind the userFind to set
	 */
	public void setUserFind(boolean userFind) {
		this.userFind = userFind;
	}
	
	
}
