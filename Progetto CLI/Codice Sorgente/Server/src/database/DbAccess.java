package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che gestisce l'accesso al DB per la lettura dei dati di training.
 *
 * @author Map Tutor
 */
public class DbAccess {

	/**
	 * Stringa componente la connectionString che rappresenta il DBMS.
	 */
	private final String DBMS = "jdbc:mysql";

	/**
	 * Stringa componente la connectionString che rappresenta il Server IP.
	 */
	private final String SERVER = "localhost";

	/**
	 * Stringa componente la connectionString che rappresenta la porta del Server.
	 */
	private final int PORT = 3306;

	/**
	 * Stringa componente la connectionString che rappresenta il DB name.
	 */
	private final String DATABASE = "map";

	/**
	 * Stringa componente la connectionString che rappresenta lo UserID.
	 */
	private final String USER_ID = "Student";

	/**
	 * Stringa componente la connectionString che rappresenta la password.
	 */
	private final String PASSWORD = "map";

	/**
	 * Istanza di Connection per la connessione al DB.
	 */
	private Connection conn;

	/**
	 * Costruttore di DbAccess che inizializza una connessione al DB.
	 */
	public DbAccess() throws DatabaseConnectionException {
		String connectionString =  DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
				+ "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";

		try {
			conn = DriverManager.getConnection(connectionString, USER_ID, PASSWORD);
		} catch (SQLException e) {
			System.out.println("Impossibile connettersi al DB");
			throw new DatabaseConnectionException(e.toString());
		}
	}

	/**
	 * Metodo per ottenere l'istanza di Connection.
	 *
	 * @return Connection del DbAccess.
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * Metodo per la chiusura della Connection.
	 */
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Impossibile chiudere la connessione");
		}
	}
}
