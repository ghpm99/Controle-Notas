/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Util;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public abstract class CarregarController implements Initializable{

    @FXML
    public ProgressBar barraCarregar2;

    @FXML
    public ProgressIndicator circuloCarregar1;//retonta

    @FXML
    public Label status;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
        

    }

    public abstract void run();

    public abstract void sucesso();
    
    public abstract void falha();
    
    public void setStatus(String arg){
        Platform.runLater(() -> status.setText(arg));
    }

}
