package controller;

import java.io.IOException;
import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;

public class ShowPredictionController {

    @FXML
    private RowConstraints firstRow;

    @FXML
    private RowConstraints secondRow;

    @FXML
    private RowConstraints thirdRow;

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

        //ridimensione delle componenti della scena prima del set
		popupController.resize(currentStage.getHeight(), currentStage.getWidth());

        //definiamo le operazioni da compiere quando una dimensione della schermata viene modificata
		currentStage.heightProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
				popupController.resize(currentStage.getHeight(), currentStage.getWidth());
			}
		});

		currentStage.widthProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
				popupController.resize(currentStage.getHeight(), currentStage.getWidth());
			}
		});

        //set dello scene
        currentStage.setScene(parentScene);
        
    }

    public void resize(double height, double width) {
        Double size = Math.min(height, width);
        
        firstRow.setPrefHeight((height/22)*6);
        secondRow.setPrefHeight((height/22)*6);
        thirdRow.setPrefHeight((height/22)*10);

        captionLabel.setStyle("-fx-font-size: " + size/13 + "; -fx-alignment: center");
        captionLabel.setPrefWidth(width);
        captionLabel.setPrefHeight((height/22)*6);

        predictionLabel.setStyle("-fx-font-size: " + size/11 + "; -fx-alignment: center");
        predictionLabel.setPrefWidth(width);
        predictionLabel.setPrefHeight((height/22)*6);

        sameKnnButton.setStyle("-fx-font-size: " + size/21 + "; -fx-background-color: #FFFFFF");
        sameKnnButton.setPrefHeight((height/22)*6);
        sameKnnButton.setPrefWidth((width/2)-30);

        differentKnnButton.setStyle("-fx-font-size: " + size/21 + "; -fx-background-color: #FFFFFF");
        differentKnnButton.setPrefHeight((height/22)*6);
        differentKnnButton.setPrefWidth((width/2)-30);

    }

}
