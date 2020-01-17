/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal;

import Conexao.Cliente;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class SugestaoController implements Initializable, PaneController {

    @FXML
    private Slider avaliacaoSlider;

    @FXML
    private Label avaliacaoLabel;

    @FXML
    private TextField assuntoText;

    @FXML
    private TextArea campoText;

    private final String[] avaliacao = new String[]{"0 - Péssimo", "1 - Ruim","2 - Regular","3 - Satisfeito","4 - Bom","5 - Ótimo"};

    @FXML
    void enviar(ActionEvent event) {
        Cliente.INSTANCIA.avaliar((int)avaliacaoSlider.getValue(), assuntoText.getText(), campoText.getText());
        avaliacaoSlider.setValue(3.0d);
        assuntoText.setText("");
        campoText.setText("");
        atualizarAvaliacao();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        avaliacaoSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            atualizarAvaliacao();
        });  
        
        atualizarAvaliacao();
    }

    private void atualizarAvaliacao(){
        avaliacaoLabel.textProperty().setValue(avaliacao[(int) avaliacaoSlider.getValue()]);
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
