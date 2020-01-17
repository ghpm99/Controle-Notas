/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Contrato;

import Util.Modulos;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class DadosAdicionaisMapaController implements Initializable{

    private Stage stage;

    @FXML // fx:id="observacao"
    private TextArea observacao; // Value injected by FXMLLoader

    @FXML // fx:id="observacaoAssinatura"
    private TextArea observacaoAssinatura; // Value injected by FXMLLoader

    @FXML // fx:id="encerramento"
    private TextField encerramento; // Value injected by FXMLLoader

    @FXML // fx:id="fisico"
    private TextField fisico; // Value injected by FXMLLoader

    @FXML // fx:id="observacaoSistema"
    private TextArea observacaoSistema; // Value injected by FXMLLoader

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
    }

    @FXML
    private void salvar(ActionEvent event){
        stage.close();
    }

    public String getObservacao(){
        return observacao.getText();
    }

    public void setObservacao(String observacao){
        this.observacao.setText(observacao);
    }

    public String getObservacaoAssinatura(){
        return observacaoAssinatura.getText();
    }

    public void setObservacaoAssinatura(String observacaoAssinatura){
        this.observacaoAssinatura.setText(observacaoAssinatura);
    }

    public String getEncerramento(){
        return encerramento.getText();
    }

    public void setEncerramento(String encerramento){
        this.encerramento.setText(encerramento);
    }

    public String getFisico(){
        return fisico.getText();
    }

    public void setFisico(String fisico){
        this.fisico.setText(fisico);
    }

    public String getObservacaoSistema(){
        return observacaoSistema.getText();
    }

    public void setObservacaoSistema(String observacaoSistema){
        this.observacaoSistema.setText(observacaoSistema);
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

}
