package controller;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupController {

    @FXML
    private Label errorLabel;

    Stage stage;
    Parent root;

    public void openPopup(String msg) throws IOException {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("../fxml/errorPopup.fxml"));
        stage = new Stage();
        root = fxml.load();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        //stage.initOwner(popupButton.getScene().getWindow());
        ((PopupController)fxml.getController()).errorLabel.setText(msg);
        stage.show();
    }

}
