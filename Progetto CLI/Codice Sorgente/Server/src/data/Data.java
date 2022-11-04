package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Scanner;
import database.DbAccess;
import database.InsufficientColumnNumberException;
import database.TableData;
import database.TableSchema;
import database.Column;
import example.Example;
import example.ExampleSizeException;
import java.util.LinkedList;
import java.util.Iterator;
import utility.Keyboard;

/**
 * Classe che si occupa di contenere le informazioni riguardanti il dataset.
 */
public class Data implements Serializable {

	/**
	 * Dataset, costituito da un array di oggetti istanza della classe Example.
	 */
	private LinkedList<Example> data;

	/**
	 * Array di valori della variabile target che completa il dataset.
	 */
	private LinkedList<Double> target;

	/**
	 * Numero di esempi memorizzato in data.
	 */
	private int numberOfExamples;

	/**
	 * Array delle variabili indipendenti che definiscono lo schema del dataset.
	 */
	private LinkedList<Attribute> explanatorySet;

	/**
	 * Attributo Continuo che rappresenta il target.
	 */
	private ContinuousAttribute classAttribute;

	/**
	 * Costruttore di Data per acquisire il dataset da file testuale.
	 *
	 * @param fileName - Nome del file.
	 * @throws TrainingDataException
	 */
	public Data(String fileName) throws TrainingDataException {

		File inFile = new File(fileName);

		try {

			Scanner sc = new Scanner(inFile);
			String line = sc.nextLine();
			String[] s = line.split(" ");

			//controllo sulla riga @schema
			if (!s[0].equals("@schema")) {
				sc.close();
				throw new TrainingDataException("\nErrore nello schema: '@schema' deve essere la prima parola del file.");
			} else if (s.length != 2) {
				sc.close();
				throw new TrainingDataException("\nErrore nella riga di @schema.");
			} else if (Integer.valueOf(s[1]) < 1) {
				sc.close();
				throw new TrainingDataException("\nErrore nello schema: la grandezza dello schema non e' valida.");
			}

			explanatorySet = new LinkedList<Attribute>();
			Integer size = Integer.valueOf(s[1]);

			short iAttribute = 0;
			line = sc.nextLine();
			s = line.split(" ");

			//popolamento dell'Explanatory Set
			while (!s[0].equals("@target")) {

				if (s[0].equals("@desc")) { //aggiungo l'attributo allo spazio descrittivo
					//@desc motor discrete
					if (iAttribute >= size) {
						sc.close();
						throw new TrainingDataException("\nErrore nello schema: troppi attributi.");
					} else if (s.length != 3) {
						sc.close();
						throw new TrainingDataException("\nErrore nella riga di @desc numero" + (iAttribute + 1) + ".");
					}

					if (s[2].equals("discrete")) {
						explanatorySet.add(new DiscreteAttribute(s[1], iAttribute));
					} else if (s[2].equals("continuous")) {
						explanatorySet.add(new ContinuousAttribute(s[1], iAttribute));
					} else {
						sc.close();
						throw new TrainingDataException("\nErrore nello schema: ogni attributo deve avere un tipo tra continuous e discrete.");
					}
				} else {
					sc.close();
					throw new TrainingDataException("\nErrore nello schema: @desc richiesto.");
				}
				iAttribute++;
				line = sc.nextLine();
				s = line.split(" ");

			}

			//controllo che non ci siano meno example
			if (iAttribute < getNumberOfExplanatoryAttributes()) {
				sc.close();
				throw new TrainingDataException("\nAttributi mancanti.");
			}

			//acquisizione del target
			if (s.length != 2) {
				sc.close();
				throw new TrainingDataException("\nErrore nella riga di @target.");
			}
			classAttribute = new ContinuousAttribute(s[1], iAttribute);

			line = sc.nextLine();
			s = line.split(" ");
			if (!s[0].equals("@data")) {
				sc.close();
				throw new TrainingDataException("\nErrore nello schema: @data richiesto.");
			} else if (s.length != 2) {
				sc.close();
				throw new TrainingDataException("\nErrore nella riga di @data.");
			}

			//assegnazione numero di esempi
			try {
				numberOfExamples = Integer.valueOf(s[1]);
			} catch (NumberFormatException ex) {
				sc.close();
				throw new TrainingDataException("\nErrore nello schema: numero di esempi non valido.");
			}

			if (numberOfExamples < 1) {
				sc.close();
				throw new TrainingDataException("\nErrore nello schema: numero di esempi minore di 1.");
			}

			//popolamento data e target
			data = new LinkedList<Example>();
			target = new LinkedList<Double>();

			short iRow = 0;
			while (sc.hasNextLine()) {

				Example e = new Example(explanatorySet.size());
				line = sc.nextLine();

				s = line.split(",");
				if (iRow >= numberOfExamples) {
					sc.close();
					throw new TrainingDataException("\nErrore nello schema: numero di righe maggiore.");
				}

				if (s.length != explanatorySet.size() + 1) {
					sc.close();
					throw new TrainingDataException("\nErrore nello schema: righe non uniformi allo schema dichiarato.");
				}

				for (short jColumn = 0; jColumn < s.length - 1; jColumn++) {
					if (explanatorySet.get(jColumn) instanceof DiscreteAttribute) {
						e.set(s[jColumn], jColumn);
					} else if (explanatorySet.get(jColumn) instanceof ContinuousAttribute) {
						try {
							double value = Double.valueOf(s[jColumn]);
							((ContinuousAttribute) explanatorySet.get(jColumn)).setMax(value);
							((ContinuousAttribute) explanatorySet.get(jColumn)).setMin(value);
							e.set(value, jColumn);
						} catch (NumberFormatException ex) {
							sc.close();
							throw new TrainingDataException("\nErrore nello schema: un attributo continuous non e' numerico.");
						}
					}
				}

				data.add(e);
				try {
					double value = Double.valueOf(s[s.length - 1]);
					target.add(value);
				} catch (NumberFormatException ex) {
					sc.close();
					throw new TrainingDataException("\nIl target deve essere numerico.");
				}
				iRow++;

			}

			sc.close();
			if (iRow != numberOfExamples) {
				throw new TrainingDataException("\nErrore nello schema: il numero di righe è minore.");
			}
		} catch (FileNotFoundException ex) {
			throw new TrainingDataException("\nFile non trovato.");
		} catch (NumberFormatException ex) {
			throw new TrainingDataException("\nErrore nello schema: lo schema non rispetta la posizione dei numeri.");
		}

	}

	/**
	 * Costruttore di Data per acquisire il dataset da database.
	 *
	 * @param db - DbAccess relativo al database.
	 * @param tableName - Nome della table da cui acquisire il dataset.
	 * @throws TrainingDataException
	 * @throws InsufficientColumnNumberException
	 */
	public Data(DbAccess db, String tableName) throws TrainingDataException, InsufficientColumnNumberException {
		try {

			TableSchema schema = new TableSchema(tableName, db);
			TableData tableData = new TableData(db, schema);

			explanatorySet = new LinkedList<Attribute>();

			//popolamento dell'Explanatory Set
			short iAttribute = 0;
			Iterator<Column> columnIt = schema.iterator();
			while (columnIt.hasNext()) {
				Column current = columnIt.next();
				if (current.isNumber()) {
					explanatorySet.add(new ContinuousAttribute(current.getColumnName(), iAttribute));
				} else {
					explanatorySet.add(new DiscreteAttribute(current.getColumnName(), iAttribute));
				}
				iAttribute++;
			}
			classAttribute = new ContinuousAttribute(schema.target().getColumnName(), iAttribute);

			data = new LinkedList<Example>();
			target = new LinkedList<Double>();

			Iterator<Example> examplesIt = tableData.getExamples().iterator();

			//popolamento data e target
			short iRow = 0;
			while (examplesIt.hasNext()) {
				Example e = new Example(explanatorySet.size());
				Example current = examplesIt.next();
				for (short i = 0; i < e.size(); i++) {
					if (explanatorySet.get(i) instanceof ContinuousAttribute) {
						double value = (Double) current.get(i);
						((ContinuousAttribute) explanatorySet.get(i)).setMax(value);
						((ContinuousAttribute) explanatorySet.get(i)).setMin(value);
						e.set(value, i);
					} else {
						e.set(current.get(i), i);
					}
				}
				data.add(e);
				target.add((Double) tableData.getTargetValues().get(iRow));
				iRow++;
			}

			//assegnazione numero di esempi
			numberOfExamples = iRow;

		} catch (SQLException exc) {
			System.out.println("SQLException: " + exc.getMessage());
            System.out.println("SQLState: " + exc.getSQLState());
            System.out.println("VendorError: " + exc.getErrorCode());
			throw new InsufficientColumnNumberException("Tabella non trovata.");
		}
	}

	/**
	 * Metodo per acquisire un example da tastiera.
	 *
	 * @return Example richiesto da tastiera.
	 */
	public Example readExample() {
		Example e = new Example(getNumberOfExplanatoryAttributes());
		int i = 0;
		for (Attribute a : explanatorySet) {
			if (a instanceof DiscreteAttribute) {
			System.out.print("Inserisci valore discreto X[" + i + "]:");
			e.set(Keyboard.readString(), i);
			} else {
				double x = 0.0;
				do {
					System.out.print("Inserisci valore continuo X[" + i + "]:");
					x = Keyboard.readDouble();
				} while (Double.valueOf(x).equals(Double.NaN));
				e.set(x, i);
			}
			i++;
		}
		return e;
	}

	/**
	 * Metodo per acquisire un example richiesto al Client.
	 *
	 * @param out - ObjectOutputStream per la comunicazione con il Client.
	 * @param in - ObjectInputStream per la comunicazione con il Client.
	 * @return Example richiesto al Client.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ClassCastException
	 */
	public Example readExample(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException, ClassCastException {
		Example e = new Example(getNumberOfExplanatoryAttributes());
		int i = 0;
		String risposta;
		String msg;

		for (Attribute a : explanatorySet) {

			if (a instanceof DiscreteAttribute) {
				risposta = "@READSTRING";
				out.writeObject(risposta);

				msg = "Inserisci valore discreto X[" + i + "]:";
				out.writeObject(msg);

				e.set((String) in.readObject(), i);
			} else {
				risposta = "@READDOUBLE";
				out.writeObject(risposta);

				msg = "Inserisci valore continuo X[" + i + "]:";
				out.writeObject(msg);

				e.set((Double) in.readObject(), i);
			}
			i++;

		}
		risposta = "@ENDEXAMPLE";
		out.writeObject(risposta);

		return e;
	}

	/**
	 * Metodo che converte l'esempio in un esempio scalato in base ai valori di min e max del relativo attributo.
	 *
	 * @param e - Example da convertire.
	 * @return Example convertito.
	 */
	private Example scaledExample(Example e) throws TrainingDataException {
		Example newE = new Example(explanatorySet.size());

		if (explanatorySet.size() != e.size()) {
			throw new ExampleSizeException("Lunghezza dell'example non uniforme all'ExplanatorySet.");
		}

		for (int jColumn = 0; jColumn < e.size(); jColumn++) {
			if (explanatorySet.get(jColumn) instanceof DiscreteAttribute) {
				newE.set(e.get(jColumn), jColumn);
			} else if (explanatorySet.get(jColumn) instanceof ContinuousAttribute) {
				try {
					//utilizziamo variabili locali per leggibilità//
					Double value = (Double) e.get(jColumn);
					ContinuousAttribute jAttribute = (ContinuousAttribute) explanatorySet.get(jColumn);
					newE.set(jAttribute.scale(value), jColumn);
				} catch (ClassCastException exc) {
					throw new TrainingDataException();
				}
			}
		}
		return newE;
	}

	/**
	 * Metodo che partiziona Data rispetto all'elemento x di key e restiutisce il punto di separazione.
	 *
	 * @param key - LinkedList di Double che mantiene i valori delle distanze.
	 * @param inf - Estremo inferiore dell'intervallo da ordinare.
	 * @param sup - Estremo superiore dell'intervallo da ordinare.
	 * @return Intero che rappresenta il punto di separazione tra le due parti dell'array.
	 */
	private int partition(LinkedList<Double> key, int inf, int sup) {
		int i;
		int j;

		i = inf;
		j = sup;
		int	med = (inf + sup) / 2;

		Double x = key.get(med);

		data.get(inf).swap(data.get(med));

		Double temp = target.get(inf);
		target.set(inf, target.get(med));
		target.set(med, temp);

		temp = key.get(inf);
		key.set(inf, key.get(med));
		key.set(med, temp);

		while (true) {

			while (i <= sup && key.get(i) <= x) {
				i++;
			}

			while (key.get(j) > x) {
				j--;
			}

			if (i < j) {

				data.get(i).swap(data.get(j));
				temp = target.get(i);
				target.set(i, target.get(j));
				target.set(j, temp);

				temp = key.get(i);
				key.set(i, key.get(j));
				key.set(j, temp);

			} else {
				break;
			}
		}

		data.get(inf).swap(data.get(j));

		temp = target.get(inf);
		target.set(inf, target.get(j));
		target.set(j, temp);

		temp = key.get(inf);
		key.set(inf, key.get(j));
		key.set(j, temp);

		return j;
	}

	/**
	 * Algoritmo quicksort per l'ordinamento di data usando come relazione d'ordine totale il minore uguale definita su key.
	 *
 	 * @param key - LinkedList di Double che indica le distanze.
	 * @param inf - Estremo inferiore dell'intervallo da ordinare.
	 * @param sup - Estremo superiore dell'intervallo da ordinare.
	 */
	private void quicksort(LinkedList<Double> key, int inf, int sup) {

		if (sup >= inf) {

			int pos;

			pos = partition(key, inf, sup);
			quicksort(key, inf, pos - 1);
			quicksort(key, pos + 1, sup);

		}
	}

	/**
	 * Metodo che restituisce la dimensione dell'Explanatory Set.
	 *
	 * @return Int che rappresenta la dimensione dell'Explanatory Set.
	 */
	public int getNumberOfExplanatoryAttributes() {
		return explanatorySet.size();
	}

	/**
	 * Restituisce la predizione del valore di Target, a partire dall'example e dal valore di k.
	 *
	 * @param e - Example di cui calcolare il Target.
	 * @param k - Valore di k da considerare.
	 * @return Double che rappresenta il valore del Target predetto.
	 */
	public double avgClosest(Example e, int k) {

        LinkedList<Double> key = new LinkedList<Double>();
		Iterator<Example> eIterator = data.iterator();

		//scalo l'esempio in input
		try {
			e = scaledExample(e);

			while (eIterator.hasNext()) {
				key.add(e.distance(scaledExample(eIterator.next())));
			}
		} catch (TrainingDataException exc) {
			System.out.println(exc.getMessage());
		}

		//ordina data, target e key in accordo ai valori contenuti in key (usare quicksort)
		quicksort(key, 0, numberOfExamples - 1);

		//identifica gli esempi di data che sono associati alle k distanze più piccole in key
		short istanceC = 0;
		short distanceC = 0;
		double avg = 0;
		Iterator<Double> tIterator = target.iterator();
		while (istanceC < numberOfExamples && distanceC < k) {
			avg += tIterator.next();
			istanceC++;

			while (istanceC < numberOfExamples && key.get(istanceC).equals(key.get(istanceC - 1))) {
				avg += tIterator.next();
				istanceC++;
			}
			distanceC++;
		}

		//calcolo la media
		avg = avg / istanceC;

        return avg;
    }

	/**
     * Metodo toString() sovrascritto per restituire info utili riguardanti il dataset.
     *
     * @return String che rappresenta le informazioni del dataset.
     */
	@Override
	public String toString() {
		return "Data [classAttribute=" + classAttribute + ", data=" + data + ", explanatorySet=" + explanatorySet
				+ ", numberOfExamples=" + numberOfExamples + ", target=" + target + "]";
	}

}





