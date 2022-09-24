package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import example.Example;

/**
 * Classe che si occupa di gestire le transazioni relative a una Tabella e ne memorizza le righe.
 */
public class TableData {

	/**
	 * DbAccess relativo al DB.
	 */
	private DbAccess db;

	/**
	 * Nome della tabella.
	 */
	private String table;

	/**
	 * TableSchema che descrive la tabella.
	 */
	private TableSchema tSchema;

	/**
	 * Lista di Example che compone la tabella.
	 */
	private ArrayList<Example> transSet;

	/**
	 * Lista di Target della tabella.
	 */
	private ArrayList<Object> target;

	/**
	 * Costruttore di TableData che inizializza gli attributi leggendo dal DB.
	 *
	 * @param db - DbAccess per l'accesso al DB.
	 * @param tSchema - TableSchema che descrive la tabella.
	 * @throws SQLException
	 * @throws InsufficientColumnNumberException
	 */
	public TableData(DbAccess db, TableSchema tSchema) throws SQLException, InsufficientColumnNumberException {
		this.db = db;
		this.tSchema = tSchema;
		this.table = tSchema.getTableName();
		transSet = new ArrayList<Example>();
		target = new ArrayList<Object>();
		init();
	}

	/**
	 * Metodo ausiliario al costruttore che legge la tabella dal DB mediante una Query
	 * e cataloga le informazioni all'interno degli attributi di TableData.
	 *
	 * @throws SQLException
	 */
	private void init() throws SQLException {
		String query = "select ";

		for (Column c : tSchema) {
			query += c.getColumnName();
			query += ",";
		}
		query += tSchema.target().getColumnName();
		query += (" FROM " + table);

		Statement statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
			Example currentTuple = new Example(tSchema.getNumberOfAttributes());
			int i = 0;
			for (Column c : tSchema) {
				if (c.isNumber()) {
					currentTuple.set(rs.getDouble(i + 1), i);
				} else {
					currentTuple.set(rs.getString(i + 1), i);
				}
				i++;
			}
			transSet.add(currentTuple);

			if (tSchema.target().isNumber()) {
				target.add(rs.getDouble(tSchema.target().getColumnName()));
			} else {
				target.add(rs.getString(tSchema.target().getColumnName()));
			}
		}
		rs.close();
		statement.close();
	}

	/**
	 * Metodo che restituisce la Lista di Example.
	 *
	 * @return ArrayList di Example della tabella.
	 */
	public ArrayList<Example> getExamples() {
		return transSet;
	}

	/**
	 * Metodo che restituisce la Lista di Target.
	 *
	 * @return ArrayList di Target della Tabella
	 */
	public ArrayList<Object> getTargetValues() {
		return target;
	}

	/**
	 * Metodo per l'esecuzione di una Query di prova al dataset.
	 *
	 * @param column - Colonna numerica da calcolare.
	 * @param aggregate - MIN o MAX
	 * @return Valore del Minimo o Massimo della colonna.
	 * @throws NoValueException
	 */
	Object getAggregateColumnValue(Column column, QUERY_TYPE aggregate) throws NoValueException {

		Double res = 0.0;

		if (column.isNumber()) {
			try {
				Statement s = db.getConnection().createStatement();

				ResultSet r  = s.executeQuery(
					"SELECT " + aggregate
					+ "(" + column.getColumnName() + ") "
					+ "FROM " + table + ";"
				);

				while (r.next()) {
					res = r.getDouble(1);
				}
				s.close();
			} catch (SQLException e) {
				System.out.println("SQLException" + e.getMessage());
				System.out.println("SQLState" + e.getSQLState());
				System.out.println("VendorError" + e.getErrorCode());
			}
		} else {

			//assenza di valore per colonne non numeriche
			throw new NoValueException("Colonna non numerica.");
		}

		return res;
	}
}
