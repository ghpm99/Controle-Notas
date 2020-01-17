/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Contrato;

import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Guilherme
 */
public class OrdemCompraCadastroController implements Initializable,PaneController {

    BorderPane pai;
    @FXML
    private TableView produtos;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        ArrayList<String> linha = new ArrayList<>();
        linha.add("1");
        linha.add("2");
        linha.add("3");
        linha.add("4");
        linha.add("5");
        linha.add("6");
        linha.add("7");
        linha.add("8");
        linha.add("9");
        linha.add("10");
        linha.add("11");
        
        ObservableList data = FXCollections.observableArrayList(linha);
        
        produtos.setItems(data);
    }    

    /**
     *
     * @param controller
     */
    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    /**
     *
     * @param modulo
     */
    @Override
    public void setModuloVoltar(Modulos modulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void succeeded() {
    }
 
    
    
}
