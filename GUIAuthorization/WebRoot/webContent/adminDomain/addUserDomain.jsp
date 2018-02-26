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
			<div align="left" class="title"><ice:outputText value="Asignar Administrador"
				></ice:outputText></div><br/>
			<div id="filtro"></div>



			<ice:panelGrid columns="2" width="600px">

				<ice:outputLabel value="Dominio"></ice:outputLabel>
				<ice:selectOneMenu value="#{domainBean.shortNameDomain}"
					valueChangeListener="#{domainBean.reloadUsersByDomain}"
					partialSubmit="true" styleClass="combo">
					<f:selectItems value="#{listadosGUIAuthorization.domainList}" />
				</ice:selectOneMenu>

				<ice:outputLabel value="Usuario"></ice:outputLabel>
				<ice:panelGroup>
					<ice:inputText value="#{domainBean.useradmin}" readonly="true" styleClass="cajaSimple"></ice:inputText>
					<ice:commandButton value="Buscar"
						action="#{domainBean.showPopupUser}" styleClass="button"></ice:commandButton>
				</ice:panelGroup>
				<f:facet name="footer">
					<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>
				</f:facet>
			</ice:panelGrid>
			<ice:panelGrid columns="1">
				<ice:commandButton value="Asignar Usuario"
					action="#{domainBean.saveAdminDomain}" styleClass="button"></ice:commandButton>
			</ice:panelGrid>
			<ice:panelGrid columns="1">
				<f:facet name="header">
					<ice:outputLabel value="Usuarios Administradores del Dominio"></ice:outputLabel>
				</f:facet>
				<ice:dataTable var="usersByDomain"
					value="#{domainBean.usersByDomain}" width="600px">
					<ice:column>
						<ice:selectBooleanCheckbox value="#{usersByDomain.selected}"></ice:selectBooleanCheckbox>
					</ice:column>
					<ice:column>
						<f:facet name="header">
							<ice:outputText value="Nombre de Usuario" />
						</f:facet>
						<ice:outputText value="#{usersByDomain.id}"></ice:outputText>
					</ice:column>
					<ice:column>
						<f:facet name="header">
							<ice:outputText value="Nombre" />
						</f:facet>
						<ice:outputText value="#{usersByDomain.name}"></ice:outputText>
					</ice:column>
					<ice:column>
						<f:facet name="header">
							<ice:outputText value="Apellido" />
						</f:facet>
						<ice:outputText value="#{usersByDomain.lastName}"></ice:outputText>
					</ice:column>
				</ice:dataTable>
				<ice:panelGrid columns="1">
					<ice:commandButton value="Desasociar"
						rendered="#{domainBean.renderDelete}"
						action="#{domainBean.deleteAdminDomain}" styleClass="button"></ice:commandButton>
				</ice:panelGrid>
				<ice:panelGrid columns="1">
					<ice:message for="actualizacion" id="actualizacion" />
				</ice:panelGrid>

			</ice:panelGrid>

			<ice:panelPopup rendered="#{domainBean.viewPopupUser}"
				draggable="true" modal="true" autoCentre="true">
				<f:facet name="header">
					<ice:outputLabel value="Lista de Usuarios"></ice:outputLabel>
				</f:facet>
				<f:facet name="body">
					<ice:panelGrid columns="1">
						<ice:dataTable value="#{domainBean.usersAdminDomainList}"
							var="userAdmin" id="userList" rows="10" width="450px"
							rendered="#{!domainBean.userAdminDomainEmpty}">
							<ice:column>
								<ice:commandLink value="#{userAdmin.id}"
									action="#{domainBean.addUser}" styleClass="textoVinculo">
									<f:param value="#{userAdmin.id}" name="userId" />
								</ice:commandLink>
							</ice:column>
							<ice:column>
								<ice:commandLink value="#{userAdmin.name}"
									action="#{domainBean.addUser}" styleClass="textoVinculo"/>
							</ice:column>
							<ice:column>
								<ice:commandLink value="#{userAdmin.lastName}"
									action="#{domainBean.addUser}" styleClass="textoVinculo"/>
							</ice:column>

							<f:facet name="footer">
								<ice:panelGrid columns="1">

									<ice:dataPaginator id="dataScroll_1" for="userList"
										fastStep="3" paginator="true" paginatorMaxPages="4"
										rendered="#{domainBean.renderPaginator}">
										<f:facet name="first">
											<ice:graphicImage id="firstpage_1"
												url="./css/css-images/arrow-first.gif" style="border:none;" />
										</f:facet>
										<f:facet name="last">
											<ice:graphicImage id="lastpage_1"
												url="./css/css-images/arrow-last.gif" style="border:none;" />
										</f:facet>
										<f:facet name="previous">
											<ice:graphicImage id="previouspage_1"
												url="./css/css-images/arrow-previous.gif"
												style="border:none;" />
										</f:facet>
										<f:facet name="next">
											<ice:graphicImage id="nextpage_1"
												url="./css/css-images/arrow-next.gif" style="border:none;" />
										</f:facet>
										<f:facet name="fastforward">
											<ice:graphicImage id="fastforward_1"
												url="./css/css-images/arrow-ff.gif" style="border:none;" />
										</f:facet>
										<f:facet name="fastrewind">
											<ice:graphicImage id="fastrewind_1"
												url="./css/css-images/arrow-fr.gif" style="border:none;" />
										</f:facet>
									</ice:dataPaginator>
								</ice:panelGrid>
							</f:facet>
						</ice:dataTable>
						<div class="mensajeError"><ice:outputLabel
							value="No existen usuarios asociados al Rol Administrador de Dominio"
							rendered="#{domainBean.userAdminDomainEmpty}"></ice:outputLabel></div>
						<ice:commandButton value="Cerrar"
							action="#{domainBean.closePopupUser}" styleClass="button"></ice:commandButton>
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