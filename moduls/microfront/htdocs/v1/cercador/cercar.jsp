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
	<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:message key="cercar.resultados" /></title>			
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>
	<script type="text/javascript" src="v1/js/globales.js"></script>

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
					<li><bean:message key="cercar.resultados" /></li>
				</ul>

											 
				<!-- informacio sin menu -->
				<div id="infoNoMenu">
				
								<!-- titol -->
								<h2 id="titolPagina"><bean:message key="cercar.resultados" /></h2>
								
								<logic:present name="MVS_listado_buscador">
								
										<logic:present name="MVS_listado_buscador" property="lista">
										
											<p>
												<strong><bean:message key="cercar.encontrados" /> <bean:write name="MVS_listado_buscador" property="numEncontrados"/> <bean:message key="cercar.resultados.en" /> 
												"<bean:write name="MVS_listado_buscador" property="consultaOriginal"/>"</strong>
											</p>								
										
											
											<bean:define id="docus" name="MVS_listado_buscador" property="lista"/>
									
									   		<logic:iterate id="i" name="docus">
									   		
												<ul class="resultatsRecercaInteligent">
													<li>
														<a href="<bean:write name="i" property="url"/>"><bean:write name="i" property="titulo"/></a> 
													
														<br/>
														<span><bean:write name="i" property="id" filter="yes"/> </span> 
														<span class="detall"><bean:write name="i" property="descripcion" filter="yes"/></span>
													</li>
												</ul>   		
									
									   		</logic:iterate>
									
											<br/>
									   	</logic:present>
									    	
									    							
							</logic:present>		
					
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


