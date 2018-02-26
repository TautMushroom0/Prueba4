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
				<ice:outputText value="Crear Grupo Rol"></ice:outputText>
			</div><br/>
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
			<ice:inputText value="#{groupRoleBean.groupRole.name}" id="nameAdd" required="true" styleClass="cajaSimple"/>
			<ice:message for="nameAdd"></ice:message>
			
			<f:facet name="footer">
				<ice:commandButton value="Guardar" action="#{groupRoleBean.addGroupRole}" styleClass="button"></ice:commandButton>
			</f:facet>
			</ice:panelGrid>
			<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>


		</ui:define>
	</ui:composition>
	</body>
	</html>

</jsp:root>