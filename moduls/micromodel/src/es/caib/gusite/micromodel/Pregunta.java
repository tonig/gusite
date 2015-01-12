package es.caib.gusite.micromodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Clase Pregunta. Bean que define una Pregunta de una Encuesta
 * Modela la tabla de BBDD GUS_PREGUN.
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_PREGUN")
public class Pregunta implements Traducible2 {

	private static final long serialVersionUID = 8513598333939006319L;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_PREGUNTA_ID_GENERATOR", sequenceName="GUS_SEQPRE", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_PREGUNTA_ID_GENERATOR")
	@Column(name="PRE_CODI")
	private Long id;

    @XmlAttribute
	@Column(name="PRE_ENCCOD")
    private Long idencuesta;

    @XmlElement
    @ManyToOne
	@JoinColumn(name="PRE_IMAGEN")
    private Archivo imagen;

    @XmlAttribute
    @Column(name="PRE_MULTIR")
    private String multiresp;

    @XmlAttribute
    @Column(name="PRE_VISCMP")
    private String visiblecmp;

    @XmlAttribute
    @Column(name="PRE_OBLIGA")
    private String obligatorio;

    @XmlAttribute
    @Column(name="PRE_VISIB")
    private String visible;

    @XmlAttribute
    @Column(name="PRE_ORDEN")
    private Integer orden;

    @XmlAttribute
    @Column(name="PRE_NRESP")
    private Integer nrespuestas;

    @XmlAttribute
    @Column(name="PRE_MINCONT")
    private Integer minContestadas;

    @XmlAttribute
    @Column(name="PRE_MAXCONT")
    private Integer maxContestadas;

    @XmlElement
    @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
    @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name="RES_PRECOD")
    @MapKey
	@OrderBy("orden ASC")
	@Fetch(FetchMode.SUBSELECT)
    private List<Respuesta> respuestas = new ArrayList();

  	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
  	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
  	@JoinColumn(name="PID_PRECOD")
  	@MapKey(name="id.codigoIdioma")
  	private Map<String, TraduccionPregunta> traducciones = new HashMap<String, TraduccionPregunta>();

  	@Transient
  	private String idi = Idioma.getIdiomaPorDefecto();;

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)
    public Map<String, TraduccionPregunta> getTranslates() {
        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionPregunta> traducciones) {
        this.traducciones = traducciones;
    }

    public Map<String, TraduccionPregunta> getTraducciones() {
        return traducciones;
    }

    public void setTraducciones(Map traducciones) {
        this.traducciones = traducciones;
    }

    public Long getId() {
		return id;
	}
    
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getIdencuesta() {
		return idencuesta;
	}
	
	public void setIdencuesta(Long idencuesta) {
		this.idencuesta = idencuesta;
	}
	
	public Archivo getImagen() {
		return imagen;
	}
	
	public void setImagen(Archivo imagen) {
		this.imagen = imagen;
	}
	
	public String getMultiresp() {
		return multiresp;
	}
	
	public void setMultiresp(String multiresp) {
		this.multiresp = multiresp;
	}
	
	public Integer getNrespuestas() {
		return nrespuestas;
	}
	
	public void setNrespuestas(Integer nrespuestas) {
		this.nrespuestas = nrespuestas;
	}
	
	public String getObligatorio() {
		return obligatorio;
	}
	
	public void setObligatorio(String obligatorio) {
		this.obligatorio = obligatorio;
	}
	
	public Integer getOrden() {
		return orden;
	}
	
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	
	public String getVisible() {
		return visible;
	}
	
	public void setVisible(String visible) {
		this.visible = visible;
	}
	
	public String getVisiblecmp() {
		return visiblecmp;
	}
	
	public void setVisiblecmp(String visiblecmp) {
		this.visiblecmp = visiblecmp;
	}

	public List<Respuesta> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<Respuesta> respuestas) {
		this.respuestas = respuestas;
	}

	// Metodos para poder leer las colecciones del XML
	
	public void addRespuestas(Respuesta resp) {
		respuestas.add(resp);
	}
	
	public void addTraduccionMap(String lang, TraduccionPregunta traduccion) {
        setTraduccion(lang, traduccion);
    }

	/**
	 * @return the minContestadas
	 */
	public Integer getMinContestadas() {
		return minContestadas;
	}

	/**
	 * @param minContestadas the minContestadas to set
	 */
	public void setMinContestadas(Integer minContestadas) {
		this.minContestadas = minContestadas;
	}

	/**
	 * @return the maxContestadas
	 */
	public Integer getMaxContestadas() {
		return maxContestadas;
	}

	/**
	 * @param maxContestadas the maxContestadas to set
	 */
	public void setMaxContestadas(Integer maxContestadas) {
		this.maxContestadas = maxContestadas;
	}

	public Traduccion getTraduccion() {
		return (Traduccion) traducciones.get(Idioma.getIdiomaPorDefecto());
	}

	public Traduccion getTraduccion(String idioma) {
		return (Traduccion) traducciones.get(idioma);
	}

	public void setTraduccion(String idioma, Traduccion traduccion) {
        if (traduccion == null) {
            traducciones.remove(idioma);
        } else {
            traducciones.put(idioma, (TraduccionPregunta)traduccion);
        }
	}

	public Traduccion getTraduce() {
		return (Traduccion) traducciones.get(idi);
	}

	public Map getTraduccionMap() {
		return traducciones;
	}

	public void setTraduccionMap(Map traduccionMap) {
		this.traducciones = new HashMap(traduccionMap);
		
	}

	public void setIdi(String idi) {
		this.idi=idi;
	}
}