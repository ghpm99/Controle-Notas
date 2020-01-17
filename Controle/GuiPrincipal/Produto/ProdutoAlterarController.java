/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Produto;

import Conexao.Cliente;
import Util.CarregarGui;
import Util.Modulos;
import Util.PaneController;
import classes.almoxarife.ProdutoAlmoxarife;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Guilherme
 */
public class ProdutoAlterarController implements Initializable, PaneController {

    private BorderPane pai;
    @FXML
    private TextField codigo;
    @FXML
    private TextField descricao;
    @FXML
    private TextField complemento;
    @FXML
    private TextField estoque;
    @FXML
    private TextField unidade;
    @FXML
    private TextField destino;
    @FXML
    private TextField localizacao;

    private ProdutoAlmoxarife produto;

    private Modulos modulo = Modulos.PRODUTOBUSCAR;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editar();
        atualizar();
    }

    /**
     *
     * @param controller
     */
    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    @FXML
    private void voltar() {
        voltar(modulo);
    }

    /**
     *
     * @param modulo
     */
    @Override
    public void setModuloVoltar(Modulos modulo) {
        this.modulo = modulo;
    }

    @Override
    public void succeeded() {
    }

    public void setProduto(ProdutoAlmoxarife produto) {
        this.produto = produto;
    }

    private void atualizar() {
        codigo.setText(produto.getCodigo());
        descricao.setText(produto.getDescricao());
        complemento.setText(produto.getComplemento());
        localizacao.setText(produto.getLocalizacao());
        destino.setText(produto.getDestino());
        unidade.setText(produto.getUnidade());
        estoque.setText(String.valueOf(produto.getEstoque()));
    }

    private void editar() {
        codigo.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                processarRetorno(Cliente.INSTANCIA.setString("PRODUTO", "CODIGO", produto.getId(), codigo.getText()), codigo);
            }
        });
        descricao.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                processarRetorno(Cliente.INSTANCIA.setString("PRODUTO", "DESCRICAO", produto.getId(), descricao.getText()), descricao);
            }
        });
        complemento.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                processarRetorno(Cliente.INSTANCIA.setString("PRODUTO", "COMPLEMENTO", produto.getId(), complemento.getText()), complemento);
            }
        });
        localizacao.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                processarRetorno(Cliente.INSTANCIA.setString("PRODUTO", "LOCALIZACAO", produto.getId(), localizacao.getText()), localizacao);
            }
        });
        destino.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                processarRetorno(Cliente.INSTANCIA.setString("PRODUTO", "DESTINO", produto.getId(), destino.getText()), destino);
            }
        });
        unidade.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                processarRetorno(Cliente.INSTANCIA.setString("PRODUTO", "UNIDADE", produto.getId(), unidade.getText()), unidade);

            }
        });
    }

    private boolean marcarErro(Node node) {
        desmarcarSucesso(node);
        if (!node.getStyleClass().contains("erro")) {
            node.getStyleClass().add("erro");
        }
        return true;
    }

    private boolean desmarcarErro(Node node) {
        if (node.getStyleClass().contains("erro")) {
            node.getStyleClass().remove("erro");
        }
        return false;
    }

    private boolean marcarSucesso(Node node) {
        desmarcarErro(node);
        if (!node.getStyleClass().contains("sucesso")) {
            node.getStyleClass().add("sucesso");
        }
        return true;
    }

    private boolean desmarcarSucesso(Node node) {
        if (node.getStyleClass().contains("sucesso")) {
            node.getStyleClass().remove("sucesso");
        }
        return false;
    }

    private void processarRetorno(int retorno, Node campo) {
        if (retorno == 1) {
            marcarSucesso(campo);
        } else {
            marcarErro(campo);
        }
    }

    private void voltar(Modulos modulo) {
        new CarregarGui().voltar(pai, modulo);
    }
}
