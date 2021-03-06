<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="es.caib.gusite.micromodel.Microsite" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!--[if !IE]><!--><meta http-equiv="X-UA-Compatible" content="IE=edge" /><!--<![endif]-->
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
</head>

<body>	
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.configuracion" /></li>
		<li class="pagActual"><bean:message key="menu.cabecerapie" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/configuracion.gif" alt="<bean:message key="menu.cabecerapie" />" />
	<bean:message key="menu.cabecerapie" />. <span><bean:message key="micro.cabpie" /></span></h1>
	
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
		<script language="javascript" type="text/javascript" src="tinymce/plugins/compat3x/plugin.min.js"></script>
		<script language="javascript" type="text/javascript">
		
		
		//Paso 1. Inicializamos tinyMCE.
		tinymce.init({
		    selector: 'textarea.editorTinyMCE',
			language: 'ca',
			plugins: "code, compat3x, link, textcolor, acheck, importcss, image ,paste, botonMIC "
			,toolbar1: 'newdocument | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | styleselect formatselect | botonMIC' 
			,toolbar2: 'bullist numlist | outdent indent | link unlink image removeformat cleanup '+editarCodigo+' insertararchivos acheck '
			,menubar: false
			,content_css : "<bean:write name="MVS_css_tiny" filter="false" ignore="true"/>"	
			//,importcss_file_filter: "<bean:write name="MVS_css_tiny" filter="false" ignore="true"/>"
			,external_plugins: {
				"acheck": "plugins/acheck/editor_plugin.js"
			} 
			, theme_advanced_buttons1_add_before : "insertararchivos,separator"
			, theme_advanced_buttons3 : ""
			, theme_advanced_toolbar_location : "top"
			, theme_advanced_toolbar_align : "left"
			, theme_advanced_path_location : "bottom"
			, verify_html : false
			, setupcontent_callback : function (editor_id, body, doc) {
				tinymce.settings['idform'] = document.microForm.id.value;
			}
		  
			, theme_advanced_resizing : false
			, accessibility_warnings : true	
			, plugin_insertdate_dateFormat : "%d/%m/%Y"
			, plugin_insertdate_timeFormat : "%H:%M:%S"
			//, extended_valid_elements : "hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"
			, templates: [
			    {title: "Basica", 				description: "Pàgina Bàsica.", 								url:"tinymce/templates/basica.htm"},
			    {title: "Taula corporativa", 	description: "Taula amb capçalera i files alternant color de fons", 	url:"tinymce/templates/tabla.htm"},
			    {title: "Component agenda", 	description: "Component agenda en una taula de dues col.lumnes", url:"tinymce/templates/agenda.htm"}			    
			  ]
			,image_advtab: true
			,file_picker_types: 'file image media'
			,file_picker_callback: function(callback, value, meta) {			    
				Rcallback_tiny=callback;
				Rmeta_tiny=meta;				    
				window.open('/sacmicroback/recursos.do?tiny=true','recursos','scrollbars=yes,width=700,height=400');
			}
			, theme_advanced_resizing : false
			, nonbreaking_force_tab : true
			, apply_source_formatting : true		
			, convert_urls: false				
			, block_formats: 'Paràgraf=p;Adreça=address;Preformatat=pre;Títol=h1;Subtítol nivell 1=h2;Subtítol nivell 2=h3;Subtítol nivell 3=h4;Subtítol nivell 4=h5;Subtítol nivell 5=h6'
			<logic:notEqual name="MVS_usuario" property="permisosTiny" value="1">		
				,paste_as_text: true
				,invalid_elements: 'br'
			</logic:notEqual>
			, content_css: [
			    'css/estils.css'
			] 
			//, extended_valid_elements : "span"
		});
				
		
		var Rcallback_tiny;
	    var Rmeta_tiny;		
		
		function Rmeterurl_tiny(laurl, texto) {		
			var prefijo_alt= 'desc_';
					
			var desc_alt = prefijo_alt + laurl;
			if (typeof texto != 'undefined'){
				if(desc_alt != ""){
					desc_alt = texto;	
					// si es una miga de pan cogemos el ultimo valor
					var at = texto.split(">");
					var archivo = at[at.length-1];
					if(archivo.length>0){
						desc_alt = prefijo_alt + archivo;
					}				
				}			
			}
					
			if (Rmeta_tiny.filetype == 'file') {
				 Rcallback_tiny(laurl);
		    }
			
			if ( Rmeta_tiny.filetype == 'media' ) {
				 Rcallback_tiny(laurl);
		    }
			
		    if (Rmeta_tiny.filetype == 'image') {
		    	Rcallback_tiny(laurl, {alt: desc_alt});
		    }
		}	
	
		
		</script>
		<!-- /tinyMCE -->
		
		

<%session.setAttribute("action_path_key",null);%>		
	<html:form action="/cabeceraPieEdita.do" method="POST" enctype="multipart/form-data" styleId="accFormulario">
			
		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	
		
		<input type="hidden" name="accion" value=""/>
		<!-- botonera -->
		<div id="botonera">
			<logic:present name="MVS_microsite">
			<span class="grup">
		   	<button type="button" title='<bean:message key="micro.boto.tornargestio.titol"/>' onclick='window.parent.location.href="index.do?idsite=<bean:write name="MVS_microsite" property="id"/>";'>
		   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.titol"/>' />
		   </button> 
			</span>
			</logic:present>
			<span class="grup">
				<button type="button" title="<bean:message key="op.12" />" onclick="previsualizar();"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
			</span>
			<logic:present name="MVS_microsite">
			<logic:equal name="MVS_microsite" property="funcionalidadTraduccion" value="true">
			<span class="grup">				
			<button type="submit" title='<bean:message key="operacion.traducir"/>' onclick="submitForm('Traduir');">
			   		<img src="imgs/botons/clonar.gif" alt='<bean:message key="operacion.traducir"/>' /> &nbsp;<bean:message key="operacion.traducir" />
			</button>
			</span> 
			</logic:equal>
			</logic:present>
			<button type="submit" title='<bean:message key="operacion.guardar"/>' onclick="submitForm('Guardar');">
		   		<img src="imgs/botons/guardar.gif" alt='<bean:message key="operacion.guardar"/>' /> &nbsp;<bean:message key="operacion.guardar" />
		   	</button>
		</div>

		<input type="hidden" name="espera" value="si" id="espera" />
			
		<bean:define id="puedoeditar" value="0" />

	     <logic:present name="MVS_rol_sys_adm" >
		     <logic:equal  name="MVS_rol_sys_adm" value="yes">
	        	 <bean:define id="puedoeditar" value="1" />
	    	 </logic:equal>
	     </logic:present>
			
		<div style="font-weight:bold; color:#FF4400;">
		<html:errors/>
		</div>
	
		<ul id="submenu">
			<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
				<li<%=(j.intValue()==0?" class='selec'":"")%>><a href="#" onclick="mostrarForm(this);"><bean:message name="lang" /></a></li>
	        </logic:iterate>
		</ul>  	
			
			<html:hidden property="id" />
			<input type="hidden" name="grabacabpie" value="Grabar" />
		
			<div id="formulario">
		
		    <logic:iterate id="traducciones" name="microForm" property="traducciones" indexId="i" >
		    <html:hidden property="titulo" name="traducciones" indexed="true" />
			<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
			
				<h2><bean:message key="micro.cabe" /></h2>
				
				<p>
				<bean:message key="micro.cab" />
				<logic:equal name="i" value="0">
					<logic:equal name="puedoeditar" value="1">
						<html:select property="tipocabecera" styleId="capsalSiNo_<%=i%>" onchange="ficarCapsal(this.id);">
			    		    <html:option value="0"><bean:message key="micro.cabpie.op1" /></html:option>
				            <html:option value="1"><bean:message key="micro.cabpie.op2" /></html:option>
				            <html:option value="2"><bean:message key="micro.cabpie.op3" /></html:option>
				        </html:select>
			        </logic:equal>
			        <logic:equal name="puedoeditar" value="0">
			        	<html:select property="tipocabecera" styleId="capsalSiNo_<%=i%>">
							<logic:equal name="microForm" property="tipocabecera" value="0"><html:option value="0"><bean:message key="micro.cabpie.op1" /></html:option></logic:equal>
							<logic:equal name="microForm" property="tipocabecera" value="1"><html:option value="1"><bean:message key="micro.cabpie.op2" /></html:option></logic:equal>
							<logic:equal name="microForm" property="tipocabecera" value="2"><html:option value="2"><bean:message key="micro.cabpie.op3" /></html:option></logic:equal>
				        </html:select>
			        </logic:equal>
				</logic:equal>
				<logic:notEqual name="i" value="0">
					<strong id="capsalSiNo_<%=i%>">No</strong>
				</logic:notEqual>
				
				
			    	</p>
			    	
			 <div id="capaCapsal<%=i%>">   	
				<!-- las tablas están entre divs por un bug del FireFox -->
				<div id="siNoC_<%=i%>0">
					<table cellpadding="0" cellspacing="0" class="edicio">
					<tr>
						<td><strong><bean:message key="micro.sincabecera" /></strong>.</td>
					</tr>
					</table>
				</div>    	        
				<div id="siNoC_<%=i%>1" style="display:none;">
								<table cellpadding="0" cellspacing="0" class="edicio">
						<tr>
							<th>
								<logic:equal name="i" value="0">
								<bean:message key="micro.cabvisible"/>
								</logic:equal>
							</th>
							<th><bean:message key="micro.cabtexto"/></th>
							<th><bean:message key="micro.caburl"/></th>
						</tr>
						<tr>
							<td>
								<logic:equal name="i" value="0">
								<html:checkbox property="optMapa" value="S" disabled="false" />
								</logic:equal>
								<logic:notEqual name="i" value="0">
									<html:checkbox property="optMapa" value="S" disabled="true" />			
								</logic:notEqual>
							</td>
							<td><bean:message key="micro.mapa"/></td>
							<td>/mapa.do?mkey=<bean:write name="microForm" property="claveunica"/></td>
						</tr>
						<tr class="par">
							<td>
								<logic:equal name="i" value="0">
								<html:checkbox property="optFaq" value="S" disabled="false"/>
								</logic:equal>
								<logic:notEqual name="i" value="0">
								<html:checkbox property="optFaq" value="S" disabled="true" />			
								</logic:notEqual>
							</td>
							<td><bean:message key="micro.faq"/></td>
							<td>/faqs.do?mkey=<bean:write name="microForm" property="claveunica"/></td>
						</tr>
						<tr>
							<td>
								<logic:equal name="i" value="0">
								<html:checkbox property="optContacto" value="S" disabled="false"/>
								</logic:equal>
								<logic:notEqual name="i" value="0">
								<html:checkbox property="optContacto" value="S" disabled="true" />			
								</logic:notEqual>
							</td>
							<td><bean:message key="micro.contacto"/></td>
							<td>/contacto.do?mkey=<bean:write name="microForm" property="claveunica"/></td>
						</tr>
						<tr class="par">
							<td>
								<logic:equal name="i" value="0">
								<html:checkbox property="opt1" value="S" disabled="false" />
								</logic:equal>
								<logic:notEqual name="i" value="0">
								<html:checkbox property="opt1" value="S" disabled="true" />			
								</logic:notEqual>
							</td>
							<td><html:text property="txtop1" name="traducciones" size="35" maxlength="64" indexed="true" /></td>
							<td><html:text property="urlop1" name="traducciones" size="45" maxlength="256" indexed="true" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].urlop1');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button></td>
						</tr>
						<tr>
							<td>
								<logic:equal name="i" value="0">
								<html:checkbox property="opt2" value="S" disabled="false"/>
								</logic:equal>
								<logic:notEqual name="i" value="0">
								<html:checkbox property="opt2" value="S" disabled="true" />			
								</logic:notEqual>
							</td>
							<td><html:text property="txtop2" name="traducciones" size="35" maxlength="64" indexed="true" /></td>
							<td><html:text property="urlop2" name="traducciones" size="45" maxlength="256" indexed="true" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].urlop2');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button></td>
						</tr>
						</table>
				
				</div>
				<div id="siNoC_<%=i%>2">
					<table cellpadding="0" cellspacing="0" class="edicio">
					
						<tr id="tinymceEditor<%=i%>">
							<td>
								<html:textarea property="cabecerapersonal" name="traducciones" styleClass="editorTinyMCE" rows="10" cols="70" indexed="true" style="width:800px; height:300px;" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		<hr />    	        
				<h2><bean:message key="micro.pie" /></h2>
				
				<p>
				<bean:message key="micro.tipopie" />
				
				<logic:equal name="i" value="0">
					<logic:equal name="puedoeditar" value="1">
							<html:select property="tipopie" styleId="peuSiNo_<%=i%>" onchange="ficarCapsal(this.id);">
					    		<html:option value="0"><bean:message key="micro.cabpie.op1" /></html:option>
						    	<html:option value="1"><bean:message key="micro.cabpie.op2" /></html:option>
						     	<html:option value="2"><bean:message key="micro.cabpie.op3" /></html:option>
						     </html:select>
				    </logic:equal>
		    		<logic:equal name="puedoeditar" value="0">
		    		
		    				<html:select property="tipopie" styleId="peuSiNo_<%=i%>" >
					    		<logic:equal name="microForm" property="tipopie" value="0"><html:option value="0"><bean:message key="micro.cabpie.op1" /></html:option></logic:equal>
						    	<logic:equal name="microForm" property="tipopie" value="1"><html:option value="1"><bean:message key="micro.cabpie.op2" /></html:option></logic:equal>
						     	<logic:equal name="microForm" property="tipopie" value="2"><html:option value="2"><bean:message key="micro.cabpie.op3" /></html:option></logic:equal>
						     </html:select>
		    		
		    		</logic:equal>
				</logic:equal>
				<logic:notEqual name="i" value="0">
					<strong id="peuSiNo_<%=i%>">No</strong>
				</logic:notEqual>
				
		    	</p>
		
			<div id="capaPeu<%=i%>">
				<div id="siNoP_<%=i%>0">
					<table cellpadding="0" cellspacing="0" class="edicio">
					<tr>
						<td><strong><bean:message key="micro.sinpie" /></strong>.</td>
					</tr>
					</table>
				</div>
				<div id="siNoP_<%=i%>1" style="display:none;">
					<table cellpadding="0" cellspacing="0" class="edicio">
					<tr>
						<th>
							<logic:equal name="i" value="0">
							<bean:message key="micro.cabvisible"/>
							</logic:equal>
						</th>
						<th><bean:message key="micro.cabtexto"/></th>
						<th><bean:message key="micro.caburl"/></th>
					</tr>
					<tr>
						<td>
							<logic:equal name="i" value="0">
							<html:checkbox property="opt3" value="S" disabled="false"/>
							</logic:equal>
							<logic:notEqual name="i" value="0">
							<html:checkbox property="opt3" value="S" disabled="true" />			
							</logic:notEqual>
						</td>
						<td><html:text property="txtop3" name="traducciones" size="35" maxlength="64" indexed="true" /></td>
						<td><html:text property="urlop3" name="traducciones" size="45" maxlength="256" indexed="true" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].urlop3');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button></td>
					</tr>
					<tr class="par">
						<td>
							<logic:equal name="i" value="0">
							<html:checkbox property="opt4" value="S" disabled="false"/>
							</logic:equal>
							<logic:notEqual name="i" value="0">
							<html:checkbox property="opt4" value="S" disabled="true" />			
							</logic:notEqual>
						</td>
						<td><html:text property="txtop4" name="traducciones" size="35" maxlength="64" indexed="true" /></td>
						<td><html:text property="urlop4" name="traducciones" size="45" maxlength="256" indexed="true" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].urlop4');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button></td>
		
					</tr>
					<tr>
						<td>
							<logic:equal name="i" value="0">
							<html:checkbox property="opt5" value="S" disabled="false"/>
							</logic:equal>
							<logic:notEqual name="i" value="0">
							<html:checkbox property="opt5" value="S" disabled="true" />			
							</logic:notEqual>
						</td>
						<td><html:text property="txtop5" name="traducciones" size="35" maxlength="64" indexed="true" /></td>
						<td><html:text property="urlop5" name="traducciones" size="45" maxlength="256" indexed="true" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].urlop5');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button></td>
					</tr>
					<tr class="par">
						<td>
							<logic:equal name="i" value="0">
							<html:checkbox property="opt6" value="S" disabled="false"/>
							</logic:equal>
							<logic:notEqual name="i" value="0">
							<html:checkbox property="opt6" value="S" disabled="true" />			
							</logic:notEqual>
						</td>
						<td><html:text property="txtop6" name="traducciones" size="35" maxlength="64" indexed="true" /></td>
						<td><html:text property="urlop6" name="traducciones" size="45" maxlength="256" indexed="true" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].urlop6');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button></td>
					</tr>
					<tr>
						<td>
							<logic:equal name="i" value="0">
							<html:checkbox property="opt7" value="S" disabled="false"/>
							</logic:equal>
							<logic:notEqual name="i" value="0">
							<html:checkbox property="opt7" value="S" disabled="true" />			
							</logic:notEqual>
						</td>
						<td><html:text property="txtop7" name="traducciones" size="35" maxlength="64" indexed="true" /></td>
						<td><html:text property="urlop7" name="traducciones" size="45" maxlength="256" indexed="true" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].urlop7');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button></td>
					</tr>
					</table>
					
					<html:hidden property="titulocampanya" name="traducciones" indexed="true" />
					<html:hidden property="subtitulocampanya" name="traducciones" indexed="true" />
		
				</div>
				<div id="siNoP_<%=i%>2" style="display:none;">
					<table cellpadding="0" cellspacing="0" class="edicio">
					<tr>
						<td>
							<html:textarea property="piepersonal" name="traducciones" styleClass="editorTinyMCE" rows="10" cols="70" indexed="true" style="width:800px; height:300px;" />
						</td>
					</tr>
					</table>
				</div>
			</div>
			</div>
		    </logic:iterate>
		
		</div>
		
	<br/>
	<br/>
		
	</html:form>



</body>
<%Microsite micro = (Microsite)session.getAttribute("MVS_microsite"); %>
<%micro.setMensajeError(null); %>
<%micro.setMensajeInfo(null); %>
<% session.setAttribute("MVS_microsite",micro);%>
</html>


<script language="javascript" type="text/javascript">

	<logic:present name="MVS_microsite">
	function previsualizar() {
		abrirWindow('<bean:message key="url.aplicacion" />index.do?lang=ca&idsite=<bean:write name="MVS_microsite" property="id"/>&stat=no');
	}

	function submitForm(){
		var accForm = document.getElementById('accFormulario');
		accForm.submit();
	}
	</logic:present>


	function submitForm(nom_accio){
		var accForm = document.getElementById('accFormulario');
		accForm.accion.value= nom_accio;
		 if (nom_accio== "Traduir") {
			 accForm.accion.value="<bean:message key='operacion.traducir'/>";
		 }else if (nom_accio== "Guardar"){
			 accForm.accion.value="<bean:message key='operacion.guardar'/>";
		}
		accForm.submit();
	}
	
    var Rcajatemp;
    function Rpopupurl(obj) {
    	Rcajatemp=document.microForm[obj];
		window.open('recursos.do','recursos','scrollbars=yes,width=700,height=400');
    }
	
	function Rmeterurl(laurl) {
		Rcajatemp.value=laurl;
	}
	
	ficarCapsal(document.microForm.tipocabecera.id);
	ficarCapsal(document.microForm.tipopie.id);

// -->
</script>


<jsp:include page="/moduls/pieControl.jsp"/>