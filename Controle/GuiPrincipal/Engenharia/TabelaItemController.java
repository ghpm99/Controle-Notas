/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Engenharia;

import Conexao.Cliente;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import static Util.Conversao.quantidade;
import Util.ItemOrcamento;
import Util.Modulos;
import Util.PaneController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class TabelaItemController implements Initializable, PaneController {

    private BorderPane pai;
    @FXML
    private TableView<ItemOrcamento> itens;

    private ArrayList<ItemOrcamento> itensArray = new ArrayList<>();
    @FXML
    private TableColumn item;
    @FXML
    private Label status;
    @FXML
    private TableColumn plano;

    @FXML
    private TableColumn descricao;

    @FXML
    private TableColumn unidade;

    @FXML
    private TableColumn qnt;

    @FXML
    private TextField qntNovo;

    @FXML
    private TableColumn precoUnit;

    @FXML
    private TableColumn precoTotal;

    @FXML
    private TextField itemNovo;

    @FXML
    private TextField planoNovo;

    @FXML
    private TextField descricaoNovo;

    @FXML
    private TextField unidadeNovo;

    @FXML
    private TextField precoUnitNovo;

    @FXML
    private TextField precoTotalNovo;

    private ItemOrcamento newItem = new ItemOrcamento();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //private String item = "", planoConta = "",  descricao ="", unidade = "";
        //private long quantidadeTotal, precoUnitario, precoTotal;
        itens.setRowFactory(s -> {
            TableRow<ItemOrcamento> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if (!linha.isEmpty()) {
                    newItem = linha.getItem();
                    status.setText("alterando " + newItem.getId() + " - " + newItem.getPlanoConta());
                    atualizarCampos();

                }
            });
            return linha;

        });
        item.setCellValueFactory(new PropertyValueFactory<ItemOrcamento, String>("item"));
        plano.setCellValueFactory(new PropertyValueFactory<ItemOrcamento, String>("planoConta"));
        descricao.setCellValueFactory(new PropertyValueFactory<ItemOrcamento, String>("descricao"));
        unidade.setCellValueFactory(new PropertyValueFactory<ItemOrcamento, String>("unidade"));
        qnt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemOrcamento, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemOrcamento, String> c) {
                return new SimpleStringProperty(longToString(quantidade, c.getValue().getQuantidadeTotal()));
            }
        });
        precoUnit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemOrcamento, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemOrcamento, String> c) {
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getPrecoUnitario()));
            }
        });
        precoTotal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemOrcamento, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemOrcamento, String> c) {
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getPrecoTotal()));
            }
        });
        atualizar();
    }

    @FXML
    void novo(ActionEvent event) {
        Cliente.INSTANCIA.incluirItemOrcamento();
        status.setText("Nova entrada");
    }

    @FXML
    void salvar(ActionEvent event) {
        atualizarItem();
        int retorno = 0;
        switch (retorno) {
            case 1:
                status.setText("Atualizado com sucesso");
                break;
            case 0:
                status.setText("Falhou");
                break;
            case -1:
                status.setText("Erro");
                break;
            default:
                status.setText("Erro desconhecido");
                break;
        }
        atualizar();
    }

    private void atualizarItem() {
        newItem.setItem(itemNovo.getText());
        newItem.setPlanoConta(planoNovo.getText());
        newItem.setDescricao(descricaoNovo.getText());
        newItem.setUnidade(unidadeNovo.getText());
        newItem.setQuantidadeTotal(converter(qntNovo.getText()));
        newItem.setPrecoUnitario(converter(precoUnitNovo.getText()));
        newItem.setPrecoTotal(converter(precoTotalNovo.getText()));
    }

    private long converter(String texto) {
        try {
            return Long.valueOf(texto.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }

    }

    private void atualizar() {
        itensArray.clear();
        itensArray = Cliente.INSTANCIA.listarItensOrcamento();
        atualizarTabela();
    }

    private void atualizarTabela() {
        itens.setItems(FXCollections.observableArrayList(itensArray));
    }

    private void add(ItemOrcamento item) {
        itensArray.add(item);
    }

    private void atualizarCampos() {
        itemNovo.setText(newItem.getItem());
        planoNovo.setText(newItem.getPlanoConta());
        descricaoNovo.setText(newItem.getDescricao());
        unidadeNovo.setText(newItem.getUnidade());
        qntNovo.setText(longToString(quantidade, newItem.getQuantidadeTotal()));
        precoUnitNovo.setText(longToString(quantidade, newItem.getPrecoUnitario()));
        precoTotalNovo.setText(longToString(quantidade, newItem.getPrecoTotal()));

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
