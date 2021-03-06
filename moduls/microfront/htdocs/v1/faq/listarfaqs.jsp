<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>">
	<logic:present name="MVS_micrositetitulo">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:message key="listarfaqs.listado"/></title>
	</logic:present>			
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>
	<script type="text/javascript" src="v1/js/globales.js"></script>
	
	<jsp:include page="/v1/general/scriptmenu.jsp"/>
	

	</head>

	
	<body>
		
		<div id="contenedor">
			<!-- capçal -->
			<jsp:include page="/v1/general/cabecera.jsp"/>

			<!-- continguts -->
			<div id="continguts">
				<!-- trazabilidad -->
				<ul id="mollaPa">
					<li><a href="home.do?idsite=<bean:write name="MVS_idsite" />&amp;lang=<bean:write name="MVS_idioma" />"><bean:message key="general.inicio"/></a></li>
					<li><bean:message key="listarfaqs.preguntas"/></li>
				</ul>
				
				<!-- campanya -->
				<logic:present name="MVS_home_tmp_campanya">
					<bean:write name="MVS_home_tmp_campanya" filter="false"/>
					<!-- Capa separadora. NO quitar nunca  -->	
					<div id="enllasDestPeu"></div>
				</logic:present>
				<!-- fin campanya -->				


				<!-- menu -->
					<logic:present name="MVS_microsite">
							<logic:equal name="MVS_microsite" property="tipomenu" value="1">
								<div id="marcLateral">
								<h2 class="invisible">Menú general</h2>
									<jsp:include page="/v1/general/menu.jsp"/>
								</div>					
							</logic:equal>
							<logic:equal name="MVS_microsite" property="tipomenu" value="2">
								<div id="marcLateralAmbIcones">
								<h2 class="invisible">Menú general</h2>
									<jsp:include page="/v1/general/menu.jsp"/>
								</div>					
							</logic:equal>
						</logic:present>	
				<!-- fin menu -->
											 
				<!-- informacio con o sin menu -->
				<logic:equal name="MVS_microsite" property="tipomenu" value="0">
					<div id="infoNoMenu">
				</logic:equal>
				<logic:notEqual name="MVS_microsite" property="tipomenu" value="0">
					<div id="info">
				</logic:notEqual>	
								
								<!-- titol -->
								<h2 id="titolPagina"><bean:message key="listarfaqs.preguntas"/></h2>
								<!-- informacio FAQ-->
								<div id="infoFAQ">
									<br/>
									<logic:iterate name="MVS_listado" id="i">
										<h3><bean:write name="i" property="tema" ignore="true"/></h3>
										<dl>
											<logic:iterate name="i" id="j" property="listadopreguntas">
												<dt><strong><bean:write name="j" property="traduce.pregunta" ignore="true" filter="false" /></strong></dt>
												<dd><bean:write name="j" property="traduce.respuesta" ignore="true" filter="false" />
												<logic:present name="j" property="traduce.url">
														<br/>
														<bean:define id="externa" name="j" property="traduce.url" type="java.lang.String" />
														<bean:define id="sitio" name="MVS_idsite"  />
														<a href="<bean:write name="j" property="traduce.url" />"<%if(externa.indexOf("idsite="+sitio)==-1){out.println("target='_blanck'");}else{out.println("target='_self'");}%>><bean:message key="url.adicional"/></a>
												</logic:present>
												<br/><br/>
												</dd>								
											</logic:iterate>
										</dl>
									</logic:iterate>	
								</div>		
										
				</div>	
				<!-- fin informacio -->
				

			</div>
			<!-- fin continguts -->
			
			
			<!-- peu -->
			<jsp:include page="/v1/general/pie.jsp"/>
			<!-- fin peu -->
		</div>	
	</body>
</html>


