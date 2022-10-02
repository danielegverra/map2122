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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/mainPage.fxml"));
			
			Parent root = loader.load();
			//bisogna scegliere se farlo connettere di default a localhost
			c = new Client(ipAddressTextField.getText(), Integer.valueOf(portTextField.getText()), loader.getController());
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			((MainController)loader.getController()).setClient(c);
			((MainController)loader.getController()).setConnection(ipAddressTextField.getText(), Integer.valueOf(portTextField.getText()));
			//stage.show();
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
			//stage.initOwner(popupButton.getScene().getWindow());
			stage.show();

		} 

		
    }
}