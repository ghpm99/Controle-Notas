/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Util;

import Conexao.Cliente;
import Util.Modulos;
import Util.PaneController;
import Util.Sobre;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class SobreController implements Initializable, PaneController {

    @FXML
    private ImageView iconeImage;

    @FXML
    private Label versaoLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        versaoLabel.setText(
                Cliente.INSTANCIA.getSobre() + "\n"
                + Sobre.getMensagem() + "\n"
                + "os.name:" + System.getProperty("os.name")
                + " - os.version:" + System.getProperty("os.version")
                + " - java.version:" + System.getProperty("java.version")
                + " - user.name:" + System.getProperty("user.name")
                + " - os.arch:" + System.getProperty("os.arch")
                + " - user.dir:" + System.getProperty("user.dir"));
    }

    @Override
    public void setPaneController(BorderPane controller) {
    }

    @Override
    public void setModuloVoltar(Modulos modulo) {
    }

    @Override
    public void succeeded() {
    }

}
