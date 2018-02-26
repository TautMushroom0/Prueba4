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
			<div align="left" class="title"><ice:outputText value="Administración de Roles"
				></ice:outputText></div><br/>
			<ice:panelGrid columns="4">
				<ice:panelGrid columns="1" >
					<f:facet name="header">
						<ice:outputLabel value="Dominio"></ice:outputLabel>
					</f:facet>
					<ice:panelGroup>
						<ice:selectOneMenu value="#{roleBean.idDomain}" disabled="true" styleClass="combo">
							<f:selectItems value="#{listadosGUIAuthorization.domainList}" />
						</ice:selectOneMenu>
					</ice:panelGroup>
				</ice:panelGrid>
				<ice:panelGrid columns="1" >
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
						<ice:inputText value="#{roleBean.roleUpd.name}" id="nameUpd" styleClass="cajaSimple"></ice:inputText>
						<ice:message for="nameUpd"></ice:message>
					</ice:panelGroup>
				</ice:panelGrid>
				<ice:panelGrid columns="1">
					<f:facet name="header">
						<ice:outputLabel value="Descripción"></ice:outputLabel>
					</f:facet>
					<ice:inputText value="#{roleBean.roleUpd.description}" styleClass="cajaSimple"></ice:inputText>
				</ice:panelGrid>
				<ice:panelGrid columns="2">
					<ice:commandButton value="Guardar" action="#{roleBean.updateRole}" styleClass="button"></ice:commandButton>
					<ice:commandButton value="Regresar" action="regresar" styleClass="button"></ice:commandButton>
				</ice:panelGrid>
				<f:facet name="footer">
					<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>
				</f:facet>
			</ice:panelGrid>


			<ice:dataTable var="permission" value="#{roleBean.permissions}"
				rendered="#{!roleBean.permissionsEmpty}"
				style="border: 1px solid #d9d9d9;" width="640px">
				<f:facet name="header">
					<ice:outputText value="Permisos Asociados a Rol"></ice:outputText>
				</f:facet>
				<ice:column>
					<ice:selectBooleanCheckbox value="#{permission.selected}"></ice:selectBooleanCheckbox>
				</ice:column>
				<ice:column>
					<f:facet name="header">
						<ice:outputText value="Nombre"></ice:outputText>
					</f:facet>
					<ice:outputText value="#{permission.name}"></ice:outputText>
				</ice:column>
				<ice:column>
					<f:facet name="header">
						<ice:outputText value="Descripción"></ice:outputText>
					</f:facet>
					<ice:outputText value="#{permission.description}"></ice:outputText>
				</ice:column>
			</ice:dataTable>
			<ice:outputText value="No hay permisos Asociados"
				rendered="#{roleBean.permissionsEmpty}"></ice:outputText>

			<ice:panelGrid columns="2">
				<ice:commandButton value="Asociar Permisos"
					action="#{roleBean.showPanel}" styleClass="button"></ice:commandButton>
				<ice:commandButton value="Desasociar Permisos"
					rendered="#{!roleBean.permissionsEmpty}"
					action="#{roleBean.deletePermissionsFromRole}" styleClass="button"></ice:commandButton>
			</ice:panelGrid>


			<ice:panelPopup rendered="#{roleBean.viewPopupPermission}"
				draggable="true" modal="true" >
				<f:facet name="header">
					<ice:outputLabel></ice:outputLabel>
				</f:facet>
				<f:facet name="body" >
					<ice:dataTable scrollable="true" columnWidths="10px,180px,180px" scrollHeight="320px;" value="#{roleBean.permNoAttach}" var="permNoAta" >
						<ice:column>
							<ice:selectBooleanCheckbox value="#{permNoAta.selected}"></ice:selectBooleanCheckbox>
						</ice:column>
						<ice:column>
							<ice:outputText value="#{permNoAta.name}"></ice:outputText>
						</ice:column>
						<ice:column>
							<ice:outputText value="#{permNoAta.description}"></ice:outputText>
						</ice:column>
						<f:facet name="footer">
							<ice:commandButton value="Aceptar"
								action="#{roleBean.addPermissionsToRole}" styleClass="button"></ice:commandButton>
						</f:facet>
					</ice:dataTable>
				</f:facet>
				<f:facet name="footer">


				</f:facet>
			</ice:panelPopup>
		</ui:define>
	</ui:composition>
	</body>
	</html>

</jsp:root>