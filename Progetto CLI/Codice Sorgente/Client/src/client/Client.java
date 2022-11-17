package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import utility.Keyboard;

/**
 * Classe che si occupa di effettuare la richiesta di predict al server.
 */
public class Client {

	/**
	 * Oggetto della classe Socket che stabilisce la connessione con il Server.
	 */
	private Socket socket = null;

	/**
	 * Oggetto della classe ObjectOutputStream che gestisce lo stream di oggetti in uscita
	 * verso il Server.
	 */
	private ObjectOutputStream out = null;

	/**
	 * Oggetto della classe ObjectInputStream che gestisce lo stream di oggetti in entrata
	 * dal Server.
	 */
	private ObjectInputStream in = null;


	/**
	 * Costruttore di classe che si occupa di inizializzare gli attributi della classe e di
	 * invocare il metodo talking().
	 *
	 * @param address - Stringa che contiene l'indirizzo IP del Server.
	 * @param port - Intero che indica la porta su cui è in ascolto il Server.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Client(String address, int port) throws IOException, ClassNotFoundException {

		socket = new Socket(address, port);
		System.out.println(socket + "\n\n");
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		talking();

	}

	/**
	 * Metodo che si occupa della comunicazione con il Server.
	 * Viene richiesta la scelta di una delle tre opzioni di acquisizione del dataset.
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void talking() throws IOException, ClassNotFoundException {

		int decision = 0;
		String menu = "";

		String answer = "";

		do {
			do {

				System.out.println("Carica KNN da file : [1]");
				System.out.println("Carica KNN da binary file : [2]");
				System.out.println("Carica KNN da database : [3] \n");

				do {
					decision = Keyboard.readInt();
					if(decision < 1 || decision > 3) {
						System.out.println("\nIl valore immesso deve essere 1,2 oppure 3, inserisci un numero valido.\n");
					}
				} while (decision < 1 || decision > 3);

				out.writeObject(decision);
				answer = (String) in.readObject();
				
				if(answer.contains("@ERROR")) {
					System.out.println("Si è verificato un errore: ");
					System.out.println(in.readObject());
					System.out.println("Immettere nuovamente la scelta: ");
				}

			} while (!answer.contains("@OK"));

			do {
				String tableName = "";
				System.out.println("\nInserire nome della tabella o file (senza l'estensione): ");
				tableName = Keyboard.readString();
				out.writeObject(tableName);
				answer = (String) in.readObject();

				if(answer.contains("@ERROR")) {
					System.out.println(in.readObject());
					System.out.println("Riprova.");
					out.writeObject(decision);
					in.readObject();
				}
			} while (answer.contains("@ERROR"));

			System.out.println("KNN caricato nel Server!");

			// predict
			String c;
			do {
				out.writeObject(4);
				boolean flag = true; //reading example
				do {
					answer = (String) (in.readObject());
					if (!answer.contains("@ENDEXAMPLE")) {
						// sto leggendo l'esempio
						String msg = (String) (in.readObject());
						if (answer.equals("@READSTRING")) {
							System.out.println(msg);
							out.writeObject(Keyboard.readString());
						} else {
							double x = 0.0;
							do {
								System.out.println(msg);
								x = Keyboard.readDouble();
							} while (new Double(x).equals(Double.NaN));
							out.writeObject(x);
						}

					} else {
						flag = false;
					}
				} while (flag);

				//sto leggendo k
				answer = (String) (in.readObject());
				int k = 0;
				do {
					System.out.print(answer);
					k = Keyboard.readInt();
				} while (k < 1);
				out.writeObject(k);

				System.out.println("Prediction:" + in.readObject());

				System.out.println("Vuoi ripetere la predizione?");
				System.out.println("Scrivi Y in caso positivo, altrimenti scrivi qualsiasi altra cosa.");
				c = Keyboard.readString();

			} while (c.toLowerCase().equals("y"));
			System.out.println("Vuoi ripetere una nuova esecuzione con un nuovo oggetto KNN?");
			System.out.println("Scrivi Y in caso positivo, altrimenti scrivi qualsiasi altra cosa.");
			menu = Keyboard.readString();
		} while (menu.toLowerCase().equals("y"));

		out.writeObject(0);

	}

	/**
	 * Main della classe Client, che fa partire la richiesta di predict al Server.
	 *
	 * @param args - I parametri devono essere l'indirizzo IP e la porta del Server.
	 */
	public static void main(String[] args) {
	

		InetAddress addr;
		try {
			addr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
			return;
		} 

		Client c;
		try {
			c = new Client(args[0], Integer.valueOf(args[1]));
		}  catch (IOException | ClassNotFoundException | NumberFormatException e) {
			System.out.println("Errore di connessione al Server...");
			System.out.println("Premi invio per chiudere il programma...");
          	new java.util.Scanner(System.in).nextLine();
			return;
		} 
	}

}
