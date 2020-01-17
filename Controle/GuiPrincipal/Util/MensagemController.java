/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Util;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class MensagemController implements Initializable{

    @FXML // fx:id="cancelButton"
    private Button cancelButton; // Value injected by FXMLLoader

    @FXML // fx:id="detailsLabel"
    private Label detailsLabel; // Value injected by FXMLLoader

    @FXML // fx:id="imagem"
    private ImageView imagem; // Value injected by FXMLLoader

    @FXML // fx:id="actionParent"
    private HBox actionParent; // Value injected by FXMLLoader

    @FXML // fx:id="okButton"
    private Button okButton; // Value injected by FXMLLoader

    @FXML // fx:id="okParent"
    private HBox okParent; // Value injected by FXMLLoader

    @FXML // fx:id="messageLabel"
    private Label messageLabel; // Value injected by FXMLLoader

    private boolean retorno;
    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO        
    }

    @FXML
    void cancelarButon(ActionEvent event){
        retorno = false;
        stage.close();
    }

    @FXML
    void okButon(ActionEvent event){
        retorno = true;
        stage.close();
    }

    void setMensagem(String mensagem){
        messageLabel.setText(mensagem);
    }

    void setDetalhes(String detalhes){
        detailsLabel.setText(detalhes);
    }

    void setImagem(Image imagem){
        this.imagem.setImage(imagem);
    }

    void setBoolean(boolean retorno){
        this.retorno = retorno;
    }

    boolean getBoolean(){
        return retorno;
    }

    void setStage(Stage stage){
        this.stage = stage;
    }
}
