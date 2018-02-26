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
			<div align="left" class="title"><ice:outputText value="Asociar Usuarios a Rol"
				styleClass="title"></ice:outputText></div><br/>
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
					<f:selectItem itemValue="0" itemLabel=" -----" />
					<f:selectItems value="#{roleBean.groupRoleList}" />
				</ice:selectOneMenu>

				<ice:outputLabel value="Rol"></ice:outputLabel>
				<ice:panelGroup>
					<ice:inputText readonly="true" value="#{roleBean.roleName}" styleClass="cajaSimple"></ice:inputText>
					<ice:commandButton value="Buscar"
						action="#{roleBean.showPopupRole}" styleClass="button"></ice:commandButton>
				</ice:panelGroup>

				<f:facet name="footer">
					<ice:messages globalOnly="true"></ice:messages>
				</f:facet>
			</ice:panelGrid>


			<ice:panelGrid columns="1" style="marging-top:20px;">
				<ice:panelGrid rendered="#{!roleBean.usersEmpty}">
					<f:facet name="header">
						<ice:outputLabel value="Usuarios Asociados al Rol"></ice:outputLabel>
					</f:facet>
				</ice:panelGrid>
				<ice:outputText rendered="#{!roleBean.usersEmpty}"
					value="Busqueda de Usuarios"></ice:outputText>
				<ice:panelGrid rendered="#{!roleBean.usersEmpty}" columns="2">
					<ice:outputLabel value="Nombre de Usuario:" />
					<ice:panelGroup>
						<ice:inputText value="#{roleBean.userName}" styleClass="cajaSimple"></ice:inputText>
					</ice:panelGroup>
					<ice:outputLabel value="Nombre:" />
					<ice:panelGroup>
						<ice:inputText value="#{roleBean.name}" styleClass="cajaSimple"></ice:inputText>
					</ice:panelGroup>
					<ice:outputLabel value="Apellido:" />
					<ice:panelGroup>
						<ice:inputText value="#{roleBean.lastName}" styleClass="cajaSimple"></ice:inputText>
					</ice:panelGroup>
				</ice:panelGrid>
				<ice:panelGrid rendered="#{!roleBean.usersEmpty}" columns="3">
					<ice:panelGroup>
						<ice:commandButton value="Buscar  Todos"
							action="#{roleBean.populateUsers}" styleClass="button"></ice:commandButton>
					</ice:panelGroup>
					<ice:panelGroup>
						<ice:commandButton value="Buscar"
							action="#{roleBean.findUserByAll}" styleClass="button"></ice:commandButton>
					</ice:panelGroup>
					<ice:panelGroup>
						<ice:commandButton value="Limpiar" action="#{roleBean.clearField}" styleClass="button"></ice:commandButton>
					</ice:panelGroup>
				</ice:panelGrid>
				<ice:dataTable var="user" value="#{roleBean.users}"
					rendered="#{!roleBean.usersEmpty}"
					style="border: 1px solid #d9d9d9;" width="640px" id="userList"
					rows="#{roleBean.rowsNumber}">
					<ice:column>
						<ice:selectBooleanCheckbox value="#{user.selected}"></ice:selectBooleanCheckbox>
					</ice:column>
					<ice:column>
						<f:facet name="header">
							<ice:columnGroup>
								<ice:headerRow>
									<ice:column rowspan="2">
										<ice:outputText value="Nombre de Usuario">
										</ice:outputText>
									</ice:column>
									<ice:column rowspan="2">
										<ice:commandButton title="Ordenar por Nombre de Usuario"
											id="btnOrdUsn" image="./css/css-images/btn_order.gif"
											action="#{roleBean.orderListUserName}"></ice:commandButton>
									</ice:column>
								</ice:headerRow>
							</ice:columnGroup>
						</f:facet>
						<ice:outputText value="#{user.id}"></ice:outputText>
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
											action="#{roleBean.orderListName}"></ice:commandButton>
									</ice:column>
								</ice:headerRow>
							</ice:columnGroup>
						</f:facet>
						<ice:outputText value="#{user.name}"></ice:outputText>
					</ice:column>
					<ice:column>
						<f:facet name="header">
							<ice:columnGroup>
								<ice:headerRow>
									<ice:column rowspan="2">
										<ice:outputText value="Apellido">
										</ice:outputText>
									</ice:column>
									<ice:column rowspan="2">
										<ice:commandButton title="Ordenar por Apellido"
											id="btnOrdLastN" image="./css/css-images/btn_order.gif"
											action="#{roleBean.orderListLastName}"></ice:commandButton>
									</ice:column>
								</ice:headerRow>
							</ice:columnGroup>
						</f:facet>
						<ice:outputText value="#{user.lastName}"></ice:outputText>
					</ice:column>
					<f:facet name="footer">
						<ice:dataPaginator id="dataScroll_1" for="userList" fastStep="3"
							paginator="true" paginatorMaxPages="4"
							rendered="#{roleBean.renderPaginatorUser}">
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
					<ice:commandButton value="Asociar Usuario"
						action="#{roleBean.showPanelUser}" styleClass="button"></ice:commandButton>
					<ice:commandButton value="Desasociar Usuarios"
						rendered="#{!roleBean.usersEmpty}"
						action="#{roleBean.deleteUsersFromRole}" styleClass="button"></ice:commandButton>
				</ice:panelGrid>
			</ice:panelGrid>

			<ice:panelPopup  rendered="#{roleBean.viewPopupUser}" draggable="true"
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
								<ice:inputText value="#{roleBean.userName}" styleClass="cajaSimple"></ice:inputText>
								<ice:commandButton value="Buscar" action="#{roleBean.findUser}" styleClass="button"></ice:commandButton>
							</ice:panelGroup>

						</ice:panelGrid>
						<ice:panelGrid style="marging-top:20px;" columns="1"
							rendered="#{!roleBean.usersFindEmpty}">
							<f:facet name="header">
								<ice:outputLabel value="Listado de usuarios encontrados"></ice:outputLabel>
							</f:facet>
						</ice:panelGrid>
						<ice:dataTable value="#{roleBean.usersFind}" var="userFind"
							binding="#{roleBean.userFind}" rows="#{roleBean.rowsNumber}"
							width="400px" rendered="#{!roleBean.usersFindEmpty}">
							<ice:column>
								<f:facet name="header">
									<ice:outputLabel value="Nombre de Usuario"></ice:outputLabel>
								</f:facet>
								<ice:outputText value="#{userFind.id}"></ice:outputText>
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
							<ice:column>
								<ice:commandButton id="btnAdd" title="Agregar a lista"
									image="./css/css-images/tree_nav_top_open_no_siblings.gif"
									action="#{roleBean.addListUsers}"></ice:commandButton>
							</ice:column>
						</ice:dataTable>
						<ice:panelGrid style="marging-top:20px;" columns="1"
							rendered="#{!roleBean.usersTmpEmpty}">
							<f:facet name="header">
								<ice:outputLabel value="Listado de usuarios por Asociar"></ice:outputLabel>
							</f:facet>
						</ice:panelGrid>
						<ice:panelGrid columns="2" rendered="#{!roleBean.usersTmpEmpty}">
							<ice:dataTable binding="#{roleBean.user}"
								value="#{roleBean.usersTemp}" var="userTemp"
								rows="#{roleBean.rowsNumber}" width="400px">
								<ice:column>
									<f:facet name="header">
										<ice:outputLabel value="Nombre de Usuario"></ice:outputLabel>
									</f:facet>
									<ice:outputText value="#{userTemp.id}"></ice:outputText>
								</ice:column>
								<ice:column>
									<f:facet name="header">
										<ice:outputLabel value="Nombre"></ice:outputLabel>
									</f:facet>
									<ice:outputText value="#{userTemp.name}"></ice:outputText>
								</ice:column>
								<ice:column>
									<f:facet name="header">
										<ice:outputLabel value="Apellido"></ice:outputLabel>
									</f:facet>
									<ice:outputText value="#{userTemp.lastName}"></ice:outputText>
								</ice:column>
								<ice:column>
									<ice:commandButton title="Eliminar de lista" id="btnDel"
										image="./css/css-images/tree_nav_top_close_no_siblings.gif"
										action="#{roleBean.deleteListUsers}"></ice:commandButton>
								</ice:column>
							</ice:dataTable>
						</ice:panelGrid>
						<ice:panelGrid columns="2">
							<ice:commandButton value="Adicionar"
								action="#{roleBean.addUserToRole}"
								rendered="#{!roleBean.usersTmpEmpty}" styleClass="button"></ice:commandButton>
							<ice:commandButton value="Cerrar"
								action="#{roleBean.closePopupUser}" styleClass="button"></ice:commandButton>
						</ice:panelGrid>
						<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>
					</ice:panelGrid>
				</f:facet>
				<f:facet name="footer">


				</f:facet>
			</ice:panelPopup>

			<ice:panelPopup rendered="#{roleBean.viewPopupRole}" draggable="true"
				modal="true" autoCentre="true">
				<f:facet name="header">
					<ice:outputLabel></ice:outputLabel>
				</f:facet>
				<f:facet name="body">
					<ice:panelGrid columns="1">
						<ice:dataTable var="role" value="#{roleBean.roleList}"
							width="500px" rows="#{roleBean.rowsNumber}" id="roleList">
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
									<ice:outputText value="Descripción" />
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