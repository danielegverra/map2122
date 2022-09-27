package controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MainController {

	//ATTRIBUTI
	private int decision = 0;
	private String file = "NULL";
	private Boolean check;
	private Boolean waitManager;
	private Boolean waitClient = true;
	private String msg;
	private String errorMsg;

	//FXML
	
	@FXML
    private Label errorLabel;

	private FXMLLoader m;

	private Stage stage;


	@FXML
    private JFXButton binaryButton;

    @FXML
    private JFXButton dbButton;

    @FXML
    private JFXButton fileButton;;

    @FXML
    private TextField fileNameField;

   
	//GET E SET

	public FXMLLoader getPopupManager() {
		return m;
	}

	public String getFile() {
		return file;
	}

	public int getDecision() {
		return decision;
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

	//METODI


	public void resetFile() {
		file = "NULL";
	}


	@FXML
	void decision(ActionEvent event) throws IOException, InterruptedException {
		if (event.getSource() == fileButton) {
			System.out.println("1");
			decision = 1;
			getName();
		} else if (event.getSource() == binaryButton) {
			System.out.println("2");
			decision = 2;
			getName();
		} else if (event.getSource() == dbButton) {
			System.out.println("3");
			decision = 3;
			getName();
		}
		waitManager = true;
		while (waitManager) {
			Thread.currentThread().sleep(100);
		}
		//tipoAttributo.setText(msg);
		
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
			m = new FXMLLoader(getClass().getResource("../fxml/popupPage.fxml"));
			root = m.load();
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("KNN");
			stage.setResizable(false);
			waitClient = false;
			//stage.initOwner(popupButton.getScene().getWindow());
			((PopupController)m.getController()).changeMsg();
			((PopupController)m.getController()).hideButton();
			stage.show();
		} else {
			System.out.println(errorMsg);
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
