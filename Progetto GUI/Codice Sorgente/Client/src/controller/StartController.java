package controller;

import java.io.IOException;
import com.jfoenix.controls.JFXButton;

import client.Client;
import javafx.event.ActionEvent;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * Classe che si occupa di controllare e gestire lo scene della pagina iniziale.
 */
public class StartController {

	/**
	 * TextField che acquisisce l'indirizzo ip del server
	 * con cui il Client si deve connettere.
	 */
	@FXML
    private TextField ipAddressTextField;

	/**
	 * TextField che acquisisce la porta del server
	 * su cui il Client si deve connettere.
	 */
    @FXML
    private TextField portTextField;
    
	/**
	 * Bottone che conferma la scelta dell'indirizzo ip e 
	 * della porta, iniziando la connessione con il server.
	 */
    @FXML
    private JFXButton startButton;	

	@FXML
    private Label titleLabel;	

	@FXML
    private Label ipLabel;

	@FXML
    private Label portLabel;

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

	@FXML
    private ColumnConstraints firstColumn;

	@FXML
    private ColumnConstraints secondColumn;

	@FXML
    private ColumnConstraints thirdColumn;

	/**
	 * Metodo che fa partire la connessione al server identificato dall'ip
	 * e porta desiderata dall'utente.
	 * 
	 * @param event - Evento causato dalla pressione di startButton.
	 * @throws IOException
	 */
    @FXML
    void avviaClient(ActionEvent event) throws IOException {
		
		try {
			Integer PORT;
			String ipAddress;

			//assegno il contenuto di ipAddress
			if (ipAddressTextField.getText().equals("")) {
				ipAddress = "localhost";
			} else {
				ipAddress = ipAddressTextField.getText();
			}

			//assegno il valore di PORT
			if (portTextField.getText().equals("")) {
				PORT = 2025;
			} else {
				PORT = Integer.valueOf(portTextField.getText());
			}

			//connessione e cambio scena
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/mainPage.fxml"));
			Parent root = loader.load();
			Client client = new Client(ipAddress, PORT, loader.getController());
			Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			MainController newController = (MainController)loader.getController();

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
			stage.setScene(scene);

			//set delle componenti del controller
			newController.setClient(client);
			newController.setConnection(ipAddress, PORT);
			
			//start thread
			System.out.println("--> Connesione al Server riuscita");
            client.start();
		}  catch (IOException | ClassNotFoundException | NumberFormatException e) {
			System.out.println("--> Errore di connessione al Server");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/errorPage.fxml"));
			Stage stage = new Stage();
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("KNN: Popup di errore");
			stage.show();

		} 
		
    }

	
	public void resize(double height, double width) {
		Double size = Math.min(height, width);

		//controllo non sia NaN poichè alla prima chiamata lo stage non ha ancora 
		//delle dimensioni, perciò size risulterà NaN
		if(!Double.isNaN(size)) {
			firstColumn.setPrefWidth(width/4);
			secondColumn.setPrefWidth(width/2);
			thirdColumn.setPrefWidth(width/4);
			
			firstRow.setPrefHeight(height/3);
			secondRow.setPrefHeight(height/6);
			thirdRow.setPrefHeight(height/8);
			forthRow.setPrefHeight(height/6);
			fifthRow.setPrefHeight(height/6);

			titleLabel.setStyle("-fx-font-size: " + size/15 + "; -fx-font-weight: bold; -fx-alignment: center");
			titleLabel.setPrefWidth(width/2);
			titleLabel.setPrefHeight(height/3);

			ipLabel.setStyle("-fx-font-size: " + size/20 + "; -fx-alignment: center");
			ipLabel.setPrefWidth(width/4);
			ipLabel.setPrefHeight(height/7);

			portLabel.setStyle("-fx-font-size: " + size/20 + "; -fx-alignment: center");
			portLabel.setPrefWidth(width/4);
			portLabel.setPrefHeight(height/7);

			startButton.setStyle("-fx-font-size: " + size/33 + "; -fx-background-color: #263238");
			startButton.setPrefHeight(height/11);
			startButton.setPrefWidth(width/6);

			ipAddressTextField.setStyle("-fx-font-size: " + size/33 + "; -fx-alignment: center");
			ipAddressTextField.setPrefHeight(height/16);
			ipAddressTextField.setPrefWidth(width/3);

			portTextField.setStyle("-fx-font-size: " + size/33 + "; -fx-alignment: center");
			portTextField.setPrefHeight(height/16);
			portTextField.setPrefWidth(width/3);
		}
	}

}