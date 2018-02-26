<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ice="http://www.icesoft.com/icefaces/component">

	<body>
		<ui:composition template="./template/mainTemplate.jsp">
			<ui:define name="content">
				<ice:panelGroup rendered="#{menuBean.permissionsEmpty}">
					<br/>
					<ice:outputText value="No hay permisos disponibles" styleClass="title"></ice:outputText>
				</ice:panelGroup>
			</ui:define>
		</ui:composition>
	</body>
</html>

