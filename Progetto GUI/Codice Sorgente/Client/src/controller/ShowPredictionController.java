package controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ShowPredictionController {

    @FXML
    private Label captionLabel;

    @FXML
    private JFXButton differentKnnButton;

    @FXML
    private Label predictionLabel;

    @FXML
    private JFXButton sameKnnButton;

    PopupController popupController;

    /**
     * Scene del Popup generatore della predizione, conservato per consentire,
     * in caso di nuova predizione col medesimo KNN, la possibilità di 
     * rigenerare la schermata.
     */
    Scene parentScene;

    public void setParentScene(Scene parentScene) {
        this.parentScene = parentScene;
    }

    public void setPopupController(PopupController popupController) {
        this.popupController = popupController;
    }

    /**
     * Booleano che indica se, al termine della predizione, si vuole
     * o meno effettuare un'altra predizione con lo stesso KNN.
     */
    private boolean sameKnn;

    /**
     * Stringa che funge da semaforo tra la classe Client e ShowPredictionController.
     * Il suo valore indica quale delle due classi ha la priorità, quindi
     * in determinati momenti dell'esecuzione del programma la classe
     * rimanente dovrà aspettare che la classe con priorità abbia terminato
     * uno specifico task necessario per il corretto funzionamento.
     */
    private String round = "#CLIENT";

    public boolean isSameKnn() {
        return sameKnn;
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

    public void setPrediction(String prediction) {
        predictionLabel.setText(prediction);
    }


    @FXML
    void useOtherKnn(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        sameKnn = false;
        round = "#CLIENT";
    }

    @FXML
    void useSameKnn(ActionEvent event) throws InterruptedException, IOException {
        //Setto la variabile che comunica al Client di ripetere la predizione
        sameKnn = true;

        //Aspetto che il Client comunichi al Manager il msg giusto
        round = "#CLIENT";
        while(popupController.getRound().compareTo("#CLIENT") == 0) {
            Thread.sleep(100);
        }

        //aggiorna i messaggi segnalati dal popup di predizione
        popupController.changeMsg();

        //cambia la scena ripristinando il popup di predizione
        Stage currentStage = (Stage)captionLabel.getScene().getWindow();
        currentStage.setScene(parentScene);
        
    }

}
