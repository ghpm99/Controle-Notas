/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Contrato;

import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class OrdemCompraController implements Initializable,PaneController{

    private BorderPane pai;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
    }    

    /**
     *
     * @param controller
     */
    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }
    
    @FXML
    private void cadastro(){
        try{
            pai.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Modulos.ORDEMCADASTRO.getCaminho()));
            Parent parent = (Parent) loader.load();
            PaneController controler = (PaneController) loader.getController();
            controler.setPaneController(pai);
            pai.getChildren().add(parent);
        }catch(Exception e){
            e.printStackTrace();
        }
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
