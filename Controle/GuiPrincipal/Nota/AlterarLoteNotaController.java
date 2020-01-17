/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Nota;

import Util.LoteNota;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class AlterarLoteNotaController implements Initializable, PaneController {

    private BorderPane pai;
    private LoteNota lote;

    @FXML
    private CheckBox dataSelect;

    @FXML
    private CheckBox classificadaSelect;

    @FXML
    private DatePicker data;

    @FXML
    private CheckBox classificada;

    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

    /**
     *
     * @param lote
     * @param stage
     */
    public void setLoteNota(LoteNota lote, Stage stage) {
        this.lote = lote;
        this.stage = stage;
    }

    @FXML
    void alterar(ActionEvent event) {
        this.lote.setClassificadaSelect(classificadaSelect.isSelected());
        this.lote.setClassificada(classificada.isSelected());
        this.lote.setDataSelect(dataSelect.isSelected());
        if (dataSelect.isSelected()) {
            this.lote.setData(Date.from(Instant.from(data.getValue().atStartOfDay(ZoneId.systemDefault()))));
        }
        this.stage.close();
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
