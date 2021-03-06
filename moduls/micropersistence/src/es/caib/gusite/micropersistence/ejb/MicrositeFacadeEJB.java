package es.caib.gusite.micropersistence.ejb;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import es.caib.gusite.micromodel.Actividadagenda;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.ArchivoLite;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Componente;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.IdiomaMicrosite;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.MicrositeCompleto;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micromodel.Temafaq;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micromodel.UsuarioPropietarioMicrosite;
import es.caib.gusite.micromodel.UsuarioPropietarioRespuesta;
import es.caib.gusite.micropersistence.delegate.ActividadDelegate;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.ComponenteDelegate;
import es.caib.gusite.micropersistence.delegate.ContactoDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;
import es.caib.gusite.micropersistence.delegate.FrqssiDelegate;
import es.caib.gusite.micropersistence.delegate.MenuDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.delegate.TemaDelegate;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;
import es.caib.gusite.micropersistence.delegate.UsuarioDelegate;
import es.caib.gusite.micropersistence.util.ArchivoUtil;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.micropersistence.util.PathUOResult;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.model.IndexFile;
import es.caib.solr.api.model.MultilangLiteral;
import es.caib.solr.api.model.types.EnumAplicacionId;
import es.caib.solr.api.model.types.EnumCategoria;
import es.caib.solr.api.model.types.EnumIdiomas;

/**
 * SessionBean para consultar Microsite.
 * 
 * @ejb.bean name="sac/micropersistence/MicrositeFacade"
 *           jndi-name="es.caib.gusite.micropersistence.MicrositeFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class MicrositeFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -2076446869522196666L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	/**
	 * Inicializo los parámetros de la consulta de Microsite.... No está bien
	 * hecho debería ser Statefull
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "";
		super.from = " from Microsite site join site.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza un Microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarMicrosite(Microsite site) {

		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();
			Map<String, TraduccionMicrosite> listaTraducciones = new HashMap<String, TraduccionMicrosite>();
			Set<IdiomaMicrosite> idiomas = new HashSet<IdiomaMicrosite>();
			
			Microsite siteOriginal = null;
			
			ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
			List<Archivo> archivosPorBorrar = new ArrayList<Archivo>();
			Archivo estiloCSS = null;
			Archivo imagenPrincipal = null;
			Archivo imagenCampanya = null;
			
			boolean nuevo = (site.getId() == null) ? true : false;
			
			if (!nuevo)
				siteOriginal = DelegateUtil.getMicrositeDelegate().obtenerMicrosite(site.getId());

			if (nuevo) {
				
				Iterator<TraduccionMicrosite> it = site.getTraducciones().values().iterator();
				while (it.hasNext()) {
					TraduccionMicrosite trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				site.setTraducciones(null);

				Iterator<IdiomaMicrosite> iter = site.getIdiomas().iterator();
				while (iter.hasNext()) {
					IdiomaMicrosite idi = iter.next();
					idiomas.add(idi);
				}
				site.setIdiomas((Set<IdiomaMicrosite>) null);

				site.setClaveunica(this.obtenerClaveUnica(site));
				
				// Archivos nuevos: toca guardar referencia y poner a null antes de guardado ya que
				// es una entidad aún sin guardar. Los crearemos tras el guardado del microsite.
				if (site.getEstiloCSS() != null) {
					estiloCSS = site.getEstiloCSS();
					site.setEstiloCSS(null);
				}
				
				if (site.getImagenPrincipal() != null) {
					imagenPrincipal = site.getImagenPrincipal();
					site.setImagenPrincipal(null);
				}
				
				if (site.getImagenCampanya() != null) {
					imagenCampanya = site.getImagenCampanya();
					site.setImagenCampanya(null);
				}
				
			} else {
				
				if (site.getEstiloCSS() != null) {
					
					if (site.getEstiloCSS().getId() != null) {
						if (site.getEstiloCSS().getDatos() != null)
							archivoDelegate.grabarArchivo(site.getEstiloCSS());
					} else {
						archivoDelegate.insertarArchivo(site.getEstiloCSS());
					}
										
				} else {
					
					// Archivo a null pero anterior no lo era: solicitan borrado 
					if (siteOriginal.getEstiloCSS() != null) {
						archivosPorBorrar.add(siteOriginal.getEstiloCSS());
					}
										
				}
				
				if (site.getImagenPrincipal() != null) {
					
					if (site.getImagenPrincipal().getId() != null) {
						if (site.getImagenPrincipal().getDatos() != null)
							archivoDelegate.grabarArchivo(site.getImagenPrincipal());
					} else {
						archivoDelegate.insertarArchivo(site.getImagenPrincipal());
					}
										
				} else {
					
					// Archivo a null pero anterior no lo era: solicitan borrado 
					if (siteOriginal.getImagenPrincipal() != null) {
						archivosPorBorrar.add(siteOriginal.getImagenPrincipal());
					}
					
				}
				
				if (site.getImagenCampanya() != null) {
					
					if (site.getImagenCampanya().getId() != null) {
						if (site.getImagenCampanya().getDatos() != null)
							archivoDelegate.grabarArchivo(site.getImagenCampanya());
					} else {
						archivoDelegate.insertarArchivo(site.getImagenCampanya());
					}
										
				} else {
					
					// Archivo a null pero anterior no lo era: solicitan borrado 
					if (siteOriginal.getImagenCampanya() != null) {
						archivosPorBorrar.add(siteOriginal.getImagenCampanya());
					}
										
				}
				
			}

			if (site.getUri() == null || site.getUri().equals("")) {
				site.setUri(site.getClaveunica());
			}
			
			session.saveOrUpdate(site);
			session.flush();

			if (nuevo) {
				
				// Traducciones e idiomas.
				for (TraduccionMicrosite trad : listaTraducciones.values()) {
					trad.getId().setCodigoMicrosite(site.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				site.setTraducciones(listaTraducciones);

				Iterator<IdiomaMicrosite> iterIdi = idiomas.iterator();
				while (iterIdi.hasNext()) {
					IdiomaMicrosite idi = iterIdi.next();
					idi.getId().setCodigoMicrosite(site.getId());
					session.saveOrUpdate(idi);
				}
				session.flush();
				site.setIdiomas(idiomas);
	
				// Ahora se asocian todos los usuarios admin
				UsuarioDelegate uDel = DelegateUtil.getUsuarioDelegate();
				List<?> listau = uDel.listarUsuariosPerfil("gusadmin");
				Iterator<?> iter = listau.iterator();
				while (iter.hasNext()) {
					Usuario user = (Usuario) iter.next();
					UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(site.getId(), user.getId());
					session.save(upm);
				}
				
				// Ahora se asocian todos los usuarios system
				listau = uDel.listarUsuariosPerfil("gussystem");
				iter = listau.iterator();
				while (iter.hasNext()) {
					Usuario user = (Usuario) iter.next();
					UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(site.getId(), user.getId());
					session.save(upm);
				}
				
				// Archivos
				if (estiloCSS != null) {
					archivoDelegate.insertarArchivo(estiloCSS);
					site.setEstiloCSS(estiloCSS);
				}
				
				if (imagenPrincipal != null) {
					archivoDelegate.insertarArchivo(imagenPrincipal);
					site.setImagenPrincipal(imagenPrincipal);
				}
				
				if (imagenCampanya != null) {
					archivoDelegate.insertarArchivo(imagenCampanya);
					site.setImagenCampanya(imagenCampanya);
				}
				
				if (estiloCSS != null || imagenPrincipal != null || imagenCampanya != null) {
					session.saveOrUpdate(site);
				}
				
			}
			
			// Borramos archivos FKs del Microsite que han solicitado que se borren.
			if (archivosPorBorrar.size() > 0)
				archivoDelegate.borrarArchivos(archivosPorBorrar);

			session.flush();
			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(site, op);

			SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_MICROSITE.toString(), site.getId(), null, IndexacionUtil.REINDEXAR);
			
			return site.getId();

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} catch (DelegateException e) {
			
			throw new EJBException(e);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Crea o actualiza UsuarioPropietarioMicrosite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarUsuarioPropietarioMicrosite(UsuarioPropietarioMicrosite upm) {

		Session session = this.getSession();
		try {
			session.save(upm);
			session.flush();
			return upm.getPk().getIdmicrosite();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra un UsuarioPropietarioMicrosite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarUsuarioPropietarioMicrosite(UsuarioPropietarioMicrosite upm) {

		Session session = this.getSession();
		try {
			Criteria criteri = session.createCriteria(UsuarioPropietarioMicrosite.class);
			criteri.add(Restrictions.eq("pk.idmicrosite", upm.getPk().getIdmicrosite()));
			criteri.add(Restrictions.eq("pk.idusuario", upm.getPk().getIdusuario()));
			UsuarioPropietarioMicrosite upm2 = (UsuarioPropietarioMicrosite) criteri.uniqueResult();

			session.delete(upm2);
			session.flush();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Microsite obtenerMicrosite(Long id) {

		Session session = this.getSession();
		try {
			Microsite site = (Microsite) session.get(Microsite.class, id);
			return site;

		} catch (ObjectNotFoundException oNe) {
			log.error(oNe.getMessage());
			return new Microsite();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Microsite a partir de su clave de identificación.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Microsite obtenerMicrositebyKey(String key) {

		Session session = this.getSession();
		try {
			Criteria criteri = session.createCriteria(Microsite.class);
			criteri.add(Restrictions.eq("claveunica", key));
			Microsite site = (Microsite) criteri.uniqueResult();
			return site;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Microsite a partir de su URI.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Microsite obtenerMicrositebyUri(String uri) {

		Session session = this.getSession();
		try {
			Criteria criteri = session.createCriteria(Microsite.class);
			criteri.add(Restrictions.eq("uri", uri));
			Microsite site = (Microsite) criteri.uniqueResult();
			return site;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Microsite para la exportación
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public MicrositeCompleto obtenerMicrositeCompleto(Long id) {

		Session session = this.getSession();
		try {
			MicrositeCompleto site = (MicrositeCompleto) session.get(MicrositeCompleto.class, id);
			if (site.getTema() != null) {
				Hibernate.initialize(site.getTema().getArchivoTemaFronts());
			}
			return site;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Microsite para la exportación
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public MicrositeCompleto obtenerMicrositeCompletobyKey(String key) {

		Session session = this.getSession();
		try {
			String hql = "select mic" + " from MicrositeCompleto mic" + " where mic.claveunica = '" + key + "'";
			Query query = session.createQuery(hql);
			// List list = query.list();
			// Criteria criteri =
			// session.createCriteria(MicrositeCompleto.class);
			// criteri.add(Expression.eq("claveunica", "'" + key + "'"));
			// return (MicrositeCompleto) criteri.uniqueResult();
			// return (list.size() > 0) ? (MicrositeCompleto) list.get(0) :
			// null;
			return (MicrositeCompleto) query.uniqueResult();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Crea un Microsite Completo, durante importación
	 * 
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public Long grabarMicrositeCompleto(MicrositeCompleto site) {

		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();
			Map<String, TraduccionMicrosite> listaTraducciones = new HashMap<String, TraduccionMicrosite>();
			Set<IdiomaMicrosite> idiomas = new HashSet<IdiomaMicrosite>();
			
			Microsite siteOriginal = null;
			
			ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
			List<Archivo> archivosPorBorrar = new ArrayList<Archivo>();
			Archivo estiloCSS = null;
			Archivo imagenPrincipal = null;
			Archivo imagenCampanya = null;
			
			boolean nuevo = (site.getId() == null) ? true : false;
			
			if (!nuevo)
				siteOriginal = DelegateUtil.getMicrositeDelegate().obtenerMicrosite(site.getId());

			if (nuevo) {
				
				Iterator<TraduccionMicrosite> it = site.getTraducciones().values().iterator();
				while (it.hasNext()) {
					TraduccionMicrosite trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				site.setTraducciones(null);

				Iterator<IdiomaMicrosite> iter = site.getIdiomas().iterator();
				while (iter.hasNext()) {
					IdiomaMicrosite idi = iter.next();
					idiomas.add(idi);
				}
				site.setIdiomas((Set<IdiomaMicrosite>) null);

				site.setClaveunica(this.obtenerClaveUnica(site));
				if (site.getUri() == null || site.getUri().equals("")) {
					site.setUri(site.getClaveunica());
				}
				
				// Archivos nuevos: toca guardar referencia y poner a null antes de guardado ya que
				// es una entidad aún sin guardar. Los crearemos tras el guardado del microsite.
				if (site.getEstiloCSS() != null) {
					estiloCSS = site.getEstiloCSS();
					site.setEstiloCSS(null);
				}
				
				if (site.getImagenPrincipal() != null) {
					imagenPrincipal = site.getImagenPrincipal();
					site.setImagenPrincipal(null);
				}
				
				if (site.getImagenCampanya() != null) {
					imagenCampanya = site.getImagenCampanya();
					site.setImagenCampanya(null);
				}
				
			} else {
				
				if (site.getEstiloCSS() != null) {
					
					if (site.getEstiloCSS().getId() != null) {
						if (site.getEstiloCSS().getDatos() != null)
							archivoDelegate.grabarArchivo(site.getEstiloCSS());
					} else {
						archivoDelegate.insertarArchivo(site.getEstiloCSS());
					}
										
				} else {
					
					// Archivo a null pero anterior no lo era: solicitan borrado 
					if (siteOriginal.getEstiloCSS() != null) {
						archivosPorBorrar.add(siteOriginal.getEstiloCSS());
					}
										
				}
				
				if (site.getImagenPrincipal() != null) {
					
					if (site.getImagenPrincipal().getId() != null) {
						if (site.getImagenPrincipal().getDatos() != null)
							archivoDelegate.grabarArchivo(site.getImagenPrincipal());
					} else {
						archivoDelegate.insertarArchivo(site.getImagenPrincipal());
					}
										
				} else {
					
					// Archivo a null pero anterior no lo era: solicitan borrado 
					if (siteOriginal.getImagenPrincipal() != null) {
						archivosPorBorrar.add(siteOriginal.getImagenPrincipal());
					}
					
				}
				
				if (site.getImagenCampanya() != null) {
					
					if (site.getImagenCampanya().getId() != null) {
						if (site.getImagenCampanya().getDatos() != null)
							archivoDelegate.grabarArchivo(site.getImagenCampanya());
					} else {
						archivoDelegate.insertarArchivo(site.getImagenCampanya());
					}
										
				} else {
					
					// Archivo a null pero anterior no lo era: solicitan borrado 
					if (siteOriginal.getImagenCampanya() != null) {
						archivosPorBorrar.add(siteOriginal.getImagenCampanya());
					}
										
				}
				
			}
			
			session.saveOrUpdate(site);
			session.flush();

			if (nuevo) {
				
				for (TraduccionMicrosite trad : listaTraducciones.values()) {
					trad.getId().setCodigoMicrosite(site.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				site.setTraducciones(listaTraducciones);

				Iterator<IdiomaMicrosite> iterIdi = idiomas.iterator();
				while (iterIdi.hasNext()) {
					IdiomaMicrosite idi = iterIdi.next();
					idi.getId().setCodigoMicrosite(site.getId());
					session.saveOrUpdate(idi);
				}
				session.flush();
				site.setIdiomas(idiomas);

				UsuarioDelegate usudel = DelegateUtil.getUsuarioDelegate();
				Usuario usu = usudel.obtenerUsuariobyUsername(this.getUsuario(session).getUsername());
				UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(site.getId(), usu.getId());
				session.save(upm);
				
				// Archivos
				if (estiloCSS != null) {
					archivoDelegate.insertarArchivo(estiloCSS);
					site.setEstiloCSS(estiloCSS);
				}
				
				if (imagenPrincipal != null) {
					archivoDelegate.insertarArchivo(imagenPrincipal);
					site.setImagenPrincipal(imagenPrincipal);
				}
				
				if (imagenCampanya != null) {
					archivoDelegate.insertarArchivo(imagenCampanya);
					site.setImagenCampanya(imagenCampanya);
				}
				
				if (estiloCSS != null || imagenPrincipal != null || imagenCampanya != null) {
					session.saveOrUpdate(site);
				}
				
			}
			
			// Borramos archivos FKs del Microsite que han solicitado que se borren.
			if (archivosPorBorrar.size() > 0)
				archivoDelegate.borrarArchivos(archivosPorBorrar);
			
			session.flush();
			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(site, op);

			return site.getId();

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} catch (DelegateException e) {
			
			throw new EJBException(e);
			
		} catch (Exception e) {
			
			throw new EJBException(e);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * borra un Microsite Completo
	 * 
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public void borrarMicrositeCompleto(Long id) {

		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();

			// Primero: borrar los usuarios asociados
			Criteria criteri = session.createCriteria(UsuarioPropietarioMicrosite.class);
			criteri.add(Restrictions.eq("pk.idmicrosite", id));

			Iterator<?> iter = criteri.list().iterator();
			while (iter.hasNext()) {
				UsuarioPropietarioMicrosite upm = (UsuarioPropietarioMicrosite) iter.next();
				session.delete(upm);
			}

			// Segundo: En el caso que el microsite tenga encuestas, borraremos
			// la relación de usuarios con las respuestas de la encuesta
			List<?> listIdRespu = this.idRespDeEncDelMicrosite(id);
			if (!listIdRespu.isEmpty()) {
				Criteria criteriUsuPropiResp = session.createCriteria(UsuarioPropietarioRespuesta.class);
				criteriUsuPropiResp.add(Restrictions.in("id.idrespuesta", listIdRespu));
				Iterator<?> iterUsuPropiResp = criteriUsuPropiResp.list().iterator();
				while (iterUsuPropiResp.hasNext()) {
					UsuarioPropietarioRespuesta upm = (UsuarioPropietarioRespuesta) iterUsuPropiResp.next();
					session.delete(upm);
				}
			}

			// Tercero: borrar el microsite completo
			MicrositeCompleto site = (MicrositeCompleto) session.get(MicrositeCompleto.class, id);
			
			// Obtenemos archivos comunes del Microsite y los borramos.
			ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
			Set<ArchivoLite> listaArchivosLite = site.getDocus();
			
			List<Archivo> listaArchivos = new ArrayList<Archivo>();
			for (ArchivoLite al : listaArchivosLite)
				listaArchivos.add(ArchivoUtil.archivoLite2Archivo(al));
			
			archivoDelegate.borrarArchivos(listaArchivos);
			
			FaqDelegate faqDelegate = DelegateUtil.getFaqDelegate();
			for (Iterator it = site.getFaqs().iterator(); it.hasNext();) {
				faqDelegate.borrarFaq(((Faq) it.next()).getId());
			}

			TemaDelegate temaDelegate = DelegateUtil.getTemafaqDelegate();
			for (Object tema : temaDelegate.listarCombo(id)) {
				temaDelegate.borrarTema(((Temafaq) tema).getId());
			}

			AgendaDelegate agendaDelegate = DelegateUtil.getAgendaDelegate();
			for (Iterator it = site.getAgendas().iterator(); it.hasNext();) {
				agendaDelegate.borrarAgenda(((Agenda) it.next()).getId());
			}

			ActividadDelegate actividadDelegate = DelegateUtil.getActividadagendaDelegate();
			for (Iterator it = site.getActividades().iterator(); it.hasNext();) {
				actividadDelegate.borrarActividad(((Actividadagenda) it.next()).getId());
			}

			NoticiaDelegate noticiaDelegate = DelegateUtil.getNoticiasDelegate();
			for (Iterator it = site.getNoticias().iterator(); it.hasNext();) {
				noticiaDelegate.borrarNoticia(((Noticia) it.next()).getId());
			}

			ComponenteDelegate componenteDelegate = DelegateUtil.getComponentesDelegate();
			for (Iterator it = site.getComponentes().iterator(); it.hasNext();) {
				componenteDelegate.borrarComponente(((Componente) it.next()).getId());
			}

			TipoDelegate tipoDelegate = DelegateUtil.getTipoDelegate();
			for (Iterator it = site.getTiponotis().iterator(); it.hasNext();) {
				tipoDelegate.borrarTipo(((Tipo) it.next()).getId());
			}

			FrqssiDelegate frqssiDelegate = DelegateUtil.getFrqssiDelegate();
			for (Iterator it = site.getFrqssis().iterator(); it.hasNext();) {
				frqssiDelegate.borrarFrqssi(((Frqssi) it.next()).getId());
			}

			ContactoDelegate contactoDelegate = DelegateUtil.getContactoDelegate();
			for (Iterator it = site.getFormularioscontacto().iterator(); it.hasNext();) {
				contactoDelegate.borrarContacto(((Contacto) it.next()).getId());
			}

			EncuestaDelegate encuestaDelegate = DelegateUtil.getEncuestaDelegate();
			for (Iterator it = site.getEncuestas().iterator(); it.hasNext();) {
				encuestaDelegate.borrarEncuesta(((Encuesta) it.next()).getId());
			}

			ContenidoDelegate contenidoDelegate = DelegateUtil.getContenidoDelegate();
			for (Object contenido : contenidoDelegate.listarAllContenidos(id.toString())) {
				contenidoDelegate.borrarContenido(((Contenido) contenido).getId());
			}

			MenuDelegate menuDelegate = DelegateUtil.getMenuDelegate();
			for (Iterator it = site.getMenus().iterator(); it.hasNext();) {
				menuDelegate.borrarMenu(((Menu) it.next()).getId());
			}

			session.createQuery("delete TraduccionMicrosite tra where tra.id.codigoMicrosite = " + id.toString()).executeUpdate();
			session.createQuery("delete UsuarioPropietarioMicrosite upm where upm.pk.idmicrosite = " + id.toString()).executeUpdate();
			session.createQuery("delete IdiomaMicrosite imic where imic.id.codigoMicrosite = " + id.toString()).executeUpdate();
			session.createQuery("delete PersonalizacionPlantilla perPla where perPla.microsite.id = " + id.toString()).executeUpdate();
			session.createQuery("delete Microsite mic where mic.id = " + id.toString()).executeUpdate();

			session.flush();
			
			// Borrado de archivos que son FKs (ha de ser posterior, debido al modelo de datos).
			if (site.getEstiloCSS() != null)
				archivoDelegate.borrarArchivo(site.getEstiloCSS().getId());
			
			if (site.getImagenPrincipal() != null)
				archivoDelegate.borrarArchivo(site.getImagenPrincipal().getId());
			
			if (site.getImagenCampanya() != null)
				archivoDelegate.borrarArchivo(site.getImagenCampanya().getId());
			
			ArchivoUtil.borrarDirMicrosite(site.getId());
			
			tx.commit();
			this.close(session);

			SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_MICROSITE.toString(), site.getId(), null, IndexacionUtil.DESINDEXAR);
			
			/**
			 * Ojo, el site está eliminado así que hay que asegurarse de que no
			 * se intenta enlazar
			 */
			this.grabarAuditoria(null, site, Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} catch (DelegateException e) {
			
			throw new EJBException(e);
			
		} catch (IOException e) {

			throw new EJBException(e);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Lista todos los Ids de las respuestas de las encuestas de un microsite.
	 */
	private List<?> idRespDeEncDelMicrosite(Long idMicrosite) {

		Session session = this.getSession();
		try {
			String hql = "SELECT RESP.id " + "FROM Encuesta ENC,  Pregunta PRE, Respuesta RESP " + "WHERE RESP.idpregunta = PRE.id "
					+ "AND PRE.idencuesta = ENC.id " + "AND ENC.idmicrosite = " + idMicrosite.toString();

			Query query = session.createQuery(hql);
			List<?> queryList = query.list();
			return queryList;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Reemplaza un microsite basandose en la clave unica del microsite.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public Long reemplazarMicrositeCompleto(MicrositeCompleto site) {

		Session session = this.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			MicrositeCompleto oldsite = this.obtenerMicrositeCompletobyKey(site.getClaveunica());
			ArrayList<Long> listausuariosold = new ArrayList<Long>();

			if (oldsite != null) {
				// Primero: recoger todos los usuarios asociados al antiguo
				// microsite y además borrar upm
				String hql = "select usuMic" + " from UsuarioPropietarioMicrosite usuMic" + " where usuMic.pk.idmicrosite = " + oldsite.getId().toString();
				Query query = session.createQuery(hql);
				Iterator<?> iter = query.list().iterator();
				while (iter.hasNext()) {
					UsuarioPropietarioMicrosite upm = (UsuarioPropietarioMicrosite) iter.next();
					listausuariosold.add(upm.getPk().getIdusuario());
					session.delete(upm);
				}

				// Segundo: En el caso que el microsite tenga encuestas,
				// borraremos la relación de usuarios con las respuestas de la encuesta
				List<?> listIdRespu = this.idRespDeEncDelMicrosite(oldsite.getId());
				if (!listIdRespu.isEmpty()) {
					Criteria criteriUsuPropiResp = session.createCriteria(UsuarioPropietarioRespuesta.class);
					criteriUsuPropiResp.add(Restrictions.in("id.idrespuesta", listIdRespu));

					Iterator<?> iterUsuPropiResp = criteriUsuPropiResp.list().iterator();
					while (iterUsuPropiResp.hasNext()) {
						UsuarioPropietarioRespuesta upm = (UsuarioPropietarioRespuesta) iterUsuPropiResp.next();
						session.delete(upm);
					}
				}

				// Tercero: borrar, si procede, el microsite antiguo
				this.borrarMicrositeCompleto(oldsite.getId());
				session.flush();
			}
			// Tercero: grabar el nuevo microsite
			this.grabarMicrositeCompleto(site);
			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(site, Auditoria.MODIFICAR);

			return site.getId();

		} catch (HibernateException he) {
			try {
				if (tx != null) {
					tx.rollback();
				}
			} catch (HibernateException e) {
				throw new EJBException(e);
			}
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los Microsites
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Long> listarMicrosites() {

		final Session session = this.getSession();
		try {
			final Query query = session.createQuery(" select micro.id from Microsite micro ");
			return query.list();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}


	/**
	 * Marcar microsite como indexado. En caso de no pasar ID, se da por hecho que se quiere marcar todos como NO indexados.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void marcarComoIndexado(final Long id, final int indexado, final Boolean todoCorrecto) {

		final Session session = this.getSession();
		try {
			int indexadoCorrecto;
			if (todoCorrecto == null) {
				indexadoCorrecto = Microsite.INDEXADO_INICIAL;
			} else if (todoCorrecto) {
				indexadoCorrecto = Microsite.INDEXADO_CORRECTAMENTE;
			} else {
				indexadoCorrecto = Microsite.INDEXADO_INCORRECTAMENTE;
			}
			
			final Query query;
			if (id == null) {
				query = session.createQuery("update Microsite set indexado = " + indexado +" , indexadoCorrectamente = " + indexadoCorrecto);
			} else {
				query = session.createQuery("update Microsite set indexado = " + indexado +" , indexadoCorrectamente = " + indexadoCorrecto + " where id = " + id);
			}
			query.executeUpdate();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}
	
	
	/**
	 * Resumen de los microsites indexados.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public String getResumenMicrositesIndexados(boolean conResumen) {
		final Session session = this.getSession();
		try {
			final Query query = session.createQuery("select id, indexado, indexadoCorrectamente, uri from Microsite");
			List<Object[]> objetos = query.list();
			StringBuffer respuesta = new StringBuffer();
			int indexados = 0;
			int indexadosNO = 0;
			StringBuffer micrositeErrores = new StringBuffer();
			int indexadosErroneamente = 0;
			for(Object[] objeto : objetos) {
				Long indexado = null;
				if (objeto[1] != null) {
					indexado = Long.valueOf(objeto[1].toString());
				}
				if (indexado == null || indexado == Microsite.NO_INDEXADO) {
					indexadosNO++;
				} else {
					indexados++;
					Long indexadoCorrectamente = null;
					if (objeto[2] != null) {
						indexadoCorrectamente = Long.valueOf(objeto[2].toString());
					}
					if (indexadoCorrectamente != null && indexadoCorrectamente == Microsite.INDEXADO_INCORRECTAMENTE ) {
						
						if (conResumen) {
							indexadosErroneamente++;
						} else {
							String id = objeto[0].toString();
							String uri = objeto[3].toString();
							if (micrositeErrores.length() == 0) {
								micrositeErrores.append("<ul>");
							} 
							micrositeErrores.append("<li><a target='_blank' href='index.do?idsite=" + id +"'>" + id + ":" + uri + "</a></li>");
						}
					}
				}
			}
			
			
			int total = indexados + indexadosNO;
			respuesta.append( "Hi ha un total de "+total+" microsites ( " + indexadosNO + " no indexats , "+indexados+" indexats ). ");
			if (conResumen) {
				respuesta.append("\n Amb errors un total de "+indexadosErroneamente+" microsites");
			} else {
				if (micrositeErrores.length() > 0) {
					respuesta.append("\n Amb errors: " + micrositeErrores +" </ul>");
				}
			}
			return respuesta.toString();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}
	
	/**
	 * Obtiene el total de microsites indexado, o no, y en que estado.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long getCuantosMicrosites(Integer indexado, Integer estadoIndexacion)throws java.rmi.RemoteException,DelegateException {		
		final Session session = this.getSession();
		try {
			final Query query = session.createQuery(" select count(micro) from Microsite micro where micro.indexado = " + indexado + " and micro.indexadoCorrectamente = " + estadoIndexacion);
			
			return (Long) query.uniqueResult();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}
	
	/**
	 * Lista todos los Microsites sin indexar.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Long> listarMicrositesSinIndexar(int cuantos) {

		final Session session = this.getSession();
		try {
			final Query query = session.createQuery(" select micro.id from Microsite micro where micro.indexado = " + Microsite.NO_INDEXADO);
			query.setMaxResults(cuantos);
			return query.list();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Listado ligero de todos los Microsites. Solo rellena el idioma por
	 * defecto
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarMicrositesThin() {

		Session session = this.getSession();
		try {
			String sql = "select mic.id, mic.unidadAdministrativa, trad.titulo " + "from Microsite mic join mic.traducciones trad "
					+ "where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "' order by trad.titulo";

			Query query = session.createQuery(sql);

			ArrayList<Microsite> lista = new ArrayList<Microsite>();
			Iterator<?> res = query.iterate();
			int i = 0;
			while (res.hasNext()) {
				Object[] fila = (Object[]) res.next();
				Microsite mic = new Microsite();
				mic.setId((Long) fila[0]);
				Integer idua = (Integer) fila[1];
				mic.setUnidadAdministrativa(idua.intValue());
				TraduccionMicrosite trad = new TraduccionMicrosite();
				trad.setTitulo((String) fila[2]);
				mic.setTraduccion(Idioma.getIdiomaPorDefecto(), trad);
				lista.add(i, mic);
				i++;
			}

			return lista;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los Microsites a los que el usuario puede acceder
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarMicrositesFiltro(Usuario usu, Map<?, ?> param) {

		Session session = this.getSession();
		try {
			Criteria criteri = session.createCriteria(Microsite.class);
			this.populateCriteria(criteri, param);
			List<?> microlista = criteri.list();

			ArrayList<?> microlistaOrd = new ArrayList<Object>(microlista);
			Comparator<Object> comp = new MicrosComparator();
			Collections.sort(microlistaOrd, comp);

			return microlistaOrd;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	private static class MicrosComparator implements Comparator<Object> {
		@Override
		public int compare(Object element1, Object element2) {

			String nom1 = (((TraduccionMicrosite) ((Microsite) element1).getTraduccion("ca")).getTitulo() != null) ? ((TraduccionMicrosite) ((Microsite) element1)
					.getTraduccion("ca")).getTitulo() : "";
			String nom2 = (((TraduccionMicrosite) ((Microsite) element2).getTraduccion("ca")).getTitulo() != null) ? ((TraduccionMicrosite) ((Microsite) element2)
					.getTraduccion("ca")).getTitulo() : "";

			return nom1.toLowerCase().compareTo(nom2.toLowerCase());
		}
	}

	/**
	 * borra un Microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public void borrarMicrosite(Long id) {

		Session session = this.getSession();
		try {
			Transaction tx = session.beginTransaction();
			Microsite site = (Microsite) session.get(Microsite.class, id);
			session.delete(site);
			session.flush();
			tx.commit();
			this.close(session);

			/**
			 * Ojo, el site está eliminado así que hay que asegurarse de que no
			 * se intenta enlazar
			 */
			this.grabarAuditoria(null, site, Auditoria.ELIMINAR);

			SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_MICROSITE.toString(), site.getId(), null, IndexacionUtil.DESINDEXAR);
			
		} catch (Exception he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Anyade un id de contenido al listado de los ultimos modificados en el
	 * microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void grabarUltimoIdcontenido(Microsite site, Long idcontenido) {

		Session session = this.getSession();
		try {
			site.setServiciosSeleccionados(this.manejaListadoUltimosIds(site.getServiciosSeleccionados(), "" + idcontenido.longValue()));
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(site);
			session.flush();
			tx.commit();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> listarMicrositesbyUser(Usuario usuario) {

		Session session = this.getSession();
		try {
			String hql = "select mic.id, mic.unidadAdministrativa, trad.titulo ";
			hql += " from UsuarioPropietarioMicrosite upm, Microsite mic join mic.traducciones trad ";
			hql += " where upm.pk.idusuario=" + usuario.getId().longValue() + " and mic.id = upm.pk.idmicrosite and trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "' order by trad.titulo";

			Query query = session.createQuery(hql);

			ArrayList<Microsite> lista = new ArrayList<Microsite>();
			Iterator<?> res = query.iterate();
			int i = 0;
			while (res.hasNext()) {
				Object[] fila = (Object[]) res.next();
				Microsite mic = new Microsite();
				mic.setId((Long) fila[0]);
				Integer idua = (Integer) fila[1];
				mic.setUnidadAdministrativa(idua.intValue());
				TraduccionMicrosite trad = new TraduccionMicrosite();
				trad.setTitulo((String) fila[2]);
				mic.setTraduccion(Idioma.getIdiomaPorDefecto(), trad);
				lista.add(i, mic);
				i++;
			}

			return lista;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> llistarMicrodeluser(String id) {

		Session session = this.getSession();
		try {
			String hql = "select mic.id, mic.unidadAdministrativa, trad.titulo ";
			hql += " from UsuarioPropietarioMicrosite upm, Microsite mic join mic.traducciones trad ";
			hql += " where upm.pk.idusuario = " + id + " and  mic.id = upm.pk.idmicrosite and trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "' order by trad.titulo";

			Query query = session.createQuery(hql);

			ArrayList<Microsite> lista = new ArrayList<Microsite>();
			Iterator<?> res = query.iterate();
			int i = 0;
			while (res.hasNext()) {
				Object[] fila = (Object[]) res.next();
				Microsite mic = new Microsite();
				mic.setId((Long) fila[0]);
				Integer idua = (Integer) fila[1];
				mic.setUnidadAdministrativa(idua.intValue());
				TraduccionMicrosite trad = new TraduccionMicrosite();
				trad.setTitulo((String) fila[2]);
				mic.setTraduccion(Idioma.getIdiomaPorDefecto(), trad);
				lista.add(i, mic);
				i++;
			}

			return lista;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}
	
	/**
	 * Obtiene un Microsite para la exportación
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Long> obtenerIdsArchivosMicrosite(Long id) {

		Session session = this.getSession();
		
		List<Long> lista = new ArrayList<Long>();
		
		try {
						
			// Archivos comunes
			Query query = session.createQuery("select a.id from Archivo a where a.idmicrosite = ?");
			query.setLong(0, id);
			List<Long> listaQuery = query.list();
			
			for (Long l : listaQuery) {
				// Si no es nulo ni repetido, lo añadimos a la lista.
				if (l != null && lista.indexOf(l) == -1)
					lista.add(l);
			}
						
			return lista;
			
		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Usuario> listarUsernamesbyMicrosite(Long idmicrosite) {

		Session session = this.getSession();
		try {
			String hql = "select usuario";
			hql += " from UsuarioPropietarioMicrosite upm, Usuario usuario";
			hql += " where upm.pk.idmicrosite = ";
			hql += idmicrosite.longValue();
			hql += " and  usuario.id = upm.pk.idusuario order by usuario.username";

			Query query = session.createQuery(hql);
			return (List<Usuario>) query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	private void populateCriteria(Criteria criteri, Map<?, ?> parametros) {

		parametros.remove("id");
		for (Object name : parametros.keySet()) {
			String key = (String) name;
			Object value = parametros.get(key);
			if (value != null) {
				if (value instanceof String) {
					String sValue = (String) value;
					if (sValue.length() > 0) {
						criteri.add(Restrictions.ilike(key, value));
					}
				} else {
					criteri.add(Restrictions.eq(key, value));
				}
			}
		}
	}

	/**
	 * Metodo privado que se le pasa un string con ids separados por ;, y le
	 * añade en la primera posicion el nuevo id
	 * 
	 * @param oldlistado
	 * @param newIdcontenido
	 * @return
	 */
	private String manejaListadoUltimosIds(String oldlistado, String newIdcontenido) {

		int _numMax = 5;
		String _nulo = "-1";
		Hashtable<String, String> hshlistaids = new Hashtable<String, String>();

		// inicializo 5 entradas a nulo
		for (int i = 0; i < _numMax; i++) {
			hshlistaids.put("" + i, _nulo);
		}

		String servs = oldlistado;

		if (servs != null) {
			StringTokenizer st = new StringTokenizer(servs, ";");
			int n = st.countTokens();

			// recoger el hash
			for (int i = 0; i < n; i++) {
				hshlistaids.put("" + i, st.nextToken());
			}

			// comprobar que no se repita.
			int repe = -1; // obtendrá la posicion repetida
			for (int i = 0; i < n; i++) {
				if (hshlistaids.get("" + i).equals(newIdcontenido)) {
					repe = i;
				}
			}
			if (repe != -1) {
				// es repetido
				hshlistaids.put("" + repe, _nulo);
				for (int i = repe; i < _numMax - 1; i++) {
					hshlistaids.put("" + i, hshlistaids.get("" + (i + 1)));
				}
			}

			// ahora, desplazar el hash
			for (int i = _numMax - 1; i >= 0; i--) {
				hshlistaids.put("" + (i + 1), hshlistaids.get("" + i));
			}
		}

		// anyadir el id que se ha pasado
		hshlistaids.put("0", newIdcontenido);

		// volcar el hash en el string final
		servs = "";
		for (int i = 0; i < _numMax; i++) {
			if (!hshlistaids.get("" + i).equals(_nulo)) {
				servs += hshlistaids.get("" + i) + ";";
			}
		}
		if (servs.length() > 0) {
			servs = servs.substring(0, servs.length() - 1);
		}

		return servs;
	}

	
	/**
	 * Metodo que genera una clave única.
	 * 
	 * @param site
	 * @return
	 */
	private String obtenerClaveUnica(Object site) {

		String retorno = "";

		SimpleDateFormat fmt = new SimpleDateFormat("yyMMddHHmmss");
		retorno += "M" + fmt.format(new Date());
		retorno += site.hashCode();

		return retorno;
	}
	
	/**
	 * Obtiene un Microsite
	 * 
	 */
	private Microsite obtenerMicrositeBySolr(Long id) {

		final Session session = this.getSession();
		try {
			return (Microsite) session.get(Microsite.class, id);
		} catch (Exception exception) {
			log.error(exception.getMessage());
			return null;
		} finally {
			this.close(session);
		}
	}
	
	/**
	 * Método para indexar según la id y la categoria. 
	 * @param solrIndexer
	 * @param idElemento
	 * @param categoria
	 * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     * @ejb.transaction type="RequiresNew"
	 */
	public SolrPendienteResultado indexarSolrArchivo(final SolrIndexer solrIndexer, final Long idElemento, final EnumCategoria categoria, final Long idArchivo, final PathUOResult iPathUO) {
		
		log.debug("MicrositeEJB.indexarSolrArchivo. idElemento:" + idElemento +" categoria:"+categoria +" idArchivo:"+idArchivo);
		
		try {
			ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();
			
			//Paso 0. Obtenemos el contenido y comprobamos si se puede indexar.
			final Microsite micro = obtenerMicrositeBySolr(idElemento);
			if (micro == null) {
				return new SolrPendienteResultado(true, "Error obteniendo el microsite.");
			}
			
			if (!IndexacionUtil.isIndexable(micro)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}
			
			final Archivo archivo = archi.obtenerArchivo(idArchivo);
			if (!IndexacionUtil.isIndexable(archivo)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}
			
			byte[] contenidoFichero = archi.obtenerContenidoFichero(archivo);
			
			// Los archivos solo se indexa en un idioma, por lo que si se quiere que se encuentren en todos los idiomas,
			// habrá que indexarse en todos los idiomas.
			
			for (String keyIdioma : micro.getTraducciones().keySet()) {
			
				final MultilangLiteral extension = new MultilangLiteral();
				final MultilangLiteral titulo = new MultilangLiteral();
				final MultilangLiteral descripcion = new MultilangLiteral();
				final MultilangLiteral urls = new MultilangLiteral();
				final MultilangLiteral urlPadre = new MultilangLiteral();
				final MultilangLiteral tituloPadre = new MultilangLiteral();
				final MultilangLiteral searchTextOptional = new MultilangLiteral();
				final List<EnumIdiomas> idiomas = new ArrayList<EnumIdiomas>();
				
				final EnumIdiomas enumIdioma = EnumIdiomas.fromString(keyIdioma);
				
				final TraduccionMicrosite traduccion = (TraduccionMicrosite) micro.getTraduccion(keyIdioma);
		    	
				if (traduccion != null && enumIdioma != null) {
					
					// Si no tiene titulo microsite no indexamos
					if (traduccion.getTitulo() == null || traduccion.getTitulo().isEmpty()) {
						continue;
					}
					
					PathUOResult pathUO;
					if (iPathUO == null) {
						pathUO = IndexacionUtil.calcularPathUOsMicrosite(micro, keyIdioma);
					} else {
						pathUO = iPathUO;
					}
					
					//Si sigue a nulo, es que no es visible o no existe la UO.
					if (pathUO == null) {
						return new SolrPendienteResultado(false, "El microsite està associat a una Unitat Orgànica inexistent, per favor, posis en contacte amb l'administrador.");
					}
					
					//Anyadimos idioma al enumerado.
					idiomas.add(enumIdioma);
					
					//Seteamos los primeros campos multiidiomas: Titulo, Descripción
					titulo.addIdioma(enumIdioma, IndexacionUtil.getTituloArchivo(archivo));
			    	descripcion.addIdioma(enumIdioma, IndexacionUtil.getDescripcionArchivo(archivo));
			    	
			    	// Extension
			    	extension.addIdioma(enumIdioma, FilenameUtils.getExtension(archivo.getNombre()));

			    	// Text optional
			    	searchTextOptional.addIdioma(enumIdioma, pathUO.getUosText());
			    	
			    	//URL
			    	urls.addIdioma(enumIdioma, IndexacionUtil.getUrlArchivo(micro, archivo, keyIdioma));
			    	
			    	// Padre
			    	urlPadre.addIdioma(enumIdioma, IndexacionUtil.getUrlMicrosite(micro, keyIdioma));	    		
			    	tituloPadre.addIdioma(enumIdioma, IndexacionUtil.getTituloMicrosite(micro, keyIdioma));
			    	
			    	final IndexFile indexFile = new IndexFile();
					indexFile.setCategoria(EnumCategoria.GUSITE_ARCHIVO);
					indexFile.setAplicacionId(EnumAplicacionId.GUSITE);
					indexFile.setElementoId(idArchivo.toString() + "_" + enumIdioma.toString());
					indexFile.setTitulo(titulo);
					indexFile.setDescripcion(descripcion);
					indexFile.setUrl(urls);
					indexFile.setCategoriaPadre(EnumCategoria.GUSITE_MICROSITE);
					indexFile.setElementoIdPadre(micro.getId().toString());
					indexFile.setUrlPadre(urlPadre);
					indexFile.setUos(pathUO.getUosPath());
					indexFile.setSearchTextOptional(searchTextOptional);
					indexFile.setIdioma(enumIdioma);
					indexFile.setFileContent(contenidoFichero);
					indexFile.setExtension(extension);
					indexFile.setCategoriaRaiz(EnumCategoria.GUSITE_MICROSITE);
					indexFile.setElementoIdRaiz(micro.getId().toString());					
					indexFile.setInterno(!micro.getRestringido().equals("N") ? true : false);
						
					solrIndexer.indexarFichero(indexFile);
			    	
				}
			}
			
			
			
			return new SolrPendienteResultado(true);
			
		} catch(Exception exception) {
			log.error("Error intentando indexar idElemento:" + idElemento +" categoria:"+categoria +" idArchivo:"+idArchivo, exception);
			return new SolrPendienteResultado(false, exception.getMessage());
		}
	}
	
	/**
	 * Obtiene lista de microsites para una Unidad Administrativa
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Long> obtenerMicrositesbyUA(List<Integer> listIds) {

		Session session = this.getSession();
		try {
			 String hql = "select mic.id from Microsite mic where mic.unidadAdministrativa in  (:list)";
			 Query query = session.createQuery(hql);
			 query.setParameterList("list", listIds);
			 List list = query.list();
			 return list;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}
	
	/**
	 * Comprueba si estan todos los microsites indexados.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public boolean isTodosIndexados () {

		Session session = this.getSession();
		try {
			 String hql = "select count(mic.id) from Microsite mic where mic.indexado = " + Microsite.NO_INDEXADO;
			 Query query = session.createQuery(hql);
			 int idCuantos = Integer.parseInt(query.uniqueResult().toString());
			 boolean todosIndexados;
			 if (idCuantos  == 0)  {
				 todosIndexados = true;
			 } else {
				 todosIndexados = false;
			 }
			 return todosIndexados;
			 
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}
}
