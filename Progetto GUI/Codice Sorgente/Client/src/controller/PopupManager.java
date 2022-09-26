package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.stage.Stage;
import javafx.scene.Node;


public class PopupManager {

    //FXML

    @FXML
    private Label tipoAttributo;

    @FXML
    private TextField textField;

    @FXML
    private Label labelPredizione;

    @FXML
    private JFXButton provaButton;

    @FXML
    private Label labelPredizioneEffettiva;

    @FXML
    private JFXButton differentKnnButton;

    @FXML
    private JFXButton sameKnnButton;


    //ATTRIBUTI

    private String msg;
    private String tmp;
    private String prediction;
    private Boolean waitManager = true;
    private Boolean waitClient = true;
    private boolean sameKnn;

    //GET E SET

    public void setWaitManager(Boolean waitManager) {
        this.waitManager = waitManager;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTipoAttributo() {
        return tipoAttributo.getText();
    }

    public void setTipoAttributo(String tipoAttributo) {
        this.tipoAttributo.setText(tipoAttributo);
    }

    
    public String getTmp() {
        return tmp;
    }

    public Boolean getWaitClient() {
        return waitClient;
    }

    public void setWaitClient(Boolean waitClient) {
        this.waitClient = waitClient;
    }

    
    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }    

    public void hideButton() {
        provaButton.setOpacity(0);
    }
    
    public boolean isSameKnn() {
        return sameKnn;
    }

    //METODI
    

    public void changeMsg() throws InterruptedException {

        while (waitManager) {
            Thread.currentThread().sleep(300);
        }
        tipoAttributo.setText(msg);
    }

    
    @FXML
    void nextValue(KeyEvent event) throws InterruptedException {

        if (event.getCode() == KeyCode.ENTER) {

            tmp = textField.getText().trim();
            textField.clear();
            System.out.println(tmp);

            //se tmp non è vuota la mando al Client
            if (tmp.compareTo("") != 0) {

                //Attendo che Client prenda tmp e restiuisca il messaggio
                waitManager = true;
                waitClient = false;
                while (waitManager) {
                    Thread.currentThread().sleep(100);
                }
                changeMsg();
            } 
            //se tmp è vuota non faccio nulla
        }
    }

    @FXML
    void close(ActionEvent event) {

        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        return;
    }

    @FXML
    void changeText(ActionEvent event) throws InterruptedException {

        labelPredizione.setOpacity(1);
        System.out.println(prediction);
        labelPredizioneEffettiva.setText(prediction);
        labelPredizioneEffettiva.setOpacity(1);
        provaButton.setOpacity(0);
        sameKnnButton.setLayoutX(14);
        sameKnnButton.setLayoutY(206);
        differentKnnButton.setLayoutX(198);
        differentKnnButton.setLayoutY(206);
    }

    
    public void showButton() {

        provaButton.setOpacity(1);
        provaButton.setLayoutX(77);
        provaButton.setLayoutY(120);
        textField.setOpacity(0);
        tipoAttributo.setOpacity(0);
        setWaitClient(true);
    }

    @FXML
    void useSameKnn(ActionEvent event) throws InterruptedException{
        
        //Setto la variabile che comunica al Client di ripetere la predizione
        sameKnn = true;

        //Aspetto che il Client comunichi al Manager il msg giusto
        waitManager = true;
        waitClient = false;
        while(waitManager) {
            Thread.currentThread().sleep(100);
        }

        //Reset delle label per prendere la predizione
        sameKnnButton.setLayoutX(435);
        sameKnnButton.setLayoutY(171);
        differentKnnButton.setLayoutX(435);
        differentKnnButton.setLayoutY(250);
        provaButton.setOpacity(0);
        provaButton.setLayoutY(94);
        provaButton.setLayoutX(288);
        textField.setOpacity(1);
        tipoAttributo.setText(msg);
        tipoAttributo.setOpacity(1);
        labelPredizione.setOpacity(0);
        labelPredizioneEffettiva.setOpacity(0);

    }

    @FXML
    void useOtherKnn(ActionEvent event) {
        this.close(event);
        sameKnn = false;
        setWaitClient(false);
    }

}
