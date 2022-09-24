package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.Data;
import data.TrainingDataException;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.InsufficientColumnNumberException;
import mining.KNN;

/**
 * Classe per la gestione della comunicazione con un singolo Client.
 */
class ServerOneClient extends Thread {

    /**
     * Socket per la comunicazione con il Client.
     */
    private Socket socket;

    /**
     * ObjectInputStream per la ricezione di Object dal Client.
     */
    private ObjectInputStream in;

    /**
     * ObjectOutputStream per la trasmissione di Object al Client.
     */
    private ObjectOutputStream out;

    /**
     * Costruttore di ServerOneClient che inizializza gli attributi socket, in e out e avvia il thread.
     *
     * @param s
     * @throws IOException
     */
    ServerOneClient(Socket s) throws IOException {

        socket = s;
        out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
        start();
    }

    /**
     * Metodo per l'avvio del thread e per l'effettiva gestione delle richieste del CLient
     * associato all'istanza corrente di ServerOneClient.
     */
    @Override
    public void run() {
        try {

            int decision;
            String tableName;
            KNN knn = null;
            String answer = "";
            String err = "";

            while (!answer.contains("@END")) {
                decision = (Integer) in.readObject();

                switch (decision) {

                    //file di testo
                    case 1 :
                    out.writeObject("@OK");
                    tableName = (String) in.readObject();
                    try {
                        knn = new KNN(new Data("../../../File/" + tableName + ".dat"));
                        try {
                            knn.salva("../../../File/" + tableName + ".dmp");
                        } catch (IOException exc) {
                            System.out.println(exc.getMessage());
                        }
                        answer = "@CORRECT";
                    } catch (TrainingDataException exc) {
                        System.err.println(exc.getMessage());
                        err = exc.getMessage();
                        answer = "@ERROR";
                    }
                    out.writeObject(answer);
                    break;

                    //file binario
                    case 2 :
                    out.writeObject("@OK");
                    tableName = (String) in.readObject();
                    try {
                        knn = KNN.carica("../../../File/" + tableName + ".dmp");
                        answer = "@CORRECT";
                    } catch (IOException | ClassNotFoundException exc) {
                        System.err.println(exc.getMessage());
                        err = exc.getMessage();
                        answer = "@ERROR";
                    }
                    out.writeObject(answer);
                    break;

                    //database
                    case 3 :
                    DbAccess db = null;
                    try {
                        db = new DbAccess();
                        out.writeObject("@OK");
                        tableName = (String) in.readObject();
                        System.out.println(tableName);
                        knn = new KNN(new Data(db, tableName));
                        System.out.println(knn);
                        try {
                            knn.salva("../../../File/" + tableName + "DB.dmp");
                        } catch (IOException exc) {
                            System.out.println(exc.getMessage());
                        }
                        db.closeConnection();
                        answer = "@CORRECT";
                    } catch (DatabaseConnectionException exc) {
                        System.err.println(exc.getMessage());
                        err = exc.getMessage();
                        answer = "@ERROR";
                    } catch (TrainingDataException | InsufficientColumnNumberException exc) {
                        System.err.println(exc.getMessage());
                        err = exc.getMessage();
                        answer = "@ERROR";
                        db.closeConnection();
                    }
                    out.writeObject(answer);
                    break;

                    //calcolo del predict
                    case 4 :
                    answer = String.valueOf(knn.predict(out, in));
                    out.writeObject(answer);
                    break;

                    //exit
                    case 0 :
                    answer = "@END";
                    out.writeObject(answer);
                    break;

                    //errore di immissione
                    default :
                    answer = "@notOK";
                    out.writeObject(answer);
                    break;

                }

                if (answer.contains("@ERROR")) {
                    out.writeObject(err);
                }

            }

        } catch (IOException exc) {
            System.out.println("\nErrore nella comunicazione con il Client.");
        } catch (ClassNotFoundException exc) {
            System.out.println("\nErrore nel casting dei dati.");
        }

    }

}
