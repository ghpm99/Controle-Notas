/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Util;

import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class LogController implements Initializable, PaneController {

    @FXML
    private TextArea logText;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void okButton(ActionEvent event) {
        stage.close();
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLog(ArrayList<String> arg) {
        arg.forEach(s -> {
            logText.setText(logText.getText() + "\n" + s);
        });
    }
}
