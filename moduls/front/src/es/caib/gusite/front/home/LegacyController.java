package es.caib.gusite.front.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import es.caib.gusite.front.general.ExceptionFrontContenido;
import es.caib.gusite.front.general.ExceptionFrontEncuesta;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontNoticia;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.FrontController;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.service.ContactosDataService;
import es.caib.gusite.front.service.ContenidoDataService;
import es.caib.gusite.front.service.EncuestasDataService;
import es.caib.gusite.front.service.NoticiasDataService;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;

/**
 * Convierte peticiones antiguas a nuevas.
 * 
 * @author at4.net
 */
@Controller
public class LegacyController extends FrontController {

	private static Log log = LogFactory.getLog(LegacyController.class);

	@Autowired
	protected ContenidoDataService contenidoDataService;

	/**
	 * Hay mucho contenido referenciado relativo
	 * (src="archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868") Así que captamos
	 * toda la url **archivopub.do Con el tiempo, podemos estudiar los logs para
	 * sustituir estos requests.
	 * 
	 */
	@RequestMapping(value = "/{mkey}/{lang:[a-zA-Z][a-zA-Z]}/{tipo}/{tipoId}/contenido.do", params = Microfront.PIDSITE)
	public String contenidoRelativo(@PathVariable("mkey") SiteId prevSiteId, @PathVariable(value=Microfront.PLANG) Idioma lang, @PathVariable("tipo") char tipo,
			@PathVariable("tipoId") String tipoId, @RequestParam(Microfront.PIDSITE) Long idSite,
			@RequestParam(Microfront.PCONT) Long idContenido, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") String tipobeta,
			@RequestParam(value = Microfront.PVISUALIZAR, required = false, defaultValue = "") String previsual,
			HttpServletResponse response) {

		return this.contenido(idSite, lang, idContenido, model, pcampa, null,tipobeta, previsual, response);
	}

	/**
	 * Urls de contenido antiguo.
	 * contenido.do?idsite=449&cont=15812&lang=ca&campa=yes z
	 */
	@RequestMapping(value = "contenido.do", params = { Microfront.PIDSITE })
	public String contenido(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) Long idContenido, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") String tipobeta,
			@RequestParam(value = Microfront.PVISUALIZAR, required = false, defaultValue = "") String previsual,
			HttpServletResponse response){

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			Contenido contenido = this.contenidoDataService.getContenido(microsite, idContenido, lang.getLang());
			//#81 Si el contenido ha sido borrado, esto se encarga de redirigirlo.
			if (contenido == null) {
				throw new ExceptionFrontContenido(microsite.getUri(), idContenido);
			}

			return "redirect:" + addGenericParams(this.urlFactory.contenido(microsite, lang, contenido), pcampa, mcont, tipobeta, previsual);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (ExceptionFrontContenido e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
		
		
	}

	private String addGenericParams(String baseUri, String pcampa, String mcont) {		
	/*//se comenta por problemas en #106
	 * 	if(!StringUtils.isEmpty(pcampa) || !StringUtils.isEmpty(mcont) ){
			baseUri = this.urlFactory.borraUltimaBarra(baseUri);	
		}*/
		UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(baseUri);		
		if (!StringUtils.isEmpty(pcampa)) {
			uri.replaceQueryParam(Microfront.PCAMPA, pcampa);
		}
		if (!StringUtils.isEmpty(mcont)) {
			uri.replaceQueryParam(Microfront.MCONT, mcont);
		}
		return uri.build().toUriString();
	}
	
	private String addGenericParams(String baseUri, String pcampa, String mcont, String tipo, String previsual) {				
	/*	//se comenta por problemas en #106
	 * if(!StringUtils.isEmpty(pcampa) || !StringUtils.isEmpty(mcont) || !StringUtils.isEmpty(tipo) || !StringUtils.isEmpty(previsual) ){
			baseUri = this.urlFactory.borraUltimaBarra(baseUri);	
		}*/		
		UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(baseUri);		
		
		if (!StringUtils.isEmpty(pcampa)) {
			uri.replaceQueryParam(Microfront.PCAMPA, pcampa);
		}
		if (!StringUtils.isEmpty(mcont)) {
			uri.replaceQueryParam(Microfront.MCONT, mcont);
		}
		if (!StringUtils.isEmpty(tipo)) {
			uri.replaceQueryParam(Microfront.PTIPO, tipo);
		}
		if (!StringUtils.isEmpty(previsual)) {
			uri.replaceQueryParam(Microfront.PVISUALIZAR, previsual);
		}
		return uri.build().toUriString();
	}


	/**
	 * Hay mucho contenido referenciado relativo
	 * (src="archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868") Así que captamos
	 * toda la url **archivopub.do Con el tiempo, podemos estudiar los logs para
	 * sustituir estos requests.
	 * 
	 */
	@RequestMapping(value = "/{mkey}/{lang:[a-zA-Z][a-zA-Z]}/{tipo}/{tipoId}/contenido.do", params = "mkey")
	public String contenidoRelativo(@PathVariable("mkey") SiteId prevSiteId, @PathVariable(value=Microfront.PLANG) Idioma lang, @PathVariable("tipo") char tipo,
			@PathVariable("tipoId") String tipoId, @RequestParam("mkey") String mkey,
			@RequestParam(Microfront.PCONT) Long idContenido, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") String tipobeta,
			@RequestParam(value = Microfront.PVISUALIZAR, required = false, defaultValue = "") String previsual,
			HttpServletResponse response) {

		return this.contenido(mkey, lang, idContenido, model, pcampa, null,tipobeta,previsual, response);
	}

	/**
	 * Urls de contenido antiguo.
	 * contenido.do?idsite=449&cont=15812&lang=ca&campa=yes z
	 */
	@RequestMapping(value = "contenido.do", params = "mkey")
	public String contenido(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) Long idContenido, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") String tipobeta,
			@RequestParam(value = Microfront.PVISUALIZAR, required = false, defaultValue = "") String previsual,
			HttpServletResponse response) {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			return this.contenido(microsite.getId(), lang, idContenido, model, pcampa, mcont, tipobeta, previsual, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}
	
	

	@Autowired
	protected ContactosDataService contactosDataService;

	/**
	 * Urls de contenido antiguo. contacto.do?mkey=M94&lang=ca z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "contacto.do", params = { Microfront.PIDSITE })
	public String contacto(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) Long idContacto, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			Contacto contacto = this.contactosDataService.getFormulario(microsite, lang, idContacto);

			return "redirect:" + addGenericParams(this.urlFactory.contacto(microsite, lang, contacto), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. contacto.do?mkey=M94&lang=es&cont=213 z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "contacto.do", params = "mkey")
	public String contacto(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) Long idContacto, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			return this.contacto(microsite.getId(), lang, idContacto, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. contactos.do?mkey=M94&lang=ca z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "contactos.do", params = { Microfront.PIDSITE })
	public String contactos(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			Model model, @RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
			}
			return "redirect:" + this.addGenericParams(this.urlFactory.listarContactos(microsite, lang), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. contactos.do?mkey=M94&lang=es&cont=213 z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "contactos.do", params = "mkey")
	public String contactos(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			return "redirect:" + this.addGenericParams(this.urlFactory.listarContactos(microsite, lang), pcampa, mcont);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	@Autowired
	protected NoticiasDataService noticiasDataService;

	/**
	 * Urls de contenido antiguo.
	 * noticia.do?mkey=M11050411433211050557&lang=ca&cont=31549 z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "noticia.do", params = { Microfront.PIDSITE })
	public String noticia(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) Long idNoticia, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			Noticia noticia = this.noticiasDataService.loadNoticia(idNoticia, lang);
			if (noticia == null) {
				if (idNoticia == null) {
					throw new ExceptionFrontNoticia();	
				}
				throw new ExceptionFrontNoticia(idSite.toString(), idNoticia.toString());		
			}
			
			return "redirect:" + this.addGenericParams(this.urlFactory.noticia(microsite, lang, noticia), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (ExceptionFrontNoticia e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
		
	}

	/**
	 * Urls de contenido antiguo.
	 * noticia.do?mkey=M11050411433211050557&lang=ca&cont=31549 z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "noticia.do", params = "mkey")
	public String noticia(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) Long idNoticia, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			
			return this.noticia(microsite.getId(), lang, idNoticia, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo.
	 * noticias.do?mkey=M10101513200930602688&lang=ca&tipo=3306 z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "noticias.do", params = { Microfront.PIDSITE })
	public String noticias(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PTIPO) Long idTipo, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;

		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			Tipo tipo = this.noticiasDataService.loadTipo(idTipo, lang);

			return "redirect:" + addGenericParams(this.urlFactory.listarElementos(microsite, lang, tipo), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo.
	 * noticias.do?mkey=M10101513200930602688&lang=ca&tipo=3306 z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "noticias.do", params = "mkey")
	public String noticias(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PTIPO) Long idTipo, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			return this.noticias(microsite.getId(), lang, idTipo, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo.
	 * noticias.do?mkey=M10101513200930602688&lang=ca&tipo=3306&tanyo=2010 z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "noticias.do", params = { "tanyo", Microfront.PIDSITE })
	public String noticiasAnyo(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PTIPO) Long idTipo, @RequestParam("tanyo") String anyo, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;

		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			Tipo tipo = this.noticiasDataService.loadTipo(idTipo, lang);

			return "redirect:" + this.addGenericParams(this.urlFactory.listarAnual(microsite, lang, tipo, anyo), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo.
	 * noticias.do?mkey=M10101513200930602688&lang=ca&cont=3306&tanyo=2010 z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "noticias.do", params = { "tanyo", "mkey" })
	public String noticiasAnyo(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PTIPO) Long idTipo, @RequestParam("tanyo") String anyo, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			return this.noticiasAnyo(microsite.getId(), lang, idTipo, anyo, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. qssi.do?[sitelang]&cont=formQssi z
	 * 
	 * @throws DelegateException
	 * @throws ExceptionFrontPagina
	 */
	@RequestMapping(value = "qssi.do", params = { Microfront.PIDSITE })
	public String qssi(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) long idQssi, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException,
			ExceptionFrontPagina {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			return "redirect:" + this.addGenericParams(this.urlFactory.qssi(microsite, lang, idQssi), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. qssi.do?[sitelang]&cont=formQssi z
	 * 
	 * @throws DelegateException
	 * @throws ExceptionFrontPagina
	 */
	@RequestMapping(value = "qssi.do", params = "mkey")
	public String qssi(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, @RequestParam(Microfront.PCONT) long idQssi,
			Model model, @RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException,
			ExceptionFrontPagina {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			return this.qssi(microsite.getId(), lang, idQssi, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo.
	 * elementodocumento.do?mkey=M60&cont=16707&lang=CA z
	 * 
	 * @throws DelegateException
	 * @throws ExceptionFrontPagina
	 */
	@RequestMapping(value = "elementodocumento.do", params = { Microfront.PIDSITE })
	public String elementodocumento(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) String idDocumento, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletResponse response) throws DelegateException,
			ExceptionFrontPagina {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			return "redirect:" + this.urlFactory.noticiaDescarga(microsite, lang, idDocumento);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo.
	 * elementodocumento.do?mkey=M60&cont=16707&lang=CA z
	 * 
	 * @throws DelegateException
	 * @throws ExceptionFrontPagina
	 */
	@RequestMapping(value = "elementodocumento.do", params = "mkey")
	public String elementodocumento(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) String idDocumento, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletResponse response) throws DelegateException,
			ExceptionFrontPagina {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			return this.elementodocumento(microsite.getId(), lang, idDocumento, model, pcampa, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. agenda.do?mkey=M7&lang=ca&cont=20131122 z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "agenda.do", params = { Microfront.PIDSITE })
	public String agenda(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) String idAgenda, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			return "redirect:" + this.addGenericParams(this.urlFactory.listarAgendaFechaFormateada(microsite, lang, idAgenda), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. agenda.do?mkey=M7&lang=ca&cont=20131122 z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "agenda.do", params = "mkey")
	public String agenda(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) String idAgenda, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			
			return this.agenda(microsite.getId(), lang, idAgenda, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. agendas.do?mkey=M09112511445915080171&lang=ca
	 * z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "agendas.do", params = { Microfront.PIDSITE })
	public String agendas(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			return "redirect:" + this.addGenericParams(this.urlFactory.listarAgenda(microsite, lang), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. agendas.do?mkey=M09112511445915080171&lang=ca
	 * 
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "agendas.do", params = "mkey")
	public String agendas(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			return this.agendas(microsite.getId(), lang, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	@Autowired
	protected EncuestasDataService encuestasDataService;

	
	
	/**
	 * *****************************************
	 * Urls de contenido antiguo. encuestas.do?idsite=5274
	 * @throws DelegateException
	 */
	@RequestMapping(value = "encuestas.do", params = { Microfront.PIDSITE })
	public String encuestas(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			Model model, @RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			return "redirect:" + this.addGenericParams(this.urlFactory.home(microsite, lang), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}
	
	//********************************************************
	
	/**
	 * Urls de contenido antiguo.
	 * encuesta.do?mkey=M11010412302126072344&lang=ca&cont=769 z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "encuesta.do", params = { Microfront.PIDSITE })
	public String encuesta(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) Long idEncuesta, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			Encuesta encuesta = this.encuestasDataService.getEncuesta(microsite, lang, idEncuesta);
			if (microsite == null) {
				if (idEncuesta == null) {
					throw new ExceptionFrontEncuesta();	
				}
				throw new ExceptionFrontEncuesta(idSite.toString(), idEncuesta.toString());				
			}
			
			return "redirect:" + this.addGenericParams(this.urlFactory.encuesta(microsite, lang, encuesta), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (ExceptionFrontEncuesta e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo.
	 * encuesta.do?mkey=M11010412302126072344&lang=ca&cont=769 z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "encuesta.do", params = "mkey")
	public String encuesta(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam(Microfront.PCONT) Long idEncuesta, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			
			return this.encuesta(microsite.getId(), lang, idEncuesta, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. index.do?mkey=M77&lang=es 
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "index.do", params = { Microfront.PIDSITE })
	public String index(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response)
			throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			return "redirect:" + this.addGenericParams(this.urlFactory.home(microsite, lang), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. index.do?mkey=M77&lang=es z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "index.do", params = "mkey")
	public String index(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			
			return this.index(microsite.getId(), lang, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. home.do?mkey=M77&lang=es z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "home.do", params = { Microfront.PIDSITE })
	public String home(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response)
			throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			return this.index(microsite.getId(), lang, model, pcampa, mcont, response);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. home.do?mkey=M77&lang=es z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "home.do", params = "mkey")
	public String home(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			
			return this.index(microsite.getId(), lang, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. mapa.do?idsite=1244&lang=CA z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "mapa.do", params = { Microfront.PIDSITE })
	public String mapa(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response)
			throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			return "redirect:" + this.addGenericParams(this.urlFactory.mapa(microsite, lang), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. mapa.do?idsite=1244&lang=CA z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "mapa.do", params = "mkey")
	public String mapa(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			
			return this.mapa(microsite.getId(), lang, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	
	/**
	 * Urls de contenido antiguo. menupreview.do?mkey=M12050713445419070774
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "menupreview.do", params = "mkey")
	public String menupreview(@RequestParam("mkey") String mkey, Model model, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, DEFAULT_IDIOMA);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			
			return "redirect:" + this.urlFactory.menuPreview(microsite, DEFAULT_IDIOMA);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}
	
		
	/**
	 * Urls de contenido antiguo. accessibilitat.do?idsite=1244&lang=CA z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "accessibilitat.do", params = { Microfront.PIDSITE })
	public String accessibilitat(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response)
			throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			return "redirect:" + this.addGenericParams(this.urlFactory.accessibilitat(microsite, lang), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. accessibilitat.do?idsite=1244&lang=CA z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "accessibilitat.do", params = "mkey")
	public String accessibilitat(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response)
			throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			
			
			return this.accessibilitat(microsite.getId(), lang, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. faqs.do?idsite=1244&lang=CA z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "faqs.do", params = { Microfront.PIDSITE })
	public String faqs(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response)
			throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			return "redirect:" + this.addGenericParams(this.urlFactory.listarFaqs(microsite, lang), pcampa, mcont);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Urls de contenido antiguo. faqs.do?idsite=1244&lang=CA z
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping(value = "faqs.do", params = "mkey")
	public String faqs(@RequestParam("mkey") String mkey, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang, Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont, HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);				
			}
			
			return this.faqs(microsite.getId(), lang, model, pcampa, mcont, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Hay mucho contenido referenciado relativo
	 * (src="archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868") Así que captamos
	 * toda la url **archivopub.do Con el tiempo, podemos estudiar los logs para
	 * sustituir estos requests.
	 * 
	 */
	@RequestMapping(value = "/{mkey}/{lang:[a-zA-Z][a-zA-Z]}/{tipo}/{tipoId}/archivopub.do", params = { Microfront.PCTRL, "id" })
	public String archivopubRelativo(@PathVariable("mkey") SiteId prevSiteId, @PathVariable(value=Microfront.PLANG) Idioma lang, @PathVariable("tipo") char tipo,
			@PathVariable("tipoId") String tipoId, @RequestParam(value = Microfront.PCTRL) String ctrl, @RequestParam(value = "id") Long id,
			Model model, HttpServletResponse response) {

		return this.archivopub(ctrl, id, model, response);

	}

	/**
	 * Urls de contenido antiguo.
	 * archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868
	 * 
	 */
	@RequestMapping(value = "archivopub.do", params = { Microfront.PCTRL, "id" })
	public String archivopub(@RequestParam(Microfront.PCTRL) String ctrl, @RequestParam("id") Long id, Model model, HttpServletResponse response) {

		Microsite microsite = null;
		try {

			Long idSite = this.idSiteFromCtrl(ctrl);

			microsite = this.dataService.getMicrosite(idSite, DEFAULT_IDIOMA);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			return "redirect:" + this.urlFactory.archivopubById(microsite, id);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 

	}

	/**
	 * Urls de contenido antiguo.
	 * archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868
	 * 
	 */
/*	@RequestMapping(value = "archivopub.do", params = { Microfront.PCTRL, "id", "nombre" })
	public String archivopub(@RequestParam(Microfront.PCTRL) String ctrl, @RequestParam("id") Long id, @RequestParam("nombre") String nombre, Model model, HttpServletResponse response, final HttpServletRequest req) {

		Microsite microsite = null;
		try {
			log.info("url de llamada al servicio: " + req.getRequestURI());

			Long idSite = this.idSiteFromCtrl(ctrl);

			microsite = this.dataService.getMicrosite(idSite, DEFAULT_IDIOMA);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			String url = this.urlFactory.archivopubByNombre(microsite, nombre);
			log.info("url de redireccion: " + url);
			return "redirect:" + url;

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 

	}
*/
	@RequestMapping(value = "archivopub.do", params = { "nombre", "nombresite" })
	public String archivopub(@RequestParam("nombre") String nombre, @RequestParam("nombresite") String nombresite, Model model, HttpServletResponse response) {
		String ctrlReal = "";
		Microsite microsite = null;
		try {
			nombresite = obtenNombreSite(nombresite);
			microsite = this.dataService.getMicrositeByUri(nombresite, DEFAULT_IDIOMA);
			if (microsite == null) {
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + nombresite);				
			}
			String url = this.urlFactory.archivopubByNombre(microsite, nombre);
			log.info("url de redireccion: " + url);
			return "redirect:" + url;
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}
	
	@RequestMapping(value = "archivopub.do", params = { Microfront.PCTRL, "id", "nombresite" })
	public String archivopub(@RequestParam(Microfront.PCTRL) String ctrl, @RequestParam("id") Long id, @RequestParam("nombresite") String nombresite, Model model, HttpServletResponse response) {
		String ctrlReal = "";
		Microsite microsite = null;
		try {
			nombresite = obtenNombreSite(nombresite);
			microsite = this.dataService.getMicrositeByUri(nombresite, DEFAULT_IDIOMA);
			if (microsite == null) {
				if (nombresite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + nombresite);				
			}
			ctrlReal = ctrlFromIdSite(microsite.getId(), id, ctrl.substring(0, 5));
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
		return archivopub(ctrlReal, id, model, response);
	}
	
	@RequestMapping(value = "archivopub.do", params = { Microfront.PCTRL, "id", "nombre", "nombresite" })
	public String archivopub(@RequestParam(Microfront.PCTRL) String ctrl, @RequestParam("id") Long id, @RequestParam("nombre") String nombre, @RequestParam("nombresite") String nombresite, Model model, HttpServletResponse response, final HttpServletRequest req) {
		return archivopub(nombre, nombresite, model, response);
	}

	@RequestMapping(value = "archivopub.do", params = { "nombre", "uri", "nombresite" })
	public String archivopub(@RequestParam("nombre") String nombre, @RequestParam("uri") String uri, @RequestParam("nombresite") String nombresite, Model model, HttpServletResponse response) {
		return archivopub(nombre, uri, model, response);
	}

	private Long idSiteFromCtrl(String ctrl) {

		String what = ctrl.substring(0, 5);
		Long whatKey = new Long(ctrl.substring(5, ctrl.indexOf(Microfront.separatordocs)));

		// TODO: extraer Delegates a DataService
		try {

			if (what.equals(Microfront.RAGENDA)) {
				AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
				Agenda agenda;
				agenda = agendadel.obtenerAgenda(whatKey);
				return agenda.getIdmicrosite();
			}

			if (what.equals(Microfront.RCONTENIDO)) {
				ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();
				Contenido contenido = contenidodel.obtenerContenido(whatKey);
				return contenido.getMenu().getMicrosite().getId();
			}

			if (what.equals(Microfront.RNOTICIA)) {
				NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
				Noticia noticia = noticiadel.obtenerNoticia(whatKey);
				return noticia.getIdmicrosite();
			}

			if (what.equals(Microfront.RMICROSITE)) {
				return whatKey;
			}
		} catch (DelegateException e) {
			log.error(e);
		}

		// No debería llegar aquí
		return new Long(0);
	}

	private String ctrlFromIdSite(Long idMicosite, Long id, String ctrl) {
		return ctrl+idMicosite+Microfront.separatordocs+id;
	}

	/**
	 * Urls de contenido antiguo.
	 * archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868
	 * 
	 */
	@RequestMapping(value = "archivopub.do", params = Microfront.PNAME)
	public String archivopubPorNombre(@RequestParam(Microfront.PNAME) String nombre, @RequestParam(value = Microfront.PIDSITE) Long idSite,
			Model model, HttpServletResponse response) {

		Microsite microsite = null;
		try {

			microsite = this.dataService.getMicrosite(idSite, DEFAULT_IDIOMA);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			return "redirect:" + this.urlFactory.archivopubByNombre(microsite, nombre);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 

	}

	/**
	 * Hay mucho contenido referenciado relativo
	 * (src="archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868") Así que captamos
	 * toda la url **archivopub.do Con el tiempo, podemos estudiar los logs para
	 * sustituir estos requests.
	 * 
	 */
	@RequestMapping(value = "/{mkey}/{lang:[a-zA-Z][a-zA-Z]}/{tipo}/{tipoId}/archivopub.do", params = Microfront.PNAME)
	public String archivopubRelativoPorNombre(@PathVariable("mkey") SiteId siteId, @PathVariable(value=Microfront.PLANG) Idioma lang,
			@PathVariable("tipo") char tipo, @PathVariable("tipoId") String tipoId, @RequestParam(value = Microfront.PNAME) String name, Model model, HttpServletResponse response) {
		return this.archivopubRelativoPorNombre(siteId, name, model, response);

	}

	/**
	 * Hay archivos referenciados relativos desde archivos css que se cargan
	 * como archivopub
	 */
	@RequestMapping(value = "/{uri}/f/archivopub.do", params = Microfront.PNAME)
	public String archivopubRelativoPorNombre(@PathVariable("uri") SiteId URI, @RequestParam(value = Microfront.PNAME) String name, Model model, HttpServletResponse response) {

		Microsite microsite = null;
		try {

			microsite = this.dataService.getMicrositeByUri(URI.uri, DEFAULT_IDIOMA);
			if (microsite == null) {
				if (URI == null || URI.uri == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI.uri);				
			}
			
			return "redirect:" + this.urlFactory.archivopubByNombre(microsite, name);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 

	}

	
	/**
	 * Urls de contenido antiguo.
	 * /taw.do?ttr=CNTSP&idioma=ca&id=24454&idsite=1364
	 * 
	 * @throws DelegateException
	 */
	@RequestMapping("taw.do")
	public String taw(@RequestParam(Microfront.PIDSITE) Long idSite, @RequestParam(value=Microfront.PLANG, defaultValue=Microfront.DEFAULT_IDIOMA) Idioma lang,
			@RequestParam("id") Long idCont,
			@RequestParam("ttr") String ttr,
			Model model, HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();	
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);				
			}
			
			String url = "";
			if (ttr.equals(Microfront.RNOTICIA)) {
				url = this.urlFactory.tawItemNoticia(microsite, lang, this.noticiasDataService.loadNoticia(idCont, lang));
			} else if (ttr.equals(Microfront.RAGENDA)) {
				url = this.urlFactory.tawItemAgenda(microsite, lang, this.dataService.loadAgenda(idCont, lang));
			} else if (ttr.equals(Microfront.RCONTENIDO)) {
				url = this.urlFactory.tawItemContenido(microsite, lang, this.contenidoDataService.getContenido(microsite, idCont, lang.getLang()));
			}
			
			return "redirect:" + url;

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}
	
	private String obtenNombreSite(String nombreEntero) throws ExceptionFrontMicro {
		if (nombreEntero == null) {
			throw new ExceptionFrontMicro();	
		}
		return nombreEntero.indexOf("/") > 0 ? nombreEntero.substring(0, nombreEntero.indexOf("/")) : nombreEntero;

	}


}
