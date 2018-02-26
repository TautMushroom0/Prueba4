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
			<div align="left" class="title"><ice:outputText value="Asociar Permisos a Rol"
				></ice:outputText></div><br/>
			<ice:panelGrid columns="2">
				<ice:outputLabel value="Dominio"></ice:outputLabel>
				<ice:selectOneMenu value="#{roleBean.idDomain}"
					valueChangeListener="#{roleBean.reloadGroupRoleList}"
					partialSubmit="true" styleClass="combo">
					<f:selectItems value="#{listadosGUIAuthorization.domainList}" />
				</ice:selectOneMenu>

				<ice:outputLabel value="Grupo Rol"></ice:outputLabel>
				<ice:selectOneMenu value="#{roleBean.groupRoleKey}"
					valueChangeListener="#{roleBean.reloadRoleList}"
					partialSubmit="true" styleClass="combo">
					<f:selectItems value="#{roleBean.groupRoleList}" />
				</ice:selectOneMenu>

				<ice:outputLabel value="Rol"></ice:outputLabel>
				<ice:panelGroup>
					<ice:inputText value="#{roleBean.roleName}"  readonly="true" styleClass="cajaSimple"></ice:inputText>
					<ice:commandButton value="Buscar"
						action="#{roleBean.showPopupRole}" styleClass="button"></ice:commandButton>
				</ice:panelGroup>

				<f:facet name="footer">
					<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>
				</f:facet>
			</ice:panelGrid>
		     	<ice:panelGrid columns="1" 
				style="marging-top:20px;">
			<ice:panelGrid columns="2" style="marging-top:20px;">
					<ice:outputText value="Busqueda por Descripción" />
					<ice:outputText />
					<ice:outputLabel value="Descripción:" />
					<ice:panelGroup>
						<ice:inputText value="#{roleBean.description}" styleClass="cajaSimple"></ice:inputText>
					</ice:panelGroup>
					<ice:panelGroup>
						<ice:commandButton value="Buscar Todos"
							action="#{roleBean.populatePermissions}" styleClass="button"></ice:commandButton>
					</ice:panelGroup>
					<ice:panelGroup>
						<ice:commandButton value="Buscar"
							action="#{roleBean.findDescription}" styleClass="button"></ice:commandButton>
					</ice:panelGroup>
				</ice:panelGrid>
				<f:facet name="header">
					<ice:outputLabel value="Permisos Asociados al Rol"></ice:outputLabel>
				</f:facet>
				<ice:dataTable var="permission" value="#{roleBean.permissions}"
					rendered="#{!roleBean.permissionsEmpty}"
					style="border: 1px solid #d9d9d9;" width="640px"
					id="permissionList" rows="#{roleBean.rowsNumber}">
					<ice:column>
						<ice:selectBooleanCheckbox value="#{permission.selected}"></ice:selectBooleanCheckbox>
					</ice:column>
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
												action="#{roleBean.orderListNamePermission}"></ice:commandButton>
										</ice:column>
									</ice:headerRow>
								</ice:columnGroup>
							</f:facet>
						<ice:outputText value="#{permission.name}"></ice:outputText>
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
												action="#{roleBean.orderListDescPermission}"></ice:commandButton>
										</ice:column>
									</ice:headerRow>
								</ice:columnGroup>
							</f:facet>
						<ice:outputText value="#{permission.description}"></ice:outputText>
					</ice:column>
					<f:facet name="footer">
						<ice:dataPaginator id="dataScroll_1" for="permissionList"
							fastStep="3" paginator="true" paginatorMaxPages="4"
							rendered="#{roleBean.renderPaginatorPermission}">
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
				<ice:panelGrid columns="2">
					<ice:commandButton value="Asociar Permisos"
						action="#{roleBean.showPanel}" styleClass="button"></ice:commandButton>
					<ice:commandButton value="Desasociar Permisos"
						rendered="#{!roleBean.permissionsEmpty}"
						action="#{roleBean.deletePermissionsFromRole}" styleClass="button"></ice:commandButton>
				</ice:panelGrid>
			</ice:panelGrid>

			<ice:panelPopup rendered="#{roleBean.viewPopupPermission}"
				draggable="true" modal="true" autoCentre="true" >
				<f:facet name="header">
					<ice:outputLabel
						value="Listado de Permisos Disponibles Para Asociar"></ice:outputLabel>
				</f:facet>
				<f:facet name="body">
					<ice:panelGrid columns="1">
					<ice:panelGrid columns="2" style="marging-top:20px;">
					<ice:outputLabel value="Busqueda por Descripción:" />
					<ice:panelGroup>
						<ice:inputText value="#{roleBean.description}" styleClass="cajaSimple"></ice:inputText>
					</ice:panelGroup>
					<ice:panelGroup>
						<ice:commandButton value="Buscar Todos"
							action="#{roleBean.findAll}" styleClass="button"></ice:commandButton>
					</ice:panelGroup>
					<ice:panelGroup>
						<ice:commandButton value="Buscar"
							action="#{roleBean.findDescPermNoAttach}" styleClass="button"></ice:commandButton>
					</ice:panelGroup>
				</ice:panelGrid>
						<ice:dataTable value="#{roleBean.permNoAttach}" var="permNoAta"
							id="permNoAtaList" rows="#{roleBean.rowsNumber}" width="400px" rendered="#{!roleBean.permissionNoAtaEmpty}">
							<ice:column>
								<ice:selectBooleanCheckbox value="#{permNoAta.selected}"></ice:selectBooleanCheckbox>
							</ice:column>
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
												action="#{roleBean.orderListNamePermNoAttach}"></ice:commandButton>
										</ice:column>
									</ice:headerRow>
								</ice:columnGroup>
							</f:facet>
								<ice:outputText value="#{permNoAta.name}"></ice:outputText>
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
												action="#{roleBean.orderListDescPermNoAttach}"></ice:commandButton>
										</ice:column>
									</ice:headerRow>
								</ice:columnGroup>
							</f:facet>
								<ice:outputText value="#{permNoAta.description}"></ice:outputText>
							</ice:column>
							<f:facet name="footer">
								<ice:dataPaginator id="dataScroll_1" for="permNoAtaList"
									fastStep="3" paginator="true" paginatorMaxPages="4"
									rendered="#{roleBean.renderPaginatorPermNoAta}">
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
							</f:facet>
						</ice:dataTable>
						<ice:outputLabel value="No existen permisos" rendered="#{roleBean.permissionNoAtaEmpty}"></ice:outputLabel>
						<div align="center"><ice:commandButton value="Aceptar"
							action="#{roleBean.addPermissionsToRole}" styleClass="button"></ice:commandButton></div>
					</ice:panelGrid>
				</f:facet>
				<f:facet name="footer">
				</f:facet>
			</ice:panelPopup>

			<ice:panelPopup rendered="#{roleBean.viewPopupRole}" draggable="true"
				modal="true" autoCentre="true">
				<f:facet name="header">
					<ice:outputLabel value="Lista de Roles"></ice:outputLabel>
				</f:facet>
				<f:facet name="body">
					<ice:panelGrid columns="1">
						<ice:dataTable var="role" value="#{roleBean.roleList}"
							width="500px" rows="#{roleBean.rowsNumber}" id="roleList" rendered="#{!roleBean.roleListEmpty}">
							<ice:column>
								<f:facet name="header">
									<ice:outputText value="Nombre" />
								</f:facet>
								<ice:commandLink value="#{role.name}"
									action="#{roleBean.loadRoleName}" styleClass="textoVinculo">
									<f:param name="roleKey" value="#{role.id}" />
								</ice:commandLink>
							</ice:column>
							<ice:column>
								<f:facet name="header">
									<ice:outputText value="Descripcion" />
								</f:facet>
								<ice:commandLink value="#{role.description}"
									action="#{roleBean.loadRoleName}" styleClass="textoVinculo">
									<f:param name="roleKey" value="#{role.id}" />
								</ice:commandLink>
							</ice:column>
							<f:facet name="footer">
								<ice:dataPaginator id="dataScroll_1" for="roleList" fastStep="3"
									paginator="true" paginatorMaxPages="4"
									rendered="#{roleBean.renderPaginator}">
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
							</f:facet>
						</ice:dataTable>
						<ice:outputLabel value="No existen roles" rendered="#{roleBean.roleListEmpty}"></ice:outputLabel>
						<div align="center"><ice:commandButton value="Cerrar"
							action="#{roleBean.closePopupRole}" styleClass="button"></ice:commandButton></div>
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