/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import Conexao.Cliente;
import classes.financeiro.NotaFinanceiro;
import GuiPrincipal.Main;
import GuiPrincipal.Relatorio.RelatorioNotaController;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import Util.HistoricoPesquisa;
import Util.Modulos;
import Util.PaneController;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class AbertasFinanceiroController implements Initializable, PaneController {

    private BorderPane pai;

    @FXML
    private TableColumn<NotaFinanceiro, String> valorNota;
    @FXML
    private TableView<NotaFinanceiro> notasAbertoTabela;
    @FXML
    private TableColumn<NotaFinanceiro, String> vencimentoNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> vencimentoRealNota;
    @FXML
    private TextField valor;
    @FXML
    private TableColumn<NotaFinanceiro, String> fornecedorNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> diarioNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> cnpjNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> numeroNota;
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
    private TableColumn<NotaFinanceiro, String> classificadaNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> fluxoNota;
    @FXML
    private TableColumn<NotaFinanceiro, String> vencimentoNotaNota;
    @FXML
    private ChoiceBox<String> campo;

    private SimpleDateFormat sdf;

    private ArrayList<NotaFinanceiro> abertas = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        numeroNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getNumero()));
        fornecedorNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getFornecedor()));
        cnpjNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getCnpj()));
        fluxoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getFluxo())));
        vencimentoNotaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getVencimentoNota())));
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
        classificadaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().isClassificada()?"Sim":"Não"));
        baixadaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().isBaixada() ? "Sim" : "Nao"));
        linhaNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getLinha()));
        codigoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getCodigo()));

        HistoricoPesquisa historico = Cliente.INSTANCIA.buscarCampos("NOTA");
        campo.setItems(FXCollections.observableArrayList(historico.getItens()));
        campo.getSelectionModel().select(historico.getCampo());
        valor.setText(historico.getValor());

        notasAbertoTabela.setRowFactory(s -> {
            TableRow<NotaFinanceiro> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if (evento.getClickCount() == 2 && (!linha.isEmpty())) {
                    dividirPagamento(linha.getItem());
                    atualizar();
                }
            });
            return linha;
        });
        atualizar();
    }

    private void atualizar() {
        abertas.clear();
        abertas = Cliente.INSTANCIA.listarAbertasFinanceiro();
        atualizarTabela();
    }

    private void atualizarTabela() {
        notasAbertoTabela.setItems(FXCollections.observableArrayList(abertas));
    }

    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @FXML
    void dividir() {
        NotaFinanceiro nota = notasAbertoTabela.getSelectionModel().getSelectedItem();
        if (nota == null) {
            return;
        }
        dividirPagamento(nota);
        atualizar();
    }

    private void dividirPagamento(NotaFinanceiro nota) {
        try {
            FXMLLoader loader = new FXMLLoader(DividirPagamentoController.class.getResource(Modulos.DIVIDIRPAGAMENTO.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Dividir Pagamento");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            DividirPagamentoController controller = loader.getController();
            controller.setStage(stage);
            controller.setIdNota(nota.getIdNota());

            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void succeeded() {
    }

}
