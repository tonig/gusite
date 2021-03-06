package es.caib.gusite.microback.base.bean;

import java.io.Serializable;

/**
 * Bean de uso genérico que guarda el par 'clave,valor'.
 * 
 * @author Indra
 * 
 */

public class Pardato implements Serializable {
	
	private static final long serialVersionUID = -6529169297752633960L;
	private String key="";
	private String value="";
	
	public Pardato() {
		
	}
	
	public Pardato(String clave, String valor) {
		this.key=clave;
		this.value=valor;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	

}
