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

			<ice:panelGrid columns="1">
				<ice:outputText value="Buscar:"></ice:outputText>
				<ice:inputText value="#{userBean.userParamLdap}" styleClass="cajaSimple"></ice:inputText>
				<ice:commandButton value="Consultar"
					action="#{userBean.findUserLdap}" styleClass="button"></ice:commandButton>
				<f:facet name="footer">					
				</f:facet>
			</ice:panelGrid>
			
			<ice:dataTable value="#{userBean.usersFind}" var="user">
				<ice:column>
					<ice:outputText value="#{user.identification}"></ice:outputText>
				</ice:column>
				<ice:column>
					<ice:outputText value="#{user.userName}"></ice:outputText>
				</ice:column>
				<ice:column>
					<ice:outputText value="#{user.name}"></ice:outputText>
				</ice:column>
				<ice:column>
					<ice:outputText value="#{user.surname}"></ice:outputText>
				</ice:column>
				<ice:column>
					<ice:outputText value="#{user.tipo_identificacion}"></ice:outputText>
				</ice:column>
			</ice:dataTable>
			<ice:commandButton value="Agregar" action="#{userBean.addUser}" styleClass="button"></ice:commandButton>
			<ice:messages globalOnly="true" styleClass="button"></ice:messages>
		</ui:define>

	</ui:composition>
	</body>
	</html>
</jsp:root>
