/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import Conexao.Cliente;
import classes.financeiro.Diario;
import classes.financeiro.NotaFinanceiro;
import classes.fornecedor.Fornecedor;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class IncluirPagamentoController implements Initializable, PaneController {

    @FXML
    private TextField cnpjField;
    @FXML
    private TableView<NotaFinanceiro> notasAbertoTabela;
    @FXML
    private TableColumn<NotaFinanceiro, String> numeroNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> diarioNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> fornecedorNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> cnpjNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> vencimentoNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> vencimentoRealNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> valorNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> descontoNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> jurosNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> valorLiquidoNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> tipoNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> baixadaNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> linhaNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> codigoNota;
    @FXML
    private TableView<Fornecedor> fornecedores;
    @FXML
    private TableColumn<Fornecedor, String> cnpj;
    @FXML
    private TableColumn<Fornecedor, String> nome;

    private SimpleDateFormat sdf;

    private BorderPane controller;

    private Diario diario;
    
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

        baixadaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().isBaixada() ? "Sim" : "Nao"));
        linhaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getLinha()));
        codigoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getCodigo()));

        notasAbertoTabela.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        fornecedores.getSelectionModel().selectedItemProperty().addListener((obs, old, newSel) -> {
            if (newSel != null) {
                atualizarTabelaPagamentos();
            }
        });
        
        cnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        nome.setCellValueFactory(new PropertyValueFactory<>("razaoSocial"));
        
    }

    @FXML
    void buscarCnpj(ActionEvent event) {
        fornecedores.getItems().clear();
        fornecedores.setItems(FXCollections.observableArrayList(Cliente.INSTANCIA.buscarFornecedorCampo("CNPJ", cnpjField.getText().toUpperCase())));
    }

    @FXML
    void incluirPagamento(ActionEvent event) {
        NotaFinanceiro notaTemp = notasAbertoTabela.getSelectionModel().getSelectedItem();
        if (notaTemp == null || notaTemp.getIdDiario() != 0) {
            return;
        }
        if (!diario.getNotas().contains(notaTemp)) {
            diario.getNotas().add(notaTemp);
            Cliente.INSTANCIA.incluirNotaEmDiario(notaTemp.getId(), diario.getId());
        }

    }    
    
    @Override
    public void setPaneController(BorderPane controller) {
        this.controller = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setDiario(Diario diario) {
        this.diario = diario;
    }

    private void atualizarTabelaPagamentos(){
        notasAbertoTabela.getItems().clear();
        notasAbertoTabela.setItems(FXCollections.observableArrayList(Cliente.INSTANCIA.listarPagamentosFornecedor(fornecedores.getSelectionModel().getSelectedItem().getId(), false)));
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void succeeded() {
    }
}
