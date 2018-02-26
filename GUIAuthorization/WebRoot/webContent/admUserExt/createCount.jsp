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
			<div align="left" class="title"><ice:outputText value="Crear Cuenta para Usuario Externo" ></ice:outputText>
			</div><br/>
			<ice:panelGrid columns="1">
				
				<ice:panelGrid columns="1">
					<ice:outputText value="Tipo identidad:"></ice:outputText>
					<ice:panelGroup>
					<ice:selectOneMenu value="#{userExtBean.tipoIdentidad}" styleClass="combo">
						<f:selectItems value="#{userExtBean.tiposIdentidadList}" />
					</ice:selectOneMenu>
				    </ice:panelGroup>
					<ice:outputLabel value="Número:" />
					<ice:panelGroup>
					<ice:inputText value="#{userExtBean.numeroIdentidad}"
						id="numeroAdd" required="true" styleClass="cajaSimple"/>
						<ice:message for="numeroAdd"></ice:message>
					</ice:panelGroup>
					<ice:outputLabel value="Email:" />
					<ice:panelGroup>
					<ice:inputText value="#{userExtBean.mail}" id="emailnAdd"
						required="true" styleClass="cajaSimple"/>
						<ice:message for="emailAdd"></ice:message>
						</ice:panelGroup>
						
					<ice:outputLabel value="Nombre:" />
					<ice:panelGroup>
						<ice:outputLabel value="#{userExtBean.fullName}" id="nombreAdd"
						 styleClass="cajaSimple"/>
						<ice:message for="nombreAdd"></ice:message>
					</ice:panelGroup>
						
							
					<f:facet name="footer">
						<ice:commandButton value="Crear"
							action="#{userExtBean.validarUsuario}" styleClass="button"></ice:commandButton>
					</f:facet>
				</ice:panelGrid>
				<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>
			</ice:panelGrid>
		</ui:define>
	</ui:composition>
	</body>
	</html>

</jsp:root>