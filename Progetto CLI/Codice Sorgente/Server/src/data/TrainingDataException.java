package data;

/**
 * Eccezioni relative al Training Set.
 */
public class TrainingDataException extends Exception {

    /**
     * Costruttore di classe semplice.
     */
    public TrainingDataException() { }

    /**
     * Costruttore di classe che fornisce messaggio riguardante l'eccezione.
     *
     * @param s - Stringa che rappresenta il messaggio da fornire.
     */
    public TrainingDataException(String s) {
        super(s);
    }

}
