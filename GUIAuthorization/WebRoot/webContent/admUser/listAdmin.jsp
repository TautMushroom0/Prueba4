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
				<ice:outputText value="Administración de Usuarios"/>
			</div>
			<ice:panelGrid columns="3" width="600px" style="border: 1px solid #d9d9d9;">
				<ice:panelGroup>
					<ice:outputText value="Seleccione un Dominio"></ice:outputText>
					<ice:selectOneMenu value="#{userBean.idDomain}"	valueChangeListener="#{userBean.reloadUserbydomainList}" partialSubmit="true" styleClass="combo">
						<f:selectItems value="#{listadosGUIAuthorization.domainList}" />
					</ice:selectOneMenu>
				</ice:panelGroup>
				<ice:commandLink value="Modificar Usuario" action="#{userBean.modifyUser}"></ice:commandLink>
				<ice:commandLink value="Agregar Usuario" action="#{userBean.addUser}"></ice:commandLink>
			</ice:panelGrid>

			<ice:panelGroup rendered="#{userBean.renderModify}">
			<div style="float: left;">
				<ice:panelGroup>
					<ice:dataTable var="user" value="#{userBean.userbydomainList}"
						width="300px" id="userData" rows="10">
						<ice:column>
							<f:facet name="header">
								<ice:outputText value="Usuarios"></ice:outputText>
							</f:facet>
							<ice:panelGrid columns="1">
								<ice:panelGrid columns="1">
									<ice:commandLink value="#{user.id}"
										action="#{userBean.verAction}" styleClass="textoVinculo">
										<f:param name="userId" value="#{user.id}" />
									</ice:commandLink>
								</ice:panelGrid>
								<ice:panelGrid columns="2">
									<ice:commandLink value="#{user.name} #{user.lastName}"
										action="#{userBean.verAction}" styleClass="textoVinculo">
										<f:param name="userId" value="#{user.id}" />
									</ice:commandLink>

								</ice:panelGrid>
							</ice:panelGrid>
						</ice:column>

						<f:facet name="footer">
							<ice:messages globalOnly="true"></ice:messages>
							<ice:outputText value=""></ice:outputText>
						</f:facet>
					</ice:dataTable>
					<ice:dataPaginator id="dataScroll_1" for="userData" fastStep="3"
						paginator="true" paginatorMaxPages="4"
						rendered="#{userBean.renderPaginator}">
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
								url="./css/css-images/arrow-previous.gif" style="border:none;" />
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
				</ice:panelGroup>
				</div>
				<div style="padding-left: 40px;">
				<ice:dataTable var="role" value="#{userBean.rolebyuserList}"
					width="300px">
					<ice:column>
						<ice:selectBooleanCheckbox value="#{role.selected}"></ice:selectBooleanCheckbox>
					</ice:column>
					<ice:column>
						<f:facet name="header">
							<ice:outputText value="Roles"></ice:outputText>
						</f:facet>

						<ice:outputText value="#{role.description}" />
					</ice:column>
					<f:facet name="footer">
						<ice:panelGrid columns="2">
							<ice:commandButton value="Asociar Roles"
								action="#{userBean.showPanel}" rendered="#{!userBean.userEmpty}" styleClass="button"></ice:commandButton>
							<ice:commandButton value="Desasociar Roles"	rendered="#{!userBean.rolesEmpty}" action="#{userBean.deleteRoleFromUser}" styleClass="button"/>
						</ice:panelGrid>
					</f:facet>
				</ice:dataTable>
				</div>
				</ice:panelGroup>
				
				<ice:panelGroup rendered="#{userBean.renderAdd}">
					<div style="width: 600px;">
					<div style="float: left;">
					<ice:panelGrid columns="1">
						<ice:outputText value="Buscar en LDAP:"></ice:outputText>
						<ice:inputText value="#{userBean.userParamLdap}" styleClass="cajaSimple"></ice:inputText>
						<ice:commandButton value="Consultar" action="#{userBean.findUserLdap}" styleClass="button"></ice:commandButton>
						<ice:outputText value="Buscar Localmente:"></ice:outputText>
						<ice:inputText value="#{userBean.userParamLocal}" styleClass="cajaSimple"></ice:inputText>
						<ice:commandButton value="Consultar" action="#{userBean.findUserLocal}" styleClass="button"></ice:commandButton>
						<f:facet name="footer">
						<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>
						</f:facet>
					</ice:panelGrid>
					</div>
					<div style="margin-left: 20px;float: right">
						<ice:dataTable value="#{userBean.roleNoAttach}" var="roleNoAta">
							<ice:column>
								<ice:selectBooleanCheckbox value="#{roleNoAta.selected}"></ice:selectBooleanCheckbox>
							</ice:column>
							<ice:column>
								<ice:outputText value="#{roleNoAta.name}"></ice:outputText>
							</ice:column>
							<ice:column>
								<ice:outputText value="#{roleNoAta.description}"></ice:outputText>
							</ice:column>
							<f:facet name="footer">
								<ice:commandButton value="Agregar"
									action="#{userBean.addRoleToUserNew}" styleClass="button"></ice:commandButton>
							</f:facet>
						</ice:dataTable>
					</div>
					
					</div>
				</ice:panelGroup>
		
		<ice:panelPopup rendered="#{userBean.viewPopupRole}"
				draggable="true" modal="true" autoCentre="true">
				<f:facet name="header">
					<ice:outputLabel></ice:outputLabel>
				</f:facet>
				<f:facet name="body">
					<ice:dataTable value="#{userBean.roleNoAttach}" var="roleNoAta">
						<ice:column>
							<ice:selectBooleanCheckbox value="#{roleNoAta.selected}"></ice:selectBooleanCheckbox>
						</ice:column>
						<ice:column>
							<ice:outputText value="#{roleNoAta.name}"></ice:outputText>
						</ice:column>
						<ice:column>
							<ice:outputText value="#{roleNoAta.description}"></ice:outputText>
						</ice:column>
						<f:facet name="footer">
							<ice:commandButton value="Aceptar"
								action="#{userBean.addRoleToUser}" styleClass="button"></ice:commandButton>
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