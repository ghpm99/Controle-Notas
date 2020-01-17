/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class FinanceiroController implements Initializable,PaneController{

    private BorderPane pai;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
    }    

    @Override
    public void setPaneController(BorderPane controller){
      this.pai = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void succeeded() {
    }
    
}
