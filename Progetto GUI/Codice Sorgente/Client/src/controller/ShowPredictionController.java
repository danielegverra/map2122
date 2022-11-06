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

/**
 * Classe che si occupa di controllare e gestire lo scene del popup
 * che rivela la predizione.
 */

public class ShowPredictionController {

    /**
	 * Prima riga della schermata.
	 */

    @FXML
    private RowConstraints firstRow;

    /**
	 * Seconda riga della schermata.
	 */

    @FXML
    private RowConstraints secondRow;

    /**
	 * Terza riga della schermata.
	 */

    @FXML
    private RowConstraints thirdRow;


    /**
     * Label che descrive la tipologia della schermata.
     */

    @FXML
    private Label captionLabel;

    /**
     * Button per ripetere la predizione con diverso
     * Training Set.
     */

    @FXML
    private JFXButton differentKnnButton;

    /**
     * Label contenente la predizione calcolata
     * dal Server.
     */

    @FXML
    private Label predictionLabel;

    /**
     * Bottone per ripetere la predizione con lo stesso
     * Training Set.
     */

    @FXML
    private JFXButton sameKnnButton;

    /**
     * Oggetto di tipo PopupController necessario per ripristinare 
     * la scena precedente in caso di nuova predizione con lo stesso dataset.
     */

    private PopupController popupController;

    /**
     * Scene del Popup generatore della predizione, conservato per consentire,
     * in caso di nuova predizione col medesimo KNN, la possibilità di 
     * rigenerare la schermata.
     */

    private Scene parentScene;

    /**
     * Metodo per settare il valore della variabile parentScene
     * 
     * @param parentScene - Variabile parentScene da assegnare.
     */

    void setParentScene(Scene parentScene) {
        this.parentScene = parentScene;
    }

    /**
     * Metodo per settare il valore della variabile popupController.
     * 
     * @param popupController - Variabile del popupController da assegnare.
     */

    void setPopupController(PopupController popupController) {
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

    /**
     * Metodo per ottenere il valore della variabile sameKnn.
     * @return Boolean che rappresenta il valore di sameKnn.
     */
    
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

    /**
     * Metodo per settare il valore della variabile prediction
     * 
     * @param prediction - Variabile contenente il valore della predizione da settare
     */

    void setPrediction(String prediction) {
        predictionLabel.setText(prediction);
    }

    /**
     * Metodo per cambiare la scena nel caso in cui l'utente volesse
     * ripetere la predizione con un KNN differente.
     * 
     * @param event - E' il trigger necessario a cambiare scena 
     * azionato da un bottone
     * @throws InterruptedException
     * @throws IOException
     */

    @FXML
    private void useOtherKnn(ActionEvent event) throws InterruptedException, IOException {
        //Setto la variabile che comunica al Client di non ripetere la predizione
        sameKnn = false;
        round = "#CLIENT";
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        
    }

    /**
     * Metodo per cambiare la scena nel caso in cui l'utente volesse
     * ripetere la predizione con lo stesso KNN.
     * 
     * @param event - E' il trigger necessario a cambiare scena 
     * azionato da un bottone
     */

    @FXML
    private void useSameKnn(ActionEvent event) throws InterruptedException, IOException {
        //Setto la variabile che comunica al Client di ripetere la predizione
        sameKnn = true;

        //Aspetto che il Client comunichi al Manager il msg giusto
        round = "#CLIENT";
        while(popupController.getRound().compareTo("#CLIENT") == 0) {
            Thread.sleep(100);
        }

        if (popupController.getErrorPopup().compareTo("#EXIT") != 0) {
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
        } else {
            popupController.handleSocketError((Stage)((Node) event.getSource()).getScene().getWindow());
        }
    }

    /**
	 * Metodo che gestisce il ridimensionamento della finestra
	 * ogni qualvolta l'utente allarga o ingrandsice il software
     * 
	 * @param height - Indica l'altezza corrente della finestra
	 * @param width - Indica la larghezza corrente della finestra
	 */
	
    void resize(double height, double width) {
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
