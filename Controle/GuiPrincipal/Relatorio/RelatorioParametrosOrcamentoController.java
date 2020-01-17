/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Relatorio;

import Util.ParametrosOrcamento;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class RelatorioParametrosOrcamentoController implements Initializable{

    private Stage stage;

    private ParametrosOrcamento parametros;
    @FXML
    private TextField tarefa;
    @FXML
    private TextField planoCusto;
    @FXML
    private TextField fornecedor;

    private boolean ok;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
        ok = false;
    }

    /**
     *
     * @param stage
     */
    public void setStage(Stage stage){
        this.stage = stage;
    }

    /**
     *
     * @param parametros
     */
    public void setParametros(ParametrosOrcamento parametros){
        this.parametros = parametros;
    }

    /**
     *
     */
    @FXML
    public void salvar(){
        parametros.setTarefa(tarefa.getText());
        parametros.setPlanoCusto(planoCusto.getText());
        parametros.setFornecedor(fornecedor.getText());
        ok = true;
        stage.close();
    }

    /**
     *
     */
    @FXML
    public void cancelar(){
        ok = false;
        stage.close();
    }

    /**
     *
     * @return
     */
    public boolean isOk(){
        return ok;
    }
    
}
