package es.caib.gusite.micropersistence.ejb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Actividadagenda;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.TraduccionActividadagenda;
import es.caib.gusite.micromodel.TraduccionActividadagendaPK;
import es.caib.gusite.micropersistence.delegate.DelegateException;

/**
 * SessionBean para manipular las actividades de la agenda.
 * 
 * @ejb.bean name="sac/micropersistence/ActividadFacade"
 *           jndi-name="es.caib.gusite.micropersistence.ActividadFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */

public abstract class ActividadFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -2032209327451288969L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
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
		super.select = "select activi";
		super.from = " from Actividadagenda activi join activi.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "' and activi.idmicrosite = "
				+ site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.nombre" };
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
		super.select = "select activi";
		super.from = " from Actividadagenda activi join activi.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.nombre" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza una actividad
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarActividad(Actividadagenda activi)
			throws DelegateException {

		Session session = this.getSession();
		boolean nuevo = false;
		try {
			if (activi.getId() == null) {
				nuevo = true;
			}
			Transaction tx = session.beginTransaction();
			Map<String, TraduccionActividadagenda> listaTraducciones = new HashMap<String, TraduccionActividadagenda>();

			if (nuevo) {
				Iterator<TraduccionActividadagenda> it = activi
						.getTraducciones().values().iterator();
				while (it.hasNext()) {
					TraduccionActividadagenda trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				activi.setTraducciones(null);
			}

			session.saveOrUpdate(activi);
			session.flush();

			if (nuevo) {
				for (TraduccionActividadagenda trad : listaTraducciones
						.values()) {
					trad.getId().setCodigoActividadAgenda(activi.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				activi.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(activi, op);

			return activi.getId();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una Actividad
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Actividadagenda obtenerActividad(Long id) {

		Session session = this.getSession();
		try {
			Actividadagenda activi = (Actividadagenda) session.get(
					Actividadagenda.class, id);
			return activi;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las actividades
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarActividades() {

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
	 * Borra una actividad
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarActividad(Long id) {

		Session session = this.getSession();
		try {
			Actividadagenda activi = (Actividadagenda) session.get(
					Actividadagenda.class, id);

			session.createQuery(
					"delete from TraduccionActividadagenda tact where tact.id.codigoActividadAgenda = "
							+ id).executeUpdate();
			session.createQuery(
					"delete from Actividadagenda activi where activi.id = "
							+ id).executeUpdate();
			session.flush();
			this.close(session);

			this.grabarAuditoria(activi, Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las actividades de los eventos de la agenda para usar en
	 * Combos
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarCombo(Long idmicrosite) {

		Session session = this.getSession();
		try {
			Query query = session
					.createQuery("select activi from Actividadagenda activi join activi.traducciones trad where trad.id.codigoIdioma = '"
							+ Idioma.getIdiomaPorDefecto()
							+ "' and activi.idmicrosite = "
							+ idmicrosite.toString());
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
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
					.createQuery("from Actividadagenda activi where activi.idmicrosite = "
							+ site.toString()
							+ " and activi.id = "
							+ id.toString());
			return query.list().isEmpty();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

}
