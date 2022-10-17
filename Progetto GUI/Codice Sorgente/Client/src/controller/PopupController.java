package controller;

import java.io.IOException;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
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

    @FXML
    private Label captionLabel;

    @FXML
    private JFXButton showPredictionButton;

    @FXML
    private Label predictionLabel;

    @FXML
    private JFXButton differentKnnButton;

    @FXML
    private JFXButton sameKnnButton;

    /**
     * Stringa che contiene il messaggio ricevuto dal client,
     * il quale verrà stampato a video, avvalorando typeAttributeLabel.
     */
    private String msg;

    private String tmp;
    private String prediction;

    /**
     * Stringa che funge da semaforo tra la classe Client e PopupController.
     * Il suo valore indica quale delle due classi ha la priorità, quindi
     * in determinati momenti dell'esecuzione del programma la classe
     * rimanente dovrà aspettare che la classe con priorità abbia terminato
     * uno specifico task necessario per il corretto funzionamento.
     */
    private String round = "#CLIENT";

    /**
     * Booleano che indica se, al termine della predizione, si vuole
     * o meno effettuare un'altra predizione con lo stesso KNN.
     */
    private boolean sameKnn;

    /**
     * Stringa che mantiene informazioni su eventuali errori della schermata.
     * All'interno del programma questa viene controllata in determinati
     * momenti per capire se aprire o meno un popup di errore.
     */
    private String error = "#NONE";

    private Stage parentStage;

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

    public void hideButton() {
        showPredictionButton.setOpacity(0);
    }
    
    public boolean isSameKnn() {
        return sameKnn;
    }

    public void setErrorPopup(String error) {
        this.error = error;
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/startPage.fxml"));
			        Parent root = loader.load();
                    Scene scene = new Scene(root);
			        parentStage.setScene(scene);
                    
                    ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
                    System.out.println("--> Ritorno alla schermata iniziale");
                    openErrorPopup("Errore di Connessione:", "Uscita imminente, ristabilire una nuova connessione.");
                    //return;
                } else if (error.compareTo("#REPEAT") == 0) {
                    openErrorPopup("Errore di Immissione:", "Bisogna inserire un valore numerico idoneo.");
                    error = "#NONE";
                }
                changeMsg();
            } 
            //se tmp è vuota non faccio nulla
        }
    }

    @FXML
    void changeText(ActionEvent event) throws InterruptedException {
        captionLabel.setOpacity(1);
        predictionLabel.setText(prediction);
        predictionLabel.setOpacity(1);
        showPredictionButton.setOpacity(0);
        sameKnnButton.setLayoutX(14);
        sameKnnButton.setLayoutY(206);
        differentKnnButton.setLayoutX(198);
        differentKnnButton.setLayoutY(206);
    }

    
    public void showButton() {

        showPredictionButton.setOpacity(1);
        showPredictionButton.setLayoutX(77);
        showPredictionButton.setLayoutY(120);
        valueField.setOpacity(0);
        typeAttributeLabel.setOpacity(0);
        round = "#CONTROLLER";
    }

    @FXML
    void useSameKnn(ActionEvent event) throws InterruptedException {
        
        //Setto la variabile che comunica al Client di ripetere la predizione
        sameKnn = true;

        //Aspetto che il Client comunichi al Manager il msg giusto
        round = "#CLIENT";
        while(round.compareTo("#CLIENT") == 0) {
            Thread.sleep(100);
        }

        //Reset delle label per prendere la predizione
        valueField.requestFocus();
        sameKnnButton.setLayoutX(435);
        sameKnnButton.setLayoutY(171);
        differentKnnButton.setLayoutX(435);
        differentKnnButton.setLayoutY(250);
        showPredictionButton.setOpacity(0);
        showPredictionButton.setLayoutY(94);
        showPredictionButton.setLayoutX(288);
        valueField.setOpacity(1);
        typeAttributeLabel.setText(msg);
        typeAttributeLabel.setOpacity(1);
        captionLabel.setOpacity(0);
        predictionLabel.setOpacity(0);

    }

    @FXML
    void useOtherKnn(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        sameKnn = false;
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

}
