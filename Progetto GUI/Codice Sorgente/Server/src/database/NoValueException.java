package database;

/**
 * Eccezioni relative all'assenza di valore da restituire data l'errata chiamata del metodo.
 */
public class NoValueException extends Exception {

	/**
	 * Costruttore di classe semplice.
	 */
	public NoValueException() { }

	/**
     * Costruttore di classe che fornisce messaggio riguardante l'eccezione.
     *
     * @param msg - Stringa che rappresenta il messaggio da fornire.
     */
	public NoValueException(String msg) {
		super(msg);
	}

}
