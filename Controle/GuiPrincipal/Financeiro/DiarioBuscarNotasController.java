/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import Conexao.Cliente;
import classes.financeiro.Diario;
import classes.financeiro.NotaFinanceiro;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class DiarioBuscarNotasController implements Initializable {

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
    private TableColumn<NotaFinanceiro, String> linhaNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> codigoNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> classificadaNota;

    private SimpleDateFormat sdf;

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
        linhaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getLinha()));
        codigoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getCodigo()));
        diarioNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> param) -> {
            return new SimpleStringProperty(param.getValue().getIdDiario() == 0 ? "" : "DC-" + new DecimalFormat("00000").format(param.getValue().getIdDiario()));
        });
        classificadaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().isClassificada()?"Sim":"Não"));
        notasAbertoTabela.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        notasAbertoTabela.setItems(FXCollections.observableArrayList(Cliente.INSTANCIA.listarAbertasFinanceiro()));
    }

    @FXML
    void cancelar(ActionEvent event) {
        stage.close();
    }

    @FXML
    void incluir(ActionEvent event) {
        notasAbertoTabela.getSelectionModel().getSelectedItems().forEach(s -> {
            if (!diario.getNotas().contains(s)) {
                if (s.getIdDiario() == 0) {
                    diario.getNotas().add(s);
                    Cliente.INSTANCIA.incluirNotaEmDiario(s.getId(), diario.getId());
                }
            }
        });
        stage.close();
    }

    public void setDiario(Diario diario) {
        this.diario = diario;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
