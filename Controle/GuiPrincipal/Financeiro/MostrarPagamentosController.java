/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import classes.financeiro.NotaFinanceiro;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import java.text.DecimalFormat;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class MostrarPagamentosController implements Initializable,PaneController{

   
    @FXML
    private TableColumn<NotaFinanceiro, String> valorNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> diarioNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> linhaNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> baixadaNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> cnpjNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> fornecedorNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> descontoNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> valorLiquidoNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> jurosNota;
    @FXML
    private TableView<NotaFinanceiro> notasFinanceiro;
    @FXML
    private TableColumn<NotaFinanceiro, String> tipoNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> vencimentoRealNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> vencimentoNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> codigoNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> numeroNota;
    
    private SimpleDateFormat sdf;
    
    private BorderPane controller;
    
    private Stage stage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
       sdf = new SimpleDateFormat("dd/MM/yyyy");
        numeroNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getNumero()));
        fornecedorNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getFornecedor()));
        cnpjNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getCnpj()));
        vencimentoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getVencimento())));
        vencimentoRealNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getVencimentoReal())));
        valorNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getValor())));
        descontoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getDesconto())));
        jurosNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getJuros())));
        valorLiquidoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getValorLiquido())));
        tipoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getTipo()));
        diarioNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> param) -> {
            return new SimpleStringProperty(param.getValue().getIdDiario() == 0 ? "" : "DC-" + new DecimalFormat("00000").format(param.getValue().getIdDiario()));
        });
        
        baixadaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().isBaixada()? "Sim" : "Nao"));
        linhaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getLinha()));
        codigoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getCodigo()));
        
        
        notasFinanceiro.setRowFactory(s -> {
            TableRow<NotaFinanceiro> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if (evento.getClickCount() == 2 && (!linha.isEmpty())) {
                   //fazer algo
                }
            });
            return linha;
        });
    }    

    @Override
    public void setPaneController(BorderPane controller){
        this.controller = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setStage(Stage stage){
        this.stage = stage;
    }
    
    public void setPagamento(ArrayList<NotaFinanceiro> arg0){
        notasFinanceiro.getItems().clear();
        notasFinanceiro.setItems(FXCollections.observableArrayList(arg0));
    }

    @Override
    public void succeeded() {
    }
}
