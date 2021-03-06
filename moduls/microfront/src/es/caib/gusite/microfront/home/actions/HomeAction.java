package es.caib.gusite.microfront.home.actions;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ForwardingActionForward;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.home.util.Bdhome;
import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.exception.ExceptionFrontMicro;
import es.caib.gusite.microfront.exception.ExceptionFrontPagina;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionMicrosite;

/**
 * Action home <P>
 *  Definición Struts:<BR>
 *  action path="/home" <BR> 
 *  unknown="true" <BR>
 *  forward name="plantilla1v4" path=/v4/home/home1.jsp <BR>
 *  forward name="plantilla1v1" path=/v1/home/home1.jsp
 * @author Indra
 *
 */
public class HomeAction extends BaseAction {
	private static final String MICROSITE_VERSIONOVA = "4";
	protected String idioma="";

	/**
	* Metodo público execute para la homeAction
	* @param mapping
	* @param form
	* @param request
	* @param response
	* @return ActionForward
	* @exception IOException, ServletException, Exception
	*/	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception{
	  
	  	String forwardlocal="";
	  	Microsite microsite = crearMicrosite();
	  	
	  	try {
     		
	  		//averiguar información de la home
			Bdhome bdhome = crearMicrositeHome(request);
		    microsite = recuperarMicrositeDeSesion(request);
	  	    
		    if (bdhome.isError()) {
		    	return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA, response));
		    }

		    forwardlocal = crearHomeParaVersionesAntiguas(request, forwardlocal, bdhome);

		    //url del site
		    if (bdhome.getTipohome().equals(MICROSITE_VERSIONOVA)) {
		    	ActionForward elforward = new ForwardingActionForward();
		    	elforward.setPath("/"+bdhome.getUrlhome()+ "&" + Microfront.PCAMPA + "=yes");
		    	return elforward;
		    }

		    /*** SIEMPRE FIJAS para version 4**/
		    request.setAttribute("MVS2_mollapan", mollapan(request, bdhome.getIdioma()));
		    menucaib(request, bdhome.getIdioma());
		    /*** FIN SIEMPRE **/

		    //Pegote de la estructura a seguir. Campo restringido, N=azules, S=azules, 2=blancos.
		    //OJO: es la estructura y no la hoja de estilos.
		    if ( (microsite.getRestringido().equals("N")) || (microsite.getRestringido().equals("S")) )  
		    	return mapping.findForward(forwardlocal+"v1");
		    else
		    	return mapping.findForward(forwardlocal+"v4");	

	
        } catch (ExceptionFrontPagina efp) {  	
        	log.error(efp.getMessage());
        	return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA, response));

        } catch (ExceptionFrontMicro efm) {
	    	log.error(efm.getMessage());
	    	return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_MICRO, response));
	    	
	    }  catch (Exception e) {
        	log.error(e.getMessage());
        	return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA, response));
        }      

  	}

	private String crearHomeParaVersionesAntiguas(HttpServletRequest request,
			String forwardlocal, Bdhome bdhome) {
		if ( (bdhome.getTipohome().equals("1")) 
				|| (bdhome.getTipohome().equals("2")) 
				|| (bdhome.getTipohome().equals("3"))
				|| (bdhome.getTipohome().equals("5")) ) {
			request.setAttribute("MVS_home_campanya",bdhome.getTagHtmlCampanya());
			request.setAttribute("MVS_home_noticias",bdhome.getTagHtmlNoticias());
			request.setAttribute("MVS_home_agenda_calendario",bdhome.getTagHtmlAgendaCalendario());
			request.setAttribute("MVS_home_agenda_listado",bdhome.getTagHtmlAgendaListado());
			forwardlocal="plantilla1";
		}
		return forwardlocal;
	}

	private Microsite recuperarMicrositeDeSesion(HttpServletRequest request) {
		return (Microsite)request.getSession().getAttribute("MVS_microsite");
	}

	protected Bdhome crearMicrositeHome(HttpServletRequest request)
			throws Exception {
		return new Bdhome(request);
	}

	protected Microsite crearMicrosite() {
		return new Microsite();
	}
  
	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * @param request  
	 * @param idi   idioma
	 * @return string recorrido en el microsite
	 */	 
  	private String mollapan(HttpServletRequest request, String idi) {
		StringBuffer stbuf = new StringBuffer("");
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idi.toUpperCase(), idi.toUpperCase()));
		String mvs_ua = "" + request.getSession().getAttribute("MVS_ua");
		
		stbuf.append("<li><a href=\"http://www.caib.es\">" + rb.getString("general.inicio") + "</a></li>");
		if (!(mvs_ua.equals("null")) && (mvs_ua.length()>0))
			stbuf.append("<li>&gt; " + mvs_ua + "</li>");
		//añado el titulo del microsite
		Microsite microsite = recuperarMicrositeDeSesion(request);
		String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(idi)!=null)?((TraduccionMicrosite)microsite.getTraduccion(idi)).getTitulo():"&nbsp;";
		stbuf.append("<li>&gt; <a href=\"home.do?mkey="+microsite.getClaveunica()+"&lang=" + idi + "\">" + titulo_mic+ " </a></li>");
		return stbuf.toString();
	}	
	
}