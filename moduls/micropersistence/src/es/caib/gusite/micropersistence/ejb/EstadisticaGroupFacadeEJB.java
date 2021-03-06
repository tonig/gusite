package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import es.caib.gusite.micromodel.EstadisticaGroup;
import es.caib.gusite.micromodel.MicrositeCompleto;

/**
 * SessionBean para consultar estadisticas.
 * 
 * @ejb.bean name="sac/micropersistence/EstadisticaGroupFacade"
 *           jndi-name="es.caib.gusite.micropersistence.EstadisticaGroupFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class EstadisticaGroupFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 3540763968562580629L;
	protected String groupby = "";

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
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select stat.referencia ";
		super.from = " from Estadistica stat ";

		super.where = " ";
		super.whereini = " ";
		super.orderby = "";
		this.groupby = "";
		super.camposfiltro = new String[] { "stat.referencia" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Lista todas las estadisticas agrupadas por referencia
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<EstadisticaGroup> listarEstadisticasbyRefThin(Integer publico) {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			super.select = "select stat.idmicrosite, stat.referencia, SUM(stat.accesos) ";
			if (publico != null) {
				super.where += (super.where.length() > 1) ? " and " : " where ";
				super.where += " stat.publico = " + publico.intValue() + " ";
			}
			this.groupby = " group by stat.idmicrosite, stat.referencia ";
			Query query = session.createQuery(this.select + this.from
					+ this.where + this.groupby + this.orderby);
			query.setFirstResult(this.cursor - 1);
			query.setMaxResults(this.tampagina);

			ArrayList<EstadisticaGroup> lista = new ArrayList<EstadisticaGroup>();
			Iterator<?> res = query.iterate();
			int i = 0;
			while (res.hasNext()) {
				Object[] fila = (Object[]) res.next();
				EstadisticaGroup statg = new EstadisticaGroup();
				statg.setMicrosite(new MicrositeCompleto());
				statg.getMicrosite().setId((Long) fila[0]);
				statg.setReferencia((String) fila[1]);
				statg.setAccesos(Integer.parseInt("" + fila[2]));
				if (publico != null) {
					statg.setPublico(publico.intValue());
				}
				lista.add(i, statg);
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
	 * Lista todas las estadisticas agrupadas por item
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<EstadisticaGroup> listarEstadisticasbyItemThin(Integer publico) {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			super.select = "select stat.idmicrosite, stat.referencia, stat.item, SUM(stat.accesos) ";
			if (publico != null) {
				super.where += (super.where.length() > 1) ? " and " : " where ";
				super.where += " stat.publico = " + publico.intValue() + " ";
			}
			this.groupby = " group by stat.idmicrosite, stat.referencia, stat.item ";
			Query query = session.createQuery(this.select + this.from
					+ this.where + this.groupby + this.orderby);
			query.setFirstResult(this.cursor - 1);
			query.setMaxResults(this.tampagina);

			ArrayList<EstadisticaGroup> lista = new ArrayList<EstadisticaGroup>();
			Iterator<?> res = query.iterate();
			int i = 0;
			while (res.hasNext()) {
				Object[] fila = (Object[]) res.next();
				EstadisticaGroup statg = new EstadisticaGroup();
				statg.setMicrosite(new MicrositeCompleto());
				statg.getMicrosite().setId((Long) fila[0]);
				statg.setReferencia((String) fila[1]);
				statg.setItem((Long) fila[2]);
				statg.setAccesos(Integer.parseInt("" + fila[3]));
				if (publico != null) {
					statg.setPublico(publico.intValue());
				}
				lista.add(i, statg);
				i++;
			}
			return lista;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

}
