package example;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;

/**
 * Classe che si occupa di contenere le informazioni riguardanti un singolo Example
 * che compone il dataset.
 */
public class Example implements Serializable {

    /**
     * ArrayList di Object che contiene i singoli attributi che compongono l'Example.
     */
    private ArrayList<Object> example;

    /**
	 * Costruttore di Example che inizializza a seconda della dimensione in input.
	 *
	 * @param size - Dimensione dell'Example.
	 */
    public Example(int size) {
        example = new ArrayList<Object>(size);
        for (short i = 0; i < size; i++) {
            example.add(new Object());
        }
    }

    /**
     * Metodo per settare l'Attributo nella posizione index.
     *
     * @param o - Atrributo da settare.
     * @param index - Indice da settare nell'Example.
     */
    public void set(Object o, int index) {
        example.set(index, o);
    }

    /**
     * Metodo che restituisce l'Attributo in posizione index.
     *
     * @param index - Indice dell'attributo da restituire.
     * @return Attributo all'indice index.
     */
    public Object get(int index) {
        return example.get(index);
    }

    /**
     * Metodo che scambia gli attributi dell'Example corrente con quelli dell'Example dato in input.
     *
     * @param e - Example da invertire con quello su cui si richiama il metodo.
     */
    public void swap(Example e) {
        if (example.size() != e.example.size()) {
            throw new ExampleSizeException("Example con dimensioni diverse");
        }
        for (int iObject = 0; iObject < example.size(); iObject++) {
            Object tmp = get(iObject);
            set(e.get(iObject), iObject);
            e.set(tmp, iObject);
        }
    }

    /**
     * Metodo per il calcolo della distanza tra l'Example corrente e l'Example preso in input.
     *
     * @param e - Example di cui calcolare la distanza con l'Example attuale.
     * @return Double che rappresenta il valore della distanza.
     */
    public double distance(Example e) {
        double dist = 0;
        if (example.size() != e.example.size()) {
            throw new ExampleSizeException("Example con dimensioni diverse");
        }
        Iterator<Object> exampleIt = e.example.iterator();
        for (Object o : example) {
            dist += singleDistance(o, exampleIt.next());
        }
        return dist;
    }

    /**
     * Metodo per il calcolo della singola distanza tra due Attributi singoli.
     * Il calcolo è differenziato a seconda della natura dei due attributi (Continuous o Discrete).
     *
     * @param first - Primo attributo.
     * @param second - Secondo attributo.
     * @return Double che rappresenta la distanza tra due Attributi singoli.
     */
    private double singleDistance(Object first, Object second) {

        if (first instanceof String && second instanceof String) {

            //Se sono uguali ritorno 1.0 altrimenti 0.0
            return (((String) first).compareTo((String) second) != 0) ? 1.0 : 0.0;

        } else if (first instanceof Double && second instanceof Double) {

            //Restituisco la differenza
            return Math.abs(((Double) first - (Double) second));
        }

        //Lancia l'eccezione se i due attributi confrontati non sono dello stesso tipo.
        throw new ExampleSizeException("Attributi non omogenei.");

    }

    /**
     * Metodo che restituisce la dimensione dell'Example.
     *
     * @return Double che rappresenta da dimensione dell'Example.
     */
    public int size() {
        return example.size();
    }

    /**
     * Metodo toString() sovrascritto per restituire info utili riguardanti l'Example.
     *
     * @return String che rappresenta le informazioni dell'Example.
     */
	@Override
    public String toString() {
        return "Example [example=" + example + "]";
    }

}
