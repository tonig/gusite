// Document JavaScript

//Funciones generales estáticas. Antes estaban en scriptmenu
var nodo='0'; //Nodo actual del menú

function onClickAnyo(anyo) {
	document.getElementById("filtro").value = ''; 
	establecerAnyoEnBuscador(anyo);
}

function establecerAnyoEnBuscador(anyo) {
	document.getElementById("tanyo").value = anyo; 
}	


/* mostrar calendari de l'agenda */

function mostrarCalendari() {
	num = $('agendaSelect').options[$('agendaSelect').selectedIndex].value;
	divs = $('agendaCalendaris').getElementsByTagName('div');
	for(i=0;i<divs.length;i++) {
		divs[i].style.display = (i==num) ? 'block' : 'none';
	}
}
function cambiMes(click){
	if(click=="mesPosterior"){
		if(window.$('agendaSelect').options[$('agendaSelect').selectedIndex+1]){
			$('agendaSelect').value = $('agendaSelect').options[$('agendaSelect').selectedIndex+1].value;
		}
	}else{
		if(window.$('agendaSelect').options[$('agendaSelect').selectedIndex-1]){
			$('agendaSelect').value = $('agendaSelect').options[$('agendaSelect').selectedIndex-1].value;
		}
	}
	mostrarCalendari();
};
/* menu lateral */

var menuJS = {
	iniciar: function() {
		var menu = $('marcLateralV2');
		if(menu == null) {
			menu = $('marcLateralAmbIconesV2');
		}
		if(menu != null && menu.length != 0) {
			As = menu.getElementsByTagName('a');
			var nodoX;
			//return;
			for(i=0;i<As.length;i++) {		
				if(jQuery(As[i]).hasClass('pareADon')) {
					menuJS.obrir(As[i]);
					As[i].href = 'javascript:void(0);';
					As[i].className = 'pareAD';
					jQuery(As[i]).click(menuJS.obrirA);
				}
				if(As[i].parentNode.id != false) nodoX = As[i].parentNode.id;
			}
			if(nodo != 0 && nodo == nodoX) {
				// obri carpeta de segon nivell
				nodeUL = $(nodo).parentNode;
				nodeUL.style.display = 'block';
				nodeA = nodeUL.parentNode.firstChild;
				if(nodeUL.parentNode.nodeName != 'DIV') nodeA.className = 'pareADon';
				// obri carpeta de primer nivell
				nodeUL = nodeA.parentNode.parentNode;
				nodeUL.style.display = 'block';
				nodeA = nodeUL.parentNode.firstChild;
				if(nodeUL.parentNode.nodeName != 'DIV') nodeA.className = 'pareADon';
			}
				//Nuevo: Coge el padre de los seleccionados, los abre y le quita la posibilidad de cerrarse
				var primerLI = jQuery(".seleccionado").parent().parent();
				var primerA = primerLI.find("a")[0];
				if(primerA != undefined){
					primerA = jQuery(primerA);
					primerA.off("click");
					primerA.attr("href","#");
					primerA.removeClass("pareADon pareAD");
					primerA.addClass("carpetaSelec");
					var ultimoLI = primerLI.parent().parent();
					if(ultimoLI[0].tagName == "LI"){
						primerA.parent().find('ul').show();
						var ultimoA = ultimoLI.find("a")[0];
						ultimoA = jQuery(ultimoA);
						ultimoA.off("click");
						ultimoA.attr("href","#");
						ultimoA.removeClass("pareADon pareAD");
						ultimoA.addClass("carpetaSelec");
						//ultimoA.parent().find('ul').show();
					}else{
						primerA.parent().find('.primerosHijos').show();
					}
					
				}
			}
		},
		obrir: function(obj) {
			nextList = obj.parentNode.getElementsByTagName('ul')[0];
			if(obj.className == 'pareAD') {
				obj.className = 'pareADon';
				nextList.style.display = 'block';	
			} else {
				obj.className = 'pareAD';
				nextList.style.display = 'none';	
			}
		},
		obrirA: function(e) {
			obj = e.target || e.srcElement;
			menuJS.obrir(obj);
		}
	}

	/* tamany lletra */

tamanyInicial = 100; // %
var tamanyLletra = {
	iniciar: function() {
		var eines = jQuery("#eines");
		if(eines != null && eines.length != 0) {
			
			if(getCookie("caib") != null) {
				tamanyInicial = getCookie("caib");
				document.body.style.fontSize = tamanyInicial + '%';
			};		
			DIVs = eines.find("div");
			if(DIVs[2] != null) {
				codi = DIVs[2].innerHTML;
				codiNou = codi + "<a id=\"lletraAumentar\" href=\"javascript:void(0);\" title=\"Aumentar tamany lletra\">A+</a> / <a id=\"lletraDisminuir\" href=\"javascript:void(0);\" title=\"Disminuir tamany lletra\">A-</a>";
				DIVs[2].innerHTML = codiNou;
			};
			addEvent($('lletraAumentar'),'click',tamanyLletra.aumentar,false);
			addEvent($('lletraDisminuir'),'click',tamanyLletra.disminuir,false);
		}
	},
	aumentar: function() {
		if($('eines') != null) {		
			tamanyInicial = parseInt(tamanyInicial) + 8;
			if(tamanyInicial > 148) tamanyInicial = 148;
			document.body.style.fontSize = tamanyInicial + '%';
			setCookie("caib",tamanyInicial,365);
		}
	},
	disminuir: function() {
		if($('eines') != null) {	
			tamanyInicial = parseInt(tamanyInicial) - 8;
			if(tamanyInicial < 52) tamanyInicial = 52;
			document.body.style.fontSize = tamanyInicial + '%';
			setCookie("caib",tamanyInicial,365);
		}
	}
};

/* */

function addEvent(obj,tipo,fn,captura) {
	if(obj.addEventListener) { obj.addEventListener(tipo,fn,captura); return true;
	} else if (obj.attachEvent) { var r = obj.attachEvent('on'+tipo,fn); return r;
} else { obj['on'+tipo] = fn; }
}

function $(id) {
	elemento = document.getElementById(id); return elemento;
}

function setCookie(nombre, valor, expiraDias) {
	var ExpiraFecha = new Date(); ExpiraFecha.setTime(ExpiraFecha.getTime() + (expiraDias * 24 * 3600 * 1000));
	document.cookie = nombre + "=" + escape(valor) + ((expiraDias == null) ? "" : "; expires=" + ExpiraFecha.toGMTString()) + "; path=/";
}

function getCookie(nombre) {
	if(document.cookie.length > 0) {
		begin = document.cookie.indexOf(nombre+"=");
		if(begin != -1) {
			begin += nombre.length+1;
			end = document.cookie.indexOf(";", begin);
			if(end == -1) end = document.cookie.length;
			return unescape(document.cookie.substring(begin, end));
		} 
	}
	return null;
}

function onContent(f){//(C)webreflection.blogspot.com
	var a,b=navigator.userAgent,d=document,w=window,c="__onContent__",e="addEventListener",o="opera",r="readyState",
	s="<scr".concat("ipt defer src='//:' on",r,"change='if(this.",r,"==\"complete\"){this.parentNode.removeChild(this);",c,"()}'></scr","ipt>");
	w[c]=(function(o){return function(){w[c]=function(){};for(a=arguments.callee;!a.done;a.done=1)f(o?o():o)}})(w[c]);
	if(d[e])d[e]("DOMContentLoaded",w[c],false);
	if(/WebKit|Khtml/i.test(b)||(w[o]&&parseInt(w[o].version())<9))
		(function(){/loaded|complete/.test(d[r])?w[c]():setTimeout(arguments.callee,1)})();
	else if(/MSIE/i.test(b))d.write(s);
};

/* */

onContent(menuJS.iniciar);
onContent(tamanyLletra.iniciar);
