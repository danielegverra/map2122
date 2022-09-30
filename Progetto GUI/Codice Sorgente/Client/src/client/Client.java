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

	private MainController control;
	
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

		this.control = m;
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

				
				while (control.getDecision() == 0) {
					sleep(100);
				}

				decision = control.getDecision();
				control.resetDecision();
				out.writeObject(decision);
				answer = (String) in.readObject();

				if (answer.contains("@ERROR")) {
					control.setErrorMsg((String) in.readObject());
					control.setCheck(false);
					control.setWaitManager(false);
				} else {

					tableName = control.getFile();
					out.writeObject(tableName);
					answer = (String) in.readObject();
					
					if(answer.contains("@ERROR")) {	
						control.setErrorMsg((String) in.readObject());
						control.setCheck(false);
						control.setWaitManager(false);
					}
				}
			} while (!answer.contains("@CORRECT"));

			control.setCheck(true);
			control.setWaitManager(false);
			System.out.println("--> KNN caricato correttamente");

			while(control.getWaitClient()) {
				sleep(100);
			}
			control.setWaitClient(true);
			PopupController popup = control.getPopupManager().getController();

			// predict
			do {
				out.writeObject(4);
				boolean flag = true; //reading example
				do {
					answer = (String) (in.readObject());
					if (!answer.contains("@ENDEXAMPLE")) {
						// sto leggendo l'esempio
						String msg = (String) (in.readObject());
						
						popup.setMsg(msg);

						popup.setWaitClient(true);
						popup.setWaitManager(false);
						if (answer.equals("@READSTRING")) {
							
							//Attendo che Manager mi dia l'attributo discreto
							while(popup.getWaitClient()) {
								sleep(100);
							}
							
							out.writeObject(popup.getTmp());
						} else {
							double x = 0.0;
							do {

								//Attendo che Manager mi dia l'attributo continuo
								while(popup.getWaitClient()) {
									sleep(100);
								}
								//Provo ad assegnare il testo a x, se invece il testo è errato
								//l'eccezione mi resetterà waitClient a true e rientrerà nel ciclo
								try {
									x = Double.valueOf(popup.getTmp());
								} catch (NumberFormatException ex) {
									/*
									 *  QUI BISOGNA LANCIARE IL POPUP DI ERRORE NEL CASO DI NUMERO INSERITO NON VALIDO
									 */
									popup.setWaitClient(true);
									popup.setWaitManager(false);
								}
								
							} while (popup.getWaitClient());
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
					
					popup.setMsg(answer);
					popup.setWaitClient(true);
					popup.setWaitManager(false);
					while(popup.getWaitClient()) {

						sleep(100);
					}
					try { 
						k = Integer.valueOf(popup.getTmp());
					} catch (NumberFormatException ex) {
						/*
						 * QUI BISOGNA LANCIARE IL POPUP DI ERRORE NEL CASO DI NUMERO INSERITO NON VALIDO
						 */
					}

				} while (k < 1);
				out.writeObject(k);
	
				String s = (String)in.readObject();
				System.out.println("--> Preidizione ottenuta: " + s);
				popup.showButton();
				popup.setPrediction(s);
				
				//Aspetto che il Manager scelga di ripetere o meno il predict 
				popup.setWaitClient(true);
				popup.setWaitManager(false);
				while (popup.getWaitClient()) {
					sleep(100);
				}
				
			} while (popup.isSameKnn());
			
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
