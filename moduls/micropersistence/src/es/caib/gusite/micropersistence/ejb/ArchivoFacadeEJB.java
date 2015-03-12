package es.caib.gusite.micropersistence.ejb;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import es.caib.gusite.lucene.analysis.Analizador;
import es.caib.gusite.lucene.model.Catalogo;
import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.IndexObject;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IndexerDelegate;
import es.caib.gusite.micropersistence.util.ArchivoUtil;

/**
 * SessionBean para obtener archivos de BD.
 * 
 * @ejb.bean name="sac/micropersistence/ArchivoFacade"
 *           jndi-name="es.caib.gusite.micropersistence.ArchivoFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */

public abstract class ArchivoFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 81125150632029055L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {

		super.ejbCreate();
		try {
			super.langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
		} catch (Exception ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Obtiene el archivo Comprobamos que pertenece al microsite o es público
	 * (microsite=0)
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Archivo obtenerArchivo(Long id) {

		Session session = this.getSession();
		Archivo archi = null;

		try {
			Query query = session.createQuery("from Archivo a where a.id="
					+ id.toString());
			if (query.list().size() == 1) {
				archi = (Archivo) query.list().get(0);
			}

			if (ArchivoUtil.almacenarEnFilesystem()) {
				this.obtenerDatosArchivoExportado(archi);
			}

			return archi;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} catch (IOException e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Devuelve una lista con todos los Ids de los archivos en la tabla
	 * GUS_DOCUS (necesario para proceso de exportación a disco).
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Object[]> obtenerTodosLosArchivosSinBlobs() {

		Session session = this.getSession();
		try {

			// amartin: order by a.idmicrosite desc, a.id asc para que el último
			// microsite sea el 0 (¿archivos comunes?).
			Query query = session
					.createQuery("select a.id, a.nombre, a.idmicrosite from Archivo a order by a.idmicrosite desc, a.id asc");
			List<Object[]> lista = query.list();

			return lista;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un archivo por el nombre Comprobamos que pertenece al microsite o
	 * es público (microsite=0)
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Archivo obtenerArchivobyName(Long site, String nombre) {

		Session session = this.getSession();
		try {
			// TODO amartin: ¿usar método checkSite() aquí, propio de este mismo
			// EJB, para ahorrar código?

			Criteria criteriID = session.createCriteria(Archivo.class);
			criteriID.add(Restrictions.eq("idmicrosite", site));
			criteriID.add(Restrictions.eq("nombre", nombre));

			List<?> listArchi = criteriID.list();
			Iterator<?> iterArchi = listArchi.iterator();
			Archivo archi = null;

			while (iterArchi.hasNext()) {
				archi = (Archivo) iterArchi.next();

				if (ArchivoUtil.almacenarEnFilesystem()) {
					this.obtenerDatosArchivoExportado(archi);
				}

				return archi;
			}

			Long site0 = new Long(0);
			Criteria criteri = session.createCriteria(Archivo.class);
			criteri.add(Restrictions.eq("idmicrosite", site0));
			criteri.add(Restrictions.eq("nombre", nombre));

			listArchi = criteri.list();
			iterArchi = listArchi.iterator();

			while (iterArchi.hasNext()) {

				archi = (Archivo) iterArchi.next();
				String hql = "from Noticia noti join noti.traducciones trad join trad.docu doc where doc.id='"
						+ archi.getId()
						+ "' and noti.idmicrosite='"
						+ site
						+ "'";
				Query query = session.createQuery(hql);
				List<?> listNot = query.list();

				if (listNot.size() == 1) {

					if (ArchivoUtil.almacenarEnFilesystem()) {
						this.obtenerDatosArchivoExportado(archi);
					}

					return archi;
				}
			}

			return archi;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} catch (IOException e) {
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
					.createQuery("from Archivo archi where (archi.idmicrosite=0 or archi.idmicrosite="
							+ site.toString()
							+ ") and archi.id="
							+ id.toString());
			return query.list().isEmpty();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Inserta un nuevo documento en la BD
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long insertarArchivo(Archivo a) throws DelegateException {

		Session session = this.getSession();
		boolean anularBlob = false;

		try {
			Transaction tx = session.beginTransaction();

			// Guardamos para obtener el ID del registro.
			session.saveOrUpdate(a);
			session.flush();

			if (ArchivoUtil.almacenarEnFilesystem()) {

				// Guardamos archivo en FS.
				ArchivoUtil.exportaArchivoAFilesystem(a);

				// Se escribirá el archivo en BD con el BLOB a null.
				a.setDatos(null);
				anularBlob = true;
			}

			if (anularBlob) {
				session.saveOrUpdate(a);
				session.flush();
			}

			tx.commit();
			return a.getId();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} catch (IOException e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Borra un documento de la BD
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarArchivo(Long id) throws DelegateException {

		Session session = this.getSession();
		try {
			Archivo a = (Archivo) session.get(Archivo.class, id);

			if (ArchivoUtil.almacenarEnFilesystem()) {
				// Si estamos guardando los archivos en el FS, comprobamos si
				// existe.
				// Si es así, lo borramos del FS.
				if (ArchivoUtil.existeArchivoEnFilesystem(a)) {
					ArchivoUtil.borrarArchivo(a);
				}
			}
			session.delete(a);
			session.flush();
			this.close(session);

			this.grabarAuditoria(a, Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} catch (IOException e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Crea o actualiza un archivo
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarArchivo(Archivo a) {

		Session session = this.getSession();
		boolean nuevo = (a.getId() == null) ? true : false;
		try {
			Transaction tx = session.beginTransaction();

			if (ArchivoUtil.almacenarEnFilesystem()) {
				// Si es una actualización, toca borrar el anterior (por si es
				// un archivo con diferente nombre)
				// antes de escribir el nuevo en el FS.
				if (!nuevo) {
					Archivo aOld = this.obtenerArchivo(a.getId());
					ArchivoUtil.borrarArchivo(aOld);
				}

				// Guardamos archivo en FS.
				ArchivoUtil.exportaArchivoAFilesystem(a);

				// Se escribirá el archivo en BD con el BLOB a null.
				a.setDatos(null);
			}

			session.saveOrUpdate(a);
			session.flush();
			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(a, op);

			return a.getId();

		} catch (HibernateException he) {
			if (!nuevo) {
				this.indexBorraArchivo(a.getId());
			}
			throw new EJBException(he);
		} catch (IOException e) {
			if (!nuevo) {
				this.indexBorraArchivo(a.getId());
			}
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

	/***************************************************************************************/
	/******************* INDEXACION ************************************/
	/***************************************************************************************/

	/**
	 * Añade un archivo al indice en todos los idiomas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void indexInsertaArchivo(Archivo archi, ModelFilterObject filter) {

		if (archi.getDatos() != null) {
			IndexObject io = new IndexObject();
			io.addArchivo(archi);

			try {
				io.setId(Catalogo.SRVC_MICRO_DOCUMENTOS + "." + archi.getId());
				io.setClasificacion(Catalogo.SRVC_MICRO_DOCUMENTOS);

				io.setMicro(filter.getMicrosite_id());
				io.setRestringido(filter.getRestringido());
				io.setUo(filter.getUo_id());
				io.setMateria(filter.getMateria_id());
				io.setSeccion(filter.getSeccion_id());
				io.setFamilia(filter.getFamilia_id());

				io.setTitulo(archi.getNombre());
				io.addTextLine(archi.getNombre());
				io.setUrl("/sacmicrofront/archivopub.do?ctrl=MCRST"
						+ io.getMicro().toString() + "ZI"
						+ archi.getId().toString() + "&id="
						+ archi.getId().toString());
				io.setCaducidad("");
				io.setPublicacion("");

				if (io.getText().length() > 200) {
					io.addDescripcionLine(io.getText().substring(0, 199)
							+ "...");
				} else {
					io.addDescripcionLine(io.getText());
				}

				IndexerDelegate indexerDelegate = DelegateUtil
						.getIndexerDelegate();
				for (int i = 0; i < this.langs.size(); i++) {
					String idioma = (String) this.langs.get(i);

					// Configuración del writer
					Directory directory = indexerDelegate
							.getHibernateDirectory(idioma);
					IndexWriter writer = new IndexWriter(directory,
							Analizador.getAnalizador(idioma), false,
							MaxFieldLength.UNLIMITED);
					writer.setMergeFactor(20);
					writer.setMaxMergeDocs(Integer.MAX_VALUE);

					try {
						indexerDelegate.insertaObjeto(io, idioma, writer);
					} finally {
						writer.close();
						directory.close();
					}
				}

			} catch (DelegateException ex) {
				throw new EJBException(ex);
			} catch (Exception e) {
				log.warn("[indexInsertaArchivo:" + archi.getId()
						+ "] No se ha podido indexar elemento. "
						+ e.getMessage());
			}
		}
	}

	/**
	 * Elimina el archivo del indice en todos los idiomas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void indexBorraArchivo(Long id) {

		try {
			IndexerDelegate indexerDelegate = DelegateUtil.getIndexerDelegate();

			for (int i = 0; i < this.langs.size(); i++) {
				indexerDelegate.borrarObjeto(Catalogo.SRVC_MICRO_DOCUMENTOS
						+ "." + id, "" + this.langs.get(i));
			}

		} catch (DelegateException ex) {
			throw new EJBException(ex);
		}
	}

	private void obtenerDatosArchivoExportado(Archivo a) throws IOException {

		// Si el archivo existe en el FS, leemos los datos de ahí.
		if (ArchivoUtil.existeArchivoEnFilesystem(a)) {
			// Obtenemos bytes del archivo asociado en FS.
			a.setDatos(ArchivoUtil.obtenerDatosArchivoEnFilesystem(a));

		} else {
			// TODO amartin: consultar si quieren que se exporte el archivo a
			// disco en este caso y luego se
			// haga el setDatos() del archivo con lo obtenido de la BD.
		}
	}

}