package es.caib.gusite.front.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import es.caib.gusite.front.general.ExceptionFront;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.noticia.NoticiaCriteria;
import es.caib.gusite.front.noticia.ResultadoNoticias;
import es.caib.gusite.front.util.Cadenas;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micropersistence.delegate.BuscarElementosParameter;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;

@Service
public class NoticiasDataService {

	protected static Log log = LogFactory.getLog(NoticiasDataService.class);

	/**
	 * Método privado para preparar el orden en el cual se mostrara el listado.
	 */
	private String getOrdenNoticias(NoticiaCriteria criteria) {

		return this.getOrdenNoticias(criteria.getTipo());
	}

	/**
	 * Método privado para preparar el orden en el cual se mostrara el listado.
	 */
	private String getOrdenNoticias(Tipo tipo) {

		if (tipo.getOrden().equals("0")) {
			return " order by noti.orden ";
		}
		if (tipo.getOrden().equals("1")) {
			return " order by noti.fpublicacion ";
		}
		if (tipo.getOrden().equals("2")) {
			return " order by noti.fpublicacion desc";
		}
		if (tipo.getOrden().equals("3")) {
			return " order by trad.titulo ";
		}
		return "";
	}

	private String construirPartialWhereAnyo(int anyo) {
		String where = "";
		where += "     (to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + anyo
				+ "-12-31')";
		where += " and (to_char(noti.fpublicacion,'yyyy-MM-dd')>='" + anyo
				+ "-01-01')";
		return where;
	}

	private BuscarElementosParameter rellenarParametrosBuscador(
			Microsite microsite, Idioma lang, NoticiaCriteria criteria)
			throws Exception {

		String txtsearch = criteria.getFiltro().toUpperCase();

		Map<String, String> paramMap = new HashMap<String, String>();
		Map<String, String> traduccionMap = new HashMap<String, String>();

		String whereAnyo = "";

		// Parámetros generales
		if (criteria.getAnyo() > 0) {
			whereAnyo = this.construirPartialWhereAnyo(criteria.getAnyo());
		} else {
			paramMap.put("fpublicacion", txtsearch);
		}

		// Parámetros traducibles
		traduccionMap.put("titulo", txtsearch);

		traduccionMap.put("subtitulo", txtsearch);
		traduccionMap.put("fuente", txtsearch);
		traduccionMap.put("laurl", txtsearch);
		traduccionMap.put("urlnom", txtsearch);
		traduccionMap.put("texto", txtsearch);

		BuscarElementosParameter paramsBuscador = new BuscarElementosParameter();
		paramsBuscador.parametros = paramMap;
		paramsBuscador.traduccion = traduccionMap;
		paramsBuscador.idmicrosite = microsite.getId().toString();
		paramsBuscador.idioma = lang.getLang();
		paramsBuscador.idtipo = "" + criteria.getTipo().getId();
		paramsBuscador.where = whereAnyo;
		return paramsBuscador;
	}

	@SuppressWarnings("unchecked")
	public ResultadoNoticias<Noticia> listarNoticias(Microsite microsite,
			Idioma lang, NoticiaCriteria criteria) throws DelegateException {

		try {
			// preparar el tipo de noticias.
			String txttipo = "" + criteria.getTipo().getId();
			String txtsearch = criteria.getFiltro();

			if (StringUtils.isEmpty(txtsearch)) {
				NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
				noticiadel.init();
				java.sql.Date dt = new java.sql.Date((new Date()).getTime());
				String wherenoticias = "where noti.visible='S' and noti.idmicrosite="
						+ microsite.getId() + " and noti.tipo=" + txttipo;
				wherenoticias += " and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='"
						+ dt + "')";
				wherenoticias += " and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='"
						+ dt + "')";

				if (criteria.getAnyo() > 0) {
					wherenoticias += " and "
							+ this.construirPartialWhereAnyo(criteria.getAnyo());
				}

				noticiadel.setWhere(wherenoticias);
				noticiadel.setOrderby2(this.getOrdenNoticias(criteria));
				noticiadel.setTampagina(criteria.getTipo().getTampagina());

				if (criteria.getOrdenacion() != null
						&& criteria.getOrdenacion().length() > 0) {
					noticiadel.setOrderby(criteria.getOrdenacion());
				}
				noticiadel.setPagina(criteria.getPagina());

				List<Noticia> listanoticias = noticiadel
						.listarNoticiasThin(lang.getCodigoEstandar());
				for (Noticia n : listanoticias) {
					n.setIdi(lang.getLang());
					if (n.getTipo() != null) {
						n.getTipo().setIdi(lang.getLang());
					}
				}

				ResultadoNoticias<Noticia> ret = new ResultadoNoticias<Noticia>(
						listanoticias,
						(Map<String, Integer>) noticiadel.getParametros());
				// listado tradicional
				ret.setBusqueda(false);
				return ret;

			} else {

				ResultadoNoticias<Noticia> ret = this.buscarNoticiasPorCampos(
						microsite, lang, criteria);
				ret.setBusqueda(true);
				return ret;

			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResultadoNoticias<Noticia>(
					new ErrorMicrosite("Error",
							"Se ha producido un error desconocido en el listado de noticias."));
		}

	}

	@SuppressWarnings("unchecked")
	public List<Noticia> listarNoticiasTipo(Microsite microsite, String lang,
			Long idTipo, int numNoticias) throws DelegateException,
			ExceptionFrontPagina {

		Tipo tipo = this.loadTipo(idTipo, lang);

		// preparar el tipo de noticias.
		NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
		noticiadel.init();
		java.sql.Date dt = new java.sql.Date((new Date()).getTime());
		String wherenoticias = "where noti.visible='S' and noti.idmicrosite="
				+ microsite.getId() + " and noti.tipo=" + tipo.getId();
		wherenoticias += " and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='"
				+ dt + "')";
		wherenoticias += " and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='"
				+ dt + "')";

		noticiadel.setWhere(wherenoticias);
		noticiadel.setOrderby2(this.getOrdenNoticias(tipo));
		noticiadel.setTampagina(numNoticias);

		noticiadel.setPagina(1);

		List<Noticia> listanoticias = noticiadel.listarNoticiasThin(lang);
		for (Noticia n : listanoticias) {
			n.setIdi(lang);
			if (n.getTipo() != null) {
				n.getTipo().setIdi(lang);
			}
		}

		return listanoticias;

	}

	@SuppressWarnings("unchecked")
	private ResultadoNoticias<Noticia> buscarNoticiasPorCampos(
			Microsite microsite, Idioma lang, NoticiaCriteria criteria)
			throws Exception, DelegateException {

		NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();

		BuscarElementosParameter paramsBuscador = this
				.rellenarParametrosBuscador(microsite, lang, criteria);

		List<Noticia> listanoticias = (List<Noticia>) noticiadel
				.buscarElementos(paramsBuscador);

		// busqueda por lucene. Pendiente de activar (ya estaba pendiente en
		// microfront)
		// listanoticias=noticiadel.buscarElementosLuc(microsite.getId().toString(),idioma,txttipo,
		// txtsearch, true);
		return new ResultadoNoticias<Noticia>(listanoticias,
				(Map<String, Integer>) noticiadel.getParametros());
	}

	private static final int ONE_YEAR = 1;
	private static final String TOTS_ANYS = "Tots";

	/**
	 * Método para obtener una lista con los años.
	 * 
	 * @return
	 */
	public List<String> obtenerListaAnyos(Microsite microsite, Idioma lang,
			Tipo tipo) {

		try {
			// preparar el tipo de noticias.
			NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
			noticiadel.init();
			String wherenoticias = "where trad.id.codigoIdioma='"
					+ lang.getLang()
					+ "' and noti.visible='S' and noti.idmicrosite="
					+ microsite.getId() + " and noti.tipo=" + tipo.getId()
					+ " and noti.fpublicacion is not null ";
			noticiadel.setWhere(wherenoticias);
			noticiadel.setOrderby2("order by noti.fpublicacion desc");
			noticiadel.setTampagina(Microfront.MAX_INTEGER);// todooooooo
			noticiadel.setPagina(1);

			List<String> listaanyos = noticiadel.listarAnyos();

			// si hay mas de un anyo, se añade "tots" al principio
			if (ONE_YEAR < listaanyos.size()) {
				List<String> tmpList = listaanyos;
				listaanyos = new ArrayList<String>();
				listaanyos.add(TOTS_ANYS);
				listaanyos.addAll(tmpList);
			}

			return listaanyos;

		} catch (Exception e) {
			log.error(e.getMessage());
			// TODO:
			// beanerror = new ErrorMicrosite("Error",
			// "Se ha producido un error desconocido al obtener el listado de años para la paginacion.");
			return null;
		}

	}

	/**
	 * Método para recoger Externo.
	 * 
	 * @throws ExceptionFront
	 */
	public String cargaExterno(Tipo tipo, HttpServletRequest req)
			throws ExceptionFrontPagina {
		try {
			TipoDelegate tD = DelegateUtil.getTipoDelegate();

			// preparamos el map a enviar
			Map<String, String> filtrado = new HashMap<String, String>();

			Enumeration<?> paramNames = req.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = (String) paramNames.nextElement();
				String paramValue = req.getParameter(paramName);
				filtrado.put(paramName, paramValue);
			}

			return tD.obtenerPegoteHTMLExterno(tipo.getId(), filtrado);
		} catch (DelegateException ne) {
			throw new ExceptionFrontPagina(
					"[Se ha producido un error desconocido en el listado de noticias]  Stack=="
							+ Cadenas.statcktrace2String(ne.getStackTrace(), 6),
					ne);
		}
	}

	public Noticia loadNoticia(long idNoticia, Idioma lang)
			throws DelegateException, ExceptionFrontPagina {
		NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
		Noticia noticia = noticiadel.obtenerNoticia(idNoticia);
		if (noticia == null) {
			throw new ExceptionFrontPagina("Noticia no encontrada: "
					+ idNoticia, ExceptionFrontPagina.HTTP_NOT_FOUND);
		}
		noticia.setIdi(lang.getLang());
		if (noticia.getTipo() != null) {
			noticia.getTipo().setIdi(lang.getLang());
		}

		return noticia;
	}

	public Noticia loadNoticia(String uriNoticia, String lang)
			throws DelegateException, ExceptionFrontPagina {
		NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
		Noticia noticia = noticiadel.obtenerNoticiaDesdeUri(lang, uriNoticia);
		if (noticia == null) {
			// Si no lo encontramos por idioma, buscamos cualquiera. Esto sirve
			// para el cambio de idioma sencillo
			noticia = noticiadel.obtenerNoticiaDesdeUri(null, uriNoticia);
		}
		if (noticia == null) {
			throw new ExceptionFrontPagina("Noticia no encontrada: "
					+ uriNoticia, ExceptionFrontPagina.HTTP_NOT_FOUND);
		}
		noticia.setIdi(lang);
		if (noticia.getTipo() != null) {
			noticia.getTipo().setIdi(lang);
		}
		return noticia;
	}

	public Tipo loadTipo(long idTipo, Idioma lang) throws DelegateException,
			ExceptionFrontPagina {
		return this.loadTipo(idTipo, lang.getLang());
	}

	public Tipo loadTipo(long idTipo, String lang) throws DelegateException,
			ExceptionFrontPagina {
		TipoDelegate tipodel = DelegateUtil.getTipoDelegate();
		Tipo tipo = tipodel.obtenerTipo(idTipo);
		if (tipo == null) {
			throw new ExceptionFrontPagina("Tipo no encontrado: " + idTipo,
					ExceptionFrontPagina.HTTP_NOT_FOUND);
		}
		tipo.setIdi(lang);
		return tipo;

	}

}