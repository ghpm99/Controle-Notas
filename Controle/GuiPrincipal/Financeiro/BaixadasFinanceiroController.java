/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import Conexao.Cliente;
import classes.financeiro.NotaFinanceiro;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import Util.HistoricoPesquisa;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class BaixadasFinanceiroController implements Initializable, PaneController{
private BorderPane pai;
    
    @FXML
    private TableColumn valorNota;
    @FXML
    private TableView<NotaFinanceiro> notasAbertoTabela;
    @FXML
    private TableColumn vencimentoNota;
    @FXML
    private TableColumn vencimentoRealNota;
    @FXML
    private TextField valor;
    @FXML
    private TableColumn fornecedorNota;
    @FXML
    private TableColumn cnpjNota;
    @FXML
    private TableColumn numeroNota;
    @FXML
    private TableColumn descontoNota;
    @FXML
    private TableColumn jurosNota;
    @FXML
    private TableColumn valorLiquidoNota;
    @FXML
    private TableColumn tipoNota;
    @FXML
    private TableColumn baixadaNota;
    @FXML
    private TableColumn linhaNota;
    @FXML
    private TableColumn codigoNota;
    @FXML
    private ChoiceBox<String> campo;

    private SimpleDateFormat sdf;

    private ArrayList<NotaFinanceiro> abertas = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        numeroNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(c.getValue().getNumero());
            }
        });
        fornecedorNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(c.getValue().getFornecedor());
            }
        });
        cnpjNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(c.getValue().getCnpj());
            }
        });
        vencimentoNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(sdf.format(c.getValue().getVencimento()));
            }
        });
        vencimentoRealNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(sdf.format(c.getValue().getVencimentoReal()));
            }
        });
        valorNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getValor()));
            }
        });
        descontoNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getDesconto()));
            }
        });
        jurosNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getJuros()));
            }
        });
        valorLiquidoNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getValorLiquido()));
            }
        });
        tipoNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(c.getValue().getTipo());
            }
        });
        
        baixadaNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(c.getValue().isBaixada()? "Sim" : "Nao");
            }
        });
        linhaNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(c.getValue().getLinha());
            }
        });
        codigoNota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NotaFinanceiro, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NotaFinanceiro, String> c){
                return new SimpleStringProperty(c.getValue().getCodigo());
            }
        });
                
        HistoricoPesquisa historico = Cliente.INSTANCIA.buscarCampos("NOTA");        
        campo.setItems(FXCollections.observableArrayList(historico.getItens()));
        campo.getSelectionModel().select(historico.getCampo());
        valor.setText(historico.getValor());
        
        atualizar();
    }

    @Override
    public void setPaneController(BorderPane controller){
        this.pai = controller;
    }
    
    private void atualizar(){
        abertas.clear();
        abertas = Cliente.INSTANCIA.listarBaixadasFinanceiro();
        atualizarTabela();
    }

    private void atualizarTabela(){
        notasAbertoTabela.setItems(FXCollections.observableArrayList(abertas));
    }

    @Override
    public void setModuloVoltar(Modulos modulo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void succeeded() {
    }

}
