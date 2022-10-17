package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Classe che si occupa di controllare e gestire lo scene del popup di errore.
 */
public class ErrorController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label subtitleLable;

    public void setTitleLabel(String titleLabel) {
        this.titleLabel.setText(titleLabel);
    }

    public void setSubtitleLable(String subtitleLable) {
        this.subtitleLable.setText(subtitleLable);
    }

}
