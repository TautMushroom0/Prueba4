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
			<div align="left" class="title"><ice:outputText value="Administración de Roles"
				></ice:outputText></div><br/>
			<ice:panelGrid columns="2" width="500px">
				<ice:outputText value="Dominio"></ice:outputText>
				<ice:outputText value="Grupo Rol"></ice:outputText>
				<ice:selectOneMenu value="#{roleBean.idDomain}"
					valueChangeListener="#{roleBean.reloadGroupRoleList}"
					partialSubmit="true" styleClass="combo">
					<f:selectItems value="#{listadosGUIAuthorization.domainList}" />
				</ice:selectOneMenu>
				<ice:selectOneMenu value="#{roleBean.groupRoleKey}"
					valueChangeListener="#{roleBean.reloadRoleList}"
					partialSubmit="true" styleClass="combo">
					<f:selectItem itemValue="0" itemLabel=" -----" />
					<f:selectItems value="#{roleBean.groupRoleList}" />
				</ice:selectOneMenu>
			</ice:panelGrid>

			<ice:dataTable var="role" value="#{roleBean.roleList}" width="500px"
				binding="#{roleBean.roles}" id="roleList"
				rows="#{roleBean.rowsNumber}">
				<ice:column>
					<f:facet name="header">
						<ice:outputText value="Nombre" />
					</f:facet>
					<ice:outputText value="#{role.name}" action="#{roleBean.verAction}" />
				</ice:column>
				<ice:column>
					<f:facet name="header">
						<ice:outputText value="Descripcion" />
					</f:facet>
					<ice:outputText value="#{role.description}"
						action="#{roleBean.verAction}" />
				</ice:column>
				<ice:column>
					<ice:commandButton
						image="./css/css-images/tree_nav_top_close_no_siblings.gif"
						action="#{roleBean.deleteRole}" title="Eliminar Rol">
					</ice:commandButton>
				</ice:column>
				<ice:column>
					<ice:commandButton image="./css/css-images/theme_refresh.png"
						action="#{roleBean.verAction}" title="Actualizar Rol">
						<f:param name="roleKey" value="#{role.id}" />
					</ice:commandButton>
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
			<div class="mensajeError"><ice:messages globalOnly="true"></ice:messages></div>
		</ui:define>
	</ui:composition>
	</body>
	</html>

</jsp:root>