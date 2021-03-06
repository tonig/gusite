package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Clase TraduccionMicrosite. Encapsula los datos que pueden tener valor en
 * diferentes idiomas del objeto Microsite.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({ TraduccionMicrositePK.class })
@Entity
@Table(name = "GUS_MICIDI")
public class TraduccionMicrosite implements Traduccion {

	private static final long serialVersionUID = -8457206824862799985L;

	@XmlElement
	@EmbeddedId
	private TraduccionMicrositePK id;

	@XmlAttribute
	@Column(name = "MID_TITULO")
	private String titulo;

	// test
	@XmlAttribute
	@Column(name = "MID_DESCRIPTION")
	private String description;

	@XmlAttribute
	@Column(name = "MID_KEYWORDS")
	private String keywords;
	// test

	@XmlAttribute
	@Column(name = "MID_TXTOP1")
	private String txtop1;

	@XmlAttribute
	@Column(name = "MID_URLOP1")
	private String urlop1;

	@XmlAttribute
	@Column(name = "MID_TXTOP2")
	private String txtop2;

	@XmlAttribute
	@Column(name = "MID_URLOP2")
	private String urlop2;

	@XmlAttribute
	@Column(name = "MID_TXTOP3")
	private String txtop3;

	@XmlAttribute
	@Column(name = "MID_URLOP3")
	private String urlop3;

	@XmlAttribute
	@Column(name = "MID_TXTOP4")
	private String txtop4;

	@XmlAttribute
	@Column(name = "MID_URLOP4")
	private String urlop4;

	@XmlAttribute
	@Column(name = "MID_TXTOP5")
	private String txtop5;

	@XmlAttribute
	@Column(name = "MID_URLOP5")
	private String urlop5;

	@XmlAttribute
	@Column(name = "MID_TXTOP6")
	private String txtop6;

	@XmlAttribute
	@Column(name = "MID_URLOP6")
	private String urlop6;

	@XmlAttribute
	@Column(name = "MID_TXTOP7")
	private String txtop7;

	@XmlAttribute
	@Column(name = "MID_URLOP7")
	private String urlop7;

	@XmlAttribute
	@Column(name = "MID_TITCAM")
	private String titulocampanya;

	@XmlAttribute
	@Column(name = "MID_SUBTCA")
	private String subtitulocampanya;

	@XmlAttribute
	@Column(name = "MID_CABPER")
	private String cabecerapersonal;

	@XmlAttribute
	@Column(name = "MID_PIEPER")
	private String piepersonal;

	public String getTitulocampanya() {
		return this.titulocampanya;
	}

	public void setTitulocampanya(String titulocampanya) {
		this.titulocampanya = titulocampanya;
	}

	// test
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	// test

	public TraduccionMicrosite() {
	}

	public String getTxtop1() {
		return this.txtop1;
	}

	public void setTxtop1(String txtop1) {
		this.txtop1 = txtop1;
	}

	public String getTxtop2() {
		return this.txtop2;
	}

	public void setTxtop2(String txtop2) {
		this.txtop2 = txtop2;
	}

	public String getTxtop3() {
		return this.txtop3;
	}

	public void setTxtop3(String txtop3) {
		this.txtop3 = txtop3;
	}

	public String getTxtop4() {
		return this.txtop4;
	}

	public void setTxtop4(String txtop4) {
		this.txtop4 = txtop4;
	}

	public String getTxtop5() {
		return this.txtop5;
	}

	public void setTxtop5(String txtop5) {
		this.txtop5 = txtop5;
	}

	public String getTxtop6() {
		return this.txtop6;
	}

	public void setTxtop6(String txtop6) {
		this.txtop6 = txtop6;
	}

	public String getTxtop7() {
		return this.txtop7;
	}

	public void setTxtop7(String txtop7) {
		this.txtop7 = txtop7;
	}

	public String getUrlop1() {
		return this.urlop1;
	}

	public void setUrlop1(String urlop1) {
		this.urlop1 = urlop1;
	}

	public String getUrlop2() {
		return this.urlop2;
	}

	public void setUrlop2(String urlop2) {
		this.urlop2 = urlop2;
	}

	public String getUrlop3() {
		return this.urlop3;
	}

	public void setUrlop3(String urlop3) {
		this.urlop3 = urlop3;
	}

	public String getUrlop4() {
		return this.urlop4;
	}

	public void setUrlop4(String urlop4) {
		this.urlop4 = urlop4;
	}

	public String getUrlop5() {
		return this.urlop5;
	}

	public void setUrlop5(String urlop5) {
		this.urlop5 = urlop5;
	}

	public String getUrlop6() {
		return this.urlop6;
	}

	public void setUrlop6(String urlop6) {
		this.urlop6 = urlop6;
	}

	public String getUrlop7() {
		return this.urlop7;
	}

	public void setUrlop7(String urlop7) {
		this.urlop7 = urlop7;
	}

	public String getSubtitulocampanya() {
		return this.subtitulocampanya;
	}

	public void setSubtitulocampanya(String subtitulocampanya) {
		this.subtitulocampanya = subtitulocampanya;
	}

	public String getCabecerapersonal() {
		return this.cabecerapersonal;
	}

	public void setCabecerapersonal(String cabecerapersonal) {
		this.cabecerapersonal = cabecerapersonal;
	}

	public String getPiepersonal() {
		return this.piepersonal;
	}

	public void setPiepersonal(String piepersonal) {
		this.piepersonal = piepersonal;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public TraduccionMicrositePK getId() {
		return this.id;
	}

	public void setId(TraduccionMicrositePK id) {
		this.id = id;
	}

}