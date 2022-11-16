package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Classe che si occupa di controllare e gestire lo scene del popup di errore.
 */
public class ErrorController {

    /**
     * Label che contiene il titolo dell'errore.
     */
    @FXML
    private Label titleLabel;

    /**
     * Label che contiene la descrizione dell'errore.
     */
    @FXML
    private Label subtitleLable;

    /**
     * Metodo che setta il contenuto della titleLabel.
     * 
     * @param titleLabel - Nuovo contenuto della titleLabel
     */
    void setTitleLabel(String titleLabel) {
        this.titleLabel.setText(titleLabel);
    }

    /**
     * Metodo che setta il contenuto della subtitleLabel.
     * 
     * 
     * @param subtitleLabel - Nuovo contenuto della subtitleLabel
     */
    void setSubtitleLable(String subtitleLable) {
        this.subtitleLable.setText(subtitleLable);
    }

}
