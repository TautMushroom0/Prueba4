<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<jsp:root version="1.2"
	xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
<html>

	<body>
		<ui:composition template="./template/mainTemplate.jsp">
			<ui:define name="content">
				<ice:panelGroup rendered="#{!userBean.nuevo}">
					<ice:panelGrid columns="4">
						<ice:outputLabel value="Nombre" />
						<ice:outputText value="#{userBean.name}" />
						<ice:outputLabel value="Apellidos" />
						<ice:outputText value="#{userBean.lastName}" />
						<ice:outputLabel value="Email" />
						<ice:outputText value="#{userBean.mail}" />
						<ice:outputLabel value="Estado" />
						<ice:outputText value="#{userBean.nameBlocked}" />
					</ice:panelGrid>

					<ice:dataTable var="roles" value="#{userBean.rolebyuserList}">
						<ice:column>
							<f:facet name="header">
								<ice:outputText value="Nombre" />
							</f:facet>
							<ice:outputText value="#{roles.name}"/>
						</ice:column>
						<ice:column>
							<f:facet name="header">
								<ice:outputText value="Descripción" />
							</f:facet>
							<ice:outputText value="#{roles.description}"/>
						</ice:column>
						<ice:column>
							<ice:commandButton
								image="./css/css-images/tree_nav_top_close_no_siblings.gif"
								actionListener="#{userBean.deleteRole}">
								<f:attribute name="roleKey" value="#{roles.id}" />
							</ice:commandButton>
						</ice:column>
					</ice:dataTable>
				</ice:panelGroup>
			</ui:define>
		</ui:composition>
	</body>
</html>
</jsp:root>
