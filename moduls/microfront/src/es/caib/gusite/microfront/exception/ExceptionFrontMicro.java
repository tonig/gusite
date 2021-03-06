package es.caib.gusite.microfront.exception;

/**
 *  Clase ExceptionFrontMicro se lanzará cuando se genere una excepción en el front al cargar un Microsite.
 * @author Indra
 */
public class ExceptionFrontMicro extends ExceptionFront {

	private static final long serialVersionUID = 6141005456421067868L;

	public ExceptionFrontMicro () {};
	
	public ExceptionFrontMicro (String msg) {
		super(msg);
	}
	
}	