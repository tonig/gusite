package es.caib.gusite.micropersistence.search;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import es.caib.gusite.micromodel.Indexable;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IndexerDelegate;

/**
 * Clase IndexerInterceptor implementa Interceptor
 * 
 * @author INDRA
 */
public class IndexerInterceptor implements Interceptor {

	protected static final Log log = LogFactory
			.getLog(IndexerInterceptor.class);

	private IndexerDelegate indexerDelegate = null;

	private IndexerDelegate getIndexerDelegate() {

		if (this.indexerDelegate == null) {
			this.indexerDelegate = DelegateUtil.getIndexerDelegate();
		}

		return this.indexerDelegate;

	}

	public IndexerInterceptor() {
	}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {
		return false;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) throws CallbackException {
		if (entity instanceof Indexable) {
			try {
				this.getIndexerDelegate().indexarObjeto(entity);
			} catch (DelegateException e) {
				log.error("onFlushDirty: " + e.getMessage());
			}
		}
		return false;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {
		if (entity instanceof Indexable) {
			try {
				this.getIndexerDelegate().indexarObjeto(entity);
			} catch (DelegateException e) {
				log.error("onSave: " + e.getMessage());
			}
		}
		return false;
	}

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {
		if (entity instanceof Indexable) {
			try {
				this.getIndexerDelegate().desindexarObjeto(entity);
			} catch (DelegateException e) {
				log.error("onDelete: " + e.getMessage());
			}
		}
	}

	@Override
	public void preFlush(Iterator entities) throws CallbackException {
	}

	@Override
	public void postFlush(Iterator entities) throws CallbackException {
	}

	public Boolean isUnsaved(Object entity) {
		return null;
	}

	@Override
	public int[] findDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		return null;
	}

	public Object instantiate(Class clazz, Serializable id)
			throws CallbackException {
		return null;
	}

	@Override
	public void afterTransactionBegin(Transaction arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTransactionCompletion(Transaction arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTransactionCompletion(Transaction arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getEntity(String arg0, Serializable arg1)
			throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntityName(Object arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object instantiate(String arg0, EntityMode arg1, Serializable arg2)
			throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isTransient(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCollectionRecreate(Object arg0, Serializable arg1)
			throws CallbackException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollectionRemove(Object arg0, Serializable arg1)
			throws CallbackException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollectionUpdate(Object arg0, Serializable arg1)
			throws CallbackException {
		// TODO Auto-generated method stub

	}

	@Override
	public String onPrepareStatement(String arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
}