/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Produto;

import Conexao.Cliente;
import Util.CarregarGui;
import Util.HistoricoPesquisa;
import Util.Modulos;
import Util.PaneController;
import classes.almoxarife.ProdutoAlmoxarife;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Guilherme
 */
public class ProdutoController implements Initializable, PaneController {

    @FXML
    private TableColumn<ProdutoAlmoxarife, String> codigoProduto, descricaoProduto, complementoProduto, unidadeProduto, localizacaoProduto, destinoProduto, estoqueProduto;

    @FXML
    private ComboBox<String> categoria;

    @FXML
    private TextField campo;

    @FXML
    private TableView<ProdutoAlmoxarife> tabelaProduto;

    private BorderPane pai;

    private ArrayList<ProdutoAlmoxarife> produtos = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tabelaProduto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        tabelaProduto.setRowFactory(s -> {
            TableRow<ProdutoAlmoxarife> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if(evento.getClickCount() == 2 && (!linha.isEmpty())){
                    visualizar(linha.getItem());
                }
            });
            return linha;
        });

        HistoricoPesquisa historico = Cliente.INSTANCIA.buscarCampos("PRODUTOESTOQUE");
        categoria.setItems(FXCollections.observableArrayList(historico.getItens()));
        categoria.getSelectionModel().select(historico.getCampo());
        campo.setText(historico.getValor());

        codigoProduto.setCellValueFactory(new PropertyValueFactory("codigo"));
        descricaoProduto.setCellValueFactory(new PropertyValueFactory("descricao"));
        complementoProduto.setCellValueFactory(new PropertyValueFactory("complemento"));
        unidadeProduto.setCellValueFactory(new PropertyValueFactory("unidade"));
        localizacaoProduto.setCellValueFactory(new PropertyValueFactory("localizacao"));
        destinoProduto.setCellValueFactory(new PropertyValueFactory("destino"));
        estoqueProduto.setCellValueFactory((TableColumn.CellDataFeatures<ProdutoAlmoxarife, String> c) -> new SimpleStringProperty(String.valueOf(c.getValue().getEstoque())));

        produtos = Cliente.INSTANCIA.buscarUltimosProdutosAlmoxarife(50);

        atualizar();
    }

    private void atualizar() {
        tabelaProduto.setItems(FXCollections.observableArrayList(produtos));
    }

    @FXML
    private void novoCadastro() {
        visualizar(Cliente.INSTANCIA.incluirProdutoAlmoxarife());
    }

    @FXML
    private void buscar() {
        if (categoria.getValue().equals("Buscar em...")) {
            return;
        }

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void succeeded() {
    }

    private void visualizar(ProdutoAlmoxarife produtoAlmoxarife) {
        ProdutoAlterarController controller = new ProdutoAlterarController();
        controller.setPaneController(pai);
        controller.setModuloVoltar(Modulos.PRODUTO);
        controller.setProduto(produtoAlmoxarife);
        new CarregarGui().mostrar(pai, Modulos.PRODUTOCADASTRO, controller);
    }

}
