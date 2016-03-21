package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.lucene.analysis.Analizador;
import es.caib.gusite.lucene.model.Catalogo;
import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.IndexObject;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.RespuestaDato;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionPregunta;
import es.caib.gusite.micromodel.TraduccionRespuesta;
import es.caib.gusite.micromodel.UsuarioPropietarioRespuesta;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IndexerDelegate;

/**
 * SessionBean para manipular encuestas.
 * 
 * @ejb.bean name="sac/micropersistence/EncuestaFacade"
 *           jndi-name="es.caib.gusite.micropersistence.EncuestaFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
@SuppressWarnings({"unchecked", "deprecation"})
public abstract class EncuestaFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -8128896105109064587L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
		try {
			super.langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
		} catch (Exception ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Inicializo los parámetros de la consulta....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(Long site) {
		super.tampagina = 10;
		super.pagina = 0;
		// super.select="";
		super.select = "select enc.id, enc.fcaducidad, enc.fpublicacion, enc.visible, trad.titulo ";
		super.from = " from Encuesta enc join enc.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "' and enc.idmicrosite = "
				+ site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void initra(Long site, String idiomapasado) {
		super.tampagina = 10;
		super.pagina = 0;
		// super.select="";
		super.select = "select enc.id, enc.fcaducidad, enc.fpublicacion, enc.visible, trad.titulo ";
		super.from = " from Encuesta enc join enc.traducciones trad ";
		super.where = " where (trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto()
				+ "' or trad.id.codigoIdioma = '" + idiomapasado
				+ "') and enc.idmicrosite = " + site.toString();
		super.whereini = " ";
		super.orderby = " order by enc.id,trad.id.codigoIdioma desc";

		super.camposfiltro = new String[] { "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		// super.select="";
		super.select = "select enc.id, enc.fcaducidad, enc.fpublicacion, enc.visible, trad.titulo ";
		super.from = " from Encuesta enc join enc.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza una encuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarEncuesta(Encuesta enc) throws DelegateException {

		Session session = this.getSession();
		try {
			boolean nuevo = (enc.getId() == null) ? true : false;
			Transaction tx = session.beginTransaction();

			Map<String, TraduccionEncuesta> listaTraducciones = new HashMap<String, TraduccionEncuesta>();

			if (nuevo) {
				Iterator<TraduccionEncuesta> it = enc.getTraducciones()
						.values().iterator();
				while (it.hasNext()) {
					TraduccionEncuesta trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				enc.setTraducciones(null);
			}

			session.saveOrUpdate(enc);
			session.flush();

			if (nuevo) {
				for (TraduccionEncuesta trad : listaTraducciones.values()) {
					trad.getId().setCodigoEncuesta(enc.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				enc.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(enc, op);

			return enc.getId();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una encuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Encuesta obtenerEncuesta(Long id) {

		Session session = this.getSession();
		try {
			Encuesta enc = (Encuesta) session.get(Encuesta.class, id);
			session.refresh(enc);
			return enc;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}
	

	/**
	 * Obtiene una encuesta a partir de la URI
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Encuesta obtenerEncuestaDesdeUri(String idioma, String uri, String site) {

		Session session = this.getSession();
		try {
			Query query;
			if (idioma != null) {
				query = session
						.createQuery("select encuesta from Encuesta encuesta JOIN encuesta.traducciones te where te.id.codigoIdioma = :idioma and te.uri = :uri and encuesta.idmicrosite = :site");
				query.setParameter("idioma", idioma);
			} else {
				query = session
						.createQuery("select encuesta from Encuesta encuesta JOIN encuesta.traducciones te where te.uri = :uri and encuesta.idmicrosite = :site");				
			}
			query.setParameter("uri", uri);
			query.setParameter("site",Long.valueOf(site));
			
			query.setMaxResults(1);
			return (Encuesta) query.uniqueResult();
		} catch (ObjectNotFoundException oNe) {
			log.error(oNe.getMessage());
			return new Encuesta();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las encuestas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Encuesta> listarEncuestas() {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación

			Query query = session.createQuery(this.select + this.from
					+ this.where + this.orderby);

			ScrollableResults scr = query.scroll();
			ArrayList<Encuesta> lista = new ArrayList<Encuesta>();
			scr.first();
			scr.scroll(this.cursor - 1);
			int i = 0;
			while (this.tampagina > i++) {
				Object[] fila = (Object[]) scr.get();
				Encuesta enc = new Encuesta();
				enc.setId((Long) fila[0]);
				enc.setFcaducidad((java.util.Date) fila[1]);
				enc.setFpublicacion((java.util.Date) fila[2]);
				enc.setVisible((String) fila[3]);

				TraduccionEncuesta trad = new TraduccionEncuesta();
				trad.setTitulo((String) fila[4]);
				enc.setTraduccion(Idioma.getIdiomaPorDefecto(), trad);

				lista.add(enc);
				if (!scr.next()) {
					break;
				}
			}
			scr.close();
			return lista;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las encuestas de recursos
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarEncuestasrec(String idiomapasado) {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			Query query = session.createQuery(this.select + this.from
					+ this.where + this.orderby);

			ScrollableResults scr = query.scroll();
			ArrayList<Encuesta> lista = new ArrayList<Encuesta>();
			scr.first();
			scr.scroll(this.cursor - 1);
			int i = 0;
			while (this.tampagina > i++) {
				Object[] fila = (Object[]) scr.get();
				Encuesta enc = new Encuesta();
				enc.setId((Long) fila[0]);
				enc.setFcaducidad((java.util.Date) fila[1]);
				enc.setFpublicacion((java.util.Date) fila[2]);
				enc.setVisible((String) fila[3]);

				TraduccionEncuesta trad = new TraduccionEncuesta();
				trad.setTitulo((String) fila[4]);
				enc.setTraduccion(idiomapasado, trad);

				lista.add(enc);
				if (!scr.next()) {
					break;
				}
			}
			scr.close();
			return lista;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra una encuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarEncuesta(Long id) throws DelegateException {

		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();
			Encuesta encuesta = (Encuesta) session.get(Encuesta.class, id);

			List<Pregunta> preguntas = session.createQuery("from Pregunta where idencuesta = " + id).list();
			for (int p = 0; p < preguntas.size(); p++) {
								
				List<?> respuestas = this.listarRespuestas(new Long(preguntas.get(p).getId()));

				for (int i = 0; i < respuestas.size(); i++) {
					session.createQuery("delete from UsuarioPropietarioRespuesta where id.idrespuesta = " 
							+ ((Respuesta) respuestas.get(i)).getId())
							.executeUpdate();
				}

				// Segundo: Borramos las respuestas de las preguntas.
				for (int i = 0; i < respuestas.size(); i++) {
					session.createQuery("delete from TraduccionRespuesta where id.codigoRespuesta = "
							+ ((Respuesta) respuestas.get(i)).getId())
							.executeUpdate();
					session.createQuery("delete from Respuesta where id = "
							+ ((Respuesta) respuestas.get(i)).getId())
							.executeUpdate();
				}

				session.createQuery("delete from TraduccionPregunta where id.codigoPregunta = " + preguntas.get(p).getId())
						.executeUpdate();
				session.createQuery("delete from Pregunta where id = " + preguntas.get(p).getId())
						.executeUpdate();
				
				// Borrar imagen asociada a pregunta, si existe.
				if (preguntas.get(p).getImagen() != null)
					DelegateUtil.getArchivoDelegate().borrarArchivo(preguntas.get(p).getImagen().getId());
				
			}

			session.createQuery("delete from TraduccionEncuesta where id.codigoEncuesta = " + id)
					.executeUpdate();
			session.createQuery("delete from Encuesta where id = " + id)
					.executeUpdate();

			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(encuesta, Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Crea o actualiza una pregunta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void grabarPregunta(Pregunta pre) throws DelegateException {
		
		Session session = this.getSession();
		Boolean nuevo = false;
		Pregunta preOriginal = null;
		
		ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
		Archivo imagen = null;
		List<Archivo> archivosPorBorrar = new ArrayList<Archivo>();
		
		try {
			
			if (pre.getId() == null) {
				nuevo = true;
			}

			Transaction tx = session.beginTransaction();
			Map<String, TraduccionPregunta> listaTraducciones = new HashMap<String, TraduccionPregunta>();

			if (nuevo) {
				
				Iterator<TraduccionPregunta> it = pre.getTraducciones().values().iterator();
				while (it.hasNext()) {
					TraduccionPregunta trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				pre.setTraducciones(null);
				
				if (pre.getImagen() != null) {
					
					imagen = pre.getImagen();
					pre.setImagen(null);
										
				}
			
			} else {
				
				preOriginal = this.obtenerPregunta(pre.getId());
				
				if (pre.getImagen() != null) {
					if (pre.getImagen().getId() != null)
						archivoDelegate.grabarArchivo(pre.getImagen());
					else
						archivoDelegate.insertarArchivo(pre.getImagen());
				} else {
					// Archivo a null pero anterior no lo era: solicitan borrado 
					if (preOriginal.getImagen() != null) {
						archivosPorBorrar.add(preOriginal.getImagen());
					}
				}
				
			}

			session.saveOrUpdate(pre);
			session.flush();

			if (nuevo) {
				
				for (TraduccionPregunta trad : listaTraducciones.values()) {
					trad.getId().setCodigoPregunta(pre.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				pre.setTraducciones(listaTraducciones);
				
				if (imagen != null) {
					archivoDelegate.insertarArchivo(imagen);
				}
				
				pre.setImagen(imagen);
				
				session.saveOrUpdate(pre);
				session.flush();
				
			}

			tx.commit();
			
			// Borramos archivo FK de la pregunta solicitado para borrar.
			if (archivosPorBorrar.size() > 0)
				archivoDelegate.borrarArchivos(archivosPorBorrar);

			// Actualizamos el indice
			Encuesta enc = (Encuesta) session.get(Encuesta.class, pre.getIdencuesta());
			this.indexBorraEncuesta(enc.getId());
			this.indexInsertaEncuesta(enc, null);
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(enc.getIdmicrosite(), pre, op);

		} catch (HibernateException e) {
			
			throw new EJBException(e);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Obtiene una pregunta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Pregunta obtenerPregunta(Long id) {

		Session session = this.getSession();
		try {
			Pregunta pre = (Pregunta) session.get(Pregunta.class, id);
			Hibernate.initialize(pre);
			return pre;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las preguntas de una encuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarPreguntas(Long id) {

		Session session = this.getSession();
		try {
			String hql = "select pre" + " from Pregunta pre"
					+ " join pre.traducciones trad"
					+ " where trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "'"
					+ " and pre.idencuesta = " + id.toString();

			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las preguntas de una encuesta ordenadas ascendentemente
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarPreguntasOrdAsc(Long id) {

		Session session = this.getSession();
		try {
			String hql = "select pre" + " from Pregunta pre"
					+ " join pre.traducciones trad"
					+ " where trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "'"
					+ " and pre.idencuesta = " + id.toString()
					+ " order by pre.orden asc";

			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Elimina una o varias preguntas de la encuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void eliminarPreguntas(String[] idpreguntas, Long encuesta_id)
			throws DelegateException {
		
		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();
			List<Pregunta> preguntas = new ArrayList<Pregunta>();
			
			for (String idpregunta : idpreguntas) {
				
				List<?> respuestas = this.listarRespuestas(new Long(idpregunta));
				preguntas.add((Pregunta) session.get(Pregunta.class, Long.parseLong(idpregunta)));

				for (int i = 0; i < respuestas.size(); i++) {
					session.createQuery("delete from UsuarioPropietarioRespuesta where id.idrespuesta = "
							+ ((Respuesta) respuestas.get(i)).getId()).executeUpdate();
				}

				// Segundo: Borramos las respuestas de las preguntas.
				for (int i = 0; i < respuestas.size(); i++) {
					session.createQuery("delete from TraduccionRespuesta where id.codigoRespuesta = "
							+ ((Respuesta) respuestas.get(i)).getId()).executeUpdate();
					session.createQuery("delete from Respuesta where id = "
							+ ((Respuesta) respuestas.get(i)).getId()).executeUpdate();
				}

				session.createQuery("delete from TraduccionPregunta where id.codigoPregunta = "
						+ Long.parseLong(idpregunta)).executeUpdate();
				session.createQuery("delete from Pregunta where id = "
						+ Long.parseLong(idpregunta)).executeUpdate();
				
			}

			session.flush();
			tx.commit();			
			
			// Actualizamos el indice
			Encuesta enc = (Encuesta) session.get(Encuesta.class, encuesta_id);
			this.indexBorraEncuesta(enc.getId());
			this.indexInsertaEncuesta(enc, null);
			this.close(session);

			for (Pregunta p : preguntas) {
				
				// Borramos archivos asociados.
				if (p.getImagen() != null)
					DelegateUtil.getArchivoDelegate().borrarArchivo(p.getImagen().getId());
				
				// Grabamos operación de borrado.
				this.grabarAuditoria(enc.getIdmicrosite(), p, Auditoria.ELIMINAR);
				
			}

		} catch (HibernateException e) {
			
			throw new EJBException(e);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Crea o actualiza una respuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void grabarRespuesta(Respuesta res) throws DelegateException {

		Session session = this.getSession();
		try {
			boolean nuevo = (res.getId() == null) ? true : false;

			Transaction tx = session.beginTransaction();
			Map<String, TraduccionRespuesta> listaTraducciones = new HashMap<String, TraduccionRespuesta>();

			if (nuevo) {
				Iterator<TraduccionRespuesta> it = res.getTraducciones()
						.values().iterator();
				while (it.hasNext()) {
					TraduccionRespuesta trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				res.setTraducciones(null);
			}

			session.saveOrUpdate(res);
			session.flush();

			if (nuevo) {
				for (TraduccionRespuesta trad : listaTraducciones.values()) {
					trad.getId().setCodigoRespuesta(res.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				res.setTraducciones(listaTraducciones);
			}

			tx.commit();
			// Actualizamos el indice
			Encuesta enc = this.obtenerEncuesta(this.obtenerPregunta(
					res.getIdpregunta()).getIdencuesta());
			this.indexBorraEncuesta(enc.getId());
			this.indexInsertaEncuesta(enc, null);
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(enc.getIdmicrosite(), res, op);

		} catch (HibernateException e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una respuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Respuesta obtenerRespuesta(Long id) {

		Session session = this.getSession();
		try {
			Respuesta res = (Respuesta) session.get(Respuesta.class, id);
			return res;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una respuesta libre del usuario
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public RespuestaDato obtenerRespuestaDato(Long idRespuesta, Long idUsuario) {

		Session session = this.getSession();
		try {
			String hql = "from RespuestaDato res" + " where res.idusuari = "
					+ idUsuario.toString() + " and res.idrespueta = "
					+ idRespuesta.toString();

			Query query = session.createQuery(hql);
			return (RespuestaDato) query.uniqueResult();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene la llista de respuestes libre del usuario
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Hashtable<Long, RespuestaDato> listarRespuestasDato(Long idEncuesta,
			Long idUsuario) {

		Session session = this.getSession();
		try {
			String hql = "from RespuestaDato res" + " where res.idusuari = "
					+ idUsuario.toString() + " and res.idencuesta = "
					+ idEncuesta.toString();

			Query query = session.createQuery(hql);
			List<RespuestaDato> list = query.list();
            Hashtable<Long, RespuestaDato> hash = new Hashtable<Long, RespuestaDato>();
            for (RespuestaDato res : list) {
                hash.put(res.getIdrespueta(), res);
            }
			return hash;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las respuestas de una pregunta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarRespuestas(Long id) {

		Session session = this.getSession();
		try {
			String hql = "select res" + " from Respuesta res"
					+ " join res.traducciones trad"
					+ " where trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "'"
					+ " and res.idpregunta=" + id.toString();

			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * elimina una o varias respuestas de la pregunta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void eliminarRespuestas(String[] idrespuestas, Long pregunta_id)
			throws DelegateException {

		Session session = this.getSession();
		try {
			Transaction tx = session.beginTransaction();

			// Primero: En el caso que la pregunta tenga respuesta, borraremos
			// la relación de usuarios con las respuestas de la pregunta
			for (String idrespuesta : idrespuestas) {
				session.createQuery(
						"delete from UsuarioPropietarioRespuesta where id.idrespuesta = "
								+ idrespuesta).executeUpdate();
			}

			List<Respuesta> respuestas = new ArrayList<Respuesta>();
			// Segundo: Borramos las respuestas de las preguntas.
			for (String idrespuesta : idrespuestas) {
				respuestas.add((Respuesta) session.get(Respuesta.class,
						Long.parseLong(idrespuesta)));
				session.createQuery(
						"delete from TraduccionRespuesta where id.codigoRespuesta = "
								+ idrespuesta).executeUpdate();
				session.createQuery(
						"delete from Respuesta where id = " + idrespuesta)
						.executeUpdate();
			}

			session.flush();
			tx.commit();
			// Actualizamos el indice
			Encuesta enc = this.obtenerEncuesta(this.obtenerPregunta(
					pregunta_id).getIdencuesta());
			this.indexBorraEncuesta(enc.getId());
			this.indexInsertaEncuesta(enc, null);
			this.close(session);

			for (Respuesta r : respuestas) {
				this.grabarAuditoria(enc.getIdmicrosite(), r,
						Auditoria.ELIMINAR);
			}

		} catch (HibernateException e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Comprueba que el elemento pertenece al Microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public boolean checkSite(Long site, Long id) {

		Session session = this.getSession();
		try {
			Query query = session
					.createQuery("from Encuesta enc where enc.idmicrosite = "
							+ site.toString() + " and enc.id = "
							+ id.toString());
			return query.list().isEmpty();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Suma 1 al numero de veces que es respondida una respuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void sumarRespuesta(Long id) {

		Session session = this.getSession();
		try {
			Respuesta res = (Respuesta) session.get(Respuesta.class, id);
			if (res != null) {
				if (res.getNrespuestas() != null) {
					res.setNrespuestas(new Integer(res.getNrespuestas()
							.intValue() + 1));
				} else {
					res.setNrespuestas(new Integer(1));
				}
			}
			session.saveOrUpdate(res);
			session.flush();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Suma 1 al numero de veces respondida una pregunta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void sumarPregunta(Long id) {

		Session session = this.getSession();
		try {
			Pregunta pre = (Pregunta) session.get(Pregunta.class, id);
			if (pre != null) {
				if (pre.getNrespuestas() != null) {
					pre.setNrespuestas(new Integer(pre.getNrespuestas()
							.intValue() + 1));
				} else {
					pre.setNrespuestas(new Integer(1));
				}
				session.saveOrUpdate(pre);
				session.flush();
			}

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Crea o actualiza grabarUsuarioPropietarioRespuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long grabarUsuarioPropietarioRespuesta(
			UsuarioPropietarioRespuesta upm) {

		Session session = this.getSession();
		try {
			session.save(upm);
			session.flush();
			return upm.getId().getIdusuario();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtener todos los usuarios que han seleccionado una respuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> obtenerUsuariosRespuesta(Long idRespuesta) {

		Session session = this.getSession();
		try {
			String hql = " from UsuarioPropietarioRespuesta res where res.id.idrespuesta = "
					+ idRespuesta.toString();
			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtener todas las respuestas de un usuario
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> obtenerRespuestasDeUsuario(Long idUsuario) {

		Session session = this.getSession();
		try {
			String hql = "select res.id.idrespuesta from UsuarioPropietarioRespuesta res where res.id.idusuario = "
					+ idUsuario.toString();
			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene los usuarios de una encuesta.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.oper},${role.super}"
	 */
	public List<?> obtenerUsuariosEncuesta(Long id) {

		Session session = this.getSession();
		try {
			String hql = "select distinct u.id.idusuario"
					+ " from UsuarioPropietarioRespuesta u"
					+ " where u.id.idrespuesta in (" + "select r.id"
					+ " from Pregunta p, Respuesta r"
					+ " where r.idpregunta = p.id" + " and p.idencuesta = "
					+ id + ")";

			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtener el número de votos por respuesta en función de un grupo de
	 * usuarios
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Hashtable<String, String> obtenerNumVotosByResp(
			Collection<?> condicioUsu) {

		Session session = this.getSession();
		Hashtable<String, String> hash = new Hashtable<String, String>();
		try {
			String filtro = "";
			Iterator<?> iter = condicioUsu.iterator();
			while (iter.hasNext()) {
				String valor = (String) iter.next();
				filtro += "res.id.idusuario =" + valor + " OR ";
			}

			if (filtro.length() > 0) {
				filtro = filtro.substring(0, filtro.length() - 3);
			}

			if (filtro.length() > 0) {
				String hql = "select count(*), res.id.idrespuesta"
						+ " from UsuarioPropietarioRespuesta res" + " where ("
						+ filtro + ")" + " group by res.id.idrespuesta";

				Query query = session.createQuery(hql);
				Iterator<?> res = query.iterate();
				while (res.hasNext()) {
					Object[] fila = (Object[]) res.next();
					String ncount = "" + fila[0];
					String valor = "" + fila[1];

					if (valor != null && !valor.equals("null")
							&& valor.length() > 0) {
						if (ncount != null && !ncount.equals("null")
								&& ncount.length() > 0) {
							hash.put(valor, ncount);
						}
					}
				}
			}
			return hash;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/***************************************************************************************/
	/******************* INDEXACION ************************************/
	/***************************************************************************************/

	/**
	 * Añade la encuesta al indice en todos los idiomas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void indexInsertaEncuesta(Encuesta enc, ModelFilterObject filter) {

		IndexObject io = new IndexObject();
		try {
			if (filter == null) {
				filter = DelegateUtil.getMicrositeDelegate()
						.obtenerFilterObject(enc.getIdmicrosite());
			}

			if (filter != null && filter.getBuscador().equals("N")) {
				return;
			}

			IndexerDelegate indexerDelegate = DelegateUtil.getIndexerDelegate();
			for (int i = 0; i < this.langs.size(); i++) {
				String idioma = (String) this.langs.get(i);
				io = new IndexObject();

				// Configuración del writer
				Directory directory = indexerDelegate
						.getHibernateDirectory(idioma);
				IndexWriter writer = new IndexWriter(directory,
						Analizador.getAnalizador(idioma), false,
						MaxFieldLength.UNLIMITED);
				writer.setMergeFactor(20);
				writer.setMaxMergeDocs(Integer.MAX_VALUE);

				try {
					io.setId(Catalogo.SRVC_MICRO_ENCUESTAS + "." + enc.getId());
					io.setClasificacion(Catalogo.SRVC_MICRO_ENCUESTAS);

					io.setMicro(filter.getMicrosite_id());
					io.setRestringido(filter.getRestringido());
					io.setUo(filter.getUo_id());
					io.setMateria(filter.getMateria_id());
					io.setSeccion(filter.getSeccion_id());
					io.setFamilia(filter.getFamilia_id());

					io.setTitulo("");
					io.setUrl("/sacmicrofront/encuesta.do?lang=" + idioma
							+ "&idsite=" + enc.getIdmicrosite().toString()
							+ "&cont=" + enc.getId());
					io.setCaducidad("");
					io.setPublicacion("");
					io.setDescripcion("");
					io.setTituloserviciomain(filter.getTraduccion(idioma)
							.getMaintitle());

					if (enc.getFcaducidad() != null) {
						io.setCaducidad(new java.text.SimpleDateFormat(
								"yyyyMMdd").format(enc.getFcaducidad()));
					}

					if (enc.getFpublicacion() != null) {
						io.setPublicacion(new java.text.SimpleDateFormat(
								"yyyyMMdd").format(enc.getFpublicacion()));
					}

					TraduccionEncuesta trad = (TraduccionEncuesta) enc
							.getTraduccion(idioma);
					if (trad != null) {
						io.addTextLine(trad.getTitulo());
						io.setTitulo(trad.getTitulo());

						io.addTextopcionalLine(filter.getTraduccion(idioma)
								.getMateria_text());
						io.addTextopcionalLine(filter.getTraduccion(idioma)
								.getSeccion_text());
						io.addTextopcionalLine(filter.getTraduccion(idioma)
								.getUo_text());
						io.addDescripcionLine(trad.getTitulo());
					}

					Iterator<?> itpreg = enc.getPreguntas().iterator();
					while (itpreg.hasNext()) {
						Pregunta pre = (Pregunta) itpreg.next();

						TraduccionPregunta trad1 = ((TraduccionPregunta) pre
								.getTraduccion(idioma));
						if (trad1 != null) {
							io.addTextLine(trad1.getTitulo());
						}

						Iterator<?> itresp = pre.getRespuestas().iterator();
						while (itresp.hasNext()) {
							Respuesta res = (Respuesta) itresp.next();

							TraduccionRespuesta trad2 = ((TraduccionRespuesta) res
									.getTraduccion(idioma));
							if (trad2 != null) {
								io.addTextLine(trad2.getTitulo());
							}
						}
					}

					if (io.getText().length() > 0) {
						indexerDelegate.insertaObjeto(io, idioma, writer);
					}
				} finally {
					writer.close();
					directory.close();
				}
			}

		} catch (DelegateException ex) {
			log.warn("[indexInsertaEncuesta:" + enc.getId()
					+ "] No se ha podido indexar encuesta. " + ex.getMessage());
			// throw new EJBException(ex);
		} catch (Exception e) {
			log.warn("[indexInsertaEncuesta:" + enc.getId()
					+ "] No se ha podido indexar elemento. " + e.getMessage());
		}
	}

	/**
	 * Elimina la encuesta en el indice en todos los idiomas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void indexBorraEncuesta(Long id) {

		try {
			for (int i = 0; i < this.langs.size(); i++) {
				DelegateUtil.getIndexerDelegate().borrarObjeto(
						Catalogo.SRVC_MICRO_ENCUESTAS + "." + id,
						"" + this.langs.get(i));
			}

		} catch (DelegateException ex) {
			throw new EJBException(ex);
		}
	}

}
