package data;

import java.io.Serializable;

/**
 * Classe astratta che si occupa di contenere le informazioni essenziali sugli attributi
 * del nostro dataset.
 */
abstract class Attribute implements Serializable {

    /**
     * Stringa contenente il nome dell'attributo.
     */
    private String name;

    /**
     * Intero che memorizza l'indice dell'attributo all'interno del dataset.
     */
    private int index;

    /**
     * Costruttore di classe che inizializza gli attributi dell'oggetto.
     *
     * @param n - Nome dell'attributo.
     * @param i - Indice dell'attributo.
     */
    Attribute(String n, int i) {
        this.name = n;
        this.index = i;
    }

    /**
     * Metodo che restituisce il nome dell'attributo.
     *
     * @return String contenente il nome dell'attributo.
     */
    String getName() {
        return name;
    }

    /**
     * Metodo che restituisce l'indice dell'attributo.
     *
     * @return Intero che rappresenta l'indice dell'attributo.
     */
    int getIndex() {
        return index;
    }

    /**
     * Metodo toString() sovrascritto per restituire info utili riguardanti l'attributo.
     *
     * @return String che rappresenta le informazioni dell'attributo.
     */
    @Override
    public String toString() {
        return "Attribute [index=" + index + ", name=" + name + "]";
    }

}

