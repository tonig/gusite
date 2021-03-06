package es.caib.gusite.microback.action.buscaordena;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.actionform.busca.BuscaOrdenaTemaActionForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.TemaDelegate;


/**
 * Action que prepara el listado de Temas <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/temas" <BR> 
 *  name="BuscaOrdenaTemaActionForm" <BR> 
 *  forward name="listarTemas" path="/listaTemas.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaTemasAction extends Action {

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

    protected static Log log = LogFactory.getLog(es.caib.gusite.microback.action.buscaordena.buscaordenaTiposAction.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	
    	TemaDelegate bdTema = DelegateUtil.getTemafaqDelegate();
    	bdTema.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

        //Podemos recibir datos de filtro u ordenación del listado
        BuscaOrdenaTemaActionForm f = (BuscaOrdenaTemaActionForm) form;
        
        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdTema.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdTema.setOrderby(f.getOrdenacion());

        // Indicamos la página a visualizar
        if (request.getParameter("pagina")!=null)
        	bdTema.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdTema.setPagina(1);
            
        List<?> lista=bdTema.listarTemas();
        request.setAttribute("parametros_pagina",bdTema.getParametros());
        
        if (lista.size()!=0) // Si hay algún registro
            request.setAttribute("listado",lista);
        else  // Si no hay registros limpiamos el filtro
            f.setFiltro("");

        return mapping.findForward("listarTemas");
        
    }

}
