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
import javafx.stage.Stage;

public class StartPageController {

	//Attributi

    private Client c;
	private Manager m;

    private Stage stage;
	private Scene scene;
	private Parent root;
	FXMLLoader loader;

	//FXML
    
    @FXML
    private JFXButton startButton;

	//METODI

    @FXML
    void avviaClient(ActionEvent event) throws IOException {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/mainPage.fxml"));
			root = loader.load();
			c = new Client("localhost", Integer.valueOf("2025"), loader.getController());
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			//stage.show();
            c.start();
		}  catch (IOException | ClassNotFoundException | NumberFormatException e) {
			System.out.println("Errore di connessione al Server...");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/erroreavvio.fxml"));
			root = loader.load();
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
		} 

		
    }
}