package es.caib.gusite.micropersistence.ejb;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Correo;
import es.caib.gusite.micromodel.ListaDistribucion;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

/**
 * SessionBean para consultar listaDistribucion.
 * 
 * @ejb.bean name="sac/micropersistence/ListaDistribucionFacade"
 *           jndi-name="es.caib.gusite.micropersistence.ListaDistribucionFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Salvador Antich
 */
public abstract class LDistribucionFacadeEJB extends HibernateEJB {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	 * Inicializo los parámetros de la consulta de una listaDistribucion....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(Long idDistribucion, Long microsite) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "";
		super.from = " from ListaDistribucion c ";
		super.where = " where c.id = " + idDistribucion
				+ " and c.microsite.id = " + microsite;
		super.whereini = " ";
		super.orderby = " ";
		super.camposfiltro = new String[] { "c.nombre ", "c.descripcion " };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta de ListaDistribucion....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(Long microsite) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "";
		super.from = " from ListaDistribucion c ";
		super.where = "where c.microsite.id = " + microsite;
		super.whereini = " ";
		super.orderby = " ";

		super.camposfiltro = new String[] { "c.nombre ", "c.descripcion " };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza una ListaDistribucion
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarListaDistribucion(ListaDistribucion listaDistribucion)
			throws DelegateException {

		Session session = this.getSession();
		try {
			boolean nuevo = (listaDistribucion.getId() == null) ? true : false;
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(listaDistribucion);
			session.flush();
			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(listaDistribucion, op);

			return listaDistribucion.getId();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Añade un correo a una ListaDistribucion
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public boolean anadeCorreo(Long idLista, String correo) {

		Session session = this.getSession();
		Correo auxCorreo = null;
		boolean insertat = false;
		try {
			// L'identificador de "correu" es assigned i no pot saber si es
			// creada o modificada ho feim a ma
			Transaction tx = session.beginTransaction();
			auxCorreo = (Correo) session.get(Correo.class, correo);
			if (auxCorreo == null && correo != null) {
				auxCorreo = new Correo(correo, false);
				session.save(auxCorreo);
				insertat = true;
			}
			ListaDistribucion listaDistribucion = (ListaDistribucion) session
					.get(ListaDistribucion.class, idLista);
			listaDistribucion.getDestinatarios().add(auxCorreo);
			session.update(listaDistribucion);
			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(listaDistribucion, Auditoria.MODIFICAR);

			return insertat;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Añade un correo en el sistema
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void anadeCorreo(Correo correo) {

		Session session = this.getSession();
		try {
			Transaction tx = session.beginTransaction();
			session.save(correo);
			session.flush();
			tx.commit();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Actualiza un correo
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void actualizaCorreo(Correo correo) {

		Session session = this.getSession();
		try {
			Transaction tx = session.beginTransaction();
			session.update(correo);
			session.flush();
			tx.commit();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Consulta un correo
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Correo consultaCorreo(String correo) {

		Session session = this.getSession();
		try {
			return (Correo) session.get(Correo.class, correo);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Borra un correo de una ListaDistribucion
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public boolean borrarCorreo(Long idLista, String correo) {

		Session session = this.getSession();
		Correo auxCorreo = new Correo(correo, false);
		boolean borrat = false;
		try {
			Transaction tx = session.beginTransaction();
			ListaDistribucion listaDistribucion = (ListaDistribucion) session
					.get(ListaDistribucion.class, idLista);
			if (listaDistribucion.getDestinatarios().contains(auxCorreo)) {
				listaDistribucion.getDestinatarios().remove(auxCorreo);
				borrat = true;
			}
			session.update(listaDistribucion);
			session.flush();
			tx.commit();
			return borrat;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una ListaDistribucion
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public ListaDistribucion obtenerListaDistribucion(Long id) {

		Session session = this.getSession();
		try {
			ListaDistribucion listaDistribucion = (ListaDistribucion) session
					.get(ListaDistribucion.class, id);
			return listaDistribucion;

		} catch (ObjectNotFoundException oNe) {
			log.error(oNe.getMessage());
			return new ListaDistribucion();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Comprueba si existe una ListaDistribucion
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public boolean existeListaDistribucion(Long id) {

		Session session = this.getSession();
		try {
			List<?> lista = session.createQuery(
					"from ListaDistribucion c where c.id = " + id.toString())
					.list();
			return (lista.size() > 0);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las ListaDistribucion
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarAllListaDistribucion() {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			Query query = session.createQuery(this.select + this.from
					+ this.orderby);
			query.setFirstResult(this.cursor - 1);
			query.setMaxResults(this.tampagina);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los convocatorias de un microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarListaDistribucion() throws DelegateException {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			Query query = session.createQuery(this.select + this.from
					+ this.where + this.orderby);
			query.setFirstResult(this.cursor - 1);
			query.setMaxResults(this.tampagina);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra una ListaDistribucion
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarListaDistribucion(Long id) throws DelegateException {

		Session session = this.getSession();
		try {
			Transaction tx = session.beginTransaction();
			ListaDistribucion listaDistribucion = (ListaDistribucion) session
					.get(ListaDistribucion.class, id);
			session.delete(listaDistribucion);
			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(listaDistribucion, Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * exporta una ListaDistribucion a CSV
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public StringBuffer exportarListaDistribucion(Long id)
			throws DelegateException {

		final String NEW_COL = ";";
		final String NEW_LINE = "\n";
		final String DELIMITADOR = "";

		Session session = this.getSession();
		StringBuffer sbf = new StringBuffer();
		try {
			Transaction tx = session.beginTransaction();
			ListaDistribucion listaDistribucion = (ListaDistribucion) session
					.get(ListaDistribucion.class, id);
			for (Object element : listaDistribucion.getDestinatarios()) {
				Correo correo = (Correo) element;
				if (!correo.getNoEnviar()) {
					sbf.append(DELIMITADOR + correo.getCorreo() + DELIMITADOR);
					sbf.append(NEW_COL);
					sbf.append(DELIMITADOR
							+ ((correo.getNombre() != null) ? correo
									.getNombre() : "") + DELIMITADOR);
					sbf.append(NEW_COL);
					sbf.append(DELIMITADOR
							+ ((correo.getApellidos() != null) ? correo
									.getApellidos() : "") + DELIMITADOR);
					sbf.append(NEW_LINE);
				}
			}

			session.flush();
			tx.commit();
			return sbf;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

}
