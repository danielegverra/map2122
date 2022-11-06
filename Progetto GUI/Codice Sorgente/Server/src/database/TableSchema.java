package database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Classe che descrive il pattern della tabella.
 */
public class TableSchema implements Iterable<Column> {

	/**
	 * Lista di Colonne della Tabella.
	 */
	private List<Column> tableSchema = new ArrayList<Column>();

	/**
	 * Colonna Target della Tabella.
	 */
	private Column target;

	/**
	 * Nome della Tabella.
	 */
	private String tableName;

	/**
	 * Costruttore di TableSchema che inizializza gli attributi in maniera coerente con la
	 * lettura delle informazioni da Database (tenendo conto dei Tipi delle Colonne nel DB
	 * in relazione con i corrispondenti Attributi, Continuous o Discrete).
	 *
	 * @param tableName - Nome della tabella.
	 * @param db - DbAccess per il relativo Database.
	 * @throws SQLException
	 * @throws InsufficientColumnNumberException
	 */
	public TableSchema(String tableName, DbAccess db) throws SQLException, InsufficientColumnNumberException {
		this.tableName = tableName;

		HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
		//http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR", "string");
		mapSQL_JAVATypes.put("VARCHAR", "string");
		mapSQL_JAVATypes.put("LONGVARCHAR", "string");
		mapSQL_JAVATypes.put("BIT", "string");
		mapSQL_JAVATypes.put("SHORT", "number");
		mapSQL_JAVATypes.put("INT", "number");
		mapSQL_JAVATypes.put("LONG", "number");
		mapSQL_JAVATypes.put("FLOAT", "number");
		mapSQL_JAVATypes.put("DOUBLE", "number");

		DatabaseMetaData meta = db.getConnection().getMetaData();
	    ResultSet res = meta.getColumns(null, null, tableName, null);

	    while (res.next()) {
	        if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME"))) {
	        	if (res.isLast()) {
	        		target = new Column(
						res.getString("COLUMN_NAME"),
						mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))
					);
		 		} else {
	        		tableSchema.add(new Column(
	        			res.getString("COLUMN_NAME"),
	        			mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))
					));
				}
			}
	    }
	    res.close();
	    if (target == null || tableSchema.size() == 0) {
			throw new InsufficientColumnNumberException("La tabella selezionata contiene meno di due colonne");
		}
	}

	/**
	 * Metodo che restituisce la colonna Target.
	 *
	 * @return Column target.
	 */
	public Column target() {
		return target;
	}

	/**
	 * Metodo che restituisce il numero di Attributi.
	 *
	 * @return Intero relativo al numero di colonne.
	 */
	int getNumberOfAttributes() {
		return tableSchema.size();
	}

	/**
	 * Metodo che restituisce il nome della tabella.
	 *
	 * @return Nome della tabella.
	 */
	String getTableName() {
		return tableName;
	}

	/**
	 * Metodo che restituisce l'iteratore alla lista di Column 'tableSchema'.
	 *
	 * @return Iteratore della lista di Column tableSchema.
	 */
	public Iterator<Column> iterator() {
		return tableSchema.iterator();
	}

}
