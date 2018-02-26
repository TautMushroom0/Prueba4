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
			<div align="left" class="title"><ice:outputText value="Actualizar Dominio"></ice:outputText>
			</div><br/>
			<div id="filtro"></div>


			<ice:panelGrid columns="3">
				<ice:outputLabel value="Nombre"></ice:outputLabel>
				<ice:outputLabel value="Nombre Corto"></ice:outputLabel>
				<ice:outputLabel value="Descripción"></ice:outputLabel>

				<ice:inputText value="#{domainBean.domainUpd.name}" id="nameAdd" styleClass="cajaSimple"/>
				<ice:inputText value="#{domainBean.domainUpd.shortName}" id="shortNameAdd" styleClass="cajaSimple"/>
				<ice:inputText value="#{domainBean.domainUpd.description}" styleClass="cajaSimple"/>
			</ice:panelGrid>
			<ice:panelGrid columns="2">
				<ice:commandButton value="Guardar" action="#{domainBean.updateDomain}" styleClass="button"></ice:commandButton>
				<ice:commandButton value="Regresar" action="return" styleClass="button"></ice:commandButton>
				<f:facet name="footer">
				<ice:messages globalOnly="true"></ice:messages>
			</f:facet>
			</ice:panelGrid>

			
		</ui:define>
	</ui:composition>
	</body>
	</html>

</jsp:root>