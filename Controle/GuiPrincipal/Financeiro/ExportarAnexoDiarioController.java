/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class ExportarAnexoDiarioController implements Initializable {

    
    @FXML
    private ListView<String> saidaAnexos;
    @FXML
    private ListView<String> tiposAnexo;
    

    private ArrayList<String> tiposAnexoSelecionado;
    private ArrayList<String> tiposAnexoDisponiveis = new ArrayList<>();
    

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tiposAnexoDisponiveis.add("Nota");
        tiposAnexoDisponiveis.add("Documento");
        tiposAnexoDisponiveis.add("Boleto");


        atualizar();
    }

    private void atualizar() {
        tiposAnexo.setItems(FXCollections.observableArrayList(tiposAnexoDisponiveis));        
        if (tiposAnexoSelecionado != null) {
            saidaAnexos.setItems(FXCollections.observableArrayList(tiposAnexoSelecionado));
        }        
    }

    @FXML
    void cancelarButton(ActionEvent event) {
        tiposAnexoSelecionado.clear();        
        stage.close();
    }

    @FXML
    void exportarButton(ActionEvent event) {
        stage.close();
    }  

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void RemoverTipoAnexo(ActionEvent event) {
        String temp = saidaAnexos.getSelectionModel().getSelectedItem();
        if (temp == null) {
            return;
        }
        tiposAnexoDisponiveis.add(temp);
        tiposAnexoSelecionado.remove(temp);
        atualizar();
    }

    @FXML
    void adicionarTipoAnexo(ActionEvent event) {
        String temp = tiposAnexo.getSelectionModel().getSelectedItem();
        if (temp == null) {
            return;
        }
        tiposAnexoSelecionado.add(temp);
        tiposAnexoDisponiveis.remove(temp);
        atualizar();
    }


    public void setArrayTipoAnexo(ArrayList<String> tipo) {
        tiposAnexoSelecionado = tipo;
    }
    
}
