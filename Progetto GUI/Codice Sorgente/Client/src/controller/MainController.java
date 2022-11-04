package controller;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import com.jfoenix.controls.JFXButton;
import client.Client;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Classe che si occupa di controllare e gestire lo scene della pagina principale.
 */
public class MainController {

	/**
	 * Label che contiene il titolo della schermata.
	 */
    @FXML
    private Label titleLabel;

	/**
	 * Label che indica all'utente di selezionare una scelta per l'acquisizione
	 * del dataset.
	 */
	@FXML
    private Label buttonDescLabel;

	/**
	 * Label che indica all'utente di immettere il nome di un file da caricare.
	 */
    @FXML
    private Label descriptionLabel;

	/**
	 * Bottone che rappresenta la scelta del caricamento da file binario.
	 */
	@FXML
    private JFXButton binaryButton;

	/**
	 * Bottone che rappresenta la scelta del caricamento da database.
	 */
    @FXML
    private JFXButton dbButton;

	/**
	 * Bottone che rappresenta la scelta del caricamento da file testuale.
	 */
    @FXML
    private JFXButton fileButton;

	/**
	 * TextField che si occupa di acquisire il nome del file o della tabella
	 * da cui caricare il dataset.
	 */
    @FXML
    private TextField fileNameField;

	/**
	 * Prima colonna della schermata.
	 */
	@FXML
    private ColumnConstraints firstColumn;

	/**
	 * Seconda colonna della schermata.
	 */
    @FXML
    private ColumnConstraints secondColumn;

	/**
	 * Terza colonna della schermata.
	 */
    @FXML
    private ColumnConstraints thirdColumn;

	/**
	 * Prima riga della schermata.
	 */
	@FXML
    private RowConstraints firstRow;

	/**
	 * Seconda riga della schermata.
	 */
	@FXML
    private RowConstraints secondRow;

	/**
	 * Terza riga della schermata.
	 */
	@FXML
    private RowConstraints thirdRow;

	/**
	 * Quarta riga della schermata.
	 */
	@FXML
    private RowConstraints forthRow;

	/**
	 * Quinta riga della schermata.
	 */
	@FXML
    private RowConstraints fifthRow;

	/**
	 * Intero che rappresenta la decisione presa dall'utente riguardo
	 * l'operazione da seguire.
	 */
	private int decision = 0;

	/**
	 * Stringa contenente il nome del file immesso dall'utente.
	 */
	private String file;

	/**
	 * Booleano che segnala la correttezza delle condizioni che permettono
	 * al popup di aprirsi dopo la scelta della acquisizione del dataset.
	 */
	private Boolean checkPopup;

	/**
     * Stringa che funge da semaforo tra la classe Client e MainController.
     * Il suo valore indica quale delle due classi ha la priorità, quindi
     * in determinati momenti dell'esecuzione del programma la classe
     * rimanente dovrà aspettare che la classe con priorità abbia terminato
     * uno specifico task necessario per il corretto funzionamento.
     */
	private String round; 

	/**
	 * Stringa contenente il messaggio da stampare all'utente 
	 * in caso di errore.
	 */
	private String errorMsg;

	/**
	 * Istanza del Client legata al popup aperto. Viene memorizzata per
	 * garantire la corretta chiusura del thread in caso di chiusura 
	 * forzata del popup. In quel caso viene aperta da zero una nuova istanza
	 * del client.
	 */
	private Client client;

	/**
	 * Stringa contenente l'indirizzo ip del server su cui collegarsi.
	 */
	private String ipAddress;

	/**
	 * Intero contenente la porta sel server su cui collegarsi.
	 */
	private int PORT;

	/**
     * Stringa che mantiene informazioni su eventuali errori della schermata.
     * All'interno del programma questa viene controllata in determinati
     * momenti per capire se aprire o meno un popup di errore.
     */
	private String error = "#NONE";

	/**
	 * FXMLLoader della pagina popup aperta.
	 */
	private FXMLLoader popupLoader;

	/**
	 * Metodo per il set della variabile error.
	 * 
	 * @param error - Nuovo valore della variabile error
	 */
	public void setErrorPopup(String error) {
		this.error = error;
	}

	/**
	 * Metodo per il set della variabile client.
	 * 
	 * @param client - Nuova istanza di Client da assegnare alla variabile
	 */
	void setClient(Client client) {
		this.client = client;
	}

	/**
	 * Metodo che restituisce l'istanza di PopupController legata al popup 
	 * aperto.
	 * 
	 * @return PopupController legato al popup aperto
	 */
	public PopupController getPopupController() {
		return popupLoader.getController();
	}

	/**
	 * Metodo che restituisce il valore della variabile file.
	 * 
	 * @return Stringa che rappresenta il valore di file.
	 */
	public String getFile() {
		return file;
	}

	/**
	 * Metodo che memorizza il contenuto della textField nella variabile
	 * file e svuota la stessa textField.
	 */
	private void getName() {
		file = fileNameField.getText();
		fileNameField.clear();
    }

	/**
	 * Metodo per restituire il valore dell'attributo decision.
	 * 
	 * @return Intero che rappresenta il valore di decision.
	 */
	public int getDecision() {
		return decision;
	}

	/**
	 * Metodo per resettare il valore di decision a 0.
	 */
	public void resetDecision() {
		decision = 0;
	}
	
	/**
     * Metodo per settare il valore della variabile round.
     * 
     * @param round - Stringa contenente il nuovo valore della variabile round.
     */
	public void setRound(String round) {
		this.round = round;
	}

	/**
     * Metodo per restituire il valore della variabile round.
     * 
     * @return Stringa che contiene il valore della variabile round.
     */
	public String getRound() {
		return round;
	}

	/**
	 * Metodo che setta il contenuto della variabile checkPopup.
	 * 
	 * @param check - Boolean che rappresenta il nuovo valore da assegnare a checkPopup
	 */
	public void setCheckPopup(Boolean check) {
		this.checkPopup = check;
	}

	/**
	 * Metodo che setta il contenuto della variabile errorMsg.
	 * 
	 * @param errorMsg - Stringa da assegnare a errorMsg
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
	 * Metodo che setta i valori dell'ip e della porta su cui si è
	 * connessi, per consentire, in caso di reset del client dovuto alla 
	 * chiusura del popup, di aprire un nuovo thread da zero.
	 * 
	 * @param ipAddress - Stringa contenente l'ip da memorizzare
	 * @param PORT - Intero contenente la porta da memorizzare
	 */
	void setConnection(String ipAddress, Integer PORT) {
		this.ipAddress = ipAddress;
		this.PORT = PORT;
	}
	
	/**
	 * Metodo richiamato nel momento in cui si preme uno dei tre pulsanti
	 * relativi al metodo con cui caricare il dataset. Si occupa del gestire
	 * la richiesta dell'utente, aprendo il popup oppure restituendo 
	 * un messaggio di errore a seconda dell'evenienza.
	 * 
	 * @param event - ActionEvent che causa la chiamata del metodo
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@FXML
	private void decision(ActionEvent event) throws IOException, InterruptedException {

		//gestione di decision
		if (event.getSource() == fileButton) {
			decision = 1;
		} else if (event.getSource() == binaryButton) {
			decision = 2;
		} else if (event.getSource() == dbButton) {
			decision = 3;
		}
		getName();

		//attendo il turno del controller
		round = "#CLIENT";
		while (round.compareTo("#CLIENT") == 0) {
			Thread.sleep(100);
		}

		//in caso di errore viene aperto il popup e ristabilita la schermata iniziale
		if(error.compareTo("#EXIT") == 0) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/startPage.fxml"));
			Parent root = loader.load();
			Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			StartController newController = (StartController)loader.getController();

			//definiamo le operazioni da compiere quando una dimensione della schermata viene modificata
			stage.heightProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
					newController.resize(stage.getHeight(), stage.getWidth());
				}
			});
	
			stage.widthProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
					newController.resize(stage.getHeight(), stage.getWidth());
				}
			});

            //ridimensione delle componenti della scena prima del set
            newController.resize(stage.getHeight(), stage.getWidth());

			stage.setScene(scene);
			
			System.out.println("--> Ritorno alla schermata iniziale");
			openErrorPopup("Errore di Connessione:", "Uscita imminente, ristabilire una nuova connessione.");
		} else {
			//apro il popup di errore o di previsione a seconda del controllo
			if (checkPopup) {
				openPopup((Stage)((Node) event.getSource()).getScene().getWindow());
			} else {
				System.out.println("--> Errore nell'acqusizione della sorgente KNN");
				openErrorPopup("Attenzione!", errorMsg);
			}
		}
	}

	/**
	 * Metodo che consiste nell'apertura del popup di acquisizione della 
	 * predizione.
	 * 
	 * @param parentStage - Stage del programma main a cui è legato il popup
	 * @throws IOException
	 * @throws InterruptedException
	 */
    private void openPopup(Stage parentStage) throws IOException, InterruptedException {
		Parent root;
		Stage stage = new Stage();
		popupLoader = new FXMLLoader(getClass().getResource("../fxml/popupPage.fxml"));
		root = popupLoader.load();
		stage.setScene(new Scene(root));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("KNN: Prediction");
		stage.getIcons().add(new Image(MainController.class.getResourceAsStream("/fxml/1Icon.jpg")));
		
		//variazione del valore della variabile semaforo
		round = "#CLIENT";

		//definiamo l'operazione da compiere in caso di chiusura della schermata
		stage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				try {
					//in maniera tale da chiudere il thread client e aprirne un altro identico da zero
					client.close();
					//inserire indirizzo ip e porta dallo start controller
					client = new Client(ipAddress, PORT, MainController.this);
					client.start();
				} catch (IOException | ClassNotFoundException | NumberFormatException e ) {
					//gestisco l'eccezione data dalla connessione al server
					System.out.println(e.getMessage());
					try {
						getPopupController().handleSocketError(stage);
					} catch (IOException exc) {
						exc.printStackTrace();
					}
				}         			
				System.out.println("--> Chiusura Stage in corso");
			}
		});

		PopupController newController = (PopupController)popupLoader.getController();

		//definiamo le operazioni da compiere quando una dimensione della schermata viene modificata
		stage.heightProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
				newController.resize(stage.getHeight(), stage.getWidth());
			}
		});

		stage.widthProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
				newController.resize(stage.getHeight(), stage.getWidth());
			}
		});

		//ridimensione delle componenti della scena prima del set
		newController.resize(stage.getHeight(), stage.getWidth());

		//set della scena
		getPopupController().changeMsg();
		getPopupController().setParentStage(parentStage);
		stage.show();
	}

	/**
	 * Metodo che consiste nell'apertura del popup di errore.
	 * 
	 * @param title - Titolo da assegnare al popup
	 * @param subtitle - Sottotitolo da assegnare al popup
	 * @throws IOException
	 */
	private void openErrorPopup(String title, String subtitle) throws IOException {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("../fxml/errorPage.fxml"));
        Stage stage = new Stage();
        Parent root = fxml.load();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("KNN: Error Popup");
		stage.getIcons().add(new Image(MainController.class.getResourceAsStream("/fxml/1Icon.jpg")));
        ((ErrorController)fxml.getController()).setTitleLabel(title);
        ((ErrorController)fxml.getController()).setSubtitleLable(subtitle);
        stage.show();
    }

	/**
	 * Metodo che viene impiegato per proporzionare le dimensioni delle
	 * componenti della schermata in base alle dimensioni della schermata stessa
	 * e viene richiamato ogni volta che le suddette dimensioni vengono modificate.
	 * 
	 * @param height - Double che rappresenta l'altezza della schermata.
	 * @param width - Double che rappresenta la larghezza della schermata.
	 * 
	 */
	void resize(double height, double width) {
		double size = Math.min(height, width);

		firstColumn.setPrefWidth(width/4);
		secondColumn.setPrefWidth(width/2);
		thirdColumn.setPrefWidth(width/4);
		
		firstRow.setPrefHeight(height/3);
		secondRow.setPrefHeight(height/6);
		thirdRow.setPrefHeight(height/6);
		forthRow.setPrefHeight(height/6);
		fifthRow.setPrefHeight(height/6);

		titleLabel.setStyle("-fx-font-size: " + size/15 + "; -fx-font-weight: bold; -fx-alignment: center");
		titleLabel.setPrefWidth(width/2);
		titleLabel.setPrefHeight(height/3);
		
		descriptionLabel.setStyle("-fx-font-size: " + size/25 + "; -fx-alignment: center");
		descriptionLabel.setPrefWidth(width/2);
		descriptionLabel.setPrefHeight(height/6);
		
		buttonDescLabel.setStyle("-fx-font-size: " + size/25 + "; -fx-alignment: center");
		buttonDescLabel.setPrefWidth(width/2);
		buttonDescLabel.setPrefHeight(height/6);

		fileButton.setStyle("-fx-font-size: " + size/25 + "; -fx-background-color: #FFFFFF");
		fileButton.setPrefHeight(height/10);
		fileButton.setPrefWidth(width/5);

		binaryButton.setStyle("-fx-font-size: " + size/25 + "; -fx-background-color: #FFFFFF");
		binaryButton.setPrefHeight(height/10);
		binaryButton.setPrefWidth(width/5);

		dbButton.setStyle("-fx-font-size: " + size/25 + "; -fx-background-color: #FFFFFF");
		dbButton.setPrefHeight(height/10);
		dbButton.setPrefWidth(width/5);
		
		fileNameField.setStyle("-fx-font-size: " + size/25 + "; -fx-alignment: center");
		fileNameField.setPrefHeight(height/10);
		fileNameField.setPrefWidth(width/3);
	}

}
