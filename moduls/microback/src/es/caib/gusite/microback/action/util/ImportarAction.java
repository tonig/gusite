package es.caib.gusite.microback.action.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.Microback;
import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.ImportarForm;
import es.caib.gusite.microback.utils.betwixt.Configurator;
import es.caib.gusite.microback.utils.job.IndexacionJobUtil;
import es.caib.gusite.micromodel.Actividadagenda;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.ArchivoLite;
import es.caib.gusite.micromodel.Componente;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Lineadatocontacto;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.MicrositeCompleto;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.PersonalizacionPlantilla;
import es.caib.gusite.micromodel.Plantilla;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.TemaFront;
import es.caib.gusite.micromodel.Temafaq;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionArchivo;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionFaq;
import es.caib.gusite.micromodel.TraduccionLineadatocontacto;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micromodel.TraduccionTipo;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micromodel.Version;
import es.caib.gusite.micropersistence.delegate.ActividadDelegate;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.ComponenteDelegate;
import es.caib.gusite.micropersistence.delegate.ContactoDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.EstadisticaDelegate;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;
import es.caib.gusite.micropersistence.delegate.FrqssiDelegate;
import es.caib.gusite.micropersistence.delegate.MenuDelegate;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.PersonalizacionPlantillaDelegate;
import es.caib.gusite.micropersistence.delegate.PlantillaDelegate;
import es.caib.gusite.micropersistence.delegate.TemaDelegate;
import es.caib.gusite.micropersistence.delegate.TemaFrontDelegate;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;
import es.caib.gusite.micropersistence.delegate.UsuarioDelegate;
import es.caib.gusite.micropersistence.delegate.VersionDelegate;
import es.caib.gusite.micropersistence.util.ArchivoUtil;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.micropersistence.util.log.MicroLog;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.UnidadData;

/**
 * Action que importa un microsite <P>
 * Definición Struts:<BR>
 * action path="importar"<BR>
 * name="importarForm"<BR>
 * scope="request" <BR>
 * unknown="false" <BR>
 * forward name="detalle" path="/importar.jsp"
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ImportarAction extends BaseAction {

	private static Log log = LogFactory.getLog(ImportarAction.class);
	private static String[] roles = new String[] { "gussystem", "gusadmin" };
	
	// Constantes para la descompresión del archivo ZIP.
	private final String NOMBRE_BASE_DIR_ZIP = "_importacion_microsite";
	
	private String mensaje = "";
	private Hashtable<String, Long> tablamapeos = new Hashtable<String, Long>();
	private Hashtable<String, String> hshURLs = new Hashtable<String, String>();
	private Long idmicroant;
	private String _hashcode;
	
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        _hashcode = "" + this.hashCode() + new SimpleDateFormat("hhmmss").format(new java.util.Date()); //un poco chapuza, pero sirve perfectamente

        recogerUsuario(request);

		// Solo podrán importar los roles gussystem y gusadmin
        Hashtable<String, String> rolenames = recogerRoles(request);
		if (!(rolenames.contains(roles[0]) || rolenames.contains(roles[1]))) {
			addMessage(request, "peticion.error");
			return mapping.findForward("info");
		}

		request.getSession().removeAttribute("MVS_importprocessor");
		request.getSession().removeAttribute("MVS_avisoindexadorimportprocessor");

		ImportarForm f = (ImportarForm) form;
		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		
		try {
			
            if (f.getArchi() != null) {

                addImportLog("Inici importació, USUARI: [" + request.getSession().getAttribute("username") + "]");
                addImportLogVisual(request, (String) rb.getObject("logimport.upload"));
                addImportLogVisual(request, (String) rb.getObject("logimport.tamano") + ": " + f.getArchi().getFileSize() + " bytes");
                addImportLogVisual(request, (String) rb.getObject("logimport.integridad.ini"));

                BeanReader beanReader = new BeanReader();
                Configurator.configure(beanReader);

                String tmpDir = System.getProperty("java.io.tmpdir") + File.separator;
                String prefijoFechaHora = obtenerFechaHora();
                String dirDescompresionZIP = tmpDir + prefijoFechaHora + NOMBRE_BASE_DIR_ZIP;

                JAXBContext jaxbContext = JAXBContext.newInstance(MicrositeCompleto.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                InputStream fisXML = zipReaderXml(f, dirDescompresionZIP);
                Reader reader = new InputStreamReader(fisXML, "ASCII");
                MicrositeCompleto micro = (MicrositeCompleto) jaxbUnmarshaller.unmarshal(reader);

                File fileDirDescompresionZIP = new File(dirDescompresionZIP);
                fileDirDescompresionZIP.mkdir();
                ZipInputStream zis = new ZipInputStream(f.getArchi().getInputStream());
                Map<Long, String> listaArchivos = zipReaderArchivos(dirDescompresionZIP, zis);
                micro = importarArchivosMicrosite(micro, listaArchivos);

                addImportLogVisual(request, (String) rb.getObject("logimport.integridad.fin"));

                // Comprobamos si la versión con que se exportó y la versión donde se importa es la misma
                if (Double.valueOf(micro.getVersion()) > Double.valueOf(Microback.microsites_version)) {
                    addMessage(request, "micro.importar.version", micro.getVersion(), Microback.microsites_version);
                    return mapping.findForward("info");
                }
                
                addImportLogVisual(request, (String) rb.getObject("logimport.version") + " [" + micro.getVersion() + "]");

                // si hay un nuevo título, lo actualizo en el idioma por defecto
                if (f.getNuevonombre().length() > 0) {
                    ((TraduccionMicrosite) micro.getTraduccion()).setTitulo("" + f.getNuevonombre());
                }

                // Actualizo la fecha de creación
                micro.setFecha(new Date());
                mensaje = "";
                tablamapeos = new Hashtable<String, Long>();
                hshURLs = new Hashtable<String, String>();

                if (!tieneUA(micro, request, rb)) {
                    throw new Exception("No se pudo obtener la unidad administrativa asociada al microsite");
                }

                crearMicro(micro, listaArchivos, request, f.getTarea());
                request.setAttribute("mensaje", mensaje);

                if ((f.getIndexar() != null) && (f.getIndexar().equals("S"))) {
                    String aviso = (String) rb.getObject("micro.importar.aviso1") + (String) rb.getObject("micro.importar.aviso2") + (String) rb.getObject("micro.importar.aviso3");
                    request.getSession().setAttribute("MVS_avisoindexadorimportprocessor", aviso);
                    
                    addImportLogVisual(request, "Comença indexació");
                    
                    IndexacionJobUtil.crearJob(IndexacionUtil.TIPO_MICROSITE, null, micro.getId());                    
                    
                    addImportLogVisual(request, "Indexació executant-se en background");
                }

                addImportLog("Fi importació, USUARI: [" + request.getSession().getAttribute("username") + "], nou MICROSITE: [" + micro.getId() + "]");
            }

		} catch (Exception e) {
			log.error((String) rb.getObject("logimport.error"), e);
			addMessage(request, "peticion.error");
			addImportLogVisualStackTrace(request, (String) rb.getObject("logimport.error"), e.getStackTrace());
			return mapping.findForward("info");
		}
		
		return mapping.findForward("detalle");
	}

    private void recogerUsuario(HttpServletRequest request) throws DelegateException {

        if (request.getSession().getAttribute("MVS_usuario") == null) {
            UsuarioDelegate usudel = DelegateUtil.getUsuarioDelegate();
            Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
            request.getSession().setAttribute("MVS_usuario", usu);
        }
    }

	private Hashtable<String, String> recogerRoles(HttpServletRequest request) {

        Hashtable<String, String> rolenames = new Hashtable<String, String>();
        if (request.getSession().getAttribute("rolenames") == null) {
            if (request.getRemoteUser() != null) {
                request.getSession().setAttribute("username", request.getRemoteUser());
                rolenames = new Hashtable<String, String>();

                for (int i = 0; i < roles.length; i++) {
                    if (request.isUserInRole(roles[i])) {
                        rolenames.put(roles[i], roles[i]);
                    }
                }
                request.getSession().setAttribute("rolenames", rolenames);
            }

        } else {
            rolenames = (Hashtable<String, String>) request.getSession().getAttribute("rolenames");
        }

        return rolenames;
    }

    private InputStream zipReaderXml(ImportarForm f, String dirDescompresionZIP) throws IOException {

        try {
            byte[] buffer = new byte[1024];

            File fileDirDescompresionZIP = new File(dirDescompresionZIP);
            fileDirDescompresionZIP.mkdir();

            ZipInputStream zis = new ZipInputStream(f.getArchi().getInputStream());
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(dirDescompresionZIP + File.separator + fileName);
                log.debug("file unzip : " + newFile.getAbsoluteFile());

                // create all non exists folders else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                if (ze.isDirectory()) {
                    newFile.mkdir();

                } else {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();

                    if (ze.getName().endsWith(".xml")) {
                        // Si es el archivo XML con el microsite importado, guardamos un InputStream que apunte a él para procesarlo justo después de descomprimir el ZIP.
                        zis.closeEntry();
                        zis.close();
                        return new FileInputStream(newFile);
                    }
                }

                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

        } catch (SecurityException e) {
            log.error(e);
            throw e;
        } catch (IOException e) {
            log.error(e);
            throw e;
        }

        return null;
    }
    
    private byte[] leerDatosPath (String path) throws IOException {

    	File newFile = new File(path);
    	return Files.readAllBytes(newFile.toPath());
    	
    }

    private Map<Long, String> zipReaderArchivos(String dirDescompresionZIP, ZipInputStream zis) throws IOException {

        Map<Long, String> listaArchivos = new HashMap<Long, String>();
        try {
            byte[] buffer = new byte[1024];

            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();
                String path = dirDescompresionZIP + File.separator + fileName;
                
                File newFile = new File(dirDescompresionZIP + File.separator + fileName);
                log.debug("file unzip : " + newFile.getAbsoluteFile());

                // create all non exists folders else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                if (ze.isDirectory()) {
                    newFile.mkdir();

                } else if (!ze.getName().endsWith(".xml")) {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    String[] idName = newFile.getName().split("_");
                    Long id = Long.parseLong(idName[0]);
                    listaArchivos.put(id, path);

                }

                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
            return listaArchivos;

        } catch (SecurityException e) {
            log.error(e);
            throw e;
        } catch (IOException e) {
            log.error(e);
            throw e;
        }
    }

    private MicrositeCompleto importarArchivosMicrosite(MicrositeCompleto micro, Map<Long, String> archivos) throws IOException {

        if (micro.getImagenPrincipal() != null && archivos.get(micro.getImagenPrincipal().getId()) != null) {
            micro.getImagenPrincipal().setDatos( leerDatosPath(archivos.get(micro.getImagenPrincipal().getId())));
        }
        if (micro.getImagenCampanya() != null && archivos.get(micro.getImagenCampanya().getId()) != null) {
            micro.getImagenCampanya().setDatos( leerDatosPath(archivos.get(micro.getImagenCampanya().getId())));
        }
        if (micro.getEstiloCSS() != null && archivos.get(micro.getEstiloCSS().getId()) != null) {
            micro.getEstiloCSS().setDatos(leerDatosPath(archivos.get(micro.getEstiloCSS().getId())));
        }

        for (Object menu : micro.getMenus()) {
            if (((Menu) menu).getImagenmenu() != null && archivos.get(((Menu) menu).getImagenmenu().getId()) != null) {
                ((Menu) menu).getImagenmenu().setDatos(leerDatosPath(archivos.get(((Menu) menu).getImagenmenu().getId())));
            }
            for (Contenido contenido : ((Menu) menu).getContenidos()) {
                if (contenido.getImagenmenu() != null && archivos.get(contenido.getImagenmenu().getId()) != null) {
                    contenido.getImagenmenu().setDatos(leerDatosPath(archivos.get(contenido.getImagenmenu().getId())));
                }
            }
        }

        for (Object agenda : micro.getAgendas()) {
            for (TraduccionAgenda trad : ((Agenda) agenda).getTraducciones().values()) {
                if (trad.getDocumento() != null && archivos.get(trad.getDocumento().getId()) != null) {
                    trad.getDocumento().setDatos(leerDatosPath(archivos.get(trad.getDocumento().getId())));
                    trad.getDocumento().setId(null);
                }
                if (trad.getImagen() != null && archivos.get(trad.getImagen().getId()) != null) {
                    trad.getImagen().setDatos(leerDatosPath(archivos.get(trad.getImagen().getId())));
                    trad.getImagen().setId(null);
                }
            }
        }

        for (Noticia noticia : (Set<Noticia>)micro.getNoticias()) {
            if (noticia.getImagen() != null && archivos.get(noticia.getImagen().getId()) != null) {
                noticia.getImagen().setDatos(leerDatosPath(archivos.get(noticia.getImagen().getId())));
            }
            for (TraduccionNoticia trad : noticia.getTraducciones().values()) {
                if (trad.getDocu() != null && archivos.get(trad.getDocu().getId()) != null) {
                    trad.getDocu().setDatos(leerDatosPath(archivos.get(trad.getDocu().getId())));
                    trad.getDocu().setId(null);
                }
            }
        }

        for (Componente componente : (Set<Componente>)micro.getComponentes()) {
            if (componente.getImagenbul() != null && archivos.get(componente.getImagenbul().getId()) != null) {
                componente.getImagenbul().setDatos(leerDatosPath(archivos.get(componente.getImagenbul().getId())));
            }
        }

        for (Object encuesta : micro.getEncuestas()) {
            for (Pregunta pregunta : ((Encuesta) encuesta).getPreguntas()) {
                if (pregunta.getImagen() != null && archivos.get(pregunta.getImagen().getId()) != null) {
                    pregunta.getImagen().setDatos(leerDatosPath(archivos.get(pregunta.getImagen().getId())));
                }
            }
        }

        return micro;
    }

    private boolean tieneUA(MicrositeCompleto micro, HttpServletRequest request, ResourceBundle rb) {

        try {
            UnidadData ua = PluginFactory.getInstance().getOrganigramaProvider().getUnidadData(micro.getIdUA(), "ca");

            return (ua == null) ? false : true;

        } catch (Exception e) {
            log.error((String) rb.getObject("logimport.errorUA"));
            addMessage(request, "peticion.errorUA");
            addImportLogVisualStackTrace(request, (String) rb.getObject("logimport.errorUA"), e.getStackTrace());

            return false;
        }
    }
	
	private String obtenerFechaHora() {
		
		Calendar cal = Calendar.getInstance();
		
        // Cero inicial para meses o días menores que 10.
        String mes = ((cal.get(Calendar.MONTH) + 1) > 10) ? String.valueOf((cal.get(Calendar.MONTH) + 1)) : "0" + String.valueOf((cal.get(Calendar.MONTH) + 1));
        String dia = (cal.get(Calendar.DAY_OF_MONTH) > 10) ? String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) : "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String fecha = cal.get(Calendar.YEAR) + mes + dia; // Formato: YYYYMMDD
        String hora = cal.get(Calendar.HOUR_OF_DAY) + "" + cal.get(Calendar.MINUTE) + "" + cal.get(Calendar.SECOND); // Formato: HHmmss
        String prefijoFechaHora = fecha + "_" + hora;
        
        return prefijoFechaHora;
	}

	/**
     * Esta función realiza las operaciones necesarias para crear un nuevo microsite
     * basándose en el Microsite importado en XML
     * @param mic
     * @param request
     * @param tarea
	 * @throws DelegateException 
	 */
	private void crearMicro(MicrositeCompleto mic, Map<Long, String> listaArchivos, HttpServletRequest request, String tarea) throws DelegateException {

        MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
        //IndexerDelegate indexdel = DelegateUtil.getIndexerDelegate();
        ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");

        Set activi = mic.getActividades();
        Set agenda = mic.getAgendas();
        Set tpnoti = mic.getTiponotis();
		Set notics = mic.getNoticias();
        Set frqssi = mic.getFrqssis();
        Set temas = mic.getTemas();
		Set faq = mic.getFaqs();
        Set menus = mic.getMenus();
        Set docus = mic.getDocus();
		Set contactos = mic.getFormularioscontacto();
		Set compos = mic.getComponentes();
		Set encuestas = mic.getEncuestas();
        Set perPlantillas = mic.getPersonalizacionesPlantilla();
        TemaFront temaFront = mic.getTema();

        // Elimino del bean los objetos que grabaré posteriormente
        mic.setActividades(null);
        mic.setAgendas(null);
        mic.setFaqs(null);
        mic.setMenus(null);
        mic.setNoticias(null);
        mic.setFrqssis(null);
        mic.setTemas(null);
        mic.setTiponotis(null);
        mic.setDocus(null);
        mic.setFormularioscontacto(null);
        mic.setComponentes(null);
        mic.setEncuestas(null);
        mic.setPersonalizacionesPlantilla(null);
        mic.setTema(null);

		/*
		 * Se realizan las importaciones objeto o objeto debido al particular
		 * diseño de la BD excepto con los objetos que cuelgan directamente y
		 * sin hijos del microsite, además hay que actualizar las URL de ciertos
		 * campos.
		 * 
		 * Elementos insertados independientemente: Actividades, Agendas, Tipo
		 * de Noticias, Noticias, Formularios QSSI, Temas, Faqs, Banners Menus,
		 * Formularios de Contacto, Documentos , Encuestas y Componentes
		 * 
		 * Elementos insertados junto con el bean MicrositeCompleto: Microsite,
		 * Idiomas Microsite
		 */
		try {
			// In/habilitamos que se indexen cosas en solr
			//indexdel.setBloqueado(true);

			// Inserto el microsite sin los objetos independientes
			idmicroant = mic.getId(); // necesario para saber el id a sustituir en los enlaces
			mic.setId(null);

            Long idImagenPrincipal = null;
            if (mic.getImagenPrincipal() != null) {
                idImagenPrincipal = mic.getImagenPrincipal().getId();
                mic.getImagenPrincipal().setId(null);
            }

            Long idImagenCampanya = null;
            if (mic.getImagenCampanya() != null) {
                idImagenCampanya = mic.getImagenCampanya().getId();
                mic.getImagenCampanya().setId(null);
            }

            Long idEstiloCSS = null;
            if (mic.getEstiloCSS() != null) {
                idEstiloCSS = mic.getEstiloCSS().getId();
                mic.getEstiloCSS().setId(null);
            }
            
            if (temaFront != null) {
                relacionaTema(temaFront, mic, request);
            }

			Long idmicronuevo = null;
			if (tarea.equals("R")) {
				// reemplazar
				idmicronuevo = bdMicro.reemplazarMicrositeCompleto(mic);

				// Guardamos resumen de estadisticas para el micro site reemplazado
				EstadisticaDelegate estdel = DelegateUtil.getEstadisticaDelegate();
				estdel.crearEstadisticasMicroReemplazado(idmicroant, idmicronuevo);
				// Eliminar estadísticas
				estdel.borrarEstadisticasMicroSite(idmicroant);
				addImportLogVisual(request, "Reemplaçat Microsite: " + mic.getClaveunica());
			} else {
                mic.setId(null);
                //El uri no puede repetirse
                Microsite otroMicrosite = bdMicro.obtenerMicrositebyUri(mic.getUri());
                if (otroMicrosite != null) {
                	//El uri está duplicada, la dejamos vacía para que se inicialice
                	mic.setUri(null);//
                }
				// crear
				idmicronuevo = bdMicro.grabarMicrositeCompleto(mic);
				addImportLogVisual(request, "Crear Nou Microsite: " + mic.getClaveunica());
			}

            if (idImagenPrincipal != null) {
                tablamapeos.put(new String("DCM_" + idImagenPrincipal), idImagenPrincipal);
            }
            if (idImagenCampanya != null) {
                tablamapeos.put(new String("DCM_" + idImagenCampanya), idImagenCampanya);
            }
            if (idEstiloCSS != null) {
                tablamapeos.put(new String("DCM_" + idEstiloCSS), idEstiloCSS);
            }

			insertaContactos(contactos, mic, request);
			insertaAgendas(activi, agenda, mic, request);
			insertaNoticias(tpnoti, notics, mic, request);
			insertaFrqssis(frqssi, mic, request);
			insertaFaqs(temas, faq, mic, request);
			insertaMenusContenidos(menus, mic, request);
			insertaComponentes(compos, mic, request);
			insertaEncuestas(encuestas, mic, request);
			insertaDocus(docus, listaArchivos, idmicronuevo, mic, request); // cambio orden
            insertarPerPlantillas(perPlantillas, mic, request);

			tablamapeos.put(new String("MIC_" + idmicroant), idmicronuevo.longValue());

			calculaHshURLs(idmicronuevo);
			addImportLogVisual(request, (String) rb.getObject("logimport.referencias.calculo"));
			mic = bdMicro.obtenerMicrositeCompleto(mic.getId()); // refresco el microsite
			actualizaURLMicrositeCompleto(tablamapeos, mic, request);
			addImportLogVisual(request, (String) rb.getObject("logimport.referencias.proceso"));

			if (mensaje.length() == 0) {
                mensaje = "<strong>" + (String) rb.getObject("logimport.creado") + "</strong>";
            }
			
			addImportLogVisual(request, mensaje);
			
		} catch (Exception ex) {
			mensaje += (String) rb.getObject("logimport.error") + ": " + ((TraduccionMicrosite) mic.getTraduccion()).getTitulo() + "</strong><br/><br/>" + ex.toString() + "<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		} finally {
			//indexdel.setBloqueado(false); // Habilitamos que se indexen cosas en solr
		}
		
		return;
	}

	private void relacionaTema(TemaFront temaFront, MicrositeCompleto mic, HttpServletRequest request) throws Exception {
		
		TemaFrontDelegate tfDel = DelegateUtil.getTemaFrontDelegate();
		TemaFront temaLocal = tfDel.obtenerTemabyUri(temaFront.getUri());
		if (temaLocal == null) {
			throw new Exception("El microsite requiere el tema " + temaFront.getUri() + " que no existe en esta implantación");
		}
		mic.setTema(temaLocal);
		
	}

	/**
	 *  Esta función crea los formularios de contacto en el nuevo Microsite
	 */
	private void insertaContactos(Set conts, MicrositeCompleto mic, HttpServletRequest request) {
		
		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Set<Contacto> lista = new HashSet<Contacto>();
		
		try {
			ContactoDelegate bdContacto = DelegateUtil.getContactoDelegate();
			Iterator<?> it = conts.iterator();

			while (it.hasNext()) {
				Contacto cont = (Contacto) it.next();
				// Replicamos el formulario para no modificar el original
				Contacto contnuevo = (Contacto) BeanUtils.cloneBean(cont);
				contnuevo.setId(null);
				contnuevo.setIdmicrosite(mic.getId());
                List<Lineadatocontacto> lineas = contnuevo.getLineasdatocontacto();
                contnuevo.setLineasdatocontacto(null);
                Long idContacto = bdContacto.grabarContacto(contnuevo);
                for (Lineadatocontacto linea : lineas) {
                    linea.setId(null);
                    for (TraduccionLineadatocontacto trad : linea.getTraducciones().values()) {
                        trad.getId().setCodigoLineadatocontacto(null);
                    }
                    bdContacto.creamodificaLinea(linea, idContacto);
                }

				tablamapeos.put(new String("FRM_" + cont.getId()), contnuevo.getId());
				lista.add(contnuevo);
				stlog.append(contnuevo.getId() + " ");
			}
			
			mic.setFormularioscontacto(lista);
			addImportLogVisual(request, (String) rb.getObject("logimport.formularios") + ": " + stlog.toString());
			
		} catch (Exception ex) {
			addImportLogVisual(request, (String) rb.getObject("logimport.formularios") + ": " + stlog.toString());
			mensaje += (String) rb.getObject("logimport.formularios.error") + ": " + ex.toString() + "<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
	}

    /**
	 * Esta función crea los documentos en el nuevo Microsite
	 */
	private void insertaDocus(Set docus, Map<Long, String> listaArchivos, Long idmicronuevo, MicrositeCompleto mic, HttpServletRequest request) {
		
		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Set<ArchivoLite> lista = new HashSet<ArchivoLite>();
		ArchivoDelegate bdDocu = DelegateUtil.getArchivoDelegate();

		try {
						
			Iterator<?> it = docus.iterator();
			while (it.hasNext()) {
				
				ArchivoLite doc = (ArchivoLite) it.next();
				// Replicamos el documento para no modificar el original
				ArchivoLite docnuevo = (ArchivoLite) BeanUtils.cloneBean(doc);

				if (doc.getPagina() != null) {
					Long newidpagina = (Long) tablamapeos.get("CON_" + doc.getPagina().longValue());
					docnuevo.setPagina(newidpagina);
				}
				
				Archivo archivo = ArchivoUtil.archivoLite2Archivo(docnuevo);
				
				// Anulamos los IDs de los archivos asociados a la BD.
				docnuevo.setId(null);
				archivo.setId(null);
				
				// Obtenemos datos del archivo y asignamos ID del Microsite importado.
				archivo.setDatos(leerDatosPath(listaArchivos.get(doc.getId())));
				archivo.setIdmicrosite(idmicronuevo);
				
				Long idDoc = bdDocu.insertarArchivo(archivo);
				docnuevo.setId(idDoc);
				
				tablamapeos.put(new String("DCM_" + doc.getId()), idDoc);

				lista.add(docnuevo);
				stlog.append(idDoc + " ");
				
			}
			
			mic.setDocus(lista);
			addImportLogVisual(request, (String) rb.getObject("logimport.documentos") + ": " + stlog.toString());
			
		} catch (Exception ex) {
			
			addImportLogVisual(request, (String) rb.getObject("logimport.documentos") + ": " + stlog.toString());
			mensaje += (String) rb.getObject("logimport.documentos.error") + ": " + ex.toString() + "<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
			
		}
		
	}

	/**
	 * Esta función crea las actividades y los eventos de la agenda en el nuevo
	 * Microsite
	 */
	private void insertaAgendas(Set activis, Set agendas, MicrositeCompleto mic, HttpServletRequest request) {

		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Long idact_nueva;
		Set<Actividadagenda> lista1 = new HashSet<Actividadagenda>();
		Set<Agenda> lista2 = new HashSet<Agenda>();
		ActividadDelegate bdActividad = DelegateUtil.getActividadagendaDelegate();
		AgendaDelegate bdAgenda = DelegateUtil.getAgendaDelegate();

		try {
			Iterator<?> it = activis.iterator();

			while (it.hasNext()) {

				Actividadagenda act = (Actividadagenda) it.next();
				// Replicamos la actividad para no modificar la original
				Actividadagenda actnueva = (Actividadagenda) BeanUtils.cloneBean(act);
				actnueva.setId(null);
				actnueva.setIdmicrosite(mic.getId());
				// creamos las actividades
				idact_nueva = bdActividad.grabarActividad(actnueva);
				lista1.add(actnueva);
				// buscamos eventos de agenda con esa actividad
				Iterator<?> it1 = agendas.iterator();
				
				while (it1.hasNext()) {
					Agenda age = (Agenda) it1.next();
					
					if (age.getActividad().getId().compareTo(act.getId()) == 0) {
						// creo el evento de agenda
						Agenda agenueva = (Agenda) BeanUtils.cloneBean(age);
						agenueva.setId(null);
						agenueva.getActividad().setId(idact_nueva);
						agenueva.setIdmicrosite(mic.getId());
						agenueva.getActividad().setIdmicrosite(mic.getId());
						bdAgenda.grabarAgenda(agenueva);
						lista2.add(agenueva);
						stlog.append(agenueva.getId() + " ");
					}
				}
			}
			
			mic.setActividades(lista1);
			mic.setAgendas(lista2);
			addImportLogVisual(request, (String) rb.getObject("logimport.eventos") + ": " + stlog.toString());
			
		} catch (Exception ex) {
			addImportLogVisual(request, (String) rb.getObject("logimport.eventos") + ": " + stlog.toString());
			mensaje += (String) rb.getObject("logimport.eventos.error") + ": <br/>" + ex.toString() + "<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
	}

	/**
	 *  Esta función crea los tipos de noticias y las noticias
	 *  en el nuevo Microsite
	 */
	private void insertaNoticias(Set tpnotis, Set notics, MicrositeCompleto mic, HttpServletRequest request) {

		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Long idtpnotic_nueva;
		Set<Tipo> lista1 = new HashSet<Tipo>();
		Set<Noticia> lista2 = new HashSet<Noticia>();
		TipoDelegate bdTipo = DelegateUtil.getTipoDelegate();
		NoticiaDelegate bdNoticia = DelegateUtil.getNoticiasDelegate();
        Long idDoc = null;

		try {
			Iterator<?> it = tpnotis.iterator();

			while (it.hasNext()) {

				Tipo tp = (Tipo) it.next();
				// Replicamos el tipo de noticia para no modificar la original
				Tipo tpnuevo = (Tipo) BeanUtils.cloneBean(tp);
				tpnuevo.setId(null);
				tpnuevo.setIdmicrosite(mic.getId().longValue());
				// creamos los tipos de noticias
                tpnuevo = generarNuevasUrisTipo(tpnuevo, tp.getIdmicrosite().toString());
				idtpnotic_nueva = bdTipo.grabarTipo(tpnuevo);
				tablamapeos.put(new String("TPN_" + tp.getId()), idtpnotic_nueva);
				lista1.add(tpnuevo);
				// buscamos noticias con ese tipo de noticia
				Iterator<?> it1 = notics.iterator();

				while (it1.hasNext()) {
					
					Noticia not = (Noticia)it1.next();
					
					if (not.getTipo().getId().compareTo(tp.getId()) == 0) {
						// creo la noticia
						Noticia notnueva = (Noticia) BeanUtils.cloneBean(not);
						notnueva.setId(null);
						notnueva.getTipo().setId(idtpnotic_nueva);
						notnueva.setIdmicrosite(mic.getId().longValue());
						notnueva.getTipo().setIdmicrosite(mic.getId().longValue());
                        idDoc = null;
                        if (notnueva.getImagen() != null) {
                            idDoc = notnueva.getImagen().getId();
                            notnueva.getImagen().setId(null);
                        }

                        notnueva = generarNuevasUrisNoti(notnueva);
						bdNoticia.grabarNoticia(notnueva);

                        if (idDoc != null) {
                            tablamapeos.put(new String("DCM_" + idDoc), notnueva.getImagen().getId());
                        }
						tablamapeos.put(new String("NOT_" + not.getId()), notnueva.getId());
						lista2.add(notnueva);
						stlog.append(notnueva.getId() + " ");
					}
				}
			}

			mic.setTiponotis(lista1);
			mic.setNoticias(lista2);
			addImportLogVisual(request, (String) rb.getObject("logimport.elementos") + ": " + stlog.toString());

		} catch (Exception ex) {
			addImportLogVisual(request, (String) rb.getObject("logimport.elementos") + ": " + stlog.toString());
			mensaje += (String) rb.getObject("logimport.elementos.error") + ": " + ex.toString() + "<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
	}

	/**
	 *  Esta función crea los Formularios QSSI
	 *  en el nuevo Microsite
	 */
	private void insertaFrqssis(Set frqssis, MicrositeCompleto mic, HttpServletRequest request) {
		
		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Set<Frqssi> lista = new HashSet<Frqssi>();
		FrqssiDelegate bdFrqssi = DelegateUtil.getFrqssiDelegate();
		
		try {
			Iterator<?> it = frqssis.iterator();

			while (it.hasNext()) {

				Frqssi frq = (Frqssi) it.next();
				// Replicamos el frqssi para no modificar el original
				Frqssi frqnuevo = (Frqssi) BeanUtils.cloneBean(frq);
				frqnuevo.setId(null);
				frqnuevo.setIdmicrosite(mic.getId());
				bdFrqssi.grabarFrqssi(frqnuevo);
				tablamapeos.put(new String("FRQ_" + frq.getId()), frqnuevo.getId());
				lista.add(frqnuevo);
				stlog.append(frqnuevo.getId() + " ");

			}

			mic.setFrqssis(lista);
			addImportLogVisual(request, (String) rb.getObject("logimport.qssi") + ": " + stlog.toString());

		} catch (Exception ex) {
			addImportLogVisual(request, (String) rb.getObject("logimport.qssi") + ": " + stlog.toString());
			mensaje += (String) rb.getObject("logimport.qssi.error") + ": " + ex.toString() + "<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
	}

	/**
	 * Esta función crea los temas y las faqs en el nuevo Microsite
	 */
	private void insertaFaqs(Set temas, Set faqs, MicrositeCompleto mic, HttpServletRequest request) {

		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Long idtema_nuevo;
		Set<Temafaq> lista1 = new HashSet<Temafaq>();
		Set<Faq> lista2 = new HashSet<Faq>();
		TemaDelegate bdTema = DelegateUtil.getTemafaqDelegate();
		FaqDelegate bdFaq = DelegateUtil.getFaqDelegate();

		try {

			Iterator<?> it = temas.iterator();

			while (it.hasNext()) {

				Temafaq tema = (Temafaq) it.next();
				// Replicamos el tema de las faq para no modificar la original
				Temafaq temanuevo = (Temafaq) BeanUtils.cloneBean(tema);
				temanuevo.setId(null);
				temanuevo.setIdmicrosite(mic.getId());
				// creamos los temas de las faqs
				idtema_nuevo = bdTema.grabarTema(temanuevo);
				lista1.add(temanuevo);

				// buscamos las faq con ese Tema
				Iterator<?> it1 = faqs.iterator();

				while (it1.hasNext()) {
					
					Faq faq = (Faq) it1.next();
					
					if (faq.getTema().getId().compareTo(tema.getId()) == 0) {
						// creo la faq
						Faq faqnuevo = (Faq) BeanUtils.cloneBean(faq);
						faqnuevo.setId(null);
						faqnuevo.getTema().setId(idtema_nuevo);
						faqnuevo.setIdmicrosite(mic.getId());
						faqnuevo.getTema().setIdmicrosite(mic.getId());
						bdFaq.grabarFaq(faqnuevo);
						lista2.add(faqnuevo);
						stlog.append(faqnuevo.getId() + " ");
					}
				}
			}

			mic.setTemas(lista1);
			mic.setFaqs(lista2);
			addImportLogVisual(request, (String) rb.getObject("logimport.faqs") + ": " + stlog.toString());

		} catch (Exception ex) {
			addImportLogVisual(request, (String) rb.getObject("logimport.faqs") + ": " + stlog.toString());
			mensaje += (String) rb.getObject("logimport.faqs.error") + ": " + ex.toString() + "<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
	}
	
	/**
	 * Esta función crea las encuestas en el nuevo Microsite
	 */
	private void insertaComponentes(Set compos, MicrositeCompleto mic, HttpServletRequest request) {
		
		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Set<Componente> lista = new HashSet<Componente>();
		ComponenteDelegate bdCompo = DelegateUtil.getComponentesDelegate();
        Long idDoc = null;
		
		try {
			Iterator<?> it = compos.iterator();

            while (it.hasNext()) {
                Componente cmp = (Componente) it.next();
				Long idanterior = cmp.getId();
				// Replicamos el componente para no modificar el original
				Componente cmpnuevo = (Componente) BeanUtils.cloneBean(cmp);
				cmpnuevo.setId(null);
				cmpnuevo.setIdmicrosite(mic.getId());

				/*****
				 * VRS: pegote para recoger el nuevo tipo de listado asociado al
				 * componente
				 ******/
				Long idtpnuevo = (Long) tablamapeos.get(new String("TPN_" + cmp.getTipo().getId()));
				Iterator<?> iter = mic.getTiponotis().iterator();
				while (iter.hasNext()) {
					Tipo tptmp = (Tipo) iter.next();
					if (tptmp.getId().longValue() == idtpnuevo.longValue()) {
						cmpnuevo.setTipo(tptmp);
						break;
					}
				}
				/****** VRS: fin pegote **************************************************************/

                idDoc = null;
                if (cmpnuevo.getImagenbul() != null) {
                    idDoc = cmpnuevo.getImagenbul().getId();
                    cmpnuevo.getImagenbul().setId(null);
                }

				bdCompo.grabarComponente(cmpnuevo);

                if (idDoc != null) {
                    tablamapeos.put(new String("DCM_" + idDoc), cmpnuevo.getImagenbul().getId());
                }
				tablamapeos.put(new String("COM_" + idanterior), cmpnuevo.getId());
				lista.add(cmpnuevo);
				stlog.append(cmpnuevo.getId() + " ");
				
			}
			
			mic.setComponentes(lista);
			addImportLogVisual(request, (String) rb.getObject("logimport.componentes") + ": " + stlog.toString());
			
		} catch (Exception ex) {
			addImportLogVisual(request, (String) rb.getObject("logimport.componentes") + ": " + stlog.toString());
			mensaje += (String) rb.getObject("logimport.componentes.error") + ": " + ex.toString() + "<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
	}

	/**
	 * Esta función crea los menus y contenidos en el nuevo Microsite
	 */
	private void insertaMenusContenidos(Set menus, MicrositeCompleto mic, HttpServletRequest request) {

		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlogmenu = new StringBuffer("");
		StringBuffer stlogpagina = new StringBuffer("");
		Set<Menu> lista1 = new HashSet<Menu>();
		List<Contenido> lista2 = new ArrayList<Contenido>();
		Hashtable<Long, Long> mapeos = new Hashtable<Long, Long>();
		MenuDelegate bdMenu = DelegateUtil.getMenuDelegate();
		ContenidoDelegate bdConte = DelegateUtil.getContenidoDelegate();
        MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
        Long idDoc = null;

		try {
            Microsite micrositeMenu = bdMicro.obtenerMicrosite(mic.getId());
			Iterator<?> it = menus.iterator();
			// Guardo primero los padres

			while (it.hasNext()) {
				Menu men = (Menu) it.next();
				
				if (men.getPadre().intValue() == 0) {
					// Replicamos el menu para no modificar la original
					Menu menunuevo = (Menu) BeanUtils.cloneBean(men);
					menunuevo.setId(null);
					menunuevo.setMicrosite(micrositeMenu);
					List<Contenido> contenidos = menunuevo.getContenidos();
					menunuevo.setContenidos(null); // vacío los contenidos

                    idDoc = null;
                    if (menunuevo.getImagenmenu() != null) {
                        idDoc = menunuevo.getImagenmenu().getId();
                        menunuevo.getImagenmenu().setId(null);
                    }

					// creamos el menus nuevo vacio
					bdMenu.grabarMenu(menunuevo);

                    if (idDoc != null) {
                        tablamapeos.put(new String("DCM_" + idDoc), menunuevo.getImagenmenu().getId());
                    }
					mapeos.put(men.getId(), menunuevo.getId()); // anterior y nuevo del padre

					// buscamos los contenidos de ese menu
					lista2 = new ArrayList<Contenido>();
					for (Contenido con : contenidos) {
						// creo el contenido
						Contenido connuevo = (Contenido) BeanUtils.cloneBean(con);
						connuevo.setId(null);
						connuevo.setMenu(menunuevo);

                        idDoc = null;
                        if (connuevo.getImagenmenu() != null) {
                            idDoc = connuevo.getImagenmenu().getId();
                            connuevo.getImagenmenu().setId(null);
                        }

                        connuevo = generarNuevasUrisCont(connuevo);
						bdConte.grabarContenido(connuevo);

                        if (idDoc != null) {
                            tablamapeos.put(new String("DCM_" + idDoc), connuevo.getImagenmenu().getId());
                        }
						tablamapeos.put(new String("CON_" + con.getId()), connuevo.getId());
						lista2.add(connuevo);
						stlogpagina.append(connuevo.getId() + " ");
					}
					
					menunuevo.setContenidos(lista2);
					lista1.add(menunuevo);
					stlogmenu.append(menunuevo.getId() + " ");
				}
			}

			it = menus.iterator();
			// Por último los hijos
			while (it.hasNext()) {
				Menu men = (Menu) it.next();

				if (men.getPadre().intValue() > 0) {
					// Replicamos el menu para no modificar la original
					Menu menunuevo = (Menu) BeanUtils.cloneBean(men);
					menunuevo.setId(null);
					menunuevo.setMicrosite(micrositeMenu);
					menunuevo.setPadre((Long) mapeos.get(men.getPadre()));
					List contenidos = menunuevo.getContenidos();
					menunuevo.setContenidos(null); // vacío los contenidos

                    idDoc = null;
                    if (menunuevo.getImagenmenu() != null) {
                        idDoc = menunuevo.getImagenmenu().getId();
                        menunuevo.getImagenmenu().setId(null);
                    }

					// creamos el menus nuevo vacio
					bdMenu.grabarMenu(menunuevo);

                    if (idDoc != null) {
                        tablamapeos.put(new String("DCM_" + idDoc), menunuevo.getImagenmenu().getId());
                    }
					// buscamos los contenidos de ese menu
					Iterator<?> it1 = contenidos.iterator();
					lista2 = new ArrayList<Contenido>();
					while (it1.hasNext()) {
						Contenido con = (Contenido) it1.next();
						// creo el contenido
						Contenido connuevo = (Contenido) BeanUtils.cloneBean(con);
						connuevo.setId(null);
						connuevo.setMenu(menunuevo);
                        idDoc = null;
                        if (connuevo.getImagenmenu() != null) {
                            idDoc = connuevo.getImagenmenu().getId();
                            connuevo.getImagenmenu().setId(null);
                        }

                        connuevo = generarNuevasUrisCont(connuevo);
						bdConte.grabarContenido(connuevo);

                        if (idDoc != null) {
                            tablamapeos.put(new String("DCM_" + idDoc), connuevo.getImagenmenu().getId());
                        }
						tablamapeos.put(new String("CON_" + con.getId()), connuevo.getId());
						lista2.add(connuevo);
						stlogpagina.append(connuevo.getId() + " ");
					}
					
					menunuevo.setContenidos(lista2);
					lista1.add(menunuevo);
					stlogmenu.append(menunuevo.getId() + " ");
				}
			}

			mic.setMenus(lista1);
			addImportLogVisual(request, (String) rb.getObject("logimport.menus") + ": " + stlogmenu.toString());
			addImportLogVisual(request, (String) rb.getObject("logimport.paginas") + ": " + stlogpagina.toString());
			
		} catch (Exception ex) {
			addImportLogVisual(request, (String) rb.getObject("logimport.menus") + ": " + stlogmenu.toString());
			addImportLogVisual(request, (String) rb.getObject("logimport.paginas") + ": " + stlogpagina.toString());
			mensaje += (String) rb.getObject("logimport.paginas.error") + ": " + ex.toString() + "<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
	}
	
	/**
	 * Esta función crea las encuestas en el nuevo Microsite
	 */
	private void insertaEncuestas(Set encus, MicrositeCompleto mic, HttpServletRequest request) {
		
		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		StringBuffer stlog = new StringBuffer("");
		Set<Encuesta> lista = new HashSet<Encuesta>();
		EncuestaDelegate bdEncu = DelegateUtil.getEncuestaDelegate();
		
		try {
			Iterator<?> it = encus.iterator();
			
			while (it.hasNext()) {
				Encuesta enc = (Encuesta) it.next();
				// Replicamos la encuesta para no modificar la original
				Encuesta encnueva = (Encuesta) BeanUtils.cloneBean(enc);
				encnueva.setId(null);
				encnueva.setIdmicrosite(mic.getId());

                List<Pregunta> preguntas = encnueva.getPreguntas();
                encnueva.setPreguntas(null);
                encnueva = generarNuevasUrisEnc(encnueva);
                Long idNew = bdEncu.grabarEncuesta(encnueva);
				encnueva.setId(idNew);
                insertarPreguntas(encnueva, preguntas, bdEncu);

				tablamapeos.put(new String("ENC_" + enc.getId()), encnueva.getId());
				lista.add(encnueva);
				stlog.append(encnueva.getId() + " ");
			}
			
			mic.setEncuestas(lista);
			addImportLogVisual(request, (String) rb.getObject("logimport.encuestas") + ": " + stlog.toString());
			
		} catch (Exception ex) {
			addImportLogVisual(request, (String) rb.getObject("logimport.encuestas") + ": " + stlog.toString());
			mensaje += (String) rb.getObject("logimport.encuestas.error") + ": <br/>" + ex.toString() + "<br/>";
			addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
		}
	}

    private void insertarPreguntas(Encuesta enc, List<Pregunta> preguntas, EncuestaDelegate bdEncu) throws DelegateException {

        Long idDoc = null;
        for (Iterator<Pregunta> iter = preguntas.iterator(); iter.hasNext();) {
            Pregunta preg = iter.next();
            preg.setId(null);
            preg.setIdencuesta(enc.getId());
            List<Respuesta> respuestas = preg.getRespuestas();
            preg.setRespuestas(null);
            idDoc = null;
            if (preg.getImagen() != null) {
                idDoc = preg.getImagen().getId();
                preg.getImagen().setId(null);
            }

            bdEncu.grabarPregunta(preg);

            if (preg.getImagen() != null) {
                tablamapeos.put(new String("DOC_" + idDoc), preg.getImagen().getId());
            }
            insertarRespuestas(preg, respuestas, bdEncu);
        }
    }

    private void insertarRespuestas(Pregunta preg, List<Respuesta> respuestas, EncuestaDelegate bdEncu) throws DelegateException {

        for (Iterator<Respuesta> iter = respuestas.iterator(); iter.hasNext();) {
            Respuesta resp = iter.next();
            resp.setId(null);
            resp.setIdpregunta(preg.getId());
            bdEncu.grabarRespuesta(resp);
        }
    }

    private void insertarPerPlantillas(Set perPlantillas, MicrositeCompleto mic, HttpServletRequest request) {

        ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
        StringBuffer stlog = new StringBuffer("");
        Set<PersonalizacionPlantilla> lista = new HashSet<PersonalizacionPlantilla>();
        PersonalizacionPlantillaDelegate personalizacionPlantillaDelegate = DelegateUtil.getPersonalizacionPlantillaDelegate();
        PlantillaDelegate plantillaDelegate = DelegateUtil.getPlantillaDelegate();
        VersionDelegate versionDelegate = DelegateUtil.getVersionDelegate();
        MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();

        try {
            Iterator<?> it = perPlantillas.iterator();

            while (it.hasNext()) {
                PersonalizacionPlantilla perPlantilla = (PersonalizacionPlantilla) it.next();
                PersonalizacionPlantilla perPlantillaNueva = (PersonalizacionPlantilla) BeanUtils.cloneBean(perPlantilla);

                Plantilla plantilla = perPlantillaNueva.getPlantilla();
                Version version = plantilla.getVersion();
                Version versionBD = versionDelegate.obtenerVersion(version.getVersion());
                if (versionBD == null) {
                    version = versionDelegate.crearVersion(version);
                }

                plantilla.setVersion(version);
                plantilla = plantillaDelegate.obtenerPlantillaPorNombre(plantilla.getNombre());
                if (plantilla == null) {
                    plantilla.setId(null); // FIXME amartin 24/06/2015: esto produce fijo una NPE, ya que aquí plantilla == null.
                    plantilla = plantillaDelegate.crearPlantilla(plantilla);
                }

                Microsite site = micrositeDelegate.obtenerMicrosite(mic.getId());
                perPlantillaNueva.setMicrosite(site);
                perPlantillaNueva.setId(null);
                perPlantillaNueva.setPlantilla(plantilla);
                perPlantillaNueva = personalizacionPlantillaDelegate.crearPersonalizacionPlantilla(perPlantillaNueva);

//                tablamapeos.put(new String("ENC_" + enc.getId()), encnueva.getId());
                lista.add(perPlantillaNueva);
                stlog.append(perPlantillaNueva.getId() + " ");
            }

            mic.setPersonalizacionesPlantilla(lista);
            addImportLogVisual(request, "logimport.perPlantillas: " + stlog.toString());

        } catch (Exception ex) {
            addImportLogVisual(request, (String) rb.getObject("logimport.perPlantillas") + ": " + stlog.toString());
            mensaje += (String) rb.getObject("logimport.perPlantillas.error") + ": <br/>" + ex.toString() + "<br/>";
            addImportLogVisualStackTrace(request, mensaje, ex.getStackTrace());
        }
    }

	// Actualiza los objetos que tienen campo URL
	private void actualizaURLMicrositeCompleto(Hashtable mapa, MicrositeCompleto mic, HttpServletRequest request) {
		
		ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
        ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
		
		try {
			// Actualizo agendas
			Iterator<?> it;
			Set objs = mic.getAgendas();
			if (objs != null) {
				it = objs.iterator();
				while (it.hasNext()) {
					Agenda obj = (Agenda) it.next();
                    for (TraduccionAgenda trad : obj.getTraducciones().values()) {
                        if (trad.getDocumento() != null) {
                            trad.setDocumento(archivoDelegate.obtenerArchivo(trad.getDocumento().getId()));
                            trad.getDocumento().setDatos(archivoDelegate.obtenerContenidoFichero(trad.getDocumento()));
                        }
                        if (trad.getImagen() != null) {
                            trad.setImagen(archivoDelegate.obtenerArchivo(trad.getImagen().getId()));
                            trad.getImagen().setDatos(archivoDelegate.obtenerContenidoFichero(trad.getImagen()));
                        }
                    }
					obj = (Agenda) actualizaURL(obj, mic.getId(), request);
					DelegateUtil.getAgendaDelegate().grabarAgenda(obj);
				}
			}
			
			// Actualizo contenidos de los menus
			Set menus = mic.getMenus();
			if (menus != null) {
				Iterator<?> itmenu = menus.iterator();
				while (itmenu.hasNext()) {
					Menu menu = (Menu) itmenu.next();
                    if (menu.getImagenmenu() != null) {
                        menu.setImagenmenu(archivoDelegate.obtenerArchivo(menu.getImagenmenu().getId()));
                        menu.getImagenmenu().setDatos(archivoDelegate.obtenerContenidoFichero(menu.getImagenmenu()));
                    }
					List conts = menu.getContenidos();
					if (conts != null) {
						it = conts.iterator();
						while (it.hasNext()) {
							Contenido obj = (Contenido) it.next();
                            if (obj.getImagenmenu() != null) {
                                obj.setImagenmenu(archivoDelegate.obtenerArchivo(obj.getImagenmenu().getId()));
                                obj.getImagenmenu().setDatos(archivoDelegate.obtenerContenidoFichero(obj.getImagenmenu()));
                            }
							obj = (Contenido) actualizaURL(obj, mic.getId(), request);
							DelegateUtil.getContenidoDelegate().grabarContenido(obj);
						}
					}
				}
			}

			// Actualizo faqs
			objs = mic.getFaqs();
			if (objs != null) {
				it = objs.iterator();
				while (it.hasNext()) {
					Faq obj = (Faq) it.next();
					obj = (Faq) actualizaURL(obj, mic.getId(), request);
					DelegateUtil.getFaqDelegate().grabarFaq(obj);
				}
			}

			// Actualizo Noticias
			objs = mic.getNoticias();
			if (objs != null) {
				it = objs.iterator();
				while (it.hasNext()) {
					Noticia obj = (Noticia) it.next();
                    if (obj.getImagen() != null) {
                        obj.setImagen(archivoDelegate.obtenerArchivo(obj.getImagen().getId()));
                        obj.getImagen().setDatos(archivoDelegate.obtenerContenidoFichero(obj.getImagen()));
                    }
                    for (TraduccionNoticia trad : obj.getTraducciones().values()) {
                        if (trad.getDocu() != null) {
                            trad.setDocu(archivoDelegate.obtenerArchivo(trad.getDocu().getId()));
                            trad.getDocu().setDatos(archivoDelegate.obtenerContenidoFichero(trad.getDocu()));
                        }
                    }
					obj = (Noticia) actualizaURL(obj, mic.getId(), request);
					DelegateUtil.getNoticiasDelegate().grabarNoticia(obj);
				}
			}

			// Actualizo Frqssis
			objs = mic.getFrqssis();
			if (objs != null) {
				it = objs.iterator();
				while (it.hasNext()) {
					Frqssi obj = (Frqssi) it.next();
					obj = (Frqssi) actualizaURL(obj, mic.getId(), request);
					DelegateUtil.getFrqssiDelegate().grabarFrqssi(obj);
				}
			}

			// Actualizo Documentos / Archivos
			objs = mic.getDocus();
			if (objs != null) {

				it = objs.iterator();
				
				while (it.hasNext()) {
					
					ArchivoLite obj = (ArchivoLite) it.next();
                    Archivo arc = archivoDelegate.obtenerArchivo(obj.getId());
					arc = (Archivo) actualizaURL(arc, mic.getId(), request);
					arc.setDatos(archivoDelegate.obtenerContenidoFichero(arc));
					archivoDelegate.grabarArchivo(arc);
					
				}
				
			}

			// Actualizo el microsite solo
			MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
			Microsite mic2 = bdMicro.obtenerMicrosite(mic.getId());
            if (mic2.getImagenPrincipal() != null) {
                mic2.setImagenPrincipal(archivoDelegate.obtenerArchivo(mic2.getImagenPrincipal().getId()));
                mic2.getImagenPrincipal().setDatos(archivoDelegate.obtenerContenidoFichero(mic2.getImagenPrincipal()));
            }
            if (mic2.getImagenCampanya() != null) {
                mic2.setImagenCampanya(archivoDelegate.obtenerArchivo(mic2.getImagenCampanya().getId()));
                mic2.getImagenCampanya().setDatos(archivoDelegate.obtenerContenidoFichero(mic2.getImagenCampanya()));
            }
            if (mic2.getEstiloCSS() != null) {
                mic2.setEstiloCSS(archivoDelegate.obtenerArchivo(mic2.getEstiloCSS().getId()));
                mic2.getEstiloCSS().setDatos(archivoDelegate.obtenerContenidoFichero(mic2.getEstiloCSS()));
            }
			mic2 = (Microsite) actualizaURL(mic2, mic.getId(), request);
			bdMicro.grabarMicrosite(mic2);

		} catch (Exception ex) {
			mensaje += (String) rb.getObject("logimport.urls.error") + ": <br/>" + ex.toString() + "<br/>";
			addImportLogVisualStackTrace(request, "[WARN] " + mensaje, ex.getStackTrace());
		}
	}
		
	private void calculaHshURLs(Long idsitenew) {

		// TODO El día que se utilizen keys significativas para identificar
		// objetos, este tinglao que hay aquí no
		// no tendrá sentido (ya no hará falta). De todas formas, habrá que
		// pensar en los microsites ya existentes.

		Hashtable<String, String> hshmapeoimagenes = new Hashtable<String, String>();

		String oldurl = "";
		String newurl = "";
		
		Enumeration<String> enumera = tablamapeos.keys();
		while (enumera.hasMoreElements()) {
			
			String clave = (String) enumera.nextElement();
			String newidcont = "" + ((Long) tablamapeos.get(clave)).longValue();
			String oldidcont = clave.substring(4, clave.length());

			if (clave.indexOf("NOT_") != -1) {
				oldurl = "noticia.do?idsite=" + idmicroant.longValue() + "&cont=" + oldidcont;
				newurl = "noticia.do?idsite=" + idsitenew.longValue() + "&cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "noticia.do?idsite=" + idmicroant.longValue() + "&cont=" + oldidcont;
				newurl = "noticia.do?idsite=" + idsitenew.longValue() + "&cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "noticia.do?cont=" + oldidcont + "&idsite=" + idmicroant.longValue();
				newurl = "noticia.do?cont=" + newidcont + "&idsite=" + idsitenew.longValue();
				hshURLs.put(oldurl, newurl);
				oldurl = "archivopub.do?ctrl=NTCS0" + oldidcont;
				newurl = "archivopub.do?ctrl=NTCS0" + newidcont;
				hshmapeoimagenes.put(oldurl, newurl);
			}

			if (clave.indexOf("TPN_") != -1) {
				oldurl = "noticias.do?idsite=" + idmicroant.longValue() + "&tipo=" + oldidcont;
				newurl = "noticias.do?idsite=" + idsitenew.longValue() + "&tipo=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "noticias.do?idsite=" + idmicroant.longValue() + "&tipo=" + oldidcont;
				newurl = "noticias.do?idsite=" + idsitenew.longValue() + "&tipo=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "noticias.do?tipo=" + oldidcont + "&idsite=" + idmicroant.longValue();
				newurl = "noticias.do?tipo=" + newidcont + "&idsite=" + idsitenew.longValue();
				hshURLs.put(oldurl, newurl);

				// ahora las de los tags dummys de contenido
				oldurl = "propertyid=\"" + oldidcont + "\"";
				newurl = "propertyid=\"" + newidcont + "\"";
				hshURLs.put(oldurl, newurl);
				oldurl = "propertyID=\"" + oldidcont + "\"";
				newurl = "propertyID=\"" + newidcont + "\"";
				hshURLs.put(oldurl, newurl);
			}

			if (clave.indexOf("COM_") != -1) {
				oldurl = "componenteid=\"" + oldidcont + "\"";
				newurl = "componenteid=\"" + newidcont + "\"";
				hshURLs.put(oldurl, newurl);
				oldurl = "componenteID=\"" + oldidcont + "\"";
				newurl = "componenteID=\"" + newidcont + "\"";
				hshURLs.put(oldurl, newurl);
			}

			if (clave.indexOf("ENC_") != -1) {
				oldurl = "encuesta.do?idsite=" + idmicroant.longValue() + "&cont=" + oldidcont;
				newurl = "encuesta.do?idsite=" + idsitenew.longValue() + "&cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "encuesta.do?idsite=" + idmicroant.longValue() + "&cont=" + oldidcont;
				newurl = "encuesta.do?idsite=" + idsitenew.longValue() + "&cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "encuesta.do?cont=" + oldidcont + "&idsite=" + idmicroant.longValue();
				newurl = "encuesta.do?cont=" + newidcont + "&idsite=" + idsitenew.longValue();
				hshURLs.put(oldurl, newurl);

				// ahora las de los tags dummys de encuesta para el contenido
				oldurl = "componenteid=\"" + oldidcont + "\"";
				newurl = "componenteid=\"" + newidcont + "\"";
				hshURLs.put(oldurl, newurl);
				oldurl = "componenteID=\"" + oldidcont + "\"";
				newurl = "componenteID=\"" + newidcont + "\"";
				hshURLs.put(oldurl, newurl);
			}

			if (clave.indexOf("FRM_") != -1) {
				oldurl = "contacto.do?idsite=" + idmicroant.longValue() + "&cont=" + oldidcont;
				newurl = "contacto.do?idsite=" + idsitenew.longValue() + "&cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "contacto.do?idsite=" + idmicroant.longValue() + "&cont=" + oldidcont;
				newurl = "contacto.do?idsite=" + idsitenew.longValue() + "&cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "contacto.do?cont=" + oldidcont + "&idsite=" + idmicroant.longValue();
				newurl = "contacto.do?cont=" + newidcont + "&idsite=" + idsitenew.longValue();
				hshURLs.put(oldurl, newurl);
			}

			if (clave.indexOf("FRQ_") != -1) {
				oldurl = "frqssi.do?idsite=" + idmicroant.longValue() + "&cont=" + oldidcont;
				newurl = "frqssi.do?idsite=" + idsitenew.longValue() + "&cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "frqssi.do?idsite=" + idmicroant.longValue() + "&cont=" + oldidcont;
				newurl = "frqssi.do?idsite=" + idsitenew.longValue() + "&cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "frqssi.do?cont=" + oldidcont + "&idsite=" + idmicroant.longValue();
				newurl = "frqssi.do?cont=" + newidcont + "&idsite=" + idsitenew.longValue();
				hshURLs.put(oldurl, newurl);
			}

			if (clave.indexOf("CON_") != -1) {
				oldurl = "contenido.do?idsite=" + idmicroant.longValue() + "&cont=" + oldidcont;
				newurl = "contenido.do?idsite=" + idsitenew.longValue() + "&cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "contenido.do?idsite=" + idmicroant.longValue() + "&cont=" + oldidcont;
				newurl = "contenido.do?idsite=" + idsitenew.longValue() + "&cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "contenido.do?cont=" + oldidcont + "&idsite=" + idmicroant.longValue();
				newurl = "contenido.do?cont=" + newidcont + "&idsite=" + idsitenew.longValue();
				hshURLs.put(oldurl, newurl);

				oldurl = "contenido.do?idsite=" + idmicroant.longValue() + "&amp;cont=" + oldidcont;
				newurl = "contenido.do?idsite=" + idsitenew.longValue() + "&amp;cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "contenido.do?idsite=" + idmicroant.longValue() + "&amp;cont=" + oldidcont;
				newurl = "contenido.do?idsite=" + idsitenew.longValue() + "&amp;cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "contenido.do?cont=" + oldidcont + "&amp;idsite=" + idmicroant.longValue();
				newurl = "contenido.do?cont=" + newidcont + "&amp;idsite=" + idsitenew.longValue();
				hshURLs.put(oldurl, newurl);

				oldurl = "archivopub.do?ctrl=CNTSP" + oldidcont;
				newurl = "archivopub.do?ctrl=CNTSP" + newidcont;
				hshmapeoimagenes.put(oldurl, newurl);
			}

			if (clave.indexOf("BAN_") != -1) {
				oldurl = "archivopub.do?ctrl=BNNR0" + oldidcont;
				newurl = "archivopub.do?ctrl=BNNR0" + newidcont;
				hshmapeoimagenes.put(oldurl, newurl);
			}

			/* FNRUIZ */
			// Los documento de la cabecera y pie del microsite ¿¿¿???
			if (clave.indexOf("MIC_") != -1) {
				oldurl = "contenido.do?idsite=" + idmicroant.longValue() + "&cont=" + oldidcont;
				newurl = "contenido.do?idsite=" + idsitenew.longValue() + "&cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "contenido.do?idsite=" + idmicroant.longValue() + "&cont=" + oldidcont;
				newurl = "contenido.do?idsite=" + idsitenew.longValue() + "&cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "contenido.do?cont=" + oldidcont + "&idsite=" + idmicroant.longValue();
				newurl = "contenido.do?cont=" + newidcont + "&idsite=" + idsitenew.longValue();
				hshURLs.put(oldurl, newurl);

				oldurl = "contenido.do?idsite=" + idmicroant.longValue() + "&amp;cont=" + oldidcont;
				newurl = "contenido.do?idsite=" + idsitenew.longValue() + "&amp;cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "contenido.do?idsite=" + idmicroant.longValue() + "&amp;cont=" + oldidcont;
				newurl = "contenido.do?idsite=" + idsitenew.longValue() + "&amp;cont=" + newidcont;
				hshURLs.put(oldurl, newurl);
				oldurl = "contenido.do?cont=" + oldidcont + "&amp;idsite=" + idmicroant.longValue();
				newurl = "contenido.do?cont=" + newidcont + "&amp;idsite=" + idsitenew.longValue();
				hshURLs.put(oldurl, newurl);

				oldurl = "archivopub.do?ctrl=MCRST" + oldidcont;
				newurl = "archivopub.do?ctrl=MCRST" + newidcont;
				hshmapeoimagenes.put(oldurl, newurl);
			}
			/* FNRUIZ */
		}

		// ahora se vuelve a recorrer y se cogen solo los documentos y se
		// concatenan a lo que ya habíamos montado temporalmente
		Enumeration<String> enumera2 = tablamapeos.keys();
		while (enumera2.hasMoreElements()) {
			
			String clave = (String) enumera2.nextElement();
			String newidcont = "" + ((Long) tablamapeos.get(clave)).longValue();
			String oldidcont = clave.substring(4, clave.length());

			if (clave.indexOf("DCM_") != -1) {
				Enumeration<String> enumera3 = hshmapeoimagenes.keys();
				while (enumera3.hasMoreElements()) {
					String clave3 = (String) enumera3.nextElement();
					String newidcont3 = (String) hshmapeoimagenes.get(clave3);

					oldurl = clave3 + "ZI" + oldidcont + "&amp;id=" + oldidcont;
					newurl = newidcont3 + "ZI" + newidcont + "&amp;id=" + newidcont;
					hshURLs.put(oldurl, newurl);
					oldurl = clave3 + "ZI" + oldidcont + "&amp;id=" + oldidcont;
					newurl = newidcont3 + "ZI" + newidcont + "&amp;id=" + newidcont;
					hshURLs.put(oldurl, newurl);
				}
			}
		}

		// ahora las que son fijas en cada servicio
		oldurl = "home.do?idsite=" + idmicroant.longValue();
		newurl = "home.do?idsite=" + idsitenew.longValue();
		hshURLs.put(oldurl, newurl);

		oldurl = "faqs.do?idsite=" + idmicroant.longValue();
		newurl = "faqs.do?idsite=" + idsitenew.longValue();
		hshURLs.put(oldurl, newurl);

		oldurl = "agendas.do?idsite=" + idmicroant.longValue();
		newurl = "agendas.do?idsite=" + idsitenew.longValue();
		hshURLs.put(oldurl, newurl);

		oldurl = "contactos.do?idsite=" + idmicroant.longValue();
		newurl = "contactos.do?idsite=" + idsitenew.longValue();
		hshURLs.put(oldurl, newurl);

		oldurl = "mapa.do?idsite=" + idmicroant.longValue();
		newurl = "mapa.do?idsite=" + idsitenew.longValue();
		hshURLs.put(oldurl, newurl);

	}
		 
	private Object actualizaURL(Object objeto, Long idsitenew, HttpServletRequest request) {

		Object retorno = null;
		retorno = objeto;

		if (objeto instanceof Noticia) {
			Noticia noti = (Noticia)objeto;
			Map<?, ?> mapatraducciones = (Map<?, ?>)noti.getTraduccionMap();
			Iterator<?> iter = mapatraducciones.values().iterator();
			while (iter.hasNext()) {
				TraduccionNoticia tranot = (TraduccionNoticia)iter.next();
				if (tranot != null) {
					tranot.setLaurl(reemplazaURL(tranot.getLaurl(), "" + idsitenew.longValue(), request));
					tranot.setTexto(reemplazaContenido(tranot.getTexto(), "" + idmicroant.longValue(), "" + idsitenew.longValue(), request));
				}
			}
			return noti;
		}

		/*
		 * if (objeto instanceof Frqssi) { Frqssi frq = (Frqssi)objeto; Map
		 * mapatraducciones = (Map)frq.getTraduccionMap(); Iterator iter =
		 * mapatraducciones.values().iterator(); while (iter.hasNext()) {
		 * TraduccionFrqssi trafrq = (TraduccionFrqssi)iter.next(); if
		 * (trafrq!=null) {
		 * trafrq.setNombre(reemplazaContenido(trafrq.getNombre(), ""
		 * +idmicroant.longValue(), "" + idsitenew.longValue())); } } return
		 * frq; }
		 */

		if (objeto instanceof Agenda) {
			Agenda agen = (Agenda) objeto;
			Map<?, ?> mapatraducciones = (Map<?, ?>) agen.getTraduccionMap();
			Iterator<?> iter = mapatraducciones.values().iterator();
			while (iter.hasNext()) {
				TraduccionAgenda traagen = (TraduccionAgenda) iter.next();
				if (traagen != null && traagen.getUrl() != null) {
					traagen.setUrl(reemplazaURL(traagen.getUrl(), "" + idsitenew.longValue(), request));
				}
			}

			return agen;
		}

		if (objeto instanceof Faq) {
			Faq faq = (Faq)objeto;
			Map<?, ?> mapatraducciones = (Map<?, ?>) faq.getTraduccionMap();
			Iterator<?> iter = mapatraducciones.values().iterator();
			while (iter.hasNext()) {
				TraduccionFaq trafaq = (TraduccionFaq) iter.next();
				if (trafaq != null && trafaq != null) {
					trafaq.setUrl(reemplazaURL(trafaq.getUrl(), "" + idsitenew.longValue(), request));
				}
			}

			return faq;
		}

		if (objeto instanceof Contenido) {
			Contenido conte = (Contenido) objeto;
			Map<?, ?> mapatraducciones = (Map<?, ?>) conte.getTraduccionMap();
			Iterator<?> iter = mapatraducciones.values().iterator();
			while (iter.hasNext()) {
				TraduccionContenido traconte = (TraduccionContenido) iter.next();
				if (traconte != null) {
                    if (traconte.getUrl() != null) {
                        traconte.setUrl(reemplazaURL(traconte.getUrl(), "" + idsitenew.longValue(), request));
                    }
                    if (traconte.getTexto() != null) {
                        traconte.setTexto(reemplazaContenido(traconte.getTexto(), "" + idmicroant.longValue(), "" + idsitenew.longValue(), request));
                    }
				}
			}
		}

		if (objeto instanceof Archivo) {
			Archivo archi = (Archivo) objeto;
			Map<?, ?> mapatraducciones = (Map<?, ?>) archi.getTraduccionMap();
			Iterator<?> iter = mapatraducciones.values().iterator();
			while (iter.hasNext()) {
				TraduccionArchivo traarchi = (TraduccionArchivo) iter.next();
				if (traarchi != null) {
                    if (traarchi.getUrl() != null) {
                        traarchi.setUrl(reemplazaURL(traarchi.getUrl(), "" + idsitenew.longValue(), request));
                    }
                    if (traarchi.getTexto() != null) {
                        traarchi.setTexto(reemplazaContenido(traarchi.getTexto(), "" + idmicroant.longValue(), "" + idsitenew.longValue(), request));
                    }
				}
			}
		}

		if (objeto instanceof Microsite) {
			Microsite micro = (Microsite) objeto;

			micro.setUrlcampanya(reemplazaURL(micro.getUrlcampanya(), "" + idsitenew.longValue(), request));
			micro.setUrlhome(reemplazaURL(micro.getUrlhome(), "" + idsitenew.longValue(), request));

			Map<?, ?> mapatraducciones = (Map<?, ?>)micro.getTraduccionMap();
			Iterator<?> iter = mapatraducciones.values().iterator();
			while (iter.hasNext()) {
				TraduccionMicrosite tramicro = (TraduccionMicrosite)iter.next();
				if (tramicro != null) {
					tramicro.setCabecerapersonal(reemplazaContenido(tramicro.getCabecerapersonal(), "" + idmicroant.longValue(), "" + idsitenew.longValue(), request));
					tramicro.setPiepersonal(reemplazaContenido(tramicro.getPiepersonal(), "" + idmicroant.longValue(), "" + idsitenew.longValue(), request));
					tramicro.setUrlop1(reemplazaURL(tramicro.getUrlop1(), "" + idsitenew.longValue(), request));
					tramicro.setUrlop2(reemplazaURL(tramicro.getUrlop2(), "" + idsitenew.longValue(), request));
					tramicro.setUrlop3(reemplazaURL(tramicro.getUrlop3(), "" + idsitenew.longValue(), request));
					tramicro.setUrlop4(reemplazaURL(tramicro.getUrlop4(), "" + idsitenew.longValue(), request));
					tramicro.setUrlop5(reemplazaURL(tramicro.getUrlop5(), "" + idsitenew.longValue(), request));
					tramicro.setUrlop6(reemplazaURL(tramicro.getUrlop6(), "" + idsitenew.longValue(), request));
					tramicro.setUrlop7(reemplazaURL(tramicro.getUrlop7(), "" + idsitenew.longValue(), request));
				}
			}

			return micro;
		}

		return retorno;
	}
		 
	private String reemplazaContenido(String cadenacontenido, String idsiteold, String idsitenew, HttpServletRequest request) {
		
		String retornocontenido = "";
		retornocontenido = cadenacontenido;
		
		if (cadenacontenido != null) {
			
			StringBuffer stbuf = new StringBuffer(cadenacontenido);
			Enumeration<String> enumera = hshURLs.keys();
			while (enumera.hasMoreElements()) {
				String oldstringtmp = (String)enumera.nextElement();
				String newstringtmp = (String)hshURLs.get(oldstringtmp);

				// retornocontenido = retornocontenido.replaceAll(oldstringtmp,
				// newstringtmp);
				int pos = stbuf.indexOf(oldstringtmp);
				while (pos > -1) {
					stbuf.replace(pos, pos + oldstringtmp.length(), newstringtmp);
					pos = stbuf.toString().indexOf(oldstringtmp, pos + newstringtmp.length());
				}
			}
			
			retornocontenido = stbuf.toString();
		}
		
		return retornocontenido;
	}
		 
	private String reemplazaURL(String cadenaurl, String idsitenew, HttpServletRequest request) {
		
		String retornourl = "";
		retornourl = cadenaurl;
		
		if (retornourl != null) {
			// noticia.do?idsite=234&cont=32
			if (retornourl.indexOf("noticia.do") != -1) {
				retornourl = reemplazaParametrosURL1(retornourl, "idsite", "cont", "NOT_", idsitenew, request);
			}

			// frqssi.do?idsite=24&cont=15
			if (retornourl.indexOf("frqssi.do") != -1) {
				retornourl = reemplazaParametrosURL1(retornourl, "idsite", "cont", "FRQ_", idsitenew, request);
			}

			// noticias.do?idsite=24&tipo=7
			if (retornourl.indexOf("noticias.do") != -1) {
				retornourl = reemplazaParametrosURL1(retornourl, "idsite", "tipo", "TPN_", idsitenew, request);
			}

			// contacto.do?idsite=24&cont=15
			if (retornourl.indexOf("contacto.do") != -1) {
				retornourl = reemplazaParametrosURL1(retornourl, "idsite", "cont", "FRM_", idsitenew, request);
			}

			// contenido.do?idsite=24&cont=65
			if (retornourl.indexOf("contenido.do") != -1) {
				retornourl = reemplazaParametrosURL1(retornourl, "idsite", "cont", "CON_", idsitenew, request);
			}

			// encuesta.do?idsite=24&cont=65
			if (retornourl.indexOf("encuesta.do") != -1) {
				retornourl = reemplazaParametrosURL1(retornourl, "idsite", "cont", "ENC_", idsitenew, request);
			}

			// home.do?idsite=24
			if (retornourl.indexOf("home.do") != -1) {
				retornourl = reemplazaParametrosURL2(retornourl, "idsite", idsitenew, request);
			}

			// mapa.do?idsite=24
			if (retornourl.indexOf("mapa.do") != -1) {
				retornourl = reemplazaParametrosURL2(retornourl, "idsite", idsitenew, request);
			}

			// agendas.do?idsite=24
			if (retornourl.indexOf("agendas.do") != -1) {
				retornourl = reemplazaParametrosURL2(retornourl, "idsite", idsitenew, request);
			}

			// faqs.do?idsite=24
			if (retornourl.indexOf("faqs.do") != -1) {
				retornourl = reemplazaParametrosURL2(retornourl, "idsite", idsitenew, request);
			}

			// contactos.do?idsite=24
			if (retornourl.indexOf("contactos.do") != -1) {
				retornourl = reemplazaParametrosURL2(retornourl, "idsite", idsitenew, request);
			}

			// archivopub.do?ctrl=CNTSP234ZI207&id=207
			if (retornourl.indexOf("archivopub.do") != -1) {
				retornourl = reemplazaParametrosURL3(retornourl, "ctrl", "id", "DCM_", request);
			}
		}
		
		return retornourl;
	}
		 
	private String reemplazaParametrosURL1(String cadena, String nomparamIdsite, String nomparam2, String tipomapeo, 
			String idsitenew, HttpServletRequest request) {
		
		// reemplazar 2 parametros
		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		String retornourl = "";
		retornourl = cadena;
		
		try {
			String txt_param_key = "";
			String txt_param_value = "";

			String str[] = null;
			String strparam[] = null;

			StringTokenizer st = new StringTokenizer(retornourl, "?");
			int n = st.countTokens();
			if (n == 2) {
				
				// uri más parametros
				str = new String[n];
				for (int i = 0; i < n; i++) {
					str[i] = st.nextToken();
				}
				
				if (str[1].length() > 12) {
					StringTokenizer stparam = new StringTokenizer(str[1], "&");
					int z = stparam.countTokens();
					strparam = new String[z];
					for (int i = 0; i < z; i++) {
						strparam[i] = stparam.nextToken();
						StringTokenizer stkeyvalue = new StringTokenizer(strparam[i], "=");
						int y = stkeyvalue.countTokens();
						if (y > 1) {
							txt_param_key = stkeyvalue.nextToken();
							txt_param_value = stkeyvalue.nextToken();
							if (txt_param_key.equals(nomparamIdsite)) {
								retornourl = retornourl.replaceAll(nomparamIdsite + "=" + txt_param_value, nomparamIdsite + "=" + idsitenew);
							}
							if (txt_param_key.equals(nomparam2)) {
								if (tablamapeos.get(tipomapeo + txt_param_value) != null) {
									String newid = "" + ((Long)tablamapeos.get(tipomapeo + txt_param_value)).longValue();
									retornourl = retornourl.replaceAll(nomparam2 + "=" + txt_param_value, nomparam2 + "=" + newid);
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error((String)rb.getObject("logimport.url1.error") + ": " + e.getMessage());
			addImportLogVisualStackTrace(request, "[WARN] " + (String)rb.getObject("logimport.url1.error") + ": " + e.getMessage(), e.getStackTrace());
		}
		
		return retornourl;
	}

	private String reemplazaParametrosURL2(String cadena, String nomparam1, String idsitenew, HttpServletRequest request) {
		
		// reemplazar 1 parametro, y sin tipo de mapeo
		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		String retornourl = "";
		retornourl = cadena;
		
		try {
			String txt_param_key = "";
			String txt_param_value = "";

			String str[] = null;
			String strparam[] = null;

			StringTokenizer st = new StringTokenizer(retornourl, "?");
			int n = st.countTokens();
			if (n == 2) {
				// uri más parametros
				str = new String[n];
				for (int i = 0; i < n; i++) {
					str[i] = st.nextToken();
				}
				if (str[1].length() > 5) {
					StringTokenizer stparam = new StringTokenizer(str[1], "&");
					int z = stparam.countTokens();
					strparam = new String[z];
					for (int i = 0; i < z; i++) {
						strparam[i] = stparam.nextToken();
						StringTokenizer stkeyvalue = new StringTokenizer(strparam[i], "=");
						int y = stkeyvalue.countTokens();
						if (y > 0) {
							txt_param_key = stkeyvalue.nextToken();
							txt_param_value = stkeyvalue.nextToken();
							if (txt_param_key.equals(nomparam1)) {
								retornourl = retornourl.replaceAll(nomparam1 + "=" + txt_param_value, nomparam1 + "=" + idsitenew);
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			log.error((String)rb.getObject("logimport.url2.error") + ": " + e.getMessage());
			addImportLogVisualStackTrace(request, "[WARN] " + (String)rb.getObject("logimport.url2.error") + ": " + e.getMessage(), e.getStackTrace());
		}

		return retornourl;
	}
		 
	private String reemplazaParametrosURL3(String cadena, String nomparamCtrl, String nomparam2, String tipomapeo, HttpServletRequest request) {
		
		// reemplazar 2 parametros de archivos de imagen
		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		String retornourl = "";
		retornourl = cadena;
		
		try {
			String txt_param_key = "";
			String txt_param_value = "";

			String str[] = null;
			String strparam[] = null;

			StringTokenizer st = new StringTokenizer(retornourl, "?");
			int n = st.countTokens();
			if (n == 2) {
				// uri más parametros
				str = new String[n];
				for (int i = 0; i < n; i++) {
					str[i] = st.nextToken();
				}
				if (str[1].length() > 15) {
					StringTokenizer stparam = new StringTokenizer(str[1], "&");
					int z = stparam.countTokens();
					strparam = new String[z];
					for (int i = 0; i < z; i++) {
						strparam[i] = stparam.nextToken();
						StringTokenizer stkeyvalue = new StringTokenizer(strparam[i], "=");
						int y = stkeyvalue.countTokens();
						if (y == 2) {
							txt_param_key = stkeyvalue.nextToken();
							txt_param_value = stkeyvalue.nextToken();

							if (txt_param_key.equals(nomparamCtrl)) {
								// aquí hay que averiguar ademas el id del
								// servicio y el tipo de servicio
								String tiposervicio = txt_param_value.substring(0, 5);
								int pos_zi = txt_param_value.indexOf("ZI");
								String oldidservicio = txt_param_value.substring(5, pos_zi);
								String oldiditem = txt_param_value.substring(pos_zi + 2, txt_param_value.length());

								// String newiditem =
								// (String)tablamapeos.get(tipomapeo +
								// oldiditem);
								String newiditem = "" + ((Long)tablamapeos.get(tipomapeo + oldiditem)).longValue();

								String newidservicio = "";
								if (tiposervicio.equals("BNNR0"))
									newidservicio = "" + ((Long)tablamapeos.get("BAN_" + oldidservicio)).longValue();
								if (tiposervicio.equals("CNTSP"))
									newidservicio = "" + ((Long)tablamapeos.get("CON_" + oldidservicio)).longValue();
								if (tiposervicio.equals("NTCS0"))
									newidservicio = "" + ((Long)tablamapeos.get("NOT_" + oldidservicio)).longValue();
								if (tiposervicio.equals("FQSSI"))
									newidservicio = "" + ((Long)tablamapeos.get("FRQ_" + oldidservicio)).longValue();
								if (tiposervicio.equals("MCRST"))
									newidservicio = "" + ((Long)tablamapeos.get("MIC_" + oldidservicio)).longValue();

								retornourl = retornourl.replaceAll(nomparamCtrl + "=" + tiposervicio + oldidservicio + "ZI" + oldiditem, nomparamCtrl + "=" + tiposervicio + newidservicio + "ZI" + newiditem);
							}

							if (txt_param_key.equals(nomparam2)) {
								if (tablamapeos.get(tipomapeo + txt_param_value) != null) {
									String newid = "" + ((Long)tablamapeos.get(tipomapeo + txt_param_value)).longValue();
									retornourl = retornourl.replaceAll(nomparam2 + "=" + txt_param_value, nomparam2 + "=" + newid);
								}
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			log.error((String)rb.getObject("logimport.url3.error") + ": " + e.getMessage());
			addImportLogVisualStackTrace(request, "[WARN] " + (String)rb.getObject("logimport.url3.error") + ": " + e.getMessage(), e.getStackTrace());
		}

		return retornourl;
	}

	private void addImportLog(String mensaje) {
		MicroLog.addLog("{i" + _hashcode + "} " + mensaje);
	}

	@SuppressWarnings("unused")
	private void addImportLogStackTrace(String mensaje, StackTraceElement[] mensajes) {
		MicroLog.addLogStackTrace("{i" + _hashcode + "} " + mensaje, mensajes);
	}

	private void addImportLogVisual(HttpServletRequest request, String mensaje) {
		MicroLog.addLogVisual(request, "{i" + _hashcode + "} " + mensaje);
	}

	private void addImportLogVisualStackTrace(HttpServletRequest request, String mensaje, StackTraceElement[] mensajes) {
		MicroLog.addLogVisualStackTrace(request, "{i" + _hashcode + "} " + mensaje, mensajes);
	}

    private Tipo generarNuevasUrisTipo(Tipo tipo, String site) {

        TipoDelegate tipoDelegate = DelegateUtil.getTipoDelegate();
        for (TraduccionTipo trad : tipo.getTraducciones().values()) {
            try {
                int count = 0;
                String newUri = trad.getUri();
                Tipo tipoUri = tipoDelegate.obtenerTipoDesdeUri(trad.getId().getCodigoIdioma(), newUri, site);
                while (tipoUri != null) {
                    newUri = trad.getUri() + "_" + count;
                    tipoUri = tipoDelegate.obtenerTipoDesdeUri(trad.getId().getCodigoIdioma(), newUri, site);
                    count++;
                }
                trad.setUri(newUri);

            } catch (DelegateException de) {
                trad = null;
            }
        }
        return tipo;
    }

    private Noticia generarNuevasUrisNoti(Noticia noticia) {

        NoticiaDelegate noticiaDelegate = DelegateUtil.getNoticiasDelegate();
        for (TraduccionNoticia trad : noticia.getTraducciones().values()) {
            try {
                int count = 0;
                String newUri = trad.getUri();
                Noticia noticiaUri = noticiaDelegate.obtenerNoticiaDesdeUri(trad.getId().getCodigoIdioma(), newUri, noticia.getIdmicrosite().toString());
                while (noticiaUri != null) {
                    newUri = trad.getUri() + "_" + count;
                    noticiaUri = noticiaDelegate.obtenerNoticiaDesdeUri(trad.getId().getCodigoIdioma(), newUri, noticia.getIdmicrosite().toString());
                    count++;
                }
                trad.setUri(newUri);

            } catch (DelegateException de) {
                trad = null;
            }
        }
        return noticia;
    }

    private Contenido generarNuevasUrisCont(Contenido contenido) {

        ContenidoDelegate contenidoDelegate = DelegateUtil.getContenidoDelegate();
        for (TraduccionContenido trad : contenido.getTraducciones().values()) {
            try {
                int count = 0;
                String newUri = trad.getUri();
                Contenido contenidoUri = contenidoDelegate.obtenerContenidoDesdeUri(trad.getId().getCodigoIdioma(), newUri, contenido.getMenu().getMicrosite().getId().toString());
                while (contenidoUri != null) {
                    newUri = trad.getUri() + "_" + count;
                    contenidoUri = contenidoDelegate.obtenerContenidoDesdeUri(trad.getId().getCodigoIdioma(), newUri, contenido.getMenu().getMicrosite().getId().toString());
                    count++;
                }
                trad.setUri(newUri);

            } catch (DelegateException de) {
                trad = null;
            }
        }
        return contenido;
    }

    private Encuesta generarNuevasUrisEnc(Encuesta encuesta) {

        EncuestaDelegate encuestaDelegate = DelegateUtil.getEncuestaDelegate();
        for (TraduccionEncuesta trad : encuesta.getTraducciones().values()) {
            try {
                int count = 0;
                String newUri = trad.getUri();
                Encuesta encuestaUri = encuestaDelegate.obtenerEncuestaDesdeUri(trad.getId().getCodigoIdioma(), newUri, encuesta.getIdmicrosite().toString());
                while (encuestaUri != null) {
                    newUri = trad.getUri() + "_" + count;
                    encuestaUri = encuestaDelegate.obtenerEncuestaDesdeUri(trad.getId().getCodigoIdioma(), newUri, encuesta.getIdmicrosite().toString());
                    count++;
                }
                trad.setUri(newUri);

            } catch (DelegateException de) {
                trad = null;
            }
        }
        return encuesta;
    }

}
