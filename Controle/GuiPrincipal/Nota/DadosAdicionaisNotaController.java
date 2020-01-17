/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Nota;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class DadosAdicionaisNotaController implements Initializable {

    @FXML
    private TextArea observacao;
    @FXML
    private TextField descricao;
    @FXML
    private TextField pmweb;
    @FXML
    private Label descricaoTamanho;
    @FXML
    private Label observacaoTamanho;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        observacao.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(newValue.length() >= 399){
                observacao.setText(oldValue);
            }
            observacaoTamanho.setText(String.valueOf(newValue.length()));
        });
        descricao.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(newValue.length() >= 399){
                descricao.setText(oldValue);
            }
            descricaoTamanho.setText(String.valueOf(newValue.length()));
        });
    }

    public void editavel(boolean arg){
        this.observacao.setEditable(arg);
        this.descricao.setEditable(arg);
        this.pmweb.setEditable(arg);
    }
    
    public void setObservacao(String observacao) {
        this.observacao.setText(observacao);
        observacaoTamanho.setText(String.valueOf(observacao.length()));
    }

    public void setDescricao(String descricao) {
        this.descricao.setText(descricao);
        descricaoTamanho.setText(String.valueOf(descricao.length()));
    }

    public String getObservacao() {
        return observacao.getText();
    }

    public String getDescricao() {
        return descricao.getText();
    }
    
    public void setIdPmWeb(int id){        
        this.pmweb.setText(String.valueOf(id));
    }
    
    public int getIdPmWeb(){
        return Integer.valueOf(pmweb.getText().replaceAll("[^0-9]", ""));
    }

}
