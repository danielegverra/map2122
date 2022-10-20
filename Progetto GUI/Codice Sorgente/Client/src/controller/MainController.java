package controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import client.Client;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * Classe che si occupa di controllare e gestire lo scene della pagina principale.
 */
public class MainController {

	@FXML
    private Label buttonDescLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label titleLabel;

	@FXML
    private Label errorLabel;

	@FXML
    private JFXButton binaryButton;

    @FXML
    private JFXButton dbButton;

    @FXML
    private JFXButton fileButton;

    @FXML
    private TextField fileNameField;

	@FXML
    private ColumnConstraints firstColumn;

    @FXML
    private ColumnConstraints secondColumn;

    @FXML
    private ColumnConstraints thirdColumn;

	@FXML
    private RowConstraints firstRow;

	@FXML
    private RowConstraints secondRow;

	@FXML
    private RowConstraints thirdRow;

	@FXML
    private RowConstraints forthRow;

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

	private FXMLLoader popupLoader;

	private Stage stage;



	public void setErrorPopup (String error) {
		this.error = error;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public PopupController getPopupController() {
		return popupLoader.getController();
	}

	public String getFile() {
		return file;
	}

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

	public void setCheckPopup(Boolean check) {
		this.checkPopup = check;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setConnection(String ipAddress, Integer PORT) {
		this.ipAddress = ipAddress;
		this.PORT = PORT;
	}
	

	@FXML
	void decision(ActionEvent event) throws IOException, InterruptedException {

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
			Scene scene = new Scene(root);
			((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(scene);
			
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

    void openPopup(Stage parentStage) throws IOException, InterruptedException {
		Parent root;
		stage = new Stage();
		popupLoader = new FXMLLoader(getClass().getResource("../fxml/popupPage.fxml"));
		root = popupLoader.load();
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("KNN");
		stage.setResizable(false);
		round = "#CLIENT";
		stage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				try {
					client.close();
					//inserire indirizzo ip e porta dallo start controller
					client = new Client(ipAddress, PORT, MainController.this);
					client.start();
				} catch (IOException | ClassNotFoundException | NumberFormatException e ) {
					e.printStackTrace();
				}         			
				System.out.println("--> Chiusura Stage in corso");
			}
		});
		((PopupController) popupLoader.getController()).changeMsg();
		((PopupController) popupLoader.getController()).setParentStage(parentStage);
		((PopupController) popupLoader.getController()).hideButton();
		stage.show();
	}

	public void openErrorPopup(String title, String subtitle) throws IOException {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("../fxml/errorPage.fxml"));
        Stage stage = new Stage();
        Parent root = fxml.load();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("KNN: Popup di errore");
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
		fileButton.setPrefWidth(width/4);

		binaryButton.setStyle("-fx-font-size: " + size/25 + "; -fx-background-color: #FFFFFF");
		binaryButton.setPrefHeight(height/10);
		binaryButton.setPrefWidth(width/4);

		dbButton.setStyle("-fx-font-size: " + size/25 + "; -fx-background-color: #FFFFFF");
		dbButton.setPrefHeight(height/10);
		dbButton.setPrefWidth(width/4);
		
		fileNameField.setPrefHeight(height/10);
		fileNameField.setPrefWidth(width/3);
	}

}
