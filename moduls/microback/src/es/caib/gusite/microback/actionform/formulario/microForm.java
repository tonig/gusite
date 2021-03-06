package es.caib.gusite.microback.actionform.formulario;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import es.caib.gusite.microback.actionform.TraDynaActionForm;
import es.caib.gusite.microback.utils.General;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 *	Formulario para los microsites
 *
 *@author Indra
 *
 */
public class microForm extends TraDynaActionForm {

	private static final long serialVersionUID = 4850120417775102506L;
	protected static Log log = LogFactory.getLog(microForm.class);
	
    /* (non-Javadoc)
     * @see org.apache.struts.action.DynaActionForm#initialize(org.apache.struts.action.ActionMapping)
     */
    public void initialize(ActionMapping actionMapping) {
        super.initialize(actionMapping);        
        inicio();
    }

    /* (non-Javadoc)
     * @see es.caib.gusite.microback.actionform.TraDynaActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        inicio();
    }

    private void inicio() {
        
    	// Obtenemos UAs para el mantenimiento del microsite.
        set("listaUAs", General.obtenerListaUAs());
    	
    }
   
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {

    	ActionErrors errors = new ActionErrors();
    	String accion = ""+request.getParameter("accion");
    	
    	if((accion.equals(propertiesMessages.getString("operacion.guardar")))
    	    	& (request.getRequestURI().toUpperCase().indexOf("MICROEDITA.DO") != -1)) {

    		if (get("idUA")==null) 
                errors.add("idUA", new ActionError("error.micro.uo"));
        
    		if (get("imagenPrincipal")!=null)
    			if (((FormFile)get("imagenPrincipal")).getFileSize()>1024*100)  // 100 Kb
        		errors.add("imagenPrincipal", new ActionError("error.micro.imagenPrincipal"));
        
    		if (get("imagenCampanya")!=null)
    			if (((FormFile)get("imagenCampanya")).getFileSize()>1024*100)  // 100 Kb
        		errors.add("imagenCampanya", new ActionError("error.micro.imagenCampanya"));
        
    		if (get("estiloCSS")!=null)
    			if (((FormFile)get("estiloCSS")).getFileSize()>1024*100)  // 100 Kb
        		errors.add("estiloCSS", new ActionError("error.micro.estiloCSS"));
    		
    		if (!esEntero(""+get("numeronoticias")) || (get("numeronoticias")==null) )
            	errors.add("numeronoticias", new ActionError("error.micro.numeronoticias"));
    		
    		if ( ((String[])get("idiomas")).length==0)
    			errors.add("idiomas", new ActionError("error.micro.idiomas"));
        
            try {
            	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
            	List<?> lang = idiomaDelegate.listarLenguajes();
            	for (int i=0;i<lang.size();i++) {
            		TraduccionMicrosite trad = (TraduccionMicrosite)((ArrayList<?>)get("traducciones")).get(i);
            		if (lang.get(i).equals(Idioma.getIdiomaPorDefecto())){
            			if (trad.getTitulo().length()==0) 
            				errors.add("titulo", new ActionError("error.micro.titulo",Idioma.getIdiomaPorDefecto()));
            	    }
            		else{ 
                			if (trad.getTitulo().length()==0 ){
                				 String[] langChecked = (String[]) get("idiomas");
                				for (int j=0;j<langChecked.length;j++){
                					if ( lang.get(i).equals(langChecked[j])) {
                					 errors.add("titulo", new ActionError("error.micro.idioma.no.defecto", idiomaDelegate.obtenerIdioma("" + lang.get(i)).getNombre() ));		
                					}
                				}
                    		} 
            	     }
            	}
            } catch (Exception e) {
            	log.error("Error comprobando titulo del Microsite", e);
       			e.printStackTrace();
            }

    		
    	}
    	
    	if((accion.equals(propertiesMessages.getString("operacion.guardar")))
    	& (request.getRequestURI().toUpperCase().indexOf("CABECERAPIEEDITA.DO") != -1)) {
            try {
            	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
            	List<?> lang = idiomaDelegate.listarLenguajes();
            	for (int i=0;i<lang.size();i++) {
            		TraduccionMicrosite trad = (TraduccionMicrosite)((ArrayList<?>)get("traducciones")).get(i);
            		if (trad.getCabecerapersonal()!=null)
            			if (trad.getCabecerapersonal().length()>4000)
            				errors.add("cabecerapersonal", new ActionError("error.micro.cabecerapersonal"));
            		if (trad.getPiepersonal()!=null)
            			if (trad.getPiepersonal().length()>4000)
            				errors.add("piepersonal", new ActionError("error.micro.piepersonal"));
            	}
            	
            } catch (Exception e) {
            	log.error("Error comprobando tamaño de cabecera y pie personalizado en el microsite", e);
       			e.printStackTrace();
            }
    		
    	}
    	return errors;

    }
    
}
