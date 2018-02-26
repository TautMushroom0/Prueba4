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
			<div align="left" class="title"><ice:outputText value="Crear Grupo Permiso"
				></ice:outputText></div><br/>
			<ice:panelGrid columns="1">
				<ice:outputText value="Seleccione el Dominio:"></ice:outputText>
				<ice:selectOneMenu value="#{groupPermissionBean.shortNameDomain}"
					valueChangeListener="#{groupPermissionBean.reloadGroupPermissionList}"
					partialSubmit="true" styleClass="combo">
					<f:selectItems value="#{listadosGUIAuthorization.domainList}" />
				</ice:selectOneMenu>
			</ice:panelGrid>

			<ice:panelGrid columns="1">
				<ice:outputText value="Nombre" />
				<ice:inputText value="#{groupPermissionBean.groupPermission.name}" required="true"
					id="nameAdd" styleClass="cajaSimple"/>
					<ice:message for="nameAdd"></ice:message>
			</ice:panelGrid>
			<ice:panelGrid columns="1">
				<ice:commandButton value="Guardar"
					action="#{groupPermissionBean.addGroupPermission}" styleClass="button"></ice:commandButton>
				<f:facet name="footer">
					<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>
				</f:facet>
			</ice:panelGrid>

		</ui:define>
	</ui:composition>
	</body>
	</html>

</jsp:root>