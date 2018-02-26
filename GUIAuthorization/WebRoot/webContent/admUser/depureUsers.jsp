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
				<ice:outputLabel
					value="Haga click en el boton 'Depurar' para ver los usuarios sin Rol"></ice:outputLabel>
				<ice:commandButton value="Depurar"
					action="#{userBean.findUsersToDepure}" styleClass="button"></ice:commandButton>

				<ice:dataTable var="userDepure" value="#{userBean.usersWithoutRole}"
					width="600px">
					<ice:column>
						<ice:outputText value="#{userDepure.id}"></ice:outputText>
					</ice:column>
					<ice:column>
						<ice:outputText value="#{userDepure.name}"></ice:outputText>
					</ice:column>
					<ice:column>
						<ice:outputText value="#{userDepure.lastName}"></ice:outputText>
					</ice:column>
					<f:facet name="footer">
						<ice:commandButton value="Aceptar"
							action="#{userBean.depureUsers}"></ice:commandButton>
					</f:facet>
				</ice:dataTable>
			</ice:panelGrid>
		</ui:define>
	</ui:composition>
	</body>
	</html>
</jsp:root>
