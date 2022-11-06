package example;

/**
 * Eccezioni relative alla dimensione dell'Example.
 */
public class ExampleSizeException extends RuntimeException {

    /**
     * Costruttore di classe semplice.
     */
    public ExampleSizeException() { };

    /**
     * Costruttore di classe che fornisce messaggio riguardante l'eccezione.
     *
     * @param s - Stringa che rappresenta il messaggio da fornire.
     */
    public ExampleSizeException(String s) {
        super(s);
    }

}
