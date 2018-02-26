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
			<div align="left" class="title">
				<ice:outputText value="Actualizar Grupo Rol" styleClass="title"></ice:outputText>
			</div>
			<ice:panelGrid columns="1">
			<ice:outputText value="Seleccione el Dominio:"></ice:outputText>
			<ice:selectOneMenu value="#{groupRoleBean.shortNameDomain}"
				valueChangeListener="#{groupRoleBean.reloadGroupRoleList}"
				partialSubmit="true" styleClass="combo">
				<f:selectItems value="#{listadosGUIAuthorization.domainList}" />
			</ice:selectOneMenu>
			</ice:panelGrid>
			
			<ice:panelGrid columns="1">
			<ice:outputLabel value="Nombre" />
			<ice:inputText value="#{groupRoleBean.groupRoleUpd.name}" id="nameAdd" styleClass="cajaSimple"/>
			<f:facet name="footer">
				
			</f:facet>
			</ice:panelGrid>
			<ice:panelGrid columns="2">
			<ice:commandButton value="Guardar" action="#{groupRoleBean.updateGroupRole}" styleClass="button"></ice:commandButton>
			<ice:commandButton value="Regresar" action="return" styleClass="button"></ice:commandButton>
			</ice:panelGrid>
			<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>


		</ui:define>
	</ui:composition>
	</body>
	</html>

</jsp:root>