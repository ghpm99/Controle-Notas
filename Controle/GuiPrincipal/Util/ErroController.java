/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Util;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class ErroController implements Initializable {

    @FXML
    private Label mensagem;
    private boolean exit;
    private Stage stage;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void ok() {
        if (exit) {
            System.exit(1);
        } else {
            stage.close();
        }
    }

    /**
     *
     * @param msg
     */
    public void setMensagem(String msg) {
        this.mensagem.setText(msg);
    }

    /**
     *
     * @param arg
     */
    public void exitOnClose(boolean arg) {
        this.exit = arg;
    }

    /**
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
