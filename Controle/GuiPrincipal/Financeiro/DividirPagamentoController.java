/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Financeiro;

import Conexao.Cliente;
import classes.financeiro.NotaFinanceiro;
import Util.Conversao;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author guilherme.machado
 */
public class DividirPagamentoController implements Initializable, PaneController {

    @FXML
    private TextField valorLiquidoCampo;
    @FXML
    private TextField valorBaixadoCampo;
    @FXML
    private TextField valorAbertoCampo;
    @FXML
    private TableView<NotaFinanceiro> notasAbertoTabela;
    @FXML
    private TableColumn<NotaFinanceiro, String> numeroNota, fornecedorNota, cnpjNota, vencimentoNota, vencimentoRealNota, valorNota;
    @FXML
    private Label statusLabel;

    private SimpleDateFormat sdf;

    private ArrayList<NotaFinanceiro> abertas = new ArrayList<>();

    private BorderPane pai;

    private int idNota;

    private Stage stage;

    private long valorLiquido, valorBaixado, valorAberto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        numeroNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getNumero()));
        fornecedorNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getFornecedor()));
        cnpjNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(c.getValue().getCnpj()));
        vencimentoNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getVencimento())));
        vencimentoNota.setCellFactory(TextFieldTableCell.forTableColumn());
        vencimentoNota.setOnEditCommit((TableColumn.CellEditEvent<NotaFinanceiro, String> event) -> {
            try {
                if (event.getRowValue().getIdDiario() == 0) {
                    Date novo = sdf.parse(event.getNewValue());
                    Cliente.INSTANCIA.setDate("PAGAMENTONOTA", "VENCIMENTO", event.getRowValue().getId(), novo);                    
                }
                atualizar();
            } catch (ParseException ex) {
                Logger.getLogger(DividirPagamentoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        vencimentoRealNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(sdf.format(c.getValue().getVencimentoReal())));
        vencimentoRealNota.setCellFactory(TextFieldTableCell.forTableColumn());
        vencimentoRealNota.setOnEditCommit((TableColumn.CellEditEvent<NotaFinanceiro, String> event) -> {
            try {
                if (event.getRowValue().getIdDiario() == 0) {
                    Date novo = sdf.parse(event.getNewValue());
                    Cliente.INSTANCIA.setDate("PAGAMENTONOTA", "VENCIMENTOREAL", event.getRowValue().getId(), novo);                    
                }
                atualizar();
            } catch (ParseException ex) {
                Logger.getLogger(DividirPagamentoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        valorNota.setCellValueFactory((TableColumn.CellDataFeatures<NotaFinanceiro, String> c) -> new SimpleStringProperty(longToString(dinheiro, c.getValue().getValor())));
        valorNota.setCellFactory(TextFieldTableCell.forTableColumn());
        valorNota.setOnEditCommit((TableColumn.CellEditEvent<NotaFinanceiro, String> event) -> {
            String status = "";
            switch (Cliente.INSTANCIA.atualizarValorFianceiro(event.getRowValue().getId(), editarTexto(event.getNewValue()))) {
                case 1:
                    status = "Atualizado com sucesso";
                    break;
                case 0:
                    status = "Nenhum valor atualizado";
                    break;
                case -1:
                    status = "Erro de comunicação";
                    break;
                case -2:
                    status = "Erro ao atualizar";
                    break;
                case -3:
                    status = "Valor já baixado";
                    break;
                case -4:
                    status = "Valor já em diario";
                    break;
                case -5:
                    status = "Não atualizado";
                    break;
                default:
                    status = "Erro desconhecido";
                    break;
            }
            statusLabel.setText(status);
            atualizar();
        });
    }

    private void atualizar() {
        abertas.clear();
        abertas = Cliente.INSTANCIA.listarNotasDividir(idNota);
        atualizarTabela();
        atualizarValores();
    }

    private void atualizarTabela() {
        notasAbertoTabela.getItems().clear();
        notasAbertoTabela.setItems(FXCollections.observableArrayList(abertas));
    }

    private void atualizarValores() {
        valorBaixado = 0;
        valorBaixado = abertas.stream().filter(nota -> nota.isBaixada()).map((d) -> d.getValor()).reduce(valorBaixado, (accumulator, _item) -> accumulator + _item);

        valorAberto = valorLiquido - valorBaixado;

        valorAberto = abertas.stream().filter(nota -> !nota.isBaixada()).map((d) -> d.getValor()).reduce(valorAberto, (accumulator, _item) -> accumulator - _item);

        valorBaixadoCampo.setText(Conversao.longToString(dinheiro, valorBaixado));
        valorAbertoCampo.setText(Conversao.longToString(dinheiro, valorAberto));
        valorLiquidoCampo.setText(Conversao.longToString(dinheiro, valorLiquido));
    }

    @FXML
    void dividir(ActionEvent event) {
        Cliente.INSTANCIA.incluirPagamentoNota(idNota);
        atualizar();
    }

    @FXML
    void juntar(ActionEvent event) {
        NotaFinanceiro temp = notasAbertoTabela.getSelectionModel().getSelectedItem();
        if (temp == null) {
            return;
        }
        Cliente.INSTANCIA.removerPagamentoNota(temp);
        atualizar();
    }

    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    @Override
    public void setModuloVoltar(Modulos modulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setIdNota(int nota) {
        System.out.println(nota);
        this.idNota = nota;
        valorLiquido = Cliente.INSTANCIA.buscarValorLiquidoNota(idNota);
        atualizar();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Long editarTexto(String newValue) {
        return (newValue.isEmpty() ? 0 : Long.valueOf(newValue.replaceAll("[^0-9-]", "")));
    }

    @Override
    public void succeeded() {
    }

}
