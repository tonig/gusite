package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Estadistica;
import es.caib.gusite.micropersistence.intf.EstadisticaFacade;
import es.caib.gusite.micropersistence.intf.EstadisticaFacadeHome;
import es.caib.gusite.micropersistence.util.EstadisticaFacadeUtil;

/**
 * Business delegate para manipular estadisticas.
 * 
 * @author Indra
 */
public class EstadisticaDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -2936270744997126554L;

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	public void init() throws DelegateException {
		try {
			this.getFacade().init();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza una estadística
	 * 
	 * @param estadistica
	 * @return Long Id de la estadística
	 * @throws DelegateException
	 */
	public Long grabarEstadistica(Estadistica estadistica)
			throws DelegateException {
		try {
			return this.getFacade().grabarEstadistica(estadistica);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene una estadistica
	 * 
	 * @param id
	 *            de la estadística
	 * @return Estadistica
	 * @throws DelegateException
	 */
	public Estadistica obtenerEstadistica(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerEstadistica(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene una estadistica pasando los parametros idmicrosite,item,ref y mes
	 * 
	 * @param idmicrosite
	 *            Id del microsite
	 * @param item
	 * @param ref
	 * @param mes
	 * @param publico
	 * @return Estadistica
	 * @throws DelegateException
	 */
	public Estadistica obtenerEstadistica(Long idmicrosite, Long item,
			String ref, Integer mes, Integer publico) throws DelegateException {
		try {
			return this.getFacade().obtenerEstadistica(idmicrosite, item, ref,
					mes, publico);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las Estadistica
	 * 
	 * @return una Lista
	 * @throws DelegateException
	 */
	public List<?> listarEstadisticas() throws DelegateException {
		try {
			return this.getFacade().listarEstadisticas();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borra la Estadistica de un MicroSite
	 * 
	 * @param idmicrosite
	 * @throws DelegateException
	 */
	public void borrarEstadisticasMicroSite(Long idmicrosite)
			throws DelegateException {
		try {
			this.getFacade().borrarEstadisticasMicroSite(idmicrosite);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea la Estadistica de un MicroSite que se sobreescribe
	 * 
	 * @param antiguo
	 * @param nuevo
	 * @return Identificador
	 * @throws DelegateException
	 */
	public Long crearEstadisticasMicroReemplazado(Long antiguo, Long nuevo)
			throws DelegateException {
		try {
			return this.getFacade().crearEstadisticasMicroReemplazado(antiguo,
					nuevo);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public Hashtable<?, ?> getParametros() throws DelegateException {
		try {
			return this.getFacade().getParametros();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void parametrosCons() throws DelegateException {
		try {
			this.getFacade().parametrosCons();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getPagina() throws DelegateException {
		try {
			return this.getFacade().getPagina();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setPagina(int pagina) throws DelegateException {
		try {
			this.getFacade().setPagina(pagina);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby(String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby(orderby);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby2(String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby2(orderby);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getValorBD(String valor) throws DelegateException {
		try {
			return this.getFacade().getValorBD(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setFiltro(String valor) throws DelegateException {
		try {
			this.getFacade().setFiltro(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getWhere() throws DelegateException {
		try {
			return this.getFacade().getWhere();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setWhere(String valor) throws DelegateException {
		try {
			this.getFacade().setWhere(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getTampagina() throws DelegateException {
		try {
			return this.getFacade().getTampagina();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setTampagina(int tampagina) throws DelegateException {
		try {
			this.getFacade().setTampagina(tampagina);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private EstadisticaFacade getFacade() throws RemoteException {
		return (EstadisticaFacade) this.facadeHandle.getEJBObject();
	}

	protected EstadisticaDelegate() throws DelegateException {
		try {
			EstadisticaFacadeHome home = EstadisticaFacadeUtil.getHome();
			EstadisticaFacade remote = home.create();
			this.facadeHandle = remote.getHandle();
		} catch (NamingException e) {
			throw new DelegateException(e);
		} catch (CreateException e) {
			throw new DelegateException(e);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
}
