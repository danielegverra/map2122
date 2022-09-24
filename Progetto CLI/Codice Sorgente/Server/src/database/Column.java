package database;

/**
 * Classe che si occupa di memorizzare le informazioni riguardanti una singola colonna della tabella.
 */
public class Column {

	/**
	 * Nome della colonna.
	 */
	private String name;

	/**
	 * Tipo della colonna.
	 */
	private String type;

	/**
	 * Costruttore di Column per inizializzare gli attributi.
	 *
	 * @param name - Stringa che rappresenta il nome della colonna.
	 * @param type - Stringa che rappresenta il tipo della colonna.
	 */
	Column(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Metodo che restituisce il nome della colonna.
	 *
	 * @return String del nome della colonna.
	 */
	public String getColumnName() {
		return name;
	}

	/**
	 * Metodo che restituisce il booleano per sapere se la colonna è numerica.
	 *
	 * @return Boolean che ci dice se la colonna rappresenta un numero o meno.
	 */
	public boolean isNumber() {
		return type.equals("number");
	}

	/**
     * Metodo toString() sovrascritto per restituire info utili riguardanti la Column.
     *
     * @return String che rappresenta le informazioni della Column.
     */
	@Override
	public String toString() {
		return name + ":" + type;
	}
}
