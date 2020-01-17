/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Util;

import GuiPrincipal.Main;
import Util.Modulos;
import Util.PaneController;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class RenomearController implements Initializable, PaneController {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @Override
    public void setPaneController(BorderPane controller) {
    }

    @Override
    public void setModuloVoltar(Modulos modulo) {
    }

    @FXML
    void renomear(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        File pasta = chooser.showDialog(Main.getInstancia().getStage());
        if(pasta == null){
            return;
        }
        for (File a : pasta.listFiles()) {
            if (a.getName().toUpperCase().startsWith("NF ")) {
                File temp = new File(pasta.getAbsolutePath() + "/" + a.getName().replaceAll(" ", "/").replaceAll(".pdf", "").toUpperCase());
                temp.mkdirs();
                a.renameTo(new File(temp.getAbsolutePath() + "/Invoice.pdf"));
            }else if (a.getName().toUpperCase().startsWith("MED ")){
                File temp = new File(pasta.getAbsolutePath() + "/" + a.getName().toUpperCase().replaceFirst("MED", "NF").replaceAll(" ", "/").replaceAll(".PDF", ""));
                temp.mkdirs();
                a.renameTo(new File(temp.getAbsolutePath() + "/Medicao.pdf"));
            }
        }
    }

    @Override
    public void succeeded() {
    }

}
