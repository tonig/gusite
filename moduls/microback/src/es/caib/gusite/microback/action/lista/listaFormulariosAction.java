package es.caib.gusite.microback.action.lista;


import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.listaActionForm;
import es.caib.gusite.microback.actionform.formulario.formularioconForm;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Lineadatocontacto;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.ContactoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

/**
 * Action que valida y trata el listado de formularios de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/formulariosAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleFormu" path="/detalleFormulario.jsp"<BR> 
 *  forward name="listarFormu" path="/formularios.do"
 *  
 *  @author Indra
 */
public class listaFormulariosAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(listaFormulariosAction.class);
	
    @Override
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm listaActionForm = (listaActionForm) form;
        request.setAttribute("idmicrosite", ((Microsite) request.getSession().getAttribute("MVS_microsite")).getId());
        //********************************************************
        //*********** ERROR DE VALIDACION FORMUALRIO *************
        //********************************************************
        if (request.getSession().getAttribute("formularioconForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	formularioconForm fdet=(formularioconForm) request.getSession().getAttribute("formularioconForm");        	       
        	
        	//rellenamos las lineas del formulario (no venian informadas)
        	Long id = (Long) fdet.get("id");
        	if(id!=null) {
	        	ContactoDelegate bdFormu = DelegateUtil.getContactoDelegate();        
	        	Contacto formulario = bdFormu.obtenerContacto(id);
	        	Iterator<?> it = formulario.getLineasdatocontacto().iterator();	
	            ArrayList<Lineadatocontacto> lineas= new ArrayList<Lineadatocontacto>();
	            while (it.hasNext()) {
	            	lineas.add((Lineadatocontacto)it.next());
	            }         	
	            fdet.set("lineasdatocontacto",lineas);
        	}
	            request.setAttribute("validacion", "si");
	            request.setAttribute("formularioconForm", fdet);
	            return mapping.findForward("detalleFormu");
        	
            
        }
        
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( listaActionForm.getAccion().equals("crear")) {
        	request.getSession().removeAttribute("formularioconForm");
        	return mapping.findForward("detalleFormu");
        }
        
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( listaActionForm.getAccion().equals("borrar")) {
            Long id =null;
        	ContactoDelegate bdFormu = DelegateUtil.getContactoDelegate();
            
            String lis="";
            for (int i=0;i<listaActionForm.getSeleccionados().length;i++) {
                id = new Long(listaActionForm.getSeleccionados()[i]);
                bdFormu.borrarContacto(id);
                lis+=id+", ";
            }
            lis=lis.substring(0,lis.length()-2);
                       
            request.getSession().setAttribute("mensajeBorrarFormulario", new String(lis));
            
            return mapping.findForward("listarFormu");           
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");

    }

}