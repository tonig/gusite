package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import es.caib.gusite.micromodel.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.lucene.analysis.Analizador;
import es.caib.gusite.lucene.model.Catalogo;
import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IndexerDelegate;

/**
 * SessionBean para consultar Agenda.
 *
 * @ejb.bean
 *  name="sac/micropersistence/AgendaFacade"
 *  jndi-name="es.caib.gusite.micropersistence.AgendaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */

public abstract class AgendaFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 441274285622365185L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        try {
        	super.langs= DelegateUtil.getIdiomaDelegate().listarLenguajes();

        } catch(Exception ex) {
        	throw new EJBException(ex);
        }
    }

    /**
     * Inicializo los parámetros de la consulta de Agenda.... 
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init(Long site) {
    	super.tampagina = 10;
    	super.pagina = 0;
    	super.select = "select agenda";
    	super.from = " from Agenda agenda join agenda.traducciones trad ";
    	super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "' and agenda.idmicrosite=" + site.toString();
    	super.whereini = " ";
    	super.orderby = "";

    	super.camposfiltro = new String[] {"agenda.organizador","agenda.finicio","agenda.ffin","trad.titulo"};
    	super.cursor = 0;
    	super.nreg = 0;
    	super.npags = 0;
    }      

    /**
     * Inicializo los parámetros de la consulta de Agenda.... 
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init(Long site,String idiomapasado) {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="select agenda.id,trad.titulo ,agenda.finicio,agenda.idmicrosite";
    	super.from=" from Agenda agenda join agenda.traducciones trad ";
    	super.where=" where (trad.id.codigoIdioma='"+Idioma.getIdiomaPorDefecto()+"' or trad.id.codigoIdioma='"+idiomapasado+"')  and agenda.idmicrosite="+site.toString();
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"agenda.organizador","agenda.finicio","agenda.ffin","trad.titulo"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }      
    /**
     * Inicializo los parámetros de la consulta de Agenda.... 
     * No está bien hecho debería ser Statefull
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="select agenda ";
    	super.from=" from Agenda agenda join agenda.traducciones trad ";
    	super.where=" where trad.id.codigoIdioma='"+Idioma.getIdiomaPorDefecto()+"'";
    	super.whereini=" ";
    	super.orderby="";
    	super.camposfiltro= new String[] {"agenda.organizador","agenda.finicio","agenda.ffin","trad.titulo"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }          
    
    /**
     * Crea o actualiza una agenda
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarAgenda(Agenda agenda) throws DelegateException {
        Session session = getSession();
        boolean nuevo=false;
        try {
            Transaction tx = session.beginTransaction();
            if (agenda.getId()==null) nuevo=true;
            
            Map<String, TraduccionAgenda> listaTraducciones = new HashMap<String, TraduccionAgenda>();
            
            if (nuevo) {
            	
            	Iterator<TraduccionAgenda> it = agenda.getTraducciones().values().iterator();
            	while (it.hasNext())
            	{
            		TraduccionAgenda trd = it.next();
            		listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
            	}
            	agenda.setTraducciones(null);
            }
            
            session.saveOrUpdate(agenda);
            session.flush();
            
            if (nuevo) {
	            for (TraduccionAgenda trad : listaTraducciones.values())
	            {
	            	trad.getId().setCodigoAgenda(agenda.getId());
	            	session.saveOrUpdate(trad);
	            }
	            session.flush();
	            agenda.setTraducciones(listaTraducciones);
            }
            
            tx.commit();
            //if (!nuevo) indexBorraAgenda(agenda.getId());
            //indexInsertaAgenda(agenda, null);
            Microsite site = (Microsite) session.get(Microsite.class, agenda.getIdmicrosite());
            close(session);

            Auditoria auditoria = new Auditoria();
            auditoria.setEntidad(Agenda.class.getSimpleName());
            auditoria.setIdEntidad(agenda.getId().toString());
            auditoria.setMicrosite(site);
            int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
            auditoria.setOperacion(op);
            DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);

            return agenda.getId();

        } catch (HibernateException he) {
        	if (!nuevo) indexBorraAgenda(agenda.getId());
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new DelegateException(e);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene una Agenda
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Agenda obtenerAgenda(Long id) {
        Session session = getSession();
        try {
        	Agenda agenda = (Agenda) session.get(Agenda.class, id);
            return agenda;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Lista todas las Agendas
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarAgendas() {
        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
           	
        	Query query = session.createQuery(select+from+where+orderby);
            query.setFirstResult(cursor-1);
            query.setMaxResults(tampagina);
        	return query.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Lista todas las Agenda
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<Agenda> listarAgendasrec(String idiomapasado) {
        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
        	Query query = session.createQuery(select+from+where+orderby);
        	ArrayList<Agenda> lista= new ArrayList<Agenda>(); 
        	ScrollableResults scr = query.scroll();
        	scr.first();
        	scr.scroll(cursor-1);
        	int i = 0;
            while (tampagina > i++) {
            Object[] fila = (Object[]) scr.get();
            Agenda age =  new Agenda();
            age.setIdmicrosite((Long)fila[3]);
           	age.setId((Long)fila[0]);
           	age.setFinicio((Date)fila[2]);
           	TraduccionAgenda tradage = new TraduccionAgenda();
           	tradage.setTitulo((String)fila[1]);
          	age.setTraduccion(idiomapasado,tradage);
          	lista.add(age);
          	if (!scr.next()) break;
            }
            scr.close();
        	return lista;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    /**
     * Lista todos los eventos de la agenda poniendole un idioma por defecto
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public ArrayList<Agenda> listarAgendas(String idioma) {
        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
           	
        	Query query = session.createQuery(select+from+where+orderby);
            query.setFirstResult(cursor-1);
            query.setMaxResults(tampagina);
            return crearlistadostateful(query.list(),idioma);
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }    
    
    /**
     * Lista todos las Agenda
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public ArrayList<?> listarAgendas(Date fecha, String idioma) {
        Session session = getSession();
        try {
        	//parametrosCons(); // Establecemos los parámetros de la paginación
        	java.sql.Date dt = new java.sql.Date(fecha.getTime());


        	if (where.toLowerCase().indexOf("where")!=-1) { 
        		where+= " and to_char(agenda.finicio,'yyyy-MM-dd')<='" + dt + "'"; 
        		where+= " and ( (agenda.ffin is null) OR (to_char(agenda.ffin , 'yyyy-MM-dd')>='" + dt + "') )";
        	} else {
    			where=" where to_char(agenda.finicio,'yyyy-MM-dd')<='" + dt + "'"; 
    			where+= " and ( (agenda.ffin is null) OR (to_char(agenda.ffin , 'yyyy-MM-dd')>='" + dt + "') )";        	
           	}
          	
        	parametrosCons(); // Establecemos los parámetros de la paginación
        	
        	Query query = session.createQuery(select+from+where+orderby);
            query.setFirstResult(cursor-1);
            query.setMaxResults(tampagina);
            return crearlistadostateful(query.list(),idioma);
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    } 

    /**
     * Borra una Agenda
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarAgenda(Long id) throws DelegateException {

        Session session = getSession();
        try {
        	Transaction tx = session.beginTransaction();
        	Agenda agenda = (Agenda) session.get(Agenda.class, id);
            Microsite site = (Microsite) session.get(Microsite.class, agenda.getIdmicrosite());

//            session.delete(agenda);
        	session.createQuery("delete from TraduccionAgenda tage where tage.id.codigoAgenda=" + id).executeUpdate();
        	session.createQuery("delete from Agenda age where age.id=" + id).executeUpdate();
//            indexBorraAgenda(agenda.getId());
            session.flush();
            tx.commit();
            close(session);

            Auditoria auditoria = new Auditoria();
            auditoria.setEntidad(Actividadagenda.class.getSimpleName());
            auditoria.setIdEntidad(id.toString());
            auditoria.setMicrosite(site);
            auditoria.setOperacion(Auditoria.ELIMINAR);
            DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new DelegateException(e);
        } finally {
            close(session);
        }
    }
    
    /**
     * metodo privado que devuelve un arraylist 'nuevo'.
     * Vuelca el contenido del listado e inicializa el idioma del bean.
     * @param listado
     * @param idioma
     * @return
     * @ejb.permission unchecked="true"
     */
    private ArrayList<Agenda> crearlistadostateful(List<?> listado, String idioma) {
		ArrayList<Agenda> lista = new ArrayList<Agenda>();
    	Iterator<?> iter = listado.iterator();
	    Agenda agenda;
         while (iter.hasNext()) {
        	 agenda = new Agenda();
        	 agenda = (Agenda)iter.next();
        	 agenda.setIdi(idioma);
        	 lista.add(agenda);
         }
         return lista;
    }    

    /**
     * Comprueba que el elemento pertenece al Microsite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public boolean checkSite(Long site, Long id) {
        Session session = getSession();
        try {
        	Query query = session.createQuery("from Agenda age where age.idmicrosite="+site.toString()+" and age.id="+id.toString());
        	return query.list().isEmpty();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Añade el evento al indice en todos los idiomas
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void indexInsertaAgenda(Agenda age, ModelFilterObject filter) {

		IndexObject io = new IndexObject();
		try {
		    if (filter == null) {
		        filter = DelegateUtil.getMicrositeDelegate().obtenerFilterObject(age.getIdmicrosite());
		    }

			if (filter != null && filter.getBuscador().equals("N")) {
			    return;
			}

			IndexerDelegate indexerDelegate = DelegateUtil.getIndexerDelegate();
			for (int i = 0; i < langs.size(); i++) {
				String idioma = (String) langs.get(i);
				io = new IndexObject();

				// Configuración del writer
                Directory directory = indexerDelegate.getHibernateDirectory(idioma);
                IndexWriter writer = new IndexWriter(directory, Analizador.getAnalizador(idioma), false, MaxFieldLength.UNLIMITED);
                writer.setMergeFactor(20);
                writer.setMaxMergeDocs(Integer.MAX_VALUE);

                try {
                    io.setId(Catalogo.SRVC_MICRO_EVENTOS + "." + age.getId());
                    io.setClasificacion(Catalogo.SRVC_MICRO_EVENTOS);

                    io.setMicro(filter.getMicrosite_id());
                    io.setRestringido(filter.getRestringido());
                    io.setUo(filter.getUo_id());
                    io.setMateria(filter.getMateria_id());
                    io.setSeccion(filter.getSeccion_id());
                    io.setFamilia(filter.getFamilia_id());

                    io.setTitulo("");
                    io.setCaducidad("");
                    io.setPublicacion("");
                    io.setDescripcion("");
                    io.setTituloserviciomain(filter.getTraduccion(idioma).getMaintitle());

                    if (age.getFinicio() != null) {
                        io.setUrl("/sacmicrofront/agenda.do?lang=" + idioma + "&idsite=" + age.getIdmicrosite().toString() + "&cont=" + new java.text.SimpleDateFormat("yyyyMMdd").format(age.getFinicio()));
                        io.addTextLine(new java.text.SimpleDateFormat("dd/MM/yyyy").format(age.getFinicio()));
                        io.setCaducidad(new java.text.SimpleDateFormat("yyyyMMdd").format(age.getFinicio()));
                    }

                    if (age.getFfin() != null) {
                        io.addTextLine(new java.text.SimpleDateFormat("dd/MM/yyyy").format(age.getFfin()));
                        io.setPublicacion(new java.text.SimpleDateFormat("yyyyMMdd").format(age.getFfin()));
                    }
                    io.addTextLine(age.getOrganizador());

                    TraduccionAgenda trad = ((TraduccionAgenda) age.getTraduccion(idioma));
                    if (trad != null) {
                        io.addTextLine(trad.getTitulo());

                        if (trad.getDescripcion() != null) {  // simulamos un bean Archivo con el contenido
                            Archivo archi = new Archivo();
                            archi.setMime("text/html");
                            archi.setPeso(trad.getDescripcion().length());
                            archi.setDatos(trad.getDescripcion().getBytes());
                            io.addArchivo(archi);

                            if (trad.getDescripcion().length() > 200) {
                                io.addDescripcionLine(trad.getDescripcion().substring(0, 199) + "...");
                            } else {
                                io.addDescripcionLine(trad.getDescripcion());
                            }
                        }

                        io.addTextopcionalLine(filter.getTraduccion(idioma).getMateria_text());
                        io.addTextopcionalLine(filter.getTraduccion(idioma).getSeccion_text());
                        io.addTextopcionalLine(filter.getTraduccion(idioma).getUo_text());

                        if (trad.getTitulo() != null) {
                            io.setTitulo(trad.getTitulo());
                        }

                        if (trad.getDocumento() != null) {
                            io.addArchivo(trad.getDocumento());
                        }
                    }

                    TraduccionActividadagenda trad1 = (TraduccionActividadagenda) age.getActividad().getTraduccion(idioma);
                    if (trad1 != null) {
                        io.addTextLine(trad1.getNombre());
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
		    throw new EJBException(ex);
		} catch (Exception e) {
			log.warn("[indexInsertaAgenda:" + age.getId() + "] No se ha podido indexar elemento. " + e.getMessage());
		}
	}

    /**
     * Elimina el evento en el indice en todos los idiomas
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
	public void indexBorraAgenda(Long id)  {
		
		try {
			for (int i = 0; i < langs.size(); i++) {
				DelegateUtil.getIndexerDelegate().borrarObjeto(Catalogo.SRVC_MICRO_EVENTOS + "." + id, "" + langs.get(i));
			}

		} catch (DelegateException ex) {
			throw new EJBException(ex);
		}
	}
	
}