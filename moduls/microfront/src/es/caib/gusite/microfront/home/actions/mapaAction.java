package es.caib.gusite.microfront.home.actions;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.home.util.Bdmapa;
import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.exception.ExceptionFrontMicro;
import es.caib.gusite.microfront.exception.ExceptionFrontPagina;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionMicrosite;

/**
 *  Action para consultar un mapa <P>
 *  Definición Struts:<BR>
 *  action path="/mapa" <BR> 
 *  unknown="false" <BR>
 *  forward name="mapav4" path="/v4/home/mapa.jsp" <BR>
 *  forward name="mapav1" path="/v1/home/mapa.jsp
 *  
 *  @author - Indra
 */
public class mapaAction extends BaseAction {

	  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{

		  String forwardlocal = "mapa";
		  
		  try {
		     		
			  Bdmapa bdmapa = new Bdmapa(request);

			  /*** SIEMPRE FIJAS para version 4**/
			  request.setAttribute("MVS2_mollapan", mollapan(request, bdmapa.getIdioma()));
			  menucaib(request, bdmapa.getIdioma());
			  /*** FIN SIEMPRE **/

			  //Pegote de la estructura a seguir. Campo restringido, N=azules, S=azules, 2=blancos.
			  //OJO: es la estructura y no la hoja de estilos.
			  Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
			  if ( (microsite.getRestringido().equals("N")) || (microsite.getRestringido().equals("S")) )  
				  return mapping.findForward(forwardlocal+"v1");
			  else
				  return mapping.findForward(forwardlocal+"v4");

		  } catch (ExceptionFrontPagina efp) {
			  log.error(efp.getMessage());
			  return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_PAGINA, response));

	      } catch (ExceptionFrontMicro efm) {
	    	  log.error(efm.getMessage());
	    	  return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_MICRO, response));
		    	
	      } catch (Exception e) {
	    	  log.error(e.getMessage());
	    	  return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_PAGINA, response));
	      } 	    
		    
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
		Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(idi)!=null)?((TraduccionMicrosite)microsite.getTraduccion(idi)).getTitulo():"&nbsp;";
		stbuf.append("<li>&gt; <a href=\"home.do?mkey="+microsite.getClaveunica()+"&lang=" + idi + "\">" + titulo_mic+ " </a></li>");
		stbuf.append("<li>&gt; " + rb.getString("mapa.mapa") + "</li>");
		
		return stbuf.toString();
	}	
	
}
