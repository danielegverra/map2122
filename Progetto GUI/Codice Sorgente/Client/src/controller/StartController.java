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
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class StartController {

	//Attributi

    private Client c;
	

	//FXML

	@FXML
    private TextField ipAddressTextField;

    @FXML
    private TextField portTextField;
    
    @FXML
    private JFXButton startButton;

	//METODI

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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/RmainPage.fxml"));
			Parent root = loader.load();
			c = new Client(ipAddress, PORT, loader.getController());
			Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);

			stage.heightProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
					((MainController)loader.getController()).resize(stage.getHeight(), stage.getWidth());
				}
			});
	
			stage.widthProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
					((MainController)loader.getController()).resize(stage.getHeight(), stage.getWidth());
				}
			});

			stage.setScene(scene);
			((MainController)loader.getController()).setClient(c);
			((MainController)loader.getController()).setConnection(ipAddress, PORT);
			
			System.out.println("--> Connesione al Server riuscita");
            c.start();
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
}