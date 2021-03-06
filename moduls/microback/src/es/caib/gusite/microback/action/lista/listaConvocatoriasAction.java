package es.caib.gusite.microback.action.lista;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.TraDynaActionForm;
import es.caib.gusite.microback.actionform.listaActionForm;
import es.caib.gusite.microback.actionform.formulario.contenidoForm;
import es.caib.gusite.microback.actionform.formulario.convocatoriaForm;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Convocatoria;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.ConvocatoriaDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.FrqssiDelegate;
import es.caib.gusite.micropersistence.delegate.MenuDelegate;


/**
 * Action que valida y trata el listado de contenidos de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/contenidosAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleConte" path="/detalleContenido.jsp"<BR> 
 *  forward name="info" path="/infoContenido.jsp" 
 *  
 *  @author Indra
 */
public class listaConvocatoriasAction extends BaseAction {

    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @return 
     */

	protected static Log log = LogFactory.getLog(listaConvocatoriasAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

    	listaActionForm f = (listaActionForm) form;
        
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("convocatoriaForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	TraDynaActionForm fdet=(TraDynaActionForm) request.getSession().getAttribute("convocatoriaForm");
        	request.setAttribute("convocatoriaForm", fdet);
            return mapping.findForward("detalleConvocatoria");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("crear")) {
        	request.getSession().removeAttribute("convocatoriaForm");
        	return mapping.findForward("detalleConvocatoria");
        }
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("borrar")) {
            Long id =null;
        	ConvocatoriaDelegate bdConvocatoria = DelegateUtil.getConvocatoriaDelegate();
            
            String lis="";
            for (int i=0;i<f.getSeleccionados().length;i++) {
                id = new Long(f.getSeleccionados()[i]);
                bdConvocatoria.borrarConvocatoria(id);
                lis+=id+", ";
            }
            lis=lis.substring(0,lis.length()-2);
            
            addMessage(request, "mensa.listaconvocatorias");
            addMessage(request, "mensa.listaconvborradas", new String(lis));
            
            return mapping.findForward("info");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }    	
}