package es.caib.gusite.microfront.job;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;
import es.caib.gusite.plugins.PluginException;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadData;
import es.caib.gusite.plugins.organigrama.UnidadListData;

/**
 * Clase MenuCabecera. Utilizado para trabajar con las cabeceras del microsite
 * @author Indra
 *
 */
public class MenuCabecera {

	/* Se guardará en cada hash una entrada por idioma. Y a su vez, el `value` del hash será un ArrayList con el listado correspondiente. */
	private static Hashtable<String, Collection<?>> uos = new Hashtable<String, Collection<?>>();
	
	private static Log log = LogFactory.getLog(MenuCabecera.class);
	
	/**
	 * Parte que se ejecuta la primera vez que se invoca a esta clase
	 */
	static {
		log.info("Refresco de menu estático de PortalCAIB en Microsites realizado.");
		refrescarMenu();
	}
	
	/**
	 * Método público para refrescar el Menu de los microsites
	 */
	public static void refrescarMenu() {

		OrganigramaProvider organigramaProvider = null;
    	try {
    		organigramaProvider = PluginFactory.getInstance().getOrganigramaProvider();
		} catch (Exception e) {
			log.debug("PluginFactory todavía no está disponible");
			return;
    	} 

	    	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
			List<Idioma> idiomas;
			try {
				idiomas = idiomaDelegate.listarIdiomas();
			} catch (DelegateException e1) {
	    		log.error("No se han podido obtener los idiomas disponibles en GUSITE, no se puede calcular el menú de la cabecera de portalcaib", e1);
	    		return;
			}
			
			Collection<UnidadListData> uasIdiomaDefecto;
			try {
				uasIdiomaDefecto = organigramaProvider.getUnidadesPrincipales(Microfront.DEFAULT_IDIOMA);
				uos.put(Microfront.DEFAULT_IDIOMA, uasIdiomaDefecto);
			} catch (PluginException e1) {
	    		log.error("NO SE HA PODIDO CALCULAR EL MENU DE LA CABECERA DEL PORTALCAIB para el idioma por defecto: " + Microfront.DEFAULT_IDIOMA, e1);
				e1.printStackTrace();
				return;
			}
			
			for (Idioma lang : idiomas) {
				if (lang.getLang().equals(Microfront.DEFAULT_IDIOMA)) {
					continue; //Para el idioma por defecto ya lo hemos calculado
				}
		    	try {
					Collection<UnidadListData> nuevasUAs = organigramaProvider.getUnidadesPrincipales(lang.getLang());
					uos.put(lang.getLang(), nuevasUAs);
		    	} catch (Exception e) {
		    		log.error("NO SE HA PODIDO CALCULAR EL MENU DE LA CABECERA DEL PORTALCAIB para el idioma " + lang.getLang() + ". Usando idioma por defecto", e);
					uos.put(lang.getLang(), uasIdiomaDefecto);
		    	}
			}
			
	}

	/**
	 * Devuelve un listado de las consellerias
	 * @param lang
	 * @return ArrayList
	 */
	public static Collection<?> getUos(String lang) {
		return uos.get(lang);
	}
	
}
