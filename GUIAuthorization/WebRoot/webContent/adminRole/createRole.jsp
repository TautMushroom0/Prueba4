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
			<div align="left" class="title"><ice:outputText value="Crear Rol"
				></ice:outputText></div>
			<ice:panelGrid columns="5">
				<ice:panelGrid columns="1">
					<f:facet name="header">
						<ice:outputLabel value="Dominio"></ice:outputLabel>
					</f:facet>
					<ice:panelGroup>
						<ice:selectOneMenu value="#{roleBean.idDomain}" valueChangeListener="#{roleBean.reloadGroupRoleList}"
					partialSubmit="true" immediate="true" styleClass="combo">
							
							<f:selectItems value="#{listadosGUIAuthorization.domainList}" />
						</ice:selectOneMenu>
					</ice:panelGroup>
				</ice:panelGrid>
				<ice:panelGrid columns="1">
					<f:facet name="header">
						<ice:outputLabel value="Grupo Rol"></ice:outputLabel>
					</f:facet>
					<ice:panelGroup>
						<ice:selectOneMenu value="#{roleBean.groupRoleKey}" styleClass="combo">
							
							<f:selectItems value="#{roleBean.groupRoleList}" />
						</ice:selectOneMenu>
					</ice:panelGroup>
				</ice:panelGrid>
				<ice:panelGrid columns="1">
					<f:facet name="header">
						<ice:outputLabel value="Nombre"></ice:outputLabel>
					</f:facet>
					<ice:panelGroup>
						<ice:inputText value="#{roleBean.name}" id="nombre" required="true" styleClass="cajaSimple"></ice:inputText>
						<ice:message for="nombre"></ice:message>
					</ice:panelGroup>
				</ice:panelGrid>
				<ice:panelGrid columns="1">
					<f:facet name="header">
						<ice:outputLabel value="Descripción"></ice:outputLabel>
					</f:facet>
					<ice:inputText value="#{roleBean.description}" id="descripcion" required="true" styleClass="cajaSimple"></ice:inputText>
					<ice:message for="descripcion"></ice:message>
				</ice:panelGrid>
				<ice:panelGrid columns="2">
					<ice:commandButton value="Guardar"
						action="#{roleBean.addRole}" styleClass="button">
					</ice:commandButton>
				</ice:panelGrid>
				<f:facet name="footer">
					<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>
				</f:facet>
			</ice:panelGrid>


			</ui:define>
	</ui:composition>
	</body>
	</html>

</jsp:root>