package client;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import controller.MainController;
import controller.PopupController;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Classe che si occupa di effettuare la richiesta di predict al server.
 */
public class Client extends Thread {

	private MainController m;
	
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
	public Client(String address, int port, MainController m) throws IOException, ClassNotFoundException {

		this.m = m;
		socket = new Socket(address, port);
		//System.out.println(socket + "\n");
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

	}

	/**
	 * Metodo che si occupa della comunicazione con il Server.
	 * Viene richiesta la scelta di una delle tre opzioni di acquisizione del dataset.
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */

	public void run() {
		
		try {
			talking();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void talking() throws IOException, ClassNotFoundException, InterruptedException {

		int decision = 0;
		String answer = "";

		while (true) {
			do {
				String tableName = "";

				
				while (m.getDecision() == 0 || m.getFile().equals("NULL")) {
					sleep(100);
				}

				decision = m.getDecision();
				out.writeObject(decision);
				answer = (String) in.readObject();

				if (answer.contains("@ERROR")) {
					m.setErrorMsg((String) in.readObject());
					m.setCheck(false);
					m.setWaitManager(false);
					//System.out.println("Si è verificato un errore: ");
					//System.out.println(in.readObject());
					//System.out.println("Riprova: ");
					m.resetFile();
				} else {

					tableName = m.getFile();
					m.resetFile();
					out.writeObject(tableName);
					answer = (String) in.readObject();
					
					if(answer.contains("@ERROR")) {	
						m.setErrorMsg((String) in.readObject());
						m.setCheck(false);
						m.setWaitManager(false);
						//System.out.println("Si è verificato un errore: ");
						//System.out.println(in.readObject());
						//System.out.println("Riprova: ");
						m.resetFile();
					}
				}
			} while (!answer.contains("@CORRECT"));


			
			m.setCheck(true);
			m.setWaitManager(false);
			System.out.println("--> KNN caricato correttamente");

			while(m.getWaitClient()) {
				sleep(100);
			}
			m.setWaitClient(true);
			PopupController pm = m.getPopupManager().getController();

			// predict
			do {
				out.writeObject(4);
				boolean flag = true; //reading example
				do {
					answer = (String) (in.readObject());
					if (!answer.contains("@ENDEXAMPLE")) {
						// sto leggendo l'esempio
						String msg = (String) (in.readObject());
						//System.out.println(msg);
						pm.setMsg(msg);

						pm.setWaitClient(true);
						pm.setWaitManager(false);
						if (answer.equals("@READSTRING")) {
							
							//out.writeObject(Keyboard.readString());

							//Attendo che Manager mi dia l'attributo discreto
							while(pm.getWaitClient()) {
								sleep(100);
							}
							
							out.writeObject(pm.getTmp());
							//pm.setWaitClient(true);
						} else {
							double x = 0.0;
							do {

								//Attendo che Manager mi dia l'attributo continuo
								while(pm.getWaitClient()) {
									sleep(100);
								}
								//Provo ad assegnare il testo a x, se invece il testo è errato
								//l'eccezione mi resetterà waitClient a true e rientrerà nel ciclo
								try {
									x = Double.valueOf(pm.getTmp());
								} catch (NumberFormatException ex) {
									/*
									 *  QUI BISOGNA LANCIARE IL POPUP DI ERRORE NEL CASO DI NUMERO INSERITO NON VALIDO
									 */
									pm.setWaitClient(true);
									pm.setWaitManager(false);
								}
								//pm.setWaitClient(true);
							} while (pm.getWaitClient());
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
					//System.out.print(answer);
					pm.setMsg(answer);
					pm.setWaitClient(true);
					pm.setWaitManager(false);
					while(pm.getWaitClient()) {

						sleep(100);
					}
					try { 
						k = Integer.valueOf(pm.getTmp());
					} catch (NumberFormatException ex) {
						/*
						 * QUI BISOGNA LANCIARE IL POPUP DI ERRORE NEL CASO DI NUMERO INSERITO NON VALIDO
						 */
					}

				} while (k < 1);
				out.writeObject(k);
	
				String s = (String)in.readObject();
				System.out.println("--> Preidizione ottenuta: " + s);
				pm.showButton();
				pm.setPrediction(s);

				//System.out.println("Vuoi ripetere la predizione?");
				//System.out.println("Scrivi Y in caso positivo, altrimenti scrivi qualsiasi altra cosa.");
				
				//Aspetto che il Manager scelga di ripetere o meno il predict 
				pm.setWaitClient(true);
				pm.setWaitManager(false);
				while (pm.getWaitClient()) {
					sleep(100);
				}
				

			} while (pm.isSameKnn());
			//System.out.println("Vuoi ripetere una nuova esecuzione con un nuovo oggetto KNN?");
			//System.out.println("Scrivi Y in caso positivo, altrimenti scrivi qualsiasi altra cosa.");
			
		}

		/*
		 * QUESTA PARTE DEL CODICE NON VENIVA RAGGIUNTA MAI CON LA GUI
		 * QUINDI COSA FARE???
		 */

		//out.writeObject(0);

	}

	//DOVE VIENE CHIAMATO??????
	void openPopup() throws IOException {
		Stage stage;
		Parent root;

		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("popup.fxml"));
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	
}
