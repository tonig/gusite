package es.caib.gusite.micropersistence.exception;

import javax.ejb.EJBException;

public class NombreFicheroExistenteException extends EJBException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -169238390232528392L;
	
	private String mensaje;

	public NombreFicheroExistenteException(String paramString) {
		this.mensaje = paramString;
	}

	public String getMensaje()
	{
	    return this.mensaje;
	}

	public void setMensaje(String paramString)
	{
	    this.mensaje = paramString;
    }

}
