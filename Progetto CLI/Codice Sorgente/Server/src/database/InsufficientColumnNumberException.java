package database;

/**
 * Eccezioni relative al numero di colonne della tabella.
 */
public class InsufficientColumnNumberException extends Exception {

	/**
	 * Costruttore di classe semplice.
	 */
	public InsufficientColumnNumberException() { }

	/**
     * Costruttore di classe che fornisce messaggio riguardante l'eccezione.
     *
     * @param msg - Stringa che rappresenta il messaggio da fornire.
     */
	public InsufficientColumnNumberException(String msg) {
		super(msg);
	}
}
