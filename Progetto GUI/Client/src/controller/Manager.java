package controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Manager {

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
	public static Stage stage;

	@FXML
    private Label tipoAttributo;

	@FXML
    private JFXButton binaryButton;

    @FXML
    private JFXButton dbButton;

    @FXML
    private JFXButton fileButton;;

    @FXML
    private TextField nomeFile;

    @FXML
    private Button startButton;

	@FXML
    private JFXButton popupButton;


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

	public void setMsg(String msg) {
		tipoAttributo.setText(msg);
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	//METODI


	public void resetFile() {
		file = "NULL";
	}


	@FXML
	void decisione(ActionEvent event) throws IOException, InterruptedException {
		if (event.getSource() == fileButton) {
			System.out.println("1");
			decision = 1;
			getNome();
		} else if (event.getSource() == binaryButton) {
			System.out.println("2");
			decision = 2;
			getNome();
		} else if (event.getSource() == dbButton) {
			System.out.println("3");
			decision = 3;
			getNome();
		}
		waitManager = true;
		while (waitManager) {
			Thread.currentThread().sleep(100);
		}
		//tipoAttributo.setText(msg);
		
		openPopup();
		
	}

	@FXML
    void getNome(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			file = nomeFile.getText().trim();
			System.out.println(file);
			nomeFile.clear();
		}
    }

	private void getNome() {
		file = nomeFile.getText().trim();
		System.out.println(file);
		nomeFile.clear();
    }


    void openPopup() throws IOException, InterruptedException {
		
		Parent root;

		if(check) {
			stage = new Stage();
			m = new FXMLLoader(getClass().getResource("../fxml/popup.fxml"));
			root = m.load();
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("KNN");
			stage.setResizable(false);
			waitClient = false;
			//stage.initOwner(popupButton.getScene().getWindow());
			((PopupManager)m.getController()).changeMsg();
			((PopupManager)m.getController()).hideButton();
			stage.show();
		} else {
			System.out.println(errorMsg);
			new PopupController().openPopup(errorMsg);
			
		}
	}

	

}
