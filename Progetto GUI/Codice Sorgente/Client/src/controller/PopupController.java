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
import javafx.scene.layout.RowConstraints;
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

    /**
     * Label che descrive il tipo di attributo da inserire.
     */

    @FXML
    private Label typeAttributeLabel;

    /**
     * TextField in cui inserire l'attributo richiesto.
     */

    @FXML
    private TextField valueField;

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
     * Stringa che contiene il messaggio ricevuto dal client,
     * il quale verrà stampato a video, avvalorando typeAttributeLabel.
     */
    
    private String msg;

    /**
     * Stringa che contiene il valore inserito dall'utente.
     */

    private String tmp;

    /**
     * Stringa che contiene la predizione ottenuta dal Server.
     */

    private String prediction;

    /**
     * Booleano che indica se mostrare o meno la scenta
     * contenente il valore della predizione.
     */

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

    /**
     * Stage principale che ha aperto il popup, necessario in caso di errore
     * di chiusura della socket per ripristinare la pagina iniziale del programma.
     */

    private Stage parentStage;

    /**
     * FXMLLoader relativo allo ShowPredictionController da caricare.
     */

    private FXMLLoader showPredictionLoader;

    /**
     * Metodo per ottenere il controller dell'attributo showPredictionLoader, necessario al client
     * per comunicare con quest'ultimo.
     * 
     * @return Controller dell'FXMLLoader
     */

    public ShowPredictionController getShowPredictionController() {
        return showPredictionLoader.getController();
    }

    /**
     * Metodo per settare il parentStage della schermata.
     * 
     * @param parentStage - indica lo stage da settare
     */

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

    /**
     * Metodo per ottenere il testo della label contenente
     * il tipo di attributo da inserire. 
     * 
     * @return Stringa contenente il tipo di attributo.
     */

    public String getTypeAttributeLabel() {
        return typeAttributeLabel.getText();
    }

    /**
     * Metodo per settare il testo della label contenente
     * il tipo di attributo da inserire. 
     */

    public void setTypeAttributeLabel(String tipoAttributo) {
        this.typeAttributeLabel.setText(tipoAttributo);
    }

    /**
     * Metodo per ottenere l'attributo getTmp
     * 
     * @return Stringa che contiene il valore di tmp.
     */
    public String getTmp() {
        return tmp;
    }
    
    /**
     * Metodo per settare il valore della predizione.
     * 
     * @param prediction - Stringa contenente il valore
     * della predizione.
     */
    public void setPrediction(String prediction) {
        this.prediction = prediction;
    } 
    
    /**
     * Metodo per settare la stringa di errore del Popup.
     * @param error - Stringa contente l'errore da settare.
     */

    public void setErrorPopup(String error) {
        this.error = error;
    }

    /**
     * Metodo per ottenere la stringa di errore del Popup.
     * 
     * @return Stringa contenente il valore di error.
     */

    public String getErrorPopup() {
        return error;
    }


    /**
     * Metodo per ottenere l'attributo parentStage.
     * 
     * @return Stage corrispondente a parentStage.
     */
    public Stage getParentStage() {
        return parentStage;
    }

    /**
     * Metodo per settare il valore dell'attributo doShowPrediction.
     * 
     * @param value - Booleano contenente il valore da settare.
     */

    public void setShowPrediction(boolean value) {
        doShowPrediction = value;
    }

    /**
     * Metodo per cambiare la label relativa al tipo di 
     * attributo che l'utente deve inserire.
     * 
     * @throws InterruptedException
     */

    public void changeMsg() throws InterruptedException {

        while (round.compareTo("#CLIENT") == 0) {
            Thread.sleep(100);
        }
        typeAttributeLabel.setText(msg);
    }

    
    /**
     * Metodo che serve ad aggiornare la schermata non appena 
     * l'utente inserisce un attributo e digita invia
     * 
     * @param event - Trigger per azionare il metodo 
     * @throws InterruptedException
     * @throws IOException
     */
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
    

    /**
     * Metodo che cambia la schermata per mostrare la predizione
     * ottenuta dal Server.
     * 
     * @throws IOException
     */

    private void showPrediction() throws IOException {
        showPredictionLoader = new FXMLLoader(getClass().getResource("../fxml/showPredictionPage.fxml"));
        Parent root = showPredictionLoader.load();
        Stage stage = (Stage)valueField.getScene().getWindow();
        Scene scene = new Scene(root);
        getShowPredictionController().setPrediction(prediction);
        getShowPredictionController().setPopupController(this);
        getShowPredictionController().setParentScene(valueField.getScene());
        ShowPredictionController newController = (ShowPredictionController)showPredictionLoader.getController();

        //definiamo le operazioni da compiere quando una dimensione della schermata viene modificata
        stage.heightProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
                newController.resize(stage.getHeight(), stage.getWidth());
            }
        });

        stage.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
                newController.resize(stage.getHeight(), stage.getWidth());
            }
        });

        //ridimensione delle componenti della scena prima del set
        newController.resize(stage.getHeight(), stage.getWidth());

        stage.setScene(scene);
        
        round = "#CLIENT";
    }

    /**
     * Metodo che apre un popup di errore qualora dovessero
     * verificarsi degli oerrori durante l'inserimento di valori.
     * 
     * @param title - è il titolo di errore da settare nel Popup.
     * @param subtitle - è la descrizione dell'errore da settare nel Popup.
     * @throws IOException
     */

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

    /**
     * Metodo che serve a gestire l'errore derivante dal caso
     * in cui il Client dovesse interrompere la comunicazione col Server,
     * aprendo un Popup di errore e tornando alla schermata principale.
     * 
     * @param popupStage - è lo stage relativo al Popup aperto
     * @throws IOException
     */
    public void handleSocketError(Stage popupStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/startPage.fxml"));
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

    /**
	 * Metodo che gestisce il ridimensionamento della finestra
	 * ogni qualvolta l'utente allarga o ingrandsice il software
     * 
	 * @param height - indica l'altezza corrente della finestra
	 * @param width - indica la larghezza corrente della finestra
	 */
	
    public void resize(double height, double width) {
		Double size = Math.min(height, width);

		//controllo non sia NaN poichè alla prima chiamata lo stage non ha ancora 
		//delle dimensioni, perciò size risulterà NaN
		if(!Double.isNaN(size)) {
			
			firstRow.setPrefHeight(height/2);
			secondRow.setPrefHeight(height/2);

            typeAttributeLabel.setStyle("-fx-font-size: " + size/11 + "; -fx-alignment: center");
            typeAttributeLabel.setPrefHeight(height/2);
            typeAttributeLabel.setPrefWidth(width);

            valueField.setStyle("-fx-font-size: " + size/19 + "; -fx-alignment: center");
            valueField.setPrefHeight((height/22)*4);
            valueField.setPrefWidth((width/7)*5);
					
		}
    }

}
