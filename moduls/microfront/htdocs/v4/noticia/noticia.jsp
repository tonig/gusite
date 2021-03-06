<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="es.caib.gusite.micromodel.Noticia"%>
<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.png" type="image/x-ico"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>">	
	<logic:present name="MVS_noticia" property="traduce">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:write name="MVS_noticia" property="traduce.titulo" filter="false"/></title>
	</logic:present>
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>
	<script type="text/javascript" src="v4/js/jquery/jquery-1.11.0.min.js"></script>
	<script type="text/javascript" src="v4/js/globales.js"></script>
	
	<jsp:include page="/v4/general/scriptmenu.jsp"/>
	</head>
	<body>
		<div id="contenedor">
			<!-- capçal -->
			<jsp:include page="/v4/general/cabecera.jsp"/>
			<!-- continguts -->
			<div id="continguts">
				<!-- campanya -->
				<logic:present name="MVS_home_tmp_campanya">
					<bean:write name="MVS_home_tmp_campanya" filter="false"/>
				</logic:present>
				<!-- fin campanya -->				
				<!-- menu -->
				<logic:present name="MVS_microsite">
					<logic:equal name="MVS_microsite" property="tipomenu" value="1">
						<div id="marcLateral">
							<jsp:include page="/v4/general/menu.jsp"/>
						</div>					
					</logic:equal>
					<logic:equal name="MVS_microsite" property="tipomenu" value="2">
						<div id="marcLateralAmbIcones">
							<jsp:include page="/v4/general/menu.jsp"/>
						</div>					
					</logic:equal>
				</logic:present>	
				<!-- fin menu -->							 
				<!-- informacio o sin menu -->
				<logic:equal name="MVS_microsite" property="tipomenu" value="0">
					<div id="infoNoMenu">
				</logic:equal>
				<logic:notEqual name="MVS_microsite" property="tipomenu" value="0">
					<div id="info">
				</logic:notEqual>	
						
					<h2><bean:message key="noticia.detalle"/> <bean:write name="MVS_tiponoticia" /></h2>
					<!-- informacio -->
					<div id="infoNoticia">
						<logic:present name="MVS_noticia" property="traduce">
							<h3 id="titolNoticia"><bean:write name="MVS_noticia" property="traduce.titulo" filter="false"/></h3>
							<p id="subtitol"><bean:write name="MVS_noticia" property="traduce.subtitulo" filter="false"/></p>
							<logic:notEmpty name="MVS_noticia" property="imagen">
								<img id="imgNoticia" src="archivopub.do?ctrl=<bean:write name="MVS_servicio"/><bean:write name="MVS_noticia" property="id" />ZI<bean:write name="MVS_noticia" property="imagen.id" />&amp;id=<bean:write name="MVS_noticia" property="imagen.id" />&amp;nombre=<bean:write name="MVS_noticia" property="imagen.nombre" />" <bean:write name='MVS_anchoImg'/> alt=""/>						
							</logic:notEmpty>
							<p id="fontNoticia"><bean:write name="MVS_noticia" property="fpublicacion" format="dd/MM/yyyy"/><logic:notEmpty name="MVS_noticia" property="traduce.fuente"> - <bean:write name="MVS_noticia" property="traduce.fuente" filter="false"/></logic:notEmpty></p>
							<p><bean:write name="MVS_noticia" property="traduce.texto" filter="false"/></p>	
							<!-- Ubicacion -->																
							<logic:notEmpty name="MVS_noticia" property="latitud">
								<logic:notEmpty name="MVS_noticia" property="longitud">
									<logic:present name="MVS_menu_cont_notic">								
										<logic:equal name="MVS_menu_cont_notic" value="-1">
											<a href="noticia.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;cont=<bean:write name="MVS_noticia" property="id" />&amp;lang=<bean:write name="MVS_idioma" />&amp;fmapa=true"><bean:message key="noticia.ubicacion"/></a>							
										</logic:equal>
										<logic:notEqual name="MVS_menu_cont_notic" value="-1">
											<a href="noticia.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;cont=<bean:write name="MVS_noticia" property="id" />&amp;lang=<bean:write name="MVS_idioma" />&amp;mcont=<bean:write name="MVS_menu_cont_notic" />&amp;fmapa=true"><bean:message key="noticia.ubicacion"/></a>							
										</logic:notEqual>
									</logic:present>
									<logic:notPresent name="MVS_menu_cont_notic">																		
										<a href="noticia.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;cont=<bean:write name="MVS_noticia" property="id" />&amp;lang=<bean:write name="MVS_idioma" />&amp;fmapa=true"><bean:message key="noticia.ubicacion"/></a>																	
									</logic:notPresent>															
								</logic:notEmpty>												
							</logic:notEmpty>
							<logic:notEmpty name="MVS_noticia" property="traduce.docu">
								<p><bean:message key="noticia.descdocumento"/></p>
								<p><bean:message key="general.archivo"/> <bean:write name="MVS_noticia" property="traduce.docu.mime" />, <bean:write name="MVS_noticia" property="traduce.docu.peso" /> bytes - 
								<a href="archivopub.do?ctrl=<bean:write name="MVS_servicio"/><bean:write name="MVS_noticia" property="id" />ZI<bean:write name="MVS_noticia" property="traduce.docu.id" />&amp;id=<bean:write name="MVS_noticia" property="traduce.docu.id" />&amp;nombre=<bean:write name="MVS_noticia" property="traduce.docu.nombre" />" target="blank"><bean:write name="MVS_noticia" property="traduce.docu.nombre" /></a></p>
							</logic:notEmpty>
							<logic:notEmpty name="MVS_noticia" property="traduce.laurl">
								<p>
							   		 <bean:define id="externa" name="MVS_noticia" property="traduce.laurl" type="java.lang.String" />
									 <bean:define id="sitio" name="MVS_idsite"  />
									 <a href="<bean:write name="MVS_noticia" property="traduce.laurl"/>" <%if(externa.indexOf("idsite="+sitio)==-1){out.println("target='_blanck'");}else{out.println("target='_self'");}%>>													
									 <logic:notEmpty name="MVS_noticia" property="traduce.urlnom" >
										<bean:write name="MVS_noticia" property="traduce.urlnom" ignore="true"/>
									 </logic:notEmpty>
									 <logic:empty name="MVS_noticia" property="traduce.urlnom" >
										<bean:message key="url.adicional"/>
									 </logic:empty>													
									</a>
								</p>
							</logic:notEmpty>
						</logic:present>
					</div>				
				</div>	
				<!-- fin informacio -->
			</div>
			<!-- fin continguts -->
			<!-- peu -->
			<jsp:include page="/v4/general/pie.jsp"/>
			<!-- fin peu -->
		</div>	
	</body>
</html>
