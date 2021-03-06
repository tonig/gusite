package es.caib.gusite.front.microtag;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.service.FrontDataService;
import es.caib.gusite.micromodel.Componente;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TraduccionComponente;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micropersistence.delegate.ComponenteDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;

/**
 * Parseo de 'componentes'. Esta clase contiene métodos que parsean los tags
 * especiales de los microsites. Los tags son de la version 1.4 Devuelven trozos
 * de código HTML pertenecientes a la listados de elementos.
 * 
 * @author Indra
 */
public class MParserComponente extends MParserHTML {

	protected static Log log = LogFactory.getLog(MParserComponente.class);

	/**
	 * Constructor de la clase
	 * 
	 * @param version
	 */
	public MParserComponente() {
		super();
	}

	/**
	 * Método que devuelve un string preparado para insertar en un html. Ese
	 * string contiene el listado de los últimos elementos según el 'id' del
	 * componente. Este método es el que se usa para los tags de la version 1.4
	 * 
	 * @param idmicrosite
	 * @param idcomponente
	 * @param idioma
	 * @return StringBuffer que contiene todo el 'html' necesario para montar el
	 *         listado
	 */
	public StringBuffer getHtmlElementosComponente(Long idmicrosite, String idcomponente, String idioma) {
		StringBuffer retorno = new StringBuffer();

		try {
			ComponenteDelegate compodel = DelegateUtil.getComponentesDelegate();
			Componente componente = compodel.obtenerComponente(new Long(Long.parseLong(idcomponente)));

			// si es de clase noticia.
			if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_NOTICIA)) {
				retorno.append(this.getHtmlComponenteTNoticia(idmicrosite, componente, idioma));
			}
			// si es de clase enlace.
			if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_LINK)) {
				retorno.append(this.getHtmlComponenteTEnlace(idmicrosite, componente, idioma));
			}
			// si es de clase documento
			if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_DOCUMENTO)) {
				retorno.append(this.getHtmlComponenteTDocumento(idmicrosite, componente, idioma));
			}
			// si es de clase documento
			if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_CONEXIO_EXTERNA)) {
				retorno.append(this.getHtmlComponenteTExterno(idmicrosite, componente, idioma));
			}

			if (idmicrosite.longValue() == 0) {
				retorno.append("");
			}

		} catch (Exception e) {
			log.error("[getHtmlElementosComponente]: " + e.getMessage());
			retorno = new StringBuffer("");
		}

		return retorno;
	}

	/**
	 * Método privado que devuelve un string para insertar en un HTML un
	 * documento externo
	 * 
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @return StringBuffer Código HTML
	 */
	private StringBuffer getHtmlComponenteTExterno(Long idmicrosite, Componente componente, String idioma) {
		StringBuffer retorno = new StringBuffer();

		try {
			String nombreformulario = "formnoticiasexternocomp" + componente.getId().longValue();
			String nombre_elemento = ((TraduccionComponente) componente.getTraduccion(idioma)) != null ? ((TraduccionComponente) componente
					.getTraduccion(idioma)).getTitulo() : "[ empty ]";

			// al div le ponemos el id de la clase que acabamos de configurar
			retorno.append(this.configStyle3(idmicrosite, componente));
			retorno.append("<div id=\"element").append(componente.getId()).append("\">");
			retorno.append("<h2>").append(nombre_elemento).append("</h2>");
			retorno.append("<form action=\"noticias.do\" name=\"").append(nombreformulario).append("\" id=\"").append(nombreformulario)
					.append("\" >");

			retorno.append("<input type=\"hidden\" name=\"lang\" value=\"").append(idioma).append("\">");
			retorno.append("<input type=\"hidden\" name=\"nameform\" value=\"").append(nombreformulario).append("\">");

			TipoDelegate tD = DelegateUtil.getTipoDelegate();

			// preparamos el map a enviar
			Map<String, String> filtrado = new HashMap<String, String>();
			filtrado.put("lang", idioma);
			filtrado.put("nameform", nombreformulario);
			filtrado.put("idcomponente", "" + componente.getId().longValue());

			String resultadoHTML = tD.obtenerPegoteHTMLExterno(componente.getTipo().getId(), filtrado);

			// retorno.append("<style>").append(resultado.get(Microfront.MAP_EXT_MCSTYLE)).append("</style>");
			retorno.append(resultadoHTML);
			// retorno.append("<script>").append(resultado.get(Microfront.MAP_EXT_MCJAVASCRIPT)).append("</script>");

			retorno.append("</form></div>");
			if (idmicrosite.longValue() == 0) {
				retorno.append("");
			}

		} catch (Exception e) {
			log.error("[getHtmlComponenteTExterno]: " + e.getMessage());
			retorno = new StringBuffer("");
		}

		return retorno;
	}

	/**
	 * Método privado que devuelve un string para insertar en un HTML una
	 * Noticia
	 * 
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @return StringBuffer Código Html
	 */
	private StringBuffer getHtmlComponenteTNoticia(Long idmicrosite, Componente componente, String idioma) {
		StringBuffer retorno = new StringBuffer();

		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));

		try {
			NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
			noticiadel.init();
			noticiadel.setPagina(1);
			noticiadel.setTampagina(componente.getNumelementos().intValue());
			java.sql.Date dt = new java.sql.Date((new Date()).getTime());
			String wherenoticias = "where noti.visible='S' and noti.idmicrosite=" + idmicrosite + " and noti.tipo="
					+ componente.getTipo().getId().toString();
			wherenoticias += " and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
			wherenoticias += " and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
			noticiadel.setWhere(wherenoticias);
			noticiadel.setOrderby2(this.preparaOrden(componente.getOrdenacion().toString()));
			List<?> listanoticias = noticiadel.listarNoticiasThin(idioma);

			String nombre_elemento = ((TraduccionComponente) componente.getTraduccion(idioma)) != null ? ((TraduccionComponente) componente
					.getTraduccion(idioma)).getTitulo() : "[ empty ]";

			// al div le ponemos el id de la clase que acabamos de configurar
			retorno.append(this.configStyle3(idmicrosite, componente));
			retorno.append("<div id=\"element" + componente.getId() + "\">");
			retorno.append("<h2>" + nombre_elemento + "</h2>");

			if (listanoticias.size() != 0) {

				retorno.append("<table width=\"100%\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" id=\"element" + componente.getId() + "\">");
				Iterator<?> iter = listanoticias.iterator();
				int cont = 0;
				// Si SoloImagen, es mostra en forma de mosaic a dues columnes
				String nCols = (componente.getSoloimagen().equals("S")) ? "" : "<tr>";

				Microsite microsite = DelegateUtil.getMicrositeDelegate().obtenerMicrosite(idmicrosite);

				while (iter.hasNext()) {
					java.text.SimpleDateFormat dia = new java.text.SimpleDateFormat("dd/MM/yyyy");
					Noticia noti = (Noticia) iter.next();

					retorno.append(((cont % 2) == 0) ? "<tr class=\"par\">" : nCols);
					if (componente.getSoloimagen().equals("S")) {
						// en el caso de que sólo imágenes
						retorno.append("<td style=\"width:1%\" >");
						retorno.append("<a href=\"" + MicroURI.uriNoticia(idmicrosite, noti.getId(), idioma) + "\">");
						if (noti.getImagen() != null) {
							retorno.append("<img src=\""
									+ MicroURI.uriImg(noti.getImagen().getNombre(), microsite.getUri())
									+ "\" alt=\"\" class=\"imagen\" />");
						} else {
							if (componente.getImagenbul() != null) {
								retorno.append("<img src=\""
										+ MicroURI.uriImg(componente.getImagenbul().getNombre(), microsite.getUri()) 
										+ "\" alt=\"" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo()
										+ "\" class=\"imagen\" />&nbsp;");
							} else {
								retorno.append("<img src=\"imgs/listados/bullet_gris.gif\" alt=\"\" /> &nbsp;");
							}
							retorno.append(((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo());
						}
						retorno.append("</a>");
						retorno.append("</td><td>&nbsp;</td> \n");

					} else {
						retorno.append("<td style=\"width:1%\" >");
						if (noti.getImagen() != null) {
							retorno.append("<img src=\""
									+ MicroURI.uriImg(noti.getImagen().getNombre(), microsite.getUri())
									+ "\" width=\"48\" height=\"48\" alt=\"\" class=\"imagen\" width=\"266\" height=\"127\" />&nbsp;");
						} else {
							if (componente.getImagenbul() != null) {
								retorno.append("<img src=\""
										+ MicroURI.uriImg(componente.getImagenbul().getNombre(), microsite.getUri()) + "\" alt=\"" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo()
										+ "\" class=\"imagen\" />&nbsp;");
							} else {
								retorno.append("<img src=\"imgs/listados/bullet_gris.gif\" alt=\"\" /> &nbsp;");
							}
						}
						retorno.append(noti.getFpublicacion() != null ? dia.format(noti.getFpublicacion()) : "");
						retorno.append("&nbsp;<a href=\"" + MicroURI.uriNoticia(idmicrosite, noti.getId(), idioma) + "\">");
						retorno.append(((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo() + "</a>");
						String subTitol = ((TraduccionNoticia) noti.getTraduccion(idioma)).getSubtitulo();
						retorno.append("<p></p>" + (subTitol == null ? "" : subTitol) + "<br/>");

						retorno.append("</td> \n");
					}
					retorno.append(((cont % 2) == 0) ? nCols : "</tr> \n");
					cont++;
				}
				retorno.append("</table> \n");

			} else {
				retorno.append("<p>" + rb.getString("microhtml.nonoticias") + "</p>");
			}
			retorno.append("</div>");
			if (idmicrosite.longValue() == 0) {
				retorno.append("");
			}

		} catch (Exception e) {
			log.error("[getHtmlComponenteTNoticia]: " + e.getMessage());
			retorno = new StringBuffer("");
		}

		return retorno;
	}

	/**
	 * Método privado que devuelve un string para insertar en un HTML un Enlace
	 * 
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @return StringBuffer Código HTML
	 */
	private StringBuffer getHtmlComponenteTEnlace(Long idmicrosite, Componente componente, String idioma) {
		StringBuffer retorno = new StringBuffer();

		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));

		try {
			NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
			noticiadel.init();
			noticiadel.setPagina(1);
			noticiadel.setTampagina(componente.getNumelementos().intValue());
			java.sql.Date dt = new java.sql.Date((new Date()).getTime());
			String wherenoticias = "where noti.visible='S' and noti.idmicrosite=" + idmicrosite + " and noti.tipo="
					+ componente.getTipo().getId().toString();
			wherenoticias += " and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
			wherenoticias += " and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
			noticiadel.setWhere(wherenoticias);
			noticiadel.setOrderby2(this.preparaOrden(componente.getOrdenacion().toString()));
			List<?> listanoticias = noticiadel.listarNoticiasThin(idioma);

			String nombre_elemento = ((TraduccionComponente) componente.getTraduccion(idioma)) != null ? ((TraduccionComponente) componente
					.getTraduccion(idioma)).getTitulo() : "[ empty ]";

			// al div le ponemos el id de la clase que acabamos de configurar
			retorno.append(this.configStyle3(idmicrosite, componente));
			retorno.append("<div id=\"element" + componente.getId() + "\">");
			retorno.append("<h2>" + nombre_elemento + "</h2>");
			if (listanoticias.size() != 0) {

				retorno.append("<table width=\"100%\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" id=\"element" + componente.getId() + "\">");
				Iterator<?> iter = listanoticias.iterator();
				int cont = 0;
				// Si SoloImagen, es mostra en forma de mosaic a dues columnes
				String nCols = (componente.getSoloimagen().equals("S")) ? "" : "<tr>";
				Microsite microsite = DelegateUtil.getMicrositeDelegate().obtenerMicrosite(idmicrosite);

				while (iter.hasNext()) {
					Noticia noti = (Noticia) iter.next();
					retorno.append(((cont % 2) == 0) ? "<tr class=\"par\">" : nCols);
					if (componente.getSoloimagen().equals("S") && (noti.getImagen() != null)) {
						// en el caso de que sólo imágenes
						retorno.append("<td style=\"width:1%\" >");
						retorno.append("<a href=\"" + MicroURI.uriNoticia(idmicrosite, noti.getId(), idioma) + "\">");
						if (noti.getImagen() != null) {
							retorno.append("<img src=\""
									+ MicroURI.uriImg(noti.getImagen().getNombre(), microsite.getUri())
									+ "\" alt=\"" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo() + "\" class=\"imagen\" />");
						} else {
							retorno.append(((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo());
						}
						retorno.append("</a>");
						retorno.append("</td><td>&nbsp;</td> \n");

					} else {
						retorno.append("<td style=\"width:1%\" >");
						if (componente.getImagenbul() != null) {
							retorno.append("<img src=\""
									+ MicroURI.uriImg(componente.getImagenbul().getNombre(), microsite.getUri())
									+ "\" alt=\"\" class=\"imagen\" />");
						} else {
							retorno.append("<img src=\"imgs/listados/bullet_gris.gif\" alt=\"\" />");
						}

						boolean urlExt = ((TraduccionNoticia) noti.getTraduccion(idioma)).getLaurl().startsWith("http:");
						String target = (urlExt) ? "_blank" : "_self";
						retorno.append("<a href=\"" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getLaurl() + "\" target=\"" + target
								+ "\">&nbsp;" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo() + "</a>");

						if (((TraduccionNoticia) noti.getTraduccion(idioma)).getSubtitulo() != null) {
							retorno.append("<p></p>" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getSubtitulo());
						}
						if (((TraduccionNoticia) noti.getTraduccion(idioma)).getTexto() != null) {
							retorno.append("<p></p>" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTexto());
						}
						retorno.append("</td> \n");
					}
					retorno.append(((cont % 2) == 0) ? nCols : "</tr> \n");
					cont++;
				}
				retorno.append("</table> \n");

			} else {
				retorno.append("<p>" + rb.getString("microhtml.nonoticias") + "</p>");
			}
			retorno.append("</div>");
			if (idmicrosite.longValue() == 0) {
				retorno.append("");
			}

		} catch (Exception e) {
			log.error("[getHtmlComponenteTEnlace]: " + e.getMessage());
			retorno = new StringBuffer("");
		}

		return retorno;
	}

	/**
	 * Método privado que devuelve un string para insertar en un HTML un
	 * documento
	 * 
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @return StringBuffer Código HTML
	 */
	private StringBuffer getHtmlComponenteTDocumento(Long idmicrosite, Componente componente, String idioma) {
		StringBuffer retorno = new StringBuffer();

		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
		try {
			NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
			noticiadel.init();
			noticiadel.setPagina(1);
			noticiadel.setTampagina(componente.getNumelementos().intValue());
			java.sql.Date dt = new java.sql.Date((new Date()).getTime());
			String wherenoticias = "where trad.id.codigoIdioma='" + idioma + "' and noti.visible='S' and noti.idmicrosite=" + idmicrosite
					+ " and noti.tipo=" + componente.getTipo().getId().toString();
			wherenoticias += " and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
			wherenoticias += " and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
			noticiadel.setWhere(wherenoticias);
			noticiadel.setOrderby2(this.preparaOrden(componente.getOrdenacion().toString()));
			List<?> listanoticias = noticiadel.listarNoticiasThin(idioma);

			String nombre_elemento = ((TraduccionComponente) componente.getTraduccion(idioma)) != null ? ((TraduccionComponente) componente
					.getTraduccion(idioma)).getTitulo() : "[ empty ]";

			// al div le ponemos el id de la clase que acabamos de configurar
			retorno.append(this.configStyle3(idmicrosite, componente));
			retorno.append("<div id=\"element" + componente.getId() + "\">");
			retorno.append("<h2>" + nombre_elemento + "</h2>");

			if (listanoticias.size() != 0) {

				retorno.append("<table width=\"100%\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" id=\"element" + componente.getId() + "\">");
				Iterator<?> iter = listanoticias.iterator();
				int cont = 0;
				// Si SoloImagen, es mostra en forma de mosaic a dues columnes
				String nCols = (componente.getSoloimagen().equals("S")) ? "" : "<tr>";
				Microsite microsite = DelegateUtil.getMicrositeDelegate().obtenerMicrosite(idmicrosite);

				while (iter.hasNext()) {

					Noticia noti = (Noticia) iter.next();
					retorno.append(((cont % 2) == 0) ? "<tr class=\"par\">" : nCols);
					if (componente.getSoloimagen().equals("S") && (noti.getImagen() != null)) {
						// en el caso de que sólo imágenes
						retorno.append("<td style=\"width:1%\" >");
						retorno.append("<a href=\"" + MicroURI.uriNoticia(idmicrosite, noti.getId(), idioma) + "\">");
						if (noti.getImagen() != null) {
							retorno.append("<img src=\""
									+ MicroURI.uriImg(noti.getImagen().getNombre(), microsite.getUri())
									+ "\" alt=\"" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo() + "\" class=\"imagen\" />");
						} else {
							retorno.append(((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo());
						}
						retorno.append("</a>");
						retorno.append("</td><td>&nbsp;</td> \n");

					} else {
						retorno.append("<td style=\"width:1%\" >");
						if (componente.getImagenbul() != null) {
							retorno.append("<img src=\""
									+ MicroURI.uriImg(componente.getImagenbul().getNombre(), microsite.getUri())
									+ "\" alt=\"\" class=\"imagen\" />");
						} else {
							retorno.append("<img src=\"imgs/listados/bullet_gris.gif\" alt=\"\" />");
						}
						retorno.append("<a href=\"elementodocumento.do?idsite=" + idmicrosite + "&cont=" + noti.getId() + "&lang=" + idioma
								+ "\" target=\"_blank\">&nbsp;" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo() + "</a>");
						if (((TraduccionNoticia) noti.getTraduccion(idioma)).getTexto() != null) {
							retorno.append("<p></p>" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTexto());
						}
						retorno.append("</td> \n");
					}
					retorno.append(((cont % 2) == 0) ? nCols : "</tr> \n");
					cont++;
				}
				retorno.append("</table> \n");

			} else {
				retorno.append("<p>" + rb.getString("microhtml.nonoticias") + "</p>");
			}
			retorno.append("</div>");
			if (idmicrosite.longValue() == 0) {
				retorno.append("");
			}

		} catch (Exception e) {
			log.error("[getHtmlElementos]: " + e.getMessage());
			retorno = new StringBuffer("");
		}

		return retorno;
	}

	/**
	 * Método que crea un bloque completo del tag de html "<style>"
	 * 
	 * @param idmicrosite
	 * @param componente
	 * @return StringBuffer con todo el bloque '<style>'
	 */
	private StringBuffer configStyle3(Long idmicrosite, Componente componente) {
		StringBuffer retorno = new StringBuffer();

		// se monta todo en un estilo, luego simplemente hay que aplicarlo
		retorno.append("<style> " + "\n");
		// retorno.append("div#element" + componente.getId() +
		// " h2 { padding:.5em 0 0 2em; border-top: #85bbe4 2px solid; background: url(imgs/titol/ico_blau.gif) #fff no-repeat left top; color: #00276c;  } \n");
		if (componente.getSoloimagen().equals("S")) {
			retorno.append("table#element" + componente.getId() + " tr { margin:0; padding:.2em; } \n");
			retorno.append("table#element" + componente.getId() + " tr td { vertical-align:top; }   \n");

		} else {
			if (componente.getImagenbul() != null) {
				retorno.append("table#element" + componente.getId() + " tr { margin:0; padding:.2em; } \n");
				retorno.append("table#element" + componente.getId() + " tr td { vertical-align:top; }  \n");
			} else {
				if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_NOTICIA)) {
					retorno.append("table#element" + componente.getId() + " tr { margin:0; padding:.2em; } \n");
					retorno.append("div#element" + componente.getId() + " tr td { vertical-align:top; }  \n");
				} else {
					retorno.append("table#element" + componente.getId() + " tr { margin:0; padding:.2em; } \n");
					retorno.append("table#element" + componente.getId() + " tr td { vertical-align:top; }  \n");
				}
			}
		}
		if (componente.getFilas().equals("S")) {
			retorno.append("table#element" + componente.getId() + " tr.par { background-color:#EFEFEF; } \n");
		}

		retorno.append("table#element" + componente.getId() + " tr td span.font { font-weight: bold; font-style: italic } \n");
		retorno.append("table#element" + componente.getId() + " tr td span.enllas { margin-top: 0.2em; display: block; padding-left: 0.2em } \n");
		retorno.append("table#element" + componente.getId() + " tr td img { margin-right:0em; } \n");
		retorno.append("table#element" + componente.getId()
				+ " tr td img.imagen { float:left; margin-right:.3em; padding-right:.2em; padding-bottom:.8em;} \n");
		retorno.append(" </style>" + "\n");

		return retorno;
	}


}
