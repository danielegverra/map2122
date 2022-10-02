package controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import client.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class MainController {

	//ATTRIBUTI
	private int decision = 0;
	private String file;
	private Boolean check;
	private Boolean waitManager;
	private Boolean waitClient = true;
	private String errorMsg;
	private Client client;
	private String ipAddress;
	private Integer PORT;

	public void setClient(Client client) {
		this.client = client;
	}

	//FXML
	
	@FXML
    private Label errorLabel;

	private FXMLLoader popupLoader;

	private Stage stage;


	@FXML
    private JFXButton binaryButton;

    @FXML
    private JFXButton dbButton;

    @FXML
    private JFXButton fileButton;;

    @FXML
    private TextField fileNameField;

	public PopupController getPopupController() {
		return popupLoader.getController();
	}

	public String getFile() {
		return file;
	}

	public int getDecision() {
		return decision;
	}

	public void resetDecision() {
		decision = 0;
	}
	
	public void setWaitManager(Boolean wait) {
		this.waitManager = wait;
	}


	public void setCheck(Boolean check) {
		this.check = check;
	}


	public Boolean getWaitClient() {
		return waitClient;
	}

	public void setWaitClient(Boolean waitClient) {
		this.waitClient = waitClient;
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
		if (event.getSource() == fileButton) {
			decision = 1;
		} else if (event.getSource() == binaryButton) {
			decision = 2;
		} else if (event.getSource() == dbButton) {
			decision = 3;
		}
		getName();
		waitManager = true;
		while (waitManager) {
			Thread.currentThread().sleep(100);
		}
		
		openPopup();
	}

	private void getName() {
		file = fileNameField.getText();
		fileNameField.clear();
    }


    void openPopup() throws IOException, InterruptedException {
		
		Parent root;

		if(check) {
			stage = new Stage();
			popupLoader = new FXMLLoader(getClass().getResource("../fxml/popupPage.fxml"));
			root = popupLoader.load();
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("KNN");
			stage.setResizable(false);
			waitClient = false;
			stage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					try {
						client.close();
						//inserire indirizzo ip e porta dallo start controller
						client = new Client("localhost", Integer.valueOf("2025"), MainController.this);
						getPopupController().setClient(client);
						client.start();
					} catch (IOException | ClassNotFoundException | NumberFormatException e ) {
						e.printStackTrace();
					}         			
					System.out.println("--> Chiusura Stage in corso");
				}
			});

			((PopupController) popupLoader.getController()).changeMsg();
			((PopupController) popupLoader.getController()).hideButton();
			stage.show();
		} else {
			System.out.println("--> Errore nell'acqusizione della sorgente KNN");
			openErrorPopup("Attenzione!", errorMsg);
			
		}
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

}
