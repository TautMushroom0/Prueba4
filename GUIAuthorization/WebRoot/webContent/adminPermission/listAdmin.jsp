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
			<div align="left" class="title"><ice:outputText value="Administración de Permisos"
				></ice:outputText></div><br/>
			<ice:panelGrid columns="1">
				<ice:panelGrid columns="2">
					<ice:outputText value="Dominio"></ice:outputText>
					<ice:outputText value="Grupo Permiso"></ice:outputText>
					<ice:selectOneMenu value="#{permissionBean.idDomain}"
						valueChangeListener="#{permissionBean.reloadGroupPermissionList}"
						partialSubmit="true" styleClass="combo">
						<f:selectItems value="#{listadosGUIAuthorization.domainList}" />
					</ice:selectOneMenu>
					<ice:selectOneMenu value="#{permissionBean.groupPermissionKey}"
						valueChangeListener="#{permissionBean.reloadPermissionList}"
						partialSubmit="true" styleClass="combo">
						<f:selectItem itemValue="0" itemLabel=" -----" />
						<f:selectItems value="#{permissionBean.groupPermissionList}" />
					</ice:selectOneMenu>
				</ice:panelGrid>
				<f:verbatim>
					<br />
				</f:verbatim>
				<ice:messages></ice:messages>
				<f:verbatim>
					<br />
				</f:verbatim>
				<ice:panelGrid columns="2"
					rendered="#{permissionBean.groupPermissionKey!=0}">
					<ice:outputText value="Busqueda por Descripción" />
					<ice:outputText />
					<ice:outputLabel value="Descripción:" />
					<ice:panelGroup>
						<ice:inputText value="#{permissionBean.description}" styleClass="cajaSimple"></ice:inputText>
					</ice:panelGroup>
					<ice:panelGroup>
						<ice:commandButton value="Buscar Todos"
							action="#{permissionBean.loadPermissionList}" styleClass="button"></ice:commandButton>
					</ice:panelGroup>
					<ice:panelGroup>
						<ice:commandButton value="Buscar"
							action="#{permissionBean.findDescription}" styleClass="button"></ice:commandButton>
					</ice:panelGroup>
				</ice:panelGrid>
				<ice:panelGrid columns="1"
					rendered="#{!permissionBean.permissionEmpty}">
					<ice:dataTable var="permission"
						rendered="#{!permissionBean.permissionEmpty}"
						value="#{permissionBean.permissionList}"
						binding="#{permissionBean.permissions}" id="permissionList"
						rows="#{permissionBean.rowsNumber}" >
						<ice:column>
							<f:facet name="header">
								<ice:columnGroup>
									<ice:headerRow>
										<ice:column rowspan="2">
											<ice:outputText value="Nombre">
											</ice:outputText>
										</ice:column>
										<ice:column rowspan="2">
											<ice:commandButton title="Ordenar por Nombre" id="btnOrdName"
												image="./css/css-images/btn_order.gif"
												action="#{permissionBean.orderListPermissionByName}"></ice:commandButton>
										</ice:column>
									</ice:headerRow>
								</ice:columnGroup>
							</f:facet>
							<ice:outputText value="#{permission.name}" id="nameUpd" />
						</ice:column>
						<ice:column>
							<f:facet name="header">
								<ice:columnGroup>
									<ice:headerRow>
										<ice:column rowspan="2">
											<ice:outputText value="Descripción">
											</ice:outputText>
										</ice:column>
										<ice:column rowspan="2">
											<ice:commandButton title="Ordenar por Descripción"
												id="btnOrdDesc" image="./css/css-images/btn_order.gif"
												action="#{permissionBean.orderListPermissionByDesc}"></ice:commandButton>
										</ice:column>
									</ice:headerRow>
								</ice:columnGroup>
							</f:facet>
							<ice:outputText value="#{permission.description}" />
						</ice:column>
						<ice:column>
							<ice:commandButton
								image="./css/css-images/tree_nav_top_close_no_siblings.gif"
								action="#{permissionBean.deletePermission}"
								title="Eliminar Permiso">
							</ice:commandButton>
						</ice:column>
						<ice:column>
							<ice:commandButton image="./css/css-images/theme_refresh.png"
								action="#{permissionBean.verAction}" title="Actualizar Permiso">
							</ice:commandButton>
						</ice:column>
						<f:facet name="footer">
							<ice:dataPaginator id="dataScroll_1" for="permissionList"
								fastStep="3" paginator="true" paginatorMaxPages="4"
								rendered="#{permissionBean.renderPaginator}">
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
						</f:facet>
					</ice:dataTable>
				</ice:panelGrid>
			</ice:panelGrid>
		</ui:define>
	</ui:composition>
	</body>
	</html>

</jsp:root>