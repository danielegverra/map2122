package controller;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;


/**
 * Classe che si occupa di controllare e gestire lo scene del popup
 * che.effettua la predizione.
 */
public class PopupController {

    @FXML
    private Label typeAttributeLabel;

    @FXML
    private TextField valueField;

    /**
     * Stringa che contiene il messaggio ricevuto dal client,
     * il quale verrà stampato a video, avvalorando typeAttributeLabel.
     */
    private String msg;

    private String tmp;
    private String prediction;
    private boolean doShowPrediction = false;

    /**
     * Stringa che funge da semaforo tra la classe Client e PopupController.
     * Il suo valore indica quale delle due classi ha la priorità, quindi
     * in determinati momenti dell'esecuzione del programma la classe
     * rimanente dovrà aspettare che la classe con priorità abbia terminato
     * uno specifico task necessario per il corretto funzionamento.
     */
    private String round = "#CLIENT";

    /**
     * Stringa che mantiene informazioni su eventuali errori della schermata.
     * All'interno del programma questa viene controllata in determinati
     * momenti per capire se aprire o meno un popup di errore.
     */
    private String error = "#NONE";

    private Stage parentStage;

    private FXMLLoader showPredictionLoader;

    public ShowPredictionController getShowPredictionController() {
        return showPredictionLoader.getController();
    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
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
     * Metodo per settare il valore della variabile msg.
     * 
     * @param msg - Stringa contenente il nuovo valore della variabile msg.
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTypeAttributeLabel() {
        return typeAttributeLabel.getText();
    }

    public void setTypeAttributeLabel(String tipoAttributo) {
        this.typeAttributeLabel.setText(tipoAttributo);
    }

    public String getTmp() {
        return tmp;
    }
    
    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }    

    public void setErrorPopup(String error) {
        this.error = error;
    }

    public String getErrorPopup() {
        return error;
    }

    public Stage getParentStage() {
        return parentStage;
    }

    public void setShowPrediction(boolean value) {
        doShowPrediction = value;
    }

    public void changeMsg() throws InterruptedException {

        while (round.compareTo("#CLIENT") == 0) {
            Thread.sleep(100);
        }
        typeAttributeLabel.setText(msg);
    }

    
    @FXML
    void nextValue(KeyEvent event) throws InterruptedException, IOException {

        if (event.getCode() == KeyCode.ENTER) {

            //aggiorno il contenuto da inviare al client e svuoto il valueField
            tmp = valueField.getText().trim();
            valueField.clear();

            //se tmp non è vuota la mando al Client
            if (tmp.compareTo("") != 0) {

                //Attendo che Client prenda tmp e restiuisca il messaggio
                round = "#CLIENT";
                while (round.compareTo("#CLIENT") == 0) {
                    Thread.sleep(100);
                }

                //controllo di eventuali errori (differenza tra #EXIT che chiude tutto e #REPEAT che apre solo un popup di errore)
                if (error.compareTo("#EXIT") == 0) {
                    //gestione dell'errore
                    handleSocketError((Stage)((Node) event.getSource()).getScene().getWindow());
                } else if (error.compareTo("#REPEAT") == 0) {
                    openErrorPopup("Errore di Immissione:", "Bisogna inserire un valore numerico idoneo.");
                    error = "#NONE";
                }

                //se ho terminato cambio scena, altrimenti cambio solo l'etichetta della label
                if(doShowPrediction) {
                    doShowPrediction = false;
                    showPrediction();
                } else {
                    changeMsg();
                }
                
            } 
            //se tmp è vuota non faccio nulla
        }
    }
    
    private void showPrediction() throws IOException {
        showPredictionLoader = new FXMLLoader(getClass().getResource("../fxml/newshowPredictionPage.fxml"));
        Parent root = showPredictionLoader.load();
        Stage stage = (Stage)valueField.getScene().getWindow();
        Scene scene = new Scene(root);
        getShowPredictionController().setPrediction(prediction);
        getShowPredictionController().setPopupController(this);
        getShowPredictionController().setParentScene(valueField.getScene());
        stage.setScene(scene);
        
        round = "#CLIENT";
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

    public void handleSocketError(Stage popupStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/newStartPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        StartController newController = (StartController)loader.getController();

        //definiamo le operazioni da compiere quando una dimensione della schermata viene modificata
        parentStage.heightProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
                newController.resize(parentStage.getHeight(), parentStage.getWidth());
            }
        });

        parentStage.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
                newController.resize(parentStage.getHeight(), parentStage.getWidth());
            }
        });

        //ridimensione delle componenti della scena prima del set
        newController.resize(parentStage.getHeight(), parentStage.getWidth());

        parentStage.setScene(scene);
        
        popupStage.close();
        System.out.println("--> Ritorno alla schermata iniziale");
        openErrorPopup("Errore di Connessione:", "Uscita imminente, ristabilire una nuova connessione.");
    }

}
