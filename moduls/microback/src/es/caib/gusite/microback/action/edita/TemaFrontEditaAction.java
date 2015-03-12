package es.caib.gusite.microback.action.edita;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.TemaFrontForm;
import es.caib.gusite.microback.utils.Cadenas;
import es.caib.gusite.micromodel.*;
import es.caib.gusite.micropersistence.delegate.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by brújula on 20/02/2015.
 */
public class TemaFrontEditaAction extends BaseAction {

    protected static Log log = LogFactory.getLog(TemaFrontEditaAction.class);

    private static final String VERSION_DEFAULT = "50";

    public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        TemaFrontForm temaFrontForm = (TemaFrontForm) form;
        TemaFrontDelegate temaFrontDelegate = DelegateUtil.getTemaFrontDelegate();

        if (esNuevaPlantilla(request)) {
            request.getSession().removeAttribute("PerPlantillasForm");
            List<Plantilla> plantillas = DelegateUtil.getPlantillaDelegate().listarPlantilla();
            request.setAttribute("plantillas", plantillas);
            return mapping.findForward("detallePlantilla");

        } else if (esGuardar(request)) {
            crearActualizarTemaFront(request, temaFrontForm);

        } else if (esEliminarArchivos(request)) {
            elimiarArchivos(temaFrontForm);

        } else if (esEliminarPerPlantillas(request)) {
            elimiarPerPlantillas(temaFrontForm);

        } else if (esSubirArchivo(temaFrontForm)) {
            subirArchivo(temaFrontForm);
            temaFrontForm.set("operacion", null);
        }

        cargarPerTemaFront(request, temaFrontForm);
        List<TemaFront> temaFronts = temaFrontDelegate.listarTemaFront();
        request.setAttribute("temasFrontPadres", temaFronts);
        return mapping.findForward("detalle");
    }

    private boolean esGuardar(HttpServletRequest request) {
        return request.getParameter("accion") != null && request.getParameter("accion").equals("guardar");
    }

    private boolean esNuevaPlantilla(HttpServletRequest request) {
        return request.getParameter("accion") != null && request.getParameter("accion").equals("crear");
    }

    private boolean esEliminarArchivos(HttpServletRequest request) {
        return request.getParameter("accion") != null && request.getParameter("accion").equals("borrarArchivos");
    }

    private boolean esEliminarPerPlantillas(HttpServletRequest request) {
        return request.getParameter("accion") != null && request.getParameter("accion").equals("borrar");
    }

    private boolean esSubirArchivo(TemaFrontForm temaFrontForm) {
        return temaFrontForm.get("operacion") != null && temaFrontForm.get("operacion").equals("crear");
    }

    private void crearActualizarTemaFront(HttpServletRequest request, TemaFrontForm temaFrontForm) throws DelegateException {

        TemaFrontDelegate temaFrontDelegate = DelegateUtil.getTemaFrontDelegate();
        TemaFront temaFront;
        Long id = (Long) temaFrontForm.get("id");
        if (id == null) {
            temaFront = new TemaFront();
            temaFront.setActualizacion(new Date());
            temaFront = setFormToBean(temaFrontForm, temaFront);
            Version version = DelegateUtil.getVersionDelegate().obtenerVersion(VERSION_DEFAULT);
            temaFront.setVersion(version);
            temaFrontDelegate.crearTemaFront(temaFront);
        } else {
            temaFront = temaFrontDelegate.obtenerTemaFront(id);
            temaFront = setFormToBean(temaFrontForm, temaFront);
            temaFrontDelegate.actualizarTemaFront(temaFront);
        }
        addMessage(request, "frontTemas.info.modifica");
        setBeanToForm(temaFront, temaFrontForm);
    }

    private void cargarPerTemaFront(HttpServletRequest request, TemaFrontForm temaFrontForm) throws DelegateException {

        TemaFrontForm temaFrontSession = (TemaFrontForm) request.getSession().getAttribute("TemaFrontForm");
        Long id = (Long) temaFrontSession.get("id");
        TemaFront temaFront = DelegateUtil.getTemaFrontDelegate().obtenerTemaFront(id);
        setBeanToForm(temaFront, temaFrontForm);
    }

    private void subirArchivo(TemaFrontForm temaFrontForm) throws DelegateException, IOException {

        ArchivoTemaFrontDelegate archivoTemaFrontDelegate = DelegateUtil.getArchivoTemaFrontDelegate();
        if (temaFrontForm.get("archi") != null) {
            FormFile file = (FormFile) temaFrontForm.get("archi");
            if (archivoValido(file)) {
                ArchivoTemaFront archivoTemaFront = new ArchivoTemaFront();
                Long id = (Long) temaFrontForm.get("id");
                TemaFront tema = DelegateUtil.getTemaFrontDelegate().obtenerTemaFront(id);
                archivoTemaFront.setTema(tema);
                archivoTemaFront.setArchivo(populateArchivo(null, file, null, null));
                archivoTemaFront.setPath(Cadenas.string2uri(archivoTemaFront.getArchivo().getNombre()));
                archivoTemaFrontDelegate.crearArchivoTemaFront(archivoTemaFront);
            }
        }
    }

    private void elimiarArchivos(TemaFrontForm temaFrontForm) throws DelegateException {

        ArchivoTemaFrontDelegate archivoTemaFrontDelegate = DelegateUtil.getArchivoTemaFrontDelegate();
        String[] seleccionados = (String[]) temaFrontForm.get("seleccionados");
        for (String select : seleccionados) {
            ArchivoTemaFront archivoTemaFront = archivoTemaFrontDelegate.obtenerArchivoTemaFront(Long.parseLong(select));
            if (archivoTemaFront != null) {
                archivoTemaFrontDelegate.borrarArchivoTemaFront(archivoTemaFront);
            }
        }
    }

    private void elimiarPerPlantillas(TemaFrontForm temaFrontForm) throws DelegateException {

        PersonalizacionPlantillaDelegate personalizacionPlantillaDelegate = DelegateUtil.getPersonalizacionPlantillaDelegate();
        String[] seleccionados = (String[]) temaFrontForm.get("seleccionados");
        for (String select : seleccionados) {
            PersonalizacionPlantilla personalizacionPlantilla = personalizacionPlantillaDelegate.obtenerPersonalizacionPlantilla(Long.parseLong(select));
            if (personalizacionPlantilla != null) {
                personalizacionPlantillaDelegate.borrarPersonalizacionPlantilla(personalizacionPlantilla);
            }
        }
    }

    private void setBeanToForm(TemaFront temaFront, TemaFrontForm temaFrontForm) {

        temaFrontForm.set("id", temaFront.getId());
        temaFrontForm.set("nombre", temaFront.getNombre());
        if (temaFront.getTemaPadre() != null) {
            temaFrontForm.set("temaPadre", temaFront.getTemaPadre().getId());
        }

        ArrayList personalizacionesPlantilla = new ArrayList<PersonalizacionPlantilla>(temaFront.getPersonalizacionesPlantilla());
        if (personalizacionesPlantilla.size() > 0) {
            temaFrontForm.set("personalizacionesPlantilla", personalizacionesPlantilla);
        }

        ArrayList archivos = new ArrayList<ArchivoTemaFront>(temaFront.getArchivoTemaFronts());
        if (archivos.size() > 0) {
            temaFrontForm.set("archivos", archivos);
        }

        if (temaFront.getCss() != null) {
            temaFrontForm.set("css", temaFront.getCss());
        }
    }

    private TemaFront setFormToBean(TemaFrontForm temaFrontForm, TemaFront temaFront) throws DelegateException {

        temaFront.setNombre((String) temaFrontForm.get("nombre"));
        Long id = (Long) temaFrontForm.get("temaPadre");
        TemaFront temaPadre = DelegateUtil.getTemaFrontDelegate().obtenerTemaFront(id);
        temaFront.setTemaPadre(temaPadre);

        return temaFront;
    }
}