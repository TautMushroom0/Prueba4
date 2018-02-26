<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ice="http://www.icesoft.com/icefaces/component">

<ice:outputDeclaration doctypeRoot="HTML"
	doctypePublic="-//W3C//DTD HTML 4.01 Transitional//EN"
	doctypeSystem="http://www.w3.org/TR/html4/loose.dtd" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>GUI Authorization | OSI</title>

<link href="./css/colsanitas.css" rel="stylesheet" type="text/css" />

</head>
<body>
	<ice:form id="domainForm">
		<div id="ajuste">
			<div id="contenedor">
				<div id="cabezote">
					<div class="cabezoteInt">
						<div class="usuarioLogContenedor">
							<span class="linkUno">Bienvenido(a), </span>
							<ice:outputText value="#{identity.displayName}" styleClass="linkUno" />
							<span class="linkUno"> | </span>
							<ice:commandLink value="Salir" action="#{identity.logout}" styleClass="linkUno" />
						</div>
					</div>
				</div>
				<div id="contenido">
					<div class="conteMenuPpal">
						<ice:menuBar orientation="horizontal">
							<ice:menuItems value="#{menuBean.menuModel}" immediate="true" />
						</ice:menuBar>
					</div>
					<div class="contenidoInt" align="center">
						<ui:insert name="content"></ui:insert>
					</div>
				</div>
				<div id="pie">
					<div class="pieInt"></div>
				</div>
			</div>
		</div>



	</ice:form>
</body>
</html>