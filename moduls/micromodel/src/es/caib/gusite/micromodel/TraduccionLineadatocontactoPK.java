package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_FRMIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionLineadatocontactoPK implements Serializable {

	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	@Column(name = "RID_CODIDI")
	private String codigoIdioma;

	@XmlAttribute
	@Column(name = "RID_FLICOD")
	private Long codigoLineadatocontacto;

	public String getCodigoIdioma() {
		return this.codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public Long getCodigoLineadatocontacto() {
		return this.codigoLineadatocontacto;
	}

	public void setCodigoLineadatocontacto(Long codigoLineadatocontacto) {
		this.codigoLineadatocontacto = codigoLineadatocontacto;
	}

	public TraduccionLineadatocontactoPK() {
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionLineadatocontactoPK)) {
			return false;
		}
		TraduccionLineadatocontactoPK castOther = (TraduccionLineadatocontactoPK) other;
		return this.codigoIdioma.equals(castOther.codigoIdioma)
				&& (this.codigoLineadatocontacto == castOther.codigoLineadatocontacto);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash
				* prime
				+ ((int) (this.codigoLineadatocontacto ^ (this.codigoLineadatocontacto >>> 32)));

		return hash;
	}
}