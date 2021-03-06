<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!--[if !IE]><!--><meta http-equiv="X-UA-Compatible" content="IE=edge" /><!--<![endif]-->
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	
		<!-- tinyMCE. Editar codigo segun el rol (siendo 1 el valor si puede editar codigo). -->
		<script language="javascript" type="text/javascript">
					var editarCodigo = "";
		</script>
		<logic:equal name="MVS_usuario" property="permisosTiny" value="1">
			<script language="javascript" type="text/javascript">
					editarCodigo = "code";
			</script>
		</logic:equal>
		
		<!-- tinyMCE -->
		<script language="javascript" type="text/javascript" src="tinymce/tinymce.min.js"></script>
		<script language="javascript" type="text/javascript">
		
		
		//Paso 1. Inicializamos tinyMCE.
		tinymce.init({
		    selector: 'textarea.editorTinyMCE',
			language: 'ca',
			plugins: "code, compat3x, link, textcolor, acheck ,paste, botonMIC"
			,toolbar1: 'bold italic underline | alignleft aligncenter alignright alignjustify bullist numlist | outdent indent | link unlink forecolor removeformat cleanup '+editarCodigo+' acheck | botonMIC'
			,menubar: false
			, content_css: [
			    'css/estils.css'
			] 
			//, extended_valid_elements : "span"
			,external_plugins: {
				"acheck": "plugins/acheck/editor_plugin.js"
			}
			<logic:notEqual name="MVS_usuario" property="permisosTiny" value="1">		
				,paste_as_text: true
				,invalid_elements: 'br'
			</logic:notEqual>
		  });
		
		var Rcajatemp_tiny;
	   	var Rwin_tiny;
		
		function Rmeterurl_tiny(laurl) {
			document.getElementById(Rcajatemp_tiny).value = laurl;
		}	
		</script>
		<!-- /tinyMCE -->
</head>

<body>


	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.recursos" /></li>
		<li><a href="faqs.do"><bean:message key="faq.faqs" /></a></li>
	    <logic:present name="faqForm" property="id">
         		<li class="pagActual"><bean:message key="faq.modificacion" /></li>
	    </logic:present>		
	    <logic:notPresent name="faqForm" property="id">
	         <li class="pagActual"><bean:message key="faq.alta" /></li>
	    </logic:notPresent>				
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/faqs.gif" alt="<bean:message key="faq.faqs" />" />
	<bean:message key="faq.faqs" />. 
		<span>
		    <logic:present name="faqForm" property="id">
	         		<bean:message key="faq.modificacion" />
		    </logic:present>		
		    <logic:notPresent name="faqForm" property="id">
		         <bean:message key="faq.alta" />
		    </logic:notPresent>					
		</span>
	</h1>



	<bean:size id="tamano" name="temasCombo"/>
	<logic:equal name="tamano" value="0">
		<p>
			<div class="alerta" style="font-weight:bold; color:#FF1111;">
				<em><strong><bean:message key="faq.tema.nohay" />.</strong> <bean:message key="faq.tema.alerta" />.&nbsp;&nbsp;&nbsp;<button type="button" title="<bean:message key="tema.crear" />" onclick="document.location='temasAcc.do?accion=crear';"><img src="imgs/botons/nou.gif" alt="<bean:message key="tema.crear" />" /></button> </em><br/>
				<br/>
			</div>
		</p>					
	</logic:equal>
	
	<logic:notEqual name="tamano" value="0">
	<!-- botonera -->
	<div id="botonera">
		<button type="button" title="<bean:message key="op.15" />" onclick="submitForm();"><img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" /></button>
	</div>


	<div style="font-weight:bold; color:#FF4400;">
	<html:errors/>
	</div>
	
		<html:form action="/faqEdita.do" enctype="multipart/form-data"  styleId="accFormulario">
		
				     <logic:present name="faqForm" property="id">
					     <input type="hidden" name="modifica" value="Grabar">
				         <html:hidden property="id" />
				     </logic:present>
					 <logic:notPresent name="faqForm" property="id">
					  	<input type="hidden" name="anyade" value="Crear">
					 </logic:notPresent>    
		
	
				
				<div id="formulario">
					<!-- las tablas están entre divs por un bug del FireFox -->
						<table cellpadding="0" cellspacing="0" class="edicio">
				
								<tr class="par">
										<td class="etiqueta"><bean:message key="faq.fecha" /></td>
										<td>
											<html:text property="fecha" readonly="readonly" maxlength="16" />
										</td>
										<td class="etiqueta"><bean:message key="faq.tema" /></td>
										<td>
							                <html:select property="idTema">
							                	<html:option value=""><bean:message key="faq.selectema" /></html:option>
								                <html:options collection="temasCombo" labelProperty="traduccion.nombre" property="id"/>
							    	        </html:select>
										</td>
								</tr>
								<tr>
										<td class="etiqueta"><bean:message key="faq.visible" /></td>
										<td colspan="3"><html:radio property="visible" value="S" />&nbsp;Sí­<html:radio property="visible" value="N" />&nbsp;No</td>
								</tr>
						
								<tr>
										<td colspan="4">
								
												<ul id="submenu">
													<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
														<li<%=(j.intValue()==0?" class='selec'":"")%>><a href="#" onclick="mostrarForm(this);"><bean:message name="lang" /></a></li>
											        </logic:iterate>
												</ul>    
								
											    <logic:iterate id="traducciones" name="faqForm" property="traducciones" indexId="i" >
											     	     <bean:define id="idiomaahora" value="Catalan" type="java.lang.String" />
												            <logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
												            		<%if(j.intValue()==i.intValue()){%>
															  	<bean:define id="idiomaahora" name="lang" type="java.lang.String" />  
															<%}%>   	
															 
												            </logic:iterate>
												<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
												
													<table cellpadding="0" cellspacing="0" class="edicio">
													<tr>
														<td class="etiqueta"><bean:message key="faq.pregunta" />:</td>
														<td><html:textarea property="pregunta" name="traducciones" styleClass="editorTinyMCE" rows="5" cols="50" indexed="true" /></td>
													</tr>
													<tr><td colspan="2">&nbsp;</td></tr>
													<tr>
														<td class="etiqueta"><bean:message key="faq.respuesta" />:</td>
														<td><html:textarea property="respuesta" name="traducciones" styleClass="editorTinyMCE" rows="5" cols="50" indexed="true" /></td>
													</tr>
													<tr>
														<td class="etiqueta"><bean:message key="url.adicional" />:</td>
														<td></td>
													</tr>
													<tr>																
														<td class="etiqueta"><bean:message key="faq.url" />:</td>
														<td><html:text property="url" name="traducciones"  size="60" maxlength="1024" indexed="true" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].url', 'traducciones[<%=i%>].urlnom','<bean:write name="idiomaahora"/>');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button></td>
													</tr>
													<tr>														
														<td class="etiqueta"><bean:message key="faq.urlnom" />:</td>
														<td><html:text property="urlnom" name="traducciones" size="114" maxlength="512" indexed="true" /></td>
													</tr>
													</table>
											
												</div>
											    </logic:iterate>
								
										</td>
								</tr>
						</table>
				
		
				</div>
		
		
		</html:form>
	
	
	</logic:notEqual>


</body>
</html>

<script type="text/javascript">
<!--
	
	function submitForm(){
		var accForm = document.getElementById('accFormulario');
		accForm.submit();
	}
	
    var RcajatempUrl;
	var RcajatempDesc;

    function Rpopupurl(objurl, objdesc, idioma ) 
    {

      RcajatempUrl =document.faqForm[objurl];
      RcajatempDesc =document.faqForm[objdesc];
      window.open('recursos.do?lang='+idioma,'recursos','scrollbars=yes,width=700,height=400');

    }

      function Rmeterurl(laurl, descr) 
    {
            RcajatempUrl.value=laurl;
			RcajatempDesc.value=descr;

    }
	
// -->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>