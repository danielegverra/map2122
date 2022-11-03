package mining;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import data.Data;
import example.Example;
import utility.Keyboard;

/**
 * Classe che si occupa di effettuare il calcolo della previsione di un attributo Target
 * all'interno di un dataset, dato uno specifico Example.
 */
public class KNN implements Serializable {

    /**
     * Attributo contenente il dataset da analizzare.
     */
    private Data data;

    /**
     * Costruttore di KNN che avvalora il training set.
     *
     * @param trainingSet - Dataset da caricare.
     */
    public KNN(Data trainingSet) {
        data = trainingSet;
    }

    /**
     * Metodo per il calcolo del valore del Target dato un Example preso da tastiera.
     *
     * @return Double che rappresenta il valore del Target calcolato.
     */
    public double predict() {
        Example e = data.readExample();
        int k = 0;
        do {
            System.out.print("Inserisci valore k >= 1: ");
            k = Keyboard.readInt();
        } while (k < 1);
        return data.avgClosest(e, k);
    }

    /**
     * Metodo per il calcolo del valore del Target dato un Example ottenuto dal Client.
     *
     * @param out - ObjectOutputStream per la comunicazione con il Client.
	 * @param in - ObjectInputStream per la comunicazione con il Client.
     * @return Double che rappresenta il valore del Target calcolato.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws ClassCastException
     */
    public double predict(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException, ClassCastException {
        Example e = data.readExample(out, in);
        int k = 0;

        out.writeObject("Inserisci valore intero per k >= 1: ");
        k = (Integer) (in.readObject());

        return data.avgClosest(e, k);
    }

    /**
     * Metodo toString() sovrascritto per restituire info utili riguardanti il KNN.
     *
     * @return String che rappresenta le informazioni del KNN.
     */
	@Override
    public String toString() {
        return "KNN [data=" + data + "]";
    }

    /**
     * Metodo per il salvataggio su file binario del KNN attuale.
     *
     * @param nomeFile - Stringa contenente il nome del file in cui salvare il KNN.
     * @throws IOException
     */
    public void salva(String nomeFile) throws IOException {
		FileOutputStream outFile = new FileOutputStream(nomeFile);
		ObjectOutputStream outstream = new ObjectOutputStream(outFile);
		outstream.writeObject(this);
        outstream.close();
	}

    /**
     * Metodo per il caricamento del KNN da file.
     *
     * @param nomeFile - Stringa contenente il nome del file da cui caricare il KNN.
     * @return Istanza di KNN contenuta nel file da caricare.
     * @throws IOException
     * @throws ClassNotFoundException
     */
	public static KNN carica(String nomeFile) throws IOException, ClassNotFoundException {

		FileInputStream inFile = new FileInputStream(nomeFile);
		ObjectInputStream inStream = new ObjectInputStream(inFile);
		KNN knn = (KNN) inStream.readObject();
		inStream.close();

		return knn;
	}

}
