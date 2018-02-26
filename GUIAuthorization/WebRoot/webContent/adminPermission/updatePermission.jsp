<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<jsp:root version="1.2" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
	<html>
	<body>
	<ui:composition template="./../template/mainTemplate.jsp">
		<ui:define name="content">
			<div align="left" class="title"><ice:outputText value="Crear Permisos"></ice:outputText>
			</div><br/>
			<ice:panelGrid columns="2">
				<ice:outputText value="Dominio"></ice:outputText>
				<ice:outputText value="Grupo Permiso"></ice:outputText>
				<ice:selectOneMenu value="#{permissionBean.idDomain}"
					valueChangeListener="#{permissionBean.reloadGroupPermissionList}"
					partialSubmit="true" styleClass="combo">
					<f:selectItems value="#{listadosGUIAuthorization.domainList}" />
				</ice:selectOneMenu>
				<ice:selectOneMenu value="#{permissionBean.groupPermissionKey}"
					valueChangeListener="#{permissionBean.reloadPermissionList}"
					partialSubmit="true" styleClass="combo">
					<f:selectItem itemValue="0" itemLabel=" -----" />
					<f:selectItems value="#{permissionBean.groupPermissionList}" />
				</ice:selectOneMenu>
			</ice:panelGrid>

			<ice:panelGrid columns="1">
				<ice:outputLabel value="Nombre" />
				<ice:inputText value="#{permissionBean.permissionUpd.name}"
					id="nameAdd" styleClass="cajaSimple"/>
				<ice:outputLabel value="Descripcion" />
				<ice:inputText value="#{permissionBean.permissionUpd.description}" styleClass="cajaSimple"/>
			</ice:panelGrid>
			
<ice:panelGrid columns="2">
			<ice:commandButton value="Guardar" action="#{permissionBean.updatePermission}" styleClass="button"></ice:commandButton>
			<ice:commandButton value="Regresar" action="return" styleClass="button"></ice:commandButton>
			<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>
			</ice:panelGrid>
			
		</ui:define>
	</ui:composition>
	</body>
	</html>

</jsp:root>