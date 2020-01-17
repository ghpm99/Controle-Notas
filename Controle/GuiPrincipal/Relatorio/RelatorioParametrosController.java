/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Relatorio;

import Util.Parametros;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class RelatorioParametrosController implements Initializable{

    /*
     Variaveis
     */
    @FXML
    private DatePicker emissaoDe;
    @FXML
    private DatePicker emissaoAte;
    @FXML
    private DatePicker vencimentoDe;
    @FXML
    private DatePicker vencimentoAte;
    @FXML
    private TextField fornecedor;
    @FXML
    private TextField valorDe;
    @FXML
    private TextField valorAte;
    @FXML
    private CheckBox classificada;

    private Stage stage;

    private Parametros parametros;

    private boolean ok;

    private SimpleDateFormat sdf;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
        sdf = new SimpleDateFormat("yyyy-MM-dd");
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
     * @return
     */
    public Parametros retorno(){

        return new Parametros(Date.from(Instant.from(emissaoDe.getValue().atStartOfDay(ZoneId.systemDefault()))),
                Date.from(Instant.from(emissaoAte.getValue().atStartOfDay(ZoneId.systemDefault()))),
                Date.from(Instant.from(vencimentoDe.getValue().atStartOfDay(ZoneId.systemDefault()))),
                Date.from(Instant.from(vencimentoAte.getValue().atStartOfDay(ZoneId.systemDefault()))),
                fornecedor.getText(), editarTexto(valorDe.getText()), editarTexto(valorAte.getText()),
                classificada.isSelected(), classificada.isIndeterminate());
    }

    /**
     *
     * @param para
     */
    public void setParametro(Parametros para){
        this.parametros = para;
    }

    /**
     *
     */
    public void salvar(){
        if(emissaoDe.getValue() != null){
            parametros.setEmissaoDe(Date.from(Instant.from(emissaoDe.getValue().atStartOfDay(ZoneId.systemDefault()))));
        }
        if(emissaoAte.getValue() != null){
            parametros.setEmissaoAte(Date.from(Instant.from(emissaoAte.getValue().atStartOfDay(ZoneId.systemDefault()))));
        }
        if(vencimentoDe.getValue() != null){
            parametros.setVencimentoDe(Date.from(Instant.from(vencimentoDe.getValue().atStartOfDay(ZoneId.systemDefault()))));
        }
        if(vencimentoAte.getValue() != null){
            parametros.setVencimentoAte(Date.from(Instant.from(vencimentoAte.getValue().atStartOfDay(ZoneId.systemDefault()))));
        }
        if(!fornecedor.getText().equals("")){
            parametros.setFornecedor(fornecedor.getText());
        }
        if(!valorDe.getText().equals("")){
            parametros.setValorDe(editarTexto(valorDe.getText()));
        }
        if(!valorAte.getText().equals("")){
            parametros.setValorAte(editarTexto(valorAte.getText()));
        }
        parametros.setClassificada(classificada.isSelected());
        parametros.setAll(classificada.isIndeterminate());
        ok = true;
        stage.close();
    }

    private Long editarTexto(String newValue){
        return (newValue.isEmpty() ? 0 : Long.valueOf(newValue.replaceAll("[^0-9]", "")));
    }

    /**
     *
     */
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
