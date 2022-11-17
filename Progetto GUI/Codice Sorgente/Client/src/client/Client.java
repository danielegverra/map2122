package client;

import controller.MainController;
import controller.PopupController;
import controller.ShowPredictionController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Classe che si occupa di effettuare la richiesta di predict al server.
 */
public class Client extends Thread {

	/**
	 * Oggetto della classe MainController legato alla schermata principale
	 * dell'interfaccia.
	 */
	private MainController control;

	/**
	 * Oggetto della classe PopupController legato alla schermata del popup
	 * di immissione della predizione.
	 */
	private PopupController popup;

	/**
	 * Oggetto della classe ShowPredictionController legato alla schermata del popup
	 * di visualizzazione del risultato della predizione.
	 */
	private ShowPredictionController prediction;

	/**
	 * Booleano che segnala se il popup è aperto o meno.
	 * Esso è necessario per la corretta gestione della chiusura del
	 * popup o della schermata principale, a seconda del momento in cui
	 * si presenta un errore.
	 */
	private boolean isOpenedPopup;

	/**
	 * Booleano che segnala quando il Thread ha bisogno di essere chiuso,
	 * operazione necessaria per non permettere che più thread della classe
	 * siano aperti se non necessario.
	 */
	private boolean doClose;
	
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
	 * @param m - MainController della schermata legata al Client.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Client(String address, int port, MainController m) throws IOException, ClassNotFoundException {
		this.control = m;
		socket = new Socket(address, port);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}

	/**
	 * Metodo della classe Thread che si occupa di richiamare il metodo 
	 * principale della classe Client.
	 */
	@Override
	public void run() {
		
		try {
			talking();
		} catch (InterruptedException | ClassNotFoundException | IOException e) {
			//capire se devo gestire l'uscita dal popup o dalla mainPage
			if(isOpenedPopup) {
				popup.setErrorPopup("#EXIT");
				popup.setRound("#CONTROLLER");
			} else {
				control.setErrorPopup("#EXIT");
				control.setRound("#CONTROLLER");
			}
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Metodo che si occupa della comunicazione con il Server.
	 * Viene richiesta la scelta di una delle tre opzioni di acquisizione del dataset.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	private void talking() throws IOException, ClassNotFoundException, InterruptedException {

		int decision = 0;
		String answer = "";

		while (true) {
			isOpenedPopup = false;
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
					control.setCheckPopup(false);
					control.setRound("#CONTROLLER");
				} else {

					tableName = control.getFile();
					out.writeObject(tableName);
					answer = (String) in.readObject();
					
					if(answer.contains("@ERROR")) {	
						control.setErrorMsg((String) in.readObject());
						control.setCheckPopup(false);
						control.setRound("#CONTROLLER");
					}
				}
			} while (!answer.contains("@CORRECT"));

			control.setCheckPopup(true);
			control.setRound("#CONTROLLER");
			System.out.println("--> KNN caricato correttamente");

			while(control.getRound().compareTo("#CONTROLLER") == 0) {
				sleep(100);
			}
			control.setRound("#CONTROLLER");
			popup = control.getPopupController();

			//segnala che il popup è stato aperto, serve per gestire nel metodo run()
			//il caso in cui viene sollevata una eccezione a programma in corso
			isOpenedPopup = true;

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
						popup.setRound("#CONTROLLER");
						if (answer.equals("@READSTRING")) {
							
							//Attendo che Manager mi dia l'attributo discreto
							while(popup.getRound().compareTo("#CONTROLLER") == 0) {
								//per chiudere il thread quando il popup viene chiuso
								if(doClose) {
									return;
								}
								sleep(100);
							}
							
							out.writeObject(popup.getTmp());
						} else {
							double x = 0.0;
							do {
								//Attendo che Manager mi dia l'attributo continuo
								while(popup.getRound().compareTo("#CONTROLLER") == 0) {
									//per chiudere il thread quando il popup viene chiuso
									if(doClose) {
										return;
									}
									sleep(100);
								}
								//Provo ad assegnare il testo a x, se invece il testo è errato
								//l'eccezione mi resetterà waitClient a true e rientrerà nel ciclo
								try {
									x = Double.valueOf(popup.getTmp());
								} catch (NumberFormatException ex) {
									popup.setErrorPopup("#REPEAT");
									popup.setRound("#CONTROLLER");
								}
								
							} while (popup.getRound().compareTo("#CONTROLLER") == 0);
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
					popup.setRound("#CONTROLLER");
					while(popup.getRound().compareTo("#CONTROLLER") == 0) {

						//per chiudere il thread quando il popup viene chiuso
						if(doClose) {
							return;
						}
						sleep(100);
					}
					try { 
						k = Integer.valueOf(popup.getTmp());
					} catch (NumberFormatException ex) {
						popup.setErrorPopup("#REPEAT");
					}

				} while (k < 1);
				out.writeObject(k);
	
				String s = (String)in.readObject();
				System.out.println("--> Predizione ottenuta: " + s);
				popup.setPrediction(s);
				popup.setShowPrediction(true);

				popup.setRound("#CONTROLLER");
				while(popup.getRound().compareTo("#CONTROLLER") == 0) {

					//per chiudere il thread quando il popup viene chiuso
					if(doClose) {
						return;
					}
					sleep(100);
				}

				prediction = popup.getShowPredictionController();
				
				//Aspetto che il Manager scelga di ripetere o meno il predict 
				prediction.setRound("#CONTROLLER");
				while (prediction.getRound().compareTo("#CONTROLLER") == 0) {

					//per chiudere il thread quando il popup viene chiuso
					if(doClose) {
						return;
					}
					sleep(100);
				}
				
			} while (prediction.isSameKnn());
			
		}
	}

	/**
	 * Metodo che si occupa di far terminare il Thread del Client in caso di predizione interrotta.
	 */
	public void close() {
		doClose = true;
	}

}
