package database;

/**
 * Eccezioni riguardanti la connessione al DB.
 */
public class DatabaseConnectionException extends Exception {

	/**
     * Costruttore di classe semplice.
     */
	DatabaseConnectionException() { }

	/**
     * Costruttore di classe che fornisce messaggio riguardante l'eccezione.
     *
     * @param msg - Stringa che rappresenta il messaggio da fornire.
     */
	DatabaseConnectionException(String msg) {
		super(msg);
	}
}
