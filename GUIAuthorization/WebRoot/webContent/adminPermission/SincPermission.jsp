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
				<ice:outputText value="Sincronizar Permisos desde ambiente #{sincPermissionBean.serverInterchange} [#{sincPermissionBean.versionAplicacion}]"></ice:outputText>
			</div>
			<br/>
			<div id="filtro"></div>
			<ice:dataTable value="#{sincPermissionBean.domainList}" var="domain" rendered="#{! empty sincPermissionBean.domainList}"
				binding="#{sincPermissionBean.domain}" width="600px" id="domainList" rows="#{sincPermissionBean.rowsNumber}">
				<ice:column>
					<f:facet name="header">
						<ice:outputText value="Nombre"></ice:outputText>
					</f:facet>
					<ice:panelGroup>
						<ice:outputText value="#{domain.name}" id="nameUpd" styleClass="textoVinculo"/>
						<ice:message for="nameUpd" id="messagenameUpd" styleClass="messageError"></ice:message>
						</ice:panelGroup>
				</ice:column>
				<ice:column>
					<f:facet name="header">
						<ice:outputText value="Nombre Corto"></ice:outputText>
					</f:facet>
					
					<ice:panelGroup>
						<ice:outputText value="#{domain.shortName}" id="shortNameUpd" styleClass="textoVinculo"/>
						<div class="mensajeError"><ice:message for="shortNameUpd" styleClass="messageError"></ice:message></div>
						</ice:panelGroup>
				</ice:column>
				<ice:column>
					<f:facet name="header">
						<ice:outputText value="Descripción"></ice:outputText>
					</f:facet>
					<ice:outputText value="#{domain.description}" />
				</ice:column>

				
				<ice:column>
					<ice:commandButton image="./css/css-images/theme_refresh.png"
						action="#{sincPermissionBean.sincronizar}" alt="Actualizar Dominio" title="Sincronizar Dominio" styleClass="button">
					</ice:commandButton>
				</ice:column>
				
				<f:facet name="footer">
					<ice:dataPaginator id="dataScroll_1" for="domainList" fastStep="3"
						paginator="true" paginatorMaxPages="4"
						rendered="#{sincPermissionBean.renderPaginator}">
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
					<ice:messages globalOnly="true"></ice:messages>			
					
		</ui:define>
	</ui:composition>
	</body>
	</html>

</jsp:root>