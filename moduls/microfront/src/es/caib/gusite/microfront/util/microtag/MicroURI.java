package es.caib.gusite.microfront.util.microtag;

import es.caib.gusite.microfront.Microfront;

/**
 * Clase utilizada para devolver uri que apuntan a elementos como documentos, noticias, eventos de agenda, etc.
 * @author pmelia
 *
 */
public class MicroURI {

	/**
	 * Método que devuelve la uri de acceso a un documento.
	 * @param constante
	 * @param iditem
	 * @param iddoc
	 * @return String una uri
	 */
	@Deprecated
	public static String uriImg(String constante, long iditem, long iddoc) {
		String retorno="";
		retorno="archivopub.do?ctrl=" + constante + iditem + Microfront.separatordocs + iddoc + "&id=" + iddoc;
		return retorno;
	}
	
	/**
	 * Método que devuelve la nueva uri de acceso a un documento.
	 * 
	 * @param nombreDoc
	 * @param nombreSite
	 * @return String la nueva uri con el nombre del documento y el nombre del site
	 */
	public static String uriImg(String nombreDoc, String nombreSite) {
		String retorno = "";
		retorno = "archivopub.do?nombre=" + nombreDoc + "&uri=" + nombreSite;
		return retorno;
	}

	/**
	 * Método que devuelve la uri de acceso a una noticia
	 * @param idsite
	 * @param iditem
	 * @param idioma
	 * @return String una uri
	 */
	public static String uriNoticia(Long idsite, Long iditem, String idioma) {
		String retorno="";
		retorno="noticia.do?" + Microfront.PIDSITE + "=" + idsite + "&" + Microfront.PCONT + "=" + iditem + "&" + Microfront.PLANG + "=" + idioma;
		return retorno;
	}
	
	/**
	 * Método que devuelve la uri de acceso a un evento de agenda
	 * @param idsite
	 * @param iditem
	 * @param idioma
	 * @return String una uri
	 */
	public static String uriAgenda(Long idsite, String iditem, String idioma) {
		String retorno="";
		retorno="agenda.do?" + Microfront.PIDSITE + "=" + idsite + "&" + Microfront.PCONT + "=" + iditem + "&" + Microfront.PLANG + "=" + idioma;
		return retorno;
	}
	
	/**
	 * Método que devuelve la uri de acceso a una pagina de contenido
	 * @param idsite
	 * @param iditem
	 * @param idioma
	 * @return String una uri
	 */
	public static String uriContenido(Long idsite, String iditem, String idioma) {
		String retorno="";
		retorno="contenido.do?" + Microfront.PIDSITE + "=" + idsite + "&" + Microfront.PCONT + "=" + iditem + "&" + Microfront.PLANG + "=" + idioma;
		return retorno;
	}	
	
	/**
	 * Método que devuelve la uri de acceso a un formulario de contacto
	 * @param idsite
	 * @param iditem
	 * @param idioma
	 * @return String una uri
	 */
	public static String uriContacto(Long idsite, Long iditem, String idioma) {
		String retorno="";
		retorno="contacto.do?" + Microfront.PIDSITE + "=" + idsite + "&" + Microfront.PCONT + "=" + iditem + "&" + Microfront.PLANG + "=" + idioma;
		return retorno;
	}
	
	/**
	 * Método que devuelve la uri de acceso a un recurso del portal.
	 * En el parametro recurso se pasa la página a visualizar
	 * @param recurso
	 * @param idsite
	 * @param idioma
	 * @return String una uri
	 */
	public static String uriGeneral(String recurso, Long idsite, String idioma) {
		String retorno="";
		if (recurso.indexOf("?")!=-1) 
			retorno=recurso + "&" + Microfront.PIDSITE + "=" + idsite + "&" + Microfront.PLANG + "=" + idioma;
		else
			retorno=recurso + "?" + Microfront.PIDSITE + "=" + idsite + "&" + Microfront.PLANG + "=" + idioma;
		return retorno;
	}	
	
	/**
	 * Método que devuelve la uri de acceso a un recurso del portal.
	 * En el parametro recurso se pasa la página a visualizar
	 * @param recurso
	 * @param idsite
	 * @param iditem
	 * @param idioma
	 * @return String una uri
	 */
	public static String uriGeneral(String recurso, Long idsite, String iditem, String idioma) {
		String retorno="";
		if (recurso.indexOf("?")!=-1) 
			retorno=recurso + "&" + Microfront.PIDSITE + "=" + idsite + "&" + Microfront.PCONT + "=" + iditem + "&" + Microfront.PLANG + "=" + idioma;
		else
			retorno=recurso + "?" + Microfront.PIDSITE + "=" + idsite + "&" + Microfront.PCONT + "=" + iditem + "&" + Microfront.PLANG + "=" + idioma;
		return retorno;
	}	
	
}
