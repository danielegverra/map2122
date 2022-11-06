package data;

/**
 * Classe che implementa Attribute, contiene le informazioni riguardanti
 * un attributo Continuo.
 */
class ContinuousAttribute extends Attribute  {

    /**
     * Double che memorizza il minimo valore che l'attributo assume nel dataset.
     */
    private double min;

    /**
     * Double che memorizza il massimo valore che l'attributo assume nel dataset.
     */
    private double max;

    /**
     * Costruttore di classe che inizializza gli attributi dell'oggetto.
     *
     * @param name - Nome dell'attributo.
     * @param index - Indice dell'attributo.
     */
    ContinuousAttribute(String name, int index) {
        super(name, index);

        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
    }

    /**
     * Metodo per settare l'attributo min al valore richiesto.
     *
     * @param v - Double che rappresenta il nuovo valore di min.
     */
    void setMin(double v) {
        if (v < min) {
            min = v;
        }
    }

    /**
     * Metodo per settare l'attributo max al valore richiesto.
     *
     * @param v - Double che rappresenta il nuovo valore di max.
     */
    void setMax(double v) {
        if (v > max) {
            max = v;
        }
    }

    /**
     * Metodo che si occupa di scalare il valore di value in base a min e max.
     *
     * @param value - Valore da scalare in base a min e max.
     * @return Double che rappresenta il valore scalato dell'attributo in base ai valori di min e max.
     */
    double scale(Double value) {
        return (value - min) / (max - min);
    }

}
