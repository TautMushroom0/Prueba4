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
			<div align="left" class="title"><ice:outputText value="Actualizar Estado Cuenta para Usuario Externo" ></ice:outputText>
			</div><br/>
			<ice:panelGrid columns="1">
				
				<ice:panelGrid columns="2">
					
					<ice:outputLabel value="Usuario:" rendered="#{userExtBean.userFind}"/>
					<ice:panelGroup>
					<ice:outputText value="#{userExtBean.userName}" rendered="#{userExtBean.userFind}"
						styleClass="cajaSimple"/>
						<ice:message for="userName"></ice:message>
					</ice:panelGroup>
					
					<ice:outputText value="Cambiar a Estado:" rendered="#{userExtBean.userFind}"></ice:outputText>
					<ice:panelGroup>
					<ice:selectOneMenu value="#{userExtBean.estadoCuenta}" rendered="#{userExtBean.userFind}" styleClass="combo">
						<f:selectItems value="#{userExtBean.estadoCuentaList}" />
					</ice:selectOneMenu>
				    </ice:panelGroup>
					
					<ice:commandButton value="Buscar Usuario"
						action="#{userExtBean.showPanelUser}" styleClass="button"></ice:commandButton>
					 
					 
					<ice:commandButton value="Actualizar" rendered="#{userExtBean.userFind}"
							action="#{userExtBean.actualizarEstadoCuenta}" styleClass="button"></ice:commandButton>
							
					<ice:commandButton value="Recordar Password" rendered="#{userExtBean.userFind}"
							action="#{userExtBean.recordarPassword}" styleClass="button"></ice:commandButton>		
					
				</ice:panelGrid>
				<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>
			</ice:panelGrid>
			
			<ice:panelPopup  rendered="#{userExtBean.viewPopupUser}" draggable="true"
				modal="true" autoCentre="true">

				<ice:messages globalOnly="true"></ice:messages>

				<f:facet name="header">
					<ice:outputLabel value="Busqueda de Usuario a Asociar"></ice:outputLabel>
				</f:facet>
				<f:facet name="body">
					<ice:panelGrid columns="1">
						<ice:panelGrid columns="1">
							<ice:outputText
								value="Ingrese el Nombre de Usuario y haga clic en Buscar:"></ice:outputText>
							<ice:panelGroup>
								<ice:inputText value="#{userExtBean.userName}" styleClass="cajaSimple"></ice:inputText>
								<ice:commandButton value="Buscar" action="#{userExtBean.findUser}" styleClass="button"></ice:commandButton>
							</ice:panelGroup>
							<ice:panelGroup>
								<ice:commandButton value="Cerrar" action="#{userExtBean.cerrarPopup}" styleClass="button"></ice:commandButton>
							</ice:panelGroup>
							

						</ice:panelGrid>
						<ice:panelGrid style="marging-top:20px;" columns="1"
							rendered="#{!userExtBean.usersFindEmpty}">
							<f:facet name="header">
								<ice:outputLabel value="Listado de usuarios encontrados"></ice:outputLabel>
							</f:facet>
						</ice:panelGrid>
						<ice:dataTable value="#{userExtBean.usersFindList}" var="userFind"
							rows="#{roleBean.rowsNumber}"
							width="400px" rendered="#{!userExtBean.usersFindEmpty}">
							<ice:column>
								<f:facet name="header">
									<ice:outputLabel value="Nombre de Usuario"></ice:outputLabel>
								</f:facet>
								<ice:commandLink value="#{userFind.id}" action="#{userExtBean.seleccionarUsuario}">
									<f:param value="#{userFind.id}" name="userId" />
								</ice:commandLink>
								
							</ice:column>
							<ice:column>
								<f:facet name="header">
									<ice:outputLabel value="Nombre"></ice:outputLabel>
								</f:facet>
								<ice:outputText value="#{userFind.name}"></ice:outputText>
							</ice:column>
							<ice:column>
								<f:facet name="header">
									<ice:outputLabel value="Apellido"></ice:outputLabel>
								</f:facet>
								<ice:outputText value="#{userFind.lastName}"></ice:outputText>
							</ice:column>
						</ice:dataTable>
						<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>
					</ice:panelGrid>
				</f:facet>
				<f:facet name="footer">


				</f:facet>
			</ice:panelPopup>
		</ui:define>
	</ui:composition>
	</body>
	</html>

</jsp:root>